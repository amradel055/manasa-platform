/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.entity.GlYear;
import com.isag.views.CashAccountSettlementBean;
import com.isag.views.CashAccountSettlmentView;
import com.isag.views.SubAccountSummaryReport;
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

    public BigDecimal findOpeningBalancesForSpecificCashAccount(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type );

    public BigDecimal findOpeningBalancesForSpecificCashAccount2(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type );

    public List<CashAccountSettlmentView> findOpeningBalancesForSpecificCashAccountByGroups(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer type, GlYear glYear);

    public List<CashAccountSettlmentView> findAllRemainingCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean, StringBuilder stringBuilder);

    public BigDecimal findOpeningBalancesForDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type );

    public List<CashAccountSettlmentView> findAllCashAccountSettlmentWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean);

    public BigDecimal findOpeningBalancesForSpecificCashAccountWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type );

    public List<Object[]> findDateWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean);
    
    public CashAccountSettlementBean search(SubAccountSummarySearchBean cashAccountSettlementSearchBean );
    
    public CashAccountSettlementBean searchNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean);
}
