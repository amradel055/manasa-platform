/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvDelegatorService {

    public void deleteInventoryDelegator(InventoryDelegator inventoryDelegator);

    public InventoryDelegator updateInventoryDelegator(InventoryDelegator inventoryDelegator);

    public InventoryDelegator addInventoryDelegator(InventoryDelegator inventoryDelegator);

    public List<InventoryDelegator> getDelegatorsByBranch(Integer id);

    public List<InventoryDelegator> getSalesByBranch(Integer branchId);

    public List<InventoryDelegator> getPurchaseByBranch(Integer branchId);

    public List<InventoryDelegator> getDelegatorsByBranchAndId(Integer id, Integer branchId);

//    public InventoryDelegator findInventoryDelegator(Integer id);
    public List<InventoryDelegator> findDelegator(Integer branchId, Integer delegatorId, String code, Integer type);

    public List<InventoryDelegatorDTO> getSalesDTOByBranch(Integer branchId);

    public List<InventoryDelegator> getDelegatorByBranch(Integer branchId, Integer type);
    
    public List<InventoryDelegatorDTO> getDelegatorByInventory(Integer gallaryId, Integer type);

    public List<InventoryDelegatorDTO> getDelegatorDTOByBranch(Integer branchId, Integer type);

    public InventoryDelegatorDTO findInventoryDelegator(Integer id);

    public List<InventoryDelegatorDTO> getDelegatorsByBranchDTO(Integer type, TobyUser tobyUser);

    public List<InventoryDelegatorDTO> getSalesByBranchDTO(Integer branchId, TobyUser tobyUser);

    public List<InventoryDelegatorDTO> getPurchaseByBranchDTO(Integer branchId, TobyUser tobyUser);

    public boolean validateCode(TobyUser tobyUser, Integer inventoryDelegatorId, String code, Integer type);

    public InventoryDelegatorDTO updateInventoryDelegatorDTO(InventoryDelegatorDTO inventoryDelegatorDTO, TobyUser tobyUser);
    
    public InventoryDelegatorDTO findDelegatorDefault(Integer branchId, Integer type) ;

}
