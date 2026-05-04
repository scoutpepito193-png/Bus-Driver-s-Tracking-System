package Service;

import Model.Driver;
import Repository.DriverRepo;
import Repository.SalaryRepo;
import Repository.DriverAttendanceRepo;
import java.util.ArrayList;
import java.util.List;

public class SalaryService
{
    private final DriverRepo driverRepo = new DriverRepo();
    private final SalaryRepo salaryRepo = new SalaryRepo();
    private final DriverAttendanceRepo attendanceRepo = new DriverAttendanceRepo();
    
    public void processDailySalary()
    {
        List<Driver> drivers = driverRepo.getAllActiveDrivers();
        
        for (Driver d : drivers)
        {
            int driverId = d.getdriverID();
            
            double todayRevenue = salaryRepo.getTodayRevenue(driverId);
            
            String salaryType;
            double salaryAmount;
            
            if (todayRevenue > 10000)
            {
                salaryType = "COMMISSION";
                salaryAmount = todayRevenue * 0.079; 
            }
            
            else
            {
                salaryType = "REGULAR";
                salaryAmount = 500;
            }
            
            if (!salaryRepo.salaryExists(driverId))
            {
                salaryRepo.insertSalary(driverId, todayRevenue, salaryType, salaryAmount);
            }
        }
    }
}
