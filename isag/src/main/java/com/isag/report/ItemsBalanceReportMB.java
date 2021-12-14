/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.SymbolService;
import com.isag.businessservice.reports.ItemsBalanceViewService;
import com.isag.businessservice.reports.ItemsBalanceViewService;
import com.isag.businessservice.reports.searchBean.CommonSearchBean;
import com.isag.converter.GlBankConverter;
import com.isag.converter.SymbolConverter;
import com.isag.entity.GlBank;
import com.isag.entity.Symbol;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.report.entity.BankBalanceReportBean;
import com.isag.views.ItemsBalanceView;
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
@Named(value = "itemsBalanceReportMB")
@ViewScoped
public class ItemsBalanceReportMB extends BaseGlAccountReportBean {

    private Date dateFrom;
    private Date dateTo;
    private GlBank glBankFrom;
    private GlBank glBankTo;
    private List<GlBank> bankTransactionViewLocalList;
    private List<Symbol> glItemsList;
    private GlBankConverter glBankConverter;
    private List<ItemsBalanceView> bankBalanceViewList;
    private Map<Integer, BankBalanceReportBean> balanceViewMap = new HashMap<>();
    private List<BankBalanceReportBean> bankBalanceReportBeanList;
    private Integer generateKeyMap;
    private CommonSearchBean commonSearchBean;
    private Map<Integer, GlBank> glBankMap = new HashMap<>();
    private Map<Integer, Symbol> symbolMap = new HashMap<>();
    private Set<Integer> serchedBanksSet;
    private StringBuilder bankBuilder;
    private Symbol itemFrom;
    private Symbol itemTo;
    private List<Symbol> itemsFrom;
    private List<Symbol> itemsTo;
    private SymbolConverter itemConverterFrom;
    private SymbolConverter itemConverterTo;

    @EJB
    GlBankService glBankService;
    @EJB
    ItemsBalanceViewService itemsBalanceViewService;
    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    SymbolService symbolService;

    @PostConstruct
    public void init() {
        if (getGlYearSelection() != null) {
            load();
            reset();
            getItemsFrom();
            getItemsTo();
        } else {
            redirectFinancailYearPage();
        }
//        prepareOpeneingBlancesValues(commonSearchBean);
    }

    @Override
    public void reset() {
        bankTransactionViewLocalList = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());
        setGlItemsList(symbolService.getGLItemsByCompanyId(getUserData().getCompany().getId(),null));

        glBankConverter = new GlBankConverter(bankTransactionViewLocalList);
        commonSearchBean = new CommonSearchBean();
        bankBuilder = new StringBuilder();
        resetDateFrom();
        resetDateTo();
        loadGlBankMap();
        getItemFrom();
        getItemTo();
    }

    public void loadGlBankMap() {
        if (bankTransactionViewLocalList != null && !bankTransactionViewLocalList.isEmpty()) {
            for (GlBank bank : bankTransactionViewLocalList) {
                glBankMap.put(bank.getId(), bank);
            }
        }
        if (glItemsList != null && !glItemsList.isEmpty()) {
            for (Symbol symbol : glItemsList) {
                symbolMap.put(symbol.getId(), symbol);
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
        bankBalanceViewList = itemsBalanceViewService.getAllBankBalancesTransactionsByBankId(getUserData().getUserBranch().getId(), getGlYearSelection(), dateTo, bankBuilder, itemFrom.getSerial(), itemTo.getSerial());

//        for (ItemsBalanceView bbv : bankBalanceViewList) {
//            System.out.println(bbv.getGlValue());
//        }
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {
            for (ItemsBalanceView balanceReportView : bankBalanceViewList) {
                BankBalanceReportBean balanceReportBean;
//                if (balanceReportView.getBankIdTo() != null && serchedBanksSet.contains(balanceReportView.getBankIdTo())) {
                if (balanceReportView.getItemSerial() != null && serchedBanksSet.contains(balanceReportView.getItemSerial())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!getBalanceViewMap().containsKey(balanceReportView.getItemSerial())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial().toString());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setItemName(balanceReportView.getItemName());
                        balanceReportBean.setItemSerial(balanceReportView.getItemSerial());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setPreviousBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(BigDecimal.ZERO);
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial().toString());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setItemName(balanceReportView.getItemName());
                        balanceReportBean.setItemSerial(balanceReportView.getItemSerial());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBean = getBalanceViewMap().get(balanceReportView.getItemSerial());
                        if (balanceReportView.getDate().before(dateFrom)) {
                            balanceReportBean.setPreviousBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getPreviousBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBean.getPreviousBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getDebit().add(balanceReportView.getValueLocal())).setScale(2, RoundingMode.UP) : balanceReportBean.getDebit().setScale(2, RoundingMode.UP));
                        }
                    }

                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdTo(), balanceReportBean);
                    if (balanceReportBean.getCredit().compareTo(BigDecimal.ZERO) != 0 || balanceReportBean.getDebit().compareTo(BigDecimal.ZERO) != 0) {
                        getBalanceViewMap().put(balanceReportView.getItemSerial(), balanceReportBean);
                    }
                }

