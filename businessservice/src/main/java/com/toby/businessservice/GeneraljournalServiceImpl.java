/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlYear;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvReturnPurchase;
import com.toby.views.PostRestrictionsView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class GeneraljournalServiceImpl implements GeneraljournalService {

    @EJB
    private GenericDAO dao;

    @EJB
    private GeneraljournalDetailsService detailsService;

    @EJB
    private GlBankTransactionService glBankTransactionService;

    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;

    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;

    @Override
    public synchronized GeneralJournal addGeneralJournal(GeneralJournal generalJournal) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", generalJournal.getBranchId().getId());
        params.put("companyId", generalJournal.getCompanyId().getId());
        params.put("glYear", generalJournal.getGlYear().getId());
        Map<String, Object> params1 = new HashMap<>();
        params1.put("generalType", generalJournal.getGeneralType().getId());
        params1.put("glYear", generalJournal.getGlYear().getId());
        params1.put("branchId", generalJournal.getBranchId().getId());

        Integer generaldocumet = dao.findEntityByQuery("SELECT MAX(g.generalDecument) FROM GeneralJournal g  WHERE g.glYear.id =:glYear AND g.generalType.id =:generalType and g.branchId.id =:branchId  ", params1);

        if (generaldocumet == null) {
            generaldocumet = 1;

        } else {
            generaldocumet = generaldocumet + 1;
        }
        Integer max = 0;
        synchronized (max) {
            try {
                max = dao.findEntityByQuery("SELECT MAX(g.serial) FROM GeneralJournal g  WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId and g.glYear.id =:glYear  ", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (max == null) {
            max = 0;
        }
        // generalJournal.setSerial(25);
        if (generalJournal.getId() == null) {
            generalJournal.setSerial(max + 1);
            if (generalJournal.getGeneralType() != null && (generalJournal.getGeneralType().getSerial() < 1000 || generalJournal.getGeneralType().getSerial() > 2000)) {
                generalJournal.setGeneralDecument(generaldocumet);
            }
        }
        return dao.updateEntity(generalJournal);
    }

    @Override
    public GeneralJournal addGeneralJournalFromExcel(GeneralJournal generalJournal) {
        return dao.updateEntity(generalJournal);
    }

    @Override
    public synchronized void addGeneralJournalsForReviewing(List<GeneralJournal> generalJournalList) {
        if (generalJournalList != null && !generalJournalList.isEmpty()) {
            for (GeneralJournal gen : generalJournalList) {
                dao.updateEntity(gen);
            }
        }
    }

    @Override
    public void deleteGeneralJournal(GeneralJournal generalJournal) {
        dao.deleteEntity(generalJournal);
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournal() {
        return dao.findAll(GeneralJournal.class);
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournalByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(GeneralJournal.class, Id);

    }

    @Override
    public List<GeneralJournalDetails> getALLGeneralJournalDetailsByGeneralJournal(Integer generalJournalId) {
        Map<String, Object> params = new HashMap<>();
        params.put("generalJournalId", generalJournalId);
        List<GeneralJournalDetails> detailses = dao.findListByQuery("SELECT e FROM GeneralJournalDetails e WHERE e.generalJournalId.id = :generalJournalId", params);
        return detailses;
    }

    @Override
    public BigDecimal getCreditAmountForGlAccount(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", id);
        return dao.findEntityByQuery("SELECT SUM(details.creditamount) FROM GeneralJournalDetails details WHERE details.glACCOUNTId.id=:accountId", params);
    }

    @Override
    public BigDecimal getDebitAmountForGlAccount(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", id);
        return dao.findEntityByQuery("SELECT SUM(details.debitAmount) FROM GeneralJournalDetails details WHERE details.glACCOUNTId.id=:accountId", params);
    }

    @Override
    public List<GeneralJournalDetails> getALLGenDetailsBYGlAccount(Integer Id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GeneralJournalDetails> getALLGenDetailsBYCostCenter(Integer Id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GeneralJournalDetails> getALLGenDetailsBYAdminUnit(Integer Id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getMaxSerialToSelectedDocumentsType(Integer selectedDocumentsType) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", selectedDocumentsType);
        Integer max = dao.findEntityByQuery("SELECT MAX(g.generalDecument) FROM GeneralJournal g  WHERE g.generalType.id=:id", params);
        return max;
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournalByBranchId(Integer selectedBranch) {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedBranch", selectedBranch);
        List<GeneralJournal> generalJournal = dao.findListByQuery("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :selectedBranch", params);
        return generalJournal;
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournalByBranchIdAndYear(Integer selectedBranch, GlYear year) {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedBranch", selectedBranch);
        params.put("dateFrom", year.getDateFrom());
        params.put("dateTo", year.getDateTo());
        List<GeneralJournal> generalJournal = dao.findListByQuery("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :selectedBranch AND e.generalData >= :dateFrom AND e.generalData <= :dateTo order by e.serial desc", params);
        return generalJournal;
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournalByBranchIdAndYearAndSymbolId(Integer selectedBranch, GlYear year, Integer symbolId) {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedBranch", selectedBranch);
        params.put("dateFrom", year.getDateFrom());
        params.put("dateTo", year.getDateTo());
        params.put("symbolId", symbolId);

        List<GeneralJournal> generalJournal = dao.findListByQuery("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :selectedBranch AND e.generalData >= :dateFrom AND e.generalData <= :dateTo AND e.generalType.id=:symbolId order by e.serial desc", params);
        return generalJournal;
    }

    @Override
    public List<GeneralJournal> getALLGeneralJournalDocumentsReport(JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", journalDocumentsReportSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder queryBuilderDetail = new StringBuilder();
        appendJournalNumberForQueryBuilder(queryBuilder, params, journalDocumentsReportSearchBean);
        appendDoocumentTypeForQueryBuilder(queryBuilder, params, journalDocumentsReportSearchBean);
        appendDoocumentumForQueryBuilder(queryBuilder, params, journalDocumentsReportSearchBean);
        appendUserForQueryBuilder(queryBuilder, params, journalDocumentsReportSearchBean);
        appendRevisionForQueryBuilder(queryBuilder, params, journalDocumentsReportSearchBean);
        appendGlAccountForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalCostCenterForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalAdminUnitForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalDateForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        appendGeneralJournalAmountForQueryBuilder(queryBuilderDetail, params, journalDocumentsReportSearchBean);
        List<GeneralJournal> generalJournal = dao.findListByQuery("SELECT e FROM GeneralJournal e WHERE  e.branchId.id = :branchId and e.id in (select distinct d.generalJournalId.id from GeneralJournalDetails d where d.branchId.id = :branchId" + queryBuilderDetail.toString() + ")" + queryBuilder.toString(), params);
        return generalJournal;
    }

    private void appendRevisionForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getPostFlag() != null && !journalDocumentsReportSearchBean.getPostFlag().equals("")) {
            int postFlagBeforConversion = Integer.parseInt(journalDocumentsReportSearchBean.getPostFlag());
            boolean post_flag;
            if (postFlagBeforConversion == 1) {
                post_flag = Boolean.TRUE;
            } else {
                post_flag = Boolean.FALSE;
            }
            params.put("post_flag", post_flag);
            queryBuilder.append(" and e.post_flag = :post_flag ");
        }
    }

    private void appendDoocumentTypeForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getDocumentTypeFrom() != null && journalDocumentsReportSearchBean.getDocumentTypeFrom() != 0) {
            params.put("documentTypeFrom", journalDocumentsReportSearchBean.getDocumentTypeFrom());
            queryBuilder.append(" and e.generalType.id >= :documentTypeFrom ");
        }
        if (journalDocumentsReportSearchBean.getDocumentTypeTo() != null && journalDocumentsReportSearchBean.getDocumentTypeTo() != 0) {
            params.put("documentTypeTo", journalDocumentsReportSearchBean.getDocumentTypeTo());
            queryBuilder.append(" and e.generalType.id <= :documentTypeTo");
        }
    }

    private void appendDoocumentumForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getDocumentNumForm() != null && journalDocumentsReportSearchBean.getDocumentNumForm() != 0) {
            params.put("documentNumForm", journalDocumentsReportSearchBean.getDocumentNumForm());
            queryBuilder.append(" and e.generalDecument >= :documentNumForm ");
        }

        if (journalDocumentsReportSearchBean.getDocumentNumTo() != null && journalDocumentsReportSearchBean.getDocumentNumTo() != 0) {
            params.put("documentNumTo", journalDocumentsReportSearchBean.getDocumentNumTo());
            queryBuilder.append(" and e.generalDecument <= :documentNumTo");
        }
    }

    private void appendJournalNumberForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getJournalNumTo() != null && journalDocumentsReportSearchBean.getJournalNumTo() != 0) {
            params.put("journalNumTo", journalDocumentsReportSearchBean.getJournalNumTo());
            queryBuilder.append(" and e.serial <= :journalNumTo");
        }

        if (journalDocumentsReportSearchBean.getJournalNumForm() != null && journalDocumentsReportSearchBean.getJournalNumForm() != 0) {
            params.put("journalNumFrom", journalDocumentsReportSearchBean.getJournalNumForm());
            queryBuilder.append(" and e.serial >= :journalNumFrom ");
        }
    }

    private void appendUserForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getUserFrom() != null && journalDocumentsReportSearchBean.getUserFrom() != 0) {
            params.put("userFrom", journalDocumentsReportSearchBean.getUserFrom());
            queryBuilder.append(" and e.createdBy.id >= :userFrom ");
        }

        if (journalDocumentsReportSearchBean.getUserTo() != null && journalDocumentsReportSearchBean.getUserTo() != 0) {
            params.put("userTo", journalDocumentsReportSearchBean.getUserTo());
            queryBuilder.append(" and e.createdBy.id <= :userTo");
        }
    }

    private void appendGlAccountForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getGlAccountFrom() != null && journalDocumentsReportSearchBean.getGlAccountFrom() != 0) {
            params.put("glAccount", journalDocumentsReportSearchBean.getGlAccountFrom());
            queryBuilder.append(" and d.glACCOUNTId.id >= :glAccount ");
        }

        if (journalDocumentsReportSearchBean.getGlAccountTo() != null && journalDocumentsReportSearchBean.getGlAccountTo() != 0) {
            params.put("glAccount", journalDocumentsReportSearchBean.getGlAccountTo());
            queryBuilder.append(" and d.glACCOUNTId.id <= :glAccount");
        }
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
    
    private void appendGeneralJournalAmountForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getValue() != null) {
            params.put("amount", journalDocumentsReportSearchBean.getValue());
            queryBuilder.append(" and ( d.debitAmount = :amount ");  
            queryBuilder.append(" OR d.creditamount =  :amount )");
        }

    }

    private void appendGeneralJournalDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        if (journalDocumentsReportSearchBean.getPeriodFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(journalDocumentsReportSearchBean.getPeriodFrom());
            queryBuilder.append(" and e.generalData >= '").append(formatDateFrom).append("'");
        }

        if (journalDocumentsReportSearchBean.getPeriodTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(journalDocumentsReportSearchBean.getPeriodTo());
            queryBuilder.append(" and e.generalData <= '").append(formatDateTo).append("'");
        }

    }

    //khaleddd
    @Override
    public Integer findMaxId() {
        Integer max = dao.findEntityByQuery("SELECT MAX(g.id) FROM GeneralJournal g ");
        return max;
    }

    @Override
    public void deleteHeadForShiftingScreen(Integer generalJournalId) {
        dao.executeDeleteQuery("delete from GeneralJournal g WHERE g.id= " + generalJournalId);
    }

    @Override
    public void deleteHeadAndDetailsForShiftingScreen(List<PostRestrictionsView> postRestrictionsViewList, Integer flow, Integer generalJournalId) {
        if (postRestrictionsViewList != null && !postRestrictionsViewList.isEmpty()) {
            for (PostRestrictionsView prvDelete : postRestrictionsViewList) {
                if (prvDelete.getGeneralJournalId() != null && prvDelete.getGeneralJournalId().compareTo(0) == 1) {

                    if (prvDelete.getScreenCode() < 1009) {
                         if (prvDelete.getScreenCode() > 0) {
                        updateGlBankData(prvDelete, generalJournalId, flow);
                        } else {
                            List<GlBankTransaction> glBankTransactions = glBankTransactionService.getALLGlBankTransactionByGeneralJournalId(prvDelete.getGeneralJournalId());
                            if (glBankTransactions != null && !glBankTransactions.isEmpty()) {
                                for (GlBankTransaction bankTransaction : glBankTransactions) {
                                    prvDelete.setId(bankTransaction.getId());
                                    updateGlBankData(prvDelete, generalJournalId, flow);
                                }
                            }
                        }
                    } else if (prvDelete.getScreenCode() >= 1009 && prvDelete.getScreenCode() < 1015) {
                        updateInvPurchaseInvoiceData(prvDelete, generalJournalId, flow);
                    } else if (prvDelete.getScreenCode() >= 1015 && prvDelete.getScreenCode() < 1021) {
                        updateInvReturnPurchaseInvoiceData(prvDelete, generalJournalId, flow);
                    }
                    detailsService.deleteDetaildForShiftingScreen(prvDelete.getGeneralJournalId());
                    deleteHeadForShiftingScreen(prvDelete.getGeneralJournalId());
                }
            }
        }
    }

    public void updateGlBankData(PostRestrictionsView prv, Integer generalJournalId, Integer flow) {
        GlBankTransaction glBankTransaction = new GlBankTransaction();
        glBankTransaction.setId(prv.getId() != null ? prv.getId() : null);
        if (flow > 0) {
            glBankTransaction.setPostFlag(1);
        } else {
            glBankTransaction.setPostFlag(0);
        }
        GeneralJournal generalJournal = new GeneralJournal();
        generalJournal.setId(generalJournalId);
        if (generalJournalId != null) {
            glBankTransaction.setGeneralJournalId(generalJournal);
        }
        glBankTransactionService.updateGlBankTransaction(glBankTransaction);
    }

    public void updateInvPurchaseInvoiceData(PostRestrictionsView prv, Integer generalJournalId, Integer flow) {
        InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
        invPurchaseInvoice.setId(prv.getId() != null ? prv.getId() : null);
        if (flow > 0) {
            invPurchaseInvoice.setPostFlag(1);
        } else {
            invPurchaseInvoice.setPostFlag(0);
        }
        GeneralJournal generalJournal = new GeneralJournal();
        generalJournal.setId(generalJournalId);
        if (generalJournalId != null) {
            invPurchaseInvoice.setGeneralJournalId(generalJournal);
        }

        invPurchaseInvoiceService.updateInvPurchaseInvoice(invPurchaseInvoice);
    }

    public void updateInvReturnPurchaseInvoiceData(PostRestrictionsView prv, Integer generalJournalId, Integer flow) {
        InvReturnPurchase invReturnPurchase = new InvReturnPurchase();
        invReturnPurchase.setId(prv.getId() != null ? prv.getId() : null);
        if (flow > 0) {
            invReturnPurchase.setPostFlag(1);
        } else {
            invReturnPurchase.setPostFlag(0);
        }
        GeneralJournal generalJournal = new GeneralJournal();
        generalJournal.setId(generalJournalId);
        if (generalJournalId != null) {
            invReturnPurchase.setGeneralJournalId(generalJournal);
        }

        invReturnPurchaseService.updateReturnPurchaseInvoice(invReturnPurchase);
    }

    @Override
    public void addGeneralJournalForCorrectence(List<GeneralJournal> generalJournals) {
        for (GeneralJournal gj : generalJournals) {
            dao.updateEntity(gj);
        }
    }

    @Override
    public GeneralJournal findGeneralJournalRowBySerial(Integer branchId, Integer serial, Integer glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("serial", serial);
        params.put("glYear", glYear);

        return dao.findEntityByQuery("SELECT g FROM GeneralJournal g  WHERE g.glYear.id =:glYear and g.branchId.id = :branchId and g.serial =:serial", params);
    }

    @Override
    public List<GeneralJournal> getAllNotRevisionJournals(CommonSearchBean commonSearchBean, GlYear glYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", commonSearchBean.getBranchId());
        params.put("glYear", glYear.getId());
        params.put("postFlag", Boolean.FALSE);
        commonSearchBean.setDateFrom(glYear.getDateFrom());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM GeneralJournal e WHERE e.branchId.id = :branchId AND e.glYear.id=:glYear AND e.post_flag=:postFlag ";
        stringBuilder.append(query);
        String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
        stringBuilder.append(" and e.generalData >= '").append(formatDateFrom).append("'");
        String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateTo());
        stringBuilder.append(" and e.generalData <= '").append(formatDateTo).append("'");

        if (commonSearchBean.getDateTo() != null) {
            String formatDateSearchTo = new SimpleDateFormat("yyyy-MM-dd").format(commonSearchBean.getDateTo());
            stringBuilder.append(" and e.generalData <= '").append(formatDateSearchTo).append("'");
        }

        List<GeneralJournal> generalJournalList = dao.findListByQuery(stringBuilder.toString(), params);
        return generalJournalList;
    }

    @Override
    public GeneralJournal findGeneralJournal(Integer id) {
        return dao.findEntityById(GeneralJournal.class, id);
    }

    @Override
    public void updateGeneralJournalsForReviewing(Date dateFrom, Date dateTo, Integer branchId, Integer postFlag, String postFlagReview) {
        //UPDATE `toby`.`general_journal` SET `post_flag` = '0', `post_flag_review` = NULL WHERE (`id` = '259');
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE GeneralJournal e SET e.post_flag = ");
        if (postFlag == 1) {
            stringBuilder.append(true);
        } else {
            stringBuilder.append(false);
        }
        if (postFlagReview == null) {
            stringBuilder.append(", e.postFlagReview = ");
            stringBuilder.append(postFlagReview);
        } else {
            stringBuilder.append(", e.postFlagReview = '").append(postFlagReview).append("'");
        }
        //stringBuilder.append(postFlagReview);
        stringBuilder.append(" WHERE e.branchId.id = ");
        stringBuilder.append(branchId.toString());

        if (dateFrom != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            stringBuilder.append(" and e.generalData >= '").append(formatDateFrom).append("'");
        }
        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            stringBuilder.append(" and e.generalData <= '").append(formatDateTo).append("'");
        }
        dao.executeDeleteQuery(stringBuilder.toString());

    }
}
