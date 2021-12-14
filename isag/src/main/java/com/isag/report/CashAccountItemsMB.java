/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.AccountsSystemSettingsService;
import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlYearService;
import com.isag.businessservice.SymbolService;
import com.isag.businessservice.reports.CashAccountItemsViewService;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.comparatorsVars.CashAccountItemsViewComparator;
import com.isag.converter.GlBankConverter;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.GlBankDTO;
import com.isag.entity.AccountsSystemSettings;
import com.isag.entity.GlBank;
import com.isag.entity.Symbol;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.views.CashAccountItemsView;
import com.isag.views.SubAccountSummaryReport;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author WIN7
 */
@Named(value = "cashAccountItemsMB")
@ViewScoped
public class CashAccountItemsMB extends BaseGlAccountReportBean {

    private Integer branchId;
    private GlBankDTOConverter glBankConverter;
    private SymbolConverter symbolConverterFrom;
    private SymbolConverter symbolConverterTo;
    private List<Symbol> itemsFrom;
    private List<Symbol> itemsTo;
    private List<GlBankDTO> glBankList;

    private SubAccountSummarySearchBean cashAccountSettlementSearchBean;
    private List<CashAccountItemsView> cashAccountSettlementList;
    private List<SubAccountSummaryReport> cashAccountSettlementReports;

    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private BigDecimal openingBalance = BigDecimal.ZERO;

    private Map<Integer, GlBankDTO> glBankMap = new HashMap<>();
    private Map<Integer, GlBankDTO> glBankCopyMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalForDebitMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalForCreditMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalForBalanceMap = new HashMap<>();
    private Map<Integer, BigDecimal> openingBalanceMap = new HashMap<>();
    private TreeMap<Integer, SubAccountSummaryReport> cashTreeMap = new TreeMap<>();

    private Integer distinctBankId;
    private StringBuilder builder;
    private Integer glBankTransactionIdSeclected;
    private AccountsSystemSettings accountsSystemSettings;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;
    private SubAccountSummaryReport glBankTransactionTypeSeclected;
    @EJB
    GlAccountService glAccountService;

    @EJB
    GlYearService glYearService;

    @EJB
    CostCenterService costCenterService;

    @EJB
    GlBankService glBankService;

    @EJB
    CashAccountItemsViewService cashAccountItemsViewService;
    @EJB
    SymbolService symbolService;

