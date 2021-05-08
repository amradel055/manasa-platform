/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.settlementdeed;

import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionInvoiceViewService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlBankConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.SymbolConverter;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.Symbol;
import com.toby.entiy.GlBankTransactionDetailEntity;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.generaljournal.GeneralJournalBean;
import com.toby.toby.GlBankBasicDataForm;
import com.toby.toby.UserData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "SettlementDeedFormMB")
@ViewScoped
public class SettlementDeedFormMB extends GlBankBasicDataForm {

    private Boolean allowAccount;
    private GlAccount accountGLItems;
    private List<Symbol> glItemsList;
    private SymbolConverter glItemConverter;

    private Integer index = 0;
    private String uri;

    private Integer branchId;
    private Integer companyId;
    private Integer glBankTransactionId;
    private Integer organizationTypeUpdated;

    private Boolean checkView = Boolean.FALSE;
    private Boolean lineCheckView = Boolean.FALSE;

    private Boolean paymentDropDownView = Boolean.FALSE;
    private String paymentTypeName;

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;

    private Boolean payed = Boolean.FALSE;

    private Boolean printButton = Boolean.FALSE;

    private Date dateFrom;
    private Date dateTo;

    private BigDecimal localValue = BigDecimal.ZERO;
    private BigDecimal valueSummary = BigDecimal.ZERO;
    private GlAccount account;

    // DB Entities
    private GlBankTransaction glBankTransaction;
    private GlBankTransaction glBankTransactionExist;

    private GlBankTransaction glBankTransactionSelectionPre;
    private List<GlBankTransaction> glBankTransactionListPre;

    private List<GlBankTransactionDetail> glBankTransactionDetailsList;
    private List<GlBankTransactionDetail> glBankTransactionDetailsUpdatedList;
    private List<GlBankTransactionDetail> glBankTransactionDetailsDeletedList;

    // Interface Entities
    private GlBankTransactionEntity glBankTransactionEntity = new GlBankTransactionEntity();
    private GlBankTransactionEntity glBankTransactionEntityMapper = new GlBankTransactionEntity();
    private GlBankTransactionDetailEntity glBankTransactionDetailEntity;
    private GlBankTransactionDetailEntity glBankTransactionDetailEntitySelected;
    private GlBankTransactionDetailEntity glBankTransactionDetailEntityUnSelected;

    private List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;

    private List<GlBankTransactionDetailEntity> rowsDeleted;

    private boolean organizationSiteView = false;
    private boolean accounts = false;
    private boolean saveValidation = true;
    // Lists
    private List<GlAccount> glAccountList;
    private List<CostCenter> costCenterList;
    private List<GlBank> glBankList;
    private List<GlAdminUnit> adminUnitList;
    private List<InvOrganizationSite> organizationList;
    private List<InvOrganizationSite> CustomerList;
    private List<InvOrganizationSite> supplierList;

    private Map<Integer, GlBankTransactionDetail> glBankTransactionDetailsMap = new HashMap();

    private GlBankConverter bankConverter;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;

    private GlaccountConverter glAccountConverter;

    private Symbol symbol;
    private List<Symbol> symbolList;
    private SymbolConverter symbolConverter;
    private Integer OrganizationTypeAfterDialogConfirmation;

    private String remark1;
    private String remark2;

    private Integer organizeType;

    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private GlAccount glAccountSelected;
    private CostCenter costCenterSelected;
    private GlAdminUnit glAdminUnitSelected;

    private InvOrganizationSiteConverter supplierConvertor;

    private StringBuilder errorMessageBuilder;
    private StringBuilder errorNoNeedMessageBuilder;

    private AccountsSystemSettings accountsSystemSettings;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;

    @EJB
    private GlBankTransactionService glBankTransactionService;

    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;

    @EJB
    private GlAccountService glAccountService;

    @EJB
    private CostCenterService costCenterService;

    @EJB
    private GlBankService glBankService;

    @EJB
    private GlAdminUnitService glAdminUnitService;

    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private TobyUserBankService tobyUserBankService;

    @EJB
    SymbolService symbolService;

