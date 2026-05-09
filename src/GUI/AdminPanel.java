package GUI;

import Service.SuperAdminService;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * AdminPanel - Main admin selection interface
 * Provides navigation between SuperAdmin and SubAdmin portals
 * Features professional gradient design and responsive layout
 */
public class AdminPanel extends JFrame {

    private SuperAdminService sas = new SuperAdminService();

    public AdminPanel() {
        setTitle("BDTracker - Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        setIconImage(createAppIcon());

        // Create main panel with gradient background
        JPanel mainPanel = new GradientBackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        addComponents(mainPanel);
        setVisible(true);
    }

    /**
     * Creates the application icon - bus symbol with gradient
     */
    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(155, 89, 182));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("A", 16, 50);

        g2d.dispose();
        return icon;
    }

    /**
     * Adds all components to the main panel
     */
    private void addComponents(JPanel mainPanel) {
        // Header with logo
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = createCenterContentPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooterPanel();
        footerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        footerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the header panel with logo and title
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(108, 52, 131)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Left side - Logo and Title
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("[ BUS ] BDTracker");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("ADMIN PANEL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(titleLabel);

        // Right side - Back button
        JButton backBtn = new JButton("BACK TO MAIN");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(140, 50));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(231, 76, 60));
            }
        });
        backBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the center content panel with admin role cards
     */
    private JPanel createCenterContentPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Container for cards
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 50));
        containerPanel.setOpaque(false);
        containerPanel.setMaximumSize(new Dimension(1000, 500));

        // Super Admin Card
        JPanel superAdminCard = createAdminRoleCard(
            "SUPER ADMIN",
            "Full System Control",
            new String[]{
                "Manage all sub admins",
                "Approve/reject driver requests",
                "View comprehensive reports",
                "Monitor all drivers in real-time",
                "Configure system settings"
            },
            new Color(155, 89, 182),
            new Color(108, 52, 131),
            e -> handleSuperAdminClick()
        );
        containerPanel.add(superAdminCard);

        // Sub Admin Card
        JPanel subAdminCard = createAdminRoleCard(
            "SUB ADMIN",
            "Fleet Management",
            new String[]{
                "Manage assigned drivers",
                "Track GPS locations",
                "Handle driver requests",
                "Generate driver reports",
                "Monitor performance"
            },
            new Color(46, 204, 113),
            new Color(25, 138, 68),
            e -> handleSubAdminClick()
        );
        containerPanel.add(subAdminCard);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        centerPanel.add(containerPanel, gbc);

        return centerPanel;
    }

    /**
     * Creates a professional admin role card with features list
     */
    private JPanel createAdminRoleCard(String title, String subtitle, String[] features,
                                        Color primaryColor, Color secondaryColor,
                                        java.awt.event.ActionListener actionListener) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 420));
        card.setMaximumSize(new Dimension(360, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitleLabel);

        // Features section
        card.add(Box.createVerticalStrut(20));
        JLabel featuresHeaderLabel = new JLabel("Key Features:");
        featuresHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        featuresHeaderLabel.setForeground(new Color(80, 80, 80));
        featuresHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(featuresHeaderLabel);

        card.add(Box.createVerticalStrut(8));

        // Features list
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String feature : features) {
            JLabel featureLabel = new JLabel("  " + feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            featureLabel.setForeground(new Color(60, 60, 60));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            featureLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            featuresPanel.add(featureLabel);
        }

        card.add(featuresPanel);
        card.add(Box.createVerticalGlue());

        // Action button
        JButton selectBtn = new JButton("SELECT");
        selectBtn.setMaximumSize(new Dimension(300, 50));
        selectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectBtn.setBackground(primaryColor);
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectBtn.setFocusPainted(false);
        selectBtn.setBorderPainted(false);
        selectBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        selectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                selectBtn.setBackground(secondaryColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                selectBtn.setBackground(primaryColor);
            }
        });
        selectBtn.addActionListener(actionListener);

        card.add(selectBtn);

        return card;
    }

    /**
     * Handles Super Admin selection - checks if account exists
     */
    private void handleSuperAdminClick() {
        try {
            // Check if SuperAdmin account already exists in database
            boolean exists = sas.checkAccout();

            if (exists) {
                // Account exists - show login panel
                this.setVisible(false);
                new SuperAdminLogin(this);
            } else {
                // No account exists - show signup panel
                this.setVisible(false);
                new SuperAdminSignupPanel(this);
            }
        } catch (Exception ex) {
            // Show error dialog
            JOptionPane.showMessageDialog(this,
                "Error checking account: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Handles Sub Admin selection - goes to SubAdminLogin
     */
    private void handleSubAdminClick() {
        this.setVisible(false);
        new SubAdminLogin(this);
    }

    /**
     * Creates the footer panel
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));

        JLabel footerLabel = new JLabel("(c) 2026 BDTracker. All rights reserved. | Secure Bus Fleet Management");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 170, 190));

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        return footerPanel;
    }

    /**
     * Gradient background panel with professional styling
     */
    class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient background
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 245, 250),
                    getWidth(), getHeight(), new Color(220, 235, 250));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Decorative shapes
            g2d.setColor(new Color(155, 89, 182, 20));
            g2d.fillOval(-150, -100, 500, 500);
            g2d.fillOval(getWidth() - 200, getHeight() - 400, 600, 600);
        }
    }
}