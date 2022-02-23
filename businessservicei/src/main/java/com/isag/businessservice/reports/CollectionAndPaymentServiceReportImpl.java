/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.CollectionPaymentView;
import com.isag.views.CollectionPaymentTotalsTemp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class CollectionAndPaymentServiceReportImpl implements CollectionAndPaymentServiceReport {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    private void CollectionPaymentStoredProcedure(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("branchId", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("orgNameFrom", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("orgNameTo", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("fromDate", Date.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("toDate", Date.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("status", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("projectGuideFrom", String.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("projectGuideTo", String.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("userID", Integer.class, 'i')); 
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        params.put("orgNameFrom", collectionAndPaymentReportSearchBean.getOrgNameFrom() != null ? collectionAndPaymentReportSearchBean.getOrgNameFrom().getId() : null);
        params.put("orgNameTo", collectionAndPaymentReportSearchBean.getOrgNameTo() != null ? collectionAndPaymentReportSearchBean.getOrgNameTo().getId() : null);
        params.put("fromDate", collectionAndPaymentReportSearchBean.getFromDate());
        params.put("toDate", collectionAndPaymentReportSearchBean.getToDate());
        params.put("status", collectionAndPaymentReportSearchBean.getStatus());
        params.put("projectGuideFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null ? collectionAndPaymentReportSearchBean.getProjectGuideFrom().getCode(): null);
        params.put("projectGuideTo", collectionAndPaymentReportSearchBean.getProjectGuideTo() != null ? collectionAndPaymentReportSearchBean.getProjectGuideTo().getCode(): null);
        params.put("userID", collectionAndPaymentReportSearchBean.getUserId());
        dao.StoredProcedure("CollectionPaymentTotals", procedureParameter, params);
    }
    Integer count = Integer.valueOf(0);
    BigDecimal totalAmount = BigDecimal.ZERO;
    BigDecimal totalRemain = BigDecimal.ZERO;
    BigDecimal totalPay = BigDecimal.ZERO;
    CollectionAndPaymentReportSearchBean caprsb;

    private CollectionAndPaymentReportSearchBean getCaprsb() {
        if (caprsb == null) {
            caprsb = new CollectionAndPaymentReportSearchBean();
        }
        return caprsb;
    }

    @Override
    public List<CollectionPaymentTotalsTemp> getAllCollectionPaymentReport(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters, Boolean search) {
        filters.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        if (!getCaprsb().equals(collectionAndPaymentReportSearchBean) || search) {
            caprsb = collectionAndPaymentReportSearchBean;
            CollectionPaymentStoredProcedure(collectionAndPaymentReportSearchBean);
            totalAmount = dao.findEntityByQuery("SELECT SUM(t.dueValue) FROM CollectionPaymentTotalsTemp t where t.userId = :userId", filters);
            totalRemain = dao.findEntityByQuery("SELECT SUM(t.remain) FROM CollectionPaymentTotalsTemp t where t.userId = :userId", filters);
            totalPay = dao.findEntityByQuery("SELECT SUM(t.pay) FROM CollectionPaymentTotalsTemp t where t.userId = :userId", filters);
        }

        /*   if (count - (count % pageSize) == first) {
            pageSize = (count % pageSize);
        }*/
 /*StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(t) FROM CollectionPaymentTotalsTemp t ");
      
        Long details = dao.findEntityByQuery(builder.toString(), filters);
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Select t FROM CollectionPaymentTotalsTemp t where t.userId = :userId");
        appendFilterTemp(filters, stringBuilder);

        count = getCountLazey(collectionAndPaymentReportSearchBean);

        List<CollectionPaymentTotalsTemp> list = dao.findListByQuery(stringBuilder.toString(), filters, first, pageSize);

        return list;
    }

    @Override
    public Integer getTotalRegistorsTemp() {
        return count;
    }

    @Override
    public BigDecimal getTotalAmountTemp() {
        return totalAmount;
    }

    @Override
    public BigDecimal getTotalRemainTemp() {
        return totalRemain;
    }

    @Override
    public BigDecimal getTotalPayTemp() {
        return totalPay;
    }

    public void appendFilterTemp(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  (t.customerName  like CONCAT('%',:globalFilter ,'%') OR t.dueDate like CONCAT('%',:globalFilter ,'%') OR t.payDate like CONCAT('%',:globalFilter ,'%') OR t.customerPhone like CONCAT('%',:globalFilter ,'%') OR t.dueValue like CONCAT('%',:globalFilter ,'%') OR t.pay  like CONCAT('%',:globalFilter ,'%') OR t.remain like CONCAT('%',:globalFilter ,'%') OR t.statusText like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<CollectionPaymentView> getAllCollectionPaymentReportSearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT RIV FROM CollectionPaymentView RIV WHERE RIV.branchId= :branchId");

        stringBuilder.append(appendOrganization(collectionAndPaymentReportSearchBean, params));
        stringBuilder.append(appendDate(collectionAndPaymentReportSearchBean, params));
        stringBuilder.append(appendStatus(collectionAndPaymentReportSearchBean, params));
        stringBuilder.append(appendProject(collectionAndPaymentReportSearchBean, params));

        List<CollectionPaymentView> details = dao.findListByQuery(stringBuilder.toString(), params);

        return details;
    }

    private Integer getCountLazey(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> filters = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        filters.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        builder.append("SELECT count(t) FROM CollectionPaymentTotalsTemp t WHERE t.userId = :userId");
        appendFilterTemp(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details.intValue();
    }

    private String appendOrganization(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (collectionAndPaymentReportSearchBean.getOrgNameFrom() != null) {
            params.put("orgNameFrom", collectionAndPaymentReportSearchBean.getOrgNameFrom().getId());
            appendQuery.append(" AND RIV.customerId >= :orgNameFrom ");
        }

        if (collectionAndPaymentReportSearchBean.getOrgNameTo() != null) {
            params.put("orgNameTo", collectionAndPaymentReportSearchBean.getOrgNameTo().getId());
            appendQuery.append(" AND RIV.customerId <= :orgNameTo");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendDate(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (collectionAndPaymentReportSearchBean.getFromDate() != null) {
            params.put("fromDate", collectionAndPaymentReportSearchBean.getFromDate());
            appendQuery.append(" AND RIV.dueDate >= :fromDate ");
        }

        if (collectionAndPaymentReportSearchBean.getToDate() != null) {
            params.put("toDate", collectionAndPaymentReportSearchBean.getToDate());
            appendQuery.append(" AND RIV.dueDate <= :toDate");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendStatus(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (collectionAndPaymentReportSearchBean.getStatus() != null && collectionAndPaymentReportSearchBean.getStatus() != 0) {
            params.put("status", collectionAndPaymentReportSearchBean.getStatus());
            appendQuery.append(" AND RIV.status = :status ");
        } else if (collectionAndPaymentReportSearchBean.getStatus() != null && collectionAndPaymentReportSearchBean.getStatus() == 0) {
            appendQuery.append(" AND ( RIV.status is null OR RIV.status = 0 ) ");
        } else {
            return "";
        }
        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendProject(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null) {
            params.put("projectGuideFrom", collectionAndPaymentReportSearchBean.getProjectGuideFrom().getId());
            appendQuery.append(" AND RIV.projectId >= :projectGuideFrom ");
        }

        if (collectionAndPaymentReportSearchBean.getProjectGuideTo() != null) {
            params.put("projectGuideTo", collectionAndPaymentReportSearchBean.getProjectGuideTo().getId());
            appendQuery.append(" AND RIV.projectId <= :projectGuideTo");
        }
        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GlBankTransaction> getAllTransactions(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder glBankTransaction = new StringBuilder();

        glBankTransaction.append("SELECT RIV.dueDateId FROM CollectionPaymentView RIV WHERE RIV.branchId= :branchId");
        glBankTransaction.append(appendOrganization(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendDate(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendStatus(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendProject(collectionAndPaymentReportSearchBean, params));
        stringBuilder.append("SELECT GBT FROM GlBankTransaction GBT WHERE GBT.transactionType=1 AND GBT.installmentId IS NOT NULL and GBT.installmentId.id IN (").append(glBankTransaction.toString()).append(" ) AND GBT.branchId.id = :branchId");

        List<GlBankTransaction> glBankTransactiondetails = dao.findListByQuery(stringBuilder.toString(), params);

        return glBankTransactiondetails;
    }

    @Override
    public Long getTotalRegistors(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();

        StringBuilder glBankTransaction = new StringBuilder();
        glBankTransaction.append("SELECT RIV.dueDateId FROM CollectionPaymentView RIV WHERE RIV.branchId= :branchId");
        glBankTransaction.append(appendOrganization(collectionAndPaymentReportSearchBean, filters));
        glBankTransaction.append(appendDate(collectionAndPaymentReportSearchBean, filters));
        glBankTransaction.append(appendStatus(collectionAndPaymentReportSearchBean, filters));
        glBankTransaction.append(appendProject(collectionAndPaymentReportSearchBean, filters));

        builder.append("SELECT count(GBT) FROM GlBankTransaction GBT WHERE GBT.transactionType=1 AND GBT.installmentId IS NOT NULL and GBT.installmentId.id IN (").append(glBankTransaction.toString()).append(" ) AND GBT.branchId.id = :branchId");
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    @Override
    public List<GlBankTransactionDTO> getAllTransactionsLazyDTO(int first, int pageSize, String sortField, Boolean asc, CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean, Map<String, Object> filters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT GBT.value,GBT.date,GBT.installmentId FROM GlBankTransaction GBT WHERE GBT.transactionType=1 AND GBT.installmentId IS NOT NULL and GBT.installmentId.id IN ( Select t FROM Temp t ) AND GBT.branchId.id = :branchId");
        List<GlBankTransaction> list = dao.findListByQuery(stringBuilder.toString(), filters, first, pageSize);
        List<GlBankTransactionDTO> dTOList = new ArrayList<>();
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<GlBankTransactionDTO> getAllTransactionsDTO(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder glBankTransaction = new StringBuilder();

        glBankTransaction.append("SELECT RIV.dueDateId FROM CollectionPaymentView RIV WHERE RIV.branchId= :branchId");
        glBankTransaction.append(appendOrganization(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendDate(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendStatus(collectionAndPaymentReportSearchBean, params));
        glBankTransaction.append(appendProject(collectionAndPaymentReportSearchBean, params));
        stringBuilder.append("SELECT GBT FROM GlBankTransaction GBT WHERE GBT.transactionType=1 AND GBT.installmentId IS NOT NULL and GBT.installmentId.id IN (").append(glBankTransaction.toString()).append(" ) AND GBT.branchId.id = :branchId");
        List<GlBankTransaction> list = dao.findListByQuery(stringBuilder.toString(), params);
        List<GlBankTransactionDTO> dTOList = new ArrayList<>();
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<CollectionPaymentView> getCollectionPaymentTotals(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        List<CollectionPaymentView> CollectionPaymenTotalList = new ArrayList<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder totals = new StringBuilder();
        totals.append("SELECT RIV.projectId , RIV.customerId , sum(COALESCE(RIV.dueValue,0)) FROM CollectionPaymentView RIV WHERE RIV.branchId = :branchId ");

        totals.append(appendOrganization(collectionAndPaymentReportSearchBean, params));
        totals.append(appendDate(collectionAndPaymentReportSearchBean, params));
        totals.append(appendStatus(collectionAndPaymentReportSearchBean, params));
        totals.append(appendProject(collectionAndPaymentReportSearchBean, params));
        totals.append(" GROUP BY RIV.customerId,RIV.projectId ");
        List<Object[]> res = dao.findListByQuery(totals.toString(), params);
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        CollectionPaymenTotalList = getTheTotals(res);
        return CollectionPaymenTotalList;
    }

    @Override
    public List<CollectionPaymentView> getCollectionPaymentTotalsLazey(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        List<CollectionPaymentView> CollectionPaymenTotalList = new ArrayList<>();

        params.put("branchId", collectionAndPaymentReportSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder totals = new StringBuilder();
        totals.append("SELECT RIV.projectId , RIV.customerId , sum(COALESCE(RIV.dueValue,0)) FROM CollectionPaymentView RIV WHERE RIV.branchId = :branchId ");

        totals.append(appendOrganization(collectionAndPaymentReportSearchBean, params));
        totals.append(appendDate(collectionAndPaymentReportSearchBean, params));
        totals.append(appendStatus(collectionAndPaymentReportSearchBean, params));
        totals.append(appendProject(collectionAndPaymentReportSearchBean, params));
        totals.append(" GROUP BY RIV.customerId,RIV.projectId ");
        List<Object[]> res = dao.findListByQuery(totals.toString(), params);
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        CollectionPaymenTotalList = getTheTotals(res);
        return CollectionPaymenTotalList;
    }

    @Override
    public List<CollectionPaymentTotalsTemp> getAllCollectionPaymentReport(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", collectionAndPaymentReportSearchBean.getUserId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Select t FROM CollectionPaymentTotalsTemp t where t.userId = :userId");
        List<CollectionPaymentTotalsTemp> list = dao.findListByQuery(stringBuilder.toString(), filters);
        return list;
    }

    public List<CollectionPaymentView> getTheTotals(List<Object[]> res) {

        List<CollectionPaymentView> CollectionPaymentTotalList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            CollectionPaymentView instView = new CollectionPaymentView();
            instView.setSerial((Integer) line[0]);
            instView.setCustomerId((Integer) line[1]);
            instView.setDueValue((BigDecimal) line[2]);
            CollectionPaymentTotalList.add(instView);
        }
        return CollectionPaymentTotalList;
    }

    private void mapToDTO(GlBankTransactionDTO dTO, GlBankTransaction entity) {
        dTO.setSerial(entity.getSerial());
        dTO.setDate(entity.getDate());
        dTO.setValue(entity.getValue());
        dTO.setPostFlag(entity.getPostFlag());
        dTO.setPaymentType(entity.getPaymentType());
        dTO.setOrganizationType(entity.getOrganizationType());
        dTO.setGlYear(entity.getGlYear());
        dTO.setValueLocal(entity.getValueLocal());
        dTO.setRemark(entity.getRemark());
        dTO.setRemark2(entity.getRemark2());
        dTO.setTransactionType(entity.getTransactionType());
        dTO.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
        dTO.setInstallmentId(entity.getInstallmentId() != null ? entity.getInstallmentId().getId() : null);
        dTO.setInvoiceId(entity.getInvoiceId() != null ? entity.getInvoiceId().getId() : null);
        dTO.setRate(entity.getRate());
        dTO.setChequeNumber(entity.getChequeNumber());
        dTO.setChequeDate(entity.getChequeDate());
        dTO.setChequeDueDate(entity.getChequeDueDate());
        dTO.setChequeStatus(entity.getChequeStatus());
        if (entity.getParent() != null) {
            GlBankTransactionDTO parent = new GlBankTransactionDTO();
            parent.setId(entity.getParent().getId());
            dTO.setParent(parent);

        }
        if (entity.getDocumentTypeId() != null) {
            SymbolDTO parent = new SymbolDTO();
            parent.setId(entity.getDocumentTypeId().getId());
            dTO.setDocumentTypeId(parent);

        }

        if (entity.getGlBankId() != null) {
            GlBankDTO parent = new GlBankDTO();
            parent.setId(entity.getGlBankId().getId());
            dTO.setGlBankId(parent);

        }
        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
    }

    public GlBankTransaction mapFromDTO(GlBankTransactionDTO dTO, IsagUser isagUser) {
        GlBankTransaction entity = new GlBankTransaction();
        entity.setSerial(dTO.getSerial());
        entity.setDate(dTO.getDate());
        entity.setValue(dTO.getValue());
        entity.setPostFlag(dTO.getPostFlag());
        entity.setPaymentType(dTO.getPaymentType());
        entity.setOrganizationType(dTO.getOrganizationType());
        entity.setGlYear(dTO.getGlYear());
        entity.setValueLocal(dTO.getValueLocal());
        entity.setRemark(dTO.getRemark());
        entity.setRemark2(dTO.getRemark2());
        entity.setTransactionType(dTO.getTransactionType());
        if (dTO.getGeneralJournalId() != null) {
            GeneralJournal generalJournal = new GeneralJournal();
            generalJournal.setId(dTO.getGeneralJournalId());
            entity.setGeneralJournalId(generalJournal);
        }

        if (dTO.getInstallmentId() != null) {
            InstContractDetail parent = new InstContractDetail();
            parent.setId(dTO.getInstallmentId());
            entity.setInstallmentId(parent);
        }

        if (dTO.getInvoiceId() != null) {
            InvPurchaseInvoice parent = new InvPurchaseInvoice();
            parent.setId(dTO.getInvoiceId());
            entity.setInvoiceId(parent);
        }
        entity.setRate(dTO.getRate());
        entity.setChequeNumber(dTO.getChequeNumber());
        entity.setChequeDate(dTO.getChequeDate());
        entity.setChequeDueDate(dTO.getChequeDueDate());
        entity.setChequeStatus(dTO.getChequeStatus());
        if (dTO.getParent() != null) {
            GlBankTransaction parent = new GlBankTransaction();
            parent.setId(dTO.getParent().getId());
            entity.setParent(parent);

        }
        if (dTO.getDocumentTypeId() != null) {
            Symbol parent = new Symbol();
            parent.setId(dTO.getDocumentTypeId().getId());
            entity.setDocumentTypeId(parent);

        }

        if (dTO.getGlBankId() != null) {
            GlBank parent = new GlBank();
            parent.setId(dTO.getGlBankId().getId());
            entity.setGlBankId(parent);
        }

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<GlBankTransaction> list, List<GlBankTransactionDTO> dTOList) {
        for (GlBankTransaction entity : list) {
            GlBankTransactionDTO dTO = new GlBankTransactionDTO();
            mapToDTO(dTO, entity);
            dTOList.add(dTO);
        }
    }

}
