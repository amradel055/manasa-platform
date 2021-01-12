/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.toby.dto.InvItemDTO;
import com.toby.entity.GlYear;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsDataView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author elsakr6
 */
@Remote
public interface ItemsDataViewService {

    public List<ItemsDataView> findItemsDataViewByBranchIdAndItemIdAndInventoryId(Integer branchId, Integer itemId);

    public InvItemDTO addItemsData(InvItemDTO invItemDTO, TobyUser tobyUser);

//    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear);
//    
//    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);
    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean);

    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear);
    
    public InvItemDTO findInvItem(Integer invItemById, TobyUser tobyUser);

}
