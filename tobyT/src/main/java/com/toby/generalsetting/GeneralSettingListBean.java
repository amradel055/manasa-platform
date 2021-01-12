/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.generalsetting;

import com.toby.businessservice.GlGeneralSettingServiceImpl;
import com.toby.entity.GlGeneralSetting;
import com.toby.toby.BaseListBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author hq004
 */
public class GeneralSettingListBean extends BaseListBean implements Serializable {

    private GlGeneralSetting generalsetting;

    @EJB
    GlGeneralSettingServiceImpl generalsettingFacadeREST;

    public GeneralSettingListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            reset();
        } catch (Exception e) {
            saveError(e, "GeneralSettingListBean", "init");
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
        try {
            setGeneralsetting(generalsettingFacadeREST.getGeneralSetting().get(0));
        } catch (Exception e) {
            saveError(e, "GeneralSettingListBean", "reset");
        }
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
