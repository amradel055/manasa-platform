/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.CommonSearchBean;
import com.isag.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlYear;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GeneraljournalDetailsService {

    public GeneralJournalDetails addGenDetalils(GeneralJournalDetails details);

    public List<GeneralJournalDetails> addGenDetalils(List<GeneralJournalDetails> details, List<GeneralJournalDetails> generalListDeleted);

    public void deleteGenDetalils(GeneralJournalDetails details);

    public void deleteGenDetalils(List<GeneralJournalDetails> details);

    public GeneralJournalDetails updateGenDetalils(GeneralJournalDetails details);

    public List<GeneralJournalDetails> getAllGenDetalils();

    public List<GeneralJournalDetails> getAllJournalDetailsForJorunal(Integer id);

    public void deleteGenDetalilsByGeneralJournal(Integer generalJournal);

    public Integer getMaxSerialByGeneral(Integer id);

    public BigDecimal getBalanceForGlAccount(Integer glACCOUNTId, Date startGlYear, Date endDate);

    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccount(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glYear);

    public List<GeneralJournalDetails> getAllJournalDetailsForJorunalBySearchBean(Integer id, JournalDocumentsReportSearchBean journalDocumentsReportSearchBean);

    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean, GlYear glYear);

    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(Integer branchId, Integer debitOrCredit, Integer symbolId, CommonSearchBean commonSearchBean, GlYear glYear);

    public List<GeneralJournalDetails> getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean);

    public List<GeneralJournalDetails> getTheSumOfDebitAndSumOfCreditForAssetsAndDeductionsGlAccounts(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean , GlYear glYear);

    public  List<GeneralJournalDetails> getTheNetProfitForIncomeAndExpensesGlAccounts(Integer branchId, Integer debitOrCredit, CommonSearchBean commonSearchBean , GlYear glYear);

    public void deleteDetaildForShiftingScreen(Integer generalJournalId);

}
