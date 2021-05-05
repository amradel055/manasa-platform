/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.BankBalanceViewService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.converter.GlBankConverter;
import com.toby.converter.GlBankDTOConverter;
import com.toby.dto.GlBankDTO;
import com.toby.entity.GlBank;
import com.toby.report.entity.BankBalanceReportBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.BankBalanceView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Named(value = "bankBalanceReportMB")
@ViewScoped
public class BankBalanceReportMB extends BaseGlAccountReportBean {

    /**
     * @return the glBankMap
     */
    public Map<Integer, GlBankDTO> getGlBankMap() {
        return glBankMap;
    }

    /**
     * @param glBankMap the glBankMap to set
     */
    public void setGlBankMap(Map<Integer, GlBankDTO> glBankMap) {
        this.glBankMap = glBankMap;
    }

    private Date dateFrom;
    private Date dateTo;
    private GlBankDTO glBankFrom;
    private GlBankDTO glBankTo;
    private List<GlBankDTO> bankTransactionViewLocalList;
    private GlBankDTOConverter glBankConverter;
    private List<BankBalanceView> bankBalanceViewList;
    private Map<Integer, BankBalanceReportBean> balanceViewMap = new HashMap<>();
    private List<BankBalanceReportBean> bankBalanceReportBeanList;
    private Integer generateKeyMap;
    private CommonSearchBean commonSearchBean;
    private Map<Integer, GlBankDTO> glBankMap = new HashMap<>();
    private Set<Integer> serchedBanksSet;
    private StringBuilder bankBuilder;
    @EJB
    GlBankService glBankService;
    @EJB
    BankBalanceViewService bankBalanceViewService;
    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    @PostConstruct
    public void init() {
        if (getGlYearSelection() != null) {
            load();
            reset();
        } else {
            redirectFinancailYearPage();
        }
        prepareOpeneingBlancesValues(commonSearchBean);
    }

    public void convertDateFrom() {
        getCommonSearchBean().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getCommonSearchBean().setDateTo(findDateFromHijriTo());
    }

    @Override
    public void reset() {
//        bankTransactionViewLocalList = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());
        bankTransactionViewLocalList = getUserData().getGlBankDTOList();
        glBankConverter = new GlBankDTOConverter(bankTransactionViewLocalList);
        commonSearchBean = new CommonSearchBean();
        bankBuilder = new StringBuilder();
        resetDateFrom();
        resetDateTo();
        loadGlBankMap();
    }

    public void loadGlBankMap() {
        if (bankTransactionViewLocalList != null && !bankTransactionViewLocalList.isEmpty()) {
            for (GlBankDTO bank : bankTransactionViewLocalList) {
                glBankMap.put(bank.getId(), bank);
            }
        }
    }

