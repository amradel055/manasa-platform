/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.QuantityItemsStoreByDate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author anady
 */
@Remote
public interface QuantityItemsStoreByDateService {

    public List<QuantityItemsStoreByDate> findInventoryItemsList(Integer inventoryId, Integer branchId, Boolean sellBuy);

    public BigDecimal findInventoryItem(Integer inventoryId, Integer branchId, Integer itemId, Boolean sellBuy, Date date);

    public BigDecimal findInventoryItemByDate(Integer inventoryId, Integer branchId, Integer itemId, Date date);

    public List<QuantityItemsStoreByDate> findItemsForBranchList(Integer itemId, Integer branchId, Boolean sellBuy);

    public List<QuantityItemsStoreByDate> findAllQuantityItemsListStoreByBranchId(Integer branchId, Boolean sellBuy);

    public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId, Boolean sellBuy);

    public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId, Boolean sellBuy, Date date);
    
     public Map<Integer,BigDecimal> getAllQuantatyItemByinventory(Integer invInventoryId, String invItemId, Boolean sellBuy, Date date);
    
    public BigDecimal getQuantatyItemByinventoryRecalcCostAvarage(Integer invInventoryId, Integer invItemId, Boolean sellBuy, Date date);

}
