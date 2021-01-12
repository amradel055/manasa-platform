/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseOrder;

import com.toby.businessservice.InvPurchaseOrderService;
import com.toby.toby.InventoryBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "invPurchaseOrderFormDTOMB")
@ViewScoped
public class invPurchaseOrderFormDTOMB extends InventoryBasicDataForm {
    
    // EJBs
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;

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
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addInvoiceOrder")) {
                reset();
                fillDefaultList();
                putCurrency();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvoiceOrder")) {
                fillInvInvoiceDTO();
            } else {
                exit("../invpurchaseorder_1/invPurchaseOrderList.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "load");

        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invPurchaseOrderService.save(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public String exit() {
        try {
            exit("../invpurchaseorder_1/invPurchaseOrderList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print() {
        try {

            
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "print");

        }
    }

}
