package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.SubAdmin;

public class SubAdminRepo
{   
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
}
