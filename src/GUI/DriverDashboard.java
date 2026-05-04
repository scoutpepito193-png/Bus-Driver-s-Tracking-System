package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Service.DriverService;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverDashboard extends JFrame {

    DriverService ds = new DriverService();
    private Driver driver;

    public DriverDashboard(Driver driver, DriverService driverService) {
        this.driver = driver;
        
        setTitle("Driver Dashboard");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("DRIVER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + driver.getfirstName() + " " + driver.getlastName());
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
        
        // Profile Tab
        tabbedPane.addTab("Profile", createProfilePanel());
        
        // Performance Tab
        tabbedPane.addTab("Performance", createPerformancePanel());
        
        // Requests Tab
        tabbedPane.addTab("Requests", createRequestsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        addInfoRow(infoPanel, "Driver ID:", driver.getpublic_driver_id());
        addInfoRow(infoPanel, "Name:", driver.getfirstName() + " " + driver.getlastName());
        addInfoRow(infoPanel, "Contact:", driver.getcontactNumber());
        addInfoRow(infoPanel, "Address:", driver.getaddress());
        addInfoRow(infoPanel, "License Number:", driver.getlicenseNum());
        addInfoRow(infoPanel, "Gender:", driver.getgender());
        
        panel.add(infoPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<DriverPerformance> records = ds.getDriverRecords(driver.getpublic_driver_id());
        String[] columns = {"Tickets", "Revenue", "KM/L"};
        Object[][] data = new Object[records.size()][3];
        
        for (int i = 0; i < records.size(); i++) {
            DriverPerformance dp = records.get(i);
            data[i][0] = dp.gettotalTickets();
            data[i][1] = String.format("%.2f", dp.gettotalRevenue());
            data[i][2] = String.format("%.2f", dp.getaverageKMPL());
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
    
    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel requestPanel = new JPanel(new GridBagLayout());
        requestPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        
        // Request Reason
        gbc.gridy = 0;
        requestPanel.add(new JLabel("Request Reason:"), gbc);
        gbc.gridy = 1;
        JTextArea reasonArea = new JTextArea(5, 30);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        requestPanel.add(new JScrollPane(reasonArea), gbc);
        
        // Submit Button
        gbc.gridy = 2;
        JButton submitBtn = new JButton("SUBMIT REQUEST");
        submitBtn.setBackground(new Color(52, 152, 219));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> {
            if (reasonArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a reason", "Validation", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Request submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                reasonArea.setText("");
            }
        });
        requestPanel.add(submitBtn, gbc);
        
        panel.add(requestPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 248, 255),
                    getWidth(), getHeight(), new Color(235, 240, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}