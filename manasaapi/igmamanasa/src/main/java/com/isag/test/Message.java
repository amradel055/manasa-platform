/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.test;

import com.isag.isag.UserData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


public class Message {

    private String message = "Hello World!";

    public StreamedContent findGraphicImage(String imageName) {
        InputStream stream;
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            UserData user = (UserData) context.getSessionMap().get("userLogInData");
            stream = new FileInputStream(new File(user.getImagePath() + imageName));
            return new DefaultStreamedContent(stream, "image/png", imageName);
//            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public StreamedContent findGraphicNotificationImage(String imageName) {
        InputStream stream;
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            UserData user = (UserData) context.getSessionMap().get("userLogInData");
            stream = new FileInputStream(new File(user.getImagePath().concat("notification/") + imageName));
            return new DefaultStreamedContent(stream, "image/png", imageName);
//            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
