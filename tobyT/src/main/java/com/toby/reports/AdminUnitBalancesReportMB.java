/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.GlAdminUnitService;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.report.entity.IncomeMenuBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named(value = "adminUnitBalancesReportMB")
@ViewScoped
public class AdminUnitBalancesReportMB extends ComplexRevisionBalanceMB {

    private List<GlAdminUnit> adminUnitList;
    private StringBuilder stringBuilder;
    private GlAdminUnitConverter adminUnitConverterFrom;
    private GlAdminUnitConverter adminUnitConverterTo;
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private List<GlAdminUnit> glAdminUnitToList;
    private List<GlAdminUnit> glAdminUnitFromList;
    private Map<Integer, GlAdminUnit> glAdminUnitMap = new HashMap<>();
    private TreeMap<Integer, GlAdminUnit> glAdminUnitTreeMap = new TreeMap<>();
    private List<GlAdminUnit> adminUnitRootOrderedList;
    private List<IncomeMenuBean> incomeMenuBeanCombinedViewList;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private GlaccountConverter accountConverter;

    @EJB
    private GlAdminUnitService glAdminUnitService;

    @PostConstruct
    public void init() {
        resetSome();
        load();
        loadAllAdminUnitFrom();
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;
        accountConverter = new GlaccountConverter(getGlAccountList());

    }

    public void fillStringBuilder() {
        stringBuilder = new StringBuilder();
        if (adminUnitList != null && !adminUnitList.isEmpty()) {
            for (GlAdminUnit gau : adminUnitList) {
                if (stringBuilder != null && stringBuilder.length() == 0) {
                    stringBuilder.append(gau.getId().toString());
                } else {
                    stringBuilder.append(",").append(gau.getId().toString());
                }
            }
            super.setStringBuilderParent(new StringBuilder());
            super.setStringBuilderParent(stringBuilder);
        }
    }

    public void resetSome() {
        reset();
        adminUnitList = new ArrayList<>();
        glAdminUnitFromList = new ArrayList<>();
        loadAllAdminUnitFrom();
        glAdminUnitToList = new ArrayList<>();
        adminUnitRootOrderedList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        adminUnitConverterFrom = new GlAdminUnitConverter(getGlAdminUnitList());
        glAdminUnitMap = new HashMap<>();
        glAdminUnitTreeMap = new TreeMap<>();
        incomeMenuBeanCombinedViewList = new ArrayList<>();
        glAccountSelectedFrom = null;
        glAccountSelectedTo = null;
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        super.setTotalDebit(BigDecimal.ZERO);
        super.setTotalCredit(BigDecimal.ZERO);
        super.setTotalFirstBalance(BigDecimal.ZERO);
    }

    public void intializeData() {
        glAdminUnitMap = new HashMap<>();
        glAdminUnitTreeMap = new TreeMap<>();
        incomeMenuBeanCombinedViewList = new ArrayList<>();
        adminUnitRootOrderedList = new ArrayList<>();
        getComplexRevisionBalanceSearchBeanA().setAccountFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        getComplexRevisionBalanceSearchBeanA().setAccountTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        super.setTotalDebit(BigDecimal.ZERO);
        super.setTotalCredit(BigDecimal.ZERO);
        super.setTotalFirstBalance(BigDecimal.ZERO);
    }

