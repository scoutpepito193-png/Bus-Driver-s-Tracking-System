package Test;

import java.util.Scanner;
import Service.SuperAdminService;

public class ConsoleTest
{   
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        SuperAdminService sas = new SuperAdminService();
        
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
                OUTER:
                while (attemp != 3) {
                    System.out.println("Log-In");
                    System.out.print("ID: ");
                    String id = scan.nextLine();
                    
                    System.out.print("Password: ");
                    String password = scan.nextLine();
                    int logIn = sas.logIn(id, password);
                    
                    switch (logIn) {
                        case 0 -> {
                            System.out.println("Wrong Credentials!");
                            ++attemp;
                           }
                        case 2 -> System.out.println("Account Not Found");
                        default -> {
                            break OUTER;
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
