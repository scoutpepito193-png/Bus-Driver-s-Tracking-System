package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DriverRepo
{
    public int countDrivers()
    {
        int count = 0;
        
        try
        {
            Connection conn = dbConnection.getConnection();
            
            String sql = "SELECT COUNT(*) FROM driver";
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
