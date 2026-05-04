package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AdminRoleSelection extends JFrame {
    
    private JFrame parentFrame;
    
    public AdminRoleSelection(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        setTitle("BDTracker - Select Admin Role");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
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
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(20, 40, 80));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel headerLabel = new JLabel("Select Your Admin Role");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        mainPanel.add(headerPanel);
        
        // Spacer
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 30));
        contentPanel.setOpaque(false);
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Super Admin Card (IMPROVED)
        JPanel superAdminCard = createRoleCard(
            "SUPER ADMIN",
            "Full System Control & Management",
            new String[]{
                "-Manage all sub admins and drivers",
                "-Approve/reject driver requests",
                "-View comprehensive system reports",
                "-Monitor all drivers in real-time",
                "-Configure system settings",
                "-Track GPS locations"
            },
            new Color(155, 89, 182),
            new Color(108, 52, 131),
            e -> goToSuperAdminLogin()
        );
        contentPanel.add(superAdminCard);
        
        // Sub Admin Card (IMPROVED)
        JPanel subAdminCard = createRoleCard(
            "SUB ADMIN",
            "Fleet & Driver Management",
            new String[]{
                "-Manage assigned drivers",
                "-Track GPS locations in real-time",
                "-Handle driver requests & issues",
                "-Generate detailed driver reports",
                "-Request driver approval",
                "-Monitor driver performance"
            },
            new Color(46, 204, 113),
            new Color(25, 138, 68),
            e -> goToSubAdminLogin()
        );
        contentPanel.add(subAdminCard);
        
        mainPanel.add(contentPanel);
        
        // Spacer
        mainPanel.add(Box.createVerticalGlue());
        
        // Back Button Panel
        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        backPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 10));
        
        JButton backBtn = new JButton("← BACK TO MAIN");
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setForeground(new Color(40, 40, 40));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(150, 45));
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
        backPanel.add(backBtn);
        mainPanel.add(backPanel);
    }
    
    private void goToSuperAdminLogin() {
        this.setVisible(false);
        new SuperAdminLogin(this);
    }
    
    private void goToSubAdminLogin() {
        this.setVisible(false);
        new SubAdminLogin(this);
    }
    
    private JPanel createRoleCard(String title, String subtitle, String[] features,
                                   Color primaryColor, Color secondaryColor,
                                   java.awt.event.ActionListener actionListener) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(320, 360));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 3),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Features Panel
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            featureLabel.setForeground(new Color(60, 60, 60));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            featureLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            featuresPanel.add(featureLabel);
        }
        
        // Select Button
        JButton selectBtn = new JButton("SELECT");
        selectBtn.setMaximumSize(new Dimension(280, 50));
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
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(featuresPanel);
        card.add(Box.createVerticalGlue());
        card.add(selectBtn);
        
        return card;
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 245, 250),
                    getWidth(), getHeight(), new Color(220, 235, 250));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}