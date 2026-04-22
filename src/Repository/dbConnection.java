package Repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection
{
    private static final String URL = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.nasqhnnkzinnmujlalso";
    private static final String PASSWORD = "goaltogetrich4b";
    
    public static Connection getConnection()
    {
        Connection conn = null;
        
        try
        {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e)
        {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
        
        return conn;
    }
    
    
}
