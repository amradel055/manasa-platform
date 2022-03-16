package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.BalanceAccountMonthlySearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.BalanceAccountView;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class BalanceAccountViewServiceImp implements BalanceAccountViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<BalanceAccountView> getAllBalanceAccountViewList(int accountId, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        String query = "Select bal from BalanceAccountView bal where bal.accountId = :accountId";
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(query);
        appendCostCenterFrom(queryBuilder, balanceAccountMonthlySearchBean);
        appendCostCenterTo(queryBuilder, balanceAccountMonthlySearchBean);
        appendAdminUnitFrom(queryBuilder, balanceAccountMonthlySearchBean);
        appendAdminUnitTo(queryBuilder, balanceAccountMonthlySearchBean);
        return dao.findListByQuery(queryBuilder.toString(), params);
    }

    @Override
    public BalanceAccountView getBalanceAccountViewList(int accountId, Date startDate, Date endDate) {
        Map<String, Object> params = new HashMap<>();
        String start = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String end = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
        params.put("accountId", accountId);
        params.put("startDate", start);
        params.put("endDate", end);
        return dao.findEntityByQuery("Select bal from BalanceAccountView where accountId = :accountId and"
                + " generalDate >= :startDate and generalDate <= :endDate", params);
    }

    @Override
    public List<BalanceAccountView> getBalanceAccountViewFilteredList(BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        Map<String, Object> params = new HashMap();
        StringBuilder queryBuilder = new StringBuilder();
        appendAccountNumberFrom(queryBuilder, balanceAccountMonthlySearchBean);
        appendAccountNumberTo(queryBuilder, balanceAccountMonthlySearchBean);

        String query = "SELECT bal FROM BalanceAccountView bal WHERE bal.rowNum != null " + queryBuilder.toString();
        queryBuilder.append(" ORDER BY bal.accountId ASC");
        return dao.findListByQuery(query, params);
    }

    private void appendAccountNumberFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAccountNumFrom() != null && balanceAccountMonthlySearchBean.getAccountNumFrom() > 0) {
            queryBuilder.append(" and bal.accNumber >= ").append(balanceAccountMonthlySearchBean.getAccountNumFrom());
        }
    }

    private void appendAccountNumberTo(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAccountNumTo() != null && balanceAccountMonthlySearchBean.getAccountNumTo() > 0) {
            queryBuilder.append(" and bal.accNumber <= ").append(balanceAccountMonthlySearchBean.getAccountNumTo());
        }
    }

    private void appendCostCenterFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getCostCenterForm() != null && balanceAccountMonthlySearchBean.getCostCenterForm() > 0) {
            queryBuilder.append(" and bal.costCenter >= ").append(balanceAccountMonthlySearchBean.getCostCenterForm());
        }
    }

    private void appendCostCenterTo(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getCostCenterTo() != null && balanceAccountMonthlySearchBean.getCostCenterTo() > 0) {
            queryBuilder.append(" and bal.costCenter <= ").append(balanceAccountMonthlySearchBean.getCostCenterTo());
        }
    }

    private void appendAdminUnitFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAdminUnitForm() != null && balanceAccountMonthlySearchBean.getAdminUnitForm() > 0) {
            queryBuilder.append(" and bal.adminUnit >= ").append(balanceAccountMonthlySearchBean.getAdminUnitForm());
        }
    }

    private void appendAdminUnitTo(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAdminUnitTo() != null && balanceAccountMonthlySearchBean.getAdminUnitTo() > 0) {
            queryBuilder.append(" and bal.adminUnit <= ").append(balanceAccountMonthlySearchBean.getAdminUnitTo());
        }
    }
}
