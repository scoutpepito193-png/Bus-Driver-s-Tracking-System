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

    private DriverService ds = new DriverService();
    private Driver driver;
    
    private boolean profileLoaded = false;
    private boolean performanceLoaded = false;
    private boolean rankingLoaded = false;

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
        
        // Header (IMPROVED)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("DRIVER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Panel with Lazy Loading
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add empty panels first
        tabbedPane.addTab("Profile", new JPanel());
        tabbedPane.addTab("Performance", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        
        // Add listener to load tab content only when clicked
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            switch(selectedIndex) {
                case 0:
                    if (!profileLoaded) {
                        tabbedPane.setComponentAt(0, createProfilePanel());
                        profileLoaded = true;
                    }
                    break;
                case 1:
                    if (!performanceLoaded) {
                        tabbedPane.setComponentAt(1, createPerformancePanel());
                        performanceLoaded = true;
                    }
                    break;
                case 2:
                    if (!rankingLoaded) {
                        tabbedPane.setComponentAt(2, createRankingPanel());
                        rankingLoaded = true;
                    }
                    break;
            }
        });
        
        // Trigger first tab load
        tabbedPane.setSelectedIndex(0);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 20, 15));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.setMaximumSize(new Dimension(600, 400));
        
        addInfoRow(infoPanel, "Driver ID:", driver.getpublic_driver_id());
        addInfoRow(infoPanel, "Full Name:", driver.getfirstName() + " " + driver.getlastName());
        addInfoRow(infoPanel, "Contact:", driver.getcontactNumber());
        addInfoRow(infoPanel, "Address:", driver.getaddress());
        addInfoRow(infoPanel, "License Number:", driver.getlicenseNum());
        addInfoRow(infoPanel, "Gender:", driver.getgender());
        addInfoRow(infoPanel, "Status:", driver.getStatus() != null ? driver.getStatus() : "Active");
        addInfoRow(infoPanel, "Date of Birth:", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        
        panel.add(infoPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComponent.setForeground(new Color(52, 152, 219));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComponent.setForeground(new Color(60, 60, 60));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            List<DriverPerformance> records = ds.getDriverRecords(driver.getpublic_driver_id());
            String[] columns = {"Tickets", "Revenue", "KM/L", "Date"};
            Object[][] data = new Object[records.size()][4];
            
            for (int i = 0; i < records.size(); i++) {
                DriverPerformance dp = records.get(i);
                data[i][0] = dp.gettotalTickets();
                data[i][1] = String.format("%.2f", dp.gettotalRevenue());
                data[i][2] = String.format("%.2f", dp.getaverageKMPL());
                data[i][3] = "Today";
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading performance panel: " + e.getMessage());
            panel.add(new JLabel("Error loading performance data"));
        }
        
        return panel;
    }
    
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        try {
            List<Driver> rankings = ds.getDriverRanking();
            
            // Find current driver's rank
            int currentRank = -1;
            for (int i = 0; i < rankings.size(); i++) {
                if (rankings.get(i).getpublic_driver_id().equals(driver.getpublic_driver_id())) {
                    currentRank = i + 1;
                    break;
                }
            }
            
            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
            statsPanel.setBackground(Color.WHITE);
            statsPanel.setMaximumSize(new Dimension(600, 150));
            
            // Current Rank Card
            JPanel rankCard = new JPanel(new BorderLayout());
            rankCard.setBackground(Color.WHITE);
            rankCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            JLabel rankLabel = new JLabel("Your Rank");
            rankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rankLabel.setForeground(new Color(100, 100, 100));
            
            JLabel rankValue = new JLabel(currentRank != -1 ? "#" + currentRank : "N/A");
            rankValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
            rankValue.setForeground(new Color(52, 152, 219));
            rankValue.setHorizontalAlignment(JLabel.CENTER);
            
            rankCard.add(rankLabel, BorderLayout.NORTH);
            rankCard.add(rankValue, BorderLayout.CENTER);
            statsPanel.add(rankCard);
            
            // Total Drivers Card
            JPanel totalCard = new JPanel(new BorderLayout());
            totalCard.setBackground(Color.WHITE);
            totalCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            JLabel totalLabel = new JLabel("Total Drivers");
            totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            totalLabel.setForeground(new Color(100, 100, 100));
            
            JLabel totalValue = new JLabel(String.valueOf(rankings.size()));
            totalValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
            totalValue.setForeground(new Color(46, 204, 113));
            totalValue.setHorizontalAlignment(JLabel.CENTER);
            
            totalCard.add(totalLabel, BorderLayout.NORTH);
            totalCard.add(totalValue, BorderLayout.CENTER);
            statsPanel.add(totalCard);
            
            panel.add(statsPanel, BorderLayout.NORTH);
            
            // Rankings Table
            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[Math.min(rankings.size(), 10)][3];
            
            for (int i = 0; i < Math.min(rankings.size(), 10); i++) {
                Driver d = rankings.get(i);
                data[i][0] = (i + 1) + "";
                data[i][1] = d.getfirstName() + " " + d.getlastName();
                data[i][2] = d.getranking();
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
            panel.add(new JLabel("Error loading ranking data"));
        }
        
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