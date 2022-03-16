/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.isag.AutoComplete1;
import com.isag.isag.BaseReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
@Named(value = "periodAccountStatementMB")
@ViewScoped
public class PeriodAccountStatementMB extends BaseReportBean {

    private SubAccountSummarySearchBean accountSummarySearchBean;
    private List<GlBankTransactionDTO> glBankTransactionDTOList;

    @EJB
    private GlBankTransactionService bankTransactionService;

    @Override
    @PostConstruct
    public void load() {
        reset();
    }

    @Override
    public void reset() {
        setGlBankTransactionDTOList(new ArrayList<GlBankTransactionDTO>());
        setAccountSummarySearchBean(new SubAccountSummarySearchBean());
        getAccountSummarySearchBean().setBranchId(getUserlogin().getBranchId().getId());
    }

    @Override
    public void search() {
        setGlBankTransactionDTOList(bankTransactionService.getALLGlBankTransactionIsClosedPeriod(getAccountSummarySearchBean()));
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        reportManagement(hashMap);
        hashMap.put("reportNameText", "كشف حساب فتره");
        hashMap.put("fromDocumentSerialText", "من مستند");
        hashMap.put("toDocumentSerialText", "الي مستند");
        hashMap.put("fromDateText", "من تاريخ");
        hashMap.put("toDateText", "الي تاريخ");
        hashMap.put("fromDocumentSerialValue", getAccountSummarySearchBean().getFromDocumentSerial());
        hashMap.put("toDocumentSerialValue", getAccountSummarySearchBean().getToDocumentSerial());
        hashMap.put("fromDateValue", getAccountSummarySearchBean().getDateFrom());
        hashMap.put("toDateValue", getAccountSummarySearchBean().getDateTo());

        hashMap.put("bankText", "الخزينة");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("bankSerialText", "رقم السند");
        hashMap.put("documentSerialText", "رقم الكشف");
        hashMap.put("transactionText", "نوع الحركة");
        hashMap.put("transferToText", "مناولة");
        hashMap.put("remarkText", "البيان");
        hashMap.put("debitText", "مدين");
        hashMap.put("creditText", "دائن");
        hashMap.put("balanceText", "رصيد الفتره");
        hashMap.put("openingBalanceText", "رصيد البدايه");

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (getGlBankTransactionDTOList() != null && !getGlBankTransactionDTOList().isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "periodAccountStatement.jasper", getGlBankTransactionDTOList(), "pdf");
        } else {
            errorMessage(" لا يوجد نتائج للطباعه");
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
     * @return the accountSummarySearchBean
     */
    public SubAccountSummarySearchBean getAccountSummarySearchBean() {
        if (accountSummarySearchBean == null) {
            accountSummarySearchBean = new SubAccountSummarySearchBean();
        }
        return accountSummarySearchBean;
    }

    /**
     * @param accountSummarySearchBean the accountSummarySearchBean to set
     */
    public void setAccountSummarySearchBean(SubAccountSummarySearchBean accountSummarySearchBean) {
        this.accountSummarySearchBean = accountSummarySearchBean;
    }

    /**
     * @return the glBankTransactionDTOList
     */
    public List<GlBankTransactionDTO> getGlBankTransactionDTOList() {
        return glBankTransactionDTOList;
    }

    /**
     * @param glBankTransactionDTOList the glBankTransactionDTOList to set
     */
    public void setGlBankTransactionDTOList(List<GlBankTransactionDTO> glBankTransactionDTOList) {
        this.glBankTransactionDTOList = glBankTransactionDTOList;
    }

}
