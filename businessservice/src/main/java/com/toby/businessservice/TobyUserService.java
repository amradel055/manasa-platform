/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.TobyUserDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface TobyUserService {

    public TobyUser authenticateUser(String userName, String password);

    public List<TobyUser> getAllUsersListByCompanyId(Integer companyId);

    public List<TobyUser> getAllUsers();

    public TobyUser findUser(Integer userId);

    public void deleteUser(TobyUser user);

    public TobyUser addUser(TobyUser user);

    public TobyUser updateUser(TobyUser user);

    public List<String> getAuthorizedScreens(Integer id);

    public List<TobyUser> getAllUsersByName(String name);

    public List<TobyUser> getAllUsersByCode(String userCode);

    public List<TobyUser> getAllUsersByNameAndCode(String user_name, String userCode);

    public List<TobyUser> getUsersForBranch(Integer selectedBranchId);

    public List<TobyUser> getUsersForBranch(Integer selectedBranchId, TobyUser tobyUser);

    public TobyUser validateOldPassword(TobyUser tobyUser, String newPassword);

    public String validateNewPassword(String userPassword, String newPassword);

    public String validateConfirmationPassword(String confirmationPassword, String newPassword);

    public List<TobyUserDTO> getAllUserOfproduction(TobyUser tobyUser);

    public List<TobyUserDTO> getUsersByCompanyId(Integer companyId);
    
    public void updateInvItem2(List<TobyUser> tobyUserList);
    
    public TobyUser updateUser(Integer userId);
    
    public List<TobyUserDTO> getUsersByInventoryId(Integer InventoryId);

}
