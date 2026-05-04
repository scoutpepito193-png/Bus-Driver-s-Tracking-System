package GUI;

import Model.SuperAdmin;
import Service.SuperAdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperAdminSignupPanel extends JFrame {
    
    private SuperAdminService superAdminService;
    private JFrame parentFrame;

    public SuperAdminSignupPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.superAdminService = new SuperAdminService();
        
        setTitle("BDTracker - Super Admin Signup");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(700, 600));
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
        
        g2d.setColor(new Color(155, 89, 182));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("S", 16, 50);
        
        g2d.dispose();
        return icon;
    }
    
    private void addComponents(JPanel mainPanel) {
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(108, 52, 131)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel headerLabel = new JLabel("SUPER ADMIN SIGNUP");
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
            new AdminRoleSelection(new Menu());
            dispose();
        });
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel - for scrolling
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formContainer.setMaximumSize(new Dimension(600, 600));
        formContainer.setPreferredSize(new Dimension(600, 600));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        
        // Company ID
        gbc.gridy = 0;
        JLabel companyIdLabel = new JLabel("Company ID:");
        companyIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        companyIdLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(companyIdLabel, gbc);
        
        gbc.gridy = 1;
        JTextField companyIdField = new JTextField(20);
        companyIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        companyIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        companyIdField.setMaximumSize(new Dimension(400, 35));
        companyIdField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(companyIdField, gbc);
        
        // First Name
        gbc.gridy = 2;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        firstNameLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(firstNameLabel, gbc);
        
        gbc.gridy = 3;
        JTextField firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        firstNameField.setMaximumSize(new Dimension(400, 35));
        firstNameField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridy = 4;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lastNameLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(lastNameLabel, gbc);
        
        gbc.gridy = 5;
        JTextField lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        lastNameField.setMaximumSize(new Dimension(400, 35));
        lastNameField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(lastNameField, gbc);
        
        // Password
        gbc.gridy = 6;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(passwordLabel, gbc);
        
        gbc.gridy = 7;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        passwordField.setMaximumSize(new Dimension(400, 35));
        passwordField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridy = 8;
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmPassLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(confirmPassLabel, gbc);
        
        gbc.gridy = 9;
        JPasswordField confirmPassField = new JPasswordField(20);
        confirmPassField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        confirmPassField.setMaximumSize(new Dimension(400, 35));
        confirmPassField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(confirmPassField, gbc);
        
        // Signup Button
        gbc.gridy = 10;
        gbc.insets = new Insets(25, 0, 0, 0);
        JButton signupBtn = new JButton("CREATE ACCOUNT");
        signupBtn.setBackground(new Color(155, 89, 182));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signupBtn.setFocusPainted(false);
        signupBtn.setPreferredSize(new Dimension(400, 45));
        signupBtn.setMaximumSize(new Dimension(400, 45));
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupBtn.setBackground(new Color(108, 52, 131));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupBtn.setBackground(new Color(155, 89, 182));
            }
        });
        
        signupBtn.addActionListener(e -> {
            String companyId = companyIdField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            
            // Validation
            if (companyId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
                password.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Call backend to register super admin
            boolean success = superAdminService.registerSA(
                companyId,
                firstName,
                lastName,
                "", // contact - not required for signup
                "", // position - not required for signup
                "", // photo - not required for signup
                password,
                confirmPass
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Account created successfully!\nPlease log in.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Go to profile setup
                SuperAdmin superAdmin = superAdminService.getSAData();
                new SuperAdminProfileSetup(superAdmin, superAdminService);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        formContainer.add(signupBtn, gbc);
        
        // Add form to center panel
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerPanel.add(formContainer, centerGbc);
        
        // Add scrollpane
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
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
            
            g2d.setColor(new Color(155, 89, 182, 30));
            g2d.fillOval(-100, -100, 400, 400);
            g2d.fillOval(getWidth() - 200, getHeight() - 300, 500, 500);
        }
    }
}