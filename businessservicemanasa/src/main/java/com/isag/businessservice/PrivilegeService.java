/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.IgmaPrivilege;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface PrivilegeService {

    public List<IgmaPrivilege> getRolePrivileges(Integer roleId);

    public IgmaPrivilege save(IgmaPrivilege privilege);
    
    public void save(List<IgmaPrivilege> privilege);

    public IgmaPrivilege getUserPrivilegeOnScreen(Integer userId, String entityName);

    public List<IgmaPrivilege> getUserPrivileges(Integer userId, Integer branchId);

    public void revokeAllPrivilege(Integer id);
    
    public void revokePrivileges();
}
