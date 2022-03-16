/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invMonthTarget;

import com.isag.businessservice.MonthTargetService;
import com.isag.dto.InvMonthTargetDTO;
import com.isag.isag.InventoryBasicDataForm;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmedEssam
 */
@Named(value = "invMonthTargetListMB")
@ViewScoped
public class InvMonthTargetListMB extends InventoryBasicDataForm {

    @EJB
    private MonthTargetService monthTargetService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        getInvMonthTargetDTOList();
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddInvMonthTarget");
            exit("../invMonthTarget/invMonthTargetForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "InvMonthTargetListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getInvMonthTargetDTO()  != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invMonthTargetSelected", getInvInvoiceDTOSelected().getInvMonthTargetDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditInvMonthTargetDTO");
                exit("../invMonthTarget/invMonthTargetForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvMonthTargetListMB", "goToEdit");
            return null;
        }
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getInvMonthTargetDTO() != null) {
                try {
                    monthTargetService.deleteMonthTargetDTO(getInvInvoiceDTOSelected(), getUserData().getUser());
                    setInvMonthTargetDTOList(null);
                    getInvMonthTargetDTOList();
                    deletedMeesage();
                } catch (Exception e) {
                    errorMsg(userDDs.get("UNIQE_KEY_ERROR"));
                }
            }
        } catch (Exception ex) {
            saveError(ex, "InvMonthTargetListMB", "delete");
        }
    }

    @Override
    public String save() {
        return "";

    }

    @Override
    public String exit() {
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
