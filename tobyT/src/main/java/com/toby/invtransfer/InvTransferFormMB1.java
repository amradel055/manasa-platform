package com.toby.invtransfer;

import com.toby.businessservice.InvTransferService;
import com.toby.dto.InvDetailDTO;
import com.toby.toby.InventoryBasicDataForm;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Amr
 */
@Named(value = "invTransferFormMB1")
@ViewScoped
public class InvTransferFormMB1 extends InventoryBasicDataForm {

    @EJB
    private InvTransferService  invTransferService;

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
            saveError(e, "InvTransferFormMB", "init");

        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            checkDetailData();
            setInvInvoiceDTO(invTransferService.save(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
            checkDetailData();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }
    
    public String copy() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setId(null);
            getInvInvoiceDTO().setSerial(null);
            getInvInvoiceDTO().setSerialTax(null);            
            getInvInvoiceDTO().setCreatedBy(null);
            for(InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()){
                detailDTO.setId(null);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                fillInvInvoiceDTO();
//                getInvInventoryDTOList();                
//                getInvInventoryDTOListTo();
            } else {
                exit("../invQuotation_1/invQuotationList.xhtml");
            }
            getInvInventoryDTOList();                
                getInvInventoryDTOListTo();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void fillInventoryItem() {
        try {
            if (getInvInvoiceDTO().getInvInventoryId() != null && getInvInvoiceDTO().getInvInventoryId().getId() != null
                    && getInvInvoiceDTO().getInvInventoryIdTo()!= null && getInvInvoiceDTO().getInvInventoryIdTo().getId() != null) {
                if (getInvInvoiceDTO().getInvInventoryId().getId().compareTo(getInvInvoiceDTO().getInvInventoryIdTo().getId()) == 0) {
                    getInvInvoiceDTO().setInvInventoryIdTo(null);
                    errorMessage("يجب اختيار مستودعين مختلفين");
                }
                
                validatedetail();
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "fillInventoryItem()");
        }
    }

}
