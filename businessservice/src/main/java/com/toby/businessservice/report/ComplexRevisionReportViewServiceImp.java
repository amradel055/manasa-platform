package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.CostCenter;
import com.toby.entity.GlYear;
import com.toby.views.ComplexRevisionBalance;
import java.util.List;

import javax.ejb.EJB;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.Stateless;

@Stateless
public class ComplexRevisionReportViewServiceImp implements ComplexRevisionReportViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ComplexRevisionBalance> getComplexRevisionBalanceList() {
        List<ComplexRevisionBalance> accountsBalances = dao.findListByQuery("SELECT crb FROM ComplexRevisionBalance crb");
        if (accountsBalances != null && !accountsBalances.isEmpty()) {
            return accountsBalances;
        }
        return null;
    }

    public StringBuilder createSql(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        StringBuilder sql = new StringBuilder();

        return sql;
    }

    @Override
    public List<ComplexRevisionBalance> getALLRevisionBalanceReport(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        //params.put("glCostCenterId", complexRevisionBalanceSearchBean.getGlCostCenterId());
        StringBuilder queryBuilder = new StringBuilder();
        appendcomplexNumberForQueryBuilder(queryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexCostCenterForQueryBuilder(queryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexLevelsForQueryBuilder(queryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForQueryBuilder(queryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilder(queryBuilder, params, complexRevisionBalanceSearchBean);
        String query = "SELECT crb FROM ComplexRevisionBalance crb WHERE crb.glbranchId = :branchId " + queryBuilder.toString();
        List<ComplexRevisionBalance> complexRevisionBalance = dao.findListByQuery(query, params);
        return complexRevisionBalance;
    }

    private void appendcomplexNumberForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getAccountFrom() != null && complexRevisionBalanceSearchBean.getAccountFrom() > 0) {
            params.put("accountFrom", complexRevisionBalanceSearchBean.getAccountFrom());
            queryBuilder.append(" and crb.glAccountNumber >= :accountFrom ");
        }
        if (complexRevisionBalanceSearchBean.getAccountTo() != null && complexRevisionBalanceSearchBean.getAccountTo() > 0) {
            params.put("accountTo", complexRevisionBalanceSearchBean.getAccountTo());
            queryBuilder.append(" and crb.glAccountNumber <= :accountTo");
        }

    }

    private void appendcomplexCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
//        if (complexRevisionBalanceSearchBean.getCostCenterFrom() != null && complexRevisionBalanceSearchBean.getCostCenterFrom() > 0) {
//            params.put("costCenterFrom", complexRevisionBalanceSearchBean.getCostCenterFrom());
//            queryBuilder.append(" and crb.glCostCenterId >= :costCenterFrom ");
//        }
//
//        if (complexRevisionBalanceSearchBean.getCostCenterTo() != null && complexRevisionBalanceSearchBean.getCostCenterTo() > 0) {
//            params.put("costCenterTo", complexRevisionBalanceSearchBean.getCostCenterTo());
//            queryBuilder.append(" and crb.glCostCenterId <=  :costCenterTo");
//        }

        if(complexRevisionBalanceSearchBean.getCostCenterList() != null && !complexRevisionBalanceSearchBean.getCostCenterList().isEmpty()){
            StringBuilder costCenter = null;
            for(CostCenter costCenterid : complexRevisionBalanceSearchBean.getCostCenterList()){
                if(costCenter == null){
                    costCenter = new StringBuilder();
                    costCenter.append(costCenterid.getId());
                }else{
                    costCenter.append(",").append(costCenterid.getId());
                }
            }
            queryBuilder.append(" and crb.glCostCenterId in (").append(costCenter.toString()).append(")");
        }
    }

    private void appendcomplexAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getAdminUnitFrom() != null && complexRevisionBalanceSearchBean.getAdminUnitFrom() > 0) {
            params.put("adminUnitFrom", complexRevisionBalanceSearchBean.getAdminUnitFrom());
            queryBuilder.append(" and crb.glAdminUnitId >= :adminUnitFrom ");
        }

        if (complexRevisionBalanceSearchBean.getAdminUnitTo() != null && complexRevisionBalanceSearchBean.getAdminUnitTo() > 0) {
            params.put("adminUnitTo", complexRevisionBalanceSearchBean.getAdminUnitTo());
            queryBuilder.append(" and crb.glAdminUnitId <=  :adminUnitTo");
        }

    }

    private void appendcomplexLevelsForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {

        if (complexRevisionBalanceSearchBean.getLevelFrom() != null && complexRevisionBalanceSearchBean.getLevelFrom() > 0) {
            params.put("levelFrom", complexRevisionBalanceSearchBean.getLevelFrom());
            queryBuilder.append(" and crb.level >= :levelFrom ");
        }
        if (complexRevisionBalanceSearchBean.getLevelTo() != null && complexRevisionBalanceSearchBean.getLevelTo() > 0) {
            params.put("levelTo", complexRevisionBalanceSearchBean.getLevelTo());
            queryBuilder.append(" and crb.level <= :levelTo");
        }

    }

    private void appendcomplexDateForQueryBuilder(StringBuilder queryBuilderForAny, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getPeriodFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodFrom());
            queryBuilderForAny.append(" and crb.date >= '").append(formatDateFrom).append("'");
        }

        if (complexRevisionBalanceSearchBean.getPeriodTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodTo());
            queryBuilderForAny.append(" and crb.date <= '").append(formatDateTo).append("'");
        }
        if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
            String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom() != null ? complexRevisionBalanceSearchBean.getDateFrom() : complexRevisionBalanceSearchBean.getPeriodFrom());
            queryBuilderForAny.append(" and crb.date >= '").append(formatDatefrom).append("'");
        }
        if (complexRevisionBalanceSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateTo() != null ? complexRevisionBalanceSearchBean.getDateTo() : complexRevisionBalanceSearchBean.getPeriodTo());
            queryBuilderForAny.append(" and crb.date <= '").append(formatDateTo).append("'");
        }
    }

    @Override
    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCredit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder firstQueryBuilder = new StringBuilder();;
        String query = "SELECT crb.glAccountNumber , sum(COALESCE(crb.debit,0)) , sum(COALESCE(crb.credit,0))  FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId   ";
        firstQueryBuilder.append(query);
        if (complexRevisionBalanceSearchBean.isPostFlag()) {
            firstQueryBuilder.append(" and crb.postFlag = true ");
        }
        complexRevisionBalanceSearchBean.setDateTo(complexRevisionBalanceSearchBean.getPeriodTo());
        complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexCostCenterForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
