/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlYear;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
 * @author hq004
 */
@Stateless
public class GeneraljournalDetailsServiceImpl implements GeneraljournalDetailsService {

    private Integer maxSerial = 1;
    @EJB
    private GeneraljournalDetailsService detailsService;
    @EJB
    private GenericDAO dao;

    @Override
    public GeneralJournalDetails addGenDetalils(GeneralJournalDetails details) {
        return dao.updateEntity(details);
    }

    @Override
    public void deleteGenDetalils(GeneralJournalDetails details) {
        dao.deleteEntity(details);
    }

    @Override
    public void deleteGenDetalilsByGeneralJournal(Integer generalJournal) {
        dao.executeDeleteQuery("delete from GeneralJournalDetails d WHERE d.id > 0 AND d.generalJournalId.id=" + generalJournal);
    }

    @Override
    public GeneralJournalDetails updateGenDetalils(GeneralJournalDetails details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<GeneralJournalDetails> getAllGenDetalils() {
        return dao.findAll(GeneralJournalDetails.class);
    }

    @Override
    public List<GeneralJournalDetails> getAllJournalDetailsForJorunal(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("journalId", id);
        return dao.findListByQuery("SELECT d FROM GeneralJournalDetails d WHERE d.generalJournalId.id=:journalId", params);
    }

    @Override
    public List<GeneralJournalDetails> getAllJournalDetailsForJorunalBySearchBean(Integer id, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("journalId", id);
        StringBuilder queryBuilderDetail = new StringBuilder();

        appendGeneralJournalCostCenterForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalAdminUnitForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalAccountNumberForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        List<GeneralJournalDetails> generalJournalDetails = dao.findListByQuery("SELECT d FROM GeneralJournalDetails d WHERE d.generalJournalId.id=:journalId" + queryBuilderDetail.toString(), params);
        return generalJournalDetails;
    }

    @Override
    public Integer getMaxSerialByGeneral(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("journalId", id);
        Integer max = dao.findEntityByQuery("SELECT  MAX(d.serial) FROM GeneralJournalDetails d WHERE d.generalJournalId.id=:journalId", params);
        return max;
    }

    @Override
    public BigDecimal getBalanceForGlAccount(Integer glACCOUNTId, Date startGlYear, Date endDate) {
        Map<String, Object> params = new HashMap();
        params.put("glACCOUNTId", glACCOUNTId);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0)) FROM GeneralJournalDetails gjd LEFT JOIN"
                + " gjd.generalJournalId gj  where gjd.glACCOUNTId.id = :glACCOUNTId ");

        if (startGlYear != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(startGlYear);
            stringBuilder.append(" and gj.generalData >= '").append(formatDateFrom).append("'");
        }

        if (endDate != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            stringBuilder.append(" and gj.generalData <= '").append(formatDateTo).append("'");
        }

        stringBuilder.append(" group by gjd.glACCOUNTId.id");

        BigDecimal total = dao.findEntityByQuery(stringBuilder.toString(), params);
        return total;
    }

    @Override
    public List<GeneralJournalDetails> addGenDetalilsGlobal(List<GeneralJournalDetails> details, TobyUser tobyUser, Integer generalJournalId) {
        List<GeneralJournalDetails> list = new ArrayList<>();
        GeneralJournalDetails gjd;
        if (details != null && !details.isEmpty()) {
            for (GeneralJournalDetails journalDetails : details) {
                if (journalDetails.getCostCenterId() != null && journalDetails.getCostCenterId().getId() == -1) {
                    journalDetails.setCostCenterId(null);
                }
                if (journalDetails.getAdminUnitId() != null && journalDetails.getAdminUnitId().getId() == -1) {
                    journalDetails.setAdminUnitId(null);
                }
                fillSerial(journalDetails);

                journalDetails.setBranchId(tobyUser.getBranchId());
                journalDetails.setCreationDate(new Date());
                journalDetails.setCreatedBy(tobyUser);

                if (generalJournalId != null) {
                    GeneralJournal generalJournal = new GeneralJournal();
                    generalJournal.setId(generalJournalId);
                    journalDetails.setGeneralJournalId(generalJournal);
                }

                gjd = dao.updateEntity(journalDetails);
                list.add(gjd);
            }
        }
        return list;
    }

    @Override
    public GeneralJournalDetails addGenDetalil(Boolean isDebit, BigDecimal value, Integer generalJournalId, Integer accountId, Integer costCenterId, String remark, TobyUser tobyUser) {

        GeneralJournalDetails generalJournalDetail = new GeneralJournalDetails();

        if (generalJournalId != null) {
            GeneralJournal journal = new GeneralJournal();
            journal.setId(generalJournalId);
            generalJournalDetail.setGeneralJournalId(journal);
        }
        if (isDebit) {
            generalJournalDetail.setDebitAmount(value);
            generalJournalDetail.setDebitAmountLocal(value);
        } else {
            generalJournalDetail.setCreditamount(value);
            generalJournalDetail.setCreditamountLocal(value);
        }

        if (accountId != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(accountId);
            generalJournalDetail.setGlACCOUNTId(glAccount);
        }
        generalJournalDetail.setDiscribtion(remark);
        generalJournalDetail.setRate(BigDecimal.ONE);
        Currency currency = new Currency();
        currency.setId(110);
        generalJournalDetail.setCurrencyId(currency);

        if (costCenterId != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(costCenterId);
            generalJournalDetail.setCostCenterId(costCenter);
        }
        generalJournalDetail.setBranchId(tobyUser.getBranchId());
        generalJournalDetail.setCompanyId(tobyUser.getCompanyId());
        generalJournalDetail.setCreatedBy(tobyUser);
        generalJournalDetail.setCreationDate(new Date());

        findTheMaximumSerial(generalJournalDetail);

        fillSerial(generalJournalDetail);
        generalJournalDetail = dao.updateEntity(generalJournalDetail);

        return generalJournalDetail;
    }

    @Override
    public List<GeneralJournalDetails> addGenDetalils(List<GeneralJournalDetails> details, List<GeneralJournalDetails> generalListDeleted) {
        List<GeneralJournalDetails> list = new ArrayList<>();
        GeneralJournalDetails gjd;
        if (details != null && !details.isEmpty()) {
            findTheMaximumSerial(details);
            for (GeneralJournalDetails journalDetails : details) {
                if (journalDetails.getCostCenterId() != null && journalDetails.getCostCenterId().getId() == -1) {
                    journalDetails.setCostCenterId(null);
                }
                if (journalDetails.getAdminUnitId() != null && journalDetails.getAdminUnitId().getId() == -1) {
                    journalDetails.setAdminUnitId(null);
                }
                fillSerial(journalDetails);
                gjd = dao.updateEntity(journalDetails);
                list.add(gjd);
            }
        }

        if (generalListDeleted != null && !generalListDeleted.isEmpty()) {
            for (GeneralJournalDetails journalDetails : generalListDeleted) {
                if (journalDetails.getId() != null) {
                    dao.deleteEntity(journalDetails);
                }
            }
        }
        return list;
    }

    public void fillSerial(GeneralJournalDetails generalJournalDetails) {
        if (generalJournalDetails.getSerial() == null) {
            generalJournalDetails.setSerial(maxSerial);
            maxSerial++;
        }
    }

    public void findTheMaximumSerial(List<GeneralJournalDetails> generalJournalDetails) {
        maxSerial = detailsService.getMaxSerialByGeneral(generalJournalDetails.get(0).getGeneralJournalId().getId());

        if (maxSerial == null) {
            maxSerial = 1;
        } else {
            maxSerial = maxSerial + 1;
        }
    }

    public void findTheMaximumSerial(GeneralJournalDetails generalJournalDetails) {
        maxSerial = detailsService.getMaxSerialByGeneral(generalJournalDetails.getGeneralJournalId().getId());

        if (maxSerial == null) {
            maxSerial = 1;
        } else {
            maxSerial = maxSerial + 1;
        }
    }

    @Override
    public void deleteGenDetalils(List<GeneralJournalDetails> details) {
        for (GeneralJournalDetails detail : details) {
            if (detail.getId() != null) {
                dao.deleteEntity(detail);
            }
        }
    }

    @Override
    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccount(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamount,0)) - sum(COALESCE(gjd.debitAmount,0)) , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gj.glYear.id = :glyear ");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0))  , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and gj.glYear.id = :glyear ");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);
        }

        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    @Override
    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamountLocal,0)) - sum(COALESCE(gjd.debitAmountLocal,0)) , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gac.accClass = com.toby.define.AccountClassEnum.CREDIT and gj.glYear.id = :glyear ");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmountLocal,0)) - sum(COALESCE(gjd.creditamountLocal,0))  , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and gac.accClass = com.toby.define.AccountClassEnum.DEBIT and gj.glYear.id = :glyear ");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);
        }

        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    public List<GeneralJournalDetails> getTheListBasedOnAccClass(List<Object[]> res) {

        List<GeneralJournalDetails> GeneralJournalDetailsBasedOnAccClassList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            GeneralJournalDetails detailsIterated = new GeneralJournalDetails();
            detailsIterated.setDebitAmount((BigDecimal) line[0]);
            detailsIterated.setSerial((Integer) line[1]);
            GeneralJournalDetailsBasedOnAccClassList.add(detailsIterated);
        }
        return GeneralJournalDetailsBasedOnAccClassList;
    }

    public List<GeneralJournalDetails> getTheListSumForDebitAndCreditAndBalance(List<Object[]> res, Integer debitOrCredit) {

        List<GeneralJournalDetails> GeneralJournalDetailsBasedOnAccClassList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            GeneralJournalDetails detailsIterated = new GeneralJournalDetails();
            if (debitOrCredit == 1) {
                detailsIterated.setCreditamount((BigDecimal) line[0]);
                detailsIterated.setDebitAmount((BigDecimal) line[1]);
            } else {
                detailsIterated.setDebitAmount((BigDecimal) line[0]);
                detailsIterated.setCreditamount((BigDecimal) line[1]);
            }

            if (detailsIterated.getDebitAmount().signum() == -1) {
                detailsIterated.setCreditamount(detailsIterated.getCreditamount().add(detailsIterated.getDebitAmount().multiply(BigDecimal.valueOf(-1))));
                detailsIterated.setDebitAmount(BigDecimal.ZERO);
            }
            if (detailsIterated.getCreditamount().signum() == -1) {
                detailsIterated.setDebitAmount(detailsIterated.getDebitAmount().add(detailsIterated.getCreditamount().multiply(BigDecimal.valueOf(-1))));
                detailsIterated.setCreditamount(BigDecimal.ZERO);
            }
            // debitAmountLocal will be used as balance
            detailsIterated.setDebitAmountLocal((BigDecimal) line[2]);
            /*if (detailsIterated.getDebitAmountLocal().signum() == -1) {
                detailsIterated.setDebitAmountLocal(detailsIterated.getDebitAmountLocal().multiply(BigDecimal.valueOf(-1)));
            }*/
            detailsIterated.setSerial((Integer) line[3]);
            GeneralJournalDetailsBasedOnAccClassList.add(detailsIterated);
        }
        return GeneralJournalDetailsBasedOnAccClassList;
    }

    public void makeAppendWork(CommonSearchBean commonSearchBean, Integer debitOrCredit, StringBuilder stringBuilder, Map<String, Object> params) {
        if (commonSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(commonSearchBean.getDateFrom());
            stringBuilder.append(" and gj.generalData >= '").append(formatDateFrom).append("'");
        }

        if (commonSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(commonSearchBean.getDateTo());
            stringBuilder.append(" and gj.generalData <= '").append(formatDateTo).append("'");
        }

        if (commonSearchBean.getCostCenterFrom() != null && commonSearchBean.getCostCenterFrom() > 0) {
            params.put("costCenterFrom", commonSearchBean.getCostCenterFrom());
            stringBuilder.append(" and gjd.costCenterId.id >= :costCenterFrom ");
        }

        if (commonSearchBean.getCostCenterTo() != null && commonSearchBean.getCostCenterTo() > 0) {
            params.put("costCenterTo", commonSearchBean.getCostCenterTo());
            stringBuilder.append(" and gjd.costCenterId.id <=  :costCenterTo");
        }

        if (commonSearchBean.getAdminUnitFrom() != null && commonSearchBean.getAdminUnitFrom() > 0) {
            params.put("adminUnitFrom", commonSearchBean.getAdminUnitFrom());
            stringBuilder.append(" and gjd.adminUnitId.id >= :adminUnitFrom ");
        }

        if (commonSearchBean.getAdminUnitTo() != null && commonSearchBean.getAdminUnitTo() > 0) {
            params.put("adminUnitTo", commonSearchBean.getAdminUnitTo());
            stringBuilder.append(" and gjd.adminUnitId.id <=  :adminUnitTo");
        }
        /*  if (debitOrCredit != null && debitOrCredit == 1) {
         stringBuilder.append(" and gac.accClass = com.toby.define.AccountClassEnum.CREDIT ");
         } else if (debitOrCredit != null && debitOrCredit == 2) {
         stringBuilder.append(" and gac.accClass = com.toby.define.AccountClassEnum.DEBIT ");
         }*/

        stringBuilder.append(" group by gjd.glACCOUNTId.id");
    }

    private void appendGeneralJournalCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getCostCenterFrom() != null && journalDocumentsReportSearchBean.getCostCenterFrom() > 0) {
            params.put("costCenterFrom", journalDocumentsReportSearchBean.getCostCenterFrom());
            queryBuilder.append(" and d.costCenterId.id >= :costCenterFrom ");
        }

        if (journalDocumentsReportSearchBean.getCostCenterTo() != null && journalDocumentsReportSearchBean.getCostCenterTo() > 0) {
            params.put("costCenterTo", journalDocumentsReportSearchBean.getCostCenterTo());
            queryBuilder.append(" and d.costCenterId.id <=  :costCenterTo");
        }

    }

    private void appendGeneralJournalAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getAdminUnitFrom() != null && journalDocumentsReportSearchBean.getAdminUnitFrom() > 0) {
            params.put("adminUnitFrom", journalDocumentsReportSearchBean.getAdminUnitFrom());
            queryBuilder.append(" and d.adminUnitId.id >= :adminUnitFrom ");
        }

        if (journalDocumentsReportSearchBean.getAdminUnitTo() != null && journalDocumentsReportSearchBean.getAdminUnitTo() > 0) {
            params.put("adminUnitTo", journalDocumentsReportSearchBean.getAdminUnitTo());
            queryBuilder.append(" and d.adminUnitId.id <=  :adminUnitTo");
        }

    }

    private void appendGeneralJournalAccountNumberForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getGlAccountFrom() != null && journalDocumentsReportSearchBean.getGlAccountFrom() > 0) {
            params.put("glAccountFromFrom", journalDocumentsReportSearchBean.getGlAccountFrom());
            queryBuilder.append(" and d.glACCOUNTId.id >= :glAccountFromFrom ");
        }

        if (journalDocumentsReportSearchBean.getGlAccountFrom() != null && journalDocumentsReportSearchBean.getGlAccountFrom() > 0) {
            params.put("glAccountFromTo", journalDocumentsReportSearchBean.getGlAccountFrom());
            queryBuilder.append(" and d.glACCOUNTId.id <=  :glAccountFromTo");
        }

    }

    @Override
    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(Integer branchId, Integer debitOrCredit, Integer symbolId, CommonSearchBean commonSearchBean, GlYear glyear) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            params.put("symbolId", symbolId);
            params.put("glyear", glyear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamount,0)) - sum(COALESCE(gjd.debitAmount,0)) , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.CREDIT and gj.generalType.id = :symbolId");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            params.put("symbolId", symbolId);
            params.put("glyear", glyear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0))  , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and  gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.DEBIT and gj.generalType.id = :symbolId");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClass(res);
        }

        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    @Override
    public void deleteDetaildForShiftingScreen(Integer generalJournalId) {
        dao.executeDeleteQuery("delete from GeneralJournalDetails d WHERE d.generalJournalId.id= " + generalJournalId);
    }

    /**
     * @return the maxSerial
     */
    public Integer getMaxSerial() {
        return maxSerial;
    }

    /**
     * @param maxSerial the maxSerial to set
     */
    public void setMaxSerial(Integer maxSerial) {
        this.maxSerial = maxSerial;
    }

    @Override
    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamount,0)) ,sum(COALESCE(gjd.debitAmount,0)) ,(sum(COALESCE(gjd.creditamount,0)) - sum(COALESCE(gjd.debitAmount,0))) , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gac.accClass = com.toby.define.AccountClassEnum.CREDIT");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListSumForDebitAndCreditAndBalance(res, debitOrCredit);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)),sum(COALESCE(gjd.creditamount,0)) ,(sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0)))  , gjd.glACCOUNTId.id FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and gac.accClass = com.toby.define.AccountClassEnum.DEBIT");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListSumForDebitAndCreditAndBalance(res, debitOrCredit);
        }
        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    @Override
    public List<GeneralJournalDetails> getTheSumOfDebitAndSumOfCreditForAssetsAndDeductionsGlAccounts(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glyear) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            params.put("glyear", glyear.getId());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamount,0)) - sum(COALESCE(gjd.debitAmount,0)) , gjd.glACCOUNTId.id , gac.accClass FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.CREDIT and (gac.accGroup = com.toby.define.AccountGroupEnum.ASSETS OR gac.accGroup = com.toby.define.AccountGroupEnum.DEDUCTION)");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClassDebitOrCredit(res);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            params.put("glyear", glyear.getId());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0))  , gjd.glACCOUNTId.id , gac.accClass FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.DEBIT and (gac.accGroup = com.toby.define.AccountGroupEnum.ASSETS OR gac.accGroup = com.toby.define.AccountGroupEnum.DEDUCTION)");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccClassDebitOrCredit(res);
        }

        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    @Override
    public List<GeneralJournalDetails> getTheNetProfitForIncomeAndExpensesGlAccounts(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap();

        List<GeneralJournalDetails> generalJournalDetailsBasedOnAccClassFirsList = new ArrayList<>();
        if (debitOrCredit != null && debitOrCredit == 1) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.creditamount,0)) - sum(COALESCE(gjd.debitAmount,0)) , gjd.glACCOUNTId.id ,gac.accGroup FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac where gjd.branchId.id = :branchId and gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.CREDIT and (gac.accGroup = com.toby.define.AccountGroupEnum.INCOME OR gac.accGroup = com.toby.define.AccountGroupEnum.EXPENSES)");
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccGroup(res);

        } else if (debitOrCredit != null && debitOrCredit == 2) {
            params.put("branchId", branchId);
            params.put("glyear", glYear.getId());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT sum(COALESCE(gjd.debitAmount,0)) - sum(COALESCE(gjd.creditamount,0))  , gjd.glACCOUNTId.id ,gac.accGroup FROM GeneralJournalDetails gjd LEFT JOIN"
                    + " gjd.generalJournalId gj LEFT JOIN gjd.glACCOUNTId gac  where gjd.branchId.id = :branchId and gj.glYear.id = :glyear and gac.accClass = com.toby.define.AccountClassEnum.DEBIT and (gac.accGroup = com.toby.define.AccountGroupEnum.INCOME OR gac.accGroup = com.toby.define.AccountGroupEnum.EXPENSES)");
            debitOrCredit = 2;
            makeAppendWork(commonSearchBean, debitOrCredit, stringBuilder, params);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            generalJournalDetailsBasedOnAccClassFirsList = getTheListBasedOnAccGroup(res);
        }
        return generalJournalDetailsBasedOnAccClassFirsList;
    }

    public List<GeneralJournalDetails> getTheListBasedOnAccGroup(List<Object[]> res) {

        List<GeneralJournalDetails> GeneralJournalDetailsBasedOnAccClassList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            GeneralJournalDetails detailsIterated = new GeneralJournalDetails();
            detailsIterated.setDebitAmount((BigDecimal) line[0]);
            detailsIterated.setSerial((Integer) line[1]);
            com.toby.define.AccountGroupEnum x = (com.toby.define.AccountGroupEnum) line[2];
            detailsIterated.setDiscribtion(x.toString());
            GeneralJournalDetailsBasedOnAccClassList.add(detailsIterated);
        }
        return GeneralJournalDetailsBasedOnAccClassList;
    }

    public List<GeneralJournalDetails> getTheListBasedOnAccClassDebitOrCredit(List<Object[]> res) {

        List<GeneralJournalDetails> GeneralJournalDetailsBasedOnAccClassList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            GeneralJournalDetails detailsIterated = new GeneralJournalDetails();
            detailsIterated.setDebitAmount((BigDecimal) line[0]);
            detailsIterated.setSerial((Integer) line[1]);
            com.toby.define.AccountClassEnum x = (com.toby.define.AccountClassEnum) line[2];
            detailsIterated.setDiscribtion(x.toString());

            if (detailsIterated.getDebitAmount().signum() == -1) {
                detailsIterated.setDebitAmount(detailsIterated.getDebitAmount().multiply(BigDecimal.valueOf(-1)));
                if (detailsIterated.getDiscribtion().contains("CREDIT")) {
                    detailsIterated.setDiscribtion("DEBIT");
                } else {
                    detailsIterated.setDiscribtion("CREDIT");
                }
            }

            GeneralJournalDetailsBasedOnAccClassList.add(detailsIterated);
        }
        return GeneralJournalDetailsBasedOnAccClassList;
    }

}
