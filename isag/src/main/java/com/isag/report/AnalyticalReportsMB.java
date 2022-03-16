/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAnalyticsAccountsServcie;
import com.isag.converter.GlaccountConverter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAnalyticsAccounts;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author hq002
 */
public class AnalyticalReportsMB extends BaseListBean {

    private UserData userData;
    private GlAnalyticsAccounts glAnalyticsAccounts;
    private List<GlAnalyticsAccounts> rowsOfGlAnalytics = new ArrayList<>();
    private List<GlAnalyticsAccounts> glAnalyticsList;
    private List<GlAnalyticsAccounts> glAnalyticsToSaveList;
    private List<GlAccount> glAccountList;

    private GlAccount glAccountSlected;

    private PieChartModel pieCurrentRatio;
    private PieChartModel pieLiquidityRatio;
    private PieChartModel ownerEquityRatio;
    private PieChartModel salesRatioRatio;
    private PieChartModel circulationRatio;
    private PieChartModel circulationStoreRatio;

    private PieChartModel pieModel2;
    private BigDecimal resultRatio;
    private BigDecimal resultRatioOfLiquidityRatio;
    private BigDecimal resultRatioOfOwnerEquityRatio;
    private BigDecimal resultRatioOfSalesRatio;
    private BigDecimal resultRatioOfCirculationRatio;
    private BigDecimal resultRatioOfCirculationStoreRatio;

    private BigDecimal assetCurrentRatio = BigDecimal.valueOf(1169318.78);
    private BigDecimal deductionCurrentRatio = BigDecimal.valueOf(200000.69);

    private BigDecimal assetCurrentLastPeriodRatio = BigDecimal.valueOf(1031419.81);

    private BigDecimal netProfit = BigDecimal.valueOf(400000.12);
    private BigDecimal ownerEquity = BigDecimal.valueOf(1179855.93);

    private BigDecimal sales = BigDecimal.valueOf(200000.12);

    private BigDecimal totalOfAssets = BigDecimal.valueOf(800000.74);

    private BigDecimal costOfSoldGoods = BigDecimal.valueOf(800000.12);
    private BigDecimal storageOfLastPeriod = BigDecimal.valueOf(1550000.97);

