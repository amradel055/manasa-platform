/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.IgmaRole;
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
    public List<IgmaRole> getAllRoles() {
        return dao.findAll(IgmaRole.class);

    }

    @Override
    public IgmaRole addRole(IgmaRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public IgmaRole findRole(Integer roleId) {
        return dao.findEntityById(IgmaRole.class, roleId);
    }

    @Override
    public IgmaRole updateRole(IgmaRole role) {
        return dao.updateEntity(role);
    }

    @Override
    public List<IgmaRole> getAllRolesListByCompanyId(Integer companyId ) {
        return dao.findEntityListByCompanyId(IgmaRole.class,companyId);
    }

    @Override
    public void deleteRole(IgmaRole role) {
      dao.deleteEntity(role);
    }

}
    // Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

