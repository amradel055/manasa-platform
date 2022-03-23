package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.BalanceAccountMonthlySearchBean;
import com.isag.views.BalanceAccountView;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface BalanceAccountViewService {

    List<BalanceAccountView> getAllBalanceAccountViewList(int accountId , BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean);

    public BalanceAccountView getBalanceAccountViewList(int accountId, Date startDate, Date endDate);

    public List<BalanceAccountView> getBalanceAccountViewFilteredList(BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean);

}
