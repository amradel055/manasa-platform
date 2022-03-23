/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.core.GenericDAO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class analyticalRatiosServiceImpl implements analyticalRatiosService {

    @EJB
    private GenericDAO dao;

    @Override
    public BigDecimal getCurrentRatio(Date startGlYear, Date endDate, StringBuilder stringBuilder) {

        Map<String, Object> params = new HashMap();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT SUM(COALESCE(arv.balanceLocal , 0)) From AnalyticalRatiosView arv WHERE arv.accountId in");
        queryBuilder.append(" ( ");
        queryBuilder.append(stringBuilder);
        queryBuilder.append(" ) ");
        if (startGlYear != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(startGlYear);
            queryBuilder.append(" and arv.date >= '").append(formatDateFrom).append("'");
        }

        if (endDate != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            queryBuilder.append(" and arv.date <= '").append(formatDateTo).append("'");
        }
        BigDecimal total = dao.findEntityByQuery(queryBuilder.toString());
        return total;
    }

    @Override
    public BigDecimal getSpecificRatio(Date startGlYear, Date endDate, Integer accountId) {
        Map<String, Object> params = new HashMap();
        StringBuilder stringBuilder = new StringBuilder();
        params.put("accountId", accountId);
        stringBuilder.append("SELECT SUM(COALESCE(arv.balanceLocal , 0)) From AnalyticalRatiosView arv WHERE arv.accountId = :accountId");

        if (startGlYear != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(startGlYear);
            stringBuilder.append(" and arv.date >= '").append(formatDateFrom).append("'");
        }

        if (endDate != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            stringBuilder.append(" and arv.date <= '").append(formatDateTo).append("'");
        }
        BigDecimal total = dao.findEntityByQuery(stringBuilder.toString(), params);

        return total;
    }
}
