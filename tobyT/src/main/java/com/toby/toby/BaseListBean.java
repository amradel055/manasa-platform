/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq004
 */
public abstract class BaseListBean extends Basic implements BaseManagedBean {

    public abstract void init();

    public abstract void delete();

    public abstract void filter();

    public abstract String goToAdd();

    public abstract String goToEdit();

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger("redirect error").log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
// public boolean isFileExist(String filePath) {
//        File file = new File(getUserData().getImageReportPath());
//        return file.exists();
//    }

    public boolean savedMeesage(UserData userData, String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), msg));
        return false;
    }

    public boolean errorMessage(UserData userData, String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), msg));
        return false;
    }
}
