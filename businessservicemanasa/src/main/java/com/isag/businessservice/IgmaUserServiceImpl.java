/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaUserDTO;
import com.isag.entity.Employee;
import com.isag.entity.IgmaUser;
import com.isag.entity.IgmaUserRole;
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
public class IgmaUserServiceImpl implements IgmaUserService {

    @EJB
    private GenericDAO dao;

    public IgmaUserServiceImpl() {

    }

    @Override
    public IgmaUser authenticateUser(String userCode, String password) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        System.out.println("userCode: " + userCode);
        IgmaUser user;
        user = dao.findEntityByNamedQuery("IgmaUser.authenticate", params);
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
        IgmaUser user;
        user = dao.findEntityByNamedQuery("IgmaUser.authenticate", params);
        System.out.println("user: " + user);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<IgmaUser> getAllUsersListByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(IgmaUser.class, companyId);
    }
    
    @Override
    public List<IgmaUserDTO> getUsersByCompanyId(Integer companyId){
    List<IgmaUser> details = dao.findEntityListByCompanyId(IgmaUser.class, companyId);
    return returnTODTO(details, null);
    }

    @Override
    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId) {
        List<IgmaUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iur FROM IgmaUserRole iur where iur.branchId = :id";
        List<IgmaUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IgmaUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

    @Override
    public IgmaUser findUser(Integer userId) {
        return dao.findEntityById(IgmaUser.class, userId);
    }

    @Override
    public List<IgmaUser> getAllUsers() {
        return dao.findAll(IgmaUser.class);

    }
    
    @Override
    public List<IgmaUserDTO> getAllUsersDTO() {
        List<IgmaUser> list = dao.findAll(IgmaUser.class);
        return returnTODTO(list, null);
    }

    @Override
    public void deleteUser(IgmaUser user) {
        dao.deleteEntity(user);
    }

    @Override
    public IgmaUser addUser(IgmaUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<String> getAuthorizedScreens(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("roleId", id);
        return dao.findListByQuery("SELECT priv.screenId.name FROM IsagPrivilege priv WHERE priv.roleId.id = :roleId AND priv.view = true", params);
    }

    @Override
    public IgmaUser updateUser(IgmaUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<IgmaUser> getAllUsersByName(String name) {
        Map<String, Object> params = new HashMap();
        params.put("name", name);
        return dao.findListByQuery("SELECT user FROM IgmaUser user WHERE  user.name LIKE CONCAT('%',:name,'%')", params);

    }

    @Override
    public List<IgmaUser> getAllUsersByCode(String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        return dao.findListByQuery("SELECT user FROM IgmaUser user WHERE  user.userCode= :userCode", params);

    }

    @Override
    public List<IgmaUser> getAllUsersByNameAndCode(String user_name, String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        params.put("name", user_name);
        return dao.findListByQuery("SELECT user FROM IgmaUser user WHERE  user.userCode= :userCode AND user.name LIKE ':name%'", params);

    }

    @Override
    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId, IgmaUser isagUser) {
        List<IgmaUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT iur FROM IgmaUserRole iur where iur.branchId.id = :id");
        params.put("id", selectedBranchId);

        if (isagUser.getName() != null && !isagUser.getName().isEmpty()) {
            params.put("name", isagUser.getName());
            sql.append(" AND iur.userId.name LIKE CONCAT('%',:name,'%') ");

        }
        if (isagUser.getUserCode() != null && !isagUser.getUserCode().isEmpty()) {
            params.put("userCode", isagUser.getUserCode());
            sql.append(" AND iur.userId.userCode = :userCode");
        }

        List<IgmaUserRole> isagUserRoleList = dao.findListByQuery(sql.toString(), params);
        for (IgmaUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

    @Override
    public IgmaUser validateOldPassword(IgmaUser isagUser, String newPassword) {
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
    
    public List<IgmaUserDTO> returnTODTO(List<IgmaUser> list, IgmaUserDTO dTO) {
        List<IgmaUserDTO> listDTO = new ArrayList<>();
        for(IgmaUser isagUser : list){
            listDTO.add(mapTODTO(isagUser, dTO));
        }
        return listDTO;
    }

    public IgmaUserDTO mapTODTO(IgmaUser entity, IgmaUserDTO dTO) {
        if (dTO == null) {
            dTO = new IgmaUserDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(IgmaUserDTO dTO, IgmaUser entity) {

        dTO.setId(entity.getId());
        dTO.setName(entity.getName());
        dTO.setPassword(entity.getPassword());
        dTO.setUserCode(entity.getUserCode());
        
     

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
    }

    
     @Override
    public List<Employee> getEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT e.employeeId FROM IgmaUser e WHERE e.branchId.id = :branchId  AND e.id = :userId", params);
    }
    
    @Override
    public String updateUserPassword(Integer userId , String oldPassword , String newPassword) {
        IgmaUser eUser = dao.findEntityById(IgmaUser.class, userId);
        if(eUser.getPassword().equals(oldPassword)){
        eUser.setPassword(newPassword);
         dao.updateEntity(eUser);
         return "تم تغير كلمة السر" ;
        }else {
         return "كلمة السر خاطئه" ;
        }
      
    }
}
