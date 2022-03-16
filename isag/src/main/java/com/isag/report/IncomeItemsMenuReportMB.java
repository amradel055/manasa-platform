/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GeneralBudgetGuideService;
import com.isag.entity.GeneralBudgetGuide;
import com.isag.entity.GlAccount;
import com.isag.entiy.GlAccountEntity;
import com.isag.isag.UserData;
import com.isag.report.entity.IncomeMenuBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
@Named(value = "incomeItemsMenuReportMB")
@ViewScoped
public class IncomeItemsMenuReportMB extends HelperForIncomeMenuAndFinancialMenu {

    private ExternalContext context;
    private List<GeneralBudgetGuide> generalBudgetGuideList;
    private List<GlAccount> glAccountPrepareList;
    private List<GlAccountEntity> glAccountEntityArrangedList;
    private List<IncomeMenuBean> incomeMenuBeanArrangedList;
    private List<GlAccountEntity> glAccountEntityMirrorList;
    private Integer count;
    private BigDecimal totalOfEveryIndividualGroup;
    private BigDecimal totalOfWholeGroup;
    private Integer firstBeanReference;
    Integer totalOfIncome;
    Integer totalOfExpenses;
    BigDecimal netProfitOrLoss;
    private String lastName;

    @EJB
    private GeneralBudgetGuideService budgetGuideService;

    @PostConstruct
    public void init2() {
        setContext(FacesContext.getCurrentInstance().getExternalContext());
        setUserData((UserData) getContext().getSessionMap().get("userLogInData"));
        super.setNetProfitOrLoss(BigDecimal.ZERO);
        super.init();
        //   super.search();

        glAccountEntityMirrorList = new ArrayList<>(getGlAccountEntityList());
        calculateRatio();
    }

    public void resetFields() {
        generalBudgetGuideList = new ArrayList<>();
        glAccountPrepareList = new ArrayList<>();
        glAccountEntityArrangedList = new ArrayList<>();
        incomeMenuBeanArrangedList = new ArrayList<>();
        glAccountEntityMirrorList = new ArrayList<>();
        totalOfEveryIndividualGroup = BigDecimal.ZERO;
        netProfitOrLoss = BigDecimal.ZERO;
    }

    public void searchDate() {

        super.search();
    }