    @EJB
    private GlBankTransactionInvoiceViewService bankTransactionInvoiceViewService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());
            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
            // lists
            glAccountList = new ArrayList<>();
            costCenterList = new ArrayList<>();
            glBankList = new ArrayList<>();
            adminUnitList = new ArrayList<>();
            organizationList = new ArrayList<>();
            glItemsList = new ArrayList<>();

            rowsDeleted = new ArrayList<>();
            symbolList = new ArrayList<>();

            load();

            paymentDropDownView = true;

            organizationTypeUpdated = 0; // set to 0 during ad

            setAllowAccount(Boolean.TRUE);
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));
            if (getAccountsSystemSettings() != null && getAccountsSystemSettings().getWorkWithAccounts() != null && !getAccountsSystemSettings().getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                setAllowAccount(Boolean.FALSE);
            }

            glBankTransaction.setCompanyId(getUserData().getCompany());
            glBankTransaction.setBranchId(getUserData().getUserBranch());
            glBankTransaction.setTransactionType(0);

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                setGlBankTransactionId((Integer) context.getSessionMap().get("glBankTransactionIdSeclected"));
                findSettlementDeedObject(getGlBankTransactionId(), true);
            }

            symbolList = symbolService.getAllDocumentsByCompanyId(getUserData().getCompany().getId());
            if (symbolList != null && !symbolList.isEmpty()) {
                symbolConverter = new SymbolConverter(symbolList);
            }

            fillLists();

            viewPaymentFields();
            updateTransactionRate();

            if (glBankList == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                try {
                    fc.getExternalContext().redirect("../base/financailyear.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            checkVisability();
            if (glBankTransactionEntity.getId() == null) {
                printButton = Boolean.TRUE;
            } else {
                printButton = Boolean.FALSE;
            }

        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "init");
        }
    }

    private void findSettlementDeedObject(int glBankTransId, Boolean isUpdate) {
        try {
            glBankTransactionExist = glBankTransactionService.findGlBankTransactionById(glBankTransId);
            glBankTransactionDetailsList = new ArrayList<>();
            glBankTransactionDetailEntityList = new ArrayList<>();
            glBankTransactionDetailsUpdatedList = new ArrayList<>();
            valueSummary = BigDecimal.ZERO;
            localValue = BigDecimal.ZERO;
            glBankTransactionDetailsList = glBankTransactionDetailsService.getAllGlBankTransactionDetailsByGlBankTransactionId(glBankTransId);
            glBankTransactionEntity = mapGlBankTransactionToGlBankTransactionEntity(glBankTransactionExist, isUpdate);
            if (glBankTransactionEntity != null) {
                remark1 = glBankTransactionEntity.getRemark() != null ? glBankTransactionEntity.getRemark() : null;
                remark2 = glBankTransactionEntity.getRemark2() != null ? glBankTransactionEntity.getRemark2() : null;
            }
            organizationTypeUpdated = getGlBankTransactionEntity().getOrganizationType();

            payed = true;
            paymentDropDownView = false;

            updateTransactionRate();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "findSettlementDeedObject");
        }

    }

    @Override
    public void load() {
        try {
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionEntity.setOrganizationType(2);
            glBankTransactionEntity.setChequeStatus(0);
            glBankTransactionEntity.setChequeDate(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
            glBankTransactionEntity.setChequeDueDate(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
            glBankTransactionEntity.setPaymentType(0);
            glBankTransactionEntity.setSerial(null);
            glBankTransactionEntity.setValue(glBankTransactionEntity.getValue() != null ? glBankTransactionEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
//            glBankTransactionEntity.setValueLocal(glBankTransactionEntity.getValueLocal() != null ? glBankTransactionEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
            glBankTransactionEntity.setValueLocal(glBankTransactionEntity.getValue() != null ? glBankTransactionEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
            glBankTransactionEntity.setRate(glBankTransactionEntity.getRate() != null ? glBankTransactionEntity.getRate().setScale(2, BigDecimal.ROUND_UP) : null);
            glBankTransactionEntity.setDate(new Date());

            glBankTransaction = new GlBankTransaction();
            glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
            glBankTransactionDetailsList = new ArrayList<>();
            glBankTransactionDetailsUpdatedList = new ArrayList<>();
            glBankTransactionDetailEntityList = new ArrayList<>();
            glBankTransactionDetailsDeletedList = new ArrayList<>();
            glBankTransactionDetailEntitySelected = new GlBankTransactionDetailEntity();
            valueSummary = BigDecimal.ZERO;
            localValue = BigDecimal.ZERO;
            printButton = Boolean.TRUE;
            paymentTypeName = new String();
            paymentDropDownView = Boolean.TRUE;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "load");
        }
    }
    

    private void fillLists() {
        try {
            costCenterList = costCenterService.getAllSubCostCenterByBranchIdActive(branchId);
            costCenterConverter = new CostCenterConverter(costCenterList);

//        glBankList = glBankService.getAllGlBankByBranchId(branchId);
            fillGlBanks();

            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActive(branchId);
            glAdminUnitConverter = new GlAdminUnitConverter(adminUnitList);

            glItemsList = symbolService.getGLItemsByCompanyId(companyId);
            glItemConverter = new SymbolConverter(glItemsList);

            fillAccounts();

            if (glBankTransactionEntity.getGlBank() == null) {
                glBankTransactionEntity.setGlBank((glBankList != null && !glBankList.isEmpty()) ? glBankList.get(0) : null);
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "fillLists");
        }

    }

    private void fillGlBanks() {
        try {
            if (glBankTransactionEntity.getPaymentType() != 1) {
                glBankList = tobyUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 0);
            } else {
                glBankList = tobyUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 1);
            }

            bankConverter = new GlBankConverter(glBankList);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "fillGlBanks");
        }
    }

    public void fillAccounts() {
        try {
            Integer type = getGlBankTransactionEntity().getOrganizationType();
            if (type == 0) {
                if (CustomerList == null || CustomerList.size() == 0) {
//                    CustomerList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0);
                } else {
                    organizationList = CustomerList;
//                organizationList = new ArrayList<>();
//                MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//                for (InvOrganizationSite detail : CustomerList) {
//                    mapperFactory.classMap(InvOrganizationSite.class, InvOrganizationSite.class)
//                            .byDefault().register();
//                    MapperFacade mapper = mapperFactory.getMapperFacade();
//                    InvOrganizationSite dest = mapper.map(detail, InvOrganizationSite.class);
//                    organizationList.add(dest);
//                }
                }
//                organizationList = CustomerList;
//                supplierConvertor = new InvOrganizationSiteConverter(organizationList);
            } else if (type == 1) {
                if (supplierList == null || supplierList.size() == 0) {
//                    supplierList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1);
                }
//                organizationList = supplierList;
//                supplierConvertor = new InvOrganizationSiteConverter(organizationList);
            } else if (type == 2) {
                if (glAccountList == null || glAccountList.size() == 0) {
                    if (allowAccount) {
                        glAccountList = glAccountService.getBranchGLAccountsActiveWithoutGlBankAccounts(branchId);
                    } else {
                        glAccountList = new ArrayList<>();
                        for (Symbol symbol : getGlItemsList()) {
                            setAccountGLItems(new GlAccount());
                            setAccountGLItems(symbol.getAccountId());
                            glAccountList.add(getAccountGLItems());
                        }
                    }
                }

            }

            if ((type == 0 || type == 1) && organizationList != null && !organizationList.isEmpty()) {
                glAccountList = new ArrayList<>();
//                for (InvOrganizationSite invOrganizationSite : organizationList) {
//                    account = new GlAccount();
//                    account = invOrganizationSite.getAccountId();
//                    glAccountList.add(account);
//                }

//                setInvOrganizationSiteConverter(new InvOrganizationSiteConverter(organizationList));
            }

            setGlAccountConverter(new GlaccountConverter(glAccountList));
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "fillAccounts");
        }
    }

    public void checkVisability() {
        try {

            viewFooterColms();
            fillAccounts();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "checkVisability");
        }
    }

    public void viewFooterColms() {
        try {
            if (glBankTransactionEntity.getOrganizationType() == 2) {
                organizationSiteView = false;
                accounts = true;
            } else {
                accounts = false;
                organizationSiteView = true;
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "viewFooterColms");
        }
    }

    public void showOrHideDialog() {
        try {
            if (glBankTransactionEntity.getId() == null && (glBankTransactionDetailEntityList == null || glBankTransactionDetailEntityList.isEmpty())) {
                changeOrganizationSiteOk();
            } else {
                OpenDlg("dlg1");
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "showOrHideDialog");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "CloseDlg");
        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
            OrganizationTypeAfterDialogConfirmation = glBankTransactionEntity.getOrganizationType();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "OpenDlg");
        }
    }

    public void loadDialog() {
        try {
            setGlBankTransactionListPre(glBankTransactionService.getALLGlBankTransactionRecievableNotloadByBranchId(getBranchId(), 3));
            OpenDlg("dlg2");
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "loadDialog");
        }

    }

    public void loadnotes() {
        try {
            findSettlementDeedObject(glBankTransactionSelectionPre.getId(), false);
            paymentDropDownView = true;
            glBankTransactionEntity.setSerial(null);
            glBankTransactionEntity.setId(null);
            glBankTransactionEntity.setSerailParent(glBankTransactionSelectionPre.getSerial());
            for (GlBankTransactionDetail transactionDetail : glBankTransactionDetailsList) {
                transactionDetail.setSerial(null);
                transactionDetail.setId(null);
            }
            CloseDlg("dlg2");
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "loadnotes");
        }

    }

    public void changeOrganizationSiteCancel() {
        glBankTransactionEntity.setOrganizationType(organizationTypeUpdated);
    }

    public void changeOrganizationSiteOk() {
        try {
//            if (glBankTransactionDetailEntityList != null && !glBankTransactionDetailEntityList.isEmpty()) {
//                glBankTransactionEntity.setOrganizationType(OrganizationTypeAfterDialogConfirmation);
//            }
            if (!Objects.equals(organizationTypeUpdated, glBankTransactionEntity.getOrganizationType())) {
                organizationTypeUpdated = glBankTransactionEntity.getOrganizationType();
                glBankTransactionDetailEntityList = new ArrayList<>();

                glBankTransactionDetailsDeletedList = glBankTransactionDetailsList;
            }
            valueSummary = localValue = BigDecimal.ZERO;

            glBankTransactionDetailEntitySelected = new GlBankTransactionDetailEntity();
            glBankTransactionDetailEntityList = new ArrayList<>();
            fillAccounts();
            viewFooterColms();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "changeOrganizationSiteOk");
        }
    }

    public void viewPaymentFields() {
        try {

            if (glBankTransactionEntity.getPaymentType() == 0) {
                checkView = false;
                lineCheckView = false;
            } else if (glBankTransactionEntity.getPaymentType() == 1) {
                checkView = true;
                lineCheckView = false;
            } else {
                checkView = false;
                lineCheckView = true;
            }

            fillGlBanks();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "viewPaymentFields");
        }

    }

    public void updateDebitAccount() {
        try {
            if (glBankTransactionDetailEntitySelected != null) {

                if (glBankTransactionDetailEntitySelected.getInvOrganizationSite() == null || glBankTransactionDetailEntitySelected.getInvOrganizationSite().getAccountId() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل مربوطه على حساب"));
                    return;
                } else {
                    glBankTransactionDetailEntitySelected.setAccountDebitId(glBankTransactionDetailEntitySelected.getInvOrganizationSite().getAccountId().getId());
                }

                if (glBankTransactionEntity.getGlBank() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة"));
                } else {
                    glBankTransactionDetailEntitySelected.setAccountCreditId(glBankTransactionEntity.getGlBank().getAccountId().getId());
                }

                glBankTransactionDetailEntitySelected.setGlBankTransactionInvoiceViewList(bankTransactionInvoiceViewService.getGlBankTransactionInvoiceViewList(glBankTransactionDetailEntitySelected.getInvOrganizationSite().getId(), getUserData().getUser()));
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateDebitAccount");
        }
    }

    public void updateDebitAccountGlItems() {
        try {
            if (glBankTransactionDetailEntitySelected != null) {

                if (glBankTransactionDetailEntitySelected.getGlItems() == null || glBankTransactionDetailEntitySelected.getGlItems().getAccountId() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار  البند  مربوط على حساب"));
                    return;
                } else {
                    glBankTransactionDetailEntitySelected.setAccountDebitId(glBankTransactionDetailEntitySelected.getGlItems().getAccountId().getId());
                }

                if (glBankTransactionEntity.getGlBank() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة"));
                } else {
                    glBankTransactionDetailEntitySelected.setAccountCreditId(glBankTransactionEntity.getGlBank().getAccountId().getId());
                }
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateDebitAccount");
        }
    }

    private GlBankTransactionEntity mapGlBankTransactionToGlBankTransactionEntity(GlBankTransaction bankTransaction, Boolean isUpdate) {
        glBankTransactionEntityMapper = new GlBankTransactionEntity();
        try {

            glBankTransactionEntityMapper.setId(bankTransaction.getId());
            glBankTransactionEntityMapper.setSerial(bankTransaction.getSerial());
            glBankTransactionEntityMapper.setDate(bankTransaction.getDate() != null ? bankTransaction.getDate() : new Date());
            glBankTransactionEntityMapper.setPaymentType(bankTransaction.getPaymentType() != null ? bankTransaction.getPaymentType() : 0);

            if (bankTransaction.getPaymentType() == 1) {
                glBankTransactionEntityMapper.setChequeDate(bankTransaction.getChequeDate() != null ? bankTransaction.getChequeDate() : new Date());
                glBankTransactionEntityMapper.setChequeDueDate(bankTransaction.getChequeDueDate() != null ? bankTransaction.getChequeDueDate() : new Date());
                glBankTransactionEntityMapper.setChequeNumber(bankTransaction.getChequeNumber() != null ? bankTransaction.getChequeNumber() : null);
                glBankTransactionEntityMapper.setChequeStatus(bankTransaction.getChequeStatus() != null ? bankTransaction.getChequeStatus() : 0);
                paymentTypeName = "الشيك";
            } else if (bankTransaction.getPaymentType() == 2) {
                glBankTransactionEntityMapper.setChequeStatus(bankTransaction.getChequeStatus() != null ? bankTransaction.getChequeStatus() : 0);
                paymentTypeName = "الشيك الخطي";
            } else {
                paymentTypeName = "نقدي";
            }

            glBankTransactionEntityMapper.setGlBank(bankTransaction.getGlBankId() != null ? bankTransaction.getGlBankId() : null);
            glBankTransactionEntityMapper.setOrganizationType(bankTransaction.getOrganizationType() != null ? bankTransaction.getOrganizationType() : 0);
            glBankTransactionEntityMapper.setRemark(bankTransaction.getRemark() != null ? bankTransaction.getRemark() : null);
            glBankTransactionEntityMapper.setRemark2(bankTransaction.getRemark2() != null ? bankTransaction.getRemark2() : null);
            glBankTransactionEntityMapper.setTransactionType(bankTransaction.getTransactionType() != null ? bankTransaction.getTransactionType() : 0);
            glBankTransactionEntityMapper.setValue(bankTransaction.getValue() != null ? bankTransaction.getValue() : BigDecimal.ZERO);
            glBankTransactionEntityMapper.setRate(bankTransaction.getRate() != null ? bankTransaction.getRate() : BigDecimal.ONE);
//            glBankTransactionEntityMapper.setValueLocal(bankTransaction.getValueLocal() == null ? BigDecimal.ZERO : bankTransaction.getValueLocal());
            glBankTransactionEntityMapper.setValueLocal(bankTransaction.getValue() == null ? BigDecimal.ZERO : bankTransaction.getValue());
            glBankTransactionEntityMapper.setGlYear(bankTransaction.getGlYear() != null ? bankTransaction.getGlYear() : null);
            glBankTransactionEntityMapper.setPostFlag(bankTransaction.getPostFlag());
            glBankTransactionEntityMapper.setCreatedNameParent(bankTransaction.getParent() != null ? bankTransaction.getParent().getCreatedBy().getName() : null);
            glBankTransactionEntityMapper.setCreatedByName(bankTransaction.getCreatedBy().getName());
            glBankTransactionEntityMapper.setSerailParent(bankTransaction.getParent() != null ? bankTransaction.getParent().getSerial() : null);
            glBankTransactionEntityMapper.setCreatedBy(bankTransaction.getCreatedBy());
            glBankTransactionEntityMapper.setCreationDate(bankTransaction.getCreationDate());
            if (isUpdate) {
                setGlBankTransactionSelectionPre(bankTransaction.getParent());
            }
            for (GlBankTransactionDetail transactionDetail : glBankTransactionDetailsList) {
                glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
                ++index;
                glBankTransactionDetailEntity.setIndex(index);

                glBankTransactionDetailEntity.setId(transactionDetail.getId() != null ? transactionDetail.getId() : null);
                glBankTransactionDetailEntity.setCostCenterId(transactionDetail.getCostCenterId() != null ? transactionDetail.getCostCenterId().getId() : null);
                glBankTransactionDetailEntity.setCostCenter(transactionDetail.getCostCenterId() != null ? transactionDetail.getCostCenterId() : null);
                glBankTransactionDetailEntity.setAccountCreditId(transactionDetail.getAccountCreditId() != null ? transactionDetail.getAccountCreditId().getId() : null);
                glBankTransactionDetailEntity.setGlAccount(transactionDetail.getAccountDebitId() != null ? transactionDetail.getAccountDebitId() : null);
                glBankTransactionDetailEntity.setAccountDebitId(transactionDetail.getAccountDebitId() != null ? transactionDetail.getAccountDebitId().getId() : null);
                glBankTransactionDetailEntity.setAdminUnitId(transactionDetail.getAdminUnitId() != null ? transactionDetail.getAdminUnitId().getId() : null);
                glBankTransactionDetailEntity.setGlAdminUnit(transactionDetail.getAdminUnitId() != null ? transactionDetail.getAdminUnitId() : null);
                glBankTransactionDetailEntity.setGlBankTransactionId(transactionDetail.getGlBankTransactionId() != null ? transactionDetail.getGlBankTransactionId().getId() : null);
                glBankTransactionDetailEntity.setRemarks(transactionDetail.getRemarks() != null ? transactionDetail.getRemarks() : null);
                if (transactionDetail.getInvoiceId() != null) {
                    glBankTransactionDetailEntity.setInvoiceId(transactionDetail.getInvoiceId().getId());
                }
                InvOrganizationSite ios = transactionDetail.getOrganizationSiteId();
                InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                if (ios != null) {
                    invOrganizationSiteDTO.setId(ios.getId());
                    invOrganizationSiteDTO.setName(ios.getName());
                    invOrganizationSiteDTO.setCode(ios.getCode());
                    invOrganizationSiteDTO.setMobile(ios.getMobile());
                    if(ios.getAccountId() != null){
                        GlAccountDTO glAccountDTO = new GlAccountDTO();
                        glAccountDTO.setId(ios.getAccountId().getId());
                        glAccountDTO.setName(ios.getAccountId().getName());
                        glAccountDTO.setAccNumber(ios.getAccountId().getAccNumber());
                        invOrganizationSiteDTO.setAccountId(glAccountDTO);
                    }
                }
                glBankTransactionDetailEntity.setInvOrganizationSite(transactionDetail.getOrganizationSiteId() != null ? invOrganizationSiteDTO : null);
                glBankTransactionDetailEntity.setValue(transactionDetail.getValue() != null ? transactionDetail.getValue() : null);
//                glBankTransactionDetailEntity.setValueLocal(transactionDetail.getValueLocal() != null ? transactionDetail.getValueLocal() : null);
                glBankTransactionDetailEntity.setValueLocal(transactionDetail.getValue() != null ? transactionDetail.getValue() : null);
                glBankTransactionDetailEntity.setSerial(transactionDetail.getSerial() != null ? transactionDetail.getSerial() : null);
                glBankTransactionDetailEntity.setGlItems(transactionDetail.getSymbolId());
                glBankTransactionDetailEntity.setOrganizationsiteName(transactionDetail.getOrganizationSiteId() == null ? null : transactionDetail.getOrganizationSiteId().getName());
                valueSummary = valueSummary.add(glBankTransactionDetailEntity.getValue() != null ? glBankTransactionDetailEntity.getValue() : BigDecimal.ZERO);
                localValue = localValue.add(glBankTransactionDetailEntity.getValue() != null ? glBankTransactionDetailEntity.getValue() : BigDecimal.ZERO);
//                localValue = localValue.add(glBankTransactionDetailEntity.getValueLocal() != null ? glBankTransactionDetailEntity.getValueLocal() : BigDecimal.ZERO);
                glBankTransactionDetailsMap.put(transactionDetail.getId(), transactionDetail);
                glBankTransactionDetailEntityList.add(glBankTransactionDetailEntity);
            }

//        localValue = valueSummary.multiply(glBankTransactionEntityMapper.getRate());
            return glBankTransactionEntityMapper;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "mapGlBankTransactionToGlBankTransactionEntity");
            return glBankTransactionEntityMapper;
        }
    }

    public void updateTransactionRate() {
        try {

//            checkDate(true);
            if (glBankTransactionEntity.getGlBank() != null && glBankTransactionEntity.getGlBank().getCurrencyId() != null) {
                if (glBankTransactionEntity.getId() == null) {
//                    glBankTransactionEntity.setRate(updateRate(glBankTransactionEntity.getGlBank().getCurrencyId(), glBankTransactionEntity.getDate()));
                    glBankTransactionEntity.setRate(BigDecimal.ONE);
                }
                glBankTransactionEntity.setRate(glBankTransactionEntity.getRate() != null ? glBankTransactionEntity.getRate().setScale(2, BigDecimal.ROUND_UP) : null);

                changeRate();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار العملة"));
                glBankTransactionEntity.setValue(null);
                glBankTransactionEntity.setValueLocal(null);
            }

            checkAllowDate();

        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateTransactionRate");
        }
    }

    public void changeRate() {
        try {
            calculateLocalValue();
            updatetransactionDetailLocalValue();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "changeRate");
        }
    }

    public void catchValues() {
        int x = 0;
    }

    @Override
    public void calculateLocalValue() {
        try {
            glBankTransactionEntity.setValueLocal((glBankTransactionEntity.getValue() == null ? BigDecimal.ZERO : glBankTransactionEntity.getValue()).multiply(glBankTransactionEntity.getRate() == null ? BigDecimal.ONE : glBankTransactionEntity.getRate()));
            glBankTransactionEntity.setValueLocal(glBankTransactionEntity.getValue() != null ? glBankTransactionEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
//            glBankTransactionEntity.setValueLocal(glBankTransactionEntity.getValueLocal() != null ? glBankTransactionEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "calculateLocalValue");
        }
    }

    public void setNewValueOfAmount(GlBankTransactionDetailEntity bankTransactionDetailEntity) {
        glBankTransactionEntity.setValue(bankTransactionDetailEntity.getValue());
        changeRate();
    }

    public void updatetransactionDetailLocalValue() {
        try {
            localValue = BigDecimal.ZERO;
            for (GlBankTransactionDetailEntity detailEntity : glBankTransactionDetailEntityList) {
//                BigDecimal detailRate = null;
                BigDecimal detailRate = BigDecimal.ONE;
//                if (accounts) {
//                    detailRate = updateRate(detailEntity.getGlAccount() != null ? detailEntity.getGlAccount().getCurrencyId() : null, glBankTransactionEntity.getDate());
//                } else if (organizationSiteView) {
//                    detailRate = updateRate(detailEntity.getInvOrganizationSite() != null ? detailEntity.getInvOrganizationSite().getCurrencyId() : null, glBankTransactionEntity.getDate());
//                }
                detailEntity.setValueLocal((detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue()).multiply(detailRate == null ? BigDecimal.ONE : detailRate));
                detailEntity.setValueLocal(detailEntity.getValue() != null ? detailEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
//                detailEntity.setValueLocal(detailEntity.getValueLocal() != null ? detailEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
                localValue = (localValue == null ? BigDecimal.ZERO : localValue).add(detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue());
//                localValue = (localValue == null ? BigDecimal.ZERO : localValue).add(detailEntity.getValueLocal() == null ? BigDecimal.ZERO : detailEntity.getValueLocal());
            }
            localValue = localValue.setScale(2, BigDecimal.ROUND_UP);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updatetransactionDetailLocalValue");
        }
//        updateLocalValueSummary();
    }

    public void addGlBankTransactionDetail() {
        try {
            Integer organizationType = 0;
            if (glBankTransactionEntity != null && glBankTransactionEntity.getOrganizationType() != null) {
                organizationType = glBankTransactionEntity.getOrganizationType();
            } else if (glBankTransactionExist != null && glBankTransactionExist.getOrganizationType() != null) {
                organizationType = glBankTransactionExist.getOrganizationType();
            }

            if (glBankTransactionDetailEntityList != null && !glBankTransactionDetailEntityList.isEmpty()) {
                GlBankTransactionDetailEntity bankTransactionDetailEntity = glBankTransactionDetailEntityList.get(glBankTransactionDetailEntityList.size() - 1);
                if ((organizationType.compareTo(0) == 0 || organizationType.compareTo(1) == 0)) {
                    if (bankTransactionDetailEntity.getInvOrganizationSite() == null || bankTransactionDetailEntity.getInvOrganizationSite().getAccountId() == null
                            || bankTransactionDetailEntity.getValue() == null || bankTransactionDetailEntity.getValue().compareTo(BigDecimal.ZERO) == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب استكمال البيانات"));
                    } else {
                        addNewGlBankItem();
                    }
                } else if (organizationType.compareTo(2) == 0) {
                    if ((bankTransactionDetailEntity.getGlAccount() == null && bankTransactionDetailEntity.getGlItems() == null)
                            || bankTransactionDetailEntity.getValue() == null || bankTransactionDetailEntity.getValue().compareTo(BigDecimal.ZERO) == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب استكمال البيانات"));
                    } else {
                        addNewGlBankItem();
                    }
                }
            } else {
                addNewGlBankItem();
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "addGlBankTransactionDetail");
        }
    }

    private void addNewGlBankItem() {
        try {
            for (GlBankTransactionDetailEntity detailEntity : glBankTransactionDetailEntityList) {
                detailEntity.setMarkEdit(Boolean.FALSE);
            }

            GlBankTransactionDetailEntity glBankTransactionDetailsEntityNew = new GlBankTransactionDetailEntity();
            glBankTransactionDetailsEntityNew.setMarkEdit(Boolean.TRUE);
            glBankTransactionDetailsEntityNew.setIndex(++index);

            if (glBankTransactionDetailEntityUnSelected != null && glBankTransactionDetailEntityUnSelected.getMarkEdit()) {
                glBankTransactionDetailEntityUnSelected.setMarkEdit(false);
            }
            glBankTransactionDetailEntitySelected = glBankTransactionDetailsEntityNew;
            glBankTransactionDetailEntityUnSelected = glBankTransactionDetailsEntityNew;

            glBankTransactionDetailEntityList.add(glBankTransactionDetailsEntityNew);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "addNewGlBankItem");
        }
    }

    public void deleteGlBankTransactionDetail() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);

            if (glBankTransactionDetailEntitySelected.getId() != null) {
                getRowsDeleted().add(glBankTransactionDetailEntitySelected);
            }

            valueSummary = valueSummary.subtract(glBankTransactionDetailEntitySelected.getValue() != null ? glBankTransactionDetailEntitySelected.getValue() : BigDecimal.ZERO);

