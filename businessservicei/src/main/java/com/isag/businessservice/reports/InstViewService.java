/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.views.InstView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstViewService {

    public List<InstView> getInstViewBySearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<InstView> getInstViewBySearchBeanByLevel(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);
    
    public List<InstView> getInstViewBySearchBeanByLevelForItems(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<InstView> getInstViewTotals(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

    public List<InstView> getInstViewTotalsByLevel(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);
    
    public List<InstView> getInstViewTotalsByLevelForItems(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean);

}
