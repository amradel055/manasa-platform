/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.views.GlTransactionBankListView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface GlTransactionBankListViewService {
    
     public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer branchId, Integer transactionType);
     
     public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
     
      public Long getTotalRegistors(Integer transactionType, Integer branchId, Map<String, Object> filters);

}
