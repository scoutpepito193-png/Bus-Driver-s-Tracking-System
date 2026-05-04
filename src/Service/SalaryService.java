package Service;

import Repository.DriverRepo;
import Repository.SalaryRepo;
import Repository.DriverAttendanceRepo;

public class SalaryService
{
    private final DriverRepo driverRepo = new DriverRepo();
    private final SalaryRepo salaryRepo = new SalaryRepo();
    private final DriverAttendanceRepo attendanceRepo = new DriverAttendanceRepo();
    
    public boolean processDailySalary(String publicId, double revenue)
    {
        int driverId = driverRepo.getDriverIdByPublicID(publicId);
        
        if (driverId == -1)
        {
            return false;
        }
        
        double todayRevenue = salaryRepo.getTodayRevenue(driverId);
        
        
        String salaryType;
        double salaryAmount;
        
        if (revenue > 10000)
        {
            salaryType = "COMMISSION";
            salaryAmount = revenue * 0.079;
        }
        else
        {
            salaryType = "REGULAR";
            salaryAmount = 500;
        }
        
        return salaryRepo.insertSalary(driverId, revenue, salaryType, salaryAmount);
    }
    
}
