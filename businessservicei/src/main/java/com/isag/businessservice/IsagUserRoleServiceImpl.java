/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
import com.isag.entity.Employee;
import com.isag.entity.GlBank;
import com.isag.entity.GlYear;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserBank;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserInventory;
import com.isag.entity.IsagUserRole;
import com.isag.entity.IsagUserYear;
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
public class IsagUserRoleServiceImpl implements IsagUserRoleService {

    @EJB
    private GenericDAO dao;
    @EJB
    private IsagUserYearService isagUserYearService;

    @EJB
    private IsagUserBankService isagUserBankService;

    @EJB
    private IsagUserInventoryService isagUserInventoryService;

    @EJB
    private IsagUserEmployeeService isagUserEmployeeService;

    @Override
    public void deleteUserRole(IsagUserRole isagUserRole) {
        dao.deleteEntity(isagUserRole);
    }

    @Override
    public List<IsagUserRole> getAllUserRole() {
        return dao.findAll(IsagUserRole.class);
    }

    @Override
    public IsagUserRole addUserRole(IsagUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public IsagUserRole findUserRole(Integer userRoleId) {
        return dao.findEntityById(IsagUserRole.class, userRoleId);
    }

    @Override
    public IsagUserRole updateUserRole(IsagUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public List<IsagUserRole> findUserRoleByUserId(Integer userid) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        List<IsagUserRole> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.id =:userId"
                + " )", params);
        return userRoles;
    }

