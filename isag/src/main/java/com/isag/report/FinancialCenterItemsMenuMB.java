/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GeneralBudgetGuideService;
import com.isag.businessservice.GlAccountService;
import com.isag.entity.GeneralBudgetGuide;
import com.isag.entity.GlAccount;
import com.isag.entiy.GlAccountEntity;
import com.isag.report.entity.IncomeMenuBean;
import com.isag.report.entity.MenuItemsViewBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
@Named(value = "financialCenterItemsMenuMB")
@ViewScoped
public class FinancialCenterItemsMenuMB extends ComplexRevisionBalanceMB {

    private ExternalContext context;
    private List<GeneralBudgetGuide> generalBudgetGuideList;
    private List<GlAccount> glAccountPrepareList;
    private List<GlAccountEntity> glAccountEntityArrangedList;
    private List<IncomeMenuBean> incomeMenuBeanArrangedList;
    private List<GlAccountEntity> glAccountEntityMirrorList;
    private StringBuilder stringBuilder;

    private Integer count;
    private BigDecimal totalOfEveryIndividualGroup;
    private BigDecimal totalOfWholeGroup;
    private Integer firstBeanReference;
    Integer totalOfAsset;
    Integer totalOfDeduction;
    private String lastName;
    private String firstTotalName;
    private String secondTotalName;
    private BigDecimal firstTotalValue;
    private BigDecimal secondTotalValue;
    private Integer assetIndex;
    private Integer deductionIndex;
    private List<MenuItemsViewBean> menuItemsViewBeanList;
    private Map<Integer, MenuItemsViewBean> menuItemsViewMap;
    private Map<String, BigDecimal> totalBalanceMenuItemsMap;
    private String localUri;

    @EJB
    private GeneralBudgetGuideService budgetGuideService;
    @EJB
    private GlAccountService accountService;

    @PostConstruct
    public void init2() {
        super.init();
        resetAllFields();
        super.setZeroAmount(true);
        localUri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
    }

    public void fillStringBuilder() {
        stringBuilder = new StringBuilder();
        if (generalBudgetGuideList != null && !generalBudgetGuideList.isEmpty()) {
            for (GeneralBudgetGuide gbg : generalBudgetGuideList) {
                if (stringBuilder != null && stringBuilder.length() == 0) {
                    stringBuilder.append(gbg.getId().toString());
                } else {
                    stringBuilder.append(",").append(gbg.getId().toString());
                }
            }
        }
    }

    public void resetAllFields() {
        generalBudgetGuideList = new ArrayList<>();
        glAccountPrepareList = new ArrayList<>();
        menuItemsViewBeanList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        menuItemsViewMap = new HashMap<>();
        totalBalanceMenuItemsMap = new HashMap<>();
    }

