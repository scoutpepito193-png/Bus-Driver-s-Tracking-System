package GUI;

import javax.swing.*;
import java.awt.*;

public class SubAdminLogin extends JFrame {

    public SubAdminLogin() {

        setTitle("Sub Admin Login");
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

        JLabel title = new JLabel("SUB ADMIN LOGIN");
        title.setBounds(320, 50, 250, 30);

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