/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author amr
 */
public class Utility {
    
    public static Boolean checkContainString(String code){

        final Pattern standaloneNumber = Pattern.compile("[0-9]");
        Matcher matcher = standaloneNumber.matcher(code);
        int pos = 0;
        int count2 = 0;

        while (matcher.find(pos)) {
            pos = matcher.end();
            count2++;
        }
        
        Integer diff = code.length() - count2;
        if(diff == 0){
            return true;
        }
        return false;
    }
    
    public static Date sumDateAndNumer(Date date , Integer number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        date = c.getTime() ;
         return date ;
    }
}
