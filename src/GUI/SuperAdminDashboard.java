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
import java.util.List;

public class SuperAdminDashboard extends JFrame {

    private DriverService ds = new DriverService();
    private SubAdminService subs = new SubAdminService();
    private SuperAdminService sas = new SuperAdminService();
    private SuperAdmin superAdmin;
    
    // Track which tabs have been loaded
    private boolean overviewLoaded = false;
    private boolean driversLoaded = false;
    private boolean subAdminsLoaded = false;
    private boolean requestsLoaded = false;
    private boolean rankingsLoaded = false;

    public SuperAdminDashboard(SuperAdmin superAdmin, SuperAdminService superAdminService,
                           DriverService driverService, SubAdminService subAdminService) {
        this.superAdmin = superAdmin;
        
        setTitle("Super Admin Dashboard");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Header (IMPROVED - Removed welcome message)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("SUPER ADMIN DASHBOARD");
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
        tabbedPane.addTab("Overview", new JPanel());
        tabbedPane.addTab("Drivers", new JPanel());
        tabbedPane.addTab("Sub Admins", new JPanel());
        tabbedPane.addTab("Requests", new JPanel());
        tabbedPane.addTab("Rankings", new JPanel());
        
        // Add listener to load tab content only when clicked
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            switch(selectedIndex) {
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
                    if (!subAdminsLoaded) {
                        tabbedPane.setComponentAt(2, createSubAdminsPanel());
                        subAdminsLoaded = true;
                    }
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
            }
        });
        
        // Trigger first tab load
        tabbedPane.setSelectedIndex(0);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        try {
            panel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(52, 152, 219)));
            panel.add(createStatCard("Total Sub Admins", String.valueOf(subs.totalSubAdmin()), new Color(46, 204, 113)));
            panel.add(createStatCard("Total Vehicles", "0", new Color(241, 196, 15)));
            panel.add(createStatCard("Pending Requests", String.valueOf(sas.totalPending()), new Color(230, 126, 34)));
            panel.add(createStatCard("Approved Requests", String.valueOf(sas.totalApproved()), new Color(155, 89, 182)));
            panel.add(createStatCard("Rejected Requests", String.valueOf(sas.totalRejected()), new Color(231, 76, 60)));
        } catch (Exception e) {
            System.err.println("Error loading overview panel: " + e.getMessage());
            panel.add(new JLabel("Error loading overview data"));
        }
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            List<DriverPerformance> list = ds.getPerformance();
            String[] columns = {"Driver ID", "Name", "Tickets", "Revenue", "KM/L"};
            Object[][] data = new Object[list.size()][5];
            
            for (int i = 0; i < list.size(); i++) {
                DriverPerformance dp = list.get(i);
                data[i][0] = dp.getdriver().getpublic_driver_id();
                data[i][1] = dp.getdriver().getfirstName() + " " + dp.getdriver().getlastName();
                data[i][2] = dp.gettotalTickets();
                data[i][3] = String.format("%.2f", dp.gettotalRevenue());
                data[i][4] = String.format("%.2f", dp.getaverageKMPL());
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(155, 89, 182));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading drivers panel: " + e.getMessage());
            panel.add(new JLabel("Error loading drivers data"));
        }
        
        return panel;
    }
    
    private JPanel createSubAdminsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            List<SubAdmin> list = subs.getSubAdmins();
            String[] columns = {"Sub Admin ID", "Name", "Contact", "Position"};
            Object[][] data = new Object[list.size()][4];
            
            for (int i = 0; i < list.size(); i++) {
                SubAdmin sa = list.get(i);
                data[i][0] = sa.getpublic_sub_id();
                data[i][1] = sa.getfirstName() + " " + sa.getlastName();
                data[i][2] = sa.getcontactNum();
                data[i][3] = sa.getposition() != null ? sa.getposition() : "N/A";
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(155, 89, 182));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading sub admins panel: " + e.getMessage());
            panel.add(new JLabel("Error loading sub admins data"));
        }
        
        return panel;
    }
    
    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            List<Request> list = sas.getAllRequest();
            String[] columns = {"Request Code", "Type", "Status"};
            Object[][] data = new Object[list.size()][3];
            
            for (int i = 0; i < list.size(); i++) {
                Request r = list.get(i);
                data[i][0] = r.getRequestCode();
                data[i][1] = r.getRequestInfo();
                data[i][2] = r.getStatus();
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(155, 89, 182));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            // Button Panel
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            btnPanel.setBackground(Color.WHITE);
            
            JButton approveBtn = new JButton("✓ APPROVE");
            approveBtn.setBackground(new Color(46, 204, 113));
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            approveBtn.setFocusPainted(false);
            approveBtn.setBorderPainted(false);
            approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            approveBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String requestCode = (String) table.getValueAt(selectedRow, 0);
                    boolean approved = sas.approveRequest(requestCode);
                    JOptionPane.showMessageDialog(panel,
                        approved ? "Request Approved Successfully" : "Approval Failed",
                        "Result", JOptionPane.INFORMATION_MESSAGE);
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a request", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            JButton rejectBtn = new JButton("✗ REJECT");
            rejectBtn.setBackground(new Color(231, 76, 60));
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rejectBtn.setFocusPainted(false);
            rejectBtn.setBorderPainted(false);
            rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rejectBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(panel, "Feature coming soon", "Info", JOptionPane.INFORMATION_MESSAGE);
            });
            
            btnPanel.add(approveBtn);
            btnPanel.add(rejectBtn);
            panel.add(btnPanel, BorderLayout.SOUTH);
        } catch (Exception e) {
            System.err.println("Error loading requests panel: " + e.getMessage());
            panel.add(new JLabel("Error loading requests data"));
        }
        
        return panel;
    }
    
    private JPanel createRankingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            List<Driver> list = ds.getDriverRanking();
            String[] columns = {"Rank", "Driver ID", "Name", "Score"};
            Object[][] data = new Object[list.size()][4];
            
            for (int i = 0; i < list.size(); i++) {
                Driver d = list.get(i);
                data[i][0] = (i + 1) + "st";
                data[i][1] = d.getpublic_driver_id();
                data[i][2] = d.getfirstName() + " " + d.getlastName();
                data[i][3] = d.getranking();
            }
            
            JTable table = new JTable(new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(30);
            table.getTableHeader().setBackground(new Color(155, 89, 182));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            // Center align all columns
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading rankings panel: " + e.getMessage());
            panel.add(new JLabel("Error loading rankings data"));
        }
        
        return panel;
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 245, 245),
                    getWidth(), getHeight(), new Color(235, 240, 245));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}