/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.lesson;

import com.isag.businessservice.IgmaUserService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.LessonService;
import com.isag.businessservice.PeriodsService;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.converter.SchoolStagesGuideDTOConverter;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.LessonDTO;
import com.isag.dto.PeriodsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.isag.BaseFormBean;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "lessonFormMB")
@ViewScoped
public class LessonFormMB extends BaseFormBean {

    @EJB
    SchoolStagesGuideService schoolStagesGuideService;
    @EJB
    IgmaUserService igmaUserService;
    @EJB
    MaterialsService materialsService;
    @EJB
    LessonService lessonService;
    @EJB
    PeriodsService periodsService;

    private String host;

    private FileUploadEvent fileUploadEvent;
    private String uploadedImage;
    private FileUploadController fileUploadController = new FileUploadController();
    private String showImage;
    private String imageName;
    private String fileName;
    private StreamedContent image;
    private Message messageBean;

    private List<SchoolStagesGuideDTO> schoolStagesGuideDTOList;
    private SchoolStagesGuideDTOConverter schoolStagesGuideDTOConverter;
    private List<MaterialsDTO> materialsDTOList;
    private LessonDTO lessonDTO;

    private List<PeriodsDTO> periodsDTOList;

    public void upload(FileUploadEvent event) {
        try {
            setFileUploadEvent(event);
            setUploadedImage(getFileUploadController().upload(event));
            setShowImage((getUploadedImage() != null) ? getFileUploadController().getDestination().concat(getUploadedImage()) : getFileUploadController().getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(getFileName());
            getLessonDTO().setImage(getFileName());
            setImage(getMessageBean().findGraphicImage(getFileName()));
        } catch (Exception e) {
            saveError(e, "lessonFormBean", "upload");
        }
    }

    public void fillMaterialList() {
        setMaterialsDTOList(materialsService.findMaterialsByStage(getLessonDTO().getSchoolStagesGuideId().getId()));
    }

    public void fillParentList() {
        periodsDTOList = periodsService.findPeriodsByMaterialId(getLessonDTO().getMaterialId());
    }

    @Override
    public String save() {
        lessonService.save(getLessonDTO(), getUserData().getUser());
        reset();
        exit();
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddLesson")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditLesson")) {
            try {
                Integer studenPeriodId = (Integer) getContext().getSessionMap().get("lessonSelected");
                setLessonDTO(lessonService.findLessonById(studenPeriodId));
                fillMaterialList();
                fillParentList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setLessonDTO(null);
    }

    public String exit() {
        exit("../lesson/lessonList.xhtml");
        return "";
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
     * @return the materialsDTOList
     */
    public List<MaterialsDTO> getMaterialsDTOList() {
        if (materialsDTOList == null) {
            setMaterialsDTOList(new ArrayList<>());
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
     * @return the schoolStagesGuideDTOList
     */
    public List<SchoolStagesGuideDTO> getSchoolStagesGuideDTOList() {
        if (schoolStagesGuideDTOList == null) {

            setSchoolStagesGuideDTOList(schoolStagesGuideService.findschoolStagesGuide(232));
            setSchoolStagesGuideDTOConverter(new SchoolStagesGuideDTOConverter(schoolStagesGuideDTOList));
        }
        return schoolStagesGuideDTOList;
    }

    /**
     * @param schoolStagesGuideDTOList the schoolStagesGuideDTOList to set
     */
    public void setSchoolStagesGuideDTOList(List<SchoolStagesGuideDTO> schoolStagesGuideDTOList) {
        this.schoolStagesGuideDTOList = schoolStagesGuideDTOList;
    }

    /**
     * @return the schoolStagesGuideDTOConverter
     */
    public SchoolStagesGuideDTOConverter getSchoolStagesGuideDTOConverter() {
        return schoolStagesGuideDTOConverter;
    }

    /**
     * @param schoolStagesGuideDTOConverter the schoolStagesGuideDTOConverter to
     * set
     */
    public void setSchoolStagesGuideDTOConverter(SchoolStagesGuideDTOConverter schoolStagesGuideDTOConverter) {
        this.schoolStagesGuideDTOConverter = schoolStagesGuideDTOConverter;
    }

    /**
     * @return the lessonDTO
     */
    public LessonDTO getLessonDTO() {
        if (lessonDTO == null) {
            setLessonDTO(new LessonDTO());
        }
        return lessonDTO;
    }

    /**
     * @param lessonDTO the lessonDTO to set
     */
    public void setLessonDTO(LessonDTO lessonDTO) {
        this.lessonDTO = lessonDTO;
    }

    /**
     * @return the periodsDTOList
     */
    public List<PeriodsDTO> getPeriodsDTOList() {
        if (periodsDTOList == null) {
            setPeriodsDTOList(new ArrayList<>());
        }
        return periodsDTOList;
    }

    /**
     * @param periodsDTOList the periodsDTOList to set
     */
    public void setPeriodsDTOList(List<PeriodsDTO> periodsDTOList) {
        this.periodsDTOList = periodsDTOList;
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
