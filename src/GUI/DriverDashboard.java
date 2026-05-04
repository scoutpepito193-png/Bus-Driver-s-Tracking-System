package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Service.DriverService;

import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * DriverDashboard - Driver portal for viewing profile, performance, and rankings
 * FIXED: Improved profile layout, fixed rankings, fixed header text cropping
 */
public class DriverDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private Driver driver;
    
    // Lazy loading flags
    private boolean profileLoaded = false;
    private boolean performanceLoaded = false;
    private boolean rankingLoaded = false;

    public DriverDashboard(Driver driver, DriverService driverService) {
        this.driver = driver;
        
        setTitle("Trackify - Driver Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header Panel - FIXED: Increased height to prevent text cropping
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 103, 210)));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        headerPanel.setPreferredSize(new Dimension(0, 110)); // FIXED: Increased from 100 to 110
        
        // Logo - Same as Menu
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
        
        // Tabbed Pane with readable names
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
     * Creates application icon - matches Menu branding
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
     * Creates Profile Panel displaying driver information
     * FIXED: Cleaner layout with better organization and larger text
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        panel.setBackground(new Color(240, 242, 245));

        // ── Avatar + Name banner ──────────────────────────────────────────────
        JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        bannerPanel.setBackground(new Color(52, 152, 219));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Circle avatar with driver initials
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
        fullName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        fullName.setForeground(Color.WHITE);
        JLabel idLabel = new JLabel("ID: " + (driver.getpublic_driver_id() != null ? driver.getpublic_driver_id() : "N/A"));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idLabel.setForeground(new Color(210, 235, 255));
        nameBlock.add(fullName);
        nameBlock.add(Box.createVerticalStrut(4));
        nameBlock.add(idLabel);

        bannerPanel.add(avatar);
        bannerPanel.add(nameBlock);
        panel.add(bannerPanel, BorderLayout.NORTH);

        // ── Details grid ─────────────────────────────────────────────────────
        JPanel profileCard = new JPanel(new GridLayout(0, 2, 50, 18));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
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

        panel.add(profileCard, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Adds a profile information row with label and value - FIXED: Larger, more readable text
     */
    private void addProfileRow(JPanel panel, String label, String value) {
        // Label Panel
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setOpaque(false);
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 16)); // FIXED: Increased from 14 to 16
        labelComponent.setForeground(new Color(52, 152, 219));
        labelPanel.add(labelComponent);
        
        // Value Panel
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valuePanel.setOpaque(false);
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // FIXED: Increased from 14 to 16
        valueComponent.setForeground(new Color(60, 60, 60));
        valuePanel.add(valueComponent);
        
        panel.add(labelPanel);
        panel.add(valuePanel);
    }
    
    /**
     * Creates Performance Panel with driver performance metrics
     */
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
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
     * Creates Rankings Panel with driver rank and top drivers list
     * FIXED: Now properly displays ranking data
     */
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));
        
        try {
            List<Driver> rankings = ds.getDriverRanking();
            
            // Check if rankings list is empty or null
            if (rankings == null || rankings.isEmpty()) {
                JLabel noDataLabel = new JLabel("No ranking data available");
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
                if (d != null && d.getpublic_driver_id() != null && 
                    d.getpublic_driver_id().equals(driver.getpublic_driver_id())) {
                    currentRank = i + 1;
                    break;
                }
            }
            
            // Top Stats Panel
            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 40, 40));
            statsPanel.setBackground(new Color(240, 242, 245));
            statsPanel.setMaximumSize(new Dimension(1000, 220));
            statsPanel.setPreferredSize(new Dimension(1000, 220));
            
            // Current Rank Card - ENHANCED DESIGN
            JPanel rankCard = new JPanel(new BorderLayout());
            rankCard.setBackground(Color.WHITE);
            rankCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            
            JLabel rankLabel = new JLabel("Your Current Rank");
            rankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            rankLabel.setForeground(new Color(100, 100, 100));
            rankLabel.setHorizontalAlignment(JLabel.CENTER);
            
            JLabel rankValue = new JLabel(currentRank != -1 ? "#" + currentRank : "N/A");
            rankValue.setFont(new Font("Segoe UI", Font.BOLD, 72));
            rankValue.setForeground(new Color(52, 152, 219));
            rankValue.setHorizontalAlignment(JLabel.CENTER);
            
            rankCard.add(rankLabel, BorderLayout.NORTH);
            rankCard.add(rankValue, BorderLayout.CENTER);
            statsPanel.add(rankCard);
            
            // Total Drivers Card - ENHANCED DESIGN
            JPanel totalCard = new JPanel(new BorderLayout());
            totalCard.setBackground(Color.WHITE);
            totalCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            
            JLabel totalLabel = new JLabel("Total Drivers");
            totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            totalLabel.setForeground(new Color(100, 100, 100));
            totalLabel.setHorizontalAlignment(JLabel.CENTER);
            
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
            
            // Rankings Table - Top 10 drivers
            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[Math.min(rankings.size(), 10)][3];
            
            for (int i = 0; i < Math.min(rankings.size(), 10); i++) {
                Driver d = rankings.get(i);
                data[i][0] = String.valueOf(i + 1);
                if (d != null) {
                    String fn = (d.getfirstName() != null) ? d.getfirstName() : "";
                    String ln = (d.getlastName()  != null) ? d.getlastName()  : "";
                    data[i][1] = (fn + " " + ln).trim().isEmpty() ? "(Unknown)" : (fn + " " + ln).trim();
                    Object rank = d.getranking();
                    data[i][2] = (rank != null) ? rank : 0;
                } else {
                    data[i][1] = "(Unknown)";
                    data[i][2] = 0;
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