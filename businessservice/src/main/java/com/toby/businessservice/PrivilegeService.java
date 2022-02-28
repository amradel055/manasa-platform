/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyPrivilege;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface PrivilegeService {

    public List<TobyPrivilege> getRolePrivileges(Integer roleId);

    public TobyPrivilege save(TobyPrivilege privilege);
    
    public void save(List<TobyPrivilege> privilege);

    public TobyPrivilege getUserPrivilegeOnScreen(Integer userId, String entityName);

    public List<TobyPrivilege> getUserPrivileges(Integer userId, Integer branchId);

    public void revokeAllPrivilege(Integer id);
    
    public void revokePrivileges();
}
