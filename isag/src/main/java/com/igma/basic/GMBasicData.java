/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.basic;

import com.isag.businessservice.GmFreezeService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.bussinessservice.global.Utility;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmMovementDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.BaseFormBean;
import com.isag.uploadfile.FileUploadController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Igma
 */
public class GMBasicData extends BaseFormBean {

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private InvOrganizationSiteDTO invOrganizationSiteDTOSelected;
    private GmSubscriptionsDTO gmSubscriptionsDTO;
    private BigDecimal remain;
    private String filename;
    private String file;
    private String host;
    private FileUploadController fileUploadController;
    private GmMovementDTO gmMovement;
    private GmSubscriptionsDTO gmSubscriptionsDTOSelected;
    private GmFreezeDto gmFreezeDto;

    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;
    @EJB
    private GmFreezeService gmFreezeService;

    public void tagmeed() {
        if (getGmSubscriptionsDTOSelected() == null && getInvOrganizationSiteDTOSelected() != null) {
            setGmSubscriptionsDTOSelected(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getInvOrganizationSiteDTOSelected().getId()));
        }
        setInvOrganizationSiteDTOSelected(organizationSiteService.findOrganizationSiteDTOByIdSmall(getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId()));
        getGmFreezeDto().setCustomerId(getInvOrganizationSiteDTOSelected());
        getGmFreezeDto().setSubscriptionId(getGmSubscriptionsDTOSelected());
        getGmFreezeDto().setDatefrom(new Date());

