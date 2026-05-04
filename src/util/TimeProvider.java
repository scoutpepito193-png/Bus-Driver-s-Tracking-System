package util;

import java.time.LocalDate;

public class TimeProvider
{
    public static LocalDate now()
    {
        return LocalDate.now();
    }
    
    public static LocalDate startOfMonth(LocalDate date)
    {
        return date.withDayOfMonth(1);
    }
    
    public static LocalDate startOfNextMonth(LocalDate date)
    {
        return date.withDayOfMonth(1).plusMonths(1);
    }
}
