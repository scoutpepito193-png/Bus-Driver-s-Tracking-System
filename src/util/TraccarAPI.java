package util;

import java.net.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TraccarAPI 
{
    private static String sessionCookie;
    private static final String BASE_URL = "https://reception-collectables-routine-colin.trycloudflare.com/";
    
    public static APIResponse logIn(String email, String password)
    {
        try
        {
            URL url = new URL(BASE_URL + "api/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            String data = "email=" + URLEncoder.encode(email, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            
            OutputStream os = conn.getOutputStream();            
            os.write(data.getBytes());
            os.flush();
            os.close();
            
            int code = conn.getResponseCode();
            
            if (code == 200)
            {
                String cookie = conn.getHeaderField("Set-Cookie");
                
                if (cookie != null)
                {
                    sessionCookie = cookie.split(";", 2)[0];
                    return new APIResponse(true, null);
                }
                
                return new APIResponse(false, "Missing session cookie");
            }
            
            BufferedReader err = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                
            String line;
            StringBuilder sb = new StringBuilder();
                
            while ((line = err.readLine()) != null)
            {
                sb.append(line);
            }
                
            return new APIResponse(false, sb.toString());
            
            
        }
        
        catch(Exception e)
        {
            return new APIResponse(false, e.getMessage());
        }   
    }
    
private static boolean isLoggedIn()
{
    if (sessionCookie == null || sessionCookie.isEmpty())
    {
        return false;
    }

    try
    {
        URL url = new URL(BASE_URL + "api/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", sessionCookie);

        int code = conn.getResponseCode();

        return code == 200;
    }
    catch (Exception e)
    {
        return false;
    }
}
    
    public static int creationDeviceID(String name, String uniqueId)
    {
        try
        {
            URL url = new URL(BASE_URL + "api/devices");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cookie", sessionCookie);

            String json = "{"
                    + "\"name\":\"" + name + "\","
                    + "\"uniqueId\":\"" + uniqueId + "\""
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200 && responseCode != 201)
            {
                BufferedReader err = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream())
                );

                StringBuilder error = new StringBuilder();
                String line;

                while ((line = err.readLine()) != null)
                {
                    error.append(line);
                }

                err.close();

                System.out.println("Failed: HTTP " + responseCode);
                System.out.println("Error: " + error);

                return -1;
            }

            BufferedReader buff = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = buff.readLine()) != null)
            {
                response.append(line);
            }

            buff.close();

            JSONObject obj = new JSONObject(response.toString());

            return obj.getInt("id");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }
    
public static JSONObject getLatestPosition(int deviceID) {
    try {
        if (!isLoggedIn()) {
            APIResponse res = logIn("scoutpepito193@gmail.com", "goaltogetrich4b");

            if (!res.isSuccess()) {
                System.out.println("Traccar login failed: " + res.getMessage());
                return null;
            }
        }

        URL url = new URL(BASE_URL + "api/positions?deviceId=" + deviceID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", sessionCookie);
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();
        System.out.println("Traccar position HTTP code: " + code);

        InputStream stream = code >= 200 && code < 300
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader buff = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = buff.readLine()) != null) {
            sb.append(line);
        }

        buff.close();

        String responseText = sb.toString();
        System.out.println("Traccar position response: " + responseText);

        if (code != 200) {
            return null;
        }

        if (responseText.trim().isEmpty() || responseText.trim().equals("[]")) {
            System.out.println("No GPS position found for device ID: " + deviceID);
            return null;
        }

        JSONArray arr = new JSONArray(responseText);

        if (arr.length() == 0) {
            return null;
        }

        return arr.getJSONObject(0);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
    

    public static double getSpeedKmh(int deviceID)
    {
        JSONObject position = getLatestPosition(deviceID);
        
        if (position == null)
        {
            return -1;
        }
        
        double speedMs = position.optDouble("speed", 0);
        
        return speedMs * 1.852;
    }
    
    public static boolean isOverSpeed(int deviceID)
    {
        double speed = getSpeedKmh(deviceID);
        
        if (speed < 0)
        {
            return false;
        }
        
        return speed > 70;
    }
}
