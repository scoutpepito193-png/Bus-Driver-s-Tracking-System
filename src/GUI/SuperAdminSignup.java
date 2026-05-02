package GUI;

import Service.SuperAdminService;

import java.awt.*;
import javax.swing.*;

public class SuperAdminSignup extends JFrame {

    SuperAdminService sas = new SuperAdminService();

    public SuperAdminSignup() {

        setTitle("Super Admin Sign Up");
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

        JLabel title = new JLabel("SUPER ADMIN SIGN UP");
        title.setBounds(300, 30, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));
        form.setBounds(180, 70, 430, 250);
        form.setBackground(new Color(255, 192, 0));

        JTextField idField = new JTextField();
        JTextField fnameField = new JTextField();
        JTextField lnameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField photoField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        form.add(new JLabel("ID:"));
        form.add(idField);

        form.add(new JLabel("First Name:"));
        form.add(fnameField);

        form.add(new JLabel("Last Name:"));
        form.add(lnameField);

        form.add(new JLabel("Contact:"));
        form.add(contactField);

        form.add(new JLabel("Position:"));
        form.add(positionField);

        form.add(new JLabel("Photo URL:"));
        form.add(photoField);

        form.add(new JLabel("Password:"));
        form.add(passField);

        form.add(new JLabel("Confirm Password:"));
        form.add(confirmField);

        JButton signup = new JButton("SIGN UP");
        signup.setBounds(320, 340, 150, 40);

        signup.addActionListener(e -> {

            boolean success = sas.registerSA(
                    idField.getText(),
                    fnameField.getText(),
                    lnameField.getText(),
                    contactField.getText(),
                    positionField.getText(),
                    photoField.getText(),
                    new String(passField.getPassword()),
                    new String(confirmField.getPassword())
            );

            if (success) {
                new SuccessPanel("Signup Successful!");
                dispose();
            } else {
                new ErrorPanel("Signup Failed!");
            }
        });

        add(title);
        add(form);
        add(signup);
        add(back);

        setVisible(true);
    }
}
