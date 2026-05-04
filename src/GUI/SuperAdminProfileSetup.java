package GUI;

import Model.SuperAdmin;
import Service.SuperAdminService;
import Service.DriverService;
import Service.SubAdminService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperAdminProfileSetup extends JFrame {
    private SuperAdminService superAdminService;
    private SuperAdmin superAdmin;
    private DriverService driverService;
    private SubAdminService subAdminService;

    // Constructor with just required parameters for signup flow
    public SuperAdminProfileSetup(SuperAdmin superAdmin, SuperAdminService superAdminService) {
        this(superAdmin, superAdminService, new DriverService(), new SubAdminService());
    }

    // Full constructor for dashboard flow
    public SuperAdminProfileSetup(SuperAdmin superAdmin, SuperAdminService superAdminService, 
                                  DriverService driverService, SubAdminService subAdminService) {
        this.superAdmin = superAdmin;
        this.superAdminService = superAdminService;
        this.driverService = driverService;
        this.subAdminService = subAdminService;
        
        setTitle("Trackify - Complete Your Profile");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        
        // Header with Logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(108, 52, 131)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Logo Label
        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Complete Your Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(titleLabel);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel - for vertical centering
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        formContainer.setMaximumSize(new Dimension(600, 500));
        formContainer.setPreferredSize(new Dimension(600, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        
        // Age Label
        gbc.gridy = 0;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ageLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(ageLabel, gbc);
        
        // Age Field
        gbc.gridy = 1;
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(25, 18, 70, 1));
        ageSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ageSpinner.setMaximumSize(new Dimension(400, 35));
        ageSpinner.setPreferredSize(new Dimension(400, 35));
        formContainer.add(ageSpinner, gbc);
        
        // Contact Number Label
        gbc.gridy = 2;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        contactLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(contactLabel, gbc);
        
        // Contact Field
        gbc.gridy = 3;
        JTextField contactField = new JTextField(20);
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        contactField.setMaximumSize(new Dimension(400, 35));
        contactField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(contactField, gbc);
        
        // Position Label
        gbc.gridy = 4;
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        positionLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(positionLabel, gbc);
        
        // Position Field
        gbc.gridy = 5;
        JTextField positionField = new JTextField(20);
        positionField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        positionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        positionField.setMaximumSize(new Dimension(400, 35));
        positionField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(positionField, gbc);
        
        // Photo URL Label
        gbc.gridy = 6;
        JLabel photoLabel = new JLabel("Photo URL:");
        photoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        photoLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(photoLabel, gbc);
        
        // Photo URL Field
        gbc.gridy = 7;
        JTextField photoField = new JTextField(20);
        photoField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        photoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        photoField.setMaximumSize(new Dimension(400, 35));
        photoField.setPreferredSize(new Dimension(400, 35));
        formContainer.add(photoField, gbc);
        
        // Complete Button
        gbc.gridy = 8;
        gbc.insets = new Insets(25, 0, 0, 0);
        JButton completeBtn = new JButton("COMPLETE PROFILE");
        completeBtn.setBackground(new Color(155, 89, 182));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        completeBtn.setFocusPainted(false);
        completeBtn.setPreferredSize(new Dimension(400, 45));
        completeBtn.setMaximumSize(new Dimension(400, 45));
        completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                completeBtn.setBackground(new Color(108, 52, 131));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                completeBtn.setBackground(new Color(155, 89, 182));
            }
        });
        
        completeBtn.addActionListener(e -> {
            // Validate inputs
            String contactNum = contactField.getText().trim();
            String position = positionField.getText().trim();
            String photoURL = photoField.getText().trim();
            int age = (int) ageSpinner.getValue();
            
            if (contactNum.isEmpty() || position.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Update profile with collected data
            superAdmin.setAge(age);
            superAdmin.setcontactNum(contactNum);
            superAdmin.setposition(position);
            superAdmin.setphotoURL(photoURL);
            
            JOptionPane.showMessageDialog(this, "Profile completed successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // ✅ OPEN DASHBOARD WITH ALL 4 REQUIRED PARAMETERS
            new SuperAdminDashboard(superAdmin, superAdminService, driverService, subAdminService);
            
            dispose();
        });
        
        formContainer.add(completeBtn, gbc);
        
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