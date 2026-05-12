package Model;

public class Route
{
    private int routeID;
    private int terminalID;
   
    private String routeName;
    
    public int getRouteID() { return routeID; }
    public void setRouteID(int routeID) { this.routeID = routeID; }
    
    public int getTerminalID() { return terminalID; }
    public void setTerminalID(int terminalID) { this.terminalID = terminalID; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
}
