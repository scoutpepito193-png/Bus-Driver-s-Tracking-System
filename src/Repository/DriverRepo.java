package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import Service.DriverService;
import Model.Driver;
import Model.DriverPerformance;
import util.Session;
import util.TimeProvider;

public class DriverRepo
{   
    
    public boolean requestDriverRegistration(Driver d, String requestCode)
    {   
        String sql = "INSERT INTO request (request_code, request_info, details, sub_admin_id) "
                + "VALUES (?,?::request_type,?::jsonb,?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            int subAdminID = Session.currentSubAdmin.getSubID();
            
            String details = "{"
                    + "\"public_driver_id\":\"" + d.getpublic_driver_id() + "\","
                    + "\"firstName\":\"" + d.getfirstName() + "\","
                    + "\"lastName\":\"" + d.getlastName() + "\","
                    + "\"gender\":\"" + d.getgender() + "\","
                    + "\"dateOfBirth\":\"" + d.getdateOfBirth() + "\","
                    + "\"address\":\"" + d.getaddress() + "\","
                    + "\"contactNumber\":\"" + d.getcontactNumber() + "\","
                    + "\"licenseNum\":\"" + d.getlicenseNum() + "\","
                    + "\"licenseExpiry\":\"" + d.getlicenseExpiry() + "\","
                    + "\"photoURL\":\"" + d.getphotoURL() + "\","
                    + "\"password\":\"" + d.getpassword() + "\""
                    + "}";
            
            prepS.setString(1, requestCode);
            prepS.setString(2, "DRIVER REGISTRATION");
            prepS.setString(3, details);
            prepS.setInt(4, subAdminID);
            
            int rows = prepS.executeUpdate();
            
            return rows > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    public boolean requestDriverRemoval(String publicDriverID, String details)
    {
        DriverService ds = new DriverService();
        String getIDsql = "SELECT driver_id FROM driver WHERE public_driver_id = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(getIDsql))
        {
            prepS.setString(1, publicDriverID.trim());
            
            ResultSet res = prepS.executeQuery();
            
            if(!res.next())
            {
                return false;
            }
            
            int driver = res.getInt("driver_id");
            
            String requestCode = ds.generateReqCode();
            
            String insertSQL = "INSERT INTO request (request_code, request_info, status, details, driver_id) "
                    + "VALUES (?, 'REMOVE DRIVER', 'PENDING', ?::jsonb, ?)";
            
            PreparedStatement prepSt = conn.prepareStatement(insertSQL);
            
            String jsonDetails = "{\"reason\":\"" + details + "\"}";
            
            prepSt.setString(1, requestCode);
            prepSt.setString(2, jsonDetails);
            prepSt.setInt(3, driver);
            
            return prepSt.executeUpdate() > 0;
            
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean insertApprovedDriver(Driver d)
    {
        String sql = "INSERT INTO driver "
                + "(public_driver_id, first_name, last_name, gender, date_of_birth, address, contact_number, license_number, license_expiry_date, photo_url, driver_password, traccar_device_id) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, d.getpublic_driver_id());
            prepS.setString(2, d.getfirstName());
            prepS.setString(3, d.getlastName());
            prepS.setString(4, d.getgender());
            prepS.setDate(5, java.sql.Date.valueOf(d.getdateOfBirth()));
            prepS.setString(6, d.getaddress());
            prepS.setString(7, d.getcontactNumber());
            prepS.setString(8, d.getlicenseNum());
            prepS.setDate(9, java.sql.Date.valueOf(d.getlicenseExpiry()));
            prepS.setString(10, d.getphotoURL());
            prepS.setString(11, d.getpassword());
            prepS.setInt(12, d.getTracerID());
            
            return prepS.executeUpdate() > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deactivateDriver(int driverId)
    {
        String sql = "UPDATE driver SET status = 'INACTIVE' WHERE driver_id = ?";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);

            return ps.executeUpdate() > 0;
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }

        return false;
    }
    
    public int countDrivers()
    {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM driver WHERE status = 'ACTIVE'";
        
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
    
    public List<Driver> driverRanking()
    {
        List<Driver> list = new ArrayList<>();
        
        String sql = " SELECT d.first_name, d.last_name, r.driver_rank "
                + "FROM ranking r "
                + "JOIN driver d ON d.driver_id = r.driver_id "
                + "ORDER BY r.driver_rank ASC";
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
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
        String sql = "SELECT driver_id FROM driver WHERE public_driver_id = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
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
    
    public Driver getDriverProfileById(int driverID)
    {
        String sql = "SELECT public_driver_id, first_name, last_name, gender, "
                    + "date_of_birth, address, contact_number, license_number, "
                    + "license_expiry_date, photo_url, status"
                    + "FROM driver WHERE driver_id = ?";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setInt(1, driverID);

            ResultSet res = prepS.executeQuery();

            if(res.next())
            {
                Driver d = new Driver();

            
                d.setpublic_driver_id(res.getString("public_driver_id"));
                d.setfirstName(res.getString("first_name"));
                d.setlastName(res.getString("last_name"));
                d.setgender(res.getString("gender"));

                if(res.getDate("date_of_birth") != null)
                    d.setdateOfBirth(res.getDate("date_of_birth").toLocalDate());

                d.setaddress(res.getString("address"));
                d.setcontactNumber(res.getString("contact_number"));
                d.setlicenseNum(res.getString("license_number"));

                if(res.getDate("license_expiry_date") != null)
                    d.setlicenseExpiry(res.getDate("license_expiry_date").toLocalDate());

                d.setphotoURL(res.getString("photo_url"));
                d.setStatus(res.getString("status"));

                return d;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public boolean enterDriverPerformance(DriverPerformance dp)
    {
        String sql = "INSERT INTO driver_performance "
                + "(driver_id, average_kmpl, total_tickets, total_revenue, record_date) "
                + "VALUES (?,?,?,?, CURRENT_DATE)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
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
    
    public DriverPerformance getDriverPerformance(int driverID)
{
    String sql = "SELECT * FROM driver_performance WHERE driver_id = ?";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql))
    {
        ps.setInt(1, driverID);

        ResultSet rs = ps.executeQuery();

        if(rs.next())
        {
            DriverPerformance p = new DriverPerformance();

            p.settotalTickets(rs.getInt("total_tickets"));
            p.settotalRevenue(rs.getDouble("total_revenue"));
            p.setaverageKMPL(rs.getDouble("average_kmpl"));

            return p;
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }

    return null;
}
    
    public List<DriverPerformance> driverPerformance()
    {
        List<DriverPerformance> list = new ArrayList<>();
        
        LocalDate today = TimeProvider.now();
        LocalDate startMonth = TimeProvider.startOfMonth(today);
        LocalDate nextMonth = TimeProvider.startOfNextMonth(today);
        
        String sql = "SELECT d.public_driver_id, d.first_name, d.last_name, "
                + "COALESCE(AVG(p.average_kmpl), 0) AS average_kmpl, "
                + "COALESCE(SUM(p.total_tickets), 0) AS total_tickets, "
                + "COALESCE(SUM(p.total_revenue), 0) AS total_revenue "
                + "FROM driver d "
                + "LEFT JOIN driver_performance p ON d.driver_id = p.driver_id "
                + "AND p.record_date >= ? "
                + "AND p.record_date < ? "
                + "WHERE status = 'ACTIVE' "
                + "GROUP BY d.driver_id, d.first_name, d.last_name "
                + "ORDER BY d.last_name ASC";        
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setDate(1, java.sql.Date.valueOf(startMonth));
            prepS.setDate(2, java.sql.Date.valueOf(nextMonth));
            
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
        String sql = "SELECT * FROM driver WHERE public_driver_id = ? AND driver_password = ? AND status = 'ACTIVE'";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, publicDriverId);
            prepS.setString(2, password);

            ResultSet res = prepS.executeQuery();

            if(res.next())
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
        List<DriverPerformance> list = new ArrayList<>();
        
        String sql = "SELECT dp.* FROM driver_performance dp "
                + "JOIN driver d ON dp.driver_id = d.driver_id "
                + "WHERE d.public_driver_id = ? "
                + "ORDER BY dp.record_date DESC";        

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
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

    public boolean updateDriverStatus(int driverID, String status)
    {
        String sql = "UPDATE driver SET status = ? WHERE driver_id = ?";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
        
        
        
            prepS.setString(1, status);
            prepS.setInt(2, driverID);
        
            return prepS.executeUpdate() > 0;
        }
    
        catch(Exception e)
        {
            e.printStackTrace();
        }
    
        return false;
    }

    public Integer getTraccarDeviceId(int driverID)
    {
        String sql = "SELECT traccar_device_id FROM driver WHERE driver_id = ?";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverID);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                int deviceId = rs.getInt("traccar_device_id");

                if (rs.wasNull())
                {
                    return null;
                }

                return deviceId;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    

    public void updateRanking()
    {
        LocalDate today = TimeProvider.now();
        
        String deleteSql = "DELETE FROM ranking";
        String sql = "INSERT INTO ranking (driver_id, driver_rank, rank_date) "
                + "SELECT d.driver_id, "
                + "ROW_NUMBER() OVER (ORDER BY "
                + "SUM(CASE WHEN a.status = 'PRESENT' THEN 1.0 "
                + "         WHEN a.status = 'HALF DAY' THEN 0.5 "
                + "         ELSE 0.0 END) DESC, "
                + "SUM(dp.total_tickets) DESC, "
                + "SUM(dp.total_revenue) DESC, "
                + "SUM(dp.violations) ASC "
                + ") AS driver_rank, "
                + "? as rank_date "
                + "FROM driver d "
                + "JOIN driver_performance dp ON d.driver_id = dp.driver_id "
                + "JOIN driver_attendance a ON d.driver_id = a.driver_id "
                + "WHERE EXTRACT(MONTH FROM a.date) = ? "
                + "AND EXTRACT(YEAR FROM a.date) = ? "
                + "AND EXTRACT(MONTH FROM dp.record_date) = ? "
                + "AND EXTRACT(YEAR FROM dp.record_date) = ? "
                + "GROUP BY d.driver_id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement prepS = conn.prepareStatement(sql))
        {
            deleteStmt.executeUpdate();            
            
            prepS.setDate(1, java.sql.Date.valueOf(today));
            prepS.setInt(2, today.getMonthValue());
            prepS.setInt(3, today.getYear());
            prepS.setInt(4, today.getMonthValue());
            prepS.setInt(5, today.getYear());
            
            prepS.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public List<Driver> getAllActiveDrivers()
    {
        List<Driver> list = new ArrayList<>();
        
        String sql = "SELECT driver_id, public_driver_id "
                + "FROM driver WHERE status = 'ACTIVE'";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                Driver d = new Driver();
                d.setdriverID(rs.getInt("driver_id"));
                d.setpublic_driver_id(rs.getString("public_driver_id"));
                
                list.add(d);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
}

