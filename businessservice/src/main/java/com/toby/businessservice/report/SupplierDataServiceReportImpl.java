/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.API.SupplierDataAPI;
import com.toby.businessservice.reports.searchBean.SupplierDataReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.InvOrganizationSite;

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
public class SupplierDataServiceReportImpl implements SupplierDataServiceReport {
    
    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;
    
    @Override
    public List<InvOrganizationSite> getAllSupplierDataReportSearchBean(SupplierDataReportSearchBean supplierDataReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", supplierDataReportSearchBean.getBranchId());
        params.put("type", supplierDataReportSearchBean.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM InvOrganizationSite RIV WHERE RIV.branchId.id = :branchId and RIV.type != :type");
        
        stringBuilder.append(appendOrganization(supplierDataReportSearchBean, params));
        
        List<InvOrganizationSite> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }
    
    private String appendOrganization(SupplierDataReportSearchBean supplierDataReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();
        if (supplierDataReportSearchBean.getFromorganizationName() != null) {
            params.put("FromOrganization", supplierDataReportSearchBean.getFromorganizationName().getId());
            appendQuery.append(" AND RIV.id >= :FromOrganization ");
        }
        
        if (supplierDataReportSearchBean.getToorganizationName() != null) {
            params.put("ToOrganization", supplierDataReportSearchBean.getToorganizationName().getId());
            appendQuery.append(" AND RIV.id <= :ToOrganization ");
        }
        
        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<InvOrganizationSite> getAllSupplierDataReportSearchBeanAPI(SupplierDataAPI supplierDataAPI) {
        SupplierDataReportSearchBean supplierDataReportSearchBean = new SupplierDataReportSearchBean();
        if (supplierDataAPI != null) {
            supplierDataReportSearchBean.setBranchId(supplierDataAPI.getBranchId());
            supplierDataReportSearchBean.setType(supplierDataAPI.getInvType());
            supplierDataReportSearchBean.setFromorganizationName(supplierDataAPI.getFromorganizationName());
            supplierDataReportSearchBean.setToorganizationName(supplierDataAPI.getToorganizationName());
            supplierDataReportSearchBean.setShowReport(Boolean.FALSE);
            
        }
        return getAllSupplierDataReportSearchBean(supplierDataReportSearchBean);
    }
    
}
