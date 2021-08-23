/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagUserDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IsagUserService {

    public IsagUser authenticateUser(String userName, String password);

    public Boolean ckeckAdmin(String password);

    public List<IsagUser> getAllUsersListByCompanyId(Integer companyId);

    public List<IsagUser> getAllUsers();

    public List<IsagUserDTO> getAllUsersDTO();


    public List<IsagUserDTO> getUsersByCompanyId(Integer companyId);

    public IsagUser findUser(Integer userId);

    public void deleteUser(IsagUser user);

    public IsagUser addUser(IsagUser user);

    public IsagUser updateUser(IsagUser user);
    
    public List<String> getAuthorizedScreens(Integer id);

    public List<IsagUser> getAllUsersByName(String name);

    public List<IsagUser> getAllUsersByCode(String userCode);

    public List<IsagUser> getAllUsersByNameAndCode(String user_name, String userCode);

    public List<IsagUser> getUsersForBranch(Integer selectedBranchId);

    public List<IsagUser> getUsersForBranch(Integer selectedBranchId, IsagUser isagUser);

    public IsagUser validateOldPassword(IsagUser isagUser, String newPassword);

    public String validateNewPassword(String userPassword, String newPassword);

    public String validateConfirmationPassword(String confirmationPassword, String newPassword);

    public List<Employee> getEmployeesByUserAndBranch(Integer userId, Integer branchId);
}
