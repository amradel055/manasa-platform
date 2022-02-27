/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.InvDelegatorSalesByMainItemsGroupSearchBean;
import com.toby.entity.SalesDelegateView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InvDelegatorSalesByMainItemsGroupService {

    List<SalesDelegateView> findAllSalesDelegate(InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean);
}
