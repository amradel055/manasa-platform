/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.OfferCompanyService;
import com.toby.businessservice.ProDetailTypeService;
import com.toby.businessservice.ProGroupService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.entity.TobyUser;
import com.toby.converter.CostCenterDTOConverter;
import com.toby.converter.CurrencyDTOConverter;
import com.toby.converter.GlAdminUnitDTOConverter;
import com.toby.converter.GlBankDTOConverter;
import com.toby.converter.InvDelegatorDTOConvertor;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.InvInvoiceDTOConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.ItemDTOConverter;
import com.toby.converter.ItemsBarcodesDetailsViewConverter;
import com.toby.converter.OfferCompanyDTOConverter;
import com.toby.converter.ProDetailTypeDTOConverter;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.SymbolConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Symbol;
import com.toby.views.ItemsBarcodesDetailsView;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author AHMEDESSAM
 */
@ViewScoped
@Named(value = "autoComplete")
public class AutoComplete1 extends Basic {

    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private ProDetailTypeService proDetailTypeService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProGroupService proGroupService;
    @EJB
    private OfferCompanyService offerCompanyService;
    
    private Integer branchId;
    private ProDetailTypeDTOConverter proDetailTypeDTOConverter;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private InvOrganizationSiteDTOConverter supplierConvertorFrom;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor;
    private InvDelegatorDTOConvertor purshaseConvertorFrom;
    private InvInventoryDTOConverter invInventoryDTOConverter;
    private CostCenterDTOConverter costCenterDTOConverter;
    private GlAdminUnitDTOConverter glAdminUnitDTOConverter;
    private ItemDTOConverter itemDTOConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private InvInvoiceDTOConverter invoiceDTOConverter;
    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private ProGroupDTOConverter proGroupDTOConverter;
    private OfferCompanyDTOConverter offerCompanyDTOConverter;
//    InvInvoiceDTO

    public static List<InvOrganizationSite> completOrgSite(String query, List<InvOrganizationSite> invOrgSites, InvOrganizationSiteConverter invOrgSiteConverter) {

        List<InvOrganizationSite> invOrgSiteList = invOrgSites;
        if (query == null || query.trim().equals("")) {

            invOrgSiteConverter = new InvOrganizationSiteConverter(invOrgSites);
            return invOrgSiteList;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invOrgSites.size(); i++) {
            invOrgSiteFilter = invOrgSiteList.get(i);
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

        invOrgSiteConverter = new InvOrganizationSiteConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvInvoiceDTO> completeProWorkOrder(String query) {
        List<InvInvoiceDTO> list = new ArrayList<>();
        list = autoCompleteService.autoCompleteProWorkOrderByBranch(getBranchId(), query);
//        setProDetailTypeDTOConverter(new ProDetailTypeDTOConverter(proDetailTypeDTOList));
        return list;

    }

    public List<ProDetailTypeDTO> proDetailType(String query) {
        List<ProDetailTypeDTO> proDetailTypeDTOList = new ArrayList<>();
        proDetailTypeDTOList = proDetailTypeService.getallProDetailTypeByBranchId(getBranchId(), query);
        setProDetailTypeDTOConverter(new ProDetailTypeDTOConverter(proDetailTypeDTOList));
        return proDetailTypeDTOList;

    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 0, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvItemDTO> completeItemsData(String query) {
        List<InvItemDTO> autoCompleteInvItem;
        autoCompleteInvItem = autoCompleteService.autoCompleteInvItem(getBranchId(), query);
        setItemDTOConverter(new ItemDTOConverter(autoCompleteInvItem));
        return autoCompleteInvItem;
    }

    public List<ItemsBarcodesDetailsView> completeItemsBarcodesDetailsView(String query) {
        List<ItemsBarcodesDetailsView> autoCompleteInvItem;
        autoCompleteInvItem = autoCompleteService.autoCompleteItemsBarcodesDetailsViewBranchId(getBranchId(), query);
        setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(autoCompleteInvItem));
        return autoCompleteInvItem;
    }

    public List<InvOrganizationSiteDTO> completeCustomerTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 0, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeSupplierFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 1, query,null);
        setSupplierConvertorFrom(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeSupplierTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 1, query,null);
        setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeCustomerAndSupplierFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 2, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeCustomerAndSupplierTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 2, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvInvoiceDTO> completeInvPurchaseForFactoryDTO(String query) {
        List<InvInvoiceDTO> list = new ArrayList<>();
        list = autoCompleteService.autoCompleteInvPurchaseForFactoryDTO(getBranchId(), query);
        setInvoiceDTOConverter(new InvInvoiceDTOConverter(list));
        return list;

    }

    public List<InvOrganizationSiteDTO> completeContractorFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 4, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeContractorTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getBranchId(), Boolean.TRUE, 4, query,null);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InventoryDelegatorDTO> completePurshaseFrom(String query) {
        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
        inventoryDelegatorDTOList = autoCompleteService.autoCompleteInventoryDelegatorByBranch(getBranchId(), 0, query);
        setPurshaseConvertorFrom(new InvDelegatorDTOConvertor(inventoryDelegatorDTOList));
        return inventoryDelegatorDTOList;
    }

