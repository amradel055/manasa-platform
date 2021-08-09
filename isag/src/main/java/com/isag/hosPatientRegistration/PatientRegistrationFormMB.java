/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.hosPatientRegistration;

import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.PatientRegistrationService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.CurrencyConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.converter.HosPatientRegistrationConvertor;
import com.isag.converter.InvDelegatorConvertor;

import com.isag.converter.SymbolConverter;
import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.HosPatientRegistration;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.Symbol;
import com.isag.entiy.HosPatientRegistrationEntity;

import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang.NumberUtils;

/**
 *
 * @author hq002
 */
@Named(value = "patientRegistrationFormMB")
@ViewScoped
public class PatientRegistrationFormMB extends BaseFormBean implements Serializable {

    private HosPatientRegistration hosPatientRegistration;
    private Integer patientRegistrationId;
    private boolean company;
    private boolean patient;

    private List<Currency> currencylist;
    private Currency currency;
    private List<Symbol> patientRegistrationTypeList;
    private Symbol patientRegistrationType;
    private List<Symbol> regionList;
    private List<Symbol> nationalitiesList;
    private Symbol region;
    private List<Symbol> patientTypeList;
    private Symbol patientType;
    private List<InventoryDelegator> inventoryDelegatorList;
    private InventoryDelegator inventoryDelegator;
    private List<HosPatientRegistration> mainCompanyList;
    private HosPatientRegistration mainCompany;
    private List<GlAccount> accountIdList;
    private GlAccount accountId;
    private HosPatientRegistrationEntity hosPatientRegistrationEntity;
    private boolean activity;
    private Boolean issales;
    private InvDelegatorConvertor salesConverter;
    private SymbolConverter patientTypeConverter;
    private SymbolConverter regionConverter;
    private SymbolConverter nationalityConverter;
    private GlaccountConverter accountConverter;
    private HosPatientRegistrationConvertor companyConvertor;
    private CurrencyConverter currencyConverter;
    private List<InventoryDelegator> salesPersonsList;

    private HttpServletRequest request;
    private String url;
    private boolean saveValidate;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private PatientRegistrationService patientRegistrationService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private InvDelegatorService ineventoryDelegatorService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private InvDelegatorService invDelegatorService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();

