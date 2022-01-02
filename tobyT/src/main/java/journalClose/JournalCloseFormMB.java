/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journalClose;

import com.toby.businessservice.JournalCloseService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.JournalCloseDTO;
import com.toby.dto.JournalCloseDetailsDTO;
import com.toby.test.Message;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import com.toby.uploadfile.FileUploadController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesListener;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hq002
 */
@Named(value = "JournalCloseFormMB")
@ViewScoped
public class JournalCloseFormMB extends BaseFormBean implements Serializable {

    private InvInventoryDTOConverter invInventoryDTOConverter;
    private JournalCloseDTO journalCloseDTO;
    private JournalCloseDetailsDTO journalCloseDetailsDTO;
    private List<JournalCloseDetailsDTO> detailsDTOsList = new ArrayList<>() ;
    private List<InvInventoryDTO> invInventoryDTOList;
    private UploadedFile files;
    private FileUploadEvent fileUploadEvent;
    private FileUploadController fileUploadController = new FileUploadController();
    private String uploadedImage;
    private String showImage;
    private String fileName;
    private String imageName;
    private Message messageBean;
    private StreamedContent image;
    private String destination;
    String imageId ;
    @EJB
    private JournalCloseService journalCloseService;
    
    
    
    
//    public void uploadMultiple() {
//        if (getFiles() != null) {
//
//            FacesMessage message = new FacesMessage("Successful", getFiles().getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//
//        }
//    }

    public void upload(FileUploadEvent event) {
        
        try {
            setFileUploadEvent(event);
            setUploadedImage(uploadImage(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getDestination().concat(getUploadedImage()) : getFileUploadController().getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
            setImage(getMessageBean().findGraphicImage(getFileName()));
            
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "upload");
        }
    }
    
     public synchronized void imageReName(UploadedFile file) {
        imageId = (int) (new Date().getTime() / 1000) + "_";
        imageName = "Toby_" + imageId + file.getFileName();
        setImageName(imageName);
  
    }
    
     public String uploadImage(FileUploadEvent event) {
        
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
         JournalCloseDetailsDTO journalCloseDetailDTO = new JournalCloseDetailsDTO();
         journalCloseDetailDTO.setImageName(getImageName());
         detailsDTOsList.add(journalCloseDetailDTO);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
       
    public List<InvInventoryDTO> completeInventory(String query) {
        return AutoComplete.completeInventoryDTO(query, getInvInventoryDTOList(), getInvInventoryDTOConverter());
    }

    public List<InvInventoryDTO> completeGallary(String query) {
        return AutoComplete.completeInventoryDTO(query, getGallaryLists(), getGallaryDTOConverters());
    }

    public JournalCloseFormMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {

        } catch (Exception e) {
            saveError(e, "UserFormBean", "init");
        }
    }

    @Override
    public String save() {
        if(getJournalCloseDTO().getGalleryId() != null){
         journalCloseService.save(getJournalCloseDTO() , getDetailsDTOsList());
         return "" ;
        }
        else {
         return "" ;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the journalCloseDTO
     */
    public JournalCloseDTO getJournalCloseDTO() {
        if (journalCloseDTO == null) {
            journalCloseDTO = new JournalCloseDTO();
        }
        return journalCloseDTO;
    }

    /**
     * @param journalCloseDTO the journalCloseDTO to set
     */
    public void setJournalCloseDTO(JournalCloseDTO journalCloseDTO) {
        this.journalCloseDTO = journalCloseDTO;
    }

    /**
     * @return the journalCloseDetailsDTO
     */
    public JournalCloseDetailsDTO getJournalCloseDetailsDTO() {
        if (journalCloseDetailsDTO == null) {
            journalCloseDetailsDTO = new JournalCloseDetailsDTO();
        }
        return journalCloseDetailsDTO;
    }

    /**
     * @param journalCloseDetailsDTO the journalCloseDetailsDTO to set
     */
    public void setJournalCloseDetailsDTO(JournalCloseDetailsDTO journalCloseDetailsDTO) {
        this.journalCloseDetailsDTO = journalCloseDetailsDTO;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            invInventoryDTOList = getUserData().getInventoryDTOList();
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the files
     */
    public UploadedFile getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(UploadedFile files) {
        this.files = files;
    }

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
        if(messageBean == null){
        messageBean = new Message();
        }
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

    /**
     * @return the detailsDTOsList
     */
    public List<JournalCloseDetailsDTO> getDetailsDTOsList() {
        return detailsDTOsList;
    }

    /**
     * @param detailsDTOsList the detailsDTOsList to set
     */
    public void setDetailsDTOsList(List<JournalCloseDetailsDTO> detailsDTOsList) {
        this.detailsDTOsList = detailsDTOsList;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

}
