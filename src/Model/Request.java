package Model;

public class Request
{
    private int requestID;
    private int driverID;
    
    private String requestCode;
    private String requestInfo;
    private String status;
    private String details;
    private String routeName;
    
    public int getRequestID() { return requestID; }
    public void  setRequestID(int requestID) { this.requestID = requestID; }
    
    public int getDriverID() { return driverID; }
    public void setDriverID(int driverID) { this.driverID = driverID; }
    
    public String getRequestCode() { return requestCode; }
    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }
    
    public String getRequestInfo() { return requestInfo; }
    public void setRequestInfo(String requestInfo) { this.requestInfo = requestInfo; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }    
}
