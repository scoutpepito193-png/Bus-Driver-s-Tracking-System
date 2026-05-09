package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.SuperAdmin;
import Model.Request;
import Model.AuthResult;
import Model.Role;

public class SuperAdminRepo
{
    public boolean checkExistingSA()
    {
        String sql = "SELECT 1 FROM super_admin";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {   
            ResultSet res = prepS.executeQuery();
            return res.next();
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void registerSA(SuperAdmin sa)
    {
        
        String sql = "INSERT INTO super_admin (public_id, password, first_name,"
                + "last_name, contact_number, position_role, photo_url)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, sa.getPublicID());
            prepS.setString(2, sa.getPassword());
            prepS.setString(3, sa.getfirstName());
            prepS.setString(4, sa.getlastName());
            prepS.setString(5, sa.getcontactNum());
            prepS.setString(6, sa.getposition());
            prepS.setString(7, sa.getphotoURL());
            
            prepS.executeUpdate();
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public AuthResult logInRepo(String publicID, String password)
    {

        String sql = "SELECT * FROM super_admin WHERE public_id = ? AND password = ?";

        try (Connection conn = dbConnection.getConnection();
            PreparedStatement prepS = conn.prepareStatement(sql))
        {

            prepS.setString(1, publicID);
            prepS.setString(2, password);

            ResultSet res = prepS.executeQuery();

            if (res.next())
            {

                SuperAdmin sa = new SuperAdmin();
                sa.setPublicID(res.getString("public_id"));
                sa.setPassword(res.getString("password"));
            
                return new AuthResult(sa, Role.SUPER_ADMIN);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public SuperAdmin getSuperAdminData()
    {
        String sql = "SELECT * FROM super_admin LIMIT 1";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                SuperAdmin sa = new SuperAdmin();
                
                sa.setPublicID(res.getString("public_id"));
                sa.setPassword(res.getString("password"));                
                sa.setfirstName(res.getString("first_name"));
                sa.setlastName(res.getString("last_name"));
                sa.setcontactNum(res.getString("contact_number"));
                sa.setposition(res.getString("position_role"));
                sa.setphotoURL(res.getString("photo_url"));
                
                return sa;
            }
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;       
    }
    
    public List<Request> getAllRequest()
    {
        List<Request> list = new ArrayList<>();
        
        
        String sql = "SELECT * FROM request "
                + "WHERE status = 'PENDING' "
                + "ORDER BY created_at DESC";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                Request req = new Request();
                
                req.setRequestCode(res.getString("request_code"));
                req.setRequestInfo(res.getString("request_info"));
                req.setDetails(res.getString("details"));
                req.setStatus(res.getString("status"));
                
                list.add(req);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public Request getRequest(String reqCode)
    {
        String sql = "SELECT * FROM request WHERE request_code = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1,reqCode);
            
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
            {
                Request req = new Request();
                
                req.setRequestCode(res.getString("request_code"));
                req.setRequestInfo(res.getString("request_info"));
                req.setStatus(res.getString("status"));
                
                int driverID = res.getInt("driver_id");
                
                if(res.wasNull())
                {
                    req.setDriverID(-1);
                }
                else
                {
                    req.setDriverID(driverID);
                }
                
                req.setDetails(res.getString("details"));
                
                return req;
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public String getRequestDetails(String reqCode)
    {
        String sql = "SELECT details "
                + "FROM request "
                + "WHERE request_code = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, reqCode);
            
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
            {
                return res.getString("details");
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateRequestStatus(String reqCode, String status)
    {
        
        String sql = "UPDATE request "
                + "SET status = ?::request_status "
                + "WHERE request_code = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
            prepS.setString(1, status);
            prepS.setString(2, reqCode);
            
            return prepS.executeUpdate() > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public int countPendingReq()
    {
        int count = 0;
        
        String sql = "SELECT COUNT(*) FROM request WHERE status = 'PENDING'";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {           
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                count = res.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return count;
    }
    
    public int countApprovedReq()
    {
        int count = 0;
        
        String sql = "SELECT COUNT(*) FROM request WHERE status = 'APPROVED'";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {           
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                count = res.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return count;
    }

    public int countRejectedReq()
    {
        int count = 0;
        
        String sql = "SELECT COUNT(*) FROM request WHERE status = 'REJECTED'";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {           
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                count = res.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return count;
    }
}
