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
                   /*
                   int attempt = 0;
                   while (attempt != 3)
                    {
                        System.out.println("Log-In");
                        System.out.print("ID: ");
                        String id = scan.nextLine();
                   
                        System.out.print("Password: ");
                        String password = scan.nextLine();
                        
                        boolean logIn = sas.logIn(id, regID, password, regPass);
                        
                        if (logIn  == false)
                        {
                            ++attempt;
                        }
                        else
                        {
                            logCount++;
                            break;
                        }
                    }
                   
                   if (logCount == 1)
                   {
                            boolean flag = true;
                           
                            while (flag)
                            {
                                System.out.println("Set-Up Profile");
                                System.out.print("ID: ");
                                String id = scan.nextLine();
                                
                                System.out.print("First Name: ");
                                String fname = scan.nextLine();
                           
                                System.out.println("Last Name: ");
                                String lname = scan.nextLine();
                           
                                System.out.println("Contact Number: ");
                                String contactNum = scan.nextLine();
                           
                                System.out.println("Position: ");
                                String position = scan.nextLine();
                                
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
                                    
                                        confirm = sas.registerSA(id, fname, lname, contactNum, position, password, confirmPass);
                                    
                                        if (confirm == false)
                                        {
                                            System.out.println("Password doesn't match");
                                        }
                                        else
                                        {
                                            regID = id;
                                            regPass = password;
                                            confirm = true;
                                            flag = false;
                                        }
                                        
                                    }
                                }
                            }                       
                   }
                    
                    
                           
                   /*while ()
                   
                   if (id.equals(regID) && password.equals(regPass))
                   {
                       logCount++;
                       
                       if (logCount == 0)
                        {

                            
                        }
                        System.out.println("");
                   }
                   else if (!id.equals(regID))
                   {
                       System.out.println("Wrong ID");
                   }
                   else if (!password.equals(regPass))
                   {
                       System.out.println("Wrong Password");
                   }
                   else
                   {
                       System.out.println("Wrong Credentials");
                   }*/
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
