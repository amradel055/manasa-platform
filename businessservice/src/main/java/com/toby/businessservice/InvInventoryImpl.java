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
import com.toby.dto.GlBankTransactionDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.text.SimpleDateFormat;
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
    public InvInventory mapToEntity(InvInventoryDTO dto, TobyUser tobyUser) {
        try {
            InvInventory entity = new InvInventory();
            entity.setCode(dto.getCode());
            entity.setInvType(dto.getInvType());
            entity.setId(dto.getId());
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setTypeItem(dto.getTypeItem());
            entity.setDaribaValue(dto.getDaribaValue());
            entity.setSegilValue(dto.getSegilValue());
            entity.setPhone(dto.getPhone());
            entity.setInvName(dto.getInvName());
            entity.setSellerName(dto.getSellerName());

            if (dto.getGlAdminUnitId() != null) {
                GlAdminUnit glAdminUnit = new GlAdminUnit();
                glAdminUnit.setId(dto.getGlAdminUnitId().getId());
                glAdminUnit.setName(dto.getGlAdminUnitId().getName());
                glAdminUnit.setCode(dto.getGlAdminUnitId().getCode());
                entity.setAdminUnitId(glAdminUnit);
            }

            if (dto.getCostCenterId() != null) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(dto.getCostCenterId().getId());
                costCenter.setName(dto.getCostCenterId().getName());
                entity.setCostCenterId(costCenter);
            }

            if (dto.getAccountId() != null) {
                GlAccount account = new GlAccount();
                account.setId(dto.getAccountId().getId());
                entity.setAccountId(account);
            }

            if (dto.getRunningAccount() != null) {
                GlAccount account = new GlAccount();
                account.setId(dto.getRunningAccount().getId());
                entity.setRunningAccount(account);
            }

            if (dto.getCostAccount() != null) {
                GlAccount account = new GlAccount();
                account.setId(dto.getCostAccount().getId());
                entity.setCostAccount(account);
            }

            if (dto.getCustomerAccount() != null) {
                GlAccount account = new GlAccount();
                account.setId(dto.getCustomerAccount().getId());
                entity.setCustomerAccount(account);
            }

            if (dto.getCreatedBy() != null) {
                TobyUser user = new TobyUser();
                user.setId(dto.getCreatedBy());
                entity.setCreatedBy(user);
                entity.setCreationDate(dto.getCreatedDate());
                entity.setModifiedBy(tobyUser);
                entity.setModificationDate(new Date());
                if (dto.getCompanyId() != null) {
                    TobyCompany company = new TobyCompany();
                    company.setId(dto.getCompanyId());
                    entity.setCompanyId(company);
                }

                if (dto.getBranchId() != null) {
                    Branch branch = new Branch();
                    branch.setId(dto.getBranchId());
                    entity.setBranchId(branch);
                }
            } else {
                entity.setCreatedBy(tobyUser);
                entity.setCreationDate(new Date());
                entity.setCompanyId(tobyUser.getCompanyId());
                entity.setBranchId(tobyUser.getBranchId());
            }

            return entity;
        } catch (Exception e) {
            dto.setMsg(e.getMessage());
        }
        return null;
    }

    @Override
    public InvInventoryDTO mapToDTO(InvInventory entity) {

        InvInventoryDTO dto = new InvInventoryDTO();
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);

        maptoDTOSmall(dto, entity);

        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dto.setPhone(entity.getPhone());

        dto.setTypeItem(entity.getTypeItem());

        dto.setMsg(entity.getMsg());
        dto.setSellerName(entity.getSellerName());
        dto.setInvName(entity.getInvName());
        if (entity.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getAccountId().getId());
            accountDTO.setName(entity.getAccountId().getName());
            accountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            dto.setAccountId(accountDTO);
        }
        if (entity.getRunningAccount() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getRunningAccount().getId());
            accountDTO.setName(entity.getRunningAccount().getName());
            accountDTO.setAccNumber(entity.getRunningAccount().getAccNumber());
            dto.setRunningAccount(accountDTO);
        }
        if (entity.getCostAccount() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getCostAccount().getId());
            accountDTO.setName(entity.getCostAccount().getName());
            accountDTO.setAccNumber(entity.getCostAccount().getAccNumber());
            dto.setCostAccount(accountDTO);
        }
        if (entity.getCustomerAccount() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getCustomerAccount().getId());
            accountDTO.setName(entity.getCustomerAccount().getName());
            accountDTO.setAccNumber(entity.getCustomerAccount().getAccNumber());
            dto.setCustomerAccount(accountDTO);
        }
        if (entity.getAdminUnitId() != null) {
            GlAdminUnitDTO glAdminUnit = new GlAdminUnitDTO();
            glAdminUnit.setId(entity.getAdminUnitId().getId());
            glAdminUnit.setName(entity.getAdminUnitId().getName());
            glAdminUnit.setCode(entity.getAdminUnitId().getCode());
            dto.setGlAdminUnitId(glAdminUnit);
        }
        if (entity.getCostCenterId() != null) {
            CostCenterDTO costCenter = new CostCenterDTO();
            costCenter.setId(entity.getCostCenterId().getId());
            costCenter.setName(entity.getCostCenterId().getName());
            costCenter.setCode(entity.getCostCenterId().getCode());
            dto.setCostCenterId(costCenter);
        }
        return dto;
    }

    @Override
    public InvInventoryDTO maptoDTOSmall(InvInventoryDTO dto, InvInventory entity) {
        if (dto == null) {
            dto = new InvInventoryDTO();
        }
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setIndex(entity.getId());
        dto.setInvType(entity.getInvType());
        dto.setDaribaValue(entity.getDaribaValue());
        dto.setSegilValue(entity.getSegilValue());
        dto.setInvName(entity.getInvName());
        dto.setName(entity.getName());
        return dto;
    }

    public List<InvInventoryDTO> mapToDTOList(List<InvInventory> invInventoryList, TobyUser tobyUser) {
        List<InvInventoryDTO> invInventoryDTOList = new ArrayList<>();
        for (InvInventory inventoryDelegator : invInventoryList) {
            invInventoryDTOList.add(mapToDTO(inventoryDelegator));
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
    public InvInventoryDTO findInventory(Integer id) {
        InvInventory invInventory = dao.findEntityById(InvInventory.class, id);
        return mapToDTO(invInventory);
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
    public List<InvInventoryDTO> findInventoryByBranch(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvInventory> inventoryList = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId AND i.invType = 2 ", params);
        List<InvInventoryDTO> inventoryListdDTOList = mapToDTOList(inventoryList, tobyUser);
        return inventoryListdDTOList;

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
                invInventoryDTO = mapToDTO(invInventory);
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

    @Override
    public List<InvInventoryDTO> findInventoryByBranchIdDTO(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvInventory> inventoryList = dao.findListByQuery("SELECT i FROM InvInventory i where i.branchId.id = :branchId ", params);
        List<InvInventoryDTO> inventoryListdDTOList = mapToDTOList(inventoryList, tobyUser);
        return inventoryListdDTOList;

    }

}
