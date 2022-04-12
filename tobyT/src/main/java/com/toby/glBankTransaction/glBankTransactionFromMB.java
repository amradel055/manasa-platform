/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.glBankTransaction;

import com.toby.bean.BankBalanceReportBean;
import com.toby.bean.BankBalanceReportSearchBean;
import com.toby.businessservice.CurrencyOperationService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.report.BankBalanceViewService;
import com.toby.bussinessservice.global.Utility;
import com.toby.converter.GlBankConverter;
import com.toby.dto.GlBankDTO;
import com.toby.entity.Currency;
import com.toby.entity.CurrencyOperation;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entiy.GlBankEntity;
import com.toby.entiy.GlBankTransactionDetailEntity;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.toby.GlBankBasicDataForm;
import com.toby.toby.UserData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;
import tafqeet.Tafqeet;

/**
 *
 * @author hq002
 */
@Named(value = "glBankTransactionFromMB")
@ViewScoped
public class glBankTransactionFromMB extends GlBankBasicDataForm {
    
    private GlBankTransaction glBankTransaction;
    private GlBankTransactionEntity glBankTransactionEntity;
    private BigDecimal bankCash;
    private GlBankTransactionDetail glBankTransactionDetail;
    private GlBankTransactionDetailEntity GlBankTransactionDetailEntity;
    private BankBalanceReportSearchBean balanceReportSearchBean;
    private Integer glBankTransactionId;
    
    private GlBank glBank;
    private List<GlBank> glBankListTo;
    private List<GlBank> glBankListFrom;
    private GlBankEntity glBankEntity;
    
    private GlBankConverter glBankConverterTo;
    private GlBankConverter glBankConverterFrom;
    
    private List<Currency> currencylist;
    private Currency currency;
    
    private CurrencyOperation currencyOperation;
    private List<CurrencyOperation> currencyOperations;
    
    private Map<Integer, GlBankTransactionDetail> invQuotationDetailList;

//    private Boolean disableSave = false;
    private Boolean saveValidation;
    private List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;
    private String remark;
    @EJB
    private CurrencyOperationService currencyOperationService;
    
    @EJB
    GlBankTransactionService bankTransactionService;
    
    @EJB
    GlBankTransactionDetailsService bankTransactionDetailsService;
    
    @EJB
    TobyUserBankService tobyUserBankService;
    
    @EJB
    private GlAccountService glAccountService;
    
    @EJB
    private BankBalanceViewService bankBalanceViewService;
    
    @Override
    @PostConstruct
    public void init() {
        try {
            glBankTransaction = new GlBankTransaction();
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionDetail = new GlBankTransactionDetail();
            GlBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
            load();
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "init");
        }
    }
    
    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            
            glBankTransactionEntity = new GlBankTransactionEntity();
            GlBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
            glBankTransactionEntity.setPaymentType(0);
//            glBankListTo = tobyUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), glBankTransactionEntity.getPaymentType());
            glBankListFrom = tobyUserBankService.getCashGlBankByBranchId(getUserData().getUserBranch().getId());
