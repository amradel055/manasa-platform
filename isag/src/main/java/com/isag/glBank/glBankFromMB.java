/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.glBank;

import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlBankTransactionDetailsService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.converter.CurrencyConverter;
import com.isag.converter.GlBankConverter;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.dto.GlBankDTO;
import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBank;
import com.isag.entiy.GlBankEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "glBankFromMB")
@ViewScoped
public class glBankFromMB extends BaseFormBean {

    private GlBankConverter bankConverter;
    private GlBankDTOConverter glBankConverter;

    private List<GlBankDTO> glBankList;
    private GlBankDTO glBankListSelected;

    private GlBank glBank;
    private Integer glBankId;
    private GlBankEntity glBankEntity;

    private Integer companyId;
    private Integer branchId;
    ExternalContext context;

    // Lists
    private List<Currency> currencylist;
    private List<GlAccount> glAccountList;

    private GlaccountConverter accountConverter;
    private CurrencyConverter currencyConverter;

    private Map<String, String> userDDs;

    private Boolean showMessage = false;
    private Boolean disableSave = false;

    @EJB
    private CurrencyService currencyService;

    @EJB
    private GlAccountService glAccountService;

    @EJB
    GlBankService glBankService;

    @EJB
    private GlBankTransactionDetailsService bankTransactionDetailsService;
    @EJB
    private IsagUserBankService isagUserBankService;

    @Override
    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            userDDs = getUserData().getUserDDs();

            glBank = new GlBank();
            glBankEntity = new GlBankEntity();

            companyId = getUserData().getCompany().getId();
            branchId = getUserData().getUserBranch().getId();

