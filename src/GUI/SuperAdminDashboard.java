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
import org.json.JSONObject;

public class SuperAdminDashboard extends JFrame {

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

    public SuperAdminDashboard(SuperAdmin superAdmin, SuperAdminService superAdminService,
                               DriverService driverService, SubAdminService subAdminService) {
        this.superAdmin = superAdmin;
        this.ds = driverService;
        this.subs = subAdminService;
        this.sas = superAdminService;

        util.Session.currentSuperAdmin = superAdmin;

        setTitle("Trackify - Super Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(createAppIcon());

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(60, 60, 60));
        tabbedPane.setOpaque(true);

        tabbedPane.addTab("Overview", createOverviewPanel());
        overviewLoaded = true;

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
                default:
                    break;
            }
        });

        tabbedPane.setSelectedIndex(0);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private Image createAppIcon() {
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(
                64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB
        );

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

    private String getTerminalName(int terminalID) {
        return switch (terminalID) {
            case 1 -> "Cebu North Bus Terminal (NBT)";
            case 2 -> "Cebu South Bus Terminal (CSBT)";
            case 3 -> "Ceres Garage";
            case 4 -> "Marina Mall (Mactan)";
            case 5 -> "Carmen Bus Terminal";
            default -> "N/A";
        };
    }

    private JPanel createOverviewPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));
        panel.setBackground(new Color(240, 242, 245));

        try {
            List<SubAdmin> subAdminList = subs.getSubAdmins();
            int subAdminCount = (subAdminList != null) ? subAdminList.size() : 0;

            JPanel statsPanel = new JPanel(new GridLayout(1, 5, 20, 20));
            statsPanel.setOpaque(false);
            statsPanel.setPreferredSize(new Dimension(0, 180));

            statsPanel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(52, 152, 219)));
            statsPanel.add(createStatCard("Total Sub Admins", String.valueOf(subAdminCount), new Color(46, 204, 113)));
            statsPanel.add(createStatCard("Pending Requests", String.valueOf(sas.totalPending()), new Color(230, 126, 34)));
            statsPanel.add(createStatCard("Approved Requests", String.valueOf(sas.totalApproved()), new Color(155, 89, 182)));
            statsPanel.add(createStatCard("Rejected Requests", String.valueOf(sas.totalRejected()), new Color(231, 76, 60)));

            panel.add(statsPanel, BorderLayout.NORTH);

            JPanel rankingPanel = new JPanel(new BorderLayout());
            rankingPanel.setBackground(Color.WHITE);
            rankingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            JLabel titleLabel = new JLabel("Top Drivers");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(155, 89, 182));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            rankingPanel.add(titleLabel, BorderLayout.NORTH);

            ds.updateRanking();
            List<Driver> driverRankings = ds.getDriverRanking();

            if (driverRankings == null || driverRankings.isEmpty()) {
                JLabel noDataLabel = new JLabel("No driver ranking data available");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                rankingPanel.add(noDataLabel, BorderLayout.CENTER);
            } else {
                int rowCount = Math.min(driverRankings.size(), 10);
                String[] columns = {"Rank", "Driver Name", "Score"};
                Object[][] data = new Object[rowCount][3];

                for (int i = 0; i < rowCount; i++) {
                    Driver d = driverRankings.get(i);
                    data[i][0] = String.valueOf(i + 1);

                    if (d != null) {
                        String firstName = d.getfirstName() != null ? d.getfirstName() : "";
                        String lastName = d.getlastName() != null ? d.getlastName() : "";
                        String fullName = (firstName + " " + lastName).trim();

                        data[i][1] = fullName.isEmpty() ? "(unknown driver)" : fullName;
                        data[i][2] = d.getranking();
                    } else {
                        data[i][1] = "(unknown driver)";
                        data[i][2] = 0;
                    }
                }

                JTable table = new JTable(new DefaultTableModel(data, columns) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });

                styleTable(table, new Color(155, 89, 182));

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                rankingPanel.add(scrollPane, BorderLayout.CENTER);
            }

            panel.add(rankingPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading overview panel: " + e.getMessage());
            e.printStackTrace();

            JLabel errorLabel = new JLabel("Unable to load overview data.");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setForeground(new Color(231, 76, 60));
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(18, 16, 18, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(80, 80, 80));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }



    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            Map<String, List<DriverPerformance>> driversByTerminal = ds.getAllDriverPerformanceByTerminal();

            if (driversByTerminal == null || driversByTerminal.isEmpty()) {
                JLabel noData = new JLabel("No drivers registered yet.");
                noData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noData.setForeground(new Color(100, 100, 100));
                noData.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noData, BorderLayout.CENTER);
                return panel;
            }

            JPanel terminalContainer = new JPanel();
            terminalContainer.setLayout(new BoxLayout(terminalContainer, BoxLayout.Y_AXIS));
            terminalContainer.setBackground(new Color(240, 242, 245));

            for (Map.Entry<String, List<DriverPerformance>> entry : driversByTerminal.entrySet()) {
                JPanel terminalBox = createTerminalDriverBox(entry.getKey(), entry.getValue());
                terminalContainer.add(terminalBox);
                terminalContainer.add(Box.createVerticalStrut(20));
            }

            JScrollPane mainScrollPane = new JScrollPane(terminalContainer);
            mainScrollPane.setBorder(null);
            mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            panel.add(mainScrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading drivers panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel createTerminalDriverBox(String terminalName, List<DriverPerformance> driverList) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(terminalName + " (" + driverList.size() + " drivers)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        box.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Driver ID", "Driver Name", "Status", "Total Tickets", "Revenue", "Average KM/L"};
        Object[][] data = new Object[driverList.size()][6];

        for (int i = 0; i < driverList.size(); i++) {
            DriverPerformance dp = driverList.get(i);

            if (dp == null || dp.getdriver() == null) {
                data[i][0] = "(not assigned)";
                data[i][1] = "(unknown driver)";
                data[i][2] = "N/A";
                data[i][3] = 0;
                data[i][4] = "PHP 0.00";
                data[i][5] = "0.00";
                continue;
            }

            Driver driver = dp.getdriver();

            data[i][0] = driver.getpublic_driver_id() != null && !driver.getpublic_driver_id().isEmpty()
                    ? driver.getpublic_driver_id()
                    : "(not assigned)";

            String firstName = driver.getfirstName() != null ? driver.getfirstName() : "";
            String lastName = driver.getlastName() != null ? driver.getlastName() : "";
            String fullName = (firstName + " " + lastName).trim();

            data[i][1] = fullName.isEmpty() ? "(unknown driver)" : fullName;
            data[i][2] = driver.getStatus() != null ? driver.getStatus() : "N/A";
            data[i][3] = dp.gettotalTickets();
            data[i][4] = "PHP " + String.format("%.2f", dp.gettotalRevenue());
            data[i][5] = String.format("%.2f", dp.getaverageKMPL());
        }

        JTable table = new JTable(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        styleTable(table, new Color(155, 89, 182));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(0, Math.min(260, 55 + driverList.size() * 32)));

        box.add(scrollPane, BorderLayout.CENTER);

        return box;
    }

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

            String[] columns = {"Sub Admin ID", "Name", "Contact", "Position", "Terminal", "Status"};
            Object[][] data = new Object[list.size()][6];

            for (int i = 0; i < list.size(); i++) {
                SubAdmin sa = list.get(i);

                if (sa == null) {
                    continue;
                }

                String fn = (sa.getfirstName() != null) ? sa.getfirstName() : "";
                String ln = (sa.getlastName() != null) ? sa.getlastName() : "";

                data[i][0] = (sa.getpublic_sub_id() != null && !sa.getpublic_sub_id().isEmpty())
                        ? sa.getpublic_sub_id()
                        : "(no ID)";
                data[i][1] = (fn + " " + ln).trim();
                data[i][2] = (sa.getcontactNum() != null && !sa.getcontactNum().isEmpty())
                        ? sa.getcontactNum()
                        : "(not set)";
                data[i][3] = (sa.getposition() != null && !sa.getposition().isEmpty())
                        ? sa.getposition()
                        : "(not set)";
                data[i][4] = getTerminalName(sa.getTerminalID());
                data[i][5] = "Active";
            }

            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            styleTable(table, new Color(155, 89, 182));

            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int row = table.rowAtPoint(evt.getPoint());

                        if (row >= 0 && row < list.size()) {
                            String publicSubId = (String) table.getValueAt(row, 0);

                            SubAdmin fullSubAdmin = subs.searchSubAdminById(publicSubId);

                            if (fullSubAdmin != null) {
                                showSubAdminProfileDialog(fullSubAdmin);
                            } else {
                                showErrorDialog("Error", "Sub Admin profile not found.");
                            }
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading sub admins panel: " + e.getMessage());
            e.printStackTrace();
        }

        return panel;
    }

    private void showSubAdminProfileDialog(SubAdmin subAdmin) {
        JDialog dlg = new JDialog(this, "Sub Admin Profile", true);
        dlg.setSize(550, 550);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("SUB ADMIN PROFILE");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(155, 89, 182));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(headerLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        int terminalID = subAdmin.getTerminalID();

        addProfileField(contentPanel, "Sub Admin ID:", subAdmin.getpublic_sub_id() != null ? subAdmin.getpublic_sub_id() : "N/A");
        addProfileField(contentPanel, "First Name:", subAdmin.getfirstName() != null ? subAdmin.getfirstName() : "N/A");
        addProfileField(contentPanel, "Last Name:", subAdmin.getlastName() != null ? subAdmin.getlastName() : "N/A");
        addProfileField(contentPanel, "Gender:", subAdmin.getgender() != null ? subAdmin.getgender() : "N/A");
        addProfileField(contentPanel, "Date of Birth:", subAdmin.getdateOfBirth() != null ? subAdmin.getdateOfBirth().toString() : "N/A");
        addProfileField(contentPanel, "Contact Number:", subAdmin.getcontactNum() != null ? subAdmin.getcontactNum() : "N/A");
        addProfileField(contentPanel, "Position:", subAdmin.getposition() != null ? subAdmin.getposition() : "N/A");
        addProfileField(contentPanel, "Terminal ID:", terminalID > 0 ? String.valueOf(terminalID) : "N/A");
        addProfileField(contentPanel, "Assigned Terminal:", getTerminalName(terminalID));

        contentPanel.add(Box.createVerticalGlue());

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

                if (r == null) {
                    continue;
                }

                data[i][0] = r.getRequestCode();
                data[i][1] = r.getRequestInfo();
                data[i][2] = r.getStatus();
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(tableModel);
            styleTable(table, new Color(155, 89, 182));

            table.addMouseListener(new java.awt.event.MouseAdapter() {
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

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
            btnPanel.setBackground(new Color(240, 242, 245));
            btnPanel.setPreferredSize(new Dimension(0, 70));

            JButton approveBtn = new JButton("APPROVE REQUEST");
            approveBtn.setBackground(new Color(46, 204, 113));
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            approveBtn.setFocusPainted(false);
            approveBtn.setBorderPainted(false);
            approveBtn.setPreferredSize(new Dimension(220, 50));
            approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            approveBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();

                if (selectedRow >= 0) {
                    String requestCode = (String) table.getValueAt(selectedRow, 0);

                    try {
                        boolean approved = sas.approveRequest(requestCode);

                        if (approved) {
                            showInfoDialog("Success", "Request Approved Successfully!");
                            tableModel.removeRow(selectedRow);
                            requestsLoaded = false;
                            overviewLoaded = false;
                            driversLoaded = false;
                        } else {
                            showErrorDialog("Error", "Approval failed. Request may be invalid or driver data incomplete.");
                        }
                    } catch (Exception ex) {
                        showErrorDialog("Error", "An error occurred: " + ex.getMessage());
                    }
                } else {
                    showErrorDialog("Warning", "Please select a request to approve");
                }
            });

            JButton rejectBtn = new JButton("REJECT REQUEST");
            rejectBtn.setBackground(new Color(231, 76, 60));
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rejectBtn.setFocusPainted(false);
            rejectBtn.setBorderPainted(false);
            rejectBtn.setPreferredSize(new Dimension(220, 50));
            rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            rejectBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();

                if (selectedRow >= 0) {
                    String requestCode = (String) table.getValueAt(selectedRow, 0);
                    String reason = showRejectReasonDialog();

                    if (reason != null) {
                        try {
                            boolean rejected = sas.rejectRequest(requestCode);

                            if (rejected) {
                                showInfoDialog("Success", "Request Rejected!<br>Reason: " + reason);
                                tableModel.removeRow(selectedRow);
                                requestsLoaded = false;
                                overviewLoaded = false;
                            } else {
                                showErrorDialog("Error", "Rejection failed. Please try again.");
                            }
                        } catch (Exception ex) {
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

    private void viewDriverProfileByRequest(String requestCode, List<Request> requests) {
        try {
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

            String detailsJson = targetRequest.getDetails();

            if (detailsJson == null || detailsJson.isEmpty()) {
                showErrorDialog("Error", "No driver details in request");
                return;
            }

            JSONObject json = new JSONObject(detailsJson);

            Driver driver = new Driver();
            driver.setpublic_driver_id(json.optString("public_driver_id", "N/A"));
            driver.setfirstName(json.optString("firstName", "N/A"));
            driver.setlastName(json.optString("lastName", "N/A"));
            driver.setgender(json.optString("gender", "N/A"));
            driver.setdateOfBirth(json.has("dateOfBirth") ? LocalDate.parse(json.getString("dateOfBirth")) : null);
            driver.setcontactNumber(json.optString("contactNumber", "N/A"));
            driver.setlicenseNum(json.optString("licenseNum", "N/A"));
            driver.setlicenseExpiry(json.has("licenseExpiry") ? LocalDate.parse(json.getString("licenseExpiry")) : null);

            showDriverProfileDialog(driver);
        } catch (Exception e) {
            showErrorDialog("Error", "Failed to load driver profile: " + e.getMessage());
        }
    }

    private void showDriverProfileDialog(Driver driver) {
        JDialog dlg = new JDialog(this, "Driver Profile", true);
        dlg.setSize(550, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("DRIVER PROFILE");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(155, 89, 182));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(headerLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        addProfileField(contentPanel, "Driver ID:", driver.getpublic_driver_id());
        addProfileField(contentPanel, "First Name:", driver.getfirstName());
        addProfileField(contentPanel, "Last Name:", driver.getlastName());
        addProfileField(contentPanel, "Gender:", driver.getgender());
        addProfileField(contentPanel, "Date of Birth:", driver.getdateOfBirth() != null ? driver.getdateOfBirth().toString() : "N/A");
        addProfileField(contentPanel, "Contact Number:", driver.getcontactNumber());
        addProfileField(contentPanel, "License Number:", driver.getlicenseNum());
        addProfileField(contentPanel, "License Expiry:", driver.getlicenseExpiry() != null ? driver.getlicenseExpiry().toString() : "N/A");

        contentPanel.add(Box.createVerticalGlue());

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

    private JPanel createRankingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 242, 245));

        try {
            ds.updateRankingByTerminal();

            Map<String, List<Driver>> rankingsByTerminal = ds.getDriverRankingByTerminal();

            if (rankingsByTerminal == null || rankingsByTerminal.isEmpty()) {
                JLabel noDataLabel = new JLabel("No driver ranking data available");
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setForeground(new Color(100, 100, 100));
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(noDataLabel, BorderLayout.CENTER);
                return panel;
            }

            JPanel rankingContainer = new JPanel();
            rankingContainer.setLayout(new BoxLayout(rankingContainer, BoxLayout.Y_AXIS));
            rankingContainer.setBackground(new Color(240, 242, 245));

            for (Map.Entry<String, List<Driver>> entry : rankingsByTerminal.entrySet()) {
                String terminalName = entry.getKey();
                List<Driver> driverList = entry.getValue();

                JPanel terminalRankingBox = createTerminalRankingBox(terminalName, driverList);
                rankingContainer.add(terminalRankingBox);
                rankingContainer.add(Box.createVerticalStrut(20));
            }

            JScrollPane scrollPane = new JScrollPane(rankingContainer);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading rankings panel: " + e.getMessage());
            e.printStackTrace();

            JLabel errorLabel = new JLabel("Unable to load rankings.");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setForeground(new Color(231, 76, 60));
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createTerminalRankingBox(String terminalName, List<Driver> driverList) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(terminalName + " Rankings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        box.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Rank", "Driver ID", "Driver Name"};
        Object[][] data = new Object[driverList.size()][3];

        for (int i = 0; i < driverList.size(); i++) {
            Driver d = driverList.get(i);

            if (d != null) {
                String firstName = d.getfirstName() != null ? d.getfirstName() : "";
                String lastName = d.getlastName() != null ? d.getlastName() : "";
                String fullName = (firstName + " " + lastName).trim();

                data[i][0] = d.getranking();
                data[i][1] = d.getpublic_driver_id() != null ? d.getpublic_driver_id() : "(no ID)";
                data[i][2] = fullName.isEmpty() ? "(unknown driver)" : fullName;
            } else {
                data[i][0] = i + 1;
                data[i][1] = "(no ID)";
                data[i][2] = "(unknown driver)";
            }
        }

        JTable table = new JTable(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        styleTable(table, new Color(155, 89, 182));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(0, Math.min(260, 55 + driverList.size() * 32)));

        box.add(scrollPane, BorderLayout.CENTER);

        return box;
    }



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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("CREATE NEW SUB ADMIN ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        formPanel.add(makeLabel("Sub Admin ID"), gbc);
        gbc.gridy++;
        JTextField idField = makeField();
        formPanel.add(idField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("First Name"), gbc);
        gbc.gridy++;
        JTextField fnField = makeField();
        formPanel.add(fnField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Last Name"), gbc);
        gbc.gridy++;
        JTextField lnField = makeField();
        formPanel.add(lnField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Contact Number"), gbc);
        gbc.gridy++;
        JTextField ctField = makeField();
        formPanel.add(ctField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Position"), gbc);
        gbc.gridy++;
        JTextField posField = makeField();
        formPanel.add(posField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Assign Terminal"), gbc);
        gbc.gridy++;

        String[] terminals = {
                "Cebu North Bus Terminal (NBT)",
                "Cebu South Bus Terminal (CSBT)",
                "Ceres Garage",
                "Marina Mall (Mactan)",
                "Carmen Bus Terminal"
        };

        JComboBox<String> terminalBox = new JComboBox<>(terminals);
        terminalBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        terminalBox.setPreferredSize(new Dimension(300, 42));
        formPanel.add(terminalBox, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Password"), gbc);
        gbc.gridy++;
        JPasswordField pwField = makePasswordField();
        formPanel.add(pwField, gbc);
        gbc.gridy++;

        formPanel.add(makeLabel("Confirm Password"), gbc);
        gbc.gridy++;
        JPasswordField cpwField = makePasswordField();
        formPanel.add(cpwField, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.gridwidth = 2;

        JButton createBtn = new JButton("CREATE SUB ADMIN");
        createBtn.setBackground(new Color(155, 89, 182));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.setFocusPainted(false);
        createBtn.setBorderPainted(false);
        createBtn.setPreferredSize(new Dimension(300, 50));
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String fn = fnField.getText().trim();
            String ln = lnField.getText().trim();
            String ct = ctField.getText().trim();
            String pos = posField.getText().trim();
            String pw = new String(pwField.getPassword());
            String cpw = new String(cpwField.getPassword());
            String selectedTerminal = (String) terminalBox.getSelectedItem();

            int terminalID = switch (selectedTerminal) {
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

            boolean success = subs.registerSubAdmin(
                    id, fn, ln, "M", LocalDate.now(), "", ct, pos, pw, cpw, terminalID
            );

            if (success) {
                showInfoDialog("Success", "Sub Admin account created successfully!");

                idField.setText("");
                fnField.setText("");
                lnField.setText("");
                ctField.setText("");
                posField.setText("");
                pwField.setText("");
                cpwField.setText("");

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
        btn.addActionListener(ev -> dlg.dispose());

        p.add(btn);

        dlg.add(p);
        dlg.setVisible(true);
    }

    private String showRejectReasonDialog() {
        JDialog dlg = new JDialog(this, "Reject Request", true);
        dlg.setSize(450, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Why are you rejecting this request?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(231, 76, 60));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        JTextArea reasonArea = new JTextArea(5, 40);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = new JButton("CANCEL");
        JButton okBtn = new JButton("REJECT");

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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
