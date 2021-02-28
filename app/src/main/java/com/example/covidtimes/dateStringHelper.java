package com.example.covidtimes;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class dateStringHelper {
    private static String default_time = "T00:00:00Z";
    private static String default_state_time = "T00:00:00.000";

    private static String addDashes(String year, String month, String day){
        return year + "-" + month + "-" + day;
    }

    public static String getQueryableDate(String year, String month, String day){
        return addDashes(year,month,day) + default_time;
    }
    public static String getStateQueryableDate(String year, String month, String day){
        return addDashes(year, month, day) + default_state_time;
    }
    public static String getCurrentStateQueryableDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        String date = sdf.format(c.getTime());
        return date + default_state_time;
    }

    public static boolean isValidDate(String year, String month, String day){
        String date = addDashes(year,month,day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try{
            sdf.parse(date);
        } catch (ParseException e){
            return false;
        }
        return true;
    }
}
