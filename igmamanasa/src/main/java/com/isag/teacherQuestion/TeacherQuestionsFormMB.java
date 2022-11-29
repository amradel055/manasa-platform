/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.teacherQuestion;

import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.QuestionAnswerDetailService;
import com.isag.businessservice.QuestionCommonService;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.QuestionAnswerDetailDTO;
import com.isag.dto.QuestionCommonDTO;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.isag.BaseFormBean;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author hq004
 */
@Named(value = "teacherQuestionsFormMB")
@ViewScoped
public class TeacherQuestionsFormMB extends BaseFormBean {

    private List<MaterialsDTO> materialsDTOList;
    private QuestionCommonDTO questionCommonDTO;
    private QuestionAnswerDetailDTO questionAnswerDetailDTOSelected;

    private Message messageBean;
    private FileUploadEvent fileUploadEvent;
    private String uploadedImage;
    private FileUploadController fileUploadController = new FileUploadController();
    private String showImage;
    private String imageName;
    private String fileName;
    private StreamedContent image;
    private boolean exitBoolean = Boolean.FALSE;

    private String host;

    @EJB
    QuestionCommonService questionCommonService;

    @EJB
    MaterialsService materialsService;

    @EJB
    QuestionAnswerDetailService questionAnswerDetailService;

    public void deleteImage() {
        if (getQuestionAnswerDetailDTOSelected() != null && getQuestionAnswerDetailDTOSelected().getId() != null) {
            questionAnswerDetailService.deleteByDetailId(getQuestionAnswerDetailDTOSelected().getId());

        }
        getQuestionCommonDTO().getQuestionAnswerDTO().getAnswerDetailDTOList().remove(getQuestionAnswerDetailDTOSelected());
    }

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
    
     public void hishis(){
        System.err.println("hishis  ---- ");
    }

    public void upload2(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            setUploadedImage(getFileUploadController().upload(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getDestination().concat(getUploadedImage()) : getFileUploadController().getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
//            for (QuestionAnswerDetailDTO questionAnswerDetailDTO : getQuestionCommonDTO().getQuestionAnswerDTO().getAnswerDetailDTOList()) {
            getQuestionAnswerDetailDTOSelected().setImage(getFileName());
//            setImage(getMessageBean().findGraphicImage(getFileName()));

//            }
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "upload");
        }
    }

    public void addAnswerDetail() {
        try {
            if (getQuestionCommonDTO().getQuestionAnswerDTO() != null && getQuestionCommonDTO().getQuestionAnswerDTO().getId() == null) {
                setExitBoolean((boolean) Boolean.TRUE);
                QuestionAnswerDetailDTO detailDTO = new QuestionAnswerDetailDTO();
                detailDTO.setMarkEdit(Boolean.TRUE);
                detailDTO.setIndex(getIndex());
                questionCommonDTO.getQuestionAnswerDTO().getAnswerDetailDTOList().add(detailDTO);
                questionAnswerDetailDTOSelected = detailDTO;
            }
        } catch (Exception e) {
            saveError(e, "answer", "add");
        }
    }

    public void reset() {
        try {
            setQuestionCommonDTO(null);

        } catch (Exception e) {
            saveError(e, "QuestionForm", "reset");
        }
    }

    @Override
    public String save() {
        if (getQuestionCommonDTO() != null) {

            setQuestionCommonDTO(questionCommonService.save(getQuestionCommonDTO(), getUserData().getUser()));
            reset();
        }
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        load();

    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddTeachersQuestions")) {
                reset();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditTeacherQuestions")) {
                Integer questionSelected = ((Integer) getContext().getSessionMap().get("teachersDTOSelected"));
                setEditedQuestionsValues(questionSelected);

            }

        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "init");
        }

    }

    public void setEditedQuestionsValues(Integer id) {
        try {
            setQuestionCommonDTO(questionCommonService.findDto(id));
        } catch (Exception e) {
            saveError(e, "question", "setEdit");
        }
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

    /**
     * @return the materialsDTOList
     */
    public List<MaterialsDTO> getMaterialsDTOList() {
        if (materialsDTOList == null) {
            setMaterialsDTOList(materialsService.findMaterialsByTeacher(getUserData().getUser().getId()));
        }
        return materialsDTOList;
    }

    /**
     * @param materialsDTOList the materialsDTOList to set
     */
    public void setMaterialsDTOList(List<MaterialsDTO> materialsDTOList) {
        this.materialsDTOList = materialsDTOList;
    }

    /**
     * @return the questionAnswerDetailDTOSelected
     */
    public QuestionAnswerDetailDTO getQuestionAnswerDetailDTOSelected() {
        if (questionAnswerDetailDTOSelected == null) {
            setQuestionAnswerDetailDTOSelected(new QuestionAnswerDetailDTO());
        }
        return questionAnswerDetailDTOSelected;
    }

    /**
     * @param questionAnswerDetailDTOSelected the
     * questionAnswerDetailDTOSelected to set
     */
    public void setQuestionAnswerDetailDTOSelected(QuestionAnswerDetailDTO questionAnswerDetailDTOSelected) {
        this.questionAnswerDetailDTOSelected = questionAnswerDetailDTOSelected;
    }

    /**
     * @return the host
     */
    public String getHost() {
        HttpServletRequest request = (HttpServletRequest) getContext().getRequest();
        setHost(request.getRequestURL().substring(0, request.getRequestURL().indexOf("faces/")).concat("images/"));
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

}
