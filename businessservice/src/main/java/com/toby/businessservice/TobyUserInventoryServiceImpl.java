/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserRole;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class TobyUserInventoryServiceImpl implements TobyUserInventoryService {

    @EJB
    private GenericDAO dao;

    @EJB
    TobyUserInventoryService tobyUserInventoryService;

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranch(Integer userId, Integer branchId,Integer type) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranchByType(userId, branchId,type);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(tobyUserBank.getInvInventoryId() != null ? tobyUserBank.getInvInventoryId() : null);
        }

        return inventoryList;
    }
    
    @Override
    public List<InvInventory> getAllInventroisByUserAndBranchPer(Integer userId, Integer branchId) {

        List<TobyUserInventory> tobyUserBankList = tobyUserInventoryService.getAllTobyUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if(tobyUserBank.getInvInventoryId() != null){
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return inventoryList;
    }
    
    @Override
    public List<InvInventory> getAllInventroisByUserAndBranchPerByType(Integer userId, Integer branchId,Integer invType) {

        List<TobyUserInventory> tobyUserBankList = tobyUserInventoryService.getAllTobyUserInventroiesByUserAndBranchByType(userId, branchId,invType);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if(tobyUserBank.getInvInventoryId() != null){
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return inventoryList;
    }

    @Override
    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);

        return dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = 0 ", params);
    }

    @Override
    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranchByType(Integer userId, Integer branchId,Integer invType) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        params.put("invType", invType);
        return dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = :invType", params);
    }

    @Override
    public TobyUserInventory updateTobyUserInventory(TobyUserInventory tobyUserInventory) {
        return dao.updateEntity(tobyUserInventory);
    }

    @Override
    public void deleteInventory(TobyUserRole tobyUserRole, InvInventory inventory) {
        String sql = "delete from TobyUserInventory e  where e.branchId.id  = " + tobyUserRole.getBranchId().getId()
                + " AND e.userId.id = " + tobyUserRole.getUserId().getId() + " AND e.invInventoryId.id = " + inventory.getId();
        dao.executeDeleteQuery(sql);
    }
    
    @Override
    public void deleteTobyUserInventory(List<TobyUserInventory> tobyUserInventoryList) {
        dao.deleteEntity(tobyUserInventoryList);
    }
    
    @Override
    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranch(TobyUser tobyUser) {
// صفحة فواتير المبيعات
        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranch(tobyUser.getId(), tobyUser.getBranchId().getId());

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId(), tobyUser));
        }

        return inventoryList;
    }
    
    @Override
    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranchByType(TobyUser tobyUser,Integer invType) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranchByType(tobyUser.getId(), tobyUser.getBranchId().getId(),invType);

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId(), tobyUser));
        }

        return inventoryList;
    }

    
    public List<TobyUserInventory> getAllTobyUserGallaryByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        params.put("invType", 2);

        return dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE (e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = :invType)", params);
    }
    
    @Override
    public List<InvInventoryDTO> getAllGallaryByUserAndBranch(TobyUser tobyUser) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserGallaryByUserAndBranch(tobyUser.getId(), tobyUser.getBranchId().getId());

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId(), tobyUser));
        }

        return inventoryList;
    }
    
    @Override
    public List<InvInventoryDTO> getAllInventroisByUserAndBranchPerDTO(Integer userId, Integer branchId) {

        List<TobyUserInventory> tobyUserBankList = tobyUserInventoryService.getAllTobyUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if(tobyUserBank.getInvInventoryId() != null){
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return returnListDTO(inventoryList);
    }
    
    
     private List<InvInventoryDTO> returnListDTO(List<InvInventory> list) {
        List<InvInventoryDTO> dTOList = new ArrayList<>();
        for (InvInventory entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private InvInventoryDTO mapTODTO(InvInventory entity, Boolean needDetail) {
        InvInventoryDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private InvInventoryDTO initMapToDTO(InvInventory entity) {

        InvInventoryDTO dTO = new InvInventoryDTO();
        dTO.setId(entity.getId());

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId(): null);

        return dTO;
    }

    private InvInventory mapFromDTO(InvInventoryDTO dTO, TobyUser tobyUser) {
        InvInventory entity = new InvInventory();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

        if (dTO.getId()
                == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

}
