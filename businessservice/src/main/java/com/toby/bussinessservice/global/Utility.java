/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static Date sumDateAndNumer(Date date, Integer number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        date = c.getTime();
        return date;
    }

    public static Date subDateAndNumer(Date date, Integer number) {
        Integer numberNew = number * -1;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, numberNew);
        date = c.getTime();
        return date;
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date dicreaseHoursToJavaUtilDate(Date date, int hours) {
        Integer hoursNew = hours * -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hoursNew);
        return calendar.getTime();
    }

    public static Date hourNow() {
        Integer hoursNew = 7 * -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hoursNew);
        return calendar.getTime();
    }

    public static Date findDateStatic() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateInString = "06/05/2022";
            Date date = formatter.parse(dateInString);
            return date;
        } catch (ParseException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
