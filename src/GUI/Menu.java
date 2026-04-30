package GUI;

import Model.Driver;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.util.List;
import Model.DriverPerformance;
import Model.SuperAdmin;
import Service.DriverService;
import Service.SubAdminService;
import Service.SuperAdminService;

public class Menu extends JFrame implements ActionListener {

    JButton button, button1;

    // ===== storage ni ======
    
    
    static String superAdminUser = null;
    static String superAdminPass = null;
    
    DriverService ds =  new DriverService();
    SuperAdminService sas = new SuperAdminService();
    SubAdminService subs = new SubAdminService();
    
    boolean superAdminExists = sas.checkAccout();
    
    public Menu() {
        
           
        ImageIcon logo = new ImageIcon("/logo.jpg");
        setIconImage(logo.getImage());
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
        // ======= DRIVER PANEL =======
        class  DriverDashboard extends JFrame{
            
            DriverDashboard(Driver driver){
                 
                setTitle("Driver Dashboard");
                setSize(800,450);
                setLocationRelativeTo(null);
                setLayout(null);
                getContentPane().setBackground(new Color(255, 192, 0));
                
                JLabel name = new JLabel("Name" + driver.getfirstName() + " " + driver.getlastName());
                name.setBounds(50,50,400,30);
                
                JLabel id = new JLabel("ID" + driver.getpublic_driver_id());
                id.setBounds(50,90,400,30);
                
                JButton records = new JButton("View Records");
                records.setBounds(50,150,200,40);
                
                records.addActionListener(e ->{
                    
                    List <DriverPerformance> list = ds.getDriverRecords(driver.getpublic_driver_id());
                    
                    StringBuilder sb = new StringBuilder();
                    
                    for (DriverPerformance dp : list)
                    {
                        sb.append("Tickets: ").append(dp.gettotalTickets())
                         .append(" | Revenue: ").append(dp.gettotalRevenue())
                         .append(" | KM/L: ").append(dp.getaverageKMPL())
                         .append("\n");       
                    }
                    
                    JOptionPane.showMessageDialog(this, sb.toString());
                });
                
                JButton back = new JButton("LOG OUT");
                back.setBounds(650,10,100,30);
                
                back.addActionListener(e ->{
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
    //========= SUPER ADMIN DASHBOARD ========
        
        class SuperAdminDashboard extends JFrame
{
    SuperAdminDashboard()
    {
        setTitle("Super Admin Dashboard");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 192, 0));

        SuperAdmin sa = sas.getSAData();

        JLabel name = new JLabel("Name: " + sa.getfirstName() + " " + sa.getlastName());
        name.setBounds(50, 50, 400, 30);

        JLabel id = new JLabel("ID: " + sa.getPublicID());
        id.setBounds(50, 90, 400, 30);

        JLabel stats = new JLabel(
            "Drivers: " + ds.totalDriver() +
            " | SubAdmins: " + subs.totalSubAdmin() +
            " | Pending: " + sas.totalPending()
        );
        stats.setBounds(50, 140, 600, 30);

        add(name);
        add(id);
        add(stats);

        setVisible(true);
    }
}

    // ========= DRIVER LOGIN ==========
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

            JLabel uLabel = new JLabel("ID Number:");
            uLabel.setBounds(250, 120, 100, 30);

            JTextField username = new JTextField();
            username.setBounds(350, 120, 200, 30);

            JLabel pLabel = new JLabel("Password:");
            pLabel.setBounds(250, 170, 100, 30);

            JPasswordField password = new JPasswordField();
            password.setBounds(350, 170, 200, 30);

            JButton login = new JButton("LOGIN");
            login.setBounds(350, 230, 140, 35);
            
            login.addActionListener(e -> {
                
                String id = username.getText();
                String pass = new String(password.getPassword());
                
                Driver driver = ds.loginDriver(id, pass);
                
                if (driver !=null)
                {
                    JOptionPane.showMessageDialog(this, "Login Succesful");
                    new DriverDashboard(driver);
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Wrong Credintials");
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
                if (superAdminExists) {
                    new SuperAdminLogin();
                } else {
                    new SuperAdminSignup();
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

    //  =================SUPER ADMIN SIGN UP=================
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
                signup.setBounds(320,340,150,40);
        
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

            if(success){
                new SuccessPanel("Signup Successful!");
                dispose();
            } else {
                new ErrorPanel("Signup Failed!");
            }
        });

        add(form);
        add(signup);
        add(back);
        add(title);

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
            
            JLabel title = new JLabel("SUPER ADMIN LOGIN");
            title.setBounds(280, 50, 300, 30);
            title.setFont(new Font("Arial", Font.BOLD, 22));

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
                
                if (result == 1)
                {
                    
                    new SuperAdminDashboard();
                    dispose();
                }
                else if (result == 2)
                {
                    new ErrorPanel ("Wrong Credintials");
                    
                }
                 else
                {
                    JOptionPane.showMessageDialog(this, "Account not found");
                }
            });

            add(back);
            add(title);
            add(uLabel);
            add(user);
            add(pLabel);
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
            add(user);
            add(pass);
            add(login);
            add(uLabel);
            add(pLabel);

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
    
    class SuccessPanel  extends JFrame{
        
        SuccessPanel(String msg){
            setTitle("Succes Panel");
            setSize(400,200);
            setLocationRelativeTo(null);
            setLayout(null);
            
            JLabel label = new JLabel(msg, SwingConstants.CENTER);
            label.setBounds(50,40,300,30);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            
            JButton ok = new JButton("OK");
            ok.setBounds(150,100,100,30);
            ok.addActionListener(e -> dispose());
            
            add(label);
            add(ok);
            
            setVisible(true);
            
           
        }
    }
    class ErrorPanel extends JFrame{
        
        ErrorPanel(String msg){
            setTitle("Error");
            setSize(400,200);
            setLocationRelativeTo(null);
            setLayout(null);
            
            JLabel label = new JLabel(msg, SwingConstants.CENTER);
            label.setBounds(50,40,300,30);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            
            JButton ok = new JButton("OK");
            ok.setBounds(150,100,100,30);
            ok.addActionListener(e -> dispose());;
            
            add(label);
            add(ok);
            
            setVisible(true);
        }


}
    

    public static void main(String[] args) {
        new Menu();
    }
}
