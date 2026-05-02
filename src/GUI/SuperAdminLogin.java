package GUI;

import Service.SuperAdminService;

import java.awt.Color;
import javax.swing.*;

public class SuperAdminLogin extends JFrame {

    SuperAdminService sas = new SuperAdminService();

    public SuperAdminLogin() {

        setTitle("Super Admin Login");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 192, 0));

        JButton back = new JButton("BACK");
        back.setBounds(680, 10, 90, 25);

        back.addActionListener(e -> {
            new AdminPanel();
            dispose();
        });

        JLabel title = new JLabel("SUPER ADMIN LOGIN");
        title.setBounds(280, 50, 300, 30);
        title.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));

        JLabel uLabel = new JLabel("Admin ID:");
        uLabel.setBounds(220, 120, 100, 30);

        JLabel pLabel = new JLabel("Password:");
        pLabel.setBounds(220, 170, 100, 30);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        user.setBounds(320, 120, 220, 30);
        pass.setBounds(320, 170, 220, 30);

        JButton login = new JButton("LOGIN");
        login.setBounds(330, 230, 140, 35);

        login.addActionListener(e -> {

            int result = sas.logIn(user.getText(),
                    new String(pass.getPassword()));

            if (result == 1) {
                new SuperAdminDashboard();
                dispose();
            } else if (result == 2) {
                new ErrorPanel("Wrong Credentials");
            } else {
                JOptionPane.showMessageDialog(this, "Account not found");
            }
        });

        add(back);
        add(title);
        add(uLabel);
        add(pLabel);
        add(user);
        add(pass);
        add(login);

        setVisible(true);
    }
}