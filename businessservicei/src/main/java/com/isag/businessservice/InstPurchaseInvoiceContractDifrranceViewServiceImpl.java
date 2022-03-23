/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class InstPurchaseInvoiceContractDifrranceViewServiceImpl implements InstPurchaseInvoiceContractDifrranceViewService{
    @EJB
    private GenericDAO dao; 
      
    @Override
    public List<InstPurchaseInvoiceContractDifrranceView> getAllInstPurchaseInvoiceContractDifrrance(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.branchId = :branchId", params);
    }
    
    @Override
    public InstPurchaseInvoiceContractDifrranceView getAllInstPurchaseInvoiceContractDifrranceByType(Integer branchId,Integer id , Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("id", id);
        params.put("type", type);
        return dao.findEntityByQuery("SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.branchId = :branchId AND i.id = :id And i.tableType =:type", params);
    }
}
