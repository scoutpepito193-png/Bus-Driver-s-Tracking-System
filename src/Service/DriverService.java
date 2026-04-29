package Service;

import Model.Driver;
import Model.DriverPerformance;
import Repository.DriverRepo;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
        
public class DriverService
{
    private final DriverRepo dRepo = new DriverRepo();
    
    public boolean registerDriver(String id, String fname, String lname,
                                    String gender, LocalDate dateOfBirth,
                                    String address, String contactNum,
                                    String licenseNum, LocalDate licenseExpiry,
                                    String photoURL, String password,
                                    String confirmPass)
    {
        Driver d = new Driver();
        
        if (!password.equals(confirmPass))
        {
            return false;
        }
        
        d.setpublic_driver_id(id);
        d.setfirstName(fname);
        d.setlastName(lname);
        d.setgender(gender);
        d.setdateOfBirth(dateOfBirth);
        d.setaddress(address);
        d.setcontactNumber(contactNum);
        d.setlicenseNum(licenseNum);
        d.setlicenseExpiry(licenseExpiry);
        d.setphotoURL(photoURL);
        d.setpassword(password);
        
        dRepo.requestDriverRegistrastion(d);
        
        return true;        
    }
    
    public int totalDriver()
    {
        int total = dRepo.countDrivers();
        
        return total;
    }
    
    public boolean recordDriverPerformance(String publicID, double kmpl, int ticket, double revenue)
    {
        int driverID = dRepo.getDriverIdByPublicID(publicID);
        
        if(driverID == -1)
        {
            return false;
        }
        
        Driver d = new Driver();
        d.setdriverID(driverID);
        
        DriverPerformance dp = new DriverPerformance();
        dp.setdriver(d);
        
        dp.setaverageKMPL(kmpl);
        dp.settotalTickets(ticket);
        dp.settotalRevenue(revenue);
        
        dRepo.enterDriverPerformance(dp);
        
        return true;
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
