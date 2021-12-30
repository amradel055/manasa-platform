/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import org.apache.commons.codec.binary.Base64;
import com.isag.businessservice.GmGlobalService;
import com.isag.businessservice.GmItemService;
import com.isag.businessservice.GmMovementService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.ReportManagementService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.bussinessservice.global.Utility;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GmItemDTO;
import com.isag.dto.GmMovementDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.ReportManagement;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
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
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsFormDTOMB")
@ViewScoped
public class GmSubscriptionsFormDTOMB extends BaseFormBean {

    private GmSubscriptionsDTO gmSubscriptionsDTO;
    private GmSubscriptionsDTO gmSubscriptionsDTOold;

    private GmItemDTO gmItemDTOSelected;

    private InvOrganizationSiteDTO organizationSiteId;

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private List<GmSubscriptionsDTO> list;
    private List<GlBankDTO> glBankList;
    private GlBankDTOConverter glBankConverter;
    IReportService iReportService = new IReportServiceImpl();

    private List<GmItemDTO> gmItemDTOList;

    // EJBs
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GmItemService gmItemService;
    @EJB
    private GmGlobalService gmGlobalService;
    @EJB
    private ReportManagementService reportManagementService;
    @EJB
    private AutoCompleteService autoCompleteService;

    @EJB
    private GmMovementService gmMovementService;

