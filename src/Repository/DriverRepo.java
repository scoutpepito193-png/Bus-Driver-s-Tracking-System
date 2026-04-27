package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.Driver;
import Model.DriverPerformance;

public class DriverRepo
{   
    public int countDrivers()
    {
        Connection conn = dbConnection.getConnection();
        int count = 0;
        
        try
        {            
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
        Connection conn = dbConnection.getConnection();
        List<Driver> list = new ArrayList<>();
        
        try
        {
            String sql = " SELECT d.first_name, d.last_name, r.driver_rank "
                    + "FROM ranking r "
                    + "JOIN driver d ON d.driver_id = r.driver_id "
                    + "ORDER BY r.driver_rank ASC";
            
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                Driver d = new Driver();
                
                d.setfirstName(res.getString("first_name"));
                d.setlastName(res.getString("last_name"));
                d.setranking(res.getInt("driver_rank"));
                
                list.add(d);
            }
            
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public List<DriverPerformance> driverPerformance()
    {
        Connection conn = dbConnection.getConnection();
        List<DriverPerformance> list = new ArrayList<>();
        
        try
        {
            String sql = "SELECT d.first_name, d.last_name, "
                    + "p.average_kmpl, p.total_tickets, p.totel_revenue "
                    + "FROM driver d "
                    + "JOIN driver_performance p ON d.driver_id = p.driver_id "
                    + "ORDER BY d.last_name ASC";
            
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                Driver d = new Driver();
                DriverPerformance dp = new DriverPerformance();  
                
                d.setfirstName(res.getString("first_name"));
                d.setlastName(res.getString("last_name"));
                
                dp.setdriver(d);
                dp.setaverageKMPL(res.getDouble("average_kmpl"));
                dp.settotalTickets(res.getInt("total_tickets"));
                dp.settotalRevenue(res.getDouble("total_revenue"));
                
                list.add(dp);
            }            
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /*public List<Driver> listofDrivers()
    {
        List<Driver> d = new ArrayList<>();
        
        try
        {
            String sql = "SELECT * FROM driver";
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                d.set
            }
        }
    }*/
}
