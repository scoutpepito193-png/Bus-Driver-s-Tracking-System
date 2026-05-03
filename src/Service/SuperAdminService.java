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
import util.TraccarAPI;
import util.APIResponse;

public class SuperAdminService
{
    private final SuperAdminRepo saRepo = new SuperAdminRepo();
    private final TraccarService traccarService = new TraccarService();
    
    public boolean checkAccout()
    {
        return saRepo.checkExistingSA();
    }
    
    public int logIn(String publicID, String password)
    {
        SuperAdmin sa = saRepo.logInRepo(publicID);
        
        if (sa == null)
        {
            return 0;
        }
        
        if (!sa.getPassword().equals(password))
        {
            return 2;
        }
        
        return 1;
    }
    
    public boolean registerSA(String id, String fname, String lname,
                              String contactNum, String position, String photo,
                              String password, String confirmPass)
    {
        SuperAdmin sa = new SuperAdmin();
        
        if (!password.equals(confirmPass))
        {
            return false;
        }
        
        sa.setPublicID(id);
        sa.setfirstName(fname);
        sa.setlastName(lname);
        sa.setcontactNum(contactNum);
        sa.setposition(position);
        sa.setphotoURL(photo);
        sa.setPassword(password);
        
        saRepo.registerSA(sa);
        
        return true;
    }
    
    public SuperAdmin getSAData()
    {
        return saRepo.getSuperAdminData();
    }

    public int totalPending()
    {
        int total = saRepo.countPendingReq();
        
        return total;
    }
    
    public int totalApproved()
    {
        int total = saRepo.countApprovedReq();
        
        return total;
    }
    
    public int totalRejected()
    {
        int total = saRepo.countRejectedReq();
        
        return total;
    }
    
    public List<Request> getAllRequest()
    {
        return saRepo.getAllRequest();
    }
    
    public Request getRequest(String reqCode)
    {
        return saRepo.getRequest(reqCode);
    }
    
    public Object getReqDetails(String reqCode)
    {
        try
        {
            Request req = saRepo.getRequest(reqCode);
            if(req == null) return null;
            
            String json = req.getDetails();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());            
            
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
    
    public boolean approveRequest(String reqCode)
    {
        DriverRepo dRepo = new DriverRepo();
        
        try
        {
            Request req = saRepo.getRequest(reqCode);
            
            if(req == null) return false;
            
            String type = req.getRequestInfo();
            
            boolean success = false;
            
            if("DRIVER REGISTRATION".equals(type))
            {
                Driver d =(Driver) getReqDetails(reqCode);
                
                if(d == null) return false;
                
                APIResponse res = traccarService.initSession();

                if (!res.isSuccess())
                {
                    return false;
                }
                
                String deviceName = d.getpublic_driver_id() + " - " + d.getfirstName() + " - " + d.getlastName();
                
                String uniqueId = d.getpublic_driver_id();
           
                int deviceID = TraccarAPI.creationDeviceID(deviceName, uniqueId);
            
                if(deviceID == -1)
                {
                    return false;
                }
            
                d.setTraccarID(deviceID);
                
                success = dRepo.insertApprovedDriver(d);
            }
            
            else if("REMOVE DRIVER".equals(type))
            {
                int driverID = req.getDriverID();
                
                if(driverID == -1) return false;
                
                success = dRepo.deactivateDriver(driverID);
            }
            
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
    
    
}