/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.shiftingJournals;

import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.GlYearConverter;
import com.toby.converter.SymbolConverter;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GlYear;
import com.toby.entity.Symbol;
import com.toby.toby.UserData;
import com.toby.views.PostRestrictionsView;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.GlAnalyticsAccountsServcie;
import com.toby.businessservice.GlBankTransactionService;
//import com.toby.businessservice.InstContractService;
import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.InvReturnPurchaseService;
import com.toby.businessservice.ShiftingJournalService;
import com.toby.businessservice.report.PostRestrictionsDetailsService;
import com.toby.businessservice.report.postRestrictionsViewService;
import com.toby.entity.CostCenter;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlAnalyticsAccounts;
import com.toby.entity.GlBankTransaction;
//import com.toby.entity.InstContract;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvReturnPurchase;
import com.toby.generaljournal.GeneralJournalBean;
import com.toby.report.entity.PostRestrictionDetailsBean;
import com.toby.views.PostRestrictionsDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ahmed
 */
@Named(value = "shiftingJournalMB_new")
@ViewScoped
public class ShiftingJournalMB_New extends GeneralJournalBean {

    private Symbol symbol;
    private Integer symbolId;
    private List<Symbol> symbolList;
    private UserData userData;
    private SymbolConverter symbolConverter;
    private GeneralJournal generalJournalSelected;
    private List<GeneralJournal> generalJournalHeadList;
    private List<GlYear> glYearList;
    private Integer glYearId;
    private GlYear glYearSelection;
    private Date dateFrom;
    private Date dateTo;
    private GlYearConverter glYearConverter;
    private Integer documentFixedNumber;
    private Integer fillGeneralJournalDetailsFlow;
    private BigDecimal debitFinalSummary;
    private BigDecimal creditFinalSummary;
    private List<GeneralJournalDetails> generalJournalDetailsList;
    private PostRestrictionsView postRestrictionsView;
    private PostRestrictionsView postRestrictionsSelected;
    private PostRestrictionsDetailsView postRestrictionsDetailsView;
    private List<PostRestrictionsView> postRestrictionsViewList;
    private List<PostRestrictionsDetailsView> postRestrictionsDetailsViewList;
    private List<PostRestrictionDetailsBean> postRestrictionDetailsBeanList;
    private PostRestrictionDetailsBean postRestrictionDetailsBean;
    private PostRestrictionDetailsBean postRestrictionsDetailsBeanSelection;
    private Boolean shiftedJournals;
    private CostCenter costCenterShow;
    private GlAdminUnit adminUnitShow;
    private List<Symbol> specialDocumentsTypeList;
    private Boolean shiftValidation;
    private String option; // +getter +setter
    private List<SelectItem> options; // +getter
    private Map<Integer, Symbol> symbolsMapById;
    private Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap;
    private Boolean cancelInstallment;
    private Integer selectedInstallmentId;
    Map<Integer, Symbol> symbolsMap;
    @EJB
    SymbolService symbolService;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private TobyUserYearService tobyUserYearService;
    @EJB
    private postRestrictionsViewService restrictionsViewService;
    @EJB
    private PostRestrictionsDetailsService postRestrictionsDetailsService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GeneraljournalDetailsService detailsService;
    @EJB
    private GlAdminUnitService adminUnitService;
    @EJB
    private CostCenterService costCenterService;
    @EJB
    private SymbolService symbolCodeService;
    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;
//    @EJB
//    private InstContractService instContractService;
    @EJB
    private ShiftingJournalService shiftingJournalService;
    private final static Logger logger = Logger.getLogger(ShiftingJournalMB.class.getName());

