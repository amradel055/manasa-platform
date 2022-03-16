/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagUserDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class IsagUserServiceImpl implements IsagUserService {

    @EJB
    private GenericDAO dao;

    public IsagUserServiceImpl() {

    }

    @Override
    public IsagUser authenticateUser(String userCode, String password) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        System.out.println("userCode: " + userCode);
        IsagUser user;
        user = dao.findEntityByNamedQuery("IsagUser.authenticate", params);
        System.out.println("user: " + user);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
    
    @Override
    public Boolean ckeckAdmin(String password) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", "admin");
        IsagUser user;
        user = dao.findEntityByNamedQuery("IsagUser.authenticate", params);
        System.out.println("user: " + user);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<IsagUser> getAllUsersListByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(IsagUser.class, companyId);
    }
    
    @Override
    public List<IsagUserDTO> getUsersByCompanyId(Integer companyId){
    List<IsagUser> details = dao.findEntityListByCompanyId(IsagUser.class, companyId);
    return returnTODTO(details, null);
    }

    @Override
    public List<IsagUser> getUsersForBranch(Integer selectedBranchId) {
        List<IsagUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iur FROM IsagUserRole iur where iur.branchId = :id";
        List<IsagUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IsagUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

    @Override
    public IsagUser findUser(Integer userId) {
        return dao.findEntityById(IsagUser.class, userId);
    }

    @Override
    public List<IsagUser> getAllUsers() {
        return dao.findAll(IsagUser.class);

    }
    
    @Override
    public List<IsagUserDTO> getAllUsersDTO() {
        List<IsagUser> list = dao.findAll(IsagUser.class);
        return returnTODTO(list, null);
    }

    @Override
    public void deleteUser(IsagUser user) {
        dao.deleteEntity(user);
    }

    @Override
    public IsagUser addUser(IsagUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<String> getAuthorizedScreens(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("roleId", id);
        return dao.findListByQuery("SELECT priv.screenId.name FROM IsagPrivilege priv WHERE priv.roleId.id = :roleId AND priv.view = true", params);
    }

    @Override
    public IsagUser updateUser(IsagUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<IsagUser> getAllUsersByName(String name) {
        Map<String, Object> params = new HashMap();
        params.put("name", name);
        return dao.findListByQuery("SELECT user FROM IsagUser user WHERE  user.name LIKE CONCAT('%',:name,'%')", params);

    }

    @Override
    public List<IsagUser> getAllUsersByCode(String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        return dao.findListByQuery("SELECT user FROM IsagUser user WHERE  user.userCode= :userCode", params);

    }

    @Override
    public List<IsagUser> getAllUsersByNameAndCode(String user_name, String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        params.put("name", user_name);
        return dao.findListByQuery("SELECT user FROM IsagUser user WHERE  user.userCode= :userCode AND user.name LIKE ':name%'", params);

    }

    @Override
    public List<IsagUser> getUsersForBranch(Integer selectedBranchId, IsagUser isagUser) {
        List<IsagUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT iur FROM IsagUserRole iur where iur.branchId.id = :id");
        params.put("id", selectedBranchId);

        if (isagUser.getName() != null && !isagUser.getName().isEmpty()) {
            params.put("name", isagUser.getName());
            sql.append(" AND iur.userId.name LIKE CONCAT('%',:name,'%') ");

        }
        if (isagUser.getUserCode() != null && !isagUser.getUserCode().isEmpty()) {
            params.put("userCode", isagUser.getUserCode());
            sql.append(" AND iur.userId.userCode = :userCode");
        }

        List<IsagUserRole> isagUserRoleList = dao.findListByQuery(sql.toString(), params);
        for (IsagUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

    @Override
    public IsagUser validateOldPassword(IsagUser isagUser, String newPassword) {
        isagUser = findUser(isagUser.getId());
        if (!newPassword.equals(isagUser.getPassword())) {
            isagUser.setMsg("يجب ادخال الرقم السري");
        }

        return isagUser;
    }

    @Override
    public String validateNewPassword(String userPassword, String newPassword) {
        String msg = "";
        if (newPassword.equals(userPassword)) {
            msg = "يجب اختيار رقم سري جديد";
        }

        return msg;
    }

    @Override
    public String validateConfirmationPassword(String confirmationPassword, String newPassword) {
        String msg = "";
        if (!confirmationPassword.equals(newPassword)) {
            msg = "الرقم السري غير متطابق";
        }

        return msg;
    }
    
    public List<IsagUserDTO> returnTODTO(List<IsagUser> list, IsagUserDTO dTO) {
        List<IsagUserDTO> listDTO = new ArrayList<>();
        for(IsagUser isagUser : list){
            listDTO.add(mapTODTO(isagUser, dTO));
        }
        return listDTO;
    }

    public IsagUserDTO mapTODTO(IsagUser entity, IsagUserDTO dTO) {
        if (dTO == null) {
            dTO = new IsagUserDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(IsagUserDTO dTO, IsagUser entity) {

        dTO.setId(entity.getId());
        dTO.setName(entity.getName());
        dTO.setPassword(entity.getPassword());
        dTO.setUserCode(entity.getUserCode());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    
     @Override
    public List<Employee> getEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT e.employeeId FROM IsagUser e WHERE e.branchId.id = :branchId  AND e.id = :userId", params);
    }
}
