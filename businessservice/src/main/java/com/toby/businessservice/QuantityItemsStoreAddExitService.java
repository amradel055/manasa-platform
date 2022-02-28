/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.InvInventory;
import com.toby.entity.QuantityItemsStoreAddExit;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface QuantityItemsStoreAddExitService {

    public Map<Integer, BigDecimal> findInventoryDTOList(TobyUser tobyUser, Integer invInventoryId);

    public List<QuantityItemsStoreAddExit> findQuantityItemsStoreAddExitList(TobyUser tobyUser);

    public void checkservices();

     public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId);

}
