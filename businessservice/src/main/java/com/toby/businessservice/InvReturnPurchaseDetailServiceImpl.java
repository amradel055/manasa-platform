/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvReturnPurchaseDetail;
import java.math.BigDecimal;
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
public class InvReturnPurchaseDetailServiceImpl implements InvReturnPurchaseDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addReturnPurchaseDetail(InvReturnPurchaseDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteReturnPurchaseDetail(InvReturnPurchaseDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvReturnPurchaseDetail updateReturnPurchaseDetail(InvReturnPurchaseDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public InvReturnPurchaseDetail findInvReturnPurchaseDetailListById(Integer invReturnPurchaseDetailId) {
        return dao.findEntityById(InvReturnPurchaseDetail.class, invReturnPurchaseDetailId);
    }

    @Override
    public List<InvReturnPurchaseDetail> getAllReturnPurchaseDetailsByReturnPurchaseId(Integer invReturnPurchaseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invReturnPurchaseId", invReturnPurchaseId);
        List<InvReturnPurchaseDetail> details = dao.findListByQuery("SELECT e FROM InvReturnPurchaseDetail e WHERE e.invReturnPurchaseId.id = :invReturnPurchaseId", params);
        return details;
    }

    @Override
    public BigDecimal getQuantitySummitionForReturnPurchaseByPurchaseInvoiceDetail(Integer branchId, Integer purchaseInvoiceDetail) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("purchaseInvoiceDetail", purchaseInvoiceDetail);
        StringBuilder query = new StringBuilder();
        query.append("SELECT sum(e.quantity) FROM InvReturnPurchaseDetail e WHERE e.branchId.id =:branchId AND e.invInvoicePurchaseDetailId.id = :purchaseInvoiceDetail");

        return dao.findEntityByQuery(query.toString(), params);
    }
}
