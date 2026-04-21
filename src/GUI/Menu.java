package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Menu extends JFrame implements ActionListener{
    
    JButton button;
    JButton button1;

    public Menu() {
        //Frame Setup
        setTitle("Bus Driver Tracking System");
        setSize(800, 450);//width heigth
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center
        getContentPane().setBackground(new Color(255, 192, 0));
        
        //null= absolute positioning
        this.setLayout(null);

        //for button
        button = new JButton("DRIVER");
        button1 = new JButton("ADMIN");
        
        button.setBounds(445, 200, 150, 50); //x, y, width, height
        button.setFocusable(false);//remove box
        button.setFont(new Font("Calisto MT", Font.PLAIN, 15));//Font for text inside button
        
        button1.setBounds(200, 200, 150, 50); //x, y, width, height
        button1.setFocusable(false);//remove box
        button1.setFont(new Font("Calisto MT", Font.PLAIN, 15));//Font for text inside button
        
        //makes button functional
        button.addActionListener(this);
        button1.addActionListener(this);

        //Label
        JLabel label = new JLabel("Bus Drivers' Tracking System");
        label.setForeground(new Color(0, 0, 0));//font color
        label.setFont(new Font("Calisto MT", Font.PLAIN, 40)); 
        label.setBounds(0, 50, 800, 100); 
        label.setHorizontalAlignment(JLabel.CENTER);

        //to add the components to the frame
        this.add(button);
        this.add(button1);
        this.add(label);

        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //actions when a button is clicked
        if(e.getSource()==button){
            System.out.println("Bati ka nawng");
        }
        if(e.getSource()==button1){
            System.out.println("Gwapo Ka");
        }
    }
}