package Service;

import Repository.SubAdminRepo;


public class SubAdminService
{
    private final SubAdminRepo subARepo = new SubAdminRepo();
    
    public int totalSubAdmin()
    {
        int total = subARepo.countSubAdmin();
        
        return total;
    }
}