    private BigDecimal remain;
    private String filename;
    private String file;
    private String host;
    private FileUploadController fileUploadController;
    private GmMovementDTO gmMovement;

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
                getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addGmMovement")) {
                getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
                reset();
                fillDefaultList();
                updateMovementdata();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editGmSubscriptions")) {
                getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);

                getGmSubscriptionsDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editorganization")) {
                getOrganizationSiteId().setIsEdited(Boolean.TRUE);

                fillClientDTO();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Tamded")) {
                getGmSubscriptionsDTO().setShowCustomerDate(Boolean.FALSE);
                getGmSubscriptionsDTO().setIsEdit(Boolean.FALSE);
                fillGmSubscribtionOld();
                fillResumeClientDTO();

            } else {
                exit("../gmsubscriptions/gmsubscriptionslist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    public void updatedata() {
        Calendar c = Calendar.getInstance();
        gmSubscriptionsDTO.setValue(getGmItemDTOSelected().getCost());
        if (getGmItemDTOSelected().getPeriod() != null) {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Tamded")) {
                if (getGmSubscriptionsDTOold() != null && new Date().compareTo(getGmSubscriptionsDTOold().getEndDate()) != 1) {
                    c.setTime(getGmSubscriptionsDTOold().getEndDate());
                    c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                    gmSubscriptionsDTO.setEndDate(c.getTime());
                    c.setTime(getGmSubscriptionsDTOold().getEndDate());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    gmSubscriptionsDTO.setStartDate(c.getTime());
                } else {
                    gmSubscriptionsDTO.setDate(new Date());
                    gmSubscriptionsDTO.setStartDate(new Date());
                    c.setTime(gmSubscriptionsDTO.getStartDate());
                    c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                    gmSubscriptionsDTO.setEndDate(c.getTime());
                }
            } else {
                gmSubscriptionsDTO.setDate(new Date());
                gmSubscriptionsDTO.setStartDate(new Date());
                c.setTime(gmSubscriptionsDTO.getStartDate());
                c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                gmSubscriptionsDTO.setEndDate(c.getTime());
            }

        }
        gmSubscriptionsDTO.setGmItemDTO(getGmItemDTOSelected());
        gmSubscriptionsDTO.setTaxFlag(Boolean.TRUE);
        gmSubscriptionsDTO.setTaxvalue(gmSubscriptionsDTO.getValue().multiply(new BigDecimal(0.15)).setScale(2, RoundingMode.HALF_UP));
        gmSubscriptionsDTO.setFinalNet(gmSubscriptionsDTO.getValue().add(gmSubscriptionsDTO.getTaxvalue()).setScale(2, RoundingMode.HALF_UP));

    }

    public void updateMovementdata() {
        getGmMovement().getCustomerIdFrom().setId((Integer) getContext().getSessionMap().get("gmCustomerIdSelected"));
        getGmMovement().setSubscriptionIdFrom(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getGmMovement().getCustomerIdFrom().getId()));
        getGmMovement().setCustomerIdFrom(organizationSiteService.findOrganizationSiteDTOById(getGmMovement().getCustomerIdFrom().getId()));
        getGmMovement().setDays(Utility.calcDaysBetween(getGmMovement().getSubscriptionIdFrom().getEndDate(), getGmMovement().getDate()));
    }

    public void fillDefaultList() {
        getInvOrganizationSiteDTOList();
        getGlBankList();
    }

    public void saveAndPrint() {
        save();
     
    }

    @Override
    public String save() {
        getGmSubscriptionsDTO().setMsg(null);
        getGmSubscriptionsDTO().setType(0);
      
        setGmSubscriptionsDTO(gmSubscriptionsService.save(getGmSubscriptionsDTO(), getUserData().getUser()));
        msgSave(getGmSubscriptionsDTO().getMsg());

        return "";
    }

    public String saveMovment() {
//       getGmMovement().setCustomerIdTo(organizationSiteService.findOrganizationSiteDTOById(getGmMovement().getCustomerIdTo().getId()));
        gmMovementService.save(getGmMovement(), getGmSubscriptionsDTO(), getUserData().getUser());

        return "";
    }

    public void fillInvInvoiceDTO() {
        getGmSubscriptionsDTO().setId((Integer) getContext().getSessionMap().get("TamdedId"));
        setGmSubscriptionsDTO(gmSubscriptionsService.findGmSubscriptionsDTOById(getGmSubscriptionsDTO()));
    }

    public void fillGmSubscribtionOld() {
        setGmSubscriptionsDTOold(new GmSubscriptionsDTO());
        getGmSubscriptionsDTOold().setId((Integer) getContext().getSessionMap().get("TamdedId"));
        setGmSubscriptionsDTOold(gmSubscriptionsService.findGmSubscriptionsDTOById(getGmSubscriptionsDTOold()));
    }

    public void fillClientDTO() {
        getOrganizationSiteId().setId((Integer) getContext().getSessionMap().get("invOrganizationSiteDTOSelected"));
        setOrganizationSiteId(organizationSiteService.findOrganizationSiteDTOById(getOrganizationSiteId().getId()));
    }

    public void fillResumeClientDTO() {

        getGmSubscriptionsDTO().setOrganizationSiteId(getGmSubscriptionsDTOold().getOrganizationSiteId());
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../gmsubscriptions/gmsubscriptionslist.xhtml");
        return "";
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
   public void exportPDF(){
              try {
      String reportName = "notesReceivablesDetialCopyGM.jasper";
      fillReport(prepareReport(getGmSubscriptionsDTO(), getUserData()), getUserData().getReportPath() + reportName, getGmSubscriptionsDTO().getGmSubscriptionsDetailList(),"pdf");

        } 
        catch (JRException | IOException e) {
            System.out.println(e);
        }
    }
          @Override
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if (null != exportType) switch (exportType) {
                case "pdf":
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                    httpServletResponse.flushBuffer();
                    servletOutputStream.flush();
                    servletOutputStream.close();
                    context.renderResponse();
                    context.responseComplete();
                    break;
                case "excel":
                    break;
                case "html":
                    break;
            }
        } catch (JRException | IOException ex) {
            System.out.println(ex + "exception ++++++++++++++++++");
        }

    }

    public HashMap prepareReport(GmSubscriptionsDTO invPurchasegmSubscriptionsDTO, UserData userData) {
         try {  
            HashMap hashMap = new HashMap();
//            ReportManagement reportManagement = invPurchasegmSubscriptionsDTO.getReportManagement();
//            if (reportManagement != null) {
//                hashMap.put("LeftUp1", reportManagement.getLeftUp1());
//                hashMap.put("LeftUp2", reportManagement.getLeftUp2());
//                hashMap.put("LeftUp3", reportManagement.getLeftUp3());
//                hashMap.put("LeftUp4", reportManagement.getLeftUp4());
//                hashMap.put("RightUp1", reportManagement.getRightUp1());
//                hashMap.put("RightUp2", reportManagement.getRightUp2());
//                hashMap.put("RightUp3", reportManagement.getRightUp3());
//                hashMap.put("RightUp4", reportManagement.getRightUp4());
//
//            }
            
//            hashMap.put("PrintedBy", userData.getUser().getName());
//            hashMap.put("address", userData.getUserBranch().getAddress1());
//            hashMap.put("taxValue", invPurchasegmSubscriptionsDTO.getTaxvalue());
//            hashMap.put("receiptNumberValue", invPurchasegmSubscriptionsDTO.getSerial());
//
//            if (invPurchasegmSubscriptionsDTO.getOrganizationSiteId() != null) {
//                hashMap.put("customerName", invPurchasegmSubscriptionsDTO.getOrganizationSiteId().getName());
//                hashMap.put("customerPhone", invPurchasegmSubscriptionsDTO.getOrganizationSiteId().getMobile());
//            }
////            hashMap.put("dateValue", invPurchasegmSubscriptionsDTO.getDate());
//            hashMap.put("netValue", invPurchasegmSubscriptionsDTO.getFinalNet());
//
//            hashMap.put("subscribtionName", invPurchasegmSubscriptionsDTO.getGmItemDTO().getName());
//            hashMap.put("endDate", invPurchasegmSubscriptionsDTO.getEndDate());
//            hashMap.put("nationId", invPurchasegmSubscriptionsDTO.getOrganizationSiteId().getNationId());
//            hashMap.put("email", invPurchasegmSubscriptionsDTO.getOrganizationSiteId().getEmail());
            return hashMap;
        } catch (Exception e) {
//            saveError(e, "SalesInvoiceFormMB", "prepareReport");
            return null;
        }
 
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
            setGmItemDTOSelected(null);
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
     * @return the gmItemDTOList
     */
    public List<GmItemDTO> getGmItemDTOList() {
        if (gmItemDTOList == null) {
            gmItemDTOList = gmItemService.autoCompleteGmItemDTO(getUserData().getUserBranch().getId());
        }
        return gmItemDTOList;
    }

    /**
     * @param gmItemDTOList the gmItemDTOList to set
     */
    public void setGmItemDTOList(List<GmItemDTO> gmItemDTOList) {
        this.gmItemDTOList = gmItemDTOList;
    }

    /**
     * @return the gmItemDTOSelected
     */
    public GmItemDTO getGmItemDTOSelected() {
        return gmItemDTOSelected;
    }

    /**
     * @param gmItemDTOSelected the gmItemDTOSelected to set
     */
    public void setGmItemDTOSelected(GmItemDTO gmItemDTOSelected) {
        this.gmItemDTOSelected = gmItemDTOSelected;
    }

    /**
     * @return the gmMovementService
     */
    public GmMovementService getGmMovementService() {
        return gmMovementService;
    }

    /**
     * @param gmMovementService the gmMovementService to set
     */
    public void setGmMovementService(GmMovementService gmMovementService) {
        this.gmMovementService = gmMovementService;
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
     * @return the gmSubscriptionsDTOold
     */
    public GmSubscriptionsDTO getGmSubscriptionsDTOold() {
        return gmSubscriptionsDTOold;
    }

    /**
     * @param gmSubscriptionsDTOold the gmSubscriptionsDTOold to set
     */
    public void setGmSubscriptionsDTOold(GmSubscriptionsDTO gmSubscriptionsDTOold) {
        this.gmSubscriptionsDTOold = gmSubscriptionsDTOold;
    }

}
