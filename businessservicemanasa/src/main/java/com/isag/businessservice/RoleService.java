/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.IgmaRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface RoleService {

    public List<IgmaRole> getAllRoles();

    public List<IgmaRole> getAllRolesListByCompanyId(Integer companyId);

    public IgmaRole addRole(IgmaRole role);

    public IgmaRole findRole(Integer roleId);

    public IgmaRole updateRole(IgmaRole role);

    public void deleteRole(IgmaRole role);
}
