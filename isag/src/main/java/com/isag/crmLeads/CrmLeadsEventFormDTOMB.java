/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.crmLeads;

import com.isag.isag.CrmBasicDataForm;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "crmLeadsEventFormDTOMB")
@ViewScoped
public class CrmLeadsEventFormDTOMB extends CrmBasicDataForm {

    // EJBs

    @Override
    @PostConstruct
    public void init() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditLead")) {
                Integer id = (Integer) getContext().getSessionMap().get("leadSelected");
                fillEventsDTO(id);
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

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
