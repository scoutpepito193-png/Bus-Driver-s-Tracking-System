package GUI;

import util.TraccarAPI;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

public class TraccarMapPanel extends JPanel {

    private final JFXPanel fxPanel;
    private WebEngine engine;
    private Timer refreshTimer;

    private int currentDeviceId = -1;
    private boolean mapReady = false;

    private double lastLat;
    private double lastLon;
    private double lastSpeed;
    private boolean hasPendingLocation = false;

    public TraccarMapPanel() {
        setLayout(new BorderLayout());

        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.setContextMenuEnabled(false);

            engine = webView.getEngine();
            engine.setUserAgent("Mozilla/5.0 TrackifyBusTracking/1.0");

            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    mapReady = true;
                    forceMapResize();

                    if (hasPendingLocation) {
                        updateMarker(lastLat, lastLon, lastSpeed, currentDeviceId);
                    }
                }
            });

            fxPanel.setScene(new Scene(webView));
            loadMapShell();
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                forceMapResize();
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
        refreshLocation();
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
                double speed = position.optDouble("speed", 0) * 1.852;

                lastLat = lat;
                lastLon = lon;
                lastSpeed = speed;
                hasPendingLocation = true;

                Platform.runLater(() -> updateMarker(lat, lon, speed, currentDeviceId));

            } catch (Exception e) {
                System.err.println("Traccar map error: " + e.getMessage());
            }
        }).start();
    }

    private void updateMarker(double lat, double lon, double speed, int deviceId) {
        if (engine == null || !mapReady) {
            return;
        }

        String script =
                "updateDriver(" +
                        lat + "," +
                        lon + "," +
                        "'" + String.format("%.2f", speed) + "'," +
                        "'" + deviceId + "'" +
                        ");";

        engine.executeScript(script);
        forceMapResize();
    }

    private void forceMapResize() {
        if (engine == null || !mapReady) {
            return;
        }

        Platform.runLater(() -> {
            try {
                engine.executeScript(
                        "setTimeout(function(){ if (window.map) { map.invalidateSize(true); } }, 100);" +
                        "setTimeout(function(){ if (window.map) { map.invalidateSize(true); } }, 500);" +
                        "setTimeout(function(){ if (window.map) { map.invalidateSize(true); } }, 1000);"
                );
            } catch (Exception ignored) {
            }
        });
    }

    private void loadMapShell() {
        String html =
                "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "html, body {" +
                " width:100%; height:100%; margin:0; padding:0; overflow:hidden; background:#e8eef1;" +
                "}" +
                "#map {" +
                " position:fixed; top:0; left:0; right:0; bottom:0;" +
                " width:100vw; height:100vh; min-width:100vw; min-height:100vh;" +
                " background:#e8eef1;" +
                "}" +
                ".leaflet-container { width:100vw !important; height:100vh !important; background:#e8eef1; }" +
                ".leaflet-control-attribution { font-size:10px; }" +
                ".leaflet-popup-content { font-family:Segoe UI,Arial; font-size:13px; }" +
                "</style>" +
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'>" +
                "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                "</head>" +
                "<body>" +
                "<div id='map'></div>" +
                "<script>" +
                "window.map = L.map('map', {" +
                " zoomControl:false," +
                " attributionControl:true" +
                "}).setView([10.3157,123.8854], 13);" +

                "L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {" +
                " maxZoom:19," +
                " attribution:'Tiles &copy; Esri'" +
                "}).addTo(map);" +

                "var marker = null;" +

                "function updateDriver(lat, lon, speed, deviceId) {" +
                "  if (!marker) {" +
                "    marker = L.marker([lat, lon]).addTo(map);" +
                "  } else {" +
                "    marker.setLatLng([lat, lon]);" +
                "  }" +
                "  map.setView([lat, lon], 16, { animate:false });" +
                "  marker.bindPopup('<b>Device ' + deviceId + '</b><br>Latitude: ' + lat.toFixed(6) + '<br>Longitude: ' + lon.toFixed(6) + '<br>Speed: ' + speed + ' km/h').openPopup();" +
                "  setTimeout(function(){ map.invalidateSize(true); }, 100);" +
                "  setTimeout(function(){ map.invalidateSize(true); }, 500);" +
                "}" +

                "setTimeout(function(){ map.invalidateSize(true); }, 300);" +
                "setTimeout(function(){ map.invalidateSize(true); }, 1000);" +
                "</script>" +
                "</body>" +
                "</html>";

        engine.loadContent(html);
    }
}
