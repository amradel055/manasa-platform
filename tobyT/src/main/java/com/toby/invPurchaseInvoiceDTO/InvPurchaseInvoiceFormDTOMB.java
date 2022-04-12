/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseInvoiceDTO;

import com.toby.businessservice.InvGlobalService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.ProFactoryDeliveryDetailService;
import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.InventoryBasicDataForm;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "invPurchaseInvoiceFormDTOMB")
@ViewScoped
public class InvPurchaseInvoiceFormDTOMB extends InventoryBasicDataForm {

    private Boolean checkFactory;

    private Boolean checkDiscountValue = false;
    private Boolean checkDiscountRate = false;

    private InvInvoiceDTO invInvoiceDTOSelected;

// EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    ProFactoryDeliveryService proFactoryDeliveryService;
    @EJB
    ProFactoryDeliveryDetailService proFactoryDeliveryDetailService;

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
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addInvoice")) {
                String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId());
                if (error.isEmpty()) {
                    reset();
                    fillDefaultList();
                    putCurrency();
                    getInvInvoiceDTO().setOrganizationSiteId(null);
                    if (getInvInvoiceDTO().getInventorySetupDTO() != null) {
                        getInvInvoiceDTO().setDueperiod(invGlobalService.getDeliveryDaysByGalleryId((getInvInvoiceDTO() == null || getInvInvoiceDTO().getGallaryId() == null) ? null : getInvInvoiceDTO().getGallaryId().getId()));
                        sumDateToDueDate();
                    }
                } else {
                    setErrorGlobal(error);
                    exit("../base/error.xhtml");
                }
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvoice")) {
                getInvInvoiceDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();// here more than 20 second
                getInvInvoiceDTO().setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(getInvInvoiceDTO().getId()));
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
                getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
                findAccountStatement();
                getInvOrganizationSiteDTOList(Boolean.TRUE, 0).add(0, getInvInvoiceDTO().getOrganizationSiteId());
                setOrganizationSiteId(getInvInvoiceDTO().getOrganizationSiteId());
                checkFactoryId();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvoiceReturn")) {
                getInvInvoiceDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                getInvInvoiceDTO().setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(getInvInvoiceDTO().getId()));
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
                getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
                findAccountStatement();
                getInvOrganizationSiteDTOList(Boolean.TRUE, 0).add(0, getInvInvoiceDTO().getOrganizationSiteId());
                setOrganizationSiteId(getInvInvoiceDTO().getOrganizationSiteId());
                checkFactoryId();
                getInvInvoiceDTO().setInvPurchaseInvoiceId(getInvInvoiceDTO().getId());
                getInvInvoiceDTO().setId(null);
                getInvInvoiceDTO().setLoadedSerial(getInvInvoiceDTO().getSerial());
                getInvInvoiceDTO().setSerial(null);
                getInvInvoiceDTO().setSerialTax(null);
                for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    detailDTO.setId(null);
                    detailDTO.setSerial(null);
                }

            } else {
                exit("../base/error.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            Integer id = getInvInvoiceDTO().getId();
            getInvInvoiceDTO().setMsg(null);
            checkDetailData();
            if (validateInvoiceType()) {
                try {
                    setInvInvoiceDTO(invPurchaseInvoiceService.save(getInvInvoiceDTO(), getUserData().getUser()));
                    setInvInvoiceDTO(invPurchaseInvoiceService.completeTarhilInvoiceAfterSave(getInvInvoiceDTO(), getUserData().getUser()));
                    invPurchaseInvoiceService.updateDetailInvoiceValues(getInvInvoiceDTO().getId());
                    checkDetailData();
                    setOrganizationSiteId(getInvInvoiceDTO().getOrganizationSiteId());
                    msgSave(getInvInvoiceDTO().getMsg());
                    findAccountStatement();
                    if (id == null && getUri().contains("proworkorderform") && getInvInvoiceDTO().getInvoiceType() == null) {
                        StringBuilder message = new StringBuilder();
                        message.append("عميلنا العزيز ، شكراً لتسوقك تمت عملية شراء رقم ");
                        message.append(getInvInvoiceDTO().getSerial());
                        message.append(" المدفوع ");
                        message.append(getInvInvoiceDTO().getPayed());
                        if (getInvInvoiceDTO().getGallaryId().getInvName() != null && getInvInvoiceDTO().getGallaryId().getInvName() == 0) {
                            message.append("\n");
                            message.append("نسعد بتلقي تقييمكم عن الخدمة المقدمة لكم من برنس  \n");
                            message.append(" https://forms.gle/jARVyTyhGMT9MQRi7 ");
                            sendSms(getInvInvoiceDTO().getOrganizationSiteId().getMobile(), "التوبى العالمى", message.toString(), "20099319", "0564933202");
                        } else if (getInvInvoiceDTO().getGallaryId().getInvName() != null && getInvInvoiceDTO().getGallaryId().getInvName() == 1) {
                            message.append("\n");
                            message.append("نسعد بتلقي تقييمكم عن الخدمة المقدمة لكم من لي رويال  \n");
                            message.append(" https://forms.gle/Jcti3uYixoUX8P9D6 ");
                            sendSms(getInvInvoiceDTO().getOrganizationSiteId().getMobile(), "التوبى العالمى", message.toString(), "20099320", "Royal@e12z");
                        }
                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormDTOMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }

        return "";
    }

    private Boolean validateInvoiceType() {

        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                if (detailDTO.getNumber().compareTo(detailDTO.getAvailableQuantityRow()) == 1) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "العدد أكبر من المسموح"));
                    return false;
                }
            }
        }

        if (ScreenNameClassEnum.PROWORKORDER.equals(getInvInvoiceDTO().getScreenName())) {
            if (getInvInvoiceDTO().getInvoiceType() != null && getInvInvoiceDTO().getInvoiceType() == 5) {
                for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    if (detailDTO.getItemsBarcodesDetail() == null || detailDTO.getItemsBarcodesDetail().getBarcode() == null
                            || !detailDTO.getItemsBarcodesDetail().getBarcode().equals("000012")) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يوجد بنود بالفاتورة غير التعديل برجاء حذفها"));
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVPURCHASEADDING.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invpurchaseadding/invpurchaseaddinglist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnpurchase_1/invreturnpurchaselist.xhtml");
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invsalesinvoice_1/invsalesinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnsales_1/invreturnsaleslist.xhtml");
        } else if (ScreenNameClassEnum.PROWORKORDER.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../proworkorder/proworkorderlist.xhtml");
        }
        return "";
    }

    public String goToInvNotice() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getOrganizationSiteId().getId() > 0) {
                getContext().getSessionMap().put("customerFromInvNoticeEntitySelected", getInvInvoiceDTO().getOrganizationSiteId().getId());
                getContext().getSessionMap().put("ScreenMode", "addClientFromInvoice");
                exit("../customeraddnotice/customernoticeform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public String goTClientacoount() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getOrganizationSiteId().getId() > 0) {
                return goTOrganizationSiteacoount(getInvInvoiceDTO().getOrganizationSiteId().getId());
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public String goTNotesreceivables() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invInvoiceDTOSelected", getInvInvoiceDTO());
            context.getSessionMap().put("ScreenMode", "AddFromInvInvoice");
            exit("../notesreceivables/notesreceivablesForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public String goTInvoiceMovementReport() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invInvoiceDTOSelected", getInvInvoiceDTO());
                context.getSessionMap().put("ScreenMode", "AddFromInvInvoice");
                exit("../proproductionreport/InvoiceMovementReport.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public String goTProProductionMovement() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proSalesFormSelected", getInvInvoiceDTO().getSerial());
                context.getSessionMap().put("ScreenMode", "proSalesFormAdd");
                exit("../proSales/proSalesForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public String goToInvoiceProductionStageReport() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proSalesFormSelected", getInvInvoiceDTO());
                context.getSessionMap().put("ScreenMode", "proproductionreportFormAdd");
                exit("../proproductionreport/invoiceProductionStageReport.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public void searchInvoiceBySerial() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTO().getSearchBySerial() == null) {
                getInvInvoiceDTO().setSearchBySerial(getInvInvoiceDTOSelected().getSerial());
            }
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getSearchBySerial() > 0) {
                Integer invoiceId = invPurchaseInvoiceService_1.getInvoiceBySerial(getInvInvoiceDTO().getSearchBySerial());
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", invoiceId);
                context.getSessionMap().put("ScreenMode", "editInvoice");
                init();

            }
        } catch (Exception e) {
            saveError(e, "invPurchaseInvoiceFormDTOMB", "searchInvoiceBySerial");
        }
    }

    public void searchReturnInvoiceBySerial() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getSearchBySerial() != null && getInvInvoiceDTO().getSearchBySerial() > 0) {
                Integer invoiceId = invPurchaseInvoiceService_1.getInvoiceBySerial(getInvInvoiceDTO().getSearchBySerial());
                if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)) {
                    findinvoice(invoiceId);
                } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                    findinvoice(invoiceId);
                    for (InvDetailDTO invDetailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                        invDetailDTO.setNumber(proFactoryDeliveryDetailService.findquantityAllow(invoiceId, invDetailDTO.getItemsBarcodesDetailTrans().getBarcode()));
                        invDetailDTO.setAvailableQuantityRow(invDetailDTO.getNumber());
                        afterValidation(invDetailDTO, true);
                    }
                }

