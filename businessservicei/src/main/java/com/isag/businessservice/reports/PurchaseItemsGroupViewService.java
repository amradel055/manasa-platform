/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.PurchaseItemsGroupViewSearchBean;
import com.isag.entity.PurchaseItemsGroupView;
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
