package Service;

import Model.SuperAdmin;
import Repository.SuperAdminRepo;

public class SuperAdminService
{
    private final SuperAdminRepo saRepo = new SuperAdminRepo();
    
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
        
        if (!sa.getPassword().equals(password))
        {
            return 2;
        }
        
        return 1;
    }
    
    public boolean registerSA(String id, String fname, String lname,
                              String contactNum, String position, String photo,
                              String password, String confirmPass)
    {
        SuperAdmin sa = new SuperAdmin();
        
        if (!password.equals(confirmPass))
        {
            return false;
        }
        
        sa.setPublicID(id);
        sa.setfirstName(fname);
        sa.setlastName(lname);
        sa.setcontactNum(contactNum);
        sa.setposition(position);
        sa.setphotoURL(photo);
        sa.setPassword(password);
        
        saRepo.registerSA(sa);
        
        return true;
    }
    
    public SuperAdmin getSAData()
    {
        return saRepo.getSuperAdminData();
    }

    public int totalPending()
    {
        int total = saRepo.countPendingReq();
        
        return total;
    }
    
    public int totalApproved()
    {
        int total = saRepo.countApprovedReq();
        
        return total;
    }
    
    public int totalRejected()
    {
        int total = saRepo.countRejectedReq();
        
        return total;
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