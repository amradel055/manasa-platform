/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.uploadfile;

import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.TobyCompany;
import com.toby.toby.OperatingSystem;
import com.toby.toby.UserData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author WIN7
 */
public class FileUploadController {

    private UserData userData;

    private String destination;
    private String imageName;
    String imageId ;
    List<TobyCompany> tobyCompanyList;

    List<Branch> branchList;
    List<InvItem> otherItems;
    List<InvItem> invItem;

    Boolean isWindows = OperatingSystem.isWindows();

    public FileUploadController() {
        this.destination = getUserData().getImagePath();
    }

    public String upload(FileUploadEvent event) {
        
        imageReName(event.getFile());

        try {

            copyFile(getImageName(), event.getFile().getInputstream(), getDestination());

            FacesMessage msg = new FacesMessage("Success! ", getImageName() + " is uploaded.");
            System.out.println("Success! " + getImageName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return getImageName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null ;
    }

    public synchronized void imageReName(UploadedFile file) {
        imageId = (int) (new Date().getTime() / 1000) + "_";
        imageName = "Toby_" + imageId + file.getFileName();
        setImageName(imageName);
    }


    public boolean copyFile(String fileName, InputStream in, String destination) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        try {

            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();
            System.out.println("New image is created!");
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        if (userData == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
        }
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