            load();
            checkVisability();
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));

            salesPersonsList = new ArrayList<>();

            hosPatientRegistration = new HosPatientRegistration();
            setHosPatientRegistrationEntity(new HosPatientRegistrationEntity());
            currencylist = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
            currencyConverter = new CurrencyConverter(currencylist);

            //  patientRegistrationTypeList = symbolService.getOrganizationSiteType(getUserData().getCompany().getId());
            regionList = symbolService.getRegion(getUserData().getCompany().getId());
            regionConverter = new SymbolConverter(regionList);

            nationalitiesList = symbolService.getAllNationalitiesByCompanyId(getUserData().getCompany().getId());
            nationalityConverter = new SymbolConverter(nationalitiesList);

            //  patientTypeList = symbolService.getSupplierType(getUserData().getCompany().getId());
            patientTypeConverter = new SymbolConverter(patientTypeList);

            inventoryDelegatorList = ineventoryDelegatorService.getDelegatorsByBranch(getUserData().getUserBranch().getId());
            mainCompanyList = patientRegistrationService.getAllHosPatientRegistrationByBranchId(getUserData().getUserBranch().getId(), 1);

            setCompanyConvertor(new HosPatientRegistrationConvertor(mainCompanyList));

            accountIdList = getAccountService().getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            accountConverter = new GlaccountConverter(accountIdList);

            if (url.contains("PatientRegistrationForm")) {
                salesPersonsList = invDelegatorService.getSalesByBranch(getUserData().getUserBranch().getId());
            }
            salesConverter = new InvDelegatorConvertor(salesPersonsList);

            setCompany(true);
            setPatient(false);

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setHosPatientRegistrationDataEmpty();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    patientRegistrationId = (Integer) context.getSessionMap().get("hosPatientRegistrationSelected");
                    setEditedPatientRegistrationValues(patientRegistrationId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "load");
        }
    }

    public void setHosPatientRegistrationDataEmpty() {
        try {
            setHosPatientRegistrationEntity(new HosPatientRegistrationEntity());
            if (currencylist != null && !currencylist.isEmpty()) {
                getHosPatientRegistrationEntity().setCurrencyId(currencylist.get(0));
            } else {
                errorMessage("لا توجد عملات");
            }
            if (url.contains("company")) {
                getHosPatientRegistrationEntity().setType(1);
            } else {
                getHosPatientRegistrationEntity().setType(0);
            }
            getHosPatientRegistrationEntity().setActive(true);
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "setHosPatientRegistrationDataEmpty");
        }
    }

    public void setEditedPatientRegistrationValues(Integer bId) {
        try {
            hosPatientRegistration = patientRegistrationService.findPatientRegistrationById(bId);
            validateInputsForEdit();
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "setEditedPatientRegistrationValues");
        }
    }

    public void validateCode() {
        try {

            hosPatientRegistration.setCode(getHosPatientRegistrationEntity().getCode());
            hosPatientRegistration.setBranchId(getUserData().getUserBranch());
            hosPatientRegistration.setType(getHosPatientRegistrationEntity().getType());
            hosPatientRegistration.setId(patientRegistrationId);

            if (patientRegistrationService.checkCodeExistence(hosPatientRegistration)) {
                errorMessage(getUserData().getUserDDs().get("UNIQE_SERIAL"));
                hosPatientRegistrationEntity.setCode(new String());
                saveValidate = false;
            } else {
                saveValidate = true;
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "validateCode");
        }
    }

    public void checkVisability() {
        try {
            if (getHosPatientRegistrationEntity().getType() == 1) {
                setCompany(true);
                setPatient(false);
            } else if (getHosPatientRegistrationEntity().getType() == 0) {
                setCompany(false);
                setPatient(true);
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "checkVisability");
        }
    }

    public void validateInputsForEdit() {
        try {
            getHosPatientRegistrationEntity().setId(hosPatientRegistration.getId());

            getHosPatientRegistrationEntity().setAccountId(hosPatientRegistration.getAccountId());

            getHosPatientRegistrationEntity().setCurrencyId(hosPatientRegistration.getCurrencyId());

            getHosPatientRegistrationEntity().setRegionId(hosPatientRegistration.getRegionId());

            /*  getHosPatientRegistrationEntity().setSupplierType(hosPatientRegistration.getSupplierType());

             if (hosPatientRegistration.getDelegatorId() != null) {
             getHosPatientRegistrationEntity().setDelegatorId(hosPatientRegistration.getDelegatorId());
             } else {
             getHosPatientRegistrationEntity().setDelegatorId(null);
             }

             hosPatientRegistrationEntity.setSalesPerson(hosPatientRegistration.getDelegatorId());
             getHosPatientRegistrationEntity().setParent(hosPatientRegistration.getParent());
             */
            if (hosPatientRegistration.getCode() != null) {
                getHosPatientRegistrationEntity().setCode(hosPatientRegistration.getCode());
            } else {
                getHosPatientRegistrationEntity().setCode(null);
            }
            if (hosPatientRegistration.getAddress() != null) {
                getHosPatientRegistrationEntity().setAddress(hosPatientRegistration.getAddress());
            } else {
                getHosPatientRegistrationEntity().setAddress(null);
            }
            /* if (hosPatientRegistration.getBalanceLimit() != null) {
             getHosPatientRegistrationEntity().setBalanceLimit(hosPatientRegistration.getBalanceLimit());
             } else {
             getHosPatientRegistrationEntity().setBalanceLimit(null);
             }
             if (hosPatientRegistration.getContactPerson() != null) {
             getHosPatientRegistrationEntity().setContactPerson(hosPatientRegistration.getContactPerson());
             } else {
             getHosPatientRegistrationEntity().setContactPerson(null);
             }*/
            if (hosPatientRegistration.getEmail() != null) {
                getHosPatientRegistrationEntity().setEmail(hosPatientRegistration.getEmail());
            } else {
                getHosPatientRegistrationEntity().setEmail(null);
            }
            if (hosPatientRegistration.getPhone() != null) {
                getHosPatientRegistrationEntity().setPhone(hosPatientRegistration.getPhone());
            } else {
                getHosPatientRegistrationEntity().setPhone(null);
            }
            if (hosPatientRegistration.getPhone2() != null) {
                getHosPatientRegistrationEntity().setPhone2(hosPatientRegistration.getPhone2());
            } else {
                getHosPatientRegistrationEntity().setPhone2(null);
            }
            if (hosPatientRegistration.getFax() != null) {
                getHosPatientRegistrationEntity().setFax(hosPatientRegistration.getFax());
            } else {
                getHosPatientRegistrationEntity().setFax(null);
            }
            if (hosPatientRegistration.getMobile() != null) {
                getHosPatientRegistrationEntity().setMobile(hosPatientRegistration.getMobile());
            } else {
                getHosPatientRegistrationEntity().setMobile(null);
            }
            if (hosPatientRegistration.getName() != null) {
                getHosPatientRegistrationEntity().setName(hosPatientRegistration.getName());
            } else {
                getHosPatientRegistrationEntity().setName(null);
            }

            /*  if (hosPatientRegistration.getOpenBalanceCredit() != null) {
             getHosPatientRegistrationEntity().setOpenBalanceCredit(hosPatientRegistration.getOpenBalanceCredit());
             } else {
             getHosPatientRegistrationEntity().setOpenBalanceCredit(null);
             }
             if (hosPatientRegistration.getOpenBalanceDebit() != null) {
             getHosPatientRegistrationEntity().setOpenBalanceDebit(hosPatientRegistration.getOpenBalanceDebit());
             } else {
             getHosPatientRegistrationEntity().setOpenBalanceDebit(null);
             }*/
            if (hosPatientRegistration.getPostBox() != null) {
                getHosPatientRegistrationEntity().setPostBox(hosPatientRegistration.getPostBox());
            } else {
                getHosPatientRegistrationEntity().setPostBox(null);
            }
            if (hosPatientRegistration.getZip() != null) {
                getHosPatientRegistrationEntity().setZip(hosPatientRegistration.getZip());
            } else {
                getHosPatientRegistrationEntity().setZip(null);
            }
            if (hosPatientRegistration.getRemark() != null) {
                getHosPatientRegistrationEntity().setRemark(hosPatientRegistration.getRemark());
            } else {
                getHosPatientRegistrationEntity().setRemark(null);
            }
            if (hosPatientRegistration.getActive() == 1) {
                getHosPatientRegistrationEntity().setActive(true);
            } else {
                getHosPatientRegistrationEntity().setActive(false);
            }

            getHosPatientRegistrationEntity().setType(hosPatientRegistration.getType());

            getHosPatientRegistrationEntity().setDiscountRatio(hosPatientRegistration.getDiscountRatio() != null ? hosPatientRegistration.getDiscountRatio() : BigDecimal.ZERO);
            getHosPatientRegistrationEntity().setBearRatio(hosPatientRegistration.getBearRatio() != null ? hosPatientRegistration.getBearRatio() : BigDecimal.ZERO);
            getHosPatientRegistrationEntity().setNationalId(hosPatientRegistration.getNationalId());
            getHosPatientRegistrationEntity().setNationalityId(hosPatientRegistration.getNationalityId() != null ? hosPatientRegistration.getNationalityId() : null);
            getHosPatientRegistrationEntity().setBirthDay(hosPatientRegistration.getBirthday());
            getHosPatientRegistrationEntity().setPatientHistory(hosPatientRegistration.getPatientHistory());

            getHosPatientRegistrationEntity().setAge(hosPatientRegistration.getAge());
            getHosPatientRegistrationEntity().setGender(hosPatientRegistration.getGender());
            getHosPatientRegistrationEntity().setHeight(hosPatientRegistration.getHeight());
            getHosPatientRegistrationEntity().setWeight(hosPatientRegistration.getWeight());

            getHosPatientRegistrationEntity().setParent(hosPatientRegistration.getParent());
            if (hosPatientRegistration.getType() == 1) {
                setCompany(true);
                setPatient(false);

            } else if (hosPatientRegistration.getType() == 0) {
                setCompany(false);
                setPatient(true);

            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "validateInputsForEdit");
        }
    }

    public void validateInputsForSave() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            saveValidate = true;
            hosPatientRegistration.setId(getHosPatientRegistrationEntity().getId());
            hosPatientRegistration.setAccountId(getHosPatientRegistrationEntity().getAccountId());

            hosPatientRegistration.setCurrencyId(getHosPatientRegistrationEntity().getCurrencyId());

            hosPatientRegistration.setRegionId(getHosPatientRegistrationEntity().getRegionId());
            //hosPatientRegistration.setSupplierType(getHosPatientRegistrationEntity().getSupplierType());
            //hosPatientRegistration.setDelegatorId(getHosPatientRegistrationEntity().getSalesPerson());
            //hosPatientRegistration.setParent(getHosPatientRegistrationEntity().getParent());
            hosPatientRegistration.setActive(getHosPatientRegistrationEntity().isActive() ? 1 : 0);
            hosPatientRegistration.setType(getHosPatientRegistrationEntity().getType() != null ? getHosPatientRegistrationEntity().getType() : 0);
            hosPatientRegistration.setAddress(getHosPatientRegistrationEntity().getAddress());
            //hosPatientRegistration.setBalanceLimit(getHosPatientRegistrationEntity().getBalanceLimit());
            //hosPatientRegistration.setContactPerson(getHosPatientRegistrationEntity().getContactPerson());
            hosPatientRegistration.setEmail(getHosPatientRegistrationEntity().getEmail());
            hosPatientRegistration.setFax(getHosPatientRegistrationEntity().getFax());
            hosPatientRegistration.setMobile(getHosPatientRegistrationEntity().getMobile());
            // hosPatientRegistration.setOpenBalanceCredit(getHosPatientRegistrationEntity().getOpenBalanceCredit());
            // hosPatientRegistration.setOpenBalanceDebit(getHosPatientRegistrationEntity().getOpenBalanceDebit());
            hosPatientRegistration.setRemark(getHosPatientRegistrationEntity().getRemark());
            hosPatientRegistration.setPhone(getHosPatientRegistrationEntity().getPhone());
            hosPatientRegistration.setPhone2(getHosPatientRegistrationEntity().getPhone2());
            hosPatientRegistration.setPostBox(getHosPatientRegistrationEntity().getPostBox());
            hosPatientRegistration.setName(getHosPatientRegistrationEntity().getName());
            hosPatientRegistration.setCode(getHosPatientRegistrationEntity().getCode());
            hosPatientRegistration.setZip(getHosPatientRegistrationEntity().getZip());

            hosPatientRegistration.setDiscountRatio(getHosPatientRegistrationEntity().getDiscountRatio() != null ? getHosPatientRegistrationEntity().getDiscountRatio() : BigDecimal.ZERO);
            hosPatientRegistration.setBearRatio(getHosPatientRegistrationEntity().getBearRatio() != null ? getHosPatientRegistrationEntity().getBearRatio() : BigDecimal.ZERO);
            hosPatientRegistration.setNationalId(getHosPatientRegistrationEntity().getNationalId());
            hosPatientRegistration.setNationalityId(getHosPatientRegistrationEntity().getNationalityId());
            hosPatientRegistration.setBirthday(getHosPatientRegistrationEntity().getBirthDay());
            hosPatientRegistration.setPatientHistory(getHosPatientRegistrationEntity().getPatientHistory());

            hosPatientRegistration.setAge(getHosPatientRegistrationEntity().getAge());
            hosPatientRegistration.setGender(getHosPatientRegistrationEntity().getGender());
            hosPatientRegistration.setHeight(getHosPatientRegistrationEntity().getHeight());
            hosPatientRegistration.setWeight(getHosPatientRegistrationEntity().getWeight());

            hosPatientRegistration.setParent(getHosPatientRegistrationEntity().getParent());
            if (company && (hosPatientRegistration.getDiscountRatio().compareTo(BigDecimal.ZERO) == 0 || hosPatientRegistration.getDiscountRatio().compareTo(BigDecimal.ZERO) == -1)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال قيم خصم صحيحة"));
                saveValidate = false;
            }
            /*if (!company && (hosPatientRegistration.getBearRatio().compareTo(BigDecimal.ZERO) == 0 || hosPatientRegistration.getBearRatio().compareTo(BigDecimal.ZERO) == -1)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال قيم تحمل صحيحة"));
             saveValidate = false;
             }*/
           /* if (!company && (hosPatientRegistration.getPhone().isEmpty() || !isNumeric(hosPatientRegistration.getPhone()))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال رقم تليفون صحيح"));
                saveValidate = false;
            } */
            if (hosPatientRegistration.getCode() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال الرمز"));
                saveValidate = false;
            }
            if (hosPatientRegistration.getName() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال الاسم"));
                saveValidate = false;
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "validateInputsForSave");
        }
    }

    @Override
    public String save() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            hosPatientRegistration.setCompanyId(getUserData().getCompany());
            hosPatientRegistration.setBranchId(getUserData().getUserBranch());
            validateInputsForSave();
            if (hosPatientRegistration.getId() == null) {
                hosPatientRegistration.setCreatedBy(getUserData().getUser());
                hosPatientRegistration.setCreationDate(new Date());
            } else {
                hosPatientRegistration.setModificationDate(new Date());
                hosPatientRegistration.setModifiedBy(getUserData().getUser());
            }
            if (saveValidate) {
                try {
                    if (hosPatientRegistration.getId() == null) {
                        patientRegistrationService.addPatientRegistration(hosPatientRegistration);
                    } else {
                        patientRegistrationService.updatePatientRegistration(hosPatientRegistration);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), "تم الحفظ بنجاح"));
                } catch (Exception e) {
                }
                exit();
            }

            return "";
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "save");
            return null;
        }
    }

    public static boolean isNumeric(String str) {

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();

    }

    public void updateAge() throws ParseException {
        try {
            if (getHosPatientRegistrationEntity() != null && getHosPatientRegistrationEntity().getBirthDay() != null) {

                Calendar c = Calendar.getInstance();
                c.setTime(getHosPatientRegistrationEntity().getBirthDay());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                LocalDate l1 = LocalDate.of(year, month, date);
                LocalDate now1 = LocalDate.now();
                Period diff1 = Period.between(l1, now1);
                getHosPatientRegistrationEntity().setAge(diff1.getYears());
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "updateAge");
        }
    }

    public String exit() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../PatientRegistration/PatientRegistration.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "exit");
            return null;
        }
    }

    public List<InventoryDelegator> completeSalesPerson(String query) {
        try {
            List<InventoryDelegator> centersList = salesPersonsList;
            if (query == null || query.trim().equals("")) {

                salesConverter = new InvDelegatorConvertor(centersList);
                return centersList;
            }
            List<InventoryDelegator> filteredCostCenters = new ArrayList<>();

            String nameAr;
            String code;
            InventoryDelegator costCenterFilter;
            for (int i = 0; i < salesPersonsList.size(); i++) {
                costCenterFilter = centersList.get(i);
                nameAr = costCenterFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }

                code = costCenterFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }
            }

            salesConverter = new InvDelegatorConvertor(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "completeSalesPerson");
            return null;
        }
    }

    public List<Symbol> completeSupplierType(String query) {
        try {
            List<Symbol> symbols = patientTypeList;
            if (query == null || query.trim().equals("")) {

                patientTypeConverter = new SymbolConverter(symbols);
                return symbols;
            }
            List<Symbol> filteredSymboles = new ArrayList<>();

            String nameAr;
            Integer serial;
            Symbol symbolFilter;
            for (int i = 0; i < patientTypeList.size(); i++) {
                symbolFilter = symbols.get(i);
                nameAr = symbolFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSymboles.contains(symbolFilter)) {
                        filteredSymboles.add(symbolFilter);
                    }
                }

                serial = symbolFilter.getSerial();
                if (serial != null && String.valueOf(serial).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSymboles.contains(symbolFilter)) {
                        filteredSymboles.add(symbolFilter);
                    }
                }
            }

            patientTypeConverter = new SymbolConverter(filteredSymboles);
            return filteredSymboles;
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "completeSupplierType");
            return null;
        }
    }

    public List<HosPatientRegistration> completeCompanys(String query) {
        try {
            List<HosPatientRegistration> patientRegistrationList = mainCompanyList;
            if (query == null || query.trim().equals("")) {

                setCompanyConvertor(new HosPatientRegistrationConvertor(patientRegistrationList));
                return patientRegistrationList;
            }
            List<HosPatientRegistration> filteredCompanys = new ArrayList<>();

            String nameAr;
            String code;
            HosPatientRegistration companyFilter;
            for (int i = 0; i < mainCompanyList.size(); i++) {
                companyFilter = patientRegistrationList.get(i);
                nameAr = companyFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCompanys.contains(companyFilter)) {
                        filteredCompanys.add(companyFilter);
                    }
                }

                code = companyFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCompanys.contains(companyFilter)) {
                        filteredCompanys.add(companyFilter);
                    }
                }
            }

            setCompanyConvertor(new HosPatientRegistrationConvertor(filteredCompanys));
            return filteredCompanys;
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "completeCompanys");
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
            saveError(e, "PatientRegistrationFormMB", "completeCurrency");
            return null;
        }
    }

    public List<Symbol> completeRegion(String query) {
        try {
            List<Symbol> regionListFilter = regionList;
            if (query == null || query.trim().equals("")) {

                regionConverter = new SymbolConverter(regionListFilter);
                return regionListFilter;
            }
            List<Symbol> regionListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            Symbol regionFilter;
            for (int i = 0; i < regionList.size(); i++) {
                regionFilter = regionListFilter.get(i);
                nameAr = regionFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }

                code = regionFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }
            }

            regionConverter = new SymbolConverter(regionListFiltered);
            return regionListFiltered;
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "completeRegion");
            return null;
        }
    }

    public List<Symbol> completeNationality(String query) {
        try {
            List<Symbol> regionListFilter = nationalitiesList;
            if (query == null || query.trim().equals("")) {

                nationalityConverter = new SymbolConverter(regionListFilter);
                return regionListFilter;
            }
            List<Symbol> regionListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            Symbol regionFilter;
            for (int i = 0; i < nationalitiesList.size(); i++) {
                regionFilter = regionListFilter.get(i);
                nameAr = regionFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }

                code = regionFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }
            }

            nationalityConverter = new SymbolConverter(regionListFiltered);
            return regionListFiltered;
        } catch (Exception e) {
            saveError(e, "PatientRegistrationFormMB", "completeNationality");
            return null;
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = accountIdList;
            if (query == null || query.trim().equals("")) {
                accountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < accountIdList.size(); i++) {
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
            saveError(e, "PatientRegistrationFormMB", "completeGlAccount");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the hosPatientRegistration
     */
    public HosPatientRegistration getHosPatientRegistration() {
        return hosPatientRegistration;
    }

    /**
     * @param hosPatientRegistration the hosPatientRegistration to set
     */
    public void setHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        this.hosPatientRegistration = hosPatientRegistration;
    }

    /**
     * @return the company
     */
    public boolean isCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(boolean company) {
        this.company = company;
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
     * @return the patientRegistrationTypeList
     */
    public List<Symbol> getOrganizationSiteTypeList() {
        return patientRegistrationTypeList;
    }

    /**
     * @param patientRegistrationTypeList the patientRegistrationTypeList to set
     */
    public void setOrganizationSiteTypeList(List<Symbol> patientRegistrationTypeList) {
        this.patientRegistrationTypeList = patientRegistrationTypeList;
    }

    /**
     * @return the patientRegistrationType
     */
    public Symbol getOrganizationSiteType() {
        return patientRegistrationType;
    }

    /**
     * @param patientRegistrationType the patientRegistrationType to set
     */
    public void setOrganizationSiteType(Symbol patientRegistrationType) {
        this.patientRegistrationType = patientRegistrationType;
    }

    /**
     * @return the regionList
     */
    public List<Symbol> getRegionList() {
        return regionList;
    }

    /**
     * @param regionList the regionList to set
     */
    public void setRegionList(List<Symbol> regionList) {
        this.regionList = regionList;
    }

    /**
     * @return the region
     */
    public Symbol getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(Symbol region) {
        this.region = region;
    }

    /**
     * @return the inventoryDelegatorList
     */
    public List<InventoryDelegator> getInventoryDelegatorList() {
        return inventoryDelegatorList;
    }

    /**
     * @param inventoryDelegatorList the inventoryDelegatorList to set
     */
    public void setInventoryDelegatorList(List<InventoryDelegator> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    /**
     * @return the inventoryDelegator
     */
    public InventoryDelegator getInventoryDelegator() {
        return inventoryDelegator;
    }

    /**
     * @param inventoryDelegator the inventoryDelegator to set
     */
    public void setInventoryDelegator(InventoryDelegator inventoryDelegator) {
        this.inventoryDelegator = inventoryDelegator;
    }

    /**
     * @return the mainCompanyList
     */
    public List<HosPatientRegistration> getMainCompanyList() {
        return mainCompanyList;
    }

    /**
     * @param mainCompanyList the mainCompanyList to set
     */
    public void setMainCompanyList(List<HosPatientRegistration> mainCompanyList) {
        this.mainCompanyList = mainCompanyList;
    }

    /**
     * @return the mainCompany
     */
    public HosPatientRegistration getMainCompany() {
        return mainCompany;
    }

    /**
     * @param mainCompany the mainCompany to set
     */
    public void setMainCompany(HosPatientRegistration mainCompany) {
        this.mainCompany = mainCompany;
    }

    /**
     * @return the accountIdList
     */
    public List<GlAccount> getAccountIdList() {
        return accountIdList;
    }

    /**
     * @param accountIdList the accountIdList to set
     */
    public void setAccountIdList(List<GlAccount> accountIdList) {
        this.accountIdList = accountIdList;
    }

    /**
     * @return the accountId
     */
    public GlAccount getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the currencyService
     */
    public CurrencyService getCurrencyService() {
        return currencyService;
    }

    /**
     * @param currencyService the currencyService to set
     */
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * @return the symbolService
     */
    public SymbolService getSymbolService() {
        return symbolService;
    }

    /**
     * @param symbolService the symbolService to set
     */
    public void setSymbolService(SymbolService symbolService) {
        this.symbolService = symbolService;
    }

    /**
     * @return the ineventoryDelegatorService
     */
    public InvDelegatorService getIneventoryDelegatorService() {
        return ineventoryDelegatorService;
    }

    /**
     * @param ineventoryDelegatorService the ineventoryDelegatorService to set
     */
    public void setIneventoryDelegatorService(InvDelegatorService ineventoryDelegatorService) {
        this.ineventoryDelegatorService = ineventoryDelegatorService;
    }

    /**
     * @return the accountService
     */
    public GlAccountService getAccountService() {
        return accountService;
    }

    /**
     * @param accountService the accountService to set
     */
    public void setAccountService(GlAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @return the patientTypeList
     */
    public List<Symbol> getSupplierTypeList() {
        return patientTypeList;
    }

    /**
     * @param patientTypeList the patientTypeList to set
     */
    public void setSupplierTypeList(List<Symbol> patientTypeList) {
        this.patientTypeList = patientTypeList;
    }

    /**
     * @return the patientType
     */
    public Symbol getSupplierType() {
        return patientType;
    }

    /**
     * @param patientType the patientType to set
     */
    public void setSupplierType(Symbol patientType) {
        this.patientType = patientType;
    }

    /**
     * @return the activity
     */
    public boolean isActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    /**
     * @return the patientRegistrationId
     */
    public Integer getOrganizationSiteId() {
        return patientRegistrationId;
    }

    /**
     * @param patientRegistrationId the patientRegistrationId to set
     */
    public void setOrganizationSiteId(Integer patientRegistrationId) {
        this.patientRegistrationId = patientRegistrationId;
    }

    public InvDelegatorConvertor getSalesConverter() {
        return salesConverter;
    }

    public void setSalesConverter(InvDelegatorConvertor salesConverter) {
        this.salesConverter = salesConverter;
    }

    public List<InventoryDelegator> getSalesPersonsList() {
        return salesPersonsList;
    }

    public void setSalesPersonsList(List<InventoryDelegator> salesPersonsList) {
        this.salesPersonsList = salesPersonsList;
    }

    public SymbolConverter getSupplierTypeConverter() {
        return patientTypeConverter;
    }

    public void setSupplierTypeConverter(SymbolConverter patientTypeConverter) {
        this.patientTypeConverter = patientTypeConverter;
    }

    public SymbolConverter getRegionConverter() {
        return regionConverter;
    }

    public void setRegionConverter(SymbolConverter regionConverter) {
        this.regionConverter = regionConverter;
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

    /**
     * @return the issales
     */
    public Boolean getIssales() {
        return issales;
    }

    /**
     * @param issales the issales to set
     */
    public void setIssales(Boolean issales) {
        this.issales = issales;
    }

    /**
     * @return the patient
     */
    public boolean isPatient() {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(boolean patient) {
        this.patient = patient;
    }

    /**
     * @return the companyConvertor
     */
    public HosPatientRegistrationConvertor getCompanyConvertor() {
        return companyConvertor;
    }

    /**
     * @param companyConvertor the companyConvertor to set
     */
    public void setCompanyConvertor(HosPatientRegistrationConvertor companyConvertor) {
        this.companyConvertor = companyConvertor;
    }

    /**
     * @return the hosPatientRegistrationEntity
     */
    public HosPatientRegistrationEntity getHosPatientRegistrationEntity() {
        return hosPatientRegistrationEntity;
    }

    /**
     * @param hosPatientRegistrationEntity the hosPatientRegistrationEntity to
     * set
     */
    public void setHosPatientRegistrationEntity(HosPatientRegistrationEntity hosPatientRegistrationEntity) {
        this.hosPatientRegistrationEntity = hosPatientRegistrationEntity;
    }

    /**
     * @return the saveValidate
     */
    public boolean isSaveValidate() {
        return saveValidate;
    }

    /**
     * @param saveValidate the saveValidate to set
     */
    public void setSaveValidate(boolean saveValidate) {
        this.saveValidate = saveValidate;
    }

    /**
     * @return the nationalitiesList
     */
    public List<Symbol> getNationalitiesList() {
        return nationalitiesList;
    }

    /**
     * @param nationalitiesList the nationalitiesList to set
     */
    public void setNationalitiesList(List<Symbol> nationalitiesList) {
        this.nationalitiesList = nationalitiesList;
    }

    /**
     * @return the nationalityConverter
     */
    public SymbolConverter getNationalityConverter() {
        return nationalityConverter;
    }

    /**
     * @param nationalityConverter the nationalityConverter to set
     */
    public void setNationalityConverter(SymbolConverter nationalityConverter) {
        this.nationalityConverter = nationalityConverter;
    }

}
