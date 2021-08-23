/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IsagUserRoleService {

    public void deleteUserRole(IsagUserRole isagUserRole);

    public List<IsagUserRole> getAllUserRole();

    public IsagUserRole addUserRole(IsagUserRole userRole);

    public IsagUserRole findUserRole(Integer userRoleId);

    public IsagUserRole updateUserRole(IsagUserRole userRole);

    public List<IsagUserRole> findUserRoleByUserId(Integer userid);

    public IsagUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId);

    public List<IsagUserRole> findUserRoleByBranch(Integer branchId);

    public List<Branch> findBranchByUserId(Integer userId);
    
    public List<APIBranchDTO> findBranchDTOByUserId(Integer userId);

    public void deleteUserRoleAndUserYear(IsagUserRole isagUserRole);

    public IsagUserRole findUserRoleWithYear(Integer userRoleId);

    public List<IsagUser> getUsersForBranch(Integer selectedBranchId);
    
    public List<IsagUser> getUsersForCompany(Integer selectedCompany);
}