//            localValue = (localValue == null ? BigDecimal.ZERO : localValue).subtract(glBankTransactionDetailEntitySelected.getValueLocal() != null ? glBankTransactionDetailEntitySelected.getValueLocal() : BigDecimal.ZERO);
            localValue = (localValue == null ? BigDecimal.ZERO : localValue).subtract(glBankTransactionDetailEntitySelected.getValue() != null ? glBankTransactionDetailEntitySelected.getValue() : BigDecimal.ZERO);

//        updateLocalValueSummary();
            glBankTransactionDetailEntityList.remove(glBankTransactionDetailEntitySelected);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "deleteGlBankTransactionDetail");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            GlBankTransactionDetailEntity transactionDetailEntityUpdated = context.getApplication().evaluateExpressionGet(context, "#{glBankTransactionDetailsTable}", GlBankTransactionDetailEntity.class);

            glBankTransactionDetailEntitySelected = transactionDetailEntityUpdated;
            String column_name = event.getColumn().getClientId();

            if (glBankTransactionDetailEntitySelected != null) {

                if (column_name.contains("value")) {

                    event.getSource();
                    BigDecimal newValue = (BigDecimal) event.getNewValue();
                    BigDecimal oldValue = (BigDecimal) event.getOldValue();

//                    BigDecimal detailRate = null;
                    BigDecimal detailRate = BigDecimal.ONE;
//                    if (accounts) {
//                        detailRate = updateRate(glBankTransactionDetailEntitySelected.getGlAccount().getCurrencyId(), glBankTransactionEntity.getDate());
//                    } else if (organizationSiteView) {
//                        detailRate = updateRate(glBankTransactionDetailEntitySelected.getInvOrganizationSite().getCurrencyId(), glBankTransactionEntity.getDate());
//                    }
                    glBankTransactionDetailEntitySelected.setValueLocal(newValue != null ? newValue.multiply(detailRate) : null);

                    updateSummition(newValue, oldValue, detailRate);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لم يتم تحديد بيانات الصرف"));
                setShowMessageDetails(Boolean.TRUE);
                setShowMessageGeneral(Boolean.FALSE);
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "onCellEdit");
        }
    }

    public void onRowSelectDetails(SelectEvent e) {
        try {

            if (glBankTransactionDetailEntityUnSelected != null && !glBankTransactionDetailEntityUnSelected.equals(glBankTransactionDetailEntitySelected)) {
                glBankTransactionDetailEntityUnSelected.setMarkEdit(false);
                glBankTransactionDetailEntitySelected.setMarkEdit(true);
            } else if (glBankTransactionDetailEntityUnSelected != null && glBankTransactionDetailEntityUnSelected.equals(glBankTransactionDetailEntitySelected) && glBankTransactionDetailEntitySelected.getMarkEdit() != null && glBankTransactionDetailEntitySelected.getMarkEdit()) {
                glBankTransactionDetailEntitySelected.setMarkEdit(false);
            } else if (glBankTransactionDetailEntityUnSelected != null && glBankTransactionDetailEntityUnSelected.equals(glBankTransactionDetailEntitySelected) && glBankTransactionDetailEntitySelected.getMarkEdit() != null && !glBankTransactionDetailEntitySelected.getMarkEdit()) {
                glBankTransactionDetailEntitySelected.setMarkEdit(true);
            } else {
                glBankTransactionDetailEntitySelected.setMarkEdit(true);
            }
            glBankTransactionDetailEntityUnSelected = glBankTransactionDetailEntitySelected;
            forHashasa();
            e.getSource();
        } catch (Exception ex) {
            saveError(ex, "SettlementDeedFormMB", "onRowSelectDetails");
        }
    }

    public void onRowUnselect(UnselectEvent e) {
        e.getSource();
        System.out.println("Row unselected!!!");
    }

    public void updateSummition(BigDecimal newValue, BigDecimal oldValue, BigDecimal detailRate) {
        try {
//        glBankTransactionEntity.getRate();

            valueSummary = valueSummary.subtract(oldValue == null ? BigDecimal.ZERO : oldValue);
            valueSummary = valueSummary.add(newValue == null ? BigDecimal.ZERO : newValue);
            updateLocalValueSummary(detailRate);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateSummition");
        }
    }

    public void updateLocalValueSummary(BigDecimal detailRate) {
        try {
            localValue = valueSummary.multiply(detailRate != null ? detailRate : BigDecimal.ZERO);
            localValue = localValue.setScale(2, BigDecimal.ROUND_UP);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateLocalValueSummary");
        }
    }

    @Override
    public String save() {
        try {

            saveValidation = true;
            if (glBankTransactionEntity != null) {

                if (glBankTransactionDetailEntityList != null && !glBankTransactionDetailEntityList.isEmpty()) {

                    if (!checkAllowDate()) {
                        return "";
                    }

                    if (glBankTransactionEntity.getValue() != null && glBankTransactionEntity.getValue() != null
                            && glBankTransactionEntity.getValue().compareTo(BigDecimal.ZERO) != 0
                            && localValue.compareTo(BigDecimal.ZERO) != 0) {
                        if (localValue.compareTo(glBankTransactionEntity.getValue()) != 0) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "المبلغ المحلي لا يساوي اجمالي المبلغ"));
                            setShowMessageDetails(true);
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اعطاء قيمة للمبلغ"));
                        setShowMessageDetails(true);
                        return "";
                    }

                    glBankTransaction.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                    glBankTransaction.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

                    if (glBankTransactionSelectionPre != null && glBankTransactionSelectionPre.getId() != null) {
                        if (!uri.contains("notesreceivablesdatailFormPre")) {
                            glBankTransaction.setParent(glBankTransactionSelectionPre);
                        }

                    }

                    if (glBankTransactionEntity.getId() != null) {
                        glBankTransaction.setId(glBankTransactionEntity.getId());
                        // glBankTransaction.setCreatedBy(getGlBankTransactionExist().getCreatedBy() != null ? getGlBankTransactionExist().getCreatedBy() : null);
                        // glBankTransaction.setCreationDate(getGlBankTransactionExist().getCreationDate() != null ? getGlBankTransactionExist().getCreationDate() : null);

                        glBankTransaction.setSerial(glBankTransactionEntity.getSerial());
                        glBankTransaction.setCreatedBy(glBankTransactionEntity.getCreatedBy());
                        glBankTransaction.setCreationDate(glBankTransactionEntity.getCreationDate());
                        glBankTransaction.setModifiedBy(getUserData().getUser());
                        glBankTransaction.setModificationDate(new Date());

                    } else {
                        glBankTransaction.setChequeStatus(0);
                        glBankTransaction.setCreatedBy(getUserData().getUser());
                        glBankTransaction.setCreationDate(new Date());

                    }

                    glBankTransaction.setDate(glBankTransactionEntity.getDate() != null ? glBankTransactionEntity.getDate() : null);
                    glBankTransaction.setPostFlag(0);
                    glBankTransaction.setPaymentType(glBankTransactionEntity.getPaymentType());
                    glBankTransaction.setRemark(glBankTransactionEntity.getRemark());
                    glBankTransaction.setRemark2(glBankTransactionEntity.getRemark2());
                    glBankTransaction.setOrganizationType(glBankTransactionExist != null ? glBankTransactionExist.getOrganizationType() : glBankTransactionEntity.getOrganizationType());

                    String noteSreceivablesType = accountsSystemSettings.getNoteSreceivablesType();

                    if (getUri().contains("settlementdeedForm")) {
                        glBankTransaction.setTransactionType(0);
                    } else if (getUri().contains("notesreceivablesdatailFormPre") || getUri().contains("notesreceivablesFormpre")) {
                        glBankTransaction.setTransactionType(3);
                    } else if (getUri().contains("notesreceivablesForm") || getUri().contains("notesreceivablesdatailForm")) {
                        glBankTransaction.setTransactionType(1);
                    }

                    glBankTransaction.setValue(glBankTransactionEntity.getValue() != null ? glBankTransactionEntity.getValue() : BigDecimal.ZERO);
                    glBankTransaction.setValueLocal(glBankTransactionEntity.getValue() != null ? glBankTransactionEntity.getValue() : BigDecimal.ZERO);
//                    glBankTransaction.setValueLocal(glBankTransactionEntity.getValueLocal() != null ? glBankTransactionEntity.getValueLocal() : BigDecimal.ZERO);
                    glBankTransaction.setRate(glBankTransactionEntity.getRate() != null ? glBankTransactionEntity.getRate() : BigDecimal.ONE);
                    glBankTransaction.setGlYear(getGlYearSelection().getYear() != null ? getGlYearSelection().getYear() : null);

                    if (glBankTransactionEntity.getPaymentType() == 1) {
                        glBankTransaction.setChequeDate(glBankTransactionEntity.getChequeDate() != null ? glBankTransactionEntity.getChequeDate() : new Date());
                        glBankTransaction.setChequeDueDate(glBankTransactionEntity.getChequeDueDate() != null ? glBankTransactionEntity.getChequeDueDate() : new Date());
                        glBankTransaction.setChequeNumber(glBankTransactionEntity.getChequeNumber() != null ? glBankTransactionEntity.getChequeNumber() : null);
                        glBankTransaction.setChequeStatus(glBankTransactionEntity.getChequeStatus() != null ? glBankTransactionEntity.getChequeStatus() : 0);
                    } else if (glBankTransactionEntity.getPaymentType() == 2) {
                        glBankTransaction.setChequeStatus(glBankTransactionEntity.getChequeStatus() != null ? glBankTransactionEntity.getChequeStatus() : 0);
                    }

                    if (glBankTransactionEntity.getGlBank() != null) {
                        glBankTransaction.setGlBankId(glBankTransactionEntity.getGlBank());
                    }

                    if (glBankTransactionEntity.getInvoiceId() != null) {
                        InvPurchaseInvoice invoiceId = new InvPurchaseInvoice();
                        invoiceId.setId(glBankTransactionEntity.getInvoiceId());
                        glBankTransaction.setInvoiceId(invoiceId);
                    }
                    glBankTransactionDetailsUpdatedList = new ArrayList<>();

                    GlBankTransactionDetail bankTransactionDetail;
                    for (GlBankTransactionDetailEntity detailsEntity : glBankTransactionDetailEntityList) {

                        bankTransactionDetail = new GlBankTransactionDetail();

                        if (detailsEntity.getId() != null) {
                            bankTransactionDetail = glBankTransactionDetailsMap.get(detailsEntity.getId());
                            bankTransactionDetail.setModificationDate(new Date());
                            bankTransactionDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                        } else {
                            bankTransactionDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                            bankTransactionDetail.setCreationDate(new Date());
                        }

                        bankTransactionDetail.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

                        if (getUri().contains("notesreceivablesdatailForm")) {
                            bankTransactionDetail.setBankIdFrom(glBankTransactionEntity.getGlBank());
                            setAccountCreditId(glBankTransaction.getOrganizationType(), detailsEntity, bankTransactionDetail);

                            if (glBankTransaction.getGlBankId() != null && glBankTransaction.getGlBankId().getAccountId() != null) {
                                bankTransactionDetail.setAccountDebitId(glBankTransaction.getGlBankId().getAccountId());
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "الخزنة غير مربوطة بحساب او لم يتم اختيار الخزنة"));
                                setShowMessageDetails(true);
                                return "";
                            }
                        } else {
                            bankTransactionDetail.setBankIdTo(glBankTransactionEntity.getGlBank());
                            setAccountDebitId(glBankTransaction.getOrganizationType(), detailsEntity, bankTransactionDetail);

                            if (glBankTransaction.getGlBankId() != null && glBankTransaction.getGlBankId().getAccountId() != null) {
                                bankTransactionDetail.setAccountCreditId(glBankTransaction.getGlBankId().getAccountId());
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "الخزنة غير مربوطة بحساب او لم يتم اختيار الخزنة"));
                                setShowMessageDetails(true);
                                return "";
                            }
                        }

                        /* if (detailsEntity.getAdminUnitId() != null) {
                         GlAdminUnit glAdminUnit = new GlAdminUnit();
                         glAdminUnit.setId(detailsEntity.getAdminUnitId());
                         bankTransactionDetail.setAdminUnitId(glAdminUnit);
                         } else {
                         bankTransactionDetail.setAdminUnitId(null);
                         }*/
                        bankTransactionDetail.setAdminUnitId(detailsEntity.getGlAdminUnit() != null ? detailsEntity.getGlAdminUnit() : null);

                        /*if (detailsEntity.getCostCenterId() != null) {
                         CostCenter costCenter = new CostCenter();
                         costCenter.setId(detailsEntity.getCostCenterId());
                         bankTransactionDetail.setCostCenterId(costCenter);
                         } else {
                         bankTransactionDetail.setCostCenterId(null);
                         }*/
                        bankTransactionDetail.setCostCenterId(detailsEntity.getCostCenter() != null ? detailsEntity.getCostCenter() : null);
