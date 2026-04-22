package Service;

import Model.Driver;
import Repository.DriverRepo;
        
public class DriverService
{
    private final DriverRepo dRepo = new DriverRepo();
    
    public int totalDriver()
    {
        int total = dRepo.countDrivers();
        
        return total;
    }
}
