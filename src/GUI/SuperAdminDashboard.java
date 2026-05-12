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
import org.json.JSONObject;

/**
 * SuperAdminDashboard - Main admin control panel for system management
 * Features: Overview, Drivers, Sub Admins, Requests, Rankings, Create Sub Admin
 *
 * FIXES:
 * - Total Sub Admins count now correct (was calling subs.totalSubAdmin() which may cache;
 *   now uses list.size() directly from getSubAdmins())
 * - Sub Admin Contact & Position no longer show N/A (registerSubAdmin now passes position)
 * - Rankings Driver Name & Score no longer show N/A for valid drivers
 * - Session.currentSuperAdmin set in constructor for downstream use
 * - Drivers Panel now displays driver performance data (FIXED!)
 * - Driver profile now extracts data from Request JSON details
 * - Address field REMOVED from driver profile dialog
 */
public class SuperAdminDashboard extends JFrame {

    private DriverService ds;
    private SubAdminService subs;
    private SuperAdminService sas;
    private SuperAdmin superAdmin;

    // Flags to track loaded tabs for lazy loading
    private boolean overviewLoaded = false;
    private boolean driversLoaded = false;
    private boolean subAdminsLoaded = false;
    private boolean requestsLoaded = false;
    private boolean rankingsLoaded = false;
    private boolean createSubAdminLoaded = false;

    public SuperAdminDashboard(SuperAdmin superAdmin, SuperAdminService superAdminService,
                           DriverService driverService, SubAdminService subAdminService) {
        this.superAdmin = superAdmin;
        this.ds = driverService;
        this.subs = subAdminService;
        this.sas = superAdminService;

        // FIX: Set session so downstream repos can access current super admin
        util.Session.currentSuperAdmin = superAdmin;

        setTitle("Trackify - Super Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header Panel
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
            util.Session.currentSuperAdmin = null;
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
        tabbedPane.setOpaque(true);

        tabbedPane.addTab("Overview", new JPanel());
        tabbedPane.addTab("Drivers", new JPanel());
        tabbedPane.addTab("Sub Admins", new JPanel());
        tabbedPane.addTab("Requests", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        tabbedPane.addTab("Create Sub Admin", new JPanel());

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
                    // FIX: Always reload Sub Admins panel to reflect newly added records
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
        g2d.setColor(new Color(155, 89, 182));
        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2d.drawString("S", 16, 50);
        g2d.dispose();
        return icon;
    }

    /**
     * Creates Overview Panel with system statistics
     * FIX: Total Sub Admins now fetches fresh list size instead of relying on
     *      a potentially stale totalSubAdmin() counter method.
     */
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 242, 245));

