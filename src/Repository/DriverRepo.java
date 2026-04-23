package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.Driver;

public class DriverRepo
{
    Driver d = new Driver();
    
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
    
    public List<Driver> driverRanking()
    {
        List<Driver> list = new ArrayList<>();
        
        try
        {
            Connection conn = dbConnection.getConnection();
            
            String sql = " SELECT d.first_name, d.last_name, r.driver_rank "
                    + "FROM ranking r "
                    + "JOIN driver d ON d.driver_id = r.driver_id "
                    + "ORDER BY r.driver_rank ASC ";
            
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                d.setfirstName(res.getString("first_name"));
                d.setlastName(res.getString("last_name"));
                d.setranking(res.getInt("driver_rank"));
                
                list.add(d);
            }
            if(res.next())
            {
                
            }
            
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
}
