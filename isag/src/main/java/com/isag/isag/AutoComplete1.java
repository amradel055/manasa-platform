/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.CostCenterDTOConverter;
import com.isag.converter.CurrencyDTOConverter;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.GlAdminUnitDTOConverter;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.HrSalaryEffectDTOConverter;
import com.isag.converter.InstContractConverter;
import com.isag.converter.InstProjectGuideConverter;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InstTransactionTempConverter;
import com.isag.converter.InvDelegatorDTOConvertor;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.ItemDTOConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.converter.RscAccuralDeductionConverter;
import com.isag.converter.RscQotationTenderDTOConverter;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.converter.SymbolConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.converter.TenderDTOConverter;
import com.isag.dto.CostCenterDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.InstTransactionTemp;
import com.isag.views.ItemsBarcodesDetailsView;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;
import org.primefaces.context.RequestContext;

/**
 *
 * @author AHMEDESSAM
 */
@ViewScoped
@Named(value = "autoComplete")
public class AutoComplete1 implements Serializable {

    @EJB
    private AutoCompleteService autoCompleteService;
    private Integer branchId;
    private Integer companyId;
    private Integer remCompanyId;
    private Integer employeeId;
    private Integer userId;
    private HrSalaryEffectDTOConverter hrSalaryEffectDTOConverter;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private InvOrganizationSiteDTOConverter supplierConvertorFrom;
    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;
    private RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor;
    private InvDelegatorDTOConvertor purshaseConvertorFrom;
    private InvInventoryDTOConverter invInventoryDTOConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private SymbolDTOConverter symbolDTOConverter;
    private CostCenterDTOConverter costCenterDTOConverter;
    private GlAdminUnitDTOConverter glAdminUnitDTOConverter;
    private ItemDTOConverter itemDTOConverter;
    private EmployeeDTOConverter employeeDTOConverter;
    private GlBankDTOConverter glBankDTOConverter;

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

    public List<InstProjectGuide> completeInstProject(String query, List<InstProjectGuide> instProjectGuideList, InstProjectGuideConverter instProjectGuideConverter) {

        if (query == null || query.isEmpty()) {
            instProjectGuideConverter = new InstProjectGuideConverter(instProjectGuideList);
            return instProjectGuideList;
        } else {
            instProjectGuideList = autoCompleteService.autoCompleteProjectGuidesActive(getBranchId(), query);
        }
        return instProjectGuideList;
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 0, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<GlBankDTO> completeGlbankByUser(String query) {
        List<GlBankDTO> glBankDTOList = new ArrayList<>();
        glBankDTOList = autoCompleteService.autoCompleteGlbankByUserAndBranchId(getBranchId(), getUserId(), query);
        glBankDTOConverter = new GlBankDTOConverter(glBankDTOList);
        return glBankDTOList;
    }

    public List<InvOrganizationSiteDTO> completeCustomerTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 0, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<ItemsBarcodesDetailsView> completeItemsBarcodesDetailsView(String query) {
        List<ItemsBarcodesDetailsView> autoCompleteInvItem = null;
        if (query.trim().isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg4').show();");
        } else {
            autoCompleteInvItem = autoCompleteService.autoCompleteItemsBarcodesDetailsViewBranchId(getBranchId(), query);
            setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(autoCompleteInvItem));
        }
        return autoCompleteInvItem;
    }

    public List<SymbolDTO> completeItemSymbolView(String query) {
        List<SymbolDTO> autoCompleteSymbol = null;
        autoCompleteSymbol = autoCompleteService.autoCompleteSymbolByCompanyId(getCompanyId(), query);
        setSymbolDTOConverter(new SymbolDTOConverter(autoCompleteSymbol));
        return autoCompleteSymbol;
    }

