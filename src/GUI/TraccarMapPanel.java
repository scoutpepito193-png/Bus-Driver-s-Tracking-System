package GUI;

import util.TraccarAPI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

public class TraccarMapPanel extends JPanel {

    private static final int TILE_SIZE = 256;

    private final Map<String, BufferedImage> tileCache = new ConcurrentHashMap<>();

    private Timer refreshTimer;
    private int currentDeviceId = -1;

    private double latitude = 10.3157;
    private double longitude = 123.8854;
    private double speed = 0;

    private double centerLat = 10.3157;
    private double centerLon = 123.8854;

    private int zoom = 15;
    private boolean hasLocation = false;
    private boolean followDriver = true;

    private int dragStartX;
    private int dragStartY;
    private double dragStartCenterPixelX;
    private double dragStartCenterPixelY;

    public TraccarMapPanel() {
        setBackground(new Color(232, 238, 241));
        setDoubleBuffered(true);

        addMouseWheelListener(e -> {
            int oldZoom = zoom;

            if (e.getWheelRotation() < 0) {
                zoom = Math.min(19, zoom + 1);
            } else {
                zoom = Math.max(3, zoom - 1);
            }

            if (zoom != oldZoom) {
                followDriver = false;
                repaint();
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                followDriver = false;
                dragStartX = e.getX();
                dragStartY = e.getY();
                dragStartCenterPixelX = lonToPixelX(centerLon, zoom);
                dragStartCenterPixelY = latToPixelY(centerLat, zoom);
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                double dx = e.getX() - dragStartX;
                double dy = e.getY() - dragStartY;

                double newCenterPixelX = dragStartCenterPixelX - dx;
                double newCenterPixelY = dragStartCenterPixelY - dy;

                centerLon = pixelXToLon(newCenterPixelX, zoom);
                centerLat = pixelYToLat(newCenterPixelY, zoom);

                repaint();
            }
        });
    }

