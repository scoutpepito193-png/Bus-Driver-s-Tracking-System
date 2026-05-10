package GUI;

import Service.LogInService;
import Model.AuthResult;
import Model.Role;
import Model.SuperAdmin;
import Model.SubAdmin;
import Model.Driver;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UnifiedLoginPanel extends JFrame {
    
    private LogInService logInService;
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    
    public UnifiedLoginPanel() {
        this.logInService = new LogInService();
        
        setTitle("BDTracker - Login");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 40, 80));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 152, 219)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel headerLabel = new JLabel("BDTracker LOGIN");
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
            new Menu();
            dispose();
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
        formContainer.setMaximumSize(new Dimension(600, 350));
        formContainer.setPreferredSize(new Dimension(600, 350));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;
        
        // ID Label
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(idLabel, gbc);
        
        // ID Field
        gbc.gridy = 1;
        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        idField.setMaximumSize(new Dimension(400, 40));
        idField.setPreferredSize(new Dimension(400, 40));
        formContainer.add(idField, gbc);
        
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
        
        // Attempt counter (initially hidden)
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 20, 0);
        JLabel attemptLabel = new JLabel("");
        attemptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        attemptLabel.setForeground(new Color(200, 50, 50));
        formContainer.add(attemptLabel, gbc);
        
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
            String userId = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (userId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (loginAttempts >= MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this, "Maximum login attempts reached. Please try again later.",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
                loginBtn.setEnabled(false);
                return;
            }
            
            // Call unified login service
            AuthResult result = logInService.logIn(userId, password);
            
            if (result == null) {
                loginAttempts++;
                int remaining = MAX_ATTEMPTS - loginAttempts;
                attemptLabel.setText("Invalid credentials! Attempt " + loginAttempts + " of " + MAX_ATTEMPTS);
                passwordField.setText("");
                
                if (loginAttempts >= MAX_ATTEMPTS) {
                    JOptionPane.showMessageDialog(this, "Maximum login attempts reached.",
                        "Access Denied", JOptionPane.ERROR_MESSAGE);
                    loginBtn.setEnabled(false);
                }
                return;
            }
            
            // Login successful - route based on role
            handleLoginSuccess(result);
        });
        
        formContainer.add(loginBtn, gbc);
        
        // Add form to center panel
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerPanel.add(formContainer, centerGbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void handleLoginSuccess(AuthResult result) {
        Role userRole = result.getRole();
        
        switch (userRole) {
            case SUPER_ADMIN -> {
                SuperAdmin superAdmin = (SuperAdmin) result.getUser();
                if (superAdmin != null) {
                    // Skip profile check - go directly to dashboard
                    new SuperAdminDashboard(superAdmin, new Service.SuperAdminService(),
                        new Service.DriverService(), new Service.SubAdminService());
                    dispose();
                }
            }
            
            case SUB_ADMIN -> {
                SubAdmin subAdmin = (SubAdmin) result.getUser();
                if (subAdmin != null) {
                    if (subAdmin.getAge() == 0) {
                        // Profile incomplete
                        JOptionPane.showMessageDialog(this, "Please complete your profile",
                            "Profile Setup", JOptionPane.INFORMATION_MESSAGE);
                        new SubAdminProfileSetup(subAdmin, new Service.SubAdminService());
                    } else {
                        // Profile complete - go to dashboard
                        JOptionPane.showMessageDialog(this, "Welcome " + subAdmin.getfirstName() + "!",
                            "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        // TODO: Open SubAdminDashboard when created
                    }
                    dispose();
                }
            }
            
            case DRIVER -> {
                Driver driver = (Driver) result.getUser();
                if (driver != null) {
                    // Mark driver as present
                    Service.DriverAttendanceService attendanceService = new Service.DriverAttendanceService();
                    attendanceService.markDriverPresent(driver.getpublic_driver_id());
                    
                    if (driver.getAge()==0) {
                        // Profile incomplete
                        new DriverProfileSetup(driver, new Service.DriverService());
                    } else {
                        // Profile complete - go to dashboard
                        new DriverDashboard(driver, new Service.DriverService());
                    }
                    dispose();
                }
            }
            
            default -> JOptionPane.showMessageDialog(this, "Unknown user role",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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