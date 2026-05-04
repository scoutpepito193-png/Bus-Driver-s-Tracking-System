package GUI;

import Model.Driver;
import Model.SubAdmin;
import Service.DriverService;
import Service.SubAdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class SubAdminDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SubAdminService sas = new SubAdminService();
    private SubAdmin subAdmin;
    
    private boolean overviewLoaded = false;
    private boolean driversLoaded = false;
    private boolean registerLoaded = false;
    private boolean rankingLoaded = false;

    public SubAdminDashboard(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;
        
        setTitle("Sub Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 138, 68)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        
        JLabel titleLabel = new JLabel("🚗 SUB ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("⊠ LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(150, 50));
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        logoutBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        
        tabbedPane.addTab("📊 Overview", new JPanel());
        tabbedPane.addTab("🚗 Drivers", new JPanel());
        tabbedPane.addTab("🏆 Rankings", new JPanel());
        tabbedPane.addTab("✏️ Register Driver", new JPanel());
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            switch(selectedIndex) {
                case 0:
                    if (!overviewLoaded) {
                        tabbedPane.setComponentAt(0, createOverviewPanel());
                        overviewLoaded = true;
                    }
                    break;
                case 1:
                    if (!driversLoaded) {
                        tabbedPane.setComponentAt(1, createDriversPanel());
                        driversLoaded = true;
                    }
                    break;
                case 2:
                    if (!rankingLoaded) {
                        tabbedPane.setComponentAt(2, createRankingPanel());
                        rankingLoaded = true;
                    }
                    break;
                case 3:
                    if (!registerLoaded) {
                        tabbedPane.setComponentAt(3, createRegisterDriverPanel());
                        registerLoaded = true;
                    }
                    break;
            }
        });
        
        tabbedPane.setSelectedIndex(0);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            panel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(46, 204, 113), "🚗"));
            panel.add(createStatCard("Active Drivers", "0", new Color(52, 152, 219), "✓"));
            panel.add(createStatCard("Pending Requests", "0", new Color(241, 196, 15), "⏳"));
            panel.add(createStatCard("Total Vehicles", "0", new Color(155, 89, 182), "🚌"));
        } catch (Exception e) {
            System.err.println("Error loading overview panel: " + e.getMessage());
        }
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(emojiLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<Driver> list = ds.getDriverRanking();
            String[] columns = {"Driver ID", "Driver Name", "Contact", "Status"};
            Object[][] data = new Object[list.size()][4];
            
            for (int i = 0; i < list.size(); i++) {
                Driver d = list.get(i);
                data[i][0] = d.getpublic_driver_id() != null ? d.getpublic_driver_id() : "N/A";
                data[i][1] = d.getfirstName() + " " + d.getlastName();
                data[i][2] = d.getcontactNumber() != null ? d.getcontactNumber() : "N/A";
                data[i][3] = "Active";
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(30);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(46, 204, 113));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading drivers panel: " + e.getMessage());
        }
        
        return panel;
    }
    
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<Driver> list = ds.getDriverRanking();
            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[Math.min(list.size(), 15)][3];
            
            for (int i = 0; i < Math.min(list.size(), 15); i++) {
                Driver d = list.get(i);
                if (d != null && d.getpublic_driver_id() != null) {
                    data[i][0] = (i + 1) + "";
                    data[i][1] = d.getfirstName() + " " + d.getlastName();
                    data[i][2] = d.getranking();
                }
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(30);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(46, 204, 113));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
        }
        
        return panel;
    }
    
    private JPanel createRegisterDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        formPanel.setMaximumSize(new Dimension(800, 650));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        
        // Title
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("✏️ REGISTER NEW DRIVER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(46, 204, 113));
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        
        // Driver ID
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel idLabel = new JLabel("Driver ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(idLabel, gbc);
        gbc.gridy = 2;
        JTextField idField = new JTextField(25);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        idField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(idField, gbc);
        
        // First Name
        gbc.gridy = 3;
        JLabel fnLabel = new JLabel("First Name:");
        fnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fnLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(fnLabel, gbc);
        gbc.gridy = 4;
        JTextField fnField = new JTextField(25);
        fnField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fnField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        fnField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(fnField, gbc);
        
        // Last Name
        gbc.gridy = 5;
        JLabel lnLabel = new JLabel("Last Name:");
        lnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lnLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(lnLabel, gbc);
        gbc.gridy = 6;
        JTextField lnField = new JTextField(25);
        lnField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lnField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        lnField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(lnField, gbc);
        
        // Contact
        gbc.gridy = 7;
        JLabel ctLabel = new JLabel("Contact Number:");
        ctLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ctLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(ctLabel, gbc);
        gbc.gridy = 8;
        JTextField ctField = new JTextField(25);
        ctField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ctField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        ctField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(ctField, gbc);
        
        // Password ✓ NEW FIELD
        gbc.gridy = 9;
        JLabel pwLabel = new JLabel("Password:");
        pwLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pwLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(pwLabel, gbc);
        gbc.gridy = 10;
        JPasswordField pwField = new JPasswordField(25);
        pwField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pwField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        pwField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(pwField, gbc);
        
        // Confirm Password ✓ NEW FIELD
        gbc.gridy = 11;
        JLabel cpwLabel = new JLabel("Confirm Password:");
        cpwLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cpwLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(cpwLabel, gbc);
        gbc.gridy = 12;
        JPasswordField cpwField = new JPasswordField(25);
        cpwField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cpwField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cpwField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(cpwField, gbc);
        
        // Register Button
        gbc.gridy = 13;
        gbc.insets = new Insets(30, 15, 15, 15);
        gbc.gridwidth = 2;
        JButton registerBtn = new JButton("✓ REGISTER DRIVER");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        registerBtn.setPreferredSize(new Dimension(300, 50));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(27, 149, 74));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(46, 204, 113));
            }
        });
        registerBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String fn = fnField.getText().trim();
            String ln = lnField.getText().trim();
            String ct = ctField.getText().trim();
            String pw = new String(pwField.getPassword());
            String cpw = new String(cpwField.getPassword());
            
            // Validation
            if (id.isEmpty() || fn.isEmpty() || ln.isEmpty() || ct.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!pw.equals(cpw)) {
                JOptionPane.showMessageDialog(panel, "Passwords do not match", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (pw.length() < 6) {
                JOptionPane.showMessageDialog(panel, "Password must be at least 6 characters", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // ✓ Register driver with password
            String requestCode = new DriverService().registerDriver(id, fn, ln, "M", LocalDate.now(), "", ct, "", LocalDate.now().plusYears(5), "", pw, cpw);
            
            if (requestCode != null) {
                JOptionPane.showMessageDialog(panel, 
                    "Driver registration request submitted!\nRequest Code: " + requestCode + "\n\nThis request is pending super admin approval.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields
                idField.setText("");
                fnField.setText("");
                lnField.setText("");
                ctField.setText("");
                pwField.setText("");
                cpwField.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to register driver", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(registerBtn, gbc);
        
        panel.add(formPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 245),
                    getWidth(), getHeight(), new Color(235, 245, 235));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}