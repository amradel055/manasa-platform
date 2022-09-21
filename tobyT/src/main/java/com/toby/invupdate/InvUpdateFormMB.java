package com.toby.invupdate;

import com.toby.businessservice.InvUpdateDetailService;
import com.toby.businessservice.InvUpdateService;
import com.toby.dto.InvDetailDTO;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.toby.toby.InventoryBasicDataForm;
import javax.faces.application.FacesMessage;

/**
 * @author khaled
 */
@Named(value = "invUpdateFormMB")
@ViewScoped
public class InvUpdateFormMB extends InventoryBasicDataForm {

    @EJB
    private InvUpdateService invUpdateService;

    @EJB
    private InvUpdateDetailService invUpdateDetailService;

    @Override
    @PostConstruct
    public void init() {
        try {
            String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName());
            if (error.isEmpty()) {
                load();
            } else {
                setErrorGlobal(error);
                exit("../base/error.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "init");

        }
    }

    @Override
    public void load() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddInvUpdate")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvUpdate")) {
                fillInvInvoiceDTO();
            } else {
                exit("../invupdate/InvUpdateListMB.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "load");

        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            checkDetailData();
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invUpdateService.save(getInvInvoiceDTO(), getUserData().getUser()));
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
        try {
            exit("../invupdate/invUpdateList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "exit");
            return null;
        }
    }

    public String saveNew() {
        if (getInvInvoiceDTO() != null) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invUpdateService.saveNew(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
        return "";
    }

    public void validateActulQuantityColumNew(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() != null) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invUpdateDetailService.addInvUpdateDetailsDTONew(invPurchaseInvoiceDetailsValidate, getInvInvoiceDTO().getId(), getUserData().getUser());
                replacedetail(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public void deleteInvDetailNew(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (invPurchaseInvoiceDetailsValidate.getId() != null) {
                invUpdateDetailService.deleteInvUpdateDetailDTO(invPurchaseInvoiceDetailsValidate);
            }
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

}