    public List<InvOrganizationSiteDTO> completeSupplierAndContractorTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 4, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeSupplierFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 1, query);
        setSupplierConvertorFrom(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeLeads(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.FALSE, 5, query, getEmployeeId());
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeLeadsByLeaders(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteWitAssignedList(getBranchId(), Boolean.FALSE, 5, query, getEmployeeId());
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<EmployeeDTO> completeEmployeeIsag(String query) {
        List<EmployeeDTO> employeeDTOList;
        employeeDTOList = autoCompleteService.autoCompleteEmployee(164, query);
        setEmployeeDTOConverter(new EmployeeDTOConverter(employeeDTOList));
        return employeeDTOList;
    }

    public List<InvOrganizationSiteDTO> completeSupplierTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 1, query);
        setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeCustomerAndSupplierFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 2, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeContractorAndSupplierFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteConnectorAndSupplier(getBranchId(), Boolean.TRUE, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeCustomerAndSupplierTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 2, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeContractorFrom(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 4, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InvOrganizationSiteDTO> completeContractorTo(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSite(getBranchId(), Boolean.TRUE, 4, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<InstProjectGuideDTO> completeInstProjectDTOAll(String query) {
        List<InstProjectGuideDTO> instProjectGuides = new ArrayList<>();
        instProjectGuides = autoCompleteService.autoCompleteAllInstProjectGuideDTOActive(getBranchId(), query);
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuides));
        return instProjectGuides;
    }

    public List<InstProjectGuideDTO> completeInstProjectDTOAllWithOutLevel(String query) {
        List<InstProjectGuideDTO> instProjectGuides = new ArrayList<>();
        instProjectGuides = autoCompleteService.autoCompleteAllInstProjectGuideDTOActiveWithOutLevel(getBranchId(), query);
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuides));
        return instProjectGuides;
    }

    public List<InstProjectGuideDTO> completeInstProjectDTOFrom(String query) {
        List<InstProjectGuideDTO> instProjectGuides = new ArrayList<>();
        instProjectGuides = autoCompleteService.autoCompleteInstProjectGuideDTOActive(getBranchId(), query);
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuides));
        return instProjectGuides;
    }

    public List<InstProjectGuideDTO> completeInstProjectDTOTo(String query) {
        List<InstProjectGuideDTO> instProjectGuides = new ArrayList<>();
        instProjectGuides = autoCompleteService.autoCompleteInstProjectGuideDTOActive(getBranchId(), query);
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuides));
        return instProjectGuides;
    }

    public List<RscWorkUnitsGuideDTO> completeRscWorkUnitDTOTo(String query) {
        List<RscWorkUnitsGuideDTO> list = new ArrayList<>();
        list = autoCompleteService.autoCompleteRscWorkUnitDTOActive(getBranchId(), query);
        setRscWorkUnitsGuideDTOConverter(new RscWorkUnitsGuideDTOConverter(list));
        return list;
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

    public List<InvItemDTO> completeItemsData(String query) {
        List<InvItemDTO> autoCompleteInvItem;
        autoCompleteInvItem = autoCompleteService.autoCompleteInvItem(getBranchId(), query);
        setItemDTOConverter(new ItemDTOConverter(autoCompleteInvItem));
        return autoCompleteInvItem;
    }

    public List<HrSalaryEffectDTO> completeHrSalaryEffect(String query) {
        List<HrSalaryEffectDTO> hrSalaryEffectDTOList;
        hrSalaryEffectDTOList = autoCompleteService.autoCompleteHrSalaryEffect(getBranchId(), query);
        setHrSalaryEffectDTOConverter(new HrSalaryEffectDTOConverter(hrSalaryEffectDTOList));
        return hrSalaryEffectDTOList;
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

    public static List<InstTransactionTemp> completInstTransactionView(String query, List<InstTransactionTemp> instTransactionTempList, InstTransactionTempConverter instTransactionTempConverter) {
        List<InstTransactionTemp> instTransactionTemp_List = instTransactionTempList;
        if (query == null || query.trim().equals("")) {

            instTransactionTempConverter = new InstTransactionTempConverter(instTransactionTemp_List);
            return instTransactionTemp_List;
        }
        List<InstTransactionTemp> filteredInstTransactionTemp = new ArrayList<>();

        String nameAr;
        String code;
        InstTransactionTemp instTransactionTempFilter;

        for (int i = 0; i < instTransactionTempList.size(); i++) {
            instTransactionTempFilter = instTransactionTemp_List.get(i);
            nameAr = new SimpleDateFormat("yyyy-MM-dd").format(instTransactionTempFilter.getDueDate());
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstTransactionTemp.contains(instTransactionTempFilter)) {
                    filteredInstTransactionTemp.add(instTransactionTempFilter);
                }
            }

            code = instTransactionTempFilter.getRemaining().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstTransactionTemp.contains(instTransactionTempFilter)) {
                    filteredInstTransactionTemp.add(instTransactionTempFilter);
                }
            }
        }

        instTransactionTempConverter = new InstTransactionTempConverter(filteredInstTransactionTemp);
        return filteredInstTransactionTemp;
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
            String code;
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
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
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

    public static List<RscTenderDTO> completeTender(String query, List<RscTenderDTO> rscTenderDTOList, TenderDTOConverter tenderDTOConverter) {
        List<RscTenderDTO> Tender = rscTenderDTOList;
        if (query == null || query.trim().equals("")) {
            tenderDTOConverter = new TenderDTOConverter(Tender);
            return Tender;
        }
        List<RscTenderDTO> filteredTender = new ArrayList<>();

        String nameAr;
        String code;
        RscTenderDTO tender;
        for (int i = 0; i < rscTenderDTOList.size(); i++) {
            tender = rscTenderDTOList.get(i);
            nameAr = tender.getCustomerId().getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredTender.contains(tender)) {
                    filteredTender.add(tender);
                }
            }

            code = tender.getCustomerId().getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredTender.contains(tender)) {
                    filteredTender.add(tender);
                }
            }
        }

        tenderDTOConverter = new TenderDTOConverter(filteredTender);
        return filteredTender;
    }

    public static List<RscQotationPriceTenderDTO> completeQutation(String query, List<RscQotationPriceTenderDTO> rscQotationPriceTenderList, RscQotationTenderDTOConverter rscQotationTenderDTOConverter) {
        List<RscQotationPriceTenderDTO> qotationPriceTender = rscQotationPriceTenderList;
        if (query == null || query.trim().equals("")) {
            rscQotationTenderDTOConverter = new RscQotationTenderDTOConverter(qotationPriceTender);
            return qotationPriceTender;
        }
        List<RscQotationPriceTenderDTO> filteredTender = new ArrayList<>();

        String nameAr;
        String code;
        RscQotationPriceTenderDTO tender;
        for (int i = 0; i < rscQotationPriceTenderList.size(); i++) {
            tender = rscQotationPriceTenderList.get(i);
            nameAr = tender.getCustomerId().getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredTender.contains(tender)) {
                    filteredTender.add(tender);
                }
            }

            code = tender.getCustomerId().getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredTender.contains(tender)) {
                    filteredTender.add(tender);
                }
            }
        }

        rscQotationTenderDTOConverter = new RscQotationTenderDTOConverter(filteredTender);
        return filteredTender;
    }

    public static List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideDTO(String query, List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList, RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter) {
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideDTO = rscWorkUnitsGuideDTOList;
        if (query == null || query.trim().equals("")) {
            rscWorkUnitsGuideDTOConverter = new RscWorkUnitsGuideDTOConverter(RscWorkUnitsGuideDTO);
            return RscWorkUnitsGuideDTO;
        }
        List<RscWorkUnitsGuideDTO> filteredWorkUnitsGuideDTO = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO UnitsGuideDTO;
        for (int i = 0; i < rscWorkUnitsGuideDTOList.size(); i++) {
            UnitsGuideDTO = RscWorkUnitsGuideDTO.get(i);
            nameAr = UnitsGuideDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredWorkUnitsGuideDTO.contains(UnitsGuideDTO)) {
                    filteredWorkUnitsGuideDTO.add(UnitsGuideDTO);
                }
            }

            code = UnitsGuideDTO.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredWorkUnitsGuideDTO.contains(UnitsGuideDTO)) {
                    filteredWorkUnitsGuideDTO.add(UnitsGuideDTO);
                }
            }
        }

        rscWorkUnitsGuideDTOConverter = new RscWorkUnitsGuideDTOConverter(filteredWorkUnitsGuideDTO);
        return filteredWorkUnitsGuideDTO;
    }

    public static List<RscAccrualDeductionDTO> completeAccuralOrDeduction(String query, List<RscAccrualDeductionDTO> generalAccuralList, RscAccuralDeductionConverter rscAccuralConverter) {
        List<RscAccrualDeductionDTO> generalAccural = generalAccuralList;
        if (query == null || query.trim().equals("")) {
            rscAccuralConverter = new RscAccuralDeductionConverter(generalAccural);
            return generalAccural;
        }
        List<RscAccrualDeductionDTO> filteredAccrual = new ArrayList<>();

        String nameAr;
        Integer code;
        RscAccrualDeductionDTO accrualDeductionDTO;
        for (int i = 0; i < generalAccuralList.size(); i++) {
            accrualDeductionDTO = generalAccural.get(i);
            nameAr = accrualDeductionDTO.getRscAccrualDeductionName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredAccrual.contains(accrualDeductionDTO)) {
                    filteredAccrual.add(accrualDeductionDTO);
                }
            }

            code = accrualDeductionDTO.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredAccrual.contains(accrualDeductionDTO)) {
                    filteredAccrual.add(accrualDeductionDTO);
                }
            }
        }

        rscAccuralConverter = new RscAccuralDeductionConverter(filteredAccrual);
        return filteredAccrual;
    }

    public static List<InstContract> completInstContract(String query, List<InstContract> instContractList, InstContractConverter instContractConverter) {
        try {
            List<InstContract> instContract = instContractList;
            if (query == null || query.trim().equals("")) {

                instContractConverter = new InstContractConverter(instContract);
                return instContract;
            }
            List<InstContract> filteredInst = new ArrayList<>();

            String nameAr;
            String code;
            InstContract instConstructFilter;

            for (int i = 0; i < instContractList.size(); i++) {
                instConstructFilter = instContract.get(i);
                nameAr = instConstructFilter.getProjectId().getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInst.contains(instConstructFilter)) {
                        filteredInst.add(instConstructFilter);
                    }
                }

                code = instConstructFilter.getSerial().toString();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInst.contains(instConstructFilter)) {
                        filteredInst.add(instConstructFilter);
                    }
                }
            }

            instContractConverter = new InstContractConverter(filteredInst);
            return filteredInst;
        } catch (Exception e) {
//           
            return null;
        }
    }

    private Integer getBranchId() {
        if (branchId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            IsagUser isagUser = (IsagUser) externalContext.getSessionMap().get("userlogin");
            branchId = isagUser.getBranchId().getId();
        }
        return branchId;
    }

    private Integer getUserId() {
        if (userId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            IsagUser isagUser = (IsagUser) externalContext.getSessionMap().get("userlogin");
            userId = isagUser.getId();
        }
        return userId;
    }

    private Integer getEmployeeId() {
        if (employeeId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            UserData userDate = (UserData) externalContext.getSessionMap().get("userLogInData");
            employeeId = userDate.getEmployeeDTODefault().getId();
        }
        return employeeId;
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
     * @return the instProjectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideDTOConverter() {
        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri != null && uri.contains("leads")) {
            setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(autoCompleteService.autoCompleteAllInstProjectGuideDTOActive(getBranchId(), "")));
        }
        return instProjectGuideDTOConverter;
    }

    /**
     * @param instProjectGuideDTOConverter the instProjectGuideDTOConverter to
     * set
     */
    public void setInstProjectGuideDTOConverter(InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        this.instProjectGuideDTOConverter = instProjectGuideDTOConverter;
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
        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("leads")) {
            setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(autoCompleteService.autoCompleteAllInstProjectGuideDTOActive(getBranchId(), "")));
        }
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
     * @return the employeeDTOConverter
     */
    public EmployeeDTOConverter getEmployeeDTOConverter() {
        return employeeDTOConverter;
    }

    /**
     * @param employeeDTOConverter the employeeDTOConverter to set
     */
    public void setEmployeeDTOConverter(EmployeeDTOConverter employeeDTOConverter) {
        this.employeeDTOConverter = employeeDTOConverter;
    }

    /**
     * @return the rscWorkUnitsGuideDTOConverter
     */
    public RscWorkUnitsGuideDTOConverter getRscWorkUnitsGuideDTOConverter() {
        return rscWorkUnitsGuideDTOConverter;
    }

    /**
     * @param rscWorkUnitsGuideDTOConverter the rscWorkUnitsGuideDTOConverter to
     * set
     */
    public void setRscWorkUnitsGuideDTOConverter(RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter) {
        this.rscWorkUnitsGuideDTOConverter = rscWorkUnitsGuideDTOConverter;
    }

    /**
     * @return the hrSalaryEffectDTOConverter
     */
    public HrSalaryEffectDTOConverter getHrSalaryEffectDTOConverter() {
        return hrSalaryEffectDTOConverter;
    }

    /**
     * @param hrSalaryEffectDTOConverter the hrSalaryEffectDTOConverter to set
     */
    public void setHrSalaryEffectDTOConverter(HrSalaryEffectDTOConverter hrSalaryEffectDTOConverter) {
        this.hrSalaryEffectDTOConverter = hrSalaryEffectDTOConverter;
    }

    /**
     * @return the glBankDTOConverter
     */
    public GlBankDTOConverter getGlBankDTOConverter() {
        return glBankDTOConverter;
    }

    /**
     * @param glBankDTOConverter the glBankDTOConverter to set
     */
    public void setGlBankDTOConverter(GlBankDTOConverter glBankDTOConverter) {
        this.glBankDTOConverter = glBankDTOConverter;
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

    private Integer getCompanyId() {
        if (companyId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            IsagUser isagUser = (IsagUser) externalContext.getSessionMap().get("userlogin");
            companyId = isagUser.getCompanyId().getId();
        }
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the symbolDTOConverter
     */
    public SymbolDTOConverter getSymbolDTOConverter() {
        return symbolDTOConverter;
    }

    /**
     * @param symbolDTOConverter the symbolDTOConverter to set
     */
    public void setSymbolDTOConverter(SymbolDTOConverter symbolDTOConverter) {
        this.symbolDTOConverter = symbolDTOConverter;
    }

}
