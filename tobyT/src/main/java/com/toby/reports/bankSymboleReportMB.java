package com.toby.reports;

import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.reports.searchBean.GlBankEntitySearch;
import com.toby.converter.CurrencyConverter;
import com.toby.converter.GlBankConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.GlBank;
import com.toby.entiy.GlBankEntity;
import com.toby.report.entity.GlBankReport;
import com.toby.toby.BaseGlAccountReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "bankSymboleReportMB")
@ViewScoped
public class bankSymboleReportMB extends BaseGlAccountReportBean {

    private Integer branchId;
    private GlBankConverter glBankConverter;
    private CurrencyConverter currencyConverter;
    private GlaccountConverter accountConverter;

    private List<GlBank> glBankList;
    private List<Currency> allCurrencies;
    private List<Currency> currencyList;
    private List<GlAccount> accountList;
    private List<GlAccount> allAccounts;

    private GlBankEntity glBankEntity;

    private GlBankEntitySearch glBankEntitySearch;

    private List<GlBankReport> glBankReportList;
    List<GlBank> allGlBanks = new ArrayList<>();

    private Boolean store = Boolean.FALSE;
    private Boolean bank = Boolean.FALSE;
    private Boolean bankStore = Boolean.FALSE;

    @EJB
    GlAccountService glAccountService;

    @EJB
    GlYearService glYearService;

    @EJB
    CostCenterService costCenterService;

    @EJB
    GlBankService glBankService;

    @PostConstruct
    public void init() {

        if (getGlYearSelection() != null) {
            load();
            reset();
        } else {
            redirectFinancailYearPage();
        }
    }

    @Override
    public void load() {
        super.load();
        branchId = getUserData().getUserBranch().getId();

        glBankList = new ArrayList<>();
        currencyList = new ArrayList<>();
        allCurrencies = new ArrayList<>();
        allAccounts = new ArrayList<>();
        accountList = new ArrayList<>();
        glBankReportList = new ArrayList<>();

        glBankEntitySearch = new GlBankEntitySearch();
        glBankEntitySearch.setType(3);

        viewFields();
        fillLists();

    }

    private void fillLists() {
        glBankList = glBankService.getAllGlBankByBranchId(branchId);

        for (GlBank glBank : glBankList) {
            allCurrencies.add(glBank.getCurrencyId());
            allAccounts.add(glBank.getAccountId());
        }

        currencyList = new ArrayList<>(new HashSet<>(allCurrencies));
        accountList = new ArrayList<>(new HashSet<>(allAccounts));

        glBankConverter = new GlBankConverter(glBankList);
        currencyConverter = new CurrencyConverter(currencyList);
        accountConverter = new GlaccountConverter(accountList);
    }

    @Override
    public void reset() {
        load();
        resetDateFrom();
        resetDateTo();
    }

    public void saveBankFrom(SelectEvent event) {
        GlBank bankFrom = (GlBank) event.getObject();
        glBankEntitySearch.setBankFrom(bankFrom);
    }

    public void saveBankTo(SelectEvent event) {
        GlBank bankTo = (GlBank) event.getObject();
        glBankEntitySearch.setBankTo(bankTo);
    }

