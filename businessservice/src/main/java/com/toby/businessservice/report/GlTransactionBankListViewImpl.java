/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.core.GenericDAO;
import com.toby.views.GlTransactionBankListView;
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
public class GlTransactionBankListViewImpl implements GlTransactionBankListViewService{

    @EJB
    private GenericDAO dao;
    
    @Override
    public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer branchId, Integer transactionType) {
         Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);
        return dao.findListByQuery("SELECT e FROM GlTransactionBankListView e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC ", params);
    }

    @Override
    public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlTransactionBankListView s WHERE s.branchId= :branchId AND s.transactionType =:transactionType  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<GlTransactionBankListView> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return details;
    }
    
    
      public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.date  like CONCAT('%',:globalFilter ,'%') OR s.bankFromName  like CONCAT('%',:globalFilter ,'%') OR s.bankToName like CONCAT('%',:globalFilter ,'%')  OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.remark like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public Long getTotalRegistors(Integer transactionType, Integer branchId, Map<String, Object> filters) {
         StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT count(s) FROM GlTransactionBankListView s WHERE s.branchId = :branchId AND s.transactionType =:transactionType ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }
      
}