    @PostConstruct
    @Override
    public void init() {
        try {
            reset();
            load();
            pushGlYearSelection();
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "init");
        }
    }

    public void reset() {
        try {
            symbolList = new ArrayList<>();
            generalJournalHeadList = new ArrayList<>();
            glYearList = new ArrayList<>();
            postRestrictionsViewList = new ArrayList<>();
            postRestrictionsDetailsViewList = new ArrayList<>();
            postRestrictionDetailsBean = new PostRestrictionDetailsBean();
            postRestrictionDetailsBeanList = new ArrayList<>();
            generalJournalDetailsList = new ArrayList<>();
            generalJournalSelected = new GeneralJournal();
            debitFinalSummary = BigDecimal.ZERO;
            creditFinalSummary = BigDecimal.ZERO;
            fillGeneralJournalDetailsFlow = 0;
            shiftedJournals = Boolean.FALSE;
            postRestrictionsSelected = new PostRestrictionsView();
            specialDocumentsTypeList = new ArrayList<>();
            generalJournalDetailsMap = new HashMap<>();
            symbol = null;
            cancelInstallment = false;
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "reset");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            symbolsMap = new HashMap<>();
            symbolsMapById = new HashMap<>();
            specialDocumentsTypeList = symbolCodeService.getSpecialDocumentsTypes();

            //start to group list
            if (specialDocumentsTypeList != null && !specialDocumentsTypeList.isEmpty()) {
                for (Symbol s : specialDocumentsTypeList) {
                    symbolsMap.put(s.getSerial(), s);
                    symbolsMapById.put(s.getId(), s);
                }
            }
            options = new ArrayList<>();
            SelectItemGroup group1 = new SelectItemGroup("الخزائن");
            group1.setSelectItems(new SelectItem[]{
                new SelectItem(symbolsMap.get(1001).getId(), symbolsMap.get(1001).getName()),
                //                new SelectItem(symbolsMap.get(1002).getId(), symbolsMap.get(1002).getName()),
                //                new SelectItem(symbolsMap.get(1003).getId(), symbolsMap.get(1003).getName()),
                new SelectItem(symbolsMap.get(1004).getId(), symbolsMap.get(1004).getName()),
                //                new SelectItem(symbolsMap.get(1005).getId(), symbolsMap.get(1005).getName()),
                //                new SelectItem(symbolsMap.get(1006).getId(), symbolsMap.get(1006).getName()),
                new SelectItem(symbolsMap.get(1007).getId(), symbolsMap.get(1007).getName()), //                new SelectItem(symbolsMap.get(1008).getId(), symbolsMap.get(1008).getName())
            });
            options.add(group1);

            SelectItemGroup group2 = new SelectItemGroup("المخازن");
            group2.setSelectItems(new SelectItem[]{
                new SelectItem(symbolsMap.get(1009).getId(), symbolsMap.get(1009).getName()),
                new SelectItem(symbolsMap.get(1010).getId(), symbolsMap.get(1010).getName()),
                //                new SelectItem(symbolsMap.get(1011).getId(), symbolsMap.get(1011).getName()),
                new SelectItem(symbolsMap.get(1012).getId(), symbolsMap.get(1012).getName()),
                new SelectItem(symbolsMap.get(1013).getId(), symbolsMap.get(1013).getName()),
                new SelectItem(symbolsMap.get(1015).getId(), symbolsMap.get(1015).getName()),
                new SelectItem(symbolsMap.get(1016).getId(), symbolsMap.get(1016).getName()),
                //                new SelectItem(symbolsMap.get(1017).getId(), symbolsMap.get(1017).getName()),
                new SelectItem(symbolsMap.get(1018).getId(), symbolsMap.get(1018).getName()),
                new SelectItem(symbolsMap.get(1019).getId(), symbolsMap.get(1019).getName()), //                new SelectItem(symbolsMap.get(1020).getId(), symbolsMap.get(1020).getName())
            });
            options.add(group2);

            SelectItemGroup group5 = new SelectItemGroup("الاشعارات");
            group5.setSelectItems(new SelectItem[]{
                new SelectItem(symbolsMap.get(1030).getId(), symbolsMap.get(1030).getName()),
                new SelectItem(symbolsMap.get(1031).getId(), symbolsMap.get(1031).getName()),
                new SelectItem(symbolsMap.get(1032).getId(), symbolsMap.get(1032).getName()),
                new SelectItem(symbolsMap.get(1033).getId(), symbolsMap.get(1033).getName())
            });
            options.add(group5);

            /*for (Map.Entry<Integer, Symbol> entry : symbolsMap.entrySet()) {
             if (entry.getKey() < 1009) {
             group1.setSelectItems(new SelectItem[]{new SelectItem(entry.getValue(), entry.getValue().getName())});
             SelectItem[] x =  group1.getSelectItems();
             options.add(group1);
             } else if (entry.getKey() >= 1009 && entry.getKey() <= 1020) {
             group2.setSelectItems(new SelectItem[]{new SelectItem(entry.getValue(), entry.getValue().getName())});
             options.add(group2);
             } else {
             group3.setSelectItems(new SelectItem[]{new SelectItem(entry.getValue(), entry.getValue().getName())});
             options.add(group3);
             }
             }*/
            // symbolList = symbolService.getAllDocumentsByCompanyId(userData.getCompany().getId());
            // if (symbolList != null && !symbolList.isEmpty()) {
            symbolConverter = new SymbolConverter(specialDocumentsTypeList);
            // }
            // generalJournalHeadList = generaljournalService.getALLGeneralJournalByBranchIdAndYearAndSymbolId(Integer.SIZE, year);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "load");
        }
    }

    public List<Symbol> completeSymbol(String query) {
        try {
            List<Symbol> symbolsList = specialDocumentsTypeList;
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolConverter(symbolsList);
                return symbolsList;
            }
            List<Symbol> filteredSymbols = new ArrayList<>();
            String nameAr;
            String code;
            Symbol symbolFilter;
            for (int i = 0; i < specialDocumentsTypeList.size(); i++) {
                symbolFilter = symbolsList.get(i);
                nameAr = symbolFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSymbols.contains(symbolFilter)) {
                        filteredSymbols.add(symbolFilter);
                    }
                }
            }
            symbolConverter = new SymbolConverter(filteredSymbols);
            return filteredSymbols;
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "completeSymbol");
            return null;
        }
    }

    public void loadDataTableHead() {
        long start = System.nanoTime();

        try {
            postRestrictionsViewList = new ArrayList<>();
            postRestrictionsDetailsViewList = new ArrayList<>();
            postRestrictionDetailsBeanList = new ArrayList<>();
            costCenterShow = new CostCenter();
            adminUnitShow = new GlAdminUnit();
            creditFinalSummary = BigDecimal.ZERO;
            debitFinalSummary = BigDecimal.ZERO;
            symbol = symbolsMapById.get(symbolId);
            documentFixedNumber = symbol != null ? symbol.getSerial() : null;
            setSelectedDocumentsType(symbol != null ? symbol.getId() : null);
            if (shiftedJournals) {
                postRestrictionsViewList = restrictionsViewService.getBankTransactions(userData.getUserBranch().getId(), dateFrom, dateTo, documentFixedNumber, 1);
            } else {
                postRestrictionsViewList = restrictionsViewService.getBankTransactions(userData.getUserBranch().getId(), dateFrom, dateTo, documentFixedNumber, 0);
            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "loadDataTableHead");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "loadDataTableHead takes {0} seconds", (end - start) / 1000000000);
    }

    @Override
    public void onRowSelect(SelectEvent e) {
        long start = System.nanoTime();
        try {
            postRestrictionDetailsBeanList = new ArrayList<>();
            postRestrictionsView = (PostRestrictionsView) e.getObject();
            LoadDataTableDetails(postRestrictionsView);
            //fillSummary();
        } catch (Exception ex) {
            saveError(ex, "ShiftingJournalMB", "onRowSelect");
        }

        long end = System.nanoTime();
        logger.log(Level.INFO, "onRowSelect takes {0} seconds", (end - start) / 1000000000);
    }

    public void onRowDetailsSelect(SelectEvent e) {
        long start = System.nanoTime();
        try {
            postRestrictionsDetailsBeanSelection = (PostRestrictionDetailsBean) e.getObject();
            costCenterShow = new CostCenter();
            costCenterShow = postRestrictionsDetailsBeanSelection.getCostCenter() != null ? postRestrictionsDetailsBeanSelection.getCostCenter() : null;
            adminUnitShow = new GlAdminUnit();
            adminUnitShow = postRestrictionsDetailsBeanSelection.getAdminUnit() != null ? postRestrictionsDetailsBeanSelection.getAdminUnit() : null;
        } catch (Exception ex) {
            saveError(ex, "ShiftingJournalMB", "onRowDetailsSelect");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "onRowDetailsSelect takes {0} seconds", (end - start) / 1000000000);
    }

    public void LoadDataTableDetails(PostRestrictionsView restrictionsView) {
        long start = System.nanoTime();
        try {
            if (postRestrictionsView != null) {
                if (postRestrictionsView.getScreenCode() < 1009) {
                    if (postRestrictionsView.getScreenCode() < 0) {
                        postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByBankIdDebitAccountAndDate(postRestrictionsView, postRestrictionsView.getDate());
                    } else { // here قبض نقدي 
                        postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByPostRestrictionsId(postRestrictionsView.getBranchId(), postRestrictionsView.getId(), 1);
                    }
                } else if (postRestrictionsView.getScreenCode() >= 1009 && postRestrictionsView.getScreenCode() < 1021) {
                    postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByPostRestrictionsId(postRestrictionsView.getBranchId(), postRestrictionsView.getId(), 2);
                } else if (postRestrictionsView.getScreenCode() >= 1021 && postRestrictionsView.getScreenCode() < 1022) {
                    postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByPostRestrictionsId(postRestrictionsView.getBranchId(), postRestrictionsView.getId(), 3);
                } else if (postRestrictionsView.getScreenCode() >= 1022 && postRestrictionsView.getScreenCode() < 1030) {
                    postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByPostRestrictionsId(postRestrictionsView.getBranchId(), postRestrictionsView.getId(), 4);
                } else if (postRestrictionsView.getScreenCode() >= 1030 && postRestrictionsView.getScreenCode() < 1040) {
                    postRestrictionsDetailsViewList = postRestrictionsDetailsService.getPostRestrictionsDetailsByPostRestrictionsId(postRestrictionsView.getBranchId(), postRestrictionsView.getId(), 5);
                }
                debitFinalSummary = BigDecimal.ZERO;
                creditFinalSummary = BigDecimal.ZERO;
                shiftValidation = true;
                if (postRestrictionsDetailsViewList != null && !postRestrictionsDetailsViewList.isEmpty()) {
                    for (PostRestrictionsDetailsView detailsView : postRestrictionsDetailsViewList) {
                        if (detailsView.getErrorMsg() != null && !detailsView.getErrorMsg().isEmpty()) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), detailsView.getErrorMsg()));
                            shiftValidation = false;
                        }
                        splitDebitAndCreditAccounts(restrictionsView, detailsView, postRestrictionsView.getScreenCode());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "LoadDataTableDetails");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "LoadDataTableDetails takes {0} seconds", (end - start) / 1000000000);
    }

    public void fillFinalSummary(PostRestrictionDetailsBean bean) {
        long start = System.nanoTime();

        try {
            setCreditFinalSummary(getCreditFinalSummary().add(bean.getValueLocalCredit() != null ? bean.getValueLocalCredit() : BigDecimal.ZERO));
            setDebitFinalSummary(getDebitFinalSummary().add(bean.getValueLocalDebit() != null ? bean.getValueLocalDebit() : BigDecimal.ZERO));
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "fillFinalSummary");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "loadDataTableHead takes {0} seconds", (end - start) / 1000000000);
    }

    public void splitDebitAndCreditAccounts(PostRestrictionsView restrictionsView, PostRestrictionsDetailsView detailsView, Long screenCode) {
        long start = System.nanoTime();

        try {

            Map<String, String> userDDs = getUserData().getUserDDs();

            switch (screenCode.intValue()) {
                case 1001:

                    PostRestrictionDetailsBean bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setRemarks(detailsView.getRemarks());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1002:

                    bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setRemarks(detailsView.getRemarks());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1003:

                    bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setRemarks(detailsView.getRemarks());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1004: // قبض نقدي

                    bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit((detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP)).subtract(detailsView.getSecondValue()));
                    bean.setValueLocalDebit((detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP)).subtract(detailsView.getSecondValue()));
                    bean.setRemarks(restrictionsView.getRemark());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (detailsView.getSecondValue().compareTo(BigDecimal.ZERO) == 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getSecondAccNumberDebit());
                        lastBean.setAccName(detailsView.getSecondAccNameDebit());
                        lastBean.setValueDebit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalDebit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getSecondAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getSecondAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    if (detailsView.getTaxValue().compareTo(BigDecimal.ZERO) == 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getThirdAccNumberDebit());
                        lastBean.setAccName(detailsView.getThirdAccNameDebit());
                        lastBean.setValueDebit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalDebit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getSecondAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getThirdAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    if (detailsView.getTaxValue().compareTo(BigDecimal.ZERO) == 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getThirdAccNumberDebit());
                        lastBean.setAccName(detailsView.getThirdAccNameDebit());
                        lastBean.setValueCredit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getSecondAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getThirdAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1005:

                    bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setRemarks(restrictionsView.getRemark());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1006:

                    bean = new PostRestrictionDetailsBean();
                    bean.setAccNumber(detailsView.getAccNumberDebit());
                    bean.setAccName(detailsView.getAccNameDebit());
                    bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setRemarks(restrictionsView.getRemark());
                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        lastBean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1007:

                    bean = new PostRestrictionDetailsBean();

                    bean.setAccNumber(detailsView.getAccNumberCredit());
                    bean.setAccName(detailsView.getAccNameCredit());
                    bean.setValueDebit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));

                    bean.setRemarks(restrictionsView.getRemark());
                    if (detailsView.getAccountCreditId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountCreditId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberDebit());
                        lastBean.setAccName(detailsView.getAccNameDebit());
                        lastBean.setValueCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));

                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }

                    break;

                case 1008:

                    bean = new PostRestrictionDetailsBean();

                    bean.setAccNumber(detailsView.getAccNumberCredit());
                    bean.setAccName(detailsView.getAccNameCredit());
                    bean.setValueDebit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    bean.setValueLocalDebit(postRestrictionsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));

                    bean.setRemarks(restrictionsView.getRemark());
                    if (detailsView.getAccountCreditId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountCreditId());
                        bean.setGlAccount(glAccount);
                    }
                    fillCostCenterAndAdminUnit(detailsView, bean);
                    postRestrictionDetailsBeanList.add(bean);
                    fillFinalSummary(bean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(bean);
                    }

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == postRestrictionsDetailsViewList.size() - 1) {
                        PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                        lastBean.setAccNumber(detailsView.getAccNumberDebit());
                        lastBean.setAccName(detailsView.getAccNameDebit());
                        lastBean.setValueCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));

                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }
                    break;

                case 1012:
                    SalesCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1013:
                    SalesCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1009:

                    PurchaseCode(detailsView, restrictionsView, userDDs);

                    break;

                case 1010:

                    PurchaseCode(detailsView, restrictionsView, userDDs);

                    break;

                case 1011:

                    PurchaseCode(detailsView, restrictionsView, userDDs);
                    break;

                case 1015:

                    SalesCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1016:

                    SalesCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1018:
                    PurchaseCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1019:
                    PurchaseCode(detailsView, restrictionsView, userDDs);
                    break;
                case 1021:
                    if (!cancelInstallment) {
                        installmentCode(detailsView, restrictionsView, userDDs);
                    } else {
                        cancelInstallmentCode(detailsView, restrictionsView, userDDs);
                    }
                    break;

                case -1000:

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == 0) {
                        bean = new PostRestrictionDetailsBean();
                        bean.setAccNumber(detailsView.getAccNumberDebit());
                        bean.setAccName(detailsView.getAccNameDebit());
                        bean.setValueDebit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        bean.setValueLocalDebit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        //bean.setRemarks(restrictionsView.getRemark());
                        if (detailsView.getAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountDebitId());
                            bean.setGlAccount(glAccount);
                        }
                        // fillCostCenterAndAdminUnit(detailsView, bean);
                        postRestrictionDetailsBeanList.add(bean);
                        fillFinalSummary(bean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(bean);
                        }
                    }

                    PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
                    lastBean.setAccNumber(detailsView.getAccNumberCredit());
                    lastBean.setAccName(detailsView.getAccNameCredit());
                    lastBean.setValueCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    lastBean.setValueLocalCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    //lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                    if (detailsView.getAccountCreditId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountCreditId());
                        lastBean.setGlAccount(glAccount);
                    }

                    postRestrictionDetailsBeanList.add(lastBean);
                    fillFinalSummary(lastBean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(lastBean);
                    }

                    break;

                case -2000:

                    if (postRestrictionsDetailsViewList.indexOf(detailsView) == 0) {
                        bean = new PostRestrictionDetailsBean();
                        bean.setAccNumber(detailsView.getAccNumberCredit());
                        bean.setAccName(detailsView.getAccNameCredit());
                        bean.setValueCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        bean.setValueLocalCredit(postRestrictionsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        //bean.setRemarks(restrictionsView.getRemark());
                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            bean.setGlAccount(glAccount);
                        }
                        // fillCostCenterAndAdminUnit(detailsView, bean);
                        postRestrictionDetailsBeanList.add(bean);
                        fillFinalSummary(bean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(bean);
                        }
                    }

                    lastBean = new PostRestrictionDetailsBean();
                    lastBean.setAccNumber(detailsView.getAccNumberDebit());
                    lastBean.setAccName(detailsView.getAccNameDebit());
                    lastBean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    lastBean.setValueLocalDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                    //lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                    if (detailsView.getAccountDebitId() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(detailsView.getAccountDebitId());
                        lastBean.setGlAccount(glAccount);
                    }

                    postRestrictionDetailsBeanList.add(lastBean);
                    fillFinalSummary(lastBean);
                    if (fillGeneralJournalDetailsFlow > 0) {
                        prepareGeneralJounalDetialsList(lastBean);
                    }

                    break;
                case 1022:
                    //details of first row that will be account of organization site (customer)
                    BigDecimal projectRevenue = calculateProjectRevenue(detailsView);

                    if (detailsView.getAccountDebitId() != null) {
                        bean = new PostRestrictionDetailsBean();
                        bean.setAccNumber(detailsView.getAccNumberDebit());
                        bean.setAccName(detailsView.getAccNameDebit());
                        if (detailsView.getSecondAccountCreditId() != null) {
                            bean.setValueDebit(detailsView.getValue().add(projectRevenue).setScale(3, BigDecimal.ROUND_HALF_UP));
                            bean.setValueLocalDebit(detailsView.getValue().add(projectRevenue).setScale(3, BigDecimal.ROUND_HALF_UP));

                        } else {
                            bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                            bean.setValueLocalDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        }

                        bean.setRemarks(detailsView.getRemarks());
                        if (detailsView.getAccountDebitId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountDebitId());
                            bean.setGlAccount(glAccount);
                        }
                        // fillCostCenterAndAdminUnit(detailsView, bean);
                        postRestrictionDetailsBeanList.add(bean);
                        fillFinalSummary(bean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(bean);
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب العميل او المشروعات"));
                        shiftValidation = false;
                    }

                    if (detailsView.getAccountCreditId() != null) {
                        lastBean = new PostRestrictionDetailsBean();
                        lastBean.setValueCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setAccNumber(detailsView.getAccNumberCredit());
                        lastBean.setAccName(detailsView.getAccNameCredit());
                        //lastBean.setValueCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        //lastBean.setValueLocalCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));

//                        lastBean.setValueCredit(debitFinalSummary);
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب المقاول  "));
                        shiftValidation = false;
                    }

                    //details of second row that will be account of bank id if the customer has paid advanced amount 
                    if (detailsView.getSecondAccountCreditId() != null) {
                        bean = new PostRestrictionDetailsBean();
                        bean.setAccNumber(detailsView.getSecondAccNumberCredit());
                        bean.setAccName(detailsView.getSecondAccNameCredit());
                        // bean.setValueCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                        // bean.setValueLocalCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                        bean.setValueCredit(projectRevenue);
                        bean.setValueLocalCredit(projectRevenue);
                        bean.setRemarks(detailsView.getRemarks());
                        if (detailsView.getSecondAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getSecondAccountCreditId());
                            bean.setGlAccount(glAccount);
                        }
                        // fillCostCenterAndAdminUnit(detailsView, bean);
                        postRestrictionDetailsBeanList.add(bean);
                        fillFinalSummary(bean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(bean);
                        }
                    }
