/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.PurchaseItemsGroupViewSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.PurchaseItemsGroupView;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class PurchaseItemsGroupViewServiceImpl implements PurchaseItemsGroupViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<PurchaseItemsGroupView> findAllPurchaseItemsGroup(PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", purchaseItemsGroupViewSearchBean.getBranchId());
        params.put("type", purchaseItemsGroupViewSearchBean.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT SIGV FROM PurchaseItemsGroupView SIGV WHERE SIGV.branchId = :branchId AND SIGV.type = :type ");
        appendInventoryForQueryBuilder(stringBuilder, params, purchaseItemsGroupViewSearchBean);
        appendInvoceForQueryBuilder(stringBuilder, params, purchaseItemsGroupViewSearchBean);
        appendItemForQueryBuilder(stringBuilder, params, purchaseItemsGroupViewSearchBean);
        appendDateForQueryBuilder(stringBuilder, params, purchaseItemsGroupViewSearchBean);

        List<PurchaseItemsGroupView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendInventoryForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        if (purchaseItemsGroupViewSearchBean.getInventoryIdFrom() != null && purchaseItemsGroupViewSearchBean.getInventoryIdFrom().getId() > 0) {
            params.put("invInventoryIdFrom", purchaseItemsGroupViewSearchBean.getInventoryIdFrom().getId());
            queryBuilder.append(" and SIGV.invInventoryId >= :invInventoryIdFrom ");
        }
        if (purchaseItemsGroupViewSearchBean.getInventoryIdTo() != null && purchaseItemsGroupViewSearchBean.getInventoryIdTo().getId() > 0) {
            params.put("invInventoryIdTo", purchaseItemsGroupViewSearchBean.getInventoryIdTo().getId());
            queryBuilder.append(" and SIGV.invInventoryId <= :invInventoryIdTo");
        }
    }

    private void appendInvoceForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        if (purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom() != null) {
            params.put("invoiceNumberFrom", purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom().getSerial());
            queryBuilder.append(" and SIGV.invoiceNumber >= :invoiceNumberFrom ");
        }
        if (purchaseItemsGroupViewSearchBean.getSalesInvoiceTo() != null) {
            params.put("invoiceNumberTo", purchaseItemsGroupViewSearchBean.getSalesInvoiceTo().getSerial());
            queryBuilder.append(" and SIGV.invoiceNumber <= :invoiceNumberTo");
        }
    }

    private void appendItemForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        if (purchaseItemsGroupViewSearchBean.getInvItemForm() != null && purchaseItemsGroupViewSearchBean.getInvItemForm().getCode() != null) {
            params.put("itemCodeFrom", purchaseItemsGroupViewSearchBean.getInvItemForm().getCode());
            queryBuilder.append(" and SIGV.itemNumber >= :itemCodeFrom ");
        }
        if (purchaseItemsGroupViewSearchBean.getInvItemTo() != null && purchaseItemsGroupViewSearchBean.getInvItemTo().getCode() != null) {
            params.put("itemCodeTo", purchaseItemsGroupViewSearchBean.getInvItemTo().getCode());
            queryBuilder.append(" and SIGV.itemNumber <= :itemCodeTo");
        }
    }

    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        if (purchaseItemsGroupViewSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(purchaseItemsGroupViewSearchBean.getDateFrom());
            queryBuilder.append(" and SIGV.date >= '").append(formatDateFrom).append("'");
        }

        if (purchaseItemsGroupViewSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(purchaseItemsGroupViewSearchBean.getDateTo());
            queryBuilder.append(" and SIGV.date <= '").append(formatDateTo).append("'");
        }
    }

}
