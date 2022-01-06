/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.generaljournal;

import com.toby.bean.JournalDocumentArrangedReportBean;
import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.BranchService;
import com.toby.businessservice.CloseAndSaveMonthService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.CurrencyOperationService;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.GlYearConverter;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.CloseAndSaveMonth;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.CurrencyOperation;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlYear;
import com.toby.entity.TobyCompany;
import com.toby.entity.Symbol;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.reports.GeneralJournalShowBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named(value = "generalJournalBean")
@ViewScoped
public class GeneralJournalBean extends BaseFormBean implements Serializable {

    private GeneralJournal selectedGeneralJournal;
    private List<GeneralJournal> generalList;
    private List<GeneralJournal> generalReviewList;
    private Integer selectedCompany;
    private List<TobyCompany> companies;
    private List<GeneralJournalDetails> details = new ArrayList<>();
    private GeneralJournalDetails detail;
    private List<GeneralJournalDetails> rowsDeleted = new ArrayList<>();
    private List<GeneralJournalDetails> detailsList;
    private List<GeneralJournalDetails> detailsListCopy;

    private String difference;

    private GeneralJournalDetails journalDetailSelected;

    private GeneralJournalDetails detailTarget;
    private GeneralJournalDetails detailReset;
    private GeneralJournal generalGeneralReset;
    private GlAccount gAccount;
    private Integer selectedAccount;
    private Integer selectedCurrency;
    private CostCenter costCenter;
    private GlAdminUnit adminUnit;
    private List<GlAccount> glAccounts;
    private List<CostCenter> costCenterList;
    private List<GlAdminUnit> glAdminunitlist;
    private List<Currency> currencylist;
    private List<CloseAndSaveMonth> closeAndSaveMonthList;
    private CloseAndSaveMonth closeAndSaveMonth;

    private CurrencyOperation currencyOperations;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;

    private Boolean viewCredit = Boolean.FALSE;
    private Boolean viewDebit = Boolean.FALSE;
    private BigDecimal cAmount;
    private BigDecimal dAmount;
    private BigDecimal currencyRate;
    private List<Symbol> documentsTypes;
    private Integer selectedDocumentsType;
    private Integer generalDocument;
    private Boolean debitDisabled = Boolean.FALSE;
    private Boolean creditDisabled = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;
    private Boolean view = Boolean.FALSE;
    private Boolean disableAccount = Boolean.TRUE;
    private Boolean disableAdminU = Boolean.TRUE;
    private Boolean disableCC = Boolean.TRUE;
    private Boolean EditGeneralJournal = Boolean.FALSE;
    //private Boolean flags ;
    private boolean value1;
    private boolean value2;
    private boolean flags;
    private boolean closeMonthCheck = true;

    private boolean rowAdded = false;

    private boolean parentRowAdded = false;

    private String accountName;

    private BigDecimal summaryDebit = BigDecimal.ZERO;
    private BigDecimal summaryCredit = BigDecimal.ZERO;

    private List<GlYear> glYearList;
    private Integer glYearId;
    private GlYear glYearSelection;

    private List<GeneralJournalShowBean> generalJournalShowBeanList;

    private GlYearConverter glYearConverter;

    private Integer index = 0;

    private Integer maxId;
    InetAddress ip;

    private Boolean stickyHeader;
    private Integer printType;
    private List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList;
    private String uri;
    private SelectEvent ev;
    private SelectEvent evNew;
    private Integer confirmsCounter = 0;

    private Integer datailsNotSavedReference;
    private GeneralJournal returnedGeneralJournal;

    private boolean confirmManadatoryData;
    private StringBuilder errorMessageBuilder;
    private StringBuilder errorNoNeedMessageBuilder;

    private String summaryDebitString;
    private String summaryCreditString;
    private String differenceString;

    private boolean modifyJournalAbility = true;
    private AccountsSystemSettings accountsSystemSettings;
    private Integer revisionAccount;
    private Integer glAccountLoad;

    private Date dateFrom;
    private Date dateTo;
    private boolean reviewByDate;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private CompanyService companyService;
    @EJB
    private GeneraljournalDetailsService detailsService;
    @EJB
    private GlAdminUnitService adminUnitService;
    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private BranchService branchService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private TobyUserYearService tobyUserYearService;

    @EJB
    private AccountsSystemSettingsService accountsSystemSettingsService;
    @EJB
    private CloseAndSaveMonthService closeAndSaveMonthService;

