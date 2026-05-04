package Service;

import Repository.SubAdminRepo;
import util.TraccarAPI;
import Model.Driver;
import Repository.DriverRepo;
import java.util.ArrayList;
import java.util.List;
import util.Session;
import org.json.JSONObject;
import Model.SubAdmin;
import Model.Request;
import java.time.LocalDate;


public class SubAdminService
{
    // ✓ Keep TraccarAPI calls as class variable (no DB connections)
    // But create fresh DriverRepo in each method (DB connections)
    
    public boolean registerSubAdmin(String id, String fname, String lname,
                                    String gender, LocalDate dateOfBirth,
                                    String address, String contactNum, 
                                    String photoURL, String password,
                                    String confirmPass)
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
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
        } catch (Exception e) {
            System.err.println("Error registering SubAdmin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    public SubAdmin subAdminLogIn(String publicID, String password)
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
            return subARepo.subAdminLogIn(publicID, password);
        } catch (Exception e) {
            System.err.println("Error during SubAdmin login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // ADD THIS METHOD
    public SubAdmin login(String adminId, String password)
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
            return subARepo.subAdminLogIn(adminId, password);
        } catch (Exception e) {
            System.err.println("Error during SubAdmin login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public int totalSubAdmin()
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
            int total = subARepo.countSubAdmin();
            return total;
        } catch (Exception e) {
            System.err.println("Error counting SubAdmins: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    public List<SubAdmin> getSubAdmins()
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
            return subARepo.getSubAdmins();
        } catch (Exception e) {
            System.err.println("Error fetching SubAdmins: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Request> getMyRequests()
    {
        try {
            SubAdminRepo subARepo = new SubAdminRepo();  // ← Fresh instance
            int subAdminID = Session.currentSubAdmin.getSubID();
            return subARepo.getAllMyRequest(subAdminID);
        } catch (Exception e) {
            System.err.println("Error fetching requests: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public interface LocationListener
    {
        void onLocationUpdate(double latitude, double longitude);
    }

    public JSONObject getDriverLocation(String publicDriverId)
    {
        try {
            DriverRepo driverRepo = new DriverRepo();  // ← Fresh instance
            
            int driverID = driverRepo.getDriverIdByPublicID(publicDriverId);

            Integer deviceId = driverRepo.getTraccarDeviceId(driverID);

            if(deviceId == null)
            {
                return null;
            }

            // ✓ TraccarAPI call (not affected by DB connection changes)
            JSONObject position = TraccarAPI.getLatestPosition(deviceId);

            return position;
        } catch (Exception e) {
            System.err.println("Error getting driver location: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void startTracking(String publicDriverId, LocationListener listener)
    {
        new Thread(() ->
        {
            while(true)
            {
                try
                {
                    JSONObject position = getDriverLocation(publicDriverId);
                    
                    if(position != null)
                    {
                        double lat = position.getDouble("latitude");
                        double lng = position.getDouble("longitude");
                        
                        listener.onLocationUpdate(lat, lng);
                    }
                    
                    else
                    {
                        listener.onLocationUpdate(Double.NaN, Double.NaN);
                    }
                    
                    Thread.sleep(5000);
                }
                
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}