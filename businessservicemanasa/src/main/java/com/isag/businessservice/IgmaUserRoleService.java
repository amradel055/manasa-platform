/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
import com.isag.entity.Employee;
import com.isag.entity.IgmaUser;
import com.isag.entity.IgmaUserEmployee;
import com.isag.entity.IgmaUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IgmaUserRoleService {

    public void deleteUserRole(IgmaUserRole isagUserRole);

    public List<IgmaUserRole> getAllUserRole();

    public IgmaUserRole addUserRole(IgmaUserRole userRole);

    public IgmaUserRole findUserRole(Integer userRoleId);

    public IgmaUserRole updateUserRole(IgmaUserRole userRole);

    public List<IgmaUserRole> findUserRoleByUserId(Integer userid);

    public IgmaUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId);

    public List<IgmaUserRole> findUserRoleByBranch(Integer branchId);

    public List<Branch> findBranchByUserId(Integer userId);
    
    public List<APIBranchDTO> findBranchDTOByUserId(Integer userId);

    public void deleteUserRoleAndUserYear(IgmaUserRole isagUserRole);

    public IgmaUserRole findUserRoleWithYear(Integer userRoleId);

    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId);
    
    public List<IgmaUser> getUsersForCompany(Integer selectedCompany);
}
