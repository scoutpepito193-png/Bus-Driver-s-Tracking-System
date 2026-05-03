package GUI;

import Model.Driver;
import Service.DriverService;
import javax.swing.*;

public class DriverProfileSetup extends JFrame {
    private DriverService driverService;
    private Driver driver;
    
    public DriverProfileSetup(Driver driver, DriverService driverService) {
        this.driver = driver;
        this.driverService = driverService;
        
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