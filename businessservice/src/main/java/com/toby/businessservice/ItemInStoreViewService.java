/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface ItemInStoreViewService {

    public List<String> findItemsInStoreByBranchIdAndInventoryId(Integer branchId, Integer inventoryId);
}
