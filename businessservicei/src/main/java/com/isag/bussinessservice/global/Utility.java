/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author amr
 */
public class Utility {

    public static Boolean checkContainString(String code) {

        final Pattern standaloneNumber = Pattern.compile("[0-9]");
        Matcher matcher = standaloneNumber.matcher(code);
        int pos = 0;
        int count2 = 0;

        while (matcher.find(pos)) {
            pos = matcher.end();
            count2++;
        }

        Integer diff = code.length() - count2;
        if (diff == 0) {
            return true;
        }
        return false;
    }

    public static Date returnFromDate(Date date) {
        if (date != null) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date());
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            return calendar1.getTime();
        }
        return null;
    }
    
    public static Date returnToDate(Date date) {
        if (date != null) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date());
            calendar1.set(Calendar.HOUR_OF_DAY, 23);
            calendar1.set(Calendar.MINUTE, 59);
            calendar1.set(Calendar.SECOND, 59);
            return calendar1.getTime();
        }
        return null;
    }
    
    public static Integer calcDays(Date startDate , Date endDate , Integer number) {
        LocalDate startDateNew = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDateNew = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long monthBetween = ChronoUnit.MONTHS.between(startDateNew, endDateNew);
         return (number * (monthBetween.intValue() + 1));
       
    }
    
     public static Integer calcDaysBetween(Date startDate , Date endDate) {
        LocalDate startDateNew = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDateNew = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long daysBetween = ChronoUnit.DAYS.between(endDateNew , startDateNew);
        
         return  daysBetween.intValue();
       
    }
    
    public static Date sumDateAndNumer(Date date , Integer number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        date = c.getTime() ;
         return date ;
    }
   
    }   

