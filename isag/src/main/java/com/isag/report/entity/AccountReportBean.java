/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.reports.searchBean.GlAccountReportSearchBean;
import com.isag.converter.GlaccountConverter;
import com.isag.entity.GlAccount;
import com.isag.isag.BaseGlAccountReportBean;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import javax.faces.event.ActionEvent;

public class AccountReportBean extends BaseGlAccountReportBean {

    @EJB
    private GlAccountService accountService;

    private List<GlAccount> accounts;
    private List<GlAccountReport> jasperAccounts;
    private Integer accNum;
    private String accNam;
    private JasperPrint jasperPrint;
    private GlAccountReportSearchBean glAccountReportSearchBean;

    private GlAccountReport newItemReport;
    private GlaccountConverter accountConverter;

    private List<GlAccountReport> glAccountReportList = new ArrayList<>();
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private String valueOfSearchByName;

    public AccountReportBean() {
    }

    @PostConstruct
    public void init() {
        reset();
        load();
        accountConverter = new GlaccountConverter(getGlAccountList());

    }

    @Override
    public void search() {
        valueOfSearchByName = glAccountReportSearchBean.getAccountName();
        glAccountReportSearchBean.setBranchId(getUserData().getUserBranch().getId());
        Map<String, String> userDDs = getUserData().getUserDDs();
        TreeMap<Integer, GlAccount> accountMap = new TreeMap<Integer, GlAccount>();

        TreeMap<Integer, GlAccountReport> reportMap = new TreeMap<Integer, GlAccountReport>();

        glAccountReportSearchBean.setAccountNumberFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        glAccountReportSearchBean.setAccountNumberTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        accounts = accountService.getAllGlAccounts(glAccountReportSearchBean);
        jasperAccounts = new ArrayList();
        for (GlAccount account : accounts) {
            newItemReport = new GlAccountReport();
            newItemReport.setAccNumber(account.getAccNumber() != null ? account.getAccNumber() : null);
            newItemReport.setParentNumber(account.getParentAcc() == null ? null : account.getParentAcc().getAccNumber());
            newItemReport.setLgroup(userDDs.get(account.getAccGroup().toString()) != null ? userDDs.get(account.getAccGroup().toString()) : null);
            newItemReport.setLlevel(account.getLevelAcc() != null ? account.getLevelAcc() : null);
            StringBuilder appendSpaces = new StringBuilder();
            if (account.getLevelAcc() != null) {
                for (Integer i = 0; i < account.getLevelAcc(); i++) {
                    appendSpaces.append("*");
                }
                appendSpaces.append(account.getName() != null ? account.getName() : null);
            }
            newItemReport.setLname(appendSpaces.toString());
            if (account.getStatus() == true) {
                newItemReport.setStatus("نشط");
            } else {
                newItemReport.setStatus("غير نشط");
            }

            if (account.getType() != null && account.getType() == 0) {
                newItemReport.setType("رئيسي");
            } else if (account.getType() != null && account.getType() == 1) {
                newItemReport.setType("فرعي");
            }

            glAccountReportList.add(newItemReport);

            accountMap.put(account.getAccNumber(), account);
            reportMap.put(newItemReport.getAccNumber(), newItemReport);

            jasperAccounts.add(newItemReport);
        }
        accounts = new ArrayList<GlAccount>(accountMap.values());

        glAccountReportList = new ArrayList<GlAccountReport>(reportMap.values());
    }

    @Override
    public void reset() {
        glAccountReportSearchBean = new GlAccountReportSearchBean();
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;
    }

    /* public void filter() {
     if (accNam != null && !accNam.isEmpty() && accNum != null && !accNum.equals("")) {
     accounts = accountService.getAccountByNameAndNumber(accNum, accNam);
     } else if (accNam != null && !accNam.isEmpty()) {
     accounts = accountService.getAccountByName(accNam);
     } else if (accNum != null && !accNum.equals("")) {
     accounts = accountService.getAccountNumber(accNum);
     } else {
     accounts = accountService.getAllGlAccount();
     }
     */
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

