/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.entity.TobyUser;
import com.toby.views.QuantityItemsStore;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author anady
 */
@Remote
public interface QuantityItemsStoreService {

    public List<QuantityItemsStore> findInventoryItemsList(Integer inventoryId, Integer branchId, Boolean sellBuy);

    public QuantityItemsStore findInventoryItem(Integer inventoryId, Integer branchId, Integer itemId, Boolean sellBuy);

    public List<QuantityItemsStore> findItemsForBranchList(Integer itemId, Integer branchId, Boolean sellBuy);

    public List<QuantityItemsStore> findAllQuantityItemsListStoreByBranchId(Integer branchId, Boolean sellBuy);
    
        public List<QuantityItemsStoreDTO> findInventoryItemsListDTO(Integer inventoryId, TobyUser tobyUser, Boolean sellBuy);
}
