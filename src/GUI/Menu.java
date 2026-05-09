package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Menu extends JFrame {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu());
    }
    
    public Menu() {
        setTitle("BDTracker");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);
        add(mainPanel);
        
        addComponents(mainPanel);
        
        setVisible(true);
    }
    
    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(25, 103, 210));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(10, 15, 44, 28, 4, 4);
        g2d.fillRect(14, 18, 8, 6);
        g2d.fillRect(25, 18, 8, 6);
        g2d.fillRect(36, 18, 8, 6);
        g2d.fillOval(16, 42, 6, 6);
        g2d.fillOval(42, 42, 6, 6);
        
        g2d.dispose();
        return icon;
    }
    
    private void addComponents(JPanel mainPanel) {
        
        // Top Navigation Bar
        JPanel navBar = createNavBar();
        navBar.setBounds(0, 0, 1400, 70);
        mainPanel.add(navBar);
        
        // Logo Section
        JPanel logoSection = new JPanel();
        logoSection.setOpaque(false);
        logoSection.setLayout(new BoxLayout(logoSection, BoxLayout.Y_AXIS));
        logoSection.setBounds(0, 100, 1400, 250);
        
        // Custom Logo
        JPanel logoPanel = createLogoPanel();
        logoPanel.setMaximumSize(new Dimension(1400, 150));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(logoPanel);
        
        // Title
        JLabel titleLabel = new JLabel("BDTracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(Box.createVerticalStrut(15));
        logoSection.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Professional Bus Driver Tracking & Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(200, 220, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoSection.add(Box.createVerticalStrut(10));
        logoSection.add(subtitleLabel);
        
        mainPanel.add(logoSection);
        
        // Buttons Section
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 30));
        buttonsPanel.setBounds(200, 420, 1000, 200);
        
        // Login Button
        RoundedGradientButton loginBtn = new RoundedGradientButton("LOGIN",
            new Color(52, 152, 219), new Color(25, 103, 210));
        loginBtn.setPreferredSize(new Dimension(350, 120));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 26));
        loginBtn.addActionListener(e -> goToLogin());
        buttonsPanel.add(loginBtn);
        
        mainPanel.add(buttonsPanel);
        
        // Footer
        JLabel footerLabel = new JLabel("(c) 2026 BDTracker. All rights reserved. | Secure Bus Fleet Management");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 170, 190));
        footerLabel.setHorizontalAlignment(JLabel.CENTER);
        footerLabel.setBounds(0, 850, 1400, 30);
        mainPanel.add(footerLabel);
    }
    
    private JPanel createNavBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BorderLayout());
        navBar.setBackground(new Color(20, 40, 80));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel logo = new JLabel("[ BUS ] BDTracker");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(new Color(52, 152, 219));
        
        JLabel infoLabel = new JLabel("Real-time Bus Fleet Management System");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(150, 170, 190));
        
        navBar.add(logo, BorderLayout.WEST);
        navBar.add(infoLabel, BorderLayout.EAST);
        
        return navBar;
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Outer circle
                g2d.setColor(new Color(52, 152, 219, 100));
                g2d.fillOval(centerX - 80, centerY - 80, 160, 160);
                
                // Bus body
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(centerX - 60, centerY - 40, 120, 60, 15, 15);
                
                // Windows
                g2d.fillRect(centerX - 50, centerY - 35, 15, 12);
                g2d.fillRect(centerX - 25, centerY - 35, 15, 12);
                g2d.fillRect(centerX, centerY - 35, 15, 12);
                g2d.fillRect(centerX + 25, centerY - 35, 15, 12);
                
                // Door
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(centerX + 40, centerY - 25, 15, 35);
                
                // Wheels
                g2d.fillOval(centerX - 45, centerY + 18, 16, 16);
                g2d.fillOval(centerX + 29, centerY + 18, 16, 16);
                
                // GPS signal
                g2d.setColor(new Color(46, 204, 113));
                g2d.drawOval(centerX + 50, centerY - 50, 8, 8);
                g2d.fillOval(centerX + 52, centerY - 48, 4, 4);
                g2d.drawOval(centerX + 45, centerY - 55, 18, 18);
                g2d.drawOval(centerX + 40, centerY - 60, 28, 28);
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    private void goToLogin() {
        new UnifiedLoginPanel();
        this.dispose();
    }
    
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 60, 120),
                    getWidth(), getHeight(), new Color(40, 120, 200));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new Color(255, 255, 255, 15));
            g2d.fillOval(-200, -150, 600, 600);
            g2d.fillOval(getWidth() - 100, getHeight() - 400, 700, 700);
            
            g2d.setColor(new Color(52, 152, 219, 50));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(0, 70, getWidth(), 70);
        }
    }
    
    class RoundedGradientButton extends JButton {
        private int arc = 25;
        private Color startColor;
        private Color endColor;
        
        public RoundedGradientButton(String text, Color startColor, Color endColor) {
            super(text);
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color topColor = startColor;
            Color bottomColor = endColor;
            
            if (getModel().isPressed()) {
                topColor = new Color(
                    Math.max(startColor.getRed() - 40, 0),
                    Math.max(startColor.getGreen() - 40, 0),
                    Math.max(startColor.getBlue() - 40, 0)
                );
                bottomColor = new Color(
                    Math.max(endColor.getRed() - 40, 0),
                    Math.max(endColor.getGreen() - 40, 0),
                    Math.max(endColor.getBlue() - 40, 0)
                );
            } else if (getModel().isRollover()) {
                topColor = new Color(
                    Math.min(startColor.getRed() + 30, 255),
                    Math.min(startColor.getGreen() + 30, 255),
                    Math.min(startColor.getBlue() + 30, 255)
                );
                bottomColor = new Color(
                    Math.min(endColor.getRed() + 30, 255),
                    Math.min(endColor.getGreen() + 30, 255),
                    Math.min(endColor.getBlue() + 30, 255)
                );
            }
            
            GradientPaint gradient = new GradientPaint(0, 0, topColor,
                    0, getHeight(), bottomColor);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            
            g2.setColor(new Color(0, 0, 0, 30));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() / 2 - 1, arc, arc);
            
            super.paintComponent(g);
        }
    }
}