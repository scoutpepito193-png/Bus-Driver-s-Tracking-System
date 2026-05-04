package Service;

import Model.Driver;
import Model.DriverPerformance;
import Model.DriverProfile;
import Repository.DriverRepo;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;
import util.TraccarAPI;
        
public class DriverService
{
    // ✓ Create fresh DriverRepo in each method
    // No class-level instance variable
    
    public String generateReqCode()
    {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        
        return "REQ-" + date + "-" + random;
    }
    
    public String registerDriver(String id, String fname, String lname,
                                    String gender, LocalDate dateOfBirth,
                                    String address, String contactNum,
                                    String licenseNum, LocalDate licenseExpiry,
                                    String photoURL, String password,
                                    String confirmPass)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            
            Driver d = new Driver();
            
            if (!password.trim().equals(confirmPass.trim()))
            {
                return null;
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
            
            String requestCode = generateReqCode();
            
            boolean success = dRepo.requestDriverRegistrastion(d, requestCode);
        
            if(!success)
            {
                return null;
            }
            
            return requestCode;
        } catch (Exception e) {
            System.err.println("Error registering driver: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public int totalDriver()
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            int total = dRepo.countDrivers();
            return total;
        } catch (Exception e) {
            System.err.println("Error counting drivers: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    public boolean recordDriverPerformance(String publicID, double kmpl, int ticket, double revenue)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            
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
        } catch (Exception e) {
            System.err.println("Error recording driver performance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public DriverProfile getDriverProfile(String publicID)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            
            int driverID = dRepo.getDriverIdByPublicID(publicID);

            if(driverID == -1) return null;

            Driver driver = dRepo.getDriverProfileById(driverID);
            DriverPerformance perf = dRepo.getDriverPerformance(driverID);

            DriverProfile profile = new DriverProfile();
            profile.setDriver(driver);
            profile.setPerformance(perf);

            return profile;
        } catch (Exception e) {
            System.err.println("Error getting driver profile: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    
    public List<Driver> getDriverRanking()
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            return dRepo.driverRanking();
        } catch (Exception e) {
            System.err.println("Error getting driver ranking: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<DriverPerformance> getPerformance()
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            return dRepo.driverPerformance();
        } catch (Exception e) {
            System.err.println("Error getting performance data: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Driver loginDriver(String publicDriverId, String password)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            return dRepo.driverLogin(publicDriverId, password);
        } catch (Exception e) {
            System.err.println("Error during driver login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<DriverPerformance> getDriverRecords(String publicDriverId)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            return dRepo.driverRecords(publicDriverId);
        } catch (Exception e) {
            System.err.println("Error getting driver records: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean requestDriverRemoval(String publicDriverID, String details)
    {
        try {
            DriverRepo dRepo = new DriverRepo();  // ← Fresh instance
            return dRepo.requestDriverRemoval(publicDriverID, details);
        } catch (Exception e) {
            System.err.println("Error requesting driver removal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}