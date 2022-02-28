package com.toby.businessservice.report;

import com.toby.businessservice.reports.entityBean.MonthlyAccountTransactionBean;
import com.toby.businessservice.reports.searchBean.MonthlyAccountTransactionSearchBean;
import com.toby.entity.GlAccount;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MonthlyAccountTransactionViewService {

    public List<MonthlyAccountTransactionBean> getMonthlyAccountTransactionViewList(MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean , List<GlAccount> glAccountForSummaryList);
}
