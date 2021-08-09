/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.isag.businessservice.reports.searchBean.InvStripReportViewBean;
import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.entity.GlYear;
import com.isag.views.ItemsDataView;
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
    
    public List<ItemsDataView> getAllItemsWithInventories(ItemCardReportSerachBean itemCardReportSerachBean);

}
