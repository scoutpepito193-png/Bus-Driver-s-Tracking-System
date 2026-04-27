package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Menu extends JFrame implements ActionListener {

    JButton button, button1;

    // TEMP SUPER ADMIN STORAGE RANI PIERRE HAHAHAHA
    static String superAdminUser = null;
    static String superAdminPass = null;
    static boolean superAdminExists = false;

    public Menu() {
        setTitle("Bus Driver Tracking System");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 192, 0));
        setLayout(null);

        button = new JButton("DRIVER");
        button1 = new JButton("ADMIN");

        button.setBounds(445, 200, 150, 50);
        button1.setBounds(200, 200, 150, 50);

        button.setFont(new Font("Calisto MT", Font.PLAIN, 15));
        button1.setFont(new Font("Calisto MT", Font.PLAIN, 15));

        button.addActionListener(this);
        button1.addActionListener(this);

        JLabel label = new JLabel("Bus Drivers' Tracking System");
        label.setBounds(0, 50, 800, 100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Calisto MT", Font.BOLD, 30));

        add(button);
        add(button1);
        add(label);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            new DriverLoginPanel();
            dispose();
        }
        if (e.getSource() == button1) {
            new AdminPanel();
            dispose();
        }
    }

    // DRIVER LOGIN 
    class DriverLoginPanel extends JFrame {
        DriverLoginPanel() {
            setTitle("Driver Login");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
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

    // ADMIN PANEL 
    class AdminPanel extends JFrame {
        AdminPanel() {
            setTitle("Admin Panel");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

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
                if (!superAdminExists) {
                    new SuperAdminSignup();
                } else {
                    new SuperAdminLogin();
                }
                dispose();
            });

            subAdmin.addActionListener(e -> {
                new SubAdminLogin();
                dispose();
            });

            add(back);
            add(superAdmin);
            add(subAdmin);

            setVisible(true);
        }
    }

    //  SUPER ADMIN SIGN UP 
    class SuperAdminSignup extends JFrame {
        SuperAdminSignup() {
            setTitle("Super Admin Sign Up");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

            JButton back = new JButton("BACK");
            back.setBounds(680, 10, 90, 25);
            back.addActionListener(e -> {
                new AdminPanel();
                dispose();
            });

            JLabel title = new JLabel("SUPER ADMIN SIGN UP");
            title.setBounds(300, 50, 300, 30);
            title.setFont(new Font("Arial", Font.BOLD, 20));

            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();

            user.setBounds(300, 120, 200, 30);
            pass.setBounds(300, 170, 200, 30);

            JButton signup = new JButton("SIGN UP");
            signup.setBounds(330, 230, 140, 35);

            signup.addActionListener(e -> {
                superAdminUser = user.getText();
                superAdminPass = new String(pass.getPassword());
                superAdminExists = true;

                new SuperAdminWelcome();
                dispose();
            });

            add(back);
            add(title);
            add(user);
            add(pass);
            add(signup);

            setVisible(true);
        }
    }

    // SUPER ADMIN LOGIN 
    class SuperAdminLogin extends JFrame {
        SuperAdminLogin() {
            setTitle("Super Admin Login");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

            JButton back = new JButton("BACK");
            back.setBounds(680, 10, 90, 25);
            back.addActionListener(e -> {
                new AdminPanel();
                dispose();
            });

            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();

            user.setBounds(300, 120, 200, 30);
            pass.setBounds(300, 170, 200, 30);

            JButton login = new JButton("LOGIN");
            login.setBounds(330, 230, 140, 35);

            login.addActionListener(e -> {
                if (user.getText().equals(superAdminUser)
                        && new String(pass.getPassword()).equals(superAdminPass)) {
                    new SuperAdminWelcome();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials");
                }
            });

            add(back);
            add(user);
            add(pass);
            add(login);

            setVisible(true);
        }
    }

    // SUB ADMIN LOGIN========== ref, sub sa ref
    class SubAdminLogin extends JFrame {
        SubAdminLogin() {
            setTitle("Sub Admin Login");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

            JButton back = new JButton("BACK");
            back.setBounds(680, 10, 90, 25);
            back.addActionListener(e -> {
                new AdminPanel();
                dispose();
            });

            JLabel title = new JLabel("SUB ADMIN LOGIN");
            title.setBounds(320, 50, 250, 30);

            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();

            user.setBounds(300, 120, 200, 30);
            pass.setBounds(300, 170, 200, 30);

            JButton login = new JButton("LOGIN");
            login.setBounds(330, 230, 140, 35);

            add(back);
            add(title);
            add(user);
            add(pass);
            add(login);

            setVisible(true);
        }
    }

    // ===== WELCOME SCREEN ===== welcome kuya pierre
    class SuperAdminWelcome extends JFrame {
        SuperAdminWelcome() {
            setTitle("Welcome Super Admin");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

            JLabel label = new JLabel("WELCOME SUPER ADMIN!");
            label.setBounds(250, 180, 400, 40);
            label.setFont(new Font("Arial", Font.BOLD, 25));

            JButton logout = new JButton("LOGOUT");
            logout.setBounds(330, 260, 140, 40);

            logout.addActionListener(e -> {
                new Menu();
                dispose();
            });

            add(label);
            add(logout);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Menu();
    }
}