/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.businessservice.reports.searchBean.InvStripReportViewBean;
import com.toby.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.toby.entity.GlYear;
import com.toby.views.ItemsDataView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface InventoryStripReportService {

    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);
    
    public List<ItemsDataView> getAllInvSupplierStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);

    public List<InvStripReportViewBean> getAllInvStripByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);

    public List<ItemsDataView> getAllItemsByItemCardReportSerachBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear);
}
