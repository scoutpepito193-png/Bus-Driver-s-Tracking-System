package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Model.SubAdmin;
import Model.DriverProfile;
import Service.DriverService;
import Service.SubAdminService;
import Service.SalaryService;
import util.TraccarAPI;
import Repository.DriverRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * SubAdminDashboard - Fleet and driver management interface for Sub Admins
 * Features: Overview, Drivers, Rankings, Register Driver, Live Traccar Tracking
 */
public class SubAdminDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SubAdminService sas = new SubAdminService();
    private SubAdmin subAdmin;
    private SalaryService sS = new SalaryService(); 

    private boolean overviewLoaded = false;
    private boolean driversLoaded  = false;
    private boolean registerLoaded = false;
    private boolean rankingLoaded  = false;

    public SubAdminDashboard(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;
        util.Session.currentSubAdmin = subAdmin;

        setTitle("Trackify - Sub Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        headerPanel.setPreferredSize(new Dimension(0, 110));

        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("SUB ADMIN DASHBOARD");
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
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(140, 45));
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        logoutBtn.addActionListener(e -> {
            util.Session.currentSubAdmin = null;
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

        tabbedPane.addTab("Overview", new JPanel());
        tabbedPane.addTab("Drivers", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        tabbedPane.addTab("Register Driver", new JPanel());

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    if (!overviewLoaded) {
                        tabbedPane.setComponentAt(0, createOverviewPanel());
                        overviewLoaded = true;
                    }
                    break;
                case 1:
                    tabbedPane.setComponentAt(1, createDriversPanel());
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                    driversLoaded = true;
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

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 242, 245));

        try {
            int totalDrivers = ds.totalDriver();
            panel.add(createStatCard("Total Drivers",   String.valueOf(totalDrivers), new Color(46, 204, 113)));
            panel.add(createStatCard("Active Drivers",  String.valueOf(totalDrivers), new Color(52, 152, 219)));
            panel.add(createStatCard("Pending Approvals", "0",                        new Color(241, 196, 15)));
            panel.add(createStatCard("Total Vehicles",   "0",                         new Color(155, 89, 182)));
        } catch (Exception e) {
            System.err.println("Error loading overview panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);

        return card;
    }

    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<DriverPerformance> list = ds.getPerformance();
            
            if (list == null || list.isEmpty()) {
                JLabel noData = new JLabel("No drivers registered yet.");
                noData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noData.setForeground(new Color(100, 100, 100));
                noData.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noData, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Driver ID", "Driver Name", "Total Tickets", "Revenue", "Average KM/L"};
            Object[][] data = new Object[list.size()][5];

            for (int i = 0; i < list.size(); i++) {
                DriverPerformance dp = list.get(i);
                data[i][0] = (dp.getdriver().getpublic_driver_id() != null && !dp.getdriver().getpublic_driver_id().isEmpty())
                        ? dp.getdriver().getpublic_driver_id() : "(not assigned)";
                data[i][1] = (dp.getdriver().getfirstName() + " " + dp.getdriver().getlastName()).trim();
                data[i][2] = dp.gettotalTickets();
                data[i][3] = "₱ " + dp.gettotalRevenue();
                data[i][4] = dp.getaverageKMPL();
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            });
            styleTable(table, new Color(46, 204, 113));

            final List<DriverPerformance> driverList = list;
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow >= 0 && selectedRow < driverList.size()) {
                            DriverPerformance dp = driverList.get(selectedRow);
                            showDriverProfile(dp.getdriver(), dp);
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(new Color(240, 242, 245));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            
            panel.add(centerPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            buttonPanel.setBackground(new Color(240, 242, 245));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

            JButton recordPerfBtn = new JButton("RECORD PERFORMANCE");
            recordPerfBtn.setBackground(new Color(155, 89, 182));
            recordPerfBtn.setForeground(Color.WHITE);
            recordPerfBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            recordPerfBtn.setFocusPainted(false);
            recordPerfBtn.setBorderPainted(false);
            recordPerfBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            recordPerfBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            recordPerfBtn.setPreferredSize(new Dimension(180, 40));
            recordPerfBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    recordPerfBtn.setBackground(new Color(108, 52, 131));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    recordPerfBtn.setBackground(new Color(155, 89, 182));
                }
            });
            recordPerfBtn.addActionListener(e -> showRecordPerformanceDialog());
            buttonPanel.add(recordPerfBtn);

            JButton sendSalaryBtn = new JButton("SEND SALARY");
            sendSalaryBtn.setBackground(new Color(52, 152, 219));
            sendSalaryBtn.setForeground(Color.WHITE);
            sendSalaryBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            sendSalaryBtn.setFocusPainted(false);
            sendSalaryBtn.setBorderPainted(false);
            sendSalaryBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            sendSalaryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sendSalaryBtn.setPreferredSize(new Dimension(140, 40));
            sendSalaryBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    sendSalaryBtn.setBackground(new Color(41, 128, 185));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    sendSalaryBtn.setBackground(new Color(52, 152, 219));
                }
            });
            sendSalaryBtn.addActionListener(e -> {
                try {
                    sS.processDailySalary();
                    JOptionPane.showMessageDialog(
                        SubAdminDashboard.this,
                        "Salary processed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    System.err.println("Error processing salary: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        SubAdminDashboard.this,
                        "Error processing salary.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            buttonPanel.add(sendSalaryBtn);
            panel.add(buttonPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            System.err.println("Error loading drivers panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<Driver> list = ds.getDriverRanking();

            if (list == null || list.isEmpty()) {
                JLabel noDataLabel = new JLabel("No ranking data available");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[Math.min(list.size(), 15)][3];

            for (int i = 0; i < Math.min(list.size(), 15); i++) {
                Driver d = list.get(i);
                data[i][0] = (i + 1) + "";
                if (d != null) {
                    String firstName = (d.getfirstName() != null) ? d.getfirstName() : "";
                    String lastName  = (d.getlastName()  != null) ? d.getlastName()  : "";
                    data[i][1] = (firstName + " " + lastName).trim();
                    Object rank = d.getranking();
                    data[i][2] = (rank != null) ? rank : 0;
                } else {
                    data[i][1] = "(unknown driver)";
                    data[i][2] = 0;
                }
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            });
            styleTable(table, new Color(46, 204, 113));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading ranking panel: " + e.getMessage());
            e.printStackTrace();
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
            BorderFactory.createEmptyBorder(35, 45, 35, 45)
        ));
        formPanel.setMaximumSize(new Dimension(700, 850));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("REGISTER NEW DRIVER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(46, 204, 113));
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html>Driver registration requests require SuperAdmin approval before account activation.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(241, 196, 15));
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        formPanel.add(makeLabel("Driver ID"), gbc); gbc.gridy++;
        JTextField idField = makeField(); formPanel.add(idField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("First Name"), gbc); gbc.gridy++;
        JTextField fnField = makeField(); formPanel.add(fnField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("Last Name"), gbc); gbc.gridy++;
        JTextField lnField = makeField(); formPanel.add(lnField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("Contact Number"), gbc); gbc.gridy++;
        JTextField ctField = makeField(); formPanel.add(ctField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("License Number"), gbc); gbc.gridy++;
        JTextField licenseField = makeField(); formPanel.add(licenseField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("Initial Password"), gbc); gbc.gridy++;
        JPasswordField pwField = makePasswordField(); formPanel.add(pwField, gbc); gbc.gridy++;

        formPanel.add(makeLabel("Confirm Password"), gbc); gbc.gridy++;
        JPasswordField cpwField = makePasswordField(); formPanel.add(cpwField, gbc); gbc.gridy++;

        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.gridwidth = 2;
        JButton registerBtn = new JButton("SUBMIT FOR APPROVAL");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
            String id      = idField.getText().trim();
            String fn      = fnField.getText().trim();
            String ln      = lnField.getText().trim();
            String ct      = ctField.getText().trim();
            String license = licenseField.getText().trim();
            String pw      = new String(pwField.getPassword());
            String cpw     = new String(cpwField.getPassword());

            if (id.isEmpty() || fn.isEmpty() || ln.isEmpty() || ct.isEmpty() || pw.isEmpty()) {
                showErrorDialog("Validation Error", "Please fill in all required fields");
                return;
            }
            if (!pw.equals(cpw)) {
                showErrorDialog("Validation Error", "Passwords do not match");
                return;
            }
            if (pw.length() < 6) {
                showErrorDialog("Validation Error", "Password must be at least 6 characters");
                return;
            }

            if (util.Session.currentSubAdmin == null) {
                showErrorDialog("Session Error", "Session expired. Please log out and log in again.");
                return;
            }

            try {
                String requestCode = ds.registerDriver(
                        id, fn, ln, "M", LocalDate.now(),
                        "", ct, license, LocalDate.now().plusYears(5), "", pw, cpw);

                if (requestCode != null && !requestCode.isEmpty()) {
                    showApprovalPendingDialog(requestCode);
                    idField.setText(""); fnField.setText(""); lnField.setText("");
                    ctField.setText(""); licenseField.setText("");
                    pwField.setText(""); cpwField.setText("");
                    driversLoaded = false;
                    overviewLoaded = false;
                } else {
                    showErrorDialog("Registration Failed", "Failed to submit driver registration. Request code is null.");
                }
            } catch (Exception ex) {
                System.err.println("Error registering driver: " + ex.getMessage());
                ex.printStackTrace();
                showErrorDialog("Registration Error", "An error occurred: " + ex.getMessage());
            }
        });
        formPanel.add(registerBtn, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // -------------------------------------------------------------------------
    // DRIVER PROFILE WITH LIVE TRACKING
    // -------------------------------------------------------------------------

    private void showDriverProfile(Driver driver, DriverPerformance performance) {
        if (driver == null) {
            JOptionPane.showMessageDialog(
                SubAdminDashboard.this,
                "Unable to load driver profile.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            String publicID = driver.getpublic_driver_id();
            if (publicID != null && !publicID.isEmpty()) {
                DriverProfile fullProfile = ds.getDriverProfile(publicID);
                if (fullProfile != null && fullProfile.getDriver() != null) {
                    driver = fullProfile.getDriver();
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching complete driver profile: " + e.getMessage());
        }

        JDialog profileDialog = new JDialog(this, "Driver Profile - Live Tracking", true);
        profileDialog.setSize(1300, 850);
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setResizable(false);
        profileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainContentPanel = new JPanel(new BorderLayout(15, 15));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContentPanel.setBackground(Color.WHITE);

        // LEFT PANEL - Driver Information
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(350, 800));

        JLabel titleLabel = new JLabel("DRIVER PROFILE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(46, 204, 113));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(15));

        // Personal Information Section
        JLabel personalLabel = new JLabel("Personal Information");
        personalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        personalLabel.setForeground(new Color(52, 152, 219));
        leftPanel.add(personalLabel);
        leftPanel.add(Box.createVerticalStrut(8));

        JPanel personalPanel = new JPanel();
        personalPanel.setLayout(new GridBagLayout());
        personalPanel.setBackground(Color.WHITE);
        personalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridy = 0;

        addProfileRow(personalPanel, gbc, "Driver ID:", driver.getpublic_driver_id() != null ? driver.getpublic_driver_id() : "N/A");
        addProfileRow(personalPanel, gbc, "First Name:", driver.getfirstName() != null ? driver.getfirstName() : "N/A");
        addProfileRow(personalPanel, gbc, "Last Name:", driver.getlastName() != null ? driver.getlastName() : "N/A");
        addProfileRow(personalPanel, gbc, "Gender:", driver.getgender() != null ? driver.getgender() : "N/A");
        addProfileRow(personalPanel, gbc, "Contact:", driver.getcontactNumber() != null ? driver.getcontactNumber() : "N/A");
        addProfileRow(personalPanel, gbc, "Status:", driver.getStatus() != null ? driver.getStatus() : "N/A");

        leftPanel.add(personalPanel);
        leftPanel.add(Box.createVerticalStrut(12));

        // License Information Section
        JLabel licenseLabel = new JLabel("License Information");
        licenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        licenseLabel.setForeground(new Color(155, 89, 182));
        leftPanel.add(licenseLabel);
        leftPanel.add(Box.createVerticalStrut(8));

        JPanel licensePanel = new JPanel();
        licensePanel.setLayout(new GridBagLayout());
        licensePanel.setBackground(Color.WHITE);
        licensePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        gbc.gridy = 0;
        addProfileRow(licensePanel, gbc, "License #:", driver.getlicenseNum() != null ? driver.getlicenseNum() : "N/A");
        addProfileRow(licensePanel, gbc, "DOB:", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        addProfileRow(licensePanel, gbc, "Expiry:", driver.getlicenseExpiry() != null ? driver.getlicenseExpiry().toString() : "N/A");

        leftPanel.add(licensePanel);
        leftPanel.add(Box.createVerticalStrut(12));

        // Performance Metrics Section
        JLabel performanceLabel = new JLabel("Performance Metrics");
        performanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        performanceLabel.setForeground(new Color(241, 196, 15));
        leftPanel.add(performanceLabel);
        leftPanel.add(Box.createVerticalStrut(8));

        JPanel performancePanel = new JPanel();
        performancePanel.setLayout(new GridBagLayout());
        performancePanel.setBackground(Color.WHITE);
        performancePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        gbc.gridy = 0;
        if (performance != null) {
            addProfileRow(performancePanel, gbc, "Tickets:", String.valueOf(performance.gettotalTickets()));
            addProfileRow(performancePanel, gbc, "Revenue:", "₱ " + String.format("%.2f", performance.gettotalRevenue()));
            addProfileRow(performancePanel, gbc, "KM/L:", String.format("%.2f", performance.getaverageKMPL()));
        } else {
            addProfileRow(performancePanel, gbc, "Tickets:", "No data");
            addProfileRow(performancePanel, gbc, "Revenue:", "No data");
            addProfileRow(performancePanel, gbc, "KM/L:", "No data");
        }

        leftPanel.add(performancePanel);
        leftPanel.add(Box.createVerticalGlue());

        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setBorder(null);

        // RIGHT PANEL - Live Tracking Map
        JPanel rightPanel = createLiveTrackingPanel(driver, profileDialog);

        // Main content split
        mainContentPanel.add(leftScrollPane, BorderLayout.WEST);
        mainContentPanel.add(rightPanel, BorderLayout.CENTER);

        // Bottom button panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton closeBtn = new JButton("CLOSE");
        closeBtn.setBackground(new Color(46, 204, 113));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(new Color(27, 149, 74));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(new Color(46, 204, 113));
            }
        });
        closeBtn.addActionListener(ev -> profileDialog.dispose());
        bottomPanel.add(closeBtn);

        mainContentPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.setOpaque(false);
        mainScrollPane.getViewport().setOpaque(false);

        profileDialog.add(mainScrollPane);
        profileDialog.setVisible(true);
    }

    /**
     * Creates a live tracking panel with real-time location updates and control buttons
     */
    private JPanel createLiveTrackingPanel(Driver driver, JDialog parentDialog) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Header
        JLabel headerLabel = new JLabel("🗺️ LIVE TRACKING");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(new Color(46, 204, 113));
        headerLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Map Canvas - Custom Painted
        TrackingMapCanvas mapCanvas = new TrackingMapCanvas();
        contentPanel.add(mapCanvas, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Status info labels
        JLabel statusLabel = new JLabel("🔴 Tracking Inactive");
        JLabel speedLabel = new JLabel("Speed: -- km/h");
        JLabel latLabel = new JLabel("Latitude: --");
        JLabel lonLabel = new JLabel("Longitude: --");
        JLabel timeLabel = new JLabel("Last Update: --");

        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        speedLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        latLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        speedLabel.setForeground(new Color(46, 204, 113));
        statusLabel.setForeground(new Color(60, 60, 60));
        latLabel.setForeground(new Color(100, 100, 100));
        lonLabel.setForeground(new Color(100, 100, 100));
        timeLabel.setForeground(new Color(150, 150, 150));

        // Control Panel - START, STOP, REFRESH buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(240, 242, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Start Tracking Button
        JButton startBtn = new JButton("▶ START TRACKING");
        startBtn.setBackground(new Color(46, 204, 113));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        startBtn.setFocusPainted(false);
        startBtn.setBorderPainted(false);
        startBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setPreferredSize(new Dimension(160, 35));

        // Stop Tracking Button
        JButton stopBtn = new JButton("⏹ STOP TRACKING");
        stopBtn.setBackground(new Color(231, 76, 60));
        stopBtn.setForeground(Color.WHITE);
        stopBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stopBtn.setFocusPainted(false);
        stopBtn.setBorderPainted(false);
        stopBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        stopBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stopBtn.setPreferredSize(new Dimension(160, 35));
        stopBtn.setEnabled(false); // Disabled initially

        // Refresh Button
        JButton refreshBtn = new JButton("🔄 REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 35));

        // Add hover effects for buttons
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (startBtn.isEnabled()) startBtn.setBackground(new Color(27, 149, 74));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(new Color(46, 204, 113));
            }
        });

        stopBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (stopBtn.isEnabled()) stopBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                stopBtn.setBackground(new Color(231, 76, 60));
            }
        });

        refreshBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(52, 152, 219));
            }
        });

        // Timer reference to control tracking
        final Timer[] trackingTimer = {null};

        startBtn.addActionListener(e -> {
            if (trackingTimer[0] != null) {
                trackingTimer[0].cancel();
            }
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            statusLabel.setText("🟡 Starting Tracking...");
            statusLabel.setForeground(new Color(241, 196, 15));
            trackingTimer[0] = new Timer();
            startLiveTracking(driver, mapCanvas, statusLabel, speedLabel, latLabel, lonLabel, timeLabel, parentDialog, trackingTimer[0]);
        });

        stopBtn.addActionListener(e -> {
            if (trackingTimer[0] != null) {
                trackingTimer[0].cancel();
                trackingTimer[0] = null;
            }
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            statusLabel.setText("🔴 Tracking Stopped");
            statusLabel.setForeground(new Color(231, 76, 60));
            speedLabel.setText("Speed: -- km/h");
            latLabel.setText("Latitude: --");
            lonLabel.setText("Longitude: --");
            timeLabel.setText("Last Update: --");
        });

        refreshBtn.addActionListener(e -> {
            mapCanvas.clearHistory();
            mapCanvas.repaint();
        });

        controlPanel.add(startBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(refreshBtn);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(new Color(240, 242, 245));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel statusTextPanel = new JPanel();
        statusTextPanel.setLayout(new BoxLayout(statusTextPanel, BoxLayout.Y_AXIS));
        statusTextPanel.setBackground(new Color(240, 242, 245));
        statusTextPanel.add(statusLabel);
        statusTextPanel.add(speedLabel);
        statusTextPanel.add(latLabel);
        statusTextPanel.add(lonLabel);
        statusTextPanel.add(timeLabel);

        statusPanel.add(statusTextPanel);

        mainPanel.add(controlPanel, BorderLayout.WEST);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Stop timer when dialog closes
        parentDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (trackingTimer[0] != null) {
                    trackingTimer[0].cancel();
                }
            }
        });

        return mainPanel;
    }

    /**
     * Starts live tracking with periodic updates
     */