            load();
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            fillLists();

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetGlBankData();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    glBankId = (Integer) context.getSessionMap().get("glBankSelected");
                    setEditedGlBankValues(glBankId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "load");
        }
    }

    public void fillLists() {
        try {
            currencylist = currencyService.getAllCurrencyByCompanyId(companyId);

            glBankEntity.setCurrencyId((currencylist != null && !currencylist.isEmpty()) ? currencylist.get(0) : null);
            updateGlAccounts();

            currencyConverter = new CurrencyConverter(currencylist);
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "fillLists");
        }
    }

    public List<GlAccount> updateGlAccounts() {
        try {
            if (glBankEntity.getCurrencyId() != null) {
                glAccountList = glAccountService.getGLAccountsActiveByBranchAndCurrencyId(branchId, glBankEntity.getCurrencyId().getId());
                if (glAccountList != null && !glAccountList.isEmpty()) {
                    glBankEntity.setAccountId(glAccountList.get(0));

                    showMessage = disableSave = false;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "هذه العملة غير مربوطة بحساب"));
                    glBankEntity.setAccountId(null);
                    showMessage = disableSave = true;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء خزنة بدون جود عملات"));
                showMessage = disableSave = true;
                return null;
            }

            accountConverter = new GlaccountConverter(glAccountList);
            return glAccountList;
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "updateGlAccounts");
            return null;
        }
    }

    public void setEditedGlBankValues(Integer glBankTrId) {
        try {
            glBank = new GlBank();
            glBank = glBankService.findGlBank(glBankTrId);
            glBankEntity.setId(glBank.getId());
            glBankEntity.setAccountId(glBank.getAccountId() != null ? glBank.getAccountId() : null);
            glBankEntity.setBranchId(glBank.getBranchId());
            glBankEntity.setCode(glBank.getCode() != null ? glBank.getCode() : null);
            glBankEntity.setCurrencyId(glBank.getCurrencyId() != null ? glBank.getCurrencyId() : null);
            glBankEntity.setName(glBank.getName() != null ? glBank.getName() : null);
            glBankEntity.setOpeningBalance(glBank.getOpeninngBalance() != null ? glBank.getOpeninngBalance() : BigDecimal.ZERO);
            glBankEntity.setOpeningBalanceDate(glBank.getDateOpeninngBalance() != null ? glBank.getDateOpeninngBalance() : null);
            if (glBank.getType() != null) {
                glBankEntity.setType(glBank.getType());
            }
            glBankEntity.setBankHavetransaction(bankTransactionDetailsService.checkGlBankIsUsed(glBank.getId(), branchId));
            if (glBankEntity.getBankHavetransaction()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, userDDs.get("WARNING"), "لايمكن تعديل اسم الحساب و العمله الا بعد الغاء سندات الصرف والقبض علي البنك او الخزينه"));
            }
            if (glBank.getClosedglbankId() != null) {
                GlBankDTO glBankDTO = new GlBankDTO();
                glBankDTO.setId(glBank.getClosedglbankId().getId());
                glBankEntity.setClosedglbankId(glBankDTO);
            }

        } catch (Exception e) {
            saveError(e, "glBankFromMB", "setEditedGlBankValues");
        }

    }

    public void resetGlBankData() {
        try {
            glBankEntity = new GlBankEntity();
            glBankEntity.setAccountId((glAccountList != null && !glAccountList.isEmpty()) ? glAccountList.get(0) : null);
            glBankEntity.setCurrencyId((currencylist != null && !currencylist.isEmpty()) ? currencylist.get(0) : null);
            glBankEntity.setType(0);
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "resetGlBankData");
        }
    }

    public String exit() {
        try {
            exit("../glBank/glBankList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "exit");
            return null;
        }
    }

    @Override
    public String save() {
        try {
            validateSave();
            if (!showMessage) {
                glBank.setBranchId(getUserData().getUserBranch());
                glBank.setCompanyId(getUserData().getCompany());
                glBank.setType(glBankEntity.getType() != null ? glBankEntity.getType() : null);
                glBank.setName(glBankEntity.getName());
                glBank.setCode(glBankEntity.getCode());
                glBank.setOpeninngBalance(glBankEntity.getOpeningBalance());
                glBank.setDateOpeninngBalance(glBankEntity.getOpeningBalanceDate());
                glBank.setCurrencyId(glBankEntity.getCurrencyId());
                glBank.setAccountId(glBankEntity.getAccountId());
                if(glBankEntity.getClosedglbankId() != null && glBankEntity.getClosedglbankId().getId() != null){
                    GlBank bank = new GlBank();
                    bank.setId(glBankEntity.getClosedglbankId().getId());
                    glBank.setClosedglbankId(bank);
                }

                if (glBank.getId() == null) {
                    glBank.setCreationDate(new Date());
                    glBank.setCreatedBy(getUserData().getUser());
                } else {
                    glBank.setModificationDate(new Date());
                    glBank.setModifiedBy(getUserData().getUser());
                }
                
//                GlBank bank = glBankService.mapToEntity(glBankListSelected, getUserData().getUser());
//                glBank.setClosedglbankId(bank);
                glBankService.addGlBank(glBank);
                return exit();
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "save");
            return null;
        }
    }

    public void validateSave() {
        try {
            showMessage = false;
            validateName();
            validateCurrency();
            validateAccount();
            validateCode();
            validateOpeningBalance();
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateSave");
        }

    }

    public void validateAccount() {
        try {
            if (glBankEntity.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء خزنة بدون اختيار حساب"));
                showMessage = true;
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateAccount");
        }
    }

    public void validateCurrency() {
        try {
            if (glBankEntity.getCurrencyId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء خزنة بدون اختيار عملة"));
                showMessage = true;
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateCurrency");
        }
    }

    public void validateName() {
        try {
            if (glBankEntity.getName() == null || glBankEntity.getName().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء خزنة بدون اسم"));
                showMessage = true;
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateName");
        }
    }

    public void validateOpeningBalance() {
        try {
            if (glBankEntity.getOpeningBalance() != null && glBankEntity.getOpeningBalance().compareTo(BigDecimal.ZERO) == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن ادخال رصيد بالسالب"));
                showMessage = true;
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateOpeningBalance");
        }
    }

    public void validateCode() {
        try {
            if (glBankEntity.getCode() != null && !glBankEntity.getCode().isEmpty()) {
                List<GlBank> banks = glBankService.getGlBankByBranchIdAndCode(getUserData().getUserBranch().getId(), glBankEntity.getCode(), glBankEntity.getId());
                if (banks != null && !banks.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن تكرار الكود"));
                    showMessage = true;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن انشاء خزنة بدون كود"));
                showMessage = true;
            }
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "validateCode");
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = glAccountList;
            if (query == null || query.trim().equals("")) {
                accountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < glAccountList.size(); i++) {
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
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "completeGlAccount");
            return null;
        }
    }

    public List<Currency> completeCurrency(String query) {
        try {
            List<Currency> currencies = currencylist;
            if (query == null || query.trim().equals("")) {
                currencyConverter = new CurrencyConverter(currencies);
                return currencies;
            }
            List<Currency> filteredCurrency = new ArrayList<>();

            String nameAr;
            String code;
            Currency currency;
            for (int i = 0; i < currencylist.size(); i++) {
                currency = currencies.get(i);
                nameAr = currency.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrency.contains(currency)) {
                        filteredCurrency.add(currency);
                    }
                }

                code = currency.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrency.contains(currency)) {
                        filteredCurrency.add(currency);
                    }
                }
            }

            currencyConverter = new CurrencyConverter(filteredCurrency);
            return filteredCurrency;
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "completeCurrency");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public GlBank getGlBank() {
        return glBank;
    }

    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    public Integer getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(Integer glBankId) {
        this.glBankId = glBankId;
    }

    public GlBankEntity getGlBankEntity() {
        return glBankEntity;
    }

    public void setGlBankEntity(GlBankEntity glBankEntity) {
        this.glBankEntity = glBankEntity;
    }

    public List<Currency> getCurrencylist() {
        return currencylist;
    }

    public void setCurrencylist(List<Currency> currencylist) {
        this.currencylist = currencylist;
    }

    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }

    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public Boolean getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(Boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Map<String, String> getUserDDs() {
        return userDDs;
    }

    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }

    public Boolean getDisableSave() {
        return disableSave;
    }

    public void setDisableSave(Boolean disableSave) {
        this.disableSave = disableSave;
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
     * @return the glBankList
     */
    public List<GlBankDTO> getGlBankList() {
        glBankList = getUserData().getGlBankDTOList();
        glBankConverter = new GlBankDTOConverter(glBankList);
        return glBankList;

    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
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
     * @return the glBankListSelected
     */
    public GlBankDTO getGlBankListSelected() {
        return glBankListSelected;
    }

    /**
     * @param glBankListSelected the glBankListSelected to set
     */
    public void setGlBankListSelected(GlBankDTO glBankListSelected) {
        this.glBankListSelected = glBankListSelected;
    }
}
