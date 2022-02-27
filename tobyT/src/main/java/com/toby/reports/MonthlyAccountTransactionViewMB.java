package com.toby.reports;

import com.toby.businessservice.report.BankBalanceViewService;
import com.toby.businessservice.report.MonthlyAccountTransactionViewService;
import com.toby.report.entity.MonthlyAccountTransactionReportBean;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.reports.entityBean.MonthlyAccountTransactionBean;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.GlAccountReportSearchBean;
import com.toby.businessservice.reports.searchBean.MonthlyAccountTransactionSearchBean;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.Symbol;
import com.toby.report.entity.MonthlyAccountTransactionForReportBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import net.sf.jasperreports.engine.JRException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.ListUtils;

@Named("monthlyAccountTransactionViewMB")
@ViewScoped
public class MonthlyAccountTransactionViewMB extends BaseGlAccountReportBean {

    private List<MonthlyAccountTransactionBean> monthlyAccountTransactionViewList;
    private MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean;

    private List<GlAccount> glAccountForSummaryList;
    private GlAccountReportSearchBean glAccountReportSearchBean;

    private List<MonthlyAccountTransactionReportBean> accountTransactionReportBeanList;
    private MonthlyAccountTransactionReportBean accountTransactionReportBean;

    private List<MonthlyAccountTransactionForReportBean> monthlyAccountTransactionForReportBeanList;
    private MonthlyAccountTransactionForReportBean monthlyAccountTransactionForReportBean;

    private UserData userData;

    private List<Symbol> symbolList;
    private CommonSearchBean commonSearchBean;

    private List<GeneralJournalDetails> generalJournalDetailsList;
    private List<GeneralJournalDetails> generalJournalDetailsFirstList;
    private List<GeneralJournalDetails> generalJournalDetailsSecondList;
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();
    private CostCenter costCenterFromSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private GlaccountConverter accountConverter;
    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;

    @EJB
    MonthlyAccountTransactionViewService monthlyAccountTransactionViewService;

    @EJB
    GlAccountService glAccountService;

    @EJB
    SymbolService symbolService;

