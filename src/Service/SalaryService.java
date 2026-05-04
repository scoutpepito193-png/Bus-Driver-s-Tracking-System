package Service;

import Model.Driver;
import Service.DriverAttendanceService;
import Repository.DriverRepo;
import Repository.SalaryRepo;
import Repository.DriverAttendanceRepo;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import util.TimeProvider;

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
            
            if (todayRevenue >= 10000)
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
    
    public double getMonthlySalary(int driverId)
    {
        LocalDate today = TimeProvider.now();
        
        return salaryRepo.getTotalSalary(driverId, today.getMonthValue(), today.getYear());
    }
    
    public double computeBonus(int driverId)
    {
        DriverAttendanceService das = new DriverAttendanceService();
        LocalDate today = TimeProvider.now();
        
        boolean isFull = das.isFullAttendance(driverId);
        
        if (!isFull)
        {
            return 0;
        }
        
        double monthlySalary = salaryRepo.getTotalSalary(driverId, today.getMonthValue(), today.getYear());
        
        double bonusRate = 0.10;
        
        
        return monthlySalary * bonusRate;
    }
}
