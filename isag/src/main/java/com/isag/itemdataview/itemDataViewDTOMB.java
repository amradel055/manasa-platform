/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.itemdataview;

import com.isag.crmLeads.*;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.CrmBasicDataForm;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "itemDataViewDTOMB")
@ViewScoped
public class itemDataViewDTOMB extends CrmBasicDataForm {

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;

    // EJBs
    @EJB
    private OrganizationSiteService organizationSiteService;
   

    @Override
    @PostConstruct
    public void init() {
        try {
           if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditLead")) {
                Integer id = (Integer) getContext().getSessionMap().get("leadSelected");
                ///
                setInvOrganizationSiteDTO(organizationSiteService.findOrganizationSiteDTOById(id));
            }
            
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

    /**
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