    @EJB
    BankBalanceViewService bankBalanceViewService;

    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    public MonthlyAccountTransactionViewMB() {
    }

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        load();
        reset();
        prepareOpeneingBlancesValues();
        // Amr
        costCenterConverter = new CostCenterConverter(getCostCenterList());
        accountConverter = new GlaccountConverter(getGlAccountList());
        glAdminUnitConverter = new GlAdminUnitConverter(getGlAdminUnitList());

    }

    @Override
    public void reset() {
        monthlyAccountTransactionViewList = new ArrayList<>(0);
        glAccountForSummaryList = new ArrayList<>();
        monthlyAccountTransactionSearchBean = new MonthlyAccountTransactionSearchBean();
        glAccountReportSearchBean = new GlAccountReportSearchBean();
        accountTransactionReportBeanList = new ArrayList<>();
        monthlyAccountTransactionForReportBeanList = new ArrayList<>();
        commonSearchBean = new CommonSearchBean();
        commonSearchBean.setDateFrom(getGlYearSelection().getDateFrom());
        commonSearchBean.setDateTo(getGlYearSelection().getDateTo());
        glAccountSelectedFrom = null;
        glAccountSelectedTo = null;
        costCenterFromSelected = null;
        glAdminUnitFromSelected = null;

    }

    @Override
    public void search() {

        monthlyAccountTransactionViewList = new ArrayList<>(0);
        monthlyAccountTransactionSearchBean.setBranchId(getUserData().getUserBranch().getId());
        monthlyAccountTransactionSearchBean.setAccountCodeFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        monthlyAccountTransactionSearchBean.setAccountCodeTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        monthlyAccountTransactionSearchBean.setCostCenterId(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
        monthlyAccountTransactionSearchBean.setAdminUnitId(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        glAccountReportSearchBean.setBranchId(getUserData().getUserBranch().getId());
        glAccountReportSearchBean.setAccountNumberFrom(monthlyAccountTransactionSearchBean.getAccountCodeFrom());
        glAccountReportSearchBean.setAccountNumberTo(monthlyAccountTransactionSearchBean.getAccountCodeTo());
        glAccountForSummaryList = glAccountService.getAllGlAccountsForLowerLevel(glAccountReportSearchBean);
        accountTransactionReportBean = new MonthlyAccountTransactionReportBean(Integer.MIN_VALUE, null, monthlyAccountTransactionViewList);
        accountTransactionReportBeanList = new ArrayList<>();
        for (GlAccount ga : glAccountForSummaryList) {
            if (glAccountForSummaryList != null || !glAccountForSummaryList.isEmpty()) {
                monthlyAccountTransactionSearchBean.setAccountCodeFrom(ga.getAccNumber());
                monthlyAccountTransactionViewList = monthlyAccountTransactionViewService.getMonthlyAccountTransactionViewList(monthlyAccountTransactionSearchBean, glAccountForSummaryList);
                if (monthlyAccountTransactionViewList != null || !monthlyAccountTransactionViewList.isEmpty()) {
                    accountTransactionReportBean = new MonthlyAccountTransactionReportBean(Integer.MIN_VALUE, null, monthlyAccountTransactionViewList);
                    accountTransactionReportBean.setMonthlyAccountTransactionBeans(new ArrayList<MonthlyAccountTransactionBean>());

                    accountTransactionReportBean.setAccountName(ga.getName() != null ? ga.getName() : null);
                    accountTransactionReportBean.setAccountNumber(ga.getAccNumber() != null ? ga.getAccNumber() : null);
                    Integer addOpeningBalanceReference = 0;
                    for (MonthlyAccountTransactionBean oneMonthlyAccountTransactionView : monthlyAccountTransactionViewList) {

                        if (addOpeningBalanceReference == 0 && totalBalanceMap.get(ga.getId()) != null) {
                            MonthlyAccountTransactionBean monthlyAccountTransactionBeanIterated = new MonthlyAccountTransactionBean();
                            monthlyAccountTransactionBeanIterated.setBalance(totalBalanceMap.get(ga.getId()) != null ? totalBalanceMap.get(ga.getId()) : BigDecimal.ZERO);
                            monthlyAccountTransactionBeanIterated.setCreditAmount(BigDecimal.ZERO);
                            monthlyAccountTransactionBeanIterated.setDebitAmount(BigDecimal.ZERO);
                            monthlyAccountTransactionBeanIterated.setMonthDate(null);
                            accountTransactionReportBean.getMonthlyAccountTransactionBeans().add(monthlyAccountTransactionBeanIterated);
                            addOpeningBalanceReference++;
                        }
                        MonthlyAccountTransactionBean monthlyAccountTransactionBeanIterated = new MonthlyAccountTransactionBean();
                        monthlyAccountTransactionBeanIterated.setBalance(oneMonthlyAccountTransactionView.getBalance() != null ? oneMonthlyAccountTransactionView.getBalance() : BigDecimal.ZERO);
                        monthlyAccountTransactionBeanIterated.setCreditAmount(oneMonthlyAccountTransactionView.getCreditAmount() != null ? oneMonthlyAccountTransactionView.getCreditAmount() : BigDecimal.ZERO);
                        monthlyAccountTransactionBeanIterated.setDebitAmount(oneMonthlyAccountTransactionView.getDebitAmount() != null ? oneMonthlyAccountTransactionView.getDebitAmount() : BigDecimal.ZERO);
                        monthlyAccountTransactionBeanIterated.setMonthDate(oneMonthlyAccountTransactionView.getMonthDate() != null ? oneMonthlyAccountTransactionView.getMonthDate() : null);

                        accountTransactionReportBean.getMonthlyAccountTransactionBeans().add(monthlyAccountTransactionBeanIterated);

                        if (addOpeningBalanceReference == 1 && totalBalanceMap.get(ga.getId()) != null) {
                            monthlyAccountTransactionForReportBean = new MonthlyAccountTransactionForReportBean();

                            monthlyAccountTransactionForReportBean.setAccountName(ga.getName());
                            monthlyAccountTransactionForReportBean.setAccountNumber(ga.getAccNumber());
                            monthlyAccountTransactionForReportBean.setBalance(totalBalanceMap.get(ga.getId()) != null ? totalBalanceMap.get(ga.getId()) : BigDecimal.ZERO);
                            monthlyAccountTransactionForReportBean.setCreditAmount(BigDecimal.ZERO);
                            monthlyAccountTransactionForReportBean.setDebitAmount(BigDecimal.ZERO);
                            monthlyAccountTransactionForReportBean.setMonthDate(null);
                            monthlyAccountTransactionForReportBeanList.add(monthlyAccountTransactionForReportBean);
                            addOpeningBalanceReference++;
                        }

                        monthlyAccountTransactionForReportBean = new MonthlyAccountTransactionForReportBean();
                        monthlyAccountTransactionForReportBean.setAccountName(ga.getName());
                        monthlyAccountTransactionForReportBean.setAccountNumber(ga.getAccNumber());
                        monthlyAccountTransactionForReportBean.setBalance(oneMonthlyAccountTransactionView.getBalance() != null ? oneMonthlyAccountTransactionView.getBalance() : BigDecimal.ZERO);
                        monthlyAccountTransactionForReportBean.setCreditAmount(oneMonthlyAccountTransactionView.getCreditAmount() != null ? oneMonthlyAccountTransactionView.getCreditAmount() : BigDecimal.ZERO);
                        monthlyAccountTransactionForReportBean.setDebitAmount(oneMonthlyAccountTransactionView.getDebitAmount() != null ? oneMonthlyAccountTransactionView.getDebitAmount() : BigDecimal.ZERO);
                        monthlyAccountTransactionForReportBean.setMonthDate(oneMonthlyAccountTransactionView.getMonthDate());
                        monthlyAccountTransactionForReportBeanList.add(monthlyAccountTransactionForReportBean);

                    }
                    accountTransactionReportBeanList.add(accountTransactionReportBean);
                }
            }
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (monthlyAccountTransactionForReportBeanList != null && !monthlyAccountTransactionForReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "monthlyAccountTransactionReport.jasper", monthlyAccountTransactionForReportBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    public void secondReport() throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(accountTransactionReportBean.getMonthlyAccountTransactionBeans());
        JasperPrint jasperPrint = JasperFillManager.fillReport(getUserData().getReportPath() + "monthlyAccountTransactionReport.jasper", prepareReport(), beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
    }

    public void prepareOpeneingBlancesValues() {
        symbolList = new ArrayList<>();
        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        totalBalanceMap = new HashMap<>();
        symbolList = symbolService.getTheOpeningBalanceId(getUserData().getCompany().getId());
        Symbol openingBalanceId = new Symbol();
        if (symbolList != null && !symbolList.isEmpty()) {
            openingBalanceId = symbolList.get(0);
        }
        if (openingBalanceId != null) {
            generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(getUserData().getUserBranch().getId(), 1, openingBalanceId.getId(), commonSearchBean, getGlYearSelection());
            generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(getUserData().getUserBranch().getId(), 2, openingBalanceId.getId(), commonSearchBean, getGlYearSelection());
            generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        }
        for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
            totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
        }
    }

    public List<MonthlyAccountTransactionBean> getMonthlyAccountTransactionViewList() {
        return monthlyAccountTransactionViewList;
    }

    public void setMonthlyAccountTransactionViewList(List<MonthlyAccountTransactionBean> monthlyAccountTransactionViewList) {
        this.monthlyAccountTransactionViewList = monthlyAccountTransactionViewList;
    }

    public MonthlyAccountTransactionSearchBean getMonthlyAccountTransactionSearchBean() {
        return monthlyAccountTransactionSearchBean;
    }

    public void setMonthlyAccountTransactionSearchBean(MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean) {
        this.monthlyAccountTransactionSearchBean = monthlyAccountTransactionSearchBean;
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = userData.getUserDDs();

        HashMap hashMap = new HashMap();
       
        hashMap.put("accountCodeFrom", monthlyAccountTransactionSearchBean.getAccountCodeFrom());
        hashMap.put("accountCodeTo", monthlyAccountTransactionSearchBean.getAccountCodeTo());
        hashMap.put("adminUnitId", monthlyAccountTransactionSearchBean.getAdminUnitId());
        hashMap.put("costCenterId", monthlyAccountTransactionSearchBean.getCostCenterId());

        hashMap.put("fromCostCenter", costCenterFromSelected != null ? costCenterFromSelected.getCode() : null);
        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);

        hashMap.put("fromAdminUnit", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getCode() : null);
        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);

        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);

        hashMap.put("monthlyAccountReportText", userDDs.get("MONTHLY_ACCOUNT_REPORT"));
        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
        hashMap.put("costCenterFromText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("AdminUnitfromText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("PrintedBy", userData.getUser().getName());
        //    hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

    public List<CostCenter> completeCostCenter(String query) {
        List<CostCenter> costCenterList = getCostCenterList();
        if (query == null || query.trim().equals("")) {
            costCenterConverter = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < getCostCenterList().size(); i++) {
            costCenter = costCenterList.get(i);
            nameAr = costCenter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }

            code = costCenter.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }
        }

        costCenterConverter = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = getGlAccountList();//45
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
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

        accountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        List<GlAdminUnit> glAdminUnits = getGlAdminUnitList();
        if (query == null || query.trim().equals("")) {
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < getGlAdminUnitList().size(); i++) {
            glAdminUnit = glAdminUnits.get(i);
            nameAr = glAdminUnit.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }

            code = glAdminUnit.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }
        }
        glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the glAccountReportSearchBean
     */
    public GlAccountReportSearchBean getGlAccountReportSearchBean() {
        return glAccountReportSearchBean;
    }

    /**
     * @param glAccountReportSearchBean the glAccountReportSearchBean to set
     */
    public void setGlAccountReportSearchBean(GlAccountReportSearchBean glAccountReportSearchBean) {
        this.glAccountReportSearchBean = glAccountReportSearchBean;
    }

    /**
     * @return the glAccountForSummaryList
     */
    public List<GlAccount> getGlAccountForSummaryList() {
        return glAccountForSummaryList;
    }

    /**
     * @param glAccountForSummaryList the glAccountForSummaryList to set
     */
    public void setGlAccountForSummaryList(List<GlAccount> glAccountForSummaryList) {
        this.glAccountForSummaryList = glAccountForSummaryList;
    }

    /**
     * @return the accountTransactionReportBeanList
     */
    public List<MonthlyAccountTransactionReportBean> getAccountTransactionReportBeanList() {
        return accountTransactionReportBeanList;
    }

    /**
     * @param accountTransactionReportBeanList the
     * accountTransactionReportBeanList to set
     */
    public void setAccountTransactionReportBeanList(List<MonthlyAccountTransactionReportBean> accountTransactionReportBeanList) {
        this.accountTransactionReportBeanList = accountTransactionReportBeanList;
    }

    /**
     * @return the accountTransactionReportBean
     */
    public MonthlyAccountTransactionReportBean getAccountTransactionReportBean() {
        return accountTransactionReportBean;
    }

    /**
     * @param accountTransactionReportBean the accountTransactionReportBean to
     * set
     */
    public void setAccountTransactionReportBean(MonthlyAccountTransactionReportBean accountTransactionReportBean) {
        this.accountTransactionReportBean = accountTransactionReportBean;
    }

    /**
     * @return the monthlyAccountTransactionForReportBeanList
     */
    public List<MonthlyAccountTransactionForReportBean> getMonthlyAccountTransactionForReportBeanList() {
        return monthlyAccountTransactionForReportBeanList;
    }

    /**
     * @param monthlyAccountTransactionForReportBeanList the
     * monthlyAccountTransactionForReportBeanList to set
     */
    public void setMonthlyAccountTransactionForReportBeanList(List<MonthlyAccountTransactionForReportBean> monthlyAccountTransactionForReportBeanList) {
        this.monthlyAccountTransactionForReportBeanList = monthlyAccountTransactionForReportBeanList;
    }

    /**
     * @return the monthlyAccountTransactionForReportBean
     */
    public MonthlyAccountTransactionForReportBean getMonthlyAccountTransactionForReportBean() {
        return monthlyAccountTransactionForReportBean;
    }

    /**
     * @param monthlyAccountTransactionForReportBean the
     * monthlyAccountTransactionForReportBean to set
     */
    public void setMonthlyAccountTransactionForReportBean(MonthlyAccountTransactionForReportBean monthlyAccountTransactionForReportBean) {
        this.monthlyAccountTransactionForReportBean = monthlyAccountTransactionForReportBean;
    }

    /**
     * @return the symbolList
     */
    public List<Symbol> getSymbolList() {
        return symbolList;
    }

    /**
     * @param symbolList the symbolList to set
     */
    public void setSymbolList(List<Symbol> symbolList) {
        this.symbolList = symbolList;
    }

    /**
     * @return the generalJournalDetailsList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsList() {
        return generalJournalDetailsList;
    }

    /**
     * @param generalJournalDetailsList the generalJournalDetailsList to set
     */
    public void setGeneralJournalDetailsList(List<GeneralJournalDetails> generalJournalDetailsList) {
        this.generalJournalDetailsList = generalJournalDetailsList;
    }

    /**
     * @return the generalJournalDetailsFirstList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsFirstList() {
        return generalJournalDetailsFirstList;
    }

    /**
     * @param generalJournalDetailsFirstList the generalJournalDetailsFirstList
     * to set
     */
    public void setGeneralJournalDetailsFirstList(List<GeneralJournalDetails> generalJournalDetailsFirstList) {
        this.generalJournalDetailsFirstList = generalJournalDetailsFirstList;
    }

    /**
     * @return the generalJournalDetailsSecondList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsSecondList() {
        return generalJournalDetailsSecondList;
    }

    /**
     * @param generalJournalDetailsSecondList the
     * generalJournalDetailsSecondList to set
     */
    public void setGeneralJournalDetailsSecondList(List<GeneralJournalDetails> generalJournalDetailsSecondList) {
        this.generalJournalDetailsSecondList = generalJournalDetailsSecondList;
    }

    /**
     * @return the totalBalanceMap
     */
    public Map<Integer, BigDecimal> getTotalBalanceMap() {
        return totalBalanceMap;
    }

    /**
     * @param totalBalanceMap the totalBalanceMap to set
     */
    public void setTotalBalanceMap(Map<Integer, BigDecimal> totalBalanceMap) {
        this.totalBalanceMap = totalBalanceMap;
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
     * @return the costCenterFromSelected
     */
    public CostCenter getCostCenterFromSelected() {
        return costCenterFromSelected;
    }

    /**
     * @param costCenterFromSelected the costCenterFromSelected to set
     */
    public void setCostCenterFromSelected(CostCenter costCenterFromSelected) {
        this.costCenterFromSelected = costCenterFromSelected;
    }

    /**
     * @return the glAccountSelectedTo
     */
    public GlAccount getGlAccountSelectedTo() {
        return glAccountSelectedTo;
    }

    /**
     * @param glAccountSelectedTo the glAccountSelectedTo to set
     */
    public void setGlAccountSelectedTo(GlAccount glAccountSelectedTo) {
        this.glAccountSelectedTo = glAccountSelectedTo;
    }

    /**
     * @return the glAccountSelectedFrom
     */
    public GlAccount getGlAccountSelectedFrom() {
        return glAccountSelectedFrom;
    }

    /**
     * @param glAccountSelectedFrom the glAccountSelectedFrom to set
     */
    public void setGlAccountSelectedFrom(GlAccount glAccountSelectedFrom) {
        this.glAccountSelectedFrom = glAccountSelectedFrom;
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
     * @return the costCenterConverter
     */
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the glAdminUnitConverter
     */
    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    /**
     * @param glAdminUnitConverter the glAdminUnitConverter to set
     */
    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    /**
     * @return the glAdminUnitFromSelected
     */
    public GlAdminUnit getGlAdminUnitFromSelected() {
        return glAdminUnitFromSelected;
    }

    /**
     * @param glAdminUnitFromSelected the glAdminUnitFromSelected to set
     */
    public void setGlAdminUnitFromSelected(GlAdminUnit glAdminUnitFromSelected) {
        this.glAdminUnitFromSelected = glAdminUnitFromSelected;
    }

}
