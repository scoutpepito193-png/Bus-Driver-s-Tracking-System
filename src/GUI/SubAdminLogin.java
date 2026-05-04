package GUI;

import Model.SubAdmin;
import Service.SubAdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SubAdminLogin extends JFrame {
    
    private SubAdminService subAdminService;
    private JFrame parentFrame;

    public SubAdminLogin(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.subAdminService = new SubAdminService();
        
        setTitle("BDTracker - Sub Admin Login");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(700, 500));
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        add(mainPanel);
        
        addComponents(mainPanel);
        
        setVisible(true);
    }
    
    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("A", 16, 50);
        
        g2d.dispose();
        return icon;
    }
    
    private void addComponents(JPanel mainPanel) {
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 138, 68)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel headerLabel = new JLabel("SUB ADMIN LOGIN");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        
        JButton backBtn = new JButton("BACK");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            // Close this login window
            dispose();
            // Restore AdminRoleSelection (it was hidden with setVisible(false), not disposed,
            // so it is still alive in memory and can be made visible again)
            parentFrame.setVisible(true);
        });
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel - for vertical centering
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formContainer.setMaximumSize(new Dimension(600, 350));
        formContainer.setPreferredSize(new Dimension(600, 350));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;
        
        // Admin ID Label
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Admin ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(idLabel, gbc);
        
        // Admin ID Field
        gbc.gridy = 1;
        JTextField adminIdField = new JTextField(20);
        adminIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        adminIdField.setMaximumSize(new Dimension(400, 40));
        adminIdField.setPreferredSize(new Dimension(400, 40));
        formContainer.add(adminIdField, gbc);
        
        // Password Label
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(passLabel, gbc);
        
        // Password Field
        gbc.gridy = 3;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setMaximumSize(new Dimension(400, 40));
        passwordField.setPreferredSize(new Dimension(400, 40));
        formContainer.add(passwordField, gbc);
        
        // Login Button
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(46, 204, 113));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(400, 50));
        loginBtn.setMaximumSize(new Dimension(400, 50));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(25, 138, 68));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(46, 204, 113));
            }
        });
        
        loginBtn.addActionListener(e -> {
            String adminId = adminIdField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate: both fields must be filled before attempting login
            if (adminId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Call the service layer to verify credentials.
            // Returns a SubAdmin object if credentials are valid, or null if not found / wrong password
            SubAdmin subAdmin = subAdminService.login(adminId, password);

            if (subAdmin != null) {
                // Login successful — open the Sub Admin Dashboard
                new SubAdminDashboard(subAdmin, subAdminService);

                // Hide AdminRoleSelection (the parent of this window) — no longer needed
                parentFrame.setVisible(false);

                // Close this login window
                dispose();
            } else {
                // No account found or password mismatch
                JOptionPane.showMessageDialog(this, "Invalid Credentials",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText(""); // Clear the password field so user can retry
            }
        });
        
        formContainer.add(loginBtn, gbc);
        
        // Add form to center panel
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerPanel.add(formContainer, centerGbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 250, 245),
                    getWidth(), getHeight(), new Color(220, 245, 235));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new Color(46, 204, 113, 30));
            g2d.fillOval(-100, -100, 400, 400);
            g2d.fillOval(getWidth() - 200, getHeight() - 300, 500, 500);
        }
    }
}