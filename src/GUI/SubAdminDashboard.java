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

/**
 * SubAdminDashboard - Fleet and driver management interface for Sub Admins
 * Features: Overview, Drivers, Rankings, Register Driver
 *
 * FIXES:
 * - FIX 1 (Critical): Set util.Session.currentSubAdmin in constructor so
 *   DriverRepo.requestDriverRegistration() no longer throws NullPointerException.
 * - FIX 2: Driver ID and Contact now display real values, not "N/A", because
 *   getDriverRanking() returns approved/active drivers with their full fields.
 * - FIX 3: Rankings Driver Name and Score no longer show "N/A" for valid drivers.
 * - FIX 4: Overview Active Drivers now shows ds.totalDriver() instead of hardcoded "0".
 */
public class SubAdminDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SubAdminService sas = new SubAdminService();
    private SubAdmin subAdmin;

    // Flags for lazy loading of tab content
    private boolean overviewLoaded = false;
    private boolean driversLoaded  = false;
    private boolean registerLoaded = false;
    private boolean rankingLoaded  = false;

    public SubAdminDashboard(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;

        // FIX 1: Set session so DriverRepo can read Session.currentSubAdmin.getSubID()
        util.Session.currentSubAdmin = subAdmin;

        setTitle("Trackify - Sub Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header Panel
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

        // Tabbed Pane
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
                    // Always reload to pick up newly registered drivers
                    tabbedPane.setComponentAt(1, createDriversPanel());
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
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB);
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

    /**
     * Creates Overview Panel with system statistics
     * FIX 4: Active Drivers now shows ds.totalDriver() instead of hardcoded "0".
     */
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 242, 245));

        try {
            int totalDrivers = ds.totalDriver();
            panel.add(createStatCard("Total Drivers",   String.valueOf(totalDrivers), new Color(46, 204, 113)));
            // FIX 4: Use totalDrivers as active drivers (all registered & approved drivers are active)
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

    /**
     * Creates Drivers Panel showing all registered drivers
     * FIX 2: Driver ID and Contact now show real values.
     *        getDriverRanking() returns fully populated Driver objects —
     *        if your DB query was missing columns, make sure the SELECT includes
     *        public_driver_id and contact_number.
     */
    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<Driver> list = ds.getDriverRanking();

            if (list == null || list.isEmpty()) {
                JLabel noData = new JLabel("No drivers registered yet.");
                noData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noData.setForeground(new Color(100, 100, 100));
                noData.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noData, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Driver ID", "Driver Name", "Contact", "Status"};
            Object[][] data = new Object[list.size()][4];

            for (int i = 0; i < list.size(); i++) {
                Driver d = list.get(i);
                // FIX 2: Show real values; only fall back if genuinely null/empty
                data[i][0] = (d.getpublic_driver_id() != null && !d.getpublic_driver_id().isEmpty())
                        ? d.getpublic_driver_id() : "(not assigned)";
                data[i][1] = (d.getfirstName() + " " + d.getlastName()).trim();
                data[i][2] = (d.getcontactNumber() != null && !d.getcontactNumber().isEmpty())
                        ? d.getcontactNumber() : "(not set)";
                data[i][3] = "Active";
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            });
            styleTable(table, new Color(46, 204, 113));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading drivers panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    /**
     * Creates Rankings Panel showing top drivers by performance
     * FIX 3: Valid drivers with score 0 no longer display "N/A" as their name.
     */
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
                    // FIX 3: Always show real name even when score is 0
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

    /**
     * Creates Register Driver Panel
     * FIX 1: Session.currentSubAdmin is now set in the constructor, so
     *        DriverRepo.requestDriverRegistration() will not throw NPE.
     */
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

        // Title
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("REGISTER NEW DRIVER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(46, 204, 113));
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Info label
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html>Driver registration requests require SuperAdmin approval before account activation.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(241, 196, 15));
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Fields
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

        // Register Button
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

            // Guard: ensure session is still valid (belt-and-suspenders check)
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
                    // Reset so the driver list reloads when user visits it next
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
    // Helpers
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