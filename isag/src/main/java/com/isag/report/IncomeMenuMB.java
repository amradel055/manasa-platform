/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.AccountsSystemSettingsService;
import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GeneraljournalService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.reports.searchBean.CommonSearchBean;
import com.isag.entity.AccountsSystemSettings;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlAccount;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.IncomeMenuBean;
import com.isag.report.entity.test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author hq002
 */
public class IncomeMenuMB extends BaseGlAccountReportBean {

    private List<GlAccount> glAccountRootList;
    private List<IncomeMenuBean> incomeMenuBeanList;
    private List<IncomeMenuBean> incomeMenuBeanViewList;
    private List<IncomeMenuBean> expensesAndIncomeList;
    private List<IncomeMenuBean> incomeMenuBeanViewPartOfIncomeList;
    private List<IncomeMenuBean> incomeMenuBeanViewPartOfExpensesList;
    private List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp;
    private List<GeneralJournalDetails> generalJournalDetailsForDebitList;
    private List<GeneralJournalDetails> generalJournalDetailsForCreditList;
    private Date dateTo;
    private Integer level;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal valueOfIncome = BigDecimal.ZERO;
    private BigDecimal valueOfExpenses = BigDecimal.ZERO;
    private ExternalContext context;
    private boolean flow = false;
    private boolean zeroAmount = false;
    private Integer oneListConfirmation = 0;
    private Integer indexOfIncome;
    private Integer indexOfExpenses;
    private IncomeMenuBean Lastbean;
    private CommonSearchBean commonSearchBean;

    private JSONArray c = new JSONArray();

    private List<Object> rows;
    Map<Integer, IncomeMenuBean> IncomeMenuBeanMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();
    private TreeMap<Integer, IncomeMenuBean> accountMap = new TreeMap<Integer, IncomeMenuBean>();
    private AccountsSystemSettings accountsSystemSettings;

    @EJB
    GlAccountService glAccountService;

    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;
   
    @EJB
    GeneraljournalService generaljournalService;

