package Service;

import Model.SuperAdmin;
import Repository.SuperAdminRepo;

public class SuperAdminService
{
    private SuperAdminRepo saRepo = new SuperAdminRepo();
    
    public boolean checkAccout()
    {
        return saRepo.checkExistingSA();
    }
    
    public int logIn(String publicID, String password)
    {
        SuperAdmin sa = saRepo.logInRepo(publicID);
        
        if (sa == null)
        {
            return 0;
        }
        
        if (!sa.password.equals(password))
        {
            return 2;
        }
        
        return 1;
    }
    
    /*public boolean registerSA(String id, String fname, String lname,
                            String contactNum, String position, String password,
                            String confirmPass)
    {
        if (!password.equals(confirmPass))
        {
            return false;
        }
        sa.publicID = id;
        sa.firstName = fname;
        sa.lastName = lname;
        sa.contactNum = contactNum;
        sa.position = position;        
        sa.password = password;
        
        return true;
    }
    
    public boolean logIn(String id, String regID, String password, String regPass)
    {
        return id.equals(regID) && password.equals(regPass);
    }
    
    public void dashboardOverview()
    {
         
    }*/
    
    
}