/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.entity.GlYear;
import com.toby.views.CashAccountSettlmentView;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface CashAccountSettlmentService {

    public List<CashAccountSettlmentView> findAllCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public List<CashAccountSettlmentView> findAllCashAccountSettlment2(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public List<CashAccountSettlmentView> findAllCashAccountSettlmentForLineCheck(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public BigDecimal findOpeningBalancesForSpecificCashAccount(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type, GlYear glYear);

    public BigDecimal findOpeningBalancesForSpecificCashAccount2(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type, GlYear glYear);

    public List<CashAccountSettlmentView> findOpeningBalancesForSpecificCashAccountByGroups(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer type, GlYear glYear);

    public List<CashAccountSettlmentView> findAllRemainingCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean, StringBuilder stringBuilder);
}
