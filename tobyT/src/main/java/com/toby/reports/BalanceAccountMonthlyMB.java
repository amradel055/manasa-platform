package com.toby.reports;

import com.toby.businessservice.report.BalanceAccountViewService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.reports.searchBean.BalanceAccountMonthlySearchBean;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.report.entity.BalanceAccountMonthlyBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import com.toby.views.BalanceAccountView;
import net.sf.jasperreports.engine.JRException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("balanceAccountMonthlyMB")
@ViewScoped
public class BalanceAccountMonthlyMB extends BaseGlAccountReportBean {

    private List<BalanceAccountMonthlyBean> balanceAccountMonthlyBeanList;
    private List<BalanceAccountMonthlyBean> balanceAccountMonthlyBeanViewList;
    private List<BalanceAccountMonthlyBean> holdBalanceAccountMonthlyBeanListTemp;
    private GlaccountConverter accountConverter;
    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean;
    private Date fromDate;
    private boolean firstTime;
    private boolean lastTime;
    private Integer fromMonth;
    private Integer toMonth;
    private Integer settoMonthOnce;
    private List<Integer> months;
    private List<Integer> showMonths;
    private Integer jan;
    private Integer feb;
    private Integer mar;
    private Integer apl;
    private Integer may;
    private Integer jun;
    private Integer jly;
    private Integer aug;
    private Integer sep;
    private Integer oct;
    private Integer nov;
    private Integer dec;
    private BigDecimal janTotal;
    private BigDecimal febTotal;
    private BigDecimal marTotal;
    private BigDecimal aplTotal;
    private BigDecimal mayTotal;
    private BigDecimal junTotal;
    private BigDecimal jlyTotal;
    private BigDecimal augTotal;
    private BigDecimal sepTotal;
    private BigDecimal octTotal;
    private BigDecimal novTotal;
    private BigDecimal decTotal;
    private Integer showRefernence;
    private boolean zeroAmount = false;

    @EJB
    GlAccountService glAccountService;
    @EJB
    BalanceAccountViewService balanceAccountViewService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        load();

        balanceAccountMonthlyBeanList = new ArrayList<>();
        balanceAccountMonthlyBeanViewList = new ArrayList<>();
        balanceAccountMonthlySearchBean = new BalanceAccountMonthlySearchBean();
        //balanceAccountMonthlySearchBean.setDateFrom(new GregorianCalendar(2018, Calendar.JANUARY, 01).getTime());
        //balanceAccountMonthlySearchBean.setDateTo(new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime());
        balanceAccountMonthlySearchBean.setDateFrom(getGlYearSelection().getDateFrom());
        balanceAccountMonthlySearchBean.setDateTo(getGlYearSelection().getDateTo());

