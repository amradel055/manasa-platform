/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvInventoryServiceImpl implements InvInventoryService {

    @EJB
    private GenericDAO dao;
  

    @Override
    public void deleteNewInventory(InvInventory invInventory) {
        dao.deleteEntity(invInventory);
    }

    @Override
    public InvInventory updateNewInventory(InvInventory invInventory, IsagUser user , IsagCompany company , Branch branch) {
        if (invInventory.getId() == null) {
            invInventory.setCreatedBy(user);
            invInventory.setCreationDate(new Date());
        } else {
            invInventory.setModificationDate(new Date());
            invInventory.setModifiedBy(user);
        }
        List<InvInventory> invList = new ArrayList<>();
        if (invInventory.getId() != null) {
            invList = findInventory(invInventory.getBranchId().getId(), invInventory.getId(), invInventory.getCode());
            if (invList != null && !invList.isEmpty()) {
                return null;
            }
        }

        if (invInventory.getId() == null) {
            invList = findInventory(invInventory.getBranchId().getId(), invInventory.getCode());
            if (invList != null && !invList.isEmpty()) {
                return null;
            }
        }
        return dao.updateEntity(invInventory);
    }

    @Override
    public InvInventory addNewInventory(InvInventory invInventory) {
        return dao.updateEntity(invInventory);
    }
   

    @Override
    public List<InvInventory> getALLInventoriesByBranch(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", id);
        List<InvInventory> InvInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId  ORDER BY i.code", params);
        return InvInventories;
    }
    
    @Override
    public List<InvInventory> getALLInventoriesByBranchPer(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", id);
        List<InvInventory> invInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId  ORDER BY i.code", params);
        List<InvInventory> list = new ArrayList<>();
        for(InvInventory invInventory : invInventories){
            InvInventory inventory = new InvInventory();
            inventory.setId(invInventory.getId());
            inventory.setName(invInventory.getName());
            inventory.setCode(invInventory.getCode());            
            list.add(inventory);
        }
        return list;
    }

    @Override
    public InvInventory findInventory(Integer id) {
        return dao.findEntityById(InvInventory.class, id);
    }

    @Override
    public List<InvInventory> findInventory(Integer branchId, Integer inventoryId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("inventoryId", inventoryId);
        params.put("code", code);

        return dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.id != :inventoryId and i.code = :code  ORDER BY i.code", params);
    }

    @Override
    public List<InvInventory> findInventory(Integer branchId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);

        return dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.code = :code  ORDER BY i.code", params);
    }

    @Override
    public List<InvInventory> getAllInventories() {
        return dao.findAll(InvInventory.class);
    }
}
