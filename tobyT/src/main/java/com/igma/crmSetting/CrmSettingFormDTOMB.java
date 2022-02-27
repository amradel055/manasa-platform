/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmSetting;

import com.toby.businessservice.TobyNotificationSettingService;
import com.toby.toby.CrmBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmedEssam
 */
@Named(value = "crmSettingFormDTOMB")
@ViewScoped
public class CrmSettingFormDTOMB extends CrmBasicDataForm {

    @EJB
    private TobyNotificationSettingService tobyNotificationSettingService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "CrmSettingFormDTOMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            getTobyNotificationSettingDTO();
            getCrmEventDTO();
        } catch (Exception e) {
            saveError(e, "CrmSettingFormDTOMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String save() {
        saveCrmNotificationSetting();
        saveCrmSetting();
        msgSave(getCrmSettingDTO().getMsg());
        return "";
    }
}
