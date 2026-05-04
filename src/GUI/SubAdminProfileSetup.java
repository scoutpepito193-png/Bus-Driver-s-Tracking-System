package GUI;

import Model.SubAdmin;
import Service.SubAdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * SubAdminProfileSetup - Sub-admin profile configuration UI
 * Collects: Contact, Position, Age, Photo
 * Data flows to SubAdminDashboard - database integration done by coworker's repo
 */
public class SubAdminProfileSetup extends JFrame {
    private SubAdminService subAdminService;
    private SubAdmin subAdmin;
    
    private JTextField contactField;
    private JTextField positionField;
    private JSpinner ageSpinner;
    private JLabel photoPreview;
    private File selectedPhotoFile;
    
    public SubAdminProfileSetup(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;
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
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(27, 149, 74)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Complete Your Sub Admin Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(titleLabel);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        formContainer.setMaximumSize(new Dimension(650, 500));
        formContainer.setPreferredSize(new Dimension(650, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        
        // Contact Number
        gbc.gridy = 0;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        contactLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(contactLabel, gbc);
        
        gbc.gridy = 1;
        contactField = makeTextField();
        formContainer.add(contactField, gbc);
        
        // Position
        gbc.gridy = 2;
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        positionLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(positionLabel, gbc);
        
        gbc.gridy = 3;
        positionField = makeTextField();
        formContainer.add(positionField, gbc);
        
        // Age
        gbc.gridy = 4;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ageLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(ageLabel, gbc);
        
        gbc.gridy = 5;
        ageSpinner = new JSpinner(new SpinnerNumberModel(25, 18, 70, 1));
        ageSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ageSpinner.setMaximumSize(new Dimension(400, 35));
        ageSpinner.setPreferredSize(new Dimension(400, 35));
        formContainer.add(ageSpinner, gbc);
        
        // Photo Upload
        gbc.gridy = 6;
        JLabel photoLabel = new JLabel("Profile Photo:");
        photoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        photoLabel.setForeground(new Color(60, 60, 60));
        formContainer.add(photoLabel, gbc);
        
        gbc.gridy = 7;
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        photoPanel.setOpaque(false);
        
        JButton uploadBtn = new JButton("CHOOSE PHOTO");
        uploadBtn.setBackground(new Color(46, 204, 113));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        uploadBtn.setFocusPainted(false);
        uploadBtn.setPreferredSize(new Dimension(150, 35));
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> choosePhoto());
        photoPanel.add(uploadBtn);
        
        photoPreview = new JLabel("No photo selected");
        photoPreview.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        photoPreview.setForeground(new Color(100, 100, 100));
        photoPanel.add(photoPreview);
        
        formContainer.add(photoPanel, gbc);
        
        // Complete Button
        gbc.gridy = 8;
        gbc.insets = new Insets(25, 0, 0, 0);
        JButton completeBtn = new JButton("COMPLETE PROFILE");
        completeBtn.setBackground(new Color(46, 204, 113));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        completeBtn.setFocusPainted(false);
        completeBtn.setPreferredSize(new Dimension(400, 45));
        completeBtn.setMaximumSize(new Dimension(400, 45));
        completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        completeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                completeBtn.setBackground(new Color(27, 149, 74));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                completeBtn.setBackground(new Color(46, 204, 113));
            }
        });
        
        completeBtn.addActionListener(e -> saveProfile());
        formContainer.add(completeBtn, gbc);
        
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.weightx = 1.0;
        centerGbc.weighty = 1.0;
        centerPanel.add(formContainer, centerGbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    private JTextField makeTextField() {
        JTextField f = new JTextField(25);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        f.setMaximumSize(new Dimension(400, 35));
        f.setPreferredSize(new Dimension(400, 35));
        return f;
    }
    
    private void choosePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"
        ));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            photoPreview.setText("✓ " + selectedPhotoFile.getName());
            photoPreview.setForeground(new Color(46, 204, 113));
        }
    }
    
    private void saveProfile() {
        String contact = contactField.getText().trim();
        String position = positionField.getText().trim();
        int age = (int) ageSpinner.getValue();
        
        if (contact.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Update subAdmin object
            subAdmin.setcontactnum(contact);
            subAdmin.setposition(position);
            subAdmin.setAge(age);
            
            // Handle photo if selected
            if (selectedPhotoFile != null) {
                String photoPath = "photos/subadmin_" + subAdmin.getpublic_sub_id() + "_" + System.currentTimeMillis() + ".jpg";
                subAdmin.setphotoURL(photoPath);
            }
            
            // Show success - coworker's repo will handle database save
            JOptionPane.showMessageDialog(this,
                "Profile completed successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Open dashboard
            new SubAdminDashboard(subAdmin, subAdminService);
            dispose();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "An error occurred: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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
