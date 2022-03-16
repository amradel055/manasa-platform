/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.settlementdeed;

import com.toby.bean.GlBankTransactionDetailFromEntity;
import com.toby.bean.JournalDocumentAndSettelment;
import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GeneraljournalServiceDTO;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionInvoiceViewService;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.CostCenterDTOConverter;
import com.toby.converter.GlAccountDTOConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlBankConverter;
import com.toby.converter.SymbolConverter;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.CostCenter;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.Symbol;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.generaljournal.GeneralJournalBean;
import com.toby.toby.AutoComplete;
import com.toby.toby.GlBankBasicDataForm;
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
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "settlementDeedVoucherFormMB")
@ViewScoped
public class SettlementDeedVoucherFormMB extends GlBankBasicDataForm {

    private Boolean allowAccount;
    private GlAccount accountGLItems;
    private List<Symbol> glItemsList;
    private SymbolConverter glItemConverter;

    private Integer index = 0;

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
    private GlBankTransactionDetailFromEntity glBankTransactionDetailEntity;
    private GlBankTransactionDetailFromEntity glBankTransactionDetailEntitySelected;
    private GlBankTransactionDetailFromEntity glBankTransactionDetailEntityUnSelected;

    private List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList;

    private List<GlBankTransactionDetailFromEntity> rowsDeleted;

    private boolean organizationSiteView = false;
    private boolean accounts = false;
    private boolean saveValidation = true;
    // Lists
    private List<GlAccountDTO> glAccountList;
    private List<CostCenterDTO> costCenterList;
    private List<CostCenterDTO> costCenterHeadList;
    private List<GlBank> glBankList;
    private List<GlAdminUnit> adminUnitList;

    private Map<Integer, GlBankTransactionDetail> glBankTransactionDetailsMap = new HashMap();

    private GlBankConverter bankConverter;

    private GlAccountDTOConverter glAccountConverter;

    private Symbol symbol;
    private List<Symbol> symbolList;
    private SymbolConverter symbolConverter;
    private Integer OrganizationTypeAfterDialogConfirmation;

    private String remark1;
    private String remark2;

    private Integer organizeType;

    private CostCenterDTOConverter costCenterConverter;
    private CostCenterDTOConverter costCenterHeadConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private GlAccount glAccountSelected;
    private CostCenterDTO costCenterSelected;
    private GlAdminUnit glAdminUnitSelected;

    private StringBuilder errorMessageBuilder;
    private StringBuilder errorNoNeedMessageBuilder;

    private AccountsSystemSettings accountsSystemSettings;

    private List<GlAccountDTO> glRunningAccountDTOList;
    private GlAccountDTOConverter runningAccountDTOConverter;

    private Boolean isDebit;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;

    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;

    @EJB
    private GlAccountService glAccountService;

    @EJB
    private CostCenterService costCenterService;

    @EJB
    private TobyUserBankService tobyUserBankService;

    @EJB
    SymbolService symbolService;

    @EJB
    private GlBankTransactionInvoiceViewService bankTransactionInvoiceViewService;

    @EJB
    private GeneraljournalServiceDTO generaljournalServiceDTO;

    @EJB
    private GenericDAO dao;

