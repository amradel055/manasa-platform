/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.views.NetView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author elsakr6
 */
@Stateless
public class InvoiceNetServiceImpl implements InvoiceNetService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<NetView> getInvPurchaseInvoiceFromViewByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        return dao.findListByQuery("SELECT e FROM NetView e WHERE e.branchId = :branchId AND e.type = :type order by e.headId desc", params);
    }

    @Override
    public NetView getInvPurchaseInvoiceFromViewByInvoiceId(Integer branchId, Boolean type, Integer invoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("invoiceId", invoiceId);
        List<NetView> netViews = dao.findListByQuery("SELECT e FROM NetView e WHERE e.branchId = :branchId AND e.type = :type AND e.invoiceId = :invoiceId order by e.headId desc", params);
        if (netViews != null && !netViews.isEmpty()) {
            return netViews.get(0);
        }
        return null;
    }

    @Override
    public List<NetView> getInvoiceFromViewByBranchIdandInventory(Integer branchId, Integer type, Integer invId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("inventoryId", invId);
        return dao.findListByQuery("SELECT e FROM NetView e WHERE e.branchId = :branchId AND e.type = :type AND e.inventoryId= :inventoryId order by e.headId desc", params);
    }

}
