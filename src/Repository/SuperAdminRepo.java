package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Model.SuperAdmin;

public class SuperAdminRepo
{
    public boolean checkExistingSA()
    {
        try
        {
            Connection conn = dbConnection.getConnection();
            
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
        Connection conn = dbConnection.getConnection();
        
        String sql = "INSERT INTO super_admin (public_id, password, first_name,"
                + "last_name, contact_number, position_role, photo_url)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            
            prepS.setString(1, sa.publicID);
            prepS.setString(2, sa.password);
            prepS.setString(3, sa.firstName);
            prepS.setString(4, sa.lastName);
            prepS.setString(5, sa.contactNum);
            prepS.setString(6, sa.position);
            prepS.setString(7, sa.photoURL);
            
            prepS.executeUpdate();
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public SuperAdmin logInRepo(String publicID)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "SELECT * FROM super_admin WHERE public_id = ?";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            prepS.setString(1, publicID);
            
            ResultSet res = prepS.executeQuery();
            
            if (res.next())
            {
                SuperAdmin sa = new SuperAdmin();
                
                sa.publicID = res.getString("public_id");
                sa.password = res.getString("password");
                
                return sa;
            }
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
}
