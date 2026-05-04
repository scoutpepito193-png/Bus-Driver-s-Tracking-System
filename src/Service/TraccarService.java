package Service;

import util.TraccarAPI;
import util.APIResponse;

public class TraccarService
{
    private static final String EMAIL = "scoutpepito193@gmail.com";
    private static final String PASSWORD = "goaltogetrich4b";
    
    public APIResponse initSession()
    {
        return TraccarAPI.logIn(EMAIL, PASSWORD);
    }
    
    public int createDevice(String name, String uniqueId)
    {
        return TraccarAPI.creationDeviceID(name, uniqueId);
    }
    
    public Object getPosition(int deviceId)
    {
        return TraccarAPI.getLatestPosition(deviceId);
    }
}
