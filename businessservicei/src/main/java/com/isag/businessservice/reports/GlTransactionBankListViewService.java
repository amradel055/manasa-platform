/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.entity.GlBankTransactionDetail;
import com.isag.views.GlTransactionBankListView;
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
      
     public List<GlTransactionBankListView> getALLGlTransactionBankListViewLazy(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters,Integer selectGlBankToId,Integer userId);
     
     public Long getTotalRegistorslazy(Integer transactionType, Integer branchId,Integer userId , Map<String, Object> filters,Integer selectGlBankToId);
}
