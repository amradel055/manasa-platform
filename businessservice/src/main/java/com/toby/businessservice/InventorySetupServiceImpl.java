/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InventorySetupDTO;
import com.toby.entity.InventorySetup;
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
public class InventorySetupServiceImpl implements InventorySetupService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addInventorySetup(InventorySetup inventorySetup) {
        dao.updateEntity(inventorySetup);
    }

    @Override
    public InventorySetup getInventoryByCompanyId(Integer companyId) {
        return dao.findEntityByCompany(InventorySetup.class, companyId);
    }

    @Override
    public InventorySetup getInventory() {
        // return dao.find(InventorySetup.class);
        InventorySetup inventorySetup;
        inventorySetup = dao.findEntityByQuery("SELECT e FROM  InventorySetup e WHERE e.companyId is null");
        return inventorySetup;
    }

    @Override
    public InventorySetup getInventoryByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InventorySetup> inventorySetups = dao.findListByQuery("SELECT e FROM InventorySetup e WHERE e.branchId.id = :branchId", params);

        return inventorySetups != null && !inventorySetups.isEmpty() ? inventorySetups.get(0) : null;
    }
    
    
    @Override
    public InventorySetupDTO getInventoryByBranchIdDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InventorySetup> inventorySetups = dao.findListByQuery("SELECT e FROM InventorySetup e WHERE e.branchId.id = :branchId", params);

        InventorySetupDTO dto =  mapToDTO(inventorySetups != null && !inventorySetups.isEmpty() ? inventorySetups.get(0) : null);
        return dto;
    }
    
    private InventorySetupDTO mapToDTO(InventorySetup entity){
        InventorySetupDTO dto = new InventorySetupDTO();
        dto.setDefaultSalesTax(entity.getDefaultSalesTax());
        dto.setDateSystem(entity.getDateSystem());
        dto.setId(entity.getId());
        dto.setIndex(entity.getId());
        dto.setInvSystem(entity.getInvSystem());
        dto.setItemCoding(entity.getItemCoding());
        dto.setNegativeSell(entity.getNegativeSell());
        dto.setReservationPeriod(entity.getReservationPeriod());
        dto.setSellAllow(entity.getSellAllow());
        dto.setSellBuy(entity.getSellBuy());
        dto.setTransfer(entity.getTransfer());
        dto.setBranchId(entity.getBranchId().getId());
        dto.setDeliveryPeriod(entity.getDeliveryPeriod());
//        dto.setTaxValue(entity.getTaxValue());
        return dto;
    }
    
    
}
