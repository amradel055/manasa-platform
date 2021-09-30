/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface InvInventoryService {

    public List<InvInventoryDTO> findInventoryDTOList(TobyUser tobyUser);

    public List<InvInventoryDTO> findInventoryDTOListBYBranshID(TobyUser tobyUser);

    public List<InvInventory> getALLInventoriesByBranchPer(Integer id);

    public List<InvInventory> getALLInventoriesByBranch(Integer id);

    public List<InvInventory> getALLInventoriesByBranchByType(Integer branchId, Integer invType);

    public List<InvInventoryDTO> getInventoryByInvType(TobyUser tobyUser, int type);

    public void deleteNewInventory(InvInventory inventoryDelegator);

    public InvInventory updateNewInventory(InvInventory inventoryDelegator, TobyUser user, TobyCompany company, Branch branch);

    public InvInventoryDTO updateNewInventoryDTO(InvInventoryDTO invInventoryDTO, TobyUser user);

    public InvInventory addNewInventory(InvInventory inventoryDelegator);

    public InvInventoryDTO findInventory(Integer id);

    public List<InvInventory> findInventory(Integer branchId, Integer inventoryId, String code);

    public Boolean validateCode(TobyUser tobyUser, Integer inventoryId, String code);

    public Boolean validateName(TobyUser tobyUser, Integer inventoryId, String name);

    public List<InvInventory> findInventory(Integer branchId, String code);

    public List<InvInventoryDTO> findInventoryDTOByBranchAndCode(TobyUser tobyUser, String code);

    public List<InvInventory> getAllInventories();

    public List<InvInventory> getALLInventoriesByType(Integer invType);

    public List<InvInventoryDTO> getALLInventorieDTOListByBranch(TobyUser tobyUser);
    
    public InvInventoryDTO mapToDTO(InvInventory entity) ;

}
