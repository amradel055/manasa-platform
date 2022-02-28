/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.generalsetting;

import com.toby.businessservice.GlGeneralSettingServiceImpl;
import com.toby.entity.GlGeneralSetting;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author hq002
 */
public class GeneralSettingFormBean extends BaseFormBean implements Serializable {

    private GlGeneralSetting generalsetting;

    @EJB
    GlGeneralSettingServiceImpl generalsettingFacadeREST;

    public GeneralSettingFormBean() {
    }

    @Override
    public String save() {
        try {
            generalsettingFacadeREST.updateGeneralSetting(getGeneralsetting());

            return "";
        } catch (Exception e) {
            saveError(e, "GeneralSettingFormBean", "save");
            return null;
        }
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            reset();
        } catch (Exception e) {
            saveError(e, "GeneralSettingFormBean", "init");
        }
    }

    public void reset() {
        try {
            setGeneralsetting(generalsettingFacadeREST.getGeneralSetting().get(0));
        } catch (Exception e) {
            saveError(e, "GeneralSettingFormBean", "reset");
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the generalsetting
     */
    public GlGeneralSetting getGeneralsetting() {
        return generalsetting;
    }

    /**
     * @param generalsetting the generalsetting to set
     */
    public void setGeneralsetting(GlGeneralSetting generalsetting) {
        this.generalsetting = generalsetting;
    }
}
