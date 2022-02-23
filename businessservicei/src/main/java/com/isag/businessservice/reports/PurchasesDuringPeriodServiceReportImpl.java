/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.PurchasesDuringPeriodSearchBean;
import com.isag.core.GenericDAO;
import com.isag.views.NetView;
import java.util.Calendar;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class PurchasesDuringPeriodServiceReportImpl implements PurchasesDuringPeriodServiceReport {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<NetView> getAllPurchasesDuringPeriodSearchBean(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", purchasesDuringPeriodSearchBean.getBranchId());
        params.put("type", purchasesDuringPeriodSearchBean.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM NetView RIV WHERE RIV.branchId = :branchId  and RIV.type = :type");

        stringBuilder.append(appendDate(purchasesDuringPeriodSearchBean, params));
        stringBuilder.append(appendOrganization(purchasesDuringPeriodSearchBean, params));
        stringBuilder.append(appendInventory(purchasesDuringPeriodSearchBean, params));
        stringBuilder.append(appendSerial(purchasesDuringPeriodSearchBean, params));
        stringBuilder.append(appendType(purchasesDuringPeriodSearchBean, params));
        stringBuilder.append(" ORDER BY RIV.date , RIV.serial ");
        List<NetView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendOrganization(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (purchasesDuringPeriodSearchBean.getFromorganizationName() != null) {
            params.put("FromOrganization", purchasesDuringPeriodSearchBean.getFromorganizationName().getId());
            appendQuery.append(" AND RIV.organizationSiteId >= :FromOrganization ");
        }

        if (purchasesDuringPeriodSearchBean.getToorganizationName() != null) {
            params.put("ToOrganization", purchasesDuringPeriodSearchBean.getToorganizationName().getId());
            appendQuery.append(" AND RIV.organizationSiteId <= :ToOrganization ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendDate(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (purchasesDuringPeriodSearchBean.getDateFrom() != null) {
            params.put("fromDate", purchasesDuringPeriodSearchBean.getDateFrom());
            appendQuery.append(" AND RIV.date >= :fromDate");
        }

        if (purchasesDuringPeriodSearchBean.getDateTo() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(purchasesDuringPeriodSearchBean.getDateTo());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            params.put("toDate",calendar.getTime() );
            appendQuery.append(" AND RIV.date <= :toDate");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendInventory(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (purchasesDuringPeriodSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchasesDuringPeriodSearchBean.getFromInventoryName().getId());
            appendQuery.append(" AND RIV.inventoryId >= :FromInventory ");
        }

        if (purchasesDuringPeriodSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchasesDuringPeriodSearchBean.getToInventoryName().getId());
            appendQuery.append(" AND RIV.inventoryId <= :ToInventory ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendSerial(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (purchasesDuringPeriodSearchBean.getFromserial() != null) {
            params.put("FromSerial", purchasesDuringPeriodSearchBean.getFromserial().getSerial());
            appendQuery.append(" AND RIV.serial >= :FromSerial ");
        }

        if (purchasesDuringPeriodSearchBean.getToserial() != null) {
            params.put("Toserial", purchasesDuringPeriodSearchBean.getToserial().getSerial());
            appendQuery.append(" AND RIV.serial <= :Toserial ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendType(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (purchasesDuringPeriodSearchBean.getFrompaymentType() != null && purchasesDuringPeriodSearchBean.getFrompaymentType() >= 0) {
            params.put("FromType", purchasesDuringPeriodSearchBean.getFrompaymentType());
            appendQuery.append(" AND RIV.paymentType >= :FromType ");
        }

        if (purchasesDuringPeriodSearchBean.getTopaymentType() != null && purchasesDuringPeriodSearchBean.getTopaymentType() >= 0) {
            params.put("ToType", purchasesDuringPeriodSearchBean.getTopaymentType());
            appendQuery.append(" AND RIV.paymentType <= :ToType ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
