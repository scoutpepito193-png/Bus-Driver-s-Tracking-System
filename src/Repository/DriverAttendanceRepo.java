package Repository;

import java.sql.*;
import java.time.LocalDate;
import util.TimeProvider;

public class DriverAttendanceRepo
{
    public boolean markPresent(int driverId)
    {
        LocalDate today = TimeProvider.now();
        
        String sql = "INSERT INTO driver_attendance (driver_id, status) "
                + "VALUES (?, ?, 'PRESENT') "
                + "ON CONFLICT (driver_id, date) DO NOTHING";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {   
            ps.setInt(1, driverId);
            ps.setDate(2, java.sql.Date.valueOf(today));    
            return ps.executeUpdate() > 0;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean hasAttendanceToday(int driverId)
    {
        LocalDate today = TimeProvider.now();
        
        String sql = "SELECT 1 FROM driver_attendance "
                + "WHERE driver_id = ? AND date = ?";        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {   
            ps.setInt(1, driverId);
            ps.setDate(2, java.sql.Date.valueOf(today));
            
            ResultSet rs = ps.executeQuery();
            
            return rs.next();
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public int countAbsences(int driverId)
    {
        String sql = "SELECT COUNT(*) FROM driver_attendance "
                + "WHERE driver_id = ? AND status = 'ABSENT'";        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {   
            ps.setInt(1, driverId);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
            {
                return rs.getInt(1);
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return 0;
    }
    
}
