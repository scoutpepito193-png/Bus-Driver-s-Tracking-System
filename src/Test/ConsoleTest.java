package Test;

import java.util.Scanner;
import Service.SuperAdminService;
import Service.SubAdminService;
import Service.DriverService;
import Model.SuperAdmin;
import Model.Driver;
import Model.DriverPerformance;
import Model.SubAdmin;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

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
                                            
                                            List<DriverPerformance> listdp = ds.getPerformance();
                                            
                                            System.out.println("ID\tName\tTotal Ticket\tRevenue\tAverage KM/L");
                                            for (DriverPerformance dp : listdp)
                                            {
                                                System.out.println(dp.getdriver().getpublic_driver_id() + "\t" + dp.getdriver().getlastName()
                                                                    + ", " + dp.getdriver().getfirstName() + "\t" + dp.gettotalTickets()
                                                                    + "\t" + dp.gettotalRevenue() + "\t" + dp.getaverageKMPL());
                                            }
                                             break;
                                             
                                        case 3:
                                            
                                            List<SubAdmin> listSub = subs.getSubAdmins();
                                            
                                            System.out.println("ID\tName\tPosition");
                                            for(SubAdmin subS : listSub)
                                            {
                                                System.out.println(subS.getpublic_sub_id() + "\t" + subS.getlastName() + ", " + subS.getfirstName()
                                                                    + "\t" + subS.getposition());
                                            }
                                            
                                            System.out.println();
                                            System.out.print("Add Sub-Admin [y/n]: ");
                                            char addSub = scan.next().charAt(0);
                                            scan.nextLine();
                                            
                                            if (addSub == 'y' || addSub == 'Y')
                                            {
                                                boolean flag = true;
                                                
                                                while(flag)
                                                {
                                                    System.out.print("ID: ");
                                                    String subID = scan.nextLine();
                                                
                                                    System.out.print("First Name: ");
                                                    String subFname = scan.nextLine();
                                                
                                                    System.out.print("Last Name: ");
                                                    String subLname = scan.nextLine();
                                                
                                                    System.out.print("Gender: ");
                                                    String subGender = scan.nextLine();
                                                
                                                    System.out.print("Date of Birth: ");
                                                    String input = scan.nextLine();
                                                    LocalDate subDateOfBirth = LocalDate.parse(input);
                                                
                                                    System.out.print("Address: ");
                                                    String subAddress = scan.nextLine();
                                                
                                                    System.out.print("Contact Number: ");
                                                    String subContactNumber = scan.nextLine();
                                                
                                                    System.out.print("Upload Photo: ");
                                                    String subPhotoURL = scan.nextLine();
                                                
                                                    System.out.print("Press [1] Confirm and [2] Clear: ");
                                                    int subProfileChoice = scan.nextInt();
                                                    scan.nextLine();
                                                    
                                                    
                                                    if(subProfileChoice == 1)
                                                    {
                                                        boolean confirm = false;
                                                        
                                                        while(confirm == false)
                                                        {
                                                            System.out.print("New Password: ");
                                                            String subPassword = scan.nextLine();
                                                            
                                                            System.out.print("Confirm Password: ");
                                                            String subConfirmPass = scan.nextLine();
                                                            
                                                            confirm = subs.registerSubAdmin(subID, subFname, subLname, subGender, subDateOfBirth, subAddress, subContactNumber, subPhotoURL, subPassword, subConfirmPass);
                                                            
                                                            if (confirm == false)
                                                            {
                                                                System.out.println("Password don't match");
                                                            }
                                                            else
                                                            {
                                                                flag = false;
                                                            }
                                                        }
                                                    }
                                                }
                                                
                                            }
                                            else
                                            {
                                                break;
                                            }
                                            
                                            
                                            break;
                                            
                                        case 0:
                                            
                                            inSADashboard = false;
                                            System.out.println("Sign Out!");
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    
                    
                }
                case 2 ->
                {
                    int attemp = 0;
                    boolean loggedIn = false;
                    
                    while(attemp != 3 && !loggedIn)
                    {
                        System.out.println("Sub-Admin Log-In");
                        System.out.println("[0] Cancel");
                        System.out.print("ID: ");
                        String publicSubID = scan.nextLine();
                        
                        if(publicSubID.equals("0"))
                        {
                            break;
                        }
                        
                        System.out.print("Password: ");
                        String subPass = scan.nextLine();
                        
                        SubAdmin sub = subs.subAdminLogIn(publicSubID, subPass);
                        
                        if(sub != null)
                        {
                            loggedIn = true;
                            
                            System.out.println("Sub-Admin Dashboard");
                            
                        }
                    }
                    
                    /*System.out.println("Sub-Admin Dashboard");
                    System.out.print("[1]Overview   [2]Driver   [3]My Request   [4] LeaderBoard]");*/
                    
                    
                    
                }
                case 3 -> 
                {
                        int attemp = 0;
                        boolean loggedIn = false;

                        while (attemp != 3 && !loggedIn)
                        {
                            System.out.println("Driver Log-In");
                            System.out.println("[0] Cancel");
                            System.out.print("ID: ");
                            String driverId = scan.nextLine();

                            if (driverId.equals("0"))
                            {
                                break;
                            }

                            System.out.print("Password: ");
                            String password = scan.nextLine();

                            Driver driver = ds.loginDriver(driverId, password);

                            if (driver != null)
                            {
                                loggedIn = true;

                                System.out.println("\nDriver Dashboard");
                                System.out.println("Name: " + driver.getfirstName() + " " + driver.getlastName());
                                System.out.println("ID#: " + driver.getpublic_driver_id());

                                boolean inDriverDashboard = true;
                                while (inDriverDashboard)
                                {
                                    System.out.println("\n[1] Profile  [2] Records  [3] Leaderboard  [0] Sign Out");
                                    System.out.print("Enter Choice: ");
                                    int driverChoice = scan.nextInt();
                                    scan.nextLine();

                                    switch (driverChoice)
                                    {
                                        case 1 ->
                                        {
                                            System.out.println("\n===== Driver Profile =====");
                                            System.out.println("Name          : " + driver.getfirstName() + " " + driver.getlastName());
                                            System.out.println("ID#           : " + driver.getpublic_driver_id());
                                            System.out.println("Gender        : " + driver.getgender());
                                            System.out.println("Date of Birth : " + driver.getdateOfBirth());
                                            System.out.println("Address       : " + driver.getaddress());
                                            System.out.println("Contact       : " + driver.getcontactNumber());
                                            System.out.println("License No.   : " + driver.getlicenseNum());
                                            System.out.println("License Expiry: " + driver.getlicenseExpiry());
                                        }
                                        case 2 ->
                                        {
                                            List<DriverPerformance> records = ds.getDriverRecords(driver.getpublic_driver_id());
                                            System.out.println("\n===== Driver Records =====");
                                            System.out.printf("%-15s %-15s %-15s%n", "Tickets", "Revenue", "Avg KMPL");
                                            System.out.println("=".repeat(45));
                                            for (DriverPerformance dp : records)
                                            {
                                                System.out.printf("%-15d %-15.2f %-15.2f%n",
                                                dp.gettotalTickets(),
                                                dp.gettotalRevenue(),
                                                dp.getaverageKMPL());
                                            }
                                        }
                                        case 3 ->
                                        {
                                            List<Driver> ranking = ds.getDriverRanking();
                                            System.out.println("\n===== Leaderboard =====");
                                            for (Driver d : ranking)
                                            {
                                                System.out.println("Rank [" + d.getranking() + "] - " + d.getfirstName() + " " + d.getlastName());
                                            }
                                        }
                                        case 0 ->
                                        {
                                            System.out.println("Signing out...");
                                            inDriverDashboard = false;
                                        }
                                        default -> System.out.println("Invalid choice.");
                                   }
                               }
                            }
                            else
                            {
                                System.out.println("Wrong Credentials!");
                                ++attemp;
                            }
                        }

                        if (attemp == 3)
                        {
                            System.out.println("Too many failed attempts. Access locked.");
                        }
                }
                case 0 -> System.out.println("Exit!");
                default -> {
                }
            }        
        }while(choice != 0);
    }
}