//            setGlBankConverterTo(new GlBankConverter(glBankListTo));
            setGlBankConverterFrom(new GlBankConverter(glBankListFrom));
            
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setGlBankTransactionDataEmpty();
                  getBankCashBalance();
                return;
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    glBankTransactionId = (Integer) context.getSessionMap().get("glBankTransactionSelected");
                    validateGlBankUpdatedValues(glBankTransactionId);
                    getBankCashBalance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "load");
        }
        
    }
    
    public void convertDateFrom() {
        getGlBankTransactionEntity().setDate(findDateFromHijriFrom());
    }
    
    public void setGlBankTransactionDataEmpty() {
        try {
            
            fillGlBanks();
            glBankTransactionEntity.setDate(Utility.hourNow());
            
            updateRate();
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "setGlBankTransactionDataEmpty");
        }
    }
    
    public void fillGlBanks() {
        try {
            GlBankTransactionDetailEntity.setBankFrom((glBankListFrom != null && !glBankListFrom.isEmpty()) ? glBankListFrom.get(0) : null);
            GlBankTransactionDetailEntity.setBankTo(null);
//            GlBankTransactionDetailEntity.setBankTo((glBankListTo != null && !glBankListTo.isEmpty()) ? glBankListTo.get(0) : null);

//            disableSave = false;
//            if (glBankListTo == null || glBankListTo.isEmpty() || glBankListFrom == null || glBankListFrom.isEmpty()) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد خزائن لهذا النوع من التحويل"));
//                disableSave = true;
//            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "fillGlBanks");
        }
    }
    
    public void getBankCashBalance() {
        if (GlBankTransactionDetailEntity.getBankFrom() != null) {
            getBalanceReportSearchBean().setDateTo(new Date());
            GlBankDTO bankDTO = new GlBankDTO();
            bankDTO.setId(GlBankTransactionDetailEntity.getBankFrom().getId());
            getBalanceReportSearchBean().setGlBankList(new ArrayList<>());
            getBalanceReportSearchBean().getGlBankList().add(bankDTO);
            getBalanceReportSearchBean().setBranchId(getUserData().getSelectedBranch());
            List<BankBalanceReportBean> balanceReportBeans = bankBalanceViewService.searchNative(balanceReportSearchBean);
            if(balanceReportBeans != null && !balanceReportBeans.isEmpty() && balanceReportBeans.get(0) != null){
            bankCash = balanceReportBeans.get(0).getBalance(); 
            }
        }
        
    }
    
    public void validateGlBankUpdatedValues(Integer glBankTrId) {
        try {
            glBankTransaction = new GlBankTransaction();
            glBankTransactionDetail = new GlBankTransactionDetail();
            glBankTransaction = bankTransactionService.findGlBankTransactionById(glBankTrId);
            glBankTransactionDetail = bankTransactionDetailsService.getGlBankTransactionDetailByGlBankTransactionId(glBankTrId);
            
            glBankTransactionEntity.setId(glBankTransaction.getId());
            glBankTransactionEntity.setMarkEdit(glBankTransaction.getMarkEdit() != null ? glBankTransaction.getMarkEdit() : null);
            glBankTransactionEntity.setValue(glBankTransaction.getValue() != null ? glBankTransaction.getValue() : null);
            glBankTransactionEntity.setPaymentType(glBankTransaction.getPaymentType() != null ? glBankTransaction.getPaymentType() : 0);
            glBankTransactionEntity.setOrganizationType(glBankTransaction.getOrganizationType() != null ? glBankTransaction.getOrganizationType() : 0);
            glBankTransactionEntity.setValueLocal(glBankTransaction.getValueLocal() != null ? glBankTransaction.getValueLocal() : null);
            glBankTransactionEntity.setTransactionType(glBankTransaction.getTransactionType() != null ? glBankTransaction.getTransactionType() : 0);
            glBankTransactionEntity.setRemark(glBankTransaction.getRemark() != null ? glBankTransaction.getRemark() : null);
            glBankTransactionEntity.setDate(glBankTransaction.getDate() != null ? glBankTransaction.getDate() : null);
            glBankTransactionEntity.setGlBank(glBankTransaction.getGlBankId() != null ? glBankTransaction.getGlBankId() : null);
            glBankTransactionEntity.setRate(glBankTransaction.getRate() != null ? glBankTransaction.getRate() : BigDecimal.ONE);
            glBankTransactionEntity.setInvoiceId(glBankTransaction.getInvoiceId() != null ? glBankTransaction.getInvoiceId().getId() : null);
            glBankTransactionEntity.setSerial(glBankTransaction.getSerial() != null ? glBankTransaction.getSerial() : null);
            glBankTransactionEntity.setPostFlag(glBankTransaction.getPostFlag());
            GlBankTransactionDetailEntity.setId(glBankTransactionDetail.getId() != null ? glBankTransactionDetail.getId() : null);
            GlBankTransactionDetailEntity.setMarkEdit(glBankTransactionDetail.getMarkEdit() != null ? glBankTransactionDetail.getMarkEdit() : null);
            GlBankTransactionDetailEntity.setRemarks(glBankTransactionDetail.getRemarks() != null ? glBankTransactionDetail.getRemarks() : null);
            GlBankTransactionDetailEntity.setValue(glBankTransactionDetail.getValue() != null ? glBankTransactionDetail.getValue() : null);
            GlBankTransactionDetailEntity.setValueLocal(glBankTransactionDetail.getValueLocal() != null ? glBankTransactionDetail.getValueLocal() : null);
            GlBankTransactionDetailEntity.setAccountCreditId(glBankTransactionDetail.getAccountCreditId() != null ? glBankTransactionDetail.getAccountCreditId().getId() : null);
            GlBankTransactionDetailEntity.setAccountDebitId(glBankTransactionDetail.getAccountDebitId() != null ? glBankTransactionDetail.getAccountDebitId().getId() : null);
            GlBankTransactionDetailEntity.setAdminUnitId(glBankTransactionDetail.getAdminUnitId() != null ? glBankTransactionDetail.getAdminUnitId().getId() : null);
            GlBankTransactionDetailEntity.setCostCenterId(glBankTransactionDetail.getCostCenterId() != null ? glBankTransactionDetail.getCostCenterId().getId() : null);
            GlBankTransactionDetailEntity.setGlBankTransactionId(glBankTransactionDetail.getGlBankTransactionId() != null ? glBankTransactionDetail.getGlBankTransactionId().getId() : null);
            
            GlBankTransactionDetailEntity.setBankFrom(glBankTransactionDetail.getBankIdFrom() != null ? glBankTransactionDetail.getBankIdFrom() : null);
//            GlBankTransactionDetailEntity.setBankTo(glBankTransactionDetail.getBankIdTo() != null ? glBankTransactionDetail.getBankIdTo() : null);
            remark = glBankTransactionEntity.getRemark();
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "validateGlBankUpdatedValues");
        }
    }
    
    public String exit() {
        try {
            exit("../glBankTransaction/glBankTransactionList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "exit");
            return null;
        }
    }
    
    @Override
    public String save() {
        try {
            
            glBankTransaction.setBranchId(getUserData().getUserBranch());
            glBankTransaction.setCompanyId(getUserData().getCompany());
            
            if (glBankTransaction.getId() == null) {
                glBankTransaction.setCreationDate(new Date());
                glBankTransaction.setCreatedBy(getUserData().getUser());
            } else {
                glBankTransaction.setModificationDate(new Date());
                glBankTransaction.setModifiedBy(getUserData().getUser());
            }
            
            glBankTransactionDetail.setCompanyId(getUserData().getCompany());
            
            if (glBankTransactionDetail.getId() == null) {
                glBankTransactionDetail.setCreationDate(new Date());
                glBankTransactionDetail.setCreatedBy(getUserData().getUser());
            } else {
                glBankTransactionDetail.setModificationDate(new Date());
                glBankTransactionDetail.setModifiedBy(getUserData().getUser());
            }
            
            validateInputsForSave();
            saveValidation = true;
            validateSave();
            if (saveValidation) {
                glBankTransaction.setPostFlag(0);
                glBankTransaction = bankTransactionService.addOneGlBankTransaction(glBankTransaction, glBankTransactionDetail, getUserData().getUser());
                return exit();
            }
            return "";
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "exit");
            return null;
        }
    }
    
    public void validateInputsForSave() {
        try {
            glBankTransactionDetailEntityList = new ArrayList<>();
            glBankTransaction.setDate(glBankTransactionEntity.getDate() != null ? glBankTransactionEntity.getDate() : null);
            glBankTransaction.setRate(glBankTransactionEntity.getRate() != null ? glBankTransactionEntity.getRate() : null);
            //glBankTransaction.setPostFlag(0);
            glBankTransaction.setPaymentType(glBankTransactionEntity.getPaymentType() != null ? glBankTransactionEntity.getPaymentType() : 0);
            glBankTransaction.setOrganizationType(glBankTransactionEntity.getOrganizationType() != null ? glBankTransactionEntity.getOrganizationType() : null);
            glBankTransaction.setRemark(glBankTransactionEntity.getRemark() != null ? glBankTransactionEntity.getRemark() : null);
            remark = glBankTransactionEntity.getRemark();
            glBankTransaction.setTransactionType(2);
            glBankTransaction.setValue(GlBankTransactionDetailEntity.getValue());
            glBankTransaction.setValueLocal(GlBankTransactionDetailEntity.getValueLocal());
            
            glBankTransactionDetail.setValue(GlBankTransactionDetailEntity.getValue() != null ? GlBankTransactionDetailEntity.getValue() : null);
            glBankTransactionDetail.setValueLocal(GlBankTransactionDetailEntity.getValueLocal() != null ? GlBankTransactionDetailEntity.getValueLocal() : null);
            glBankTransactionDetail.setAccountDebitId(GlBankTransactionDetailEntity.getBankFrom() != null ? GlBankTransactionDetailEntity.getBankFrom().getAccountId() : null);
//            glBankTransactionDetail.setAccountCreditId(GlBankTransactionDetailEntity.getBankTo() != null ? GlBankTransactionDetailEntity.getBankTo().getAccountId() : null);
            glBankTransactionDetail.setAccountCreditId(GlBankTransactionDetailEntity.getBankFrom() != null ? GlBankTransactionDetailEntity.getBankFrom().getTransferaccountId() : null);
            glBankTransactionDetail.setBankIdFrom(GlBankTransactionDetailEntity.getBankFrom() != null ? GlBankTransactionDetailEntity.getBankFrom() : null);
//            glBankTransactionDetail.setBankIdTo(GlBankTransactionDetailEntity.getBankTo() != null ? GlBankTransactionDetailEntity.getBankTo() : null);
            glBankTransactionDetail.setRemarks(glBankTransactionEntity.getRemark() != null ? glBankTransactionEntity.getRemark() : null);
            glBankTransactionDetail.setRateBankFrom(glBankTransactionEntity.getRateBankFrom());
            glBankTransactionDetail.setRateBankTo(glBankTransactionEntity.getRateBankTo());
            glBankTransactionDetailEntityList.add(GlBankTransactionDetailEntity);
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "validateInputsForSave");
        }
    }
    
    public void validateSave() {
        try {
            if (GlBankTransactionDetailEntity.getBankFrom() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "خزنة التحويل غير مربوطة بحساب"));
                saveValidation = false;
            }
