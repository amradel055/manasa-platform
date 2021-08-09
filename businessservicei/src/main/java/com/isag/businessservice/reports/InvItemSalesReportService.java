/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.isag.views.ItemSalesView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface InvItemSalesReportService {

    public List<ItemSalesView> getAllInvPurchaseInvoiceDetailsByInvItemSalesSearchBean(InvItemSalesSearchBean invItemSalesSearchBean);

    public List<ItemSalesView> getAllInvoiceDetailByInvItemSalesSearchBean(InvItemSalesSearchBean invItemSalesSearchBean);
}
