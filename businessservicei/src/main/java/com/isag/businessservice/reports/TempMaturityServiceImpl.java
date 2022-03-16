/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.procedure.TempMaturity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class TempMaturityServiceImpl implements TempMaturityService {

    @EJB
    private GenericDAO dao;
    private Integer count = 0;
    private BigDecimal totalValue = BigDecimal.ZERO;
    private CollectionAndPaymentReportSearchBean caprsb;

    private CollectionAndPaymentReportSearchBean getCaprsb() {
        if (caprsb == null) {
            caprsb = new CollectionAndPaymentReportSearchBean();
        }
        return caprsb;
    }

    private void collectionAndPaymentStoredProcedure(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, String StoredProcedure, Integer isProject) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("branch_Id", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("customerFrom", String.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("customerTo", String.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("projectFrom", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("projectTo", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("fromDate", Date.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("toDate", Date.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("userID", Integer.class, 'i'));
        Map<String, Object> params = new HashMap<>();
        params.put("branch_Id", collectionAndPaymentReportSearchBean.getBranchId());
        params.put("customerFrom", collectionAndPaymentReportSearchBean.getOrgNameFrom() != null ? collectionAndPaymentReportSearchBean.getOrgNameFrom().getCode() : null);
        params.put("customerTo", collectionAndPaymentReportSearchBean.getOrgNameTo() != null ? collectionAndPaymentReportSearchBean.getOrgNameTo().getCode() : null);
        if (isProject == 0) {
            params.put("projectFrom", collectionAndPaymentReportSearchBean.getInvItemDTOFrom() != null ? collectionAndPaymentReportSearchBean.getInvItemDTOFrom().getId() : null);
            params.put("projectTo", collectionAndPaymentReportSearchBean.getInvItemDTOTo()!= null ? collectionAndPaymentReportSearchBean.getInvItemDTOTo().getId() : null);
        } else {
            params.put("projectFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null ? collectionAndPaymentReportSearchBean.getProjectGuideFrom().getId() : null);
            params.put("projectTo", collectionAndPaymentReportSearchBean.getProjectGuideTo() != null ? collectionAndPaymentReportSearchBean.getProjectGuideTo().getId() : null);
        }
        params.put("fromDate", collectionAndPaymentReportSearchBean.getFromDate());
        params.put("toDate", collectionAndPaymentReportSearchBean.getToDate());
        params.put("userID", collectionAndPaymentReportSearchBean.getUserId());
        dao.StoredProcedure(StoredProcedure, procedureParameter, params);
    }

    @Override
    public List<TempMaturity> getTempMaturitySearchBean(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters, Boolean search) {
        filters.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        if (!getCaprsb().equals(collectionAndPaymentReportSearchBean) || search) {
            caprsb = collectionAndPaymentReportSearchBean;
            collectionAndPaymentStoredProcedure(collectionAndPaymentReportSearchBean, "maturity_procedure",1);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT sum(t.value) FROM TempMaturity t  where t.userId = :userId and t.instSerial = 0");
            totalValue = (BigDecimal) dao.findEntityByQuery(builder.toString(), filters);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT t FROM TempMaturity t where t.userId = :userId";
        stringBuilder.append(query);
        appendFilterTemp(filters, stringBuilder);
        List<TempMaturity> details = dao.findListByQuery(stringBuilder.toString(), filters, first, pageSize);
        count = getCountLazey(filters);
        return details;
    }

    @Override
    public List<TempMaturity> getTempMaturityItemSearchBean(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters, Boolean search) {
        filters.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        if (!getCaprsb().equals(collectionAndPaymentReportSearchBean) || search) {
            caprsb = collectionAndPaymentReportSearchBean;
            collectionAndPaymentStoredProcedure(collectionAndPaymentReportSearchBean, "maturity_item_procedure",0);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT sum(t.value) FROM TempMaturity t  where t.userId = :userId and t.instSerial = 0");
            totalValue = (BigDecimal) dao.findEntityByQuery(builder.toString(), filters);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT t FROM TempMaturity t where t.userId = :userId";
        stringBuilder.append(query);
        appendFilterTemp(filters, stringBuilder);
        List<TempMaturity> details = dao.findListByQuery(stringBuilder.toString(), filters, first, pageSize);
        count = getCountLazey(filters);
        return details;
    }

    private Integer getCountLazey(Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(t) FROM TempMaturity t where t.userId = :userId");
        appendFilterTemp(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details.intValue();
    }

    public void appendFilterTemp(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  (t.customerName  like CONCAT('%',:globalFilter ,'%') OR t.dueDate like CONCAT('%',:globalFilter ,'%') OR t.projectName like CONCAT('%',:globalFilter ,'%') OR t.value like CONCAT('%',:globalFilter ,'%') OR t.totalValue like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    @Override
    public List<TempMaturity> getreportList(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT t FROM TempMaturity t where t.userId = :userId";
        stringBuilder.append(query);
        List<TempMaturity> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

}
