package GUI;

import Model.Driver;
import Model.SubAdmin;
import Service.DriverService;
import Service.SubAdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubAdminDashboard extends JFrame {

    DriverService ds = new DriverService();
    SubAdminService sas = new SubAdminService();
    private SubAdmin subAdmin;

    public SubAdminDashboard(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;
        
        setTitle("Sub Admin Dashboard");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("SUB ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + subAdmin.getfirstName() + " " + subAdmin.getlastName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(220, 220, 220));
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(titleLabel);
        leftHeader.add(userLabel);
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Overview Tab
        tabbedPane.addTab("Overview", createOverviewPanel());
        
        // Drivers Tab
        tabbedPane.addTab("Drivers", createDriversPanel());
        
        // Register Driver Tab
        tabbedPane.addTab("Register Driver", createRegisterDriverPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Total Drivers Card
        panel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(46, 204, 113)));
        
        // Pending Requests Card
        panel.add(createStatCard("Pending Requests", "0", new Color(241, 196, 15)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<Driver> list = ds.getDriverRanking();
        String[] columns = {"Driver ID", "Name", "Contact", "Status"};
        Object[][] data = new Object[list.size()][4];
        
        for (int i = 0; i < list.size(); i++) {
            Driver d = list.get(i);
            data[i][0] = d.getpublic_driver_id();
            data[i][1] = d.getfirstName() + " " + d.getlastName();
            data[i][2] = d.getcontactNumber();
            data[i][3] = "Active";
        }
        
        JTable table = new JTable(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRegisterDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        
        // Driver ID
        gbc.gridy = 0;
        formPanel.add(new JLabel("Driver ID:"), gbc);
        gbc.gridy = 1;
        JTextField driverIdField = new JTextField(20);
        formPanel.add(driverIdField, gbc);
        
        // First Name
        gbc.gridy = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridy = 3;
        JTextField firstNameField = new JTextField(20);
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridy = 4;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy = 5;
        JTextField lastNameField = new JTextField(20);
        formPanel.add(lastNameField, gbc);
        
        // Contact
        gbc.gridy = 6;
        formPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridy = 7;
        JTextField contactField = new JTextField(20);
        formPanel.add(contactField, gbc);
        
        // Register Button
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton registerBtn = new JButton("REGISTER DRIVER");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Driver registered successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            driverIdField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            contactField.setText("");
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