/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseInvoiceDTO;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
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

    // EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    @Override
    @PostConstruct
    public void init() {
        try {
            String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName(),getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId());
            if (error.isEmpty()) {
                load();
            } else {
                setErrorGlobal(error);
                exit("../base/error.xhtml");
            }

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addInvoice")) {
                reset();
                fillDefaultList();
                putCurrency();
                getInvInvoiceDTO().setOrganizationSiteId(null);
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvoice")) {
                getInvInvoiceDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                getInvInvoiceDTO().setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(getInvInvoiceDTO().getId()));
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
                getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
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
            msgSave(getInvInvoiceDTO().getMsg());
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

}
