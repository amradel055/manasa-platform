/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

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
}
