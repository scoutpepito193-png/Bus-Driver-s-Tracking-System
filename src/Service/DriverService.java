package Service;

import Model.Driver;
import Model.DriverPerformance;
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
    
    public List<DriverPerformance> getPerformance()
    {
        return dRepo.driverPerformance();
    }
    public Driver loginDriver(String publicDriverId, String password)
    {
    return dRepo.driverLogin(publicDriverId, password);
    }

    public List<DriverPerformance> getDriverRecords(String publicDriverId)
    {
    return dRepo.driverRecords(publicDriverId);
    }
}
