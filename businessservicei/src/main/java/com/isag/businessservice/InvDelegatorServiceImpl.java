/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.entity.InventoryDelegator;
import java.util.ArrayList;
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
public class InvDelegatorServiceImpl implements InvDelegatorService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InventoryDelegator> getDelegatorsByBranch(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", id);
//        List<InventoryDelegator> InventoryDelegator = dao.findEntityByQuery("SELECT ivd FROM InventoryDelegator ivd ");
        List<InventoryDelegator> inventoryDelegators = dao.findListByQuery("SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId ORDER BY i.code", params);
//        return dao.findAll(InventoryDelegator.class);
        return inventoryDelegators;
    }

    @Override
    public InventoryDelegator findInventoryDelegator(Integer id) {
        return dao.findEntityById(InventoryDelegator.class, id);
    }

    @Override
    public void deleteInventoryDelegator(InventoryDelegator inventoryDelegator) {
        dao.deleteEntity(inventoryDelegator);
    }

    @Override
    public InventoryDelegator updateInventoryDelegator(InventoryDelegator inventoryDelegator) {

        List<InventoryDelegator> invList = new ArrayList<>();
        if (inventoryDelegator.getId() != null) {
            invList = findDelegator(inventoryDelegator.getBranchId().getId(), inventoryDelegator.getId(), inventoryDelegator.getCode(), inventoryDelegator.getType());
            if (invList != null && !invList.isEmpty()) {
                return null;
            }
        }

        if (inventoryDelegator.getId() == null) {
            invList = findDelegator(inventoryDelegator.getBranchId().getId(), null, inventoryDelegator.getCode(), inventoryDelegator.getType());
            if (invList != null && !invList.isEmpty()) {
                return null;
            }
        }

        return dao.updateEntity(inventoryDelegator);
    }

    @Override
    public InventoryDelegator addInventoryDelegator(InventoryDelegator inventoryDelegator) {
        dao.saveEntity(inventoryDelegator);
        return inventoryDelegator;
    }

    @Override
    public List<InventoryDelegator> getDelegatorsByBranchAndId(Integer id, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("id", id);

        List<InventoryDelegator> inventoryDelegators = dao.findListByQuery("SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.id = :id  ORDER BY i.code", params);
        return inventoryDelegators;
    }

    @Override
    public List<InventoryDelegator> getSalesByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = 1  ORDER BY i.code";
        List<InventoryDelegator> inventoryDelegatorList = dao.findListByQuery(sql, params);
        return inventoryDelegatorList;
    }

    @Override
    public List<InventoryDelegatorDTO> getSalesDTOByBranch(Integer branchId) {
        return returnListDTO(getSalesByBranch(branchId));
    }

    @Override
    public List<InventoryDelegator> getDelegatorByBranch(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = :type  ORDER BY i.code";
        List<InventoryDelegator> inventoryDelegatorList = dao.findListByQuery(sql, params);
        return inventoryDelegatorList;
    }

    @Override
    public List<InventoryDelegatorDTO> getDelegatorDTOByBranch(Integer branchId, Integer type) {
        return returnListDTO(getDelegatorByBranch(branchId, type));
    }

    @Override
    public InventoryDelegator getDelegatorDTOByBranchAndSerial(Integer branchId, Integer type, String code){
      Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
         params.put("code", code);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = :type  AND i.code = :code";
        InventoryDelegator inventoryDelegator = dao.findEntityByQuery(sql, params);
        return inventoryDelegator;
    }

    @Override
    public List<InventoryDelegator> getPurchaseByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = 0  ORDER BY i.code";
        List<InventoryDelegator> inventoryDelegatorList = dao.findListByQuery(sql, params);
        return inventoryDelegatorList;
    }

    @Override
    public List<InventoryDelegator> findDelegator(Integer branchId, Integer delegatorId, String code, Integer type) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        params.put("type", type);

        query.append("SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.code = :code AND i.type = :type ");

        if (delegatorId != null) {
            params.put("delegatorId", delegatorId);
            query.append(" AND i.id != :delegatorId ");
        }

        query.append(" ORDER BY i.code");

        return dao.findListByQuery(query.toString(), params);
    }

    private List<InventoryDelegatorDTO> returnListDTO(List<InventoryDelegator> list) {
        List<InventoryDelegatorDTO> dTOList = new ArrayList<>();
        for (InventoryDelegator entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    public InventoryDelegatorDTO mapTODTO(InventoryDelegator entity, Boolean needDetail) {
        InventoryDelegatorDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private InventoryDelegatorDTO initMapToDTO(InventoryDelegator entity) {

        InventoryDelegatorDTO dTO = new InventoryDelegatorDTO();
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setMobile(entity.getMobile());
        dTO.setImg(entity.getImg());
        dTO.setType(entity.getType());
        dTO.setAllowDiscountLimit(entity.getAllowDiscountLimit());
        dTO.setTargetSales(entity.getTargetSales());
        dTO.setCommission(entity.getCommission());

        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setModificationDate(entity.getModificationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setModifiedBy(entity.getModifiedBy() != null ? entity.getModifiedBy().getId() : null);
        dTO.setIndex(entity.getId());
        return dTO;
    }
}