//            else if (GlBankTransactionDetailEntity.getBankTo() == null) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "خزنة الاستلام غير مربوطة بحساب"));
//                saveValidation = false;
//            }
//            if (GlBankTransactionDetailEntity.getBankFrom().equals(GlBankTransactionDetailEntity.getBankTo())) {
//                GlBankTransactionDetailEntity.setBankTo(null);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن التحويل الى نفس الحساب"));
//                saveValidation = false;
//            }
//            if (glBankTransactionEntity != null && glBankTransactionEntity.getPostFlag() != null && glBankTransactionEntity.getPostFlag() == 1) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "تم ترحيل السند"));
//                saveValidation = false;
//            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "validateSave");
        }
    }
    
    public void updateRate() {
        try {
            
            if (GlBankTransactionDetailEntity.getBankFrom() != null && GlBankTransactionDetailEntity.getBankFrom().getCurrencyId() != null) {
                if (glBankTransactionEntity.getDate() != null) {
//                    currencyOperation = currencyOperationService.getRatesByDates(GlBankTransactionDetailEntity.getBankFrom().getCurrencyId().getId(), glBankTransactionEntity.getDate(), getUserData().getCompany().getId());
                    glBankTransactionEntity.setRate(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());
                    glBankTransactionEntity.setRateBankFrom(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());

//                    currencyOperation = currencyOperationService.getRatesByDates(GlBankTransactionDetailEntity.getBankTo().getCurrencyId().getId(), glBankTransactionEntity.getDate(), getUserData().getCompany().getId());
                    glBankTransactionEntity.setRateBankTo(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());
                    
                    calculateLocalValue();
                    getBankCashBalance();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("NO_CURRENCY_ERROR")));
                    
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("MUST_SELECT_DATE")));
            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "updateRate");
        }
        
    }
    
    public void calculateLocalValue() {
        try {
            GlBankTransactionDetailEntity.setValueLocal(glBankTransactionEntity.getRate().multiply(GlBankTransactionDetailEntity.getValue() == null ? BigDecimal.ZERO : GlBankTransactionDetailEntity.getValue()));
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "calculateLocalValue");
        }
    }
    
    public void updateDate(SelectEvent event) {
        updateRate();
    }
    
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            List<GlBankTransactionEntity> gbtdes = new ArrayList<>();
            gbtdes.add(glBankTransactionEntity);
            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                fillReport(prepareReport(), getUserData().getReportPath() + "glBankTransaction.jasper", gbtdes, "pdf");
            }
