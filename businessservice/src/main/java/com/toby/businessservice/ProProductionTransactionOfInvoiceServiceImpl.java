/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionTransactionOfInvoice;
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
public class ProProductionTransactionOfInvoiceServiceImpl implements ProProductionTransactionOfInvoiceService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ProProductionTransactionOfInvoice> getProProductionTransactionOfInvoiceListOfPourchais(Integer invPurchaseInvoiceDTOSerial, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invPurchaseInvoiceserial", invPurchaseInvoiceDTOSerial);
        List<ProProductionTransactionOfInvoice> details = dao.findListByQuery("SELECT e FROM ProProductionTransactionOfInvoice e where e.branchId=:branchId and e.invPurchaseInvoiceserial=:invPurchaseInvoiceserial", params);
        return details;
    }

    @Override
    public ProProductionTransactionOfInvoice getRenaimNumberListOfproduction(Integer invPurchaseInvoiceId, Integer clothNumber, Integer itemId, Integer proProductionStage, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
//        params.put("productionLineId", productionLineId);
        params.put("itemId", itemId);
        params.put("proProductionStage", proProductionStage);
//     params.put("productionStagesId", productionStagesId);
        params.put("clothNumber", clothNumber);
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = dao.findEntityByQuery("SELECT e FROM ProProductionTransactionOfInvoice e where  e.invPurchaseInvoiceId=:invPurchaseInvoiceId and e.productionStagesId =:proProductionStage and e.itemId=:itemId  and e.clothNumber=:clothNumber and e.branchId=:branchId  ", params);

        return proProductionTransactionOfInvoice;
    }

}
