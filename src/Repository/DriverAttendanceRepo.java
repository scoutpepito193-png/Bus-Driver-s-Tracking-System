package Repository;

import java.sql.*;
import java.time.LocalDate;
import util.TimeProvider;

public class DriverAttendanceRepo
{
    public boolean markPresent(int driverId)
    {
        LocalDate today = TimeProvider.now();
        
        String sql = "INSERT INTO driver_attendance (driver_id, date, status) "
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
    
    public int countMonthlyPresent(int driverId, LocalDate date)
    {   
        String sql = "SELECT COUNT(*) FROM driver_attendance "
                + "WHERE driver_id = ? "
                + "AND status = 'PRESENT' "
                + "AND EXTRACT(MONTH FROM date) = ? "
                + "AND EXTRACT(YEAR FROM date) = ?";
        
        try(Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, driverId);
            ps.setInt(2, date.getMonthValue());
            ps.setInt(3, date.getYear());

            
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
    
    public int getExpectedDutyDays(LocalDate date)
    {
        int daysInMonth = date.lengthOfMonth();
        
        return (int) Math.round(daysInMonth * (6.0 / 7.0));
    }

    public int countActiveDriversToday(int subAdminId) {
        int count = 0;

        LocalDate today = LocalDate.now();

        String sql =
            "SELECT COUNT(DISTINCT a.driver_id) " +
            "FROM driver_attendance a " +
            "JOIN driver d ON a.driver_id = d.driver_id " +
            "WHERE DATE(a.date) = ? " +
            "AND a.status = 'PRESENT' " +
            "AND d.assigned_by = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(today));
            stmt.setInt(2, subAdminId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }


    
public int countAbsentDriversToday(int subAdminId) {
    int count = 0;

    LocalDate today = LocalDate.now();

    String sql =
        "SELECT COUNT(*) FROM driver d " +
        "WHERE d.assigned_by = ? " +
        "AND NOT EXISTS ( " +
        "   SELECT 1 FROM driver_attendance a " +
        "   WHERE a.driver_id = d.driver_id " +
        "   AND DATE(a.date) = ? " +
        ")";

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, subAdminId);
        stmt.setDate(2, java.sql.Date.valueOf(today));

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return count;
}


}
