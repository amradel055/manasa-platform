/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvDelegatorSalesByMainItemsGroupSearchBean;
import com.isag.entity.SalesDelegateView;
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
