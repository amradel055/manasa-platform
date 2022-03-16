/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.postRestrictionsViewService;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlYear;
import com.isag.entity.InstContract;
import com.isag.entity.InvNotice;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.RscExtractRegister;
import com.isag.views.PostRestrictionsView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;
    @EJB
    private InstContractService instContractService;
    @EJB
    private GlAccountService accountService;

    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private postRestrictionsViewService postRestrictionsViewService;
    @EJB
    private RscExtractRegisterService rscExtractRegisterService;
    @EJB
    private InvNoticeService invNoticeService;

    @Override
    public String saveHeadJournalAndDetails(List<GeneralJournal> generalJournalList, Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap, Map<Integer, PostRestrictionsView> postRestricitonViewMap, GlYear glYearSelection, Integer fillGeneralJournalDetailsFlow, Integer companyId, Boolean cancelInstallment) {
        try {
            String error = "";
            generalJournalHeadMap = new HashMap<>();
            if (validateGeneralJournal(generalJournalList, glYearSelection)) {
                for (GeneralJournal journal : generalJournalList) {
                    journal = generaljournalService.addGeneralJournal(journal);
                    generalJournalHeadMap.put(journal.getGeneralDecument(), journal);

                    PostRestrictionsView prv = postRestricitonViewMap.get(journal.getGeneralDecument());
                    if (prv.getScreenCode() < 1009) {
                        if (prv.getScreenCode() > 0) {
                            updateGlBankData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                        } else {
                            Integer accountId = generalJournalDetailsMap.get(prv.getSerial()).get(0).getGlACCOUNTId().getId();
                            List<PostRestrictionsView> prvList = postRestrictionsViewService.getTransactionsIdForUpdatePostFlag(accountId, prv.getDate(), prv.getScreenCode());
                            if (prvList != null && !prvList.isEmpty()) {
                                for (PostRestrictionsView restrictionsView : prvList) {
                                    updateGlBankData(restrictionsView, journal.getId(), fillGeneralJournalDetailsFlow);
                                }
                            }
                        }
                    } else if (prv.getScreenCode() >= 1009 && prv.getScreenCode() < 1021) {
                        updateInvPurchaseInvoiceData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                    } else if (prv.getScreenCode() >= 1021 && prv.getScreenCode() < 1022) {
                        updateIstallmentContractData(prv, journal.getId(), fillGeneralJournalDetailsFlow, cancelInstallment);
                    } else if (prv.getScreenCode() >= 1022 && prv.getScreenCode() < 1030) {
                        updateExtractRegisterData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
                    } else if (prv.getScreenCode() >= 1030 && prv.getScreenCode() < 1034) {
                        updateInvNoticeData(prv, journal.getId(), fillGeneralJournalDetailsFlow);
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
            }else{
                error = "تم إعلاق الشهر أو السنة الماليه";
            }
            
            return error;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
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
            glBankTransactionService.updateGlBankTransaction(glBankTransaction);
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
            invPurchaseInvoiceService.updateInvPurchaseInvoice(invPurchaseInvoice);
        } catch (Exception e) {
            e.getMessage();
        }
    }

//    public void updateInvPurchaseReturnInvoiceData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {
//
//        try {
//            InvReturnPurchase invReturnPurchase = new InvReturnPurchase();
//            invReturnPurchase.setId(prv.getId() != null ? prv.getId() : null);
//            if (fillGeneralJournalDetailsFlow > 0) {
//                invReturnPurchase.setPostFlag(1);
//                GeneralJournal generalJournal = new GeneralJournal();
//                generalJournal.setId(generalJournalId);
//                if (generalJournalId != null) {
//                    invReturnPurchase.setGeneralJournalId(generalJournal);
//                }
//            } else {
//                invReturnPurchase.setPostFlag(0);
//                invReturnPurchase.setGeneralJournalId(null);
//            }
//            invReturnPurchaseService.updateReturnPurchaseInvoice(invReturnPurchase);
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }
    public void updateIstallmentContractData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow, Boolean cancelInstallment) {

        try {
            InstContract instContract = new InstContract();
            instContract.setId(prv.getId() != null ? prv.getId() : null);
            if (cancelInstallment != null) {
                instContract.setCancel(cancelInstallment != true ? false : cancelInstallment);
            }
            if (fillGeneralJournalDetailsFlow > 0) {
                instContract.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    instContract.setGeneralJournalId(generalJournal);
                }
            } else {
                instContract.setPostFlag(0);
                instContract.setGeneralJournalId(null);
            }
            instContractService.updateIntsContract(instContract);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateExtractRegisterData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {

        try {
            RscExtractRegister extractRegister = new RscExtractRegister();
            extractRegister.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                extractRegister.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    extractRegister.setGeneralJournalId(generalJournal);
                }
            } else {
                extractRegister.setPostFlag(0);
                extractRegister.setGeneralJournalId(null);
            }
            rscExtractRegisterService.updaterscExtractRegister(extractRegister);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateInvNoticeData(PostRestrictionsView prv, Integer generalJournalId, Integer fillGeneralJournalDetailsFlow) {

        try {
            InvNotice invNotice = new InvNotice();
            invNotice.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                invNotice.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    invNotice.setGeneralJournalId(generalJournal);
                }
            } else {
                invNotice.setPostFlag(0);
                invNotice.setGeneralJournalId(null);
            }
            invNoticeService.updateInvNoticeForShifting(invNotice);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