    public GeneralJournalBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            revisionAccount = 2;
            load();
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "init");
        }

    }

    @Override
    public String save() {
        try {
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

            setParentRowAdded(false);

            Map<String, String> userDDs = getUserData().getUserDDs();

            String dateFrom = new SimpleDateFormat("MM").format(selectedGeneralJournal.getGeneralData());
            Integer numberOfMonth = Integer.parseInt(dateFrom);
            closeAndSaveMonthList = closeAndSaveMonthService.getCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchId(getUserData().getUserBranch().getId(), glYearSelection.getId(), numberOfMonth);

            if (closeAndSaveMonthList != null && !closeAndSaveMonthList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء قيد فى شهر مغلق"));
                setShowMessageGeneral(true);
                return "";
            }

            if (glYearSelection.getOpenning() != null && glYearSelection.getOpenning() == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء قيد فى سنة مغلقة"));
                setShowMessageGeneral(true);
                return "";
            }
            try {
                showMessageGeneral = Boolean.TRUE;
                showMessageDetails = Boolean.FALSE;
                //flags =selectedGeneralJournal.getPost_flag();

                if (selectedCompany != null && selectedGeneralJournal != null) {
                    if (selectedGeneralJournal.getGeneralData().compareTo(glYearSelection.getDateFrom()) >= 0 && selectedGeneralJournal.getGeneralData().compareTo(glYearSelection.getDateTo()) <= 0 && closeMonthCheck == true) {
                        TobyCompany company = new TobyCompany();
                        company.setId(getSelectedCompany());

                        if (selectedDocumentsType != null) {
                            selectedGeneralJournal.setGeneralType(symbolService.getSymbol(selectedDocumentsType));
                        }
                        /* if (generalDocument == null) {
                         generalDocument = generaljournalService.getMaxSerialToSelectedDocumentsType(selectedDocumentsType);
                         if (generalDocument == null) {
                         generalDocument = 1;

                         } else {
                         generalDocument = generalDocument + 1;
                         }
                         }*/
                        //selectedGeneralJournal.setGeneralDecument(generalDocument);
                        selectedGeneralJournal.setCompanyId(company);
                        if (selectedGeneralJournal.getId() == null) {
                            selectedGeneralJournal.setCreatedBy(getUserData().getUser());
                            selectedGeneralJournal.setCreationDate(new Date());
                        } else {
                            selectedGeneralJournal.setModifiedBy(getUserData().getUser());
                            selectedGeneralJournal.setModificationDate(new Date());
                        }

                        selectedGeneralJournal.setMarkEdit(Boolean.FALSE);
                        // selectedGeneralJournal.setMacId(getMacId());
//                selectedGeneralJournal.setPost_flag(isValue1());
                        //author by me
                        // selectedGeneralJournal.setPost_flag(Boolean.TRUE);
                        selectedGeneralJournal.setBranchId(branchService.findBranch(getUserData().getSelectedBranch()));
                        if (selectedGeneralJournal.getSerial() == null) {
                            selectedGeneralJournal.setSerial(0);
                        }
                        selectedGeneralJournal.setGlYear(getUserData().getGlYear());
                        returnedGeneralJournal = generaljournalService.addGeneralJournal(selectedGeneralJournal);
                        if (uri != null && !uri.contains("shiftingJournals")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                        }
                        generalDocument = null;

                        if (uri.contains("generaljournal")) {
                            generalList = generaljournalService.getALLGeneralJournalByBranchIdAndYear(getUserData().getSelectedBranch(), glYearSelection);
                            isModifiable();
                        }
                        /*  else {
                         maxId = generaljournalService.findMaxId();
                         }*/
                    } else if (closeMonthCheck == false) {
                        selectedGeneralJournal.setMarkEdit(Boolean.TRUE);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء قيد جديد في شهر تم اغلاقه"));
                    } else {
                        selectedGeneralJournal.setMarkEdit(Boolean.TRUE);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار تاريخ فى السنه الماليه المحدده"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                selectedGeneralJournal.setMarkEdit(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
            }

            return "";
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "init");
            return null;
        }
    }

    public void saveReviewAccounting() throws CloneNotSupportedException {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (generalReviewList != null && !generalReviewList.isEmpty() && !reviewByDate) {
                for (GeneralJournal detailsReview : generalReviewList) {
                    detailsReview.setPost_flag(true);
                    detailsReview.setPostFlagReview("تمت مراجعته");
                }
                try {
                    generaljournalService.addGeneralJournalsForReviewing(generalReviewList);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            } else if (reviewByDate && (dateFrom != null || dateTo != null)) {
                try {
                    generaljournalService.updateGeneralJournalsForReviewing(dateFrom, dateTo, getUserData().getUserBranch().getId(), 1, "تمت مراجعته");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            }
            /* if (selectedGeneralJournal != null) {
             edit();
             save();
             } else {
             showMessageGeneral = true;
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("MUST_SELECT_Entry"), getUserData().getUserDDs().get("SAVED_ERROR")));
             }*/
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "saveReviewAccounting");
        }
    }

    public void undoReview() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            if (generalReviewList != null && !generalReviewList.isEmpty() && !reviewByDate) {
                for (GeneralJournal detailsReview : generalReviewList) {
                    detailsReview.setPost_flag(false);
                    detailsReview.setPostFlagReview("");
                }
                try {
                    generaljournalService.addGeneralJournalsForReviewing(generalReviewList);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            } else if (reviewByDate && (dateFrom != null || dateTo != null)) {
                try {
                    generaljournalService.updateGeneralJournalsForReviewing(dateFrom, dateTo, getUserData().getUserBranch().getId(), 0, null);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "undoReview");
        }

    }

    public String getMacId() {
        try {
            StringBuilder sb = new StringBuilder();
            try {
                ip = InetAddress.getLocalHost();
                // System.out.println("ip= " + ip);

//			System.out.println("Current IP address : " + ip.getHostAddress());
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);

                byte[] mac = network.getHardwareAddress();

//			System.out.print("Current MAC address : ");
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                System.out.println(sb.toString());

            } catch (UnknownHostException e) {

                e.printStackTrace();
                System.out.println(e);

            } catch (SocketException e) {

                e.printStackTrace();
                System.out.println(e);
            }
            return sb.toString();
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "getMacId");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            detailsList = new ArrayList<>();
            details = new ArrayList<>();
            journalDetailSelected = new GeneralJournalDetails();
            closeAndSaveMonthList = new ArrayList<>();
            detail = new GeneralJournalDetails();
            closeMonthCheck = true;
            generalJournalShowBeanList = new ArrayList<>();
            generalReviewList = new ArrayList<>();
            stickyHeader = false;
            printType = 0;
            modifyJournalAbility = false;
            reviewByDate = false;
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId());
            if (getUserData().getCompany() != null && getUserData().getCompany().getId() != null && getUserData().getUserBranch() != null) {

                glYearList = tobyUserYearService.findYearByUserId(getUserData().getUser().getId(), getUserData().getUserBranch());
                if (glYearList != null && !glYearList.isEmpty()) {
                    setGlYearConverter(new GlYearConverter(glYearList));
                }

                if (getUserData().getGlYear() != null) {
                    setGlYearSelection(getUserData().getGlYear());
                } else {
                    setGlYearSelection((glYearList == null || glYearList.isEmpty()) ? null : glYearList.get(0));
                }
                uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

                if (glYearSelection != null) {
                    generalList = generaljournalService.getALLGeneralJournalByBranchIdAndYear(getUserData().getSelectedBranch(), glYearSelection);
                    correctGlYear();

                    if (uri.contains("reviewaccounting")) {
                        List<GeneralJournal> generalJournals = new ArrayList<>();
                        for (GeneralJournal gj : generalList) {
                            if (gj.getGeneralType() != null && (gj.getGeneralType().getSerial() < 1000 || gj.getGeneralType().getSerial() > 2000)) {
                                if (revisionAccount != null && revisionAccount == 0 && !gj.isPost_flag()) {
                                    generalJournals.add(gj);
                                } else if (revisionAccount != null && revisionAccount == 1 && gj.isPost_flag()) {
                                    generalJournals.add(gj);
                                } else if (revisionAccount != null && revisionAccount == 2) {
                                    generalJournals.add(gj);
                                }
                            }
                            if (gj.isPost_flag() && gj.getPostFlagReview() != null) {
                                gj.setPostFlagReview("تمت مراجعته");
                            }
                        }
                        if (generalJournals != null && !generalJournals.isEmpty()) {
                            generalList = new ArrayList<>(generalJournals);
                        }else{
                            generalList = new ArrayList<>();
                        }
                    }

                    isModifiable();
                } else {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    try {
                        fc.getExternalContext().redirect("../base/financailyear.xhtml");
                    } catch (IOException ex) {
                        Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                glAccountLoad = 1;
//            generalList = generaljournalService.getALLGeneralJournalByBranchId(getUserData().getSelectedBranch());
//            generalList = generaljournalService.getALLGeneralJournalByCompanyId(getUserData().getCompany().getId());
                selectedCompany = getUserData().getCompany().getId();
                // glAccounts = accountService.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
                glAccounts = accountService.getBranchGLAccountsActiveWithException(getUserData().getUserBranch().getId());
                costCenterList = costCenterService.getAllSubCostCenterByBranchIdActive(getUserData().getUserBranch().getId());
                glAdminunitlist = adminUnitService.getAllSubAdminUnitByBranchIdActive(getUserData().getUserBranch().getId());
                currencylist = currencyService.getAllCurrenciesHavingRatesByCompanyId(getUserData().getCompany().getId());
                documentsTypes = symbolService.getDocumentsTypes(selectedCompany);
                //  branchService.getAllBranchByCompanyId(selectedCompany);
            } else {
                companies = new ArrayList<>();
                companies = companyService.getAllCompanies();
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "load");
        }

    }

    private void isModifiable() {
        try {
            if (accountsSystemSettings != null) {
                if (accountsSystemSettings.getJournalsEdit().equalsIgnoreCase("JOURNAL_ENTRY_ENOUGH")) {
                    for (GeneralJournal gj : generalList) {

                        if (gj.getCreatedBy() != null && getUserData().getUser() != null && gj.getCreatedBy().getId().compareTo(getUserData().getUser().getId()) == 0) {
                            gj.setUserModifyAbility(false);
                        } else {
                            gj.setUserModifyAbility(true);
                        }
                    }
                }
            }

            /*     for (GeneralJournal gj : generalList) {
             GeneralJournalShowBean generalJournalShowBean = new GeneralJournalShowBean();
             String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(gj.getCreationDate());
             generalJournalShowBean.setGeneralDate(formatDateFrom);
             generalJournalShowBean.setGeneralDocument(gj.getGeneralDecument() != null ? gj.getGeneralDecument() : null);
             generalJournalShowBean.setGeneralNumber(gj.getGeneralNumber());
             generalJournalShowBean.setSerial(gj.getSerial());
             generalJournalShowBean.setGeneralStatement(gj.getGeneralStatement());
             generalJournalShowBean.setPost_flag(gj.isPost_flag());
             generalJournalShowBean.setGeneralType(gj.getGeneralType().getName());
             //   generalJournalShowBean.setMarkEdit(gj.getMarkEdit() == null ? gj.getMarkEdit() : null);
             generalJournalShowBeanList.add(generalJournalShowBean);
             }*/
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "isModifiable");
        }
    }

    public void loadgeneraljournalList() {
        try {
            generalList = generaljournalService.getALLGeneralJournalByBranchIdAndYear(getUserData().getSelectedBranch(), glYearSelection);
            correctGlYear();
            getUserData().setGlYear(glYearSelection);
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("userLogInData", getUserData());
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "loadgeneraljournalList");
        }
    }

    public void correctGlYear() {
        try {
            if (generalList != null && !generalList.isEmpty()) {
                if (generalList.get(0).getGlYear() == null) {
                    if (glYearSelection.getYear().compareTo(2018) == 0) {
                        for (GeneralJournal gj : generalList) {
                            if (gj.getGlYear() == null) {
                                gj.setGlYear(glYearSelection);
                            }
                        }
                        generaljournalService.addGeneralJournalForCorrectence(generalList);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "correctGlYear");
        }
    }

    public void reload() {
        try {
            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            getSelectedGeneralJournal().setMarkEdit(Boolean.FALSE);
            /*if (selectedGeneralJournal.getGeneralType() == null) {
             generalList.remove(selectedGeneralJournal);
             }*/
            if (generalGeneralReset == null) {
                for (Iterator<GeneralJournal> iterator = generalList.iterator(); iterator.hasNext();) {
                    GeneralJournal generalJournal = new GeneralJournal();
                    generalJournal = (GeneralJournal) iterator.next();
                    if (generalJournal.getSerial() != null && generalJournal.getSerial().compareTo(selectedGeneralJournal.getSerial()) == 0) {
                        iterator.remove();
                    }
                }
            } else {
                selectedGeneralJournal = generalGeneralReset;
                int f = 0;
                for (Iterator<GeneralJournal> iterator = generalList.iterator(); iterator.hasNext();) {
                    if (iterator.next().getSerial().compareTo(selectedGeneralJournal.getSerial()) == 0) {
                        generalList.set(f, selectedGeneralJournal);
                    }
                    f++;
                }
                generalGeneralReset = null;
            }
            setParentRowAdded(false);
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "reload");
        }
    }

    public void reloadDetails() throws CloneNotSupportedException {
        try {
            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            detail.setMarkEdit(Boolean.FALSE);
            detail.setMarkDisable(Boolean.FALSE);

            Iterator it = detailsList.iterator();
            int f = 0;
            while (it.hasNext()) {
                GeneralJournalDetails gjd = new GeneralJournalDetails();
                gjd = (GeneralJournalDetails) it.next();
                if (gjd.getIndex().compareTo(detail.getIndex()) == 0) {
                    if (detail.getId() != null || detailReset != null) {
                        if (detailReset != null) {
                            detail = (GeneralJournalDetails) detailReset.clone();
                            if (detailReset.getGlACCOUNTId() != null) {
                                detail.setGlACCOUNTId((GlAccount) detailReset.getGlACCOUNTId().clone());
                            }
                            if (detailReset.getAdminUnitId() != null) {
                                detail.setAdminUnitId((GlAdminUnit) detailReset.getAdminUnitId().clone());
                            }
                            if (detailReset.getCostCenterId() != null) {
                                detail.setCostCenterId((CostCenter) detailReset.getCostCenterId().clone());
                            }
                            detailReset = null;
                        }

                        detailsList.set(f, detail);
                    } else {
                        it.remove();
                        index--;
                    }
                }
                f++;
            }
            setRowAdded(false);
            showOrHideHeader(detailsList);
            for (GeneralJournalDetails journalDetails : detailsList) {
                journalDetails.setMarkDisable(Boolean.FALSE);
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "reloadDetails");
        }
    }

    @Override
    public String getScreenName() {
        return "";
    }

    public void onRowSelect(SelectEvent e) throws CloneNotSupportedException {

        try {

            if (checkIfTheSameRowChoosen(e)) {
                if (CheckDetailsConditionsToShowDialog()) {
                    modifyJournalAbility = true;
                    if (accountsSystemSettings != null && accountsSystemSettings.getJournalsEdit().equalsIgnoreCase("JOURNAL_ENTRY_ENOUGH")) {
                        if (selectedGeneralJournal.getCreatedBy().getId().compareTo(getUserData().getUser().getId()) == 0) {
                            selectedGeneralJournal.setUserModifyAbility(false);
                        } else {
                            selectedGeneralJournal.setUserModifyAbility(true);
                        }
                    }
                    evNew = e;
                    ev = e;
                    journalDocumentArrangedReportBeanList = new ArrayList<>();
                    selectedGeneralJournal = (GeneralJournal) e.getObject();
                    showMessageGeneral = Boolean.FALSE;
                    showMessageDetails = Boolean.FALSE;
                    if (selectedGeneralJournal != null && selectedCompany != null) {
                        detailsList = detailsService.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        cloneGeneralJournalDetails(detailsList);
                        showOrHideHeader(detailsList);
                        if (!selectedGeneralJournal.isUserModifyAbility()) {
                            modifyJournalAbility = false;
                        }
                        index = 0;
                        for (GeneralJournalDetails gjd : detailsList) {
                            index++;
                            gjd.setIndex(index);
                        }
                    }
                    view = Boolean.FALSE;
                    rowsDeleted = new ArrayList<>();

                    fillSummary();
                    setRowAdded(false);
                } else {
                    OpenDlg("dlg1");
                }
            }
        } catch (Exception ex) {
            saveError(ex, "GeneralJournalBean", "onRowSelect");
        }

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:generalDetails");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":form:generalDetails");
        }
        
        updateAuthorized();
    }

    private void updateAuthorized() {
        if(detailsList != null && !detailsList.isEmpty()){
            EditGeneralJournal = true;
        }else{
            EditGeneralJournal = false;
        }
    }

    public void cloneGeneralJournalDetails(List<GeneralJournalDetails> details) throws CloneNotSupportedException {
        try {
            if (details != null && !details.isEmpty()) {
                List<GeneralJournalDetails> generalJournalDetailses = new ArrayList<>();
                for (GeneralJournalDetails gjd : details) {
                    GeneralJournalDetails deta = new GeneralJournalDetails();
                    deta = gjd;
                    if (gjd.getGlACCOUNTId() != null) {
                        deta.setGlACCOUNTId((GlAccount) gjd.getGlACCOUNTId().clone());
                    }
                    if (gjd.getAdminUnitId() != null) {
                        deta.setAdminUnitId((GlAdminUnit) gjd.getAdminUnitId().clone());
                    }
                    if (gjd.getCostCenterId() != null) {
                        deta.setCostCenterId((CostCenter) gjd.getCostCenterId().clone());
                    }
                    generalJournalDetailses.add(deta);
                }
                if (!generalJournalDetailses.isEmpty()) {
                    detailsList = new ArrayList<>(generalJournalDetailses);
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "cloneGeneralJournalDetails");
        }
    }

    public void repeatGeneralJournal() throws CloneNotSupportedException {
        try {
            if (glYearSelection.getOpenning() == null || glYearSelection.getOpenning() == 0) {

                if (!isParentRowAdded()) {
                    if (selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
                        GeneralJournal generalJournalNew = new GeneralJournal();
                        try {
                            generalJournalNew = (GeneralJournal) selectedGeneralJournal.clone();

                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("Errorin duplication");
                            return;
                        }
                        generalJournalNew.setId(null);
                        generalJournalNew.setCreatedBy(getUserData() != null ? getUserData().getUser() : null);
                        generalJournalNew.setCreationDate(new Date());
                        generalJournalNew.setPost_flag(false);
                        generalJournalNew = generaljournalService.addGeneralJournal(generalJournalNew);
                        if (accountsSystemSettings != null) {
                            if (accountsSystemSettings.getJournalsEdit().equalsIgnoreCase("JOURNAL_ENTRY_ENOUGH")) {
                                if (generalJournalNew.getCreatedBy().getId().compareTo(getUserData().getUser().getId()) == 0) {
                                    generalJournalNew.setUserModifyAbility(false);
                                    modifyJournalAbility = false;
                                } else {
                                    generalJournalNew.setUserModifyAbility(true);
                                }
                            }
                        }
                        for (GeneralJournal journal : generalList) {
                            journal.setMarkEdit(Boolean.FALSE);
                        }

                        generalList.add(generalJournalNew);

                        List<GeneralJournalDetails> gjds = new ArrayList<>();

                        detailsList = detailsService.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        showOrHideHeader(detailsList);

                        selectedGeneralJournal = generalJournalNew;

                        for (GeneralJournalDetails journalDetails : detailsList) {

                            journalDetails.setId(null);
                            journalDetails.setGeneralJournalId(generalJournalNew);
                            journalDetails.setIndex(++index);
                            gjds.add(journalDetails);
                        }

                        detailsList = detailsService.addGenDetalils(gjds, null);
                        cloneGeneralJournalDetails(detailsList);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
                    showMessageGeneral = Boolean.TRUE;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن انشاء قيد فى سنة مغلقة"));
                setShowMessageGeneral(true);
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "repeatGeneralJournal");
        }
    }

    public void repeatGeneralJournalDetail() {
        try {

            adddetails();
            GeneralJournalDetails newDetail = new GeneralJournalDetails();
            newDetail = details.get(details.size() - 1);
            try {
                newDetail = (GeneralJournalDetails) journalDetailSelected.clone();
                newDetail.setGlACCOUNTId(journalDetailSelected.getGlACCOUNTId() != null ? (GlAccount) journalDetailSelected.getGlACCOUNTId().clone() : null);
                newDetail.setAdminUnitId(journalDetailSelected.getAdminUnitId() != null ? (GlAdminUnit) journalDetailSelected.getAdminUnitId().clone() : null);
                newDetail.setCostCenterId(journalDetailSelected.getCostCenterId() != null ? (CostCenter) journalDetailSelected.getCostCenterId().clone() : null);
                newDetail.setId(null);
                details = new ArrayList<>();
                detailsList.remove(detailsList.size() - 1);
                detailsList.add(newDetail);
                dAmount = newDetail.getDebitAmount() != null ? newDetail.getDebitAmount() : BigDecimal.ZERO;
                cAmount = newDetail.getCreditamount() != null ? newDetail.getCreditamount() : BigDecimal.ZERO;
                setRowAdded(false);
                for (GeneralJournalDetails generalJournal : detailsList) {
                    generalJournal.setMarkDisable(Boolean.FALSE);
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error in duplication");
                return;
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "repeatGeneralJournalDetail");
        }

    }

    public void fillSummary() {
        try {
            setSummaryCredit(BigDecimal.ZERO);
            setSummaryDebit(BigDecimal.ZERO);
            for (GeneralJournalDetails gjd : detailsList) {
                setSummaryCredit(getSummaryCredit().add(gjd.getCreditamountLocal() == null ? BigDecimal.ZERO : gjd.getCreditamountLocal()));
                summaryCredit = summaryCredit.setScale(2, BigDecimal.ROUND_UP);
                setSummaryDebit(getSummaryDebit().add(gjd.getDebitAmountLocal() == null ? BigDecimal.ZERO : gjd.getDebitAmountLocal()));
                summaryDebit = summaryDebit.setScale(2, BigDecimal.ROUND_UP);
            }

            BigDecimal diff = summaryDebit.subtract(summaryCredit);

            difference = diff.setScale(2).toString();

            DecimalFormat df = new DecimalFormat("#,##0.00");
            summaryDebitString = df.format(summaryCredit);
            summaryCreditString = df.format(summaryDebit);
            difference = df.format(diff);
            // differenceString = df.format(difference);
//        int res;
//        res = diff.compareTo(BigDecimal.ZERO);
//
//        BigDecimal minOne = new BigDecimal(-1);
//
//        if (res == 0) {
//            diff = diff.multiply(minOne);
//            difference = "القيد متزن";
//
//        } else if (res == 1) {
//            difference = diff.setScale(2) + " مدين ";
//
//        } else {
//            diff = diff.multiply(minOne);
//
//            difference = diff.setScale(2) + " دائن ";
//        }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "fillSummary");
        }
    }

    public Boolean fillSubSummary(List<GeneralJournalDetails> detailses) {
        try {

            BigDecimal credit = BigDecimal.ZERO;
            BigDecimal debit = BigDecimal.ZERO;
            for (GeneralJournalDetails gjd : detailses) {
                credit = credit.add(gjd.getCreditamount() == null ? BigDecimal.ZERO : gjd.getCreditamount());
                debit = debit.add(gjd.getDebitAmount() == null ? BigDecimal.ZERO : gjd.getDebitAmount());
            }

            return credit.equals(debit);
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "fillSubSummary");
            return null;
        }
    }

    public void onRowUnselect() {
        try {
            if (!details.isEmpty()) {
                view = Boolean.TRUE;
                journalDetailSelected = details.get(details.size() - 1);
            } else {
                view = Boolean.FALSE;
                journalDetailSelected = null;
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "onRowUnselect");
        }
    }

    public void onRowSelectDetails(SelectEvent e) {
        try {
            currencyRate = null;
            journalDetailSelected = (GeneralJournalDetails) e.getObject();
            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            if (selectedGeneralJournal.isPost_flag() == Boolean.TRUE) {
                view = Boolean.FALSE;
            } else {
                view = Boolean.TRUE;

                if (journalDetailSelected.getCreditamountLocal() != null) {
                    viewCredit = true;
                    viewDebit = false;
                } else {
                    viewCredit = false;
                    viewDebit = true;
                }
            }
        } catch (Exception ex) {
            saveError(ex, "GeneralJournalBean", "onRowSelectDetails");
        }

    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (selectedGeneralJournal != null) {
                showMessageGeneral = Boolean.TRUE;
                showMessageDetails = Boolean.FALSE;
                try {
                    if (selectedGeneralJournal.getId() != null && (glYearSelection.getOpenning() == null || glYearSelection.getOpenning() == 0)) {
                        generaljournalService.deleteGeneralJournal(selectedGeneralJournal);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                        generalList = generaljournalService.getALLGeneralJournalByBranchIdAndYear(getUserData().getSelectedBranch(), glYearSelection);
                        isModifiable();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "تم اغلاق هذه السنة"));
                    }
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "delete");
        }

    }

    public void deleteAll() {
        try {
            if (details != null && !details.isEmpty()) {
                rowsDeleted.addAll(details);
                for (GeneralJournalDetails detaill : details) {
                    for (Iterator<GeneralJournalDetails> iterator = detailsList.iterator(); iterator.hasNext();) {
                        if (iterator.next().getIndex().compareTo(detaill.getIndex()) == 0) {
                            confirmsCounter++;
                            iterator.remove();
                        }
                    }
                }
//            detailsList.removeAll(details);
                delete = Boolean.TRUE;
                fillSummary();
            }
            detailsListCopy = new ArrayList<>(detailsList);
            setRowAdded(false);
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "deleteAll");
        }

    }

    public void edit() throws CloneNotSupportedException {
        try {
            if (glYearSelection.getOpenning() == null || glYearSelection.getOpenning() == 0) {
                if (!isParentRowAdded()) {
                    setParentRowAdded(true);
                    generalGeneralReset = (GeneralJournal) selectedGeneralJournal.clone();
                    showMessageGeneral = Boolean.FALSE;
                    showMessageDetails = Boolean.FALSE;
                    for (GeneralJournal generalJournal : generalList) {
                        generalJournal.setMarkEdit(Boolean.FALSE);
                    }
                    selectedGeneralJournal.setMarkEdit(Boolean.TRUE);
                    if (selectedGeneralJournal.getGeneralDecument() != null) {
                        generalDocument = selectedGeneralJournal.getGeneralDecument();
                    }
                    setSelectedDocumentsType(selectedGeneralJournal.getGeneralType().getId());
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
                    showMessageGeneral = Boolean.TRUE;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن التعديل في سنة تم اغلاقها"));
                showMessageGeneral = Boolean.TRUE;
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "edit");
        }
    }

//    public void onCompanyChange() {
//        if (selectedCompany != null) {
//            generalList = generaljournalService.getALLGeneralJournalByCompanyId(selectedCompany);
//            glAccounts = accountService.getCompanyGLAccountsActive(selectedCompany);
//            costCenterList = costCenterService.getCompanyCostCentersActive(selectedCompany);
//            glAdminunitlist = adminUnitService.getAllAdminUnitByCompanyIdActive(selectedCompany);
//            //currencylist = currencyService.getAllCurrencyByCompanyId(selectedCompany);
//            documentsTypes = symbolService.getDocumentsTypes(selectedCompany);
//        }
//    }
    public void ondocumentsTypesChange() {
        /*  generalDocument = generaljournalService.getMaxSerialToSelectedDocumentsType(selectedDocumentsType);
         if (generalDocument == null) {
         generalDocument = 1;

         } else {
         generalDocument = generalDocument + 1;
         }*/

    }

    public String adddetails() {
        try {
//        if (details != null && details.getGlACCOUNTId() != null && (cAmount != null || dAmount != null)) {

            selectedAccount = null;
            accountName = null;
            cAmount = null;
            dAmount = null;
            disableAccount = Boolean.TRUE;
            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            debitDisabled = Boolean.FALSE;
            creditDisabled = Boolean.FALSE;
            Integer serial;
            GeneralJournalDetails detailsnew = new GeneralJournalDetails();

            if (!isRowAdded() && selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
                loadGlAccountsBasedDocumentType();
                setRowAdded(true);
                if (selectedCompany != null && selectedGeneralJournal != null) {
                    for (GeneralJournalDetails generalJournal : detailsList) {
                        generalJournal.setMarkEdit(Boolean.FALSE);
                        generalJournal.setMarkDisable(Boolean.TRUE);
                    }

                    // detailsList.add(detailsnew);
                    TobyCompany tobyCompany = new TobyCompany();

                    tobyCompany.setId(selectedCompany);
                    detailsnew.setCompanyId(tobyCompany);
                    detailsnew.setGeneralJournalId(selectedGeneralJournal);
                    detailsnew.setBranchId(getUserData().getUserBranch());
                    detailsnew.setCreatedBy(getUserData().getUser());
                    detailsnew.setDiscribtion(selectedGeneralJournal.getGeneralStatement());
                    // To copy generalstatment from head to discription detail
                    detailsnew.setGlACCOUNTId(new GlAccount());
                    detailsnew.setCostCenterId(new CostCenter());
                    detailsnew.setAdminUnitId(new GlAdminUnit());

                    detailsnew.setGlAssistantAccount(new GlAccount());
                    index++;
                    detailsnew.setIndex(index);
                    detailsList.add(detailsnew);
                    showOrHideHeader(detailsList);

                    detailsnew.setMarkEdit(Boolean.TRUE);
                    detailsnew.setMarkDisable(Boolean.FALSE);
                    details.add(detailsnew);
                    detailReset = null;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
                showMessageDetails = Boolean.TRUE;
            }

            return "";
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "adddetails");
            return null;
        }

    }
/// rtdegdgdghf
    public void loadGlAccountsBasedDocumentType() {
        Symbol symbol = symbolService.findSymbol(selectedGeneralJournal.getGeneralType() != null ? selectedGeneralJournal.getGeneralType().getId() : null);
        if (symbol != null && symbol.getSerial() == 0 && glAccountLoad != null && glAccountLoad.compareTo(0) != 0) {
            glAccounts = accountService.getBranchAllGLAccountsActive(getUserData().getUserBranch().getId());
            glAccountLoad = 0;
        } else if (symbol != null && symbol.getSerial() != 0 && glAccountLoad != null && glAccountLoad.compareTo(2) != 0) {
            glAccounts = accountService.getBranchGLAccountsActiveWithException(getUserData().getUserBranch().getId());
            glAccountLoad = 2;
        }
    }

    public void add() {
        generalDocument = null;
        showMessageGeneral = Boolean.FALSE;
        showMessageDetails = Boolean.FALSE;

        if (!isParentRowAdded()) {

            setParentRowAdded(true);

            for (GeneralJournal generalJournal : generalList) {
                generalJournal.setMarkEdit(Boolean.FALSE);
            }

            GeneralJournal journalnew = new GeneralJournal();
            journalnew.setMarkEdit(Boolean.TRUE);
            journalnew.setSerial(0);
            selectedGeneralJournal = journalnew;
            generalList.add(0, journalnew);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
            showMessageGeneral = Boolean.TRUE;
        }

    }

//    public String addDetails() {
//        if (details != null && (cAmount != null || dAmount != null)) {
//            adddetails();
//        } else {
//            details.setMarkEdit(Boolean.TRUE);
//        }
//        return "";
//    }
    public String confirm() {
        validateConfirm();
        // detailsListCopy = new ArrayList<>();
        if (confirmManadatoryData) {
            confirmsCounter = 0;
            setRowAdded(false);
            showMessageDetails = Boolean.TRUE;

            GlAccount account = new GlAccount();
            account = accountService.findGlAccount(detail.getGlACCOUNTId().getId());
            detail.setCurrencyId(account.getCurrencyId());
            currencyOperations = currencyOperationService.getRatesByDates(detail.getCurrencyId().getId(), selectedGeneralJournal.getGeneralData(), getUserData().getCompany().getId());
            detail.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());
            detail.setGlACCOUNTId(account);

            TobyCompany company = new TobyCompany();
            company.setId(selectedCompany);
            detail.setCompanyId(company);
            detail.setBranchId(selectedGeneralJournal.getBranchId());
            detail.setModificationDate(new Date());
            detail.setModifiedBy(getUserData().getUser());
            if (cAmount != null) {
                detail.setCreditamount(cAmount);
                detail.setDebitAmount(dAmount);

                detail.setCreditamountLocal(cAmount.multiply(detail.getRate()));
                detail.setDebitAmountLocal(dAmount);

                viewCredit = true;
                viewDebit = false;
            }
            if (dAmount != null) {
                detail.setDebitAmount(dAmount);
                detail.setCreditamount(cAmount);

                detail.setDebitAmountLocal(dAmount.multiply(detail.getRate()));
                detail.setCreditamountLocal(cAmount);

                viewCredit = false;
                viewDebit = true;

            }

            if (detail.getAdminUnitId() != null && detail.getAdminUnitId().getId() != null && detail.getAdminUnitId().getId() != -1) {
                detail.setAdminUnitId(adminUnitService.findAdminUnit(detail.getAdminUnitId().getId()));
            }
            if (detail.getCostCenterId() != null && detail.getCostCenterId().getId() != null && detail.getCostCenterId().getId() != -1) {
                detail.setCostCenterId(costCenterService.findCostCenter(detail.getCostCenterId().getId()));
            }

            for (GeneralJournalDetails journalDetails : detailsList) {
                journalDetails.setMarkEdit(false);
                journalDetails.setMarkDisable(Boolean.FALSE);
            }

            detail.setMarkEdit(true);
            confirmsCounter++;

            if (detail.getGlAssistantAccount() != null && detail.getGlAssistantAccount().getId() != null) {

                detail.setGlAssistantAccount(accountService.findGlAccount(detail.getGlAssistantAccount().getId()));
            }
            detail.setGeneralJournalId(selectedGeneralJournal);
            if ((dAmount != null || cAmount != null) && detail.getGlACCOUNTId().getId() != null) {
                detail.setMarkEdit(Boolean.FALSE);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("SAVED_ERROR")));
                detail.setMarkEdit(Boolean.TRUE);
                return "";
            }

            fillSummary();
        } else {
            if (errorMessageBuilder.toString().length() > 11) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), (errorMessageBuilder.toString())));
            }
            if (errorNoNeedMessageBuilder.toString().length() > 26) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), (errorNoNeedMessageBuilder.toString())));
            }
            return "";
        }
