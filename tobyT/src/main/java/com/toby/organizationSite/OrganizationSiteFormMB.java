/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.OfferCompanyService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.CurrencyConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.OfferCompanyDTOConverter;
import com.toby.converter.SymbolConverter;
import com.toby.dto.OfferCompanyDTO;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.OfferCompany;
import com.toby.entity.Symbol;
import com.toby.entiy.InvOrganizationSiteEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq002
 */
@Named(value = "organizationSiteFormMB")
@ViewScoped
public class OrganizationSiteFormMB extends BaseFormBean implements Serializable {

    private InvOrganizationSite invOrganizationSite;
    private Integer organizationSiteId;
    private boolean customer;
    private boolean contractor;
    private boolean supplier;
    private boolean supplierAndCustomer;
    private List<Currency> currencylist;
    private Currency currency;
    private List<Symbol> organizationSiteTypeList;
    private Symbol organizationSiteType;
    private List<Symbol> regionList;
    private List<Symbol> workTypeList;
    private List<Symbol> storeRegionList;
    private Symbol region;
    private List<Symbol> supplierTypeList;
    private List<Symbol> contractorTypeList;
    private Symbol supplierType;
    private List<InventoryDelegator> inventoryDelegatorList;
    private InventoryDelegator inventoryDelegator;
    private List<InvOrganizationSite> mainCustomerList;
    private InvOrganizationSite mainCustomer;
    private List<OfferCompanyDTO> offerCompanyDTOList;
    private OfferCompanyDTO offerCompanyDTOSelected;
    private List<GlAccount> accountIdList;
    private GlAccount accountId;
    private InvOrganizationSiteEntity invOrganizationSiteEntity;
    private boolean activity;
    private Boolean issales;
    private InvDelegatorConvertor salesConverter;
    private SymbolConverter supplierTypeConverter;
    private SymbolConverter contractorTypeConverter;
    private SymbolConverter regionConverter;
    private SymbolConverter workTypeConverter;
    private SymbolConverter storeRegionConverter;
    private GlaccountConverter accountConverter;
    private InvOrganizationSiteConverter customerConvertor;
    private CurrencyConverter currencyConverter;
    private OfferCompanyDTOConverter offerCompanyDTOConverter;
    private List<InventoryDelegator> salesPersonsList;
    private Boolean openBalanceDebit = Boolean.FALSE;
    private Boolean openBalanceCredit = Boolean.FALSE;
    private Boolean openBalanceDate = Boolean.FALSE;

