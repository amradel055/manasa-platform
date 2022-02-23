/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.entity.GlYear;
import com.isag.views.ItemsDataView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author elsakr6
 */
@Remote
public interface ItemsDataViewService {

    public List<ItemsDataView> findItemsDataViewByBranchIdAndItemIdAndInventoryId(Integer branchId, Integer itemId);

    public List<ItemsDataView> findItemsByBranchId(Integer branchId);

    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear);

    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);
}