//        if (detail.getGlACCOUNTId().getId() == null) {
//            detail.setMarkEdit(Boolean.TRUE);
//        }
        //  rowsDeleted.add(details);

        return "";
    }

    public void validateConfirm() {
        confirmManadatoryData = true;
        errorMessageBuilder = new StringBuilder();
        errorNoNeedMessageBuilder = new StringBuilder();
        GlAccount account;
        Integer x = 0;
        Integer y = 0;
        errorMessageBuilder.append("يجب اختيار ");
        errorNoNeedMessageBuilder.append("لا يمكن اختيار ");
        if (detail.getGlACCOUNTId() != null && detail.getGlACCOUNTId().getId() == null) {
            confirmManadatoryData = false;
            errorMessageBuilder.append("حساب ");
            x++;
        }
        account = accountService.findGlAccount(detail.getGlACCOUNTId().getId());

        if (account != null) {
            if (account.getCostCenter().toString().equalsIgnoreCase("CC_MANDATORY") && detail.getCostCenterId() != null && detail.getCostCenterId().getId() == -1) {
                if (x > 0) {
                    errorMessageBuilder.append("و ");
                }
                errorMessageBuilder.append("مركز تكلفة ");
                confirmManadatoryData = false;
                x++;
            }

            if (account.getAdministrativeUnit().toString().equalsIgnoreCase("ADMUNT_MANDATORY") && detail.getAdminUnitId() != null && detail.getAdminUnitId().getId() == -1) {
                if (x > 0) {
                    errorMessageBuilder.append("و ");
                }
                errorMessageBuilder.append("وحدة ادارية ");
                confirmManadatoryData = false;
                x++;
            }

            if (account.getAdministrativeUnit().toString().equalsIgnoreCase("ADMUNT_NONEED") && detail.getAdminUnitId() != null && detail.getAdminUnitId().getId() != -1) {
                if (y > 0) {
                    errorNoNeedMessageBuilder.append("و ");
                }
                errorNoNeedMessageBuilder.append("وحدة ادارية ");
                confirmManadatoryData = false;
                y++;
            }
            if (account.getCostCenter().toString().equalsIgnoreCase("CC_NONEED") && detail.getCostCenterId() != null && detail.getCostCenterId().getId() != -1) {
                if (y > 0) {
                    errorNoNeedMessageBuilder.append("و ");
                }
                errorNoNeedMessageBuilder.append("مركز تكلفة ");
                confirmManadatoryData = false;
                y++;
            }
            /* if (account.getCostCenter().toString().equalsIgnoreCase("ASSACC_NONEED") && detail.getGlAssistantAccount() == null) {
             if (y > 0) {
             errorNoNeedMessageBuilder.append("و ");
             }
             errorNoNeedMessageBuilder.append("حساب مساعد ");
             confirmManadatoryData = false;
             y++;
             }*/

 /*if (account.getAssistantAcc().toString().equalsIgnoreCase("ASSACC_MANDATORY") && detail.getGlAssistantAccount() == null) {
             if (x > 0) {
             errorMessageBuilder.append("و ");
             }
             errorMessageBuilder.append("حساب مساعد ");
             confirmManadatoryData = false;
             x++;
             }*/
            if (detail.getGlACCOUNTId() != null && detail.getGlACCOUNTId().getId() != null) {
                if (x > 0) {
                    errorMessageBuilder.append("لهذا الحساب");
                }
                if (y > 0) {
                    errorNoNeedMessageBuilder.append("لهذا الحساب");
                }
            }
        } else if (account != null && account.getCurrencyId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), ("هذا الحساب غير مربوط بعملة")));
            confirmManadatoryData = false;
        }

        /*if (confirmManadatoryData && ((dAmount == null) || (cAmount == null))) {
         detail.setMarkEdit(Boolean.FALSE);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("SAVED_ERROR")));
         confirmManadatoryData = false;
         }*/
    }

    public void savegeneraljournaldetails() {
        if (!isRowAdded()) {
            if ((detailsList.size() > 0 && !detailsList.isEmpty()) || delete) {
                Integer maxSerial = detailsService.getMaxSerialByGeneral(selectedGeneralJournal.getId());
                if (maxSerial == null) {
                    maxSerial = 1;
                } else {
                    maxSerial = maxSerial + 1;
                }
                showMessageGeneral = Boolean.FALSE;
                showMessageDetails = Boolean.TRUE;
//            BigDecimal sumCredit = new BigDecimal(BigInteger.ZERO, 2);
//            BigDecimal sumdebit = new BigDecimal(BigInteger.ZERO, 2);

                try {
                    if (isBalanced()) {
                        if (detailsList.size() > 0 && !detailsList.isEmpty()) {
//                        for (GeneralJournalDetails gernallistD : detailsList) {
//                            if (gernallistD.getCreditamount() != null) {
//                                sumCredit = sumCredit.add(gernallistD.getCreditamount());
//                            }
//                            if (gernallistD.getDebitAmount() != null) {
//                                sumdebit = sumdebit.add(gernallistD.getDebitAmount());
//                            }
//                        }
                        }
                        if (summaryDebit.setScale(2).equals(summaryCredit.setScale(2))) {
                            for (GeneralJournalDetails gernallistd : detailsList) {
                                initailizeObject(gernallistd, maxSerial);
                            }
                            detailsList = detailsService.addGenDetalils(detailsList, rowsDeleted);
                            delete = Boolean.FALSE;
                            detailsList = detailsService.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                            cloneGeneralJournalDetails(detailsList);
                            index = 0;
                            for (GeneralJournalDetails gjd : detailsList) {
                                index++;
                                gjd.setIndex(index);
                            }
                            confirmsCounter = 0;
                            detailReset = null;

                            showOrHideHeader(detailsList);

                            details = new ArrayList<>();
                            detailReset = null;
                            //showMessageDetails = Boolean.TRUE;
                            showMessageGeneral = Boolean.TRUE;
                            confirmsCounter = 0;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("JOURNAL_UNBLANCED")));
                        }

                    } else {
                        for (GeneralJournalDetails gernallistd : detailsList) {
                            initailizeObject(gernallistd, maxSerial);
                        }
                        detailsList = detailsService.addGenDetalils(detailsList, rowsDeleted);
                        delete = Boolean.FALSE;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                        details = new ArrayList<>();
                        detailsList = detailsService.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        cloneGeneralJournalDetails(detailsList);
                        index = 0;
                        for (GeneralJournalDetails gjd : detailsList) {
                            index++;
                            gjd.setIndex(index);
                        }
                        confirmsCounter = 0;
                    }

                    //  rowsDeleted = new ArrayList<>();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("SAVED_ERROR")));
                }
            }
            updateAuthorized();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("SAVED_ERROR")));
        }
        rowsDeleted = new ArrayList<>();
    }

    private void initailizeObject(GeneralJournalDetails gernallistd, Integer maxSerial) {
//        if (gernallistd.getId() == null) {
//            gernallistd.setSerial(maxSerial);
//            maxSerial = maxSerial + 1;
//        }

        if (gernallistd.getAdminUnitId() != null && gernallistd.getAdminUnitId().getId() != null) {
        } else {
            gernallistd.setAdminUnitId(null);
        }

        if (gernallistd.getCostCenterId() != null && gernallistd.getCostCenterId().getId() != null) {
        } else {
            gernallistd.setCostCenterId(null);
        }

        if (gernallistd.getGlACCOUNTId() != null && gernallistd.getGlACCOUNTId().getId() != null) {
        } else {
            gernallistd.setGlACCOUNTId(null);
        }

        if (gernallistd.getGlAssistantAccount() != null && gernallistd.getGlAssistantAccount().getId() != null) {
        } else {
            gernallistd.setGlAssistantAccount(null);
        }
    }

    public Boolean isBalanced() {
        AccountsSystemSettings journalBalanced = accountsSystemSettingsService.getInventoryByCompanyId(selectedCompany);
        if (journalBalanced != null) {
            if (journalBalanced.getJournalsDailyBalanced().equalsIgnoreCase("NO_WEIGHT_JOURNAL")) {
                return false;
            }

        }

        return true;
    }

    public void editgeneraldetails() throws CloneNotSupportedException {
        if (!isRowAdded()) {
            detailReset = new GeneralJournalDetails();
            detailReset = (GeneralJournalDetails) detail.clone();
            if (detail.getGlACCOUNTId() != null) {
                detailReset.setGlACCOUNTId((GlAccount) detail.getGlACCOUNTId().clone());
            }
            if (detail.getAdminUnitId() != null) {
                detailReset.setAdminUnitId((GlAdminUnit) detail.getAdminUnitId().clone());
            }
            if (detail.getCostCenterId() != null) {
                detailReset.setCostCenterId((CostCenter) detail.getCostCenterId().clone());
            }
            /* if (detail.getGlACCOUNTId() != null) {
             detailReset.setGlACCOUNTId((GlAccount) detail.getGlACCOUNTId().clone());
             }
             if (detail.getAdminUnitId() != null) {
             detailReset.setAdminUnitId((GlAdminUnit) detail.getAdminUnitId().clone());
             }
             if (detail.getCostCenterId() != null) {
             detailReset.setCostCenterId((CostCenter) detail.getCostCenterId().clone());
             }*/

            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            for (GeneralJournalDetails generalJournalDetail : detailsList) {
                generalJournalDetail.setMarkEdit(Boolean.FALSE);
                generalJournalDetail.setMarkDisable(Boolean.TRUE);
            }
            creditDisabled = Boolean.FALSE;
            debitDisabled = Boolean.FALSE;
            detail.setMarkEdit(Boolean.TRUE);

            dAmount = null;
            cAmount = null;

            if (detail.getDebitAmount() != null) {
                dAmount = detail.getDebitAmount();
            }
            if (detail.getCreditamount() != null) {
                cAmount = detail.getCreditamount();
            }

            if (detail.getCostCenterId() == null) {
                detail.setCostCenterId(new CostCenter());
            }

            if (detail.getAdminUnitId() == null) {
                detail.setAdminUnitId(new GlAdminUnit());
            }

            if (detail.getGlAssistantAccount() == null) {
                detail.setGlAssistantAccount(new GlAccount());
            }

            if (detail.getGlACCOUNTId() == null) {
                detail.setGlACCOUNTId(new GlAccount());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
            showMessageDetails = Boolean.TRUE;
        }
        fillSummary();
    }
    //    public void onCurrencyListChange() {
    //
    //        currencyRate = null;
    //        if (selectedCurrency != null) {
    //            currencyOperations = currencyOperationService.getOperationsByCurrency(selectedCurrency);
    //            details.setRate(currencyOperations.getRate());
    //        }
    //
    //    }

    public void deletegeneraldetails() {

        Map<String, String> userDDs = getUserData().getUserDDs();
        showMessageGeneral = Boolean.FALSE;
        showMessageDetails = Boolean.TRUE;
        if (detail.getId() != null) {
            delete = Boolean.TRUE;
            rowsDeleted.add(detail);
        }
        detailsList.remove(detail);
        confirmsCounter++;
        fillSummary();
        setRowAdded(false);
        //      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
//        if (details != null && selectedCompany != null && details.getId() != null) {
//            try {
//                detailsService.deleteGenDetalils(details);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
//                detailsList = detailsService.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
//            } catch (Exception e) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
//            }
//        } else {
//            detailsList.remove(details);
//        } 
    }

    public void debitRender(ValueChangeEvent event) {
        BigDecimal amount = (BigDecimal) event.getNewValue();
        if (amount != null && !amount.equals(BigDecimal.ZERO)) {
            dAmount = amount;
            cAmount = null;
            creditDisabled = Boolean.TRUE;
        } else {
            dAmount = null;
            creditDisabled = Boolean.FALSE;
        }
    }

    public void creditRender(ValueChangeEvent event) {
        BigDecimal amount = (BigDecimal) event.getNewValue();
        if (amount != null && !amount.equals(BigDecimal.ZERO)) {
            debitDisabled = Boolean.TRUE;
            cAmount = (BigDecimal) event.getNewValue();
            dAmount = null;
//         RequestContext.getCurrentInstance().update("form:debit");
        } else {
            cAmount = null;
            debitDisabled = Boolean.FALSE;
        }
    }

    public void onGlAccountChange() {

        GlAccount account;
        accountName = null;
        disableCC = Boolean.TRUE;
        disableAdminU = Boolean.TRUE;
        disableAccount = Boolean.TRUE;
//        details.setGlACCOUNTId(gAccount);
        if (detail.getGlACCOUNTId() != null && detail.getGlACCOUNTId().getId() != null) {
            account = accountService.findGlAccount(detail.getGlACCOUNTId().getId());
            detail.getGlACCOUNTId().setName(account.getName());
            if (account.getCostCenter().toString().equalsIgnoreCase("CC_MANDATORY")) {
                if (costCenterList != null) {
                    detail.getCostCenterId().setId(costCenterList.get(0).getId());
                    disableCC = Boolean.FALSE;
                }
            }
            if (account.getAdministrativeUnit().toString().equalsIgnoreCase("ADMUNT_MANDATORY")) {
                if (glAdminunitlist != null) {
                    detail.getAdminUnitId().setId(glAdminunitlist.get(0).getId());
                    disableAdminU = Boolean.FALSE;
                }

            }
            if (account.getAssistantAcc().toString().equalsIgnoreCase("ASSACC_MANDATORY")) {
                if (glAccounts != null) {
                    detail.getGlAssistantAccount().setId(glAccounts.get(0).getId());
                    disableAccount = Boolean.FALSE;
                }

            }
        }

    }

    public void resetForShiftingJournalScreen() {
        selectedGeneralJournal = new GeneralJournal();
        closeAndSaveMonthList = new ArrayList<>();
        selectedCompany = getUserData().getCompany().getId();

    }

    public void addExtraDateToDetails() {

        GlAccount account = new GlAccount();
        if (detail != null && detail.getGlACCOUNTId() != null) {
            account = accountService.findGlAccount(detail.getGlACCOUNTId().getId());
            detail.setCurrencyId(account.getCurrencyId());
            currencyOperations = new CurrencyOperation();
            currencyOperations = currencyOperationService.getRatesByDates(detail.getCurrencyId().getId(), selectedGeneralJournal.getGeneralData(), getUserData().getCompany().getId());
            detail.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());
        }
    }

    public void addDetailsForShiftingJournalScreen() {
        if (detailsList != null && !detailsList.isEmpty()) {
            datailsNotSavedReference = 0;
            Integer maxSerial = detailsService.getMaxSerialByGeneral(selectedGeneralJournal.getId());
            if (maxSerial == null) {
                maxSerial = 1;
            } else {
                maxSerial = maxSerial + 1;
            }
            try {
                for (GeneralJournalDetails gernallistd : detailsList) {
                    initailizeObject(gernallistd, maxSerial);
                }
                detailsList = detailsService.addGenDetalils(detailsList, rowsDeleted);
                delete = Boolean.FALSE;
            } catch (Exception e) {
                datailsNotSavedReference++;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("JOURNAL_UNBLANCED")));
            }

        }
    }

    public void CloseDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').hide();");
    }

    public void OpenDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').show();");
    }

    public boolean checkIfTheSameRowChoosen(SelectEvent e) {
        if (ev != null && e.getObject().equals(ev.getObject())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean CheckDetailsConditionsToShowDialog() {
        if (details != null && !details.isEmpty()) {
            if (details.get(0).getId() == null) {
                return false;
            }
        }
        if (confirmsCounter != null && confirmsCounter > 0) {
            return false;
        }
        if (detailReset != null) {
            return false;
        } else {
            return true;
        }
    }

    public void changeRowSelectedOk() throws CloneNotSupportedException {
        details = new ArrayList<>();
        detailReset = null;
        confirmsCounter = 0;
        onRowSelect(evNew);
    }

    public void changeRowSelectedCancel() {
        selectedGeneralJournal = (GeneralJournal) ev.getObject();
    }

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

//        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
        return true;
    }

    public void print() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
            journalDocumentArrangedReportBeanList = new ArrayList<>();
            for (GeneralJournalDetails generalJournalDetails : detailsList) {
                JournalDocumentArrangedReportBean journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
                journalDocumentArrangedReportBean.setJournalNum(selectedGeneralJournal.getSerial());
                journalDocumentArrangedReportBean.setDocumentNum(selectedGeneralJournal.getGeneralDecument());
                journalDocumentArrangedReportBean.setJournalDate(selectedGeneralJournal.getGeneralData());
                journalDocumentArrangedReportBean.setUser(selectedGeneralJournal.getCreatedBy().getName());
                journalDocumentArrangedReportBean.setGeneralType(selectedGeneralJournal.getGeneralType() != null ? selectedGeneralJournal.getGeneralType().getName() : null);
                if (summaryDebit.compareTo(summaryCredit) == 0) {
                    journalDocumentArrangedReportBean.setPosting("موزون");
                } else {
                    journalDocumentArrangedReportBean.setPosting("غير موزون");
                }
                if (selectedGeneralJournal != null && selectedGeneralJournal.isPost_flag()) {
                    journalDocumentArrangedReportBean.setPostFlag("مرحل");

                } else {
                    journalDocumentArrangedReportBean.setPostFlag("غير مرحل");
                }
                journalDocumentArrangedReportBean.setRemarks(selectedGeneralJournal.getGeneralStatement());
                journalDocumentArrangedReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                journalDocumentArrangedReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                journalDocumentArrangedReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                journalDocumentArrangedReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                journalDocumentArrangedReportBean.setCreditAmount(generalJournalDetails.getCreditamount() != null ? generalJournalDetails.getCreditamount() : BigDecimal.ZERO);
                journalDocumentArrangedReportBean.setDebitAmount(generalJournalDetails.getDebitAmount() != null ? generalJournalDetails.getDebitAmount() : BigDecimal.ZERO);
                journalDocumentArrangedReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                journalDocumentArrangedReportBean.setCreationDate(generalJournalDetails.getCreationDate() != null ? generalJournalDetails.getCreationDate() : null);
                journalDocumentArrangedReportBeanList.add(journalDocumentArrangedReportBean);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), ("يجب اختيار قيد اولا")));
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (detailsList != null && !detailsList.isEmpty()) {
            print();
            if (journalDocumentArrangedReportBeanList != null && !journalDocumentArrangedReportBeanList.isEmpty()) {
                if (printType == 1) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "generalJournalLandScapeReport.jasper", journalDocumentArrangedReportBeanList, "pdf");
                } else {
                    fillReport(prepareReport(), getUserData().getReportPath() + "generalJournalReport.jasper", journalDocumentArrangedReportBeanList, "pdf");
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن طباعة قيد بدون تفاصيل"));
            showMessageGeneral = Boolean.TRUE;
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        hashMap.put("journalDocumentDailyReportText", "بيان قيوداليومية");
        hashMap.put("journalNumberText", "رقم القيد");
        hashMap.put("generalTypeText", "نوع القيد");
        hashMap.put("journalDocumentText", "رقم السند");
        hashMap.put("documentTypeText", "نوع السند");
        hashMap.put("documentDateText", "تاريخ السند");
        hashMap.put("statementText", "البيان");

        hashMap.put("debitText", "مدين");
        hashMap.put("creditText", "دائن");
        hashMap.put("accountNumberText", "رقم الحساب");
        hashMap.put("accountNameText", "اسم الحساب");
        hashMap.put("costCenterText", "مركز التكلفة");
        hashMap.put("adminUnitText", "وحدة ادارية");
        hashMap.put("journalCreatorText", "اسم وتاريخ منشئ القيد");
        hashMap.put("journalDateText", "تاريخ انشاء القيد");
        hashMap.put("journaText", "قيد");
        hashMap.put("descriptionText", "الشرح");

        hashMap.put("journalDocumentReportText", userDDs.get("JOURNAL_DOCUMENT_REPORT"));
        hashMap.put("fromJournalDocumentTypeText", userDDs.get("FROM_JOURNAL_DOCUMENT_TYPE"));
        hashMap.put("toJournalDocumentTypeText", userDDs.get("TO_JOURNAL_DOCUMENT_TYPE"));
        hashMap.put("fromJournalDocumentNumberText", userDDs.get("FROM_JOURNAL_DOCUMENT_NUMBER"));
        hashMap.put("toJournalDocumentNumberText", userDDs.get("TO_JOURNAL_DOCUMENT_NUMBER"));
        hashMap.put("fromJournalNumberText", userDDs.get("FROM_JOURNAL_NUMBER"));
        hashMap.put("toJournalNumberText", userDDs.get("TO_JOURNAL_NUMBER"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("reviewAccountText", userDDs.get("REVIEW_ACCOUNT"));
        hashMap.put("journalDailyBalancedText", userDDs.get("JOURNALS_DAILY_BALANCED"));
        hashMap.put("fromUserText", userDDs.get("FROM_USER"));
        hashMap.put("toUserText", userDDs.get("TO_USER"));
        hashMap.put("generalNumberText", userDDs.get("GENERAL_NUMBER"));
        hashMap.put("documentNumberText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("reviewAccountingText", userDDs.get("REVIEW_ACCOUNTING"));
        hashMap.put("postingText", userDDs.get("POSTING"));
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("journalAddedByText", userDDs.get("JOURNAL_ADDED_BY"));
        hashMap.put("generalStatementText", userDDs.get("GERNAL_STATEMENT"));
        hashMap.put("remarksText", userDDs.get("REMARKS"));
        hashMap.put("summaryCreditString", summaryCreditString);
        hashMap.put("summaryDebitString", summaryDebitString);

        hashMap.put("tellerText", "المحاسب");
        hashMap.put("accountsText", "مدير الحسابات");
        hashMap.put("confirmationText", "اعتماد");

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //  hashMap.put("companyName", getUserData().getCompany().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", null);
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    public void showOrHideHeader(List<GeneralJournalDetails> journalDetailse) {
        if (journalDetailse.size() >= 20) {
            stickyHeader = true;
        } else {
            stickyHeader = false;
        }
    }

    

    public void info() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
    }

    public void onCostCenterChange() {
        if (detail.getCostCenterId() != null && detail.getCostCenterId().getId().compareTo(-1) == 0) {
            detail.setCostCenterId(new CostCenter());
        }

    }

    public void onAdminUnitChange() {
        if (detail.getAdminUnitId() != null && detail.getAdminUnitId().getId().compareTo(-1) == 0) {
            detail.setAdminUnitId(new GlAdminUnit());
        }
    }

    public List<TobyCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<TobyCompany> companies) {
        this.companies = companies;
    }

    public GeneralJournal getSelectedGeneralJournal() {
        return selectedGeneralJournal;
    }

    public void setSelectedGeneralJournal(GeneralJournal selectedGeneralJournal) {
        this.selectedGeneralJournal = selectedGeneralJournal;
    }

    /**
     * @return the detailsList
     */
    public List<GeneralJournalDetails> getDetailsList() {
        return detailsList;
    }

    /**
     * @param detailsList the detailsList to set
     */
    public void setDetailsList(List<GeneralJournalDetails> detailsList) {
        this.detailsList = detailsList;
    }

    /**
     * @return the generaljournalService
     */
    public GeneraljournalService getGeneraljournalService() {
        return generaljournalService;
    }

    /**
     * @param generaljournalService the generaljournalService to set
     */
    public void setGeneraljournalService(GeneraljournalService generaljournalService) {
        this.generaljournalService = generaljournalService;
    }

    /**
     * @return the companyService
     */
    public CompanyService getCompanyService() {
        return companyService;
    }

    /**
     * @param companyService the companyService to set
     */
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * @return the selectedCompany
     */
    public Integer getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(Integer selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the detailsService
     */
    public GeneraljournalDetailsService getDetailsService() {
        return detailsService;
    }

    /**
     * @param detailsService the detailsService to set
     */
    public void setDetailsService(GeneraljournalDetailsService detailsService) {
        this.detailsService = detailsService;
    }

    /**
     * @return the gAccount
     */
    public GlAccount getgAccount() {
        return gAccount;
    }

    /**
     * @param gAccount the gAccount to set
     */
    public void setgAccount(GlAccount gAccount) {
        this.gAccount = gAccount;
    }

    /**
     * @return the costCenter
     */
    public CostCenter getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public GlAdminUnit getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(GlAdminUnit adminUnit) {
        this.adminUnit = adminUnit;
    }

    public Integer getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Integer selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public GlAdminUnitService getAdminUnitService() {
        return adminUnitService;
    }

    public void setAdminUnitService(GlAdminUnitService adminUnitService) {
        this.adminUnitService = adminUnitService;
    }

    public CostCenterService getCostCenterService() {
        return costCenterService;
    }

    public void setCostCenterService(CostCenterService costCenterService) {
        this.costCenterService = costCenterService;
    }

    public GlAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(GlAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @return the gLAccountlist
     *
     * /
     **
     * @return the costCenterList
     */
    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    /**
     * @return the glAdminunitlist
     */
    public List<GlAdminUnit> getGlAdminunitlist() {
        return glAdminunitlist;
    }

    /**
     * @param glAdminunitlist the glAdminunitlist to set
     */
    public void setGlAdminunitlist(List<GlAdminUnit> glAdminunitlist) {
        this.glAdminunitlist = glAdminunitlist;
    }

    /**
     * @return the glAccounts
     */
    public List<GlAccount> getGlAccounts() {
        return glAccounts;
    }

    /**
     * @param glAccounts the glAccounts to set
     */
    public void setGlAccounts(List<GlAccount> glAccounts) {
        this.glAccounts = glAccounts;
    }

    /**
     * @return the branchService
     */
    public BranchService getBranchService() {
        return branchService;
    }

    /**
     * @param branchService the branchService to set
     */
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * @return the showMessageGeneral
     */
    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    /**
     * @param showMessageGeneral the showMessageGeneral to set
     */
    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    /**
     * @return the showMessageDetails
     */
    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    /**
     * @param showMessageDetails the showMessageDetails to set
     */
    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

//    public List<GeneralJournalDetails> getRowsSaved() {
//        return rowsDeleted;
//    }
//
//    public void setRowsSaved(List<GeneralJournalDetails> rowsDeleted) {
//        this.rowsDeleted = rowsDeleted;
//    }
    public List<Currency> getCurrencylist() {
        return currencylist;
    }

    public void setCurrencylist(List<Currency> currencylist) {
        this.currencylist = currencylist;
    }

    public Integer getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Integer selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public CurrencyOperation getCurrencyOperations() {
        return currencyOperations;
    }

    public void setCurrencyOperations(CurrencyOperation currencyOperations) {
        this.currencyOperations = currencyOperations;
    }

    public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(BigDecimal currencyRate) {
        this.currencyRate = currencyRate;
    }

    public List<Symbol> getDocumentsTypes() {
        return documentsTypes;
    }

    public void setDocumentsTypes(List<Symbol> documentsTypes) {
        this.documentsTypes = documentsTypes;
    }

    public Integer getSelectedDocumentsType() {
        return selectedDocumentsType;
    }

    public void setSelectedDocumentsType(Integer selectedDocumentsType) {
        this.selectedDocumentsType = selectedDocumentsType;
    }

    public Integer getGeneralDocument() {
        return generalDocument;
    }

    public void setGeneralDocument(Integer generalDocument) {
        this.generalDocument = generalDocument;
    }

    public Boolean getDebitDisabled() {
        return debitDisabled;
    }

    public void setDebitDisabled(Boolean debitDisabled) {
        this.debitDisabled = debitDisabled;
    }

    public Boolean getCreditDisabled() {
        return creditDisabled;
    }

    public void setCreditDisabled(Boolean creditDisabled) {
        this.creditDisabled = creditDisabled;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public BigDecimal getcAmount() {
        return cAmount;
    }

    public void setcAmount(BigDecimal cAmount) {
        this.cAmount = cAmount;
    }

    public BigDecimal getdAmount() {
        return dAmount;
    }

    public void setdAmount(BigDecimal dAmount) {
        this.dAmount = dAmount;
    }

    public List<GeneralJournalDetails> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<GeneralJournalDetails> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getDisableAccount() {
        return disableAccount;
    }

    public void setDisableAccount(Boolean disableAccount) {
        this.disableAccount = disableAccount;
    }

    public Boolean getDisableAdminU() {
        return disableAdminU;
    }

    public void setDisableAdminU(Boolean disableAdminU) {
        this.disableAdminU = disableAdminU;
    }

    public Boolean getDisableCC() {
        return disableCC;
    }

    public void setDisableCC(Boolean disableCC) {
        this.disableCC = disableCC;
    }

    public List<GeneralJournal> getGeneralList() {
        return generalList;
    }

    public void setGeneralList(List<GeneralJournal> generalList) {
        this.generalList = generalList;
    }

    public Boolean getFlags() {
        return flags;
    }

    public void setFlags(Boolean flags) {
        this.flags = flags;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }

    public boolean isValue2() {
        return value2;
    }

    public void setValue2(boolean value2) {
        this.value2 = value2;
    }

    /**
     * @return the summaryDebit
     */
    public BigDecimal getSummaryDebit() {
        return summaryDebit;
    }

    /**
     * @param summaryDebit the summaryDebit to set
     */
    public void setSummaryDebit(BigDecimal summaryDebit) {
        this.summaryDebit = summaryDebit;
    }

    /**
     * @return the summaryCredit
     */
    public BigDecimal getSummaryCredit() {
        return summaryCredit;
    }

    /**
     * @param summaryCredit the summaryCredit to set
     */
    public void setSummaryCredit(BigDecimal summaryCredit) {
        this.summaryCredit = summaryCredit;
    }

    /**
     * @return the glYearList
     */
    public List<GlYear> getGlYearList() {
        return glYearList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    public void setGlYearList(List<GlYear> glYearList) {
        this.glYearList = glYearList;
    }

    /**
     * @return the glYearId
     */
    public Integer getGlYearId() {
        return glYearId;
    }

    /**
     * @param glYearId the glYearId to set
     */
    public void setGlYearId(Integer glYearId) {
        this.glYearId = glYearId;
    }

    /**
     * @return the glYearSelection
     */
    public GlYear getGlYearSelection() {
        return glYearSelection;
    }

    /**
     * @param glYearSelection the glYearSelection to set
     */
    public void setGlYearSelection(GlYear glYearSelection) {
        this.glYearSelection = glYearSelection;
    }

    /**
     * @return the glYearConverter
     */
    public GlYearConverter getGlYearConverter() {
        return glYearConverter;
    }

    /**
     * @param glYearConverter the glYearConverter to set
     */
    public void setGlYearConverter(GlYearConverter glYearConverter) {
        this.glYearConverter = glYearConverter;
    }

    public boolean isRowAdded() {
        return rowAdded;
    }

    public void setRowAdded(boolean rowAdded) {
        this.rowAdded = rowAdded;
    }

    public boolean isParentRowAdded() {
        return parentRowAdded;
    }

    public void setParentRowAdded(boolean parentRowAdded) {
        this.parentRowAdded = parentRowAdded;
    }

    public List<GeneralJournalDetails> getDetails() {
        return details;
    }

    public void setDetails(List<GeneralJournalDetails> details) {
        this.details = details;
    }

    public GeneralJournalDetails getJournalDetailSelected() {
        return journalDetailSelected;
    }

    public void setJournalDetailSelected(GeneralJournalDetails journalDetailSelected) {
        this.journalDetailSelected = journalDetailSelected;
    }

    public GeneralJournalDetails getDetailTarget() {
        return detailTarget;
    }

    public void setDetailTarget(GeneralJournalDetails detailTarget) {
        this.detailTarget = detailTarget;
    }

    public GeneralJournalDetails getDetail() {
        return detail;
    }

    public void setDetail(GeneralJournalDetails detail) {
        this.detail = detail;
    }

    public Boolean getViewCredit() {
        return viewCredit;
    }

    public void setViewCredit(Boolean viewCredit) {
        this.viewCredit = viewCredit;
    }

    public Boolean getViewDebit() {
        return viewDebit;
    }

    public void setViewDebit(Boolean viewDebit) {
        this.viewDebit = viewDebit;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    /**
     * @return the closeAndSaveMonthList
     */
    public List<CloseAndSaveMonth> getCloseAndSaveMonthList() {
        return closeAndSaveMonthList;
    }

    /**
     * @param closeAndSaveMonthList the closeAndSaveMonthList to set
     */
    public void setCloseAndSaveMonthList(List<CloseAndSaveMonth> closeAndSaveMonthList) {
        this.closeAndSaveMonthList = closeAndSaveMonthList;
    }

    /**
     * @return the closeAndSaveMonth
     */
    public CloseAndSaveMonth getCloseAndSaveMonth() {
        return closeAndSaveMonth;
    }

    /**
     * @param closeAndSaveMonth the closeAndSaveMonth to set
     */
    public void setCloseAndSaveMonth(CloseAndSaveMonth closeAndSaveMonth) {
        this.closeAndSaveMonth = closeAndSaveMonth;
    }

    /**
     * @return the closeMonthCheck
     */
    public boolean isCloseMonthCheck() {
        return closeMonthCheck;
    }

    /**
     * @param closeMonthCheck the closeMonthCheck to set
     */
    public void setCloseMonthCheck(boolean closeMonthCheck) {
        this.closeMonthCheck = closeMonthCheck;
    }

    /**
     * @return the generalJournalShowBeanList
     */
    public List<GeneralJournalShowBean> getGeneralJournalShowBeanList() {
        return generalJournalShowBeanList;
    }

    /**
     * @param generalJournalShowBeanList the generalJournalShowBeanList to set
     */
    public void setGeneralJournalShowBeanList(List<GeneralJournalShowBean> generalJournalShowBeanList) {
        this.generalJournalShowBeanList = generalJournalShowBeanList;
    }

    /**
     * @return the maxId
     */
    public Integer getMaxId() {
        return maxId;
    }

    /**
     * @param maxId the maxId to set
     */
    public void setMaxId(Integer maxId) {
        this.maxId = maxId;
    }

    /**
     * @return the stickyHeader
     */
    public Boolean getStickyHeader() {
        return stickyHeader;
    }

    /**
     * @param stickyHeader the stickyHeader to set
     */
    public void setStickyHeader(Boolean stickyHeader) {
        this.stickyHeader = stickyHeader;
    }

    /**
     * @return the journalDocumentArrangedReportBeanList
     */
    public List<JournalDocumentArrangedReportBean> getJournalDocumentArrangedReportBeanList() {
        return journalDocumentArrangedReportBeanList;
    }

    /**
     * @param journalDocumentArrangedReportBeanList the
     * journalDocumentArrangedReportBeanList to set
     */
    public void setJournalDocumentArrangedReportBeanList(List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList) {
        this.journalDocumentArrangedReportBeanList = journalDocumentArrangedReportBeanList;
    }

    /**
     * @return the printType
     */
    public Integer getPrintType() {
        return printType;
    }

    /**
     * @param printType the printType to set
     */
    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    /**
     * @return the ev
     */
    public SelectEvent getEv() {
        return ev;
    }

    /**
     * @param ev the ev to set
     */
    public void setEv(SelectEvent ev) {
        this.ev = ev;
    }

    /**
     * @return the evNew
     */
    public SelectEvent getEvNew() {
        return evNew;
    }

    /**
     * @param evNew the evNew to set
     */
    public void setEvNew(SelectEvent evNew) {
        this.evNew = evNew;
    }

    /**
     * @return the confirmsCounter
     */
    public Integer getConfirmsCounter() {
        return confirmsCounter;
    }

    /**
     * @param confirmsCounter the confirmsCounter to set
     */
    public void setConfirmsCounter(Integer confirmsCounter) {
        this.confirmsCounter = confirmsCounter;
    }

    /**
     * @return the detailsListCopy
     */
    public List<GeneralJournalDetails> getDetailsListCopy() {
        return detailsListCopy;
    }

    /**
     * @param detailsListCopy the detailsListCopy to set
     */
    public void setDetailsListCopy(List<GeneralJournalDetails> detailsListCopy) {
        this.detailsListCopy = detailsListCopy;
    }

    /**
     * @return the datailsNotSavedReference
     */
    public Integer getDatailsNotSavedReference() {
        return datailsNotSavedReference;
    }

    /**
     * @param datailsNotSavedReference the datailsNotSavedReference to set
     */
    public void setDatailsNotSavedReference(Integer datailsNotSavedReference) {
        this.datailsNotSavedReference = datailsNotSavedReference;
    }

    /**
     * @return the returnedGeneralJournal
     */
    public GeneralJournal getReturnedGeneralJournal() {
        return returnedGeneralJournal;
    }

    /**
     * @param returnedGeneralJournal the returnedGeneralJournal to set
     */
    public void setReturnedGeneralJournal(GeneralJournal returnedGeneralJournal) {
        this.returnedGeneralJournal = returnedGeneralJournal;
    }

    /**
     * @return the confirmManadatoryData
     */
    public boolean isConfirmManadatoryData() {
        return confirmManadatoryData;
    }

    /**
     * @param confirmManadatoryData the confirmManadatoryData to set
     */
    public void setConfirmManadatoryData(boolean confirmManadatoryData) {
        this.confirmManadatoryData = confirmManadatoryData;
    }

    /**
     * @return the errorMessageBuilder
     */
    public StringBuilder getErrorMessageBuilder() {
        return errorMessageBuilder;
    }

    /**
     * @param errorMessageBuilder the errorMessageBuilder to set
     */
    public void setErrorMessageBuilder(StringBuilder errorMessageBuilder) {
        this.errorMessageBuilder = errorMessageBuilder;
    }

    /**
     * @return the errorNoNeedMessageBuilder
     */
    public StringBuilder getErrorNoNeedMessageBuilder() {
        return errorNoNeedMessageBuilder;
    }

    /**
     * @param errorNoNeedMessageBuilder the errorNoNeedMessageBuilder to set
     */
    public void setErrorNoNeedMessageBuilder(StringBuilder errorNoNeedMessageBuilder) {
        this.errorNoNeedMessageBuilder = errorNoNeedMessageBuilder;
    }

    /**
     * @return the generalReviewList
     */
    public List<GeneralJournal> getGeneralReviewList() {
        return generalReviewList;
    }

    /**
     * @param generalReviewList the generalReviewList to set
     */
    public void setGeneralReviewList(List<GeneralJournal> generalReviewList) {
        this.generalReviewList = generalReviewList;
    }

    /**
     * @return the summaryDebitString
     */
    public String getSummaryDebitString() {
        return summaryDebitString;
    }

    /**
     * @param summaryDebitString the summaryDebitString to set
     */
    public void setSummaryDebitString(String summaryDebitString) {
        this.summaryDebitString = summaryDebitString;
    }

    /**
     * @return the summaryCreditString
     */
    public String getSummaryCreditString() {
        return summaryCreditString;
    }

    /**
     * @param summaryCreditString the summaryCreditString to set
     */
    public void setSummaryCreditString(String summaryCreditString) {
        this.summaryCreditString = summaryCreditString;
    }

    /**
     * @return the differenceString
     */
    public String getDifferenceString() {
        return differenceString;
    }

    /**
     * @param differenceString the differenceString to set
     */
    public void setDifferenceString(String differenceString) {
        this.differenceString = differenceString;
    }

    /**
     * @return the modifyJournalAbility
     */
    public boolean isModifyJournalAbility() {
        return modifyJournalAbility;
    }

    /**
     * @param modifyJournalAbility the modifyJournalAbility to set
     */
    public void setModifyJournalAbility(boolean modifyJournalAbility) {
        this.modifyJournalAbility = modifyJournalAbility;
    }

    /**
     * @return the accountsSystemSettings
     */
    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }

    /**
     * @return the revisionAccount
     */
    public Integer getRevisionAccount() {
        return revisionAccount;
    }

    /**
     * @param revisionAccount the revisionAccount to set
     */
    public void setRevisionAccount(Integer revisionAccount) {
        this.revisionAccount = revisionAccount;
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
     * @return the reviewByDate
     */
    public boolean isReviewByDate() {
        return reviewByDate;
    }

    /**
     * @param reviewByDate the reviewByDate to set
     */
    public void setReviewByDate(boolean reviewByDate) {
        this.reviewByDate = reviewByDate;
    }

    /**
     * @return the generalGeneralReset
     */
    public GeneralJournal getGeneralGeneralReset() {
        return generalGeneralReset;
    }

    /**
     * @param generalGeneralReset the generalGeneralReset to set
     */
    public void setGeneralGeneralReset(GeneralJournal generalGeneralReset) {
        this.generalGeneralReset = generalGeneralReset;
    }

    /**
     * @return the EditGeneralJournal
     */
    public Boolean getEditGeneralJournal() {
        return EditGeneralJournal;
    }

    /**
     * @param EditGeneralJournal the EditGeneralJournal to set
     */
    public void setEditGeneralJournal(Boolean EditGeneralJournal) {
        this.EditGeneralJournal = EditGeneralJournal;
    }

}
