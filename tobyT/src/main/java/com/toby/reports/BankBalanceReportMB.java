/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.bean.BankBalanceReportBean;
import com.toby.bean.BankBalanceReportSearchBean;
import com.toby.businessservice.report.BankBalanceViewService;
import com.toby.converter.GlBankDTOConverter;
import com.toby.dto.GlBankDTO;
import com.toby.toby.BaseGlAccountReportBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
@Named(value = "bankBalanceReportMBNew2")
@ViewScoped
public class BankBalanceReportMB extends BaseGlAccountReportBean {

    private List<GlBankDTO> bankTransactionViewLocalList;
    private GlBankDTOConverter glBankConverter;

    private BankBalanceReportSearchBean bankBalanceReportSearchBean;
    private List<BankBalanceReportBean> bankBalanceReportBeanList;

    @EJB
    BankBalanceViewService bankBalanceViewService;

    @PostConstruct
    public void init() {
        reset();
    }

    public void convertDateFrom() {
        getBankBalanceReportSearchBean().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getBankBalanceReportSearchBean().setDateTo(findDateFromHijriTo());
    }

    @Override
    public void reset() {
        setBankBalanceReportBeanList(new ArrayList<>());
        setBankBalanceReportSearchBean(new BankBalanceReportSearchBean());
        setBankTransactionViewLocalList(getUserData().getGlBankDTOList());
        Collections.sort(getBankTransactionViewLocalList());
        setGlBankConverter(new GlBankDTOConverter(getBankTransactionViewLocalList()));
        resetDateFrom();
        resetDateTo();
    }

    @Override
    public void search() {
        getBankBalanceReportSearchBean().setBranchId(getUserData().getUserBranch().getId());
        setBankBalanceReportBeanList(bankBalanceViewService.searchNative(getBankBalanceReportSearchBean()));
    }

    @Override
    public void checkDate(Boolean dateType) {

    }

    public BankBalanceReportBean addOpeneingBalanceToPreviousBalance(Integer accountId, BankBalanceReportBean balanceReportBean) {
        if (accountId != null) {
            balanceReportBean.setBalance((balanceReportBean.getBalance() != null ? balanceReportBean.getBalance() : BigDecimal.ZERO).add(getTotalBalanceMap().get(accountId) != null ? getTotalBalanceMap().get(accountId) : BigDecimal.ZERO));
        }
        return balanceReportBean;
    }

    @Override
    public Map<Integer, BigDecimal> getTotalBalanceMap() {
        return super.getTotalBalanceMap(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        getBankBalanceReportSearchBean().setDateFrom(new Date());

    }

    @Override
    public void resetDateTo() {
        getBankBalanceReportSearchBean().setDateTo(new Date());

    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateFrom", getBankBalanceReportSearchBean().getDateFrom());
        hashMap.put("dateTo", getBankBalanceReportSearchBean().getDateTo());
        //hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("reportNameText", userDDs.get("SAFES_BANK_BALANCES"));
        hashMap.put("recipientText", userDDs.get("SAVE_NUM"));
        hashMap.put("bankNameText", userDDs.get("SAFE_NAME"));
        hashMap.put("importedText", userDDs.get("IMPORT"));
        hashMap.put("exportedText", userDDs.get("EXPORT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("previousBalanceText", userDDs.get("PREVIOUS_BALANCE"));
        hashMap.put("transactionTypeText", userDDs.get("TRANSACTION"));

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (getBankBalanceReportBeanList() != null && !bankBalanceReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "banksBalanceReport.jasper", getBankBalanceReportBeanList(), "pdf");
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
     * @return the bankTransactionViewLocalList
     */
    public List<GlBankDTO> getBankTransactionViewLocalList() {
        return bankTransactionViewLocalList;
    }

    /**
     * @param bankTransactionViewLocalList the bankTransactionViewLocalList to
     * set
     */
    public void setBankTransactionViewLocalList(List<GlBankDTO> bankTransactionViewLocalList) {
        this.bankTransactionViewLocalList = bankTransactionViewLocalList;
    }

    /**
     * @return the bankBalanceReportBeanList
     */
    public List<BankBalanceReportBean> getBankBalanceReportBeanList() {
        return bankBalanceReportBeanList;
    }
    /**
     * @param bankBalanceReportBeanList the bankBalanceReportBeanList to set
     */
    public void setBankBalanceReportBeanList(List<BankBalanceReportBean> bankBalanceReportBeanList) {
        this.bankBalanceReportBeanList = bankBalanceReportBeanList;
    }

    /**
     * @return the glBankConverter
     */
    public GlBankDTOConverter getGlBankConverter() {
        return glBankConverter;
    }

    /**
     * @param glBankConverter the glBankConverter to set
     */
    public void setGlBankConverter(GlBankDTOConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    /**
     * @return the bankBalanceReportSearchBean
     */
    public BankBalanceReportSearchBean getBankBalanceReportSearchBean() {
        return bankBalanceReportSearchBean;
    }

    /**
     * @param bankBalanceReportSearchBean the bankBalanceReportSearchBean to set
     */
    public void setBankBalanceReportSearchBean(BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        this.bankBalanceReportSearchBean = bankBalanceReportSearchBean;
    }
}
