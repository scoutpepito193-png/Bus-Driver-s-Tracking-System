package GUI;

import Service.SuperAdminService;

import java.awt.Color;
import javax.swing.*;

public class AdminPanel extends JFrame {

    SuperAdminService sas = new SuperAdminService();

    public AdminPanel() {

        setTitle("Admin Panel");
        setSize(800, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 192, 0));
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton back = new JButton("BACK");
        back.setBounds(680, 10, 90, 25);

        back.addActionListener(e -> {
            new Menu();
            dispose();
        });

        JButton superAdmin = new JButton("SUPER ADMIN");
        JButton subAdmin = new JButton("SUB ADMIN");

        superAdmin.setBounds(300, 140, 200, 40);
        subAdmin.setBounds(300, 200, 200, 40);

        superAdmin.addActionListener(e -> {
            // Go to SuperAdminLogin with parent reference
            new SuperAdminLogin(this);
            dispose();
        });

        subAdmin.addActionListener(e -> {
            // Go to SubAdminLogin with parent reference
            new SubAdminLogin(this);
            dispose();
        });

        add(back);
        add(superAdmin);
        add(subAdmin);

        setVisible(true);
    }
}