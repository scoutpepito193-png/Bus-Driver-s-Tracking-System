package GUI;

import Model.Driver;
import Model.DriverPerformance;
import Service.DriverService;

import java.awt.Color;
import javax.swing.*;
import java.util.List;

public class DriverDashboard extends JFrame {

    DriverService ds = new DriverService();

    public DriverDashboard(Driver driver) {

        setTitle("Driver Dashboard");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 192, 0));

        JLabel name = new JLabel("Name: " + driver.getfirstName() + " " + driver.getlastName());
        name.setBounds(50, 50, 400, 30);

        JLabel id = new JLabel("ID: " + driver.getpublic_driver_id());
        id.setBounds(50, 90, 400, 30);

        JButton records = new JButton("View Records");
        records.setBounds(50, 150, 200, 40);

        records.addActionListener(e -> {

            List<DriverPerformance> list =
                    ds.getDriverRecords(driver.getpublic_driver_id());

            StringBuilder sb = new StringBuilder();

            for (DriverPerformance dp : list) {
                sb.append("Tickets: ").append(dp.gettotalTickets())
                        .append(" | Revenue: ").append(dp.gettotalRevenue())
                        .append(" | KM/L: ").append(dp.getaverageKMPL())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        });

        JButton back = new JButton("LOG OUT");
        back.setBounds(650, 10, 100, 30);

        back.addActionListener(e -> {
            new Menu();
            dispose();
        });

        add(name);
        add(id);
        add(records);
        add(back);

        setVisible(true);
    }
}