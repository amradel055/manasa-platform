/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyRole;
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
    public List<TobyRole> getAllRoles() {
        return dao.findAll(TobyRole.class);

    }

    @Override
    public TobyRole addRole(TobyRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public TobyRole findRole(Integer roleId) {
        return dao.findEntityById(TobyRole.class, roleId);
    }

    @Override
    public TobyRole updateRole(TobyRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public List<TobyRole> getAllRolesListByCompanyId(Integer companyId ) {
        return dao.findEntityListByCompanyId(TobyRole.class,companyId);
    }

    @Override
    public void deleteRole(TobyRole role) {
      dao.deleteEntity(role);
    }

}
    // Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

