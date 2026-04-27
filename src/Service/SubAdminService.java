package Service;

import Repository.SubAdminRepo;
import java.util.ArrayList;
import java.util.List;
import Model.SubAdmin;


public class SubAdminService
{
    private final SubAdminRepo subARepo = new SubAdminRepo();
    
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
