/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invopeningbalanceitem;

import com.isag.businessservice.InvOpeningBalanceItemService;
import com.isag.isag.InventoryBasicDataForm;
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

@Named(value = "InvOpeningBalanceItemFormMB")
@ViewScoped
public class InvOpeningBalanceItemFormMB extends InventoryBasicDataForm  {
    
    @EJB
    private InvOpeningBalanceItemService invOpeningBalanceItemService;

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invOpeningBalanceItemService.save(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

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
            saveError(e, "InvOpeningBalanceItemFormMB", "init");
        }
    }

    @Override
    public String exit() {
        exit("../invopeningbalanceitem/invopeningbalanceitemlist.xhtml");
        return "";
    }

    @Override
    public void load() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addINVOPENINGBALANCE")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editINVOPENINGBALANCE")) {
                  reset();
                getInvInvoiceDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                fillDefaultList();
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
            } else {
                exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
