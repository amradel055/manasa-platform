/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseInvoiceDTO;

import com.toby.businessservice.InvGlobalService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.define.ScreenNameClassEnum;
import com.toby.toby.InventoryBasicDataForm;
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

// EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    ProFactoryDeliveryService proFactoryDeliveryService;

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
                fillInvInvoiceDTO();
                getInvInvoiceDTO().setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(getInvInvoiceDTO().getId()));
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
                getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
                findAccountStatement();
                getInvOrganizationSiteDTOList(Boolean.TRUE, 0).add(0, getInvInvoiceDTO().getOrganizationSiteId());
                setOrganizationSiteId(getInvInvoiceDTO().getOrganizationSiteId());
                checkFactoryId();
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
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invPurchaseInvoiceService.save(getInvInvoiceDTO(), getUserData().getUser()));
            setOrganizationSiteId(getInvInvoiceDTO().getOrganizationSiteId());
            msgSave(getInvInvoiceDTO().getMsg());
            findAccountStatement();
            if (getUri().contains("proworkorderform")) {
                StringBuilder message = new StringBuilder();
                message.append("عميلنا العزيز ، شكراً لتسوقك تمت عملية شراء رقم ");
                message.append(getInvInvoiceDTO().getSerial());
                message.append(" المدفوع ");
                message.append(getInvInvoiceDTO().getPayed());
                message.append(" المتبقي ");
                message.append(getInvInvoiceDTO().getRemain());
                if (getInvInvoiceDTO().getInvnameType() != null && getInvInvoiceDTO().getInvnameType() == 0) {
                    sendSms(getInvInvoiceDTO().getCustomerMobile(), "التوبى العالمى", message.toString(), "20099319", "0564933202");
                } else if (getInvInvoiceDTO().getInvnameType() != null && getInvInvoiceDTO().getInvnameType() == 1) {
                    sendSms(getInvInvoiceDTO().getCustomerMobile(), "التوبى العالمى", message.toString(), "20099320", "Royal@e12z");
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }

        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
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

}
