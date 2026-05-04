package Service;

import java.util.List;
import java.util.Map;

// Jackson imports for JSON processing
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Model imports
import Model.Driver;
import Model.Request;
import Model.SuperAdmin;

// Repository imports
import Repository.SuperAdminRepo;
import Repository.DriverRepo;

// Traccar/API imports - CORRECT PACKAGE: util (not API)
import util.TraccarAPI;
import util.APIResponse;
import Service.TraccarService;

public class SuperAdminService
{
    private final SuperAdminRepo saRepo = new SuperAdminRepo();
    private final TraccarService traccarService = new TraccarService();
    
    // Check if a SuperAdmin account already exists in the database
    // Returns true if account exists, false if no account found or error occurs
    public boolean checkAccout()
    {
        try {
            return saRepo.checkExistingSA();
        } catch (Exception e) {
            System.err.println("Error checking SuperAdmin account existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Authenticate SuperAdmin login
    // Returns: 1 = success, 2 = wrong password, 0 = account not found
    public int logIn(String publicID, String password)
    {
        try {
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
        } catch (Exception e) {
            System.err.println("Error during SuperAdmin login: " + e.getMessage());
            e.printStackTrace();
            return 0; // Treat exceptions as "account not found"
        }
    }
    
    // Register a new SuperAdmin account
    // Validates that passwords match before creating account
    public boolean registerSA(String id, String fname, String lname,
                              String contactNum, String position, String photo,
                              String password, String confirmPass)
    {
        try {
            SuperAdmin sa = new SuperAdmin();
            
            // Validate that passwords match
            if (!password.equals(confirmPass))
            {
                System.out.println("Password mismatch: passwords do not match");
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
        } catch (Exception e) {
            System.err.println("Error registering SuperAdmin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Fetch SuperAdmin data from the database
    // Used after successful login to retrieve the full profile
    public SuperAdmin getSAData()
    {
        try {
            return saRepo.getSuperAdminData();
        } catch (Exception e) {
            System.err.println("Error fetching SuperAdmin data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Count total pending requests
    public int totalPending()
    {
        try {
            int total = saRepo.countPendingReq();
            return total;
        } catch (Exception e) {
            System.err.println("Error counting pending requests: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // Count total approved requests
    public int totalApproved()
    {
        try {
            int total = saRepo.countApprovedReq();
            return total;
        } catch (Exception e) {
            System.err.println("Error counting approved requests: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // Count total rejected requests
    public int totalRejected()
    {
        try {
            int total = saRepo.countRejectedReq();
            return total;
        } catch (Exception e) {
            System.err.println("Error counting rejected requests: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // Get all requests from the database
    public List<Request> getAllRequest()
    {
        try {
            return saRepo.getAllRequest();
        } catch (Exception e) {
            System.err.println("Error fetching all requests: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Get a specific request by request code
    public Request getRequest(String reqCode)
    {
        try {
            return saRepo.getRequest(reqCode);
        } catch (Exception e) {
            System.err.println("Error fetching request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Get request details and deserialize them based on request type
    // Returns Driver object for DRIVER REGISTRATION requests
    // Returns Map object for REMOVE DRIVER requests
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
            System.err.println("Error getting request details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Approve a request and process it accordingly
    // For DRIVER REGISTRATION: Creates Traccar GPS device and inserts approved driver
    // For REMOVE DRIVER: Deactivates driver from system
    public boolean approveRequest(String reqCode)
    {
        DriverRepo dRepo = new DriverRepo();
        
        try
        {
            // Get the request from database
            Request req = saRepo.getRequest(reqCode);
            
            if(req == null) {
                System.err.println("Request not found: " + reqCode);
                return false;
            }
            
            // Get the request type
            String type = req.getRequestInfo();
            
            boolean success = false;
            
            // Handle DRIVER REGISTRATION requests
            if("DRIVER REGISTRATION".equals(type))
            {
                // Deserialize the driver data from request details
                Driver d = (Driver) getReqDetails(reqCode);
                
                if(d == null) {
                    System.err.println("Failed to deserialize driver details for request: " + reqCode);
                    return false;
                }
                
                // Initialize Traccar session for GPS tracking
                APIResponse res = traccarService.initSession();
                
                if (!res.isSuccess())
                {
                    System.err.println("Failed to initialize Traccar session for driver: " + d.getpublic_driver_id());
                    return false;
                }
                
                // Create device name combining driver ID and name for easy identification
                String deviceName = d.getpublic_driver_id() + " - " + d.getfirstName() + " - " + d.getlastName();
                
                // Use driver ID as unique identifier for Traccar GPS system
                String uniqueId = d.getpublic_driver_id();
           
                // Create device in Traccar API to track this driver's GPS location
                int deviceID = TraccarAPI.creationDeviceID(deviceName, uniqueId);
            
                if(deviceID == -1)
                {
                    System.err.println("Failed to create Traccar device for driver: " + d.getpublic_driver_id());
                    return false;
                }
            
                // Store Traccar device ID in driver object for future reference
                d.setTraccarID(deviceID);
                
                // Insert approved driver into database with GPS tracking ID
                success = dRepo.insertApprovedDriver(d);
                
                if(success) {
                    System.out.println("Driver approved and registered: " + d.getpublic_driver_id());
                }
            }
            
            // Handle REMOVE DRIVER requests
            else if("REMOVE DRIVER".equals(type))
            {
                // Get the driver ID from the request
                int driverID = req.getDriverID();
                
                if(driverID == -1) {
                    System.err.println("Invalid driver ID in remove request: " + reqCode);
                    return false;
                }
                
                // Deactivate the driver in the database
                success = dRepo.deactivateDriver(driverID);
                
                if(success) {
                    System.out.println("Driver deactivated: " + driverID);
                }
            }
            
            // If the operation was successful, update request status to APPROVED
            if(success)
            {
                boolean statusUpdated = saRepo.updateRequestStatus(reqCode, "APPROVED");
                if(statusUpdated) {
                    System.out.println("Request status updated to APPROVED: " + reqCode);
                }
                return statusUpdated;
            }
            
            return false;
        }
        
        catch(Exception e)
        {
            System.err.println("Error approving request: " + e.getMessage());
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
    
   
