package util;

import java.net.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TraccarAPI 
{
    private static String sessionCookie;
    
    public static boolean logIn(String email, String password)
    {
        try
        {
            URL url = new URL("http://demo.traccar.org/api/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            String data = "email=" + email + "&password=" + password;
            
            OutputStream os = conn.getOutputStream();            
            os.write(data.getBytes());
            os.flush();
            os.close();
            
            if(conn.getResponseCode() == 200)
            {
                String cookie = conn.getHeaderField("Set-Cookie");
                
                if(cookie != null)
                {
                    sessionCookie = cookie.split(";", 2)[0];
                }
                return true;
            }
            
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
public static int creationDeviceID(String name, String uniqueId)
{
    try
    {
        URL url = new URL("http://demo.traccar.org/api/devices");
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
    
public static JSONObject getLatestPosition(int deviceID)
{
    try
    {
        URL url = new URL("http://demo.traccar.org/api/positions?deviceId=" + deviceID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        // 🔥 IMPORTANT: send session cookie
        conn.setRequestProperty("Cookie", sessionCookie);
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();

        if (code != 200)
        {
            System.out.println("HTTP Error: " + code);
            return null;
        }

        BufferedReader buff = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = buff.readLine()) != null)
        {
            sb.append(line);
        }

        buff.close();

        JSONArray arr = new JSONArray(sb.toString());

        if (arr.length() > 0)
        {
            return arr.getJSONObject(0); // latest position
        }

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}
}
