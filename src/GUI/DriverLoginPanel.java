package GUI;

import Model.Driver;
import Service.DriverService;

import java.awt.Color;
import javax.swing.*;

public class DriverLoginPanel extends JFrame {

    DriverService ds = new DriverService();

    public DriverLoginPanel() {

        setTitle("Driver Login");
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

        JLabel title = new JLabel("DRIVER LOGIN");
        title.setBounds(300, 50, 250, 30);
        title.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));

        JLabel uLabel = new JLabel("ID Number:");
        uLabel.setBounds(220, 120, 100, 30);

        JTextField username = new JTextField();
        username.setBounds(320, 120, 220, 30);

        JLabel pLabel = new JLabel("Password:");
        pLabel.setBounds(220, 170, 100, 30);

        JPasswordField password = new JPasswordField();
        password.setBounds(320, 170, 220, 30);

        JButton login = new JButton("LOGIN");
        login.setBounds(330, 230, 140, 35);

        login.addActionListener(e -> {

            String id = username.getText();
            String pass = new String(password.getPassword());

            Driver driver = ds.loginDriver(id, pass);

            if (driver != null) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                new DriverDashboard(driver);
                dispose();
            } else {
                new ErrorPanel("Wrong Credentials");
            }
        });

        add(back);
        add(title);
        add(uLabel);
        add(username);
        add(pLabel);
        add(password);
        add(login);

        setVisible(true);
    }
}