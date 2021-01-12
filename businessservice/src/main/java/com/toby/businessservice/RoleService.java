/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface RoleService {

    public List<TobyRole> getAllRoles();

    public List<TobyRole> getAllRolesListByCompanyId(Integer companyId);

    public TobyRole addRole(TobyRole role);

    public TobyRole findRole(Integer roleId);

    public TobyRole updateRole(TobyRole role);

    public void deleteRole(TobyRole role);
}
