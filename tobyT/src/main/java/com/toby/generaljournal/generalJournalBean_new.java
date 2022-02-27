/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.generaljournal;

import com.toby.bean.JournalDocumentArrangedReportBean;
import com.toby.businessservice.TobyUserYearService;
import com.toby.dto.TobyCompanyDTO;
import com.toby.entity.TobyCompany;
import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.AccountsSystemSettingsServiceDTO;
import com.toby.businessservice.BranchService;
import com.toby.businessservice.CloseAndSaveMonthService;
import com.toby.businessservice.CloseAndSaveMonthServiceDTO;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.CostCenterServiceDTO;
import com.toby.businessservice.CurrencyOperationService;
import com.toby.businessservice.CurrencyOperationServiceDTO;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.CurrencyServiceDTO;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GeneraljournalDetailsServiceDTO;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GeneraljournalServiceDTO;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAccountServiceDTO;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.GlAdminUnitServiceDTO;
import com.toby.businessservice.SymbolService;
import com.toby.converter.GlYearDTOConverter;
import com.toby.dto.AccountsSystemSettingsDTO;
import com.toby.dto.CloseAndSaveMonthDTO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.CurrencyOperationDTO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GeneralJournalDetailsDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlYearDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.GlYear;
import com.toby.entity.Symbol;
import com.toby.reports.GeneralJournalShowBean;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author AhmedEssam
 */
@Named(value = "generalJournalBean_new")
@ViewScoped
public class generalJournalBean_new extends BaseFormBean implements Serializable {

    private GeneralJournalDTO selectedGeneralJournal;
//    private List<GeneralJournalDTO> generalList;
    private LazyDataModel<GeneralJournalDTO> dataModel;
    private List<GeneralJournalDTO> generalDTOList;
    private List<GeneralJournalDTO> generalReviewList;
    private Integer selectedCompany;
    private List<TobyCompanyDTO> companies;
    private List<GeneralJournalDetailsDTO> details = new ArrayList<>();
    private GeneralJournalDetailsDTO detail;
    private List<GeneralJournalDetailsDTO> rowsDeleted = new ArrayList<>();
    private List<GeneralJournalDetailsDTO> detailsList;
    private List<GeneralJournalDetailsDTO> detailsListCopy;

    private String difference;

    private GeneralJournalDetailsDTO journalDetailSelected;

    private GeneralJournalDetailsDTO detailTarget;
    private GeneralJournalDetailsDTO detailReset;
    private GeneralJournalDTO generalGeneralReset;
    private GlAccountDTO gAccount;
    private Integer selectedAccount;
    private Integer selectedCurrency;
    private CostCenterDTO costCenter;
    private GlAdminUnitDTO adminUnit;
    private List<GlAccountDTO> glAccounts;
    private List<CostCenterDTO> costCenterList;
    private List<GlAdminUnitDTO> glAdminunitlist;
    private List<CurrencyDTO> currencylist;
    private List<CloseAndSaveMonthDTO> closeAndSaveMonthList;
    private CloseAndSaveMonthDTO closeAndSaveMonth;

    private CurrencyOperationDTO currencyOperations;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;

    private Boolean viewCredit = Boolean.FALSE;
    private Boolean viewDebit = Boolean.FALSE;
    private BigDecimal cAmount;
    private BigDecimal dAmount;
    private BigDecimal currencyRate;
    private List<SymbolDTO> documentsTypes;
    private Integer selectedDocumentsType;
    private Integer generalDocument;
    private Boolean debitDisabled = Boolean.FALSE;
    private Boolean creditDisabled = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;
    private Boolean view = Boolean.FALSE;
    private Boolean disableAccount = Boolean.TRUE;
    private Boolean disableAdminU = Boolean.TRUE;
    private Boolean disableCC = Boolean.TRUE;
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

    private List<GlYearDTO> glYearDTOList;
    private Integer glYearId;
    private GlYearDTO glYearDTOSelection;

    private List<GeneralJournalShowBean> generalJournalShowBeanList;

    private GlYearDTOConverter glYearDTOConverter;

    private Integer index = 0;

    private Integer maxId;
    InetAddress ip;

    private Boolean stickyHeader;
    private List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList;
    private String uri;
    private SelectEvent ev;
    private SelectEvent evNew;
    private Integer confirmsCounter = 0;

    private Integer datailsNotSavedReference;
    private GeneralJournalDTO returnedGeneralJournal;

    private boolean confirmManadatoryData;
    private StringBuilder errorMessageBuilder;
    private StringBuilder errorNoNeedMessageBuilder;

    private String summaryDebitString;
    private String summaryCreditString;
    private String differenceString;

    private boolean modifyJournalAbility = true;
    private AccountsSystemSettingsDTO accountsSystemSettings;
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

    @EJB
    private CloseAndSaveMonthServiceDTO closeAndSaveMonthServiceDTO;

    @EJB
    private AccountsSystemSettingsServiceDTO accountsSystemSettingsServiceDTO;

    @EJB
    private GlAccountServiceDTO accountServiceDTO;

    @EJB
    private CostCenterServiceDTO costCenterServiceDTO;

    @EJB
    private GlAdminUnitServiceDTO adminUnitServiceDTO;

    @EJB
    private CurrencyServiceDTO currencyServiceDTO;

    @EJB
    private GeneraljournalServiceDTO generaljournalServiceDTO;

    @EJB
    private GeneraljournalDetailsServiceDTO generaljournalDetailsServiceDTO;

    @EJB
    private CurrencyOperationServiceDTO currencyOperationServiceDTO;

