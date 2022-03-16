/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.views.CashAccountItemsView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface CashAccountItemsViewService {

    public List<CashAccountItemsView> findAllCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public List<CashAccountItemsView> findAllCashAccountSettlmentForLineCheck(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public List<CashAccountItemsView> findAllRemainingCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean, StringBuilder stringBuilder);
}
