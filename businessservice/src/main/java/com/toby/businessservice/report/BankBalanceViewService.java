/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.API.CashAccountSettlementAPI;
import com.toby.bean.BankBalanceReportBean;
import com.toby.bean.BankBalanceReportSearchBean;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.views.BankBalanceView;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface BankBalanceViewService {

    public List<BankBalanceView> getAllBankBalancesTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer bankIdFrom, Integer bankIdTo);

    public List<BankBalanceView> getAllBankBalancesTransactionsByBankId(Integer branchId, Date dateTo, StringBuilder builder, Date dateFrom);

    public List<BankBalanceReportBean> search(BankBalanceReportSearchBean bankBalanceReportSearchBean);

    public List<BankBalanceReportBean> searchNative(BankBalanceReportSearchBean bankBalanceReportSearchBean);

    public List<BankBalanceReportBean> searchNativeNew( SubAccountSummarySearchBean cashAccountSettlementSearchBean);
    
}
