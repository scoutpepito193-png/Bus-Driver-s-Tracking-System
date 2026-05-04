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

    DriverService ds = new DriverService();
    SubAdminService subs = new SubAdminService();
    SuperAdminService sas = new SuperAdminService();
    private SuperAdmin superAdmin;

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
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("SUPER ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + superAdmin.getfirstName() + " " + superAdmin.getlastName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(220, 220, 220));
        
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(titleLabel);
        leftHeader.add(userLabel);
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Overview Tab
        tabbedPane.addTab("Overview", createOverviewPanel());
        
        // Drivers Tab
        tabbedPane.addTab("Drivers", createDriversPanel());
        
        // Sub Admins Tab
        tabbedPane.addTab("Sub Admins", createSubAdminsPanel());
        
        // Requests Tab
        tabbedPane.addTab("Requests", createRequestsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Total Drivers Card
        panel.add(createStatCard("Total Drivers", String.valueOf(ds.totalDriver()), new Color(52, 152, 219)));
        
        // Total Sub Admins Card
        panel.add(createStatCard("Total Sub Admins", String.valueOf(subs.totalSubAdmin()), new Color(46, 204, 113)));
        
        // Pending Requests Card
        panel.add(createStatCard("Pending Requests", String.valueOf(sas.totalPending()), new Color(241, 196, 15)));
        
        // Approved Requests Card
        panel.add(createStatCard("Approved Requests", String.valueOf(sas.totalApproved()), new Color(155, 89, 182)));
        
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
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        
        List<DriverPerformance> list = ds.getPerformance();
        String[] columns = {"Driver ID", "Name", "Tickets", "Revenue", "KM/L"};
        Object[][] data = new Object[list.size()][5];
        
        for (int i = 0; i < list.size(); i++) {
            DriverPerformance dp = list.get(i);
            data[i][0] = dp.getdriver().getpublic_driver_id();
            data[i][1] = dp.getdriver().getfirstName() + " " + dp.getdriver().getlastName();
            data[i][2] = dp.gettotalTickets();
            data[i][3] = dp.gettotalRevenue();
            data[i][4] = String.format("%.2f", dp.getaverageKMPL());
        }
        
        JTable table = new JTable(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSubAdminsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<SubAdmin> list = subs.getSubAdmins();
        String[] columns = {"Sub Admin ID", "Name", "Contact", "Position"};
        Object[][] data = new Object[list.size()][4];
        
        for (int i = 0; i < list.size(); i++) {
            SubAdmin sa = list.get(i);
            data[i][0] = sa.getpublic_sub_id();
            data[i][1] = sa.getfirstName() + " " + sa.getlastName();
            data[i][2] = sa.getcontactNum();
            data[i][3] = sa.getposition();
        }
        
        JTable table = new JTable(new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton approveBtn = new JButton("APPROVE");
        approveBtn.setBackground(new Color(46, 204, 113));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        approveBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String requestCode = (String) table.getValueAt(selectedRow, 0);
                boolean approved = sas.approveRequest(requestCode);
                JOptionPane.showMessageDialog(panel,
                    approved ? "Request Approved Successfully" : "Approval Failed",
                    "Result", JOptionPane.INFORMATION_MESSAGE);
                // Refresh table
                ((DefaultTableModel) table.getModel()).setRowCount(0);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a request", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnPanel.add(approveBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 245, 245),
                    getWidth(), getHeight(), new Color(235, 235, 235));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}