//                        else {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب المرابحة"));
//                        shiftValidation = false;
//                    }

                    if (detailsView.getSecondAccountDebitId() != null) {
                        lastBean = new PostRestrictionDetailsBean();
                        lastBean.setValueDebit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalDebit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setAccNumber(detailsView.getSecondAccountDebitId());
                        lastBean.setAccName(detailsView.getSecondAccNameDebit());
                        lastBean.setRemarks(restrictionsView.getRemark());
                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getSecondAccountDebitId());
                            lastBean.setGlAccount(glAccount);
                        }
                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }
//                    else {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب المقاول  "));
//                        shiftValidation = false;
//                    }

                    if (detailsView.getThirdAccNumberCredit() != null) {
                        lastBean = new PostRestrictionDetailsBean();
                        lastBean.setValueCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setValueLocalCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                        lastBean.setAccNumber(detailsView.getThirdAccNumberCredit());
                        lastBean.setAccName(detailsView.getThirdAccNameCredit());
                        //lastBean.setValueCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                        //lastBean.setValueLocalCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));

//                        lastBean.setValueCredit(debitFinalSummary);
                        lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

                        if (detailsView.getAccountCreditId() != null) {
                            GlAccount glAccount = new GlAccount();
                            glAccount.setId(detailsView.getThirdAccountCreditId());
                            lastBean.setGlAccount(glAccount);
                        }

                        postRestrictionDetailsBeanList.add(lastBean);
                        fillFinalSummary(lastBean);
                        if (fillGeneralJournalDetailsFlow > 0) {
                            prepareGeneralJounalDetialsList(lastBean);
                        }
                    }
