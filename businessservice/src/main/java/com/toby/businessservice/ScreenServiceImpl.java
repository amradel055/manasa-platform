/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyScreen;
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
    public List<TobyScreen> getAllScreenNotAssignedToRole(Integer roleId, Integer roleParentId) {
        Map<String, Object> params = new HashMap();
        List<TobyScreen> screens;
        params.put("roleId", roleId);
        String sql = "";
        if (roleParentId == null) {
            sql = "SELECT s FROM TobyScreen s "
                    + " WHERE s.id  NOT IN ( "
                    + "  SELECT priv.screenId.id FROM TobyPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleId"
                    + " ) order by s.serial";
            screens = dao.findListByQuery(sql, params);
        } else {
            params.put("roleParentId", roleParentId);
            sql ="SELECT s FROM TobyScreen s "
                    + " WHERE s.id  NOT IN ( "
                    + "  SELECT priv.screenId.id FROM TobyPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleId"
                    + " ) AND s.id  IN ( "
                    + "  SELECT priv.screenId.id FROM TobyPrivilege priv "
                    + "   WHERE priv.roleId.id=:roleParentId"
                    + " ) order by s.serial";
            screens = dao.findListByQuery(sql, params);
        }
        return screens;
    }

    @Override
    public List<TobyScreen> getAllScreens() {
        List<TobyScreen> screens = dao.findListByQuery("SELECT s FROM TobyScreen s WHERE s.id > 0 ");
        return screens;
    }

}
