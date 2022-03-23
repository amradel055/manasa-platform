/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SalesJournalSearchBean;

import com.isag.core.GenericDAO;

import com.isag.views.NetView;
import java.util.Calendar;
import java.util.Date;

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
public class SalesJournalServiceImpl implements SalesJournalService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<NetView> getAllPurchaseInvoiceSearchBean(SalesJournalSearchBean salesJournalSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", salesJournalSearchBean.getBranchId());
        params.put("type", salesJournalSearchBean.getType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM NetView RIV WHERE RIV.branchId = :branchId");

        if (salesJournalSearchBean.getIsReturn()) {
            if (salesJournalSearchBean.getType() == 0) {
                stringBuilder.append("  AND ( RIV.type =:type OR RIV.type =2 ) ");
            } else if (salesJournalSearchBean.getType() == 1) {
                stringBuilder.append("  AND ( RIV.type =:type OR RIV.type =3 ) ");
            }
        } else {
            stringBuilder.append("  AND RIV.type =:type ");
        }

        stringBuilder.append(appendDate(salesJournalSearchBean, params));
        stringBuilder.append(appendCustomer(salesJournalSearchBean, params));
        stringBuilder.append(appendDelegator(salesJournalSearchBean, params));
        stringBuilder.append(appendPaymentType(salesJournalSearchBean, params));
        stringBuilder.append(appendSerial(salesJournalSearchBean, params));
        stringBuilder.append(appendUser(salesJournalSearchBean, params));
        stringBuilder.append(appendInventory(salesJournalSearchBean, params));
        List<NetView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendCustomer(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getFromCustomer() != null) {
            params.put("fromCustomer", salesJournalSearchBean.getFromCustomer().getId());
            appendQuery.append(" AND RIV.organizationSiteId>= :fromCustomer");
        }

        if (salesJournalSearchBean.getToCustomer() != null) {
            params.put("toCustomer", salesJournalSearchBean.getToCustomer().getId());
            appendQuery.append(" AND RIV.organizationSiteId <= :toCustomer");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendDelegator(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getFromDelegator() != null) {
            params.put("fromDelegator", salesJournalSearchBean.getFromDelegator().getId());
            appendQuery.append(" AND RIV.invDelegator >= :fromDelegator ");
        }

        if (salesJournalSearchBean.getToDelegator() != null) {
            params.put("toDelegator", salesJournalSearchBean.getToDelegator().getId());
            appendQuery.append(" AND RIV.invDelegator <= :toDelegator ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendPaymentType(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getFrompaymentType() != null) {
            params.put("frompaymentType", salesJournalSearchBean.getFrompaymentType());
            appendQuery.append(" AND RIV.paymentType >= :frompaymentType ");
        }

        if (salesJournalSearchBean.getTopaymentType() != null) {
            params.put("topaymentType", salesJournalSearchBean.getTopaymentType());
            appendQuery.append(" AND RIV.paymentType <= :topaymentType ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendSerial(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getFromserial() != null) {
            params.put("fromserial", salesJournalSearchBean.getFromserial().getSerial());
            appendQuery.append(" AND RIV.serial >= :fromserial ");
        }

        if (salesJournalSearchBean.getToserial() != null) {
            params.put("toserial", salesJournalSearchBean.getToserial().getSerial());
            appendQuery.append(" AND RIV.serial <= :toserial ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendUser(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();
        if (salesJournalSearchBean.getIsagUserId() != null) {
            params.put("userId", salesJournalSearchBean.getIsagUserId());
            appendQuery.append(" AND RIV.userId = :userId ");
        }
        return appendQuery.toString();
    }

    private String appendDate(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getDateFrom() != null) {
            params.put("fromDate", salesJournalSearchBean.getDateFrom());
            appendQuery.append(" AND RIV.date >= :fromDate");
        }

        if (salesJournalSearchBean.getDateTo() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(salesJournalSearchBean.getDateTo());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            params.put("toDate", calendar.getTime());
            appendQuery.append(" AND RIV.date <= :toDate");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendInventory(SalesJournalSearchBean salesJournalSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesJournalSearchBean.getFromInventoryName() != null) {
            params.put("fromInventory", salesJournalSearchBean.getFromInventoryName().getId());
            appendQuery.append(" AND RIV.inventoryId >= :fromInventory");
        }

        if (salesJournalSearchBean.getToInventoryName() != null) {
            params.put("toInventory", salesJournalSearchBean.getToInventoryName().getId());
            appendQuery.append(" AND RIV.inventoryId <= :toInventory");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//--------------------------
    @Override
    public List<NetView> getAllDates(SalesJournalSearchBean salesJournalSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", salesJournalSearchBean.getBranchId());

        List<NetView> detailses = dao.findListByQuery("SELECT RIV FROM NetView RIV WHERE RIV.branchId = :branchId", params);
        //  List<NetView> detailses = getTheList(res);
        return detailses;

    }

    /*  public List<NetView> getTheList(List<Object[]> res) {

        List<NetView> invPurchaseInvoiceDetailList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            NetView datesList = new NetView();
            datesList.setDate((Date) line[0]);
            datesList.setSerial((Integer) line[1]);
        }
        return invPurchaseInvoiceDetailList;
    }

     */
}
//---------------------------

