/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.notification;

import com.isag.converter.IsagUserDTOConverter;
import com.isag.dto.IgmaTokensDTO;
import com.isag.dto.NotificationDTO;
import com.isag.isag.BaseFormBean;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.StreamedContent;
import com.isag.businessservice.IgmaTokensService;

/**
 *
 * @author AhmedEssam@ISAG.com
 * @since 2020.06.12
 */
@Named(value = "notificationFormMB")
@ViewScoped
public class NotificationMB extends BaseFormBean {

    private NotificationDTO notificationDTO;
    private FileUploadEvent imageFileEvent;
    private FileUploadEvent iconFileEvent;
    private String uploadedImage;
    private String uploadedIcon;
    private FileUploadController fileUploadController;
    private Message messageBean;
    private String showImage;
    private String fileName;
    private String imageName;
    private StreamedContent image;
    private String showIcon;
    private String iconName;
    private StreamedContent icon;
    private String host;
    private Integer step;
    private String selectedImage;
    private String url;
    private HttpServletRequest request;
    private IsagUserDTOConverter isagUserDTOConverter;

    @EJB
    IgmaTokensService isagTokensService;

    @Override
    @PostConstruct
    public void init() {
        url = getRequest().getRequestURL().toString();
        if (!url.contains("home")) {
            setHost(getRequest().getRequestURL().substring(0, getRequest().getRequestURL().indexOf("faces/")).concat("images/"));
            getNotificationDTO().setSendType(0);
            setStep(0);
            preperImages();
            getNotificationDTO().setIsagUserDTOList(isagTokensService.getAllIgmaUserDTO());
        }
    }

