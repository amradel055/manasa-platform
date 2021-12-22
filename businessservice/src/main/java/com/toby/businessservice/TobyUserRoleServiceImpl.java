/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.Branch;
import com.toby.entity.GlBank;
import com.toby.entity.GlYear;
import com.toby.entity.InvInventory;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyRole;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import com.toby.entity.TobyUserYear;
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
public class TobyUserRoleServiceImpl implements TobyUserRoleService {

    @EJB
    private GenericDAO dao;
    @EJB
    private TobyUserYearService tobyUserYearService;

    @EJB
    private TobyUserBankService tobyUserBankService;
    
    @EJB
    private TobyUserProproductionService tobyUserProproductionService;

    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    
    @EJB
    private ProProductionStagesService proProductionStagesService;

    @Override
    public void deleteUserRole(TobyUserRole tobyUserRole) {
        dao.deleteEntity(tobyUserRole);
    }

    @Override
    public List<TobyUserRole> getAllUserRole() {
        return dao.findAll(TobyUserRole.class);
    }

    @Override
    public TobyUserRole addUserRole(TobyUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public TobyUserRole findUserRole(Integer userRoleId) {
        return dao.findEntityById(TobyUserRole.class, userRoleId);
    }

    @Override
    public TobyUserRole updateUserRole(TobyUserRole userRole) {
        return dao.updateEntity(userRole);
    }

    @Override
    public List<TobyUserRole> findUserRoleByUserId(Integer userid) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        List<TobyUserRole> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.id =:userId"
                + " )", params);
        return userRoles;
    }

    @Override
    public TobyUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        params.put("branchId", branchId);
        List<TobyUserRole> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.userId.id =:userId AND u.branchId.id = :branchId", params);
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0);
    }

    @Override
    public List<TobyUserRole> findUserRoleByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<TobyUserRole> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.branchId.id = :branchId", params);
        List<TobyUserRole> userRolesNew = new ArrayList<>();
        for (TobyUserRole tobyUserRole : userRoles) {
            TobyUserRole tobyUserRoleNew = new TobyUserRole();
            tobyUserRoleNew.setId(tobyUserRole.getId());
            tobyUserRoleNew.setCostAvarageUpdate(tobyUserRole.getCostAvarageUpdate());
            tobyUserRoleNew.setSellPriceUpdate(tobyUserRole.getSellPriceUpdate());
            TobyRole tobyRole = new TobyRole();
            tobyRole.setId(tobyUserRole.getRoleId().getId());
            tobyRole.setName(tobyUserRole.getRoleId().getName());
            tobyUserRoleNew.setRoleId(tobyRole);
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(tobyUserRole.getUserId().getId());
            tobyUserRoleNew.setUserId(tobyUser);
            Branch branch = new Branch();
            branch.setId(tobyUserRole.getBranchId().getId());
            tobyUserRoleNew.setBranchId(branch);
            userRolesNew.add(tobyUserRoleNew);
            }
        Integer x = 0 ;
        for (TobyUserRole tobyUserRole : userRolesNew) {
            System.err.println(x++);
            tobyUserRole.setGlYearList(tobyUserYearService.findYearByUserId(tobyUserRole.getUserId().getId(), tobyUserRole.getBranchId()));
            tobyUserRole.setGlBankList(tobyUserBankService.getAllBankListByUserAndBranch(tobyUserRole.getUserId().getId(), tobyUserRole.getBranchId().getId()));
            tobyUserRole.setInventoryList(tobyUserInventoryService.getAllInventroisByUserAndBranch(tobyUserRole.getUserId().getId(), tobyUserRole.getBranchId().getId(),0));
            tobyUserRole.setGallaryList(tobyUserInventoryService.getAllInventroisByUserAndBranch(tobyUserRole.getUserId().getId(), tobyUserRole.getBranchId().getId(),2));
            tobyUserRole.setProductionStagesList(proProductionStagesService.getAllProproductionListByUserAndBranch(tobyUserRole.getUserId(), tobyUserRole.getBranchId().getId()));
        }
        return userRoles;
    }

    @Override
    public List<Branch> findBranchByUserId(Integer userId) {
        List<Branch> branchList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<TobyUserRole> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.id =:userId AND roleId IS NOT NULL"
                + " )", params);
        if (userRoles.isEmpty()) {
            return null;
        } else {
            for (TobyUserRole userRole : userRoles) {
                branchList.add(userRole.getBranchId());
            }
        }
        return branchList;
    }

    @Override
    public TobyUserRole addUserRole(TobyUserRole userRole, List<GlYear> yearListDeleted, List<TobyUserYear> tobyUserYearListSelection,
            List<GlBank> glBankListDeleted, List<TobyUserBank> tobyUserBankListAdded,
            List<ProProductionStages> productionStagesDTOListDeleted,List<TobyUserProproduction> tobyUserProProductionStagesListAdded,
            List<InvInventory> inventoryListDeleted, List<TobyUserInventory> tobyUserInventoryListAdded,
            List<InvInventory> gallaryListDeleted, List<TobyUserInventory> tobyUsergallaryListAdded) {
        TobyUserRole tobyUserRole = dao.updateEntity(userRole);

        // Delete TobyUserYear
        for (GlYear glYear : yearListDeleted) {
            if (glYear != null) {
                tobyUserYearService.DeleteYear(tobyUserRole, glYear);
            }
        }

        // Update TobyUserYear
        for (TobyUserYear tobyUserYear : tobyUserYearListSelection) {
            tobyUserYearService.updateTobyUserYear(tobyUserYear);
        }

        // Delete  TobyUserProproduction
        for ( ProProductionStages  proProductionStages : productionStagesDTOListDeleted) {
            tobyUserProproductionService.deleteProductionStages(tobyUserRole, proProductionStages);
        }

        // Update  TobyUserProproduction
        for ( TobyUserProproduction proproductionDTO : tobyUserProProductionStagesListAdded) {
            tobyUserProproductionService.updateTobyUserProproduction(proproductionDTO, userRole.getUserId());
        }
        
        // Delete TobyUserBank
        for (GlBank glBank : glBankListDeleted) {
            tobyUserBankService.deleteGlBank(tobyUserRole, glBank);
        }

        // Update TobyUserBank
        for (TobyUserBank tobyUserBank : tobyUserBankListAdded) {
            tobyUserBankService.updateTobyUserBank(tobyUserBank);
        }

        // Delete TobyUserInventory
        for (InvInventory inventory : inventoryListDeleted) {
            tobyUserInventoryService.deleteInventory(tobyUserRole, inventory);
        }

        // Update TobyUserInventory
        for (TobyUserInventory tobyUserInventory : tobyUserInventoryListAdded) {
            tobyUserInventoryService.updateTobyUserInventory(tobyUserInventory);
        }

        // Delete TobyUsergallary
        for (InvInventory inventory : gallaryListDeleted) {
            tobyUserInventoryService.deleteInventory(tobyUserRole, inventory);
        }

        // Update TobyUserInventory
        for (TobyUserInventory tobyUserInventory : tobyUsergallaryListAdded) {
            tobyUserInventoryService.updateTobyUserInventory(tobyUserInventory);
        }

        return tobyUserRole;
    }

    @Override
    public void deleteUserRoleAndUserYear(TobyUserRole tobyUserRole) {
        List<TobyUserYear> tobyUserYearList = tobyUserYearService.findTobyUserYearByUserIdAndBranchId(tobyUserRole.getId(), tobyUserRole.getBranchId());
        List<TobyUserBank> tobyUserBankList = tobyUserBankService.getAllTobyUserBankListByUserAndBranch(tobyUserRole.getId(), tobyUserRole.getBranchId().getId());
        List<TobyUserInventory> tobyUserInventoryList = tobyUserInventoryService.getAllTobyUserInventroiesByUserAndBranch(tobyUserRole.getId(), tobyUserRole.getBranchId().getId());

        tobyUserYearService.deleteTobyUserYear(tobyUserYearList);
        tobyUserBankService.deleteTobyUserBank(tobyUserBankList);
        tobyUserInventoryService.deleteTobyUserInventory(tobyUserInventoryList);
        dao.deleteEntity(tobyUserRole);
    }

    @Override
    public TobyUserRole findUserRoleWithYear(Integer userRoleId) {

        TobyUserRole tobyUserRole = dao.findEntityById(TobyUserRole.class, userRoleId);
        tobyUserRole.setGlYearList(tobyUserYearService.findYearByUserId(tobyUserRole.getUserId().getId(), tobyUserRole.getBranchId()));

        return tobyUserRole;
    }

    @Override
    public List<TobyUser> getUsersForBranch(Integer selectedBranchId) {
        List<TobyUser> tobyUserList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT iurl FROM TobyUserRole iurl where iurl.branchId.id = :id";
        List<TobyUserRole> tobyUserRoleList = dao.findListByQuery(sql, params);
        for (TobyUserRole tobyUserRole : tobyUserRoleList) {
            tobyUserList.add(tobyUserRole.getUserId());
        }
        return tobyUserList;
    }

}
