/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
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
public class IgmaUserRoleServiceImpl implements IgmaUserRoleService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteUserRole(IgmaUserRole isagUserRole) {
        dao.deleteEntity(isagUserRole);
    }

    @Override
    public List<IgmaUserRole> getAllUserRole() {
        return dao.findAll(IgmaUserRole.class);
    }

    @Override
    public IgmaUserRole addUserRole(IgmaUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public IgmaUserRole findUserRole(Integer userRoleId) {
        return dao.findEntityById(IgmaUserRole.class, userRoleId);
    }

    @Override
    public IgmaUserRole updateUserRole(IgmaUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public List<IgmaUserRole> findUserRoleByUserId(Integer userid) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        List<IgmaUserRole> userRoles = dao.findListByQuery("SELECT u FROM IgmaUserRole u "
                + " WHERE u.id =:userId"
                + " )", params);
        return userRoles;
    }

    @Override
    public IgmaUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        params.put("branchId", branchId);
        List<IgmaUserRole> userRoles = dao.findListByQuery("SELECT u FROM IgmaUserRole u "
                + " WHERE u.userId.id =:userId AND u.branchId.id = :branchId", params);
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0);
    }

    @Override
    public List<IgmaUserRole> findUserRoleByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<IgmaUserRole> userRoles = dao.findListByQuery("SELECT u FROM IgmaUserRole u "
                + " WHERE u.branchId.id = :branchId", params);
        for (IgmaUserRole isagUserRole : userRoles) {
//            isagUserRole.setGlYearList(isagUserYearService.findYearByUserId(isagUserRole.getUserId().getId(), isagUserRole.getBranchId()));
//            isagUserRole.setGlBankList(isagUserBankService.getAllBankListByUserAndBranch(isagUserRole.getUserId().getId(), isagUserRole.getBranchId().getId()));
//            isagUserRole.setInventoryList(isagUserInventoryService.getAllInventroisByUserAndBranch(isagUserRole.getUserId().getId(), isagUserRole.getBranchId().getId()));
        }
        return userRoles;
    }

    @Override
    public List<Branch> findBranchByUserId(Integer userId) {
        List<Branch> branchList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<IgmaUserRole> userRoles = dao.findListByQuery("SELECT u FROM IgmaUserRole u "
                + " WHERE u.userId.id =:userId AND u.roleId IS NOT NULL"
                + " ", params);
        if (userRoles.isEmpty()) {
            return null;
        } else {
            for (IgmaUserRole userRole : userRoles) {
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
        List<IgmaUserRole> userRoles = dao.findListByQuery("SELECT u FROM IgmaUserRole u "
                + " WHERE u.userId.id =:userId AND u.roleId IS NOT NULL"
                + " ", params);
        if (userRoles.isEmpty()) {
            return null;
        } else {
            for (IgmaUserRole userRole : userRoles) {
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

//    @Override
//    public IgmaUserRole addUserRole(IgmaUserRole userRole, List<GlYear> yearListDeleted, List<IsagUserYear> isagUserYearListSelection,
//            List<GlBank> glBankListDeleted, List<IsagUserBank> isagUserBankListAdded,
//            List<InvInventory> inventoryListDeleted, List<IsagUserInventory> isagUserInventoryListAdded,
//            List<Employee> employeeDTOListDeleted, List<IsagUserEmployee> isagUserEmployeeListAdded) {
//        IgmaUserRole isagUserRole = dao.updateEntity(userRole);

        // Delete IsagUserYear
//        for (GlYear glYear : yearListDeleted) {
//            if (glYear != null) {
//                isagUserYearService.DeleteYear(isagUserRole, glYear);
//            }
//        }
//
//        // Update IsagUserYear
//        for (IsagUserYear isagUserYear : isagUserYearListSelection) {
//            isagUserYearService.updateIsagUserYear(isagUserYear);
//        }
//
//        // Delete IsagUserBank
//        for (GlBank glBank : glBankListDeleted) {
//            isagUserBankService.deleteGlBank(isagUserRole, glBank);
//        }
//
//        // Update IsagUserBank
//        for (IsagUserBank isagUserBank : isagUserBankListAdded) {
//            isagUserBankService.updateIsagUserBank(isagUserBank);
//        }

        // Delete IsagUserInventory
//        for (InvInventory inventory : inventoryListDeleted) {
//            isagUserInventoryService.deleteInventory(isagUserRole, inventory);
//        }
//
//        // Update IsagUserInventory
//        for (IsagUserInventory isagUserInventory : isagUserInventoryListAdded) {
//            isagUserInventoryService.updateIsagUserInventory(isagUserInventory);
//        }

        // Delete IsagUserEmployee
//        for (Employee employee : employeeDTOListDeleted) {
//            isagUserEmployeeService.deleteEmployee(isagUserRole, employee);
//        }

        // Update IsagUserEmployee
//        for (IsagUserEmployee isagUserEmployee : isagUserEmployeeListAdded) {
//            isagUserEmployeeService.updateIsagUserEmployee(isagUserEmployee);
//        }

//        return isagUserRole;
//    }

    @Override
    public void deleteUserRoleAndUserYear(IgmaUserRole isagUserRole) {
//        List<IsagUserYear> isagUserYearList = isagUserYearService.findIsagUserYearByUserIdAndBranchId(isagUserRole.getId(), isagUserRole.getBranchId());
//        List<IsagUserBank> isagUserBankList = isagUserBankService.getAllIsagUserBankListByUserAndBranch(isagUserRole.getId(), isagUserRole.getBranchId().getId());
//        List<IsagUserInventory> isagUserInventoryList = isagUserInventoryService.getAllIsagUserInventroiesByUserAndBranch(isagUserRole.getId(), isagUserRole.getBranchId().getId());
//
//        if (isagUserYearList != null && !isagUserYearList.isEmpty()) {
//            isagUserYearService.deleteIsagUserYear(isagUserYearList);
//        }
//        if (isagUserBankList != null && !isagUserBankList.isEmpty()) {
//            isagUserBankService.deleteIsagUserBank(isagUserBankList);
//        }
//        if (isagUserInventoryList != null && !isagUserInventoryList.isEmpty()) {
//            isagUserInventoryService.deleteIsagUserInventory(isagUserInventoryList);
//        }
        dao.deleteEntity(isagUserRole);
    }

    @Override
    public IgmaUserRole findUserRoleWithYear(Integer userRoleId) {

        IgmaUserRole isagUserRole = dao.findEntityById(IgmaUserRole.class, userRoleId);
//        isagUserRole.setGlYearList(isagUserYearService.findYearByUserId(isagUserRole.getUserId().getId(), isagUserRole.getBranchId()));

        return isagUserRole;
    }

    @Override
    public List<IgmaUser> getUsersForBranch(Integer selectedBranchId) {
        List<IgmaUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iurl FROM IgmaUserRole iurl where iurl.branchId.id = :id";
        List<IgmaUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IgmaUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }
    
    @Override
    public List<IgmaUser> getUsersForCompany(Integer selectedCompany) {
        List<IgmaUser> isagUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedCompany);
        String sql = "SELECT iurl FROM IgmaUserRole iurl where iurl.companyId.id = :id";
        List<IgmaUserRole> isagUserRoleList = dao.findListByQuery(sql, params);
        for (IgmaUserRole isagUserRole : isagUserRoleList) {
            isagUserList.add(isagUserRole.getUserId());
        }
        return isagUserList;
    }

}
