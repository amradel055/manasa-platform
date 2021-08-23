/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.IgmaPrivilege;
import java.util.ArrayList;
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
    private List<IgmaPrivilege> IsagPrivilegeList = new ArrayList<>();

    @Override
    public List<IgmaPrivilege> getRolePrivileges(Integer roleId) {
        Map<String, Object> params = new HashMap();
        params.put("id", roleId);
        return dao.findListByQuery("SELECT priv FROM IsagPrivilege priv WHERE priv.roleId.id = :id order by priv.screenId.serial", params);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public IgmaPrivilege save(IgmaPrivilege privilege) {
        return dao.updateEntity(privilege);
    }

    @Override
    public IgmaPrivilege getUserPrivilegeOnScreen(Integer roleId, String entityName) {
        IgmaPrivilege userPrivilege = new IgmaPrivilege();
        Map<String, Object> params = new HashMap();
        params.put("roleId", roleId);
        params.put("entityName", entityName);
        List<IgmaPrivilege> privileges = dao.findListByQuery("SELECT priv FROM IsagPrivilege "
                + "priv WHERE priv.view = 1 AND priv.screenId.name = :entityName AND "
                + "priv.roleId.id = :roleId", params);
        if (privileges.size() > 0) {
            userPrivilege = privileges.get(0);
        }
        return userPrivilege;
    }
 
    @Override
    public List<IgmaPrivilege> getUserPrivileges(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
        List<IgmaPrivilege> privileges = dao.findListByQuery("SELECT priv FROM "
                + "IsagPrivilege priv WHERE (priv.view = 1 OR priv.edit = 1 OR priv.delete = 1 OR priv.add = 1) AND priv.roleId.id IN "
                + "(SELECT userrole.roleId.id FROM IsagUserRole userrole WHERE userrole.userId.id = :userId AND userrole.branchId.id=:branchId)", params);
        return privileges;
    }

    @Override
    public void revokeAllPrivilege(Integer id) {
//     Map<String, Object> params = new HashMap();
//        params.put("roleId", id);

        dao.executeDeleteQuery("delete  from IsagPrivilege priv WHERE priv.id > 0 AND priv.roleId.id=" + id);
    }
    
    @Override
    public void revokePrivileges() {
        dao.executeDeleteQuery("delete  from IsagPrivilege priv WHERE priv.id > 0  AND  (priv.add = 0 and priv.view = 0 and priv.delete = 0 and priv.edit = 0 )");
    }
     
    @Override
    public void save(List<IgmaPrivilege> privilege ) {
        for(IgmaPrivilege isagPrivilege : privilege){
            dao.updateEntity(isagPrivilege);
        }
    }
}
