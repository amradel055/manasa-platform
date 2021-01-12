/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.PurchaseItemsGroupViewSearchBean;
import com.toby.entity.PurchaseItemsGroupView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface PurchaseItemsGroupViewService {

    public List<PurchaseItemsGroupView> findAllPurchaseItemsGroup(PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean);

}
