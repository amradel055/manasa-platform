/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.IsagRole;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class RoleServiceImpl implements RoleService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<IsagRole> getAllRoles() {
        return dao.findAll(IsagRole.class);

    }

    @Override
    public IsagRole addRole(IsagRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public IsagRole findRole(Integer roleId) {
        return dao.findEntityById(IsagRole.class, roleId);
    }

    @Override
    public IsagRole updateRole(IsagRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public List<IsagRole> getAllRolesListByCompanyId(Integer companyId ) {
        return dao.findEntityListByCompanyId(IsagRole.class,companyId);
    }

    @Override
    public void deleteRole(IsagRole role) {
      dao.deleteEntity(role);
    }

}
    // Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

