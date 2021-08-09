/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.GlYear;
import com.isag.views.CashAccountItemsView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class CashAccountItemsViewServiceImpl implements CashAccountItemsViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<CashAccountItemsView> findAllCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountItemsView cas WHERE cas.branchId = :branchId " + createSql(cashAccountSettlementSearchBean, params) + " order by cas.bankId asc ,cas.date ,cas.ordered ";

        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<CashAccountItemsView> findAllRemainingCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean, StringBuilder stringBuilder) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountItemsView cas WHERE cas.branchId = :branchId";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(" and cas.bankId in ( ");
        sb.append(stringBuilder);
        sb.append(" )");
      //  sb.append(" group by cas.bankId ");

        return dao.findListByQuery(sb.toString(), params);
    }

    @Override
    public List<CashAccountItemsView> findAllCashAccountSettlmentForLineCheck(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountItemsView cas WHERE cas.branchId = :branchId and cas.paymentType = 2" + createSql(cashAccountSettlementSearchBean, params);

        return dao.findListByQuery(sql, params);
    }

    public String createSql(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        addGlBankFromAndTo(sql, params, cashAccountSettlementSearchBean);
        addDateFromAndTo(sql, cashAccountSettlementSearchBean);
        addType(sql, params, cashAccountSettlementSearchBean);
        addtransactionType(sql, params, cashAccountSettlementSearchBean);
        addserialFromAndTo(sql, params, cashAccountSettlementSearchBean);
        addItemsFromAndTo(sql, params, cashAccountSettlementSearchBean);
        // addOrderBy(sql, cashAccountSettlementSearchBean);
        return sql.toString();
    }

    private void addGlBankFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            sql.append(" and cas.bankId >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            sql.append(" and cas.bankId <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }
    }
    
    private void addItemsFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getItemSerialFrom() != null && cashAccountSettlementSearchBean.getItemSerialFrom().getId() > 0) {
            sql.append(" and cas.itemSerial >= :itemSerialFrom");
            params.put("itemSerialFrom", cashAccountSettlementSearchBean.getItemSerialFrom().getSerial());
        }

        if (cashAccountSettlementSearchBean.getItemSerialTo()!= null && cashAccountSettlementSearchBean.getItemSerialTo().getId() > 0) {
            sql.append(" and cas.itemSerial <= :itemSerialTo");
            params.put("itemSerialTo", cashAccountSettlementSearchBean.getItemSerialTo().getSerial());
        }
    }

    public void addDateFromAndTo(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            sql.append(" and cas.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            sql.append(" and cas.date <= '").append(d3).append("'");
        }
    }

    private void addType(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            sql.append(" and cas.type = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
    }
    
    private void addtransactionType(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
            sql.append(" and cas.typeId = :transactionType");
            params.put("transactionType", cashAccountSettlementSearchBean.getTransactionType());
        }
    } 
     private void addserialFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getSerialFrom() != null ) {
            sql.append(" and cas.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom().toString());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null ) {
            sql.append(" and cas.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo().toString());
        }
    }

    private void addOrderBy(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getOrderBy() != null && cashAccountSettlementSearchBean.getOrderBy() == 0) {
            sql.append(" order by cas.date");
        } else {
            sql.append(" order by cas.value");
        }
    }

    public void addDateFromAndToForOpeningBalance(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean, GlYear glYear) {
//        if (glYear.getDateFrom() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
//            sql.append(" and cas.date >= '").append(d3).append("'");
//        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            sql.append(" and cas.date ").append("< '").append(d3).append("'");
        }
    }

    public List<CashAccountItemsView> getTheListBasedOnBankIdGroup(List<Object[]> res) {

        List<CashAccountItemsView> cashAccountSettlmentViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            CashAccountItemsView cashsIterated = new CashAccountItemsView();
            cashsIterated.setValue((BigDecimal) line[0]);
            cashsIterated.setBankId((Integer) line[1]);
            cashAccountSettlmentViewList.add(cashsIterated);
        }
        return cashAccountSettlmentViewList;
    }
}
