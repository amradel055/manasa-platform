/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.entity.GlBankTransaction;
import com.isag.views.CollectionPaymentView;
import com.isag.views.CollectionPaymentTotalsTemp;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface CollectionAndPaymentServiceReport {

    public List<CollectionPaymentView> getAllCollectionPaymentReportSearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<GlBankTransaction> getAllTransactions(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<CollectionPaymentView> getCollectionPaymentTotals(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<GlBankTransactionDTO> getAllTransactionsLazyDTO(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters);

    public Long getTotalRegistors(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters);

    public List<GlBankTransactionDTO> getAllTransactionsDTO(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<CollectionPaymentView> getCollectionPaymentTotalsLazey(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<CollectionPaymentTotalsTemp> getAllCollectionPaymentReport(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters,Boolean search);

    public Integer getTotalRegistorsTemp();

    public BigDecimal getTotalAmountTemp();

    public BigDecimal getTotalRemainTemp();

    public BigDecimal getTotalPayTemp();
    
    public List<CollectionPaymentTotalsTemp> getAllCollectionPaymentReport(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);
}
