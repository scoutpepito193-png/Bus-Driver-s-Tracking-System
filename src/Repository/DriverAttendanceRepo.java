package Repository;

import java.sql.*;

public class DriverAttendanceRepo
{
    public boolean markPresent(int driverId)
    {
        String sql = "INSERT INTO driver_attendance (driver_id, status) "
                + "VALUES (?, 'PRESENT') "
                + "ON CONFLICT (driver_id, date) DO NOTHING";    
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
    
    public boolean hasAttendanceToday(int driverId)
    {
        String sql = "SELECT 1 FROM driver_attendance "
                + "WHERE driver_id = ? AND date = CURRENT_DATE";        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {   
            ps.setInt(1, driverId);
            
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
