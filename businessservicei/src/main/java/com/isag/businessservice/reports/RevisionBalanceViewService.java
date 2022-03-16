package com.isag.businessservice.reports;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import com.isag.businessservice.reports.searchBean.RevisionBalanceSearchBean;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.views.RevisionBalanceView;

@Remote
public interface RevisionBalanceViewService {

	public List<RevisionBalanceView> getRevisionBalanceViewList(RevisionBalanceSearchBean revisionBalanceSearchBean);
	public BigDecimal getBeforeBalance(SubAccountSummarySearchBean subAccountSummarySearchBean);
	public List<RevisionBalanceView> getAllRevisionBalance();
	public List<RevisionBalanceView> getRevisionBalanceByDate(Date dateFrom, Date dateTo);
	public List<RevisionBalanceView> getRevisionBalanceByLevel(Integer levelFrom, Integer levelTo);
	public List<RevisionBalanceView> getRevisionBalanceByAdminUnitNameAndNumber(Integer adminUnit);
	public List<RevisionBalanceView> getRevisionBalanceByAccountNumber(Integer accountNumber);
	
}