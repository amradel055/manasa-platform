/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.CurrencyOperation;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlYear;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvReturnPurchase;
import com.toby.entity.TobyUser;
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
 * @author ahmed
 */
@Stateless
public class ShiftingJournalServiceImpl implements ShiftingJournalService {

    @EJB
    private CloseAndSaveMonthService closeAndSaveMonthService;
    @EJB
    private GeneraljournalService generaljournalService;

    private Map<Integer, GeneralJournal> generalJournalHeadMap;

    @EJB
    private GeneraljournalDetailsService detailsService;
//    @EJB
//    private GlBankTransactionService glBankTransactionService;
//    @EJB
//    private InvPurchaseInvoiceService invPurchaseInvoiceService;
//    @EJB
//    private InvReturnPurchaseService invReturnPurchaseService;
//    @EJB
//    private InstContractService instContractService;
    @EJB
    private GlAccountService accountService;

    @EJB
    private CurrencyOperationService currencyOperationService;

    @Override
    public void saveHeadJournalAndDetails(List<GeneralJournal> generalJournalList, Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap, Map<Integer, PostRestrictionsView> postRestricitonViewMap, GlYear glYearSelection, Integer fillGeneralJournalDetailsFlow, Integer companyId, Boolean cancelInstallment) {

        generalJournalHeadMap = new HashMap<>();
        if (validateGeneralJournal(generalJournalList, glYearSelection)) {
            for (GeneralJournal journal : generalJournalList) {
                journal = generaljournalService.addGeneralJournal(journal);
                generalJournalHeadMap.put(journal.getGeneralDecument(), journal);

                PostRestrictionsView prv = postRestricitonViewMap.get(journal.getGeneralDecument());
                if (prv.getScreenCode() < 1009) {
                    updateGlBankData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                } else if (prv.getScreenCode() >= 1009 && prv.getScreenCode() < 1015) {
                    updateInvPurchaseInvoiceData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                } else if (prv.getScreenCode() >= 1015 && prv.getScreenCode() < 1021) {
                    updateInvPurchaseReturnInvoiceData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                }
            }

            for (Map.Entry<Integer, List<GeneralJournalDetails>> entry : generalJournalDetailsMap.entrySet()) {
                if (validategeneralJournalDetails(entry.getValue())) {
                    for (GeneralJournalDetails details : entry.getValue()) {
                        details.setGeneralJournalId(generalJournalHeadMap.get(details.getGeneralJournalId().getGeneralDecument()));

                        GlAccount account = new GlAccount();
                        if (details != null && details.getGlACCOUNTId() != null) {
                            account = accountService.findGlAccount(details.getGlACCOUNTId().getId());
                            details.setCurrencyId(account.getCurrencyId());
                            CurrencyOperation currencyOperations = new CurrencyOperation();
                            currencyOperations = currencyOperationService.getRatesByDates(details.getCurrencyId().getId(), details.getGeneralJournalId().getGeneralData(), companyId);
                            details.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());
                        }
                    }
                    detailsService.addGenDetalils(entry.getValue(), null);

                }
            }
        }
    }

    public boolean validateGeneralJournal(List<GeneralJournal> generalJournalList, GlYear glYearSelection) {
        if (generalJournalList != null && !generalJournalList.isEmpty()) {
            for (GeneralJournal journal : generalJournalList) {
                String dateFrom = new SimpleDateFormat("MM").format(journal.getGeneralData());
                Integer numberOfMonth = Integer.parseInt(dateFrom);
                if (closeAndSaveMonthService.isCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchIdExist(journal.getBranchId().getId(), glYearSelection.getId(), numberOfMonth)) {
                    return false;
                }
                if (glYearSelection.getOpenning() != null && glYearSelection.getOpenning() == 1) {
                    return false;
                }

            }
        } else {
            return false;
        }
        return true;
    }

    public boolean validategeneralJournalDetails(List<GeneralJournalDetails> GeneralJournalDetailsList) {
        if (GeneralJournalDetailsList != null && !GeneralJournalDetailsList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateGlBankData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {

        try {
            GlBankTransaction glBankTransaction = new GlBankTransaction();
            glBankTransaction.setId(prv.getId() != null ? prv.getId() : null);//1177
            if (fillGeneralJournalDetailsFlow > 0) {
                glBankTransaction.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    glBankTransaction.setGeneralJournalId(generalJournal);
                }
            } else {
                glBankTransaction.setPostFlag(0);
                glBankTransaction.setGeneralJournalId(null);
            }
//            glBankTransactionService.updateGlBankTransaction(glBankTransaction);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateInvPurchaseInvoiceData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {

        try {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                invPurchaseInvoice.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    invPurchaseInvoice.setGeneralJournalId(generalJournal);
                }
            } else {
                invPurchaseInvoice.setPostFlag(0);
                invPurchaseInvoice.setGeneralJournalId(null);
            }
//            invPurchaseInvoiceService.updateInvPurchaseInvoice(invPurchaseInvoice);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateInvPurchaseReturnInvoiceData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {

        try {
            InvReturnPurchase invReturnPurchase = new InvReturnPurchase();
            invReturnPurchase.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                invReturnPurchase.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    invReturnPurchase.setGeneralJournalId(generalJournal);
                }
            } else {
                invReturnPurchase.setPostFlag(0);
                invReturnPurchase.setGeneralJournalId(null);
            }
//            invReturnPurchaseService.updateReturnPurchaseInvoice(invReturnPurchase);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void tarhil(GeneralJournal generalJournal, TobyUser tobyUser) {
        generalJournal.setBranchId(tobyUser.getBranchId());
        generalJournal.setCompanyId(tobyUser.getCompanyId());
        generalJournal.setCreatedBy(tobyUser);
        generalJournal.setCreationDate(new Date());
        generalJournal.setPost_flag(true);

    }
}
