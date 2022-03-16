/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InstProjectGuide;
import com.isag.views.InstView;
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
 * @author ahmed
 */
@Stateless
public class InstViewServiceImpl implements InstViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InstView> getInstViewTotals(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        List<InstView> instViewTotalList = new ArrayList<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e.projectId , e.customerId , sum(COALESCE(e.value,0)) FROM InstView e WHERE e.branchId = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendStatusForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendProjectForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        stringBuilder.append(" GROUP BY e.customerId,e.projectId ");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        instViewTotalList = getTheTotals(res);
        return instViewTotalList;
    }

    @Override
    public List<InstView> getInstViewBySearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM InstView e WHERE e.branchId = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendStatusForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendProjectForQuery(stringBuilder, params, collectionAndPaymentReportSearchBean);
        List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    public void appendDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        if (collectionAndPaymentReportSearchBean.getFromDate() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(collectionAndPaymentReportSearchBean.getFromDate());
            queryBuilder.append(" and e.dueDate >= '").append(formatDateFrom).append("'");
        }

        if (collectionAndPaymentReportSearchBean.getToDate() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(collectionAndPaymentReportSearchBean.getToDate());
            queryBuilder.append(" and e.dueDate <= '").append(formatDateTo).append("'");
        }
    }

    private void appendCustomerForQuery(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {

        if (collectionAndPaymentReportSearchBean.getOrgNameFrom() != null) {
            params.put("customerFrom", collectionAndPaymentReportSearchBean.getOrgNameFrom().getCode());
            stringBuilder.append(" AND e.customerCode >= :customerFrom");
        }

        if (collectionAndPaymentReportSearchBean.getOrgNameTo() != null) {
            params.put("customerTo", collectionAndPaymentReportSearchBean.getOrgNameTo().getCode());
            stringBuilder.append(" AND e.customerCode <= :customerTo");
        }

    }

    private void appendProjectForQuery(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {

        if (collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null) {
            params.put("projectIdFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom().getId());
            stringBuilder.append(" AND e.projectId >= :projectIdFrom");
        }

        if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
            params.put("projectIdTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getId());
            stringBuilder.append(" AND e.projectId <= :projectIdTo");
        }

    }

    private void appendStatusForQuery(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        stringBuilder.append(" and (e.status is null OR e.status = 0 OR e.status = 2)");
    }

    public List<InstView> getTheTotals(List<Object[]> res) {

        List<InstView> instViewTotalList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            InstView instView = new InstView();
            instView.setSerial((Integer) line[0]);
            instView.setCustomerId((Integer) line[1]);
            instView.setValue((BigDecimal) line[2]);
            instViewTotalList.add(instView);
        }
        return instViewTotalList;
    }
    
    

    @Override
    public List<InstView> getInstViewTotalsByLevel(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>(); 
        List<InstView> instViewTotalList = new ArrayList<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e.instContractId.serial , e.instContractId.organizationSiteId.id , sum(COALESCE(e.value,0)) FROM InstContractDetail e WHERE e.instContractId.branchId.id = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
//        appendStatusForQueryByLevel(stringBuilder);
        appendProjectForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        stringBuilder.append(" GROUP BY e.instContractId.organizationSiteId.id,e.instContractId.serial ");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        instViewTotalList = getTheTotals(res);
        return instViewTotalList;
    }

    @Override
    public List<InstView> getInstViewTotalsByLevelForItems(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>(); 
        List<InstView> instViewTotalList = new ArrayList<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e.instContractId.invItemId.id , e.instContractId.organizationSiteId.id , sum(COALESCE(e.value,0)) FROM InstContractDetail e WHERE e.instContractId.branchId.id = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendStatusForQueryByLevel(stringBuilder);
        appendInvItemForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        stringBuilder.append(" GROUP BY e.instContractId.organizationSiteId.id,e.instContractId.invItemId.id ");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        instViewTotalList = getTheTotals(res);
        return instViewTotalList;
    }

    @Override
    public List<InstView> getInstViewBySearchBeanByLevel(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>(); 
        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM InstContractDetail e WHERE e.instContractId.branchId.id = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendStatusForQueryByLevel(stringBuilder);
        appendProjectForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        List<InstContractDetail> details = dao.findListByQuery(stringBuilder.toString(), params);
        return mapTOInstView(details);
    }

    @Override
    public List<InstView> getInstViewBySearchBeanByLevelForItems(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>(); 
        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM InstContractDetail e WHERE e.instContractId.branchId.id = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendCustomerForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        appendStatusForQueryByLevel(stringBuilder);
        appendInvItemForQueryByLevel(stringBuilder, params, collectionAndPaymentReportSearchBean);
        List<InstContractDetail> details = dao.findListByQuery(stringBuilder.toString(), params);
        return mapTOInstView(details);
    }

    private void appendCustomerForQueryByLevel(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {

        if (collectionAndPaymentReportSearchBean.getOrgNameFrom() != null) {  
            params.put("customerFrom", collectionAndPaymentReportSearchBean.getOrgNameFrom().getCode());
            stringBuilder.append(" AND e.instContractId.organizationSiteId.code >= :customerFrom");
        }

        if (collectionAndPaymentReportSearchBean.getOrgNameTo() != null) {
            params.put("customerTo", collectionAndPaymentReportSearchBean.getOrgNameTo().getCode());
            stringBuilder.append(" AND e.instContractId.organizationSiteId.code <= :customerTo");
        }

    }

    private void appendInvItemForQueryByLevel(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {

        if (collectionAndPaymentReportSearchBean.getInvItemDTOFrom() != null) { 
            params.put("itemIdFrom", collectionAndPaymentReportSearchBean.getInvItemDTOFrom().getCode());
            stringBuilder.append(" AND e.instContractId.invItemId.code >= :itemIdFrom");
        }
        if (collectionAndPaymentReportSearchBean.getInvItemDTOTo() != null) {
            params.put("itemIdTo", collectionAndPaymentReportSearchBean.getInvItemDTOTo().getCode());
            stringBuilder.append(" AND e.instContractId.invItemId.code <= :itemIdTo");
        }

    }

    private void appendProjectForQueryByLevel(StringBuilder stringBuilder, Map<String, Object> params, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {

        if (collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null) { 
            params.put("projectIdFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom().getCode());
            if (collectionAndPaymentReportSearchBean.getProjectGuideFrom().getLevel() == 3) {
                stringBuilder.append(" AND e.instContractId.projectId.code >= :projectIdFrom");
                if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
                    stringBuilder.append(" AND e.instContractId.projectId.code <= :projectIdTo");
                    params.put("projectIdTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getCode());
                }
            } else if (collectionAndPaymentReportSearchBean.getProjectGuideFrom().getLevel() == 2) {
                stringBuilder.append("  AND e.instContractId.projectId.id in (select p.id from InstProjectGuide p where p.parent.code >= :projectIdFrom ");
                params.put("projectIdFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom().getCode());
                if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
                    stringBuilder.append(" and p.parent.code <= :projectIdTo");
                    params.put("projectIdTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getCode());
                }
                stringBuilder.append(")");
            } else if (collectionAndPaymentReportSearchBean.getProjectGuideFrom().getLevel() == 1) {
                stringBuilder.append("  AND e.instContractId.projectId.id in (select p.id from InstProjectGuide p where p.parent.id in (select c.id from InstProjectGuide c where c.parent.code >= :projectIdFrom ");
                params.put("projectIdFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom().getCode());
                if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
                    stringBuilder.append(" and c.parent.code <= :projectIdTo");
                    params.put("projectIdTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getCode());
                }
                stringBuilder.append("))");
            }

        } else if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
            params.put("projectIdTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getCode());
            if (collectionAndPaymentReportSearchBean.getProjectGuideTo().getLevel() == 3) { 
                stringBuilder.append(" AND e.instContractId.projectId.code <= :projectIdTo");
            } else if (collectionAndPaymentReportSearchBean.getProjectGuideTo().getLevel() == 2) {
                stringBuilder.append("  AND e.instContractId.projectId.id in (select p.id from InstProjectGuide p where p.parent.code <= :projectIdTo) ");
            } else if (collectionAndPaymentReportSearchBean.getProjectGuideTo().getLevel() == 1) {
                stringBuilder.append("  AND e.instContractId.projectId.id in (select p.id from InstProjectGuide p where p.parent.id in (select c from InstProjectGuide c where c.parent.code <= :projectIdTo ))");
            }
        }

    }

    private void appendStatusForQueryByLevel(StringBuilder stringBuilder) {
        stringBuilder.append(" and (e.instContractId.status is null OR e.instContractId.status = 0 OR e.instContractId.status = 2)");
    }

    private List<InstView> mapTOInstView(List<InstContractDetail> details) {
        List<InstView> instViewList = new ArrayList<>();
        Integer rowNum = 0;
        for (InstContractDetail contractDetail : details) { 
            InstView instView = new InstView();
            instView.setRowNum(++rowNum);
            instView.setCustomerCode(contractDetail.getInstContractId().getOrganizationSiteId() == null ? null : contractDetail.getInstContractId().getOrganizationSiteId().getCode());
            instView.setCustomerId(contractDetail.getInstContractId().getOrganizationSiteId() == null ? null : contractDetail.getInstContractId().getOrganizationSiteId().getId());
            instView.setCustomerName(contractDetail.getInstContractId().getOrganizationSiteId() == null ? null : contractDetail.getInstContractId().getOrganizationSiteId().getName());
            instView.setDueDate(contractDetail.getDueDate());
            instView.setInstSerial(contractDetail.getSerial());
            instView.setOrganizationSiteId(contractDetail.getInstContractId().getOrganizationSiteId() == null ? null : contractDetail.getInstContractId().getOrganizationSiteId().getId());
            instView.setProjectId(contractDetail.getInstContractId().getProjectId() == null ? null : contractDetail.getInstContractId().getProjectId().getId());
            instView.setProjectName(contractDetail.getInstContractId().getProjectId() == null ? null : contractDetail.getInstContractId().getProjectId().getName());
            instView.setSerial(contractDetail.getInstContractId().getSerial());
            instView.setStatus(contractDetail.getStatus());
            instView.setTotalValue(contractDetail.getInstContractId().getTotalValue());
            instView.setValue(contractDetail.getValue());
            instViewList.add(instView);
        }
        return instViewList;
    }

}