    @Override
    @PostConstruct
    public void init() {
        try {
            revisionAccount = 0;
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
//            closeAndSaveMonthList = closeAndSaveMonthService.getCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchId(getUserData().getUserBranch().getId(), glYearSelection.getId(), numberOfMonth);
            closeAndSaveMonthList = closeAndSaveMonthServiceDTO.getCloseAndSaveMonthsDTOByYearIdAndMounthNumberAndBranchId(getUserData().getUserBranch().getId(), glYearDTOSelection.getId(), numberOfMonth);

            if (closeAndSaveMonthList != null && !closeAndSaveMonthList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء قيد فى شهر مغلق"));
                setShowMessageGeneral(true);
                return "";
            }

            if (glYearDTOSelection.getOpenning() != null && glYearDTOSelection.getOpenning() == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء قيد فى سنة مغلقة"));
                setShowMessageGeneral(true);
                return "";
            }
            try {
                showMessageGeneral = Boolean.TRUE;
                showMessageDetails = Boolean.FALSE;
                //flags =selectedGeneralJournal.getPost_flag();

                if (selectedCompany != null && selectedGeneralJournal != null) {
                    if (selectedGeneralJournal.getGeneralData().compareTo(glYearDTOSelection.getDateFrom()) >= 0 && selectedGeneralJournal.getGeneralData().compareTo(glYearDTOSelection.getDateTo()) <= 0 && closeMonthCheck == true) {
                        TobyCompany company = new TobyCompany();
                        company.setId(getSelectedCompany());

                        if (selectedDocumentsType != null) {
                            selectedGeneralJournal.setGeneralType(symbolService.getSymbolDTO(selectedDocumentsType));
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
                        selectedGeneralJournal.setCompanyId(company.getId());
                        if (selectedGeneralJournal.getId() == null) {
                            selectedGeneralJournal.setCreatedBy(getUserData().getUser().getId());
                            selectedGeneralJournal.setCreatedDate(new Date());
                        } else {
                            selectedGeneralJournal.setModifiedBy(getUserData().getUser().getId());
                            selectedGeneralJournal.setModificationDate(new Date());
                        }

                        selectedGeneralJournal.setMarkEdit(Boolean.FALSE);
                        // selectedGeneralJournal.setMacId(getMacId());
//                selectedGeneralJournal.setPost_flag(isValue1());
                        //author by me
                        // selectedGeneralJournal.setPost_flag(Boolean.TRUE);
                        selectedGeneralJournal.setBranchId(branchService.findBranch(getUserData().getSelectedBranch()).getId());
                        if (selectedGeneralJournal.getSerial() == null) {
                            selectedGeneralJournal.setSerial(0);
                        }

                        selectedGeneralJournal.setGlYear(getGlYearDTO());
//                        returnedGeneralJournal = generaljournalService.addGeneralJournal(selectedGeneralJournal);
//                        returnedGeneralJournal = generaljournalService.addGeneralJournalDTO(selectedGeneralJournal,getUserlogin());
                        returnedGeneralJournal = generaljournalServiceDTO.addGeneralJournalDTO(selectedGeneralJournal, getUserlogin());
                        if (uri != null && !uri.contains("shiftingJournals")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                        }
                        generalDocument = null;

                        if (uri.contains("generaljournal")) {
//                            generalDTOList = generaljournalService.getALLGeneralJournalDTOByBranchIdAndYear(getUserData().getSelectedBranch(), glYearSelection);
//                            generalDTOList = generaljournalServiceDTO.getALLGeneralJournalDTOByBranchIdAndYear(getUserData().getSelectedBranch(), glYearDTOSelection);
                            setDataLazyModel(null, false, false);
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

    private GlYearDTO getGlYearDTO() {
        GlYearDTO glYearDTO = new GlYearDTO();
        glYearDTO.setId(getUserData().getGlYear().getId());
        glYearDTO.setDateFrom(getUserData().getGlYear().getDateFrom());
        glYearDTO.setDateTo(getUserData().getGlYear().getDateTo());
        glYearDTO.setIsDefault(getUserData().getGlYear().getIsDefault());
        glYearDTO.setName(getUserData().getGlYear().getName());
        glYearDTO.setOpenning(getUserData().getGlYear().getOpenning());
        glYearDTO.setYear(getUserData().getGlYear().getYear());
        return glYearDTO;
    }

    public void saveReviewAccountingForAll() throws CloneNotSupportedException {
        generalReviewList = generaljournalServiceDTO.getGeneralJournalDTOByBranchIdForRevision(glYearDTOSelection, getUserData().getSelectedBranch(), getDateFrom(), getDateTo(), revisionAccount, 0, null, new HashMap<>());
        saveReviewAccounting();
    }

    public void saveReviewAccounting() throws CloneNotSupportedException {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (generalReviewList != null && !generalReviewList.isEmpty() && !reviewByDate) {
                for (GeneralJournalDTO detailsReview : generalReviewList) {
                    detailsReview.setPost_flag(true);
                    detailsReview.setPostFlagReview("تمت مراجعته");
                }
                try {
//                    generaljournalService.addGeneralJournalsForReviewing(generalReviewList);
                    generaljournalServiceDTO.addGeneralJournalsDTOForReviewing(generalReviewList, getUserlogin());

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

    public void undoReviewForAll() {
        generalReviewList = generaljournalServiceDTO.getGeneralJournalDTOByBranchIdForRevision(glYearDTOSelection, getUserData().getSelectedBranch(), getDateFrom(), getDateTo(), revisionAccount, 0, null, new HashMap<>());
        undoReview();
    }

    public void undoReview() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            if (generalReviewList != null && !generalReviewList.isEmpty() && !reviewByDate) {
                for (GeneralJournalDTO detailsReview : generalReviewList) {
                    detailsReview.setPost_flag(false);
                    detailsReview.setPostFlagReview("");
                }
                try {
//                    generaljournalService.addGeneralJournalsForReviewing(generalReviewList);
                    generaljournalServiceDTO.addGeneralJournalsDTOForReviewing(generalReviewList, getUserlogin());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            } else if (reviewByDate && (dateFrom != null || dateTo != null)) {
                try {
                    generaljournalServiceDTO.updateGeneralJournalsDTOForReviewing(dateFrom, dateTo, getUserData().getUserBranch().getId(), 0, null);
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
            journalDetailSelected = new GeneralJournalDetailsDTO();
            closeAndSaveMonthList = new ArrayList<>();
            detail = new GeneralJournalDetailsDTO();
            closeMonthCheck = true;
            generalJournalShowBeanList = new ArrayList<>();
            generalReviewList = new ArrayList<>();
            stickyHeader = false;
            modifyJournalAbility = false;
//            reviewByDate = false;
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
//            accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId());
            accountsSystemSettings = accountsSystemSettingsServiceDTO.getInventoryByCompanyId(getUserData().getCompany().getId());
            if (getUserData().getCompany() != null && getUserData().getCompany().getId() != null && getUserData().getUserBranch() != null) {

//                glYearList = tobyUserYearService.findYearByUserId(getUserData().getUser().getId(), getUserData().getUserBranch());
                glYearDTOList = tobyUserYearService.findYearDTOByUserId(getUserData().getUser().getId(), getUserData().getUserBranch());

                if (glYearDTOList != null && !glYearDTOList.isEmpty()) {
                    setGlYearDTOConverter(new GlYearDTOConverter(glYearDTOList));
                }

                if (getUserData().getGlYear() != null) {
                    setGlYearDTOSelection(getGlYearDTO());
                } else {
                    setGlYearDTOSelection((glYearDTOList == null || glYearDTOList.isEmpty()) ? null : glYearDTOList.get(0));
                }
                uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

                if (glYearDTOSelection != null) {
                    setDataLazyModel(null, false, false);

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
//                glAccounts = accountService.getBranchGLAccountsActiveWithException(getUserData().getUserBranch().getId());
//                glAccounts = accountServiceDTO.getBranchGLAccountsActiveWithException(getUserData().getUserBranch().getId());
                glAccounts = accountServiceDTO.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
//                costCenterList = costCenterService.getAllSubCostCenterByBranchIdActive(getUserData().getUserBranch().getId());
                costCenterList = costCenterServiceDTO.getAllSubCostCenterByBranchIdActive(getUserData().getUserBranch().getId());
//                glAdminunitlist = adminUnitService.getAllSubAdminUnitByBranchIdActive(getUserData().getUserBranch().getId());
                glAdminunitlist = adminUnitServiceDTO.getAllSubAdminUnitByBranchIdActive(getUserData().getUserBranch().getId());
//                currencylist = currencyService.getAllCurrenciesHavingRatesByCompanyId(getUserData().getCompany().getId());
                currencylist = currencyServiceDTO.getAllCurrenciesHavingRatesByCompanyId(getUserData().getCompany().getId());

                documentsTypes = symbolService.getDocumentsTypesDTO(selectedCompany);
                //  branchService.getAllBranchByCompanyId(selectedCompany);
            } else {
                companies = new ArrayList<>();
                companies = companyService.getAllCompaniesDTO();
            }
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "load");
        }

    }

    private void isModifiable() {
        try {
            if (accountsSystemSettings != null) {
                if (accountsSystemSettings.getJournalsEdit().equalsIgnoreCase("JOURNAL_ENTRY_ENOUGH")) {
                    for (GeneralJournalDTO gj : getGeneraDTOlList()) {

                        if (gj.getCreatedBy() != null && getUserData().getUser() != null && gj.getCreatedBy().compareTo(getUserData().getUser().getId()) == 0) {
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
            generalDTOList = generaljournalServiceDTO.getALLGeneralJournalDTOByBranchIdAndYear(getUserData().getSelectedBranch(), glYearDTOSelection);
            correctGlYear();
            GlYear glYear = new GlYear();
            glYear.setId(glYearDTOSelection.getId());
            getUserData().setGlYear(glYear);
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("userLogInData", getUserData());
        } catch (Exception e) {
            saveError(e, "GeneralJournalBean", "loadgeneraljournalList");
        }
    }

    public void correctGlYear() {
        try {
            if (generalDTOList != null && !generalDTOList.isEmpty()) {
                if (generalDTOList.get(0).getGlYear() == null) {
                    if (glYearDTOSelection.getYear().compareTo(2018) == 0) {
                        for (GeneralJournalDTO gj : generalDTOList) {
                            if (gj.getGlYear() == null) {
                                gj.setGlYear(glYearDTOSelection);
                            }
                        }
                        generaljournalServiceDTO.addGeneralJournalDTOForCorrectence(generalDTOList, getUserlogin());
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
                for (Iterator<GeneralJournalDTO> iterator = generalDTOList.iterator(); iterator.hasNext();) {
                    GeneralJournalDTO generalJournal = new GeneralJournalDTO();
                    generalJournal = (GeneralJournalDTO) iterator.next();
                    if (generalJournal.getSerial() != null && generalJournal.getSerial().compareTo(selectedGeneralJournal.getSerial()) == 0) {
                        iterator.remove();
                    }
                }
                setDataLazyModel(null, false, false);
            } else {
                selectedGeneralJournal = generalGeneralReset;
                int f = 0;
                for (Iterator<GeneralJournalDTO> iterator = generalDTOList.iterator(); iterator.hasNext();) {
                    if (iterator.next().getSerial().compareTo(selectedGeneralJournal.getSerial()) == 0) {
                        generalDTOList.set(f, selectedGeneralJournal);
                    }
                    f++;
                }
                setDataLazyModel(null, false, false);
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
                GeneralJournalDetailsDTO gjd = new GeneralJournalDetailsDTO();
                gjd = (GeneralJournalDetailsDTO) it.next();
                if (gjd.getIndex().compareTo(detail.getIndex()) == 0) {
                    if (detail.getId() != null || detailReset != null) {
                        if (detailReset != null) {
                            detail = (GeneralJournalDetailsDTO) detailReset.clone();
                            if (detailReset.getGlACCOUNTId() != null) {
                                detail.setGlACCOUNTId((GlAccountDTO) detailReset.getGlACCOUNTId().clone());
                            }
                            if (detailReset.getAdminUnitId() != null) {
                                detail.setAdminUnitId((GlAdminUnitDTO) detailReset.getAdminUnitId().clone());
                            }
                            if (detailReset.getCostCenterId() != null) {
                                detail.setCostCenterId((CostCenterDTO) detailReset.getCostCenterId().clone());
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
            for (GeneralJournalDetailsDTO journalDetails : detailsList) {
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
                        if (selectedGeneralJournal.getCreatedBy().compareTo(getUserData().getUser().getId()) == 0) {
                            selectedGeneralJournal.setUserModifyAbility(false);
                        } else {
                            selectedGeneralJournal.setUserModifyAbility(true);
                        }
                    }
                    evNew = e;
                    ev = e;
                    journalDocumentArrangedReportBeanList = new ArrayList<>();
                    selectedGeneralJournal = (GeneralJournalDTO) e.getObject();
                    showMessageGeneral = Boolean.FALSE;
                    showMessageDetails = Boolean.FALSE;
                    if (selectedGeneralJournal != null && selectedCompany != null) {
                        detailsList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        cloneGeneralJournalDetails(detailsList);
                        showOrHideHeader(detailsList);
                        if (!selectedGeneralJournal.isUserModifyAbility()) {
                            modifyJournalAbility = false;
                        }
                        index = 0;
                        for (GeneralJournalDetailsDTO gjd : detailsList) {
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
    }

    public void cloneGeneralJournalDetails(List<GeneralJournalDetailsDTO> details) throws CloneNotSupportedException {
        try {
            if (details != null && !details.isEmpty()) {
                List<GeneralJournalDetailsDTO> generalJournalDetailses = new ArrayList<>();
                for (GeneralJournalDetailsDTO gjd : details) {
                    GeneralJournalDetailsDTO deta = new GeneralJournalDetailsDTO();
                    deta = gjd;
                    if (gjd.getGlACCOUNTId() != null) {
                        deta.setGlACCOUNTId((GlAccountDTO) gjd.getGlACCOUNTId().clone());
                    }
                    if (gjd.getAdminUnitId() != null) {
                        deta.setAdminUnitId((GlAdminUnitDTO) gjd.getAdminUnitId().clone());
                    }
                    if (gjd.getCostCenterId() != null) {
                        deta.setCostCenterId((CostCenterDTO) gjd.getCostCenterId().clone());
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
            if (glYearDTOSelection.getOpenning() == null || glYearDTOSelection.getOpenning() == 0) {

                if (!isParentRowAdded()) {
                    if (selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
                        GeneralJournalDTO generalJournalNew = new GeneralJournalDTO();
                        try {
                            generalJournalNew = (GeneralJournalDTO) selectedGeneralJournal.clone();

                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("Errorin duplication");
                            return;
                        }
                        generalJournalNew.setId(null);
                        generalJournalNew.setCreatedBy(getUserData() != null ? getUserData().getUser().getId() : null);
                        generalJournalNew.setCreatedDate(new Date());
                        generalJournalNew.setPost_flag(false);
                        generalJournalNew.setCompanyId(getUserData().getUser().getCompanyId().getId());
                        generalJournalNew = generaljournalServiceDTO.addGeneralJournalDTO(generalJournalNew, getUserlogin());
                        if (accountsSystemSettings != null) {
                            if (accountsSystemSettings.getJournalsEdit().equalsIgnoreCase("JOURNAL_ENTRY_ENOUGH")) {
                                if (generalJournalNew.getCreatedBy().compareTo(getUserData().getUser().getId()) == 0) {
                                    generalJournalNew.setUserModifyAbility(false);
                                    modifyJournalAbility = false;
                                } else {
                                    generalJournalNew.setUserModifyAbility(true);
                                }
                            }
                        }
                        for (GeneralJournalDTO journal : generalDTOList) {
                            journal.setMarkEdit(Boolean.FALSE);
                        }

                        generalDTOList.add(0, generalJournalNew);

                        List<GeneralJournalDetailsDTO> gjds = new ArrayList<>();

                        detailsList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        showOrHideHeader(detailsList);

                        selectedGeneralJournal = generalJournalNew;

                        for (GeneralJournalDetailsDTO journalDetails : detailsList) {

                            journalDetails.setId(null);
                            journalDetails.setGeneralJournalId(generalJournalNew);
                            journalDetails.setIndex(++index);
                            gjds.add(journalDetails);
                        }

                        detailsList = generaljournalDetailsServiceDTO.addGenDetalils(gjds, null, getUserlogin());
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
            GeneralJournalDetailsDTO newDetail = new GeneralJournalDetailsDTO();
            newDetail = details.get(details.size() - 1);
            try {
                newDetail = (GeneralJournalDetailsDTO) journalDetailSelected.clone();
                newDetail.setGlACCOUNTId(journalDetailSelected.getGlACCOUNTId() != null ? (GlAccountDTO) journalDetailSelected.getGlACCOUNTId().clone() : null);
                newDetail.setAdminUnitId(journalDetailSelected.getAdminUnitId() != null ? (GlAdminUnitDTO) journalDetailSelected.getAdminUnitId().clone() : null);
                newDetail.setCostCenterId(journalDetailSelected.getCostCenterId() != null ? (CostCenterDTO) journalDetailSelected.getCostCenterId().clone() : null);
                newDetail.setId(null);
                details = new ArrayList<>();
                detailsList.remove(detailsList.size() - 1);
                detailsList.add(newDetail);
                dAmount = newDetail.getDebitAmount() != null ? newDetail.getDebitAmount() : BigDecimal.ZERO;
                cAmount = newDetail.getCreditamount() != null ? newDetail.getCreditamount() : BigDecimal.ZERO;
                setRowAdded(false);
                for (GeneralJournalDetailsDTO generalJournal : detailsList) {
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
            for (GeneralJournalDetailsDTO gjd : detailsList) {
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

    public Boolean fillSubSummary(List<GeneralJournalDetailsDTO> detailses) {
        try {

            BigDecimal credit = BigDecimal.ZERO;
            BigDecimal debit = BigDecimal.ZERO;
            for (GeneralJournalDetailsDTO gjd : detailses) {
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
            journalDetailSelected = (GeneralJournalDetailsDTO) e.getObject();
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
                    if (selectedGeneralJournal.getId() != null && (glYearDTOSelection.getOpenning() == null || glYearDTOSelection.getOpenning() == 0)) {
                        generaljournalServiceDTO.deleteGeneralJournalDTO(selectedGeneralJournal, getUserlogin());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                        generalDTOList = generaljournalServiceDTO.getALLGeneralJournalDTOByBranchIdAndYear(getUserData().getSelectedBranch(), glYearDTOSelection);
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
                for (GeneralJournalDetailsDTO detaill : details) {
                    for (Iterator<GeneralJournalDetailsDTO> iterator = detailsList.iterator(); iterator.hasNext();) {
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
            if (glYearDTOSelection.getOpenning() == null || glYearDTOSelection.getOpenning() == 0) {
                if (!isParentRowAdded()) {
                    setParentRowAdded(true);
                    generalGeneralReset = (GeneralJournalDTO) selectedGeneralJournal.clone();
                    showMessageGeneral = Boolean.FALSE;
                    showMessageDetails = Boolean.FALSE;

                    if (selectedGeneralJournal.getGeneralDecument() != null) {
                        generalDocument = selectedGeneralJournal.getGeneralDecument();
                    }

                    setSelectedDocumentsType(selectedGeneralJournal.getGeneralType().getId());

                    setDataLazyModel(selectedGeneralJournal, false, true);

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
            GeneralJournalDetailsDTO detailsnew = new GeneralJournalDetailsDTO();

            if (!isRowAdded() && selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
                loadGlAccountsBasedDocumentType();
                setRowAdded(true);
                if (selectedCompany != null && selectedGeneralJournal != null) {
                    for (GeneralJournalDetailsDTO generalJournal : detailsList) {
                        generalJournal.setMarkEdit(Boolean.FALSE);
                        generalJournal.setMarkDisable(Boolean.TRUE);
                    }

                    // detailsList.add(detailsnew);
                    TobyCompany tobyCompany = new TobyCompany();

                    tobyCompany.setId(selectedCompany);
                    detailsnew.setCompanyId(tobyCompany.getId());
                    detailsnew.setGeneralJournalId(selectedGeneralJournal);
                    detailsnew.setBranchId(getUserData().getUserBranch().getId());
                    detailsnew.setCreatedBy(getUserData().getUser().getId());
                    detailsnew.setDiscribtion(selectedGeneralJournal.getGeneralStatement());
                    // To copy generalstatment from head to discription detail
                    detailsnew.setGlACCOUNTId(new GlAccountDTO());
                    detailsnew.setCostCenterId(new CostCenterDTO());
                    detailsnew.setAdminUnitId(new GlAdminUnitDTO());

                    detailsnew.setGlAssistantAccount(new GlAccountDTO());
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

    public void loadGlAccountsBasedDocumentType() {
        Symbol symbol = symbolService.findSymbol(selectedGeneralJournal.getGeneralType() != null ? selectedGeneralJournal.getGeneralType().getId() : null);
        if (symbol != null && symbol.getSerial() == 0 && glAccountLoad != null && glAccountLoad.compareTo(0) != 0) {
            glAccounts = accountServiceDTO.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            glAccountLoad = 0;
        } else if (symbol != null && symbol.getSerial() != 0 && glAccountLoad != null && glAccountLoad.compareTo(2) != 0) {
//            glAccounts = accountServiceDTO.getBranchGLAccountsActiveWithException(getUserData().getUserBranch().getId());
            glAccounts = accountServiceDTO.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            glAccountLoad = 2;
        }
    }

    public void add() {
        generalDocument = null;
        showMessageGeneral = Boolean.FALSE;
        showMessageDetails = Boolean.FALSE;

        if (!isParentRowAdded()) {

            setParentRowAdded(true);

            for (GeneralJournalDTO generalJournal : generalDTOList) {
                generalJournal.setMarkEdit(Boolean.FALSE);
            }

            GeneralJournalDTO journalnew = new GeneralJournalDTO();
            journalnew.setMarkEdit(Boolean.TRUE);
            journalnew.setSerial(0);
            journalnew.setIndex(getIndex());
            selectedGeneralJournal = journalnew;
            setDataLazyModel(journalnew, true, false);

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال بيانات القيد اولا قبل ادخال قيد اخر"));
            showMessageGeneral = Boolean.TRUE;
        }

    }

    private void setDataLazyModel(GeneralJournalDTO journalnew, Boolean isAdd, Boolean isupdate) {
        this.setDataModel(new LazyDataModel<GeneralJournalDTO>() {
            private static final long serialVersionUID = 1L;

            @Override
            public GeneralJournalDTO getRowData(String rowKey) {
                for (GeneralJournalDTO car : generalDTOList) {
                    if (car.getIndex().toString().equals(rowKey)) {
                        return car;
                    }
                }

                return null;
            }

            @Override
            public Integer getRowKey(GeneralJournalDTO car) {
                return car.getIndex();
            }

            @Override
            public List<GeneralJournalDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                if (uri.contains("reviewaccounting")) {
                    setRowCount(generaljournalServiceDTO.getTotalGeneralJournalForRevision(glYearDTOSelection, getUserData().getSelectedBranch(), getDateFrom(), getDateTo(), revisionAccount, filters).intValue());
                    generalDTOList = generaljournalServiceDTO.getGeneralJournalDTOByBranchIdForRevision(glYearDTOSelection, getUserData().getSelectedBranch(), getDateFrom(), getDateTo(), revisionAccount, first, pageSize, filters);
                } else {
                    setRowCount(generaljournalServiceDTO.getTotalGeneralJournal(glYearDTOSelection, getUserData().getSelectedBranch(), filters).intValue());
                    generalDTOList = generaljournalServiceDTO.getGeneralJournalDTOByBranchId(glYearDTOSelection, getUserData().getSelectedBranch(), first, pageSize, filters);
                }
                correctGlYear();
                if (isAdd) {
                    if (generalDTOList.size() > 0) {
                        generalDTOList.remove(generalDTOList.size() - 1);
                    }
                    generalDTOList.add(0, journalnew);
                    if (generalDTOList.size() == 1) {
                     setRowCount(2);
                       
                    }
                }
                if (isupdate) {
                    for (GeneralJournalDTO generalJournal : generalDTOList) {
                        if (journalnew != null && generalJournal.getId().equals(journalnew.getId())) {
                            generalJournal.setMarkEdit(Boolean.TRUE);
                        } else {
                            generalJournal.setMarkEdit(Boolean.FALSE);
                        }
                    }
                }
                return generalDTOList;
            }
        });
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

            GlAccountDTO account = new GlAccountDTO();
            account = accountServiceDTO.findGlAccountDTO(detail.getGlACCOUNTId().getId());
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(account.getCurrencyId());
            detail.setCurrencyId(currencyDTO);
            currencyOperations = currencyOperationServiceDTO.getRatesByDates(detail.getCurrencyId().getId(), selectedGeneralJournal.getGeneralData(), getUserData().getCompany().getId());
            detail.setRate(currencyOperations == null ? BigDecimal.ONE : currencyOperations.getRate());
            detail.setGlACCOUNTId(account);

            TobyCompany company = new TobyCompany();
            company.setId(selectedCompany);
            detail.setCompanyId(company.getId());
            detail.setBranchId(selectedGeneralJournal.getBranchId());
            detail.setModificationDate(new Date());
            detail.setModifiedBy(getUserData().getUser().getId());
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
                detail.setAdminUnitId(adminUnitServiceDTO.findAdminUnit(detail.getAdminUnitId().getId()));
            }
            if (detail.getCostCenterId() != null && detail.getCostCenterId().getId() != null && detail.getCostCenterId().getId() != -1) {
                detail.setCostCenterId(costCenterServiceDTO.findCostCenter(detail.getCostCenterId().getId()));
            }

            for (GeneralJournalDetailsDTO journalDetails : detailsList) {
                journalDetails.setMarkEdit(false);
                journalDetails.setMarkDisable(Boolean.FALSE);
            }

            detail.setMarkEdit(true);
            confirmsCounter++;

            if (detail.getGlAssistantAccount() != null && detail.getGlAssistantAccount().getId() != null) {

                detail.setGlAssistantAccount(accountServiceDTO.findGlAccountDTO(detail.getGlAssistantAccount().getId()));
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
        GlAccountDTO account;
        Integer x = 0;
        Integer y = 0;
        errorMessageBuilder.append("يجب اختيار ");
        errorNoNeedMessageBuilder.append("لا يمكن اختيار ");
        if (detail.getGlACCOUNTId() != null && detail.getGlACCOUNTId().getId() == null) {
            confirmManadatoryData = false;
            errorMessageBuilder.append("حساب ");
            x++;
        }
        account = accountServiceDTO.findGlAccountDTO(detail.getGlACCOUNTId().getId());

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
                            for (GeneralJournalDetailsDTO gernallistd : detailsList) {
                                initailizeObject(gernallistd, maxSerial);
                            }
                            detailsList = generaljournalDetailsServiceDTO.addGenDetalils(detailsList, rowsDeleted, getUserlogin());
                            delete = Boolean.FALSE;
                            detailsList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                            cloneGeneralJournalDetails(detailsList);
                            index = 0;
                            for (GeneralJournalDetailsDTO gjd : detailsList) {
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
                        for (GeneralJournalDetailsDTO gernallistd : detailsList) {
                            initailizeObject(gernallistd, maxSerial);
                        }
                        detailsList = generaljournalDetailsServiceDTO.addGenDetalils(detailsList, rowsDeleted, getUserlogin());
                        delete = Boolean.FALSE;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                        details = new ArrayList<>();
                        detailsList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(selectedGeneralJournal.getId());
                        cloneGeneralJournalDetails(detailsList);
                        index = 0;
                        for (GeneralJournalDetailsDTO gjd : detailsList) {
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
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("SAVED_ERROR")));
        }
        rowsDeleted = new ArrayList<>();
    }

    private void initailizeObject(GeneralJournalDetailsDTO gernallistd, Integer maxSerial) {
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
        AccountsSystemSettingsDTO journalBalanced = accountsSystemSettingsServiceDTO.getInventoryByCompanyId(selectedCompany);
        if (journalBalanced != null) {
            if (journalBalanced.getJournalsDailyBalanced().equalsIgnoreCase("NO_WEIGHT_JOURNAL")) {
                return false;
            }

        }

        return true;
    }

    public void editgeneraldetails() throws CloneNotSupportedException {
        if (!isRowAdded()) {
            detailReset = new GeneralJournalDetailsDTO();
            detailReset = (GeneralJournalDetailsDTO) detail.clone();
            if (detail.getGlACCOUNTId() != null) {
                detailReset.setGlACCOUNTId((GlAccountDTO) detail.getGlACCOUNTId().clone());
            }
            if (detail.getAdminUnitId() != null) {
                detailReset.setAdminUnitId((GlAdminUnitDTO) detail.getAdminUnitId().clone());
            }
            if (detail.getCostCenterId() != null) {
                detailReset.setCostCenterId((CostCenterDTO) detail.getCostCenterId().clone());
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
            for (GeneralJournalDetailsDTO generalJournalDetail : detailsList) {
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
                detail.setCostCenterId(new CostCenterDTO());
            }

            if (detail.getAdminUnitId() == null) {
                detail.setAdminUnitId(new GlAdminUnitDTO());
            }

            if (detail.getGlAssistantAccount() == null) {
                detail.setGlAssistantAccount(new GlAccountDTO());
            }

            if (detail.getGlACCOUNTId() == null) {
                detail.setGlACCOUNTId(new GlAccountDTO());
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

        GlAccountDTO account;
        accountName = null;
        disableCC = Boolean.TRUE;
        disableAdminU = Boolean.TRUE;
        disableAccount = Boolean.TRUE;
//        details.setGlACCOUNTId(gAccount);
        if (detail.getGlACCOUNTId() != null && detail.getGlACCOUNTId().getId() != null) {
            account = accountServiceDTO.findGlAccountDTO(detail.getGlACCOUNTId().getId());
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
        selectedGeneralJournal = new GeneralJournalDTO();
        closeAndSaveMonthList = new ArrayList<>();
        selectedCompany = getUserData().getCompany().getId();

    }

    public void addExtraDateToDetails() {

        GlAccountDTO account = new GlAccountDTO();
        if (detail != null && detail.getGlACCOUNTId() != null) {
            account = accountServiceDTO.findGlAccountDTO(detail.getGlACCOUNTId().getId());
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(account.getCurrencyId());
            detail.setCurrencyId(currencyDTO);
            currencyOperations = new CurrencyOperationDTO();
            currencyOperations = currencyOperationServiceDTO.getRatesByDates(detail.getCurrencyId().getId(), selectedGeneralJournal.getGeneralData(), getUserData().getCompany().getId());
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
                for (GeneralJournalDetailsDTO gernallistd : detailsList) {
                    initailizeObject(gernallistd, maxSerial);
                }
                detailsList = generaljournalDetailsServiceDTO.addGenDetalils(detailsList, rowsDeleted, getUserlogin());
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
        selectedGeneralJournal = (GeneralJournalDTO) ev.getObject();
    }

    public void print() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (selectedGeneralJournal != null && selectedGeneralJournal.getId() != null) {
            for (GeneralJournalDetailsDTO generalJournalDetails : detailsList) {
                JournalDocumentArrangedReportBean journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
                journalDocumentArrangedReportBean.setJournalNum(selectedGeneralJournal.getSerial());
                journalDocumentArrangedReportBean.setDocumentNum(selectedGeneralJournal.getGeneralDecument());
                journalDocumentArrangedReportBean.setJournalDate(selectedGeneralJournal.getGeneralData());
                journalDocumentArrangedReportBean.setUser(selectedGeneralJournal.getCreated_by().getName());
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
                journalDocumentArrangedReportBean.setCreationDate(generalJournalDetails.getCreatedDate() != null ? generalJournalDetails.getCreatedDate() : null);
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
                if (accountsSystemSettings.getAccountPrinting().equals("HORIZONTAL")) {
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

    public void showOrHideHeader(List<GeneralJournalDetailsDTO> journalDetailse) {
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
            detail.setCostCenterId(new CostCenterDTO());
        }

    }

    public void onAdminUnitChange() {
        if (detail.getAdminUnitId() != null && detail.getAdminUnitId().getId().compareTo(-1) == 0) {
            detail.setAdminUnitId(new GlAdminUnitDTO());
        }
    }

    public List<TobyCompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<TobyCompanyDTO> companies) {
        this.companies = companies;
    }

    public GeneralJournalDTO getSelectedGeneralJournal() {
        return selectedGeneralJournal;
    }

    public void setSelectedGeneralJournal(GeneralJournalDTO selectedGeneralJournal) {
        this.selectedGeneralJournal = selectedGeneralJournal;
    }

    /**
     * @return the detailsList
     */
    public List<GeneralJournalDetailsDTO> getDetailsList() {
        return detailsList;
    }

    /**
     * @param detailsList the detailsList to set
     */
    public void setDetailsList(List<GeneralJournalDetailsDTO> detailsList) {
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
    public GlAccountDTO getgAccount() {
        return gAccount;
    }

    /**
     * @param gAccount the gAccount to set
     */
    public void setgAccount(GlAccountDTO gAccount) {
        this.gAccount = gAccount;
    }

    /**
     * @return the costCenter
     */
    public CostCenterDTO getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenterDTO costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public GlAdminUnitDTO getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(GlAdminUnitDTO adminUnit) {
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
    public List<CostCenterDTO> getCostCenterList() {
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenterDTO> costCenterList) {
        this.costCenterList = costCenterList;
    }

    /**
     * @return the glAdminunitlist
     */
    public List<GlAdminUnitDTO> getGlAdminunitlist() {
        return glAdminunitlist;
    }

    /**
     * @param glAdminunitlist the glAdminunitlist to set
     */
    public void setGlAdminunitlist(List<GlAdminUnitDTO> glAdminunitlist) {
        this.glAdminunitlist = glAdminunitlist;
    }

    /**
     * @return the glAccounts
     */
    public List<GlAccountDTO> getGlAccounts() {
        return glAccounts;
    }

    /**
     * @param glAccounts the glAccounts to set
     */
    public void setGlAccounts(List<GlAccountDTO> glAccounts) {
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
    public List<CurrencyDTO> getCurrencylist() {
        return currencylist;
    }

    public void setCurrencylist(List<CurrencyDTO> currencylist) {
        this.currencylist = currencylist;
    }

    public Integer getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Integer selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public CurrencyOperationDTO getCurrencyOperations() {
        return currencyOperations;
    }

    public void setCurrencyOperations(CurrencyOperationDTO currencyOperations) {
        this.currencyOperations = currencyOperations;
    }

    public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(BigDecimal currencyRate) {
        this.currencyRate = currencyRate;
    }

    public List<SymbolDTO> getDocumentsTypes() {
        return documentsTypes;
    }

    public void setDocumentsTypes(List<SymbolDTO> documentsTypes) {
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

    public List<GeneralJournalDetailsDTO> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<GeneralJournalDetailsDTO> rowsDeleted) {
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

//    public List<GeneralJournalDTO> getGeneralList() {
//        return generalList;
//    }
//
//    public void setGeneralList(List<GeneralJournalDTO> generalList) {
//        this.generalList = generalList;
//    }
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
    public List<GlYearDTO> getGlYearDTOList() {
        return glYearDTOList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    public void setGlYearDTOList(List<GlYearDTO> glYearDTOList) {
        this.glYearDTOList = glYearDTOList;
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
    public GlYearDTO getGlYearDTOSelection() {
        return glYearDTOSelection;
    }

    /**
     * @param glYearSelection the glYearSelection to set
     */
    public void setGlYearDTOSelection(GlYearDTO glYearDTOSelection) {
        this.glYearDTOSelection = glYearDTOSelection;
    }

    /**
     * @return the glYearDTOConverter
     */
    public GlYearDTOConverter getGlYearDTOConverter() {
        return glYearDTOConverter;
    }

    /**
     * @param glYearDTOConverter the glYearDTOConverter to set
     */
    public void setGlYearDTOConverter(GlYearDTOConverter glYearDTOConverter) {
        this.glYearDTOConverter = glYearDTOConverter;
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

    public List<GeneralJournalDetailsDTO> getDetails() {
        return details;
    }

    public void setDetails(List<GeneralJournalDetailsDTO> details) {
        this.details = details;
    }

    public GeneralJournalDetailsDTO getJournalDetailSelected() {
        return journalDetailSelected;
    }

    public void setJournalDetailSelected(GeneralJournalDetailsDTO journalDetailSelected) {
        this.journalDetailSelected = journalDetailSelected;
    }

    public GeneralJournalDetailsDTO getDetailTarget() {
        return detailTarget;
    }

    public void setDetailTarget(GeneralJournalDetailsDTO detailTarget) {
        this.detailTarget = detailTarget;
    }

    public GeneralJournalDetailsDTO getDetail() {
        return detail;
    }

    public void setDetail(GeneralJournalDetailsDTO detail) {
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
    public List<CloseAndSaveMonthDTO> getCloseAndSaveMonthList() {
        return closeAndSaveMonthList;
    }

    /**
     * @param closeAndSaveMonthList the closeAndSaveMonthList to set
     */
    public void setCloseAndSaveMonthList(List<CloseAndSaveMonthDTO> closeAndSaveMonthList) {
        this.closeAndSaveMonthList = closeAndSaveMonthList;
    }

    /**
     * @return the closeAndSaveMonth
     */
    public CloseAndSaveMonthDTO getCloseAndSaveMonth() {
        return closeAndSaveMonth;
    }

    /**
     * @param closeAndSaveMonth the closeAndSaveMonth to set
     */
    public void setCloseAndSaveMonth(CloseAndSaveMonthDTO closeAndSaveMonth) {
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
    public List<GeneralJournalDetailsDTO> getDetailsListCopy() {
        return detailsListCopy;
    }

    /**
     * @param detailsListCopy the detailsListCopy to set
     */
    public void setDetailsListCopy(List<GeneralJournalDetailsDTO> detailsListCopy) {
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
    public GeneralJournalDTO getReturnedGeneralJournal() {
        return returnedGeneralJournal;
    }

    /**
     * @param returnedGeneralJournal the returnedGeneralJournal to set
     */
    public void setReturnedGeneralJournal(GeneralJournalDTO returnedGeneralJournal) {
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
    public List<GeneralJournalDTO> getGeneralReviewList() {
        return generalReviewList;
    }

    /**
     * @param generalReviewList the generalReviewList to set
     */
    public void setGeneralReviewList(List<GeneralJournalDTO> generalReviewList) {
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
    public AccountsSystemSettingsDTO getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettingsDTO accountsSystemSettings) {
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
    public GeneralJournalDTO getGeneralGeneralReset() {
        return generalGeneralReset;
    }

    /**
     * @param generalGeneralReset the generalGeneralReset to set
     */
    public void setGeneralGeneralReset(GeneralJournalDTO generalGeneralReset) {
        this.generalGeneralReset = generalGeneralReset;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<GeneralJournalDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<GeneralJournalDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the generaDTOlList
     */
    public List<GeneralJournalDTO> getGeneraDTOlList() {
        if (generalDTOList == null || generalDTOList.isEmpty()) {
            generalDTOList = new ArrayList<>();
        }
        return generalDTOList;
    }

    /**
     * @param generaDTOlList the generaDTOlList to set
     */
    public void setGeneraDTOlList(List<GeneralJournalDTO> generalDTOList) {
        this.generalDTOList = generalDTOList;
    }

}