        if (getSettoMonthOnce() == null) {
            toMonth = 12;
            setSettoMonthOnce((Integer) 1);
            reset();
        }
        costCenterConverter = new CostCenterConverter(getCostCenterList());
        glAdminUnitConverter = new GlAdminUnitConverter(getGlAdminUnitList());
        accountConverter = new GlaccountConverter(getGlAccountList());
    }

    @Override
    public void reset() {
        fromMonth = 1;
        toMonth = 12;
        showMonths = new ArrayList<>();
        jan = 1;
        feb = 1;
        mar = 1;
        apl = 1;
        may = 1;
        jun = 1;
        jly = 1;
        aug = 1;
        sep = 1;
        oct = 1;
        nov = 1;
        dec = 1;
        showMonths.add(jan);
        showMonths.add(feb);
        showMonths.add(mar);
        showMonths.add(apl);
        showMonths.add(may);
        showMonths.add(jun);
        showMonths.add(jly);
        showMonths.add(aug);
        showMonths.add(sep);
        showMonths.add(oct);
        showMonths.add(nov);
        showMonths.add(dec);

        balanceAccountMonthlyBeanList = new ArrayList<>();
        balanceAccountMonthlyBeanViewList = new ArrayList<>();
        holdBalanceAccountMonthlyBeanListTemp = new ArrayList<>();
        balanceAccountMonthlySearchBean = new BalanceAccountMonthlySearchBean();
//        balanceAccountMonthlySearchBean.setDateFrom(new GregorianCalendar(2018, Calendar.JANUARY, 01).getTime());
//        balanceAccountMonthlySearchBean.setDateTo(new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime());
        balanceAccountMonthlySearchBean.setDateFrom(getGlYearSelection().getDateFrom());
        balanceAccountMonthlySearchBean.setDateTo(getGlYearSelection().getDateTo());
        costCenterToSelected = null;
        costCenterFromSelected = null;
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;

    }

    public void resetScreen(ActionEvent actionEvent) {
        fromMonth = 1;
        toMonth = 12;
        showMonths = new ArrayList<>();
        jan = 1;
        feb = 1;
        mar = 1;
        apl = 1;
        may = 1;
        jun = 1;
        jly = 1;
        aug = 1;
        sep = 1;
        oct = 1;
        nov = 1;
        dec = 1;
        showMonths.add(jan);
        showMonths.add(feb);
        showMonths.add(mar);
        showMonths.add(apl);
        showMonths.add(may);
        showMonths.add(jun);
        showMonths.add(jly);
        showMonths.add(aug);
        showMonths.add(sep);
        showMonths.add(oct);
        showMonths.add(nov);
        showMonths.add(dec);
        balanceAccountMonthlyBeanList = new ArrayList<>();
        balanceAccountMonthlyBeanViewList = new ArrayList<>();
        balanceAccountMonthlySearchBean = new BalanceAccountMonthlySearchBean();
//        balanceAccountMonthlySearchBean.setDateFrom(new GregorianCalendar(2018, Calendar.JANUARY, 01).getTime());
//        balanceAccountMonthlySearchBean.setDateTo(new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime());
        balanceAccountMonthlySearchBean.setDateFrom(getGlYearSelection().getDateFrom());
        balanceAccountMonthlySearchBean.setDateTo(getGlYearSelection().getDateTo());
    }

    @Override
    public void search() {
        balanceAccountMonthlySearchBean.setAccountNumFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        balanceAccountMonthlySearchBean.setAccountNumTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        balanceAccountMonthlySearchBean.setCostCenterForm(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
        balanceAccountMonthlySearchBean.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        balanceAccountMonthlySearchBean.setAdminUnitForm(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        balanceAccountMonthlySearchBean.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
        balanceAccountMonthlyBeanList = new ArrayList<>();
        List<GlAccount> glAccountRootList = glAccountService.getBranchAccountRoots(getUserData().getSelectedBranch());

        prepareDataForReport(glAccountRootList);

        balanceAccountMonthlyBeanViewList = new ArrayList<>();
        for (BalanceAccountMonthlyBean accountMonthlyBean : balanceAccountMonthlyBeanList) {
            if (balanceAccountMonthlySearchBean.getAccountNumFrom() != null && balanceAccountMonthlySearchBean.getAccountNumFrom() > 0 && balanceAccountMonthlySearchBean.getAccountNumTo() != null && balanceAccountMonthlySearchBean.getAccountNumTo() > 0) {
                if (accountMonthlyBean.getAccountNumber() >= balanceAccountMonthlySearchBean.getAccountNumFrom() && accountMonthlyBean.getAccountNumber() <= balanceAccountMonthlySearchBean.getAccountNumTo()) {
                    balanceAccountMonthlyBeanViewList.add(accountMonthlyBean);
                }
            } else if (balanceAccountMonthlySearchBean.getAccountNumFrom() != null && balanceAccountMonthlySearchBean.getAccountNumFrom() > 0 && (balanceAccountMonthlySearchBean.getAccountNumTo() == null || balanceAccountMonthlySearchBean.getAccountNumTo() == 0)) {
                if (accountMonthlyBean.getAccountNumber() >= balanceAccountMonthlySearchBean.getAccountNumFrom()) {
                    balanceAccountMonthlyBeanViewList.add(accountMonthlyBean);
                }
            } else if (balanceAccountMonthlySearchBean.getAccountNumTo() != null && balanceAccountMonthlySearchBean.getAccountNumTo() > 0 && (balanceAccountMonthlySearchBean.getAccountNumFrom() == null || balanceAccountMonthlySearchBean.getAccountNumFrom() == 0)) {
                if (accountMonthlyBean.getAccountNumber() <= balanceAccountMonthlySearchBean.getAccountNumTo()) {
                    balanceAccountMonthlyBeanViewList.add(accountMonthlyBean);
                }
            }
        }

        if (!balanceAccountMonthlyBeanViewList.isEmpty()) {
            balanceAccountMonthlyBeanList = new ArrayList<>(balanceAccountMonthlyBeanViewList);
        }
        if (balanceAccountMonthlyBeanList != null && !balanceAccountMonthlyBeanList.isEmpty()) {
            setStickyHeader(true);
            //   getTotalForEveryColumn(balanceAccountMonthlyBeanList);
        } else {
            setStickyHeader(false);
        }
        deleteZeroRecords();
        calculateSumOfEachRow();
    }

    public void getTotalForEveryColumn(List<BalanceAccountMonthlyBean> balanceAccountMonthlyBeanAnyList) {
        janTotal = BigDecimal.ZERO;
        febTotal = BigDecimal.ZERO;
        marTotal = BigDecimal.ZERO;
        aplTotal = BigDecimal.ZERO;
        mayTotal = BigDecimal.ZERO;
        junTotal = BigDecimal.ZERO;
        jlyTotal = BigDecimal.ZERO;
        augTotal = BigDecimal.ZERO;
        sepTotal = BigDecimal.ZERO;
        octTotal = BigDecimal.ZERO;
        novTotal = BigDecimal.ZERO;
        decTotal = BigDecimal.ZERO;
        for (BalanceAccountMonthlyBean accountMonthlyBeanForTotal : balanceAccountMonthlyBeanAnyList) {
            janTotal = janTotal.add(accountMonthlyBeanForTotal.getJanValue() != null ? accountMonthlyBeanForTotal.getJanValue() : BigDecimal.ZERO);
            febTotal = febTotal.add(accountMonthlyBeanForTotal.getFebValue() != null ? accountMonthlyBeanForTotal.getFebValue() : BigDecimal.ZERO);
            marTotal = marTotal.add(accountMonthlyBeanForTotal.getMarValue() != null ? accountMonthlyBeanForTotal.getMarValue() : BigDecimal.ZERO);
            aplTotal = aplTotal.add(accountMonthlyBeanForTotal.getAprValue() != null ? accountMonthlyBeanForTotal.getAprValue() : BigDecimal.ZERO);
            mayTotal = mayTotal.add(accountMonthlyBeanForTotal.getMayValue() != null ? accountMonthlyBeanForTotal.getMayValue() : BigDecimal.ZERO);
            junTotal = junTotal.add(accountMonthlyBeanForTotal.getJuneValue() != null ? accountMonthlyBeanForTotal.getJuneValue() : BigDecimal.ZERO);
            jlyTotal = jlyTotal.add(accountMonthlyBeanForTotal.getJulValue() != null ? accountMonthlyBeanForTotal.getJulValue() : BigDecimal.ZERO);
            augTotal = augTotal.add(accountMonthlyBeanForTotal.getAugValue() != null ? accountMonthlyBeanForTotal.getAugValue() : BigDecimal.ZERO);
            sepTotal = sepTotal.add(accountMonthlyBeanForTotal.getSepValue() != null ? accountMonthlyBeanForTotal.getSepValue() : BigDecimal.ZERO);
            octTotal = octTotal.add(accountMonthlyBeanForTotal.getOctValue() != null ? accountMonthlyBeanForTotal.getOctValue() : BigDecimal.ZERO);
            novTotal = novTotal.add(accountMonthlyBeanForTotal.getNovValue() != null ? accountMonthlyBeanForTotal.getNovValue() : BigDecimal.ZERO);
            decTotal = decTotal.add(accountMonthlyBeanForTotal.getDecValue() != null ? accountMonthlyBeanForTotal.getDecValue() : BigDecimal.ZERO);
        }
    }

    private void prepareDataForReport(List<GlAccount> glAccountRootList) {
        for (GlAccount glAccount : glAccountRootList) {
            BalanceAccountMonthlyBean balanceAccountMonthlyBean = new BalanceAccountMonthlyBean();
            balanceAccountMonthlyBean.setAccountName("*" + glAccount.getName());
            balanceAccountMonthlyBean.setId(glAccount.getId());
            balanceAccountMonthlyBean.setAccountNumber(glAccount.getAccNumber());
            balanceAccountMonthlyBeanList.add(balanceAccountMonthlyBean);
            getChildTreeNodesForAccount(glAccount);
        }
    }

    private void getChildTreeNodesForAccount(GlAccount parentAcc) {
        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                BalanceAccountMonthlyBean balanceAccountMonthlyBean = new BalanceAccountMonthlyBean();
                for (int i = 0; i < childAcc.getLevelAcc(); i++) {
                    if (balanceAccountMonthlyBean.getAccountName() == null) {
                        balanceAccountMonthlyBean.setAccountName("*");
                    } else {
                        balanceAccountMonthlyBean.setAccountName("*" + balanceAccountMonthlyBean.getAccountName());
                    }
                }
                balanceAccountMonthlyBean.setAccountName(balanceAccountMonthlyBean.getAccountName() + childAcc.getName());
                balanceAccountMonthlyBean.setId(childAcc.getId());
                balanceAccountMonthlyBean.setAccountNumber(childAcc.getAccNumber());

                List<BalanceAccountView> balanceAccountViewList = balanceAccountViewService.getAllBalanceAccountViewList(childAcc.getId(), balanceAccountMonthlySearchBean);
                if (balanceAccountViewList != null && !balanceAccountViewList.isEmpty()) {
                    System.out.println("tot");
                }
                if (balanceAccountViewList.size() != 0) {
                    lastTime = false;
                    firstTime = true;
                    int month = this.month(balanceAccountMonthlySearchBean.getDateFrom());
                    int numberOfMonths = this.numberOfMonths();
                    for (int i = 0; i < numberOfMonths; i++) {
                        if (i == numberOfMonths - 1) {
                            lastTime = true;
                        }
                        this.putValuesOfMonths(month, balanceAccountViewList, balanceAccountMonthlyBean, parentAcc);
                        month += 1;
                    }

                }
                balanceAccountMonthlyBeanList.add(balanceAccountMonthlyBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    private void putValuesOfMonths(int month, List<BalanceAccountView> balanceAccountViewList, BalanceAccountMonthlyBean balanceAccountMonthlyBean, GlAccount parentAcc) {
        Date toDate;
        if (firstTime) {
            fromDate = balanceAccountMonthlySearchBean.getDateFrom();
        } else {
            fromDate = this.findLastDayOfMonth(fromDate, false);
        }
        if (!lastTime) {
            toDate = this.findLastDayOfMonth(fromDate, true);
        } else {
            toDate = balanceAccountMonthlySearchBean.getDateTo();
        }
        firstTime = false;
        switch (month) {
            case 1:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setJanValue(balanceAccountMonthlyBean.getJanValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getJanValue(), month);
                break;
            case 2:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setFebValue(balanceAccountMonthlyBean.getFebValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getFebValue(), month);
                break;
            case 3:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setMarValue(balanceAccountMonthlyBean.getMarValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getMarValue(), month);
                break;
            case 4:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setAprValue(balanceAccountMonthlyBean.getAprValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getAprValue(), month);
                break;
            case 5:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setMayValue(balanceAccountMonthlyBean.getMayValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getMayValue(), month);
                break;
            case 6:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setJuneValue(balanceAccountMonthlyBean.getJuneValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getJuneValue(), month);
                break;
            case 7:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setJulValue(balanceAccountMonthlyBean.getJulValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getJulValue(), month);
                break;
            case 8:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setAugValue(balanceAccountMonthlyBean.getAugValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getAugValue(), month);
                break;
            case 9:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setSepValue(balanceAccountMonthlyBean.getSepValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getSepValue(), month);
                break;
            case 10:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setOctValue(balanceAccountMonthlyBean.getOctValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getOctValue(), month);
                break;
            case 11:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setNovValue(balanceAccountMonthlyBean.getNovValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getNovValue(), month);
                break;
            case 12:
                for (BalanceAccountView balanceAccountView : balanceAccountViewList) {
                    if ((fromDate.before(balanceAccountView.getGeneralDate())
                            || fromDate.compareTo(balanceAccountView.getGeneralDate()) == 0)
                            && (toDate.after(balanceAccountView.getGeneralDate())
                            || toDate.compareTo(balanceAccountView.getGeneralDate()) == 0)) {
                        balanceAccountMonthlyBean.setDecValue(balanceAccountMonthlyBean.getDecValue().add(balanceAccountView.getBalance()));
                    }
                }
                putValueOfParent(parentAcc, balanceAccountMonthlyBean.getDecValue(), month);
                break;
        }
    }

    private void putValueOfParent(GlAccount parentAcc, BigDecimal balance, int month) {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        for (BalanceAccountMonthlyBean balanceAccountMonthlyBean : balanceAccountMonthlyBeanList) {
            if (balanceAccountMonthlyBean.getId().equals(parentAcc.getId())) {
                switch (month) {
                    case 1:
                        if (balanceAccountMonthlyBean.getJanValue() == null) {
                            balanceAccountMonthlyBean.setJanValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setJanValue(balanceAccountMonthlyBean.getJanValue().add(balance));
                        break;
                    case 2:
                        if (balanceAccountMonthlyBean.getFebValue() == null) {
                            balanceAccountMonthlyBean.setFebValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setFebValue(balanceAccountMonthlyBean.getFebValue().add(balance));
                        break;
                    case 3:
                        if (balanceAccountMonthlyBean.getMarValue() == null) {
                            balanceAccountMonthlyBean.setMarValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setMarValue(balanceAccountMonthlyBean.getMarValue().add(balance));
                        break;
                    case 4:
                        if (balanceAccountMonthlyBean.getAprValue() == null) {
                            balanceAccountMonthlyBean.setAprValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setAprValue(balanceAccountMonthlyBean.getAprValue().add(balance));
                        break;
                    case 5:
                        if (balanceAccountMonthlyBean.getMayValue() == null) {
                            balanceAccountMonthlyBean.setMayValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setMayValue(balanceAccountMonthlyBean.getMayValue().add(balance));
                        break;
                    case 6:
                        if (balanceAccountMonthlyBean.getJuneValue() == null) {
                            balanceAccountMonthlyBean.setJuneValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setJuneValue(balanceAccountMonthlyBean.getJuneValue().add(balance));
                        break;
                    case 7:
                        if (balanceAccountMonthlyBean.getJulValue() == null) {
                            balanceAccountMonthlyBean.setJulValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setJulValue(balanceAccountMonthlyBean.getJulValue().add(balance));
                        break;
                    case 8:
                        if (balanceAccountMonthlyBean.getAugValue() == null) {
                            balanceAccountMonthlyBean.setAugValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setAugValue(balanceAccountMonthlyBean.getAugValue().add(balance));
                        break;
                    case 9:
                        if (balanceAccountMonthlyBean.getSepValue() == null) {
                            balanceAccountMonthlyBean.setSepValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setSepValue(balanceAccountMonthlyBean.getSepValue().add(balance));
                        break;
                    case 10:
                        if (balanceAccountMonthlyBean.getOctValue() == null) {
                            balanceAccountMonthlyBean.setOctValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setOctValue(balanceAccountMonthlyBean.getOctValue().add(balance));
                        break;
                    case 11:
                        if (balanceAccountMonthlyBean.getNovValue() == null) {
                            balanceAccountMonthlyBean.setNovValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setNovValue(balanceAccountMonthlyBean.getNovValue().add(balance));
                        break;
                    case 12:
                        if (balanceAccountMonthlyBean.getDecValue() == null) {
                            balanceAccountMonthlyBean.setDecValue(BigDecimal.ZERO);
                        }
                        balanceAccountMonthlyBean.setDecValue(balanceAccountMonthlyBean.getDecValue().add(balance));
                        break;
                }
            }
        }
        if (parentAcc.getParentAcc() != null) {
            putValueOfParent(parentAcc.getParentAcc(), balance, month);
        }
    }

    private int month(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    private Date findLastDayOfMonth(Date date, boolean lastDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (lastDay) {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            c.add(Calendar.DATE, 1);
        }
        return c.getTime();
    }

    private int numberOfMonths() {
        Calendar fromDate = new GregorianCalendar();
        Calendar toDate = new GregorianCalendar();
        fromDate.setTime(balanceAccountMonthlySearchBean.getDateFrom());
        toDate.setTime(balanceAccountMonthlySearchBean.getDateTo());
        int yearsInBetween = toDate.get(Calendar.YEAR) - fromDate.get(Calendar.YEAR);
        int monthsDiff = toDate.get(Calendar.MONTH) - fromDate.get(Calendar.MONTH);
        return yearsInBetween * 12 + monthsDiff;
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getCompany().getName());

        hashMap.put("balanceAccountMonthlyText", userDDs.get("STATEMENT_MONTHLY_ACCOUNT"));
        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("fromCostCenterText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("toCostCenterText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("fromAdminUnitText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("toAdminUnitText", userDDs.get("ADMIN_UNIT_TO"));
        hashMap.put("januaryMonthText", userDDs.get("JANUARY"));
        hashMap.put("februaryMonthText", userDDs.get("FEBRUARY"));
        hashMap.put("marchMonthText", userDDs.get("MARCH"));
        hashMap.put("aprilMonthText", userDDs.get("APRIL"));
        hashMap.put("mayMonthText", userDDs.get("MAY"));
        hashMap.put("juneMonthText", userDDs.get("JUNE"));
        hashMap.put("julyMonthText", userDDs.get("JULY"));
        hashMap.put("augustMonthText", userDDs.get("AUGUST"));
        hashMap.put("septemberMonthText", userDDs.get("SEPTEMBER"));
        hashMap.put("octoberMonthText", userDDs.get("OCT"));
        hashMap.put("novomberMonthText", userDDs.get("NOV"));
        hashMap.put("decemberMonthText", userDDs.get("DEC"));
        hashMap.put("januValue", showMonths.get(0));
        hashMap.put("febValue", showMonths.get(1));
        hashMap.put("marValue", showMonths.get(2));
        hashMap.put("aprValue", showMonths.get(3));
        hashMap.put("mayValue", showMonths.get(4));
        hashMap.put("junValue", showMonths.get(5));
        hashMap.put("julyValue", showMonths.get(6));
        hashMap.put("augValue", showMonths.get(7));
        hashMap.put("septValue", showMonths.get(8));
        hashMap.put("octValue", showMonths.get(9));
        hashMap.put("novValue", showMonths.get(10));
        hashMap.put("decValue", showMonths.get(11));

        hashMap.put("fromAccountNumber", balanceAccountMonthlySearchBean.getAccountNumFrom());
        hashMap.put("toAccountNumber", balanceAccountMonthlySearchBean.getAccountNumTo());
        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
        hashMap.put("toCostCenterName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
        hashMap.put("toAdminUnitName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);
        hashMap.put("fromMonth", fromMonth);
        hashMap.put("toMonth", toMonth);
        hashMap.put("fromMonthText", userDDs.get("FROM_MONTH"));
        hashMap.put("toMonthText", userDDs.get("TO_MONTH"));

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (balanceAccountMonthlyBeanList != null && !balanceAccountMonthlyBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "balanceAcountMonthlyReport.jasper", balanceAccountMonthlyBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }

    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {

    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {

    }

    @Override
    public String getScreenName() {
        return null;
    }

    public List<BalanceAccountMonthlyBean> getBalanceAccountMonthlyBeanList() {
        return balanceAccountMonthlyBeanList;
    }

    public void setBalanceAccountMonthlyBeanList(List<BalanceAccountMonthlyBean> balanceAccountMonthlyBeanList) {
        this.balanceAccountMonthlyBeanList = balanceAccountMonthlyBeanList;
    }

    public BalanceAccountMonthlySearchBean getBalanceAccountMonthlySearchBean() {
        return balanceAccountMonthlySearchBean;
    }

    public void setBalanceAccountMonthlySearchBean(BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        this.balanceAccountMonthlySearchBean = balanceAccountMonthlySearchBean;
    }

    @Override
    public void resetDateFrom() {
        balanceAccountMonthlySearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    @Override
    public void resetDateTo() {
        balanceAccountMonthlySearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(balanceAccountMonthlySearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(balanceAccountMonthlySearchBean.getDateTo())) {
                resetDateTo();
            }
        }
    }

    public void hideAndShow() {

        months = new ArrayList<>();
        jan = 1;
        feb = 2;
        mar = 3;
        apl = 4;
        may = 5;
        jun = 6;
        jly = 7;
        aug = 8;
        sep = 9;
        oct = 10;
        nov = 11;
        dec = 12;
        months.add(jan);
        months.add(feb);
        months.add(mar);
        months.add(apl);
        months.add(may);
        months.add(jun);
        months.add(jly);
        months.add(aug);
        months.add(sep);
        months.add(oct);
        months.add(nov);
        months.add(dec);
        showMonths = new ArrayList<>();
        Iterator it = months.iterator();
        showRefernence = 0;
        while (it.hasNext()) {
            showRefernence = (Integer) it.next();
            if (fromMonth != null && fromMonth > 0 && toMonth != null && toMonth > 0) {
                if (showRefernence >= fromMonth && showRefernence <= toMonth) {
                    setShowRefernence(1);
                    showMonths.add(showRefernence);
                } else {
                    setShowRefernence(0);
                    showMonths.add(showRefernence);
                }
            } else if (fromMonth != null && fromMonth > 0 && (toMonth == null || toMonth == 0)) {
                if (showRefernence >= fromMonth) {
                    showMonths.add(showRefernence);
                }
            } else if ((fromMonth == null || fromMonth == 0) && toMonth != null && toMonth > 0) {
                if (showRefernence <= toMonth) {
                    showMonths.add(showRefernence);
                }
            }
        }
        calculateSumOfEachRow();
    }

    public void deleteZeroRecords() {

        if (zeroAmount && balanceAccountMonthlyBeanList != null && !balanceAccountMonthlyBeanList.isEmpty()) {
            holdBalanceAccountMonthlyBeanListTemp = new ArrayList<BalanceAccountMonthlyBean>(balanceAccountMonthlyBeanList);
            Iterator it = balanceAccountMonthlyBeanList.iterator();
            List<BalanceAccountMonthlyBean> accountMonthlyBeansIterationList = new ArrayList<>();
            while (it.hasNext()) {
                BalanceAccountMonthlyBean ipo = new BalanceAccountMonthlyBean();
                ipo = (BalanceAccountMonthlyBean) it.next();

                if (ipo.getJanValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getFebValue().compareTo(BigDecimal.ZERO) != 0
                        || ipo.getMarValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getAprValue().compareTo(BigDecimal.ZERO) != 0
                        || ipo.getMayValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getJuneValue().compareTo(BigDecimal.ZERO) != 0
                        || ipo.getJulValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getAugValue().compareTo(BigDecimal.ZERO) != 0
                        || ipo.getSepValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getOctValue().compareTo(BigDecimal.ZERO) != 0
                        || ipo.getNovValue().compareTo(BigDecimal.ZERO) != 0 || ipo.getDecValue().compareTo(BigDecimal.ZERO) != 0) {
                    accountMonthlyBeansIterationList.add(ipo);
                }
            }
            balanceAccountMonthlyBeanList = new ArrayList<>(accountMonthlyBeansIterationList);
        } else if (holdBalanceAccountMonthlyBeanListTemp != null && !holdBalanceAccountMonthlyBeanListTemp.isEmpty()) {
            balanceAccountMonthlyBeanList = new ArrayList<>(holdBalanceAccountMonthlyBeanListTemp);
        }
    }

    public void calculateSumOfEachRow() {
        if (balanceAccountMonthlyBeanList != null && !balanceAccountMonthlyBeanList.isEmpty()) {
            Integer sumReference;
            for (BalanceAccountMonthlyBean bamb : balanceAccountMonthlyBeanList) {
                sumReference = 1;
                bamb.setTotalValue(BigDecimal.ZERO);
                if (fromMonth == 1 && toMonth == 12) {
                    bamb.setTotalValue(bamb.getJanValue().add(bamb.getFebValue()).add(bamb.getMarValue())
                            .add(bamb.getAprValue()).add(bamb.getMayValue()).add(bamb.getJuneValue())
                            .add(bamb.getJulValue()).add(bamb.getAugValue()).add(bamb.getSepValue())
                            .add(bamb.getOctValue()).add(bamb.getNovValue()).add(bamb.getDecValue()));

                } else {

                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getJanValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getFebValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getMarValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getAprValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getMayValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getJuneValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getJulValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getAugValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getSepValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getOctValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getNovValue()));
                    }
                    sumReference++;
                    if (fromMonth <= sumReference && toMonth >= sumReference) {
                        bamb.setTotalValue(bamb.getTotalValue().add(bamb.getDecValue()));
                    }
                }
            }
        }
    }

    public void changeMonth() {
        toMonth = 5;

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

    /**
     * @return the balanceAccountMonthlyBeanViewList
     */
    public List<BalanceAccountMonthlyBean> getBalanceAccountMonthlyBeanViewList() {
        return balanceAccountMonthlyBeanViewList;
    }

    /**
     * @param balanceAccountMonthlyBeanViewList the
     * balanceAccountMonthlyBeanViewList to set
     */
    public void setBalanceAccountMonthlyBeanViewList(List<BalanceAccountMonthlyBean> balanceAccountMonthlyBeanViewList) {
        this.balanceAccountMonthlyBeanViewList = balanceAccountMonthlyBeanViewList;
    }

    /**
     * @return the fromMonth
     */
    public Integer getFromMonth() {
        return fromMonth;
    }

    /**
     * @param fromMonth the fromMonth to set
     */
    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    /**
     * @return the toMonth
     */
    public Integer getToMonth() {
        return toMonth;
    }

    /**
     * @param toMonth the toMonth to set
     */
    public void setToMonth(Integer toMonth) {
        this.toMonth = toMonth;
    }

    /**
     * @return the jan
     */
    public Integer getJan() {
        return jan;
    }

    /**
     * @param jan the jan to set
     */
    public void setJan(Integer jan) {
        this.jan = jan;
    }

    /**
     * @return the feb
     */
    public Integer getFeb() {
        return feb;
    }

    /**
     * @param feb the feb to set
     */
    public void setFeb(Integer feb) {
        this.feb = feb;
    }

    /**
     * @return the mar
     */
    public Integer getMar() {
        return mar;
    }

    /**
     * @param mar the mar to set
     */
    public void setMar(Integer mar) {
        this.mar = mar;
    }

    /**
     * @return the apl
     */
    public Integer getApl() {
        return apl;
    }

    /**
     * @param apl the apl to set
     */
    public void setApl(Integer apl) {
        this.apl = apl;
    }

    /**
     * @return the may
     */
    public Integer getMay() {
        return may;
    }

    /**
     * @param may the may to set
     */
    public void setMay(Integer may) {
        this.may = may;
    }

    /**
     * @return the jun
     */
    public Integer getJun() {
        return jun;
    }

    /**
     * @param jun the jun to set
     */
    public void setJun(Integer jun) {
        this.jun = jun;
    }

    /**
     * @return the jly
     */
    public Integer getJly() {
        return jly;
    }

    /**
     * @param jly the jly to set
     */
    public void setJly(Integer jly) {
        this.jly = jly;
    }

    /**
     * @return the aug
     */
    public Integer getAug() {
        return aug;
    }

    /**
     * @param aug the aug to set
     */
    public void setAug(Integer aug) {
        this.aug = aug;
    }

    /**
     * @return the sep
     */
    public Integer getSep() {
        return sep;
    }

    /**
     * @param sep the sep to set
     */
    public void setSep(Integer sep) {
        this.sep = sep;
    }

    /**
     * @return the oct
     */
    public Integer getOct() {
        return oct;
    }

    /**
     * @param oct the oct to set
     */
    public void setOct(Integer oct) {
        this.oct = oct;
    }

    /**
     * @return the nov
     */
    public Integer getNov() {
        return nov;
    }

    /**
     * @param nov the nov to set
     */
    public void setNov(Integer nov) {
        this.nov = nov;
    }

    /**
     * @return the dec
     */
    public Integer getDec() {
        return dec;
    }

    /**
     * @param dec the dec to set
     */
    public void setDec(Integer dec) {
        this.dec = dec;
    }

    /**
     * @return the months
     */
    public List<Integer> getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    /**
     * @return the showRefernence
     */
    public Integer getShowRefernence() {
        return showRefernence;
    }

    /**
     * @param showRefernence the showRefernence to set
     */
    public void setShowRefernence(Integer showRefernence) {
        this.showRefernence = showRefernence;
    }

    /**
     * @return the showMonths
     */
    public List<Integer> getShowMonths() {
        return showMonths;
    }

    /**
     * @param showMonths the showMonths to set
     */
    public void setShowMonths(List<Integer> showMonths) {
        this.showMonths = showMonths;
    }

    /**
     * @return the settoMonthOnce
     */
    public Integer getSettoMonthOnce() {
        return settoMonthOnce;
    }

    /**
     * @param settoMonthOnce the settoMonthOnce to set
     */
    public void setSettoMonthOnce(Integer settoMonthOnce) {
        this.settoMonthOnce = settoMonthOnce;
    }

    /**
     * @return the janTotal
     */
    public BigDecimal getJanTotal() {
        return janTotal;
    }

    /**
     * @param janTotal the janTotal to set
     */
    public void setJanTotal(BigDecimal janTotal) {
        this.janTotal = janTotal;
    }

    /**
     * @return the febTotal
     */
    public BigDecimal getFebTotal() {
        return febTotal;
    }

    /**
     * @param febTotal the febTotal to set
     */
    public void setFebTotal(BigDecimal febTotal) {
        this.febTotal = febTotal;
    }

    /**
     * @return the marTotal
     */
    public BigDecimal getMarTotal() {
        return marTotal;
    }

    /**
     * @param marTotal the marTotal to set
     */
    public void setMarTotal(BigDecimal marTotal) {
        this.marTotal = marTotal;
    }

    /**
     * @return the aplTotal
     */
    public BigDecimal getAplTotal() {
        return aplTotal;
    }

    /**
     * @param aplTotal the aplTotal to set
     */
    public void setAplTotal(BigDecimal aplTotal) {
        this.aplTotal = aplTotal;
    }

    /**
     * @return the mayTotal
     */
    public BigDecimal getMayTotal() {
        return mayTotal;
    }

    /**
     * @param mayTotal the mayTotal to set
     */
    public void setMayTotal(BigDecimal mayTotal) {
        this.mayTotal = mayTotal;
    }

    /**
     * @return the junTotal
     */
    public BigDecimal getJunTotal() {
        return junTotal;
    }

    /**
     * @param junTotal the junTotal to set
     */
    public void setJunTotal(BigDecimal junTotal) {
        this.junTotal = junTotal;
    }

    /**
     * @return the jlyTotal
     */
    public BigDecimal getJlyTotal() {
        return jlyTotal;
    }

    /**
     * @param jlyTotal the jlyTotal to set
     */
    public void setJlyTotal(BigDecimal jlyTotal) {
        this.jlyTotal = jlyTotal;
    }

    /**
     * @return the augTotal
     */
    public BigDecimal getAugTotal() {
        return augTotal;
    }

    /**
     * @param augTotal the augTotal to set
     */
    public void setAugTotal(BigDecimal augTotal) {
        this.augTotal = augTotal;
    }

    /**
     * @return the sepTotal
     */
    public BigDecimal getSepTotal() {
        return sepTotal;
    }

    /**
     * @param sepTotal the sepTotal to set
     */
    public void setSepTotal(BigDecimal sepTotal) {
        this.sepTotal = sepTotal;
    }

    /**
     * @return the octTotal
     */
    public BigDecimal getOctTotal() {
        return octTotal;
    }

    /**
     * @param octTotal the octTotal to set
     */
    public void setOctTotal(BigDecimal octTotal) {
        this.octTotal = octTotal;
    }

    /**
     * @return the novTotal
     */
    public BigDecimal getNovTotal() {
        return novTotal;
    }

    /**
     * @param novTotal the novTotal to set
     */
    public void setNovTotal(BigDecimal novTotal) {
        this.novTotal = novTotal;
    }

    /**
     * @return the decTotal
     */
    public BigDecimal getDecTotal() {
        return decTotal;
    }

    /**
     * @param decTotal the decTotal to set
     */
    public void setDecTotal(BigDecimal decTotal) {
        this.decTotal = decTotal;
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
     * @return the holdBalanceAccountMonthlyBeanListTemp
     */
    public List<BalanceAccountMonthlyBean> getHoldBalanceAccountMonthlyBeanListTemp() {
        return holdBalanceAccountMonthlyBeanListTemp;
    }

    /**
     * @param holdBalanceAccountMonthlyBeanListTemp the
     * holdBalanceAccountMonthlyBeanListTemp to set
     */
    public void setHoldBalanceAccountMonthlyBeanListTemp(List<BalanceAccountMonthlyBean> holdBalanceAccountMonthlyBeanListTemp) {
        this.holdBalanceAccountMonthlyBeanListTemp = holdBalanceAccountMonthlyBeanListTemp;
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

}
