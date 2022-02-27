/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SalesDataReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.InventoryDelegator;
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
public class SalesDataReportServiceImpl implements SalesDataReportService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<InventoryDelegator> getAllInventoryDelegatorSearchBean(SalesDataReportSearchBean salesDataReportSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", salesDataReportSearchBean.getBranchId());
        params.put("type", salesDataReportSearchBean.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM InventoryDelegator RIV WHERE RIV.branchId.id = :branchId and RIV.type != :type");

        stringBuilder.append(appendSales(salesDataReportSearchBean, params));

        List<InventoryDelegator> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendSales(SalesDataReportSearchBean salesDataReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (salesDataReportSearchBean.getFromName() != null) {
            params.put("FromName", salesDataReportSearchBean.getFromName().getId());
            appendQuery.append(" AND RIV.id >= :FromName ");
        }

        if (salesDataReportSearchBean.getToName() != null) {
            params.put("ToName", salesDataReportSearchBean.getToName().getId());
            appendQuery.append(" AND RIV.id <= :ToName ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