    @Override
    public void search() {

        viewFields();
        glBankEntitySearch.setBranchId(branchId);

        glBankReportList = new ArrayList<>();
        allGlBanks = glBankService.findAllGlBankForReport(glBankEntitySearch);

        GlBankReport bankReport;
        for (GlBank glBank : allGlBanks) {
            bankReport = new GlBankReport();
            bankReport.setId(glBank.getId());
            bankReport.setCode(glBank.getCode() != null ? glBank.getCode() : null);
            bankReport.setBankName(glBank.getName() != null ? glBank.getName() : null);
            bankReport.setAccounntCode((glBank.getAccountId() != null && glBank.getAccountId().getAccNumber() != null) ? glBank.getAccountId().getAccNumber() : null);
            bankReport.setAccounntName((glBank.getAccountId() != null && glBank.getAccountId().getName() != null) ? glBank.getAccountId().getName() : null);
            bankReport.setCurrencyName((glBank.getCurrencyId() != null && glBank.getCurrencyId().getName() != null) ? glBank.getCurrencyId().getName() : null);

            if (glBank.getType() != null) {
                if (glBank.getType() == 0) {
                    bankReport.setTypeName("خزنة");
                } else if (glBank.getType() == 1) {
                    bankReport.setTypeName("بنك");
                } else if (glBank.getType() == 2) {
                    bankReport.setTypeName("خزينة شيكات خطية");
                }

                bankReport.setType(glBank.getType());
            }

            glBankReportList.add(bankReport);
        }
    }

    private void viewFields() {
        if (glBankEntitySearch.getType() == 0 || glBankEntitySearch.getType() == 3) {
            store = Boolean.TRUE;
            bank = Boolean.FALSE;
            bankStore = Boolean.FALSE;
        } else if (glBankEntitySearch.getType() == 1) {
            store = Boolean.FALSE;
            bank = Boolean.TRUE;
            bankStore = Boolean.FALSE;
        } else if (glBankEntitySearch.getType() == 2) {
            store = Boolean.FALSE;
            bank = Boolean.FALSE;
            bankStore = Boolean.TRUE;
        }
    }

    public List<GlBank> completeGlBankTo(String query) {
        List<GlBank> glBanksList = getGlBankList();
        if (query == null || query.trim().equals("")) {

            glBankConverter = new GlBankConverter(glBanksList);
            return glBanksList;
        }
        List<GlBank> filteredGlBanks = new ArrayList<>();

        String nameAr;
        String code;
        GlBank glBankFilter;
        for (int i = 0; i < getGlBankList().size(); i++) {
            glBankFilter = glBanksList.get(i);
            nameAr = glBankFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlBanks.contains(glBankFilter)) {
                    filteredGlBanks.add(glBankFilter);
                }
            }

