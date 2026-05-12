package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Model.DriverProfile;
import Service.DriverService;
import Service.SalaryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SalaryService ss = new SalaryService();
    private Driver driver;

    private boolean profileLoaded = false;
    private boolean performanceLoaded = false;
    private boolean rankingLoaded = false;

    public DriverDashboard(Driver driver, DriverService driverService) {
        this.driver = driver;

        if (driverService != null) {
            this.ds = driverService;
        }

        loadFullDriverProfile();

        setTitle("Trackify - Driver Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        headerPanel.setPreferredSize(new Dimension(0, 110));

        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("DRIVER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createVerticalStrut(3));
        leftHeader.add(titleLabel);

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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(60, 60, 60));

        tabbedPane.addTab("Profile", createProfilePanel());
        profileLoaded = true;

        tabbedPane.addTab("Performance", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();

            switch (selectedIndex) {
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

                default:
                    break;
            }
        });

        tabbedPane.setSelectedIndex(0);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadFullDriverProfile() {
        try {
            if (driver == null || driver.getpublic_driver_id() == null || driver.getpublic_driver_id().trim().isEmpty()) {
                return;
            }

            DriverProfile fullProfile = ds.getDriverProfile(driver.getpublic_driver_id());

            if (fullProfile != null && fullProfile.getDriver() != null) {
                this.driver = fullProfile.getDriver();
            }
        } catch (Exception e) {
            System.err.println("Error loading full driver profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Image createAppIcon() {
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(
                64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB
        );

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

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panel.setBackground(new Color(240, 242, 245));

        JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        bannerPanel.setBackground(new Color(52, 152, 219));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 70, 70);

                g2.setColor(new Color(52, 152, 219));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));

                String initials = getDriverInitials();

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

        JLabel fullName = new JLabel(getDriverFullName());
        fullName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        fullName.setForeground(Color.WHITE);

        JLabel idLabel = new JLabel("ID: " + safeText(driver.getpublic_driver_id()));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        idLabel.setForeground(new Color(210, 235, 255));

        nameBlock.add(fullName);
        nameBlock.add(Box.createVerticalStrut(2));
        nameBlock.add(idLabel);

        bannerPanel.add(avatar);
        bannerPanel.add(nameBlock);

        panel.add(bannerPanel, BorderLayout.NORTH);

        JPanel profileCard = new JPanel(new GridLayout(0, 2, 50, 18));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(35, 50, 35, 50)
        ));

        addProfileRow(profileCard, "Contact Number", safeText(driver.getcontactNumber()));
        addProfileRow(profileCard, "License Number", safeText(driver.getlicenseNum()));
        addProfileRow(profileCard, "Address", safeText(driver.getaddress()));
        addProfileRow(profileCard, "Gender", safeText(driver.getgender()));
        addProfileRow(profileCard, "Status", driver.getStatus() != null ? driver.getStatus() : "Active");
        addProfileRow(profileCard, "Date of Birth", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        addProfileRow(profileCard, "Monthly Salary", getMonthlySalaryFromBackend());

        panel.add(profileCard, BorderLayout.CENTER);

        return panel;
    }

    private String getMonthlySalaryFromBackend() {
        try {
            String publicDriverId = driver.getpublic_driver_id();

            if (publicDriverId == null || publicDriverId.trim().isEmpty()) {
                return "No data";
            }

            int driverId = ds.getDriverId(publicDriverId);

            if (driverId == -1) {
                return "No data";
            }

            double monthlySalary = ss.getMonthlySalary(driverId);

            if (monthlySalary <= 0) {
                return "Pending";
            }

            return String.format("PHP %.2f", monthlySalary);
        } catch (Exception e) {
            System.err.println("Error retrieving monthly salary: " + e.getMessage());
            return "Error";
        }
    }

    private void addProfileRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(new Color(52, 152, 219));

        JLabel valueComponent = new JLabel(value != null && !value.trim().isEmpty() ? value : "N/A");
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(new Color(60, 60, 60));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

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

            String[] columns = {"Tickets", "Revenue", "KM/L", "Date"};
            Object[][] data = new Object[records.size()][4];

            for (int i = 0; i < records.size(); i++) {
                DriverPerformance dp = records.get(i);

                if (dp == null) {
                    data[i][0] = 0;
                    data[i][1] = "PHP 0.00";
                    data[i][2] = "0.00";
                    data[i][3] = "N/A";
                    continue;
                }

                data[i][0] = dp.gettotalTickets();
                data[i][1] = String.format("PHP %.2f", dp.gettotalRevenue());
                data[i][2] = String.format("%.2f", dp.getaverageKMPL());
                data[i][3] = "Today";
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            styleTable(table);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading performance panel: " + e.getMessage());
            e.printStackTrace();

            JLabel errorLabel = new JLabel("Unable to load performance records.");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setForeground(new Color(231, 76, 60));
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            int totalDrivers = ds.totalDriver();
            List<Driver> rankings = ds.getDriverRanking();

            if (rankings == null || rankings.isEmpty()) {
                JLabel noDataLabel = new JLabel("Rankings will be available once ranking data is calculated");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }

            int currentRank = findCurrentDriverRank(rankings);

            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 50, 30));
            statsPanel.setBackground(new Color(240, 242, 245));
            statsPanel.setPreferredSize(new Dimension(1100, 220));

            statsPanel.add(createRankingStatCard(
                    "Your Current Rank",
                    currentRank != -1 ? "#" + currentRank : "N/A",
                    new Color(52, 152, 219)
            ));

            statsPanel.add(createRankingStatCard(
                    "Total Drivers",
                    String.valueOf(totalDrivers),
                    new Color(46, 204, 113)
            ));

            panel.add(statsPanel, BorderLayout.NORTH);

            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[rankings.size()][3];

            for (int i = 0; i < rankings.size(); i++) {
                Driver d = rankings.get(i);

                data[i][0] = String.valueOf(i + 1);

                if (d != null) {
                    String fn = d.getfirstName() != null ? d.getfirstName() : "";
                    String ln = d.getlastName() != null ? d.getlastName() : "";
                    String name = (fn + " " + ln).trim();

                    data[i][1] = name.isEmpty() ? "(Unknown)" : name;

                    Object score = d.getranking();
                    data[i][2] = score != null ? score : 0;
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

            styleTable(table);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
            e.printStackTrace();

            JLabel errorLabel = new JLabel("Unable to load rankings.");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setForeground(new Color(231, 76, 60));
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createRankingStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 60));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private int findCurrentDriverRank(List<Driver> rankings) {
        String currentPublicId = driver.getpublic_driver_id();
        String currentName = getDriverFullName();

        for (int i = 0; i < rankings.size(); i++) {
            Driver d = rankings.get(i);

            if (d == null) {
                continue;
            }

            String rankedPublicId = d.getpublic_driver_id();

            if (currentPublicId != null && rankedPublicId != null && currentPublicId.equals(rankedPublicId)) {
                return i + 1;
            }

            String rankedName = ((d.getfirstName() != null ? d.getfirstName() : "") + " "
                    + (d.getlastName() != null ? d.getlastName() : "")).trim();

            if (!currentName.isEmpty() && currentName.equalsIgnoreCase(rankedName)) {
                return i + 1;
            }
        }

        return -1;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
    }

    private String getDriverFullName() {
        String firstName = driver.getfirstName() != null ? driver.getfirstName() : "";
        String lastName = driver.getlastName() != null ? driver.getlastName() : "";

        String fullName = (firstName + " " + lastName).trim();

        return fullName.isEmpty() ? "Driver" : fullName;
    }

    private String getDriverInitials() {
        String initials = "";

        if (driver.getfirstName() != null && !driver.getfirstName().isEmpty()) {
            initials += driver.getfirstName().charAt(0);
        }

        if (driver.getlastName() != null && !driver.getlastName().isEmpty()) {
            initials += driver.getlastName().charAt(0);
        }

        return initials.isEmpty() ? "D" : initials.toUpperCase();
    }

    private String safeText(String value) {
        return value != null && !value.trim().isEmpty() ? value : "N/A";
    }

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    new Color(245, 248, 255),
                    getWidth(),
                    getHeight(),
                    new Color(235, 240, 255)
            );

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