    private void preperImages() {
        File file = new File(getUserData().getImagePath().concat("notification/"));
        if (file.list() != null) {
            for (String img : file.list()) {
                getNotificationDTO().getImages().add(img);
            }
        }
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sendSpecificToken(NotificationDTO notificationDTO) {
        try {
            HttpClient client = HttpClientBuilder.create().build();//"https://fcm.googleapis.com/v1/projects/isag-5a27c/messages:send"
            HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.setHeader("Content-type", "application/json;  UTF-8");
            post.setHeader("Authorization", "key=AAAALdWGdDI:APA91bFiHvLR-epkyVB8oIzo7ypl12SVvs-WmGlzeqW8L1pXsGT4_XF0m-s3iEcMAVIbA8LgCbSU9I6Qmj_GHltIpKgTv0DPpVD-t6SQ2sHTvxNn0Ij0MQHvJdanfTj8PFjQ3A0OywwG");
            JSONObject message = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", notificationDTO.getMessage());
            notification.put("title", notificationDTO.getTitle());
            notification.put("image", notificationDTO.getImage());
            notification.put("icon", notificationDTO.getIcon());
            notification.put("dir", notificationDTO.getDir());
            notification.put("badge", notificationDTO.getBadge());
//            notification.put("priority", "high");
            notification.put("content_available", true);
            message.put("notification", notification);
            message.put("to", notificationDTO.getToken());
            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            System.out.println(response);
            System.out.println(message);
        } catch (Exception e) {
            saveError(e, "sendSpecificToken", "NotificationMB");
        }
    }

    public void uploadImage(FileUploadEvent event) {
        try {
            imageFileEvent = event;
            setUploadedImage(getFileUploadController().uploadNotification(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getNotificationDestination().concat(getUploadedImage()) : getFileUploadController().getNotificationDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
            setImage(getMessageBean().findGraphicNotificationImage(getFileName()));
        } catch (Exception e) {
            saveError(e, "uploadImage", "NotificationMB");
        }
    }

    public void uploadIcon(FileUploadEvent event) {
        try {
            iconFileEvent = event;
            setUploadedIcon(getFileUploadController().uploadNotification(event));
            setShowIcon((getUploadedIcon() != null) ? getFileUploadController().getNotificationDestination().concat(getUploadedIcon()) : getFileUploadController().getNotificationDestination().concat("1.png"));
            setFileName(getUploadedIcon());
            setIconName(getFileName());
            setIcon(getMessageBean().findGraphicNotificationImage(getFileName()));
        } catch (Exception e) {
            saveError(e, "uploadIcon", "NotificationMB");
        }
    }

    public void notficationImage(Boolean position) {
        if (position) {
            if (getNotificationDTO().getImages() != null && getNotificationDTO().getImages().size() - 1 == getStep()) {
                setStep(0);
            } else {
                setStep(getStep() + 1);
            }

        } else {
            if (getNotificationDTO().getImages() != null && getStep() == 0) {
                setStep(getNotificationDTO().getImages().size() - 1);
            } else {
                setStep(getStep() - 1);
            }
        }
        selectedImage = getNotificationDTO().getImages().get(getStep());
    }

    public void selectImage() {
        if (selectedImage != null && !selectedImage.isEmpty()) {
            getNotificationDTO().setImage(getHost().concat(selectedImage));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), "تم اختيار صوره " + selectedImage));
        } else {
            selectedImage = getNotificationDTO().getImages().get(getStep());
            getNotificationDTO().setImage(getHost().concat(selectedImage));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), "تم اختيار صوره " + selectedImage));
        }
    }

    public void send() throws IOException {
        if (getNotificationDTO() != null) {

            if (getIcon() == null) {
                getNotificationDTO().setIcon("http://159.65.84.164:8080/igma/faces/javax.faces.resource/isaglogowhite.png?ln=img");
            } else {
                getNotificationDTO().setIcon(getHost().concat(getIcon().getName()));
            }
            if (getImage() != null) {
                getNotificationDTO().setImage(getHost().concat(getImage().getName()));
            }

            if (getNotificationDTO().getSendType() == 0) {
                sendToAll();
            } else if (getNotificationDTO().getSendType() == 1) {
                sendToOpen();
            } else if (getNotificationDTO().getSendType() == 2) {
                sendToSpecificUser();
            }
        }

    }

    private void sendToAll() {
        getNotificationDTO().setTokens(isagTokensService.getAllTokan());
        if (getNotificationDTO().getTokens() != null && !getNotificationDTO().getTokens().isEmpty()) {
            for (String token : getNotificationDTO().getTokens()) {
                getNotificationDTO().setToken(token);
                sendSpecificToken(getNotificationDTO());
            }
        }
    }

    private void sendToOpen() {
        getNotificationDTO().setTokens(isagTokensService.getAllTokanOpen());
        if (getNotificationDTO().getTokens() != null && !getNotificationDTO().getTokens().isEmpty()) {
            for (String token : getNotificationDTO().getTokens()) {
                getNotificationDTO().setToken(token);
                sendSpecificToken(getNotificationDTO());
            }
        }

    }

    private void sendToSpecificUser() {
        if (getNotificationDTO().getIsagUserDTOSelected() != null && !getNotificationDTO().getIsagUserDTOSelected().getToken().isEmpty()) {
            getNotificationDTO().setToken(getNotificationDTO().getIsagUserDTOSelected().getToken());
            sendSpecificToken(getNotificationDTO());
        }
    }

    public void updateToken() {
        if (url.contains("home") && getNotificationDTO().getToken() != null) {
            if (getRequest().getAttribute("sessionId") == null || (getRequest().getAttribute("sessionId") != null && !request.getAttribute("sessionId").equals(request.getSession().getId()))) {
                getRequest().setAttribute("sessionId", getRequest().getSession().getId());
                IgmaTokensDTO isagTokensDTO = new IgmaTokensDTO();
                isagTokensDTO.setToken(getNotificationDTO().getToken());
                isagTokensService.addIgmaTokenAlreadyExist(isagTokensDTO, getUserData().getUserlogin());
            }
//            if (getRequest().getAttribute("sessionId").equals(getRequest().getSession().getId())) {
//                getRequest().setAttribute("sessionId", getRequest().getSession().getId());
//                IgmaTokensDTO isagTokensDTO = new IgmaTokensDTO();
//                isagTokensDTO.setToken(notificationDTO.getToken());
//                isagTokensService.addIsagTokenAlreadyExist(isagTokensDTO, getUserData().getUserlogin());
//            }
        }
    }

    public void closeServer() {
        getNotificationDTO().setIcon("http://159.65.84.164:8080/igma/faces/javax.faces.resource/isaglogowhite.png?ln=img");
        getNotificationDTO().setImage("http://159.65.84.164:8080/igma/faces/javax.faces.resource/isagUpdate.jpg?ln=img");
        getNotificationDTO().setTitle("تحديث البرنامج");
        getNotificationDTO().setMessage("عميلنا العزيز سيتم ايقاف الخدمه مؤقتا بعد عشره دقائق لإجراء بعض التحديثات من فضلك يرجي حفظ التعديلات لعدم فقدانها");
        sendToOpen();
    }

    /**
     * @return the notificationDTO
     */
    public NotificationDTO getNotificationDTO() {
        if (notificationDTO == null) {
            notificationDTO = new NotificationDTO();
        }
        return notificationDTO;
    }

    /**
     * @param notificationDTO the notificationDTO to set
     */
    public void setNotificationDTO(NotificationDTO notificationDTO) {
        this.notificationDTO = notificationDTO;
    }

    /**
     * @param imageFileEvent the imageFileEvent to set
     */
    public void setImageFileEvent(FileUploadEvent imageFileEvent) {
        this.imageFileEvent = imageFileEvent;
    }

    /**
     * @return the iconFileEvent
     */
    public FileUploadEvent getIconFileEvent() {
        return iconFileEvent;
    }

    /**
     * @param iconFileEvent the iconFileEvent to set
     */
    public void setIconFileEvent(FileUploadEvent iconFileEvent) {
        this.iconFileEvent = iconFileEvent;
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
     * @return the uploadedIcon
     */
    public String getUploadedIcon() {
        return uploadedIcon;
    }

    /**
     * @param uploadedIcon the uploadedIcon to set
     */
    public void setUploadedIcon(String uploadedIcon) {
        this.uploadedIcon = uploadedIcon;
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
        if (messageBean == null) {
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
     * @return the showIcon
     */
    public String getShowIcon() {
        return showIcon;
    }

    /**
     * @param showIcon the showIcon to set
     */
    public void setShowIcon(String showIcon) {
        this.showIcon = showIcon;
    }

    /**
     * @return the iconName
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * @param iconName the iconName to set
     */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    /**
     * @return the icon
     */
    public StreamedContent getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(StreamedContent icon) {
        this.icon = icon;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the step
     */
    public Integer getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(Integer step) {
        this.step = step;
    }

    /**
     * @return the selectedImage
     */
    public String getSelectedImage() {
        return selectedImage;
    }

    /**
     * @param selectedImage the selectedImage to set
     */
    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return the fileUploadController
     */
    public FileUploadController getFileUploadController() {
        if (getUserData() != null) {
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
     * @return the isagUserDTOConverter
     */
    public IsagUserDTOConverter getIsagUserDTOConverter() {
        if (isagUserDTOConverter == null) {
            isagUserDTOConverter = new IsagUserDTOConverter(getNotificationDTO().getIsagUserDTOList());
        }
        return isagUserDTOConverter;
    }

    /**
     * @param isagUserDTOConverter the isagUserDTOConverter to set
     */
    public void setIsagUserDTOConverter(IsagUserDTOConverter isagUserDTOConverter) {
        this.isagUserDTOConverter = isagUserDTOConverter;
    }
}
