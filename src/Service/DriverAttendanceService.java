package Service;

import Repository.DriverAttendanceRepo;
import Repository.DriverRepo;

public class DriverAttendanceService
{
    private DriverRepo driverRepo = new DriverRepo();
    private DriverAttendanceRepo attendanceRepo = new DriverAttendanceRepo();
    
    private int getDriverId(String publicId)
    {
        return driverRepo.getDriverIdByPublicID(publicId);
    }
    
    public boolean markDriverPresent(String publicId)
    {
        int driverId = getDriverId(publicId);
        return attendanceRepo.markPresent(driverId);
    }
    
    public boolean isPresentToday(String publicId)
    {
        int driverId = getDriverId(publicId);
        return attendanceRepo.hasAttendanceToday(driverId);
    }
    
    public int getAbsenceCount(String publicId)
    {
        int driverId = getDriverId(publicId);
        return attendanceRepo.countAbsences(driverId);
    }
}