    @Override
    public IsagUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        params.put("branchId", branchId);
        List<IsagUserRole> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.userId.id =:userId AND u.branchId.id = :branchId", params);
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0);
    }

    @Override
    public List<IsagUserRole> findUserRoleByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<IsagUserRole> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.branchId.id = :branchId", params);
        for (IsagUserRole isagUserRole : userRoles) {
            isagUserRole.setGlYearList(isagUserYearService.findYearByUserId(isagUserRole.getUserId().getId(), isagUserRole.getBranchId()));
            isagUserRole.setGlBankList(isagUserBankService.getAllBankListByUserAndBranch(isagUserRole.getUserId().getId(), isagUserRole.getBranchId().getId()));
            isagUserRole.setInventoryList(isagUserInventoryService.getAllInventroisByUserAndBranch(isagUserRole.getUserId().getId(), isagUserRole.getBranchId().getId()));
        }
        return userRoles;
    }

    @Override
    public List<Branch> findBranchByUserId(Integer userId) {
        List<Branch> branchList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<IsagUserRole> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.userId.id =:userId AND u.roleId IS NOT NULL"
                + " ", params);
        if (userRoles.isEmpty()) {
            return null;
        } else {
            for (IsagUserRole userRole : userRoles) {
                branchList.add(userRole.getBranchId());
            }
        }
        return branchList;
    }
    
    @Override
    public List<APIBranchDTO> findBranchDTOByUserId(Integer userId) {
        List<Branch> branchList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<IsagUserRole> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.userId.id =:userId AND u.roleId IS NOT NULL"
                + " ", params);
        if (userRoles.isEmpty()) {
            return null;
        } else {
            for (IsagUserRole userRole : userRoles) {
                branchList.add(userRole.getBranchId());
            }
        }
        
        if(branchList != null && !branchList.isEmpty()){
            List<APIBranchDTO> dTOList = new ArrayList<>();
            return returnListDTO(branchList,dTOList);
        }
        return null;
    }
    
    private List<APIBranchDTO> returnListDTO(List<Branch> list, List<APIBranchDTO> dTOList) {
        for (Branch entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
        return dTOList;
    }
    
    public APIBranchDTO mapTODTO(Branch entity, APIBranchDTO dTO) {
        if (dTO == null) {
            dTO = new APIBranchDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }
    
    private void initMapToDTO(APIBranchDTO dTO, Branch entity) {

        dTO.setNameAr(entity.getNameAr());
        dTO.setNameEn(entity.getNameEn());
        dTO.setId(entity.getId());

    }

    @Override
    public IsagUserRole addUserRole(IsagUserRole userRole, List<GlYear> yearListDeleted, List<IsagUserYear> isagUserYearListSelection,
            List<GlBank> glBankListDeleted, List<IsagUserBank> isagUserBankListAdded,
            List<InvInventory> inventoryListDeleted, List<IsagUserInventory> isagUserInventoryListAdded,
            List<Employee> employeeDTOListDeleted, List<IsagUserEmployee> isagUserEmployeeListAdded) {
        IsagUserRole isagUserRole = dao.updateEntity(userRole);

        // Delete IsagUserYear
        for (GlYear glYear : yearListDeleted) {
            if (glYear != null) {
                isagUserYearService.DeleteYear(isagUserRole, glYear);
            }
        }

        // Update IsagUserYear
        for (IsagUserYear isagUserYear : isagUserYearListSelection) {
            isagUserYearService.updateIsagUserYear(isagUserYear);
        }

        // Delete IsagUserBank
        for (GlBank glBank : glBankListDeleted) {
            isagUserBankService.deleteGlBank(isagUserRole, glBank);
        }

        // Update IsagUserBank
        for (IsagUserBank isagUserBank : isagUserBankListAdded) {
            isagUserBankService.updateIsagUserBank(isagUserBank);
        }

        // Delete IsagUserInventory
        for (InvInventory inventory : inventoryListDeleted) {
            isagUserInventoryService.deleteInventory(isagUserRole, inventory);
        }

        // Update IsagUserInventory
        for (IsagUserInventory isagUserInventory : isagUserInventoryListAdded) {
            isagUserInventoryService.updateIsagUserInventory(isagUserInventory);
        }

        // Delete IsagUserEmployee
//        for (Employee employee : employeeDTOListDeleted) {
//            isagUserEmployeeService.deleteEmployee(isagUserRole, employee);
//        }

        // Update IsagUserEmployee
//        for (IsagUserEmployee isagUserEmployee : isagUserEmployeeListAdded) {
//            isagUserEmployeeService.updateIsagUserEmployee(isagUserEmployee);
//        }

        return isagUserRole;
    }

    @Override
    public void deleteUserRoleAndUserYear(IsagUserRole isagUserRole) {
        List<IsagUserYear> isagUserYearList = isagUserYearService.findIsagUserYearByUserIdAndBranchId(isagUserRole.getId(), isagUserRole.getBranchId());
        List<IsagUserBank> isagUserBankList = isagUserBankService.getAllIsagUserBankListByUserAndBranch(isagUserRole.getId(), isagUserRole.getBranchId().getId());
        List<IsagUserInventory> isagUserInventoryList = isagUserInventoryService.getAllIsagUserInventroiesByUserAndBranch(isagUserRole.getId(), isagUserRole.getBranchId().getId());

        if (isagUserYearList != null && !isagUserYearList.isEmpty()) {
            isagUserYearService.deleteIsagUserYear(isagUserYearList);
        }
        if (isagUserBankList != null && !isagUserBankList.isEmpty()) {
            isagUserBankService.deleteIsagUserBank(isagUserBankList);
        }
        if (isagUserInventoryList != null && !isagUserInventoryList.isEmpty()) {
            isagUserInventoryService.deleteIsagUserInventory(isagUserInventoryList);
        }
        dao.deleteEntity(isagUserRole);
    }

    @Override
    public IsagUserRole findUserRoleWithYear(Integer userRoleId) {

        IsagUserRole isagUserRole = dao.findEntityById(IsagUserRole.class, userRoleId);
        isagUserRole.setGlYearList(isagUserYearService.findYearByUserId(isagUserRole.getUserId().getId(), isagUserRole.getBranchId()));

        return isagUserRole;
    }

    @Override
    public List<IsagUser> getUsersForBranch(Integer selectedBranchId) {
        List<IsagUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iurl FROM IsagUserRole iurl where iurl.branchId.id = :id";
        List<IsagUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IsagUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }
    
    @Override
    public List<IsagUser> getUsersForCompany(Integer selectedCompany) {
        List<IsagUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedCompany);
        String sql = "SELECT iurl FROM IsagUserRole iurl where iurl.companyId.id = :id";
        List<IsagUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IsagUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

}
