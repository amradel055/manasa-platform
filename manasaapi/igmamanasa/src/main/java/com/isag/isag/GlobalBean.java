/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author amr
 */
@Named(value = "globalBean")
@RequestScoped
public class GlobalBean implements Serializable {
    
    private StreamedContent image ; 
    private StreamedContent graphicImage;
    private UserData userData;
    
    public GlobalBean(){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        findGraphicImage();
    }
    public StreamedContent findImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("Isag_1549986541_2.PNG");
            return new DefaultStreamedContent(new FileInputStream(new File("D:\\img", filename)));
        }
    }
    
    public void findGraphicImage(){
        InputStream stream;
        try {
            stream = new FileInputStream(new File(getUserData().getImagePath() + getUserData().getUserBranch().getImage()));
            this.setGraphicImage(new DefaultStreamedContent(stream, "image/png", getUserData().getUserBranch().getImage()));
//            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public StreamedContent findGraphicImage(String imageName){
        InputStream stream;
        try {
            stream = new FileInputStream(new File(getUserData().getImagePath() + imageName));
            return new DefaultStreamedContent(stream, "image/png", imageName);
//            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * @return the image
     */
    public StreamedContent getImage() {
        if(image == null){
            try {
                image = findImage();
            } catch (IOException ex) {
                Logger.getLogger(GlobalBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(StreamedContent image) {
        this.image = image;
    }

    /**
     * @return the graphicImage
     */
    public StreamedContent getGraphicImage() {
        return graphicImage;
    }

    /**
     * @param graphicImage the graphicImage to set
     */
    public void setGraphicImage(StreamedContent graphicImage) {
        this.graphicImage = graphicImage;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    
}
