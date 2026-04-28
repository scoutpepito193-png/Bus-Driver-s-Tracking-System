package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.SubAdmin;

public class SubAdminRepo
{   

    public void registerSubAdmin(SubAdmin subA)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "INSERT INTO sub_admin (public_sub_id, first_name, last_name, gender, "
                + "date_of_birth, address, contact_number, photo_url, password)"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            
            prepS.setString(1, subA.getpublic_sub_id());
            prepS.setString(2, subA.getfirstName());
            prepS.setString(3, subA.getlastName());
            prepS.setString(4, subA.getgender());
            prepS.setDate(5, java.sql.Date.valueOf(subA.getdateOfBirth()));
            prepS.setString(6, subA.getaddress());
            prepS.setString(7, subA.getcontactNum());
            prepS.setString(8, subA.getphotoURL());
            prepS.setString(9, subA.getpassword());
            
            prepS.executeUpdate();
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public SubAdmin logIn(String publicID)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "SELECT * FROM sub_admin WHERE public_sub_id = ?";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            prepS.setString(1, publicID);
            
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
            {
                SubAdmin sub = new SubAdmin();
                
                sub.setpublic_sub_id(res.getString("public_sub_id"));
                sub.setpassword(res.getString("password"));
                
                return sub;
            }
            
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<SubAdmin> getSubAdmins()
    {
        
        Connection conn = dbConnection.getConnection();
        List<SubAdmin> list = new ArrayList<>();
        
        try
        {
            String sql = "SELECT public_sub_id, first_name, last_name, position_role "
                    + "FROM sub_admin";
            
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                SubAdmin sub = new SubAdmin();
                
                sub.setpublic_sub_id(res.getString("public_sub_id"));
                sub.setfirstName(res.getString("first_name"));
                sub.setlastName(res.getString("last_name"));
                sub.setposition(res.getString("position_role"));
                
                list.add(sub);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public int countSubAdmin()
    {
        Connection conn = dbConnection.getConnection();
        int count = 0;
        
        try
        {
            String sql = "SELECT * FROM sub_admin";
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
