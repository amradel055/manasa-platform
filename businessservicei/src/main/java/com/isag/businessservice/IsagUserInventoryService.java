/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.EmployeeDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserInventory;
import com.isag.entity.IsagUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface IsagUserInventoryService {

    public List<InvInventory> getAllInventroisByUserAndBranch(Integer userId, Integer branchId);

    public List<InvInventory> getAllInventroisByUserAndBranchPer(Integer userId, Integer branchId);

    public List<IsagUserInventory> getAllIsagUserInventroiesByUserAndBranch(Integer userId, Integer branchId);

    public List<IsagUserEmployee> getAllIsagUserEmployeesByUserAndBranch(Integer userId, Integer branchId);

    public IsagUserInventory updateIsagUserInventory(IsagUserInventory isagUserInventory);

    public void deleteInventory(IsagUserRole isagUserRole, InvInventory inventory);

    public void deleteIsagUserInventory(List<IsagUserInventory> isagUserInventoryList);

    public List<InvInventoryDTO> getAllInventroisByUserAndBranchPerDTO(Integer userId, Integer branchId);

    public List<EmployeeDTO> getAllEmployeesByUserAndBranchPerDTO(Integer userId, Integer branchId);

}
