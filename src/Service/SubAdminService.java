package Service;

import Repository.SubAdminRepo;
import java.util.ArrayList;
import java.util.List;
import Model.SubAdmin;
import java.time.LocalDate;


public class SubAdminService
{
    private final SubAdminRepo subARepo = new SubAdminRepo();
    
    
    public boolean registerSubAdmin(String id, String fname, String lname,
                                    String gender, LocalDate dateOfBirth,
                                    String address, String contactNum, 
                                    String photoURL, String password,
                                    String confirmPass)
    {
        SubAdmin subA = new SubAdmin();
        
        if (!password.equals(confirmPass))
        {
            return false;
        }
        
        subA.setpublic_sub_id(id);
        subA.setfirstName(fname);
        subA.setlastName(lname);
        subA.setgender(gender);
        subA.setdateOfBirth(dateOfBirth);
        subA.setaddress(address);
        subA.setcontactnum(contactNum);
        subA.setphotoURL(photoURL);
        subA.setpassword(password);
        
        subARepo.registerSubAdmin(subA);
        
        return true;
    }
    
    
    /*public boolean subAdminLogIn(String publicID, String password)
    {
        
    }*/
    
    public int totalSubAdmin()
    {
        int total = subARepo.countSubAdmin();
        
        return total;
    }
    
    public List<SubAdmin> getSubAdmins()
    {
        return subARepo.getSubAdmins();
    }
}