    public void findAccountGroups() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (localUri.contains("financialCenter")) {
            generalBudgetGuideList = budgetGuideService.getAllGeneralBudgetGuideByForFinancialItemMenuBranchId(getUserData().getUserBranch().getId());
        } else {
            generalBudgetGuideList = budgetGuideService.getAllGeneralBudgetGuideByForIncomeItemMenuBranchId(getUserData().getUserBranch().getId());
        }
        if (generalBudgetGuideList != null && !generalBudgetGuideList.isEmpty()) {
            for (GeneralBudgetGuide gbg : generalBudgetGuideList) {
                MenuItemsViewBean itemsViewBean = new MenuItemsViewBean();
                itemsViewBean.setItem(gbg.getNameAr());
                itemsViewBean.setId(gbg.getId());
                itemsViewBean.setAccountGroup(userDDs.get(gbg.getAccGroup().name()));
                menuItemsViewMap.put(gbg.getId(), itemsViewBean);
                totalBalanceMenuItemsMap.put(userDDs.get(gbg.getAccGroup().name()), BigDecimal.ZERO);
            }
        }
        fillStringBuilder();
        glAccountPrepareList = accountService.findAllGLAccountsWhichHaveGeneralBudgetId(getUserData().getUserBranch().getId(), stringBuilder);
    }

    public void searchData() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        resetAllFields();
        findAccountGroups();
        if (glAccountPrepareList != null && !glAccountPrepareList.isEmpty()) {
            setGlAccountFromMenuItemList(new ArrayList<>());
            setGlAccountFromMenuItemList(glAccountPrepareList);
        }
        super.search();
        getIncomeMenuBeanViewList();
        if (getIncomeMenuBeanViewList() != null && !getIncomeMenuBeanViewList().isEmpty()) {
            for (IncomeMenuBean bean : getIncomeMenuBeanViewList()) {
                if (bean.getGeneralBudgetGuideCreditId() != null && bean.getGeneralBudgetGuideDebitId() != null && bean.getGeneralBudgetGuideDebitId().compareTo(bean.getGeneralBudgetGuideCreditId()) == 0) {
                    if (menuItemsViewMap.containsKey(bean.getGeneralBudgetGuideDebitId()) || menuItemsViewMap.containsKey(bean.getGeneralBudgetGuideCreditId())) {
                        MenuItemsViewBean itemsViewBean = menuItemsViewMap.get(bean.getGeneralBudgetGuideDebitId() != null ? bean.getGeneralBudgetGuideDebitId() : bean.getGeneralBudgetGuideCreditId());
                        itemsViewBean.setBalance(itemsViewBean.getBalance().add(bean.getCreditBalance() != BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP) ? bean.getCreditBalance() : bean.getDebitBalance()));
                    }
                } else {
                    MenuItemsViewBean itemsViewBean = new MenuItemsViewBean();
                    if (menuItemsViewMap.containsKey(bean.getGeneralBudgetGuideDebitId())) {
                        itemsViewBean = menuItemsViewMap.get(bean.getGeneralBudgetGuideDebitId());
                        itemsViewBean.setBalance(itemsViewBean.getBalance().add(bean.getDebitBalance()));
                    }
                    if (menuItemsViewMap.containsKey(bean.getGeneralBudgetGuideCreditId())) {
                        itemsViewBean = menuItemsViewMap.get(bean.getGeneralBudgetGuideCreditId());
                        itemsViewBean.setBalance(itemsViewBean.getBalance().add(bean.getCreditBalance()));
                    }
                }
            }
        }
        menuItemsViewBeanList = new ArrayList<>(menuItemsViewMap.values());
        assetIndex = 0;
        deductionIndex = 0;
        int x = 0;
        for (MenuItemsViewBean ViewBean : menuItemsViewBeanList) {
            fillTotalBalanceForEachGroup(ViewBean);
        }
        calculateRatio();
        // putTotalBalanceGroupRows();
    }

    public void fillTotalBalanceForEachGroup(MenuItemsViewBean itemsViewBean) {
        if (totalBalanceMenuItemsMap.containsKey(itemsViewBean.getAccountGroup())) {
            BigDecimal totalBalance = totalBalanceMenuItemsMap.get(itemsViewBean.getAccountGroup());
            totalBalance = totalBalance.add(itemsViewBean.getBalance() != null ? itemsViewBean.getBalance() : BigDecimal.ZERO);
            totalBalanceMenuItemsMap.put(itemsViewBean.getAccountGroup(), totalBalance);
        }
    }

    public void calculateRatio() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        Iterator it = menuItemsViewBeanList.iterator();
        List<MenuItemsViewBean> menuItemsViewIterationList = new ArrayList<>();
        BigDecimal percent = BigDecimal.valueOf(100);
        firstTotalValue = BigDecimal.ZERO;
        secondTotalValue = BigDecimal.ZERO;
        while (it.hasNext()) {
            MenuItemsViewBean mivb = new MenuItemsViewBean();
            mivb = (MenuItemsViewBean) it.next();

            BigDecimal totalBalance = totalBalanceMenuItemsMap.get(mivb.getAccountGroup());
            if (totalBalance.compareTo(BigDecimal.ZERO) != 0) {
                mivb.setRatio(mivb.getBalance().abs().divide(totalBalance.abs(), 4, 4).multiply(percent).setScale(2, BigDecimal.ROUND_UP));
                mivb.setTotalGroupBalance(totalBalance);
            }
            if (mivb.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                menuItemsViewIterationList.add(mivb);
            }
            if (localUri.contains("financialCenter")) {
                if (mivb.getAccountGroup().contains(userDDs.get("FIXED_ASSETS")) || mivb.getAccountGroup().contains(userDDs.get("CIRCULATED_ASSETS"))) {
                    firstTotalName = "اجمالي  " + userDDs.get("ASSETS");
                    firstTotalValue = firstTotalValue.add(mivb.getBalance());
                    mivb.setHigherAccountGroup(userDDs.get("ASSETS"));
                } else {
                    secondTotalName = "اجمالي  " + userDDs.get("DEDUCTION");
                    secondTotalValue = secondTotalValue.add(mivb.getBalance());
                    mivb.setHigherAccountGroup(userDDs.get("DEDUCTION"));
                }
            } else {
                if (mivb.getAccountGroup().contains(userDDs.get("INCOME"))) {
                    firstTotalName = "اجمالي  " + userDDs.get("INCOME");
                    firstTotalValue = firstTotalValue.add(mivb.getBalance());
                } else {
                    secondTotalName = "اجمالي  " + userDDs.get("EXPENSES");
                    secondTotalValue = secondTotalValue.add(mivb.getBalance());
                }
            }
        }

        menuItemsViewBeanList = new ArrayList<>(menuItemsViewIterationList);
        /* if (menuItemsViewBeanList != null && !menuItemsViewBeanList.isEmpty()) {
            for (MenuItemsViewBean mivb : menuItemsViewBeanList) {
                BigDecimal totalBalance = totalBalanceMenuItemsMap.get(mivb.getAccountGroup());
                if (totalBalance.compareTo(BigDecimal.ZERO) != 0) {
                    mivb.setRatio(mivb.getBalance().abs().divide(totalBalance.abs(), 4, 4).multiply(percent).setScale(2, BigDecimal.ROUND_UP));
                    mivb.setTotalGroupBalance(totalBalance);
                }
            }
        }*/
    }

    public void putTotalBalanceGroupRows() {
        MenuItemsViewBean mivb = new MenuItemsViewBean();
        mivb.setItem(firstTotalName);
        mivb.setBalance(firstTotalValue);
        menuItemsViewBeanList.add(assetIndex != 0 ? assetIndex : menuItemsViewBeanList.size() - 1, mivb);
        mivb = null;

        mivb = new MenuItemsViewBean();
        mivb.setItem(secondTotalName);
        mivb.setBalance(secondTotalValue);
        menuItemsViewBeanList.add(deductionIndex != 0 ? deductionIndex : menuItemsViewBeanList.size() - 1, mivb);
        mivb = null;
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        

        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_COD"));
        hashMap.put("itemNameText", userDDs.get("IT_NAME"));
        hashMap.put("itemCodeText", userDDs.get("ITEM_CODE"));
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("ratioText", userDDs.get("PERCENTAGE"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("totalText", userDDs.get("TOTAL"));
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("totalProfitOrLossText", lastName);
        hashMap.put("menuNameText", userDDs.get("FINANCIAL_CENTER_ITEMS"));
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("yearFromValue", getComplexRevisionBalanceSearchBeanA().getPeriodFrom());
        hashMap.put("yearToValue", getComplexRevisionBalanceSearchBeanA().getPeriodTo());
        hashMap.put("firstTotalName", firstTotalName);
        hashMap.put("firstTotalValue", firstTotalValue);
        hashMap.put("secondTotalName", secondTotalName);
        hashMap.put("secondTotalValue", secondTotalValue);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (menuItemsViewBeanList != null && !menuItemsViewBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "financialItemsMenuReport.jasper", menuItemsViewBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public void searching() {
        super.init();
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the totalOfEveryIndividualGroup
     */
    public BigDecimal getTotalOfEveryIndividualGroup() {
        return totalOfEveryIndividualGroup;
    }

    /**
     * @param totalOfEveryIndividualGroup the totalOfEveryIndividualGroup to set
     */
    public void setTotalOfEveryIndividualGroup(BigDecimal totalOfEveryIndividualGroup) {
        this.totalOfEveryIndividualGroup = totalOfEveryIndividualGroup;
    }

    /**
     * @return the totalOfWholeGroup
     */
    public BigDecimal getTotalOfWholeGroup() {
        return totalOfWholeGroup;
    }

    /**
     * @param totalOfWholeGroup the totalOfWholeGroup to set
     */
    public void setTotalOfWholeGroup(BigDecimal totalOfWholeGroup) {
        this.totalOfWholeGroup = totalOfWholeGroup;
    }

    /**
     * @return the firstBeanReference
     */
    public Integer getFirstBeanReference() {
        return firstBeanReference;
    }

    /**
     * @param firstBeanReference the firstBeanReference to set
     */
    public void setFirstBeanReference(Integer firstBeanReference) {
        this.firstBeanReference = firstBeanReference;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the generalBudgetGuideList
     */
    public List<GeneralBudgetGuide> getGeneralBudgetGuideList() {
        return generalBudgetGuideList;
    }

    /**
     * @param generalBudgetGuideList the generalBudgetGuideList to set
     */
    public void setGeneralBudgetGuideList(List<GeneralBudgetGuide> generalBudgetGuideList) {
        this.generalBudgetGuideList = generalBudgetGuideList;
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
     * @return the menuItemsViewBeanList
     */
    public List<MenuItemsViewBean> getMenuItemsViewBeanList() {
        return menuItemsViewBeanList;
    }

    /**
     * @param menuItemsViewBeanList the menuItemsViewBeanList to set
     */
    public void setMenuItemsViewBeanList(List<MenuItemsViewBean> menuItemsViewBeanList) {
        this.menuItemsViewBeanList = menuItemsViewBeanList;
    }

    /**
     * @return the menuItemsViewMap
     */
    public Map<Integer, MenuItemsViewBean> getMenuItemsViewMap() {
        return menuItemsViewMap;
    }

    /**
     * @param menuItemsViewMap the menuItemsViewMap to set
     */
    public void setMenuItemsViewMap(Map<Integer, MenuItemsViewBean> menuItemsViewMap) {
        this.menuItemsViewMap = menuItemsViewMap;
    }

    /**
     * @return the totalBalanceMenuItemsMap
     */
    public Map<String, BigDecimal> getTotalBalanceMenuItemsMap() {
        return totalBalanceMenuItemsMap;
    }

    /**
     * @param totalBalanceMenuItemsMap the totalBalanceMenuItemsMap to set
     */
    public void setTotalBalanceMenuItemsMap(Map<String, BigDecimal> totalBalanceMenuItemsMap) {
        this.totalBalanceMenuItemsMap = totalBalanceMenuItemsMap;
    }

    /**
     * @return the firstTotalName
     */
    public String getFirstTotalName() {
        return firstTotalName;
    }

    /**
     * @param firstTotalName the firstTotalName to set
     */
    public void setFirstTotalName(String firstTotalName) {
        this.firstTotalName = firstTotalName;
    }

    /**
     * @return the secondTotalName
     */
    public String getSecondTotalName() {
        return secondTotalName;
    }

    /**
     * @param secondTotalName the secondTotalName to set
     */
    public void setSecondTotalName(String secondTotalName) {
        this.secondTotalName = secondTotalName;
    }

    /**
     * @return the firstTotalValue
     */
    public BigDecimal getFirstTotalValue() {
        return firstTotalValue;
    }

    /**
     * @param firstTotalValue the firstTotalValue to set
     */
    public void setFirstTotalValue(BigDecimal firstTotalValue) {
        this.firstTotalValue = firstTotalValue;
    }

    /**
     * @return the secondTotalValue
     */
    public BigDecimal getSecondTotalValue() {
        return secondTotalValue;
    }

    /**
     * @param secondTotalValue the secondTotalValue to set
     */
    public void setSecondTotalValue(BigDecimal secondTotalValue) {
        this.secondTotalValue = secondTotalValue;
    }

    /**
     * @return the assetIndex
     */
    public Integer getAssetIndex() {
        return assetIndex;
    }

    /**
     * @param assetIndex the assetIndex to set
     */
    public void setAssetIndex(Integer assetIndex) {
        this.assetIndex = assetIndex;
    }

    /**
     * @return the deductionIndex
     */
    public Integer getDeductionIndex() {
        return deductionIndex;
    }

    /**
     * @param deductionIndex the deductionIndex to set
     */
    public void setDeductionIndex(Integer deductionIndex) {
        this.deductionIndex = deductionIndex;
    }

    /**
     * @return the localUri
     */
    public String getLocalUri() {
        return localUri;
    }

    /**
     * @param localUri the localUri to set
     */
    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

}
