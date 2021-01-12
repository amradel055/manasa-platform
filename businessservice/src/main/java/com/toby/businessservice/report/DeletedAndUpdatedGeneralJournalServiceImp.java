/**
 * 
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.DeletedAndUpdatedSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.DeletedAndUpdatedGeneralJournal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class DeletedAndUpdatedGeneralJournalServiceImp implements DeletedAndUpdatedGeneralJournalService {

	@EJB
	private GenericDAO dao;

	@Override
	public List<DeletedAndUpdatedGeneralJournal> findAllDeletedAndUpdateGeneralJournals(
			DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean, Integer branchId) {
		Map<String, Object> params = new HashMap<>();

		List<DeletedAndUpdatedGeneralJournal> deletedAndUpdatedGeneralJournal = new ArrayList<DeletedAndUpdatedGeneralJournal>();
		params.put("branchId", branchId);

		StringBuilder sqlQuery = createSql(deletedAndUpdatedSearchBean);
		String sql = "SELECT dag FROM DeletedAndUpdatedGeneralJournal dag WHERE dag.branchId = :branchId " + sqlQuery;

		deletedAndUpdatedGeneralJournal = dao.findListByQuery(sql, params);

		System.out.println("deletedAndUpdatedGeneralJournal.size" + deletedAndUpdatedGeneralJournal.size());

		return deletedAndUpdatedGeneralJournal;
	}

	private StringBuilder createSql(DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		StringBuilder sql = new StringBuilder();

		addVoucherIdTo(sql, deletedAndUpdatedSearchBean);
		addVoucherIdFrom(sql, deletedAndUpdatedSearchBean);
		addAccountNumTo(sql, deletedAndUpdatedSearchBean);
		addAccountNumFrom(sql, deletedAndUpdatedSearchBean);
		addDateFrom(sql, deletedAndUpdatedSearchBean);
		addDateTo(sql, deletedAndUpdatedSearchBean);
		return sql;
	}

	private void addVoucherIdFrom(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getVoucherIdFrom() != null
				&& deletedAndUpdatedSearchBean.getVoucherIdFrom() > 0) {
			sql.append(" and dag.serial >= ").append(deletedAndUpdatedSearchBean.getVoucherIdFrom());
		}
	}

	private void addVoucherIdTo(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getVoucherIdTo() != null && deletedAndUpdatedSearchBean.getVoucherIdTo() > 0) {
			sql.append(" and dag.serial <= ").append(deletedAndUpdatedSearchBean.getVoucherIdTo());
		}
	}

	private void addDateTo(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getDateTo() != null) {

			String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(deletedAndUpdatedSearchBean.getDateTo());
			sql.append(" and dag.generalDateNew <= '").append(dateTo).append("'");
		}
	}

	private void addDateFrom(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getDateFrom() != null) {

			String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(deletedAndUpdatedSearchBean.getDateFrom());
			sql.append(" and dag.generalDate >= '").append(dateFrom).append("'");
		}
	}

	private void addAccountNumFrom(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getAccountNumberFrom() != null
				&& deletedAndUpdatedSearchBean.getAccountNumberFrom() > 0) {
			sql.append(" and dag.accountNumber >= ").append(deletedAndUpdatedSearchBean.getAccountNumberFrom());
		}
	}

	private void addAccountNumTo(StringBuilder sql, DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
		if (deletedAndUpdatedSearchBean.getAccountNumberTo() != null
				&& deletedAndUpdatedSearchBean.getAccountNumberTo() > 0) {
			sql.append(" and dag.accountNumberNew <= ").append(deletedAndUpdatedSearchBean.getAccountNumberTo());
		}
	}
}