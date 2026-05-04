package Repository;

import java.sql.*;

public class SalaryRepo
{
    public boolean insertSalary(int driverId, double revenue, String type, double amount)
    {
        String sql = "INSERT INTO driver_salary (driver_id, revenue, salary_type, salary_amount) "
                + "VALUES (?, ?, ?, ?)";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setDouble(2, revenue);
            ps.setString(3, type);
            ps.setDouble(4, amount);
            
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
                + "WHERE driver_id = ? AND record_date = CURRENT_DATE";
        
        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
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
