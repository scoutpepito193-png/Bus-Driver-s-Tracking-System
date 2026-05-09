package Test;

import java.util.Scanner;
import Service.SuperAdminService;
import Service.SubAdminService;
import Service.DriverService;
import Service.LogInService;
import Model.SuperAdmin;
import Model.Driver;
import Model.DriverPerformance;
import Model.SubAdmin;
import Model.Request;
import Model.AuthResult;
import Model.DriverProfile;
import Service.DriverAttendanceService;
import Service.SalaryService;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import util.Session;
import org.json.JSONObject;

public class ConsoleTest
{   
    
    
    public static void main(String[] args)
    {
        LogInService logs = new LogInService();
    

        Scanner scan = new Scanner(System.in);
        SuperAdminService sas = new SuperAdminService();
        SubAdminService subs = new SubAdminService();
        DriverService ds = new DriverService();
        LogInService logS = new LogInService();
        
        int choice;        
        do
        {
            System.out.println("Bus Driver's Tracking System");
            System.out.println("1. Log In");
            System.out.println("0. Exit");
        
            System.out.print("Enter Choice: ");
            choice = scan.nextInt();
            scan.nextLine();
            
            
        
            if(choice == 1)
            {
                boolean verify = sas.checkAccout();
                
                if(verify == false)
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
                            while (!confirm)
                            {
                                System.out.println("New Password: ");
                                String password = scan.nextLine();
                                
                                System.out.println("Confirm Password: ");
                                String confirmPass = scan.nextLine();
                                
                                confirm = sas.registerSA(id, fname, lname, contactNum, position, photo, password, confirmPass);
                                
                                if (!confirm)
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
                
                boolean inLogIn = false;
                int attempt = 0;
                while(attempt < 3 && !inLogIn)
                {
                    System.out.println("[0]Back");
                    System.out.print("ID Number: ");
                    String idNum = scan.nextLine();
                
                    if(idNum.equals("0"))
                    {
                        break;
                    }
                
                    System.out.print("Password: ");
                    String password = scan.nextLine();
                    
                    attempt++;
                    AuthResult result = logs.logIn(idNum, password);
                    
                    if(result == null)
                    {
                        System.out.println("Invalid credentials! Attempt " + attempt + " of 3");
                        continue;
                    }
                    
                    inLogIn = true;
                    
                    switch (result.getRole())
                    {
                        case SUPER_ADMIN ->
                        {
                            SuperAdmin saOutput = sas.getSAData();
                            System.out.println("Super Admin Dashboard");
                            System.out.println("Profile Picture: " + saOutput.getphotoURL());
                            System.out.println("Name: " + saOutput.getfirstName() + " " + saOutput.getlastName());
                            System.out.println("Position: " + saOutput.getposition());
                            System.out.println("ID: " + saOutput.getPublicID());
                            System.out.println();

                            System.out.println("[1] Overview    [2] Driver    [3] Sub Admin       [4] Request       [5] Search       [0] Sign Out");
                            System.out.println();

                            boolean inSADashboard = true;

                            while (inSADashboard)
                            {
                                System.out.print("Enter Choice: ");
                                int superAdminDBchoice = scan.nextInt();
                                scan.nextLine();

                                OUTER:
                                switch (superAdminDBchoice)
                                {
                                    case 1 ->
                                    {
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
                                    }
                                    case 2 ->
                                    {
                                        List<DriverPerformance> listdp = ds.getPerformance();

                                        System.out.println("ID\tName\tTotal Ticket\tRevenue\tAverage KM/L");
                                        for (DriverPerformance dp : listdp)
                                        {
                                            System.out.println(dp.getdriver().getpublic_driver_id() + "\t" + dp.getdriver().getlastName()
                                                    + ", " + dp.getdriver().getfirstName() + "\t" + dp.gettotalTickets()
                                                    + "\t" + dp.gettotalRevenue() + "\t" + dp.getaverageKMPL());
                                        }
                                    }
                                    case 3 ->
                                    {
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
                                        switch (addSub) {
                                            case 'y', 'Y' ->
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

                                                        while(!confirm)
                                                        {
                                                            System.out.print("New Password: ");
                                                            String subPassword = scan.nextLine();

                                                            System.out.print("Confirm Password: ");
                                                            String subConfirmPass = scan.nextLine();

                                                            confirm = subs.registerSubAdmin(subID, subFname, subLname, subGender, subDateOfBirth, subAddress,
                                                                    subContactNumber, subPhotoURL, subPassword, subConfirmPass);

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
                                            case 'n', 'N' ->
                                            {
                                                break OUTER;
                                            }
                                            default -> System.out.println("Invalid Input");
                                        }
                                    }
                                    case 4 ->
                                    {
                                        List<Request> listReq = sas.getAllRequest();

                                        System.out.println("Request Code\t\tRequest Info\t\tStatus");
                                        for (Request req : listReq)
                                        {
                                            System.out.println(req.getRequestCode() + "\t" + req.getRequestInfo() + "\t" + req.getStatus());
                                        }

                                        System.out.print("Enter Request Code or [0]To Exit: ");
                                        String viewRequest = scan.nextLine().trim();

                                        if(viewRequest.equals("0"))
                                        {
                                            break;
                                        }

                                        Request req = sas.getRequest(viewRequest);

                                        if(req == null)
                                        {
                                            System.out.println("Request not found!");
                                            break;
                                        }

                                        String type = req.getRequestInfo();

                                        if("DRIVER REGISTRATION".equals(type))
                                        {
                                            Driver d = (Driver) sas.getReqDetails(viewRequest);
                                            System.out.println("ID: " + d.getpublic_driver_id());
                                            System.out.println("Name: " + d.getfirstName() + " " + d.getlastName());
                                            System.out.println("Gender: " + d.getgender());
                                            System.out.println("Date Of Birth: " + d.getdateOfBirth());
                                            System.out.println("Address: " + d.getaddress());
                                            System.out.println("Contact Number: " + d.getcontactNumber());
                                            System.out.println("License Number: " + d.getlicenseNum());
                                            System.out.println("License Expiry: " + d.getlicenseExpiry());
                                        }
                                        else if("REMOVE DRIVER".equals(type))
                                        {
                                            Map<String, String> data = (Map<String, String>) sas.getReqDetails(viewRequest);
                                            System.out.println("Reason of Removal: " + data.get("reason"));
                                        }
                                        else
                                        {
                                            System.out.println("Cant Find Type of Request");
                                        }

                                        System.out.println("[1]Approve [2]Reject [0]Back");
                                        System.out.print("Choice: ");
                                        int approveORreject = scan.nextInt();
                                        scan.nextLine();

                                        switch(approveORreject)
                                        {
                                            case 1 ->
                                            {
                                                boolean approved = sas.approveRequest(viewRequest);

                                                if(approved == true)
                                                {
                                                    System.out.println("Request Approved");
                                                }
                                                else
                                                {
                                                    System.out.println("Approval Failed");
                                                }
                                            }

                                            case 2 ->
                                            {                                           
                                                boolean rejected = sas.rejectRequest(viewRequest);

                                                if(rejected == true)
                                                {
                                                    System.out.println("Request Rejected");
                                                }
                                                else
                                                {
                                                    System.out.println("Rejection Failed");
                                                }
                                            }

                                            case 0 ->
                                            {
                                                break;
                                            }

                                            default -> System.out.println("Invalid Input");

                                        }
                                    }

                                    case 5 ->
                                    {
                                        System.out.println("\n[1] Search Driver");
                                        System.out.println("[2] Search Sub Admin");
                                        System.out.print("Choose: ");
                                        int searchType = scan.nextInt();
                                        scan.nextLine();

                                        System.out.println("[1] Search by Name");
                                        System.out.println("[2] Search by ID");
                                        System.out.print("Choose: ");
                                        int searchChoice = scan.nextInt();
                                        scan.nextLine();

                                        if (searchType == 1)
                                        {
                                            Driver foundDriver = null;

                                            if (searchChoice == 1)
                                            {
                                                System.out.print("Enter Driver Name: ");
                                                String name = scan.nextLine();
                                                foundDriver = subs.searchDriverByName(name);
                                            }
                                            else if (searchChoice == 2)
                                            {
                                                System.out.print("Enter Driver ID: ");
                                                String driverId = scan.nextLine();
                                                foundDriver = subs.searchDriverById(driverId);
                                            }

                                            if (foundDriver != null)
                                            {
                                                System.out.println("\n===== Driver Info =====");
                                                System.out.println("Name          : " + foundDriver.getfirstName() + " " + foundDriver.getlastName());
                                                System.out.println("ID#           : " + foundDriver.getpublic_driver_id());
                                                System.out.println("Gender        : " + foundDriver.getgender());
                                                System.out.println("Date of Birth : " + foundDriver.getdateOfBirth());
                                                System.out.println("Address       : " + foundDriver.getaddress());
                                                System.out.println("Contact       : " + foundDriver.getcontactNumber());
                                                System.out.println("License No.   : " + foundDriver.getlicenseNum());
                                                System.out.println("License Expiry: " + foundDriver.getlicenseExpiry());

                                                List<DriverPerformance> records = subs.searchDriverRecords(foundDriver.getpublic_driver_id());
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
                                            else
                                            {
                                                System.out.println("Driver not found.");
                                            }
                                        }
                                        else if (searchType == 2)
                                        {
                                            SubAdmin foundSub = null;

                                            if (searchChoice == 1)
                                            {
                                                System.out.print("Enter Sub Admin Name: ");
                                                String name = scan.nextLine();
                                                foundSub = subs.searchSubAdminByName(name);
                                            }
                                            else if (searchChoice == 2)
                                            {
                                                System.out.print("Enter Sub Admin ID: ");
                                                String subId = scan.nextLine();
                                                foundSub = subs.searchSubAdminById(subId);
                                            }

                                            if (foundSub != null)
                                            {
                                                System.out.println("\n===== Sub Admin Info =====");
                                                System.out.println("Name    : " + foundSub.getfirstName() + " " + foundSub.getlastName());
                                                System.out.println("ID#     : " + foundSub.getpublic_sub_id());
                                                System.out.println("Gender  : " + foundSub.getgender());
                                                System.out.println("Address : " + foundSub.getaddress());
                                                System.out.println("Contact : " + foundSub.getcontactNum());
                                                System.out.println("Position: " + foundSub.getposition());
                                            }
                                            else
                                            {
                                                System.out.println("Sub Admin not found.");
                                            }
                                        }
                                    }
                                    
                                    case 0 ->
                                    {
                                        inSADashboard = false;
                                        System.out.println("Sign Out!");
                                    }
                                }
                            }
                        }

                        case SUB_ADMIN ->
                        {
                            SubAdmin subAdmin = (SubAdmin) result.getUser();
                            
                            if(subAdmin != null)
                            {
                                Session.currentSubAdmin = subAdmin;
                                
                                System.out.println("SubAdmin Dashboard");
                                System.out.println("Name: " + subAdmin.getfirstName() + " " + subAdmin.getlastName());
                                System.out.println("Position: " + subAdmin.getposition());
                                System.out.println("ID: " + subAdmin.getpublic_sub_id());
                                System.out.println();

                                System.out.println("[1]Overview   [2]Driver   [3]My Request   [4] LeaderBoard    [5] Search    [0] Sign-Out");
                                System.out.println();

                                boolean inDashboard = true;

                                while(inDashboard)
                                {
                                    System.out.print("Enter Choice: ");
                                    int inDashboardChoice = scan.nextInt();
                                    scan.nextLine();

                                    switch(inDashboardChoice)
                                    {
                                        case 1:
                                            System.out.println("Total Driver: ");
                                            System.out.println("Active Drivers: ");
                                            System.out.println("Inactive Drivers: ");
                                            System.out.println("Violations Logged: ");

                                            List<Driver> list = ds.getDriverRanking();

                                            System.out.println("Top Drivers");
                                            for (Driver d : list)
                                            {
                                                System.out.println("Rank [" + d.getranking() + "] - " + d.getfirstName() + " " + d.getlastName());
                                            }

                                            break;

                                        case 2:

                                            System.out.println("[1] Add Driver      [2] Record Performance      [3] View Driver");
                                            System.out.print("Enter Choice: ");
                                            int addORrecordChoice = scan.nextInt();
                                            scan.nextLine();

                                            if(addORrecordChoice == 1)
                                            {
                                                boolean flag = true;

                                                while(flag)
                                                {
                                                    System.out.print("ID: ");
                                                    String driverID = scan.nextLine();

                                                    System.out.print("First Name: ");
                                                    String driverfName = scan.nextLine();

                                                    System.out.print("Last Name: ");
                                                    String driverlName = scan.nextLine();

                                                    System.out.print("Gender: ");
                                                    String driverGender = scan.nextLine();

                                                    System.out.print("Date of Birth: ");
                                                    String input = scan.nextLine();
                                                    LocalDate driverDateOfBirth = LocalDate.parse(input);

                                                    System.out.print("Address: ");
                                                    String driverAddress = scan.nextLine();

                                                    System.out.print("Contact Number: ");
                                                    String driverContactNumber = scan.nextLine();

                                                    System.out.print("License Number: ");
                                                    String driverLicenseNum = scan.nextLine();

                                                    System.out.print("License Expiry Date: ");
                                                    String input2 = scan.nextLine();
                                                    LocalDate driverLicenseExpiry = LocalDate.parse(input2);

                                                    System.out.print("Upload Photo: ");
                                                    String driverPhotoURL = scan.nextLine();

                                                    System.out.print("Press [1]Confirm or [2]Clear: ");
                                                    int driverAddChoice = scan.nextInt();
                                                    scan.nextLine();

                                                    if(driverAddChoice == 1)
                                                    {
                                                        boolean confirm = false;

                                                        while(confirm == false)
                                                        {
                                                            System.out.print("New Password: ");
                                                            String driverPass = scan.nextLine();

                                                            System.out.print("Confirm Password: ");
                                                            String driverConfirmPass = scan.nextLine();

                                                            String reqCode = ds.registerDriver(driverID, driverfName, driverlName, driverGender, driverDateOfBirth, driverAddress,
                                                                    driverContactNumber, driverLicenseNum, driverLicenseExpiry, driverPhotoURL, driverPass, driverConfirmPass);

                                                            if(reqCode != null)
                                                            {
                                                                flag = false;
                                                                confirm = true;
                                                                System.out.println("Driver Registration Submitted!");
                                                            }
                                                            else
                                                            {
                                                                System.out.println("Password doesn't match");
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (addORrecordChoice == 2)
                                            {

                                                boolean flag = false;

                                                while(flag == false)
                                                {
                                                    System.out.print("Enter Driver ID: ");
                                                    String d_ID = scan.nextLine();

                                                    System.out.print("Average KM/L: ");
                                                    double aveKMPL = scan.nextDouble();

                                                    System.out.print("Total Ticket: ");
                                                    int totalTickets = scan.nextInt();

                                                    System.out.print("Total Revenue: ");
                                                    double totalRevenue = scan.nextDouble();
                                                    scan.nextLine();

                                                    flag = ds.recordDriverPerformance(d_ID, aveKMPL, totalTickets, totalRevenue);

                                                    if(flag == false)
                                                    {
                                                        System.out.println("Driver not Found");
                                                    }

                                                }
                                            }

                                            List<DriverPerformance> listdp = ds.getPerformance();

                                            System.out.println("ID\tName\tTotal Ticket\tRevenue\tAverage KM/L");
                                            for (DriverPerformance dp : listdp)
                                            {
                                                System.out.println(dp.getdriver().getpublic_driver_id() + "\t" + dp.getdriver().getlastName()
                                                        + ", " + dp.getdriver().getfirstName() + "\t" + dp.gettotalTickets()
                                                        + "\t" + dp.gettotalRevenue() + "\t" + dp.getaverageKMPL());
                                            }

                                            System.out.print("Remove Driver [y/n]: ");
                                            char removeDriver = scan.next().charAt(0);
                                            scan.nextLine();

                                            if (removeDriver == 'y' || removeDriver == 'Y')
                                            {
                                                System.out.print("Enter Driver ID to Remove: ");
                                                String publicDriverID = scan.nextLine();

                                                System.out.print("Reason of Removal: ");
                                                String reason = scan.nextLine();

                                                boolean succes = ds.requestDriverRemoval(publicDriverID, reason);

                                                if(succes)
                                                {
                                                    System.out.println("Request Sent");
                                                }

                                                else
                                                {
                                                    System.out.println("Failed");
                                                }
                                            }

                                            System.out.println("View Driver Profile [y/n]: ");
                                            char viewDriverProfile = scan.next().charAt(0);
                                            scan.nextLine();

                                            if(viewDriverProfile == 'y' || viewDriverProfile == 'Y')
                                            {
                                                System.out.println("Enter Driver ID: ");
                                                String dID = scan.nextLine();

                                                DriverProfile profile = ds.getDriverProfile(dID);

                                                if(profile != null)
                                                {
                                                    Driver d = profile.getDriver();
                                                    DriverPerformance p = profile.getPerformance();

                                                    System.out.println("\n===== DRIVER PROFILE =====");
                                                    System.out.println("Name: " + d.getfirstName() + " " + d.getlastName());
                                                    System.out.println("ID: " + d.getpublic_driver_id());
                                                    System.out.println("Gender: " + d.getgender());
                                                    System.out.println("DOB: " + d.getdateOfBirth());
                                                    System.out.println("Address: " + d.getaddress());
                                                    System.out.println("Contact: " + d.getcontactNumber());

                                                    System.out.println("\n--- PERFORMANCE ---");
                                                    System.out.println("Tickets: " + p.gettotalTickets());
                                                    System.out.println("Revenue: " + p.gettotalRevenue());
                                                    System.out.println("KM/L: " + p.getaverageKMPL());
                                                }

                                                else
                                                {
                                                    System.out.println("Driver not found.");
                                                }
                                            }

                                            System.out.print("Update Driver Salary [y/n]: ");
                                            char updateSalary = scan.next().charAt(0);
                                            scan.nextLine();

                                            if(updateSalary == 'y' || updateSalary == 'Y')
                                            {
                                                SalaryService salaryService = new SalaryService();
                                                salaryService.processDailySalary();
                                            }

                                            else
                                            {
                                                System.out.println("Failed");
                                            }

                                            System.out.println("View Driver Location [y/n]: ");
                                            char viewDriverLoc = scan.next().charAt(0);
                                            scan.nextLine();

                                            if(viewDriverLoc == 'y' || viewDriverLoc == 'Y')
                                            {
                                                System.out.println("Enter Driver ID: ");
                                                String publicId = scan.nextLine();

                                                System.out.println("Starting live tracking... (Press Ctrl + C to stop)");

                                                subs.startTracking(publicId, (lat, lng) ->
                                                {
                                                    if (Double.isNaN(lat) || Double.isNaN(lng))
                                                    {
                                                        System.out.println("No location found (driver not linked or offline).");
                                                        return;
                                                    }

                                                    System.out.println("=== DRIVER LOCATION ===");
                                                    System.out.println("Latitude: " + lat);
                                                    System.out.println("Longitude: " + lng);

                                                });
                                            }
                                            else
                                            {
                                                break;
                                            }

                                            break;

                                        case 3:

                                            System.out.println("\n===== MY REQUESTS =====");

                                            List<Request> myList = subs.getMyRequests();

                                            if (myList.isEmpty())
                                            {
                                                System.out.println("No pending requests.");
                                            }

                                            else
                                            {
                                                System.out.println("Request Code\tInfo\tStatus");

                                                for (Request req : myList)
                                                {
                                                    System.out.println(req.getRequestCode() + "\t" + req.getRequestInfo() + "\t" + req.getStatus());
                                                }
                                            }
                                            break;
                                        case 4:
                                        {
                                            ds.updateRanking();
                                            List<Driver> ranking = ds.getDriverRanking();
                                            System.out.println("\n===== Leaderboard =====");
                                            for (Driver d : ranking)
                                            {
                                                System.out.println("Rank [" + d.getranking() + "] - " + d.getfirstName() + " " + d.getlastName());
                                            }
                                        }
                                        break;

                                        case 5:
                                        {
                                            System.out.println("\n[1] Search by Name");
                                            System.out.println("[2] Search by ID");
                                            System.out.print("Choose: ");
                                            int searchChoice = scan.nextInt();
                                            scan.nextLine();

                                            Driver foundDriver = null;

                                            if (searchChoice == 1)
                                            {
                                                System.out.print("Enter Driver Name: ");
                                                String name = scan.nextLine();
                                                foundDriver = subs.searchDriverByName(name);
                                            }
                                            else if (searchChoice == 2)
                                            {
                                                System.out.print("Enter Driver ID: ");
                                                String id = scan.nextLine();
                                                foundDriver = subs.searchDriverById(id);
                                            }

                                            if (foundDriver != null)
                                            {
                                                System.out.println("\n===== Driver Info =====");
                                                System.out.println("Name          : " + foundDriver.getfirstName() + " " + foundDriver.getlastName());
                                                System.out.println("ID#           : " + foundDriver.getpublic_driver_id());
                                                System.out.println("Gender        : " + foundDriver.getgender());
                                                System.out.println("Date of Birth : " + foundDriver.getdateOfBirth());
                                                System.out.println("Address       : " + foundDriver.getaddress());
                                                System.out.println("Contact       : " + foundDriver.getcontactNumber());
                                                System.out.println("License No.   : " + foundDriver.getlicenseNum());
                                                System.out.println("License Expiry: " + foundDriver.getlicenseExpiry());

                                                List<DriverPerformance> records = subs.searchDriverRecords(foundDriver.getpublic_driver_id());
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
                                            else
                                            {
                                                System.out.println("Driver not found.");
                                            }
                                        }
                                        break;

                                        case 0:
                                            inDashboard = false;
                                            break;
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("Failed to load Sub Admin data");
                            }
                        }

                        case DRIVER ->
                        {
                            Driver driver = (Driver) result.getUser();
                            
                            DriverProfile driverProfile = ds.getDriverProfile(driver.getpublic_driver_id());
                            
                            driver = driverProfile.getDriver();
                            
                            DriverAttendanceService attendanceService = new DriverAttendanceService();
                            attendanceService.markDriverPresent(driver.getpublic_driver_id());

                            System.out.println("\nDriver Dashboard");
                            System.out.println("Name: " + driver.getfirstName() + " " + driver.getlastName());
                            System.out.println("ID#: " + driver.getpublic_driver_id());

                            boolean inDriverDashboard = true;
                            SalaryService salaryService = new SalaryService();
                            DriverAttendanceService attendanceService2 = new DriverAttendanceService();

                            while (inDriverDashboard)
                            {
                                System.out.println("\n[1] Profile  [2] Records  [3] Leaderboard  [4] Salary     [0] Sign Out");
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
                                        ds.updateRanking();
                                        List<Driver> ranking = ds.getDriverRanking();
                                        System.out.println("\n===== Leaderboard =====");
                                        for (Driver d : ranking)
                                        {
                                            System.out.println("Rank [" + d.getranking() + "] - " + d.getfirstName() + " " + d.getlastName());
                                        }
                                    }

                                    case 4 ->
                                    {
                                        int driverID = ds.getDriverId(driver.getpublic_driver_id());

                                        double salary = salaryService.getMonthlySalary(driverID);
                                        double bonus = salaryService.computeBonus(driverID);
                                        boolean fullAttendance = attendanceService2.isFullAttendance(driverID);

                                        System.out.println("\n===== SALARY DASHBOARD =====");

                                        System.out.println("Monthly Salary : ₱" + salary);

                                        if (fullAttendance)
                                        {
                                            System.out.println("Bonus          : ₱" + bonus + " (Full Attendance ✅)");
                                        }

                                        else
                                        {
                                            System.out.println("Bonus          : ₱0 (Not Eligible ❌)");
                                        }

                                        System.out.println("-----------------------------");
                                        System.out.println("Total Pay      : ₱" + (salary + bonus));
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
                    }
                }
            }
            else if (choice == 0)
            {
                break;
            }
            else
            {
                System.out.println("Invalid Input");
            }
        }while(choice != 0);
    }
}