    @EJB
    private AccountsSystemSettingsService accountsSystemSettingsService;

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        reset();
        level = 1;
        accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId());

    }

    @Override
    public void reset() {
        glAccountRootList = new ArrayList<>();
        incomeMenuBeanList = new ArrayList<>();
        expensesAndIncomeList = new ArrayList<>();
        IncomeMenuBeanMap = new HashMap<>();
        totalBalanceMap = new HashMap<>();
        accountMap = new TreeMap<>();
        incomeMenuBeanViewPartOfIncomeList = new ArrayList<>();
        incomeMenuBeanViewPartOfExpensesList = new ArrayList<>();
        generalJournalDetailsForDebitList = new ArrayList<>();
        generalJournalDetailsForCreditList = new ArrayList<>();
        holdIncomeMenuBeanViewListTemp = new ArrayList<>();
        Lastbean = new IncomeMenuBean();
        commonSearchBean = new CommonSearchBean();
        jsonArray();

    }

    @Override
    public void search() {
//         totalBalanceMap = new HashMap<>();
//           IncomeMenuBeanMap = new HashMap<>();
        reset();
        Map<String, String> userDDs = getUserData().getUserDDs();
        commonSearchBean.setDateTo(dateTo);
        commonSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        if (checkAppearenceBeforeRevision() == 1) {
            //to make sure that in the second lab doesn't erease the previous list created for assets and deductions
            setIncomeMenuBeanViewList(new ArrayList<IncomeMenuBean>());
//            reset();
            // commonSearchBean.setDateTo(dateTo);
            glAccountRootList = glAccountService.getBranchAccountRootsForExpensesAndIncome(getUserData().getUserBranch().getId());
            Integer debitOrCredit;
            debitOrCredit = 1;
            generalJournalDetailsForDebitList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccount(getUserData().getUserBranch().getId(), debitOrCredit, commonSearchBean, getGlYearSelection());
            //debitOrCredit = 2;
            // generalJournalDetailsForCreditList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccount(getUserData().getUserBranch().getId(), getGlYearSelection().getDateFrom(), dateTo, debitOrCredit);
            //generalJournalDetailsList = ListUtils.union(generalJournalDetailsForDebitList, generalJournalDetailsForCreditList);
            for (GeneralJournalDetails journalDetails : generalJournalDetailsForDebitList) {
                totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
            }
            prepareDataForReport(glAccountRootList);
            incomeMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());
            if (level != null) {
                for (IncomeMenuBean bean : incomeMenuBeanList) {
                    if (level.compareTo(bean.getLevel()) == 0 || bean.getParent() == null) {
                        getIncomeMenuBeanViewList().add(bean);
                        accountMap.put(bean.getAccountNumber(), bean);
                    }
                }
                incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
            } else {
                setIncomeMenuBeanViewList(incomeMenuBeanList);
                for (IncomeMenuBean incomeMenuArranged : getIncomeMenuBeanViewList()) {
                    accountMap.put(incomeMenuArranged.getAccountNumber(), incomeMenuArranged);
                }
                incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
            }

            for (IncomeMenuBean bean : incomeMenuBeanList) {
                if (bean.getLevel() == 1) {
                    getExpensesAndIncomeList().add(bean);
                }
            }

            totalBalance = expensesAndIncomeList.get(1).getValue().add(expensesAndIncomeList.get(0).getValue());
            splitListAndReunionAgain(incomeMenuBeanViewList);
            calculateRatio();

            IncomeMenuBean bean = new IncomeMenuBean();
            Lastbean.setValue(totalBalance);
            if (Lastbean.getValue().signum() > 0) {
                Lastbean.setAccountName("صافي الربح ");
                Lastbean.setTotalReference(10);
            } else {
                Lastbean.setAccountName("صافي الخسارة ");
                Lastbean.setTotalReference(20);
            }
            Lastbean.setColorReference(0);
            Lastbean.setParent(null);
            Lastbean.setRatio(null);
            Lastbean.setLevel(-1);
            Lastbean.setAppearenceOfTotal(true);
            incomeMenuBeanViewList.add(Lastbean);
            deleteZeroRecords();
           
            
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب مراجعة القيود اولا"));
        }
    }

    public Integer checkAppearenceBeforeRevision() {
        if (accountsSystemSettings != null && accountsSystemSettings.getAccountStatementAppearance().equalsIgnoreCase("APPEARANCE_AFTER_REVISION")) {
            List<GeneralJournal> generalJournalList = generaljournalService.getAllNotRevisionJournals(commonSearchBean, getGlYearSelection());
            if (generalJournalList != null && generalJournalList.size() > 0) {
                return 0;
            }
        }
        return 1;
    }

    private void prepareDataForReport(List<GlAccount> glAccountRootList) {
        for (GlAccount glAccount : glAccountRootList) {
            IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
            incomeMenuBean.setAccountName(glAccount.getName());
            incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
            incomeMenuBean.setLevel(glAccount.getLevelAcc());
            incomeMenuBean.setId(glAccount.getId());
            incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());

            incomeMenuBeanList.add(incomeMenuBean);
            IncomeMenuBeanMap.put(glAccount.getId(), incomeMenuBean);
            getChildTreeNodesForAccount(glAccount);
        }
    }

    private void getChildTreeNodesForAccount(GlAccount parentAcc) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
                incomeMenuBean.setAccountName(childAcc.getName());
                incomeMenuBean.setAccountNumber(childAcc.getAccNumber());
                incomeMenuBean.setId(childAcc.getId());
                incomeMenuBean.setLevel(childAcc.getLevelAcc());
                incomeMenuBean.setParent(childAcc.getParentAcc() == null ? null : childAcc.getParentAcc().getId());
                if (childAcc.getType() != null && childAcc.getType() == 1) {
                    //total = generaljournalDetailsService.getBalanceForGlAccount(childAcc.getId(), getGlYearSelection().getDateFrom(), dateTo);
                    total = totalBalanceMap.get(childAcc.getId());
                    if (total != null) {
                        getTotal().add(total);
                        incomeMenuBean.setValue(total);
                        if (childAcc.getParentAcc() != null) {
                            putValueOfParent(childAcc.getParentAcc(), total);
                        }
                    }
                }
                incomeMenuBeanList.add(incomeMenuBean);
                IncomeMenuBeanMap.put(childAcc.getId(), incomeMenuBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal value) {
        IncomeMenuBean menuBean = IncomeMenuBeanMap.get(parentAcc.getId());
        menuBean.setValue(menuBean.getValue() == null ? BigDecimal.ZERO : menuBean.getValue());
        menuBean.setValue(menuBean.getValue().add(value == null ? BigDecimal.ZERO : value));
        //menuBean.setValue((value == null ? BigDecimal.ZERO : value).subtract(menuBean.getValue()));
        IncomeMenuBeanMap.put(parentAcc.getId(), menuBean);
        if (parentAcc.getParentAcc() != null) {
            putValueOfParent(parentAcc.getParentAcc(), value);
        }
    }

    public boolean chooseTheCurrentPage(ExternalContext context) {

        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("incomeMenu")) {
            return true;
        } else {
            return false;
        }
    }

    // chops a list into non-view sublists of length L
    static <T> List<List<T>> chopped(List<T> list, int startOfList, int endOfList) {
        List<List<T>> parts = new ArrayList<List<T>>();
        parts.add(new ArrayList<T>(list.subList(startOfList, endOfList)));
        return parts;
    }

    public List<IncomeMenuBean> splitListAndReunionAgain(List<IncomeMenuBean> incomeMenuBeans) {

        int i = 0;
        for (IncomeMenuBean imb : incomeMenuBeanViewList) {
            if (imb.getLevel() == 1) {
                if (i == 1) {
                    indexOfIncome = incomeMenuBeanViewList.indexOf(imb);
                    valueOfIncome = imb.getValue();
                } else if (i == 0) {
                    indexOfExpenses = incomeMenuBeanViewList.indexOf(imb);
                    valueOfExpenses = imb.getValue();
                }
                i++;
            }
        }
        List<List<IncomeMenuBean>> incomeMenuBeanViewPartOfIncomeList = chopped(incomeMenuBeanViewList, indexOfIncome, (incomeMenuBeanViewList.size()));
        List<List<IncomeMenuBean>> incomeMenuBeanViewPartOfExpensesList = chopped(incomeMenuBeanViewList, indexOfExpenses, indexOfIncome);
        incomeMenuBeanViewList = new ArrayList<>();
        incomeMenuBeanViewList = ListUtils.union(incomeMenuBeanViewPartOfIncomeList.get(0), incomeMenuBeanViewPartOfExpensesList.get(0));

        return incomeMenuBeanViewList;
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(dateTo)) {
                resetDateTo();
            }
        }
    }

    @Override
    public void resetDateFrom() {

    }

    @Override
    public void resetDateTo() {
        setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        String levelText = userDDs.get("LEVEL");
        String dateText = userDDs.get("DATE");
        String accountNameText = userDDs.get("ACCOUNT_NAME");
        String accountNumberText = userDDs.get("ACCOUNT_COD");
        String amountText = userDDs.get("AMOUNT");
        String ratioText = userDDs.get("PERCENTAGE");
        String totalProfitOrLossText = Lastbean.getAccountName();

        String PrintedBy = getUserData().getUser().getName();
        String companyName = getUserData().getCompany().getName();
        String companyImage = getUserData().getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();

        HashMap hashMap = new HashMap();
        // hashMap.put("companyName", companyName);
        hashMap.put("levelFrom", level);
        hashMap.put("dateTo", dateTo);

        hashMap.put("levelText", levelText);
        hashMap.put("dateText", dateText);
        hashMap.put("accountNameText", accountNameText);
        hashMap.put("accountNumberText", accountNumberText);
        hashMap.put("amountText", amountText);
        hashMap.put("ratioText", ratioText);
        hashMap.put("PrintedBy", PrintedBy);
        //hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", branchName);
        hashMap.put("totalProfitOrLossText", totalProfitOrLossText);
        hashMap.put("totalBalance", totalBalance);
        String menuNameText = userDDs.get("INCOM_MEN");
        hashMap.put("menuNameText", menuNameText);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (incomeMenuBeanViewList != null && !incomeMenuBeanViewList.isEmpty()) {
            incomeMenuBeanViewList.remove(incomeMenuBeanViewList.size() - 1);
            fillReport(prepareReport(), getUserData().getReportPath() + "incomeMenu.jasper", incomeMenuBeanViewList, "pdf");
        } else {
            search();
            incomeMenuBeanViewList.remove(incomeMenuBeanViewList.size() - 1);
            fillReport(prepareReport(), getUserData().getReportPath() + "incomeMenu.jasper", incomeMenuBeanViewList, "pdf");
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

    public void deleteZeroRecords() {

        if (zeroAmount && incomeMenuBeanViewList != null) {
            holdIncomeMenuBeanViewListTemp = new ArrayList<IncomeMenuBean>(incomeMenuBeanViewList);
            Iterator it = incomeMenuBeanViewList.iterator();
            List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();
            while (it.hasNext()) {
                IncomeMenuBean imb = new IncomeMenuBean();
                imb = (IncomeMenuBean) it.next();

                if (imb.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    incomeMenuBeanIterationList.add(imb);
                }
            }
            incomeMenuBeanViewList = new ArrayList<>(incomeMenuBeanIterationList);
        } else if (holdIncomeMenuBeanViewListTemp != null && !holdIncomeMenuBeanViewListTemp.isEmpty()) {
            incomeMenuBeanViewList = new ArrayList<>(holdIncomeMenuBeanViewListTemp);
        }
    }

    public void calculateRatio() {
        Iterator it = incomeMenuBeanViewList.iterator();
        List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();
        int x = 0;

        if (valueOfIncome.compareTo(BigDecimal.ZERO) == 0) {
            valueOfIncome = BigDecimal.ONE;
        }
        if (valueOfExpenses.compareTo(BigDecimal.ZERO) == 0) {
            valueOfExpenses = BigDecimal.ONE;
        }

        while (it.hasNext()) {
            IncomeMenuBean imb = new IncomeMenuBean();
            imb = (IncomeMenuBean) it.next();

            BigDecimal percent = BigDecimal.valueOf(100);
            //calculate the ratio for income elements first
            if (imb.getLevel() != 1 && x == 1 && imb.getLevel() != null) {
                imb.setRatio((imb.getValue().abs().divide(valueOfIncome.abs(), 4, 4)).multiply(percent).setScale(2, BigDecimal.ROUND_UP));
                incomeMenuBeanIterationList.add(imb);
            } else if (imb.getLevel() != null && imb.getLevel() == 1) {
                // first it will add income element without calculate the percentage because it's level one which is parent
                //then increment x by 1 to claculate the elements below level 1
                // the second time cursor get here it will increment the x by 1 and add the second element of level 1 which is Expenses
                incomeMenuBeanIterationList.add(imb);
                imb.setAppearenceOfTotal(true);
                imb.setRatio(null);
                x++;
            }

            // because x became 2 it will calculate the ratio to the elements below the expenses
            if (imb.getLevel() != 1 && x == 2 && imb.getLevel() != null) {
                imb.setRatio((imb.getValue().abs().divide(valueOfExpenses.abs(), 4, 4)).multiply(percent).setScale(2, BigDecimal.ROUND_UP));
                incomeMenuBeanIterationList.add(imb);
            }
        }
        incomeMenuBeanViewList = new ArrayList<>(incomeMenuBeanIterationList);
    }

    test tes = new test();
    List<test> list = new ArrayList<>();

    public void jsonArray() {
        int q = 1;
        int w = 2;
        int e = 3;
        for (int i = 0; i < 10; i++) {
            tes = new test();
            tes.setX(q);
            tes.setY(w);
            tes.setZ(e);
            q += 1;
            w += 2;
            e += 3;
            list.add(tes);
        }

        String jsonStr = "{         \"dataArray\": [{\"x\":1,\"y\":2,\"z\":3},{\"x\":2,\"y\":4,\"z\":6},{\"x\":3,\"y\":6,\"z\":9},{\"x\":4,\"y\":8,\"z\":12},{\"x\":5,\"y\":10,\"z\":15},{\"x\":6,\"y\":12,\"z\":18},{\"x\":7,\"y\":14,\"z\":21},{\"x\":8,\"y\":16,\"z\":24},{\"x\":9,\"y\":18,\"z\":27},{\"x\":10,\"y\":20,\"z\":30}]      }";

        JSONObject jsonObj = new JSONObject(jsonStr);

        System.out.println("jsonObj: " + jsonObj);

        c = jsonObj.getJSONArray("dataArray");

        rows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("'" + "x" + "'");
        colmns.add("'" + "y" + "'");
        colmns.add("'" + "z" + "'");

//        System.out.println(colmns);
        rows.add(colmns);

//        System.out.println(rows);
//
//        System.out.println("c: " + c);
        for (int i = 0; i < c.length(); i++) {
            colmns = new ArrayList<>();
            JSONObject obj = c.getJSONObject(i);
            Object A = obj.get("x");
            Object B = obj.get("y");
            Object C = obj.get("z");

            colmns.add("'" + A + "'");
            colmns.add(B);
            colmns.add(C);

            rows.add(colmns);

        }

        for (Object row : rows) {
            System.out.println(row);
        }
    }

    /**
     * @return the glAccountRootList
     */
    public List<GlAccount> getGlAccountRootList() {
        return glAccountRootList;
    }

    /**
     * @param glAccountRootList the glAccountRootList to set
     */
    public void setGlAccountRootList(List<GlAccount> glAccountRootList) {
        this.glAccountRootList = glAccountRootList;
    }

    /**
     * @return the incomeMenuBeanList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanList() {
        return incomeMenuBeanList;
    }

    /**
     * @param incomeMenuBeanList the incomeMenuBeanList to set
     */
    public void setIncomeMenuBeanList(List<IncomeMenuBean> incomeMenuBeanList) {
        this.incomeMenuBeanList = incomeMenuBeanList;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the incomeMenuBeanViewList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewList() {
        return incomeMenuBeanViewList;
    }

    /**
     * @param incomeMenuBeanViewList the incomeMenuBeanViewList to set
     */
    public void setIncomeMenuBeanViewList(List<IncomeMenuBean> incomeMenuBeanViewList) {
        this.incomeMenuBeanViewList = incomeMenuBeanViewList;
    }

    /**
     * @return the accountMap
     */
    public TreeMap<Integer, IncomeMenuBean> getAccountMap() {
        return accountMap;
    }

    /**
     * @param accountMap the accountMap to set
     */
    public void setAccountMap(TreeMap<Integer, IncomeMenuBean> accountMap) {
        this.accountMap = accountMap;
    }

    /**
     * @return the context
     */
    public ExternalContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ExternalContext context) {
        this.context = context;
    }

    /**
     * @return the flow
     */
    public boolean isFlow() {
        return flow;
    }

    /**
     * @param flow the flow to set
     */
    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    /**
     * @return the expensesAndIncomeList
     */
    public List<IncomeMenuBean> getExpensesAndIncomeList() {
        return expensesAndIncomeList;
    }

    /**
     * @param expensesAndIncomeList the expensesAndIncomeList to set
     */
    public void setExpensesAndIncomeList(List<IncomeMenuBean> expensesAndIncomeList) {
        this.expensesAndIncomeList = expensesAndIncomeList;
    }

    /**
     * @return the oneListConfirmation
     */
    public Integer getOneListConfirmation() {
        return oneListConfirmation;
    }

    /**
     * @param oneListConfirmation the oneListConfirmation to set
     */
    public void setOneListConfirmation(Integer oneListConfirmation) {
        this.oneListConfirmation = oneListConfirmation;
    }

    /**
     * @return the totalBalance
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance the totalBalance to set
     */
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * @return the indexOfIncome
     */
    public Integer getIndexOfIncome() {
        return indexOfIncome;
    }

    /**
     * @param indexOfIncome the indexOfIncome to set
     */
    public void setIndexOfIncome(Integer indexOfIncome) {
        this.indexOfIncome = indexOfIncome;
    }

    /**
     * @return the indexOfExpensesn
     */
    public Integer getIndexOfExpenses() {
        return indexOfExpenses;
    }

    /**
     * @param indexOfExpenses the indexOfExpenses to set
     */
    public void setIndexOfExpenses(Integer indexOfExpenses) {
        this.indexOfExpenses = indexOfExpenses;
    }

    /**
     * @return the incomeMenuBeanViewPartOfIncomeList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewPartOfIncomeList() {
        return incomeMenuBeanViewPartOfIncomeList;
    }

    /**
     * @param incomeMenuBeanViewPartOfIncomeList the
     * incomeMenuBeanViewPartOfIncomeList to set
     */
    public void setIncomeMenuBeanViewPartOfIncomeList(List<IncomeMenuBean> incomeMenuBeanViewPartOfIncomeList) {
        this.incomeMenuBeanViewPartOfIncomeList = incomeMenuBeanViewPartOfIncomeList;
    }

    /**
     * @return the incomeMenuBeanViewPartOfExpensesList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewPartOfExpensesList() {
        return incomeMenuBeanViewPartOfExpensesList;
    }

    /**
     * @param incomeMenuBeanViewPartOfExpensesList the
     * incomeMenuBeanViewPartOfExpensesList to set
     */
    public void setIncomeMenuBeanViewPartOfExpensesList(List<IncomeMenuBean> incomeMenuBeanViewPartOfExpensesList) {
        this.incomeMenuBeanViewPartOfExpensesList = incomeMenuBeanViewPartOfExpensesList;
    }

    /**
     * @return the generalJournalDetailsForDebitList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsForDebitList() {
        return generalJournalDetailsForDebitList;
    }

    /**
     * @param generalJournalDetailsForDebitList the
     * generalJournalDetailsForDebitList to set
     */
    public void setGeneralJournalDetailsForDebitList(List<GeneralJournalDetails> generalJournalDetailsForDebitList) {
        this.generalJournalDetailsForDebitList = generalJournalDetailsForDebitList;
    }

    /**
     * @return the generalJournalDetailsForCreditList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsForCreditList() {
        return generalJournalDetailsForCreditList;
    }

    /**
     * @param generalJournalDetailsForCreditList the
     * generalJournalDetailsForCreditList to set
     */
    public void setGeneralJournalDetailsForCreditList(List<GeneralJournalDetails> generalJournalDetailsForCreditList) {
        this.generalJournalDetailsForCreditList = generalJournalDetailsForCreditList;
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
     * @return the zeroAmount
     */
    public boolean isZeroAmount() {
        return zeroAmount;
    }

    /**
     * @param zeroAmount the zeroAmount to set
     */
    public void setZeroAmount(boolean zeroAmount) {
        this.zeroAmount = zeroAmount;
    }

    /**
     * @return the holdIncomeMenuBeanViewListTemp
     */
    public List<IncomeMenuBean> getHoldIncomeMenuBeanViewListTemp() {
        return holdIncomeMenuBeanViewListTemp;
    }

    /**
     * @param holdIncomeMenuBeanViewListTemp the holdIncomeMenuBeanViewListTemp
     * to set
     */
    public void setHoldIncomeMenuBeanViewListTemp(List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp) {
        this.holdIncomeMenuBeanViewListTemp = holdIncomeMenuBeanViewListTemp;
    }

    /**
     * @return the valueOfIncome
     */
    public BigDecimal getValueOfIncome() {
        return valueOfIncome;
    }

    /**
     * @param valueOfIncome the valueOfIncome to set
     */
    public void setValueOfIncome(BigDecimal valueOfIncome) {
        this.valueOfIncome = valueOfIncome;
    }

    /**
     * @return the valueOfExpenses
     */
    public BigDecimal getValueOfExpenses() {
        return valueOfExpenses;
    }

    /**
     * @param valueOfExpenses the valueOfExpenses to set
     */
    public void setValueOfExpenses(BigDecimal valueOfExpenses) {
        this.valueOfExpenses = valueOfExpenses;
    }

    /**
     * @return the rows
     */
    public List<Object> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<Object> rows) {
        this.rows = rows;
    }

    /**
     * @return the c
     */
    public JSONArray getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(JSONArray c) {
        this.c = c;
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
}
