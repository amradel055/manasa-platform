/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyPrivilege;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class PrivilegeServiceImpl implements PrivilegeService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<TobyPrivilege> getRolePrivileges(Integer roleId) {
        Map<String, Object> params = new HashMap();
        params.put("id", roleId);
        return dao.findListByQuery("SELECT priv FROM TobyPrivilege priv WHERE priv.roleId.id = :id order by priv.screenId.serial", params);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public TobyPrivilege save(TobyPrivilege privilege) {
        return dao.updateEntity(privilege);
    }

    @Override
    public TobyPrivilege getUserPrivilegeOnScreen(Integer roleId, String entityName) {
        TobyPrivilege userPrivilege = new TobyPrivilege();
        Map<String, Object> params = new HashMap();
        params.put("roleId", roleId);
        params.put("entityName", entityName);
        List<TobyPrivilege> privileges = dao.findListByQuery("SELECT priv FROM TobyPrivilege "
                + "priv WHERE priv.view = 1 AND priv.screenId.name = :entityName AND "
                + "priv.roleId.id = :roleId", params);
        if (privileges.size() > 0) {
            userPrivilege = privileges.get(0);
        }
        return userPrivilege;
    }

    @Override
    public List<TobyPrivilege> getUserPrivileges(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
        List<TobyPrivilege> privileges = dao.findListByQuery("SELECT priv FROM "
                + "TobyPrivilege priv WHERE (priv.view = 1 OR priv.edit = 1 OR priv.delete = 1 OR priv.add = 1) AND priv.roleId.id IN "
                + "(SELECT userrole.roleId.id FROM TobyUserRole userrole WHERE userrole.userId.id = :userId AND userrole.branchId.id=:branchId)", params);
        return privileges;
    }

    @Override
    public void revokeAllPrivilege(Integer id) {
//     Map<String, Object> params = new HashMap();
//        params.put("roleId", id);

        dao.executeDeleteQuery("delete  from TobyPrivilege priv WHERE priv.id > 0 AND priv.roleId.id=" + id);
    }
    
    @Override
    public void revokePrivileges() {
        dao.executeDeleteQuery("delete  from TobyPrivilege priv WHERE priv.id > 0  AND  (priv.add = 0 and priv.view = 0 and priv.delete = 0 and priv.edit = 0 )");
    }

    @Override
    public void save(List<TobyPrivilege> privilege ) {
        for(TobyPrivilege tobyPrivilege : privilege){
            dao.updateEntity(tobyPrivilege);
        }
    }
}
