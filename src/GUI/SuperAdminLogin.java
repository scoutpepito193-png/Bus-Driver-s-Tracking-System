package GUI;

import Model.SuperAdmin;
import Service.SuperAdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * SuperAdminLogin Frame - Professional login interface with custom forgot password dialog
 */
public class SuperAdminLogin extends JFrame {
    
    private SuperAdminService superAdminService;
    private JFrame parentFrame;

    public SuperAdminLogin(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.superAdminService = new SuperAdminService();
        
        setTitle("Trackify - Super Admin Login");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        add(mainPanel);
        
        addComponents(mainPanel);
        
        setVisible(true);
    }
    
    /**
     * Creates the application icon - matches Menu branding
     */
    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(155, 89, 182));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("S", 16, 50);
        
        g2d.dispose();
        return icon;
    }
    
    /**
     * Adds all login components to the frame
     */
    private void addComponents(JPanel mainPanel) {
        
        // Header Panel with Logo - Same branding as Menu
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(108, 52, 131)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 90));
        
        // Logo - Same as Menu
        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        
        // Title
        JLabel headerLabel = new JLabel("SUPER ADMIN LOGIN");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createVerticalStrut(5));
        leftHeader.add(headerLabel);
        
        // Back Button
        JButton backBtn = new JButton("BACK");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(231, 76, 60));
            }
        });
        backBtn.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel - Centered form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            BorderFactory.createEmptyBorder(40, 45, 40, 45)
        ));
        formContainer.setPreferredSize(new Dimension(500, 380));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;
        
        // Admin ID Label
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Admin ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(idLabel, gbc);
        
        // Admin ID Field
        gbc.gridy = 1;
        JTextField adminIdField = new JTextField(20);
        adminIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        adminIdField.setPreferredSize(new Dimension(400, 42));
        formContainer.add(adminIdField, gbc);
        
        // Password Label
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(passLabel, gbc);
        
        // Password Field
        gbc.gridy = 3;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        passwordField.setPreferredSize(new Dimension(400, 42));
        formContainer.add(passwordField, gbc);
        
        // Forgot Password Link
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 0, 20, 0);
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setOpaque(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setForeground(new Color(155, 89, 182));
        forgotBtn.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.setHorizontalAlignment(JButton.LEFT);
        forgotBtn.addActionListener(e -> showForgotPasswordDialog());
        formContainer.add(forgotBtn, gbc);
        
        // Login Button
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(155, 89, 182));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setPreferredSize(new Dimension(400, 50));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(108, 52, 131));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(155, 89, 182));
            }
        });
        
        loginBtn.addActionListener(e -> {
            String adminId = adminIdField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input fields
            if (adminId.isEmpty() || password.isEmpty()) {
                showErrorDialog("Validation Error", "Please enter both Admin ID and Password");
                return;
            }

            // Authenticate admin
            int result = superAdminService.logIn(adminId, password);

            if (result == 1) {
                // Login successful - fetch admin data
                SuperAdmin superAdmin = superAdminService.getSAData();

                if (superAdmin != null) {
                    new SuperAdminDashboard(superAdmin, superAdminService,
                        new Service.DriverService(), new Service.SubAdminService());

                    parentFrame.setVisible(false);
                    dispose();
                } else {
                    showErrorDialog("Error", "Failed to load admin profile data");
                }
            } else if (result == 2) {
                showErrorDialog("Login Failed", "Invalid password. Please try again.");
                passwordField.setText("");
            } else {
                showErrorDialog("Login Failed", "Admin account not found");
            }
        });
        
        formContainer.add(loginBtn, gbc);
        
        // Add form to center panel
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerPanel.add(formContainer, centerGbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Shows custom forgot password dialog instead of JOptionPane
     */
    private void showForgotPasswordDialog() {
        JDialog dialog = new JDialog(this, "Password Recovery", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Password Recovery");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Message
        JLabel messageLabel = new JLabel("<html><p>To reset your password, please follow these steps:</p></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(new Color(60, 60, 60));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Instructions
        String[] instructions = {
            "1. Contact the System Administrator",
            "2. Provide your Admin ID",
            "3. Verify your identity",
            "4. Receive temporary password via secure channel"
        };
        
        for (String instruction : instructions) {
            JLabel instrLabel = new JLabel(instruction);
            instrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            instrLabel.setForeground(new Color(80, 80, 80));
            instrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(instrLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Admin Contact Info
        JLabel contactLabel = new JLabel("<html><b>Administrator Contact:</b> system.admin@trackify.com</html>");
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactLabel.setForeground(new Color(155, 89, 182));
        contactLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(contactLabel);
        
        contentPanel.add(Box.createVerticalGlue());
        
        // OK Button
        JButton okBtn = new JButton("OK");
        okBtn.setBackground(new Color(155, 89, 182));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okBtn.setFocusPainted(false);
        okBtn.setBorderPainted(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(e -> dialog.dispose());
        contentPanel.add(okBtn);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Shows error dialog with custom styling
     */
    private void showErrorDialog(String title, String message) {
        JDialog errorDialog = new JDialog(this, title, true);
        errorDialog.setSize(400, 200);
        errorDialog.setLocationRelativeTo(this);
        errorDialog.setResizable(false);
        
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        errorPanel.setBackground(Color.WHITE);
        
        JLabel errorLabel = new JLabel(message);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        errorLabel.setForeground(new Color(60, 60, 60));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorPanel.add(errorLabel);
        
        errorPanel.add(Box.createVerticalGlue());
        
        JButton closeBtn = new JButton("OK");
        closeBtn.setBackground(new Color(231, 76, 60));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setPreferredSize(new Dimension(80, 35));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> errorDialog.dispose());
        errorPanel.add(closeBtn);
        
        errorDialog.add(errorPanel);
        errorDialog.setVisible(true);
    }
    
    /**
     * Custom Background Panel with gradient
     */
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
            
            // Subtle decorative elements
            g2d.setColor(new Color(155, 89, 182, 30));
            g2d.fillOval(-100, -100, 400, 400);
            g2d.fillOval(getWidth() - 200, getHeight() - 300, 500, 500);
        }
    }
}