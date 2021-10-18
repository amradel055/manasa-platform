/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Mostafa
 */
public class Session   implements HttpSessionListener{
     @Override
    public void sessionCreated(HttpSessionEvent se) {
     System.out.print("session Created");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
         
           System.out.print("session Destroyed"); 
        
  
    }
}