    public List<InventoryDelegatorDTO> completePurshaseTo(String query) {
        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
        inventoryDelegatorDTOList = autoCompleteService.autoCompleteInventoryDelegatorByBranch(getBranchId(), 0, query);
        invDelegatorDTOConvertor = new InvDelegatorDTOConvertor(inventoryDelegatorDTOList);
        return inventoryDelegatorDTOList;
    }

    public List<InventoryDelegatorDTO> completeSalesFrom(String query) {
        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
        inventoryDelegatorDTOList = autoCompleteService.autoCompleteInventoryDelegatorByBranch(getBranchId(), 1, query);
        invDelegatorDTOConvertor = new InvDelegatorDTOConvertor(inventoryDelegatorDTOList);
        return inventoryDelegatorDTOList;
    }

    public List<InventoryDelegatorDTO> completeSalesTo(String query) {
        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
        inventoryDelegatorDTOList = autoCompleteService.autoCompleteInventoryDelegatorByBranch(getBranchId(), 1, query);
        invDelegatorDTOConvertor = new InvDelegatorDTOConvertor(inventoryDelegatorDTOList);
        return inventoryDelegatorDTOList;
    }

    public List<CostCenterDTO> completeCostCentersFrom(String query) {
        List<CostCenterDTO> costCenterDTOList = new ArrayList<>();
        costCenterDTOList = autoCompleteService.autoCompleteActiveSubCostCentersByBranch(getBranchId(), query);
        setCostCenterDTOConverter(new CostCenterDTOConverter(costCenterDTOList));
        return costCenterDTOList;
    }

    public List<CostCenterDTO> completeCostCentersTo(String query) {
        List<CostCenterDTO> costCenterDTOList = new ArrayList<>();
        costCenterDTOList = autoCompleteService.autoCompleteActiveSubCostCentersByBranch(getBranchId(), query);
        setCostCenterDTOConverter(new CostCenterDTOConverter(costCenterDTOList));
        return costCenterDTOList;
    }

    public List<GlAdminUnitDTO> completeAdminUnitsFrom(String query) {
        List<GlAdminUnitDTO> glAdminUnitDTOList = new ArrayList<>();
        glAdminUnitDTOList = autoCompleteService.autoCompleteSubAdminUnitByBranchIdActiveDTO(getBranchId(), query);
        glAdminUnitDTOConverter = new GlAdminUnitDTOConverter(glAdminUnitDTOList);
        return glAdminUnitDTOList;
    }

    public List<GlAdminUnitDTO> completeAdminUnitsTo(String query) {
        List<GlAdminUnitDTO> glAdminUnitDTOList = new ArrayList<>();
        glAdminUnitDTOList = autoCompleteService.autoCompleteSubAdminUnitByBranchIdActiveDTO(getBranchId(), query);
        glAdminUnitDTOConverter = new GlAdminUnitDTOConverter(glAdminUnitDTOList);
        return glAdminUnitDTOList;
    }

    public List<InvInventoryDTO> completeGallaryDTO(String query) {
        List<InvInventoryDTO> invInventoryDTOList = new ArrayList<>();
        invInventoryDTOList = autoCompleteService.getGallaryByBranchId(getBranchId(), query);
        invInventoryDTOConverter = new InvInventoryDTOConverter(invInventoryDTOList);
        return invInventoryDTOList;
    }

    public List<InvInventoryDTO> completeGallaryForUser(String query) {
        List<InvInventoryDTO> invInventoryDTOList = new ArrayList<>();
        invInventoryDTOList = getUserData().getGallaryDTOList();
        invInventoryDTOConverter = new InvInventoryDTOConverter(invInventoryDTOList);
        return invInventoryDTOList;
    }

