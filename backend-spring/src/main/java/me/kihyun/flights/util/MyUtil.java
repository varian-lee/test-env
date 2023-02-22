package me.kihyun.flights.util;

import java.sql.Timestamp;
import java.util.Date;

public class MyUtil {
    public static long[] getRangeBySeconds(long startSeconds) {
        Timestamp ts = new Timestamp(startSeconds*1000);  
        Date fromDate = new Date(ts.getTime());  
        fromDate.setHours(0); 
        fromDate.setMinutes(0); 
        fromDate.setSeconds(0);
        
        Date toDate = new Date(ts.getTime());  
        toDate.setHours(23); 
        toDate.setMinutes(59); 
        toDate.setSeconds(59);
        
        long[] results = new long[2];
        results[0] = fromDate.getTime()/1000;
        results[1] = toDate.getTime()/1000;
        
        return results;
    } 

    public static String concatForKey(String from, String to) {
        return from + ":" + to;
    }
}