    @Override
    public BigDecimal getNetProfitOrLoss() {
        return super.getNetProfitOrLoss(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IncomeMenuBean getLastbean() {
        return super.getLastbean(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        hashMap.put("companyName", getUserData().getCompany().getName());

        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_COD"));
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("ratioText", userDDs.get("PERCENTAGE"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("totalText", userDDs.get("TOTAL"));
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("totalProfitOrLossText", lastName);
        hashMap.put("totalBalance", getNetProfitOrLoss());
        hashMap.put("menuNameText", userDDs.get("INCOM_ITEM_MEN"));
        hashMap.put("companyImage", getUserData().getImageReportPath());
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (getFinancialMenuBeanList() != null && !getFinancialMenuBeanList().isEmpty()) {
            getFinancialMenuBeanList().remove(getFinancialMenuBeanList().size() - 1);
            if (getFinancialMenuBeanList().get(getFinancialMenuBeanList().size() - 1).getValue().signum() > 0) {
                lastName = "صافي الربح :";
            } else {
                lastName = "صافي الخسارة :";
            }
            fillReport(prepareReport(), getUserData().getReportPath() + "incomeItemsMenuReport.jasper", getFinancialMenuBeanList(), "pdf");
        } else {
            search();
            getFinancialMenuBeanList().remove(getFinancialMenuBeanList().size() - 1);
            fillReport(prepareReport(), getUserData().getReportPath() + "incomeItemsMenuReport.jasper", getFinancialMenuBeanList(), "pdf");
        }
    }

    @Override
    public void setFinalTotalOfExpeneses(BigDecimal finalTotalOfExpeneses) {
        super.setFinalTotalOfExpeneses(finalTotalOfExpeneses); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFinalTotalOfIncome(BigDecimal finalTotalOfIncome) {
        super.setFinalTotalOfIncome(finalTotalOfIncome); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BigDecimal getFinalTotalOfExpeneses() {
        return super.getFinalTotalOfExpeneses(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BigDecimal getFinalTotalOfIncome() {
        return super.getFinalTotalOfIncome(); //To change body of generated methods, choose Tools | Templates.
    }

    public void calculateRatio() {
        if (getFinancialMenuBeanList() != null && !getFinancialMenuBeanList().isEmpty()) {
            Iterator it = getFinancialMenuBeanList().iterator();
            List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();

            if (BigDecimal.ZERO.compareTo(getFinalTotalOfIncome() == null ? BigDecimal.ONE : getFinalTotalOfIncome()) == 0) {
                setFinalTotalOfIncome(BigDecimal.ONE);
            }
            if (BigDecimal.ZERO.compareTo(getFinalTotalOfExpeneses() == null ? BigDecimal.ONE : getFinalTotalOfExpeneses()) == 0) {
                setFinalTotalOfExpeneses(BigDecimal.ONE);
            }

            while (it.hasNext()) {
                IncomeMenuBean imb = new IncomeMenuBean();
                imb = (IncomeMenuBean) it.next();
                if (imb.getLevel() == null) {
                    imb.setAppearenceOfTotal(true);
                    imb.setRatio(null);
                    incomeMenuBeanIterationList.add(imb);
                } else if (imb.getLevel() != null && imb.getTotalReference() == 10) {
                    imb.setRatio((imb.getValue().abs().divide(getFinalTotalOfIncome().abs(), 4, 4)).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP));
                    incomeMenuBeanIterationList.add(imb);
                } else if (imb.getLevel() != null && imb.getTotalReference() == 20) {
                    imb.setRatio((imb.getValue().abs().divide(getFinalTotalOfExpeneses().abs(), 4, 4)).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP));
                    incomeMenuBeanIterationList.add(imb);
                }
            }
            setFinancialMenuBeanList(new ArrayList<>(incomeMenuBeanIterationList));
        }
    }

    /**
     * @return the context
     */
    @Override
    public ExternalContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    @Override
    public void setContext(ExternalContext context) {
        this.context = context;
    }

    /**
     * @return the generalBudgetGuideList
     */
    @Override
    public List<GeneralBudgetGuide> getGeneralBudgetGuideList() {
        return generalBudgetGuideList;
    }

    /**
     * @param generalBudgetGuideList the generalBudgetGuideList to set
     */
    @Override
    public void setGeneralBudgetGuideList(List<GeneralBudgetGuide> generalBudgetGuideList) {
        this.generalBudgetGuideList = generalBudgetGuideList;
    }

    /**
     * @return the glAccountPrepareList
     */
    @Override
    public List<GlAccount> getGlAccountPrepareList() {
        return glAccountPrepareList;
    }

    /**
     * @param glAccountPrepareList the glAccountPrepareList to set
     */
    @Override
    public void setGlAccountPrepareList(List<GlAccount> glAccountPrepareList) {
        this.glAccountPrepareList = glAccountPrepareList;
    }

    /**
     * @return the glAccountEntityArrangedList
     */
    @Override
    public List<GlAccountEntity> getGlAccountEntityArrangedList() {
        return glAccountEntityArrangedList;
    }

    /**
     * @param glAccountEntityArrangedList the glAccountEntityArrangedList to set
     */
    @Override
    public void setGlAccountEntityArrangedList(List<GlAccountEntity> glAccountEntityArrangedList) {
        this.glAccountEntityArrangedList = glAccountEntityArrangedList;
    }

    /**
     * @return the incomeMenuBeanArrangedList
     */
    @Override
    public List<IncomeMenuBean> getIncomeMenuBeanArrangedList() {
        return incomeMenuBeanArrangedList;
    }

    /**
     * @param incomeMenuBeanArrangedList the incomeMenuBeanArrangedList to set
     */
    @Override
    public void setIncomeMenuBeanArrangedList(List<IncomeMenuBean> incomeMenuBeanArrangedList) {
        this.incomeMenuBeanArrangedList = incomeMenuBeanArrangedList;
    }

    /**
     * @return the glAccountEntityMirrorList
     */
    @Override
    public List<GlAccountEntity> getGlAccountEntityMirrorList() {
        return glAccountEntityMirrorList;
    }

    /**
     * @param glAccountEntityMirrorList the glAccountEntityMirrorList to set
     */
    @Override
    public void setGlAccountEntityMirrorList(List<GlAccountEntity> glAccountEntityMirrorList) {
        this.glAccountEntityMirrorList = glAccountEntityMirrorList;
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
}