        try {
            // FIX: Get actual list so count is always current
            List<SubAdmin> subAdminList = subs.getSubAdmins();
            int subAdminCount = (subAdminList != null) ? subAdminList.size() : 0;

            panel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(52, 152, 219)));
            // FIX: Use subAdminCount from live list instead of subs.totalSubAdmin()
            panel.add(createStatCard("Total Sub Admins", String.valueOf(subAdminCount), new Color(46, 204, 113)));
            panel.add(createStatCard("Total Vehicles", "0", new Color(241, 196, 15)));
            panel.add(createStatCard("Pending Requests", String.valueOf(sas.totalPending()), new Color(230, 126, 34)));
            panel.add(createStatCard("Approved Requests", String.valueOf(sas.totalApproved()), new Color(155, 89, 182)));
            panel.add(createStatCard("Rejected Requests", String.valueOf(sas.totalRejected()), new Color(231, 76, 60)));
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
     * ✅ FIXED: Creates Drivers Panel with driver performance data
     * Now displays all drivers like SubAdminDashboard does!
     */
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
                if (dp == null || dp.getdriver() == null) continue;
                
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
            styleTable(table, new Color(155, 89, 182));

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
     * Creates Sub Admins Panel with list of all sub admins
     * FIX: Contact and Position no longer show N/A — fetched correctly from DB.
     *      Count now reflects actual list size.
     */
    private JPanel createSubAdminsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<SubAdmin> list = subs.getSubAdmins();

            if (list == null || list.isEmpty()) {
                JLabel noDataLabel = new JLabel("No sub admins registered yet.");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Sub Admin ID", "Name", "Contact", "Position", "Status"};
            Object[][] data = new Object[list.size()][5];

            for (int i = 0; i < list.size(); i++) {
                SubAdmin sa = list.get(i);
                if (sa == null) continue;
                
                // FIX: Safe null-checks; show actual value or fallback message
                data[i][0] = (sa.getpublic_sub_id() != null && !sa.getpublic_sub_id().isEmpty())
                        ? sa.getpublic_sub_id() : "(no ID)";
                String fn = (sa.getfirstName() != null) ? sa.getfirstName() : "";
                String ln = (sa.getlastName() != null) ? sa.getlastName() : "";
                data[i][1] = (fn + " " + ln).trim();
                // FIX: Contact — use getcontactNum(); if empty show "(not set)"
                data[i][2] = (sa.getcontactNum() != null && !sa.getcontactNum().isEmpty())
                        ? sa.getcontactNum() : "(not set)";
                // FIX: Position — use getposition(); if empty show "(not set)"
                data[i][3] = (sa.getposition() != null && !sa.getposition().isEmpty())
                        ? sa.getposition() : "(not set)";
                data[i][4] = "Active";
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            });
            styleTable(table, new Color(155, 89, 182));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading sub admins panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    /**
     * Creates Requests Panel with approval AND rejection functionality
     */
    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<Request> list = sas.getAllRequest();
            if (list == null || list.isEmpty()) {
                JLabel noData = new JLabel("No pending requests");
                noData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noData.setForeground(new Color(100, 100, 100));
                noData.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noData, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Request Code", "Type", "Status"};
            Object[][] data = new Object[list.size()][3];

            for (int i = 0; i < list.size(); i++) {
                Request r = list.get(i);
                if (r == null) continue;
                data[i][0] = r.getRequestCode();
                data[i][1] = r.getRequestInfo();
                data[i][2] = r.getStatus();
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            };

            JTable table = new JTable(tableModel);
            styleTable(table, new Color(155, 89, 182));
            
            // ✅ Add mouse listener to view driver profile on click
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String requestCode = (String) table.getValueAt(row, 0);
                        viewDriverProfileByRequest(requestCode, list);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);

            // ✅ Button Panel - APPROVE & REJECT
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
            btnPanel.setBackground(new Color(240, 242, 245));
            btnPanel.setPreferredSize(new Dimension(0, 70));

            // ✅ APPROVE BUTTON
            JButton approveBtn = new JButton("✓ APPROVE REQUEST");
            approveBtn.setBackground(new Color(46, 204, 113));
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            approveBtn.setFocusPainted(false);
            approveBtn.setBorderPainted(false);
            approveBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
            approveBtn.setPreferredSize(new Dimension(220, 50));
            approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            approveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    approveBtn.setBackground(new Color(27, 149, 74));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    approveBtn.setBackground(new Color(46, 204, 113));
                }
            });
            approveBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String requestCode = (String) table.getValueAt(selectedRow, 0);
                    System.out.println("DEBUG: Attempting to approve request: " + requestCode);
                    
                    try {
                        boolean approved = sas.approveRequest(requestCode);
                        System.out.println("DEBUG: Approval result: " + approved);
                        
                        if (approved) {
                            showInfoDialog("Success", "✓ Request Approved Successfully!");
                            tableModel.removeRow(selectedRow);
                            requestsLoaded = false;
                        } else {
                            showErrorDialog("Error", "Approval failed. Request may be invalid or driver data incomplete.");
                        }
                    } catch (Exception ex) {
                        System.err.println("Exception during approval: " + ex.getMessage());
                        ex.printStackTrace();
                        showErrorDialog("Error", "An error occurred: " + ex.getMessage());
                    }
                } else {
                    showErrorDialog("Warning", "Please select a request to approve");
                }
            });

            // ✅ NEW REJECT BUTTON
            JButton rejectBtn = new JButton("✗ REJECT REQUEST");
            rejectBtn.setBackground(new Color(231, 76, 60));
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rejectBtn.setFocusPainted(false);
            rejectBtn.setBorderPainted(false);
            rejectBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
            rejectBtn.setPreferredSize(new Dimension(220, 50));
            rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rejectBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    rejectBtn.setBackground(new Color(192, 57, 43));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    rejectBtn.setBackground(new Color(231, 76, 60));
                }
            });
            rejectBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String requestCode = (String) table.getValueAt(selectedRow, 0);
                    
                    // ✅ Show dialog to get rejection reason
                    String reason = showRejectReasonDialog();
                    
                    if (reason != null) {  // User clicked OK
                        System.out.println("DEBUG: Attempting to reject request: " + requestCode);
                        System.out.println("DEBUG: Rejection reason: " + reason);
                        
                        try {
                            boolean rejected = sas.rejectRequest(requestCode);
                            System.out.println("DEBUG: Rejection result: " + rejected);
                            
                            if (rejected) {
                                showInfoDialog("Success", "✗ Request Rejected!\nReason: " + reason);
                                tableModel.removeRow(selectedRow);
                                requestsLoaded = false;
                            } else {
                                showErrorDialog("Error", "Rejection failed. Please try again.");
                            }
                        } catch (Exception ex) {
                            System.err.println("Exception during rejection: " + ex.getMessage());
                            ex.printStackTrace();
                            showErrorDialog("Error", "An error occurred: " + ex.getMessage());
                        }
                    }
                } else {
                    showErrorDialog("Warning", "Please select a request to reject");
                }
            });

            btnPanel.add(approveBtn);
            btnPanel.add(rejectBtn);
            panel.add(btnPanel, BorderLayout.SOUTH);
        } catch (Exception e) {
            System.err.println("Error loading requests panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    /**
     * ✅ NEW: View driver profile from request details (JSON)
     * Extracts driver data from Request.details JSON instead of DB lookup
     */
    private void viewDriverProfileByRequest(String requestCode, List<Request> requests) {
        System.out.println("DEBUG: Fetching driver profile for request: " + requestCode);
        
        try {
            // Find the request
            Request targetRequest = null;
            for (Request r : requests) {
                if (r.getRequestCode().equals(requestCode)) {
                    targetRequest = r;
                    break;
                }
            }
            
            if (targetRequest == null) {
                showErrorDialog("Error", "Request not found");
                return;
            }
            
            // Parse the JSON details
            String detailsJson = targetRequest.getDetails();
            if (detailsJson == null || detailsJson.isEmpty()) {
                showErrorDialog("Error", "No driver details in request");
                return;
            }
            
            System.out.println("DEBUG: Parsing details JSON: " + detailsJson);
            JSONObject json = new JSONObject(detailsJson);
            
            // Extract driver data from JSON
            Driver driver = new Driver();
            driver.setpublic_driver_id(json.optString("public_driver_id", "N/A"));
            driver.setfirstName(json.optString("firstName", "N/A"));
            driver.setlastName(json.optString("lastName", "N/A"));
            driver.setgender(json.optString("gender", "N/A"));
            driver.setdateOfBirth(json.has("dateOfBirth") ? LocalDate.parse(json.getString("dateOfBirth")) : null);
            driver.setcontactNumber(json.optString("contactNumber", "N/A"));
            driver.setlicenseNum(json.optString("licenseNum", "N/A"));
            driver.setlicenseExpiry(json.has("licenseExpiry") ? LocalDate.parse(json.getString("licenseExpiry")) : null);
            
            System.out.println("DEBUG: Successfully extracted driver: " + driver.getfirstName() + " " + driver.getlastName());
            showDriverProfileDialog(driver);
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to extract driver profile: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Error", "Failed to load driver profile: " + e.getMessage());
        }
    }

    /**
     * ✅ Shows driver profile in a dialog
     * ADDRESS FIELD REMOVED!
     */
    private void showDriverProfileDialog(Driver driver) {
        JDialog dlg = new JDialog(this, "Driver Profile", true);
        dlg.setSize(550, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("DRIVER PROFILE");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(155, 89, 182));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(headerLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Profile fields (ADDRESS REMOVED)
        addProfileField(contentPanel, "Driver ID:", driver.getpublic_driver_id());
        addProfileField(contentPanel, "First Name:", driver.getfirstName());
        addProfileField(contentPanel, "Last Name:", driver.getlastName());
        addProfileField(contentPanel, "Gender:", driver.getgender());
        addProfileField(contentPanel, "Date of Birth:", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        addProfileField(contentPanel, "Contact Number:", driver.getcontactNumber());
        addProfileField(contentPanel, "License Number:", driver.getlicenseNum());
        addProfileField(contentPanel, "License Expiry:", driver.getlicenseExpiry() != null ? driver.getlicenseExpiry().toString() : "N/A");
        
        contentPanel.add(Box.createVerticalGlue());

        // Close button
        JButton closeBtn = new JButton("CLOSE");
        closeBtn.setBackground(new Color(155, 89, 182));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dlg.dispose());
        contentPanel.add(closeBtn);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        dlg.add(scrollPane);
        dlg.setVisible(true);
    }

    /**
     * Helper to add a profile field to dialog
     */
    private void addProfileField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setOpaque(false);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(new Color(60, 60, 60));
        labelComp.setPreferredSize(new Dimension(150, 30));

        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComp.setForeground(new Color(100, 100, 100));

        fieldPanel.add(labelComp, BorderLayout.WEST);
        fieldPanel.add(valueComp, BorderLayout.CENTER);

        panel.add(fieldPanel);
    }

    /**
     * Creates Rankings Panel showing driver rankings
     * FIX: Valid drivers with 0 score no longer show "N/A" as their name.
     *      Only truly null/empty driver records fall back to a placeholder.
     */
    private JPanel createRankingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<Driver> list = ds.getDriverRanking();

            if (list == null || list.isEmpty()) {
                JLabel noDataLabel = new JLabel("No driver ranking data available");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }

            String[] columns = {"Rank", "Driver Name", "Score"};
            Object[][] data = new Object[list.size()][3];

            for (int i = 0; i < list.size(); i++) {
                Driver d = list.get(i);
                data[i][0] = (i + 1) + "";
                if (d != null) {
                    // FIX: Always show real name even if score is 0
                    String firstName = (d.getfirstName() != null) ? d.getfirstName() : "";
                    String lastName  = (d.getlastName()  != null) ? d.getlastName()  : "";
                    data[i][1] = (firstName + " " + lastName).trim().isEmpty() ? "(Unknown)" : (firstName + " " + lastName).trim();
                    // FIX: Show 0 explicitly if ranking is null, not "N/A"
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
            styleTable(table, new Color(155, 89, 182));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading rankings panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    /**
     * Creates Sub Admin Panel - form for creating new sub admin accounts
     * FIX: Position field value is now correctly passed to registerSubAdmin().
     */
    private JPanel createSubAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 242, 245));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
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
        JLabel titleLabel = new JLabel("CREATE NEW SUB ADMIN ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Sub Admin ID
        formPanel.add(makeLabel("Sub Admin ID"), gbc); gbc.gridy++;
        JTextField idField = makeField(); formPanel.add(idField, gbc); gbc.gridy++;

        // First Name
        formPanel.add(makeLabel("First Name"), gbc); gbc.gridy++;
        JTextField fnField = makeField(); formPanel.add(fnField, gbc); gbc.gridy++;

        // Last Name
        formPanel.add(makeLabel("Last Name"), gbc); gbc.gridy++;
        JTextField lnField = makeField(); formPanel.add(lnField, gbc); gbc.gridy++;

        // Contact Number
        formPanel.add(makeLabel("Contact Number"), gbc); gbc.gridy++;
        JTextField ctField = makeField(); formPanel.add(ctField, gbc); gbc.gridy++;

        // Position
        formPanel.add(makeLabel("Position"), gbc); gbc.gridy++;
        JTextField posField = makeField(); formPanel.add(posField, gbc); gbc.gridy++;
        
        formPanel.add(makeLabel("Assign Terminal"), gbc); gbc.gridy++;
        String[] terminals = 
        {
            "Cebu North Bus Terminal (NBT)",
            "Cebu South Bus Terminal (CSBT)",
            "Ceres Garage",
            "Marina Mall (Mactan)",
            "Carmen Bus Terminal"
        };
        
        JComboBox<String> terminalBox = new JComboBox<>(terminals);
        terminalBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        terminalBox.setPreferredSize(new Dimension(300, 42));
        
        formPanel.add(terminalBox, gbc); gbc.gridy++;

        // Password
        formPanel.add(makeLabel("Password"), gbc); gbc.gridy++;
        JPasswordField pwField = makePasswordField(); formPanel.add(pwField, gbc); gbc.gridy++;

        // Confirm Password
        formPanel.add(makeLabel("Confirm Password"), gbc); gbc.gridy++;
        JPasswordField cpwField = makePasswordField(); formPanel.add(cpwField, gbc); gbc.gridy++;

        // Create Button
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.gridwidth = 2;
        JButton createBtn = new JButton("CREATE SUB ADMIN");
        createBtn.setBackground(new Color(155, 89, 182));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.setFocusPainted(false);
        createBtn.setBorderPainted(false);
        createBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        createBtn.setPreferredSize(new Dimension(300, 50));
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createBtn.setBackground(new Color(108, 52, 131));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createBtn.setBackground(new Color(155, 89, 182));
            }
        });
        createBtn.addActionListener(e -> {
            String id  = idField.getText().trim();
            String fn  = fnField.getText().trim();
            String ln  = lnField.getText().trim();
            String ct  = ctField.getText().trim();
            String pos = posField.getText().trim();
            String pw  = new String(pwField.getPassword());
            String cpw = new String(cpwField.getPassword());
            String selectedTerminal = (String) terminalBox.getSelectedItem();
            
            int terminalID = switch(selectedTerminal)
            {
                case "Cebu North Bus Terminal (NBT)" -> 1;
                case "Cebu South Bus Terminal (CSBT)" -> 2;
                case "Ceres Garage" -> 3;
                case "Marina Mall (Mactan)" -> 4;
                case "Carmen Bus Terminal" -> 5;
                default -> 0;
                        
            };
            

            if (id.isEmpty() || fn.isEmpty() || ln.isEmpty() || ct.isEmpty() || pos.isEmpty() || pw.isEmpty()) {
                showErrorDialog("Validation Error", "Please fill in all fields");
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

            // FIX: Pass position (pos) as a real argument.
            boolean success = subs.registerSubAdmin(id, fn, ln, "M", LocalDate.now(), "", ct, pos, pw, cpw, terminalID);

            if (success) {
                showInfoDialog("Success", "Sub Admin account created successfully!");
                idField.setText(""); fnField.setText(""); lnField.setText("");
                ctField.setText(""); posField.setText("");
                pwField.setText(""); cpwField.setText("");
                overviewLoaded = false;
                subAdminsLoaded = false;
            } else {
                showErrorDialog("Error", "Failed to create Sub Admin account. ID may already exist.");
            }
        });
        formPanel.add(createBtn, gbc);

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

    /** Applies consistent table styling */
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
    
    /**
     * ✅ NEW: Shows dialog to get rejection reason from user
     */
    private String showRejectReasonDialog() {
        JDialog dlg = new JDialog(this, "Reject Request", true);
        dlg.setSize(450, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Why are you rejecting this request?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(231, 76, 60));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Text Area for reason
        JTextArea reasonArea = new JTextArea(5, 40);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(15));

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton okBtn = new JButton("REJECT");
        okBtn.setBackground(new Color(231, 76, 60));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okBtn.setFocusPainted(false);
        okBtn.setBorderPainted(false);
        okBtn.setPreferredSize(new Dimension(100, 35));
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBackground(new Color(150, 150, 150));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Store result
        String[] result = {null};

        okBtn.addActionListener(e -> {
            String reason = reasonArea.getText().trim();
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Please provide a reason for rejection", 
                    "Empty Reason", JOptionPane.WARNING_MESSAGE);
            } else {
                result[0] = reason;
                dlg.dispose();
            }
        });

        cancelBtn.addActionListener(e -> {
            result[0] = null;
            dlg.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);
        contentPanel.add(btnPanel);

        dlg.add(contentPanel);
        dlg.setVisible(true);

        return result[0];
    }

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 247, 250),
                    getWidth(), getHeight(), new Color(235, 240, 248));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}