        calcDays();
        OpenDlg("dlg3");
    }

    public String saveFreeze() {
        if (gmFreezeDto.getNumber().compareTo(gmFreezeDto.getRemainDays()) != 1) {
            gmFreezeService.save(getGmFreezeDto(), getUserData().getUser());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "يجب ان يكون عدد ايام التجميد اقل من او يساوي عدد الايام المتاحه"));
        }
        gmFreezeDto.setNumber(null);

        return "";
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public String updateImage() {

        if (getInvOrganizationSiteDTOSelected().getClientImage() != null && !getInvOrganizationSiteDTOSelected().getClientImage().isEmpty()) {
            organizationSiteService.updateImage(getGmSubscriptionsDTO().getOrganizationSiteId().getId(), getInvOrganizationSiteDTOSelected().getClientImage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم التسجيل بنجاح", "تم التسجيل بنجاح"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", getInvOrganizationSiteDTOSelected().getMsg()));
        }
        return "";
    }

    public void fillClientDTO() {
        getInvOrganizationSiteDTOSelected().setId((Integer) getContext().getSessionMap().get("invOrganizationSiteDTOSelected"));
        setInvOrganizationSiteDTOSelected(organizationSiteService.findOrganizationSiteDTOById(getInvOrganizationSiteDTOSelected().getId()));
    }

    public void fillInvOrgan() {
        getInvOrganizationSiteDTOSelected().setId((Integer) getContext().getSessionMap().get("invOrganizationSiteDTO"));
        setInvOrganizationSiteDTOSelected(organizationSiteService.findOrganizationSiteDTOById(getInvOrganizationSiteDTOSelected().getId()));
    }

    public void addNewCustomer2() {
        getGmSubscriptionsDTO().setOrganizationSiteId(organizationSiteService.addNewCustomer(getInvOrganizationSiteDTOSelected(), getInvOrganizationSiteDTOList(), getUserData().getUser()));
        getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
        saveImage();
        CloseDlg("dlg3");
//        exit("../gmsubscriptions/gmsubscriptionsform.xhtml");

    }

    public void updateCustomer() {
        getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
        organizationSiteService.updateGMCustomer(getInvOrganizationSiteDTOSelected());
        saveImage();
        CloseDlg("dlg3");
    }

    public String saveImage() {
        if (getFile() != null && !getFile().isEmpty()) {
            byte[] byteFile = Base64.decodeBase64(getFile().substring(getFile().indexOf("base64,") + 6));
            String imageName = getFileUploadController().upload(byteFile);
            getInvOrganizationSiteDTOSelected().setClientImage(imageName);
            updateImage();
        }
        return "";
    }

    public void calcDays() {

        getGmFreezeDto().setMustDays(Utility.calcDays(getGmSubscriptionsDTOSelected().getStartDate(), getGmSubscriptionsDTOSelected().getEndDate(), 5));
        getGmFreezeDto().setTakeDays(gmFreezeService.calcTakenDaysByCustomerIdAndSubscribtionId(getGmSubscriptionsDTOSelected().getId()));
        getGmFreezeDto().setRemainDays(getGmFreezeDto().getMustDays() - getGmFreezeDto().getTakeDays());
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null) {
            setInvOrganizationSiteDTOList(organizationSiteService.getorganizationSiteByBranchIdDTO(getUserlogin().getBranchId().getId(), true, 0));
            setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        }
        return invOrganizationSiteDTOList;
    }

    public void updateMovementdata() {
        getGmMovement().getCustomerIdFrom().setId((Integer) getContext().getSessionMap().get("gmCustomerIdSelected"));
        getGmMovement().setSubscriptionIdFrom(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getGmMovement().getCustomerIdFrom().getId()));
        getGmMovement().setCustomerIdFrom(organizationSiteService.findOrganizationSiteDTOById(getGmMovement().getCustomerIdFrom().getId()));
        getGmMovement().setDays(Utility.calcDaysBetween(getGmMovement().getSubscriptionIdFrom().getEndDate(), getGmMovement().getDate()));
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the organizationSiteDTOConvertor
     */
    public InvOrganizationSiteDTOConverter getOrganizationSiteDTOConvertor() {
        return organizationSiteDTOConvertor;
    }

    /**
     * @param organizationSiteDTOConvertor the organizationSiteDTOConvertor to
     * set
     */
    public void setOrganizationSiteDTOConvertor(InvOrganizationSiteDTOConverter organizationSiteDTOConvertor) {
        this.organizationSiteDTOConvertor = organizationSiteDTOConvertor;
    }

    /**
     * @return the gmSubscriptionsDTO
     */
    public GmSubscriptionsDTO getGmSubscriptionsDTO() {
        if (gmSubscriptionsDTO == null) {
            gmSubscriptionsDTO = new GmSubscriptionsDTO();
        }
        return gmSubscriptionsDTO;
    }

    /**
     * @param gmSubscriptionsDTO the gmSubscriptionsDTO to set
     */
    public void setGmSubscriptionsDTO(GmSubscriptionsDTO gmSubscriptionsDTO) {
        this.gmSubscriptionsDTO = gmSubscriptionsDTO;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
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
     * @return the remain
     */
    public BigDecimal getRemain() {
        if (remain == null) {
            remain = BigDecimal.ZERO;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the gmMovement
     */
    public GmMovementDTO getGmMovement() {
        if (gmMovement == null) {
            gmMovement = new GmMovementDTO();
        }
        return gmMovement;
    }

    /**
     * @param gmMovement the gmMovement to set
     */
    public void setGmMovement(GmMovementDTO gmMovement) {
        this.gmMovement = gmMovement;
    }

    /**
     * @return the invOrganizationSiteDTOSelected
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOSelected() {
        if (invOrganizationSiteDTOSelected == null) {
            invOrganizationSiteDTOSelected = new InvOrganizationSiteDTO();
        }
        return invOrganizationSiteDTOSelected;
    }

    /**
     * @param invOrganizationSiteDTOSelected the invOrganizationSiteDTOSelected
     * to set
     */
    public void setInvOrganizationSiteDTOSelected(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        this.invOrganizationSiteDTOSelected = invOrganizationSiteDTOSelected;
    }

    /**
     * @return the gmSubscriptionsDTOSelected
     */
    public GmSubscriptionsDTO getGmSubscriptionsDTOSelected() {
        return gmSubscriptionsDTOSelected;
    }

    /**
     * @param gmSubscriptionsDTOSelected the gmSubscriptionsDTOSelected to set
     */
    public void setGmSubscriptionsDTOSelected(GmSubscriptionsDTO gmSubscriptionsDTOSelected) {
        this.gmSubscriptionsDTOSelected = gmSubscriptionsDTOSelected;
    }

    /**
     * @return the gmFreezeDto
     */
    public GmFreezeDto getGmFreezeDto() {
        if (gmFreezeDto == null) {
            gmFreezeDto = new GmFreezeDto();
        }
        return gmFreezeDto;
    }

    /**
     * @param gmFreezeDto the gmFreezeDto to set
     */
    public void setGmFreezeDto(GmFreezeDto gmFreezeDto) {
        this.gmFreezeDto = gmFreezeDto;
    }
}