//        appendcomplexLevelsForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForQueryBuilder(firstQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        firstQueryBuilder.append(" group by crb.glAccountId");
        List<Object[]> res = dao.findListByQuery(firstQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setGlAccountNumber((Integer) line[0]);
            complexIterated.setDebit((BigDecimal) line[1]);
            complexIterated.setCredit((BigDecimal) line[2]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

    @Override
    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnCostCenter(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder firstQueryBuilder = new StringBuilder();;
        String query = "SELECT crb.glAccountNumber , sum(COALESCE(crb.debit,0)) , sum(COALESCE(crb.credit,0))  FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId   ";
        firstQueryBuilder.append(query);
        complexRevisionBalanceSearchBean.setDateTo(complexRevisionBalanceSearchBean.getPeriodTo());
        //complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        //complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexBasedOnCostCenterForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean, stringBuilder);
//        appendcomplexLevelsForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForQueryBuilder(firstQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        firstQueryBuilder.append(" group by crb.glAccountId");
        List<Object[]> res = dao.findListByQuery(firstQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setGlAccountNumber((Integer) line[0]);
            complexIterated.setDebit((BigDecimal) line[1]);
            complexIterated.setCredit((BigDecimal) line[2]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

    @Override
    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnAdminUnit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder firstQueryBuilder = new StringBuilder();;
        String query = "SELECT crb.glAccountNumber , sum(COALESCE(crb.debit,0)) , sum(COALESCE(crb.credit,0))  FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId   ";
        firstQueryBuilder.append(query);
        complexRevisionBalanceSearchBean.setDateTo(complexRevisionBalanceSearchBean.getPeriodTo());
        // complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        // complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexBasedOnAdminUnitForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean, stringBuilder);
//        appendcomplexLevelsForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForQueryBuilder(firstQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilder(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        firstQueryBuilder.append(" group by crb.glAccountId");
        List<Object[]> res = dao.findListByQuery(firstQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setGlAccountNumber((Integer) line[0]);
            complexIterated.setDebit((BigDecimal) line[1]);
            complexIterated.setCredit((BigDecimal) line[2]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

    private void appendcomplexDateForSecondQueryBuilder(StringBuilder secondQueryBuilder, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getPeriodFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodFrom());
            secondQueryBuilder.append(" and crb.date >= '").append(formatDateFrom).append("'");
        }

        if (complexRevisionBalanceSearchBean.getPeriodTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodTo());
            secondQueryBuilder.append(" and crb.date <= '").append(formatDateTo).append("'");
        }
        if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
            String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom());
            secondQueryBuilder.append(" and crb.date < '").append(formatDatefrom).append("'");
        }
    }

    private void appendcomplexBasedOnCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, StringBuilder stringBuilder) {
        if (stringBuilder != null && stringBuilder.length() > 0) {
            queryBuilder.append(" and crb.glCostCenterId in (").append(stringBuilder).append(")");
        }
    }

    private void appendcomplexBasedOnAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, StringBuilder stringBuilder) {
        if (stringBuilder != null && stringBuilder.length() > 0) {
            queryBuilder.append(" and crb.glAdminUnitId in (").append(stringBuilder).append(")");
        }
    }

    /*
    
     SELECT cr.glAccountId AS `glAccountNumber` , 
     (sum(ifnull(debit,0)) - sum(ifnull(credit,0))) AS balance , level AS `level`
     FROM toby.complex_revision_balance cr 
     where glBranchId = 81 and date < ?datefrom and date >= ?datefirstperiod
     group by cr.glAccountId , cr.level;
    
     */
    @Override
    public List<ComplexRevisionBalance> getFirstPeriodByBranchId(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder secondQueryBuilder = new StringBuilder();
        String query = "SELECT crb.glAccountId glAccountId , CASE WHEN (crb.accountClass = com.toby.define.AccountClassEnum.DEBIT) THEN (sum(COALESCE(crb.debit,0)) - sum(COALESCE(crb.credit,0))) ELSE (sum(COALESCE(crb.credit,0)) - sum(COALESCE(crb.debit,0))) END FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId ";
        secondQueryBuilder.append(query);
        if (complexRevisionBalanceSearchBean.isPostFlag()) {
            secondQueryBuilder.append(" and crb.postFlag = true ");
        }
        complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilder(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexCostCenterForQueryBuilder(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
//        appendcomplexLevelsForQueryBuilder(queryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForSecondQueryBuilder(secondQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilder(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
        secondQueryBuilder.append("  group by crb.glAccountId ");
        List<Object[]> res = dao.findListByQuery(secondQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setGlAccountNumber((Integer) line[0]);
            complexIterated.setDebit((BigDecimal) line[1]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

}
