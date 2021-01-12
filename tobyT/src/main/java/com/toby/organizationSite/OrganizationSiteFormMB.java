/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.CurrencyDTOConverter;
import com.toby.converter.GlAccountDTOConverter;
import com.toby.converter.InvDelegatorDTOConvertor;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.SymbolDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
@Named(value = "organizationSiteFormMB")
@ViewScoped
public class OrganizationSiteFormMB extends BaseFormBean implements Serializable {

    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private boolean customer;
    private boolean contractor;
    private boolean supplier;
    private boolean supplierAndCustomer;
    private List<CurrencyDTO> currencyDTOlist;
    private CurrencyDTO currencyDTO;
    private List<SymbolDTO> organizationSiteTypeList;
    private SymbolDTO organizationSiteType;
    private List<SymbolDTO> regionList;
    private SymbolDTO region;
    private List<SymbolDTO> supplierTypeList;
    private List<SymbolDTO> contractorTypeList;
    private SymbolDTO supplierType;
    private List<InventoryDelegatorDTO> inventoryDelegatorDTOList;
    private InventoryDelegatorDTO inventoryDelegatorDTO;
    private List<InvOrganizationSiteDTO> mainCustomerList;
    private InvOrganizationSiteDTO mainCustomer;
    private List<GlAccountDTO> accountIdList;
    private GlAccountDTO accountId;

    private boolean activity;
    private Boolean issales;
    private InvDelegatorDTOConvertor salesConverter;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor;
    private SymbolDTOConverter supplierTypeConverter;
    private SymbolDTOConverter contractorTypeConverter;
    private SymbolDTOConverter regionConverter;
    private SymbolDTOConverter workTypeConverter;
    private SymbolDTOConverter storeRegionConverter;
    private SymbolDTOConverter accountConverter;
    private InvOrganizationSiteDTOConverter customerConvertor;

    private CurrencyDTOConverter currencyDTOConverter;
    private GlAccountDTOConverter glAccountDTOConverter;
    private List<InventoryDelegatorDTO> salesPersonsList;

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

