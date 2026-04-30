package Model;

public class Request
{
    private int requestID;
    
    private String requestCode;
    private String requestInfo;
    private String status;
    private String details;
    
    public int getRequestID() { return requestID; }
    public void  setRequestID(int requestID) { this.requestID = requestID; }
    
    public String getRequestCode() { return requestCode; }
    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }
    
    public String getRequestInfo() { return requestInfo; }
    public void setRequestInfo(String requestInfo) { this.requestInfo = requestInfo; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
