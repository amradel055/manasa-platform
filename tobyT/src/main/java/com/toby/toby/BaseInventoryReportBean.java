/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvGroupService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.report.InvItemSalesReportService;
import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvGroupConverter;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvPurchaseInvoiceConverter;
import com.toby.converter.ItemConverter;
import com.toby.converter.ItemDTOConverter;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvItemDTO;
import com.toby.entity.InvGroup;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InventoryDelegator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author ahmed
 */
public abstract class BaseInventoryReportBean extends BaseReportBean {

    private List<InventoryDelegator> delegatesSalesList;
    private InvDelegatorConvertor delegatesSalesConverter;

    private List<InventoryDelegator> delegatesPurchaseList;
    private InvDelegatorConvertor delegatesPurchaseConverter;

    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    private List<InvPurchaseInvoice> invInvoicesSalesList;
    private InvPurchaseInvoiceConverter invInvoiceSalesConverter;

    private List<InvPurchaseInvoice> invInvoicesPurchaseList;
    private InvPurchaseInvoiceConverter invInvoicePurchaseConverter;

    private List<InvOrganizationSite> invCustomerOrgSiteList;
    private InvOrganizationSiteConverter invOrgSiteCustomerConverter;

    private List<InvOrganizationSite> invSupplierOrgSiteList;
    private InvOrganizationSiteConverter invOrgSiteSupplierConverter;

    private List<InvItemDTO> invItemDTOToList;
    private ItemDTOConverter itemDTOToConverter;
    private List<InvItemDTO> invItemDTOFromList;
    private ItemDTOConverter itemDTOFromConverter;
    private List<InvItem> invItemList;
    private List<InvItem> invItemListBak;
    private ItemConverter itemConverter;
    private ItemConverter itemConverterBak;

    private List<InvGroup> invGroupList;
    private InvGroupConverter groupConverter;