    public void startSearch() {
        intializeData();
        if (glAdminUnitFromSelected != null && glAdminUnitToSelected != null) {
            for (GlAdminUnit ga : glAdminUnitToList) {
                if (ga.getCode() >= glAdminUnitFromSelected.getCode() && ga.getCode() <= glAdminUnitToSelected.getCode()) {
                    glAdminUnitTreeMap.put(ga.getCode(), ga);
                }
            }
            adminUnitRootOrderedList = new ArrayList<>(glAdminUnitTreeMap.values());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار من وحدة ادارية الى وحدة ادارية"));
        }
        adminUnitList = new ArrayList<>();
        if (adminUnitRootOrderedList != null && !adminUnitRootOrderedList.isEmpty()) {
            for (GlAdminUnit gUnit : adminUnitRootOrderedList) {
                adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActiveAndCostCenterId(gUnit.getId());
                fillStringBuilder();
                super.search();
                for (IncomeMenuBean imb : super.getIncomeMenuBeanViewList()) {
                    if (imb.getDebit().compareTo(BigDecimal.ZERO) != 0 || imb.getCredit().compareTo(BigDecimal.ZERO) != 0
                            || imb.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                        imb.setAccountGroup(gUnit.getName());
                        imb.setParent(gUnit.getCode());
                        imb.setCreditBalance(super.getTotalCredit());
                        imb.setDebitBalance(super.getTotalDebit());
                        imb.setFirstDurationBalance(super.getTotalFirstBalance());
                        incomeMenuBeanCombinedViewList.add(imb);
                    }
                }
            }
        }
    }

    public void loadAllAdminUnitFrom() {
        glAdminUnitFromList = glAdminUnitService.getAllAdminUnitByBranchIdActive(getUserData().getUserBranch().getId());
    }