// In the startLiveTracking method, replace the hard-coded deviceId with:
private void startLiveTracking(
        Driver driver,
        TrackingMapCanvas mapCanvas,
        JLabel statusLabel,
        JLabel speedLabel,
        JLabel latLabel,
        JLabel lonLabel,
        JLabel timeLabel,
        JDialog parentDialog,
        Timer providedTimer) {

    Timer timer = (providedTimer != null) ? providedTimer : new Timer();
    final int[] updateCount = {0};

    TimerTask trackingTask = new TimerTask() {

        @Override
        public void run() {

            if (!parentDialog.isDisplayable()) {
                cancel();
                return;
            }

            try {

                // ==============================
                // 🔥 FIX: ALWAYS FETCH REAL DEVICE ID FROM DB
                // ==============================
                DriverRepo driverRepo = new DriverRepo();

                int driverID = driverRepo.getDriverIdByPublicID(driver.getpublic_driver_id());
                Integer deviceId = driverRepo.getTraccarDeviceId(driverID);

                if (deviceId == null || deviceId <= 0) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("⚠️ No Device ID Assigned");
                        statusLabel.setForeground(new Color(241, 196, 15));
                    });
                    return;
                }

                // ==============================
                // GET POSITION FROM TRACCAR
                // ==============================
                JSONObject position = TraccarAPI.getLatestPosition(deviceId);

                if (position != null) {

                    double latitude = position.optDouble("latitude", 0);
                    double longitude = position.optDouble("longitude", 0);
                    double speed = TraccarAPI.getSpeedKmh(deviceId);
                    boolean overSpeed = TraccarAPI.isOverSpeed(deviceId);

                    long timestamp = position.optLong("fixTime", System.currentTimeMillis());
                    String timeStr = new java.text.SimpleDateFormat("HH:mm:ss")
                            .format(new java.util.Date(timestamp));

                    updateCount[0]++;

                    mapCanvas.updateLocation(latitude, longitude, speed, overSpeed);

                    SwingUtilities.invokeLater(() -> {

                        statusLabel.setText("🟢 Online - Tracking Active");
                        statusLabel.setForeground(new Color(46, 204, 113));

                        speedLabel.setText(
                                "Speed: " + String.format("%.2f", speed) + " km/h"
                                + (overSpeed ? " ⚠️ OVERSPEED" : "")
                        );
                        speedLabel.setForeground(
                                overSpeed ? new Color(231, 76, 60) : new Color(46, 204, 113)
                        );

                        latLabel.setText(String.format("Latitude: %.6f", latitude));
                        lonLabel.setText(String.format("Longitude: %.6f", longitude));
                        timeLabel.setText("Last Update: " + timeStr +
                                " (Updates: " + updateCount[0] + ")");
                    });

                } else {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("🔴 Offline - No GPS Signal");
                        statusLabel.setForeground(new Color(231, 76, 60));
                        speedLabel.setText("Speed: N/A");
                        latLabel.setText("Latitude: --");
                        lonLabel.setText("Longitude: --");
                    });
                }

            } catch (Exception e) {
                System.err.println("Live tracking error: " + e.getMessage());

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("⚠️ Error Fetching Data");
                    statusLabel.setForeground(new Color(241, 196, 15));
                });
            }
        }
    };

    timer.scheduleAtFixedRate(trackingTask, 0, 2000);
}

    // -------------------------------------------------------------------------
    // TRACKING MAP CANVAS - Custom painted map with live marker
    // -------------------------------------------------------------------------

    class TrackingMapCanvas extends JPanel {
        private double currentLatitude = 0;
        private double currentLongitude = 0;
        private double currentSpeed = 0;
        private boolean isOverSpeed = false;
        private java.util.List<LocationPoint> locationHistory = new java.util.ArrayList<>();

        private static final double MAP_CENTER_LAT = 14.5994;
        private static final double MAP_CENTER_LON = 120.9842;

        public void updateLocation(double lat, double lon, double speed, boolean overSpeed) {
            this.currentLatitude = lat;
            this.currentLongitude = lon;
            this.currentSpeed = speed;
            this.isOverSpeed = overSpeed;
            
            locationHistory.add(new LocationPoint(lat, lon, speed, System.currentTimeMillis()));
            
            // Keep only last 50 points
            if (locationHistory.size() > 50) {
                locationHistory.remove(0);
            }
            
            SwingUtilities.invokeLater(this::repaint);
        }

        public void clearHistory() {
            locationHistory.clear();
            currentLatitude = 0;
            currentLongitude = 0;
            currentSpeed = 0;
            isOverSpeed = false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw background
            GradientPaint gradient = new GradientPaint(0, 0, new Color(230, 245, 255),
                    width, height, new Color(200, 230, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);

            // Draw grid
            g2d.setColor(new Color(200, 220, 240));
            g2d.setStroke(new BasicStroke(0.5f));
            for (int i = 0; i < width; i += 50) {
                g2d.drawLine(i, 0, i, height);
            }
            for (int i = 0; i < height; i += 50) {
                g2d.drawLine(0, i, width, i);
            }

            // Draw location history trail
            g2d.setColor(new Color(52, 152, 219, 100));
            g2d.setStroke(new BasicStroke(2f));
            for (int i = 1; i < locationHistory.size(); i++) {
                LocationPoint prev = locationHistory.get(i - 1);
                LocationPoint curr = locationHistory.get(i);
                
                int x1 = latLonToX(prev.longitude, width);
                int y1 = latLonToY(prev.latitude, height);
                int x2 = latLonToX(curr.longitude, width);
                int y2 = latLonToY(curr.latitude, height);
                
                g2d.drawLine(x1, y1, x2, y2);
            }

            // Draw current location marker
            if (currentLatitude != 0 || currentLongitude != 0) {
                int markerX = latLonToX(currentLongitude, width);
                int markerY = latLonToY(currentLatitude, height);

                // Draw marker circle
                Color markerColor = isOverSpeed ? new Color(231, 76, 60) : new Color(46, 204, 113);
                g2d.setColor(markerColor);
                g2d.fillOval(markerX - 12, markerY - 12, 24, 24);

                // Draw border
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawOval(markerX - 12, markerY - 12, 24, 24);

                // Draw speed indicator
                g2d.setColor(new Color(60, 60, 60));
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String speedText = String.format("%.0f", currentSpeed);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = markerX - fm.stringWidth(speedText) / 2;
                int textY = markerY + 5;
                g2d.drawString(speedText, textX, textY);

                // Draw compass needle showing direction
                g2d.setColor(markerColor);
                g2d.setStroke(new BasicStroke(2f));
                double angle = Math.toRadians(45); // Default angle
                int needleLength = 20;
                int needleX = markerX + (int)(needleLength * Math.sin(angle));
                int needleY = markerY - (int)(needleLength * Math.cos(angle));
                g2d.drawLine(markerX, markerY, needleX, needleY);
            }

            // Draw info box
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRoundRect(10, 10, 250, 100, 5, 5);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.drawString("Live Location Tracking", 20, 30);
            
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2d.drawString("Latitude: " + String.format("%.4f", currentLatitude), 20, 50);
            g2d.drawString("Longitude: " + String.format("%.4f", currentLongitude), 20, 70);
            g2d.drawString("Speed: " + String.format("%.2f", currentSpeed) + " km/h", 20, 90);
            g2d.drawString("Status: " + (isOverSpeed ? "⚠️ OVERSPEED" : "✓ NORMAL"), 20, 110);
        }

        private int latLonToX(double lon, int width) {
            return (int) ((lon - MAP_CENTER_LON + 2) / 4 * width);
        }

        private int latLonToY(double lat, int height) {
            return (int) ((MAP_CENTER_LAT - lat + 1.5) / 3 * height);
        }

        class LocationPoint {
            double latitude;
            double longitude;
            double speed;
            long timestamp;

            LocationPoint(double lat, double lon, double speed, long time) {
                this.latitude = lat;
                this.longitude = lon;
                this.speed = speed;
                this.timestamp = time;
            }
        }
    }

    private void addProfileRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComponent.setForeground(new Color(60, 60, 60));
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComponent.setForeground(new Color(100, 100, 100));
        panel.add(valueComponent, gbc);

        gbc.gridy++;
    }

    // -------------------------------------------------------------------------
    // RECORD PERFORMANCE DIALOG
    // -------------------------------------------------------------------------

    private void showRecordPerformanceDialog() {
        JDialog dialog = new JDialog(this, "Record Driver Performance", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("RECORD DRIVER PERFORMANCE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(46, 204, 113));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel driverIdLabel = new JLabel("Driver ID:");
        driverIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(driverIdLabel);
        JTextField driverIdField = new JTextField(20);
        driverIdField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        driverIdField.setMaximumSize(new Dimension(400, 35));
        formPanel.add(driverIdField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel kmplLabel = new JLabel("Average KM/L:");
        kmplLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(kmplLabel);
        JTextField kmplField = new JTextField(20);
        kmplField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        kmplField.setMaximumSize(new Dimension(400, 35));
        formPanel.add(kmplField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel ticketsLabel = new JLabel("Total Tickets:");
        ticketsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(ticketsLabel);
        JTextField ticketsField = new JTextField(20);
        ticketsField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ticketsField.setMaximumSize(new Dimension(400, 35));
        formPanel.add(ticketsField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel revenueLabel = new JLabel("Total Revenue (₱):");
        revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(revenueLabel);
        JTextField revenueField = new JTextField(20);
        revenueField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        revenueField.setMaximumSize(new Dimension(400, 35));
        formPanel.add(revenueField);

        formPanel.add(Box.createVerticalGlue());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("SAVE");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setPreferredSize(new Dimension(100, 40));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            String driverId = driverIdField.getText().trim();
            String kmplStr = kmplField.getText().trim();
            String ticketsStr = ticketsField.getText().trim();
            String revenueStr = revenueField.getText().trim();

            if (driverId.isEmpty() || kmplStr.isEmpty() || ticketsStr.isEmpty() || revenueStr.isEmpty()) {
                showErrorDialog("Validation Error", "Please fill in all fields");
                return;
            }

            try {
                double kmpl = Double.parseDouble(kmplStr);
                int tickets = Integer.parseInt(ticketsStr);
                double revenue = Double.parseDouble(revenueStr);

                if (kmpl < 0 || tickets < 0 || revenue < 0) {
                    showErrorDialog("Validation Error", "Values cannot be negative");
                    return;
                }

                ds.recordDriverPerformance(driverId, kmpl, tickets, revenue);
                showInfoDialog("Success", "Driver performance recorded successfully!");
                dialog.dispose();
                driversLoaded = false;

            } catch (NumberFormatException ex) {
                showErrorDialog("Input Error", "Please enter valid numeric values.");
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
                showErrorDialog("Error", "Failed to record performance: " + ex.getMessage());
            }
        });
        buttonPanel.add(saveBtn);

        JButton clearBtn = new JButton("CLEAR");
        clearBtn.setBackground(new Color(241, 196, 15));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setPreferredSize(new Dimension(100, 40));
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            driverIdField.setText("");
            kmplField.setText("");
            ticketsField.setText("");
            revenueField.setText("");
        });
        buttonPanel.add(clearBtn);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // -------------------------------------------------------------------------
    // HELPER METHODS
    // -------------------------------------------------------------------------

    private void styleTable(JTable table, Color headerColor) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(headerColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JTextField makeField() {
        JTextField f = new JTextField(25);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        f.setPreferredSize(new Dimension(300, 42));
        return f;
    }

    private JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField(25);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        f.setPreferredSize(new Dimension(300, 42));
        return f;
    }

    private void showApprovalPendingDialog(String requestCode) {
        JDialog dialog = new JDialog(this, "Registration Request Submitted", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Driver Registration Submitted");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(46, 204, 113));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        JLabel messageLabel = new JLabel("<html><p>The driver registration request has been submitted and is pending SuperAdmin approval.</p></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(new Color(60, 60, 60));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        JLabel codeLabel = new JLabel("Request Code:");
        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        codeLabel.setForeground(new Color(60, 60, 60));
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(codeLabel);

        JLabel codeValueLabel = new JLabel(requestCode);
        codeValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        codeValueLabel.setForeground(new Color(46, 204, 113));
        codeValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(codeValueLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        JLabel infoLabel = new JLabel("<html><p>Please save this request code. You will be notified once the SuperAdmin approves or rejects the registration.</p></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(infoLabel);
        contentPanel.add(Box.createVerticalGlue());

        JButton okBtn = new JButton("OK");
        okBtn.setBackground(new Color(46, 204, 113));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okBtn.setFocusPainted(false);
        okBtn.setBorderPainted(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(ev -> dialog.dispose());
        contentPanel.add(okBtn);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showErrorDialog(String title, String message) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(400, 180);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("<html>" + message + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalGlue());

        JButton btn = new JButton("OK");
        btn.setBackground(new Color(231, 76, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(80, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(ev -> dlg.dispose());
        p.add(btn);

        dlg.add(p);
        dlg.setVisible(true);
    }

    private void showInfoDialog(String title, String message) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(400, 180);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("<html>" + message + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalGlue());

        JButton btn = new JButton("OK");
        btn.setBackground(new Color(46, 204, 113));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(80, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(ev -> dlg.dispose());
        p.add(btn);

        dlg.add(p);
        dlg.setVisible(true);
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