//                if(detailsEntity.getInvOrganizationSite() != null){}
                        InvOrganizationSiteDTO ios = detailsEntity.getInvOrganizationSite();
                        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                        if (ios != null) {
                            invOrganizationSite.setId(ios.getId());
                            GlAccount accountId = new GlAccount();
                            accountId.setId(detailsEntity.getInvOrganizationSite().getAccountId().getId());
                            invOrganizationSite.setAccountId(accountId);
                        }
                        bankTransactionDetail.setOrganizationSiteId(detailsEntity.getInvOrganizationSite() != null ? invOrganizationSite : null);

                        if (detailsEntity.getInvoiceId() != null) {
                            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
                            invPurchaseInvoice.setId(detailsEntity.getInvoiceId());
                            bankTransactionDetail.setInvoiceId(invPurchaseInvoice);
                        }

                        bankTransactionDetail.setRemarks(detailsEntity.getRemarks());
                        bankTransactionDetail.setValue(detailsEntity.getValue() != null ? detailsEntity.getValue() : BigDecimal.ZERO);
                        bankTransactionDetail.setValueLocal(detailsEntity.getValue() != null ? detailsEntity.getValue() : BigDecimal.ZERO);
//                        bankTransactionDetail.setValueLocal(detailsEntity.getValueLocal() != null ? detailsEntity.getValueLocal() : BigDecimal.ZERO);

                        bankTransactionDetail.setSymbolId(detailsEntity.getGlItems());
                        glBankTransactionDetailsUpdatedList.add(bankTransactionDetail);
                        validateSave(glBankTransaction, bankTransactionDetail);
                    }

                    if (getRowsDeleted() != null && !getRowsDeleted().isEmpty()) {
                        for (GlBankTransactionDetailEntity rowDeleted : getRowsDeleted()) {
                            bankTransactionDetail = new GlBankTransactionDetail();
                            bankTransactionDetail.setId(rowDeleted.getId());
                            glBankTransactionDetailsDeletedList.add(bankTransactionDetail);
                        }
                    }
                    if (saveValidation != false) {
                        GlBankTransaction glBankTransId = glBankTransactionService.addGlBankTransaction(glBankTransaction, glBankTransactionDetailsUpdatedList, glBankTransactionDetailsDeletedList, companyId);
                        rowsDeleted = new ArrayList<>();
                        glBankTransactionDetailsDeletedList = new ArrayList<>();
                        findSettlementDeedObject(glBankTransId.getId(), true);

                        if (getUri().contains("settlementdeedForm")) {
                            exit("../settlementdeed/settlementdeedList.xhtml");
                        } else if (getUri().contains("notesreceivablesdatailFormPre") || getUri().contains("notesreceivablesFormpre")) {
                            exit("../notesreceivablespre/notesreceivablesListpre.xhtml");
                        } else if (getUri().contains("notesreceivablesForm") || getUri().contains("notesreceivablesdatailForm")) {
                            exit("../notesreceivables/notesreceivablesList.xhtml");
                        }

//                        if (getUri().contains("notesreceivablesdatailForm")) {
//                            exit("../notesreceivables/notesreceivablesList.xhtml");
//                        } else {
//                            exit("../settlementdeed/settlementdeedList.xhtml");
//                        }
                    }

                    return "";
                } else {
                    glBankTransactionDetailsUpdatedList = new ArrayList<>();
                }
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ملئ بيانات سند الصرف"));
                setShowMessageDetails(true);
            }

            return "";
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "save");
            return null;
        }
    }

    private void setAccountCreditId(Integer organizationType, GlBankTransactionDetailEntity detailsEntity, GlBankTransactionDetail bankTransactionDetail) {

        if (organizationType == 2) {
            if (allowAccount) {
                //GlAccount accountDebitId = new GlAccount();
                // accountDebitId.setId(detailsEntity.getAccountDebitId());
                bankTransactionDetail.setAccountCreditId(detailsEntity.getGlAccount());
            } else {
                bankTransactionDetail.setAccountCreditId(detailsEntity.getGlItems().getAccountId());
            }
        } else {
            if (detailsEntity.getInvOrganizationSite() != null && detailsEntity.getInvOrganizationSite().getAccountId() != null) {
                GlAccountDTO accountDTO = detailsEntity.getInvOrganizationSite().getAccountId();
                GlAccount account = new GlAccount();
                account.setId(accountDTO.getId());
                account.setAccNumber(accountDTO.getAccNumber());
                account.setName(accountDTO.getName());
                bankTransactionDetail.setAccountCreditId(account);
            }
        }
    }

    private void setAccountDebitId(Integer organizationType, GlBankTransactionDetailEntity detailsEntity, GlBankTransactionDetail bankTransactionDetail) {
        if (organizationType == 2) {
            if (allowAccount) {
                //GlAccount accountDebitId = new GlAccount();
                // accountDebitId.setId(detailsEntity.getAccountDebitId());
                bankTransactionDetail.setAccountDebitId(detailsEntity.getGlAccount());
            } else {
                bankTransactionDetail.setAccountDebitId(detailsEntity.getGlItems().getAccountId());
            }
        } else {
            if (detailsEntity.getInvOrganizationSite() != null && detailsEntity.getInvOrganizationSite().getAccountId() != null) {
                GlAccountDTO accountDTO = detailsEntity.getInvOrganizationSite().getAccountId();
                GlAccount account = new GlAccount();
                account.setId(accountDTO.getId());
                account.setAccNumber(accountDTO.getAccNumber());
                account.setName(accountDTO.getName());
                bankTransactionDetail.setAccountDebitId(account);
            }
        }
//
//                            if (detailsEntity.getGlAccount() != null) {
//                                //GlAccount accountDebitId = new GlAccount();
//                                // accountDebitId.setId(detailsEntity.getAccountDebitId());
//                                bankTransactionDetail.setAccountDebitId(detailsEntity.getGlAccount());
//                            } else if (detailsEntity.getInvOrganizationSite() != null && detailsEntity.getInvOrganizationSite().getAccountId() != null) {
//                                bankTransactionDetail.setAccountDebitId(detailsEntity.getInvOrganizationSite().getAccountId());
//                            } else if (detailsEntity.getGlItems() != null && detailsEntity.getGlItems().getId() != null) {
//                                bankTransactionDetail.setAccountDebitId(detailsEntity.getGlItems().getAccountId());
//                            }
//         bankTransactionDetail.setAccountDebitId(detailsEntity.getGlAccount() != null ? detailsEntity.getGlAccount() : null);
    }

    public Boolean checkAllowDate() {
        try {
            if (glBankTransactionEntity.getDate().after(new Date())) {
                if (accountsSystemSettings.getAllowSubsequentCacheAdministration().equalsIgnoreCase("NOT_ALLOW_AFTER_DATE")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير مسموح بتاريخ اكبر من تاريخ اليوم"));
                    setShowMessageDetails(true);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "checkAllowDate");
            return false;
        }

    }

    public void validateSave(GlBankTransaction glBankTransaction, GlBankTransactionDetail bankTransactionDetail) {
        try {

            errorMessageBuilder = new StringBuilder();
            errorNoNeedMessageBuilder = new StringBuilder();
            GlAccount account;
            Integer x = 0;
            Integer y = 0;
            errorMessageBuilder.append("يجب اختيار ");
            errorNoNeedMessageBuilder.append("لا يمكن اختيار ");

            Integer organizationType = 0;
            if (glBankTransactionEntity != null && glBankTransactionEntity.getOrganizationType() != null) {
                organizationType = glBankTransactionEntity.getOrganizationType();
            } else if (glBankTransactionExist != null && glBankTransactionExist.getOrganizationType() != null) {
                organizationType = glBankTransactionExist.getOrganizationType();
            }

            if (glBankTransactionEntity != null && glBankTransactionEntity.getPostFlag() != null && glBankTransactionEntity.getPostFlag() == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "تم ترحيل السند"));
                setShowMessageDetails(true);
                saveValidation = false;
            }

            if (organizationType.compareTo(0) == 0 || organizationType.compareTo(1) == 0) {
                if (bankTransactionDetail.getOrganizationSiteId() != null) {
                    if (bankTransactionDetail.getOrganizationSiteId().getAccountId() == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "جهة التعامل غير مربوطة بحساب"));
                        setShowMessageDetails(true);
                        saveValidation = false;
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة تعامل"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
                if (bankTransactionDetail.getValue() == null || bankTransactionDetail.getValue().equals(BigDecimal.ZERO)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "قيمة غير صحيحة"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
            }
            if (organizationType.compareTo(2) == 0) {
                if (bankTransactionDetail.getAccountDebitId() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار حساب"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
                if (bankTransactionDetail.getValue().compareTo(BigDecimal.ZERO) == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب استكمال البيانات"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
                if (bankTransactionDetail.getAccountCreditId() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار حساب"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
                account = glAccountService.findGlAccount(bankTransactionDetail.getAccountDebitId() != null ? bankTransactionDetail.getAccountDebitId().getId() : null);

                if (account != null) {
                    if (account.getCostCenter().toString().equalsIgnoreCase("CC_MANDATORY") && bankTransactionDetail.getCostCenterId() == null) {
                        if (x > 0) {
                            errorMessageBuilder.append("و ");
                        }
                        errorMessageBuilder.append("مركز تكلفة ");
                        saveValidation = false;
                        x++;
                    }

                    if (account.getAdministrativeUnit().toString().equalsIgnoreCase("ADMUNT_MANDATORY") && bankTransactionDetail.getAdminUnitId() == null) {
                        if (x > 0) {
                            errorMessageBuilder.append("و ");
                        }
                        errorMessageBuilder.append("وحدة ادارية ");
                        saveValidation = false;
                        x++;
                    }

                    if (account.getAdministrativeUnit().toString().equalsIgnoreCase("ADMUNT_NONEED") && bankTransactionDetail.getAdminUnitId() != null) {
                        if (y > 0) {
                            errorNoNeedMessageBuilder.append("و ");
                        }
                        errorNoNeedMessageBuilder.append("وحدة ادارية ");
                        saveValidation = false;
                        y++;
                    }
                    if (account.getCostCenter().toString().equalsIgnoreCase("CC_NONEED") && bankTransactionDetail.getCostCenterId() != null) {
                        if (y > 0) {
                            errorNoNeedMessageBuilder.append("و ");
                        }
                        errorNoNeedMessageBuilder.append("مركز تكلفة ");
                        saveValidation = false;
                        y++;
                    }

                    if (bankTransactionDetail.getAccountDebitId() != null && bankTransactionDetail.getAccountDebitId().getId() != null) {
                        if (x > 0) {
                            errorMessageBuilder.append("لهذا الحساب");
                        }
                        if (y > 0) {
                            errorNoNeedMessageBuilder.append("لهذا الحساب");
                        }
                    }
                }
                if (errorMessageBuilder.toString().length() > 11) {
                    setShowMessageDetails(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), (errorMessageBuilder.toString())));
                }
                if (errorNoNeedMessageBuilder.toString().length() > 26) {
                    setShowMessageDetails(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), (errorNoNeedMessageBuilder.toString())));
                }
            }
            if (glBankTransaction.getRate() == null || glBankTransaction.getRate().equals(BigDecimal.ZERO)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "المعدل غير صحيح"));
                setShowMessageDetails(true);
                saveValidation = false;
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "validateSave");
        }
    }

    public void notLoseData() {
        try {
            Integer xjg = 0;
            if (glBankTransactionDetailEntitySelected != null) {
                if (glBankTransactionDetailEntitySelected.getGlAccount() == null && glBankTransactionEntity.getOrganizationType() == 2) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل"));
                    return;
                } else {
                    glBankTransactionDetailEntitySelected.setGlAccountBak(glBankTransactionDetailEntitySelected.getGlAccount());
                    glBankTransactionDetailEntitySelected.setGlAdminUnitBak(glBankTransactionDetailEntitySelected.getGlAdminUnit());
                    glBankTransactionDetailEntitySelected.setCostCenterBak(glBankTransactionDetailEntitySelected.getCostCenter());
                }
            }

            forHashasa();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "notLoseData");
        }
    }

    public void forHashasa() {
        try {
            for (GlBankTransactionDetailEntity detailEntity : glBankTransactionDetailEntityList) {
                if (accounts) {
                    if (detailEntity.getGlAccount() == null) {
                        detailEntity.setGlAccount(detailEntity.getGlAccountBak());

                    }

                    if (detailEntity.getGlAdminUnit() == null) {
                        detailEntity.setGlAdminUnit(detailEntity.getGlAdminUnitBak());

                    }

                    if (detailEntity.getCostCenter() == null) {
                        detailEntity.setCostCenter(detailEntity.getCostCenterBak());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "forHashasa");
        }
    }

    public List<Symbol> completeSymbol(String query) {
        try {
            List<Symbol> symbolsList = getSymbolList();
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolConverter(symbolsList);
                return symbolsList;
            }
            List<Symbol> filteredSymbols = new ArrayList<>();
            String nameAr;
            String code;
            Symbol symbolFilter;
            for (int i = 0; i < getSymbolList().size(); i++) {
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
            saveError(e, "SettlementDeedFormMB", "completeSymbol");
            return null;
        }
    }

    @Override
    public void resetDateFrom() {
        try {
            getGlBankTransactionEntity().setDate(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "resetDateFrom");
        }
    }

    @Override
    public void resetDateTo() {
        try {
            setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "resetDateTo");
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        try {
            if (dateType) {
                if (checkFinancailYear(getGlBankTransactionEntity().getDate())) {
                    resetDateFrom();
                }
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "checkDate");
        }

    }

//    public void reportGenerator(String reportType, List<String> jrxmlFileNames, List<T> listBean , String SwapFile)
//{
//
//    JRConcurrentSwapFile swapFile = new JRConcurrentSwapFile(SwapFile, 102400 , 10);
//    JRAbstractLRUVirtualizer virtualizer = new JRSwapFileVirtualizer(1000, swapFile, true);
//    Map<String, JRAbstractLRUVirtualizer> parameters = new HashMap<String, JRAbstractLRUVirtualizer>();
//    parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
//    try
//    {
//        if (reportType.equalsIgnoreCase("PDF"))
//        {
//
//            try
//            {
//                JasperReport jreport1 = JasperCompileManager.compileReport(reportGenerator(ReportGenerator.class.getResourceAsStream(jrxmlFileNames.get(0)));
//                JasperPrint jprint1 = JasperFillManager.fillReport(jreport1, parameters, new JRBeanCollectionDataSource(listBean));
//
//                JasperReport jreport2 = JasperCompileManager.compileReport(ReportGenerator.class.getResourceAsStream(jrxmlFileNames.get(1)));
//                JasperPrint jprint2 = JasperFillManager.fillReport(jreport2, parameters, new JRBeanCollectionDataSource(listBean));
//
//
//                List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
//
//                jprintlist.add(jprint1);
//                jprintlist.add(jprint2);
//
//
//                String fileName="TESTReport.pdf";
//                JRExporter exporter = new JRPdfExporter();
//                exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
//
//                exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, fileName);
//
//                exporter.exportReport();
//
//                }
//                catch(Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            swapFile.dispose();
//
//        }
//    catch(Exception e)
//    {
//     e.printStackTrace();
//    }
//
//}
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            String reportName;
            List<GlBankTransactionDetailEntity> reportList = null;
            if (uri.contains("notesreceivablesdatailForm")) {
                reportList = listDuplara();
                reportName = "notesReceivablesDetial.jasper";
            } else {
                reportList = glBankTransactionDetailEntityList;
                reportName = "settlementdeed.jasper";
            }

            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                //   Integer y = Integer.parseInt(glBankTransactionEntity.getRemark2());
                // Integer y = glBankTransactionEntity.getId();
                Integer glBankTransactionEntityId = glBankTransactionEntity.getHiddenId();
                findSettlementDeedObject(glBankTransactionEntityId, true);
            } else {
                save();
            }
            if (uri.contains("notesreceivablesdatailForm")) {
                reportList = listDuplara();
                reportName = "notesReceivablesDetial.jasper";
            } else {
                reportList = glBankTransactionDetailEntityList;
                reportName = "settlementdeed.jasper";
                if (2 != glBankTransactionEntity.getOrganizationType()) {
                    reportName = "settlementdeedforOrganizationSite.jasper";
                }
            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "exportPDF");
        }
    }

    public void updateAllSummations() {
        try {
            valueSummary = BigDecimal.ZERO;
            localValue = BigDecimal.ZERO;
            for (GlBankTransactionDetailEntity detailEntity : glBankTransactionDetailEntityList) {
//                BigDecimal detailRate = null;
                BigDecimal detailRate = BigDecimal.ONE;
//                if (accounts) {
//                    detailRate = updateRate(detailEntity.getGlAccount() != null ? detailEntity.getGlAccount().getCurrencyId() : null, glBankTransactionEntity.getDate());
//                } else if (organizationSiteView) {
//                    detailRate = updateRate(detailEntity.getInvOrganizationSite() != null ? detailEntity.getInvOrganizationSite().getCurrencyId() : null, glBankTransactionEntity.getDate());
//                }
                detailEntity.setValueLocal((detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue()).multiply(detailRate == null ? BigDecimal.ONE : detailRate));
//                detailEntity.setValueLocal(detailEntity.getValueLocal() != null ? detailEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
                detailEntity.setValueLocal(detailEntity.getValue() != null ? detailEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
                valueSummary = valueSummary.add(detailEntity.getValue() != null ? detailEntity.getValue() : BigDecimal.ZERO);
                localValue = localValue.add(detailEntity.getValue() != null ? detailEntity.getValue() : BigDecimal.ZERO);
//                localValue = localValue.add(detailEntity.getValueLocal() != null ? detailEntity.getValueLocal() : BigDecimal.ZERO);
                getGlBankTransactionEntity().setValue(localValue);
            }
            forHashasa();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "updateAllSummations");
        }
    }

    private List<GlBankTransactionDetailEntity> listDuplara() throws CloneNotSupportedException {
        List<GlBankTransactionDetailEntity> list = new ArrayList<>();
        for (GlBankTransactionDetailEntity bankTransactionDetailEntity : glBankTransactionDetailEntityList) {
            GlBankTransactionDetailEntity bean = (GlBankTransactionDetailEntity) bankTransactionDetailEntity.clone();
            if (accountsSystemSettings.getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlAccount().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlAccount().getAccNumber());
            } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlItems().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlItems().getSerial());
            }
            bean.setType(0);
            bean.setTotal(valueSummary);
            list.add(bean);
        }

        for (GlBankTransactionDetailEntity bankTransactionDetailEntity : glBankTransactionDetailEntityList) {
            GlBankTransactionDetailEntity bean = (GlBankTransactionDetailEntity) bankTransactionDetailEntity.clone();
            if (accountsSystemSettings.getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlAccount().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlAccount().getAccNumber());
            } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlItems().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlItems().getSerial());
            }
            bean.setType(1);
            bean.setTotal(valueSummary);
            list.add(bean);
        }
        return list;
    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            hashMap.put("amountText", userDDs.get("AMOUNT"));
            hashMap.put("dateText", userDDs.get("DATE"));
            hashMap.put("remarkText", "البيان");
            hashMap.put("remarkText2", ("استلمنا من"));
            hashMap.put("payAccountText", "يصرف من");
            hashMap.put("tellerText", "امين صندوق");
            hashMap.put("accountsText", "المستلم");
            hashMap.put("confirmationText", "اعتماد");
            hashMap.put("valueFromText", "وذلك قيمة");
            hashMap.put("documentNumberText", "رقم السند");
            hashMap.put("chequeDateText", "تاريخ الشيك");
            hashMap.put("chequeDateValue", glBankTransactionEntityMapper.getChequeDate() != null ? glBankTransactionEntityMapper.getChequeDate() : null);
            hashMap.put("deservedDateText", "تاريخ الاستحقاق");
            hashMap.put("deservedDateValue", glBankTransactionEntityMapper.getChequeDueDate() != null ? glBankTransactionEntityMapper.getChequeDueDate() : null);
            //  hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("dateValue", glBankTransactionEntityMapper.getDate());
            hashMap.put("documentNumberValue", glBankTransactionEntityMapper.getSerial());
            hashMap.put("payAccountValue", glBankTransactionEntityMapper.getGlBank() != null ? glBankTransactionEntityMapper.getGlBank().getName() : null);
            hashMap.put("amountValue", glBankTransactionEntityMapper.getValue());
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(glBankTransactionEntityMapper.getValue().toString()), "ريـال", "هلله"));
            hashMap.put("valueFromValue", remark1);
            hashMap.put("explainationValue", remark2);
            hashMap.put("accountManager", "مدير الحسابات");

            hashMap.put("rateText", "المعدل");
            hashMap.put("rateValue", glBankTransactionEntityMapper.getRate());
            hashMap.put("amountInRateText", userDDs.get("TOTAL"));
