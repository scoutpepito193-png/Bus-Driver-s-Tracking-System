package Repository;

import java.sql.*;
import java.time.LocalDate;
import util.TimeProvider;

public class SalaryRepo
{
    public boolean insertSalary(int driverId, double revenue, String type, double amount)
    {
        String sql = "INSERT INTO driver_salary (driver_id, revenue, salary_type, salary_amount, date) "
                + "VALUES (?, ?, ?, ?, ?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setDouble(2, revenue);
            ps.setString(3, type);
            ps.setDouble(4, amount);
            ps.setDate(5, java.sql.Date.valueOf(util.TimeProvider.now()));
            
            return ps.executeUpdate() > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public double getTodayRevenue(int driverId)
    {
        String sql = "SELECT COALESCE(SUM(total_revenue), 0) "
                + "FROM driver_performance "
                + "WHERE driver_id = ? AND record_date = ?";
        
        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setDate(2, java.sql.Date.valueOf(util.TimeProvider.now()));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
            {
                return rs.getDouble(1);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public boolean salaryExists(int driverId)
    {
        String sql = "SELECT 1 FROM driver_salary "
                + "WHERE driver_id = ? AND date = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setDate(2, java.sql.Date.valueOf(util.TimeProvider.now()));
            
            ResultSet rs = ps.executeQuery();
            
            return rs.next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public double getTotalSalary(int driverId, int month, int year)

    {        
        String sql = "SELECT COALESCE(SUM(salary_amount), 0) "
                + "FROM driver_salary "
                + "WHERE driver_id = ? "
                + "AND EXTRACT(MONTH FROM date) = ? "
                + "AND EXTRACT(YEAR FROM date) = ?";
       
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
            {
                return rs.getDouble(1);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return 0;
    }
}
