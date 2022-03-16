/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlYear;
import com.toby.entity.TobyUser;

import com.toby.views.PostRestrictionsView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GeneraljournalService {

    public GeneralJournal addGeneralJournalGlobal(Integer yearSelection , Integer specialDocumentsTypes ,Integer serial,Date date ,String remark,TobyUser tobyUser,Integer type,boolean postFlag);
            
    public GeneralJournal addGeneralJournal(GeneralJournal generalJournal);

    public void addGeneralJournalForCorrectence(List<GeneralJournal> generalJournals);

    public void deleteGeneralJournal(GeneralJournal generalJournal);
    
    public void deleteGeneralJournal(Integer id);

    public List<GeneralJournal> getALLGeneralJournal();

    public List<GeneralJournal> getALLGeneralJournalByCompanyId(Integer Id);

    public List<GeneralJournalDetails> getALLGeneralJournalDetailsByGeneralJournal(Integer generalJournalId);

    public BigDecimal getCreditAmountForGlAccount(Integer id);

    public BigDecimal getDebitAmountForGlAccount(Integer id);

    public List<GeneralJournalDetails> getALLGenDetailsBYGlAccount(Integer Id);

    public List<GeneralJournalDetails> getALLGenDetailsBYCostCenter(Integer Id);

    public List<GeneralJournalDetails> getALLGenDetailsBYAdminUnit(Integer Id);

    public Integer getMaxSerialToSelectedDocumentsType(Integer selectedDocumentsType);

    public List<GeneralJournal> getALLGeneralJournalByBranchId(Integer selectedBranch);

    public List<GeneralJournal> getALLGeneralJournalByBranchIdAndYear(Integer selectedBranch, GlYear year);

    public List<GeneralJournal> getALLGeneralJournalByBranchIdAndYearAndSymbolId(Integer selectedBranch, GlYear year, Integer symbolId);

    public List<GeneralJournal> getALLGeneralJournalDocumentsReport(JournalDocumentsReportSearchBean journalDocumentsReportSearchBean);

    public Integer findMaxId();

    public void deleteHeadForShiftingScreen(Integer generalJournalId);

    public void deleteHeadAndDetailsForShiftingScreen(List<PostRestrictionsView> postRestrictionsViewList, Integer flow, Integer generalJournalId);

    public GeneralJournal findGeneralJournalRowBySerial(Integer branchId, Integer serial, Integer glYear);

    public GeneralJournal addGeneralJournalFromExcel(GeneralJournal generalJournal);

    public void addGeneralJournalsForReviewing(List<GeneralJournal> generalJournalList);

    public void updateGeneralJournalsForReviewing(Date dateFrom, Date dateTo, Integer branchId, Integer postFlag , String postFlagReview);

    public List<GeneralJournal> getAllNotRevisionJournals(CommonSearchBean commonSearchBean, GlYear glYear);

    public GeneralJournal findGeneralJournal(Integer id);

}
