package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SubAdminRepo
{
    Connection conn = dbConnection.getConnection();
    
    public int countSubAdmin()
    {
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
