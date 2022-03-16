/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class ItemInStoreViewServiceImpl implements ItemInStoreViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<String> findItemsInStoreByBranchIdAndInventoryId(Integer branchId, Integer inventoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("inventoryId", inventoryId);
        return dao.findListByQuery("SELECT distinct(e.itemBarcode) FROM ItemInStoreView e WHERE e.branchId = :branchId and e.inventoryId = :inventoryId ", params);
    }

}
