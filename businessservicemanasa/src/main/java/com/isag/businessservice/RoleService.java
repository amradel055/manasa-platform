/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.IsagRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface RoleService {

    public List<IsagRole> getAllRoles();

    public List<IsagRole> getAllRolesListByCompanyId(Integer companyId);

    public IsagRole addRole(IsagRole role);

    public IsagRole findRole(Integer roleId);

    public IsagRole updateRole(IsagRole role);

    public void deleteRole(IsagRole role);
}
