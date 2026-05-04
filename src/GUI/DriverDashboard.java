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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        
        JLabel titleLabel = new JLabel("🚗 DRIVER DASHBOARD");
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
        
        tabbedPane.addTab("👤 Profile", new JPanel());
        tabbedPane.addTab("📊 Performance", new JPanel());
        tabbedPane.addTab("🏆 Rankings", new JPanel());
        
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
        
        tabbedPane.setSelectedIndex(0);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 30, 25));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        infoPanel.setMaximumSize(new Dimension(900, 550));
        
        addInfoRow(infoPanel, "👤 Full Name:", driver.getfirstName() + " " + driver.getlastName());
        addInfoRow(infoPanel, "🆔 Driver ID:", driver.getpublic_driver_id() != null ? driver.getpublic_driver_id() : "N/A");
        addInfoRow(infoPanel, "📞 Contact:", driver.getcontactNumber() != null ? driver.getcontactNumber() : "N/A");
        addInfoRow(infoPanel, "📍 Address:", driver.getaddress() != null ? driver.getaddress() : "N/A");
        addInfoRow(infoPanel, "🎫 License Number:", driver.getlicenseNum() != null ? driver.getlicenseNum() : "N/A");
        addInfoRow(infoPanel, "⚧ Gender:", driver.getgender() != null ? driver.getgender() : "N/A");
        addInfoRow(infoPanel, "✓ Status:", driver.getStatus() != null ? driver.getStatus() : "Active");
        addInfoRow(infoPanel, "📅 Date of Birth:", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        
        panel.add(infoPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 15));
        labelComponent.setForeground(new Color(52, 152, 219));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        valueComponent.setForeground(new Color(60, 60, 60));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<DriverPerformance> records = ds.getDriverRecords(driver.getpublic_driver_id());
            String[] columns = {"Tickets", "Revenue (₱)", "KM/L", "Date"};
            Object[][] data = new Object[records.size()][4];
            
            for (int i = 0; i < records.size(); i++) {
                DriverPerformance dp = records.get(i);
                data[i][0] = dp.gettotalTickets();
                data[i][1] = String.format("₱ %.2f", dp.gettotalRevenue());
                data[i][2] = String.format("%.2f", dp.getaverageKMPL());
                data[i][3] = "Today";
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(30);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading performance panel: " + e.getMessage());
        }
        
        return panel;
    }
    
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<Driver> rankings = ds.getDriverRanking();
            
            // ✓ FIX: Check for null driver ID
            int currentRank = -1;
            for (int i = 0; i < rankings.size(); i++) {
                Driver d = rankings.get(i);
                if (d != null && d.getpublic_driver_id() != null && 
                    d.getpublic_driver_id().equals(driver.getpublic_driver_id())) {
                    currentRank = i + 1;
                    break;
                }
            }
            
            // Stats Panel
            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 40, 40));
            statsPanel.setBackground(new Color(240, 242, 245));
            statsPanel.setMaximumSize(new Dimension(1000, 220));
            
            // Current Rank Card
            JPanel rankCard = new JPanel(new BorderLayout());
            rankCard.setBackground(Color.WHITE);
            rankCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            
            JLabel rankLabel = new JLabel("🏆 Your Current Rank");
            rankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            rankLabel.setForeground(new Color(100, 100, 100));
            
            JLabel rankValue = new JLabel(currentRank != -1 ? "#" + currentRank : "N/A");
            rankValue.setFont(new Font("Segoe UI", Font.BOLD, 72));
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
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            
            JLabel totalLabel = new JLabel("👥 Total Drivers");
            totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            totalLabel.setForeground(new Color(100, 100, 100));
            
            JLabel totalValue = new JLabel(String.valueOf(rankings.size()));
            totalValue.setFont(new Font("Segoe UI", Font.BOLD, 72));
            totalValue.setForeground(new Color(46, 204, 113));
            totalValue.setHorizontalAlignment(JLabel.CENTER);
            
            totalCard.add(totalLabel, BorderLayout.NORTH);
            totalCard.add(totalValue, BorderLayout.CENTER);
            statsPanel.add(totalCard);
            
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setOpaque(false);
            topPanel.add(statsPanel);
            topPanel.add(Box.createVerticalStrut(40));
            
            panel.add(topPanel, BorderLayout.NORTH);
            
            // Rankings Table
            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[Math.min(rankings.size(), 10)][3];
            
            for (int i = 0; i < Math.min(rankings.size(), 10); i++) {
                Driver d = rankings.get(i);
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
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
            e.printStackTrace();
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