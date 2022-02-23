/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.GlGeneralSetting;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class GlGeneralSettingServiceImpl implements GlGeneralSettingService {

    @EJB
    private GenericDAO dao;

    @Override
    public GlGeneralSetting findGeneralSetting(Integer generalSettingId) {
        return dao.findEntityById(GlGeneralSetting.class , generalSettingId);
    }

    @Override
    public GlGeneralSetting updateGeneralSetting(GlGeneralSetting generalSetting) {
        return dao.updateEntity(generalSetting);
    }

    @Override
    public List<GlGeneralSetting> getGeneralSetting() {
        return dao.findAll(GlGeneralSetting.class);
    }

}
