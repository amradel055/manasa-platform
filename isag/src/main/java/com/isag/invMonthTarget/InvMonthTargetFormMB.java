/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invMonthTarget;

import com.isag.businessservice.MonthTargetService;
import com.isag.isag.InventoryBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmed
 */
@Named(value = "invMonthTargetFormMB")
@ViewScoped
public class InvMonthTargetFormMB extends InventoryBasicDataForm {

    @EJB
    private MonthTargetService monthTargetService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddInvMonthTarget")) {
            reset();
        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvMonthTargetDTO")) {
            fillInvInvoiceDTO();
        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvMonthTargetDTO() != null) {
            getInvInvoiceDTO().getInvMonthTargetDTO().setMsg(null);
            setInvInvoiceDTO(monthTargetService.addMonthTargetDTO(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            errorMsg("يرجي ادخال بيانات");
        }
        return "";

    }

    @Override
    public String exit() {
        try {
            exit("../invMonthTarget/invMonthTargetList.xhtml");
        } catch (Exception e) {
            saveError(e, "InvMonthTargetFormMB", "exit");
        }
        return "";

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
