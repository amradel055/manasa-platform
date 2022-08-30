/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface TobyUserInventoryService {

    public List<InvInventory> getAllInventroisByUserAndBranch(Integer userId, Integer branchId,Integer type);

    public List<InvInventory> getAllInventroisByUserAndBranchPer(Integer userId, Integer branchId);
    
    public List<InvInventory> getAllInventroisByUserAndBranchPerByType(Integer userId, Integer branchId,Integer invType);

    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranch(Integer userId, Integer branchId);

    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranchByType(Integer userId, Integer branchId, Integer invType);

    public TobyUserInventory updateTobyUserInventory(TobyUserInventory tobyUserInventory);

    public void deleteInventory(TobyUserRole tobyUserRole, InvInventory inventory);

    public void deleteTobyUserInventory(List<TobyUserInventory> tobyUserInventoryList);

    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranch(TobyUser tobyUser);

    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranchByType(TobyUser tobyUser, Integer invType);

    public List<InvInventoryDTO> getAllGallaryByUserAndBranch(TobyUser tobyUser);
    
    public List<InvInventoryDTO> getAllInventroisByUserAndBranchPerDTO(Integer userId, Integer branchId);
    
    public List<EmployeeDTO> getAllEmployeesByUserAndBranchPerDTO(Integer userId, Integer branchId);

}
