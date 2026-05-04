package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * AdminRoleSelection Frame - Allows users to select their admin role
 * Provides selection between Super Admin and Sub Admin with clear descriptions
 */
public class AdminRoleSelection extends JFrame {
    
    private JFrame parentFrame;
    
    public AdminRoleSelection(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        setTitle("Trackify - Select Admin Role");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
        setMinimumSize(new Dimension(900, 600));
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        addComponents(mainPanel);
        
        setVisible(true);
    }
    
    /**
     * Creates the application icon - same logo as Menu
     */
    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Blue background - matches Menu theme
        g2d.setColor(new Color(25, 103, 210));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        // Bus icon - white
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
    
    /**
     * Adds all GUI components to the main panel
     */
    private void addComponents(JPanel mainPanel) {
        
        // Header Panel - Professional branding
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(20, 40, 80));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 152, 219)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
        
        // Logo - Same as Menu
        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(new Color(52, 152, 219));
        
        // Title
        JLabel titleLabel = new JLabel("Select Your Admin Role");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createVerticalStrut(5));
        leftHeader.add(titleLabel);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Content Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Cards Container
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(1, 2, 50, 0));
        cardsContainer.setOpaque(false);
        cardsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        // Super Admin Card
        JPanel superAdminCard = createRoleCard(
            "SUPER ADMIN",
            "Full System Control & Management",
            new String[]{
                "Manage all sub admins and drivers",
                "Approve/reject driver registration requests",
                "View comprehensive system reports",
                "Monitor all drivers in real-time",
                "Configure system settings",
                "Track GPS locations"
            },
            new Color(155, 89, 182),
            new Color(108, 52, 131),
            e -> goToSuperAdminLogin()
        );
        cardsContainer.add(superAdminCard);
        
        // Sub Admin Card
        JPanel subAdminCard = createRoleCard(
            "SUB ADMIN",
            "Fleet & Driver Management",
            new String[]{
                "Manage assigned drivers",
                "Track GPS locations in real-time",
                "Handle driver requests and issues",
                "Generate detailed driver reports",
                "Request driver approval",
                "Monitor driver performance"
            },
            new Color(46, 204, 113),
            new Color(25, 138, 68),
            e -> goToSubAdminLogin()
        );
        cardsContainer.add(subAdminCard);
        
        centerPanel.add(cardsContainer);
        centerPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Panel - Back Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 20));
        bottomPanel.setPreferredSize(new Dimension(0, 70));
        
        JButton backBtn = new JButton("BACK TO MAIN");
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setForeground(new Color(40, 40, 40));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(180, 50));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(200, 200, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(220, 220, 220));
            }
        });
        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });
        bottomPanel.add(backBtn);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Navigate to Super Admin Login
     */
    private void goToSuperAdminLogin() {
        this.setVisible(false);
        new SuperAdminLogin(this);
    }
    
    /**
     * Navigate to Sub Admin Login
     */
    private void goToSubAdminLogin() {
        this.setVisible(false);
        new SubAdminLogin(this);
    }
    
    /**
     * Creates a role selection card with title, description, and features
     * @param title Card title
     * @param subtitle Card subtitle/description
     * @param features Array of feature strings
     * @param primaryColor Primary color for the card
     * @param secondaryColor Secondary color for hover effect
     * @param actionListener Action listener for the select button
     */
    private JPanel createRoleCard(String title, String subtitle, String[] features,
                                   Color primaryColor, Color secondaryColor,
                                   java.awt.event.ActionListener actionListener) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Title Label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        // Subtitle Label
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitleLabel);
        
        // Separator
        card.add(Box.createVerticalStrut(15));
        
        // Features Panel
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (String feature : features) {
            JPanel featureRow = new JPanel();
            featureRow.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
            featureRow.setOpaque(false);
            
            JLabel bulletLabel = new JLabel("■");
            bulletLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            bulletLabel.setForeground(primaryColor);
            
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            featureLabel.setForeground(new Color(60, 60, 60));
            
            featureRow.add(bulletLabel);
            featureRow.add(featureLabel);
            featuresPanel.add(featureRow);
        }
        
        card.add(featuresPanel);
        card.add(Box.createVerticalGlue());
        
        // Select Button
        JButton selectBtn = new JButton("SELECT ROLE");
        selectBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        selectBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectBtn.setBackground(primaryColor);
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectBtn.setFocusPainted(false);
        selectBtn.setBorderPainted(false);
        selectBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
     * Custom Background Panel with gradient effect
     */
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Subtle gradient background
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 245, 250),
                    getWidth(), getHeight(), new Color(220, 235, 250));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}