//                    else {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب المقاول  "));
//                        shiftValidation = false;
//                    }

                    break;
                case 1030:

                    noticeCode(detailsView, restrictionsView);

                    break;
                case 1031:

                    noticeCode(detailsView, restrictionsView);

                    break;
                case 1032:

                    noticeCode(detailsView, restrictionsView);

                    break;
                case 1033:

                    noticeCode(detailsView, restrictionsView);

                    break;
            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "splitDebitAndCreditAccounts");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "splitDebitAndCreditAccounts takes {0} seconds", (end - start) / 1000000000);
    }

    public void installmentCode(PostRestrictionsDetailsView detailsView, PostRestrictionsView restrictionsView, Map<String, String> userDDs) {
        PostRestrictionDetailsBean bean;
        //details of first row that will be account of organization site (customer)
        BigDecimal murabahaValue = calculateMurabha(detailsView);
        if (detailsView.getAccountDebitId() != null) {
            bean = new PostRestrictionDetailsBean();
            bean.setAccNumber(detailsView.getAccNumberDebit());
            bean.setAccName(detailsView.getAccNameDebit());
            bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            bean.setValueLocalDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            bean.setRemarks(detailsView.getRemarks());
            if (detailsView.getAccountDebitId() != null) {
                GlAccount glAccount = new GlAccount();
                glAccount.setId(detailsView.getAccountDebitId());
                bean.setGlAccount(glAccount);
            }
            // fillCostCenterAndAdminUnit(detailsView, bean);
            postRestrictionDetailsBeanList.add(bean);
            fillFinalSummary(bean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(bean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب للعميل"));
            shiftValidation = false;
        }
        if (detailsView.getAccountCreditId() != null) {
            PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
            lastBean.setAccNumber(detailsView.getAccNumberCredit());
            lastBean.setAccName(detailsView.getAccNameCredit());
            //lastBean.setValueCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            //lastBean.setValueLocalCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueCredit((detailsView.getValue().subtract(murabahaValue)).setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueLocalCredit((detailsView.getValue().subtract(murabahaValue)).setScale(3, BigDecimal.ROUND_HALF_UP));
//                        lastBean.setValueCredit(debitFinalSummary);
            lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

            if (detailsView.getAccountCreditId() != null) {
                GlAccount glAccount = new GlAccount();
                glAccount.setId(detailsView.getAccountCreditId());
                lastBean.setGlAccount(glAccount);
            }

            postRestrictionDetailsBeanList.add(lastBean);
            fillFinalSummary(lastBean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(lastBean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ايرادات المشروعات او المشروعات "));
            shiftValidation = false;
        }
        //details of second row that will be account of bank id if the customer has paid advanced amount
        if (detailsView.getSecondAccountCreditId() != null) {
            if (murabahaValue.compareTo(BigDecimal.ZERO) != 0) {
                bean = new PostRestrictionDetailsBean();
                bean.setAccNumber(detailsView.getSecondAccNumberCredit());
                bean.setAccName(detailsView.getSecondAccNameCredit());
                // bean.setValueCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                // bean.setValueLocalCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                bean.setValueCredit(murabahaValue);
                bean.setValueLocalCredit(murabahaValue);
                bean.setRemarks(detailsView.getRemarks());
                if (detailsView.getSecondAccountCreditId() != null) {
                    GlAccount glAccount = new GlAccount();
                    glAccount.setId(detailsView.getSecondAccountCreditId());
                    bean.setGlAccount(glAccount);
                }
                // fillCostCenterAndAdminUnit(detailsView, bean);
                postRestrictionDetailsBeanList.add(bean);
                fillFinalSummary(bean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(bean);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ايرادات المشاريع"));
            shiftValidation = false;
        }
    }

    public void cancelInstallmentCode(PostRestrictionsDetailsView detailsView, PostRestrictionsView restrictionsView, Map<String, String> userDDs) {

        PostRestrictionDetailsBean bean;
        //details of first row that will be account of organization site (customer)
        BigDecimal murabahaValue = calculateMurabha(detailsView);

        if (detailsView.getAccountCreditId() != null) {
            PostRestrictionDetailsBean lastBean = new PostRestrictionDetailsBean();
            lastBean.setAccNumber(detailsView.getAccNumberCredit());
            lastBean.setAccName(detailsView.getAccNameCredit());
            //lastBean.setValueCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            //lastBean.setValueLocalCredit(detailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueDebit((detailsView.getValue().subtract(murabahaValue)).setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueLocalDebit((detailsView.getValue().subtract(murabahaValue)).setScale(3, BigDecimal.ROUND_HALF_UP));
//                        lastBean.setValueCredit(debitFinalSummary);
            lastBean.setRemarks(restrictionsView.getRemark());
//                    fillCostCenterAndAdminUnit(detailsView, lastBean);

            if (detailsView.getAccountCreditId() != null) {
                GlAccount glAccount = new GlAccount();
                glAccount.setId(detailsView.getAccountCreditId());
                lastBean.setGlAccount(glAccount);
            }

            postRestrictionDetailsBeanList.add(lastBean);
            fillFinalSummary(lastBean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(lastBean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ايرادات المشروعات او المشروعات "));
            shiftValidation = false;
        }
        //details of second row that will be account of bank id if the customer has paid advanced amount
        if (detailsView.getSecondAccountCreditId() != null) {
            if (murabahaValue.compareTo(BigDecimal.ZERO) != 0) {
                bean = new PostRestrictionDetailsBean();
                bean.setAccNumber(detailsView.getSecondAccNumberCredit());
                bean.setAccName(detailsView.getSecondAccNameCredit());
                // bean.setValueCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                // bean.setValueLocalCredit((detailsView.getValue().subtract(detailsView.getSecondValue())).setScale(3, BigDecimal.ROUND_HALF_UP));
                bean.setValueDebit(murabahaValue);
                bean.setValueLocalDebit(murabahaValue);
                bean.setRemarks(detailsView.getRemarks());
                if (detailsView.getSecondAccountCreditId() != null) {
                    GlAccount glAccount = new GlAccount();
                    glAccount.setId(detailsView.getSecondAccountCreditId());
                    bean.setGlAccount(glAccount);
                }
                // fillCostCenterAndAdminUnit(detailsView, bean);
                postRestrictionDetailsBeanList.add(bean);
                fillFinalSummary(bean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(bean);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ايرادات المشاريع"));
            shiftValidation = false;
        }

        //details of first row that will be account of organization site (customer)
        if (detailsView.getAccountDebitId() != null) {
            bean = new PostRestrictionDetailsBean();
            bean.setAccNumber(detailsView.getAccNumberDebit());
            bean.setAccName(detailsView.getAccNameDebit());
            bean.setValueCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            bean.setValueLocalCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            bean.setRemarks(detailsView.getRemarks());
            if (detailsView.getAccountDebitId() != null) {
                GlAccount glAccount = new GlAccount();
                glAccount.setId(detailsView.getAccountDebitId());
                bean.setGlAccount(glAccount);
            }
            // fillCostCenterAndAdminUnit(detailsView, bean);
            postRestrictionDetailsBeanList.add(bean);
            fillFinalSummary(bean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(bean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب للعميل"));
            shiftValidation = false;
        }
    }

    private void PurchaseCode(PostRestrictionsDetailsView detailsView, PostRestrictionsView restrictionsView, Map<String, String> userDDs) {

        PostRestrictionDetailsBean lastBean;
        GlAnalyticsAccounts glAnalyticsAccountFirst;
        PostRestrictionDetailsBean bean;
        lastBean = new PostRestrictionDetailsBean();
        if (restrictionsView.getScreenCode() == 1018 || restrictionsView.getScreenCode() == 1019) {
            glAnalyticsAccountFirst = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "SALES_RETURN_ACCOUNT_NUMBER");

        } else {
            glAnalyticsAccountFirst = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "PURCHASE_ACCOUNT_NUMBER");

        }
        if (glAnalyticsAccountFirst.getAccountId() != null) {
            lastBean.setGlAccount(glAnalyticsAccountFirst.getAccountId());
            lastBean.setAccNumber(glAnalyticsAccountFirst.getAccountId().getAccNumber());
            lastBean.setAccName(glAnalyticsAccountFirst.getAccountId().getName());
            lastBean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueLocalDebit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setRemarks(restrictionsView.getRemark());
            fillCostCenterAndAdminUnit(detailsView, lastBean);

            postRestrictionDetailsBeanList.add(lastBean);
            fillFinalSummary(lastBean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(lastBean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب مشتريات "));
            shiftValidation = false;
        }
        if (detailsView.getTaxValue() != null && detailsView.getTaxValue().compareTo(BigDecimal.ZERO) == 1) {

            GlAnalyticsAccounts glAnalyticsAccountSecond = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "SALES_TAX_ACCOUNT");
            if (glAnalyticsAccountSecond.getAccountId() != null) {
                lastBean = new PostRestrictionDetailsBean();
                lastBean.setGlAccount(glAnalyticsAccountSecond.getAccountId());
                lastBean.setAccNumber(glAnalyticsAccountSecond.getAccountId().getAccNumber());
                lastBean.setAccName(glAnalyticsAccountSecond.getAccountId().getName());
                lastBean.setRemarks(restrictionsView.getRemark());
                lastBean.setValueDebit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                lastBean.setValueLocalDebit(detailsView.getTaxValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                postRestrictionDetailsBeanList.add(lastBean);
                fillFinalSummary(lastBean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(lastBean);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ضريبة مبيعات "));
                shiftValidation = false;
            }
        }
        bean = new PostRestrictionDetailsBean();
        bean.setAccNumber(detailsView.getAccNumberCredit());
        bean.setAccName(detailsView.getAccNameCredit());
        bean.setValueCredit(detailsView.getValue().add(detailsView.getTaxValue()).subtract(detailsView.getTaxDiscValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setValueLocalCredit(detailsView.getValueLocal().add(detailsView.getTaxValueLocal()).subtract(detailsView.getTaxDiscValueLocal()).setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setRemarks(detailsView.getRemarks());
        if (detailsView.getAccountCreditId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(detailsView.getAccountCreditId());
            bean.setGlAccount(glAccount);
        }
        fillCostCenterAndAdminUnit(detailsView, bean);
        postRestrictionDetailsBeanList.add(bean);
        fillFinalSummary(bean);
        if (fillGeneralJournalDetailsFlow > 0) {
            prepareGeneralJounalDetialsList(bean);
        }
        if (detailsView.getTaxDiscValue() != null && detailsView.getTaxDiscValue().compareTo(BigDecimal.ZERO) == 1) {
            GlAnalyticsAccounts glAnalyticsAccountDiscTax = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "DISCOUNT_TAX_ACCOUNT");
            if (glAnalyticsAccountDiscTax.getAccountId() != null) {
                lastBean = new PostRestrictionDetailsBean();
                lastBean.setGlAccount(glAnalyticsAccountDiscTax.getAccountId());
                lastBean.setAccNumber(glAnalyticsAccountDiscTax.getAccountId().getAccNumber());
                lastBean.setAccName(glAnalyticsAccountDiscTax.getAccountId().getName());
                lastBean.setRemarks(restrictionsView.getRemark());
                lastBean.setValueCredit(detailsView.getTaxDiscValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                lastBean.setValueLocalCredit(detailsView.getTaxDiscValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                postRestrictionDetailsBeanList.add(lastBean);
                fillFinalSummary(lastBean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(lastBean);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ضريبة مبيعات "));
                shiftValidation = false;
            }
        }
    }

    private void SalesCode(PostRestrictionsDetailsView detailsView, PostRestrictionsView restrictionsView, Map<String, String> userDDs) {
        PostRestrictionDetailsBean lastBean;
        GlAnalyticsAccounts glAnalyticsAccountFirst;
        PostRestrictionDetailsBean bean;
        lastBean = new PostRestrictionDetailsBean();

        bean = new PostRestrictionDetailsBean();
        bean.setAccNumber(detailsView.getAccNumberDebit());
        bean.setAccName(detailsView.getAccNameDebit());
        bean.setValueDebit(detailsView.getValue().add(detailsView.getTaxValue()).subtract(detailsView.getTaxDiscValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setValueLocalDebit(detailsView.getValueLocal().add(detailsView.getTaxValueLocal()).subtract(detailsView.getTaxDiscValueLocal()).setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setRemarks(detailsView.getRemarks());
        if (detailsView.getAccountDebitId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(detailsView.getAccountDebitId());
            bean.setGlAccount(glAccount);
        }
        fillCostCenterAndAdminUnit(detailsView, bean);
        postRestrictionDetailsBeanList.add(bean);
        fillFinalSummary(bean);
        if (fillGeneralJournalDetailsFlow > 0) {
            prepareGeneralJounalDetialsList(bean);// here
        }

        if (detailsView.getTaxDiscValue() != null && detailsView.getTaxDiscValue().compareTo(BigDecimal.ZERO) == 1) {
            GlAnalyticsAccounts glAnalyticsAccountDiscTax = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "DISCOUNT_TAX_ACCOUNT");
            if (glAnalyticsAccountDiscTax.getAccountId() != null) {
                lastBean = new PostRestrictionDetailsBean();
                lastBean.setGlAccount(glAnalyticsAccountDiscTax.getAccountId());
                lastBean.setAccNumber(glAnalyticsAccountDiscTax.getAccountId().getAccNumber());
                lastBean.setAccName(glAnalyticsAccountDiscTax.getAccountId().getName());
                lastBean.setRemarks(restrictionsView.getRemark());
                lastBean.setValueDebit(detailsView.getTaxDiscValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                lastBean.setValueLocalDebit(detailsView.getTaxDiscValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                postRestrictionDetailsBeanList.add(lastBean);
                fillFinalSummary(lastBean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(lastBean);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ضريبة مبيعات "));
                shiftValidation = false;
            }

        }
        if (restrictionsView.getScreenCode() == 1015 || restrictionsView.getScreenCode() == 1016) {
            glAnalyticsAccountFirst = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "PURCHASE_RETURN_ACCOUNT_NUMBER");

        } else {
            glAnalyticsAccountFirst = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "SALES_ACCOUNT_NUMBER");

        }
        if (glAnalyticsAccountFirst.getAccountId() != null) {
            lastBean = new PostRestrictionDetailsBean();
            lastBean.setGlAccount(glAnalyticsAccountFirst.getAccountId());
            lastBean.setAccNumber(glAnalyticsAccountFirst.getAccountId().getAccNumber());
            lastBean.setAccName(glAnalyticsAccountFirst.getAccountId().getName());
            lastBean.setValueCredit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setValueLocalCredit(detailsView.getValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
            lastBean.setRemarks(restrictionsView.getRemark());
            fillCostCenterAndAdminUnit(detailsView, lastBean);

            postRestrictionDetailsBeanList.add(lastBean);
            fillFinalSummary(lastBean);
            if (fillGeneralJournalDetailsFlow > 0) {
                prepareGeneralJounalDetialsList(lastBean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب مشتريات "));
            shiftValidation = false;
        }
        if (detailsView.getTaxValue() != null && detailsView.getTaxValue().compareTo(BigDecimal.ZERO) == 1) {

            GlAnalyticsAccounts glAnalyticsAccountSecond = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(userData.getUserBranch().getId(), "SALES_TAX_ACCOUNT");
            if (glAnalyticsAccountSecond.getAccountId() != null) {
                lastBean = new PostRestrictionDetailsBean();
                lastBean.setGlAccount(glAnalyticsAccountSecond.getAccountId());
                lastBean.setAccNumber(glAnalyticsAccountSecond.getAccountId().getAccNumber());
                lastBean.setAccName(glAnalyticsAccountSecond.getAccountId().getName());
                lastBean.setRemarks(restrictionsView.getRemark());
                lastBean.setValueCredit(detailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP));
                lastBean.setValueLocalCredit(detailsView.getTaxValueLocal().setScale(3, BigDecimal.ROUND_HALF_UP));
                postRestrictionDetailsBeanList.add(lastBean);
                fillFinalSummary(lastBean);
                if (fillGeneralJournalDetailsFlow > 0) {
                    prepareGeneralJounalDetialsList(lastBean);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار حساب ضريبة مبيعات "));
                shiftValidation = false;
            }
        }
    }

    public void noticeCode(PostRestrictionsDetailsView detailsView, PostRestrictionsView restrictionsView) {
        PostRestrictionDetailsBean bean;
        PostRestrictionDetailsBean lastBean;

        bean = new PostRestrictionDetailsBean();
        bean.setAccNumber(detailsView.getAccNumberDebit());
        bean.setAccName(detailsView.getAccNameDebit());
        bean.setValueDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setValueLocalDebit(detailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP));
        bean.setRemarks(detailsView.getRemarks());
        if (detailsView.getAccountDebitId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(detailsView.getAccountDebitId());
            bean.setGlAccount(glAccount);
        }
        postRestrictionDetailsBeanList.add(bean);
        fillFinalSummary(bean);
        if (fillGeneralJournalDetailsFlow > 0) {
            prepareGeneralJounalDetialsList(bean);
        }
        lastBean = new PostRestrictionDetailsBean();
        lastBean.setValueCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
        lastBean.setValueLocalCredit((detailsView.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
        lastBean.setAccNumber(detailsView.getAccNumberCredit());
        lastBean.setAccName(detailsView.getAccNameCredit());
        lastBean.setRemarks(restrictionsView.getRemark());
        if (detailsView.getAccountCreditId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(detailsView.getAccountCreditId());
            lastBean.setGlAccount(glAccount);
        }
        postRestrictionDetailsBeanList.add(lastBean);
        fillFinalSummary(lastBean);
        if (fillGeneralJournalDetailsFlow > 0) {
            prepareGeneralJounalDetialsList(lastBean);
        }
    }

    /*  public void shift() {
     long start = System.nanoTime();

     try {
     Map<String, String> userDDs = getUserData().getUserDDs();

     checkIfOneRecordShiftingOrAll();
     if (postRestrictionsViewList != null && !postRestrictionsViewList.isEmpty() && checkIfAccountsMustSelectedFirst()) {
     fillGeneralJournalDetailsFlow++;
     for (PostRestrictionsView prv : postRestrictionsViewList) {
     super.resetForShiftingJournalScreen();
     super.getSelectedGeneralJournal();
     super.setGlYearSelection(glYearSelection);
     generalJournalSelected = new GeneralJournal();
     generalJournalSelected.setGeneralStatement(prv.getRemark());
     generalJournalSelected.setGeneralData(prv.getDate());
     generalJournalSelected.setPost_flag(true);
     generalJournalSelected.setGeneralDecument(prv.getSerial());
     /* List<Symbol> list = new ArrayList<>();
     list = symbolService.getSymbolBySerial(getUserData().getCompany().getId(), documentFixedNumber);
     if (list != null && !list.isEmpty()) {
     generalJournalSelected.setGeneralType(list.get(0));
     }

     super.setSelectedGeneralJournal(generalJournalSelected);
     super.save();
     if (prv.getScreenCode() < 1009) {
     updateGlBankData(prv, super.getReturnedGeneralJournal() != null ? super.getReturnedGeneralJournal().getId() : null);
     } else if (prv.getScreenCode() >= 1009 && prv.getScreenCode() < 1015) {
     updateInvPurchaseInvoiceData(prv, super.getReturnedGeneralJournal() != null ? super.getReturnedGeneralJournal().getId() : null);
     } else if (prv.getScreenCode() >= 1015 && prv.getScreenCode() < 1021) {
     updateInvPurchaseReturnInvoiceData(prv, super.getReturnedGeneralJournal() != null ? super.getReturnedGeneralJournal().getId() : null);
     } else {
     updateIstallmentContractData(prv, super.getReturnedGeneralJournal() != null ? super.getReturnedGeneralJournal().getId() : null);
     }
     //generalJournalSelected.setId(super.getMaxId() != null ? super.getMaxId() : null);
     super.setSelectedGeneralJournal(super.getReturnedGeneralJournal());
     generalJournalSelected = super.getReturnedGeneralJournal();
     super.setDetailsList(new ArrayList<GeneralJournalDetails>());
     postRestrictionDetailsBeanList = new ArrayList<>();
     postRestrictionsView = prv;
     LoadDataTableDetails(prv);
     super.setSummaryCredit(creditFinalSummary);
     super.setSummaryDebit(debitFinalSummary);
     super.addDetailsForShiftingJournalScreen();
     loadDataTableHead();
     }
     fillGeneralJournalDetailsFlow = 0;
     postRestrictionsSelected = new PostRestrictionsView();
     }
     if (super.getReturnedGeneralJournal() != null && super.getReturnedGeneralJournal().getId() != null && super.getDatailsNotSavedReference() != null && super.getDatailsNotSavedReference() == 0) {
     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), ("تمت العملية بنجاح")));
     }
     } catch (Exception e) {
     saveError(e, "ShiftingJournalMB", "shift");
     }
     long end = System.nanoTime();
     logger.log(Level.INFO, "shift takes {0} seconds", (end - start)/ 1000000000);
     }*/
    public void shift() {
        long start = System.nanoTime();

        Map<String, String> userDDs = getUserData().getUserDDs();
        Map<Integer, PostRestrictionsView> postRestricitonViewMap = new HashMap<>();
        generalJournalHeadList = new ArrayList<>();
        checkIfOneRecordShiftingOrAll();
        if (postRestrictionsViewList != null && !postRestrictionsViewList.isEmpty() && checkIfAccountsMustSelectedFirst()) {
            fillGeneralJournalDetailsFlow++;
            for (PostRestrictionsView prv : postRestrictionsViewList) {
                generalJournalSelected = new GeneralJournal();
                generalJournalSelected.setGeneralStatement(prv.getRemark());
                generalJournalSelected.setGeneralData(prv.getDate());
                generalJournalSelected.setPost_flag(true);
                generalJournalSelected.setGeneralDecument(prv.getSerial());
                generalJournalSelected.setBranchId(userData.getUserBranch());
                generalJournalSelected.setCompanyId(userData.getCompany());
                generalJournalSelected.setCreatedBy(userData.getUser());
                generalJournalSelected.setCreationDate(new Date());
                generalJournalSelected.setGlYear(glYearSelection);
                generalJournalSelected.setGeneralType(symbol != null ? symbol : null);
                postRestrictionDetailsBeanList = new ArrayList<>();
                postRestrictionsView = prv;
                LoadDataTableDetails(postRestrictionsView);
                postRestricitonViewMap.put(prv.getSerial(), prv);
                generalJournalHeadList.add(generalJournalSelected);

            }
        }
        try {
            shiftingJournalService.saveHeadJournalAndDetails(generalJournalHeadList, generalJournalDetailsMap, postRestricitonViewMap, glYearSelection, fillGeneralJournalDetailsFlow, userData.getCompany().getId(), cancelInstallment);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), ("تمت العملية بنجاح")));
        } catch (Exception e) {
            loadDataTableHead();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
            saveError(e, "ShiftingJournalMB", "shift");
        } finally {
            generalJournalDetailsMap = new HashMap<>();
            fillGeneralJournalDetailsFlow = 0;
            postRestrictionsSelected = new PostRestrictionsView();
        }
        loadDataTableHead();

        long end = System.nanoTime();
        logger.log(Level.INFO, "shift takes {0} seconds", (end - start) / 1000000000);
    }

    public void prepareGeneralJounalDetialsList(PostRestrictionDetailsBean bean) {

        try {
            GeneralJournalDetails generalJournalDetail = new GeneralJournalDetails();
            generalJournalDetail.setBranchId(getUserData().getUserBranch());
            generalJournalDetail.setCreditamount(bean.getValueCredit());
            generalJournalDetail.setDebitAmount(bean.getValueDebit());
            generalJournalDetail.setCreditamountLocal(bean.getValueLocalCredit() != null ? bean.getValueLocalCredit() : BigDecimal.ZERO);
            generalJournalDetail.setDebitAmountLocal(bean.getValueLocalDebit() != null ? bean.getValueLocalDebit() : BigDecimal.ZERO);
            generalJournalDetail.setCreationDate(new Date());
            generalJournalDetail.setCreatedBy(getUserData().getUser());
            generalJournalDetail.setGlACCOUNTId(bean.getGlAccount());
            generalJournalDetail.setDiscribtion(bean.getRemarks());
            generalJournalDetail.setCompanyId(getUserData().getCompany());
            generalJournalDetail.setCostCenterId(bean.getCostCenter());
            generalJournalDetail.setAdminUnitId(bean.getAdminUnit());
            generalJournalDetail.setGeneralJournalId(generalJournalSelected);
            //generalJournalDetailsList.add(generalJournalDetail);
            // super.setDetail(new GeneralJournalDetails());
            // super.setDetail(generalJournalDetail);
            //super.addExtraDateToDetails();
            // super.getDetailsList().add(super.getDetail());
            if (fillGeneralJournalDetailsFlow > 0) {
                if (generalJournalDetailsMap != null && generalJournalDetailsMap.containsKey(postRestrictionsView.getSerial())) {
                    List<GeneralJournalDetails> journalDetailsList = generalJournalDetailsMap.get(postRestrictionsView.getSerial());
                    journalDetailsList.add(generalJournalDetail);
                } else if (generalJournalDetailsMap != null && !generalJournalDetailsMap.containsKey(postRestrictionsView.getSerial())) {
                    List<GeneralJournalDetails> journalDetailsList = new ArrayList<>();
                    journalDetailsList.add(generalJournalDetail);
                    generalJournalDetailsMap.put(postRestrictionsView.getSerial(), journalDetailsList);
                }
            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "prepareGeneralJounalDetialsList");
        }
    }

    public void undoShifting() {
        long start = System.nanoTime();

        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            checkIfOneRecordShiftingOrAll();
            try {
                generaljournalService.deleteHeadAndDetailsForShiftingScreen(postRestrictionsViewList, fillGeneralJournalDetailsFlow, null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), ("تمت العملية بنجاح")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
            }
            loadDataTableHead();
            postRestrictionsSelected = new PostRestrictionsView();
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "undoShifting");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "undoShifting takes {0} seconds", (end - start) / 1000000000);
    }

    public void updateGlBankData(PostRestrictionsView prv, Integer generalJournalId) {
        long start = System.nanoTime();

        try {
            GlBankTransaction glBankTransaction = new GlBankTransaction();
            glBankTransaction.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                glBankTransaction.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    glBankTransaction.setGeneralJournalId(generalJournal);
                }
            } else {
                glBankTransaction.setPostFlag(0);
                glBankTransaction.setGeneralJournalId(null);
            }
            glBankTransactionService.updateGlBankTransaction(glBankTransaction);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "updateGlBankData");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "updateGlBankData takes {0} seconds", (end - start) / 1000000000);
    }

    public void updateInvPurchaseInvoiceData(PostRestrictionsView prv, Integer generalJournalId) {
        long start = System.nanoTime();

        try {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                invPurchaseInvoice.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    invPurchaseInvoice.setGeneralJournalId(generalJournal);
                }
            } else {
                invPurchaseInvoice.setPostFlag(0);
                invPurchaseInvoice.setGeneralJournalId(null);
            }
            invPurchaseInvoiceService.updateInvPurchaseInvoice(invPurchaseInvoice);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "updateInvPurchaseInvoiceData");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "updateInvPurchaseInvoiceData takes {0} seconds", (end - start) / 1000000000);
    }

    public void updateInvPurchaseReturnInvoiceData(PostRestrictionsView prv, Integer generalJournalId) {
        long start = System.nanoTime();

        try {
            InvReturnPurchase invReturnPurchase = new InvReturnPurchase();
            invReturnPurchase.setId(prv.getId() != null ? prv.getId() : null);
            if (fillGeneralJournalDetailsFlow > 0) {
                invReturnPurchase.setPostFlag(1);
                GeneralJournal generalJournal = new GeneralJournal();
                generalJournal.setId(generalJournalId);
                if (generalJournalId != null) {
                    invReturnPurchase.setGeneralJournalId(generalJournal);
                }
            } else {
                invReturnPurchase.setPostFlag(0);
                invReturnPurchase.setGeneralJournalId(null);
            }
            invReturnPurchaseService.updateReturnPurchaseInvoice(invReturnPurchase);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "updateInvPurchaseReturnInvoiceData");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "updateInvPurchaseReturnInvoiceData takes {0} seconds", (end - start) / 1000000000);
    }

    public void updateIstallmentContractData(PostRestrictionsView prv, Integer generalJournalId) {
        long start = System.nanoTime();

        try {
//            InstContract instContract = new InstContract();
//            instContract.setId(prv.getId() != null ? prv.getId() : null);
//            if (fillGeneralJournalDetailsFlow > 0) {
//                instContract.setPostFlag(1);
//                GeneralJournal generalJournal = new GeneralJournal();
//                generalJournal.setId(generalJournalId);
//                if (generalJournalId != null) {
//                    instContract.setGeneralJournalId(generalJournal);
//                }
//            } else {
//                instContract.setPostFlag(0);
//                instContract.setGeneralJournalId(null);
//            }
//            instContractService.updateIntsContract(instContract);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "updateIstallmentContractData");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "updateIstallmentContractData takes {0} seconds", (end - start) / 1000000000);
    }

    public void showShiftedJournals() {
        loadDataTableHead();
    }

    public void checkIfOneRecordShiftingOrAll() {
        long start = System.nanoTime();

        try {
            if (postRestrictionsSelected != null && postRestrictionsSelected.getId() != null) {
                postRestrictionsViewList = new ArrayList<>();
                generalJournalDetailsMap = new HashMap<>();
                postRestrictionsViewList.add(postRestrictionsSelected);

            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "checkIfOneRecordShiftingOrAll");
        }
        long end = System.nanoTime();
        logger.log(Level.INFO, "checkIfOneRecordShiftingOrAll takes {0} seconds", (end - start) / 1000000000);
    }

    public PostRestrictionDetailsBean fillCostCenterAndAdminUnit(PostRestrictionsDetailsView detailsView, PostRestrictionDetailsBean bean) {

        bean.setCostCenter(costCenterService.findCostCenter(detailsView.getCostCenterId()) != null ? costCenterService.findCostCenter(detailsView.getCostCenterId()) : null);
        bean.setAdminUnit(adminUnitService.findAdminUnit(detailsView.getAdminUnitId()) != null ? adminUnitService.findAdminUnit(detailsView.getAdminUnitId()) : null);

        return bean;
    }

    public Boolean checkIfAccountsMustSelectedFirst() {
        try {
            postRestrictionsView = postRestrictionsViewList.get(0);
            LoadDataTableDetails(postRestrictionsViewList.get(0));
            if (symbol != null && symbol.getSerial() > 1008 && !shiftValidation) {
                postRestrictionDetailsBeanList = new ArrayList<>();
                generalJournalDetailsMap = new HashMap<>();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "checkIfOneRecordShiftingOrAll");
            return false;
        }

    }

    public BigDecimal calculateMurabha(PostRestrictionsDetailsView postRestrictionsDetailsView) {
        BigDecimal hundered = BigDecimal.valueOf(100).setScale(3, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalProjectPrice = postRestrictionsDetailsView.getSecondValue() != null ? postRestrictionsDetailsView.getSecondValue().setScale(3, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal murabahaRate = postRestrictionsDetailsView.getTaxValue() != null ? postRestrictionsDetailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        return (totalProjectPrice.multiply(murabahaRate).divide(hundered)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateProjectRevenue(PostRestrictionsDetailsView postRestrictionsDetailsView) {
        BigDecimal hundered = BigDecimal.valueOf(100).setScale(3, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalProjectPrice = postRestrictionsDetailsView.getValue() != null ? postRestrictionsDetailsView.getValue().setScale(3, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal murabahaRate = postRestrictionsDetailsView.getTaxValue() != null ? postRestrictionsDetailsView.getTaxValue().setScale(3, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        return (totalProjectPrice.multiply(murabahaRate).divide(hundered)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public List<GlYear> getGlYearList() {
        try {
            if (glYearList == null || glYearList.isEmpty()) {
                glYearList = tobyUserYearService.findYearByUserId(getUserData().getUser().getId(), getUserData().getUserBranch());
                setGlYearConverter(new GlYearConverter(glYearList));
            }
            return glYearList;
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "getGlYearList");
            return null;
        }
    }

    public void pushGlYearSelection() {
        try {
            resetFinancailYearDate();
            getUserData().setGlYear(glYearSelection);
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("userLogInData", getUserData());
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "pushGlYearSelection");
        }
    }

    public void resetFinancailYearDate() {
        try {
            resetDateFrom();
            resetDateTo();
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "resetFinancailYearDate");
        }
    }

    public void resetDateFrom() {
        try {
            setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "resetDateFrom");
        }
    }

    public void resetDateTo() {
        try {
            setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "resetDateTo");
        }
    }

    public void checkDate(Boolean dateType) {
        try {
            if (dateType) {
                if (checkFinancailYear(getDateFrom())) {
                    resetDateFrom();
                }
            } else {
                if (checkFinancailYear(getDateTo())) {
                    resetDateTo();
                }
            }
            loadDataTableHead();
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "checkDate");
        }
    }

    public Boolean checkFinancailYear(Date date) {
        try {
            if ((date.before(getGlYearSelection().getDateFrom()) || date.after(getGlYearSelection().getDateTo()))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
                return true;
            }
            return false;
        } catch (Exception e) {
            saveError(e, "ShiftingJournalMB", "checkFinancailYear");
            return false;
        }
    }

    public void cancelInstallment() {
        cancelInstallment = true;
//        postRestrictionsSelected = restrictionsViewService.getOneInstBankTransaction(selectedInstallmentId);
        symbol = symbolsMap.get(1021);
        shift();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the symbolList
     */
    public List<Symbol> getSymbolList() {
        return symbolList;
    }

    /**
     * @param symbolList the symbolList to set
     */
    public void setSymbolList(List<Symbol> symbolList) {
        this.symbolList = symbolList;
    }

    /**
     * @return the symbolConverter
     */
    public SymbolConverter getSymbolConverter() {
        return symbolConverter;
    }

    /**
     * @param symbolConverter the symbolConverter to set
     */
    public void setSymbolConverter(SymbolConverter symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    /**
     * @return the userData
     */
    @Override
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    @Override
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the generalJournalSelected
     */
    public GeneralJournal getGeneralJournalSelected() {
        return generalJournalSelected;
    }

    /**
     * @param generalJournalSelected the generalJournalSelected to set
     */
    public void setGeneralJournalSelected(GeneralJournal generalJournalSelected) {
        this.generalJournalSelected = generalJournalSelected;
    }

    /**
     * @return the generalJournalHeadList
     */
    public List<GeneralJournal> getGeneralJournalHeadList() {
        return generalJournalHeadList;
    }

    /**
     * @param generalJournalHeadList the generalJournalHeadList to set
     */
    public void setGeneralJournalHeadList(List<GeneralJournal> generalJournalHeadList) {
        this.generalJournalHeadList = generalJournalHeadList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    @Override
    public void setGlYearList(List<GlYear> glYearList) {
        this.glYearList = glYearList;
    }

    /**
     * @return the glYearId
     */
    @Override
    public Integer getGlYearId() {
        return glYearId;
    }

    /**
     * @param glYearId the glYearId to set
     */
    @Override
    public void setGlYearId(Integer glYearId) {
        this.glYearId = glYearId;
    }

    /**
     * @return the glYearSelection
     */
    @Override
    public GlYear getGlYearSelection() {
        if (glYearSelection == null) {
            if (getUserData().getGlYear() != null) {
                glYearSelection = getUserData().getGlYear();
            } else {
                if (getGlYearList() != null && !getGlYearList().isEmpty()) {
                    glYearSelection = getGlYearList().get(0);
                }
            }
        }
        return glYearSelection;
    }

    /**
     * @param glYearSelection the glYearSelection to set
     */
    @Override
    public void setGlYearSelection(GlYear glYearSelection) {
        this.glYearSelection = glYearSelection;
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
     * @return the glYearConverter
     */
    @Override
    public GlYearConverter getGlYearConverter() {
        return glYearConverter;
    }

    /**
     * @param glYearConverter the glYearConverter to set
     */
    @Override
    public void setGlYearConverter(GlYearConverter glYearConverter) {
        this.glYearConverter = glYearConverter;
    }

    /**
     * @return the documentFixedNumber
     */
    public Integer getDocumentFixedNumber() {
        return documentFixedNumber;
    }

    /**
     * @param documentFixedNumber the documentFixedNumber to set
     */
    public void setDocumentFixedNumber(Integer documentFixedNumber) {
        this.documentFixedNumber = documentFixedNumber;
    }

    /**
     * @return the postRestrictionsView
     */
    public PostRestrictionsView getPostRestrictionsView() {
        return postRestrictionsView;
    }

    /**
     * @param postRestrictionsView the postRestrictionsView to set
     */
    public void setPostRestrictionsView(PostRestrictionsView postRestrictionsView) {
        this.postRestrictionsView = postRestrictionsView;
    }

    /**
     * @return the postRestrictionsViewList
     */
    public List<PostRestrictionsView> getPostRestrictionsViewList() {
        return postRestrictionsViewList;
    }

    /**
     * @param postRestrictionsViewList the postRestrictionsViewList to set
     */
    public void setPostRestrictionsViewList(List<PostRestrictionsView> postRestrictionsViewList) {
        this.postRestrictionsViewList = postRestrictionsViewList;
    }

    /**
     * @return the postRestrictionsDetailsView
     */
    public PostRestrictionsDetailsView getPostRestrictionsDetailsView() {
        return postRestrictionsDetailsView;
    }

    /**
     * @param postRestrictionsDetailsView the postRestrictionsDetailsView to set
     */
    public void setPostRestrictionsDetailsView(PostRestrictionsDetailsView postRestrictionsDetailsView) {
        this.postRestrictionsDetailsView = postRestrictionsDetailsView;
    }

    /**
     * @return the postRestrictionsDetailsViewList
     */
    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsViewList() {
        return postRestrictionsDetailsViewList;
    }

    /**
     * @param postRestrictionsDetailsViewList the
     * postRestrictionsDetailsViewList to set
     */
    public void setPostRestrictionsDetailsViewList(List<PostRestrictionsDetailsView> postRestrictionsDetailsViewList) {
        this.postRestrictionsDetailsViewList = postRestrictionsDetailsViewList;
    }

    /**
     * @return the postRestrictionDetailsBean
     */
    public PostRestrictionDetailsBean getPostRestrictionDetailsBean() {
        return postRestrictionDetailsBean;
    }

    /**
     * @param postRestrictionDetailsBean the postRestrictionDetailsBean to set
     */
    public void setPostRestrictionDetailsBean(PostRestrictionDetailsBean postRestrictionDetailsBean) {
        this.postRestrictionDetailsBean = postRestrictionDetailsBean;
    }

    /**
     * @return the postRestrictionDetailsBeanList
     */
    public List<PostRestrictionDetailsBean> getPostRestrictionDetailsBeanList() {
        return postRestrictionDetailsBeanList;
    }

    /**
     * @param postRestrictionDetailsBeanList the postRestrictionDetailsBeanList
     * to set
     */
    public void setPostRestrictionDetailsBeanList(List<PostRestrictionDetailsBean> postRestrictionDetailsBeanList) {
        this.postRestrictionDetailsBeanList = postRestrictionDetailsBeanList;
    }

    /**
     * @return the debitFinalSummary
     */
    public BigDecimal getDebitFinalSummary() {
        return debitFinalSummary;
    }

    /**
     * @param debitFinalSummary the debitFinalSummary to set
     */
    public void setDebitFinalSummary(BigDecimal debitFinalSummary) {
        this.debitFinalSummary = debitFinalSummary;
    }

    /**
     * @return the creditFinalSummary
     */
    public BigDecimal getCreditFinalSummary() {
        return creditFinalSummary;
    }

    /**
     * @param creditFinalSummary the creditFinalSummary to set
     */
    public void setCreditFinalSummary(BigDecimal creditFinalSummary) {
        this.creditFinalSummary = creditFinalSummary;
    }

    /**
     * @return the generalJournalDetailsList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsList() {
        return generalJournalDetailsList;
    }

    /**
     * @param generalJournalDetailsList the generalJournalDetailsList to set
     */
    public void setGeneralJournalDetailsList(List<GeneralJournalDetails> generalJournalDetailsList) {
        this.generalJournalDetailsList = generalJournalDetailsList;
    }

    /**
     * @return the fillGeneralJournalDetailsFlow
     */
    public Integer getFillGeneralJournalDetailsFlow() {
        return fillGeneralJournalDetailsFlow;
    }

    /**
     * @param fillGeneralJournalDetailsFlow the fillGeneralJournalDetailsFlow to
     * set
     */
    public void setFillGeneralJournalDetailsFlow(Integer fillGeneralJournalDetailsFlow) {
        this.fillGeneralJournalDetailsFlow = fillGeneralJournalDetailsFlow;
    }

    /**
     * @return the shiftedJournals
     */
    public Boolean getShiftedJournals() {
        return shiftedJournals;
    }

    /**
     * @param shiftedJournals the shiftedJournals to set
     */
    public void setShiftedJournals(Boolean shiftedJournals) {
        this.shiftedJournals = shiftedJournals;
    }

    /**
     * @return the postRestrictionsSelected
     */
    public PostRestrictionsView getPostRestrictionsSelected() {
        return postRestrictionsSelected;
    }

    /**
     * @param postRestrictionsSelected the postRestrictionsSelected to set
     */
    public void setPostRestrictionsSelected(PostRestrictionsView postRestrictionsSelected) {
        this.postRestrictionsSelected = postRestrictionsSelected;
    }

    /**
     * @return the costCenterShow
     */
    public CostCenter getCostCenterShow() {
        return costCenterShow;
    }

    /**
     * @param costCenterShow the costCenterShow to set
     */
    public void setCostCenterShow(CostCenter costCenterShow) {
        this.costCenterShow = costCenterShow;
    }

    /**
     * @return the adminUnitShow
     */
    public GlAdminUnit getAdminUnitShow() {
        return adminUnitShow;
    }

    /**
     * @param adminUnitShow the adminUnitShow to set
     */
    public void setAdminUnitShow(GlAdminUnit adminUnitShow) {
        this.adminUnitShow = adminUnitShow;
    }

    /**
     * @return the postRestrictionsDetailsBeanSelection
     */
    public PostRestrictionDetailsBean getPostRestrictionsDetailsBeanSelection() {
        return postRestrictionsDetailsBeanSelection;
    }

    /**
     * @param postRestrictionsDetailsBeanSelection the
     * postRestrictionsDetailsBeanSelection to set
     */
    public void setPostRestrictionsDetailsBeanSelection(PostRestrictionDetailsBean postRestrictionsDetailsBeanSelection) {
        this.postRestrictionsDetailsBeanSelection = postRestrictionsDetailsBeanSelection;
    }

    /**
     * @return the specialDocumentsTypeList
     */
    public List<Symbol> getSpecialDocumentsTypeList() {
        return specialDocumentsTypeList;
    }

    /**
     * @param specialDocumentsTypeList the specialDocumentsTypeList to set
     */
    public void setSpecialDocumentsTypeList(List<Symbol> specialDocumentsTypeList) {
        this.specialDocumentsTypeList = specialDocumentsTypeList;
    }

    /**
     * @return the shiftValidation
     */
    public Boolean getShiftValidation() {
        return shiftValidation;
    }

    /**
     * @param shiftValidation the shiftValidation to set
     */
    public void setShiftValidation(Boolean shiftValidation) {
        this.shiftValidation = shiftValidation;
    }

    /**
     * @return the options
     */
    public List<SelectItem> getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(List<SelectItem> options) {
        this.options = options;
    }

    /**
     * @return the symbolId
     */
    public Integer getSymbolId() {
        return symbolId;
    }

    /**
     * @param symbolId the symbolId to set
     */
    public void setSymbolId(Integer symbolId) {
        this.symbolId = symbolId;
    }

    /**
     * @return the symbolsMapById
     */
    public Map<Integer, Symbol> getSymbolsMapById() {
        return symbolsMapById;
    }

    /**
     * @param symbolsMapById the symbolsMapById to set
     */
    public void setSymbolsMapById(Map<Integer, Symbol> symbolsMapById) {
        this.symbolsMapById = symbolsMapById;
    }

    /**
     * @return the generalJournalDetailsMap
     */
    public Map<Integer, List<GeneralJournalDetails>> getGeneralJournalDetailsMap() {
        return generalJournalDetailsMap;
    }

    /**
     * @param generalJournalDetailsMap the generalJournalDetailsMap to set
     */
    public void setGeneralJournalDetailsMap(Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap) {
        this.generalJournalDetailsMap = generalJournalDetailsMap;
    }

    /**
     * @return the selectedInstallmentId
     */
    public Integer getSelectedInstallmentId() {
        return selectedInstallmentId;
    }

    /**
     * @param selectedInstallmentId the selectedInstallmentId to set
     */
    public void setSelectedInstallmentId(Integer selectedInstallmentId) {
        this.selectedInstallmentId = selectedInstallmentId;
    }

}