            code = glBankFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlBanks.contains(glBankFilter)) {
                    filteredGlBanks.add(glBankFilter);
                }
            }
        }

        glBankConverter = new GlBankConverter(filteredGlBanks);
        return filteredGlBanks;
    }

    public List<Currency> completeCurrenecy(String query) {
        List<Currency> currencysList = getCurrencyList();
        if (query == null || query.trim().equals("")) {

            currencyConverter = new CurrencyConverter(currencysList);
            return currencysList;
        }
        List<Currency> filteredCurencies = new ArrayList<>();

        String nameAr;
        String code;
        Currency currencyFilter;
        for (int i = 0; i < getCurrencyList().size(); i++) {
            currencyFilter = currencysList.get(i);
            nameAr = currencyFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCurencies.contains(currencyFilter)) {
                    filteredCurencies.add(currencyFilter);
                }
            }

            code = currencyFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCurencies.contains(currencyFilter)) {
                    filteredCurencies.add(currencyFilter);
                }
            }
        }

        currencyConverter = new CurrencyConverter(filteredCurencies);
        return filteredCurencies;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = accountList;
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < accountList.size(); i++) {
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

        accountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(glBankEntitySearch.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(glBankEntitySearch.getDateTo())) {
                resetDateTo();
            }
        }
    }

    @Override
    public void resetDateFrom() {
        glBankEntitySearch.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        glBankEntitySearch.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    public void checkFinancailYearFrom() {
        if (glBankEntitySearch.getDateFrom().before(getGlYearSelection().getDateFrom()) || glBankEntitySearch.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateFrom();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public void checkFinancailYearTo() {
        if (glBankEntitySearch.getDateTo().before(getGlYearSelection().getDateFrom()) || glBankEntitySearch.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateTo();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    @Override
    public HashMap prepareReport() {

        HashMap hashMap = new HashMap();

        hashMap.put("bankSymboleReport", userDDs.get("SAFE_GUID_REP"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("companyName", getUserData().getCompany().getName());
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (glBankEntitySearch.getType() == 0 || glBankEntitySearch.getType() == 3) {
            hashMap.put("glBankFromText", userDDs.get("FROM_SAVE"));
            hashMap.put("glBankToText", userDDs.get("TO_SAFE"));
        } else if (glBankEntitySearch.getType() == 1) {
            hashMap.put("glBankFromText", userDDs.get("FROM_BANK"));
            hashMap.put("glBankToText", userDDs.get("TO_BANK"));
        } else if (glBankEntitySearch.getType() == 2) {
            hashMap.put("glBankFromText", userDDs.get("FROM_TRES_WRI_CHE"));
            hashMap.put("glBankToText", userDDs.get("TO_TRES_WRI_CHE"));
        }

//        hashMap.put("currencyFromText", "من عملة");
//        hashMap.put("currencyToText", "الى عملة");
        hashMap.put("accountFromText", userDDs.get("FROM_ACCOUNT"));
        hashMap.put("accountToText", userDDs.get("TO_ACCOUNT"));

        hashMap.put("codeText", userDDs.get("CS_COD"));
        hashMap.put("nameText", userDDs.get("NAME"));
        hashMap.put("accountCodeText", userDDs.get("ACCOUNT_COD"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("currencyText", userDDs.get("CURRENCY"));
        hashMap.put("typeText", userDDs.get("TYPE"));

        hashMap.put("glBankFrom", glBankEntitySearch.getBankFrom() != null ? glBankEntitySearch.getBankFrom().getName() : null);
        hashMap.put("glBankTo", glBankEntitySearch.getBankTo() != null ? glBankEntitySearch.getBankTo().getName() : null);

        hashMap.put("accountFrom", glBankEntitySearch.getAccountFrom() != null ? glBankEntitySearch.getAccountFrom().getName() : null);
        hashMap.put("accountTo", glBankEntitySearch.getAccountTo() != null ? glBankEntitySearch.getAccountTo().getName() : null);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (glBankReportList != null && !glBankReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "glBankReport.jasper", glBankReportList, "pdf");
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

    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }

    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    public GlBankEntity getGlBankEntity() {
        return glBankEntity;
    }

    public void setGlBankEntity(GlBankEntity glBankEntity) {
        this.glBankEntity = glBankEntity;
    }

    public GlBankEntitySearch getGlBankEntitySearch() {
        return glBankEntitySearch;
    }

    public void setGlBankEntitySearch(GlBankEntitySearch glBankEntitySearch) {
        this.glBankEntitySearch = glBankEntitySearch;
    }

    public Boolean getStore() {
        return store;
    }

    public void setStore(Boolean store) {
        this.store = store;
    }

    public Boolean getBank() {
        return bank;
    }

    public void setBank(Boolean bank) {
        this.bank = bank;
    }

    public Boolean getBankStore() {
        return bankStore;
    }

    public void setBankStore(Boolean bankStore) {
        this.bankStore = bankStore;
    }

    public List<GlBankReport> getGlBankReportList() {
        return glBankReportList;
    }

    public void setGlBankReportList(List<GlBankReport> glBankReportList) {
        this.glBankReportList = glBankReportList;
    }

    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }

    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    public List<GlAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<GlAccount> accountList) {
        this.accountList = accountList;
    }

    public List<Currency> getAllCurrencies() {
        return allCurrencies;
    }

    public void setAllCurrencies(List<Currency> allCurrencies) {
        this.allCurrencies = allCurrencies;
    }

    public List<GlAccount> getAllAccounts() {
        return allAccounts;
    }

    public void setAllAccounts(List<GlAccount> allAccounts) {
        this.allAccounts = allAccounts;
    }

    private static class userDDs {

        private static Object get(String safe_guid_rep) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public userDDs() {
        }
    }
}
