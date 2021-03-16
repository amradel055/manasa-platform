/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GlBankTransactionDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GlBankTransactionService {

    public GlBankTransaction addGlBankTransaction(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted,Integer companyId);

    public GlBankTransaction addGlBankTransactionForInstallment(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted);

    public void deleteGlBankTransaction(GlBankTransaction glBankTransaction);
    
    public void deleteGlBankTransactionByInvoiceId(Integer invoiceId) ;

    public List<GlBankTransaction> getALLGlBankTransactionByCompanyId(Integer companyId);

    public List<GlBankTransaction> getALLGlBankTransactionSettlmentByBranchId(Integer branchId);

    public List<GlBankTransaction> getALLGlBankTransactionRecievableByBranchId(Integer branchId, Integer transactionType);

    public List<GlBankTransaction> getALLGlBankTransactionRecievableNotloadByBranchId(Integer branchId, Integer transactionType);

    public GlBankTransaction findGlBankTransactionById(Integer glBankTransactionId);

    public GlBankTransaction addOneGlBankTransaction(GlBankTransaction glBankTransaction, GlBankTransactionDetail bankTransactionDetail);

    public Integer getMaxSerail(GlBankTransaction glBankTransaction);

    public GlBankTransaction updateGlBankTransaction(GlBankTransaction glBankTransaction);

    public Long getTotalRegistors(Integer transactionType, Integer branchId,Integer UserId, Map<String, Object> filters,Integer selectGlBankId);
    
    public Long getTotalRegistorsPre(Integer transactionType, Integer branchId, Map<String, Object> filters);

    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters,Integer selectGlBankId,Integer UserId);

    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableNotloadByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<GlBankTransaction> getALLGlBankTransactionByGeneralJournalId(Integer generalJournalId);
    
    public List<GlBankTransaction> getALLGlBankTransactionByInvoiceId(Integer invoiceId) ;
    
    public void addCash(InvInvoiceDTO dTO, TobyUser tobyUser);

}
