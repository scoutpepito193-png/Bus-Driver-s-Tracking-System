package Model;

public class DriverPerformance
{
    
    private Driver driver;
    private double averageKMPL;
    private int totalTickets;
    private double totalRevenue;
    
    public Driver getdriver() { return driver; }
    public void setdriver(Driver driver) { this.driver = driver; }
    
    public double getaverageKMPL() { return averageKMPL; }
    public void setaverageKMPL(double averageKMPL) { this.averageKMPL = averageKMPL; }
    
    public int gettotalTickets() { return totalTickets; }
    public void settotalTickets(int totalTickets) { this.totalTickets = totalTickets; }
    
    public double gettotalRevenue() { return totalRevenue; }
    public void settotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    
}