    private HttpServletRequest request;
    private String url;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private InvDelegatorService ineventoryDelegatorService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private OfferCompanyService offerCompanyService;

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
            saveError(e, "OrganizationSiteFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));

            salesPersonsList = new ArrayList<>();

            invOrganizationSite = new InvOrganizationSite();
            setInvOrganizationSiteEntity(new InvOrganizationSiteEntity());
            currencylist = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
            currencyConverter = new CurrencyConverter(currencylist);

            workTypeList = symbolService.getAllworkTypeIdByCompanyId(getUserData().getCompany().getId());
            workTypeConverter = new SymbolConverter(workTypeList);
            storeRegionList = symbolService.getAllStoreRegionByCompanyId(getUserData().getCompany().getId());
            storeRegionConverter = new SymbolConverter(storeRegionList);

            organizationSiteTypeList = symbolService.getOrganizationSiteType(getUserData().getCompany().getId());
            regionList = symbolService.getRegion(getUserData().getCompany().getId());
            regionConverter = new SymbolConverter(regionList);

            supplierTypeList = symbolService.getSupplierType(getUserData().getCompany().getId());
            supplierTypeConverter = new SymbolConverter(supplierTypeList);

            contractorTypeList = symbolService.getContractorType(getUserData().getCompany().getId());
            contractorTypeConverter = new SymbolConverter(contractorTypeList);

            inventoryDelegatorList = ineventoryDelegatorService.getDelegatorsByBranch(getUserData().getUserBranch().getId());
//            mainCustomerList = organizationSiteService.getInvOrganizationSiteByBranchIdPer(getUserData().getUserBranch().getId());// here 
//            customerConvertor = new InvOrganizationSiteConverter(mainCustomerList);

            offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
            setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyDTOList));

            accountIdList = getAccountService().getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            accountConverter = new GlaccountConverter(accountIdList);

            if (url.contains("customer")) {   // مندوبين المبيعات
                salesPersonsList = invDelegatorService.getSalesByBranch(getUserData().getUserBranch().getId());
            } else { // مندوبين المشتريات
                salesPersonsList = invDelegatorService.getPurchaseByBranch(getUserData().getUserBranch().getId());
            }
            salesConverter = new InvDelegatorConvertor(salesPersonsList);

            setCustomer(true);
            setSupplier(false);
            setContractor(false);
            setSupplierAndCustomer(false);
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setInvOrganizationSiteDataEmpty();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    organizationSiteId = (Integer) context.getSessionMap().get("invOrganizationSiteSelected");
                    setEditedOrganizationSiteValues(organizationSiteId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "load");
        }
    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "reset");
        }
    }

    public void setInvOrganizationSiteDataEmpty() {
        try {
            invOrganizationSiteEntity = new InvOrganizationSiteEntity();
            if (currencylist != null && !currencylist.isEmpty()) {
                invOrganizationSiteEntity.setCurrencyId(currencylist.get(0));
            } else {
                errorMessage("لا توجد عملات");
            }
            if (url.contains("customer")) {
                invOrganizationSiteEntity.setType(0);
            } else if (url.contains("supplier")) {
                invOrganizationSiteEntity.setType(1);
            } else if (url.contains("contractor")) {
                invOrganizationSiteEntity.setType(4);
            }
            invOrganizationSiteEntity.setActive(1);
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "setInvOrganizationSiteDataEmpty");
        }
    }

    public void setEditedOrganizationSiteValues(Integer bId) {
        try {
            invOrganizationSite = organizationSiteService.findOrganizationSiteById(bId);
            validateInputsForEdit();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "setEditedOrganizationSiteValues");
        }
    }

    public void validateCode() {
        try {

            invOrganizationSite.setCode(invOrganizationSiteEntity.getCode());
            invOrganizationSite.setBranchId(getUserData().getUserBranch());
            invOrganizationSite.setType(invOrganizationSiteEntity.getType());
            invOrganizationSite.setId(organizationSiteId);

            if (organizationSiteService.checkCodeExistence(invOrganizationSite)) {
                errorMessage(getUserData().getUserDDs().get("UNIQE_SERIAL"));
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "validateCode");
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
            saveError(e, "OrganizationSiteFormMB", "completeSalesPerson");
            return null;
        }
    }

    public List<Symbol> completeSupplierType(String query) {
        try {
            List<Symbol> symbols = supplierTypeList;
            if (query == null || query.trim().equals("")) {

                supplierTypeConverter = new SymbolConverter(symbols);
                return symbols;
            }
            List<Symbol> filteredSymboles = new ArrayList<>();

            String nameAr;
            Integer serial;
            Symbol symbolFilter;
            for (int i = 0; i < supplierTypeList.size(); i++) {
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

            supplierTypeConverter = new SymbolConverter(filteredSymboles);
            return filteredSymboles;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeSupplierType");
            return null;
        }
    }

    public List<OfferCompanyDTO> completeOfferCompany(String query) {
        if (offerCompanyDTOList == null || offerCompanyDTOList.isEmpty()) {
            offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
            setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyDTOList));
        }
        return offerCompanyDTOList;
    }

    public List<InvOrganizationSite> completeCustomers(String query) {
        try {
            List<InvOrganizationSite> organizationSiteList = mainCustomerList;
            if (query == null || query.trim().equals("")) {

                customerConvertor = new InvOrganizationSiteConverter(organizationSiteList);
                return organizationSiteList;
            }
            List<InvOrganizationSite> filteredCustomers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite customerFilter;
            for (int i = 0; i < mainCustomerList.size(); i++) {
                customerFilter = organizationSiteList.get(i);
                nameAr = customerFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCustomers.contains(customerFilter)) {
                        filteredCustomers.add(customerFilter);
                    }
                }

                code = customerFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCustomers.contains(customerFilter)) {
                        filteredCustomers.add(customerFilter);
                    }
                }
            }

            customerConvertor = new InvOrganizationSiteConverter(filteredCustomers);
            return filteredCustomers;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeCustomers");
            return null;
        }
    }

    public List<Symbol> completeContractorType(String query) {
        try {
            List<Symbol> symbols = contractorTypeList;
            if (query == null || query.trim().equals("")) {

                contractorTypeConverter = new SymbolConverter(symbols);
                return symbols;
            }
            List<Symbol> filteredSymboles = new ArrayList<>();

            String nameAr;
            Integer serial;
            Symbol symbolFilter;
            for (int i = 0; i < contractorTypeList.size(); i++) {
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

            contractorTypeConverter = new SymbolConverter(filteredSymboles);
            return filteredSymboles;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeContractorType");
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
            saveError(e, "OrganizationSiteFormMB", "completeCurrency");
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
            saveError(e, "OrganizationSiteFormMB", "completeRegion");
            return null;
        }
    }

    public List<Symbol> completeWorkType(String query) {
        try {
            List<Symbol> workTypeFilter = workTypeList;
            if (query == null || query.trim().equals("")) {

                regionConverter = new SymbolConverter(workTypeFilter);
                return workTypeFilter;
            }
            List<Symbol> workTypeListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            Symbol workFilter;
            for (int i = 0; i < regionList.size(); i++) {
                workFilter = workTypeFilter.get(i);
                nameAr = workFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!workTypeListFiltered.contains(workFilter)) {
                        workTypeListFiltered.add(workFilter);
                    }
                }

                code = workFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!workTypeListFiltered.contains(workFilter)) {
                        workTypeListFiltered.add(workFilter);
                    }
                }
            }

            regionConverter = new SymbolConverter(workTypeListFiltered);
            return workTypeListFiltered;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeWorkType");
            return null;
        }
    }

    public List<Symbol> completeStoreRegion(String query) {
        try {
            List<Symbol> storeRegionFiltered = storeRegionList;
            if (query == null || query.trim().equals("")) {

                storeRegionConverter = new SymbolConverter(storeRegionFiltered);
                return storeRegionFiltered;
            }
            List<Symbol> storeRegionListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            Symbol storeRegionFilter;
            for (int i = 0; i < regionList.size(); i++) {
                storeRegionFilter = storeRegionFiltered.get(i);
                nameAr = storeRegionFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!storeRegionListFiltered.contains(storeRegionFilter)) {
                        storeRegionListFiltered.add(storeRegionFilter);
                    }
                }

                code = storeRegionFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!storeRegionListFiltered.contains(storeRegionFilter)) {
                        storeRegionListFiltered.add(storeRegionFilter);
                    }
                }
            }

            storeRegionConverter = new SymbolConverter(storeRegionListFiltered);
            return storeRegionListFiltered;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeWorkType");
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
            saveError(e, "OrganizationSiteFormMB", "completeGlAccount");
            return null;
        }
    }

    public void checkVisability() {
        try {
            if (invOrganizationSiteEntity.getType() == 0) {
                customer = true;
                supplier = false;
                contractor = false;
            } else if (invOrganizationSiteEntity.getType() == 1) {
                customer = false;
                supplier = true;
                contractor = false;
            } else if (invOrganizationSiteEntity.getType() == 2) {
                customer = true;
                supplier = true;
                contractor = false;
                setSupplierAndCustomer(true);
            } else if (invOrganizationSiteEntity.getType() == 4) {
                customer = false;
                supplier = false;
                contractor = true;

            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "checkVisability");
        }
    }

    public void validateInputsForEdit() {
        try {
            invOrganizationSiteEntity.setAccountId(invOrganizationSite.getAccountId());

            invOrganizationSiteEntity.setCurrencyId(invOrganizationSite.getCurrencyId());

            invOrganizationSiteEntity.setRegionId(invOrganizationSite.getRegionId());

            invOrganizationSiteEntity.setSupplierType(invOrganizationSite.getSupplierType());
            invOrganizationSiteEntity.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());
            invOrganizationSiteEntity.setDiscount(invOrganizationSite.getDiscount());

            if (invOrganizationSite.getDelegatorId() != null) {
                invOrganizationSiteEntity.setDelegateId(invOrganizationSite.getDelegatorId().getId());
            } else {
                invOrganizationSiteEntity.setDelegateId(null);
            }

            invOrganizationSiteEntity.setSalesPerson(invOrganizationSite.getDelegatorId());

            invOrganizationSiteEntity.setParent(invOrganizationSite.getParent());

            if (invOrganizationSite.getOfferCompanyId() != null) {
                OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();
                offerCompanyDTO.setId(invOrganizationSite.getOfferCompanyId().getId());
                offerCompanyDTO.setName(invOrganizationSite.getOfferCompanyId().getName());
                offerCompanyDTO.setDiscount(invOrganizationSite.getOfferCompanyId().getDiscount());
                invOrganizationSiteEntity.setOfferCompanyDTO(offerCompanyDTO);
            }

            if (invOrganizationSite.getCode() != null) {
                invOrganizationSiteEntity.setCode(invOrganizationSite.getCode());
            } else {
                invOrganizationSiteEntity.setCode(null);
            }
            if (invOrganizationSite.getAddress() != null) {
                invOrganizationSiteEntity.setAddress(invOrganizationSite.getAddress());
            } else {
                invOrganizationSiteEntity.setAddress(null);
            }
            if (invOrganizationSite.getBalanceLimit() != null) {
                invOrganizationSiteEntity.setBalanceLimit(invOrganizationSite.getBalanceLimit());
            } else {
                invOrganizationSiteEntity.setBalanceLimit(null);
            }
            if (invOrganizationSite.getContactPerson() != null) {
                invOrganizationSiteEntity.setContactPerson(invOrganizationSite.getContactPerson());
            } else {
                invOrganizationSiteEntity.setContactPerson(null);
            }
            if (invOrganizationSite.getEmail() != null) {
                invOrganizationSiteEntity.setEmail(invOrganizationSite.getEmail());
            } else {
                invOrganizationSiteEntity.setEmail(null);
            }
            if (invOrganizationSite.getPhone() != null) {
                invOrganizationSiteEntity.setPhone1(invOrganizationSite.getPhone());
            } else {
                invOrganizationSiteEntity.setPhone1(null);
            }
            if (invOrganizationSite.getPhone2() != null) {
                invOrganizationSiteEntity.setPhone2(invOrganizationSite.getPhone2());
            } else {
                invOrganizationSiteEntity.setPhone2(null);
            }
            if (invOrganizationSite.getFax() != null) {
                invOrganizationSiteEntity.setFax(invOrganizationSite.getFax());
            } else {
                invOrganizationSiteEntity.setFax(null);
            }
            if (invOrganizationSite.getMobile() != null) {
                invOrganizationSiteEntity.setMobile(invOrganizationSite.getMobile());
            } else {
                invOrganizationSiteEntity.setMobile(null);
            }
            if (invOrganizationSite.getName() != null) {
                invOrganizationSiteEntity.setName(invOrganizationSite.getName());
            } else {
                invOrganizationSiteEntity.setName(null);
            }

            if (invOrganizationSite.getOpenBalanceCredit() != null) {
                invOrganizationSiteEntity.setOpenBalanceCredit(invOrganizationSite.getOpenBalanceCredit());
            } else {
                invOrganizationSiteEntity.setOpenBalanceCredit(null);
            }
            if (invOrganizationSite.getOpenBalanceDebit() != null) {
                invOrganizationSiteEntity.setOpenBalanceDebit(invOrganizationSite.getOpenBalanceDebit());
            } else {
                invOrganizationSiteEntity.setOpenBalanceDebit(null);
            }
            if (invOrganizationSite.getPostBox() != null) {
                invOrganizationSiteEntity.setPostBox(invOrganizationSite.getPostBox());
            } else {
                invOrganizationSiteEntity.setPostBox(null);
            }
            if (invOrganizationSite.getZip() != null) {
                invOrganizationSiteEntity.setZip(invOrganizationSite.getZip());
            } else {
                invOrganizationSiteEntity.setZip(null);
            }
            if (invOrganizationSite.getRemark() != null) {
                invOrganizationSiteEntity.setRemarks(invOrganizationSite.getRemark());
            } else {
                invOrganizationSiteEntity.setRemarks(null);
            }
            invOrganizationSiteEntity.setActive(invOrganizationSite.getActive());

            invOrganizationSiteEntity.setType(invOrganizationSite.getType());

            if (invOrganizationSite.getType() == 0) {
                setCustomer(true);
                setSupplier(false);
                setContractor(false);
                setSupplierAndCustomer(false);
            } else if (invOrganizationSite.getType() == 1) {
                setCustomer(false);
                setSupplier(true);
                setContractor(false);
                setSupplierAndCustomer(false);
            } else if (invOrganizationSite.getType() == 2) {
                setCustomer(true);
                setSupplier(true);
                setContractor(false);
                setSupplierAndCustomer(true);
            } else if (invOrganizationSite.getType() == 4) {
                setCustomer(false);
                setSupplier(true);
                setContractor(true);
                setSupplierAndCustomer(false);
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "validateInputsForEdit");
        }
    }

    public void validateInputsForSave() {
        try {
            invOrganizationSite.setAccountId(invOrganizationSiteEntity.getAccountId());

            invOrganizationSite.setCurrencyId(invOrganizationSiteEntity.getCurrencyId());

            invOrganizationSite.setRegionId(invOrganizationSiteEntity.getRegionId());

            invOrganizationSite.setSupplierType(invOrganizationSiteEntity.getSupplierType());

            invOrganizationSite.setDelegatorId(invOrganizationSiteEntity.getSalesPerson());

            if (invOrganizationSiteEntity.getOfferCompanyDTO() != null) {
                OfferCompany offerCompany = new OfferCompany();
                offerCompany.setId(invOrganizationSiteEntity.getOfferCompanyDTO().getId());
                invOrganizationSite.setOfferCompanyId(offerCompany);
            }

            invOrganizationSite.setParent(invOrganizationSiteEntity.getParent());

            invOrganizationSite.setActive(invOrganizationSiteEntity.getActive());
            invOrganizationSite.setType(invOrganizationSiteEntity.getType() != null ? invOrganizationSiteEntity.getType() : 0);
            invOrganizationSite.setAddress(invOrganizationSiteEntity.getAddress());
            invOrganizationSite.setBalanceLimit(invOrganizationSiteEntity.getBalanceLimit());
            invOrganizationSite.setContactPerson(invOrganizationSiteEntity.getContactPerson());
            invOrganizationSite.setEmail(invOrganizationSiteEntity.getEmail());
            invOrganizationSite.setFax(invOrganizationSiteEntity.getFax());
            invOrganizationSite.setMobile(invOrganizationSiteEntity.getMobile());
            invOrganizationSite.setOpenBalanceCredit(invOrganizationSiteEntity.getOpenBalanceCredit());
            invOrganizationSite.setOpenBalanceDebit(invOrganizationSiteEntity.getOpenBalanceDebit());
            invOrganizationSite.setRemark(invOrganizationSiteEntity.getRemarks());
            invOrganizationSite.setPhone(invOrganizationSiteEntity.getPhone1());
            invOrganizationSite.setPhone2(invOrganizationSiteEntity.getPhone2());
            invOrganizationSite.setPostBox(invOrganizationSiteEntity.getPostBox());
            invOrganizationSite.setName(invOrganizationSiteEntity.getName());
            invOrganizationSite.setCode(invOrganizationSiteEntity.getCode());
            invOrganizationSite.setZip(invOrganizationSiteEntity.getZip());
            invOrganizationSite.setDiscount(invOrganizationSiteEntity.getDiscount());
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "validateInputsForSave");
        }

    }

    public void openBalanceRender() {

        if (invOrganizationSiteEntity.getOpenBalanceDebit() != null) {
            setOpenBalanceCredit(Boolean.TRUE);

        }

        if (invOrganizationSiteEntity.getOpenBalanceCredit() != null) {
            setOpenBalanceDebit(Boolean.TRUE);

        }

        if (invOrganizationSiteEntity.getOpenBalanceCredit() != null || invOrganizationSiteEntity.getOpenBalanceDebit() != null) {
            setOpenBalanceDate(Boolean.TRUE);
        }

        if (invOrganizationSiteEntity.getOpenBalanceCredit() == null && invOrganizationSiteEntity.getOpenBalanceDebit() == null) {
            setOpenBalanceDebit(Boolean.FALSE);
            setOpenBalanceCredit(Boolean.FALSE);
            setOpenBalanceDate(Boolean.FALSE);
            invOrganizationSiteEntity.setOpenBalanceDate(null);
        }

    }

    public void ondateSelect(SelectEvent e) {

        Date date = (Date) e.getObject();

        invOrganizationSiteEntity.setOpenBalanceDate(date);
    }

    @Override
    public String save() {
        try {
            if (url != null && url.contains("contractorForm")) {
                if (invOrganizationSiteEntity.getOpenBalanceDebit() != null || invOrganizationSiteEntity.getOpenBalanceCredit() != null) {
                    if (invOrganizationSiteEntity.getOpenBalanceDate() == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ للرصيد الافتتاحي"));
                        return "";
                    }
                }
            }
            invOrganizationSite.setCompanyId(getUserData().getCompany());
            invOrganizationSite.setBranchId(getUserData().getUserBranch());
            validateInputsForSave();
            if (invOrganizationSite.getId() == null) {
                invOrganizationSite.setCreatedBy(getUserData().getUser());
                invOrganizationSite.setCreationDate(new Date());
            } else {
                invOrganizationSite.setModificationDate(new Date());
                invOrganizationSite.setModifiedBy(getUserData().getUser());
            }
            invOrganizationSite = organizationSiteService.addOrganizationSite(invOrganizationSite);
            invOrganizationSiteEntity.setId(invOrganizationSite.getId());
            // exit();
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "save");
            return null;
        }
    }

    public String exit() {
        try {
            if (url.contains("customer")) {
                exit("../customer/customerList.xhtml");
            } else if (url.contains("supplier")) {
                exit("../supplier/supplierList.xhtml");
            } else if (url.contains("contractor")) {
                exit("../contractor/contractorList.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSite getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSite invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
    }

    /**
     * @return the customer
     */
    public boolean isCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(boolean customer) {
        this.customer = customer;
    }

    /**
     * @return the supplier
     */
    public boolean isSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(boolean supplier) {
        this.supplier = supplier;
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
     * @return the organizationSiteTypeList
     */
    public List<Symbol> getOrganizationSiteTypeList() {
        return organizationSiteTypeList;
    }

    /**
     * @param organizationSiteTypeList the organizationSiteTypeList to set
     */
    public void setOrganizationSiteTypeList(List<Symbol> organizationSiteTypeList) {
        this.organizationSiteTypeList = organizationSiteTypeList;
    }

    /**
     * @return the organizationSiteType
     */
    public Symbol getOrganizationSiteType() {
        return organizationSiteType;
    }

    /**
     * @param organizationSiteType the organizationSiteType to set
     */
    public void setOrganizationSiteType(Symbol organizationSiteType) {
        this.organizationSiteType = organizationSiteType;
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
     * @return the mainCustomerList
     */
    public List<InvOrganizationSite> getMainCustomerList() {
        return mainCustomerList;
    }

    /**
     * @param mainCustomerList the mainCustomerList to set
     */
    public void setMainCustomerList(List<InvOrganizationSite> mainCustomerList) {
        this.mainCustomerList = mainCustomerList;
    }

    /**
     * @return the mainCustomer
     */
    public InvOrganizationSite getMainCustomer() {
        return mainCustomer;
    }

    /**
     * @param mainCustomer the mainCustomer to set
     */
    public void setMainCustomer(InvOrganizationSite mainCustomer) {
        this.mainCustomer = mainCustomer;
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
     * @return the invOrganizationSiteEntity
     */
    public InvOrganizationSiteEntity getInvOrganizationSiteEntity() {
        return invOrganizationSiteEntity;
    }

    /**
     * @param invOrganizationSiteEntity the invOrganizationSiteEntity to set
     */
    public void setInvOrganizationSiteEntity(InvOrganizationSiteEntity invOrganizationSiteEntity) {
        this.invOrganizationSiteEntity = invOrganizationSiteEntity;
    }

    /**
     * @return the supplierTypeList
     */
    public List<Symbol> getSupplierTypeList() {
        return supplierTypeList;
    }

    /**
     * @param supplierTypeList the supplierTypeList to set
     */
    public void setSupplierTypeList(List<Symbol> supplierTypeList) {
        this.supplierTypeList = supplierTypeList;
    }

    /**
     * @return the supplierType
     */
    public Symbol getSupplierType() {
        return supplierType;
    }

    /**
     * @param supplierType the supplierType to set
     */
    public void setSupplierType(Symbol supplierType) {
        this.supplierType = supplierType;
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
     * @return the organizationSiteId
     */
    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the supplierAndCustomer
     */
    public boolean isSupplierAndCustomer() {
        return supplierAndCustomer;
    }

    /**
     * @param supplierAndCustomer the supplierAndCustomer to set
     */
    public void setSupplierAndCustomer(boolean supplierAndCustomer) {
        this.supplierAndCustomer = supplierAndCustomer;
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
        return supplierTypeConverter;
    }

    public void setSupplierTypeConverter(SymbolConverter supplierTypeConverter) {
        this.supplierTypeConverter = supplierTypeConverter;
    }

    public InvOrganizationSiteConverter getCustomerConvertor() {
        return customerConvertor;
    }

    public void setCustomerConvertor(InvOrganizationSiteConverter customerConvertor) {
        this.customerConvertor = customerConvertor;
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
     * @return the contractorTypeConverter
     */
    public SymbolConverter getContractorTypeConverter() {
        return contractorTypeConverter;
    }

    /**
     * @param contractorTypeConverter the contractorTypeConverter to set
     */
    public void setContractorTypeConverter(SymbolConverter contractorTypeConverter) {
        this.contractorTypeConverter = contractorTypeConverter;
    }

    /**
     * @return the contractorTypeList
     */
    public List<Symbol> getContractorTypeList() {
        return contractorTypeList;
    }

    /**
     * @param contractorTypeList the contractorTypeList to set
     */
    public void setContractorTypeList(List<Symbol> contractorTypeList) {
        this.contractorTypeList = contractorTypeList;
    }

    /**
     * @return the contractor
     */
    public boolean isContractor() {
        return contractor;
    }

    /**
     * @param contractor the contractor to set
     */
    public void setContractor(boolean contractor) {
        this.contractor = contractor;
    }

    /**
     * @return the workTypeList
     */
    public List<Symbol> getWorkTypeList() {
        return workTypeList;
    }

    /**
     * @param workTypeList the workTypeList to set
     */
    public void setWorkTypeList(List<Symbol> workTypeList) {
        this.workTypeList = workTypeList;
    }

    /**
     * @return the workTypeConverter
     */
    public SymbolConverter getWorkTypeConverter() {
        return workTypeConverter;
    }

    /**
     * @param workTypeConverter the workTypeConverter to set
     */
    public void setWorkTypeConverter(SymbolConverter workTypeConverter) {
        this.workTypeConverter = workTypeConverter;
    }

    /**
     * @return the storeRegionList
     */
    public List<Symbol> getStoreRegionList() {
        return storeRegionList;
    }

    /**
     * @param storeRegionList the storeRegionList to set
     */
    public void setStoreRegionList(List<Symbol> storeRegionList) {
        this.storeRegionList = storeRegionList;
    }

    /**
     * @return the storeRegionConverter
     */
    public SymbolConverter getStoreRegionConverter() {
        return storeRegionConverter;
    }

    /**
     * @param storeRegionConverter the storeRegionConverter to set
     */
    public void setStoreRegionConverter(SymbolConverter storeRegionConverter) {
        this.storeRegionConverter = storeRegionConverter;
    }

    /**
     * @return the openBalanceDebit
     */
    public Boolean getOpenBalanceDebit() {
        return openBalanceDebit;
    }

    /**
     * @param openBalanceDebit the openBalanceDebit to set
     */
    public void setOpenBalanceDebit(Boolean openBalanceDebit) {
        this.openBalanceDebit = openBalanceDebit;
    }

    /**
     * @return the openBalanceCredit
     */
    public Boolean getOpenBalanceCredit() {
        return openBalanceCredit;
    }

    /**
     * @param openBalanceCredit the openBalanceCredit to set
     */
    public void setOpenBalanceCredit(Boolean openBalanceCredit) {
        this.openBalanceCredit = openBalanceCredit;
    }

    /**
     * @return the openBalanceDate
     */
    public Boolean getOpenBalanceDate() {
        return openBalanceDate;
    }

    /**
     * @param openBalanceDate the openBalanceDate to set
     */
    public void setOpenBalanceDate(Boolean openBalanceDate) {
        this.openBalanceDate = openBalanceDate;
    }

    /**
     * @return the offerCompanyDTOList
     */
    public List<OfferCompanyDTO> getOfferCompanyDTOList() {

        if (offerCompanyDTOList == null || offerCompanyDTOList.isEmpty()) {
            offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
            setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyDTOList));
        }

        return offerCompanyDTOList;
    }

    /**
     * @param offerCompanyDTOList the offerCompanyDTOList to set
     */
    public void setOfferCompanyDTOList(List<OfferCompanyDTO> offerCompanyDTOList) {
        this.offerCompanyDTOList = offerCompanyDTOList;
    }

    /**
     * @return the offerCompanyDTOSelected
     */
    public OfferCompanyDTO getOfferCompanyDTOSelected() {
        return offerCompanyDTOSelected;
    }

    /**
     * @param offerCompanyDTOSelected the offerCompanyDTOSelected to set
     */
    public void setOfferCompanyDTOSelected(OfferCompanyDTO offerCompanyDTOSelected) {
        this.offerCompanyDTOSelected = offerCompanyDTOSelected;
    }

    /**
     * @return the offerCompanyDTOConverter
     */
    public OfferCompanyDTOConverter getOfferCompanyDTOConverter() {
        return offerCompanyDTOConverter;
    }

    /**
     * @param offerCompanyDTOConverter the offerCompanyDTOConverter to set
     */
    public void setOfferCompanyDTOConverter(OfferCompanyDTOConverter offerCompanyDTOConverter) {
        this.offerCompanyDTOConverter = offerCompanyDTOConverter;
    }

}
