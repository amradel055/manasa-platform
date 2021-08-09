/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.checkUp;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.CheckUpDetailsService;
import com.isag.businessservice.CheckUpService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.GlBankConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.entity.CostCenter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.HosCheckUp;
import com.isag.entity.HosCheckUpDetail;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.Symbol;
import com.isag.entiy.HosCheckUpDetailEntity;
import com.isag.entiy.HosCheckUpEntity;
import com.isag.generaljournal.GeneralJournalBean;
import com.isag.isag.GlBankBasicDataForm;
import com.isag.isag.UserData;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "checkUpFormMB")
@ViewScoped
public class checkUpFormMB extends GlBankBasicDataForm {

    private Integer index = 0;

    private Integer branchId;
    private Integer companyId;
    private Integer hosCheckUpId;
    private Integer organizationTypeUpdated;

    private Boolean checkView = Boolean.FALSE;
    private Boolean lineCheckView = Boolean.FALSE;

    private Boolean paymentDropDownView = Boolean.FALSE;
    private String paymentTypeName;

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;

    private Boolean printButton = Boolean.FALSE;

    private Boolean payed = Boolean.FALSE;

    private Boolean saveValidation;

    private GlAccount account;

    private Date dateFrom;
    private Date dateTo;

    // DB Entities
    private HosCheckUp hosCheckUp;
    private HosCheckUp hosCheckUpExist;

    private List<HosCheckUpDetail> hosCheckUpDetailsList;
    private List<HosCheckUpDetail> hosCheckUpDetailsUpdatedList;

    // Interface Entities
    private HosCheckUpEntity hosCheckUpEntity;
    private HosCheckUpEntity hosCheckUpEntityMapper;
    private HosCheckUpDetailEntity hosCheckUpDetailEntity;
    private HosCheckUpDetailEntity hosCheckUpDetailEntitySelected;

    private List<HosCheckUpDetailEntity> hosCheckUpDetailEntityList;
    private List<HosCheckUpDetail> hosCheckUpDetailsDeletedList;

    private boolean customerView = false;
    private boolean accountView = false;
    private boolean supplierView = false;
    private boolean printAndSave = false;

    // Lists
    private List<GlAccount> glAccountList;
    private List<CostCenter> costCenterList;
    private List<GlBank> glBankList;
    private List<GlAdminUnit> adminUnitList;
    private List<InvOrganizationSite> organizationList;

    private Map<Integer, HosCheckUpDetail> hosCheckUpDetailsMap = new HashMap();

    private GlBankConverter bankConverter;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;

    private GlaccountConverter glAccountConverter;

    private Symbol symbol;
    private List<Symbol> symbolList;
    private SymbolConverter symbolConverter;
    private Integer OrganizationTypeAfterDialogConfirmation;
    private String remark1;
    private String remark2;

    @EJB
    private CheckUpService hosCheckUpService;

    @EJB
    private CheckUpDetailsService hosCheckUpDetailsService;

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
    private IsagUserBankService isagUserBankService;