    public void updateAdminUnitTo() {
        glAdminUnitToList = new ArrayList<>();
        if (glAdminUnitFromSelected != null && glAdminUnitFromSelected.getId() != null) {
            glAdminUnitToList = glAdminUnitService.getAllSubAdminUnitByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), glAdminUnitFromSelected.getLevel());
            adminUnitConverterTo = new GlAdminUnitConverter(glAdminUnitToList);
        }
    }

    public List<GlAdminUnit> completeAdminUnitFrom(String query) {
        List<GlAdminUnit> glAdminUnits = glAdminUnitFromList;
        if (query == null || query.trim().equals("")) {
            adminUnitConverterFrom = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < glAdminUnitFromList.size(); i++) {
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
        adminUnitConverterFrom = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
    }

    public List<GlAdminUnit> completeAdminUnitTo(String query) {
        List<GlAdminUnit> glAdminUnits = glAdminUnitToList;
        if (query == null || query.trim().equals("")) {
            adminUnitConverterTo = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < glAdminUnitToList.size(); i++) {
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
        adminUnitConverterTo = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = getGlAccountList();
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

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("yearFromValue", getComplexRevisionBalanceSearchBeanA().getPeriodFrom());
        hashMap.put("yearToValue", getComplexRevisionBalanceSearchBeanA().getPeriodTo());
        hashMap.put("adminUnitFromText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("adminUnitToText", userDDs.get("ADMIN_UNIT_TO"));
        hashMap.put("adminUnitText", userDDs.get("ADMIN_UNIT"));
        hashMap.put("adminUnitFromCode", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getCode() : null);
        hashMap.put("adminUnitFromName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
        hashMap.put("adminUnitToCode", glAdminUnitToSelected != null ? glAdminUnitToSelected.getCode() : null);
        hashMap.put("adminUnitToName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_COD"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("totalText", userDDs.get("TOTAL"));
        hashMap.put("totalDebitValue", super.getTotalDebit());
        hashMap.put("totalCreditValue", super.getTotalCredit());
        hashMap.put("totalBalanceValue", super.getTotalFirstBalance());
        
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("reportTitleText", userDDs.get("ADMIN_UNIT_REPORT"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (incomeMenuBeanCombinedViewList != null && !incomeMenuBeanCombinedViewList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "adminUnitBalancesReport.jasper", incomeMenuBeanCombinedViewList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    /**
     * @return the stringBuilder
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * @param stringBuilder the stringBuilder to set
     */
    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * @return the adminUnitConverterFrom
     */
    public GlAdminUnitConverter getAdminUnitConverterFrom() {
        return adminUnitConverterFrom;
    }

    /**
     * @param adminUnitConverterFrom the adminUnitConverterFrom to set
     */
    public void setAdminUnitConverterFrom(GlAdminUnitConverter adminUnitConverterFrom) {
        this.adminUnitConverterFrom = adminUnitConverterFrom;
    }

    /**
     * @return the adminUnitConverterTo
     */
    public GlAdminUnitConverter getAdminUnitConverterTo() {
        return adminUnitConverterTo;
    }

    /**
     * @param adminUnitConverterTo the adminUnitConverterTo to set
     */
    public void setAdminUnitConverterTo(GlAdminUnitConverter adminUnitConverterTo) {
        this.adminUnitConverterTo = adminUnitConverterTo;
    }

    /**
     * @return the glAdminUnitToSelected
     */
    public GlAdminUnit getGlAdminUnitToSelected() {
        return glAdminUnitToSelected;
    }

    /**
     * @param glAdminUnitToSelected the glAdminUnitToSelected to set
     */
    public void setGlAdminUnitToSelected(GlAdminUnit glAdminUnitToSelected) {
        this.glAdminUnitToSelected = glAdminUnitToSelected;
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

    /**
     * @return the glAdminUnitToList
     */
    public List<GlAdminUnit> getGlAdminUnitToList() {
        return glAdminUnitToList;
    }

    /**
     * @param glAdminUnitToList the glAdminUnitToList to set
     */
    public void setGlAdminUnitToList(List<GlAdminUnit> glAdminUnitToList) {
        this.glAdminUnitToList = glAdminUnitToList;
    }

    /**
     * @return the glAdminUnitFromList
     */
    public List<GlAdminUnit> getGlAdminUnitFromList() {
        return glAdminUnitFromList;
    }

    /**
     * @param glAdminUnitFromList the glAdminUnitFromList to set
     */
    public void setGlAdminUnitFromList(List<GlAdminUnit> glAdminUnitFromList) {
        this.glAdminUnitFromList = glAdminUnitFromList;
    }

    /**
     * @return the glAdminUnitMap
     */
    public Map<Integer, GlAdminUnit> getGlAdminUnitMap() {
        return glAdminUnitMap;
    }

    /**
     * @param glAdminUnitMap the glAdminUnitMap to set
     */
    public void setGlAdminUnitMap(Map<Integer, GlAdminUnit> glAdminUnitMap) {
        this.glAdminUnitMap = glAdminUnitMap;
    }

    /**
     * @return the glAdminUnitTreeMap
     */
    public TreeMap<Integer, GlAdminUnit> getGlAdminUnitTreeMap() {
        return glAdminUnitTreeMap;
    }

    /**
     * @param glAdminUnitTreeMap the glAdminUnitTreeMap to set
     */
    public void setGlAdminUnitTreeMap(TreeMap<Integer, GlAdminUnit> glAdminUnitTreeMap) {
        this.glAdminUnitTreeMap = glAdminUnitTreeMap;
    }

    /**
     * @return the incomeMenuBeanCombinedViewList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanCombinedViewList() {
        return incomeMenuBeanCombinedViewList;
    }

    /**
     * @param incomeMenuBeanCombinedViewList the incomeMenuBeanCombinedViewList
     * to set
     */
    public void setIncomeMenuBeanCombinedViewList(List<IncomeMenuBean> incomeMenuBeanCombinedViewList) {
        this.incomeMenuBeanCombinedViewList = incomeMenuBeanCombinedViewList;
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
     * @return the adminUnitList
     */
    public List<GlAdminUnit> getAdminUnitList() {
        return adminUnitList;
    }

    /**
     * @param adminUnitList the adminUnitList to set
     */
    public void setAdminUnitList(List<GlAdminUnit> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }

    /**
     * @return the adminUnitRootOrderedList
     */
    public List<GlAdminUnit> getAdminUnitRootOrderedList() {
        return adminUnitRootOrderedList;
    }

    /**
     * @param adminUnitRootOrderedList the adminUnitRootOrderedList to set
     */
    public void setAdminUnitRootOrderedList(List<GlAdminUnit> adminUnitRootOrderedList) {
        this.adminUnitRootOrderedList = adminUnitRootOrderedList;
    }
}
