package Repository;

import Model.Request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import Model.SubAdmin;
import Model.Driver;
import Model.DriverPerformance;
import java.time.LocalDate;

public class SubAdminRepo
{   

    public void registerSubAdmin(SubAdmin subA)
    {
        String sql = "INSERT INTO sub_admin (public_sub_id, first_name, last_name, gender, "
                + "date_of_birth, address, contact_number, photo_url, password)"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
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
    
    public SubAdmin subAdminLogIn(String publicID, String password)
    {
        String sql = "SELECT * FROM sub_admin WHERE public_sub_id = ? AND password = ?";

        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
            prepS.setString(1, publicID);
            prepS.setString(2, password);
            
            ResultSet res = prepS.executeQuery();
            
            if(res.next())
            {
                SubAdmin sub = new SubAdmin();
                
                sub.setSubID(res.getInt("sub_admin_id"));
                sub.setpublic_sub_id(res.getString("public_sub_id"));
                sub.setfirstName(res.getString("first_name"));
                sub.setlastName(res.getString("last_name"));
                sub.setdateOfBirth(res.getObject("date_of_birth", LocalDate.class));
                sub.setaddress(res.getString("address"));
                sub.setcontactnum(res.getString("contact_number"));
                sub.setposition(res.getString("position_role"));
                sub.setphotoURL(res.getString("photo_url"));
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
        List<SubAdmin> list = new ArrayList<>();
        
        String sql = "SELECT public_sub_id, first_name, last_name, position_role "
                + "FROM sub_admin";      
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
        {
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
        int count = 0;
        
        String sql = "SELECT * FROM sub_admin";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement prepS = conn.prepareStatement(sql);)
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
    
    public List<Request> getAllMyRequest(int subAdminID)
    {
        List<Request> list = new ArrayList<>();
        
        
        String sql = "SELECT * FROM request "
                + "WHERE sub_admin_id = ? "
                + "ORDER BY created_at DESC";
        
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setInt(1, subAdminID);
            
            ResultSet res = prepS.executeQuery();
            
            while(res.next())
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
        public Driver searchDriverByName(String name)
    {
        String sql = "SELECT * FROM driver WHERE LOWER(first_name || ' ' || last_name) LIKE LOWER(?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, "%" + name + "%");
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

    public Driver searchDriverById(String publicDriverId)
    {
        String sql = "SELECT * FROM driver WHERE public_driver_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, publicDriverId);
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

    public List<DriverPerformance> searchDriverRecords(String publicDriverId)
    {
        List<DriverPerformance> list = new ArrayList<>();
        String sql = "SELECT dp.* FROM driver_performance dp "
                + "JOIN driver d ON dp.driver_id = d.driver_id "
                + "WHERE d.public_driver_id = ? "
                + "ORDER BY dp.record_date DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement prepS = conn.prepareStatement(sql))
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

    public SubAdmin searchSubAdminByName(String name)
    {
        String sql = "SELECT * FROM sub_admin WHERE LOWER(first_name || ' ' || last_name) LIKE LOWER(?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, "%" + name + "%");
            ResultSet res = prepS.executeQuery();

            if (res.next())
            {
                SubAdmin sub = new SubAdmin();
                sub.setpublic_sub_id(res.getString("public_sub_id"));
                sub.setfirstName(res.getString("first_name"));
                sub.setlastName(res.getString("last_name"));
                sub.setgender(res.getString("gender"));
                sub.setaddress(res.getString("address"));
                sub.setcontactnum(res.getString("contact_number"));
                sub.setposition(res.getString("position_role"));
                return sub;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public SubAdmin searchSubAdminById(String publicSubId)
    {
        String sql = "SELECT * FROM sub_admin WHERE public_sub_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement prepS = conn.prepareStatement(sql))
        {
            prepS.setString(1, publicSubId);
            ResultSet res = prepS.executeQuery();

            if (res.next())
            {
                SubAdmin sub = new SubAdmin();
                sub.setpublic_sub_id(res.getString("public_sub_id"));
                sub.setfirstName(res.getString("first_name"));
                sub.setlastName(res.getString("last_name"));
                sub.setgender(res.getString("gender"));
                sub.setaddress(res.getString("address"));
                sub.setcontactnum(res.getString("contact_number"));
                sub.setposition(res.getString("position_role"));
                return sub;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}