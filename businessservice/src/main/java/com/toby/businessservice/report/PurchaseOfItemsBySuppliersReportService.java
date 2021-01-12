/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.toby.views.ItemSalesView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface PurchaseOfItemsBySuppliersReportService {

    public List<ItemSalesView> getAllPurchaseOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean);
    public List<ItemSalesView> getAllSalesOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean);
    public List<ItemSalesView> getAllInvoiceDetailBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean);

}