    @EJB
    SymbolService symbolService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());

            // lists
            glAccountList = new ArrayList<>();
            costCenterList = new ArrayList<>();
            glBankList = new ArrayList<>();
            adminUnitList = new ArrayList<>();
            organizationList = new ArrayList<>();

            load();

            paymentDropDownView = true;

            organizationTypeUpdated = 0; // set to 0 during ad

            hosCheckUp.setCompanyId(getUserData().getCompany());
            hosCheckUp.setBranchId(getUserData().getUserBranch());
            //  hosCheckUp.setTransactionType(0);

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                setHosCheckUpId((Integer) context.getSessionMap().get("hosCheckUpIdSeclected"));
                findnotesReceivablesObject(getHosCheckUpId());
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
            if (hosCheckUpEntity.getId() == null) {
                printButton = Boolean.TRUE;
            } else {
                printButton = Boolean.FALSE;
            }

            /* symbolList = symbolService.getAllDocumentsByCompanyId(getUserData().getCompany().getId());
             if (symbolList != null && !symbolList.isEmpty()) {
             symbolConverter = new SymbolConverter(symbolList);
             }*/
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "init");
        }
    }

    private void findnotesReceivablesObject(int glBankTransId) {
        try {
            hosCheckUpExist = hosCheckUpService.findHosCheckUpById(glBankTransId);
            hosCheckUpDetailsList = hosCheckUpDetailsService.getAllHosCheckUpDetailsByHosCheckUpId(getHosCheckUpId());
            hosCheckUpEntity = mapHosCheckUpToHosCheckUpEntity(hosCheckUpExist);

            organizationTypeUpdated = getHosCheckUpEntity().getOrganizationType();
            if (hosCheckUpEntity != null) {
                remark1 = hosCheckUpEntity.getRemark() != null ? hosCheckUpEntity.getRemark() : null;
                remark2 = hosCheckUpEntity.getRemark2() != null ? hosCheckUpEntity.getRemark2() : null;
            }
            payed = true;
            paymentDropDownView = false;
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "findnotesReceivablesObject");
        }
    }

    @Override

    public void load() {
        try {
            hosCheckUpEntity = new HosCheckUpEntity();
            hosCheckUpEntity.setOrganizationType(2);
            hosCheckUpEntity.setChequeStatus(0);
            hosCheckUpEntity.setChequeDate(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
            hosCheckUpEntity.setChequeDueDate(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
            hosCheckUpEntity.setPaymentType(0);
            hosCheckUpEntity.setSerial(null);
            hosCheckUpEntity.setValue(hosCheckUpEntity.getValue() != null ? hosCheckUpEntity.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
            hosCheckUpEntity.setValueLocal(hosCheckUpEntity.getValueLocal() != null ? hosCheckUpEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
            hosCheckUpEntity.setRate(hosCheckUpEntity.getRate() != null ? hosCheckUpEntity.getRate().setScale(2, BigDecimal.ROUND_UP) : null);

            hosCheckUpEntity.setDate(new Date());
            hosCheckUp = new HosCheckUp();
            hosCheckUpDetailEntity = new HosCheckUpDetailEntity();
            hosCheckUpDetailsList = new ArrayList<>();
            hosCheckUpDetailsUpdatedList = new ArrayList<>();
            hosCheckUpDetailEntityList = new ArrayList<>();
            hosCheckUpDetailEntitySelected = new HosCheckUpDetailEntity();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "load");
        }

    }

    private void fillLists() {
        try {
            costCenterList = costCenterService.getAllSubCostCenterByBranchIdActive(branchId);

//        glBankList = glBankService.getAllGlBankByBranchId(branchId);
            fillGlBanks();

            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActive(branchId);
            fillAccounts();

            if (hosCheckUpEntity.getGlBank() == null) {
                hosCheckUpEntity.setGlBank((glBankList != null && !glBankList.isEmpty()) ? glBankList.get(0) : null);
            }
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "fillLists");
        }

    }

    private void fillGlBanks() {
        try {
            if (hosCheckUpEntity.getPaymentType() != 1) {
                glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 0);
            } else {
                glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 1);
            }

            bankConverter = new GlBankConverter(glBankList);
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "fillGlBanks");
        }
    }

    public void fillAccounts() {
        try {
            organizationList = organizationSiteService.getCustomerByBranchId(branchId);
            glAccountList = new ArrayList<>();
            for (InvOrganizationSite invOrganizationSite : organizationList) {
                account = new GlAccount();
                account = invOrganizationSite.getAccountId();
                glAccountList.add(account);
            }
            setInvOrganizationSiteConverter(new InvOrganizationSiteConverter(organizationList));
            setGlAccountConverter(new GlaccountConverter(glAccountList));
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "fillAccounts");
        }
    }

    public void checkVisability() {
        try {
            viewFooterColms();
            fillAccounts();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "checkVisability");
        }
    }

    public void viewFooterColms() {
        try {
            if (hosCheckUpEntity.getOrganizationType() == 2) {
                customerView = false;
                accountView = true;
                supplierView = false;
            } else if (hosCheckUpEntity.getOrganizationType() == 0) {
                accountView = false;
                customerView = true;
                supplierView = false;
            } else {
                accountView = false;
                customerView = false;
                supplierView = true;
            }
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "viewFooterColms");
        }
    }

    public void changeOrganizationSiteCancel() {
        try {
            hosCheckUpEntity.setOrganizationType(organizationTypeUpdated);
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "changeOrganizationSiteCancel");
        }
    }

    public void changeOrganizationSiteOk() {
        try {
            if (hosCheckUpDetailEntityList != null && !hosCheckUpDetailEntityList.isEmpty()) {
                hosCheckUpEntity.setOrganizationType(OrganizationTypeAfterDialogConfirmation);
            }
            if (!Objects.equals(organizationTypeUpdated, hosCheckUpEntity.getOrganizationType())) {
                organizationTypeUpdated = hosCheckUpEntity.getOrganizationType();
                hosCheckUpDetailEntityList = new ArrayList<>();

                hosCheckUpDetailsDeletedList = hosCheckUpDetailsList;
            }

            hosCheckUpDetailEntitySelected = new HosCheckUpDetailEntity();
            hosCheckUpDetailEntityList = new ArrayList<>();
            fillAccounts();
            viewFooterColms();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "changeOrganizationSiteOk");
        }
    }

    public void viewPaymentFields() {
        try {

            if (hosCheckUpEntity.getPaymentType() == 0) {
                checkView = false;
                lineCheckView = false;
            } else if (hosCheckUpEntity.getPaymentType() == 1) {
                checkView = true;
                lineCheckView = false;
            } else {
                checkView = false;
                lineCheckView = true;
            }

            fillGlBanks();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "viewPaymentFields");
        }
    }

    /**
     * public void updateDebitAccount() { if (hosCheckUpDetailEntitySelected !=
     * null) {
     *
     * if (hosCheckUpDetailEntitySelected.getInvOrganizationSite() == null ||
     * hosCheckUpDetailEntitySelected.getInvOrganizationSite().getAccountId() ==
     * null) { FacesContext.getCurrentInstance().addMessage(null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR,
     * getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل"));
     * return; } else {
     * hosCheckUpDetailEntitySelected.setAccountCreditId(hosCheckUpDetailEntitySelected.getInvOrganizationSite().getAccountId().getId());
     * }
     *
     * if (hosCheckUpEntity.getGlBank() == null) {
     * FacesContext.getCurrentInstance().addMessage(null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR,
     * getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة")); } else {
     * hosCheckUpDetailEntitySelected.setAccountDebitId(hosCheckUpEntity.getGlBank().getAccountId().getId());
     * } } }
     */
    private HosCheckUpEntity mapHosCheckUpToHosCheckUpEntity(HosCheckUp bankTransaction) {
        try {

            hosCheckUpEntityMapper = new HosCheckUpEntity();
            hosCheckUpEntityMapper.setId(bankTransaction.getId());
            hosCheckUpEntityMapper.setSerial(bankTransaction.getSerial());
            hosCheckUpEntityMapper.setDate(bankTransaction.getDate() != null ? bankTransaction.getDate() : new Date());
      //  hosCheckUpEntityMapper.setPaymentType(bankTransaction.getPaymentType() != null ? bankTransaction.getPaymentType() : 0);
            //  hosCheckUpEntityMapper.setPostFlag(bankTransaction.getPostFlag());
       /* if (bankTransaction.getPaymentType() == 1) {
             hosCheckUpEntityMapper.setChequeDate(bankTransaction.getChequeDate() != null ? bankTransaction.getChequeDate() : new Date());
             hosCheckUpEntityMapper.setChequeDueDate(bankTransaction.getChequeDueDate() != null ? bankTransaction.getChequeDueDate() : new Date());
             hosCheckUpEntityMapper.setChequeNumber(bankTransaction.getChequeNumber() != null ? bankTransaction.getChequeNumber() : null);
             hosCheckUpEntityMapper.setChequeStatus(bankTransaction.getChequeStatus() != null ? bankTransaction.getChequeStatus() : 0);
             paymentTypeName = "الشيك";
             } else if (bankTransaction.getPaymentType() == 2) {
             hosCheckUpEntityMapper.setChequeStatus(bankTransaction.getChequeStatus() != null ? bankTransaction.getChequeStatus() : 0);
             paymentTypeName = "الشيك الخطي";
             } else {
             paymentTypeName = "نقدي";
             }

             hosCheckUpEntityMapper.setGlBank(bankTransaction.getGlBankId() != null ? bankTransaction.getGlBankId() : null);
             hosCheckUpEntityMapper.setOrganizationType(bankTransaction.getOrganizationType() != null ? bankTransaction.getOrganizationType() : 0);
             hosCheckUpEntityMapper.setRemark(bankTransaction.getRemark() != null ? bankTransaction.getRemark() : null);
             hosCheckUpEntityMapper.setTransactionType(bankTransaction.getTransactionType() != null ? bankTransaction.getTransactionType() : 0);
             hosCheckUpEntityMapper.setValue(bankTransaction.getValue() != null ? bankTransaction.getValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
             hosCheckUpEntityMapper.setRate(bankTransaction.getRate() != null ? bankTransaction.getRate().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ONE);
             hosCheckUpEntityMapper.setValueLocal(bankTransaction.getValueLocal() == null ? BigDecimal.ZERO : bankTransaction.getValueLocal().setScale(2, BigDecimal.ROUND_UP));
             hosCheckUpEntityMapper.setGlYear(bankTransaction.getGlYear() != null ? bankTransaction.getGlYear() : getUserData().getGlYear().getId());

             for (HosCheckUpDetail transactionDetail : hosCheckUpDetailsList) {
             hosCheckUpDetailEntity = new HosCheckUpDetailEntity();
             ++index;
             hosCheckUpDetailEntity.setIndex(index);

             hosCheckUpDetailEntity.setId(transactionDetail.getId() != null ? transactionDetail.getId() : null);
             hosCheckUpDetailEntity.setCostCenterId(transactionDetail.getCostCenterId() != null ? transactionDetail.getCostCenterId().getId() : null);
             hosCheckUpDetailEntity.setAccountCreditId(transactionDetail.getAccountCreditId() != null ? transactionDetail.getAccountCreditId().getId() : null);
             hosCheckUpDetailEntity.setAccountDebitId(transactionDetail.getAccountDebitId() != null ? transactionDetail.getAccountDebitId().getId() : null);
             hosCheckUpDetailEntity.setAdminUnitId(transactionDetail.getAdminUnitId() != null ? transactionDetail.getAdminUnitId().getId() : null);
             hosCheckUpDetailEntity.setHosCheckUpId(transactionDetail.getHosCheckUpId() != null ? transactionDetail.getHosCheckUpId().getId() : null);
             hosCheckUpDetailEntity.setRemarks(transactionDetail.getRemarks() != null ? transactionDetail.getRemarks() : null);
             hosCheckUpDetailEntity.setInvOrganizationSite(transactionDetail.getOrganizationSiteId() != null ? transactionDetail.getOrganizationSiteId() : null);
             hosCheckUpDetailEntity.setValue(transactionDetail.getValue() != null ? transactionDetail.getValue().setScale(2, BigDecimal.ROUND_UP) : null);
             hosCheckUpDetailEntity.setValueLocal(transactionDetail.getValueLocal() != null ? transactionDetail.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
             //            valueSummary = valueSummary.add(hosCheckUpDetailEntity.getValue() != null ? hosCheckUpDetailEntity.getValue() : BigDecimal.ZERO);
             hosCheckUpDetailsMap.put(transactionDetail.getId(), transactionDetail);
             hosCheckUpDetailEntityList.add(hosCheckUpDetailEntity);
             }
             */
//        localValue = valueSummary.multiply(hosCheckUpEntityMapper.getRate());
            return hosCheckUpEntityMapper;
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "viewPaymentFields");
            return null;
        }
    }

    public void updateTransactionRate() {
        try {
            checkDate(true);
            if (hosCheckUpEntity.getGlBank() != null && hosCheckUpEntity.getGlBank().getCurrencyId() != null) {
                if (hosCheckUpEntity.getId() == null) {
                    hosCheckUpEntity.setRate(updateRate(hosCheckUpEntity.getGlBank().getCurrencyId(), hosCheckUpEntity.getDate()));
                }
                hosCheckUpEntity.setRate(hosCheckUpEntity.getRate() != null ? hosCheckUpEntity.getRate().setScale(2, BigDecimal.ROUND_UP) : null);
                changeRate();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "ظٹط¬ط¨ ط§ط®طھظٹط§ط± ط§ظ„ط®ط²ظٹظ†ط©"));
                hosCheckUpEntity.setValue(null);
                hosCheckUpEntity.setValueLocal(null);
            }
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "viewPaymentFields");
        }
    }

    public void changeRate() {
        try {
            calculateLocalValue();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "changeRate");
        }
