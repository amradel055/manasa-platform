/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InventoryDelegator;
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
public class InventoryDelegatorImpl implements InventoryDelegatorService{

    @EJB
    private GenericDAO dao;
    
    
    @Override
    public List<InventoryDelegatorDTO> findInvPurchaseInvoiceforDelegator(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId()); 
        List<InventoryDelegator> detailses = dao.findListByQuery("SELECT i FROM InventoryDelegator i WHERE i.branchId.id = :branchId and i.type=1", params);
        return mapToDTOList(detailses, null);
    }
    
    //mapToEntity
    public InventoryDelegator mapToEntity(InventoryDelegatorDTO inventoryDelegatorDTO, TobyUser tobyUser) {
        
        InventoryDelegator inventoryDelegator = new InventoryDelegator();
        inventoryDelegator.setCode(inventoryDelegatorDTO.getCode());
        inventoryDelegator.setCommission(inventoryDelegatorDTO.getCommission());
        inventoryDelegator.setId(inventoryDelegatorDTO.getId());
        inventoryDelegator.setMarkDisable(inventoryDelegatorDTO.getMarkDisable());
        inventoryDelegator.setMarkEdit(inventoryDelegatorDTO.getMarkEdit());
        inventoryDelegator.setMobile(inventoryDelegatorDTO.getMobile());
        inventoryDelegator.setName(inventoryDelegatorDTO.getName());
        inventoryDelegator.setTargetSales(inventoryDelegatorDTO.getTargetSales());
        inventoryDelegator.setType(inventoryDelegatorDTO.getType());
        if (inventoryDelegatorDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(inventoryDelegatorDTO.getCreatedBy());
            inventoryDelegator.setCreatedBy(user);
            inventoryDelegator.setCreationDate(inventoryDelegatorDTO.getCreatedDate());
            inventoryDelegator.setModifiedBy(tobyUser);
            inventoryDelegator.setModificationDate(new Date());
            if (inventoryDelegatorDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(inventoryDelegatorDTO.getCompanyId());
                inventoryDelegator.setCompanyId(company);
            }

            if (inventoryDelegatorDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(inventoryDelegatorDTO.getBranchId());
                inventoryDelegator.setBranchId(branch);
            }
        } else {
            inventoryDelegator.setCreatedBy(tobyUser);
            inventoryDelegator.setCreationDate(new Date());
            inventoryDelegator.setCompanyId(tobyUser.getCompanyId());
            inventoryDelegator.setBranchId(tobyUser.getBranchId());
        }

        return inventoryDelegator;
    }
    
    
    

    public InventoryDelegatorDTO mapToDTO(InventoryDelegator inventoryDelegator, TobyUser tobyUser) {
        
        InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
        inventoryDelegatorDTO.setCreatedBy(inventoryDelegator.getCreatedBy() !=null ? inventoryDelegator.getCreatedBy().getId() :null);
        inventoryDelegatorDTO.setCreatedDate(inventoryDelegator.getCreationDate());
        inventoryDelegatorDTO.setBranchId(inventoryDelegator.getBranchId() !=null ? inventoryDelegator.getBranchId().getId() :null);
        inventoryDelegatorDTO.setId(inventoryDelegator.getId());
        inventoryDelegatorDTO.setCompanyId(inventoryDelegator.getCompanyId() !=null ? inventoryDelegator.getCompanyId().getId() : null);
        inventoryDelegatorDTO.setCommission(inventoryDelegator.getCommission());
        inventoryDelegatorDTO.setMarkDisable(inventoryDelegator.getMarkDisable());
        inventoryDelegatorDTO.setMarkEdit(inventoryDelegator.getMarkEdit());
        inventoryDelegatorDTO.setMobile(inventoryDelegator.getMobile());
        inventoryDelegatorDTO.setName(inventoryDelegator.getName());
        inventoryDelegatorDTO.setTargetSales(inventoryDelegator.getTargetSales());
        inventoryDelegatorDTO.setType(inventoryDelegator.getType());
        inventoryDelegatorDTO.setCode(inventoryDelegator.getCode());
        return inventoryDelegatorDTO;
    }

    public List<InventoryDelegatorDTO> mapToDTOList(List<InventoryDelegator> inventoryDelegatorList, TobyUser tobyUser) {
        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
        for (InventoryDelegator inventoryDelegator : inventoryDelegatorList) {
            inventoryDelegatorDTOList.add(mapToDTO(inventoryDelegator, tobyUser));
        }
        return inventoryDelegatorDTOList;
    }

    
    
}
