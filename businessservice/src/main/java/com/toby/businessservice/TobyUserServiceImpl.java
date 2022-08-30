/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.entity.Branch;
import com.toby.entity.Employee;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq004
 */
@Stateless
public class TobyUserServiceImpl implements TobyUserService {

    @EJB
    private GenericDAO dao;
    @EJB
    ProProductionStagesService proProductionStagesService;
    @EJB
    TobyUserProproductionService tobyUserProproductionService;
    @EJB
    TobyUserRoleService tobyUserRoleService;

    public TobyUserServiceImpl() {

    }

    @Override
    public TobyUser authenticateUser(String userCode, String password) {
        TobyUser user = findUserByCode(userCode);
        System.out.println("user: " + user);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    
    public TobyUser findUserByCode(String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        System.out.println("userCode: " + userCode);
        TobyUser user;
        user = dao.findEntityByNamedQuery("TobyUser.authenticate", params);
        return user;
    }

    @Override
    public List<TobyUser> getAllUsersListByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(TobyUser.class, companyId);
    }

    @Override
    public List<TobyUser> getUsersForBranch(Integer selectedBranchId) {
        List<TobyUser> tobyUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iur FROM TobyUserRole iur where iur.branchId = :id";
        List<TobyUserRole> tobyUserRoleList = dao.findListByQuery(sql, params);
        for (TobyUserRole tobyUserRole : tobyUserRoleList) {
            tobyUserList.add(tobyUserRole.getUserId());
        }
        return tobyUserList;
    }

    @Override
    public TobyUser findUser(Integer userId) {
        return dao.findEntityById(TobyUser.class, userId);
    }

    @Override
    public List<TobyUser> getAllUsers() {
        return dao.findAll(TobyUser.class);

    }

    @Override
    public void deleteUser(TobyUser user) {
        dao.deleteEntity(user);
    }

