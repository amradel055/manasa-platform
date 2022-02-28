/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.BankTransactionViewService;
import com.toby.businessservice.GlBankService;
import com.toby.converter.GlBankConverter;
import com.toby.entity.GlBank;
import com.toby.entiy.GlBankTransactionReportEntity;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.BankTransactionView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author hq002
 */
@Named(value = "bankTransactionReportMB")
@ViewScoped
public class BankTransactionReportMB extends BaseGlAccountReportBean {

    private Date dateFrom;
    private Date dateTo;
    private Integer branchId;
    private GlBank glBank;
    private String bankName;
    private GlBankConverter glBankConverter;
    private List<GlBank> bankTransactionViewLocalList;
    private List<BankTransactionView> bankTransactionViewList;
    private List<GlBankTransactionReportEntity> bankTransactionReportEntityList;

    @EJB
    GlBankService glBankService;
    @EJB
    BankTransactionViewService bankTransactionViewService;

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
    public void reset() {
        load();
        resetDateFrom();
        resetDateTo();
    }

    @Override
    public void load() {
        bankTransactionViewList = new ArrayList<>();
        bankTransactionViewLocalList = new ArrayList<>();
        bankTransactionViewLocalList = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());
        glBankConverter = new GlBankConverter(bankTransactionViewLocalList);
        bankTransactionReportEntityList = new ArrayList<>();
    }

    @Override
    public void search() {

        try {
            bankTransactionViewList = new ArrayList<>();
            bankTransactionViewList = bankTransactionViewService.getAllBankTransactions(getUserData().getUserBranch().getId(), glBank.getId(), dateFrom, dateTo);
            bankTransactionReportEntityList = new ArrayList<>();
            if (bankTransactionViewList != null && !bankTransactionViewList.isEmpty()) {
                for (BankTransactionView btv : bankTransactionViewList) {
                    GlBankTransactionReportEntity reportEntity = new GlBankTransactionReportEntity();
                    reportEntity.setDate(btv.getDate());
                    reportEntity.setBankFrom(btv.getNameBankFrom());
                    reportEntity.setBankTo(btv.getNameBankTo());

                    reportEntity.setBankIdFrom(btv.getBankIdFrom());
                    reportEntity.setBankIdTo(btv.getBankIdTo());
                    if (glBank.getId() != null && glBank.getId().equals(btv.getBankIdFrom())) {
                        reportEntity.setValueLocalExported(btv.getValueLocal());
                        reportEntity.setValueLocalImported(BigDecimal.ZERO);
                        reportEntity.setSource(btv.getNameBankTo());
                        bankName = btv.getNameBankFrom();
                    } else if (glBank.getId().equals(btv.getBankIdTo())) {
                        reportEntity.setValueLocalImported(btv.getValueLocal());
                        reportEntity.setValueLocalExported(BigDecimal.ZERO);
                        reportEntity.setSource(btv.getNameBankFrom());
                        bankName = btv.getNameBankTo();
                    }
                    reportEntity.setRemark(btv.getRemark());
                    bankTransactionReportEntityList.add(reportEntity);
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة اولا"));
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(dateFrom)) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(dateTo)) {
                resetDateTo();
            }
        }
    }

    @Override
    public void resetDateFrom() {
        setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    public void checkFinancailYearFrom() {
        if (getDateFrom().before(getGlYearSelection().getDateFrom()) || getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateFrom();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public void checkFinancailYearTo() {
        if (getDateTo().before(getGlYearSelection().getDateFrom()) || getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateTo();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public List<GlBank> completeGlBank(String query) {
        List<GlBank> glBankList = bankTransactionViewLocalList;
        if (query == null || query.trim().equals("")) {
            glBankConverter = new GlBankConverter(glBankList);
            return glBankList;
        }
        List<GlBank> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        GlBank glBank;
        for (int i = 0; i < bankTransactionViewLocalList.size(); i++) {
            glBank = glBankList.get(i);
            nameAr = glBank.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCostCenters.contains(glBank)) {
                    filteredCostCenters.add(glBank);
                }
            }

            code = glBank.getId();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredCostCenters.contains(glBank)) {
                    filteredCostCenters.add(glBank);
                }
            }
        }

        glBankConverter = new GlBankConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
      
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateFrom", dateFrom);
        hashMap.put("dateTo", dateTo);
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("reportNameText", userDDs.get("SAFE_TRANSFER_REPORT"));
        hashMap.put("recipientText", userDDs.get("SAVE_NUM"));
        hashMap.put("transactionTypeText", userDDs.get("TRANS_TYPE"));
        hashMap.put("importedText", userDDs.get("IMPORT"));
        hashMap.put("exportedText", userDDs.get("EXPORT"));
        hashMap.put("statementText", userDDs.get("STATEMENT"));
        hashMap.put("supplySourceText", userDDs.get("FUNDING_SOURCE"));
        hashMap.put("bankNameText", userDDs.get("SAFE_NAME"));
        hashMap.put("bankNameValue", glBank.getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (bankTransactionReportEntityList != null && !bankTransactionReportEntityList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "glBankTransactionReport.jasper", bankTransactionReportEntityList, "pdf");
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
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
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
     * @return the bankTransactionViewList
     */
    public List<BankTransactionView> getBankTransactionViewList() {
        return bankTransactionViewList;
    }

    /**
     * @param bankTransactionViewList the bankTransactionViewList to set
     */
    public void setBankTransactionViewList(List<BankTransactionView> bankTransactionViewList) {
        this.bankTransactionViewList = bankTransactionViewList;
    }

    /**
     * @return the bankTransactionReportEntityList
     */
    public List<GlBankTransactionReportEntity> getBankTransactionReportEntityList() {
        return bankTransactionReportEntityList;
    }

    /**
     * @param bankTransactionReportEntityList the
     * bankTransactionReportEntityList to set
     */
    public void setBankTransactionReportEntityList(List<GlBankTransactionReportEntity> bankTransactionReportEntityList) {
        this.bankTransactionReportEntityList = bankTransactionReportEntityList;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the bankTransactionViewLocalList
     */
    public List<GlBank> getBankTransactionViewLocalList() {
        return bankTransactionViewLocalList;
    }

    /**
     * @param bankTransactionViewLocalList the bankTransactionViewLocalList to
     * set
     */
    public void setBankTransactionViewLocalList(List<GlBank> bankTransactionViewLocalList) {
        this.bankTransactionViewLocalList = bankTransactionViewLocalList;
    }

    /**
     * @return the glBank
     */
    public GlBank getGlBank() {
        return glBank;
    }

    /**
     * @param glBank the glBank to set
     */
    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

}
