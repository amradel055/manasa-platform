package com.toby.invreservation;

import com.toby.businessservice.*;
import com.toby.define.ScreenNameClassEnum;
import com.toby.toby.InventoryBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;

/**
 * @author khaled
 */
@Named(value = "invReservationFormDTOMB")
@ViewScoped
public class InvReservationFormDTOMB extends InventoryBasicDataForm {

    // EJBs
    @EJB
    private InvReservationService invReservationService;

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
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddInvReservation")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvReservation")) {
                fillInvInvoiceDTO();
            } else {
                exit("../invpurchaseinvoice/invpurchaseinvoicelist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invReservationService.save(getInvInvoiceDTO(), getUserData().getUser()));
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

    public String exit() {
        if (ScreenNameClassEnum.INVRESERVATIONFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreservation_1/invReservationList.xhtml");
        } 
        return "";
    }

}