    @Override
    public TobyUser addUser(TobyUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<String> getAuthorizedScreens(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("roleId", id);
        return dao.findListByQuery("SELECT priv.screenId.name FROM TobyPrivilege priv WHERE priv.roleId.id = :roleId AND priv.view = true", params);
    }

    @Override
    public TobyUser updateUser(TobyUser user) {
        return dao.updateEntity(user);
    }

    @Override
    public List<TobyUser> getAllUsersByName(String name) {
        Map<String, Object> params = new HashMap();
        params.put("name", name);
        return dao.findListByQuery("SELECT user FROM TobyUser user WHERE  user.name LIKE CONCAT('%',:name,'%')", params);

    }

    @Override
    public List<TobyUser> getAllUsersByCode(String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        return dao.findListByQuery("SELECT user FROM TobyUser user WHERE  user.userCode= :userCode", params);

    }

    @Override
    public List<TobyUser> getAllUsersByNameAndCode(String user_name, String userCode) {
        Map<String, Object> params = new HashMap();
        params.put("userCode", userCode);
        params.put("name", user_name);
        return dao.findListByQuery("SELECT user FROM TobyUser user WHERE  user.userCode= :userCode AND user.name LIKE ':name%'", params);

    }

    @Override
    public List<TobyUser> getUsersForBranch(Integer selectedBranchId, TobyUser tobyUser) {
        List<TobyUser> tobyUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT iur FROM TobyUserRole iur where iur.branchId.id = :id");
        params.put("id", selectedBranchId);

        if (tobyUser.getName() != null && !tobyUser.getName().isEmpty()) {
            params.put("name", tobyUser.getName());
            sql.append(" AND iur.userId.name LIKE CONCAT('%',:name,'%') ");

        }
        if (tobyUser.getUserCode() != null && !tobyUser.getUserCode().isEmpty()) {
            params.put("userCode", tobyUser.getUserCode());
            sql.append(" AND iur.userId.userCode = :userCode");
        }

        List<TobyUserRole> tobyUserRoleList = dao.findListByQuery(sql.toString(), params);
        for (TobyUserRole tobyUserRole : tobyUserRoleList) {
            tobyUserList.add(tobyUserRole.getUserId());
        }
        return tobyUserList;
    }

    @Override
    public TobyUser validateOldPassword(TobyUser tobyUser, String newPassword) {
        tobyUser = findUser(tobyUser.getId());
        if (!newPassword.equals(tobyUser.getPassword())) {
            tobyUser.setMsg("يجب ادخال الرقم السري");
        }

        return tobyUser;
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

    @Override
    public List<TobyUserDTO> getAllUserOfproduction(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<TobyUser> details = dao.findListByQuery("SELECT e FROM TobyUser e where e.id in ( select p.userId.id from TobyUserProproduction p where p.branchId.id =:branchId)", params);

        return mapToDTOList(details, tobyUser);
    }

    public TobyUserDTO mapToDTO(TobyUser tobyUser) {

        TobyUserDTO tobyUserDTO = new TobyUserDTO();
        tobyUserDTO.setId(tobyUser.getId());
        tobyUserDTO.setMaster(tobyUser.getMaster());
        tobyUserDTO.setResidencyNumber(tobyUser.getResidencyNumber());
        tobyUserDTO.setMsg(tobyUser.getMsg());
        tobyUserDTO.setName(tobyUser.getName());
        tobyUserDTO.setPassword(tobyUser.getPassword());
        tobyUserDTO.setUserCode(tobyUser.getUserCode());
        tobyUserDTO.setCreatedBy(tobyUserDTO.getCreatedBy() != null ? tobyUser.getCreatedBy().getId() : null);
        tobyUserDTO.setCreatedDate(tobyUser.getCreationDate());
        tobyUserDTO.setBranchId(tobyUser.getBranchId().getId() != null ? tobyUser.getBranchId().getId() : null);
        tobyUserDTO.setCompanyId(tobyUser.getCompanyId() != null ? tobyUser.getCompanyId().getId() : null);
        tobyUserDTO.setActiveCrm(tobyUser.getActiveCrm());
//        if (tobyUser.getActiveCrm() != null) {
//            tobyUserDTO.setActiveCrm(Boolean.TRUE);
//            tobyUserDTO.setActiveCrmName("له صلاحية لدخول السي ر م ");
//        } else {
//            tobyUserDTO.setActiveCrm(Boolean.FALSE);
//            tobyUserDTO.setActiveCrmName("ليس له صلاحية لدخول السي ر م");
//
//        }

        return tobyUserDTO;
    }

    public List<TobyUserDTO> mapToDTOList(List<TobyUser> tobyUserList, TobyUser tobyUser) {
        List<TobyUserDTO> tobyUserDTOsList = new ArrayList<>();
        for (TobyUser tobyUser1 : tobyUserList) {
            tobyUserDTOsList.add(mapToDTO(tobyUser1));
        }
        return tobyUserDTOsList;
    }

    @Override
    public List<TobyUserDTO> getUsersByCompanyId(Integer companyId) {
        List<TobyUser> details = dao.findEntityListByCompanyId(TobyUser.class, companyId);
        return returnTODTO(details, null);
    }

    public List<TobyUserDTO> returnTODTO(List<TobyUser> list, TobyUserDTO dTO) {
        List<TobyUserDTO> listDTO = new ArrayList<>();
        for (TobyUser tobyUser : list) {
            listDTO.add(mapToDTO(tobyUser));
        }
        return listDTO;
    }

    @Override
    public void updateInvItem2(List<TobyUser> tobyUserList) {
        Map<String, Integer> stageMap = new HashMap<>();
        Integer i = 1;

        for (TobyUser tobyUser : tobyUserList) {
            System.out.println("tobyUser -> " + tobyUser.getUserCode());
            tobyUser = updateUser(tobyUser);
            if (stageMap.containsKey(tobyUser.getMsg())) {

                saveany(stageMap, tobyUser);

            } else {
                ProProductionStagesDTO proProductionStagesDTO = new ProProductionStagesDTO();

                proProductionStagesDTO.setName(tobyUser.getMsg());
                proProductionStagesDTO.setNameEn(tobyUser.getMsg());
                proProductionStagesDTO.setPrice(BigDecimal.ZERO);
                proProductionStagesDTO.setTypeStage(0);

                ProProductionStagesDTO x = proProductionStagesService.saveProProductionStage(proProductionStagesDTO, tobyUser);

                stageMap.put(x.getName(), x.getId());

                saveany(stageMap, tobyUser);

            }
        }
    }

    private void saveany(Map<String, Integer> stageMap, TobyUser tobyUser) {
        ProProductionStages proProductionStages = new ProProductionStages();
        TobyUserRole tobyUserRole = new TobyUserRole();

        proProductionStages.setId(stageMap.get(tobyUser.getMsg()));

        TobyUserProproduction tobyUserProproductionDTO = new TobyUserProproduction();
        tobyUserProproductionDTO.setUserId(tobyUser);
        tobyUserProproductionDTO.setProproductionId(proProductionStages);

        TobyCompany tobyCompany = new TobyCompany();
        tobyCompany.setId(125);
        tobyUserProproductionDTO.setCompanyId(tobyCompany);

        TobyUser user = new TobyUser();
        user.setId(201);
        tobyUserProproductionDTO.setCreatedBy(user);

        Branch branch = new Branch();
        branch.setId(182);
        tobyUserProproductionDTO.setBranchId(branch);

        tobyUserProproductionDTO.setCreationDate(new Date());

        tobyUserProproductionService.updateTobyUserProproduction(tobyUserProproductionDTO, tobyUser);

        tobyUserRole.setBranchId(branch);
        tobyUserRole.setCompanyId(tobyCompany);
        tobyUserRole.setCreatedBy(user);
        tobyUserRole.setCreationDate(new Date());
        tobyUserRole.setUserId(tobyUser);
        tobyUserRole.setRoleId(null);

        tobyUserRoleService.addUserRole(tobyUserRole);
    }

    @Override
    public List<TobyUserDTO> getUsersByInventoryId(Integer InventoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("InventoryId", InventoryId);
        List<TobyUser> details = dao.findListByQuery("SELECT e.userId FROM TobyUserInventory e where e.invInventoryId.id = :InventoryId AND activeCrm = :1 ", params);
        return returnTODTO(details, null);
    }

    @Override
    public TobyUser updateUser(Integer userId) {
        TobyUser tobyUser = dao.findEntityById(TobyUser.class, userId);
        return tobyUser;
    }

    @Override
    public String save(String  userName, String oldPassword, String newUserPassword, String newPasswordConfirm) {
        TobyUser tobyUser = findUserByCode(userName);
        try {
            if (validateOldPassword1(tobyUser, oldPassword) && validateNewPassword1(oldPassword, newUserPassword) && validateNewPasswordConfirm1(newPasswordConfirm, newUserPassword)) {
                tobyUser.setPassword(newUserPassword);
                updateUser(tobyUser);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "SAVED"));
            }

            return "";
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateOldPassword1(TobyUser tobyUser, String oldPassword) {
        try {
            validateOldPassword(tobyUser, oldPassword);

            if (tobyUser.getMsg() != null && !"".equals(tobyUser.getMsg())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", tobyUser.getMsg()));
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateNewPassword1(String oldPassword, String newUserPassword) {
        try {
            String msg = validateNewPassword(oldPassword, newUserPassword);

            if (!"".equals(msg)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", msg));
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateNewPasswordConfirm1(String newPasswordConfirm, String newUserPassword) {
        try {

            String msg = validateConfirmationPassword(newPasswordConfirm, newUserPassword);

            if (!"".equals(msg)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", msg));
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Employee> getEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT e.employeeId FROM TobyUser e WHERE e.branchId.id = :branchId  AND e.id = :userId", params);
    }
 

}