    private Integer branchId;
    private String screenMode;

    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvItemSalesReportService invItemSalesReportService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvGroupService invGroupService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        branchId = getUserData().getUserBranch().getId();
    }

    public void loadAllLists() {
        getDelegateSalesListByBranch();
        getDelegatePurchaseListByBranch();
        getInventoryListByBranch();
        getInvOrganizationSiteAsCustomer();
        getInvOrganizationSiteAsSupplier();
        getInvItemByBranch();
        getInvGroupByBranchId();
    }

    public void getDelegateSalesListByBranch() {
        delegatesSalesList = invDelegatorService.getSalesByBranch(branchId);
        delegatesSalesConverter = new InvDelegatorConvertor(delegatesSalesList);
        getInvoiceSalesListByBranch();
    }

    public void getInvoiceSalesListByBranch() {
//        invInvoicesSalesList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, true);
        invInvoiceSalesConverter = new InvPurchaseInvoiceConverter(invInvoicesSalesList);
    }

    public void getDelegatePurchaseListByBranch() {
        delegatesPurchaseList = invDelegatorService.getPurchaseByBranch(branchId);
        delegatesPurchaseConverter = new InvDelegatorConvertor(delegatesPurchaseList);
        getInvoicePurchaseListByBranch();
    }

    public void getInvoicePurchaseListByBranch() {
//        invInvoicesPurchaseList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, false);
        invInvoicePurchaseConverter = new InvPurchaseInvoiceConverter(invInvoicesPurchaseList);
    }

    public void getInventoryListByBranch() {
        invInventoryList = tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
        invInventoryConverter = new InvInventoryConverter(invInventoryList);
    }

    public void getInvOrganizationSiteAsCustomer() {
        invCustomerOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 0);
        invOrgSiteCustomerConverter = new InvOrganizationSiteConverter(invCustomerOrgSiteList);
    }

    public void getInvOrganizationSiteAsSupplier() {
        invSupplierOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1);
        invOrgSiteSupplierConverter = new InvOrganizationSiteConverter(invSupplierOrgSiteList);
    }

    public void getInvItemByBranch() {
        setInvItemDTOFromList(invItemService.getInvItemListByBranchId(branchId, ScreenNameClassEnum.BASEINVENTORYREPORTBEAN));
        setItemDTOFromConverter(new ItemDTOConverter(getInvItemDTOFromList()));

        setInvItemDTOToList(getInvItemDTOFromList());
        setItemDTOToConverter(new ItemDTOConverter(getInvItemDTOToList()));

//        setInvItemList(invItemService.getInvItemListByBranchIdOrigin(branchId));
//        setItemConverter(new ItemConverter(getInvItemList()));
//        
//        setInvItemListBak(invItemService.getInvItemListByBranchIdOrigin(branchId));
//        setItemConverterBak(new ItemConverter(getInvItemListBak()));
    }

    public void getInvGroupByBranchId() {
        invGroupList = invGroupService.getallInvGroupByBranchId(branchId);
        groupConverter = new InvGroupConverter(invGroupList);
    }

    public List<InventoryDelegator> completeDelegatesSales(String query) {
        List<InventoryDelegator> centersList = delegatesSalesList;
        if (query == null || query.trim().equals("")) {

            delegatesSalesConverter = new InvDelegatorConvertor(centersList);
            return centersList;
        }
        List<InventoryDelegator> filteredCostCenters = new ArrayList<>();

        String nameAr;
        String code;
        InventoryDelegator costCenterFilter;
        for (int i = 0; i < delegatesSalesList.size(); i++) {
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

        delegatesSalesConverter = new InvDelegatorConvertor(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<InventoryDelegator> completeDelegatesPurchase(String query) {
        List<InventoryDelegator> centersList = delegatesPurchaseList;
        if (query == null || query.trim().equals("")) {

            delegatesPurchaseConverter = new InvDelegatorConvertor(centersList);
            return centersList;
        }
        List<InventoryDelegator> filteredCostCenters = new ArrayList<>();

        String nameAr;
        String code;
        InventoryDelegator costCenterFilter;
        for (int i = 0; i < delegatesPurchaseList.size(); i++) {
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

        delegatesPurchaseConverter = new InvDelegatorConvertor(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<InvInventory> completeInventory(String query) {
        List<InvInventory> invList = invInventoryList;
        if (query == null || query.trim().equals("")) {

            invInventoryConverter = new InvInventoryConverter(invList);
            return invList;
        }
        List<InvInventory> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvInventory invFilter;
        for (int i = 0; i < invInventoryList.size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        invInventoryConverter = new InvInventoryConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvPurchaseInvoice> completeSalesInvoice(String query) {
        List<InvPurchaseInvoice> invList = invInvoicesSalesList;
        if (query == null || query.trim().equals("")) {

            invInvoiceSalesConverter = new InvPurchaseInvoiceConverter(invList);
            return invList;
        }
        List<InvPurchaseInvoice> filteredInvs = new ArrayList<>();

        Integer code;
        InvPurchaseInvoice invFilter;
        for (int i = 0; i < invInvoicesSalesList.size(); i++) {
            invFilter = invList.get(i);

            code = invFilter.getSerial();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        invInvoiceSalesConverter = new InvPurchaseInvoiceConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvPurchaseInvoice> completePurchaseInvoice(String query) {
        List<InvPurchaseInvoice> invList = invInvoicesPurchaseList;
        if (query == null || query.trim().equals("")) {

            invInvoicePurchaseConverter = new InvPurchaseInvoiceConverter(invList);
            return invList;
        }
        List<InvPurchaseInvoice> filteredInvs = new ArrayList<>();

        Integer code;
        InvPurchaseInvoice invFilter;
        for (int i = 0; i < invInvoicesPurchaseList.size(); i++) {
            invFilter = invList.get(i);

            code = invFilter.getSerial();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        invInvoicePurchaseConverter = new InvPurchaseInvoiceConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvOrganizationSite> completOrgSiteAsCustomer(String query) {
        List<InvOrganizationSite> invOrganizationSites = invCustomerOrgSiteList;
        if (query == null || query.trim().equals("")) {

            setInvOrgSiteCustomerConverter(new InvOrganizationSiteConverter(invOrganizationSites));
            return invOrganizationSites;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invCustomerOrgSiteList.size(); i++) {
            invOrgSiteFilter = invOrganizationSites.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        setInvOrgSiteCustomerConverter(new InvOrganizationSiteConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvOrganizationSite> completOrgSiteAsSupplier(String query) {
        List<InvOrganizationSite> invOrganizationSites = invSupplierOrgSiteList;
        if (query == null || query.trim().equals("")) {

            setInvOrgSiteSupplierConverter(new InvOrganizationSiteConverter(invOrganizationSites));
            return invOrganizationSites;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invSupplierOrgSiteList.size(); i++) {
            invOrgSiteFilter = invOrganizationSites.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        setInvOrgSiteSupplierConverter(new InvOrganizationSiteConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvItemDTO> completInvItemDTOFrom(String query) {
        List<InvItemDTO> invList = getInvItemDTOFromList();
        if (query == null || query.trim().equals("")) {

            setItemDTOFromConverter(new ItemDTOConverter(invList));
            return invList;
        }
        List<InvItemDTO> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItemDTO invFilter;
        for (int i = 0; i < getInvItemDTOFromList().size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setItemDTOFromConverter(new ItemDTOConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvItemDTO> completInvItemDTOTo(String query) {
        List<InvItemDTO> invList = getInvItemDTOToList();
        if (query == null || query.trim().equals("")) {

            setItemDTOToConverter(new ItemDTOConverter(invList));
            return invList;
        }
        List<InvItemDTO> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItemDTO invFilter;
        for (int i = 0; i < getInvItemDTOToList().size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setItemDTOToConverter(new ItemDTOConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvItem> completInvItem(String query) {
        List<InvItem> invList = getInvItemList();
        if (query == null || query.trim().equals("")) {

            setItemConverter(new ItemConverter(invList));
            return invList;
        }
        List<InvItem> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItem invFilter;
        for (int i = 0; i < getInvItemList().size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setItemConverter(new ItemConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvItem> completInvItemBak(String query) {
        List<InvItem> invList = getInvItemListBak();
        if (query == null || query.trim().equals("")) {

            setItemConverterBak(new ItemConverter(invList));
            return invList;
        }
        List<InvItem> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItem invFilter;
        for (int i = 0; i < getInvItemListBak().size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setItemConverterBak(new ItemConverter(filteredInvs));
        return filteredInvs;
    }

    /**
     * @return the invInventoryList
     */
    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    /**
     * @param invInventoryList the invInventoryList to set
     */
    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the screenMode
     */
    public String getScreenMode() {
        return screenMode;
    }

    /**
     * @param screenMode the screenMode to set
     */
    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /**
     * @return the invCustomerOrgSiteList
     */
    public List<InvOrganizationSite> getInvCustomerOrgSiteList() {
        return invCustomerOrgSiteList;
    }

    /**
     * @param invCustomerOrgSiteList the invCustomerOrgSiteList to set
     */
    public void setInvCustomerOrgSiteList(List<InvOrganizationSite> invCustomerOrgSiteList) {
        this.invCustomerOrgSiteList = invCustomerOrgSiteList;
    }

    /**
     * @return the invOrgSiteCustomerConverter
     */
    public InvOrganizationSiteConverter getInvOrgSiteCustomerConverter() {
        return invOrgSiteCustomerConverter;
    }

    /**
     * @param invOrgSiteCustomerConverter the invOrgSiteCustomerConverter to set
     */
    public void setInvOrgSiteCustomerConverter(InvOrganizationSiteConverter invOrgSiteCustomerConverter) {
        this.invOrgSiteCustomerConverter = invOrgSiteCustomerConverter;
    }

    /**
     * @return the invSupplierOrgSiteList
     */
    public List<InvOrganizationSite> getInvSupplierOrgSiteList() {
        return invSupplierOrgSiteList;
    }

    /**
     * @param invSupplierOrgSiteList the invSupplierOrgSiteList to set
     */
    public void setInvSupplierOrgSiteList(List<InvOrganizationSite> invSupplierOrgSiteList) {
        this.invSupplierOrgSiteList = invSupplierOrgSiteList;
    }

    /**
     * @return the invOrgSiteSupplierConverter
     */
    public InvOrganizationSiteConverter getInvOrgSiteSupplierConverter() {
        return invOrgSiteSupplierConverter;
    }

    /**
     * @param invOrgSiteSupplierConverter the invOrgSiteSupplierConverter to set
     */
    public void setInvOrgSiteSupplierConverter(InvOrganizationSiteConverter invOrgSiteSupplierConverter) {
        this.invOrgSiteSupplierConverter = invOrgSiteSupplierConverter;
    }

    /**
     * @return the delegatesPurchaseList
     */
    public List<InventoryDelegator> getDelegatesPurchaseList() {
        return delegatesPurchaseList;
    }

    /**
     * @param delegatesPurchaseList the delegatesPurchaseList to set
     */
    public void setDelegatesPurchaseList(List<InventoryDelegator> delegatesPurchaseList) {
        this.delegatesPurchaseList = delegatesPurchaseList;
    }

    /**
     * @return the delegatesPurchaseConverter
     */
    public InvDelegatorConvertor getDelegatesPurchaseConverter() {
        return delegatesPurchaseConverter;
    }

    /**
     * @param delegatesPurchaseConverter the delegatesPurchaseConverter to set
     */
    public void setDelegatesPurchaseConverter(InvDelegatorConvertor delegatesPurchaseConverter) {
        this.delegatesPurchaseConverter = delegatesPurchaseConverter;
    }

    /**
     * @return the delegatesSalesList
     */
    public List<InventoryDelegator> getDelegatesSalesList() {
        return delegatesSalesList;
    }

    /**
     * @param delegatesSalesList the delegatesSalesList to set
     */
    public void setDelegatesSalesList(List<InventoryDelegator> delegatesSalesList) {
        this.delegatesSalesList = delegatesSalesList;
    }

    /**
     * @return the delegatesSalesConverter
     */
    public InvDelegatorConvertor getDelegatesSalesConverter() {
        return delegatesSalesConverter;
    }

    /**
     * @param delegatesSalesConverter the delegatesSalesConverter to set
     */
    public void setDelegatesSalesConverter(InvDelegatorConvertor delegatesSalesConverter) {
        this.delegatesSalesConverter = delegatesSalesConverter;
    }

    /**
     * @return the invInvoicesPurchaseList
     */
    public List<InvPurchaseInvoice> getInvInvoicesPurchaseList() {
        return invInvoicesPurchaseList;
    }

    /**
     * @param invInvoicesPurchaseList the invInvoicesPurchaseList to set
     */
    public void setInvInvoicesPurchaseList(List<InvPurchaseInvoice> invInvoicesPurchaseList) {
        this.invInvoicesPurchaseList = invInvoicesPurchaseList;
    }

    /**
     * @return the invInvoicePurchaseConverter
     */
    public InvPurchaseInvoiceConverter getInvInvoicePurchaseConverter() {
        return invInvoicePurchaseConverter;
    }

    /**
     * @param invInvoicePurchaseConverter the invInvoicePurchaseConverter to set
     */
    public void setInvInvoicePurchaseConverter(InvPurchaseInvoiceConverter invInvoicePurchaseConverter) {
        this.invInvoicePurchaseConverter = invInvoicePurchaseConverter;
    }

    /**
     * @return the invInvoicesSalesList
     */
    public List<InvPurchaseInvoice> getInvInvoicesSalesList() {
        return invInvoicesSalesList;
    }

    /**
     * @param invInvoicesSalesList the invInvoicesSalesList to set
     */
    public void setInvInvoicesSalesList(List<InvPurchaseInvoice> invInvoicesSalesList) {
        this.invInvoicesSalesList = invInvoicesSalesList;
    }

    /**
     * @return the invInvoiceSalesConverter
     */
    public InvPurchaseInvoiceConverter getInvInvoiceSalesConverter() {
        return invInvoiceSalesConverter;
    }

    /**
     * @param invInvoiceSalesConverter the invInvoiceSalesConverter to set
     */
    public void setInvInvoiceSalesConverter(InvPurchaseInvoiceConverter invInvoiceSalesConverter) {
        this.invInvoiceSalesConverter = invInvoiceSalesConverter;
    }

    /**
     * @return the invGroupList
     */
    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    /**
     * @param invGroupList the invGroupList to set
     */
    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    /**
     * @return the groupConverter
     */
    public InvGroupConverter getGroupConverter() {
        return groupConverter;
    }

    /**
     * @param groupConverter the groupConverter to set
     */
    public void setGroupConverter(InvGroupConverter groupConverter) {
        this.groupConverter = groupConverter;
    }

    /**
     * @return the invItemList
     */
    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    /**
     * @param invItemList the invItemList to set
     */
    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    /**
     * @return the itemConverter
     */
    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    /**
     * @param itemConverter the itemConverter to set
     */
    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    /**
     * @return the invItemListBak
     */
    public List<InvItem> getInvItemListBak() {
        return invItemListBak;
    }

    /**
     * @param invItemListBak the invItemListBak to set
     */
    public void setInvItemListBak(List<InvItem> invItemListBak) {
        this.invItemListBak = invItemListBak;
    }

    /**
     * @return the itemConverterBak
     */
    public ItemConverter getItemConverterBak() {
        return itemConverterBak;
    }

    /**
     * @param itemConverterBak the itemConverterBak to set
     */
    public void setItemConverterBak(ItemConverter itemConverterBak) {
        this.itemConverterBak = itemConverterBak;
    }

    /**
     * @return the invItemDTOFromList
     */
    public List<InvItemDTO> getInvItemDTOFromList() {
        return invItemDTOFromList;
    }

    /**
     * @param invItemDTOFromList the invItemDTOFromList to set
     */
    public void setInvItemDTOFromList(List<InvItemDTO> invItemDTOFromList) {
        this.invItemDTOFromList = invItemDTOFromList;
    }

    /**
     * @return the itemDTOFromConverter
     */
    public ItemDTOConverter getItemDTOFromConverter() {
        return itemDTOFromConverter;
    }

    /**
     * @param itemDTOFromConverter the itemDTOFromConverter to set
     */
    public void setItemDTOFromConverter(ItemDTOConverter itemDTOFromConverter) {
        this.itemDTOFromConverter = itemDTOFromConverter;
    }

    /**
     * @return the invItemDTOToList
     */
    public List<InvItemDTO> getInvItemDTOToList() {
        return invItemDTOToList;
    }

    /**
     * @param invItemDTOToList the invItemDTOToList to set
     */
    public void setInvItemDTOToList(List<InvItemDTO> invItemDTOToList) {
        this.invItemDTOToList = invItemDTOToList;
    }

    /**
     * @return the itemDTOToConverter
     */
    public ItemDTOConverter getItemDTOToConverter() {
        return itemDTOToConverter;
    }

    /**
     * @param itemDTOToConverter the itemDTOToConverter to set
     */
    public void setItemDTOToConverter(ItemDTOConverter itemDTOToConverter) {
        this.itemDTOToConverter = itemDTOToConverter;
    }
}
