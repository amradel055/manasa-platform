/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.core.GenericDAO;
import com.isag.views.BankTransactionView;
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
public class BankTransactionViewServiceImpl implements BankTransactionViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<BankTransactionView> getAllBankTransactions(Integer branchId, Integer bankId, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("bankId", bankId);
        StringBuilder queryBuilder = new StringBuilder();
          queryBuilder.append("SELECT btv FROM BankTransactionView btv WHERE btv.branchId = :branchId and (btv.bankIdFrom = :bankId or btv.bankIdTo = :bankId)" );
        appendDateForQueryBuilder(queryBuilder,dateFrom ,dateTo);
        List<BankTransactionView> data = dao.findListByQuery( queryBuilder.toString(), params);

        return data;
    }
        private void appendDateForQueryBuilder(StringBuilder queryBuilder, Date dateFrom,  Date dateTo) {
        if (dateFrom != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            queryBuilder.append(" and btv.date >= '").append(formatDateFrom).append(" 00-00-00'");
        }

        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            queryBuilder.append(" and btv.date <= '").append(formatDateTo).append(" 23-59-59'");
        }
    }

}
