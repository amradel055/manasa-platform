/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvSalesInvoiceServiceImpl implements InvSalesInvoiceService {

    @EJB
    private GenericDAO dao;

    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @Override
    public InvPurchaseInvoice addInvSalesInvoice(InvPurchaseInvoice invSalesInvoice, List<InvPurchaseInvoiceDetail> invSalesInvoiceDetailsList, List<InvPurchaseInvoiceDetail> invSalesInvoiceDetailListDeleted) {
        if (invSalesInvoice.getId() != null) {
            dao.updateEntity(invSalesInvoice);
        } else {
            dao.saveEntity(invSalesInvoice);
        }

        if (invSalesInvoiceDetailListDeleted != null && !invSalesInvoiceDetailListDeleted.isEmpty()) {
            for (InvPurchaseInvoiceDetail deleted : invSalesInvoiceDetailListDeleted) {
                invPurchaseInvoiceDetailsService.deleteInvPurchaseInvoiceDetails(deleted);
            }
        }

        for (InvPurchaseInvoiceDetail invAddingOrderDetail : invSalesInvoiceDetailsList) {
            invAddingOrderDetail.setInvPurchaseInvoiceId(invSalesInvoice);
            if (invAddingOrderDetail.getId() != null) {
                invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(invAddingOrderDetail);
            } else {
                invPurchaseInvoiceDetailsService.addInvPurchaseInvoiceDetails(invAddingOrderDetail);
            }
        }
        return invSalesInvoice;
    }

    @Override
    public void deleteInvSalesInvoice(InvPurchaseInvoice invSalesInvoice) {
        if (invSalesInvoice.getId() != null && invSalesInvoice.getId() > 0) {
            dao.executeDeleteQuery("delete from InvPurchaseInvoiceDetail e WHERE e.id > 0 AND (e.type = true AND e.invPurchaseInvoiceId.id = )" + invSalesInvoice.getId());
        }
        dao.deleteEntity(invSalesInvoice);
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvSalesInvoiceByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvPurchaseInvoice.class, companyId);
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvSalesInvoiceByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = true", params);
        return invPurchaseInvoiceList;
    }

    @Override
    public InvPurchaseInvoice findInvSalesInvoiceById(Integer invSalesInvoiceId) {
        return dao.findEntityById(InvPurchaseInvoice.class, invSalesInvoiceId);
    }
}
