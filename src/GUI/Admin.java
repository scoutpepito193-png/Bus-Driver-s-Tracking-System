package GUI;

import java.awt.*;
import javax.swing.*;

public class Admin extends JFrame {

    public Admin() {
        setTitle("Admin Panel");
        setSize(800, 450);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 192, 0));
        setLayout(null);

        JButton back = new JButton("BACK");
        back.setBounds(680, 10, 90, 25);
        back.setFont(new Font("Calisto MT", Font.PLAIN, 10));
        back.setFocusable(false);
        back.addActionListener(e -> {
            new Menu();
            dispose();
        });

        JLabel label = new JLabel("LOG IN AS");
        JButton superAdmin = new JButton("SUPER ADMIN");
        JButton subAdmin = new JButton("SUB ADMIN");

        label.setFont(new Font("Calisto MT", Font.BOLD, 20));
        label.setBounds(0, 75, 800, 50);
        label.setHorizontalAlignment(JLabel.CENTER);

        superAdmin.setFont(new Font("Calisto MT", Font.PLAIN, 15));
        superAdmin.setFocusable(false);
        subAdmin.setFont(new Font("Calisto MT", Font.PLAIN, 15));
        subAdmin.setFocusable(false);

        superAdmin.setBounds(300, 135, 200, 50);
        subAdmin.setBounds(300, 210, 200, 50);

        superAdmin.addActionListener(e -> {
            // Reference static boolean from Menu class
            if (!Menu.superAdminExists) {
                new SuperAdminSignup();
            } else {
                new SuperAdminLogin();
            }
            dispose();
        });

        subAdmin.addActionListener(e -> {
            // Logic for SubAdminLogin if it exists
            dispose();
        });

        add(back);
        add(superAdmin);
        add(subAdmin);
        add(label);

        setVisible(true);
    }

    // --- INNER CLASSES START HERE ---

    // SUPER ADMIN SIGN UP 
    class SuperAdminSignup extends JFrame {
        SuperAdminSignup() {
            setTitle("Super Admin Sign Up");
            setSize(800, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(255, 192, 0));
            setLayout(null);

            JButton back = new JButton("BACK");
            back.setBounds(680, 10, 90, 25);
            back.setFont(new Font("Calisto MT", Font.PLAIN, 12));
            back.setFocusable(false);
            
            back.addActionListener(e -> {
                new Admin(); // Re-opens the Admin main panel
                dispose();
            });

            JLabel title = new JLabel("SUPER ADMIN SIGN UP");
            title.setBounds(250, 50, 300, 60);
            title.setFont(new Font("Calisto MT", Font.BOLD, 25));

            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();
            user.setBounds(300, 120, 200, 30);
            pass.setBounds(300, 170, 200, 30);

            JButton signup = new JButton("SIGN UP");
            signup.setBounds(330, 230, 140, 35);
            signup.setFont(new Font("Calisto MT", Font.PLAIN, 15));
            signup.setFocusable(false);
            
            signup.addActionListener(e -> {
                // Storing data back into the Menu static variables
                Menu.superAdminUser = user.getText();
                Menu.superAdminPass = new String(pass.getPassword());
                Menu.superAdminExists = true;

                new SuperAdminWelcome();
                dispose();
            });

            add(back); add(title); add(user); add(pass); add(signup);
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
            back.setFont(new Font("Calisto MT", Font.PLAIN, 12));
            back.setFocusable(false);
            
            back.addActionListener(e -> {
                new Admin();
                dispose();
            });

            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();
            user.setBounds(300, 120, 200, 30);
            pass.setBounds(300, 170, 200, 30);

            JButton login = new JButton("LOGIN");
            login.setBounds(330, 230, 140, 35);
            login.setFont(new Font("Calisto MT", Font.BOLD, 20));
            login.setFocusable(false);
            
            login.addActionListener(e -> {
                // Checking credentials against Menu static variables
                if (user.getText().equals(Menu.superAdminUser)
                        && new String(pass.getPassword()).equals(Menu.superAdminPass)) {
                    new SuperAdminWelcome();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials");
                }
            });

            add(back); add(user); add(pass); add(login);
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
            back.setFont(new Font("Calisto MT", Font.PLAIN, 12));
            back.setFocusable(false);
            
            back.addActionListener(e -> {
                new Admin();
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
            login.setFont(new Font("Calisto MT", Font.BOLD, 20));

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
            label.setBounds(0, 80, 800, 100);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(new Font("Calisto MT", Font.BOLD, 25));
            
            JButton login = new JButton("LOG IN");
            login.setBounds(240, 190, 140, 40);
            login.setFont(new Font("Calisto MT", Font.PLAIN, 16));
            login.setFocusable(false);
            
            login.addActionListener(e -> {
                new AdminDashboard(); 
                dispose();
            });

            JButton logout = new JButton("LOGOUT");
            logout.setBounds(420, 190, 140, 40);
            logout.setFont(new Font("Calisto MT", Font.PLAIN, 16));
            logout.setFocusable(false);
            
            logout.addActionListener(e -> {
                new Menu();
                dispose();
            });

            add(label); add(login); add(logout);
            setVisible(true);
        }
    }
    
    class AdminDashboard extends JFrame {
    AdminDashboard() {
        setTitle("Admin Dashboard");
        
        // 1. Maximize the frame while keeping the title bar (minimize/maximize/exit)
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // 2. REMOVE or COMMENT OUT setUndecorated(true)
        // setUndecorated(true); // Keeping this commented so you see the difference
        
        getContentPane().setBackground(new Color(255, 192, 0));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // 3. Get actual screen width for the label alignment
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

        JLabel welcome = new JLabel("ADMIN MAIN DASHBOARD");
        welcome.setFont(new Font("Calisto MT", Font.BOLD, 40));
        
        // Use screenWidth here instead of 1920
        welcome.setBounds(0, 100, screenWidth, 100); 
        welcome.setHorizontalAlignment(JLabel.CENTER);

        JButton logout = new JButton("BACK");
        // Positioning the back button relative to the top right
        logout.setBounds(50, 50, 150, 35); 
        
        logout.addActionListener(e -> {
            new Admin(); 
            dispose();
        });

        add(welcome);
        add(logout);
        setVisible(true);
    }
    }
}