//                else if (!proFactoryDeliveryService.chechIfSalesInFactory(invoiceId)) {
//                    findinvoice(invoiceId);
//                } 
//                else {
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "تم استلامها بالمشغل"));
//                }
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseInvoiceFormDTOMB", "searchInvoiceBySerial");
        }
    }

    private void findinvoice(Integer invoiceId) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("invPurchaseInvoiceIdSeclected", invoiceId);
        context.getSessionMap().put("ScreenMode", "editInvoiceReturn");
        init();
    }

    public void checkFactoryId() {
        checkFactory = proFactoryDeliveryService.checkProFactoryDelivery(getInvInvoiceDTO().getId());
    }

    /**
     * @return the checkFactory
     */
    public Boolean isCheckFactory() {
        if (checkFactory == null) {
            checkFactory = Boolean.TRUE;
        }
        return checkFactory;
    }

    /**
     * @param checkFactory the checkFactory to set
     */
    public void setCheckFactory(Boolean checkFactory) {
        this.checkFactory = checkFactory;
    }

    /**
     * @return the checkDiscountValue
     */
    public Boolean getCheckDiscountValue() {
        return checkDiscountValue;
    }

    /**
     * @param checkDiscountValue the checkDiscountValue to set
     */
    public void setCheckDiscountValue(Boolean checkDiscountValue) {
        this.checkDiscountValue = checkDiscountValue;
    }

    /**
     * @return the checkDiscountRate
     */
    public Boolean getCheckDiscountRate() {
        return checkDiscountRate;
    }

    /**
     * @param checkDiscountRate the checkDiscountRate to set
     */
    public void setCheckDiscountRate(Boolean checkDiscountRate) {
        this.checkDiscountRate = checkDiscountRate;
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        return invInvoiceDTOSelected;
    }

    /**
     * @param invInvoiceDTOSelected the invInvoiceDTOSelected to set
     */
    public void setInvInvoiceDTOSelected(InvInvoiceDTO invInvoiceDTOSelected) {
        this.invInvoiceDTOSelected = invInvoiceDTOSelected;
    }

}