    @PostConstruct
    public void init() {
        try {
            if (getGlYearSelection() != null) {
                reset();
            } else {
                redirectFinancailYearPage();
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @Override
    public void load() {
        cashAccountSettlementSearchBean = new SubAccountSummarySearchBean();
        cashAccountSettlementSearchBean.setOrderBy(0);
        cashAccountSettlementSearchBean.setType(3);
        cashAccountSettlementSearchBean.setCurrencyType(0);
        cashAccountSettlementReports = new ArrayList<>();
        glBankList = new ArrayList<>();
        branchId = getUserData().getUserBranch().getId();
        glBankList = glBankService.getAllGlBankDTOByBranchId(branchId);
        if (glBankList != null && !glBankList.isEmpty()) {
            for (GlBankDTO gb : glBankList) {
                glBankMap.put(gb.getId(), gb);
                totalForDebitMap.put(gb.getId(), BigDecimal.ZERO);
                totalForCreditMap.put(gb.getId(), BigDecimal.ZERO);
                totalForBalanceMap.put(gb.getId(), BigDecimal.ZERO);
                openingBalanceMap.put(gb.getId(), BigDecimal.ZERO);
            }
        }
        glBankConverter = new GlBankDTOConverter(glBankList);
        setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));
        getItemsFrom();
        getItemsTo();
    }

    @Override
    public void reset() {
        load();
        resetDateFrom();
        resetDateTo();
    }

    @Override
    public void search() {
        glBankCopyMap = new HashMap<>();
        if (glBankList != null && !glBankList.isEmpty()) {
            for (GlBankDTO gb : glBankList) {
                glBankMap.put(gb.getId(), gb);
                totalForDebitMap.put(gb.getId(), BigDecimal.ZERO);
                totalForCreditMap.put(gb.getId(), BigDecimal.ZERO);
                totalForBalanceMap.put(gb.getId(), BigDecimal.ZERO);
                openingBalanceMap.put(gb.getId(), BigDecimal.ZERO);
            }
        }

        for (Integer key : glBankMap.keySet()) {
            if (key >= cashAccountSettlementSearchBean.getGlBankFrom().getId() && key <= cashAccountSettlementSearchBean.getGlBankTo().getId()) {
                glBankCopyMap.put(key, glBankMap.get(key));
                System.out.println("Key = " + key);
            }
        }

        cashAccountSettlementSearchBean.setBranchId(branchId);

        cashAccountSettlementReports = new ArrayList<>();
        cashAccountSettlementList = cashAccountItemsViewService.findAllCashAccountSettlment(cashAccountSettlementSearchBean);
        if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
            cashAccountSettlementList.sort(new CashAccountItemsViewComparator());
        }

        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;

        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;
        SubAccountSummaryReport cashAccountSettlment;
        distinctBankId = 0;
        for (CashAccountItemsView cashAccountSettlmentView : cashAccountSettlementList) {
            if (glBankCopyMap.containsKey(cashAccountSettlmentView.getBankId())) {
                glBankCopyMap.remove(cashAccountSettlmentView.getBankId());
            }
            cashAccountSettlment = new SubAccountSummaryReport();
            cashAccountSettlment.setId(cashAccountSettlmentView.getBanktransactionId());
            cashAccountSettlment.setBankId(cashAccountSettlmentView.getBankId());
            cashAccountSettlment.setBankName(cashAccountSettlmentView.getName());
            cashAccountSettlment.setDate(cashAccountSettlmentView.getDate() != null ? cashAccountSettlmentView.getDate() : null);
            cashAccountSettlment.setRemark(cashAccountSettlmentView.getRemark() != null ? cashAccountSettlmentView.getRemark() : null);
            cashAccountSettlment.setRemark2(cashAccountSettlmentView.getRemark2() != null ? cashAccountSettlmentView.getRemark2() : null);
            cashAccountSettlment.setCode(cashAccountSettlmentView.getCode() != null ? cashAccountSettlmentView.getCode() : null);
            cashAccountSettlment.setCurrencyName(cashAccountSettlmentView.getCurrencyName() != null ? cashAccountSettlmentView.getCurrencyName() : null);
            cashAccountSettlment.setCurrencyCode(cashAccountSettlmentView.getCurrencyCode() != null ? cashAccountSettlmentView.getCurrencyCode() : null);
            cashAccountSettlment.setSerial(cashAccountSettlmentView.getSerial() != null ? cashAccountSettlmentView.getSerial().toString() : null);
            cashAccountSettlment.setTransactionTypeNumber(cashAccountSettlmentView.getTypeId());

            if (cashAccountSettlmentView.getStatus() != null) {
                if (cashAccountSettlementSearchBean.getCurrencyType() == 1) {
                    if (cashAccountSettlmentView.getStatus() == 1) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    } else if (cashAccountSettlmentView.getStatus() == 0) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    }
                } else {
                    if (cashAccountSettlmentView.getStatus() == 1) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());
                    } else if (cashAccountSettlmentView.getStatus() == 0) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    }
                }
            }

            debit = cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
            credit = cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
            balance = totalForBalanceMap.get(cashAccountSettlment.getBankId());
            balance = balance.add(debit.subtract(credit));
            totalForBalanceMap.put(cashAccountSettlment.getBankId(), balance.setScale(2, RoundingMode.UP));
            cashAccountSettlment.setBalance(balance.setScale(2, RoundingMode.UP));

            fillDebitAndCreditTotals(cashAccountSettlment, debit, credit);
            cashAccountSettlementReports.add(cashAccountSettlment);

        }

        if (cashAccountSettlementReports != null && !cashAccountSettlementReports.isEmpty()) {
            for (SubAccountSummaryReport cash : cashAccountSettlementReports) {
                cash.setTotalCredit(totalForCreditMap.get(cash.getBankId()).setScale(2, RoundingMode.UP));
                cash.setTotalDebit(totalForDebitMap.get(cash.getBankId()).setScale(2, RoundingMode.UP));
                cash.setOpeningBalance(openingBalanceMap.get(cash.getBankId()).setScale(2, RoundingMode.UP));
            }
        }

        

    }


    public void fillDebitAndCreditTotals(SubAccountSummaryReport cashAccountSettlment, BigDecimal debit, BigDecimal credit) {
        BigDecimal catchDebit = totalForDebitMap.get(cashAccountSettlment.getBankId());
        BigDecimal catchCredit = totalForCreditMap.get(cashAccountSettlment.getBankId());
        catchDebit = catchDebit.add(debit);
        catchCredit = catchCredit.add(credit);
        totalForDebitMap.put(cashAccountSettlment.getBankId(), catchDebit.setScale(2, RoundingMode.UP));
        totalForCreditMap.put(cashAccountSettlment.getBankId(), catchCredit.setScale(2, RoundingMode.UP));
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(cashAccountSettlementSearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(cashAccountSettlementSearchBean.getDateTo())) {
                resetDateTo();
            }
        }
    }

