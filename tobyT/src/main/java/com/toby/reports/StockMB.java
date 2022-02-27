/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

/**
 *
 * @author hq002
 */
import com.toby.businessservice.report.CashAccountSettlmentService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.converter.GlBankConverter;
import com.toby.entity.GlBank;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.CashAccountSettlmentView;
import com.toby.views.SubAccountSummaryReport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

@Named(value = "stockMB")
@ViewScoped
public class StockMB extends BaseGlAccountReportBean {

    private Integer branchId;
    private GlBankConverter glBankConverter;
    private List<GlBank> glBankList;

    private SubAccountSummarySearchBean cashAccountSettlementSearchBean;
    private List<CashAccountSettlmentView> cashAccountSettlementList;
    private List<SubAccountSummaryReport> cashAccountSettlementReports;

    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;

    @EJB
    GlAccountService glAccountService;

    @EJB
    GlYearService glYearService;

    @EJB
    CostCenterService costCenterService;

    @EJB
    GlBankService glBankService;

    @EJB
    CashAccountSettlmentService cashAccountSettlmentService;

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

        cashAccountSettlementSearchBean = new SubAccountSummarySearchBean();
        cashAccountSettlementSearchBean.setOrderBy(0);
        cashAccountSettlementSearchBean.setType(3);
        cashAccountSettlementSearchBean.setCurrencyType(0);
        cashAccountSettlementReports = new ArrayList<>();
        glBankList = new ArrayList<>();

        branchId = getUserData().getUserBranch().getId();

        glBankList = glBankService.getAllGlBankByBranchId(branchId);

        glBankConverter = new GlBankConverter(glBankList);

    }

    @Override
    public void reset() {
        load();
        resetDateFrom();
        resetDateTo();
    }

    @Override
    public void search() {

        cashAccountSettlementSearchBean.setBranchId(branchId);

        cashAccountSettlementReports = new ArrayList<>();
        cashAccountSettlementList = cashAccountSettlmentService.findAllCashAccountSettlmentForLineCheck(cashAccountSettlementSearchBean);

        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;

        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;
        SubAccountSummaryReport cashAccountSettlment;
        for (CashAccountSettlmentView cashAccountSettlmentView : cashAccountSettlementList) {
            cashAccountSettlment = new SubAccountSummaryReport();
            cashAccountSettlment.setId(cashAccountSettlmentView.getBanktransactionId());
            cashAccountSettlment.setDate(cashAccountSettlmentView.getDate() != null ? cashAccountSettlmentView.getDate() : null);
            cashAccountSettlment.setRemark(cashAccountSettlmentView.getRemark() != null ? cashAccountSettlmentView.getRemark() : null);

            if (cashAccountSettlmentView.getType() != null) {
                if (cashAccountSettlementSearchBean.getCurrencyType() == 1) {
                    if (cashAccountSettlmentView.getType() == 0) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValue());
                    } else if (cashAccountSettlmentView.getType() == 1) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValue());
                    }
                } else {
                    if (cashAccountSettlmentView.getType() == 0) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValueLocal());
                    } else if (cashAccountSettlmentView.getType() == 1) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValueLocal());
                    }
                }
            }

            debit = cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount() : BigDecimal.ZERO;
            credit = cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount() : BigDecimal.ZERO;

            balance = balance.add(debit.subtract(credit));

            cashAccountSettlment.setBalance(balance);

            totalDebit = totalDebit.add(debit);
            totalCredit = totalCredit.add(credit);
            cashAccountSettlementReports.add(cashAccountSettlment);
        }
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

        hashMap.put("cashAccountSettlementReport", userDDs.get("SECURI_REPO"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        hashMap.put("glBankFromText", userDDs.get("FROM_SAVE"));
        hashMap.put("glBankToText", userDDs.get("TO_SAFE"));
        hashMap.put("fromDateText", userDDs.get("YEAR_TO"));
        hashMap.put("toDateText", userDDs.get("YEAR_FROM"));

        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("documentNumberText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("remarkText", userDDs.get("NOTE"));

        hashMap.put("total", userDDs.get("TOTAL"));
        hashMap.put("totalDebit", totalDebit);
        hashMap.put("totalCredit", totalCredit);

        hashMap.put("glBankFrom", getCashAccountSettlementSearchBean().getGlBankFrom() != null ? getCashAccountSettlementSearchBean().getGlBankFrom().getName() : null);
        hashMap.put("glBankTo", getCashAccountSettlementSearchBean().getGlBankTo() != null ? getCashAccountSettlementSearchBean().getGlBankTo().getName() : null);

        hashMap.put("fromDate", getCashAccountSettlementSearchBean().getDateFrom());
        hashMap.put("toDate", getCashAccountSettlementSearchBean().getDateTo());

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (cashAccountSettlementReports != null && !cashAccountSettlementReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "stockReport.jasper", cashAccountSettlementReports, "pdf");
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

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the glBankConverter
     */
    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }

    /**
     * @param glBankConverter the glBankConverter to set
     */
    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    /**
     * @return the glBankList
     */
    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the cashAccountSettlementSearchBean
     */
    public SubAccountSummarySearchBean getCashAccountSettlementSearchBean() {
        return cashAccountSettlementSearchBean;
    }

    /**
     * @param cashAccountSettlementSearchBean the
     * cashAccountSettlementSearchBean to set
     */
    public void setCashAccountSettlementSearchBean(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        this.cashAccountSettlementSearchBean = cashAccountSettlementSearchBean;
    }

    /**
     * @return the cashAccountSettlementList
     */
    public List<CashAccountSettlmentView> getCashAccountSettlementList() {
        return cashAccountSettlementList;
    }

    /**
     * @param cashAccountSettlementList the cashAccountSettlementList to set
     */
    public void setCashAccountSettlementList(List<CashAccountSettlmentView> cashAccountSettlementList) {
        this.cashAccountSettlementList = cashAccountSettlementList;
    }

    /**
     * @return the cashAccountSettlementReports
     */
    public List<SubAccountSummaryReport> getCashAccountSettlementReports() {
        return cashAccountSettlementReports;
    }

    /**
     * @param cashAccountSettlementReports the cashAccountSettlementReports to
     * set
     */
    public void setCashAccountSettlementReports(List<SubAccountSummaryReport> cashAccountSettlementReports) {
        this.cashAccountSettlementReports = cashAccountSettlementReports;
    }

    /**
     * @return the totalCredit
     */
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    /**
     * @param totalCredit the totalCredit to set
     */
    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    /**
     * @return the totalDebit
     */
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    /**
     * @param totalDebit the totalDebit to set
     */
    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }
}
