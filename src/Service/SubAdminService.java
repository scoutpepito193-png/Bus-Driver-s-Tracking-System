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
import Model.Driver;
import Model.DriverPerformance;


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
    
    
    public SubAdmin subAdminLogIn(String publicID, String password)
    {
        return subARepo.subAdminLogIn(publicID, password);
    }
    
    // ADD THIS METHOD
    public SubAdmin login(String adminId, String password)
    {
        return subARepo.subAdminLogIn(adminId, password);
    }
    
    public int totalSubAdmin()
    {
        int total = subARepo.countSubAdmin();
        
        return total;
    }
    
    public List<SubAdmin> getSubAdmins()
    {
        return subARepo.getSubAdmins();
    }
    
    public List<Request> getMyRequests()
    {
        int subAdminID = Session.currentSubAdmin.getSubID();

        return subARepo.getAllMyRequest(subAdminID);
    }
    
    public interface LocationListener
    {
        void onLocationUpdate(double latitude, double longhitudep);
    }
    
    private final DriverRepo driverRepo = new DriverRepo();

    public JSONObject getDriverLocation(String publicDriverId)
    {
        int driverID = driverRepo.getDriverIdByPublicID(publicDriverId);

        Integer deviceId = driverRepo.getTraccarDeviceId(driverID);

        if(deviceId == null)
        {
            return null;
        }

        JSONObject position = TraccarAPI.getLatestPosition(deviceId);

        return position;
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
    public Driver searchDriverByName(String name)
{
    return subARepo.searchDriverByName(name);
}

    public Driver searchDriverById(String id)
    {
        return subARepo.searchDriverById(id);
    }

    public List<DriverPerformance> searchDriverRecords(String publicDriverId)
    {
        return subARepo.searchDriverRecords(publicDriverId);
    }
    public SubAdmin searchSubAdminByName(String name)
    {
        return subARepo.searchSubAdminByName(name);
    }

    public SubAdmin searchSubAdminById(String id)
    {
        return subARepo.searchSubAdminById(id);
    }
}