//                if (balanceReportView.getItemSerial() != null && serchedBanksSet.contains(balanceReportView.getItemSerial())) {
//                    balanceReportBean = new BankBalanceReportBean();
//                    if (!getBalanceViewMap().containsKey(balanceReportView.getBankIdfrom())) {
//                        balanceReportBean.setBankId(balanceReportView.getBankIdfrom());
//                        balanceReportBean.setSerial(balanceReportView.getSerial().toString());
//                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
//                        if (balanceReportView.getDate().before(dateFrom)) {
//                            balanceReportBean.setPreviousBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
//                        } else {
//                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
//                            balanceReportBean.setDebit(BigDecimal.ZERO);
//                        }
//                    } else {
//                        balanceReportBean.setBankId(balanceReportView.getBankIdfrom());
//                        balanceReportBean.setSerial(balanceReportView.getSerial().toString());
//                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
//                        balanceReportBean = getBalanceViewMap().get(balanceReportView.getBankIdfrom());
//                        if (balanceReportView.getDate().before(dateFrom)) {
//                            balanceReportBean.setPreviousBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getPreviousBalance().setScale(2, RoundingMode.UP) : (balanceReportBean.getPreviousBalance().subtract(balanceReportView.getValueLocal())).setScale(2, RoundingMode.UP));
//                        } else {
//                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getCredit().add(balanceReportView.getValueLocal()).setScale(2, RoundingMode.UP)) : balanceReportBean.getCredit().setScale(2, RoundingMode.UP));
//                        }
//                    }
//                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
//                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdFrom(), balanceReportBean);
//                    getBalanceViewMap().put(balanceReportView.getBankIdfrom(), balanceReportBean);
//                }
            }
        }

        bankBalanceReportBeanList = new ArrayList<>(balanceViewMap.values());

    }

    public void fillBuilders() {
        bankBuilder = new StringBuilder();
        serchedBanksSet = new HashSet<>();
        if (bankTransactionViewLocalList != null && !bankTransactionViewLocalList.isEmpty()) {
            for (GlBank bank : bankTransactionViewLocalList) {
                if (bank.getId() >= glBankFrom.getId() && bank.getId() <= glBankTo.getId()) {
                    if (bankBuilder != null && bankBuilder.length() == 0) {
                        bankBuilder.append(bank.getId());
                    } else {
                        bankBuilder.append(",");
                        bankBuilder.append(bank.getId());
                    }
                }
            }
        }

        if (glItemsList != null && !glItemsList.isEmpty()) {
            for (Symbol symbol : glItemsList) {
                if (symbol.getSerial() >= itemFrom.getSerial() && symbol.getSerial() <= itemTo.getSerial()) {
                    if (bankBuilder != null && bankBuilder.length() == 0) {
                        serchedBanksSet.add(symbol.getSerial());
                    } else {
//                        bankBuilder.append(",");
                        serchedBanksSet.add(symbol.getSerial());
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
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : new Date());

    }

    @Override
    public void resetDateTo() {
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : new Date());

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
        hashMap.put("reportNameText", "أرصدة البنود");
        hashMap.put("recipientText", userDDs.get("SAVE_NUM"));
        hashMap.put("bankNameText", userDDs.get("SAFE_NAME"));
        hashMap.put("importedText", userDDs.get("IMPORT"));
        hashMap.put("exportedText", userDDs.get("EXPORT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("itemNameText", "البنــد");
        hashMap.put("itemSerialText", "كود البند");
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
            fillReport(prepareReport(), getUserData().getReportPath() + "itemsBalanceReport.jasper", bankBalanceReportBeanList, "pdf");
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
    public List<ItemsBalanceView> getItemsBalanceViewList() {
        return bankBalanceViewList;
    }

    /**
     * @param bankBalanceViewList the bankBalanceViewList to set
     */
    public void setItemsBalanceViewList(List<ItemsBalanceView> bankBalanceViewList) {
        this.bankBalanceViewList = bankBalanceViewList;
    }

    /**
     * @return the glBankFrom
     */
    public GlBank getGlBankFrom() {
        return glBankFrom;
    }

    /**
     * @param glBankFrom the glBankFrom to set
     */
    public void setGlBankFrom(GlBank glBankFrom) {
        this.glBankFrom = glBankFrom;
    }

    /**
     * @return the glBankTo
     */
    public GlBank getGlBankTo() {
        return glBankTo;
    }

    /**
     * @param glBankTo the glBankTo to set
     */
    public void setGlBankTo(GlBank glBankTo) {
        this.glBankTo = glBankTo;
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

    /**
     * @return the glBankMap
     */
    public Map<Integer, GlBank> getGlBankMap() {
        return glBankMap;
    }

    /**
     * @param glBankMap the glBankMap to set
     */
    public void setGlBankMap(Map<Integer, GlBank> glBankMap) {
        this.glBankMap = glBankMap;
    }

    /**
     * @return the itemConverterFrom
     */
    public SymbolConverter getItemConverterFrom() {
        return itemConverterFrom;
    }

    /**
     * @param itemConverterFrom the itemConverterFrom to set
     */
    public void setItemConverterFrom(SymbolConverter itemConverterFrom) {
        this.itemConverterFrom = itemConverterFrom;
    }

    /**
     * @return the itemsFrom
     */
    public Symbol getItemFrom() {
        return itemFrom;
    }

    /**
     * @param itemsFrom the itemsFrom to set
     */
    public void setItemFrom(Symbol itemFrom) {
        this.itemFrom = itemFrom;
    }

    /**
     * @return the itemsTo
     */
    public Symbol getItemTo() {
        return itemTo;
    }

    /**
     * @param itemsTo the itemsTo to set
     */
    public void setItemTo(Symbol itemTo) {
        this.itemTo = itemTo;
    }

    /**
     * @return the itemsFrom
     */
    public List<Symbol> getItemsFrom() {
        if (itemsFrom == null || itemsFrom.isEmpty()) {
            itemsFrom = symbolService.getGLItemsByCompanyId(getUserData().getCompany().getId(),null);
            setItemConverterFrom(new SymbolConverter(itemsFrom));
        }
        return itemsFrom;
    }

    /**
     * @param itemsFrom the itemsFrom to set
     */
    public void setItemsFrom(List<Symbol> itemsFrom) {
        this.itemsFrom = itemsFrom;
    }

    /**
     * @return the itemsTo
     */
    public List<Symbol> getItemsTo() {
        if (itemsTo == null || itemsTo.isEmpty()) {
            itemsTo = symbolService.getGLItemsByCompanyId(getUserData().getCompany().getId(),null);
            setItemConverterTo(new SymbolConverter(itemsTo));
        }
        return itemsTo;
    }

    /**
     * @param itemsTo the itemsTo to set
     */
    public void setItemsTo(List<Symbol> itemsTo) {
        this.itemsTo = itemsTo;
    }

    /**
     * @return the itemConverterTo
     */
    public SymbolConverter getItemConverterTo() {
        return itemConverterTo;
    }

    /**
     * @param itemConverterTo the itemConverterTo to set
     */
    public void setItemConverterTo(SymbolConverter itemConverterTo) {
        this.itemConverterTo = itemConverterTo;
    }

    /**
     * @return the glItemsList
     */
    public List<Symbol> getGlItemsList() {
        return glItemsList;
    }

    /**
     * @param glItemsList the glItemsList to set
     */
    public void setGlItemsList(List<Symbol> glItemsList) {
        this.glItemsList = glItemsList;
    }

}
