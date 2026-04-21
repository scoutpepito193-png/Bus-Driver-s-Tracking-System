package Test;

import Repository.dbConnection;
import java.sql.Connection;

public class TestConnection
{
    public static void main(String[] args)
    {
        Connection connect = dbConnection.getConnection();
        
        System.out.println(connect);
    }
}
