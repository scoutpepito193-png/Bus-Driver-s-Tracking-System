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
    
    private final DriverRepo driverRepo = new DriverRepo();

    public JSONObject getDriverLocation(String publicDriverId)
    {
        int driverID = driverRepo.getDriverIdByPublicID(publicDriverId);
        
        Integer deviceId = driverRepo.getTraccarDeviceId(driverID);

        if(deviceId == null)
        {
            return null;
        }

        return TraccarAPI.getLatestPosition(deviceId);
    }
}
