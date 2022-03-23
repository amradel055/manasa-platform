/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.IsagPrivilege;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface PrivilegeService {

    public List<IsagPrivilege> getRolePrivileges(Integer roleId);

    public IsagPrivilege save(IsagPrivilege privilege);
    
    public void save(List<IsagPrivilege> privilege);

    public IsagPrivilege getUserPrivilegeOnScreen(Integer userId, String entityName);

    public List<IsagPrivilege> getUserPrivileges(Integer userId, Integer branchId);

    public void revokeAllPrivilege(Integer id);
    
    public void revokePrivileges();
}
