package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Model.Request;
import Model.SubAdmin;
import Model.SuperAdmin;
import Service.DriverService;
import Service.SubAdminService;
import Service.SuperAdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SuperAdminDashboardPanel extends JPanel {

    private MainFrame mainFrame;

    private DriverService ds;
    private SubAdminService subs;
    private SuperAdminService sas;
    private SuperAdmin superAdmin;

    private boolean overviewLoaded = false;
    private boolean driversLoaded = false;
    private boolean subAdminsLoaded = false;
    private boolean requestsLoaded = false;
    private boolean rankingsLoaded = false;
    private boolean createSubAdminLoaded = false;
    private boolean searchLoaded = false;

    public SuperAdminDashboardPanel(MainFrame mainFrame, SuperAdmin superAdmin) {

        this.mainFrame = mainFrame;
        this.superAdmin = superAdmin;

        this.ds = new DriverService();
        this.subs = new SubAdminService();
        this.sas = new SuperAdminService();

        util.Session.currentSuperAdmin = superAdmin;

        setLayout(new BorderLayout());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // ================= HEADER =================

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        headerPanel.setPreferredSize(new Dimension(0, 110));

        JLabel logoLabel = new JLabel("Trackify");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("SUPER ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JPanel leftHeader = new JPanel();
        leftHeader.setOpaque(false);
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));

        leftHeader.add(logoLabel);
        leftHeader.add(Box.createVerticalStrut(3));
        leftHeader.add(titleLabel);

        JLabel userLabel = new JLabel("Welcome, " + superAdmin.getfirstName() + " " + superAdmin.getlastName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(200, 220, 240));
        leftHeader.add(userLabel);

        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(140, 50));

        logoutBtn.addActionListener(e -> {
            util.Session.currentSuperAdmin = null;
            mainFrame.showLoginPanel();
        });

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ================= TABS =================

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Overview", new JPanel());
        tabbedPane.addTab("Drivers", new JPanel());
        tabbedPane.addTab("Sub Admins", new JPanel());
        tabbedPane.addTab("Requests", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        tabbedPane.addTab("Create Sub Admin", new JPanel());
        tabbedPane.addTab("Search", new JPanel());

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
                    if (!driversLoaded) {
                        tabbedPane.setComponentAt(1, createDriversPanel());
                        driversLoaded = true;
                    }
                    break;

                case 2:
                    tabbedPane.setComponentAt(2, createSubAdminsPanel());
                    subAdminsLoaded = true;
                    break;

                case 3:
                    if (!requestsLoaded) {
                        tabbedPane.setComponentAt(3, createRequestsPanel());
                        requestsLoaded = true;
                    }
                    break;

                case 4:
                    if (!rankingsLoaded) {
                        tabbedPane.setComponentAt(4, createRankingsPanel());
                        rankingsLoaded = true;
                    }
                    break;

                case 5:
                    if (!createSubAdminLoaded) {
                        tabbedPane.setComponentAt(5, createSubAdminPanel());
                        createSubAdminLoaded = true;
                    }
                    break;

                case 6:
                    if (!searchLoaded) {
                        tabbedPane.setComponentAt(6, createSearchPanel());
                        searchLoaded = true;
                    }
                    break;
            }
        });

        tabbedPane.setSelectedIndex(0);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    // ================= OVERVIEW =================

    private JPanel createOverviewPanel() {

        JPanel panel = new JPanel(new GridLayout(2, 3, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 242, 245));

        try {

            List<SubAdmin> subAdminList = subs.getSubAdmins();
            int subAdminCount = subAdminList != null ? subAdminList.size() : 0;

            panel.add(createStatCard(
                    "Total Drivers",
                    String.valueOf(ds.totalDriver()),
                    new Color(52, 152, 219)));

            panel.add(createStatCard(
                    "Total Sub Admins",
                    String.valueOf(subAdminCount),
                    new Color(46, 204, 113)));

            panel.add(createStatCard(
                    "Pending Requests",
                    String.valueOf(sas.totalPending()),
                    new Color(230, 126, 34)));

            List<Driver> topDrivers = ds.getDriverRanking();
            panel.add(createStatCard(
                    "Top Driver",
                    topDrivers != null && !topDrivers.isEmpty() ? 
                        topDrivers.get(0).getfirstName() + " " + topDrivers.get(0).getlastName() : 
                        "N/A",
                    new Color(155, 89, 182)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // ================= DRIVERS =================

    private JPanel createDriversPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {

            List<DriverPerformance> list = ds.getPerformance();

            String[] columns = {
                "Driver ID",
                "Driver Name",
                "Tickets",
                "Revenue",
                "KM/L"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (DriverPerformance dp : list) {

                String name = dp.getdriver().getfirstName()
                        + " "
                        + dp.getdriver().getlastName();

                Object[] row = {
                    dp.getdriver().getpublic_driver_id(),
                    name,
                    dp.gettotalTickets(),
                    String.format("₱%.2f", dp.gettotalRevenue()),
                    String.format("%.2f", dp.getaverageKMPL())
                };

                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            JScrollPane scrollPane = new JScrollPane(table);

            panel.add(new JLabel("Driver Performance List"), BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            panel.add(new JLabel("Error loading drivers: " + e.getMessage()));
        }

        return panel;
    }

    // ================= SUB ADMINS =================

    private JPanel createSubAdminsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {

            List<SubAdmin> list = subs.getSubAdmins();

            String[] columns = {
                "ID",
                "Name",
                "Contact",
                "Position"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (SubAdmin sa : list) {

                Object[] row = {
                    sa.getpublic_sub_id(),
                    sa.getfirstName() + " " + sa.getlastName(),
                    sa.getcontactNum(),
                    sa.getposition()
                };

                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            JScrollPane scrollPane = new JScrollPane(table);

            panel.add(new JLabel("Sub Admin List"), BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            panel.add(new JLabel("Error loading sub admins: " + e.getMessage()));
        }

        return panel;
    }

    // ================= REQUESTS =================

    private JPanel createRequestsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {

            List<Request> list = sas.getAllRequest();

            String[] columns = {
                "Request Code",
                "Type",
                "Status",
                "Action"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 3;
                }
            };

            for (Request r : list) {

                Object[] row = {
                    r.getRequestCode(),
                    r.getRequestInfo(),
                    r.getStatus(),
                    "View"
                };

                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    int col = table.columnAtPoint(evt.getPoint());

                    if (col == 3 && row >= 0) {
                        String requestCode = (String) model.getValueAt(row, 0);
                        showRequestDetails(requestCode);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);

            panel.add(new JLabel("Pending Requests"), BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            panel.add(new JLabel("Error loading requests: " + e.getMessage()));
        }

        return panel;
    }

    private void showRequestDetails(String requestCode) {

        Request req = sas.getRequest(requestCode);

        if (req == null) {
            JOptionPane.showMessageDialog(mainFrame, "Request not found!");
            return;
        }

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String type = req.getRequestInfo();

        if ("DRIVER REGISTRATION".equals(type)) {

            Driver d = (Driver) sas.getReqDetails(requestCode);

            detailsPanel.add(new JLabel("ID: " + d.getpublic_driver_id()));
            detailsPanel.add(new JLabel("Name: " + d.getfirstName() + " " + d.getlastName()));
            detailsPanel.add(new JLabel("Gender: " + d.getgender()));
            detailsPanel.add(new JLabel("Date of Birth: " + d.getdateOfBirth()));
            detailsPanel.add(new JLabel("Address: " + d.getaddress()));
            detailsPanel.add(new JLabel("Contact: " + d.getcontactNumber()));
            detailsPanel.add(new JLabel("License: " + d.getlicenseNum()));
            detailsPanel.add(new JLabel("License Expiry: " + d.getlicenseExpiry()));

        } else if ("REMOVE DRIVER".equals(type)) {

            Map<String, String> data = (Map<String, String>) sas.getReqDetails(requestCode);
            detailsPanel.add(new JLabel("Reason: " + data.get("reason")));
        }

        int result = JOptionPane.showOptionDialog(
                mainFrame,
                new JScrollPane(detailsPanel),
                "Request Details",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Approve", "Reject", "Cancel"},
                "Cancel"
        );

        if (result == 0) {
            boolean approved = sas.approveRequest(requestCode);
            if (approved) {
                JOptionPane.showMessageDialog(mainFrame, "Request Approved!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Approval Failed!");
            }
        } else if (result == 1) {
            boolean rejected = sas.rejectRequest(requestCode);
            if (rejected) {
                JOptionPane.showMessageDialog(mainFrame, "Request Rejected!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Rejection Failed!");
            }
        }
    }

    // ================= RANKINGS =================

    private JPanel createRankingsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {

            List<Driver> list = ds.getDriverRanking();

            String[] columns = {
                "Rank",
                "Driver Name",
                "Driver ID",
                "Score"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (int i = 0; i < list.size(); i++) {

                Driver d = list.get(i);

                Object[] row = {
                    i + 1,
                    d.getfirstName() + " " + d.getlastName(),
                    d.getpublic_driver_id(),
                    d.getranking()
                };

                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            JScrollPane scrollPane = new JScrollPane(table);

            panel.add(new JLabel("Driver Rankings & Leaderboard"), BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            panel.add(new JLabel("Error loading rankings: " + e.getMessage()));
        }

        return panel;
    }

    // ================= CREATE SUB ADMIN =================

    private JPanel createSubAdminPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));

        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setPreferredSize(new Dimension(600, 500));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField(15);
        JTextField fnField = new JTextField(15);
        JTextField lnField = new JTextField(15);
        JTextField genderField = new JTextField(15);
        JTextField dobField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField ctField = new JTextField(15);
        JTextField posField = new JTextField(15);

        JPasswordField pwField = new JPasswordField(15);
        JPasswordField cpwField = new JPasswordField(15);

        form.add(new JLabel("Sub Admin ID:"));
        form.add(idField);

        form.add(new JLabel("First Name:"));
        form.add(fnField);

        form.add(new JLabel("Last Name:"));
        form.add(lnField);

        form.add(new JLabel("Gender:"));
        form.add(genderField);

        form.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        form.add(dobField);

        form.add(new JLabel("Address:"));
        form.add(addressField);

        form.add(new JLabel("Contact:"));
        form.add(ctField);

        form.add(new JLabel("Position:"));
        form.add(posField);

        form.add(new JLabel("Password:"));
        form.add(pwField);

        form.add(new JLabel("Confirm Password:"));
        form.add(cpwField);

        JButton createBtn = new JButton("CREATE SUB ADMIN");
        createBtn.setBackground(new Color(46, 204, 113));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.setFocusPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton resetBtn = new JButton("RESET");
        resetBtn.setBackground(new Color(149, 165, 166));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(createBtn);
        buttonPanel.add(resetBtn);

        createBtn.addActionListener(e -> {

            if (idField.getText().isEmpty() || fnField.getText().isEmpty() || 
                lnField.getText().isEmpty() || ctField.getText().isEmpty() || 
                posField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all fields!");
                return;
            }

            String pw = new String(pwField.getPassword());
            String cpw = new String(cpwField.getPassword());

            if (!pw.equals(cpw)) {
                JOptionPane.showMessageDialog(mainFrame, "Passwords do not match!");
                return;
            }

            try {
                LocalDate dob = LocalDate.parse(dobField.getText());

                boolean success = subs.registerSubAdmin(
                        idField.getText(),
                        fnField.getText(),
                        lnField.getText(),
                        genderField.getText(),
                        dob,
                        addressField.getText(),
                        ctField.getText(),
                        posField.getText(),
                        pw,
                        cpw
                );

                if (success) {
                    JOptionPane.showMessageDialog(mainFrame, "Sub Admin Created Successfully!");
                    idField.setText("");
                    fnField.setText("");
                    lnField.setText("");
                    genderField.setText("");
                    dobField.setText("");
                    addressField.setText("");
                    ctField.setText("");
                    posField.setText("");
                    pwField.setText("");
                    cpwField.setText("");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Failed to create Sub Admin!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid date format! Use YYYY-MM-DD");
            }
        });

        resetBtn.addActionListener(e -> {
            idField.setText("");
            fnField.setText("");
            lnField.setText("");
            genderField.setText("");
            dobField.setText("");
            addressField.setText("");
            ctField.setText("");
            posField.setText("");
            pwField.setText("");
            cpwField.setText("");
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(wrapper, gbc);

        return panel;
    }

    // ================= SEARCH =================

    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 242, 245));

        // Search Options Panel
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        searchOptionsPanel.setBackground(Color.WHITE);

        JLabel searchTypeLabel = new JLabel("Search by:");
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"Driver Name", "Driver ID", "Sub Admin Name", "Sub Admin ID"});

        JTextField searchField = new JTextField(20);

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchOptionsPanel.add(searchTypeLabel);
        searchOptionsPanel.add(searchTypeCombo);
        searchOptionsPanel.add(searchField);
        searchOptionsPanel.add(searchBtn);

        panel.add(searchOptionsPanel, BorderLayout.NORTH);

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        resultsPanel.setBackground(Color.WHITE);

        JScrollPane resultsScrollPane = new JScrollPane();
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        panel.add(resultsPanel, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {

            String searchType = (String) searchTypeCombo.getSelectedItem();
            String query = searchField.getText().trim();

            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a search query!");
                return;
            }

            try {

                if (searchType.equals("Driver Name")) {

                    Driver found = subs.searchDriverByName(query);

                    if (found != null) {
                        displayDriverResult(found, resultsScrollPane);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Driver not found!");
                    }

                } else if (searchType.equals("Driver ID")) {

                    Driver found = subs.searchDriverById(query);

                    if (found != null) {
                        displayDriverResult(found, resultsScrollPane);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Driver not found!");
                    }

                } else if (searchType.equals("Sub Admin Name")) {

                    SubAdmin found = subs.searchSubAdminByName(query);

                    if (found != null) {
                        displaySubAdminResult(found, resultsScrollPane);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Sub Admin not found!");
                    }

                } else if (searchType.equals("Sub Admin ID")) {

                    SubAdmin found = subs.searchSubAdminById(query);

                    if (found != null) {
                        displaySubAdminResult(found, resultsScrollPane);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Sub Admin not found!");
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Search failed: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void displayDriverResult(Driver driver, JScrollPane scrollPane) {

        JPanel resultPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultPanel.setBackground(Color.WHITE);

        resultPanel.add(new JLabel("<html><b>Driver Information</b></html>"));
        resultPanel.add(new JLabel("ID: " + driver.getpublic_driver_id()));
        resultPanel.add(new JLabel("Name: " + driver.getfirstName() + " " + driver.getlastName()));
        resultPanel.add(new JLabel("Gender: " + driver.getgender()));
        resultPanel.add(new JLabel("Date of Birth: " + driver.getdateOfBirth()));
        resultPanel.add(new JLabel("Address: " + driver.getaddress()));
        resultPanel.add(new JLabel("Contact: " + driver.getcontactNumber()));
        resultPanel.add(new JLabel("License No: " + driver.getlicenseNum()));
        resultPanel.add(new JLabel("License Expiry: " + driver.getlicenseExpiry()));

        List<DriverPerformance> records = subs.searchDriverRecords(driver.getpublic_driver_id());

        if (records != null && !records.isEmpty()) {
            resultPanel.add(new JLabel("<html><b>Performance Records</b></html>"));

            for (DriverPerformance dp : records) {
                resultPanel.add(new JLabel("Tickets: " + dp.gettotalTickets() + " | Revenue: ₱" + dp.gettotalRevenue() + " | KM/L: " + dp.getaverageKMPL()));
            }
        }

        scrollPane.setViewportView(resultPanel);
    }

    private void displaySubAdminResult(SubAdmin subAdmin, JScrollPane scrollPane) {

        JPanel resultPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultPanel.setBackground(Color.WHITE);

        resultPanel.add(new JLabel("<html><b>Sub Admin Information</b></html>"));
        resultPanel.add(new JLabel("ID: " + subAdmin.getpublic_sub_id()));
        resultPanel.add(new JLabel("Name: " + subAdmin.getfirstName() + " " + subAdmin.getlastName()));
        resultPanel.add(new JLabel("Gender: " + subAdmin.getgender()));
        resultPanel.add(new JLabel("Date of Birth: " + subAdmin.getdateOfBirth()));
        resultPanel.add(new JLabel("Address: " + subAdmin.getaddress()));
        resultPanel.add(new JLabel("Contact: " + subAdmin.getcontactNum()));
        resultPanel.add(new JLabel("Position: " + subAdmin.getposition()));

        scrollPane.setViewportView(resultPanel);
    }

    // ================= BACKGROUND =================

    class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    new Color(245, 247, 250),
                    getWidth(),
                    getHeight(),
                    new Color(235, 240, 248)
            );

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