    @Override
    public void search() {
        fillBuilders();
        bankBalanceViewList = new ArrayList<>();
        balanceViewMap = new HashMap<>();
        bankBalanceReportBeanList = new ArrayList<>();
        dateFrom = commonSearchBean.getDateFrom() != null ? commonSearchBean.getDateFrom() : getGlYearSelection().getDateFrom();
        dateTo = commonSearchBean.getDateTo() != null ? commonSearchBean.getDateTo() : getGlYearSelection().getDateTo();
        bankBalanceViewList = bankBalanceViewService.getAllBankBalancesTransactionsByBankId(getUserData().getUserBranch().getId(), getGlYearSelection(), dateTo, bankBuilder);

//        for (BankBalanceView bbv : bankBalanceViewList) {
//            System.out.println(bbv.getGlValue());
//        }
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {
            for (BankBalanceView balanceReportView : bankBalanceViewList) {
                BankBalanceReportBean balanceReportBean;
                if (balanceReportView.getBankIdTo() != null && serchedBanksSet.contains(balanceReportView.getBankIdTo())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!getBalanceViewMap().containsKey(balanceReportView.getBankIdTo())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setBankToBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(BigDecimal.ZERO);
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBean = getBalanceViewMap().get(balanceReportView.getBankIdTo());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setBankToBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getBankToBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBean.getBankToBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getDebit().add(balanceReportView.getValueLocal())).setScale(2, RoundingMode.UP) : balanceReportBean.getDebit().setScale(2, RoundingMode.UP));
                        }
                    }

                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdTo(), balanceReportBean);
                    getBalanceViewMap().put(balanceReportView.getBankIdTo(), balanceReportBean);

                }

                if (balanceReportView.getBankIdFrom() != null && serchedBanksSet.contains(balanceReportView.getBankIdFrom())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!getBalanceViewMap().containsKey(balanceReportView.getBankIdFrom())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setBankFromBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                            balanceReportBean.setDebit(BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        balanceReportBean = getBalanceViewMap().get(balanceReportView.getBankIdFrom());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setBankFromBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getBankFromBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));

                        } else {
                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getCredit().add(balanceReportView.getValueLocal()).setScale(2, RoundingMode.UP)) : balanceReportBean.getCredit().setScale(2, RoundingMode.UP));
                        }
                    }
                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdFrom(), balanceReportBean);
                    getBalanceViewMap().put(balanceReportView.getBankIdFrom(), balanceReportBean);

                }
            }
        }
        bankBalanceReportBeanList = new ArrayList<>(balanceViewMap.values());

    }

    public void fillBuilders() {
        bankBuilder = new StringBuilder();
        serchedBanksSet = new HashSet<>();
        if (bankTransactionViewLocalList != null && !bankTransactionViewLocalList.isEmpty()) {
            for (GlBankDTO bank : bankTransactionViewLocalList) {
                if (bank.getId() >= glBankFrom.getId() && bank.getId() <= glBankTo.getId()) {
                    if (bankBuilder != null && bankBuilder.length() == 0) {
                        bankBuilder.append(bank.getId());
                        serchedBanksSet.add(bank.getId());
                    } else {
                        bankBuilder.append(",");
                        bankBuilder.append(bank.getId());
                        serchedBanksSet.add(bank.getId());
                    }
                }
            }
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(commonSearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(commonSearchBean.getDateTo())) {
                resetDateTo();
            }
        }
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
//        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : new Date());
        commonSearchBean.setDateFrom(new Date());

    }

    @Override
    public void resetDateTo() {
//        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : new Date());
        commonSearchBean.setDateTo(new Date());

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
        hashMap.put("dateFrom", dateFrom);
        hashMap.put("dateTo", dateTo);
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
        if (bankBalanceReportBeanList != null && !bankBalanceReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "banksBalanceReport.jasper", bankBalanceReportBeanList, "pdf");
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
     * @return the bankBalanceViewList
     */
    public List<BankBalanceView> getBankBalanceViewList() {
        return bankBalanceViewList;
    }

    /**
     * @param bankBalanceViewList the bankBalanceViewList to set
     */
    public void setBankBalanceViewList(List<BankBalanceView> bankBalanceViewList) {
        this.bankBalanceViewList = bankBalanceViewList;
    }

    /**
     * @return the glBankFrom
     */
    public GlBankDTO getGlBankFrom() {
        return glBankFrom;
    }

    /**
     * @param glBankFrom the glBankFrom to set
     */
    public void setGlBankFrom(GlBankDTO glBankFrom) {
        this.glBankFrom = glBankFrom;
    }

    /**
     * @return the glBankTo
     */
    public GlBankDTO getGlBankTo() {
        return glBankTo;
    }

    /**
     * @param glBankTo the glBankTo to set
     */
    public void setGlBankTo(GlBankDTO glBankTo) {
        this.glBankTo = glBankTo;
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
     * @return the balanceViewMap
     */
    public Map<Integer, BankBalanceReportBean> getBalanceViewMap() {
        return balanceViewMap;
    }

    /**
     * @param balanceViewMap the balanceViewMap to set
     */
    public void setBalanceViewMap(Map<Integer, BankBalanceReportBean> balanceViewMap) {
        this.balanceViewMap = balanceViewMap;
    }

    /**
     * @return the generateKeyMap
     */
    public Integer getGenerateKeyMap() {
        return generateKeyMap;
    }

    /**
     * @param generateKeyMap the generateKeyMap to set
     */
    public void setGenerateKeyMap(Integer generateKeyMap) {
        this.generateKeyMap = generateKeyMap;
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

    /**
     * @return the bankBuilder
     */
    public StringBuilder getBankBuilder() {
        return bankBuilder;
    }

    /**
     * @param bankBuilder the bankBuilder to set
     */
    public void setBankBuilder(StringBuilder bankBuilder) {
        this.bankBuilder = bankBuilder;
    }
}
