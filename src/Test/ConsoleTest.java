package Test;

import java.util.Scanner;
import Service.SuperAdminService;
import Service.SubAdminService;
import Service.DriverService;
import Model.SuperAdmin;
import Model.Driver;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTest
{   
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        SuperAdminService sas = new SuperAdminService();
        SubAdminService subs = new SubAdminService();
        DriverService ds = new DriverService();
        
        int choice;        
        do
        {
            System.out.println("Bus Driver's Tracking System");
            System.out.println("Select Profile");
            System.out.println("1. Super Admin");
            System.out.println("2. Sub-Admin");
            System.out.println("3. Driver");
            System.out.println("0. Exit");
        
            System.out.print("Enter Choice: ");
            choice = scan.nextInt();
            scan.nextLine();
        
        
            switch(choice)
            {
               case 1 ->
               {
                   boolean verify = sas.checkAccout();
                   
                   if (verify == false)
                   {
                        boolean flag = true;
                           
                        while (flag)
                        {
                            System.out.println("Set-Up Profile");
                            System.out.print("ID: ");
                            String id = scan.nextLine();
                              
                            System.out.print("First Name: ");
                            String fname = scan.nextLine();
                          
                            System.out.print("Last Name: ");
                            String lname = scan.nextLine();
                          
                            System.out.print("Contact Number: ");
                            String contactNum = scan.nextLine();
                           
                            System.out.print("Position: ");
                            String position = scan.nextLine();
                            
                            System.out.print("Upload Photo: ");
                            String photo = scan.nextLine();
                                
                            System.out.print("Pres [1[ to Confirm | [2] to Clear: ");
                            int sAProfileChoice = scan.nextInt();
                            scan.nextLine();
                                
                            if(sAProfileChoice == 1)
                            {
                                boolean confirm = false;
                                while (confirm == false)
                                {
                                    System.out.println("New Password: ");
                                    String password = scan.nextLine();
                                
                                    System.out.println("Confirm Password: ");
                                    String confirmPass = scan.nextLine();
                                    
                                    confirm = sas.registerSA(id, fname, lname, contactNum, position, photo, password, confirmPass);
                                    
                                    if (confirm == false)
                                    {
                                        System.out.println("Password doesn't match");
                                    }
                                    else
                                    {
                                        flag = false;
                                    }
                                        
                                }
                            }
                        }                        
                    }
                    int attemp = 0;
                    boolean loggedIn = false;
                    
                    while (attemp != 3 && !loggedIn)
                    {
                        System.out.println("Log-In");
                        System.out.print("ID: ");
                        String id = scan.nextLine();
                    
                        System.out.print("Password: ");
                        String password = scan.nextLine();
                        int logIn = sas.logIn(id, password);
                    
                        switch (logIn)
                        {
                            case 0 ->
                            {
                                System.out.println("Wrong Credentials!");
                                ++attemp;
                            }
                        
                            case 2 -> 
                            {
                                System.out.println("Account Not Found");
                                ++attemp;
                            }
                            
                            default ->
                            {
                                loggedIn = true;
                                
                                SuperAdmin saOutput = sas.getSAData();
                                System.out.println("Super Admin Dashboard");
                                System.out.println("Profile Picture: " + saOutput.getphotoURL());
                                System.out.println("Name: " + saOutput.getfirstName() + " " + saOutput.getlastName());
                                System.out.println("Position: " + saOutput.getposition());
                                System.out.println("ID: " + saOutput.getPublicID());
                                System.out.println();
                                
                                System.out.println("[1] Overview    [2] Driver  [3] Sub Admin       [0] Sign Out");
                                System.out.println();                                
                                
                                boolean inSADashboard = true;
                                
                                while (inSADashboard)
                                {
                                    System.out.print("Enter Choice: ");
                                    int superAdminDBchoice = scan.nextInt();
                                
                                    switch (superAdminDBchoice)
                                    {
                                        case 1:
                                            int totalRegisteredDriver = ds.totalDriver();
                                            int totalRegisteredSubAdmin = subs.totalSubAdmin();
                                            int totalPendingRequest = sas.totalPending();
                                
                                            System.out.println("Total Drivers: " + totalRegisteredDriver);
                                            System.out.println("Total Sub Admin: " + totalRegisteredSubAdmin);
                                            System.out.println("Total Pending Request: " + totalPendingRequest);
                                            System.out.println();
                                
                                            List<Driver> list = ds.getDriverRanking();
                                            
                                            System.out.println("Top Drivers");
                                            for (Driver d : list)
                                            {
                                                System.out.println("Rank [" + d.getranking() + "] - " + d.getfirstName() + " " + d.getlastName());
                                            }                                            
                                            break;
                                        
                                        case 2:
                                            
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    
                }
                case 2 -> {
                }
                case 3 -> {
                }
                case 0 -> System.out.println("Exit!");
                default -> {
                }
            }        
        }while(choice != 0);
    }
}
