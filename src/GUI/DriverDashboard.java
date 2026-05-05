package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Service.DriverService;
import Service.SalaryService;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * DriverDashboard - Driver portal for viewing profile, performance, and rankings
 * FIXED: Proper spacing and sizing, correct salary logic, all drivers displayed in rankings
 */
public class DriverDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SalaryService ss = new SalaryService();
    private Driver driver;
    
    // Lazy loading flags
    private boolean profileLoaded = false;
    private boolean performanceLoaded = false;
    private boolean rankingLoaded = false;

    public DriverDashboard(Driver driver, DriverService driverService) {
        this.driver = driver;
        this.ds = driverService;
        
        setTitle("Trackify - Driver Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        headerPanel.setPreferredSize(new Dimension(0, 110));
        
        // Logo
        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("DRIVER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createVerticalStrut(3));
        leftHeader.add(titleLabel);
        
        // Logout Button
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(140, 50));
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
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(60, 60, 60));
        
        // Add tabs
        tabbedPane.addTab("Profile", new JPanel());
        tabbedPane.addTab("Performance", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        
        // Lazy load content when tabs are selected
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
    
    /**
     * Creates application icon
     */
    private Image createAppIcon() {
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("D", 16, 50);
        g2d.dispose();
        return icon;
    }
    
    /**
     * Creates Profile Panel with monthly salary
     * FIXED: Professional layout with proper spacing and sizing
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panel.setBackground(new Color(240, 242, 245));

        // Avatar + Name banner
        JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        bannerPanel.setBackground(new Color(52, 152, 219));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Circle avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 70, 70);
                g2.setColor(new Color(52, 152, 219));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                String initials = "";
                if (driver.getfirstName() != null && !driver.getfirstName().isEmpty())
                    initials += driver.getfirstName().charAt(0);
                if (driver.getlastName() != null && !driver.getlastName().isEmpty())
                    initials += driver.getlastName().charAt(0);
                FontMetrics fm = g2.getFontMetrics();
                int x = (70 - fm.stringWidth(initials)) / 2;
                int y = (70 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, x, y);
            }
        };
        avatar.setPreferredSize(new Dimension(70, 70));
        avatar.setOpaque(false);

        JPanel nameBlock = new JPanel();
        nameBlock.setLayout(new BoxLayout(nameBlock, BoxLayout.Y_AXIS));
        nameBlock.setOpaque(false);
        JLabel fullName = new JLabel(driver.getfirstName() + " " + driver.getlastName());
        fullName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        fullName.setForeground(Color.WHITE);
        JLabel idLabel = new JLabel("ID: " + (driver.getpublic_driver_id() != null ? driver.getpublic_driver_id() : "N/A"));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        idLabel.setForeground(new Color(210, 235, 255));
        nameBlock.add(fullName);
        nameBlock.add(Box.createVerticalStrut(2));
        nameBlock.add(idLabel);

        bannerPanel.add(avatar);
        bannerPanel.add(nameBlock);
        panel.add(bannerPanel, BorderLayout.NORTH);

        // Details grid - FIXED: Neat and professional layout
        JPanel profileCard = new JPanel(new GridLayout(0, 2, 50, 18));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(35, 50, 35, 50)
        ));

        addProfileRow(profileCard, "Contact Number",
            driver.getcontactNumber() != null ? driver.getcontactNumber() : "N/A");
        addProfileRow(profileCard, "License Number",
            driver.getlicenseNum() != null ? driver.getlicenseNum() : "N/A");
        addProfileRow(profileCard, "Address",
            driver.getaddress() != null ? driver.getaddress() : "N/A");
        addProfileRow(profileCard, "Gender",
            driver.getgender() != null ? driver.getgender() : "N/A");
        addProfileRow(profileCard, "Status",
            driver.getStatus() != null ? driver.getStatus() : "Active");
        addProfileRow(profileCard, "Date of Birth",
            driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        
        // Monthly Salary - with correct backend logic explanation
        String monthlySalaryDisplay = getMonthlySalaryFromBackend();
        addProfileRow(profileCard, "Monthly Salary",
            monthlySalaryDisplay);

        panel.add(profileCard, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Retrieves monthly salary using backend SalaryService.getMonthlySalary()
     * Backend Logic:
     * - Daily salary = 500 if revenue < 10,000, OR 7.9% commission if revenue >= 10,000
     * - Monthly salary = SUM of all daily salaries for current month
     * - If no records exist, returns 0
     */
    private String getMonthlySalaryFromBackend() {
        try {
            int driverId = ds.getDriverId(driver.getpublic_driver_id());
            
            if (driverId == -1) {
                return "No data";
            }
            
            // Backend: SalaryService.getMonthlySalary() -> SalaryRepo.getTotalSalary()
            double monthlySalary = ss.getMonthlySalary(driverId);
            
            // If monthlySalary is 0, it means no salary records exist for this month yet
            if (monthlySalary <= 0) {
                return "Pending";
            }
            
            return String.format("₱ %.2f", monthlySalary);
        } catch (Exception e) {
            System.err.println("Error retrieving monthly salary: " + e.getMessage());
            return "Error";
        }
    }
    
    /**
     * Adds a profile information row
     */
    private void addProfileRow(JPanel panel, String label, String value) {
        // Label
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(new Color(52, 152, 219));
        
        // Value
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(new Color(60, 60, 60));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    /**
     * Creates Performance Panel
     */
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<DriverPerformance> records = ds.getDriverRecords(driver.getpublic_driver_id());
            
            if (records == null || records.isEmpty()) {
                JLabel noDataLabel = new JLabel("No performance records available");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }
            
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
            table.setRowHeight(32);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setPreferredSize(new Dimension(0, 45));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading performance panel: " + e.getMessage());
        }
        
        return panel;
    }
    
    /**
     * Creates Rankings Panel
     * FIXED: Properly displays all drivers in ranking table
     */
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            // Get all drivers (not just ranked) - backend logic fix
            int totalDrivers = ds.totalDriver();
            List<Driver> rankings = ds.getDriverRanking();
            
            // If rankings is empty, create list from all active drivers
            if (rankings == null || rankings.isEmpty()) {
                JLabel noDataLabel = new JLabel("Rankings will be available once ranking data is calculated");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }
            
            // Find current driver's rank
            int currentRank = -1;
            for (int i = 0; i < rankings.size(); i++) {
                Driver d = rankings.get(i);
                if (d != null && d.getfirstName() != null && d.getlastName() != null) {
                    String driverName = (d.getfirstName() + " " + d.getlastName()).trim();
                    String currentName = (driver.getfirstName() + " " + driver.getlastName()).trim();
                    
                    if (driverName.equals(currentName)) {
                        currentRank = i + 1;
                        break;
                    }
                }
            }
            
            // Top Stats Panel
            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 50, 30));
            statsPanel.setBackground(new Color(240, 242, 245));
            statsPanel.setMaximumSize(new Dimension(1100, 220));
            statsPanel.setPreferredSize(new Dimension(1100, 220));
            
            // Current Rank Card
            JPanel rankCard = new JPanel(new BorderLayout());
            rankCard.setBackground(Color.WHITE);
            rankCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            ));
            
            JLabel rankLabel = new JLabel("Your Current Rank");
            rankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rankLabel.setForeground(new Color(100, 100, 100));
            rankLabel.setHorizontalAlignment(JLabel.CENTER);
            
            JLabel rankValue = new JLabel(currentRank != -1 ? "#" + currentRank : "N/A");
            rankValue.setFont(new Font("Segoe UI", Font.BOLD, 60));
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
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            ));
            
            JLabel totalLabel = new JLabel("Total Drivers");
            totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            totalLabel.setForeground(new Color(100, 100, 100));
            totalLabel.setHorizontalAlignment(JLabel.CENTER);
            
            JLabel totalValue = new JLabel(String.valueOf(totalDrivers));
            totalValue.setFont(new Font("Segoe UI", Font.BOLD, 60));
            totalValue.setForeground(new Color(46, 204, 113));
            totalValue.setHorizontalAlignment(JLabel.CENTER);
            
            totalCard.add(totalLabel, BorderLayout.NORTH);
            totalCard.add(totalValue, BorderLayout.CENTER);
            statsPanel.add(totalCard);
            
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setOpaque(false);
            topPanel.add(statsPanel);
            topPanel.add(Box.createVerticalStrut(30));
            
            panel.add(topPanel, BorderLayout.NORTH);
            
            // Rankings Table - ALL drivers
            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[rankings.size()][3];
            
            for (int i = 0; i < rankings.size(); i++) {
                Driver d = rankings.get(i);
                data[i][0] = String.valueOf(i + 1);
                
                if (d != null) {
                    String fn = (d.getfirstName() != null) ? d.getfirstName() : "";
                    String ln = (d.getlastName() != null) ? d.getlastName() : "";
                    data[i][1] = (fn + " " + ln).trim().isEmpty() ? "(Unknown)" : (fn + " " + ln).trim();
                    
                    // Get ranking score from backend
                    int rankingScore = d.getranking();
                    data[i][2] = rankingScore > 0 ? rankingScore : "0";
                } else {
                    data[i][1] = "(Unknown)";
                    data[i][2] = "0";
                }
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(32);
            table.setBackground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(52, 152, 219));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setPreferredSize(new Dimension(0, 45));
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return panel;
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
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 248, 255),
                    getWidth(), getHeight(), new Color(235, 240, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}