    @Override
    @PostConstruct
    public void init() {
        try {
            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());
            System.err.println("---> 1 " + new Date());
            glBankList = new ArrayList<>();
            adminUnitList = new ArrayList<>();

            rowsDeleted = new ArrayList<>();
            symbolList = new ArrayList<>();
            System.err.println("---> 2 " + new Date());
            load();
            System.err.println("---> 3 " + new Date());
            paymentDropDownView = true;

            organizationTypeUpdated = 0; // set to 0 during ad

            setAllowAccount(Boolean.TRUE);
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));
            if (getAccountsSystemSettings() != null && getAccountsSystemSettings().getWorkWithAccounts() != null && !getAccountsSystemSettings().getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                setAllowAccount(Boolean.FALSE);
            }
            System.err.println("---> 4 " + new Date());
            glBankTransaction.setCompanyId(getUserData().getCompany());
            glBankTransaction.setBranchId(getUserData().getUserBranch());
            glBankTransaction.setTransactionType(0);

            symbolList = symbolService.getAllDocumentsByCompanyId(getUserData().getCompany().getId());
            if (symbolList != null && !symbolList.isEmpty()) {
                symbolConverter = new SymbolConverter(symbolList);
            }
            System.err.println("---> 5 " + new Date());
            fillLists(); // here 4 second
            System.err.println("---> 6 " + new Date());
            viewPaymentFields(); // here 3 second System.err.println("---> 60 " + new Date());
            updateTransactionRate();
            System.err.println("---> 7 " + new Date());
            if (glBankList == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                try {
                    fc.getExternalContext().redirect("../base/financailyear.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(GeneralJournalBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.err.println("---> 8 " + new Date());
            checkVisability();
            if (glBankTransactionEntity.getId() == null) {
                printButton = Boolean.TRUE;
            } else {
                printButton = Boolean.FALSE;
            }
            System.err.println("---> 9 " + new Date());
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "init");
        }
    }

    public void reset() {
        getGlBankTransactionEntity().setId(null);
        getGlBankTransactionEntity().setSerial(null);
        setGlBankTransactionDetailEntityList(new ArrayList<>());
        valueSummary = BigDecimal.ZERO;
        localValue = BigDecimal.ZERO;
        printButton = Boolean.TRUE;
        paymentTypeName = new String();
        paymentDropDownView = Boolean.TRUE;
    }

    public void convertDateFrom() {
        getGlBankTransactionEntity().setDate(findDateFromHijriFrom());
    }

    @Override
    public void load() {
        try {
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionEntity.setOrganizationType(2);
            glBankTransactionEntity.setPaymentType(0);
            glBankTransactionEntity.setSerial(null);
            glBankTransactionEntity.setDate(new Date());

            glBankTransaction = new GlBankTransaction();
            glBankTransactionDetailEntity = new GlBankTransactionDetailFromEntity();
            glBankTransactionDetailsList = new ArrayList<>();
            glBankTransactionDetailsUpdatedList = new ArrayList<>();
            glBankTransactionDetailEntityList = new ArrayList<>();
            glBankTransactionDetailsDeletedList = new ArrayList<>();
            glBankTransactionDetailEntitySelected = new GlBankTransactionDetailFromEntity();
            valueSummary = BigDecimal.ZERO;
            localValue = BigDecimal.ZERO;
            printButton = Boolean.TRUE;
            paymentTypeName = new String();
            paymentDropDownView = Boolean.TRUE;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "load");
        }
    }

    public List<GlAccountDTO> completeRunningGlAccount(String query) {
        getGlRunningAccountDTOList();
        return AutoComplete.completeGlAccount(query, getGlRunningAccountDTOList(), getRunningAccountDTOConverter());
    }

    private void fillLists() {
        try {

            if (glBankTransactionEntity.getOrganizationType() != null && glBankTransactionEntity.getOrganizationType() == 2) {
                getGlAccountList();
                getGlRunningAccountDTOList();
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

    public void checkVisability() {
        try {

            viewFooterColms();
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "checkVisability");
        }
    }

    public void viewFooterColms() {
        try {
            if (glBankTransactionEntity.getOrganizationType() == 2) {
                organizationSiteView = false;
                accounts = true;
                glBankTransactionEntity.setGlBank(null);
            } else {
                accounts = false;
                organizationSiteView = true;
                glBankTransactionEntity.setCostAccount(null);
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

            glBankTransactionDetailEntitySelected = new GlBankTransactionDetailFromEntity();
            glBankTransactionDetailEntityList = new ArrayList<>();
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

    public void updateTransactionRate() {
        try {
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
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "calculateLocalValue");
        }
    }

    public void setNewValueOfAmount(GlBankTransactionDetailFromEntity bankTransactionDetailEntity) {
        glBankTransactionEntity.setValue(bankTransactionDetailEntity.getValue());
        changeRate();
    }

    public void updatetransactionDetailLocalValue() {
        try {
            localValue = BigDecimal.ZERO;
            for (GlBankTransactionDetailFromEntity detailEntity : glBankTransactionDetailEntityList) {
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
                GlBankTransactionDetailFromEntity bankTransactionDetailEntity = glBankTransactionDetailEntityList.get(glBankTransactionDetailEntityList.size() - 1);
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
            for (GlBankTransactionDetailFromEntity detailEntity : glBankTransactionDetailEntityList) {
                detailEntity.setMarkEdit(Boolean.FALSE);
            }

            GlBankTransactionDetailFromEntity glBankTransactionDetailsEntityNew = new GlBankTransactionDetailFromEntity();
            glBankTransactionDetailsEntityNew.setMarkEdit(Boolean.TRUE);
            glBankTransactionDetailsEntityNew.setIndex(++index);
            glBankTransactionDetailsEntityNew.setRemarks(glBankTransactionEntity.getRemark());
            glBankTransactionDetailsEntityNew.setGlAccountCredit(getGlBankTransactionEntity().getCostAccount());
            glBankTransactionDetailsEntityNew.setGlAccountBakCredit(getGlBankTransactionEntity().getCostAccount());
            glBankTransactionDetailsEntityNew.setCostCenterCredit(getGlBankTransactionEntity().getCostCenterDTO());
            glBankTransactionDetailsEntityNew.setCostCenterBakCredit(getGlBankTransactionEntity().getCostCenterDTO());

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

            localValue = (localValue == null ? BigDecimal.ZERO : localValue).subtract(glBankTransactionDetailEntitySelected.getValue() != null ? glBankTransactionDetailEntitySelected.getValue() : BigDecimal.ZERO);

            glBankTransactionDetailEntityList.remove(glBankTransactionDetailEntitySelected);
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "deleteGlBankTransactionDetail");
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
                        if (!getUri().contains("notesreceivablesdatailFormPre")) {
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
                    glBankTransaction.setBankCommetion(glBankTransactionEntity.getBankCommition());
                    glBankTransaction.setIsDebit(getIsDebit());
                    if (glBankTransactionEntity.getCostAccount() != null) {
                        GlAccount glAccount = new GlAccount();
                        glAccount.setId(glBankTransactionEntity.getCostAccount().getId());
//                        if (glBankTransactionEntity.getCostAccount().getCostCenterId() != null) {
//                            CostCenter costCenterId = new CostCenter();
//                            costCenterId.setId(glBankTransactionEntity.getCostAccount().getCostCenterId());
//                            glAccount.setCostCenterId(costCenterId);
//                        }
                        glBankTransaction.setGlAccountId(glAccount);
                    }

                    if (glBankTransactionEntity.getCostCenterDTO() != null) {
                        CostCenter costCenter = new CostCenter();
                        costCenter.setId(glBankTransactionEntity.getCostCenterDTO().getId());
                        glBankTransaction.setCostCenter(costCenter);
                    }

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
                    glBankTransaction.setGlYear(getGlYearSelection().getYear() != null ? getGlYearSelection().getId() : null);

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
                    for (GlBankTransactionDetailFromEntity detailsEntity : glBankTransactionDetailEntityList) {

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

                            if (glBankTransactionEntity.getCostAccount() != null) {
                                GlAccount glAccount = new GlAccount();
                                glAccount.setId(glBankTransactionEntity.getCostAccount().getId());
                                bankTransactionDetail.setAccountCreditId(glAccount);
                            } else {
                                if (glBankTransaction.getGlBankId() != null && glBankTransaction.getGlBankId().getAccountId() != null) {
                                    bankTransactionDetail.setAccountCreditId(glBankTransaction.getGlBankId().getAccountId());
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "الخزنة غير مربوطة بحساب او لم يتم اختيار الخزنة"));
                                    setShowMessageDetails(true);
                                    return "";
                                }
                            }
                        }
                        bankTransactionDetail.setAdminUnitId(detailsEntity.getGlAdminUnit() != null ? detailsEntity.getGlAdminUnit() : null);

                        bankTransactionDetail.setCostCenterId(mapCostCentertToEntity(detailsEntity.getCostCenter() != null ? detailsEntity.getCostCenter() : (detailsEntity.getCostCenterBak() != null ? detailsEntity.getCostCenterBak() : null)));
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
                        bankTransactionDetail.setBankCommition(detailsEntity.getBankCommition());
                        bankTransactionDetail.setGlAccountCredit(detailsEntity.getGlAccountCredit() == null ? detailsEntity.getGlAccountBakCredit() : detailsEntity.getGlAccountCredit());
                        bankTransactionDetail.setCostCenterCredit(detailsEntity.getCostCenterCredit() == null ? detailsEntity.getCostCenterBakCredit() : detailsEntity.getCostCenterCredit());

                        bankTransactionDetail.setSymbolId(detailsEntity.getGlItems());
                        bankTransactionDetail.setGlBankTransactionId(glBankTransaction);
                        glBankTransactionDetailsUpdatedList.add(bankTransactionDetail);
                        validateSave(glBankTransaction, bankTransactionDetail);
                    }

                    if (saveValidation != false) {
                        GeneralJournal generalJournal = new GeneralJournal();
                        if (getUri().contains("settlementdeedVoucher")) {
                            generalJournal.setId(glBankTransactionDetailsService.tarhilSettlementVoucher(glBankTransactionDetailsUpdatedList, glBankTransaction, getUserData().getUser(), 3, false));
                        } else if (getUri().contains("settlementdeedVoucherNOSumDetail")) {
                            generalJournal.setId(glBankTransactionDetailsService.tarhilSettlementVoucherNOSumDetail(glBankTransactionDetailsUpdatedList, glBankTransaction, getUserData().getUser(), 3, false));
                        }
                        generalJournal = dao.findEntityById(GeneralJournal.class, generalJournal.getId());
                        glBankTransactionEntity.setSerial(generalJournal.getSerial());
                        glBankTransaction.setId(generalJournal.getId());
                        glBankTransaction.setGeneralJournalId(generalJournal);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الخفظ بنجاح"));
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

    private void setAccountCreditId(Integer organizationType, GlBankTransactionDetailFromEntity detailsEntity, GlBankTransactionDetail bankTransactionDetail) {

        if (organizationType == 2) {
            if (allowAccount) {
                bankTransactionDetail.setAccountCreditId(mapAccountToEntity(detailsEntity.getGlAccount()));
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

    private void setAccountDebitId(Integer organizationType, GlBankTransactionDetailFromEntity detailsEntity, GlBankTransactionDetail bankTransactionDetail) {
        if (organizationType == 2) {
            if (allowAccount) {
                //GlAccount accountDebitId = new GlAccount();
                // accountDebitId.setId(detailsEntity.getAccountDebitId());
                bankTransactionDetail.setAccountDebitId(mapAccountToEntity(detailsEntity.getGlAccount()));
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

            if (getUri().contains("settlementdeedVoucherForm")) {
                if (bankTransactionDetail.getGlAccountCredit() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار حساب دائن"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }

                if (bankTransactionDetail.getCostCenterCredit() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار مركز تكلفة للحساب الدائن"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
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
            for (GlBankTransactionDetailFromEntity detailEntity : glBankTransactionDetailEntityList) {
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
            List<GlBankTransactionDetailFromEntity> reportList = null;
//            if (getUri().contains("notesreceivablesdatailForm")) {
//                reportList = listDuplara();
//                reportName = "notesReceivablesDetial.jasper";
//            } else {
//                reportList = glBankTransactionDetailEntityList;
//                reportName = "settlementdeed.jasper";
//            }

//            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
//                //   Integer y = Integer.parseInt(glBankTransactionEntity.getRemark2());
//                // Integer y = glBankTransactionEntity.getId();
//                Integer glBankTransactionEntityId = glBankTransactionEntity.getHiddenId();
//                findSettlementDeedObject(glBankTransactionEntityId, true);
//            } else {
//                save();
//            }
//            if (getUri().contains("notesreceivablesdatailForm")) {
//                reportList = listDuplara();
//                reportName = "notesReceivablesDetial.jasper";
//            } else {
            reportList = glBankTransactionDetailEntityList;
            reportName = "settlementdeed.jasper";
            if (2 != glBankTransactionEntity.getOrganizationType()) {
                reportName = "settlementdeedforOrganizationSite.jasper";
            }

            HashMap prepareReport = prepareReport();

            if (reportList != null && !reportList.isEmpty()) {
                fillReport(prepareReport, getUserData().getReportPath() + reportName, reportList, "pdf");
            }
//            }
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "exportPDF");
        }
    }

    public void updateAllSummations() {
        try {
            valueSummary = BigDecimal.ZERO;
            localValue = BigDecimal.ZERO;
            for (GlBankTransactionDetailFromEntity detailEntity : glBankTransactionDetailEntityList) {
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

    private List<GlBankTransactionDetailFromEntity> listDuplara() throws CloneNotSupportedException {
        List<GlBankTransactionDetailFromEntity> list = new ArrayList<>();
        for (GlBankTransactionDetailFromEntity bankTransactionDetailEntity : glBankTransactionDetailEntityList) {
            GlBankTransactionDetailFromEntity bean = (GlBankTransactionDetailFromEntity) bankTransactionDetailEntity.clone();
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

        for (GlBankTransactionDetailFromEntity bankTransactionDetailEntity : glBankTransactionDetailEntityList) {
            GlBankTransactionDetailFromEntity bean = (GlBankTransactionDetailFromEntity) bankTransactionDetailEntity.clone();
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
//            hashMap.put("chequeDateText", "تاريخ الشيك");
//            hashMap.put("chequeDateValue", glBankTransactionEntity.getChequeDate() != null ? glBankTransactionEntity.getChequeDate() : null);
//            hashMap.put("deservedDateText", "تاريخ الاستحقاق");
//            hashMap.put("deservedDateValue", glBankTransactionEntity.getChequeDueDate() != null ? glBankTransactionEntity.getChequeDueDate() : null);
            //  hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("dateValue", glBankTransactionEntity.getDate());
            hashMap.put("documentNumberValue", glBankTransactionEntity.getSerial());
            hashMap.put("payAccountValue", glBankTransactionEntity.getGlBank() != null ? glBankTransactionEntity.getGlBank().getName() : null);
            hashMap.put("amountValue", glBankTransactionEntity.getValue());
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(glBankTransactionEntity.getValue().toString()), "ريـال", "هلله"));
            hashMap.put("valueFromValue", remark1);
            hashMap.put("explainationValue", remark2);
            hashMap.put("accountManager", "مدير الحسابات");

            hashMap.put("rateText", "المعدل");
            hashMap.put("rateValue", glBankTransactionEntity.getRate());
            hashMap.put("amountInRateText", userDDs.get("TOTAL"));
//            hashMap.put("amountInRateValue", glBankTransactionEntity.getValueLocal());
            hashMap.put("amountInRateValue", glBankTransactionEntity.getValue());

            hashMap.put("sumValue", valueSummary);
            hashMap.put("localValue", localValue);

            hashMap.put("serialText", "م");
            hashMap.put("Receipt", "لا  يعتد بهذا الايصال الا اذا كان مختوم بخاتم النقابة");
            hashMap.put("stampText", "الختم");
            hashMap.put("tellerText", "امين الخزينه");
            hashMap.put("responsibleText", "مسئول الخدمة");
            hashMap.put("responsibleValue", glBankTransactionEntity.getCreatedNameParent());
            hashMap.put("listSize", glBankTransactionDetailEntityList.size());
            hashMap.put("CreatedBy", glBankTransactionEntity.getCreatedByName());
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
                    for (GlBankTransactionDetailFromEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                        if (transactionDetailEntity.getGlAccount() != null && transactionDetailEntity.getGlAccount().getId() != null) {
                            transactionDetailEntity.setAccountName(transactionDetailEntity.getGlAccount().getName());
                            transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlAccount().getAccNumber());
                        }
                    }
                } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                    hashMap.put("accountNameText", "اسم البند");
                    hashMap.put("accountNumberText", "كود البند");
                    for (GlBankTransactionDetailFromEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                        if (transactionDetailEntity.getGlItems() != null && transactionDetailEntity.getGlItems().getId() != null) {
                            transactionDetailEntity.setAccountName(transactionDetailEntity.getGlItems().getName());
                            transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlItems().getSerial());
                        }
                    }
                }

            }

            if (glBankTransactionEntity.getPaymentType() != null) {
                switch (glBankTransactionEntity.getPaymentType()) {
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

    public List<CostCenterDTO> completeCostCenter(String query) {
        try {
            List<CostCenterDTO> costCenterList = getCostCenterList();
            if (query == null || query.trim().equals("")) {
                costCenterConverter = new CostCenterDTOConverter(costCenterList);
                return costCenterList;
            }
            List<CostCenterDTO> filteredCostCenters = new ArrayList<>();

            String nameAr;
            Integer code;
            CostCenterDTO costCenter;
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

            costCenterConverter = new CostCenterDTOConverter(filteredCostCenters);
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

    public List<GlAccountDTO> completeGlAccount(String query) {
        try {
            List<GlAccountDTO> glaccounts = getGlAccountList();//45
            if (query == null || query.trim().equals("")) {
                glAccountConverter = new GlAccountDTOConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccountDTO> filteredGlaccounts = new ArrayList<>();
            String nameAr;
            Integer code;
            GlAccountDTO glaccount;
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

            glAccountConverter = new GlAccountDTOConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeGlAccount");
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
        if (glBankTransactionEntity == null) {
            glBankTransactionEntity = new GlBankTransactionEntity();
        }
        return glBankTransactionEntity;
    }

    public void setGlBankTransactionEntity(GlBankTransactionEntity glBankTransactionEntity) {
        this.glBankTransactionEntity = glBankTransactionEntity;
    }

    public GlBankTransactionDetailFromEntity getGlBankTransactionDetailEntity() {
        return glBankTransactionDetailEntity;
    }

    public void setGlBankTransactionDetailEntity(GlBankTransactionDetailFromEntity glBankTransactionDetailEntity) {
        this.glBankTransactionDetailEntity = glBankTransactionDetailEntity;
    }

    public GlBankTransactionDetailFromEntity getGlBankTransactionDetailEntitySelected() {
        return glBankTransactionDetailEntitySelected;
    }

    public void setGlBankTransactionDetailEntitySelected(GlBankTransactionDetailFromEntity glBankTransactionDetailEntitySelected) {
        this.glBankTransactionDetailEntitySelected = glBankTransactionDetailEntitySelected;
    }

    public List<GlBankTransactionDetailFromEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    public List<GlBankTransactionDetailFromEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<GlBankTransactionDetailFromEntity> rowsDeleted) {
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

    public List<GlAccountDTO> getGlAccountList() {
        if (glAccountList == null || glAccountList.isEmpty()) {
            glAccountList = glAccountService.getBranchGLAccountsActiveDTO(getUserData().getUserBranch().getId());
            setGlAccountConverter(new GlAccountDTOConverter(glAccountList));
        }
        return glAccountList;
    }

    public void setGlAccountList(List<GlAccountDTO> glAccountList) {
        this.glAccountList = glAccountList;
    }

    public List<CostCenterDTO> getCostCenterList() {
        if (costCenterList == null || costCenterList.isEmpty()) {
            costCenterList = costCenterService.getAllCostCenterDTOList(branchId);
            costCenterConverter = new CostCenterDTOConverter(costCenterList);
        }
        return costCenterList;
    }

    public void setCostCenterList(List<CostCenterDTO> costCenterList) {
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

    public GlAccountDTOConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlAccountDTOConverter glAccountConverter) {
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
    public CostCenterDTOConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterDTOConverter costCenterConverter) {
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
    public CostCenterDTO getCostCenterSelected() {
        return costCenterSelected;
    }

    /**
     * @param costCenterSelected the costCenterSelected to set
     */
    public void setCostCenterSelected(CostCenterDTO costCenterSelected) {
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
    public GlBankTransactionDetailFromEntity getGlBankTransactionDetailEntityUnSelected() {
        return glBankTransactionDetailEntityUnSelected;
    }

    /**
     * @param glBankTransactionDetailEntityUnSelected the
     * glBankTransactionDetailEntityUnSelected to set
     */
    public void setGlBankTransactionDetailEntityUnSelected(GlBankTransactionDetailFromEntity glBankTransactionDetailEntityUnSelected) {
        this.glBankTransactionDetailEntityUnSelected = glBankTransactionDetailEntityUnSelected;
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

    /**
     * @return the glRunningAccountDTOList
     */
    public List<GlAccountDTO> getGlRunningAccountDTOList() {
        if (glRunningAccountDTOList == null || glRunningAccountDTOList.isEmpty()) {
            glRunningAccountDTOList = getGlAccountList();
            runningAccountDTOConverter = new GlAccountDTOConverter(glRunningAccountDTOList);
        }
        return glRunningAccountDTOList;
    }

    /**
     * @param glRunningAccountDTOList the glRunningAccountDTOList to set
     */
    public void setGlRunningAccountDTOList(List<GlAccountDTO> glRunningAccountDTOList) {

        this.glRunningAccountDTOList = glRunningAccountDTOList;
    }

    /**
     * @return the runningAccountDTOConverter
     */
    public GlAccountDTOConverter getRunningAccountDTOConverter() {
        return runningAccountDTOConverter;
    }

    /**
     * @param runningAccountDTOConverter the runningAccountDTOConverter to set
     */
    public void setRunningAccountDTOConverter(GlAccountDTOConverter runningAccountDTOConverter) {
        this.runningAccountDTOConverter = runningAccountDTOConverter;
    }

    public GlAccountDTO mapAccountToDTO(GlAccount account) {
        GlAccountDTO accountDTO = null;
        if (account != null) {
            accountDTO = new GlAccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setName(account.getName());
            accountDTO.setAccNumber(account.getAccNumber());

            if (account.getCostCenterId() != null) {
                accountDTO.setCostCenterId(account.getCostCenterId().getId());
            }
        }

        return accountDTO;
    }

    public GlAccount mapAccountToEntity(GlAccountDTO accountDTO) {
        GlAccount glAccount = null;
        if (accountDTO != null) {
            glAccount = new GlAccount();
            glAccount.setId(accountDTO.getId());
            glAccount.setName(accountDTO.getName());
            glAccount.setAccNumber(accountDTO.getAccNumber());

            if (glAccount.getCostCenterId() != null) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(accountDTO.getCostCenterId());
                glAccount.setCostCenterId(costCenter);
            }
        }
        return glAccount;
    }

    public CostCenterDTO mapCostCenterToDTO(CostCenter costCenter) {
        CostCenterDTO costCenterDTO = null;
        if (costCenter != null) {
            costCenterDTO = new CostCenterDTO();
            costCenterDTO.setId(costCenter.getId());
            costCenterDTO.setName(costCenter.getName());
            costCenterDTO.setCode(costCenter.getCode());

            if (costCenter.getCurrentaccountId() != null) {
                GlAccount glAccount = new GlAccount();
                glAccount.setId(costCenter.getCurrentaccountId().getId());
                costCenterDTO.setCurrentaccountId(glAccount);
            }
        }
        return costCenterDTO;
    }

    public CostCenter mapCostCentertToEntity(CostCenterDTO costCenterDTO) {
        CostCenter costCenter = null;
        if (costCenterDTO != null) {
            costCenter = new CostCenter();
            costCenter.setId(costCenterDTO.getId());
            costCenter.setName(costCenterDTO.getName());
            costCenter.setCode(costCenterDTO.getCode());

            if (costCenterDTO.getCurrentaccountId() != null) {
                GlAccount account = new GlAccount();
                account.setId(costCenterDTO.getCurrentaccountId().getId());
                costCenter.setCurrentaccountId(account);
            }
        }
        return costCenter;
    }

    public void print(ActionEvent actionEvent) throws JRException, IOException {

        exportPDF(actionEvent);
        exportPDFVoutcher(actionEvent);
    }

    public void exportPDFVoutcher(ActionEvent actionEvent) throws JRException, IOException {
        if (glBankTransaction != null && glBankTransaction.getId() != null && glBankTransaction.getGeneralJournalId() != null) {
            List<com.toby.bean.JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList = generaljournalServiceDTO.printVoutcher(glBankTransaction.getGeneralJournalId().getId());
            if (journalDocumentArrangedReportBeanList != null && !journalDocumentArrangedReportBeanList.isEmpty()) {
                List<JournalDocumentAndSettelment> listAll = new ArrayList<>();
                JournalDocumentAndSettelment journalDocumentAndSettelment = new JournalDocumentAndSettelment();
                journalDocumentAndSettelment.getGlBankTransactionDetailEntityList().addAll(glBankTransactionDetailEntityList);
                journalDocumentAndSettelment.getJournalDocumentArrangedReportBeanList().addAll(journalDocumentArrangedReportBeanList);
                listAll.add(journalDocumentAndSettelment);
                HashMap hashMap = new HashMap();
                hashMap.put("param1", prepareReportVoutcher());
                hashMap.put("param2", prepareReport());
                hashMap.put("report1path", getUserData().getReportPath() + "generalJournalReportTotal.jasper");
                hashMap.put("report2path", getUserData().getReportPath() + "settlementdeed.jasper");
                if (getUri().contains("settlementdeedVoucherForm")) {
                    fillReport(hashMap, getUserData().getReportPath() + "constraintWithsettlementdone.jasper", listAll, "pdf");
                } else {
                    fillReport(hashMap, getUserData().getReportPath() + "constraintWithsettlementd.jasper", listAll, "pdf");
                }

            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن طباعة قيد بدون تفاصيل"));
            showMessageGeneral = Boolean.TRUE;
        }
    }

    public HashMap prepareReportVoutcher() {
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
//        hashMap.put("summaryCreditString", summaryCreditString);
//        hashMap.put("summaryDebitString", summaryDebitString);

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

    /**
     * @return the costCenterHeadList
     */
    public List<CostCenterDTO> getCostCenterHeadList() {
        if (costCenterHeadList == null || costCenterHeadList.isEmpty()) {
            getCostCenterList();
            costCenterHeadList = costCenterList;
            setCostCenterHeadConverter(new CostCenterDTOConverter(costCenterHeadList));
        }
        return costCenterHeadList;
    }

    /**
     * @param costCenterHeadList the costCenterHeadList to set
     */
    public void setCostCenterHeadList(List<CostCenterDTO> costCenterHeadList) {
        this.costCenterHeadList = costCenterHeadList;
    }

    /**
     * @return the costCenterHeadConverter
     */
    public CostCenterDTOConverter getCostCenterHeadConverter() {
        return costCenterHeadConverter;
    }

    /**
     * @param costCenterHeadConverter the costCenterHeadConverter to set
     */
    public void setCostCenterHeadConverter(CostCenterDTOConverter costCenterHeadConverter) {
        this.costCenterHeadConverter = costCenterHeadConverter;
    }

    /**
     * @return the isDebit
     */
    public Boolean getIsDebit() {
        return isDebit;
    }

    /**
     * @param isDebit the isDebit to set
     */
    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

    public void updateCostCenter() {
        if (getGlBankTransactionEntity().getCostAccount() != null && getGlBankTransactionEntity().getCostAccount().getCostCenterId() != null) {
            CostCenterDTO costCenterDTO = new CostCenterDTO();
            costCenterDTO.setId(getGlBankTransactionEntity().getCostAccount().getCostCenterId());
            getGlBankTransactionEntity().setCostCenterDTO(costCenterDTO);
        }
    }

}
