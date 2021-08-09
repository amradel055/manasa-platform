/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvInventoryService {

    public void deleteNewInventory(InvInventory inventoryDelegator);

    public InvInventory updateNewInventory(InvInventory inventoryDelegator, IsagUser user, IsagCompany company, Branch branch);

    public InvInventory addNewInventory(InvInventory inventoryDelegator);

    public List<InvInventory> getALLInventoriesByBranch(Integer id);

    public List<InvInventory> getALLInventoriesByBranchPer(Integer id);

    public InvInventory findInventory(Integer id);

    public List<InvInventory> findInventory(Integer branchId, Integer inventoryId, String code);

    public List<InvInventory> findInventory(Integer branchId, String code);

    public List<InvInventory> getAllInventories();
}
