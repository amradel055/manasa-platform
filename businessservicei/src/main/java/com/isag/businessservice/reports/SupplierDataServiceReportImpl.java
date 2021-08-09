/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SupplierDataReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import java.util.ArrayList;
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

    @Override
    public List<InvOrganizationSiteDTO> getAllSupplierDataReportSearchBeanDTO(SupplierDataReportSearchBean supplierDataReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        params.put("branchId", supplierDataReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM InvOrganizationSite RIV WHERE RIV.branchId.id = :branchId ");
        
        if (supplierDataReportSearchBean.getType() == 0) {
            stringBuilder.append(" AND (RIV.type = 0 OR RIV.type = 2) ");
        } else if (supplierDataReportSearchBean.getType() == 1) {
            stringBuilder.append(" AND (RIV.type = 1 OR RIV.type = 2) ");
        } else if (supplierDataReportSearchBean.getType() == 4) {
            stringBuilder.append(" AND (RIV.type = 4 ) ");
        } else if (supplierDataReportSearchBean.getType() == 5) {
            stringBuilder.append(" AND (RIV.type = 5 ) ");
        }

        stringBuilder.append(appendOrganization(supplierDataReportSearchBean, params));

        List<InvOrganizationSite> details = dao.findListByQuery(stringBuilder.toString(), params);
        for (InvOrganizationSite invOrganizationSite : details) {
            InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();

            siteDTO.setCode(invOrganizationSite.getCode());
            siteDTO.setName(invOrganizationSite.getName());
            siteDTO.setPhone(invOrganizationSite.getMobile());
            siteDTO.setParentName(invOrganizationSite.getParent() != null ? (invOrganizationSite.getParent().getName() != null ? invOrganizationSite.getParent().getName() : null) : null);
            siteDTO.setBalanceLimit(invOrganizationSite.getBalanceLimit());
            siteDTO.setDelegatorName(invOrganizationSite.getDelegatorId() != null ? (invOrganizationSite.getDelegatorId().getName() != null ? invOrganizationSite.getDelegatorId().getName() : "") : null);
            siteDTO.setAccountName(invOrganizationSite.getAccountId() != null ? (invOrganizationSite.getAccountId().getName() != null ? invOrganizationSite.getAccountId().getName() : null) : null);
            siteDTO.setAccountNumber(invOrganizationSite.getAccountId() != null ? (invOrganizationSite.getAccountId().getAccNumber() != null ? invOrganizationSite.getAccountId().getAccNumber() : null) : null);
            siteDTO.setNationId(invOrganizationSite.getNationId());
            siteDTO.setSponsorName1(invOrganizationSite.getSponsorName1());
            siteDTO.setSponsorName2(invOrganizationSite.getSponsorName2());
            siteDTO.setSponsorPhone1(invOrganizationSite.getPhone());
            siteDTO.setSponsorPhone2(invOrganizationSite.getPhone2());
            siteDTO.setAccountBankNumber(invOrganizationSite.getAccountBankNumber());
            siteDTO.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());
            if (invOrganizationSite.getType() == 0) {
            siteDTO.setOpenBalance(invOrganizationSite.getOpenBalanceDebit().subtract(invOrganizationSite.getOpenBalanceCredit()));
        } else if (invOrganizationSite.getType() == 1 || invOrganizationSite.getType() == 4) {
            siteDTO.setOpenBalance(invOrganizationSite.getOpenBalanceCredit().subtract(invOrganizationSite.getOpenBalanceDebit()));
        } 
            
            invOrganizationSiteDTOList.add(siteDTO);
        }
        return invOrganizationSiteDTOList;
    }

    private String appendOrganization(SupplierDataReportSearchBean supplierDataReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (supplierDataReportSearchBean.getFromorganizationName() != null) {
            params.put("FromOrganization", supplierDataReportSearchBean.getFromorganizationName().getCode());
            appendQuery.append(" AND RIV.code >= :FromOrganization ");
        }

        if (supplierDataReportSearchBean.getToorganizationName() != null) {
            params.put("ToOrganization", supplierDataReportSearchBean.getToorganizationName().getCode());
            appendQuery.append(" AND RIV.code <= :ToOrganization ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
