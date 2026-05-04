package Service;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import Model.Driver;
import Model.Request;
import Model.SuperAdmin;
import Repository.SuperAdminRepo;
import Repository.DriverRepo;

public class SuperAdminService
{
    private final SuperAdminRepo saRepo = new SuperAdminRepo();
    
    // Check if a SuperAdmin account already exists in the database
    public boolean checkAccout()
    {
        return saRepo.checkExistingSA();
    }
    
    // Authenticate SuperAdmin login
    // Returns: 1 = success, 2 = wrong password, 0 = account not found
    public int logIn(String publicID, String password)
    {
        // Attempt to retrieve SuperAdmin by their public ID from the database
        SuperAdmin sa = saRepo.logInRepo(publicID);
        
        // If no SuperAdmin found with this ID
        if (sa == null)
        {
            return 0;
        }
        
        // If SuperAdmin found but password doesn't match
        if (!sa.getPassword().equals(password))
        {
            return 2;
        }
        
        // Both ID and password are correct
        return 1;
    }
    
    // Register a new SuperAdmin account
    public boolean registerSA(String id, String fname, String lname,
                              String contactNum, String position, String photo,
                              String password, String confirmPass)
    {
        SuperAdmin sa = new SuperAdmin();
        
        // Validate that passwords match
        if (!password.equals(confirmPass))
        {
            return false;
        }
        
        // Set SuperAdmin properties using database field names
        sa.setPublicID(id);
        sa.setfirstName(fname);
        sa.setlastName(lname);
        sa.setcontactNum(contactNum);
        sa.setposition(position);
        sa.setphotoURL(photo);
        sa.setPassword(password);
        
        // Register SuperAdmin in the database
        saRepo.registerSA(sa);
        
        return true;
    }
    
    // Fetch SuperAdmin data from the database
    // Used after successful login to retrieve the full profile
    public SuperAdmin getSAData()
    {
        return saRepo.getSuperAdminData();
    }
    
    // NOTE: updateSuperAdminProfile() was removed because the corresponding
    // method (SuperAdminRepo.updateSuperAdminProfile) does not exist in the repo.
    // If you need profile updates in the future, implement the method in
    // SuperAdminRepo first, then re-add this service method.

    // Count total pending requests
    public int totalPending()
    {
        int total = saRepo.countPendingReq();
        
        return total;
    }
    
    // Count total approved requests
    public int totalApproved()
    {
        int total = saRepo.countApprovedReq();
        
        return total;
    }
    
    // Count total rejected requests
    public int totalRejected()
    {
        int total = saRepo.countRejectedReq();
        
        return total;
    }
    
    // Get all requests from the database
    public List<Request> getAllRequest()
    {
        return saRepo.getAllRequest();
    }
    
    // Get a specific request by request code
    public Request getRequest(String reqCode)
    {
        return saRepo.getRequest(reqCode);
    }
    
    // Get request details and deserialize them based on request type
    public Object getReqDetails(String reqCode)
    {
        try
        {
            // Fetch the request from database
            Request req = saRepo.getRequest(reqCode);
            if(req == null) return null;
            
            // Get the details JSON string from the request
            String json = req.getDetails();
            
            // Setup ObjectMapper to handle Java Time types
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());            
            
            // Deserialize based on request type
            if("DRIVER REGISTRATION".equals(req.getRequestInfo()))
            {
                return mapper.readValue(json, Driver.class);
            }
            
            else if("REMOVE DRIVER".equals(req.getRequestInfo()))
            {
                return mapper.readValue(json, Map.class);
            }
            
            return null;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    // Approve a request and process it accordingly
    public boolean approveRequest(String reqCode)
    {
        DriverRepo dRepo = new DriverRepo();
        
        try
        {
            // Get the request from database
            Request req = saRepo.getRequest(reqCode);
            
            if(req == null) return false;
            
            // Get the request type
            String type = req.getRequestInfo();
            
            boolean success = false;
            
            // Handle DRIVER REGISTRATION requests
            if("DRIVER REGISTRATION".equals(type))
            {
                // Deserialize the driver data from request details
                Driver d = (Driver) getReqDetails(reqCode);
                
                if(d == null) return false;
                
                // Insert the approved driver into the database
                success = dRepo.insertApprovedDriver(d);
            }
            
            // Handle REMOVE DRIVER requests
            else if("REMOVE DRIVER".equals(type))
            {
                // Get the driver ID from the request
                int driverID = req.getDriverID();
                
                if(driverID == -1) return false;
                
                // Deactivate the driver in the database
                success = dRepo.deactivateDriver(driverID);
            }
            
            // If the operation was successful, update request status to APPROVED
            if(success)
            {
                return saRepo.updateRequestStatus(reqCode, "APPROVED");
            }
            
            return false;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
}
    
    /*public boolean approveRequest(String reqCode)
    {
        DriverRepo dRepo = new DriverRepo();
        try
        {
            Driver d = getReqDetails(reqCode);
            
            if(d == null) return false;
            
            boolean insert = dRepo.insertApprovedDriver(d);
            if(!insert) return false;
            
            boolean update = saRepo.updateRequestStatus(reqCode, "APPROVED");
            
            return update;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }*/

    
    /*public boolean registerSA(String id, String fname, String lname,
                            String contactNum, String position, String password,
                            String confirmPass)
    {
        if (!password.equals(confirmPass))
        {
            return false;
        }
        sa.publicID = id;
        sa.firstName = fname;
        sa.lastName = lname;
        sa.contactNum = contactNum;
        sa.position = position;        
        sa.password = password;
        
        return true;
    }
    
    public boolean logIn(String id, String regID, String password, String regPass)
    {
        return id.equals(regID) && password.equals(regPass);
    }
    
    public void dashboardOverview()
    {
         
    }*/
    
   