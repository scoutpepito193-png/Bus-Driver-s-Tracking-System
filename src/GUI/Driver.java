package GUI;

import java.awt.*;
import javax.swing.*;

public class Driver extends JFrame {
    public Driver() { // Renamed constructor to match file name
        setTitle("Driver Login");
        setSize(800, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 192, 0));
        setFont(new Font("Calisto MT", Font.PLAIN, 15));
        setLayout(null);

        JButton back = new JButton("BACK");
        back.setBounds(680, 10, 90, 25);
        back.addActionListener(e -> {
            new Menu();
            dispose();
        });

        JLabel title = new JLabel("DRIVER LOGIN");
        title.setBounds(320, 60, 200, 30);

        JLabel uLabel = new JLabel("Username:");
        uLabel.setBounds(250, 120, 100, 30);

        JTextField username = new JTextField();
        username.setBounds(350, 120, 200, 30);

        JLabel pLabel = new JLabel("Password:");
        pLabel.setBounds(250, 170, 100, 30);

        JPasswordField password = new JPasswordField();
        password.setBounds(350, 170, 200, 30);

        JButton login = new JButton("LOGIN");
        login.setBounds(350, 230, 140, 35);

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