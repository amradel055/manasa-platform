/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InventoryDelegatorDTO;
import com.isag.entity.InventoryDelegator;
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

    public InventoryDelegator findInventoryDelegator(Integer id);

    public List<InventoryDelegator> findDelegator(Integer branchId, Integer delegatorId, String code, Integer type);

    public List<InventoryDelegatorDTO> getSalesDTOByBranch(Integer branchId);

    public List<InventoryDelegator> getDelegatorByBranch(Integer branchId, Integer type);

    public List<InventoryDelegatorDTO> getDelegatorDTOByBranch(Integer branchId, Integer type);

    public InventoryDelegator getDelegatorDTOByBranchAndSerial(Integer branchId, Integer type, String code);
}
