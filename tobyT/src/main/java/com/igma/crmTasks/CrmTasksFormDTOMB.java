/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmTasks;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.toby.CrmBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "crmTasksFormDTOMB")
@ViewScoped
public class CrmTasksFormDTOMB extends CrmBasicDataForm {

    private CrmEventDTO crmEventDTO;

    // EJBs
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    private OrganizationSiteService organizationSiteService;

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

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    public String validateSave(CrmEventDTO crmEventDTO) {
        String error = cRMEventServiceDTO.validateSave(crmEventDTO);
        return error;
    }

    @Override
    public String save() {
        String error = validateSave(crmEventDTO);
        if (error != null && !error.isEmpty()) {
            errorMessage(error);
        } else {
            getCrmEventDTO().setMsg(null);
            setCrmEventDTO(cRMEventServiceDTO.save(crmEventDTO, getUserData().getUser()));
            msgSave(getCrmEventDTO().getMsg());
        }
        return "";
    }

    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        String error = organizationSiteService.validateSaveLead(invOrganizationSiteDTO,getUserData().getUser());
        return error;
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exitCrmEvent() {
//        try {
//            exit("../glBank/glBankList.xhtml");
//            return "";
//        } catch (Exception e) {
//            saveError(e, "glBankFromMB", "exit");
//            return null;
//        }
        return "";
    }

    public void resetCrmEventDTO() {
        crmEventDTO = null;
    }

    /**
     * @return the crmEventDTO
     */
    public CrmEventDTO getCrmEventDTO() {
        if (crmEventDTO == null) {
            crmEventDTO = new CrmEventDTO();
        }
        return crmEventDTO;
    }

    public void updateEventValue() {
        getCrmEventDTO().setTypee(0);
    }

    public void updateTODOValue() {
        getCrmEventDTO().setTypee(1);
    }

    /**
     * @param crmEventDTO the crmEventDTO to set
     */
    public void setCrmEventDTO(CrmEventDTO crmEventDTO) {
        this.crmEventDTO = crmEventDTO;
    }

}
