/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.support;

import com.isag.businessservice.IsagSupportService;
import com.isag.dto.IsagSupportDTO;
import com.isag.isag.SupportForm;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author khale
 */
@Named(value = "isagSupportMB")
@ViewScoped
public class IsagSupportMB extends SupportForm {

    

    private FileUploadEvent fileUploadEvent;
    private FileUploadController fileUploadController;

    private String uploadedImage;
    private String showImage;
    private String fileName;
    private String imageName;

    private Message messageBean;

    private StreamedContent image;

    @EJB
    private IsagSupportService isagSupportService;

    @PostConstruct
    @Override
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Editisagsupport")) {
            Integer isagSupport = (Integer) getContext().getSessionMap().get("isagsupportselected");
            setSupport(isagSupportService.findIsagSupport(isagSupport));
            findScreenBymodeule();
        }
    }

    @Override
    public String save() {
        getSupport().setImg(getImageName());
        setSupport(isagSupportService.save(getSupport(), getUserData().getUser()));
       savedMeesage(getUserData().getUserDDs().get("SAVED"));
     //  getSendEmailSMTP().sendTicketEmail(getUserData().getUser(), getSupport().getDescription(),getSupport().getScreen() != null ? getSupport().getScreen().getName() : null,getSupport().getScreen() != null ? getSupport().getScreen().getName() : null, "admin@isag-it.com", "isag@2020");
       
       
       
        return "";

    }

    public void reset() {
        setSupport(new IsagSupportDTO());
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void hishis() {
        System.err.println("Ok");
    }

    public void upload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            setUploadedImage(getFileUploadController().upload(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getDestination().concat(getUploadedImage()) : getFileUploadController().getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
            setImage(getMessageBean().findGraphicImage(getFileName()));
        } catch (Exception e) {

        }
    }

    public String exit() {
        exit("../Support/isagsupportList.xhtml");
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the service
     */
    /**
     * @return the fileUploadEvent
     */
    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    /**
     * @param fileUploadEvent the fileUploadEvent to set
     */
    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    /**
     * @return the fileUploadController
     */
    public FileUploadController getFileUploadController() {
        if (fileUploadController == null) {
            fileUploadController = new FileUploadController();
        }
        return fileUploadController;
    }

    /**
     * @param fileUploadController the fileUploadController to set
     */
    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    /**
     * @return the uploadedImage
     */
    public String getUploadedImage() {
        return uploadedImage;
    }

    /**
     * @param uploadedImage the uploadedImage to set
     */
    public void setUploadedImage(String uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    /**
     * @return the showImage
     */
    public String getShowImage() {
        return showImage;
    }

    /**
     * @param showImage the showImage to set
     */
    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the messageBean
     */
    public Message getMessageBean() {
        return messageBean;
    }

    /**
     * @param messageBean the messageBean to set
     */
    public void setMessageBean(Message messageBean) {
        this.messageBean = messageBean;
    }

    /**
     * @return the image
     */
    public StreamedContent getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(StreamedContent image) {
        this.image = image;
    }

}
