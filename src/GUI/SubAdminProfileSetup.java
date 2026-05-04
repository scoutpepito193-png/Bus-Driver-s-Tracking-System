package GUI;

import Model.SubAdmin;
import Service.SubAdminService;
import javax.swing.*;

public class SubAdminProfileSetup extends JFrame {
    private SubAdminService subAdminService;
    private SubAdmin subAdmin;
    
    public SubAdminProfileSetup(SubAdmin subAdmin, SubAdminService subAdminService) {
        this.subAdmin = subAdmin;
        this.subAdminService = subAdminService;
        
        setTitle("Trackify - Complete Your Profile");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add your profile setup UI components here
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Complete your profile information"));
        add(mainPanel);
        
        setVisible(true);
    }
}   