/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.IgmaScreen;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class ScreenServiceImpl implements ScreenService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<IgmaScreen> getAllScreenNotAssignedToRole(Integer roleId, Integer roleParentId) {
        Map<String, Object> params = new HashMap();
        List<IgmaScreen> screens;
        params.put("roleId", roleId);
        String sql = "";
        if (roleParentId == null) {
            sql = "SELECT s FROM IsagScreen s "
                    + " WHERE s.id  NOT IN ( "
                    + "  SELECT priv.screenId.id FROM IsagPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleId"
                    + " ) order by s.serial";
            screens = dao.findListByQuery(sql, params);
        } else {
            params.put("roleParentId", roleParentId);
            sql ="SELECT s FROM IsagScreen s "
                    + " WHERE s.id  NOT IN ( "
                    + "  SELECT priv.screenId.id FROM IsagPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleId"
                    + " ) AND s.id  IN ( "
                    + "  SELECT priv.screenId.id FROM IsagPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleParentId"
                    + " ) order by s.serial";
            screens = dao.findListByQuery(sql, params);
        }
        return screens;
    }

    @Override
    public List<IgmaScreen> getAllScreens() {
        List<IgmaScreen> screens = dao.findListByQuery("SELECT s FROM IsagScreen s WHERE s.id > 0 ");
        return screens;
    }

}
