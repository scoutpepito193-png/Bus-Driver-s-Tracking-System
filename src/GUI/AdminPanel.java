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

        // BACK button: Return to Menu
        back.addActionListener(e -> {
            new Menu();
            dispose();
        });

        JButton superAdmin = new JButton("SUPER ADMIN");
        JButton subAdmin = new JButton("SUB ADMIN");

        superAdmin.setBounds(300, 140, 200, 40);
        subAdmin.setBounds(300, 200, 200, 40);

        // SUPER ADMIN button: Check if account exists and route accordingly
        superAdmin.addActionListener(e -> {
            try {
                // Check if SuperAdmin account already exists in database
                // Calls checkAccout() method from SuperAdminService
                boolean exists = sas.checkAccout();
                
                if (exists) {
                    // Account exists in database - show login panel
                    this.setVisible(false);
                    new SuperAdminLogin(this);
                } else {
                    // No account exists - show signup panel for new account creation
                    this.setVisible(false);
                    new SuperAdminSignupPanel(this);
                }
            } catch (Exception ex) {
                // If there's an error checking account (e.g., database connection issue)
                // Show error dialog and stay on current screen
                JOptionPane.showMessageDialog(this, 
                    "Error checking account: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // SUB ADMIN button: Go to SubAdminLogin with parent reference
        subAdmin.addActionListener(e -> {
            this.setVisible(false);
            new SubAdminLogin(this);
        });

        add(back);
        add(superAdmin);
        add(subAdmin);

        setVisible(true);
    }
}