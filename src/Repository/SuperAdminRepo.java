package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.SuperAdmin;
import Model.Request;

public class SuperAdminRepo
{
    Connection conn = dbConnection.getConnection();
    
    public boolean checkExistingSA()
    {
        try
        {   
            String sql = "SELECT * FROM super_admin";
            PreparedStatement prepS = conn.prepareStatement(sql);
            
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                return res.getInt(1) > 0;
            }
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
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            
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
    
    public SuperAdmin logInRepo(String publicID)
    {
        
        String sql = "SELECT * FROM super_admin WHERE public_id = ?";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            prepS.setString(1, publicID);
            
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                SuperAdmin sa = new SuperAdmin();
                
                sa.setPublicID(res.getString("public_id"));
                sa.setPassword(res.getString("password")); 
                
                return sa;
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
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
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
        
        Connection conn = dbConnection.getConnection();
        String sql = "SELECT * FROM request "
                + "ORDER BY created_at DESC";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
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
    
    public int countPendingReq()
    {
        int count = 0;
        
        try
        {           
            String sql = "SELECT COUNT(*) FROM request WHERE status = 'PENDING'";
            PreparedStatement prepS = conn.prepareStatement(sql);
            
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
        
        try
        {           
            String sql = "SELECT COUNT(*) FROM request WHERE status = 'APPROVED'";
            PreparedStatement prepS = conn.prepareStatement(sql);
            
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
        
        try
        {           
            String sql = "SELECT COUNT(*) FROM request WHERE status = 'REJECTED'";
            PreparedStatement prepS = conn.prepareStatement(sql);
            
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
