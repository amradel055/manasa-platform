/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invaddingorder;

import com.toby.businessservice.InvAddingOrderService;
import com.toby.define.ScreenNameClassEnum;
import com.toby.toby.InventoryBasicDataForm;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "invAddingOrderFormDTOMB")
@ViewScoped
public class invAddingOrderFormDTOMB extends InventoryBasicDataForm implements Serializable {

    // EJBs
    @EJB
    private InvAddingOrderService invAddingOrderService;
    
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
            saveError(e, "invPurchaseOrderFormMB", "init");
            
        }
    }
    
    @Override
    public void load() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Addinvorder")) {
                reset();
                fillDefaultList();                
                putCurrency();
                completeGallary(null);
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Editinvorder")) {
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
            checkDetailData();
            setInvInvoiceDTO(invAddingOrderService.save(getInvInvoiceDTO(), getUserData().getUser()));
            checkDetailData();
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }
    
    public String exit() {
        try {
            if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(getInvInvoiceDTO().getScreenName())) {
                exit("../invpermissionorder_1/invpermissionorderlist.xhtml");
            }else if (ScreenNameClassEnum.INVADDINGORDERFORM.equals(getInvInvoiceDTO().getScreenName())){
                exit("../invaddingorder/invAddingOrderList1.xhtml");
            }
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
    
}