//            hashMap.put("amountInRateValue", glBankTransactionEntityMapper.getValueLocal());
            hashMap.put("amountInRateValue", glBankTransactionEntityMapper.getValue());

            hashMap.put("sumValue", valueSummary);
            hashMap.put("localValue", localValue);

            hashMap.put("serialText", "م");
            hashMap.put("Receipt", "لا  يعتد بهذا الايصال الا اذا كان مختوم بخاتم النقابة");
            hashMap.put("stampText", "الختم");
            hashMap.put("tellerText", "امين الخزينه");
            hashMap.put("responsibleText", "مسئول الخدمة");
            hashMap.put("responsibleValue", glBankTransactionEntityMapper.getCreatedNameParent());
            hashMap.put("listSize", glBankTransactionDetailEntityList.size());
            hashMap.put("CreatedBy", glBankTransactionEntityMapper.getCreatedByName());
            hashMap.put("organizationSiteText", "اسم جهة التعامل");
            hashMap.put("organizationsiteNameText", "الاسم");

            // GlAccount recipientaccount = new GlAccount();
            if (glBankTransactionDetailEntityList != null && !glBankTransactionDetailEntityList.isEmpty()) {
                /*for (int i = 0; i < glBankTransactionDetailEntityList.size(); i++) {
                 recipientaccount = glAccountService.findGlAccount(glBankTransactionDetailEntityList.get(i).getAccountDebitId());
                 if (recipientaccount != null) {
                 glBankTransactionDetailEntityList.get(i).setAccountName(recipientaccount.getName());
                 glBankTransactionDetailEntityList.get(i).setAccountNumber(recipientaccount.getAccNumber());
                 }
                 }*/

                if (accountsSystemSettings.getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                    hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
                    hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
                    for (GlBankTransactionDetailEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                        if (transactionDetailEntity.getGlAccount() != null && transactionDetailEntity.getGlAccount().getId() != null) {
                            transactionDetailEntity.setAccountName(transactionDetailEntity.getGlAccount().getName());
                            transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlAccount().getAccNumber());
                        }
                    }
                } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                    hashMap.put("accountNameText", "اسم البند");
                    hashMap.put("accountNumberText", "كود البند");
                    for (GlBankTransactionDetailEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                        if (transactionDetailEntity.getGlItems() != null && transactionDetailEntity.getGlItems().getId() != null) {
                            transactionDetailEntity.setAccountName(transactionDetailEntity.getGlItems().getName());
                            transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlItems().getSerial());
                        }
                    }
                }

            }

            if (glBankTransactionEntityMapper.getPaymentType() != null) {
                switch (glBankTransactionEntityMapper.getPaymentType()) {
                    case 0:
                        hashMap.put("reportNameText", "سند صرف نقدي ");
                        break;
                    case 1:
                        hashMap.put("reportNameText", "سند صرف شيك ");
                        break;
                    case 2:
                        hashMap.put("reportNameText", "سند صرف شيك خطي ");
                        break;
                }
            }
            hashMap.put("copyWordIcon", getUserData().getImagePath() + "copyWordIcon.png");
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            return hashMap;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "prepareReport");
            return null;
        }
    }

    public void loadThePageAgain() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../notesreceivablespre/notesreceivablesdatailFormPre.xhtml");
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "loadThePageAgain");
        }

    }

    public List<CostCenter> completeCostCenter(String query) {
        try {
            List<CostCenter> costCenterList = getCostCenterList();
            if (query == null || query.trim().equals("")) {
                costCenterConverter = new CostCenterConverter(costCenterList);
                return costCenterList;
            }
            List<CostCenter> filteredCostCenters = new ArrayList<>();

            String nameAr;
            Integer code;
            CostCenter costCenter;
            for (int i = 0; i < getCostCenterList().size(); i++) {
                costCenter = costCenterList.get(i);
                nameAr = costCenter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenter)) {
                        filteredCostCenters.add(costCenter);
                    }
                }

                code = costCenter.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredCostCenters.contains(costCenter)) {
                        filteredCostCenters.add(costCenter);
                    }
                }
            }

            costCenterConverter = new CostCenterConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeCostCenter");
            return null;
        }
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        try {
            List<GlAdminUnit> glAdminUnits = adminUnitList;
            if (query == null || query.trim().equals("")) {
                glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
                return glAdminUnits;
            }
            List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAdminUnit glAdminUnit;
            for (int i = 0; i < adminUnitList.size(); i++) {
                glAdminUnit = glAdminUnits.get(i);
                nameAr = glAdminUnit.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }

                code = glAdminUnit.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }
            }
            glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
            return filteredGlAdminUnitList;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeAdminUnit");
            return null;
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = getGlAccountList();//45
            if (query == null || query.trim().equals("")) {
                glAccountConverter = new GlaccountConverter(glaccounts);
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

            glAccountConverter = new GlaccountConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeGlAccount");
            return null;
        }
    }

    public List<InvOrganizationSite> completeSuppllier(String query) {
        try {
            List<InvOrganizationSite> supplierList = organizationList;
            if (query == null || query.trim().equals("")) {

                supplierConvertor = new InvOrganizationSiteConverter(supplierList);
                return supplierList;
            }
            List<InvOrganizationSite> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite supplierFilter;
            for (int i = 0; i < organizationList.size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = supplierFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = supplierFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            supplierConvertor = new InvOrganizationSiteConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeSuppllier");
            return null;
        }
    }

    public List<Symbol> completeGlItems(String query) {
        try {
            List<Symbol> glaccounts = getGlItemsList();
            if (query == null || query.trim().equals("")) {
                glItemConverter = new SymbolConverter(glaccounts);
                return glaccounts;
            }
            List<Symbol> filteredGlaccounts = new ArrayList<>();
            String nameAr;
            Symbol glaccount;
            for (int i = 0; i < getGlAccountList().size(); i++) {
                glaccount = glaccounts.get(i);
                nameAr = glaccount.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }
            }

            glItemConverter = new SymbolConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeGlItems");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            exit("../settlementdeed/settlementdeedList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "exit");
            return null;
        }
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    public GlBankTransaction getGlBankTransaction() {
        return glBankTransaction;
    }

    public void setGlBankTransaction(GlBankTransaction glBankTransaction) {
        this.glBankTransaction = glBankTransaction;
    }

    public List<GlBankTransactionDetail> getGlBankTransactionDetailsList() {
        return glBankTransactionDetailsList;
    }

    public void setGlBankTransactionDetailsList(List<GlBankTransactionDetail> glBankTransactionDetailsList) {
        this.glBankTransactionDetailsList = glBankTransactionDetailsList;
    }

    public List<GlBankTransactionDetail> getGlBankTransactionDetailsUpdatedList() {
        return glBankTransactionDetailsUpdatedList;
    }

    public void setGlBankTransactionDetailsUpdatedList(List<GlBankTransactionDetail> glBankTransactionDetailsUpdatedList) {
        this.glBankTransactionDetailsUpdatedList = glBankTransactionDetailsUpdatedList;
    }

    public List<GlBankTransactionDetail> getGlBankTransactionDetailsDeletedList() {
        return glBankTransactionDetailsDeletedList;
    }

    public void setGlBankTransactionDetailsDeletedList(List<GlBankTransactionDetail> glBankTransactionDetailsDeletedList) {
        this.glBankTransactionDetailsDeletedList = glBankTransactionDetailsDeletedList;
    }

    public GlBankTransactionEntity getGlBankTransactionEntity() {
        return glBankTransactionEntity;
    }

    public void setGlBankTransactionEntity(GlBankTransactionEntity glBankTransactionEntity) {
        this.glBankTransactionEntity = glBankTransactionEntity;
    }

    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntity() {
        return glBankTransactionDetailEntity;
    }

    public void setGlBankTransactionDetailEntity(GlBankTransactionDetailEntity glBankTransactionDetailEntity) {
        this.glBankTransactionDetailEntity = glBankTransactionDetailEntity;
    }

    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntitySelected() {
        return glBankTransactionDetailEntitySelected;
    }

    public void setGlBankTransactionDetailEntitySelected(GlBankTransactionDetailEntity glBankTransactionDetailEntitySelected) {
        this.glBankTransactionDetailEntitySelected = glBankTransactionDetailEntitySelected;
    }

    public List<GlBankTransactionDetailEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    public List<GlBankTransactionDetailEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<GlBankTransactionDetailEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }

    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    public List<GlAdminUnit> getAdminUnitList() {
        return adminUnitList;
    }

    public void setAdminUnitList(List<GlAdminUnit> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }

    public BigDecimal getLocalValue() {
        return localValue;
    }

    public void setLocalValue(BigDecimal localValue) {
        this.localValue = localValue;
    }

    /**
     * @return the organizationList
     */
    public List<InvOrganizationSite> getOrganizationList() {
        return organizationList;
    }

    /**
     * @param organizationList the organizationList to set
     */
    public void setOrganizationList(List<InvOrganizationSite> organizationList) {
        this.organizationList = organizationList;
    }

    /**
     * @return the valueSummary
     */
    public BigDecimal getValueSummary() {
        return valueSummary;
    }

    /**
     * @param valueSummary the valueSummary to set
     */
    public void setValueSummary(BigDecimal valueSummary) {
        this.valueSummary = valueSummary;
    }

    /**
     * @return the glBankTransactionExist
     */
    public GlBankTransaction getGlBankTransactionExist() {
        return glBankTransactionExist;
    }

    /**
     * @param glBankTransactionExist the glBankTransactionExist to set
     */
    public void setGlBankTransactionExist(GlBankTransaction glBankTransactionExist) {
        this.glBankTransactionExist = glBankTransactionExist;
    }

    /**
     * @return the bankConverter
     */
    public GlBankConverter getBankConverter() {
        return bankConverter;
    }

    /**
     * @param bankConverter the bankConverter to set
     */
    public void setBankConverter(GlBankConverter bankConverter) {
        this.bankConverter = bankConverter;
    }

    /**
     * @return the invOrganizationSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrganizationSiteConverter() {
        return invOrganizationSiteConverter;
    }

    /**
     * @param invOrganizationSiteConverter the invOrganizationSiteConverter to
     * set
     */
    public void setInvOrganizationSiteConverter(InvOrganizationSiteConverter invOrganizationSiteConverter) {
        this.invOrganizationSiteConverter = invOrganizationSiteConverter;
    }

    public Map<Integer, GlBankTransactionDetail> getGlBankTransactionDetailsMap() {
        return glBankTransactionDetailsMap;
    }

    public void setGlBankTransactionDetailsMap(Map<Integer, GlBankTransactionDetail> glBankTransactionDetailsMap) {
        this.glBankTransactionDetailsMap = glBankTransactionDetailsMap;
    }

    public GlAccount getAccount() {
        return account;
    }

    public void setAccount(GlAccount account) {
        this.account = account;
    }

    public boolean isAccounts() {
        return accounts;
    }

    public void setAccounts(boolean accounts) {
        this.accounts = accounts;
    }

    public GlaccountConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlaccountConverter glAccountConverter) {
        this.glAccountConverter = glAccountConverter;
    }

    /**
     * @return the organizationSiteView
     */
    public boolean isOrganizationSiteView() {
        return organizationSiteView;
    }

    /**
     * @param organizationSiteView the organizationSiteView to set
     */
    public void setOrganizationSiteView(boolean organizationSiteView) {
        this.organizationSiteView = organizationSiteView;
    }

    public GlBankTransactionEntity getGlBankTransactionEntityMapper() {
        return glBankTransactionEntityMapper;
    }

    public void setGlBankTransactionEntityMapper(GlBankTransactionEntity glBankTransactionEntityMapper) {
        this.glBankTransactionEntityMapper = glBankTransactionEntityMapper;
    }

    public Boolean getCheckView() {
        return checkView;
    }

    public void setCheckView(Boolean checkView) {
        this.checkView = checkView;
    }

    public Boolean getLineCheckView() {
        return lineCheckView;
    }

    public void setLineCheckView(Boolean lineCheckView) {
        this.lineCheckView = lineCheckView;
    }

    public Boolean getPaymentDropDownView() {
        return paymentDropDownView;
    }

    public void setPaymentDropDownView(Boolean paymentDropDownView) {
        this.paymentDropDownView = paymentDropDownView;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    /**
     * @return the printButton
     */
    public Boolean getPrintButton() {
        return printButton;
    }

    /**
     * @param printButton the printButton to set
     */
    public void setPrintButton(Boolean printButton) {
        this.printButton = printButton;
    }

    /**
     * @return the saveValidation
     */
    public boolean isSaveValidation() {
        return saveValidation;
    }

    /**
     * @param saveValidation the saveValidation to set
     */
    public void setSaveValidation(boolean saveValidation) {
        this.saveValidation = saveValidation;
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
     * @return the OrganizationTypeAfterDialogConfirmation
     */
    public Integer getOrganizationTypeAfterDialogConfirmation() {
        return OrganizationTypeAfterDialogConfirmation;
    }

    /**
     * @param OrganizationTypeAfterDialogConfirmation the
     * OrganizationTypeAfterDialogConfirmation to set
     */
    public void setOrganizationTypeAfterDialogConfirmation(Integer OrganizationTypeAfterDialogConfirmation) {
        this.OrganizationTypeAfterDialogConfirmation = OrganizationTypeAfterDialogConfirmation;
    }

    /**
     * @return the remark1
     */
    public String getRemark1() {
        return remark1;
    }

    /**
     * @param remark1 the remark1 to set
     */
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    /**
     * @return the remark2
     */
    public String getRemark2() {
        return remark2;
    }

    /**
     * @param remark2 the remark2 to set
     */
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    /**
     * @return the organizeType
     */
    public Integer getOrganizeType() {
        return organizeType;
    }

    /**
     * @param organizeType the organizeType to set
     */
    public void setOrganizeType(Integer organizeType) {
        this.organizeType = organizeType;
    }

    /**
     * @return the costCenterConverter
     */
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the glAdminUnitConverter
     */
    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    /**
     * @param glAdminUnitConverter the glAdminUnitConverter to set
     */
    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    /**
     * @return the glAccountSelected
     */
    public GlAccount getGlAccountSelected() {
        return glAccountSelected;
    }

    /**
     * @param glAccountSelected the glAccountSelected to set
     */
    public void setGlAccountSelected(GlAccount glAccountSelected) {
        this.glAccountSelected = glAccountSelected;
    }

    /**
     * @return the costCenterSelected
     */
    public CostCenter getCostCenterSelected() {
        return costCenterSelected;
    }

    /**
     * @param costCenterSelected the costCenterSelected to set
     */
    public void setCostCenterSelected(CostCenter costCenterSelected) {
        this.costCenterSelected = costCenterSelected;
    }

    /**
     * @return the glAdminUnitSelected
     */
    public GlAdminUnit getGlAdminUnitSelected() {
        return glAdminUnitSelected;
    }

    /**
     * @param glAdminUnitSelected the glAdminUnitSelected to set
     */
    public void setGlAdminUnitSelected(GlAdminUnit glAdminUnitSelected) {
        this.glAdminUnitSelected = glAdminUnitSelected;
    }

    /**
     * @return the glBankTransactionDetailEntityUnSelected
     */
    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntityUnSelected() {
        return glBankTransactionDetailEntityUnSelected;
    }

    /**
     * @param glBankTransactionDetailEntityUnSelected the
     * glBankTransactionDetailEntityUnSelected to set
     */
    public void setGlBankTransactionDetailEntityUnSelected(GlBankTransactionDetailEntity glBankTransactionDetailEntityUnSelected) {
        this.glBankTransactionDetailEntityUnSelected = glBankTransactionDetailEntityUnSelected;
    }

    /**
     * @return the supplierConvertor
     */
    public InvOrganizationSiteConverter getSupplierConvertor() {
        return supplierConvertor;
    }

    /**
     * @param supplierConvertor the supplierConvertor to set
     */
    public void setSupplierConvertor(InvOrganizationSiteConverter supplierConvertor) {
        this.supplierConvertor = supplierConvertor;
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
     * @return the allowAccount
     */
    public Boolean getAllowAccount() {
        return allowAccount;
    }

    /**
     * @param allowAccount the allowAccount to set
     */
    public void setAllowAccount(Boolean allowAccount) {
        this.allowAccount = allowAccount;
    }

    /**
     * @return the accountGLItems
     */
    public GlAccount getAccountGLItems() {
        return accountGLItems;
    }

    /**
     * @param accountGLItems the accountGLItems to set
     */
    public void setAccountGLItems(GlAccount accountGLItems) {
        this.accountGLItems = accountGLItems;
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

    /**
     * @return the glItemConverter
     */
    public SymbolConverter getGlItemConverter() {
        return glItemConverter;
    }

    /**
     * @param glItemConverter the glItemConverter to set
     */
    public void setGlItemConverter(SymbolConverter glItemConverter) {
        this.glItemConverter = glItemConverter;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the glBankTransactionSelectionPre
     */
    public GlBankTransaction getGlBankTransactionSelectionPre() {
        return glBankTransactionSelectionPre;
    }

    /**
     * @param glBankTransactionSelectionPre the glBankTransactionSelectionPre to
     * set
     */
    public void setGlBankTransactionSelectionPre(GlBankTransaction glBankTransactionSelectionPre) {
        this.glBankTransactionSelectionPre = glBankTransactionSelectionPre;
    }

    /**
     * @return the glBankTransactionListPre
     */
    public List<GlBankTransaction> getGlBankTransactionListPre() {
        return glBankTransactionListPre;
    }

    /**
     * @param glBankTransactionListPre the glBankTransactionListPre to set
     */
    public void setGlBankTransactionListPre(List<GlBankTransaction> glBankTransactionListPre) {
        this.glBankTransactionListPre = glBankTransactionListPre;
    }
}
