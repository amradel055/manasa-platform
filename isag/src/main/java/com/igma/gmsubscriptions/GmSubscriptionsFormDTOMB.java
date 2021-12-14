/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import org.apache.commons.codec.binary.Base64;
import com.isag.businessservice.GmGlobalService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.ReportManagementService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GmSubscriptions;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseFormBean;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.uploadfile.FileUploadController;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsFormDTOMB")
@ViewScoped
public class GmSubscriptionsFormDTOMB extends BaseFormBean {

    private GmSubscriptionsDTO gmSubscriptionsDTO;
    private SymbolDTO symbolDTO;

    private InvOrganizationSiteDTO organizationSiteId;

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private List<GmSubscriptionsDTO> list;
    private List<GlBankDTO> glBankList;
    private GlBankDTOConverter glBankConverter;
    IReportService iReportService = new IReportServiceImpl();

    private List<SymbolDTO> symbolDTOList;

    // EJBs
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private GmGlobalService gmGlobalService;
    @EJB
    private ReportManagementService reportManagementService;
    private BigDecimal remain;
    private String filename;
    private String file;
    private String host;
    private FileUploadController fileUploadController;
    private SymbolDTO symbolDTOSelected;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addGmSubscriptions")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editGmSubscriptions")) {

                getGmSubscriptionsDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                fillDefaultList();
                getGmSubscriptionsDTO().setIsEdit(Boolean.FALSE);
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editorganization")) {

                getOrganizationSiteId().setIsEdited(Boolean.TRUE);
                fillClientDTO();
            } else {
                exit("../gmsubscriptions/gmsubscriptionslist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    public void updatedata() {

        gmSubscriptionsDTO.setValue(getSymbolDTOSelected().getCost());
        if(getSymbolDTOSelected().getPeriod() != null ){
        gmSubscriptionsDTO.setDate(new Date());
        gmSubscriptionsDTO.setStartDate(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(gmSubscriptionsDTO.getStartDate());
        c.add(Calendar.DAY_OF_MONTH,getSymbolDTOSelected().getPeriod());
        gmSubscriptionsDTO.setEndDate(c.getTime());
        } 
            gmSubscriptionsDTO.setTaxFlag(Boolean.TRUE);
            gmSubscriptionsDTO.setTaxvalue(gmSubscriptionsDTO.getValue().multiply(new BigDecimal(0.15)).setScale(2, RoundingMode.HALF_UP));
            gmSubscriptionsDTO.setFinalNet(gmSubscriptionsDTO.getValue().add(gmSubscriptionsDTO.getTaxvalue()).setScale(2, RoundingMode.HALF_UP));
         
    }

    public void fillDefaultList() {
        getInvOrganizationSiteDTOList();
        getGlBankList();
    }

    @Override
    public String save() {
        if (getGmSubscriptionsDTO() != null && getGmSubscriptionsDTO().getGmSubscriptionsDetailList() != null && !getGmSubscriptionsDTO().getGmSubscriptionsDetailList().isEmpty()) {
            getGmSubscriptionsDTO().setMsg(null);
            getGmSubscriptionsDTO().setType(0);
            setGmSubscriptionsDTO(gmSubscriptionsService.save(getGmSubscriptionsDTO(), getUserData().getUser()));
            msgSave(getGmSubscriptionsDTO().getMsg());
//            reset();
//            exit("../invsalesinvoice_1/invsalesinvoiceformCeramic.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public void fillInvInvoiceDTO() {
        getGmSubscriptionsDTO().setId((Integer) getContext().getSessionMap().get("gmSubscriptionsIdSelected"));
        setGmSubscriptionsDTO(gmSubscriptionsService.findGmSubscriptionsDTOById(getGmSubscriptionsDTO()));
    }

    public void fillClientDTO() {
        getOrganizationSiteId().setId((Integer) getContext().getSessionMap().get("invOrganizationSiteDTOSelected"));
        setOrganizationSiteId(organizationSiteService.findOrganizationSiteDTOById(getOrganizationSiteId().getId()));
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../gmsubscriptions/gmsubscriptionslist.xhtml");
        return "";
    }

    public List<GlBankDTO> completeGlBank(String query) {
        return AutoComplete.completeGlBank(query, getGlBankList(), getGlBankConverter());
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public void addNewCustomer2() {
        getGmSubscriptionsDTO().setOrganizationSiteId(organizationSiteService.addNewCustomer(getOrganizationSiteId(), getInvOrganizationSiteDTOList(), getUserData().getUser()));
        getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
        saveImage();
        leaveScreen();

    }

    public void leaveScreen() {
        if (getUri().contains("gmregistration")) {
            exit("../gmregistration/gmclientlist.xhtml");

        }
    }

    public void print() {
        getGmSubscriptionsDTO().setReportManagement(reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId()));
        iReportService.printReportGmInvoice(getGmSubscriptionsDTO(), getUserData());
    }

    public void addInvDetail() {
        try {
            getGmSubscriptionsDTO().setMsg(null);
            setGmSubscriptionsDTO(gmGlobalService.addGmSubscriptionsDTODetail(getGmSubscriptionsDTO(), getIndex()));
            errorMsg(getGmSubscriptionsDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public Boolean validateItems(GmSubscriptionsDetailDTO gmSubscriptionsDetailDTO) {
        try {
            gmSubscriptionsDetailDTO.setMsg(null);
            gmSubscriptionsDetailDTO.setCost(gmSubscriptionsDetailDTO.getItemSymbolId().getCost());
            afterValidation(gmSubscriptionsDetailDTO);
            changeFocus("form:invPurchaseInvoiceDetailsTable:" + (getGmSubscriptionsDTO().getGmSubscriptionsDetailList().size() - 1) + ":QuantityT");

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateItems");
            return null;
        }
        return true;
    }

    private void afterValidation(GmSubscriptionsDetailDTO gmSubscriptionsDetailDTO) {
        replacedetail(gmSubscriptionsDetailDTO);
        updateSummition();
        errorMsg(gmSubscriptionsDetailDTO.getMsg());
    }

    public void updateSummition() {
        setGmSubscriptionsDTO(gmGlobalService.updateSummition(getGmSubscriptionsDTO()));
    }

    private void replacedetail(GmSubscriptionsDetailDTO gmSubscriptionsDetailDTO) {
        for (GmSubscriptionsDetailDTO detailDTO : getGmSubscriptionsDTO().getGmSubscriptionsDetailList()) {
            if (detailDTO.getIndex().equals(gmSubscriptionsDetailDTO.getIndex())) {
                int itemIndex = getGmSubscriptionsDTO().getGmSubscriptionsDetailList().indexOf(detailDTO);
                if (itemIndex != -1) {
                    getGmSubscriptionsDTO().getGmSubscriptionsDetailList().set(itemIndex, gmSubscriptionsDetailDTO);
                }
            }
        }
    }

    public void deleteDetail(GmSubscriptionsDetailDTO gmSubscriptionsDetailDTO) {
        try {
            if (gmSubscriptionsDetailDTO.getId() != null) {
                getGmSubscriptionsDTO().getGmSubscriptionsDetailDeletedList().add(gmSubscriptionsDetailDTO);
            }
            for (GmSubscriptionsDetailDTO detailEntity : getGmSubscriptionsDTO().getGmSubscriptionsDetailDeletedList()) {
                if (detailEntity.getItemSymbolId() == null && detailEntity.getItemSymbolIdTrans() != null) {
                    detailEntity.setItemSymbolId(detailEntity.getItemSymbolIdTrans());
                }
            }
            getGmSubscriptionsDTO().getGmSubscriptionsDetailList().remove(gmSubscriptionsDetailDTO);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public void recalcHeadValues() {
        try {
            setGmSubscriptionsDTO(gmGlobalService.recalcHeadValues(getGmSubscriptionsDTO()));
            calculateRemainValue();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "recalcHeadValues");
        }
    }

    public void calculateRemainValue() {
        try {
            getGmSubscriptionsDTO().setRemain(getGmSubscriptionsDTO().getPayed().add(getGmSubscriptionsDTO().getPayNet()).add(getGmSubscriptionsDTO().getDiscount()).subtract(getGmSubscriptionsDTO().getTotalNet()));
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }

    public String updateImage() {

        if (getOrganizationSiteId().getClientImage() != null && !getOrganizationSiteId().getClientImage().isEmpty()) {
            organizationSiteService.updateImage(getGmSubscriptionsDTO().getOrganizationSiteId().getId(), getOrganizationSiteId().getClientImage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم التسجيل بنجاح", "تم التسجيل بنجاح"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", getOrganizationSiteId().getMsg()));
        }
        return "";
    }

    public String saveImage() {
        if (getFile() != null && !getFile().isEmpty()) {
            byte[] byteFile = Base64.decodeBase64(getFile().substring(getFile().indexOf("base64,") + 6));
            String imageName = getFileUploadController().upload(byteFile);
            getOrganizationSiteId().setClientImage(imageName);
            updateImage();
        }
        return "";
    }

    public void reset() {
        try {
            setGmSubscriptionsDTO(null);
            setGmSubscriptionsDTO(null);
            addInvDetail();
            changeFocus("form:invPurchaseInvoiceDetailsTable:0:invItemNameIn");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
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
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null) {
            setInvOrganizationSiteDTOList(organizationSiteService.getorganizationSiteByBranchIdDTO(getUserlogin().getBranchId().getId(), true, 0));
            setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the glBankList
     */
    public List<GlBankDTO> getGlBankList() {
        if (glBankList == null) {
            glBankList = getUserData().getGlBankDTOList(); //  ودا بردوا
            setGlBankConverter(new GlBankDTOConverter(glBankList));
        }
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the glBankConverter
     */
    public GlBankDTOConverter getGlBankConverter() {
        return glBankConverter;
    }

    /**
     * @param glBankConverter the glBankConverter to set
     */
    public void setGlBankConverter(GlBankDTOConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
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
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        if (organizationSiteId == null) {
            setOrganizationSiteId(new InvOrganizationSiteDTO());
        }
        return organizationSiteId;
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
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the symbolDTO
     */
    public SymbolDTO getSymbolDTO() {
        return symbolDTO;
    }

    /**
     * @param symbolDTO the symbolDTO to set
     */
    public void setSymbolDTO(SymbolDTO symbolDTO) {
        this.symbolDTO = symbolDTO;
    }

    /**
     * @return the symbolDTOList
     */
    public List<SymbolDTO> getSymbolDTOList() {
        if (symbolDTOList == null) {
            setSymbolDTOList(autoCompleteService.autoCompleteSymbolByCompanyId(getUserData().getCompany().getId(), 0, null));
        }
        return symbolDTOList;
    }

    /**
     * @param symbolDTOList the symbolDTOList to set
     */
    public void setSymbolDTOList(List<SymbolDTO> symbolDTOList) {
        this.symbolDTOList = symbolDTOList;
    }

    /**
     * @return the symbolDTOSelected
     */
    public SymbolDTO getSymbolDTOSelected() {
        return symbolDTOSelected;
    }

    /**
     * @param symbolDTOSelected the symbolDTOSelected to set
     */
    public void setSymbolDTOSelected(SymbolDTO symbolDTOSelected) {
        this.symbolDTOSelected = symbolDTOSelected;
    }

}
