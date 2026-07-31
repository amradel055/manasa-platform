/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.TeacherDataAPI;
import com.isag.dto.IgmaUserDTO;
import com.isag.entity.Employee;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IgmaUserService {

    public IgmaUser authenticateUser(String userName, String password);

    public Boolean ckeckAdmin(String password);

    public List<IgmaUser> getAllUsersListByCompanyId(Integer companyId);

    public List<IgmaUser> getAllUsers();

    public List<IgmaUserDTO> getAllUsersDTO();

    public List<IgmaUserDTO> getUsersByCompanyId(Integer companyId);

    public IgmaUser findUser(Integer userId);

    public void deleteUser(IgmaUser user);

    public IgmaUser addUser(IgmaUser user);

    public IgmaUser updateUser(IgmaUser user);

    public List<String> getAuthorizedScreens(Integer id);

    public List<IgmaUser> getAllUsersByName(String name);

    public List<IgmaUser> getAllUsersByCode(String userCode);

    public List<IgmaUser> getAllUsersByNameAndCode(String user_name, String userCode);

    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId);

    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId, IgmaUser isagUser);

    public IgmaUser validateOldPassword(IgmaUser isagUser, String newPassword);

    public String validateNewPassword(String userPassword, String newPassword);

    public String validateConfirmationPassword(String confirmationPassword, String newPassword);

    public List<Employee> getEmployeesByUserAndBranch(Integer userId, Integer branchId);

    public String updateUserPassword(Integer userId, String oldPassword, String newPassword);

    public List<TeacherDataAPI> getTeacherDataAPI(Integer branchId);
}