    @Override
    @PostConstruct
    public void init() {
        try {

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

            setSalesPersonsList(new ArrayList<>());
            setCustomer(true);
            setSupplier(false);
            setContractor(false);
            setSupplierAndCustomer(false);
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setInvOrganizationSiteDataEmpty();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    Integer organizationSiteId = (Integer) getContext().getSessionMap().get("invOrganizationSiteSelected");
                    setEditedOrganizationSiteValues(organizationSiteId);
                    fillListes();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "load");
        }
    }

    private void fillListes() {
        getCurrencyDTOlist();
        getOrganizationSiteTypeList();

        getRegionList();
        getSupplierTypeList();
        getContractorTypeList();
        getInventoryDelegatorDTOList();
        getMainCustomerList();// here
        getAccountIdList();
        getSalesPersonsList();
    }

    public void reset() {
        setSalesPersonsList(new ArrayList<>());
        invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        setCustomer(true);
        setSupplier(false);
        setContractor(false);
        setSupplierAndCustomer(false);
        setInvOrganizationSiteDataEmpty();
    }

    public void setInvOrganizationSiteDataEmpty() {
        try {
            if (url.contains("customer")) {
                getInvOrganizationSiteDTO().setType(0);
            } else if (url.contains("supplier")) {
                getInvOrganizationSiteDTO().setType(1);
            } else if (url.contains("contractor")) {
                getInvOrganizationSiteDTO().setType(4);
            }
            getInvOrganizationSiteDTO().setActive(1);
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "setInvOrganizationSiteDataEmpty");
        }
    }

    public void setEditedOrganizationSiteValues(Integer bId) {
        try {
            setInvOrganizationSiteDTO(organizationSiteService.findOrganizationSiteByIdDTO(bId));

        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "setEditedOrganizationSiteValues");
        }
    }

    public boolean validateCode() {
        if (getInvOrganizationSiteDTO() != null) {
            Boolean x = organizationSiteService.validateCode(getUserData().getUser(), getInvOrganizationSiteDTO().getId(), getInvOrganizationSiteDTO().getCode(),getInvOrganizationSiteDTO().getType());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود مسبقا", null));
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "يجب ادخال بيانات", null));
            return false;
        }
    }
    public boolean validateName() {
        if (getInvOrganizationSiteDTO() != null) {
            Boolean x = organizationSiteService.validateName(getUserData().getUser(), getInvOrganizationSiteDTO().getId(), getInvOrganizationSiteDTO().getName(),getInvOrganizationSiteDTO().getType());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود مسبقا", null));
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "يجب ادخال بيانات", null));
            return false;
        }
    }

    public List<InventoryDelegatorDTO> completeSalesPerson(String query) {
        return AutoComplete.completeSalesPerson(query, getSalesPersonsList(), salesConverter);
    }

    public List<SymbolDTO> completeSupplierType(String query) {
        return AutoComplete.completeSupplierType(query, getSupplierTypeList(), supplierTypeConverter);
    }

    public List<InvOrganizationSiteDTO> completOrgSite(String query) {
        return AutoComplete.completOrgSite(query, getMainCustomerList(), customerConvertor);

    }

    public List<SymbolDTO> completeContractorType(String query) {
        return AutoComplete.completeSupplierType(query, getContractorTypeList(), contractorTypeConverter);
    }

    public List<CurrencyDTO> completeCurrency(String query) {
        return AutoComplete.completeCurrency(query, getCurrencyDTOlist(), currencyDTOConverter);
    }

    public List<SymbolDTO> completeRegion(String query) {
        return AutoComplete.completeSupplierType(query, getRegionList(), regionConverter);

    }

    public List<GlAccountDTO> completeGlAccount(String query) {
        return AutoComplete.completeGlAccount(query, getAccountIdList(), glAccountDTOConverter);
    }

    @Override
    public String save() {

        try {
            if (getInvOrganizationSiteDTO() != null && !getInvOrganizationSiteDTO().getCode().isEmpty()) {
                setInvOrganizationSiteDTO(organizationSiteService.saveInvOrganizationSiteDTO(invOrganizationSiteDTO, getUserData().getUser()));
                if (getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getId() != null && getInvOrganizationSiteDTO().getMsg() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم االحفظ بنجاح", getUserData().getUserDDs().get("SAVED")));
                } else {
                    errorMessage(getInvOrganizationSiteDTO().getMsg());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "يجب ادخال كود", null));
            }
            return "";
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "save");
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

    public Boolean getIssales() {
        return issales;
    }

    /**
     * @param issales the issales to set
     */
    public void setIssales(Boolean issales) {
        this.issales = issales;
    }

    public boolean isContractor() {
        return contractor;
    }

    /**
     * @param contractor the contractor to set
     */
    public void setContractor(boolean contractor) {
        this.contractor = contractor;
    }

    public List<CurrencyDTO> getCurrencyDTOlist() {
        if (currencyDTOlist == null || currencyDTOlist.isEmpty()) {
            currencyDTOlist = currencyService.getAllCurrencyByCompanyIdDTO(getUserData().getCompany().getId(), getUserData().getUser());
            setCurrencyDTOConverter(new CurrencyDTOConverter(currencyDTOlist));
        }

        return currencyDTOlist;
    }

    /**
     * @param currencyDTOlist the currencyDTOlist to set
     */
    public void setCurrencyDTOlist(List<CurrencyDTO> currencyDTOlist) {
        this.currencyDTOlist = currencyDTOlist;
    }

    /**
     * @return the currencyDTO
     */
    public CurrencyDTO getCurrencyDTO() {
        return currencyDTO;
    }

    /**
     * @param currencyDTO the currencyDTO to set
     */
    public void setCurrencyDTO(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    /**
     * @return the organizationSiteTypeList
     */
    public List<SymbolDTO> getOrganizationSiteTypeList() {
        if (organizationSiteTypeList == null || organizationSiteTypeList.isEmpty()) {
            organizationSiteTypeList = symbolService.getOrganizationSiteTypeDTO(getUserData().getCompany().getId(), getUserData().getUser());

        }

        return organizationSiteTypeList;
    }

    /**
     * @param organizationSiteTypeList the organizationSiteTypeList to set
     */
    public void setOrganizationSiteTypeList(List<SymbolDTO> organizationSiteTypeList) {
        this.organizationSiteTypeList = organizationSiteTypeList;
    }

    /**
     * @return the organizationSiteType
     */
    public SymbolDTO getOrganizationSiteType() {
        return organizationSiteType;
    }

    /**
     * @param organizationSiteType the organizationSiteType to set
     */
    public void setOrganizationSiteType(SymbolDTO organizationSiteType) {
        this.organizationSiteType = organizationSiteType;
    }

    /**
     * @return the regionList
     */
    public List<SymbolDTO> getRegionList() {
        if (regionList == null || regionList.isEmpty()) {
            regionList = symbolService.getRegionDTO(getUserData().getCompany().getId(), getUserData().getUser());
            setRegionConverter(new SymbolDTOConverter(regionList));

        }
        return regionList;
    }

    /**
     * @param regionList the regionList to set
     */
    public void setRegionList(List<SymbolDTO> regionList) {
        this.regionList = regionList;
    }

    /**
     * @return the region
     */
    public SymbolDTO getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(SymbolDTO region) {
        this.region = region;
    }

    /**
     * @return the supplierTypeList
     */
    public List<SymbolDTO> getSupplierTypeList() {
        if (supplierTypeList == null || supplierTypeList.isEmpty()) {
            supplierTypeList = symbolService.getSupplierTypeDTO(getUserData().getCompany().getId(), getUserData().getUser());
            setSupplierTypeConverter(new SymbolDTOConverter(supplierTypeList));
        }
        return supplierTypeList;
    }

    /**
     * @param supplierTypeList the supplierTypeList to set
     */
    public void setSupplierTypeList(List<SymbolDTO> supplierTypeList) {
        this.supplierTypeList = supplierTypeList;
    }

    /**
     * @return the contractorTypeList
     */
    public List<SymbolDTO> getContractorTypeList() {
        if (contractorTypeList == null || contractorTypeList.isEmpty()) {
            contractorTypeList = symbolService.getContractorTypeDTO(getUserData().getCompany().getId(), getUserData().getUser());
            setSupplierTypeConverter(new SymbolDTOConverter(contractorTypeList));
        }
        return contractorTypeList;
    }

    /**
     * @param contractorTypeList the contractorTypeList to set
     */
    public void setContractorTypeList(List<SymbolDTO> contractorTypeList) {
        this.contractorTypeList = contractorTypeList;
    }

    /**
     * @return the supplierType
     */
    public SymbolDTO getSupplierType() {
        return supplierType;
    }

    /**
     * @param supplierType the supplierType to set
     */
    public void setSupplierType(SymbolDTO supplierType) {
        this.supplierType = supplierType;
    }

    /**
     * @return the inventoryDelegatorDTOList
     */
    public List<InventoryDelegatorDTO> getInventoryDelegatorDTOList() {
        if (inventoryDelegatorDTOList == null || inventoryDelegatorDTOList.isEmpty()) {
            inventoryDelegatorDTOList = ineventoryDelegatorService.getDelegatorsByBranchDTO(getUserData().getUserBranch().getId(), getUserData().getUser());
            setInvDelegatorDTOConvertor(new InvDelegatorDTOConvertor(inventoryDelegatorDTOList));
        }
        return inventoryDelegatorDTOList;
    }

    /**
     * @param inventoryDelegatorDTOList the inventoryDelegatorDTOList to set
     */
    public void setInventoryDelegatorDTOList(List<InventoryDelegatorDTO> inventoryDelegatorDTOList) {
        this.inventoryDelegatorDTOList = inventoryDelegatorDTOList;
    }

    /**
     * @return the inventoryDelegatorDTO
     */
    public InventoryDelegatorDTO getInventoryDelegatorDTO() {
        return inventoryDelegatorDTO;
    }

    /**
     * @param inventoryDelegatorDTO the inventoryDelegatorDTO to set
     */
    public void setInventoryDelegatorDTO(InventoryDelegatorDTO inventoryDelegatorDTO) {
        this.inventoryDelegatorDTO = inventoryDelegatorDTO;
    }

    /**
     * @return the mainCustomerList
     */
    public List<InvOrganizationSiteDTO> getMainCustomerList() {
        if (mainCustomerList == null || mainCustomerList.isEmpty()) {
            mainCustomerList = organizationSiteService.getInvOrganizationSiteByBranchIdPerDTO(getUserData().getUserBranch().getId(), getUserData().getUser());
            setCustomerConvertor(new InvOrganizationSiteDTOConverter(mainCustomerList));

        }
        return mainCustomerList;
    }

    /**
     * @param mainCustomerList the mainCustomerList to set
     */
    public void setMainCustomerList(List<InvOrganizationSiteDTO> mainCustomerList) {
        this.mainCustomerList = mainCustomerList;
    }

    /**
     * @return the mainCustomer
     */
    public InvOrganizationSiteDTO getMainCustomer() {
        return mainCustomer;
    }

    /**
     * @param mainCustomer the mainCustomer to set
     */
    public void setMainCustomer(InvOrganizationSiteDTO mainCustomer) {
        this.mainCustomer = mainCustomer;
    }

    /**
     * @return the salesPersonsList
     */
    public List<InventoryDelegatorDTO> getSalesPersonsList() {
        if (salesPersonsList == null || salesPersonsList.isEmpty()) {
            if (url.contains("customer")) {
                salesPersonsList = invDelegatorService.getSalesByBranchDTO(getUserData().getUserBranch().getId(), getUserData().getUser());
            } else {
                salesPersonsList = invDelegatorService.getPurchaseByBranchDTO(getUserData().getUserBranch().getId(), getUserData().getUser());
            }
            setSalesConverter(new InvDelegatorDTOConvertor(salesPersonsList));
        }
        return salesPersonsList;
    }

    public void checkVisability() {
        try {
            if (getInvOrganizationSiteDTO().getType() == 0) {
                customer = true;
                supplier = false;
                contractor = false;
            } else if (getInvOrganizationSiteDTO().getType() == 1) {
                customer = false;
                supplier = true;
                contractor = false;
            } else if (getInvOrganizationSiteDTO().getType() == 2) {
                customer = true;
                supplier = true;
                contractor = false;
                setSupplierAndCustomer(true);
            } else if (getInvOrganizationSiteDTO().getType() == 4) {
                customer = false;
                supplier = false;
                contractor = true;

            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "checkVisability");
        }
    }

    /**
     * @param salesPersonsList the salesPersonsList to set
     */
    public void setSalesPersonsList(List<InventoryDelegatorDTO> salesPersonsList) {
        this.salesPersonsList = salesPersonsList;
    }

    /**
     * @return the currencyDTOConverter
     */
    public CurrencyDTOConverter getCurrencyDTOConverter() {
        return currencyDTOConverter;
    }

    /**
     * @param currencyDTOConverter the currencyDTOConverter to set
     */
    public void setCurrencyDTOConverter(CurrencyDTOConverter currencyDTOConverter) {
        this.currencyDTOConverter = currencyDTOConverter;
    }

    /**
     * @return the supplierTypeConverter
     */
    public SymbolDTOConverter getSupplierTypeConverter() {
        return supplierTypeConverter;
    }

    /**
     * @param supplierTypeConverter the supplierTypeConverter to set
     */
    public void setSupplierTypeConverter(SymbolDTOConverter supplierTypeConverter) {
        this.supplierTypeConverter = supplierTypeConverter;
    }

    /**
     * @return the contractorTypeConverter
     */
    public SymbolDTOConverter getContractorTypeConverter() {
        return contractorTypeConverter;
    }

    /**
     * @param contractorTypeConverter the contractorTypeConverter to set
     */
    public void setContractorTypeConverter(SymbolDTOConverter contractorTypeConverter) {
        this.contractorTypeConverter = contractorTypeConverter;
    }

    /**
     * @return the regionConverter
     */
    public SymbolDTOConverter getRegionConverter() {
        return regionConverter;
    }

    /**
     * @param regionConverter the regionConverter to set
     */
    public void setRegionConverter(SymbolDTOConverter regionConverter) {
        this.regionConverter = regionConverter;
    }

    /**
     * @return the workTypeConverter
     */
    public SymbolDTOConverter getWorkTypeConverter() {
        return workTypeConverter;
    }

    /**
     * @param workTypeConverter the workTypeConverter to set
     */
    public void setWorkTypeConverter(SymbolDTOConverter workTypeConverter) {
        this.workTypeConverter = workTypeConverter;
    }

    /**
     * @return the storeRegionConverter
     */
    public SymbolDTOConverter getStoreRegionConverter() {
        return storeRegionConverter;
    }

    /**
     * @param storeRegionConverter the storeRegionConverter to set
     */
    public void setStoreRegionConverter(SymbolDTOConverter storeRegionConverter) {
        this.storeRegionConverter = storeRegionConverter;
    }

    /**
     * @return the accountConverter
     */
    public SymbolDTOConverter getAccountConverter() {
        return accountConverter;
    }

    /**
     * @param accountConverter the accountConverter to set
     */
    public void setAccountConverter(SymbolDTOConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * @return the customerConvertor
     */
    public InvOrganizationSiteDTOConverter getCustomerConvertor() {
        return customerConvertor;
    }

    /**
     * @param customerConvertor the customerConvertor to set
     */
    public void setCustomerConvertor(InvOrganizationSiteDTOConverter customerConvertor) {
        this.customerConvertor = customerConvertor;
    }

    /**
     * @return the accountIdList
     */
    public List<GlAccountDTO> getAccountIdList() {
        if (accountIdList == null || accountIdList.isEmpty()) {
            accountIdList = accountService.getBranchGLAccountsActiveDTO(getUserData().getUserBranch().getId(), getUserData().getUser());
            setGlAccountDTOConverter(new GlAccountDTOConverter(accountIdList));
        }
        return accountIdList;
    }

    /**
     * @param accountIdList the accountIdList to set
     */
    public void setAccountIdList(List<GlAccountDTO> accountIdList) {
        this.accountIdList = accountIdList;
    }

    /**
     * @return the glAccountDTOConverter
     */
    public GlAccountDTOConverter getGlAccountDTOConverter() {
        return glAccountDTOConverter;
    }

    /**
     * @param glAccountDTOConverter the glAccountDTOConverter to set
     */
    public void setGlAccountDTOConverter(GlAccountDTOConverter glAccountDTOConverter) {
        this.glAccountDTOConverter = glAccountDTOConverter;
    }

    /**
     * @return the salesConverter
     */
    public InvDelegatorDTOConvertor getSalesConverter() {
        return salesConverter;
    }

    /**
     * @param salesConverter the salesConverter to set
     */
    public void setSalesConverter(InvDelegatorDTOConvertor salesConverter) {
        this.salesConverter = salesConverter;
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        if (invOrganizationSiteDTO == null) {
            invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        }
        return invOrganizationSiteDTO;
    }

    /**
     * @param invOrganizationSiteDTO the invOrganizationSiteDTO to set
     */
    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
    }

    /**
     * @return the invDelegatorDTOConvertor
     */
    public InvDelegatorDTOConvertor getInvDelegatorDTOConvertor() {
        return invDelegatorDTOConvertor;
    }

    /**
     * @param invDelegatorDTOConvertor the invDelegatorDTOConvertor to set
     */
    public void setInvDelegatorDTOConvertor(InvDelegatorDTOConvertor invDelegatorDTOConvertor) {
        this.invDelegatorDTOConvertor = invDelegatorDTOConvertor;
    }

}
