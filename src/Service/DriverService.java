package Service;

import Model.Driver;
import Repository.DriverRepo;
import java.util.ArrayList;
import java.util.List;
        
public class DriverService
{
    private final DriverRepo dRepo = new DriverRepo();
    
    public int totalDriver()
    {
        int total = dRepo.countDrivers();
        
        return total;
    }
    
    public List<Driver> getDriverRanking()
    {
        return dRepo.driverRanking();
    }
}
