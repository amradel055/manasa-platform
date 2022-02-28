/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.CostCenterService;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
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
@Named(value = "costCenterBalancesReportMB")
@ViewScoped
public class CostCenterBalancesReportMB extends ComplexRevisionBalanceMB {

    private List<CostCenter> costCenterList;
    private StringBuilder stringBuilder;
    private CostCenterConverter costCenterConverterFrom;
    private CostCenterConverter costCenterConverterTo;
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;
    private List<CostCenter> CostCenterToList;
    private List<CostCenter> CostCenterFromList;
    private Map<Integer, CostCenter> costCenterMap = new HashMap<>();
    private TreeMap<Integer, CostCenter> costCenterTreeMap = new TreeMap<>();
    private List<CostCenter> costCenterRootOrderedList;
    private List<IncomeMenuBean> incomeMenuBeanCombinedViewList;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private GlaccountConverter accountConverter;
    @EJB
    private CostCenterService costCenterService;

    @PostConstruct
    public void init() {
        resetSome();
        load();
        loadAllCostCenterFrom();
        costCenterFromSelected = null;
        costCenterToSelected = null;
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;
        accountConverter = new GlaccountConverter(getGlAccountList());

    }

    public void fillStringBuilder() {
        stringBuilder = new StringBuilder();
        if (costCenterList != null && !costCenterList.isEmpty()) {
            for (CostCenter cc : costCenterList) {
                if (stringBuilder != null && stringBuilder.length() == 0) {
                    stringBuilder.append(cc.getId().toString());
                } else {
                    stringBuilder.append(",").append(cc.getId().toString());
                }
            }
            super.setStringBuilderParent(new StringBuilder());
            super.setStringBuilderParent(stringBuilder);
        }
    }

    public void resetSome() {
        reset();
        costCenterList = new ArrayList<>();
        CostCenterFromList = new ArrayList<>();
        loadAllCostCenterFrom();
        CostCenterToList = new ArrayList<>();
        costCenterRootOrderedList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        costCenterConverterFrom = new CostCenterConverter(getCostCenterList());
        costCenterMap = new HashMap<>();
        costCenterTreeMap = new TreeMap<>();
        incomeMenuBeanCombinedViewList = new ArrayList<>();
        costCenterToSelected = null;
        costCenterFromSelected = null;
        glAccountSelectedFrom = null;
        glAccountSelectedTo = null;
        super.setTotalDebit(BigDecimal.ZERO);
        super.setTotalCredit(BigDecimal.ZERO);
        super.setTotalFirstBalance(BigDecimal.ZERO);
    }

    public void intializeData() {
        costCenterMap = new HashMap<>();
        costCenterTreeMap = new TreeMap<>();
        incomeMenuBeanCombinedViewList = new ArrayList<>();
        costCenterRootOrderedList = new ArrayList<>();
        getComplexRevisionBalanceSearchBeanA().setAccountFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        getComplexRevisionBalanceSearchBeanA().setAccountTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        super.setTotalDebit(BigDecimal.ZERO);
        super.setTotalCredit(BigDecimal.ZERO);
        super.setTotalFirstBalance(BigDecimal.ZERO);
    }

    public void loadAllCostCenterFrom() {
        CostCenterFromList = costCenterService.getAllCostCenters(getUserData().getUserBranch().getId());
    }

    public void startSearch() {
        intializeData();
        if (costCenterFromSelected != null && costCenterToSelected != null) {
            for (CostCenter cc : CostCenterToList) {
                if (cc.getCode() >= costCenterFromSelected.getCode() && cc.getCode() <= costCenterToSelected.getCode()) {
                    costCenterTreeMap.put(cc.getCode(), cc);
                }
            }
            costCenterRootOrderedList = new ArrayList<>(costCenterTreeMap.values());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار من مركز تكلفة الى مركز تكلفة"));
        }
        costCenterList = new ArrayList<>();
        if (costCenterRootOrderedList != null && !costCenterRootOrderedList.isEmpty()) {
            for (CostCenter center : costCenterRootOrderedList) {
                costCenterList = costCenterService.getAllSubCostCenterByBranchIdActiveAndCostCenterId(center.getId());
                fillStringBuilder();
                super.search();
                for (IncomeMenuBean imb : super.getIncomeMenuBeanViewList()) {
                    if (imb.getDebit().compareTo(BigDecimal.ZERO) != 0 || imb.getCredit().compareTo(BigDecimal.ZERO) != 0
                            || imb.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                        imb.setAccountGroup(center.getName());
                        imb.setParent(center.getCode());
                        imb.setCreditBalance(super.getTotalCredit());
                        imb.setDebitBalance(super.getTotalDebit());
                        imb.setFirstDurationBalance(super.getTotalFirstBalance());
                        incomeMenuBeanCombinedViewList.add(imb);
                    }
                }
            }
        }
    }

