/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.core.GenericDAO;
import com.isag.entity.GlYear;
import com.isag.views.BankBalanceView;
import com.isag.views.ItemsBalanceView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class ItemsBalanceViewServiceImpl implements ItemsBalanceViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemsBalanceView> getAllBankBalancesTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer bankIdFrom, Integer bankIdTo, Integer itemSerialFrom, Integer itemSerialTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM ItemsBalanceView bbv WHERE bbv.branchId = :branchId ");
        appendDateForQueryBuilder(queryBuilder, dateFrom, dateTo);
        addItemFromAndTo(queryBuilder, params, itemSerialFrom, itemSerialTo);
        addGlBankFromAndTo(queryBuilder, params, bankIdFrom, bankIdTo);
        List<ItemsBalanceView> data = dao.findListByQuery(queryBuilder.toString(), params);

        return data;
    }

    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Date dateFrom, Date dateTo) {
        if (dateFrom != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            queryBuilder.append(" and bbv.date >= '").append(formatDateFrom).append(" 00-00-00'");
        }

        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            queryBuilder.append(" and bbv.date <= '").append(formatDateTo).append(" 23-59-59'");
        }
    }

    private void addGlBankFromAndTo(StringBuilder sql, Map<String, Object> params, Integer bankIdFrom, Integer bankIdTo) {
        if (bankIdFrom != null && bankIdFrom > 0) {
            sql.append(" and bbv.bankIdFrom >= :bankIdFrom");
            params.put("bankIdFrom", bankIdFrom);
        }

        if (bankIdTo != null && bankIdTo > 0) {
            sql.append(" and bbv.bankIdTo <= :bankIdTo");
            params.put("bankIdTo", bankIdTo);
        }
    }
    
    private void addItemFromAndTo(StringBuilder sql, Map<String, Object> params, Integer itemSerialFrom, Integer itemSerialTo) {
        if (itemSerialFrom != null && itemSerialFrom > 0) {
            sql.append(" and bbv.itemSerial >= :itemSerialFrom");
            params.put("itemSerialFrom", itemSerialFrom);
        }

        if (itemSerialTo != null && itemSerialTo > 0) {
            sql.append(" and bbv.itemSerial <= :itemSerialTo");
            params.put("itemSerialTo", itemSerialTo);
        }
    }

    @Override
    public List<ItemsBalanceView> getAllBankBalancesTransactionsByBankId(Integer branchId, GlYear glYear, Date dateTo, StringBuilder builder, Integer itemSerialFrom, Integer itemSerialTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
//        params.put("glYear", glYear.getYear());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM ItemsBalanceView bbv WHERE bbv.branchId = :branchId ");
        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
        addItemFromAndTo(queryBuilder, params, itemSerialFrom, itemSerialTo);
        appendBanksIdFromAndTo(queryBuilder, params, builder);
//        queryBuilder.append(" and bbv.glYear = :glYear");
        List<ItemsBalanceView> data = dao.findListByQuery(queryBuilder.toString(), params);

        return data;
    }

    private void appendBanksIdFromAndTo(StringBuilder sql, Map<String, Object> params, StringBuilder builder) {
        if (builder != null && builder.length() > 0) {
            sql.append(" and (bbv.bankIdfrom in (").append(builder).append(")");
            sql.append(" or bbv.bankIdTo in (").append(builder).append("))");
        }
    }

}