    public List<ProProductionStagesDTO> completeProProductionStagesDTO(String query) {
        List<ProProductionStagesDTO> proProductionStagesDTOList = new ArrayList<>();
        proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser(),getUserData().getUserBranch().getId());
        setProProductionStagesDTOConverter(new ProProductionStagesDTOConverter(proProductionStagesDTOList));
        return proProductionStagesDTOList;
    }

    public List<ProGroupDTO> completeProGroup(String query) {
        List<ProGroupDTO> proGroups = new ArrayList<>();
        proGroups = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUser());
        setProGroupDTOConverter(new ProGroupDTOConverter(proGroups));
        return proGroups;
    }
    
    public List<OfferCompanyDTO> completeOfferCompany(String query) {
        List<OfferCompanyDTO> offerCompanyDTOList = new ArrayList<>();
        offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
        setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyDTOList));
        return offerCompanyDTOList;
    }

    public static List<Symbol> completeSymbol(String query, List<Symbol> symbolList, SymbolConverter symbolConverter) {
        List<Symbol> list = new ArrayList<>();
        try {
            List<Symbol> symbols = symbolList;
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            Symbol symbol;
            for (int i = 0; i < symbolList.size(); i++) {
                symbol = symbols.get(i);
                nameAr = symbol.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
                    list.add(symbol);
                }

                code = symbol.getId();
                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
                    list.add(symbol);
                }
            }

            symbolConverter = new SymbolConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

    public static List<ItemsBarcodesDetailsView> completeItemsData(String query, List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList, ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();
        try {

            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailsViewList;
            if (query == null || query.trim().equals("")) {
                itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }

            String nameAr;
            String code;
            ItemsBarcodesDetailsView barcodesDetailsView;
            for (int i = 0; i < itemsBarcodesDetailsViewList.size(); i++) {
                barcodesDetailsView = itemsBarcodesDetailsViews.get(i);
                nameAr = barcodesDetailsView.getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }

                code = barcodesDetailsView.getBarcode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }

            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(filteredItems);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeItemsData");
        }
        return filteredItems;
    }

    public static List<CurrencyDTO> completeCurrency(String query, List<CurrencyDTO> currencyList, CurrencyDTOConverter currencyConverter) {
        List<CurrencyDTO> filteredCurrencies = new ArrayList<>();
        try {
            List<CurrencyDTO> currencyListF = currencyList;
            if (query == null || query.trim().equals("")) {

                currencyConverter = new CurrencyDTOConverter(currencyListF);
                return currencyListF;
            }

            String nameAr;
            String code;
            CurrencyDTO currencyFilter;
            for (int i = 0; i < currencyList.size(); i++) {
                currencyFilter = currencyListF.get(i);
                nameAr = currencyFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrencies.contains(currencyFilter)) {
                        filteredCurrencies.add(currencyFilter);
                    }
                }

                code = currencyFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrencies.contains(currencyFilter)) {
                        filteredCurrencies.add(currencyFilter);
                    }
                }
            }

            currencyConverter = new CurrencyDTOConverter(filteredCurrencies);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeCurrency");
        }
        return filteredCurrencies;
    }

    public static List<GlBankDTO> completeGlBank(String query, List<GlBankDTO> glBankList, GlBankDTOConverter glBankConverter) {
        List<GlBankDTO> filteredGlBanks = new ArrayList<>();
        try {
            List<GlBankDTO> glBanksList = glBankList;
            if (query == null || query.trim().equals("")) {

                glBankConverter = new GlBankDTOConverter(glBanksList);
                return glBanksList;
            }

            String nameAr;
            Integer code;
            GlBankDTO glBankFilter;
            for (int i = 0; i < glBankList.size(); i++) {
                glBankFilter = glBanksList.get(i);
                nameAr = glBankFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }

                code = glBankFilter.getCode();
                if (code != null ) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }

            }

            glBankConverter = new GlBankDTOConverter(filteredGlBanks);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeGlBank");
        }
        return filteredGlBanks;
    }

    public static List<SymbolDTO> completeSymbolDTO(String query, List<SymbolDTO> symbolList, SymbolDTOConverter symbolConverter) {
        List<SymbolDTO> list = new ArrayList<>();
        try {
            List<SymbolDTO> symbols = symbolList;
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolDTOConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            SymbolDTO symbol;
            for (int i = 0; i < symbolList.size(); i++) {
                symbol = symbols.get(i);
                nameAr = symbol.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
                    list.add(symbol);
                }

                code = symbol.getId();
                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
                    list.add(symbol);
                }
            }

            symbolConverter = new SymbolDTOConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

    private Integer getBranchId() {
        if (branchId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            TobyUser tobyUser = (TobyUser) externalContext.getSessionMap().get("userlogin");
            branchId = tobyUser.getBranchId().getId();
        }
        return branchId;
    }

    /**
     * @return the organizationSiteDTOConvertor
     */
    public InvOrganizationSiteDTOConverter getOrganizationSiteDTOConvertor() {
        return organizationSiteDTOConvertor;
    }

    /**
     * @param organizationSiteDTOConvertor the organizationSiteDTOConvertor to
     * set
     */
    public void setOrganizationSiteDTOConvertor(InvOrganizationSiteDTOConverter organizationSiteDTOConvertor) {
        this.organizationSiteDTOConvertor = organizationSiteDTOConvertor;
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

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the costCenterDTOConverter
     */
    public CostCenterDTOConverter getCostCenterDTOConverter() {
        return costCenterDTOConverter;
    }

    /**
     * @param costCenterDTOConverter the costCenterDTOConverter to set
     */
    public void setCostCenterDTOConverter(CostCenterDTOConverter costCenterDTOConverter) {
        this.costCenterDTOConverter = costCenterDTOConverter;
    }

    /**
     * @return the glAdminUnitDTOConverter
     */
    public GlAdminUnitDTOConverter getGlAdminUnitDTOConverter() {
        return glAdminUnitDTOConverter;
    }

    /**
     * @param glAdminUnitDTOConverter the glAdminUnitDTOConverter to set
     */
    public void setGlAdminUnitDTOConverter(GlAdminUnitDTOConverter glAdminUnitDTOConverter) {
        this.glAdminUnitDTOConverter = glAdminUnitDTOConverter;
    }

    /**
     * @return the supplierConvertorFrom
     */
    public InvOrganizationSiteDTOConverter getSupplierConvertorFrom() {
        return supplierConvertorFrom;
    }

    /**
     * @param supplierConvertorFrom the supplierConvertorFrom to set
     */
    public void setSupplierConvertorFrom(InvOrganizationSiteDTOConverter supplierConvertorFrom) {
        this.supplierConvertorFrom = supplierConvertorFrom;
    }

    /**
     * @return the purshaseConvertorFrom
     */
    public InvDelegatorDTOConvertor getPurshaseConvertorFrom() {
        return purshaseConvertorFrom;
    }

    /**
     * @param purshaseConvertorFrom the purshaseConvertorFrom to set
     */
    public void setPurshaseConvertorFrom(InvDelegatorDTOConvertor purshaseConvertorFrom) {
        this.purshaseConvertorFrom = purshaseConvertorFrom;
    }

    /**
     * @return the itemDTOConverter
     */
    public ItemDTOConverter getItemDTOConverter() {
        return itemDTOConverter;
    }

    /**
     * @param itemDTOConverter the itemDTOConverter to set
     */
    public void setItemDTOConverter(ItemDTOConverter itemDTOConverter) {
        this.itemDTOConverter = itemDTOConverter;
    }

    /**
     * @return the itemsBarcodesDetailsViewConverter
     */
    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }

    /**
     * @param itemsBarcodesDetailsViewConverter the
     * itemsBarcodesDetailsViewConverter to set
     */
    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }

    /**
     * @return the proDetailTypeDTOConverter
     */
    public ProDetailTypeDTOConverter getProDetailTypeDTOConverter() {
        return proDetailTypeDTOConverter;
    }

    /**
     * @param proDetailTypeDTOConverter the proDetailTypeDTOConverter to set
     */
    public void setProDetailTypeDTOConverter(ProDetailTypeDTOConverter proDetailTypeDTOConverter) {
        this.proDetailTypeDTOConverter = proDetailTypeDTOConverter;
    }

    /**
     * @return the invoiceDTOConverter
     */
    public InvInvoiceDTOConverter getInvoiceDTOConverter() {
        return invoiceDTOConverter;
    }

    /**
     * @param invoiceDTOConverter the invoiceDTOConverter to set
     */
    public void setInvoiceDTOConverter(InvInvoiceDTOConverter invoiceDTOConverter) {
        this.invoiceDTOConverter = invoiceDTOConverter;
    }

    /**
     * @return the proProductionStagesDTOConverter
     */
    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    /**
     * @param proProductionStagesDTOConverter the
     * proProductionStagesDTOConverter to set
     */
    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    /**
     * @return the proGroupDTOConverter
     */
    public ProGroupDTOConverter getProGroupDTOConverter() {
        return proGroupDTOConverter;
    }

    /**
     * @param proGroupDTOConverter the proGroupDTOConverter to set
     */
    public void setProGroupDTOConverter(ProGroupDTOConverter proGroupDTOConverter) {
        this.proGroupDTOConverter = proGroupDTOConverter;
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
