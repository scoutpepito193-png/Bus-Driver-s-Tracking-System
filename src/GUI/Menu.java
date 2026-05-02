
package GUI;

import java.awt.*;
import javax.swing.*;

public class Menu extends JFrame {

    JButton button, button1;

    // GLOBAL STORAGE: Accessible by Admin.java using Menu.superAdminUser, etc.
    public static String superAdminUser = null;
    public static String superAdminPass = null;
    public static boolean superAdminExists = false;

    public Menu() {
        setTitle("Bus Driver Tracking System");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 192, 0));
        setLayout(null);

        JLabel label = new JLabel("Bus Driver Tracking System");
        label.setBounds(0, 90, 800, 100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Calisto MT", Font.BOLD, 30));

        button = new JButton("DRIVER");
        button1 = new JButton("ADMIN");

        button.setBounds(435, 200, 150, 50);
        button1.setBounds(215, 200, 150, 50);

        button.setFont(new Font("Calisto MT", Font.PLAIN, 15));
        button.setFocusable(false);
        button1.setFont(new Font("Calisto MT", Font.PLAIN, 15));
        button1.setFocusable(false);

        // --- LAMBDA CONNECTIONS TO EXTERNAL FILES ---
        button.addActionListener(e -> {
            new Driver(); // Opens Driver.java
            dispose();
        });

        button1.addActionListener(e -> {
            new Admin(); // Opens Admin.java
            dispose();
        });

        add(label);
        add(button);
        add(button1);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Menu();
    }
}
