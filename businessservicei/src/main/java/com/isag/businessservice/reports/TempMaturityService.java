/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.procedure.TempMaturity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface TempMaturityService {

    public List<TempMaturity> getTempMaturitySearchBean(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters,Boolean search);

    public List<TempMaturity> getTempMaturityItemSearchBean(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters, Boolean search);
    
    public Integer getCount();
    
    public BigDecimal getTotalValue();

    public List<TempMaturity> getreportList(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);
}
