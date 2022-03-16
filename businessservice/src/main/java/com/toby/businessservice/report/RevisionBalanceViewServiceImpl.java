package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.RevisionBalanceSearchBean;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.RevisionBalanceView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class RevisionBalanceViewServiceImpl implements RevisionBalanceViewService {

	@EJB
	private GenericDAO dao;

	@Override
	public List<RevisionBalanceView> getRevisionBalanceViewList(RevisionBalanceSearchBean revisionBalanceSearchBean) {

		Map<String, Object> params = new HashMap<>();
		StringBuilder queryBuilder = new StringBuilder();
		appendAccountForQueryBuilder(queryBuilder, params, revisionBalanceSearchBean);
		appendAdminUnitForQueryBuilder(queryBuilder, params, revisionBalanceSearchBean);

		List<RevisionBalanceView> revisionBalanceViewList = dao
				.findListByQuery("SELECT ga FROM RevisionBalanceView ga " + queryBuilder, params);

		HashMap<String, RevisionBalanceView> revisionBalanceViewHashMap = new HashMap<>();
		for (RevisionBalanceView revisionBalanceView : revisionBalanceViewList) {
			if (revisionBalanceViewHashMap.containsKey(revisionBalanceView.getDate())) {
				RevisionBalanceView revisionBalanceView1 = revisionBalanceViewHashMap
						.get(revisionBalanceView.getDate());

				revisionBalanceView1.setDebitAmount(
						revisionBalanceView1.getDebitAmount().add(revisionBalanceView.getDebitAmount()));
				revisionBalanceView1.setCreditAmount(
						revisionBalanceView1.getCreditAmount().add(revisionBalanceView.getCreditAmount()));
				revisionBalanceViewHashMap.put(revisionBalanceView.getDate(), revisionBalanceView1);
			} else {
				revisionBalanceViewHashMap.put(revisionBalanceView.getDate(), revisionBalanceView);
			}
		}

		int count = 0;
		for (RevisionBalanceView revisionBalanceView : revisionBalanceViewHashMap.values()) {
			if (count == 0) {
				revisionBalanceView.setBalance(
						revisionBalanceView.getDebitAmount().subtract(revisionBalanceView.getCreditAmount()));
			} else {
				revisionBalanceView.setBalance(
						new ArrayList<>(revisionBalanceViewHashMap.values()).get(count - 1).getBalance().add(
								revisionBalanceView.getDebitAmount().subtract(revisionBalanceView.getCreditAmount())));
				count++;
			}
		}

		return new ArrayList<RevisionBalanceView>(revisionBalanceViewHashMap.values());
	}

	@Override
	public BigDecimal getBeforeBalance(SubAccountSummarySearchBean subAccountSummarySearchBean) {
		return null;
	}

	private void appendAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params,
			RevisionBalanceSearchBean revisionBalanceSearchBean) {
		if (revisionBalanceSearchBean.getAdminUnitId() != 0) {
			dao.checkAndQuery(queryBuilder);
			queryBuilder.append("ga.glAdminUnitId = :adminUnitId");
		}
	}

	private void appendAccountForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params,
			RevisionBalanceSearchBean revisionBalanceSearchBean) {
		if (revisionBalanceSearchBean.getAccountIdFrom() != 0 && revisionBalanceSearchBean.getAccountIdTo() != 0) {
			params.put("accountIdFrom", revisionBalanceSearchBean.getAccountIdFrom());
			params.put("accountIdTo", revisionBalanceSearchBean.getAccountIdTo());
			dao.checkAndQuery(queryBuilder);
			queryBuilder.append("ga.glAccountNumber between :accountIdFrom and :accountIdTo");
		}
	}

	@Override
	public List<RevisionBalanceView> getAllRevisionBalance() {
		return dao.findListByQuery("SELECT rvb FROM RevisionBalanceView rvb ORDER BY rvb.glaccountId ASC");
	}

	@Override
	public List<RevisionBalanceView> getRevisionBalanceByDate(Date dateFrom, Date dateTo) {
		Map<String, Object> params = new HashMap<>();
		params.put("dateFrom", dateFrom);
		params.put("dateTo", dateTo);
		System.out.println("Query is : "+ "SELECT rvb FROM RevisionBalanceView rvb WHERE rvb.date BETWEEN "+dateFrom+" AND "+dateTo+"");
		return dao.findListByQuery("SELECT rvb FROM RevisionBalanceView rvb WHERE rvb.date BETWEEN rvb.date = :dateFrom AND rvb.date = :dateTo ", params);
	}

	@Override
	public List<RevisionBalanceView> getRevisionBalanceByLevel(Integer levelFrom, Integer levelTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RevisionBalanceView> getRevisionBalanceByAdminUnitNameAndNumber(Integer adminUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RevisionBalanceView> getRevisionBalanceByAccountNumber(Integer accountNumber) {
		Map<String, Object> params = new HashMap<>();
		params.put("accountNumber", accountNumber);
		System.out.println("SELECT rvb FROM RevisionBalanceView rvb WHERE rvb.glaccountId= "+accountNumber+"");
		return dao.findListByQuery("SELECT rvb FROM RevisionBalanceView rvb WHERE rvb.glaccountId=:accountNumber", params);
	}

	public GenericDAO getDao() {
		return dao;
	}

	public void setDao(GenericDAO dao) {
		this.dao = dao;
	}
}
