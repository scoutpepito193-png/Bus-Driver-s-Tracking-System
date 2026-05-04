package GUI;

import Model.Driver;
import Service.DriverService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DriverLoginPanel extends JFrame {
    
    private DriverService driverService;
    private JFrame parentFrame;

    public DriverLoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.driverService = new DriverService();
        
        setTitle("BDTracker - Driver Login");
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
        
        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("D", 16, 50);
        
        g2d.dispose();
        return icon;
    }
    
    private void addComponents(JPanel mainPanel) {
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel headerLabel = new JLabel("DRIVER LOGIN");
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
            // Restore the Menu window (it was hidden with setVisible(false), not disposed,
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
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formContainer.setMaximumSize(new Dimension(600, 400));
        formContainer.setPreferredSize(new Dimension(600, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;
        
        // Driver ID Label
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Driver ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(idLabel, gbc);
        
        // Driver ID Field
        gbc.gridy = 1;
        JTextField driverIdField = new JTextField(20);
        driverIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        driverIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        driverIdField.setMaximumSize(new Dimension(400, 40));
        driverIdField.setPreferredSize(new Dimension(400, 40));
        formContainer.add(driverIdField, gbc);
        
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
        
        // Forgot Password Link
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 20, 0);
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setOpaque(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setForeground(new Color(52, 152, 219));
        forgotBtn.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Please contact your sub-admin to reset password",
            "Password Reset", JOptionPane.INFORMATION_MESSAGE));
        formContainer.add(forgotBtn, gbc);
        
        // Login Button
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(400, 50));
        loginBtn.setMaximumSize(new Dimension(400, 50));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(25, 103, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(52, 152, 219));
            }
        });
        
        loginBtn.addActionListener(e -> {
            String driverId = driverIdField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // Validate: both fields must be filled before attempting login
            if (driverId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Call the service layer to verify credentials.
            // Returns a Driver object if credentials are valid AND the account has been
            // approved by an admin, or null if not found / wrong password / pending approval
            Driver driver = driverService.loginDriver(driverId, password);
            
            if (driver != null) {
                // Login successful — open the Driver Dashboard
                new DriverDashboard(driver, driverService);
                // Hide the Menu (grandparent window) — no longer needed during the session
                parentFrame.setVisible(false);
                // Close this login window
                dispose();
            } else {
                // Invalid credentials or the driver account has not been approved yet
                JOptionPane.showMessageDialog(this,
                    "Invalid Credentials or Account Not Approved",
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
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 248, 255),
                    getWidth(), getHeight(), new Color(220, 240, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new Color(52, 152, 219, 30));
            g2d.fillOval(-100, -100, 400, 400);
            g2d.fillOval(getWidth() - 200, getHeight() - 300, 500, 500);
        }
    }
}