    public void loadDevice(int deviceId) {
        currentDeviceId = deviceId;

        stopTimer();
        refreshLocation();

        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshLocation();
            }
        }, 3000, 3000);
    }

    public void clearHistory() {
        followDriver = true;
        centerLat = latitude;
        centerLon = longitude;
        refreshLocation();
        repaint();
    }

    public void stopTracking() {
        stopTimer();
    }

    private void stopTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null;
        }
    }

    private void refreshLocation() {
        if (currentDeviceId <= 0) {
            return;
        }

        new Thread(() -> {
            try {
                JSONObject position = TraccarAPI.getLatestPosition(currentDeviceId);

                if (position == null) {
                    return;
                }

                double lat = position.optDouble("latitude", 0);
                double lon = position.optDouble("longitude", 0);
                double spd = position.optDouble("speed", 0) * 1.852;

                if (lat == 0 || lon == 0) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    latitude = lat;
                    longitude = lon;
                    speed = spd;
                    hasLocation = true;

                    if (followDriver) {
                        centerLat = lat;
                        centerLon = lon;
                    }

                    repaint();
                });
            } catch (Exception e) {
                System.err.println("Traccar map error: " + e.getMessage());
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawMap(g2);

        if (hasLocation) {
            drawMarker(g2);
            drawInfoBubble(g2);
        }

        g2.dispose();
    }

    private void drawMap(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();

        double centerPixelX = lonToPixelX(centerLon, zoom);
        double centerPixelY = latToPixelY(centerLat, zoom);

        double topLeftPixelX = centerPixelX - width / 2.0;
        double topLeftPixelY = centerPixelY - height / 2.0;

        int startTileX = (int) Math.floor(topLeftPixelX / TILE_SIZE);
        int startTileY = (int) Math.floor(topLeftPixelY / TILE_SIZE);
        int endTileX = (int) Math.floor((topLeftPixelX + width) / TILE_SIZE);
        int endTileY = (int) Math.floor((topLeftPixelY + height) / TILE_SIZE);

        int maxTile = 1 << zoom;

        for (int x = startTileX; x <= endTileX; x++) {
            for (int y = startTileY; y <= endTileY; y++) {
                if (y < 0 || y >= maxTile) {
                    continue;
                }

                int wrappedX = ((x % maxTile) + maxTile) % maxTile;

                int drawX = (int) Math.round(x * TILE_SIZE - topLeftPixelX);
                int drawY = (int) Math.round(y * TILE_SIZE - topLeftPixelY);

                BufferedImage tile = getTile(wrappedX, y, zoom);

                if (tile != null) {
                    g2.drawImage(tile, drawX, drawY, TILE_SIZE, TILE_SIZE, null);
                } else {
                    g2.setColor(new Color(232, 238, 241));
                    g2.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private BufferedImage getTile(int x, int y, int z) {
        String key = z + "/" + x + "/" + y;

        BufferedImage cached = tileCache.get(key);
        if (cached != null) {
            return cached;
        }

        new Thread(() -> {
            try {
                String url = "https://cartodb-basemaps-a.global.ssl.fastly.net/rastertiles/voyager/"
                        + z + "/" + x + "/" + y + ".png";

                BufferedImage img = ImageIO.read(new URL(url));

                if (img != null) {
                    tileCache.put(key, img);
                    SwingUtilities.invokeLater(this::repaint);
                }
            } catch (Exception ignored) {
            }
        }).start();

        return null;
    }

    private void drawMarker(Graphics2D g2) {
        double centerPixelX = lonToPixelX(centerLon, zoom);
        double centerPixelY = latToPixelY(centerLat, zoom);

        double markerPixelX = lonToPixelX(longitude, zoom);
        double markerPixelY = latToPixelY(latitude, zoom);

        int x = (int) Math.round(getWidth() / 2.0 + (markerPixelX - centerPixelX));
        int y = (int) Math.round(getHeight() / 2.0 + (markerPixelY - centerPixelY));

        g2.setColor(new Color(46, 125, 50));
        g2.fillOval(x - 11, y - 28, 22, 22);

        Path2D pin = new Path2D.Double();
        pin.moveTo(x - 8, y - 12);
        pin.lineTo(x + 8, y - 12);
        pin.lineTo(x, y + 8);
        pin.closePath();
        g2.fill(pin);

        g2.setColor(Color.WHITE);
        g2.fillOval(x - 4, y - 21, 8, 8);
    }

    private void drawInfoBubble(Graphics2D g2) {
        double centerPixelX = lonToPixelX(centerLon, zoom);
        double centerPixelY = latToPixelY(centerLat, zoom);

        double markerPixelX = lonToPixelX(longitude, zoom);
        double markerPixelY = latToPixelY(latitude, zoom);

        int markerX = (int) Math.round(getWidth() / 2.0 + (markerPixelX - centerPixelX));
        int markerY = (int) Math.round(getHeight() / 2.0 + (markerPixelY - centerPixelY));

        String title = "Device " + currentDeviceId;
        String line1 = "Latitude: " + String.format("%.6f", latitude);
        String line2 = "Longitude: " + String.format("%.6f", longitude);
        String line3 = "Speed: " + String.format("%.2f", speed) + " km/h";

        int bubbleW = 230;
        int bubbleH = 105;
        int bubbleX = markerX + 18;
        int bubbleY = markerY - 125;

        if (bubbleX + bubbleW > getWidth()) {
            bubbleX = markerX - bubbleW - 18;
        }

        if (bubbleY < 0) {
            bubbleY = markerY + 18;
        }

        g2.setColor(new Color(0, 0, 0, 35));
        g2.fillRoundRect(bubbleX + 4, bubbleY + 4, bubbleW, bubbleH, 14, 14);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(bubbleX, bubbleY, bubbleW, bubbleH, 14, 14);

        g2.setColor(new Color(220, 220, 220));
        g2.drawRoundRect(bubbleX, bubbleY, bubbleW, bubbleH, 14, 14);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(35, 35, 35));
        g2.drawString(title, bubbleX + 15, bubbleY + 25);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.drawString(line1, bubbleX + 15, bubbleY + 48);
        g2.drawString(line2, bubbleX + 15, bubbleY + 68);
        g2.drawString(line3, bubbleX + 15, bubbleY + 88);
    }

    private double lonToPixelX(double lon, int zoom) {
        double scale = TILE_SIZE * Math.pow(2, zoom);
        return (lon + 180.0) / 360.0 * scale;
    }

    private double latToPixelY(double lat, int zoom) {
        double sinLat = Math.sin(Math.toRadians(lat));
        double scale = TILE_SIZE * Math.pow(2, zoom);
        return (0.5 - Math.log((1 + sinLat) / (1 - sinLat)) / (4 * Math.PI)) * scale;
    }

    private double pixelXToLon(double pixelX, int zoom) {
        double scale = TILE_SIZE * Math.pow(2, zoom);
        return pixelX / scale * 360.0 - 180.0;
    }

    private double pixelYToLat(double pixelY, int zoom) {
        double scale = TILE_SIZE * Math.pow(2, zoom);
        double n = Math.PI - 2.0 * Math.PI * pixelY / scale;
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }
}
