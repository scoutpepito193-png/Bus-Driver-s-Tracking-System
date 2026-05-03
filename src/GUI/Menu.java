package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Menu extends JFrame implements ActionListener {

    JButton button;
    JButton button1;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}