/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class InvInventoryImpl implements InvInventoryService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InvInventoryDTO> findInventoryDTOList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();

        params.put("customerId", tobyUser.getBranchId().getId());
        List<InvInventory> detailses = dao.findListByQuery("SELECT i FROM InvInventory i WHERE i.branchId.id = :branchId", params);
        return mapToDTOList(detailses, tobyUser);

    }

    //mapToEntity
    public InvInventory mapToEntity(InvInventoryDTO invInventoryDTO, TobyUser tobyUser) {
        try {
            InvInventory invInventory = new InvInventory();
            invInventory.setCode(invInventoryDTO.getCode());
            invInventory.setInvType(invInventoryDTO.getInvType());
            invInventory.setId(invInventoryDTO.getId());
            invInventory.setCode(invInventoryDTO.getCode());
            invInventory.setName(invInventoryDTO.getName());
            invInventory.setTypeItem(invInventoryDTO.getTypeItem());
            invInventory.setDaribaValue(invInventoryDTO.getDaribaValue());
            invInventory.setSegilValue(invInventoryDTO.getSegilValue());

            if (invInventoryDTO.getGlAdminUnitId() != null) {
                GlAdminUnit glAdminUnit = new GlAdminUnit();
                glAdminUnit.setId(invInventoryDTO.getGlAdminUnitId().getId());
                glAdminUnit.setName(invInventoryDTO.getGlAdminUnitId().getName());
                glAdminUnit.setCode(invInventoryDTO.getGlAdminUnitId().getCode());
                invInventory.setAdminUnitId(glAdminUnit);
            }
            if (invInventoryDTO.getCostCenterId() != null) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(invInventoryDTO.getCostCenterId().getId());
                costCenter.setName(invInventoryDTO.getCostCenterId().getName());
                invInventory.setCostCenterId(costCenter);
            }
            if (invInventoryDTO.getAccountId() != null) {
                GlAccount account = new GlAccount();
                account.setId(invInventoryDTO.getAccountId().getId());
                account.setName(invInventoryDTO.getAccountId().getName());
                invInventory.setAccountId(account);
            }

            if (invInventoryDTO.getCreatedBy() != null) {
                TobyUser user = new TobyUser();
                user.setId(invInventoryDTO.getCreatedBy());
                invInventory.setCreatedBy(user);
                invInventory.setCreationDate(invInventoryDTO.getCreatedDate());
                invInventory.setModifiedBy(tobyUser);
                invInventory.setModificationDate(new Date());
                if (invInventoryDTO.getCompanyId() != null) {
                    TobyCompany company = new TobyCompany();
                    company.setId(invInventoryDTO.getCompanyId());
                    invInventory.setCompanyId(company);
                }

                if (invInventoryDTO.getBranchId() != null) {
                    Branch branch = new Branch();
                    branch.setId(invInventoryDTO.getBranchId());
                    invInventory.setBranchId(branch);
                }
            } else {
                invInventory.setCreatedBy(tobyUser);
                invInventory.setCreationDate(new Date());
                invInventory.setCompanyId(tobyUser.getCompanyId());
                invInventory.setBranchId(tobyUser.getBranchId());
            }

            return invInventory;
        } catch (Exception e) {
            invInventoryDTO.setMsg(e.getMessage());
        }
        return null;
    }

    public InvInventoryDTO mapToDTO(InvInventory invInventory, TobyUser tobyUser) {

        InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
        invInventoryDTO.setCreatedBy(invInventory.getCreatedBy() != null ? invInventory.getCreatedBy().getId() : null);
        invInventoryDTO.setCreatedDate(invInventory.getCreationDate());
        invInventoryDTO.setBranchId(invInventory.getBranchId() != null ? invInventory.getBranchId().getId() : null);
        invInventoryDTO.setId(invInventory.getId());
        invInventoryDTO.setCode(invInventory.getCode());
        invInventoryDTO.setIndex(invInventory.getId());
        invInventoryDTO.setInvType(invInventory.getInvType());
        invInventoryDTO.setCompanyId(invInventory.getCompanyId() != null ? invInventory.getCompanyId().getId() : null);
        invInventoryDTO.setDaribaValue(invInventory.getDaribaValue());
        invInventoryDTO.setSegilValue(invInventory.getSegilValue());

        invInventoryDTO.setTypeItem(invInventory.getTypeItem());
        invInventoryDTO.setName(invInventory.getName());
        invInventoryDTO.setMsg(invInventory.getMsg());
        if (invInventory.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(invInventory.getAccountId().getId());
            accountDTO.setName(invInventory.getAccountId().getName());
            invInventoryDTO.setAccountId(accountDTO);
        }
        if (invInventory.getAdminUnitId() != null) {
            GlAdminUnitDTO glAdminUnit = new GlAdminUnitDTO();
            glAdminUnit.setId(invInventory.getAdminUnitId().getId());
            glAdminUnit.setName(invInventory.getAdminUnitId().getName());
            glAdminUnit.setCode(invInventory.getAdminUnitId().getCode());
            invInventoryDTO.setGlAdminUnitId(glAdminUnit);
        }
        if (invInventory.getCostCenterId() != null) {
            CostCenterDTO costCenter = new CostCenterDTO();
            costCenter.setId(invInventory.getCostCenterId().getId());
            costCenter.setName(invInventory.getCostCenterId().getName());
            costCenter.setCode(invInventory.getCostCenterId().getCode());
            invInventoryDTO.setCostCenterId(costCenter);
        }
        return invInventoryDTO;
    }

    public List<InvInventoryDTO> mapToDTOList(List<InvInventory> invInventoryList, TobyUser tobyUser) {
        List<InvInventoryDTO> invInventoryDTOList = new ArrayList<>();
        for (InvInventory inventoryDelegator : invInventoryList) {
            invInventoryDTOList.add(mapToDTO(inventoryDelegator, tobyUser));
        }
        return invInventoryDTOList;
    }

    @Override
    public List<InvInventoryDTO> findInventoryDTOListBYBranshID(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvInventory> detailses = dao.findListByQuery("SELECT i FROM InvInventory i WHERE i.branchId.id = :branchId", params);
        return mapToDTOList(detailses, tobyUser);

    }

    @Override
    public List<InvInventory> getALLInventoriesByBranchPer(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", id);
        List<InvInventory> invInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId  ORDER BY i.code", params);
        List<InvInventory> list = new ArrayList<>();
        for (InvInventory invInventory : invInventories) {
            InvInventory inventory = new InvInventory();
            inventory.setId(invInventory.getId());
            inventory.setName(invInventory.getName());
            inventory.setCode(invInventory.getCode());
            list.add(inventory);
        }
        return list;
    }

    @Override
    public List<InvInventory> getALLInventoriesByBranch(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", id);
        List<InvInventory> InvInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId  ORDER BY i.id desc", params);
        return InvInventories;
    }

    @Override
    public List<InvInventory> getALLInventoriesByBranchByType(Integer branchId, Integer invType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("invType", invType);
        List<InvInventory> InvInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId AND i.invType = :invType  ORDER BY i.code", params);
        return InvInventories;
    }

    @Override
    public List<InvInventory> getALLInventoriesByType(Integer invType) {
        Map<String, Object> params = new HashMap<>();
        params.put("invType", invType);
        List<InvInventory> InvInventories = dao.findListByQuery("SELECT i FROM InvInventory i where i.invType = :invType  ORDER BY i.code", params);
        return InvInventories;
    }

    @Override
    public List<InvInventoryDTO> getInventoryByInvType(TobyUser tobyUser, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("type", type);
        List<InvInventory> invInventorys = dao.findListByQuery("SELECT i FROM InvInventory i WHERE i.branchId.id = :branchId and i.invType= :type", params);
        return mapToDTOList(invInventorys, tobyUser);
    }

    @Override
    public void deleteNewInventory(InvInventory invInventory) {
        dao.deleteEntity(invInventory);
    }

    @Override
    public InvInventory updateNewInventory(InvInventory invInventory, TobyUser user, TobyCompany company, Branch branch) {
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
    public InvInventoryDTO findInventory(Integer id, TobyUser tobyUser) {
        InvInventory invInventory = dao.findEntityById(InvInventory.class, id);
        return mapToDTO(invInventory, tobyUser);
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

    @Override
    public List<InvInventoryDTO> getALLInventorieDTOListByBranch(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvInventory> invInventoryList = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId AND i.invType = 0 ORDER BY i.code", params);
        return mapToDTOList(invInventoryList, tobyUser);
    }

//    @Override
//    public List<InvInventoryDTO> findInventory(TobyUser tobyUser, String code) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", tobyUser.getBranchId().getId());
//        params.put("code", code);
//        List<InvInventory> invInventoryList = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.code = :code  ORDER BY i.code", params);
//        return mapToDTOList(invInventoryList, tobyUser);
//    }
    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer inventoryId, String code) {
        StringBuilder quarry = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);
        quarry.append("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.code = :code  ");
        if (inventoryId != null) {
            params.put("inventoryId", inventoryId);
            quarry.append(" and i.id != :inventoryId");
        }
        List<InvInventory> invInventoryList = dao.findListByQuery(quarry.toString(), params);
        if (invInventoryList == null || invInventoryList.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer inventoryId, String name) {

        StringBuilder quarry = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("name", name);
        quarry.append("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.name = :name  ");
        if (inventoryId != null) {
            params.put("inventoryId", inventoryId);
            quarry.append(" and i.id != :inventoryId");

        }
        List<InvInventory> invInventoryList = dao.findListByQuery(quarry.toString(), params);
        if (invInventoryList == null || invInventoryList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }

    @Override
    public List<InvInventoryDTO> findInventoryDTOByBranchAndCode(TobyUser tobyUser, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);

        List<InvInventory> invInventoryList = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId and i.code = :code ", params);
        return mapToDTOList(invInventoryList, tobyUser);
    }

    @Override
    public InvInventoryDTO updateNewInventoryDTO(InvInventoryDTO invInventoryDTO, TobyUser user) {
        try {
            invInventoryDTO.setMsg(null);
            String error = genralValidate(user, invInventoryDTO);
            if (error.isEmpty()) {
                InvInventory invInventory = mapToEntity(invInventoryDTO, user);
                invInventory = dao.updateEntity(invInventory);
                invInventoryDTO = mapToDTO(invInventory, user);
            } else {
                invInventoryDTO.setMsg(error);
            }
            return invInventoryDTO;
        } catch (Exception e) {
        }
        return invInventoryDTO;
    }

    public String genralValidate(TobyUser tobyUser, InvInventoryDTO invInventoryDTO) {
        StringBuilder errorMessage = new StringBuilder();
        if (!validateName(tobyUser, invInventoryDTO.getId(), invInventoryDTO.getName())) {
            errorMessage.append("الاسم موجود");

        }
        if (!validateCode(tobyUser, invInventoryDTO.getId(), invInventoryDTO.getCode())) {
            errorMessage.append("الكود موجود");
        }

        return errorMessage.toString();
    }
}
