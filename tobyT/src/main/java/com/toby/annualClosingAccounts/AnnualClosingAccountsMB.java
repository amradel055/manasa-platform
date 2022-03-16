/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.annualClosingAccounts;

import com.toby.bean.AnnualClosingAccountsBean;
import com.toby.businessservice.CurrencyOperationService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAnnualClosingService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CurrencyOperation;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAnnualClosing;
import com.toby.entity.GlYear;
import com.toby.toby.BaseGlAccountReportBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author ahmed
 */
@Named(value = "annualClosingAccountsMB")
@ViewScoped
public class AnnualClosingAccountsMB extends BaseGlAccountReportBean {

    private AnnualClosingAccountsBean annualClosingAccountsBean;
    private AnnualClosingAccountsBean annualClosingAccountsBeanSelection;
    private AnnualClosingAccountsBean annualClosingAccountsBeanMemory;
    private AnnualClosingAccountsBean annualClosingAccountsBeanDeleteSelection;
    private CommonSearchBean commonSearchBean;
    private List<GeneralJournalDetails> generalJournalDetailsListCombined;
    private List<GeneralJournalDetails> generalJournalDetailsFirstSegment;
    private List<GeneralJournalDetails> generalJournalDetailsSecondSegment;
    private List<GeneralJournalDetails> generalJournalDetailsCorrectedList;
    private Date dateOfOpeneingJournal;
    private GlaccountConverter accountConverter;
    private GlAccount glAccountSelected;
    private List<GlYear> glYearNewList;
    private GeneralJournal generalJournalHead;
    private Integer generalDocument;
    private String nextOpeningYear;
    private BigDecimal totalRetio = BigDecimal.ZERO;
    private boolean validate;
    private boolean journalExist;
    private Integer journalSerial;
    private List<AnnualClosingAccountsBean> annualClosingAccountsBeanList;
    private List<AnnualClosingAccountsBean> rowsDeleted;
    private CurrencyOperation currencyOperations;
    private Map<Integer, GeneralJournalDetails> generalJournalDetailsMap = new HashMap<>();
    private List<GlAnnualClosing> glAnnualClosingList;
    private List<GlAnnualClosing> glAnnualClosingDeletedList;
    @EJB
    private GlYearService glYearService;
    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private GeneraljournalDetailsService detailsService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private GlAnnualClosingService glAnnualClosingService;