//            else {
//                save();
//                fillReport(prepareReport(), getUserData().getReportPath() + "glBankTransaction.jasper", gbtdes, "pdf");
//            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "exportPDF");
        }
        
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
            //  hashMap.put("companyImage", getUserData().getImageReportPath());

            // hashMap.put("reportNameText", "سند قبض ");
            hashMap.put("recipientText", "المستلم");
            hashMap.put("SecretaryOfTheTreasuryText", "امين الخزينة");
            hashMap.put("recievedFromText", "استلمنا من");
            hashMap.put("signatureText", "التوقيع");
            hashMap.put("valueFromText", "وذلك قيمة");
            hashMap.put("documentNumberText", "رقم السند");
            //hashMap.put("chequeDateText", "تاريخ الشيك");
            //hashMap.put("chequeDateValue", glBankTransactionEntity.getDate() != null ? glBankTransactionEntity.getDate() : null);
            //hashMap.put("deservedDateText", "تاريخ الاستحقاق");
            // hashMap.put("deservedDateValue", glBankTransactionEntityMapper.getChequeDueDate() != null ? glBankTransactionEntityMapper.getChequeDueDate() : null);

            hashMap.put("rateText", "المعدل");
            hashMap.put("rateValue", glBankTransactionEntity.getRate());
            hashMap.put("amountInRateText", userDDs.get("TOTAL"));
            hashMap.put("amountInRateValue", glBankTransactionEntity.getValueLocal());
            
            hashMap.put("dateValue", glBankTransactionEntity.getDate());
            hashMap.put("documentNumberValue", glBankTransaction.getSerial() != null ? glBankTransaction.getSerial() : null);
            //hashMap.put("recipientValue", glBankTransactionEntity.getGlBank().getName());
            hashMap.put("amountValue", GlBankTransactionDetailEntity.getValue());
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(GlBankTransactionDetailEntity.getValue().toString()), "ريـال", "هلله"));
            hashMap.put("valueFromValue", remark);

            /*   GlAccount recipientaccount = new GlAccount();
             recipientaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountDebitId());
             hashMap.put("recipientValue", recipientaccount != null ? recipientaccount.getName() : null);

             GlAccount recievedaccount = new GlAccount();
             recievedaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountCreditId());
             hashMap.put("recievedFromValue", recievedaccount != null ? recievedaccount.getName() : null);*/
            if (GlBankTransactionDetailEntity != null) {
                if (GlBankTransactionDetailEntity.getBankFrom() != null && GlBankTransactionDetailEntity.getBankFrom().getAccountId() != null) {
                    hashMap.put("recievedFromValue", GlBankTransactionDetailEntity.getBankFrom().getAccountId().getName() != null ? GlBankTransactionDetailEntity.getBankFrom().getAccountId().getName() : null);
                }
                if (GlBankTransactionDetailEntity.getBankTo() != null && GlBankTransactionDetailEntity.getBankTo().getAccountId() != null) {
                    hashMap.put("recipientValue", GlBankTransactionDetailEntity.getBankTo().getAccountId().getName() != null ? GlBankTransactionDetailEntity.getBankTo().getAccountId().getName() : null);
                }
            }
            /*if (glBankTransactionEntity.getOrganizationType() == 1 || glBankTransactionEntity.getOrganizationType() == 0) {
             hashMap.put("recievedFromValue", GlBankTransactionDetailEntity.getInvOrganizationSite() != null ? GlBankTransactionDetailEntity.getInvOrganizationSite().getName() : null);
             } else if (glBankTransactionEntity.getOrganizationType() == 2) {
             GlAccount recipientaccount = new GlAccount();
             recipientaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountCreditId());
             hashMap.put("recievedFromValue", recipientaccount != null ? recipientaccount.getName() : null);
             }*/
            if (glBankTransactionEntity.getPaymentType() != null) {
                switch (glBankTransactionEntity.getPaymentType()) {
                    case 0:
                        hashMap.put("reportNameText", "سند تحويل نقدي");
                        break;
                    case 2:
                        hashMap.put("reportNameText", "سند تحويل شيك خطي");
                        break;
                }
            }
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            return hashMap;
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "prepareReport");
            return null;
        }
    }
    
    public List<GlBank> completeGlBankTo(String query) {
        try {
            List<GlBank> glBanksList = getGlBankListTo();
            if (query == null || query.trim().equals("")) {
                
                glBankConverterTo = new GlBankConverter(glBanksList);
                return glBanksList;
            }
            List<GlBank> filteredGlBanks = new ArrayList<>();
            
            String nameAr;
            Integer code;
            GlBank glBankFilter;
            for (int i = 0; i < getGlBankListTo().size(); i++) {
                glBankFilter = glBanksList.get(i);
                nameAr = glBankFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
                
                code = glBankFilter.getCode();
                if (code != null) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
            }
            
            glBankConverterTo = new GlBankConverter(filteredGlBanks);
            return filteredGlBanks;
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "completeGlBankTo");
            return null;
        }
    }
    
    public List<GlBank> completeGlBankFrom(String query) {
        try {
            List<GlBank> glBanksList = getGlBankListFrom();
            if (query == null || query.trim().equals("")) {
                
                glBankConverterFrom = new GlBankConverter(glBanksList);
                return glBanksList;
            }
            List<GlBank> filteredGlBanks = new ArrayList<>();
            
            String nameAr;
            Integer code;
            GlBank glBankFilter;
            for (int i = 0; i < getGlBankListFrom().size(); i++) {
                glBankFilter = glBanksList.get(i);
                nameAr = glBankFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
                
                code = glBankFilter.getCode();
                if (code != null) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
            }
            
            glBankConverterFrom = new GlBankConverter(filteredGlBanks);
            return filteredGlBanks;
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "completeGlBankFrom");
            return null;
        }
        
    }
    
    public boolean checkGlBankSimilarityAndPostFlagStatus() {
        try {
            if (GlBankTransactionDetailEntity.getBankFrom().equals(GlBankTransactionDetailEntity.getBankTo())) {
                GlBankTransactionDetailEntity.setBankTo(null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن التحويل الى نفس الحساب"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "checkGlBankSimilarityAndPostFlagStatus");
            return false;
        }
    }
    
    public boolean isGlBankHasAccount() {
        try {
            if (GlBankTransactionDetailEntity.getBankFrom() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "خزنة التحويل غير مربوطة بحساب"));
                return false;
            } else if (GlBankTransactionDetailEntity.getBankTo() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "خزنة الاستلام غير مربوطة بحساب"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "isGlBankHasAccount");
            return false;
        }
    }
    
    @Override
    public String getScreenName() {
        try {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (Exception e) {
            saveError(e, "glBankTransactionFromMB", "getScreenName");
            return null;
        }
    }

    /**
     * @return the glBankTransaction
     */
    public GlBankTransaction getGlBankTransaction() {
        return glBankTransaction;
    }

    /**
     * @param glBankTransaction the glBankTransaction to set
     */
    public void setGlBankTransaction(GlBankTransaction glBankTransaction) {
        this.glBankTransaction = glBankTransaction;
    }

    /**
     * @return the glBankTransactionEntity
     */
    public GlBankTransactionEntity getGlBankTransactionEntity() {
        return glBankTransactionEntity;
    }

    /**
     * @param glBankTransactionEntity the glBankTransactionEntity to set
     */
    public void setGlBankTransactionEntity(GlBankTransactionEntity glBankTransactionEntity) {
        this.glBankTransactionEntity = glBankTransactionEntity;
    }

    /**
     * @return the glBankTransactionDetail
     */
    public GlBankTransactionDetail getGlBankTransactionDetail() {
        return glBankTransactionDetail;
    }

    /**
     * @param glBankTransactionDetail the glBankTransactionDetail to set
     */
    public void setGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail) {
        this.glBankTransactionDetail = glBankTransactionDetail;
    }

    /**
     * @return the bankTransactionDetailEntity
     */
    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntity() {
        return GlBankTransactionDetailEntity;
    }

    /**
     * @param glBankTransactionDetailEntity the bankTransactionDetailEntity to
     * set
     */
    public void setGlBankTransactionDetailEntity(GlBankTransactionDetailEntity glBankTransactionDetailEntity) {
        this.GlBankTransactionDetailEntity = glBankTransactionDetailEntity;
    }

    /**
     * @return the glBank
     */
    public GlBank getGlBank() {
        return glBank;
    }

    /**
     * @param glBank the glBank to set
     */
    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    /**
     * @return the glBankListTo
     */
    public List<GlBank> getGlBankListTo() {
        return glBankListTo;
    }

    /**
     * @param glBankListTo the glBankListTo to set
     */
    public void setGlBankListTo(List<GlBank> glBankListTo) {
        this.glBankListTo = glBankListTo;
    }

    /**
     * @return the glBankTransactionId
     */
    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    /**
     * @param glBankTransactionId the glBankTransactionId to set
     */
    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    /**
     * @return the glBankConverterTo
     */
    public GlBankConverter getGlBankConverterTo() {
        return glBankConverterTo;
    }

    /**
     * @param glBankConverterTo the glBankConverterTo to set
     */
    public void setGlBankConverterTo(GlBankConverter glBankConverterTo) {
        this.glBankConverterTo = glBankConverterTo;
    }

    /**
     * @return the currencylist
     */
    public List<Currency> getCurrencylist() {
        return currencylist;
    }

    /**
     * @param currencylist the currencylist to set
     */
    public void setCurrencylist(List<Currency> currencylist) {
        this.currencylist = currencylist;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the glBankEntity
     */
    public GlBankEntity getGlBankEntity() {
        return glBankEntity;
    }

    /**
     * @param glBankEntity the glBankEntity to set
     */
    public void setGlBankEntity(GlBankEntity glBankEntity) {
        this.glBankEntity = glBankEntity;
    }

    /**
     * @return the currencyOperation
     */
    public CurrencyOperation getCurrencyOperation() {
        return currencyOperation;
    }

    /**
     * @param currencyOperation the currencyOperation to set
     */
    public void setCurrencyOperation(CurrencyOperation currencyOperation) {
        this.currencyOperation = currencyOperation;
    }

    /**
     * @return the currencyOperations
     */
    public List<CurrencyOperation> getCurrencyOperations() {
        return currencyOperations;
    }

    /**
     * @param currencyOperations the currencyOperations to set
     */
    public void setCurrencyOperations(List<CurrencyOperation> currencyOperations) {
        this.currencyOperations = currencyOperations;
    }
    
    public List<GlBank> getGlBankListFrom() {
        return glBankListFrom;
    }
    
    public void setGlBankListFrom(List<GlBank> glBankListFrom) {
        this.glBankListFrom = glBankListFrom;
    }
    
    public GlBankConverter getGlBankConverterFrom() {
        return glBankConverterFrom;
    }
    
    public void setGlBankConverterFrom(GlBankConverter glBankConverterFrom) {
        this.glBankConverterFrom = glBankConverterFrom;
    }
    
    public Map<Integer, GlBankTransactionDetail> getInvQuotationDetailList() {
        return invQuotationDetailList;
    }
    
    public void setInvQuotationDetailList(Map<Integer, GlBankTransactionDetail> invQuotationDetailList) {
        this.invQuotationDetailList = invQuotationDetailList;
    }

//    public Boolean getDisableSave() {
//        return disableSave;
//    }
//
//    public void setDisableSave(Boolean disableSave) {
//        this.disableSave = disableSave;
//    }
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

    /**
     * @return the glBankTransactionDetailEntityList
     */
    public List<GlBankTransactionDetailEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    /**
     * @param glBankTransactionDetailEntityList the
     * glBankTransactionDetailEntityList to set
     */
    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the balanceReportSearchBean
     */
    public BankBalanceReportSearchBean getBalanceReportSearchBean() {
        if(balanceReportSearchBean == null){
        balanceReportSearchBean = new BankBalanceReportSearchBean();
        }
        return balanceReportSearchBean;
    }

    /**
     * @param balanceReportSearchBean the balanceReportSearchBean to set
     */
    public void setBalanceReportSearchBean(BankBalanceReportSearchBean balanceReportSearchBean) {
        this.balanceReportSearchBean = balanceReportSearchBean;
    }

    /**
     * @param bankCash the bankCash to set
     */
    public void setBankCash(BigDecimal bankCash) {
        this.bankCash = bankCash;
    }

    /**
     * @return the bankCash
     */
    public BigDecimal getBankCash() {
        if (bankCash == null){
        bankCash = BigDecimal.ZERO ;
        }
        return bankCash;
    }
    
}
