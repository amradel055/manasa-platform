package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.MonthlyAccountTransactionBean;
import com.isag.businessservice.reports.searchBean.MonthlyAccountTransactionSearchBean;
import com.isag.entity.GlAccount;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MonthlyAccountTransactionViewService {

    public List<MonthlyAccountTransactionBean> getMonthlyAccountTransactionViewList(MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean , List<GlAccount> glAccountForSummaryList);
}