    public List<CostCenter> completeCostCenterFrom(String query) {
        List<CostCenter> costCenterList = CostCenterFromList;
        if (query == null || query.trim().equals("")) {
            costCenterConverterFrom = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < CostCenterFromList.size(); i++) {
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

        costCenterConverterFrom = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<CostCenter> completeCostCenterTo(String query) {
        List<CostCenter> costCenterList = CostCenterToList;
        if (query == null || query.trim().equals("")) {
            costCenterConverterTo = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < CostCenterToList.size(); i++) {
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

        costCenterConverterTo = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public void updateCostCenterTo() {
        CostCenterToList = new ArrayList<>();
        if (costCenterFromSelected != null && costCenterFromSelected.getId() != null) {
            CostCenterToList = costCenterService.getAllSubCostCenterByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), costCenterFromSelected.getLevel());
            costCenterConverterTo = new CostCenterConverter(CostCenterToList);
        }
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
        hashMap.put("costCenterFromText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("costCenterToText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("costCenterText", userDDs.get("SINGLE_COST_CENTER"));
        hashMap.put("costCenterFromCode", costCenterFromSelected != null ? costCenterFromSelected.getCode() : null);
        hashMap.put("costCenterFromName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
        hashMap.put("costCenterToCode", costCenterToSelected != null ? costCenterToSelected.getCode() : null);
        hashMap.put("costCenterToName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
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
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("reportTitleText", userDDs.get("COST_CENTER_BALANC"));
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
            fillReport(prepareReport(), getUserData().getReportPath() + "costCenterBalancesReport.jasper", incomeMenuBeanCombinedViewList, "pdf");
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
     * @return the costCenterToSelected
     */
    public CostCenter getCostCenterToSelected() {
        return costCenterToSelected;
    }

    /**
     * @param costCenterToSelected the costCenterToSelected to set
     */
    public void setCostCenterToSelected(CostCenter costCenterToSelected) {
        this.costCenterToSelected = costCenterToSelected;
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
     * @return the CostCenterToList
     */
    public List<CostCenter> getCostCenterToList() {
        return CostCenterToList;
    }

    /**
     * @param CostCenterToList the CostCenterToList to set
     */
    public void setCostCenterToList(List<CostCenter> CostCenterToList) {
        this.CostCenterToList = CostCenterToList;
    }

    /**
     * @return the costCenterConverterFrom
     */
    public CostCenterConverter getCostCenterConverterFrom() {
        return costCenterConverterFrom;
    }

    /**
     * @param costCenterConverterFrom the costCenterConverterFrom to set
     */
    public void setCostCenterConverterFrom(CostCenterConverter costCenterConverterFrom) {
        this.costCenterConverterFrom = costCenterConverterFrom;
    }

    /**
     * @return the costCenterConverterTo
     */
    public CostCenterConverter getCostCenterConverterTo() {
        return costCenterConverterTo;
    }

    /**
     * @param costCenterConverterTo the costCenterConverterTo to set
     */
    public void setCostCenterConverterTo(CostCenterConverter costCenterConverterTo) {
        this.costCenterConverterTo = costCenterConverterTo;
    }

    /**
     * @return the CostCenterFromList
     */
    public List<CostCenter> getCostCenterFromList() {
        return CostCenterFromList;
    }

    /**
     * @param CostCenterFromList the CostCenterFromList to set
     */
    public void setCostCenterFromList(List<CostCenter> CostCenterFromList) {
        this.CostCenterFromList = CostCenterFromList;
    }

    /**
     * @return the costCenterMap
     */
    public Map<Integer, CostCenter> getCostCenterMap() {
        return costCenterMap;
    }

    /**
     * @param costCenterMap the costCenterMap to set
     */
    public void setCostCenterMap(Map<Integer, CostCenter> costCenterMap) {
        this.costCenterMap = costCenterMap;
    }

    /**
     * @return the costCenterTreeMap
     */
    public TreeMap<Integer, CostCenter> getCostCenterTreeMap() {
        return costCenterTreeMap;
    }

    /**
     * @param costCenterTreeMap the costCenterTreeMap to set
     */
    public void setCostCenterTreeMap(TreeMap<Integer, CostCenter> costCenterTreeMap) {
        this.costCenterTreeMap = costCenterTreeMap;
    }

    /**
     * @return the costCenterRootOrderedList
     */
    public List<CostCenter> getCostCenterRootOrderedList() {
        return costCenterRootOrderedList;
    }

    /**
     * @param costCenterRootOrderedList the costCenterRootOrderedList to set
     */
    public void setCostCenterRootOrderedList(List<CostCenter> costCenterRootOrderedList) {
        this.costCenterRootOrderedList = costCenterRootOrderedList;
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

}