//    public void calculateExceedDateOpeningBalance() {
//        builder = new StringBuilder();
//        for (Integer key : glBankCopyMap.keySet()) {
//            if (builder.length() == 0 && builder != null) {
//                builder.append(key);
//            } else {
//                builder.append(",").append(key);
//            }
//        }
//        if (builder.length() != 0 && builder != null) {
//            cashAccountSettlementOpeningBalanceList = cashAccountSettlmentService.findAllRemainingCashAccountSettlment(cashAccountSettlementSearchBean, builder);
//        }
//        if (cashAccountSettlementOpeningBalanceList != null && !cashAccountSettlementOpeningBalanceList.isEmpty()) {
//            Set<Integer> oneRowOfGlBank = new HashSet<>();
//
//            for (CashAccountSettlmentView cashes : cashAccountSettlementOpeningBalanceList) {
//                if (!oneRowOfGlBank.contains(cashes.getBankId())) {
//                    oneRowOfGlBank.add(cashes.getBankId());
//                    BigDecimal debitOpenBalance = cashAccountSettlmentService.findOpeningBalancesForSpecificCashAccount(cashAccountSettlementSearchBean, cashes.getBankId(), 1, getGlYearSelection());
//                    BigDecimal creditOpenBalance = cashAccountSettlmentService.findOpeningBalancesForSpecificCashAccount(cashAccountSettlementSearchBean, cashes.getBankId(), 0, getGlYearSelection());
//                    openingBalance = (debitOpenBalance != null ? debitOpenBalance : BigDecimal.ZERO).subtract(creditOpenBalance != null ? creditOpenBalance : BigDecimal.ZERO);
//                    openingBalanceMap.put(cashes.getBankId(), openingBalance.setScale(2, RoundingMode.UP));
//                    totalForBalanceMap.put(cashes.getBankId(), openingBalance.setScale(2, RoundingMode.UP));
//                    if (openingBalance.compareTo(BigDecimal.ZERO) == 1) {
//                        SubAccountSummaryReport summaryReport = new SubAccountSummaryReport();
//                        summaryReport.setBankId(cashes.getBankId());
//                        summaryReport.setBankName(cashes.getName());
//                        summaryReport.setCurrencyCode(cashes.getCurrencyCode());
//                        summaryReport.setCurrencyName(cashes.getCurrencyName());
//                        summaryReport.setCode(cashes.getCode());
//                        summaryReport.setBalance(openingBalance.setScale(2, RoundingMode.UP));
//                        cashAccountSettlementReports.add(summaryReport);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void resetDateFrom() {
        cashAccountSettlementSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        cashAccountSettlementSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    public void checkFinancailYearFrom() {
        if (cashAccountSettlementSearchBean.getDateFrom().before(getGlYearSelection().getDateFrom()) || cashAccountSettlementSearchBean.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateFrom();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public void checkFinancailYearTo() {
        if (cashAccountSettlementSearchBean.getDateTo().before(getGlYearSelection().getDateFrom()) || cashAccountSettlementSearchBean.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateTo();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("cashAccountSettlementReport", userDDs.get("SAFE_ACCOUNT_BANK"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //  hashMap.put("companyName", getUserData().getCompany().getName());
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        hashMap.put("glBankFromText", userDDs.get("FROM_SAVE"));
        hashMap.put("glBankToText", userDDs.get("TO_SAFE"));
        hashMap.put("fromDateText", userDDs.get("YEAR_TO"));
        hashMap.put("toDateText", userDDs.get("YEAR_FROM"));

        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("documentNumberText", userDDs.get("TRANSACTION_NO"));
        hashMap.put("transactionTypeText", userDDs.get("TRANSACTION_TYPE"));
        hashMap.put("openingBalanceText", userDDs.get("START_BALANCE"));
        hashMap.put("remarkText", userDDs.get("STATEMENT"));
        hashMap.put("handlingText", userDDs.get("HANDLING"));

        hashMap.put("total", userDDs.get("TOTAL"));
        hashMap.put("BankNameText", userDDs.get("BANK_NAME"));
        hashMap.put("currencyText", userDDs.get("CURRENCY"));
        hashMap.put("totalDebit", totalDebit.setScale(2, RoundingMode.UP));
        hashMap.put("totalCredit", totalCredit.setScale(2, RoundingMode.UP));

        hashMap.put("glBankFrom", getCashAccountSettlementSearchBean().getGlBankFrom() != null ? getCashAccountSettlementSearchBean().getGlBankFrom().getName() : null);
        hashMap.put("glBankTo", getCashAccountSettlementSearchBean().getGlBankTo() != null ? getCashAccountSettlementSearchBean().getGlBankTo().getName() : null);

        hashMap.put("fromDate", getCashAccountSettlementSearchBean().getDateFrom());
        hashMap.put("toDate", getCashAccountSettlementSearchBean().getDateTo());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        if (cashAccountSettlementReports != null && !cashAccountSettlementReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "cashAccountSettlementReport.jasper", cashAccountSettlementReports, "pdf");
        }
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

    public String goToEdit() {
        try {
            if (glBankTransactionIdSeclected != null && glBankTransactionIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "Edit");
                redirectPage();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "CashAccountSettlementMB", "goToEdit");
            return null;
        }
    }

    private void redirectPage() {
        if (glBankTransactionTypeSeclected != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String page = null;
            switch (glBankTransactionTypeSeclected.getTransactionTypeNumber()) {
                case 0:
                    page = "glBankSelected";
                    exit("../glBank/glBankForm.xhtml");
                    break;
                case 1:
                    page = "glBankTransactionIdSeclected";
                    exit("../settlementdeed/settlementdeedForm.xhtml");
                    break;
                case 2:
                    page = "glBankTransactionIdSeclected";
                    String noteSreceivablesType = accountsSystemSettings.getNoteSreceivablesType();
                    if (noteSreceivablesType.equals("ALLOW_DETAIL")) {
                        exit("../notesreceivables/notesreceivablesdatailForm.xhtml");
                    } else {
                        exit("../notesreceivables/notesreceivablesForm.xhtml");
                    }
                    break;
                case 3:
                    page = "glBankTransactionSelected";
                    exit("../glBankTransaction/glBankTransactionForm.xhtml");
                    break;
                case 4:
                    page = "glBankTransactionSelected";
                    exit("../glBankTransaction/glBankTransactionForm.xhtml");
                    break;
                case 5:
                    page = "invPurchaseInvoiceIdSeclected";
                    exit("../invpurchaseinvoice/invpurchaseinvoiceform.xhtml");
                    break;
                case 6:
                    page = "salesinvoiceIdSeclected";
                    exit("../invsalesinvoice/invsalesinvoiceform.xhtml");
                    break;
                case 7:
                    page = "invreturnPurchaseInvoiceIdSeclected";
                    exit("../invreturnpurchase/invreturnpurchaseform.xhtml");
                    break;
                case 8:
                    page = "invPurchaseInvoiceIdSeclected";
                    exit("../invreturnsales/invreturnsalesform.xhtml");
                    break;
            }
            context.getSessionMap().put(page, glBankTransactionIdSeclected);
        }
        //  exit("../notesreceivablespre/notesreceivablesdatailFormPre.xhtml");

    }

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger("redirect error").log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public GlBankDTOConverter getGlBankConverter() {
        return glBankConverter;
    }

    public void setGlBankConverter(GlBankDTOConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    public List<GlBankDTO> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public SubAccountSummarySearchBean getCashAccountSettlementSearchBean() {
        return cashAccountSettlementSearchBean;
    }

    public void setCashAccountSettlementSearchBean(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        this.cashAccountSettlementSearchBean = cashAccountSettlementSearchBean;
    }

    public List<CashAccountItemsView> getCashAccountSettlementList() {
        return cashAccountSettlementList;
    }

    public void setCashAccountSettlementList(List<CashAccountItemsView> cashAccountSettlementList) {
        this.cashAccountSettlementList = cashAccountSettlementList;
    }

    public List<SubAccountSummaryReport> getCashAccountSettlementReports() {
        return cashAccountSettlementReports;
    }

    public void setCashAccountSettlementReports(List<SubAccountSummaryReport> cashAccountSettlementReports) {
        this.cashAccountSettlementReports = cashAccountSettlementReports;
    }

    /**
     * @return the glBankMap
     */
    public Map<Integer, GlBankDTO> getGlBankMap() {
        return glBankMap;
    }

    /**
     * @param glBankMap the glBankMap to set
     */
    public void setGlBankMap(Map<Integer, GlBankDTO> glBankMap) {
        this.glBankMap = glBankMap;
    }

    /**
     * @return the totalForDebitMap
     */
    public Map<Integer, BigDecimal> getTotalForDebitMap() {
        return totalForDebitMap;
    }

    /**
     * @param totalForDebitMap the totalForDebitMap to set
     */
    public void setTotalForDebitMap(Map<Integer, BigDecimal> totalForDebitMap) {
        this.totalForDebitMap = totalForDebitMap;
    }

    /**
     * @return the totalForCreditMap
     */
    public Map<Integer, BigDecimal> getTotalForCreditMap() {
        return totalForCreditMap;
    }

    /**
     * @param totalForCreditMap the totalForCreditMap to set
     */
    public void setTotalForCreditMap(Map<Integer, BigDecimal> totalForCreditMap) {
        this.totalForCreditMap = totalForCreditMap;
    }

    /**
     * @return the totalForBalanceMap
     */
    public Map<Integer, BigDecimal> getTotalForBalanceMap() {
        return totalForBalanceMap;
    }

    /**
     * @param totalForBalanceMap the totalForBalanceMap to set
     */
    public void setTotalForBalanceMap(Map<Integer, BigDecimal> totalForBalanceMap) {
        this.totalForBalanceMap = totalForBalanceMap;
    }

    /**
     * @return the cashTreeMap
     */
    public TreeMap<Integer, SubAccountSummaryReport> getCashTreeMap() {
        return cashTreeMap;
    }

    /**
     * @param cashTreeMap the cashTreeMap to set
     */
    public void setCashTreeMap(TreeMap<Integer, SubAccountSummaryReport> cashTreeMap) {
        this.cashTreeMap = cashTreeMap;
    }

    /**
     * @return the openingBalanceMap
     */
    public Map<Integer, BigDecimal> getOpeningBalanceMap() {
        return openingBalanceMap;
    }

    /**
     * @param openingBalanceMap the openingBalanceMap to set
     */
    public void setOpeningBalanceMap(Map<Integer, BigDecimal> openingBalanceMap) {
        this.openingBalanceMap = openingBalanceMap;
    }

    /**
     * @return the distinctBankId
     */
    public Integer getDistinctBankId() {
        return distinctBankId;
    }

    /**
     * @param distinctBankId the distinctBankId to set
     */
    public void setDistinctBankId(Integer distinctBankId) {
        this.distinctBankId = distinctBankId;
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * @return the glBankCopyMap
     */
    public Map<Integer, GlBankDTO> getGlBankCopyMap() {
        return glBankCopyMap;
    }

    /**
     * @param glBankCopyMap the glBankCopyMap to set
     */
    public void setGlBankCopyMap(Map<Integer, GlBankDTO> glBankCopyMap) {
        this.glBankCopyMap = glBankCopyMap;
    }

    /**
     * @return the builder
     */
    public StringBuilder getBuilder() {
        return builder;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public Integer getGlBankTransactionIdSeclected() {
        return glBankTransactionIdSeclected;
    }

    public void setGlBankTransactionIdSeclected(Integer glBankTransactionIdSeclected) {
        this.glBankTransactionIdSeclected = glBankTransactionIdSeclected;
    }

    /**
     * @return the glBankTransactionTypeSeclected
     */
    public SubAccountSummaryReport getGlBankTransactionTypeSeclected() {
        return glBankTransactionTypeSeclected;
    }

    /**
     * @param glBankTransactionTypeSeclected the glBankTransactionTypeSeclected
     * to set
     */
    public void setGlBankTransactionTypeSeclected(SubAccountSummaryReport glBankTransactionTypeSeclected) {
        this.glBankTransactionTypeSeclected = glBankTransactionTypeSeclected;
    }

    /**
     * @return the accountsSystemSettings
     */
    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }

    /**
     * @return the symbolConverterFrom
     */
    public SymbolConverter getSymbolConverterFrom() {
        return symbolConverterFrom;
    }

    /**
     * @param symbolConverterFrom the symbolConverterFrom to set
     */
    public void setSymbolConverterFrom(SymbolConverter symbolConverterFrom) {
        this.symbolConverterFrom = symbolConverterFrom;
    }

    /**
     * @return the symbolConverterTo
     */
    public SymbolConverter getSymbolConverterTo() {
        return symbolConverterTo;
    }

    /**
     * @param symbolConverterTo the symbolConverterTo to set
     */
    public void setSymbolConverterTo(SymbolConverter symbolConverterTo) {
        this.symbolConverterTo = symbolConverterTo;
    }

    /**
     * @return the itemsFrom
     */
    public List<Symbol> getItemsFrom() {
        if (itemsFrom == null || itemsFrom.isEmpty()) {
            itemsFrom = symbolService.getGLItemsByCompanyId(getUserData().getCompany().getId(),null);
            setSymbolConverterFrom(new SymbolConverter(itemsFrom));
        }
        return itemsFrom;
    }

    /**
     * @param itemsFrom the itemsFrom to set
     */
    public void setItemsFrom(List<Symbol> itemsFrom) {
        this.itemsFrom = itemsFrom;
    }

    /**
     * @return the itemsTo
     */
    public List<Symbol> getItemsTo() {
        if (itemsTo == null || itemsTo.isEmpty()) {
            itemsTo = symbolService.getGLItemsByCompanyId(getUserData().getCompany().getId(),null);
            setSymbolConverterTo(new SymbolConverter(itemsTo));
        }
        return itemsTo;
    }

    /**
     * @param itemsTo the itemsTo to set
     */
    public void setItemsTo(List<Symbol> itemsTo) {
        this.itemsTo = itemsTo;
    }
}
