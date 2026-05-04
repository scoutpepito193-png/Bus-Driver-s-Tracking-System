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
import java.awt.*;
import java.util.List;
import java.util.Map;

public class SuperAdminDashboard extends JFrame {

    DriverService ds = new DriverService();
    SubAdminService subs = new SubAdminService();
    SuperAdminService sas = new SuperAdminService();

    public SuperAdminDashboard(SuperAdmin superAdmin, SuperAdminService superAdminService,
                           DriverService driverService, SubAdminService subAdminService) {

        setTitle("Super Admin Features");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 192, 0));

        JLabel title = new JLabel("SUPER ADMIN FEATURES");
        title.setBounds(300, 20, 400, 30);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);

        JButton overviewBtn = new JButton("OVERVIEW");
        JButton driverBtn = new JButton("DRIVERS");
        JButton subAdminBtn = new JButton("SUB ADMINS");
        JButton requestBtn = new JButton("REQUESTS");
        JButton backBtn = new JButton("BACK");

        overviewBtn.setBounds(50, 100, 200, 40);
        driverBtn.setBounds(50, 160, 200, 40);
        subAdminBtn.setBounds(50, 220, 200, 40);
        requestBtn.setBounds(50, 280, 200, 40);
        backBtn.setBounds(700, 400, 150, 40);

        add(overviewBtn);
        add(driverBtn);
        add(subAdminBtn);
        add(requestBtn);
        add(backBtn);

        // ================= OVERVIEW =================
        overviewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Total Drivers: " + ds.totalDriver() +
                    "\nTotal SubAdmins: " + subs.totalSubAdmin() +
                    "\nPending Requests: " + sas.totalPending()
            );
        });

        // ================= DRIVERS =================
        driverBtn.addActionListener(e -> {

            List<DriverPerformance> list = ds.getPerformance();
            StringBuilder sb = new StringBuilder();

            sb.append("ID | Name | Tickets | Revenue | KM/L\n\n");

            for (DriverPerformance dp : list) {
                sb.append(dp.getdriver().getpublic_driver_id())
                        .append(" | ")
                        .append(dp.getdriver().getfirstName())
                        .append(" ")
                        .append(dp.getdriver().getlastName())
                        .append(" | ")
                        .append(dp.gettotalTickets())
                        .append(" | ")
                        .append(dp.gettotalRevenue())
                        .append(" | ")
                        .append(dp.getaverageKMPL())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // ================= SUB ADMINS =================
        subAdminBtn.addActionListener(e -> {

            List<SubAdmin> list = subs.getSubAdmins();
            StringBuilder sb = new StringBuilder();

            for (SubAdmin s : list) {
                sb.append(s.getpublic_sub_id())
                        .append(" - ")
                        .append(s.getfirstName())
                        .append(" ")
                        .append(s.getlastName())
                        .append(" | ")
                        .append(s.getposition())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // ================= REQUESTS =================
        requestBtn.addActionListener(e -> {

            List<Request> listReq = sas.getAllRequest();

            StringBuilder sb = new StringBuilder();

            for (Request r : listReq) {
                sb.append(r.getRequestCode())
                        .append(" | ")
                        .append(r.getRequestInfo())
                        .append(" | ")
                        .append(r.getStatus())
                        .append("\n");
            }

            String input = JOptionPane.showInputDialog(
                    this,
                    sb.toString() + "\n\nEnter Request Code:"
            );

            if (input == null || input.isEmpty()) return;

            Request req = sas.getRequest(input);

            if (req == null) {
                JOptionPane.showMessageDialog(this, "Request not found!");
                return;
            }

            if ("DRIVER REGISTRATION".equals(req.getRequestInfo())) {

                Driver d = (Driver) sas.getReqDetails(input);

                JOptionPane.showMessageDialog(this,
                        "ID: " + d.getpublic_driver_id() +
                        "\nName: " + d.getfirstName() + " " + d.getlastName() +
                        "\nContact: " + d.getcontactNumber()
                );

            } else if ("REMOVE DRIVER".equals(req.getRequestInfo())) {

                Map<String, String> data = (Map<String, String>) sas.getReqDetails(input);

                JOptionPane.showMessageDialog(this,
                        "Reason: " + data.get("reason")
                );
            }

            String choice = JOptionPane.showInputDialog(
                    this,
                    "[1] Approve\n[2] Reject\n[0] Back"
            );

            if ("1".equals(choice)) {
                boolean approved = sas.approveRequest(input);

                JOptionPane.showMessageDialog(this,
                        approved ? "Request Approved" : "Approval Failed"
                );
            }
        });

        // ================= BACK =================
        backBtn.addActionListener(e -> {
            new Menu();
            dispose();
        });

        setVisible(true);
    }
}