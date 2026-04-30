package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import Model.Driver;
import Model.DriverPerformance;

public class DriverRepo
{   
    
    public boolean requestDriverRegistrastion(Driver d, String requestCode)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "INSERT INTO request (request_code, request_info, details) "
                + "VALUES (?,?::request_type,?::jsonb)";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            
            String details = "{"
                    + "\"driver_id\":\"" + d.getpublic_driver_id() + "\","
                    + "\"first_name\":\"" + d.getfirstName() + "\","
                    + "\"last_name\":\"" + d.getlastName() + "\","
                    + "\"gender\":\"" + d.getgender() + "\","
                    + "\"date_of_birth\":\"" + d.getdateOfBirth() + "\","
                    + "\"address\":\"" + d.getaddress() + "\","
                    + "\"contact\":\"" + d.getcontactNumber() + "\","
                    + "\"license_number\":\"" + d.getlicenseNum() + "\","
                    + "\"license_expiry\":\"" + d.getlicenseExpiry() + "\","
                    + "\"photo_url\":\"" + d.getphotoURL() + "\","
                    + "\"password\":\"" + d.getpassword() + "\""
                    + "}";
            
            prepS.setString(1, requestCode);
            prepS.setString(2, "DRIVER REGISTRATION");
            prepS.setString(3, details);
            
            int rows = prepS.executeUpdate();
            
            return rows > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
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
    
    public int getDriverIdByPublicID(String publicID)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "SELECT driver_id FROM driver WHERE public_driver_id = ?";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            prepS.setString(1, publicID);
            
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
            {
                return res.getInt("driver_id");
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    public boolean enterDriverPerformance(DriverPerformance dp)
    {
        Connection conn = dbConnection.getConnection();
        
        String sql = "INSERT INTO driver_performance "
                + "(driver_id, average_kmpl, total_tickets, total_revenue, record_date) "
                + "VALUES (?,?,?,?, CURRENT_DATE)";
        
        try
        {
            PreparedStatement prepS = conn.prepareStatement(sql);
            
            prepS.setInt(1, dp.getdriver().getdriverID());
            prepS.setDouble(2, dp.getaverageKMPL());
            prepS.setInt(3, dp.gettotalTickets());
            prepS.setDouble(4, dp.gettotalRevenue());
            
            prepS.executeUpdate();
            return true;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<DriverPerformance> driverPerformance()
    {
        Connection conn = dbConnection.getConnection();
        List<DriverPerformance> list = new ArrayList<>();
        
        try
        {
            String sql = "SELECT d.public_driver_id, d.first_name, d.last_name, "
                    + "AVG (p.average_kmpl) AS average_kmpl, "
                    + "SUM (p.total_tickets) AS total_tickets, "
                    + "SUM (p.total_revenue) AS total_revenue "
                    + "FROM driver d "
                    + "JOIN driver_performance p ON d.driver_id = p.driver_id "
                    + "GROUP BY d.driver_id, d.first_name, d.last_name "
                    + "ORDER BY d.last_name ASC";
            
            PreparedStatement prepS = conn.prepareStatement(sql);
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
            {
                Driver d = new Driver();
                DriverPerformance dp = new DriverPerformance();  
                
                d.setpublic_driver_id(res.getString("public_driver_id"));
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
    public Driver driverLogin(String publicDriverId, String password)
{
    Connection conn = dbConnection.getConnection();

    try
    {
        String sql = "SELECT * FROM driver WHERE public_driver_id = ? AND driver_password = ?";
        PreparedStatement prepS = conn.prepareStatement(sql);
        prepS.setString(1, publicDriverId);
        prepS.setString(2, password);

        ResultSet res = prepS.executeQuery();

        if (res.next())
        {
            Driver d = new Driver();
            d.setpublic_driver_id(res.getString("public_driver_id"));
            d.setfirstName(res.getString("first_name"));
            d.setlastName(res.getString("last_name"));
            d.setgender(res.getString("gender"));
            d.setdateOfBirth(res.getObject("date_of_birth", LocalDate.class));
            d.setaddress(res.getString("address"));
            d.setcontactNumber(res.getString("contact_number"));
            d.setlicenseNum(res.getString("license_number"));
            d.setlicenseExpiry(res.getObject("license_expiry_date", LocalDate.class));
            return d;
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}

public List<DriverPerformance> driverRecords(String publicDriverId)
{
    Connection conn = dbConnection.getConnection();
    List<DriverPerformance> list = new ArrayList<>();

    try
    {
        String sql = "SELECT dp.* FROM driver_performance dp "
                + "JOIN driver d ON dp.driver_id = d.driver_id "
                + "WHERE d.public_driver_id = ? "
                + "ORDER BY dp.record_date DESC";

        PreparedStatement prepS = conn.prepareStatement(sql);
        prepS.setString(1, publicDriverId);

        ResultSet res = prepS.executeQuery();

        while (res.next())
        {
            DriverPerformance dp = new DriverPerformance();
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
