/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InventorySetupDTO;
import com.toby.entity.InventorySetup;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InventorySetupService {

    public void addInventorySetup(InventorySetup inventorySetup);

    public InventorySetup getInventoryByCompanyId(Integer companyId);

    public InventorySetup getInventoryByBranchId(Integer branchId);

    public InventorySetup getInventory();
    
    public InventorySetupDTO getInventoryByBranchIdDTO(Integer branchId);
}
