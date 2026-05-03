package GUI;

import Model.SuperAdmin;
import Service.SuperAdminService;
import javax.swing.*;

public class SuperAdminProfileSetup extends JFrame {
    private SuperAdminService superAdminService;
    private SuperAdmin superAdmin;
    
    public SuperAdminProfileSetup(SuperAdmin superAdmin, SuperAdminService superAdminService) {
        this.superAdmin = superAdmin;
        this.superAdminService = superAdminService;
        
        setTitle("BDTracker - Complete Your Profile");
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