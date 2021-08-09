package com.isag.businessservice.reports;

import java.util.List;

import javax.ejb.EJB;

import com.isag.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.GlYear;
import com.isag.views.ComplexRevisionBalance;
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
    private void appendSettingOpenningBalance(StringBuilder queryBuilder, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.isIsOpenningBalance() && complexRevisionBalanceSearchBean.getDateFrom().equals(complexRevisionBalanceSearchBean.getPeriodFrom())) {
            queryBuilder.append(" and crb.symbolSerial != 0 ");
        }

    }

    private void appendcomplexCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getCostCenterFrom() != null && complexRevisionBalanceSearchBean.getCostCenterFrom() > 0) {
            params.put("costCenterFrom", complexRevisionBalanceSearchBean.getCostCenterFrom());
            queryBuilder.append(" and crb.glCostCenterId >= :costCenterFrom ");
        }

        if (complexRevisionBalanceSearchBean.getCostCenterTo() != null && complexRevisionBalanceSearchBean.getCostCenterTo() > 0) {
            params.put("costCenterTo", complexRevisionBalanceSearchBean.getCostCenterTo());
            queryBuilder.append(" and crb.glCostCenterId <=  :costCenterTo");
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
        appendSettingOpenningBalance(firstQueryBuilder, complexRevisionBalanceSearchBean);
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

//        if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
//            String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom());
//            secondQueryBuilder.append(" and crb.date < '").append(formatDatefrom).append("'");
//        }
        if (complexRevisionBalanceSearchBean.isIsOpenningBalance()) {
            secondQueryBuilder.append(" and crb.symbolSerial = 0 ");
        } else {
            if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
                String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom());
                secondQueryBuilder.append(" and crb.date < '").append(formatDatefrom).append("'");
            }
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
     FROM isag.complex_revision_balance cr 
     where glBranchId = 81 and date < ?datefrom and date >= ?datefirstperiod
     group by cr.glAccountId , cr.level;
    
     */
    @Override
    public List<ComplexRevisionBalance> getFirstPeriodByBranchId(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder secondQueryBuilder = new StringBuilder();
        String query = "SELECT crb.glAccountId glAccountId , CASE WHEN (crb.accountClass = com.isag.define.AccountClassEnum.DEBIT) THEN (sum(COALESCE(crb.debit,0)) - sum(COALESCE(crb.credit,0))) ELSE (sum(COALESCE(crb.credit,0)) - sum(COALESCE(crb.debit,0))) END  , sum(COALESCE(crb.debit,0)) sumDebit , sum(COALESCE(crb.credit,0) )sumCredit FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId ";
        appendComplexRevision(secondQueryBuilder, query, complexRevisionBalanceSearchBean, glYear, params);
        List<Object[]> res = dao.findListByQuery(secondQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setGlAccountNumber((Integer) line[0]);
            complexIterated.setDebit((BigDecimal) line[1]);
            complexIterated.setDebitOpenningBalance((BigDecimal) line[2]);
            complexIterated.setCreditOpenningBalance((BigDecimal) line[3]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

    @Override
    public List<ComplexRevisionBalance> getFirstPeriodByBranchIdSeperate(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder secondQueryBuilder = new StringBuilder();
        String query = "SELECT sum(COALESCE(crb.debit,0)) sumDebit , sum(COALESCE(crb.credit,0) )sumCredit FROM ComplexRevisionBalance crb WHERE crb.glBranchId = :branchId ";
        appendComplexRevision(secondQueryBuilder, query, complexRevisionBalanceSearchBean, glYear, params);
        List<Object[]> res = dao.findListByQuery(secondQueryBuilder.toString(), params);
        List<ComplexRevisionBalance> complexRevisionBalance = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ComplexRevisionBalance complexIterated = new ComplexRevisionBalance();
            complexIterated.setDebit((BigDecimal) line[0]);
            complexIterated.setCredit((BigDecimal) line[1]);
            complexRevisionBalance.add(complexIterated);
        }
        return complexRevisionBalance;
    }

    private void appendComplexRevision(StringBuilder secondQueryBuilder, String query, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, Map<String, Object> params) {
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
    }

    /////////////// AMR //////////////////////////////////
    @Override
    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditDTO(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder firstQueryBuilder = new StringBuilder();;
        String query = "SELECT crb.glACCOUNTId.id , sum(COALESCE(crb.debitAmountLocal,0)) , sum(COALESCE(crb.creditamountLocal,0))  "
                + "     FROM GeneralJournalDetails crb WHERE crb.generalJournalId.branchId.id = :branchId   ";
        firstQueryBuilder.append(query);
        if (complexRevisionBalanceSearchBean.isPostFlag()) {
            firstQueryBuilder.append(" and crb.generalJournalId.post_flag = true ");
        }
        complexRevisionBalanceSearchBean.setDateTo(complexRevisionBalanceSearchBean.getPeriodTo());
        complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilderDTO(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexCostCenterForQueryBuilderDTO(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForQueryBuilderDTO(firstQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilderDTO(firstQueryBuilder, params, complexRevisionBalanceSearchBean);
        firstQueryBuilder.append(" group by crb.glACCOUNTId.id");
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

    private void appendcomplexNumberForQueryBuilderDTO(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getAccountFrom() != null && complexRevisionBalanceSearchBean.getAccountFrom() > 0) {
            params.put("accountFrom", complexRevisionBalanceSearchBean.getAccountFrom());
            queryBuilder.append(" and crb.glACCOUNTId.accNumber >= :accountFrom ");
        }
        if (complexRevisionBalanceSearchBean.getAccountTo() != null && complexRevisionBalanceSearchBean.getAccountTo() > 0) {
            params.put("accountTo", complexRevisionBalanceSearchBean.getAccountTo());
            queryBuilder.append(" and crb.glACCOUNTId.accNumber <= :accountTo");
        }

    }

    private void appendcomplexCostCenterForQueryBuilderDTO(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getCostCenterFrom() != null && complexRevisionBalanceSearchBean.getCostCenterFrom() > 0) {
            params.put("costCenterFrom", complexRevisionBalanceSearchBean.getCostCenterFrom());
            queryBuilder.append(" and crb.costCenterId.code >= :costCenterFrom ");
        }

        if (complexRevisionBalanceSearchBean.getCostCenterTo() != null && complexRevisionBalanceSearchBean.getCostCenterTo() > 0) {
            params.put("costCenterTo", complexRevisionBalanceSearchBean.getCostCenterTo());
            queryBuilder.append(" and crb.costCenterId.code <=  :costCenterTo");
        }

    }

    private void appendcomplexDateForQueryBuilderDTO(StringBuilder queryBuilderForAny, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getPeriodFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodFrom());
            queryBuilderForAny.append(" and crb.generalJournalId.generalData >= '").append(formatDateFrom).append("'");
        }

        if (complexRevisionBalanceSearchBean.getPeriodTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodTo());
            queryBuilderForAny.append(" and crb.generalJournalId.generalData <= '").append(formatDateTo).append("'");
        }
        if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
            String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom() != null ? complexRevisionBalanceSearchBean.getDateFrom() : complexRevisionBalanceSearchBean.getPeriodFrom());
            queryBuilderForAny.append(" and crb.generalJournalId.generalData >= '").append(formatDatefrom).append("'");
        }
        if (complexRevisionBalanceSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateTo() != null ? complexRevisionBalanceSearchBean.getDateTo() : complexRevisionBalanceSearchBean.getPeriodTo());
            queryBuilderForAny.append(" and crb.generalJournalId.generalData <= '").append(formatDateTo).append("'");
        }
    }

    private void appendcomplexAdminUnitForQueryBuilderDTO(StringBuilder queryBuilder, Map<String, Object> params, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getAdminUnitFrom() != null && complexRevisionBalanceSearchBean.getAdminUnitFrom() > 0) {
            params.put("adminUnitFrom", complexRevisionBalanceSearchBean.getAdminUnitFrom());
            queryBuilder.append(" and crb.adminUnitId.code >= :adminUnitFrom ");
        }

        if (complexRevisionBalanceSearchBean.getAdminUnitTo() != null && complexRevisionBalanceSearchBean.getAdminUnitTo() > 0) {
            params.put("adminUnitTo", complexRevisionBalanceSearchBean.getAdminUnitTo());
            queryBuilder.append(" and crb.adminUnitId.code <=  :adminUnitTo");
        }

    }

    @Override
    public List<ComplexRevisionBalance> getFirstPeriodByBranchIdDTO(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder secondQueryBuilder = new StringBuilder();
        String query = "SELECT crb.glACCOUNTId.id glAccountId , "
                + " CASE WHEN (crb.glACCOUNTId.accClass = com.isag.define.AccountClassEnum.DEBIT) THEN (sum(COALESCE(crb.debitAmountLocal,0)) - sum(COALESCE(crb.creditamountLocal,0))) "
                + " ELSE (sum(COALESCE(crb.creditamountLocal,0)) - sum(COALESCE(crb.debitAmountLocal,0))) END "
                + " FROM GeneralJournalDetails crb WHERE crb.generalJournalId.branchId.id = :branchId ";
        secondQueryBuilder.append(query);
        if (complexRevisionBalanceSearchBean.isPostFlag()) {
            secondQueryBuilder.append(" and crb.generalJournalId.post_flag = true ");
        }
        complexRevisionBalanceSearchBean.setPeriodTo(glYear.getDateTo());
        complexRevisionBalanceSearchBean.setDateFrom(complexRevisionBalanceSearchBean.getPeriodFrom());
        complexRevisionBalanceSearchBean.setPeriodFrom(glYear.getDateFrom());
        appendcomplexNumberForQueryBuilderDTO(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexCostCenterForQueryBuilderDTO(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
        appendcomplexDateForSecondQueryBuilderDTO(secondQueryBuilder, complexRevisionBalanceSearchBean);
        appendcomplexAdminUnitForQueryBuilderDTO(secondQueryBuilder, params, complexRevisionBalanceSearchBean);
        secondQueryBuilder.append(" group by crb.glACCOUNTId.id");
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

    private void appendcomplexDateForSecondQueryBuilderDTO(StringBuilder secondQueryBuilder, ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        if (complexRevisionBalanceSearchBean.getPeriodFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodFrom());
            secondQueryBuilder.append(" and crb.generalJournalId.generalData >= '").append(formatDateFrom).append("'");
        }

        if (complexRevisionBalanceSearchBean.getPeriodTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getPeriodTo());
            secondQueryBuilder.append(" and crb.generalJournalId.generalData <= '").append(formatDateTo).append("'");
        }

        if (!complexRevisionBalanceSearchBean.isIsOpenningBalance()) {
            secondQueryBuilder.append(" and crb.generalJournalId.generalType.serial = 0 ");
        } else {
            if (complexRevisionBalanceSearchBean.getDateFrom() != null) {
                String formatDatefrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBean.getDateFrom());
                secondQueryBuilder.append(" and crb.generalJournalId.generalData < '").append(formatDatefrom).append("'");
            }
        }

    }

}