    @PostConstruct
    public void init() {
        try {
            reset();
            load();
            accountConverter = new GlaccountConverter(getGlAccountList());
            loadData();
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "validateSerial");
        }
    }

    @Override
    public void reset() {
        try {
            annualClosingAccountsBean = new AnnualClosingAccountsBean();
            commonSearchBean = new CommonSearchBean();
            generalJournalDetailsListCombined = new ArrayList<>();
            generalJournalDetailsFirstSegment = new ArrayList<>();
            generalJournalDetailsSecondSegment = new ArrayList<>();
            generalJournalDetailsCorrectedList = new ArrayList<>();
            annualClosingAccountsBeanList = new ArrayList<>();
            glAnnualClosingList = new ArrayList<>();
            glAnnualClosingDeletedList = new ArrayList<>();
            rowsDeleted = new ArrayList<>();
            glYearNewList = new ArrayList<>();
            generalJournalDetailsMap = new HashMap<>();
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "reset");
        }

    }

    public void loadData() {
        try {
            reset();
            prepareOpeneingBlancesValues(commonSearchBean);

            generalJournalDetailsFirstSegment = generaljournalDetailsService.getTheSumOfDebitAndSumOfCreditForAssetsAndDeductionsGlAccounts(getUserData().getUserBranch().getId(), 1, commonSearchBean, getGlYearSelection());
            generalJournalDetailsSecondSegment = generaljournalDetailsService.getTheSumOfDebitAndSumOfCreditForAssetsAndDeductionsGlAccounts(getUserData().getUserBranch().getId(), 2, commonSearchBean, getGlYearSelection());
            generalJournalDetailsListCombined = ListUtils.union(generalJournalDetailsFirstSegment, generalJournalDetailsSecondSegment);
            /*   for (GeneralJournalDetails journalDetails : generalJournalDetailsListCombined) {
             if (getTotalBalanceMap().containsKey(journalDetails.getSerial())) {
             journalDetails.setDebitAmount(journalDetails.getDebitAmount().add(getTotalBalanceMap().get(journalDetails.getSerial()) != null ? getTotalBalanceMap().get(journalDetails.getSerial()) : BigDecimal.ZERO));
             }
             }*/
            findTheNetProfit();

            glYearNewList = glYearService.findNextGlYear(getUserData().getUserBranch().getId(), getGlYearSelection());
            if (glYearNewList != null && !glYearNewList.isEmpty()) {
                nextOpeningYear = glYearNewList.get(0).getName();
            } else {
                nextOpeningYear = "لا يوجد";
            }
            totalRetio = BigDecimal.ZERO;
            findGlClosingAccounts();
            journalExist = false;
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "loadData");
        }
    }

    public void prepareGeneralJournalHeadAndDetails() {
        try {
            validate = true;
            validateSave();
            if (!journalExist) {
                if (validate) {
                    generalJournalHead = new GeneralJournal();
                    generalJournalHead.setCompanyId(getUserData().getCompany());
                    generalJournalHead.setGeneralType(getOpeningBalanceId());
                    generalJournalHead.setCreatedBy(getUserData().getUser());
                    generalJournalHead.setCreationDate(new Date());
                    generalJournalHead.setBranchId(getUserData().getUserBranch());
                    generalJournalHead.setGeneralStatement("قيد افتتاحي");
                    generalJournalHead.setGeneralData(dateOfOpeneingJournal != null ? dateOfOpeneingJournal : null);
                    generalJournalHead.setGlYear(glYearNewList.get(0));
                    try {
                        generalJournalHead = generaljournalService.addGeneralJournal(generalJournalHead);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "خطأ في الحفظ"));
                    }
                    saveDetails();
                }
            } else if (validate) {
                if (generalJournalHead != null && generalJournalHead.getId() != null) {
                    saveDetails();
                }
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "prepareGeneralJournalHeadAndDetails");
        }
    }

    public void fillDetailsCorrectly() {
        try {
            for (GeneralJournalDetails gjd : generalJournalDetailsListCombined) {
                GeneralJournalDetails details = new GeneralJournalDetails();

                GlAccount account = new GlAccount();
                account.setId(gjd.getSerial());
                account = accountService.findGlAccount(account.getId());
                details.setCurrencyId(account.getCurrencyId());
                currencyOperations = currencyOperationService.getRatesByDates(details.getCurrencyId().getId(), generalJournalHead.getGeneralData(), getUserData().getCompany().getId());
                details.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());

                if (gjd.getDiscribtion().equalsIgnoreCase("CREDIT")) {
                    details.setCreditamount(gjd.getDebitAmount());
                    details.setCreditamountLocal(gjd.getDebitAmount().multiply(details.getRate()));
                } else {
                    details.setDebitAmount(gjd.getDebitAmount());
                    details.setDebitAmountLocal(gjd.getDebitAmount().multiply(details.getRate()));
                }
                details.setGlACCOUNTId(account);
                details.setGeneralJournalId(generalJournalHead);
                details.setCreatedBy(getUserData().getUser());
                details.setCreationDate(new Date());
                details.setCompanyId(getUserData().getCompany());
                details.setBranchId(getUserData().getUserBranch());
                // generalJournalDetailsCorrectedList.add(details);
                generalJournalDetailsMap.put(details.getGlACCOUNTId().getId(), details);
            }
            if (annualClosingAccountsBeanList != null && !annualClosingAccountsBeanList.isEmpty()) {
                saveClosingAccounts();
                for (AnnualClosingAccountsBean acab : annualClosingAccountsBeanList) {

                    if (generalJournalDetailsMap != null && generalJournalDetailsMap.containsKey(acab.getAccount().getId())) {
                        GeneralJournalDetails journalDetails = generalJournalDetailsMap.get(acab.getAccount().getId());

                        if (journalDetails.getGlACCOUNTId().getAccClass().toString().equalsIgnoreCase("CREDIT")) {
                            if (getNetProfit().compareTo(BigDecimal.ZERO) == 1) {
                                journalDetails.setCreditamount(journalDetails.getCreditamount().add(acab.getAmount()));
                                journalDetails.setCreditamountLocal(journalDetails.getCreditamountLocal().add(acab.getAmount().multiply(journalDetails.getRate())));
                            } else {
                                journalDetails.setCreditamount(journalDetails.getCreditamount().subtract(acab.getAmount()));
                                journalDetails.setCreditamountLocal(journalDetails.getCreditamountLocal().subtract(acab.getAmount().multiply(journalDetails.getRate())));
                            }
                        }
                        if (journalDetails.getGlACCOUNTId().getAccClass().toString().equalsIgnoreCase("DEBIT")) {
                            if (getNetProfit().compareTo(BigDecimal.ZERO) == 1) {
                                journalDetails.setDebitAmount(journalDetails.getDebitAmount().subtract(acab.getAmount()));
                                journalDetails.setDebitAmountLocal(journalDetails.getDebitAmountLocal().subtract(acab.getAmount().multiply(journalDetails.getRate())));
                            } else {
                                journalDetails.setDebitAmount(journalDetails.getDebitAmount().add(acab.getAmount()));
                                journalDetails.setDebitAmountLocal(journalDetails.getDebitAmountLocal().add(acab.getAmount().multiply(journalDetails.getRate())));
                            }
                        }
                        generalJournalDetailsMap.put(acab.getAccount().getId(), journalDetails);
                    } else {
                        GeneralJournalDetails details1 = new GeneralJournalDetails();

                        details1.setCurrencyId(acab.getAccount().getCurrencyId());
                        currencyOperations = currencyOperationService.getRatesByDates(details1.getCurrencyId().getId(), generalJournalHead.getGeneralData(), getUserData().getCompany().getId());
                        details1.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());

                        if (getNetProfit().compareTo(BigDecimal.ZERO) == 1) {
                            details1.setCreditamount(acab.getAmount());
                            details1.setCreditamountLocal(acab.getAmount().multiply(details1.getRate()));
                        } else {
                            details1.setDebitAmount(acab.getAmount());
                            details1.setDebitAmountLocal(acab.getAmount().multiply(details1.getRate()));
                        }
                        details1.setGlACCOUNTId(acab.getAccount());
                        details1.setGeneralJournalId(generalJournalHead);
                        details1.setCreatedBy(getUserData().getUser());
                        details1.setCreationDate(new Date());
                        details1.setCompanyId(getUserData().getCompany());
                        details1.setBranchId(getUserData().getUserBranch());
                        // generalJournalDetailsCorrectedList.add(details1);
                        generalJournalDetailsMap.put(acab.getAccount().getId(), details1);
                    }
                }
            }
            generalJournalDetailsCorrectedList = new ArrayList<>(generalJournalDetailsMap.values());
            arrangeDebitAndCreditsOrder();
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "fillDetailsCorrectly");
        }
    }

    public void saveDetails() {
        try {
            if (generalJournalDetailsListCombined != null && !generalJournalDetailsListCombined.isEmpty()) {
                if (generalJournalHead != null & generalJournalHead.getId() != null) {
                    fillDetailsCorrectly();
                    getGlYearSelection().setOpenning(1);
                    glYearService.updateYear(getGlYearSelection());
                    try {
                        detailsService.addGenDetalils(generalJournalDetailsCorrectedList, null);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved", "تم الحفظ واغلاق السنة"));
                        generalJournalHead = null;
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "خطأ في الحفظ"));
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "saveDetails");
        }
    }

    public void validateSave() {
        try {
            if (getGlYearSelection().getOpenning() != null && getGlYearSelection().getOpenning().compareTo(1) == 0) {
                validate = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "هذه السنة تم اغلاقها مسبقا"));
            }
            if (glYearNewList == null || glYearNewList.isEmpty()) {
                validate = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "لا توجد سنين مالية"));
            }
            /*if ( glYearNewList.get(0) == null) {
             validate = false;
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "لا توجد سنة مالية تالية"));
             }*/
            if (dateOfOpeneingJournal == null && !journalExist) {
                validate = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ فتح القيد"));
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "validateSave");
        }
    }

    public void addClosingAccounts() {
        try {
            if (getGlYearSelection().getOpenning() == null || (getGlYearSelection().getOpenning() != null && getGlYearSelection().getOpenning().compareTo(1) != 0)) {
                if (annualClosingAccountsBeanList != null && !annualClosingAccountsBeanList.isEmpty()) {
                    annualClosingAccountsBeanMemory = new AnnualClosingAccountsBean();
                    annualClosingAccountsBeanMemory = annualClosingAccountsBeanList.get(annualClosingAccountsBeanList.size() - 1);
                }

                if ((annualClosingAccountsBeanList == null || annualClosingAccountsBeanList.isEmpty()) || (annualClosingAccountsBeanMemory != null && annualClosingAccountsBeanMemory.getAccount() != null && annualClosingAccountsBeanMemory.getRatio() != null && annualClosingAccountsBeanMemory.getRatio().compareTo(BigDecimal.ZERO) == 1)) {
                    AnnualClosingAccountsBean accountsBean = new AnnualClosingAccountsBean();
                    accountsBean.setAccount(null);
                    accountsBean.setRatio(BigDecimal.ZERO);
                    annualClosingAccountsBeanList.add(accountsBean);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب استكمال البيانات"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "تم اغلاق السنة"));
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "validateaddClosingAccountsSave");
        }
    }

    public void saveClosingAccounts() {
        try {
            if (getGlYearSelection().getOpenning() == null || (getGlYearSelection().getOpenning() != null && getGlYearSelection().getOpenning().compareTo(1) != 0)) {
                Boolean save = true;
                glAnnualClosingList = new ArrayList<>();
                if (annualClosingAccountsBeanList != null && !annualClosingAccountsBeanList.isEmpty()) {
                    for (AnnualClosingAccountsBean accountsBean : annualClosingAccountsBeanList) {
                        GlAnnualClosing annualClosing = new GlAnnualClosing();
                        annualClosing.setId(accountsBean.getId());
                        annualClosing.setBranchId(getUserData().getUserBranch());
                        annualClosing.setCompanyId(getUserData().getCompany());
                        if (accountsBean.getId() == null) {
                            annualClosing.setCreationDate(new Date());
                            annualClosing.setCreatedBy(getUserData().getUser());
                        } else {
                            annualClosing.setModificationDate(new Date());
                            annualClosing.setModifiedBy(getUserData().getUser());
                        }
                        annualClosing.setRatio(accountsBean.getRatio());
                        annualClosing.setGlAccountId(accountsBean.getAccount());
                        annualClosing.setGlYearId(getGlYearSelection());
                        glAnnualClosingList.add(annualClosing);

                        if (!validateSaveClosing(accountsBean)) {
                            save = false;
                        }
                    }
                }

                if (save) {
                    glAnnualClosingService.updateGlAnuualList(glAnnualClosingList, glAnnualClosingDeletedList);
                    glAnnualClosingDeletedList = new ArrayList<>();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved", "تم الحفظ "));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "تم اغلاق السنة"));
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "saveClosingAccounts");
        }
    }

    public boolean validateSaveClosing(AnnualClosingAccountsBean accountsBean) {
        try {
            if (accountsBean.getAccount() == null || accountsBean.getRatio() == null || accountsBean.getRatio().compareTo(BigDecimal.ZERO) == 0 || accountsBean.getRatio().compareTo(BigDecimal.ZERO) == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب استكمال البيانات"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "validateSaveClosing");
            return false;
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;
            Object newValue = event.getNewValue() != null ? event.getNewValue() : null;
            String column_name = event.getColumn().getClientId();
            DataTable dataTable = (DataTable) event.getSource();
            annualClosingAccountsBeanSelection = (AnnualClosingAccountsBean) dataTable.getRowData();

            if (column_name.contains("ratio")) {
                if (!validateQuantity(newValue != null ? (BigDecimal) newValue : null)) {
                    annualClosingAccountsBeanSelection.setRatio((BigDecimal) oldValue);
                }
            }
            calculateAmountByRatioForList();
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "onCellEdit");
        }
    }

    public Boolean validateQuantity(BigDecimal newValue) {
        try {
            return newValue != null && newValue.compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "validateQuantity");
            return false;
        }
    }

    public void calculateAmountByRatioForList() {
        try {
            if (annualClosingAccountsBeanList != null && !annualClosingAccountsBeanList.isEmpty()) {
                BigDecimal hundred = new BigDecimal(100);
                totalRetio = BigDecimal.ZERO;
                for (AnnualClosingAccountsBean acab : annualClosingAccountsBeanList) {
                    if (acab.getRatio() == null) {
                        acab.setRatio(BigDecimal.ZERO);
                    }
                    acab.setRatio(acab.getRatio().abs());
                    totalRetio = totalRetio.add(acab.getRatio());
                    acab.setAmount(acab.getRatio().multiply(getNetProfit().abs()).divide(hundred));
                }
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "calculateAmountByRatioForList");
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = getGlAccountList();//45
            if (query == null || query.trim().equals("")) {
                setAccountConverter(new GlaccountConverter(glaccounts));
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();
            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < getGlAccountList().size(); i++) {
                glaccount = glaccounts.get(i);
                nameAr = glaccount.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }

                code = glaccount.getAccNumber();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }
            }

            setAccountConverter(new GlaccountConverter(filteredGlaccounts));
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "completeGlAccount");
            return null;
        }
    }

    public void arrangeDebitAndCreditsOrder() {
        try {
            if (generalJournalDetailsCorrectedList != null && !generalJournalDetailsCorrectedList.isEmpty()) {
                generalJournalDetailsFirstSegment = new ArrayList<>();
                generalJournalDetailsSecondSegment = new ArrayList<>();
                for (GeneralJournalDetails journalDetails : generalJournalDetailsCorrectedList) {
                    if ("DEBIT".equalsIgnoreCase(journalDetails.getGlACCOUNTId().getAccClass().toString())) {
                        generalJournalDetailsFirstSegment.add(journalDetails);
                    } else {
                        generalJournalDetailsSecondSegment.add(journalDetails);
                    }
                }
                generalJournalDetailsCorrectedList = new ArrayList<>();
                generalJournalDetailsCorrectedList = ListUtils.union(generalJournalDetailsFirstSegment, generalJournalDetailsSecondSegment);
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "arrangeDebitAndCreditsOrder");
        }
    }

    public void deleteAll() {
        try {
            if (getGlYearSelection().getOpenning() == null || (getGlYearSelection().getOpenning() != null && getGlYearSelection().getOpenning().compareTo(1) != 0)) {
                if (annualClosingAccountsBeanDeleteSelection != null) {
                    annualClosingAccountsBeanList.remove(annualClosingAccountsBeanDeleteSelection);
                    if (annualClosingAccountsBeanDeleteSelection.getId() != null) {
                        GlAnnualClosing annualClosing = new GlAnnualClosing();
                        annualClosing.setId(annualClosingAccountsBeanDeleteSelection.getId());
                        glAnnualClosingDeletedList.add(annualClosing);
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "تم اغلاق السنة"));
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "deleteAll");
        }
    }

    public void fetchTheGneralJounralRow() {
        try {
            if (journalSerial != null && journalSerial.compareTo(0) == 1) {
                generalJournalHead = new GeneralJournal();
                generalJournalHead = generaljournalService.findGeneralJournalRowBySerial(getUserData().getUserBranch().getId(), journalSerial, glYearNewList.get(0).getId());
                if (generalJournalHead == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "لا يوجد قيد لهذا المسلسل"));
                }
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "fetchTheGneralJounralRow");
        }
    }

    public void findGlClosingAccounts() {
        try {
            annualClosingAccountsBeanList = new ArrayList<>();
            glAnnualClosingList = new ArrayList<>();
            glAnnualClosingList = glAnnualClosingService.findGlAnnualClosingListByBranch(getUserData().getUserBranch().getId(), getGlYearSelection().getId());
            if (glAnnualClosingList != null && !glAnnualClosingList.isEmpty()) {
                for (GlAnnualClosing annualClosing : glAnnualClosingList) {
                    AnnualClosingAccountsBean accountsBean = new AnnualClosingAccountsBean();
                    accountsBean.setAccount(annualClosing.getGlAccountId());
                    accountsBean.setRatio(annualClosing.getRatio());
                    accountsBean.setId(annualClosing.getId());
                    annualClosingAccountsBeanList.add(accountsBean);
                }
            }
            calculateAmountByRatioForList();
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "findGlClosingAccounts");
        }
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkDate(Boolean dateType) {
        try {
            if (dateType) {
                if (checkFinancailYear(dateOfOpeneingJournal)) {
                    resetDateFrom();
                }
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "checkDate");
        }
    }

    @Override
    public Boolean checkFinancailYear(Date date) {
        try {
            if (glYearNewList != null && !glYearNewList.isEmpty() && (date.before(glYearNewList.get(0).getDateFrom()) || date.after(glYearNewList.get(0).getDateTo()))) {
                setShowMessageGeneral(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
                return true;
            }
            return false;
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "checkFinancailYear");
            return false;
        }
    }

    @Override
    public void resetDateFrom() {
        try {
            if (glYearNewList != null && !glYearNewList.isEmpty()) {
                setDateOfOpeneingJournal(glYearNewList.get(0) != null ? glYearNewList.get(0).getDateFrom() : null);
            }
        } catch (Exception e) {
            saveError(e, "AnnualClosingAccountsMB", "resetDateFrom");
        }
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the annualClosingAccountsBean
     */
    public AnnualClosingAccountsBean getAnnualClosingAccountsBean() {
        return annualClosingAccountsBean;
    }

    /**
     * @param annualClosingAccountsBean the annualClosingAccountsBean to set
     */
    public void setAnnualClosingAccountsBean(AnnualClosingAccountsBean annualClosingAccountsBean) {
        this.annualClosingAccountsBean = annualClosingAccountsBean;
    }

    /**
     * @return the commonSearchBean
     */
    public CommonSearchBean getCommonSearchBean() {
        return commonSearchBean;
    }

    /**
     * @param commonSearchBean the commonSearchBean to set
     */
    public void setCommonSearchBean(CommonSearchBean commonSearchBean) {
        this.commonSearchBean = commonSearchBean;
    }

    /**
     * @return the generalJournalDetailsListCombined
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsListCombined() {
        return generalJournalDetailsListCombined;
    }

    /**
     * @param generalJournalDetailsListCombined the
     * generalJournalDetailsListCombined to set
     */
    public void setGeneralJournalDetailsListCombined(List<GeneralJournalDetails> generalJournalDetailsListCombined) {
        this.generalJournalDetailsListCombined = generalJournalDetailsListCombined;
    }

    /**
     * @return the generalJournalDetailsFirstSegment
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsFirstSegment() {
        return generalJournalDetailsFirstSegment;
    }

    /**
     * @param generalJournalDetailsFirstSegment the
     * generalJournalDetailsFirstSegment to set
     */
    public void setGeneralJournalDetailsFirstSegment(List<GeneralJournalDetails> generalJournalDetailsFirstSegment) {
        this.generalJournalDetailsFirstSegment = generalJournalDetailsFirstSegment;
    }

    /**
     * @return the generalJournalDetailsSecondSegment
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsSecondSegment() {
        return generalJournalDetailsSecondSegment;
    }

    /**
     * @param generalJournalDetailsSecondSegment the
     * generalJournalDetailsSecondSegment to set
     */
    public void setGeneralJournalDetailsSecondSegment(List<GeneralJournalDetails> generalJournalDetailsSecondSegment) {
        this.generalJournalDetailsSecondSegment = generalJournalDetailsSecondSegment;
    }

    /**
     * @return the dateOfOpeneingJournal
     */
    public Date getDateOfOpeneingJournal() {
        return dateOfOpeneingJournal;
    }

    /**
     * @param dateOfOpeneingJournal the dateOfOpeneingJournal to set
     */
    public void setDateOfOpeneingJournal(Date dateOfOpeneingJournal) {
        this.dateOfOpeneingJournal = dateOfOpeneingJournal;
    }

    /**
     * @return the accountConverter
     */
    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    /**
     * @param accountConverter the accountConverter to set
     */
    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * @return the glAccountSelected
     */
    public GlAccount getGlAccountSelected() {
        return glAccountSelected;
    }

    /**
     * @param glAccountSelected the glAccountSelected to set
     */
    public void setGlAccountSelected(GlAccount glAccountSelected) {
        this.glAccountSelected = glAccountSelected;
    }

    /**
     * @return the glYearNewList
     */
    public List<GlYear> getGlYearNewList() {
        return glYearNewList;
    }

    /**
     * @param glYearNewList the glYearNewList to set
     */
    public void setGlYearNewList(List<GlYear> glYearNewList) {
        this.glYearNewList = glYearNewList;
    }

    /**
     * @return the generalJournalHead
     */
    public GeneralJournal getGeneralJournalHead() {
        return generalJournalHead;
    }

    /**
     * @param generalJournalHead the generalJournalHead to set
     */
    public void setGeneralJournalHead(GeneralJournal generalJournalHead) {
        this.generalJournalHead = generalJournalHead;
    }

    /**
     * @return the generalDocument
     */
    public Integer getGeneralDocument() {
        return generalDocument;
    }

    /**
     * @param generalDocument the generalDocument to set
     */
    public void setGeneralDocument(Integer generalDocument) {
        this.generalDocument = generalDocument;
    }

    /**
     * @return the generalJournalDetailsCorrectedList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsCorrectedList() {
        return generalJournalDetailsCorrectedList;
    }

    /**
     * @param generalJournalDetailsCorrectedList the
     * generalJournalDetailsCorrectedList to set
     */
    public void setGeneralJournalDetailsCorrectedList(List<GeneralJournalDetails> generalJournalDetailsCorrectedList) {
        this.generalJournalDetailsCorrectedList = generalJournalDetailsCorrectedList;
    }

    /**
     * @return the validate
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * @param validate the validate to set
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * @return the nextOpeningYear
     */
    public String getNextOpeningYear() {
        return nextOpeningYear;
    }

    /**
     * @param nextOpeningYear the nextOpeningYear to set
     */
    public void setNextOpeningYear(String nextOpeningYear) {
        this.nextOpeningYear = nextOpeningYear;
    }

    /**
     * @return the annualClosingAccountsBeanList
     */
    public List<AnnualClosingAccountsBean> getAnnualClosingAccountsBeanList() {
        return annualClosingAccountsBeanList;
    }

    /**
     * @param annualClosingAccountsBeanList the annualClosingAccountsBeanList to
     * set
     */
    public void setAnnualClosingAccountsBeanList(List<AnnualClosingAccountsBean> annualClosingAccountsBeanList) {
        this.annualClosingAccountsBeanList = annualClosingAccountsBeanList;
    }

    /**
     * @return the totalRetio
     */
    public BigDecimal getTotalRetio() {
        return totalRetio;
    }

    /**
     * @param totalRetio the totalRetio to set
     */
    public void setTotalRetio(BigDecimal totalRetio) {
        this.totalRetio = totalRetio;
    }

    /**
     * @return the annualClosingAccountsBeanSelection
     */
    public AnnualClosingAccountsBean getAnnualClosingAccountsBeanSelection() {
        return annualClosingAccountsBeanSelection;
    }

    /**
     * @param annualClosingAccountsBeanSelection the
     * annualClosingAccountsBeanSelection to set
     */
    public void setAnnualClosingAccountsBeanSelection(AnnualClosingAccountsBean annualClosingAccountsBeanSelection) {
        this.annualClosingAccountsBeanSelection = annualClosingAccountsBeanSelection;
    }

    /**
     * @return the rowsDeleted
     */
    public List<AnnualClosingAccountsBean> getRowsDeleted() {
        return rowsDeleted;
    }

    /**
     * @param rowsDeleted the rowsDeleted to set
     */
    public void setRowsDeleted(List<AnnualClosingAccountsBean> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    /**
     * @return the annualClosingAccountsBeanDeleteSelection
     */
    public AnnualClosingAccountsBean getAnnualClosingAccountsBeanDeleteSelection() {
        return annualClosingAccountsBeanDeleteSelection;
    }

    /**
     * @param annualClosingAccountsBeanDeleteSelection the
     * annualClosingAccountsBeanDeleteSelection to set
     */
    public void setAnnualClosingAccountsBeanDeleteSelection(AnnualClosingAccountsBean annualClosingAccountsBeanDeleteSelection) {
        this.annualClosingAccountsBeanDeleteSelection = annualClosingAccountsBeanDeleteSelection;
    }

    /**
     * @return the currencyOperations
     */
    public CurrencyOperation getCurrencyOperations() {
        return currencyOperations;
    }

    /**
     * @param currencyOperations the currencyOperations to set
     */
    public void setCurrencyOperations(CurrencyOperation currencyOperations) {
        this.currencyOperations = currencyOperations;
    }

    /**
     * @return the journalExist
     */
    public boolean isJournalExist() {
        return journalExist;
    }

    /**
     * @param journalExist the journalExist to set
     */
    public void setJournalExist(boolean journalExist) {
        this.journalExist = journalExist;
    }

    /**
     * @return the journalSerial
     */
    public Integer getJournalSerial() {
        return journalSerial;
    }

    /**
     * @param journalSerial the journalSerial to set
     */
    public void setJournalSerial(Integer journalSerial) {
        this.journalSerial = journalSerial;
    }

    /**
     * @return the annualClosingAccountsBeanMemory
     */
    public AnnualClosingAccountsBean getAnnualClosingAccountsBeanMemory() {
        return annualClosingAccountsBeanMemory;
    }

    /**
     * @param annualClosingAccountsBeanMemory the
     * annualClosingAccountsBeanMemory to set
     */
    public void setAnnualClosingAccountsBeanMemory(AnnualClosingAccountsBean annualClosingAccountsBeanMemory) {
        this.annualClosingAccountsBeanMemory = annualClosingAccountsBeanMemory;
    }

    /**
     * @return the generalJournalDetailsMap
     */
    public Map<Integer, GeneralJournalDetails> getGeneralJournalDetailsMap() {
        return generalJournalDetailsMap;
    }

    /**
     * @param generalJournalDetailsMap the generalJournalDetailsMap to set
     */
    public void setGeneralJournalDetailsMap(Map<Integer, GeneralJournalDetails> generalJournalDetailsMap) {
        this.generalJournalDetailsMap = generalJournalDetailsMap;
    }

    /**
     * @return the glAnnualClosingList
     */
    public List<GlAnnualClosing> getGlAnnualClosingList() {
        return glAnnualClosingList;
    }

    /**
     * @param glAnnualClosingList the glAnnualClosingList to set
     */
    public void setGlAnnualClosingList(List<GlAnnualClosing> glAnnualClosingList) {
        this.glAnnualClosingList = glAnnualClosingList;
    }

    /**
     * @return the glAnnualClosingDeletedList
     */
    public List<GlAnnualClosing> getGlAnnualClosingDeletedList() {
        return glAnnualClosingDeletedList;
    }

    /**
     * @param glAnnualClosingDeletedList the glAnnualClosingDeletedList to set
     */
    public void setGlAnnualClosingDeletedList(List<GlAnnualClosing> glAnnualClosingDeletedList) {
        this.glAnnualClosingDeletedList = glAnnualClosingDeletedList;
    }

}
