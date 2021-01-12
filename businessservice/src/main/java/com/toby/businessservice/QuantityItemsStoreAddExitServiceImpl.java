/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.entity.QuantityItemsStoreAddExit;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
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
public class QuantityItemsStoreAddExitServiceImpl implements QuantityItemsStoreAddExitService {

    InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO = new InvInventoryTransactionDetailDTO();
    @EJB
    private GenericDAO dao;

    @Override
    public Map<Integer, BigDecimal> findInventoryDTOList(TobyUser tobyUser, Integer inventoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("inventoryId", inventoryId);
        params.put("branchId", tobyUser.getBranchId().getId());
        List<QuantityItemsStoreAddExit> detailses = dao.findListByQuery("SELECT i FROM QuantityItemsStoreAddExit i WHERE i.inventoryId = :inventoryId and i.branchId = :branchId", params);
        Map<Integer, BigDecimal> m = new HashMap<>();
        for (QuantityItemsStoreAddExit quantityItemsStoreAddExit : detailses) {
            m.put(quantityItemsStoreAddExit.getItemId(), quantityItemsStoreAddExit.getQty());
        }
        return m;
    }

    @Override
    public List<QuantityItemsStoreAddExit> findQuantityItemsStoreAddExitList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", tobyUser.getBranchId().getId());
        List<QuantityItemsStoreAddExit> detailses = dao.findListByQuery("SELECT i FROM QuantityItemsStoreAddExit i WHERE  i.branchId.id = :branchId", params);
        return detailses;
    }

    @Override
    public void checkservices() {
        if (invInventoryTransactionDetailDTO.getQuantity() != null && invInventoryTransactionDetailDTO.getQuantity().compareTo(BigDecimal.ZERO) > 0
                && invInventoryTransactionDetailDTO.getQuantity().compareTo(invInventoryTransactionDetailDTO.getAvailableQuantity()) <= 0) {

        }
    }

    @Override
    public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId) {
        List<BigDecimal> quantity = dao.executeNativeQuery("   SELECT   (SUM((IFNULL(i.qtyin, 0) * IFNULL(i.screwing, 1))) - SUM((IFNULL(i.qtyout, 0) * IFNULL(i.screwing, 1)))) AS qty\n" +
"                 FROM\n" +
"                     (toby.items_data_view i)\n" +
"                 WHERE\n" +
"                     ((i.ScreenCode NOT IN (1 , 2))\n" +
"                         AND (i.inventory_id="+invInventoryId+"))\n" +
"                         AND (i.item_id="+invItemId+")\n" +
"                 GROUP BY i.item_id  , i.inventory_id  ");
        return quantity.get(0);

    }
 
}
