/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.GlGeneralSetting;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GlGeneralSettingService {

    public GlGeneralSetting findGeneralSetting(Integer generalSettingId);

    public GlGeneralSetting updateGeneralSetting(GlGeneralSetting generalSetting);

    public List<GlGeneralSetting> getGeneralSetting();
}
