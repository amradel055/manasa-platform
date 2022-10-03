/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.teacherQuestion;

import com.isag.businessservice.QuestionCommonService;
import com.isag.dto.QuestionAnswerDetailDTO;
import com.isag.dto.QuestionCommonDTO;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.isag.BaseFormBean;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author hq004
 */
@Named(value = "teacherQuestionsFormMB")
@ViewScoped
public class TeacherQuestionsFormMB extends BaseFormBean {

    private QuestionCommonDTO questionCommonDTO;
    private Message messageBean;
    private FileUploadEvent fileUploadEvent;
    private String uploadedImage;
    private FileUploadController fileUploadController = new FileUploadController();
    private String showImage;
    private String imageName;
    private String fileName;
    private StreamedContent image;
    private boolean exitBoolean = Boolean.FALSE;

    @EJB
    QuestionCommonService questionCommonService;

    public void upload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            setUploadedImage(getFileUploadController().upload(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getDestination().concat(getUploadedImage()) : getFileUploadController().getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
            getQuestionCommonDTO().setImage(getFileName());
            setImage(getMessageBean().findGraphicImage(getFileName()));
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "upload");
        }
    }

    public void addAnswerDetail() {
        try {
            if (getQuestionCommonDTO().getQuestionAnswerDTO() != null && getQuestionCommonDTO().getQuestionAnswerDTO().getId() != null) {
                setExitBoolean((boolean) Boolean.TRUE);
                QuestionAnswerDetailDTO questionAnswerDetailDTO = new QuestionAnswerDetailDTO();
                questionAnswerDetailDTO.setMarkEdit(Boolean.TRUE);
                questionAnswerDetailDTO.setQuestionAnswerId(getQuestionCommonDTO().getQuestionAnswerDTO().getId());
                questionCommonDTO.getQuestionAnswerDTO().getAnswerDetailDTOList().add(0, questionAnswerDetailDTO);
            }
        } catch (Exception e) {
            saveError(e, "answer", "add");
        }
    }

    @Override
    public String save() {
        if (getQuestionCommonDTO() != null) {

            setQuestionCommonDTO(questionCommonService.save(getQuestionCommonDTO(), getUserData().getUser()));
        }
        return "";
    }

    @Override
    public void init() {
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

    /**
     * @return the questionCommonDTO
     */
    public QuestionCommonDTO getQuestionCommonDTO() {
        if (questionCommonDTO == null) {
            setQuestionCommonDTO(new QuestionCommonDTO());

        }
        return questionCommonDTO;
    }

    /**
     * @param questionCommonDTO the questionCommonDTO to set
     */
    public void setQuestionCommonDTO(QuestionCommonDTO questionCommonDTO) {
        this.questionCommonDTO = questionCommonDTO;
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
     * @return the exitBoolean
     */
    public boolean isExitBoolean() {
        return exitBoolean;
    }

    /**
     * @param exitBoolean the exitBoolean to set
     */
    public void setExitBoolean(boolean exitBoolean) {
        this.exitBoolean = exitBoolean;
    }

}