    private GlaccountConverter glaccountConverter;

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    GlAccountService accountService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        glAccountSlected = new GlAccount();
        glAnalyticsAccounts = new GlAnalyticsAccounts();
        reset();
        load();
    }

    public void reset() {
        createPieModel1();
        createPieModelOfLiquidityRatio();
        createPieModelOfOwnerEquityRatio();
        createPieModelOfSalesRatio();
        createPieModelOfCirculationRatio();
        createPieModelOfCirculationStoreRatio();
    }


    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = glAccountList;
        if (query == null || query.trim().equals("")) {
            glaccountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < glAccountList.size(); i++) {
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

        glaccountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        return "";
    }

    public void saveAll() {

        if (glAnalyticsToSaveList != null && !glAnalyticsToSaveList.isEmpty()) {
            glAnalyticsAccountsServcie.saveGlAnalyticsAccounts(glAnalyticsToSaveList);
        }
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        glAnalyticsList = new ArrayList<>();
        glAnalyticsToSaveList = new ArrayList<>();
        glAnalyticsList = glAnalyticsAccountsServcie.getALLGlAnalyticsAccountsByBranch(userData.getUserBranch().getId());
        setGlAccountList(accountService.getBranchGLAccountsOnlyActiveByBranch(userData.getUserBranch().getId()));
        setGlaccountConverter(new GlaccountConverter(glAccountList));

    }

    public void saveOneByOne(GlAnalyticsAccounts analyticsAccounts) {

        System.out.println("glAccountSlected: " + analyticsAccounts);
        glAnalyticsToSaveList.add(glAnalyticsAccounts);
    }

    public void selectAccountNumber(SelectEvent event) {
        try {
            glAccountSlected = (GlAccount) event.getObject();
            GlAnalyticsAccounts analyticsAccounts = (GlAnalyticsAccounts) event.getComponent().getAttributes().get("varValue");
            analyticsAccounts.setAccountId(glAccountSlected);
            glAnalyticsToSaveList.add(analyticsAccounts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the pieCurrentRatio
     */
    public PieChartModel getPieCurrentRatio() {
        return pieCurrentRatio;
    }

    /**
     * @param pieCurrentRatio the pieCurrentRatio to set
     */
    public void setPieCurrentRatio(PieChartModel pieCurrentRatio) {
        this.pieCurrentRatio = pieCurrentRatio;
    }

    private void createPieModel1() {
        pieCurrentRatio = new PieChartModel();

        resultRatio = assetCurrentRatio.divide(deductionCurrentRatio, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        pieCurrentRatio.set("الاصول المتداولة", assetCurrentRatio);
        pieCurrentRatio.set("الخصوم المتداولة", deductionCurrentRatio);
        pieCurrentRatio.setShowDatatip(true);
        pieCurrentRatio.setTitle("نسبة التداول");
        pieCurrentRatio.setLegendPosition("n");
        pieCurrentRatio.setLegendCols(2);
        pieCurrentRatio.setLegendPlacement(LegendPlacement.INSIDE);
        pieCurrentRatio.setShowDataLabels(true);
        pieCurrentRatio.setShadow(true);
        pieCurrentRatio.setDataFormat(null);
    }

    private void createPieModelOfLiquidityRatio() {
        pieLiquidityRatio = new PieChartModel();

        resultRatioOfLiquidityRatio = assetCurrentLastPeriodRatio.divide(deductionCurrentRatio, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        pieLiquidityRatio.set("الاصول المتداولة مخزون اخر مدة", assetCurrentRatio);
        pieLiquidityRatio.set("الخصوم المتداولة", deductionCurrentRatio);

        pieLiquidityRatio.setTitle("نسبة السيولة");
        pieLiquidityRatio.setLegendPosition("s");
        pieLiquidityRatio.setLegendCols(1);
        pieLiquidityRatio.setLegendPlacement(LegendPlacement.INSIDE);
        pieLiquidityRatio.setShowDataLabels(true);
        pieLiquidityRatio.setShadow(true);
        pieLiquidityRatio.setDataFormat(null);
    }

    private void createPieModelOfOwnerEquityRatio() {
        ownerEquityRatio = new PieChartModel();

        resultRatioOfOwnerEquityRatio = netProfit.divide(ownerEquity, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        ownerEquityRatio.set("صافي الربح", assetCurrentRatio);
        ownerEquityRatio.set("حقوق الملكية", deductionCurrentRatio);

        ownerEquityRatio.setTitle("نسبة صافي الربح الى حقوق الملكية");
        ownerEquityRatio.setLegendPosition("s");
        ownerEquityRatio.setLegendCols(1);
        ownerEquityRatio.setLegendPlacement(LegendPlacement.INSIDE);
        ownerEquityRatio.setShowDataLabels(true);
        ownerEquityRatio.setShadow(true);
        ownerEquityRatio.setDataFormat(null);
    }

    private void createPieModelOfSalesRatio() {
        salesRatioRatio = new PieChartModel();

        resultRatioOfSalesRatio = netProfit.divide(sales, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        salesRatioRatio.set("صافي الربح", assetCurrentRatio);
        salesRatioRatio.set("المبيعات", deductionCurrentRatio);

        salesRatioRatio.setTitle("نسبة صافي الربح الى المبيعات");
        salesRatioRatio.setLegendPosition("s");
        salesRatioRatio.setLegendCols(1);
        salesRatioRatio.setLegendPlacement(LegendPlacement.INSIDE);
        salesRatioRatio.setShowDataLabels(true);
        salesRatioRatio.setShadow(true);
        salesRatioRatio.setDataFormat(null);
    }

    private void createPieModelOfCirculationRatio() {
        circulationRatio = new PieChartModel();

        resultRatioOfCirculationRatio = sales.divide(totalOfAssets, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        circulationRatio.set("صافي المبيعات", assetCurrentRatio);
        circulationRatio.set("اجمالي الاصول", deductionCurrentRatio);

        circulationRatio.setTitle("معدل دوران مجموع الاصول");
        circulationRatio.setLegendPosition("s");
        circulationRatio.setLegendCols(1);
        circulationRatio.setLegendPlacement(LegendPlacement.INSIDE);
        circulationRatio.setShowDataLabels(true);
        circulationRatio.setShadow(true);
        circulationRatio.setDataFormat(null);
    }

    private void createPieModelOfCirculationStoreRatio() {
        circulationStoreRatio = new PieChartModel();

        resultRatioOfCirculationStoreRatio = costOfSoldGoods.divide(storageOfLastPeriod, 4, 4).setScale(2, BigDecimal.ROUND_UP);
        circulationStoreRatio.set("تكلفة البضاعة المباعة", assetCurrentRatio);
        circulationStoreRatio.set("مخزون اخر مدة", deductionCurrentRatio);

        circulationStoreRatio.setTitle("معدل دوران المخزون");
        circulationStoreRatio.setLegendPosition("s");
        circulationStoreRatio.setLegendCols(1);
        circulationStoreRatio.setLegendPlacement(LegendPlacement.INSIDE);
        circulationStoreRatio.setShowDataLabels(true);
        circulationStoreRatio.setShadow(true);
        circulationStoreRatio.setDataFormat(null);
    }

    /**
     * @return the pieModel2
     */
    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    /**
     * @param pieModel2 the pieModel2 to set
     */
    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    /**
     * @return the resultRatio
     */
    public BigDecimal getResultRatio() {
        return resultRatio;
    }

    /**
     * @param resultRatio the resultRatio to set
     */
    public void setResultRatio(BigDecimal resultRatio) {
        this.resultRatio = resultRatio;
    }

    /**
     * @return the pieLiquidityRatio
     */
    public PieChartModel getPieLiquidityRatio() {
        return pieLiquidityRatio;
    }

    /**
     * @param pieLiquidityRatio the pieLiquidityRatio to set
     */
    public void setPieLiquidityRatio(PieChartModel pieLiquidityRatio) {
        this.pieLiquidityRatio = pieLiquidityRatio;
    }

    /**
     * @return the resultRatioOfLiquidityRatio
     */
    public BigDecimal getResultRatioOfLiquidityRatio() {
        return resultRatioOfLiquidityRatio;
    }

    /**
     * @param resultRatioOfLiquidityRatio the resultRatioOfLiquidityRatio to set
     */
    public void setResultRatioOfLiquidityRatio(BigDecimal resultRatioOfLiquidityRatio) {
        this.resultRatioOfLiquidityRatio = resultRatioOfLiquidityRatio;
    }

    /**
     * @return the ownerEquityRatio
     */
    public PieChartModel getOwnerEquityRatio() {
        return ownerEquityRatio;
    }

    /**
     * @param ownerEquityRatio the ownerEquityRatio to set
     */
    public void setOwnerEquityRatio(PieChartModel ownerEquityRatio) {
        this.ownerEquityRatio = ownerEquityRatio;
    }

    /**
     * @return the resultRatioOfOwnerEquityRatio
     */
    public BigDecimal getResultRatioOfOwnerEquityRatio() {
        return resultRatioOfOwnerEquityRatio;
    }

    /**
     * @param resultRatioOfOwnerEquityRatio the resultRatioOfOwnerEquityRatio to
     * set
     */
    public void setResultRatioOfOwnerEquityRatio(BigDecimal resultRatioOfOwnerEquityRatio) {
        this.resultRatioOfOwnerEquityRatio = resultRatioOfOwnerEquityRatio;
    }

    /**
     * @return the resultRatioOfSalesRatio
     */
    public BigDecimal getResultRatioOfSalesRatio() {
        return resultRatioOfSalesRatio;
    }

    /**
     * @param resultRatioOfSalesRatio the resultRatioOfSalesRatio to set
     */
    public void setResultRatioOfSalesRatio(BigDecimal resultRatioOfSalesRatio) {
        this.resultRatioOfSalesRatio = resultRatioOfSalesRatio;
    }

    /**
     * @return the salesRatioRatio
     */
    public PieChartModel getSalesRatioRatio() {
        return salesRatioRatio;
    }

    /**
     * @param salesRatioRatio the salesRatioRatio to set
     */
    public void setSalesRatioRatio(PieChartModel salesRatioRatio) {
        this.salesRatioRatio = salesRatioRatio;
    }

    /**
     * @return the resultRatioOfCirculationRatio
     */
    public BigDecimal getResultRatioOfCirculationRatio() {
        return resultRatioOfCirculationRatio;
    }

    /**
     * @param resultRatioOfCirculationRatio the resultRatioOfCirculationRatio to
     * set
     */
    public void setResultRatioOfCirculationRatio(BigDecimal resultRatioOfCirculationRatio) {
        this.resultRatioOfCirculationRatio = resultRatioOfCirculationRatio;
    }

    /**
     * @return the circulationRatio
     */
    public PieChartModel getCirculationRatio() {
        return circulationRatio;
    }

    /**
     * @param circulationRatio the circulationRatio to set
     */
    public void setCirculationRatio(PieChartModel circulationRatio) {
        this.circulationRatio = circulationRatio;
    }

    /**
     * @return the circulationStoreRatio
     */
    public PieChartModel getCirculationStoreRatio() {
        return circulationStoreRatio;
    }

    /**
     * @param circulationStoreRatio the circulationStoreRatio to set
     */
    public void setCirculationStoreRatio(PieChartModel circulationStoreRatio) {
        this.circulationStoreRatio = circulationStoreRatio;
    }

    /**
     * @return the resultRatioOfCirculationStoreRatio
     */
    public BigDecimal getResultRatioOfCirculationStoreRatio() {
        return resultRatioOfCirculationStoreRatio;
    }

    /**
     * @param resultRatioOfCirculationStoreRatio the
     * resultRatioOfCirculationStoreRatio to set
     */
    public void setResultRatioOfCirculationStoreRatio(BigDecimal resultRatioOfCirculationStoreRatio) {
        this.resultRatioOfCirculationStoreRatio = resultRatioOfCirculationStoreRatio;
    }

    /**
     * @return the assetCurrentRatio
     */
    public BigDecimal getAssetCurrentRatio() {
        return assetCurrentRatio;
    }

    /**
     * @param assetCurrentRatio the assetCurrentRatio to set
     */
    public void setAssetCurrentRatio(BigDecimal assetCurrentRatio) {
        this.assetCurrentRatio = assetCurrentRatio;
    }

    /**
     * @return the deductionCurrentRatio
     */
    public BigDecimal getDeductionCurrentRatio() {
        return deductionCurrentRatio;
    }

    /**
     * @param deductionCurrentRatio the deductionCurrentRatio to set
     */
    public void setDeductionCurrentRatio(BigDecimal deductionCurrentRatio) {
        this.deductionCurrentRatio = deductionCurrentRatio;
    }

    /**
     * @return the assetCurrentLastPeriodRatio
     */
    public BigDecimal getAssetCurrentLastPeriodRatio() {
        return assetCurrentLastPeriodRatio;
    }

    /**
     * @param assetCurrentLastPeriodRatio the assetCurrentLastPeriodRatio to set
     */
    public void setAssetCurrentLastPeriodRatio(BigDecimal assetCurrentLastPeriodRatio) {
        this.assetCurrentLastPeriodRatio = assetCurrentLastPeriodRatio;
    }

    /**
     * @return the netProfit
     */
    public BigDecimal getNetProfit() {
        return netProfit;
    }

    /**
     * @param netProfit the netProfit to set
     */
    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    /**
     * @return the ownerEquity
     */
    public BigDecimal getOwnerEquity() {
        return ownerEquity;
    }

    /**
     * @param ownerEquity the ownerEquity to set
     */
    public void setOwnerEquity(BigDecimal ownerEquity) {
        this.ownerEquity = ownerEquity;
    }

    /**
     * @return the sales
     */
    public BigDecimal getSales() {
        return sales;
    }

    /**
     * @param sales the sales to set
     */
    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    /**
     * @return the totalOfAssets
     */
    public BigDecimal getTotalOfAssets() {
        return totalOfAssets;
    }

    /**
     * @param totalOfAssets the totalOfAssets to set
     */
    public void setTotalOfAssets(BigDecimal totalOfAssets) {
        this.totalOfAssets = totalOfAssets;
    }

    /**
     * @return the costOfSoldGoods
     */
    public BigDecimal getCostOfSoldGoods() {
        return costOfSoldGoods;
    }

    /**
     * @param costOfSoldGoods the costOfSoldGoods to set
     */
    public void setCostOfSoldGoods(BigDecimal costOfSoldGoods) {
        this.costOfSoldGoods = costOfSoldGoods;
    }

    /**
     * @return the storageOfLastPeriod
     */
    public BigDecimal getStorageOfLastPeriod() {
        return storageOfLastPeriod;
    }

    /**
     * @param storageOfLastPeriod the storageOfLastPeriod to set
     */
    public void setStorageOfLastPeriod(BigDecimal storageOfLastPeriod) {
        this.storageOfLastPeriod = storageOfLastPeriod;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the glAnalyticsAccounts
     */
    public GlAnalyticsAccounts getGlAnalyticsAccounts() {
        return glAnalyticsAccounts;
    }

    /**
     * @param glAnalyticsAccounts the glAnalyticsAccounts to set
     */
    public void setGlAnalyticsAccounts(GlAnalyticsAccounts glAnalyticsAccounts) {
        this.glAnalyticsAccounts = glAnalyticsAccounts;
    }

    /**
     * @return the rowsOfGlAnalytics
     */
    public List<GlAnalyticsAccounts> getRowsOfGlAnalytics() {
        return rowsOfGlAnalytics;
    }

    /**
     * @param rowsOfGlAnalytics the rowsOfGlAnalytics to set
     */
    public void setRowsOfGlAnalytics(List<GlAnalyticsAccounts> rowsOfGlAnalytics) {
        this.rowsOfGlAnalytics = rowsOfGlAnalytics;
    }

    /**
     * @return the glAnalyticsList
     */
    public List<GlAnalyticsAccounts> getGlAnalyticsList() {
        return glAnalyticsList;
    }

    /**
     * @param glAnalyticsList the glAnalyticsList to set
     */
    public void setGlAnalyticsList(List<GlAnalyticsAccounts> glAnalyticsList) {
        this.glAnalyticsList = glAnalyticsList;
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the glaccountConverter
     */
    public GlaccountConverter getGlaccountConverter() {
        return glaccountConverter;
    }

    /**
     * @param glaccountConverter the glaccountConverter to set
     */
    public void setGlaccountConverter(GlaccountConverter glaccountConverter) {
        this.glaccountConverter = glaccountConverter;
    }

    /**
     * @return the glAccountSlected
     */
    public GlAccount getGlAccountSlected() {
        return glAccountSlected;
    }

    /**
     * @param glAccountSlected the glAccountSlected to set
     */
    public void setGlAccountSlected(GlAccount glAccountSlected) {
        this.glAccountSlected = glAccountSlected;
    }
}
