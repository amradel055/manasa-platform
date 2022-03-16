/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserProproductionDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlBank;
import com.toby.entity.GlYear;
import com.toby.entity.InvInventory;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import com.toby.entity.TobyUserYear;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface TobyUserRoleService {

    public void deleteUserRole(TobyUserRole tobyUserRole);

    public List<TobyUserRole> getAllUserRole();

    public TobyUserRole addUserRole(TobyUserRole userRole);

    public TobyUserRole findUserRole(Integer userRoleId);

    public TobyUserRole updateUserRole(TobyUserRole userRole);

    public List<TobyUserRole> findUserRoleByUserId(Integer userid);

    public TobyUserRole findUserRoleByUserIdAndBranch(Integer userid, Integer branchId);

    public List<TobyUserRole> findUserRoleByBranch(Integer branchId);

    public List<Branch> findBranchByUserId(Integer userId);

    public TobyUserRole addUserRole(TobyUserRole userRole, List<GlYear> yearListDeleted, List<TobyUserYear> tobyUserYearListSelection,
            List<GlBank> glBankListDeleted, List<TobyUserBank> tobyUserBankListAdded,
            List<ProProductionStages> productionStagesDTOListDeleted,List<TobyUserProproduction> tobyUserProProductionStagesListAdded,
            List<InvInventory> inventoryListDeleted, List<TobyUserInventory> tobyUserInventoryListAdded,
            List<InvInventory> gallaryListDeleted, List<TobyUserInventory> tobyUsergallaryListAdded);

    public void deleteUserRoleAndUserYear(TobyUserRole tobyUserRole);

    public TobyUserRole findUserRoleWithYear(Integer userRoleId);

    public List<TobyUser> getUsersForBranch(Integer selectedBranchId);
}