    public void PDFf() throws JRException, IOException {

        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/account.jasper");
        File file = new File(reportPath);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(jasperAccounts, true);
        JasperPrint input = JasperFillManager.fillReport(file.getPath(), new HashMap(), beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=accountreport.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(input, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void Html() throws JRException, IOException {

        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/account.jasper");
        File file = new File(reportPath);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(jasperAccounts, true);
        JasperPrint input = JasperFillManager.fillReport(file.getPath(), new HashMap(), beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=accountreport.html");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(servletOutputStream));
        exporter.setExporterInput(new SimpleExporterInput(input));
        exporter.exportReport();
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void print(ActionEvent actionEvent) throws JRException, IOException {

        String x = "3mr";
        HashMap hashMap = new HashMap();
        hashMap.put("name1", x);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(jasperAccounts);
        jasperPrint = JasperFillManager.fillReport("D:\\Projects\\accountss.jasper", hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

    }

    public List<GlAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<GlAccount> accounts) {
        this.accounts = accounts;
    }

    public List<GlAccountReport> getJasperAccounts() {
        return jasperAccounts;
    }

    public void setJasperAccounts(List<GlAccountReport> jasperAccounts) {
        this.jasperAccounts = jasperAccounts;
    }

    public Integer getAccNum() {
        return accNum;
    }

    public void setAccNum(Integer accNum) {
        this.accNum = accNum;
    }

    public String getAccNam() {
        return accNam;
    }

    public void setAccNam(String accNam) {
        this.accNam = accNam;
    }

    public GlAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(GlAccountService accountService) {
        this.accountService = accountService;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public GlAccountReportSearchBean getGlAccountReportSearchBean() {
        return glAccountReportSearchBean;
    }

    public void setGlAccountReportSearchBean(GlAccountReportSearchBean glAccountReportSearchBean) {
        this.glAccountReportSearchBean = glAccountReportSearchBean;
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        
        hashMap.put("accountNumberFrom", glAccountReportSearchBean.getAccountNumberFrom());
        hashMap.put("accountNumberTo", glAccountReportSearchBean.getAccountNumberTo());
        hashMap.put("levelFrom", glAccountReportSearchBean.getLevelFrom());
        hashMap.put("levelTo", glAccountReportSearchBean.getLevelTo());
        hashMap.put("accountReportText", "تقرير دليل الحسابات");
        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
        hashMap.put("levelFromText", userDDs.get("LEVEL_FROM"));
        hashMap.put("levelToText", userDDs.get("LEVEL_TO"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("mainAccountText", userDDs.get("MAJOR"));
        hashMap.put("accountFroupText", userDDs.get("ACCOUNT_GROUP"));
        hashMap.put("accountTypeText", userDDs.get("ACCOUNT_TYPE"));
        hashMap.put("accountStatusText", userDDs.get("ACCOUNT_STATUS"));
        hashMap.put("searchByNameText", ("البحث بالاسم"));
        if (valueOfSearchByName.length() == 0) {
            valueOfSearchByName = null;
        }
        hashMap.put("accountNameValue", valueOfSearchByName);
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (glAccountReportList != null && !glAccountReportList.isEmpty()) {
            String reportPath = getUserData().getReportPath();
            fillReport(prepareReport(), reportPath + "accountreport.jasper", glAccountReportList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
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

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GlAccountReport getNewItemReport() {
        return newItemReport;
    }

    public void setNewItemReport(GlAccountReport newItemReport) {
        this.newItemReport = newItemReport;
    }

    public List<GlAccountReport> getGlAccountReportList() {
        return glAccountReportList;
    }

    public void setGlAccountReportList(List<GlAccountReport> glAccountReportList) {
        this.glAccountReportList = glAccountReportList;
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
     * @return the valueOfSearchByName
     */
    public String getValueOfSearchByName() {
        return valueOfSearchByName;
    }

    /**
     * @param valueOfSearchByName the valueOfSearchByName to set
     */
    public void setValueOfSearchByName(String valueOfSearchByName) {
        this.valueOfSearchByName = valueOfSearchByName;
    }

}