//        updatetransactionDetailLocalValue();
    }

    public void printAndSave() {
        try {
            printAndSave = true;
            save();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "printAndSave");
        }
    }

    @Override
    public void calculateLocalValue() {
        try {
            hosCheckUpEntity.setValueLocal((hosCheckUpEntity.getValue() == null ? BigDecimal.ZERO : hosCheckUpEntity.getValue()).multiply(hosCheckUpEntity.getRate() == null ? BigDecimal.ONE : hosCheckUpEntity.getRate()));
            hosCheckUpEntity.setValueLocal(hosCheckUpEntity.getValueLocal() != null ? hosCheckUpEntity.getValueLocal().setScale(2, BigDecimal.ROUND_UP) : null);
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "calculateLocalValue");
        }
    }

    public void updatetransactionDetailLocalValue() {
        try {
            for (HosCheckUpDetailEntity detailEntity : hosCheckUpDetailEntityList) {
                detailEntity.setValueLocal((detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue()).multiply(hosCheckUpEntity.getRate() == null ? BigDecimal.ONE : hosCheckUpEntity.getRate()));
            }
            updateLocalValueSummary();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "updatetransactionDetailLocalValue");
        }
    }

    public void updateSummition(BigDecimal newValue, BigDecimal oldValue) {
        try {
            hosCheckUpEntity.getRate();

//        valueSummary = valueSummary.subtract(oldValue == null ? BigDecimal.ZERO : oldValue);
//        valueSummary = valueSummary.add(newValue == null ? BigDecimal.ZERO : newValue);
            updateLocalValueSummary();
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "updateSummition");
        }
    }

    public void updateLocalValueSummary() {
//        localValue = valueSummary.multiply(hosCheckUpEntity.getRate() != null ? hosCheckUpEntity.getRate() : BigDecimal.ZERO);
    }

    @Override
    public String save() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            saveValidation = true;
            validateSave();
            if (saveValidation) {
                if (hosCheckUpEntity != null) {

                    hosCheckUp.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                    hosCheckUp.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

                    if (hosCheckUpEntity.getId() != null) {
                        hosCheckUp.setId(hosCheckUpEntity.getId());
                        hosCheckUp.setCreatedBy(getHosCheckUpExist().getCreatedBy() != null ? getHosCheckUpExist().getCreatedBy() : null);
                        hosCheckUp.setCreationDate(getHosCheckUpExist().getCreationDate() != null ? getHosCheckUpExist().getCreationDate() : null);

                        hosCheckUp.setSerial(hosCheckUpEntity.getSerial());

                        hosCheckUp.setModifiedBy(getUserData().getUser());
                        hosCheckUp.setModificationDate(new Date());

                    } else {
                        hosCheckUp.setCreatedBy(getUserData().getUser());
                        hosCheckUp.setCreationDate(new Date());

                    }

                    hosCheckUp.setDate(hosCheckUpEntity.getDate() != null ? hosCheckUpEntity.getDate() : null);
                    /* hosCheckUp.setPostFlag(0);
                     hosCheckUp.setPaymentType(0);
                     hosCheckUp.setRemark(hosCheckUpEntity.getRemark());
                     hosCheckUp.setOrganizationType(hosCheckUpEntity.getOrganizationType() != null ? hosCheckUpEntity.getOrganizationType() : null);
                     hosCheckUp.setTransactionType(1);
                     hosCheckUp.setValue(hosCheckUpEntity.getValue() != null ? hosCheckUpEntity.getValue() : BigDecimal.ZERO);
                     hosCheckUp.setValueLocal(hosCheckUpEntity.getValueLocal() != null ? hosCheckUpEntity.getValueLocal() : BigDecimal.ZERO);
                     hosCheckUp.setRate(hosCheckUpEntity.getRate() != null ? hosCheckUpEntity.getRate() : BigDecimal.ONE);
                     hosCheckUp.setGlYear(getGlYearSelection().getYear() != null ? getGlYearSelection().getYear() : null);

                     if (hosCheckUpEntity.getPaymentType() == 1) {
                     hosCheckUp.setChequeDate(hosCheckUpEntity.getChequeDate() != null ? hosCheckUpEntity.getChequeDate() : new Date());
                     hosCheckUp.setChequeNumber(hosCheckUpEntity.getChequeNumber() != null ? hosCheckUpEntity.getChequeNumber() : null);
                     }

                     hosCheckUp.setChequeDueDate(hosCheckUpEntity.getChequeDueDate() != null ? hosCheckUpEntity.getChequeDueDate() : new Date());
                     hosCheckUp.setChequeStatus(hosCheckUpEntity.getChequeStatus() != null ? hosCheckUpEntity.getChequeStatus() : 0);

                     if (hosCheckUpEntity.getGlBank() != null) {
                     hosCheckUp.setGlBankId(hosCheckUpEntity.getGlBank());
                     }

                     if (hosCheckUpEntity.getInvoiceId() != null) {
                     InvPurchaseInvoice invoiceId = new InvPurchaseInvoice();
                     invoiceId.setId(hosCheckUpEntity.getInvoiceId());
                     hosCheckUp.setInvoiceId(invoiceId);
                     }

                     HosCheckUpDetail bankTransactionDetail = new HosCheckUpDetail();

                     if (hosCheckUpDetailEntity.getId() != null) {
                     bankTransactionDetail = hosCheckUpDetailsMap.get(hosCheckUpDetailEntity.getId());
                     bankTransactionDetail.setModificationDate(new Date());
                     bankTransactionDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                     } else {
                     bankTransactionDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                     bankTransactionDetail.setCreationDate(new Date());
                     }

                     bankTransactionDetail.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

                     if (hosCheckUpDetailEntity.getAccountDebitId() != null) {
                     GlAccount accountDebitId = new GlAccount();
                     accountDebitId.setId(hosCheckUpDetailEntity.getAccountDebitId());
                     bankTransactionDetail.setAccountDebitId(accountDebitId);
                     }

                     if (hosCheckUpDetailEntity.getInvOrganizationSite() != null) {
                     GlAccount accountCreditId = new GlAccount();
                     accountCreditId.setId(hosCheckUpDetailEntity.getInvOrganizationSite().getAccountId().getId());
                     bankTransactionDetail.setAccountCreditId(accountCreditId);
                     } else if (hosCheckUpDetailEntity.getAccountCreditId() != null) {
                     GlAccount accountCreditId = new GlAccount();
                     accountCreditId.setId(hosCheckUpDetailEntity.getAccountCreditId());
                     bankTransactionDetail.setAccountCreditId(accountCreditId);
                     }

                     bankTransactionDetail.setAccountDebitId(hosCheckUp.getGlBankId().getAccountId());

                     if (hosCheckUpDetailEntity.getAdminUnitId() != null) {
                     GlAdminUnit glAdminUnit = new GlAdminUnit();
                     glAdminUnit.setId(hosCheckUpDetailEntity.getAdminUnitId());
                     bankTransactionDetail.setAdminUnitId(glAdminUnit);
                     } else {
                     bankTransactionDetail.setAdminUnitId(null);
                     }

                     if (hosCheckUpDetailEntity.getCostCenterId() != null) {
                     CostCenter costCenter = new CostCenter();
                     costCenter.setId(hosCheckUpDetailEntity.getCostCenterId());
                     bankTransactionDetail.setCostCenterId(costCenter);
                     } else {
                     bankTransactionDetail.setCostCenterId(null);
                     }

                     bankTransactionDetail.setBankIdFrom(hosCheckUpEntity.getGlBank());
                     //                if(hosCheckUpDetailEntity.getInvOrganizationSite() != null){}
                     bankTransactionDetail.setOrganizationSiteId(hosCheckUpDetailEntity.getInvOrganizationSite() != null ? hosCheckUpDetailEntity.getInvOrganizationSite() : null);
                     bankTransactionDetail.setRemarks(hosCheckUpDetailEntity.getRemarks());
                     bankTransactionDetail.setValue(hosCheckUpEntity.getValue() != null ? hosCheckUpEntity.getValue() : BigDecimal.ZERO);
                     bankTransactionDetail.setValueLocal(hosCheckUpEntity.getValueLocal() != null ? hosCheckUpEntity.getValueLocal() : BigDecimal.ZERO);

                     //            bankTransactionDetail.set
                     try {
                     hosCheckUpDetailsUpdatedList.add(bankTransactionDetail);
                     HosCheckUp bankTransaction = hosCheckUpService.addHosCheckUp(hosCheckUp, hosCheckUpDetailsUpdatedList, null);
                     if (bankTransaction != null && bankTransaction.getId() != null && printAndSave) {
                     exportPDF(null);
                     printAndSave = false;
                     }
                     if (bankTransaction != null && bankTransaction.getId() != null) {
                     findnotesReceivablesObject(bankTransaction.getId());
                     }
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                     } catch (Exception e) {
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                     }

                     //exit("../notesreceivables/notesreceivablesList.xhtml");
                     //return "";
                     }
                     }*/
                }
            }
            return "";
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "save");
            return null;
        }
    }

    public void validateSave() {
        try {
            if (hosCheckUpEntity != null && hosCheckUpEntity.getPostFlag() != null && hosCheckUpEntity.getPostFlag() == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "تم ترحيل السند"));
                setShowMessageDetails(true);
                saveValidation = false;
            }
            if (hosCheckUpEntity.getGlBank() == null && hosCheckUpEntity.getGlBank().getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة"));
                setShowMessageDetails(true);
                saveValidation = false;
            }
            if (hosCheckUpEntity.getDate() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار التاريخ"));
                setShowMessageDetails(true);
                saveValidation = false;
            }
            if (hosCheckUpEntity.getValue() == null || hosCheckUpEntity.getValueLocal() == null || hosCheckUpEntity.getValue().compareTo(BigDecimal.ZERO) == -1 || hosCheckUpEntity.getValueLocal().compareTo(BigDecimal.ZERO) == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "قيمة المبلغ غير صحيحة"));
                setShowMessageDetails(true);
                saveValidation = false;
            }
            if (hosCheckUpEntity.getRate() == null || hosCheckUpEntity.getRate().compareTo(BigDecimal.ZERO) == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "معدل غير صحيح"));
                setShowMessageDetails(true);
                saveValidation = false;
            }

            if (hosCheckUpEntity.getOrganizationType() == 0) {
                if (hosCheckUpDetailEntity.getInvOrganizationSite() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
            } else if (hosCheckUpEntity.getOrganizationType() == 1) {
                if (hosCheckUpDetailEntity.getInvOrganizationSite() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
            } else if (hosCheckUpEntity.getOrganizationType() == 2) {
                if (hosCheckUpDetailEntity.getAccountCreditId() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار جهة التعامل"));
                    setShowMessageDetails(true);
                    saveValidation = false;
                }
            }
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "validateSave");
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
            saveError(e, "checkUpFormMB", "completeSymbol");
            return null;
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            List<HosCheckUpDetailEntity> gbtdes = new ArrayList<>();
            if (hosCheckUpEntity != null && hosCheckUpEntity.getId() != null) {
                if (hosCheckUpDetailEntityList != null && !hosCheckUpDetailEntityList.isEmpty()) {
                    gbtdes.add(hosCheckUpDetailEntityList.get(0));
                    fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
                }
            } else {
                save();
                if (hosCheckUpDetailEntityList != null && !hosCheckUpDetailEntityList.isEmpty()) {
                    gbtdes.add(hosCheckUpDetailEntityList.get(0));
                    fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
                }
            }
        } catch (Exception e) {
            saveError(e, "checkUpFormMB", "exportPDF");
        }

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    @Override
    public void resetDateTo() {
        setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(getHosCheckUpEntity().getDate())) {
                resetDateFrom();
            }
        }
    }

    public void loadThePageAgain() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../MovementClinics/CheckUpForm.xhtml");

    }

    public String exit() {
        exit("../MovementClinics/CheckUp.xhtml");
        return "";
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        //  hashMap.put("companyImage", getUserData().getImageReportPath());

        // hashMap.put("reportNameText", "سند قبض ");
        hashMap.put("recipientText", "المستلم");
        hashMap.put("SecretaryOfTheTreasuryText", "امين الخزينة");
        hashMap.put("recievedFromText", "استلمنا من");
        hashMap.put("signatureText", "التوقيع");
        hashMap.put("valueFromText", "وذلك قيمة");
        hashMap.put("documentNumberText", "رقم السند");
        hashMap.put("chequeDateText", "تاريخ الشيك");
        hashMap.put("chequeDateValue", hosCheckUpEntityMapper.getChequeDate() != null ? hosCheckUpEntityMapper.getChequeDate() : null);
        hashMap.put("deservedDateText", "تاريخ الاستحقاق");
        hashMap.put("deservedDateValue", hosCheckUpEntityMapper.getChequeDueDate() != null ? hosCheckUpEntityMapper.getChequeDueDate() : null);

        hashMap.put("rateText", "المعدل");
        hashMap.put("rateValue", hosCheckUpEntityMapper.getRate());
        hashMap.put("amountInRateText", userDDs.get("TOTAL"));
        hashMap.put("amountInRateValue", hosCheckUpEntityMapper.getValueLocal());

        hashMap.put("dateValue", hosCheckUpEntityMapper.getDate());
        hashMap.put("documentNumberValue", hosCheckUpEntityMapper.getSerial());
        hashMap.put("recipientValue", hosCheckUpEntityMapper.getGlBank().getName());
        hashMap.put("amountValue", hosCheckUpEntityMapper.getValue());
        hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(hosCheckUpEntityMapper.getValue().toString()), "جنيه", "قرش"));
        hashMap.put("valueFromValue", remark1);

        if (hosCheckUpEntity.getOrganizationType() == 1 || hosCheckUpEntity.getOrganizationType() == 0) {
            hashMap.put("recievedFromValue", hosCheckUpDetailEntity.getInvOrganizationSite() != null ? hosCheckUpDetailEntity.getInvOrganizationSite().getName() : null);
        } else if (hosCheckUpEntity.getOrganizationType() == 2) {
            GlAccount recipientaccount = new GlAccount();
            recipientaccount = glAccountService.findGlAccount(hosCheckUpDetailEntity.getAccountCreditId());
            hashMap.put("recievedFromValue", recipientaccount != null ? recipientaccount.getName() : null);
        }
        if (hosCheckUpEntityMapper.getPaymentType() != null) {
            switch (hosCheckUpEntityMapper.getPaymentType()) {
                case 0:
                    hashMap.put("reportNameText", "سندقبض نقدي ");
                    break;
                case 1:
                    hashMap.put("reportNameText", "سندقبض شيك ");
                    break;
                case 2:
                    hashMap.put("reportNameText", "سند قبض شيك خطي ");
                    break;
            }
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

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

    public Integer getHosCheckUpId() {
        return hosCheckUpId;
    }

    public void setHosCheckUpId(Integer hosCheckUpId) {
        this.hosCheckUpId = hosCheckUpId;
    }

    public HosCheckUp getHosCheckUp() {
        return hosCheckUp;
    }

    public void setHosCheckUp(HosCheckUp hosCheckUp) {
        this.hosCheckUp = hosCheckUp;
    }

    public List<HosCheckUpDetail> getHosCheckUpDetailsList() {
        return hosCheckUpDetailsList;
    }

    public void setHosCheckUpDetailsList(List<HosCheckUpDetail> hosCheckUpDetailsList) {
        this.hosCheckUpDetailsList = hosCheckUpDetailsList;
    }

    public List<HosCheckUpDetail> getHosCheckUpDetailsUpdatedList() {
        return hosCheckUpDetailsUpdatedList;
    }

    public void setHosCheckUpDetailsUpdatedList(List<HosCheckUpDetail> hosCheckUpDetailsUpdatedList) {
        this.hosCheckUpDetailsUpdatedList = hosCheckUpDetailsUpdatedList;
    }

    public HosCheckUpEntity getHosCheckUpEntity() {
        return hosCheckUpEntity;
    }

    public void setHosCheckUpEntity(HosCheckUpEntity hosCheckUpEntity) {
        this.hosCheckUpEntity = hosCheckUpEntity;
    }

    public HosCheckUpDetailEntity getHosCheckUpDetailEntity() {
        return hosCheckUpDetailEntity;
    }

    public void setHosCheckUpDetailEntity(HosCheckUpDetailEntity hosCheckUpDetailEntity) {
        this.hosCheckUpDetailEntity = hosCheckUpDetailEntity;
    }

    public HosCheckUpDetailEntity getHosCheckUpDetailEntitySelected() {
        return hosCheckUpDetailEntitySelected;
    }

    public void setHosCheckUpDetailEntitySelected(HosCheckUpDetailEntity hosCheckUpDetailEntitySelected) {
        this.hosCheckUpDetailEntitySelected = hosCheckUpDetailEntitySelected;
    }

    public List<HosCheckUpDetailEntity> getHosCheckUpDetailEntityList() {
        return hosCheckUpDetailEntityList;
    }

    public void setHosCheckUpDetailEntityList(List<HosCheckUpDetailEntity> hosCheckUpDetailEntityList) {
        this.hosCheckUpDetailEntityList = hosCheckUpDetailEntityList;
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
     * @return the hosCheckUpExist
     */
    public HosCheckUp getHosCheckUpExist() {
        return hosCheckUpExist;
    }

    /**
     * @param hosCheckUpExist the hosCheckUpExist to set
     */
    public void setHosCheckUpExist(HosCheckUp hosCheckUpExist) {
        this.hosCheckUpExist = hosCheckUpExist;
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

    public Map<Integer, HosCheckUpDetail> getHosCheckUpDetailsMap() {
        return hosCheckUpDetailsMap;
    }

    public void setHosCheckUpDetailsMap(Map<Integer, HosCheckUpDetail> hosCheckUpDetailsMap) {
        this.hosCheckUpDetailsMap = hosCheckUpDetailsMap;
    }

    public GlAccount getAccount() {
        return account;
    }

    public void setAccount(GlAccount account) {
        this.account = account;
    }

    public boolean isAccountView() {
        return accountView;
    }

    public void setAccountView(boolean accountView) {
        this.accountView = accountView;
    }

    public GlaccountConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlaccountConverter glAccountConverter) {
        this.glAccountConverter = glAccountConverter;
    }

    /**
     * @return the customerView
     */
    public boolean isCustomerView() {
        return customerView;
    }

    /**
     * @param customerView the customerView to set
     */
    public void setCustomerView(boolean customerView) {
        this.customerView = customerView;
    }

    public HosCheckUpEntity getHosCheckUpEntityMapper() {
        return hosCheckUpEntityMapper;
    }

    public void setHosCheckUpEntityMapper(HosCheckUpEntity hosCheckUpEntityMapper) {
        this.hosCheckUpEntityMapper = hosCheckUpEntityMapper;
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

    public boolean isSupplierView() {
        return supplierView;
    }

    public void setSupplierView(boolean supplierView) {
        this.supplierView = supplierView;
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
    public Boolean getSaveValidation() {
        return saveValidation;
    }

    /**
     * @param saveValidation the saveValidation to set
     */
    public void setSaveValidation(Boolean saveValidation) {
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
     * @return the hosCheckUpDetailsDeletedList
     */
    public List<HosCheckUpDetail> getHosCheckUpDetailsDeletedList() {
        return hosCheckUpDetailsDeletedList;
    }

    /**
     * @param hosCheckUpDetailsDeletedList the hosCheckUpDetailsDeletedList to
     * set
     */
    public void setHosCheckUpDetailsDeletedList(List<HosCheckUpDetail> hosCheckUpDetailsDeletedList) {
        this.hosCheckUpDetailsDeletedList = hosCheckUpDetailsDeletedList;
    }

    /**
     * @return the printAndSave
     */
    public boolean isPrintAndSave() {
        return printAndSave;
    }

    /**
     * @param printAndSave the printAndSave to set
     */
    public void setPrintAndSave(boolean printAndSave) {
        this.printAndSave = printAndSave;
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

}
