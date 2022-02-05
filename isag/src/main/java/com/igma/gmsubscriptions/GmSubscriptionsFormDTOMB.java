/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.igma.basic.GMBasicData;
import com.isag.businessservice.CreateActionService;
import com.isag.businessservice.GmGlobalService;
import com.isag.businessservice.GmItemService;
import com.isag.businessservice.GmMovementService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.ReportManagementService;
import com.isag.converter.GlBankDTOConverter;
import com.isag.dto.ActionsDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GmItemDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.entity.IsagUser;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsFormDTOMB")
@ViewScoped
public class GmSubscriptionsFormDTOMB extends GMBasicData {

    private GmSubscriptionsDTO gmSubscriptionsDTOold;

    private GmItemDTO gmItemDTOSelected;

    private List<GmSubscriptionsDTO> list;
    private List<GlBankDTO> glBankList;
    private GlBankDTOConverter glBankConverter;
    IReportService iReportService = new IReportServiceImpl();

    private List<GmItemDTO> gmItemDTOList;

    // EJBs
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;

    @EJB
    private GmItemService gmItemService;
    @EJB
    private GmGlobalService gmGlobalService;
    @EJB
    private ReportManagementService reportManagementService;

    @EJB
    private GmMovementService gmMovementService;
    @EJB
    CreateActionService createActionService;

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
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editCustomerData")) {
                fillInvOrgan();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editorganization")) {
                getInvOrganizationSiteDTOSelected().setIsEdited(Boolean.TRUE);

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
        getGmSubscriptionsDTO().setValue(getGmItemDTOSelected().getCost());
        if (getGmItemDTOSelected().getPeriod() != null) {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Tamded")) {
                if (getGmSubscriptionsDTOold() != null && new Date().compareTo(getGmSubscriptionsDTOold().getDateAfterTagmeed()) != 1) {
                    c.setTime(getGmSubscriptionsDTOold().getDateAfterTagmeed());
                    c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                    getGmSubscriptionsDTO().setEndDate(c.getTime());
                    c.setTime(getGmSubscriptionsDTOold().getDateAfterTagmeed());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    getGmSubscriptionsDTO().setStartDate(c.getTime());
                } else {
                    getGmSubscriptionsDTO().setDate(new Date());
                    getGmSubscriptionsDTO().setStartDate(new Date());
                    c.setTime(getGmSubscriptionsDTO().getStartDate());
                    c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                    getGmSubscriptionsDTO().setEndDate(c.getTime());
                }
            } else {
                getGmSubscriptionsDTO().setDate(new Date());
                getGmSubscriptionsDTO().setStartDate(new Date());
                c.setTime(getGmSubscriptionsDTO().getStartDate());
                c.add(Calendar.DAY_OF_MONTH, getGmItemDTOSelected().getPeriod());
                getGmSubscriptionsDTO().setEndDate(c.getTime());
            }

        }
        getGmSubscriptionsDTO().setGmItemDTO(getGmItemDTOSelected());
        getGmSubscriptionsDTO().setTaxFlag(Boolean.TRUE);
        getGmSubscriptionsDTO().setTaxvalue(getGmSubscriptionsDTO().getValue().multiply(new BigDecimal(0.15)).setScale(2, RoundingMode.HALF_UP));
        getGmSubscriptionsDTO().setFinalNet(getGmSubscriptionsDTO().getValue().add(getGmSubscriptionsDTO().getTaxvalue()).setScale(2, RoundingMode.HALF_UP));

    }

    public void fillDefaultList() {
        getInvOrganizationSiteDTOList();
        getGlBankList();
    }

    public void saveAndPrint() {
        save();
//        print();
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
        getGmSubscriptionsDTO().setId((Integer) getContext().getSessionMap().get("gmSubscriptionsIdSelected"));
        setGmSubscriptionsDTO(gmSubscriptionsService.findGmSubscriptionsDTOById(getGmSubscriptionsDTO()));
        setGmItemDTOSelected(getGmSubscriptionsDTO().getGmItemDTO());
    }

    public void fillGmSubscribtionOld() {
        setGmSubscriptionsDTOold(new GmSubscriptionsDTO());
        getGmSubscriptionsDTOold().setId((Integer) getContext().getSessionMap().get("TamdedId"));
        setGmSubscriptionsDTOold(gmSubscriptionsService.findGmSubscriptionsDTOById(getGmSubscriptionsDTOold()));
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

    public String exitToStatue() {
        exit("../gmsubscriptionstatue/gmsubscriptionstatue.xhtml");
        return "";
    }

    public void leaveScreen() {
        if (getUri().contains("gmregistration")) {
            exit("../gmregistration/gmclientlist.xhtml");

        }
    }

    public void print() {
        getGmSubscriptionsDTO().setReportManagement(reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId()));
        iReportService.printReportGmInvoice(getGmSubscriptionsDTO(), getUserData());
        createActionService.SaveAction(SetActionDto(getUserData().getUser(), "gmsubscriptions", "print"), getUserData().getUser());

    }

    public ActionsDTO SetActionDto(IsagUser isagUser, String screenname, String type) {
        ActionsDTO actionsDTO = new ActionsDTO();
        actionsDTO.setDate(new Date());
        actionsDTO.setUserId(isagUser.getName());
        actionsDTO.setScreenName(screenname);
        actionsDTO.setType(type);
        return actionsDTO;
    }

    public void exportPDF() {
        try {
            String reportName = "notesReceivablesDetialCopyGM.jasper";
            fillReport(prepareReport(getGmSubscriptionsDTO(), getUserData()), getUserData().getReportPath() + reportName, getGmSubscriptionsDTO().getGmSubscriptionsDetailList(), "pdf");

        } catch (JRException | IOException e) {
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
            if (null != exportType) {
                switch (exportType) {
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

    public void reset() {
        try {
            setGmSubscriptionsDTO(null);
            setGmItemDTOSelected(null);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
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
