/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.PurchaseOfItemsByDelegatorSearchBean;
import com.toby.views.ItemSalesView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface PurchaseOfItemsByDelegatorReportService {

    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBean(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean);

    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBeanNew(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean);
    
    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBeanNewHead(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean);

}
