/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.converter.CostCenterDTOConverter;
import com.isag.converter.CurrencyDTOConverter;
import com.isag.converter.DepartmentDTOConverter;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.GlAdminUnitDTOConverter;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.converter.InstProjectGuideConverter;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InstTransactionTempConverter;
import com.isag.converter.InvDelegatorDTOConvertor;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.converter.RscAccuralDeductionConverter;
import com.isag.converter.RscQotationTenderDTOConverter;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.converter.SponsorDTOConverter;
import com.isag.converter.SymbolConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.converter.TenderDTOConverter;
import com.isag.converter.hrSalaryEffectTypeConvertor;
import com.isag.dto.CostCenterDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.DepartmentDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.dto.HrSponsorDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GlAccount;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.Symbol;
import com.isag.views.InstTransactionTemp;
import com.isag.views.ItemsBarcodesDetailsView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author amr
 */
public class AutoComplete {

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

    public static List<InstProjectGuide> completeInstProject(String query, List<InstProjectGuide> instProjectGuideList, InstProjectGuideConverter instProjectGuideConverter) {
        List<InstProjectGuide> instProjectGuides = instProjectGuideList;
        if (query == null || query.trim().equals("")) {

            instProjectGuideConverter = new InstProjectGuideConverter(instProjectGuides);
            return instProjectGuides;
        }
        List<InstProjectGuide> filteredInstProject = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuide instProjectGuideFilter;

        for (int i = 0; i < instProjectGuideList.size(); i++) {
            instProjectGuideFilter = instProjectGuides.get(i);
            nameAr = instProjectGuideFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }

            code = instProjectGuideFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }
        }

        instProjectGuideConverter = new InstProjectGuideConverter(filteredInstProject);
        return filteredInstProject;
    }

    public static List<InvOrganizationSiteDTO> completOrgSiteDTO(String query, List<InvOrganizationSiteDTO> invOrgSitesDTO, InvOrganizationSiteDTOConverter invOrgSiteConverterDTO) {
        List<InvOrganizationSiteDTO> invOrgSiteList = invOrgSitesDTO;
        if (query == null || query.trim().equals("")) {

            invOrgSiteConverterDTO = new InvOrganizationSiteDTOConverter(invOrgSiteList);
            return invOrgSiteList;
        }
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSiteDTO invOrgSiteFilter;

        for (int i = 0; i < invOrgSitesDTO.size(); i++) {
            invOrgSiteFilter = invOrgSiteList.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        invOrgSiteConverterDTO = new InvOrganizationSiteDTOConverter(filteredInvs);
        return filteredInvs;
    }

    public static List<InstProjectGuideDTO> completeInstProjectDTO(String query, List<InstProjectGuideDTO> instProjectGuideDTOList, InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        List<InstProjectGuideDTO> instProjectGuides = instProjectGuideDTOList;
        if (query == null || query.trim().equals("")) {

            instProjectGuideDTOConverter = new InstProjectGuideDTOConverter(instProjectGuides);
            return instProjectGuides;
        }
        List<InstProjectGuideDTO> filteredInstProject = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuideDTO instProjectGuideFilter;

        for (int i = 0; i < instProjectGuideDTOList.size(); i++) {
            instProjectGuideFilter = instProjectGuides.get(i);
            nameAr = instProjectGuideFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }

            code = instProjectGuideFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }
        }

        instProjectGuideDTOConverter = new InstProjectGuideDTOConverter(filteredInstProject);
        return filteredInstProject;
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

    public static List<CostCenterDTO> completeCostCenters(String query, List<CostCenterDTO> costCenterDTOList, CostCenterDTOConverter costCenterDTOConverter) {
        List<CostCenterDTO> filteredCostCenters = new ArrayList<>();
        try {
            List<CostCenterDTO> centersList = costCenterDTOList;
            if (query == null || query.trim().equals("")) {

                costCenterDTOConverter = new CostCenterDTOConverter(centersList);
                return centersList;
            }

            String nameAr;
            Integer code;
            CostCenterDTO costCenterFilter;
            for (int i = 0; i < costCenterDTOList.size(); i++) {
                costCenterFilter = centersList.get(i);
                nameAr = costCenterFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }

                code = costCenterFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }
            }

            costCenterDTOConverter = new CostCenterDTOConverter(filteredCostCenters);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeCostCenters");
        }
        return filteredCostCenters;
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

    public static List<GlAdminUnitDTO> completeAdminUnits(String query, List<GlAdminUnitDTO> adminUnitList, GlAdminUnitDTOConverter adminUnitConverter) {
        List<GlAdminUnitDTO> filteredAdminUnits = new ArrayList<>();
        try {
            List<GlAdminUnitDTO> adminList = adminUnitList;
            if (query == null || query.trim().equals("")) {

                adminUnitConverter = new GlAdminUnitDTOConverter(adminList);
                return adminList;
            }

            String nameAr;
            Integer code;
            GlAdminUnitDTO currencyFilter;
            for (int i = 0; i < adminUnitList.size(); i++) {
                currencyFilter = adminList.get(i);
                nameAr = currencyFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredAdminUnits.contains(currencyFilter)) {
                        filteredAdminUnits.add(currencyFilter);
                    }
                }

                code = currencyFilter.getCode();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredAdminUnits.contains(currencyFilter)) {
                        filteredAdminUnits.add(currencyFilter);
                    }
                }
            }

            adminUnitConverter = new GlAdminUnitDTOConverter(filteredAdminUnits);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeAdminUnits");
        }
        return filteredAdminUnits;
    }

    public static List<InvInventoryDTO> completeInventoryDTO(String query, List<InvInventoryDTO> invInventoryList, InvInventoryDTOConverter invInventoryConverter) {
        List<InvInventoryDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvInventoryDTO> invList = invInventoryList;
            if (query == null || query.trim().equals("")) {

                invInventoryConverter = new InvInventoryDTOConverter(invList);
                return invList;
            }

            String nameAr;
            String code;
            InvInventoryDTO invFilter;
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

            invInventoryConverter = new InvInventoryDTOConverter(filteredInvs);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeInventoryDTO");
        }
        return filteredInvs;
    }

    public static List<InventoryDelegatorDTO> completeDelegatorDTO(String query, List<InventoryDelegatorDTO> purchasePersonList, InvDelegatorDTOConvertor purchasePersonConvertor) {
        List<InventoryDelegatorDTO> filteredInvs = new ArrayList<>();
        try {

            List<InventoryDelegatorDTO> invList = purchasePersonList;
            if (query == null || query.trim().equals("")) {

                purchasePersonConvertor = new InvDelegatorDTOConvertor(invList);
                return invList;
            }

            String nameAr;
            String code;
            InventoryDelegatorDTO invFilter;
            for (int i = 0; i < purchasePersonList.size(); i++) {
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

            purchasePersonConvertor = new InvDelegatorDTOConvertor(filteredInvs);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "completeDelegatorDTO");
        }
        return filteredInvs;
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

    public static List<GlAccount> completeGlAccount(String query, List<GlAccount> glAccountList, GlaccountConverter glaccountConverter) {
        List<GlAccount> generalGlAccount = glAccountList;
        if (query == null || query.trim().equals("")) {
            glaccountConverter = new GlaccountConverter(generalGlAccount);
            return generalGlAccount;
        }
        List<GlAccount> filteredGlAccount = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAccount glAccount;
        for (int i = 0; i < generalGlAccount.size(); i++) {
            glAccount = generalGlAccount.get(i);
            nameAr = glAccount.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlAccount.contains(glAccount)) {
                    filteredGlAccount.add(glAccount);
                }
            }

            code = glAccount.getAccNumber();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlAccount.contains(glAccount)) {
                    filteredGlAccount.add(glAccount);
                }
            }
        }

        glaccountConverter = new GlaccountConverter(filteredGlAccount);
        return filteredGlAccount;
    }

        ////// HR ////////
    public static List<DepartmentDTO> completeDepartment(String query, List<DepartmentDTO> departmentDTOList, DepartmentDTOConverter departmentDTOConverter) {
        List<DepartmentDTO> Department = departmentDTOList;
        if (query == null || query.trim().equals("")) {
            departmentDTOConverter = new DepartmentDTOConverter(Department);
            return Department;
        }
        List<DepartmentDTO> filteredDepartment = new ArrayList<>();

        String nameAr;
        Integer code;
        DepartmentDTO departmentSymbol;
        for (int i = 0; i < departmentDTOList.size(); i++) {
            departmentSymbol = departmentDTOList.get(i);
            nameAr = departmentSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredDepartment.contains(departmentSymbol)) {
                    filteredDepartment.add(departmentSymbol);
                }
            }

            code = departmentSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredDepartment.contains(departmentSymbol)) {
                    filteredDepartment.add(departmentSymbol);
                }
            }
        }

        departmentDTOConverter = new DepartmentDTOConverter(filteredDepartment);
        return filteredDepartment;
    }

    public static List<HrSponsorDTO> completeSponsor(String query, List<HrSponsorDTO> hrSponsorDTOList, SponsorDTOConverter sponsorDTOConverter) {
        List<HrSponsorDTO> Sponsor = hrSponsorDTOList;
        if (query == null || query.trim().equals("")) {
            sponsorDTOConverter = new SponsorDTOConverter(Sponsor);
            return Sponsor;
        }
        List<HrSponsorDTO> filteredSponsor = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSponsorDTO sponsorSymbol;
        for (int i = 0; i < hrSponsorDTOList.size(); i++) {
            sponsorSymbol = hrSponsorDTOList.get(i);
            nameAr = sponsorSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredSponsor.contains(sponsorSymbol)) {
                    filteredSponsor.add(sponsorSymbol);
                }
            }

            code = sponsorSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredSponsor.contains(sponsorSymbol)) {
                    filteredSponsor.add(sponsorSymbol);
                }
            }
        }

        sponsorDTOConverter = new SponsorDTOConverter(filteredSponsor);
        return filteredSponsor;
    }

    public static List<EmployeeDTO> completeEmployee(String query, List<EmployeeDTO> hrSponsorDTOList, EmployeeDTOConverter employeeDTOConverter) {
        List<EmployeeDTO> Employee = hrSponsorDTOList;
        if (query == null || query.trim().equals("")) {
            employeeDTOConverter = new EmployeeDTOConverter(Employee);
            return Employee;
        }
        List<EmployeeDTO> filteredEmployee = new ArrayList<>();

        String nameAr;
        Integer code;
        EmployeeDTO employeeSymbol;
        for (int i = 0; i < hrSponsorDTOList.size(); i++) {
            employeeSymbol = hrSponsorDTOList.get(i);
            nameAr = employeeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }

            code = employeeSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }
        }

        employeeDTOConverter = new EmployeeDTOConverter(filteredEmployee);
        return filteredEmployee;
    }

    public static List<EmployeeDTO> completeSalaryStructureEmployee(String query, List<EmployeeDTO> hrSponsorDTOList, EmployeeDTOConverter hremployeeDTOConverter) {
        List<EmployeeDTO> Employee = hrSponsorDTOList;
        if (query == null || query.trim().equals("")) {
            hremployeeDTOConverter = new EmployeeDTOConverter(Employee);
            return Employee;
        }
        List<EmployeeDTO> filteredEmployee = new ArrayList<>();

        String nameAr;
        Integer code;
        EmployeeDTO employeeSymbol;
        for (int i = 0; i < hrSponsorDTOList.size(); i++) {
            employeeSymbol = hrSponsorDTOList.get(i);
            nameAr = employeeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }

            code = employeeSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }
        }

        hremployeeDTOConverter = new EmployeeDTOConverter(filteredEmployee);
        return filteredEmployee;
    }

    public static List<SymbolDTO> completeMaritalStatus(String query, List<SymbolDTO> maritalStatusList, SymbolDTOConverter maritalStatusDTOConverter) {
        List<SymbolDTO> MaritalStatus = maritalStatusList;
        if (query == null || query.trim().equals("")) {
            maritalStatusDTOConverter = new SymbolDTOConverter(MaritalStatus);
            return MaritalStatus;
        }
        List<SymbolDTO> filteredMaritalStatus = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO maritalStatusSymbol;
        for (int i = 0; i < maritalStatusList.size(); i++) {
            maritalStatusSymbol = maritalStatusList.get(i);
            nameAr = maritalStatusSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredMaritalStatus.contains(maritalStatusSymbol)) {
                    filteredMaritalStatus.add(maritalStatusSymbol);
                }
            }

            code = maritalStatusSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredMaritalStatus.contains(maritalStatusSymbol)) {
                    filteredMaritalStatus.add(maritalStatusSymbol);
                }
            }
        }

        maritalStatusDTOConverter = new SymbolDTOConverter(filteredMaritalStatus);
        return filteredMaritalStatus;
    }

    public static List<SymbolDTO> completeEmpQualifications(String query, List<SymbolDTO> empQualificationsDTOList, SymbolDTOConverter empQualificationsConverter) {
        List<SymbolDTO> EmpQualifications = empQualificationsDTOList;
        if (query == null || query.trim().equals("")) {
            empQualificationsConverter = new SymbolDTOConverter(EmpQualifications);
            return EmpQualifications;
        }
        List<SymbolDTO> filteredEmpQualifications = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO empQualificationsSymbol;
        for (int i = 0; i < empQualificationsDTOList.size(); i++) {
            empQualificationsSymbol = empQualificationsDTOList.get(i);
            nameAr = empQualificationsSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmpQualifications.contains(empQualificationsSymbol)) {
                    filteredEmpQualifications.add(empQualificationsSymbol);
                }
            }

            code = empQualificationsSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmpQualifications.contains(empQualificationsSymbol)) {
                    filteredEmpQualifications.add(empQualificationsSymbol);
                }
            }
        }

        empQualificationsConverter = new SymbolDTOConverter(filteredEmpQualifications);
        return filteredEmpQualifications;
    }

    public static List<SymbolDTO> completePlaceBirth(String query, List<SymbolDTO> placeBirthList, SymbolDTOConverter placeBirthDTOConverter) {
        List<SymbolDTO> PlaceBirth = placeBirthList;
        if (query == null || query.trim().equals("")) {
            placeBirthDTOConverter = new SymbolDTOConverter(PlaceBirth);
            return PlaceBirth;
        }
        List<SymbolDTO> filteredplaceBirth = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO placeBirthSymbol;
        for (int i = 0; i < placeBirthList.size(); i++) {
            placeBirthSymbol = placeBirthList.get(i);
            nameAr = placeBirthSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredplaceBirth.contains(placeBirthSymbol)) {
                    filteredplaceBirth.add(placeBirthSymbol);
                }
            }

            code = placeBirthSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredplaceBirth.contains(placeBirthSymbol)) {
                    filteredplaceBirth.add(placeBirthSymbol);
                }
            }
        }

        placeBirthDTOConverter = new SymbolDTOConverter(filteredplaceBirth);
        return filteredplaceBirth;
    }

    public static List<SymbolDTO> completeJobGrade(String query, List<SymbolDTO> obGradList, SymbolDTOConverter jobGradDTOConverter) {
        List<SymbolDTO> JobGrade = obGradList;
        if (query == null || query.trim().equals("")) {
            jobGradDTOConverter = new SymbolDTOConverter(JobGrade);
            return JobGrade;
        }
        List<SymbolDTO> filteredJobGrade = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO jobGradListSymbol;
        for (int i = 0; i < obGradList.size(); i++) {
            jobGradListSymbol = obGradList.get(i);
            nameAr = jobGradListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredJobGrade.contains(jobGradListSymbol)) {
                    filteredJobGrade.add(jobGradListSymbol);
                }
            }

            code = jobGradListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredJobGrade.contains(jobGradListSymbol)) {
                    filteredJobGrade.add(jobGradListSymbol);
                }
            }
        }

        jobGradDTOConverter = new SymbolDTOConverter(filteredJobGrade);
        return filteredJobGrade;
    }

    public static List<SymbolDTO> completeNationality(String query, List<SymbolDTO> nationalityList, SymbolDTOConverter nationalityDTOConverter) {
        List<SymbolDTO> Nationality = nationalityList;
        if (query == null || query.trim().equals("")) {
            nationalityDTOConverter = new SymbolDTOConverter(Nationality);
            return Nationality;
        }
        List<SymbolDTO> filteredNationality = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO nationalityListSymbol;
        for (int i = 0; i < nationalityList.size(); i++) {
            nationalityListSymbol = nationalityList.get(i);
            nameAr = nationalityListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredNationality.contains(nationalityListSymbol)) {
                    filteredNationality.add(nationalityListSymbol);
                }
            }

            code = nationalityListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredNationality.contains(nationalityListSymbol)) {
                    filteredNationality.add(nationalityListSymbol);
                }
            }
        }

        nationalityDTOConverter = new SymbolDTOConverter(filteredNationality);
        return filteredNationality;
    }

    public static List<SymbolDTO> completeDesignation(String query, List<SymbolDTO> deisgnationList, SymbolDTOConverter designationDTOConverter) {
        List<SymbolDTO> Designation = deisgnationList;
        if (query == null || query.trim().equals("")) {
            designationDTOConverter = new SymbolDTOConverter(Designation);
            return Designation;
        }
        List<SymbolDTO> filteredDesignation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO designationListSymbol;
        for (int i = 0; i < deisgnationList.size(); i++) {
            designationListSymbol = deisgnationList.get(i);
            nameAr = designationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredDesignation.contains(designationListSymbol)) {
                    filteredDesignation.add(designationListSymbol);
                }
            }

            code = designationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredDesignation.contains(designationListSymbol)) {
                    filteredDesignation.add(designationListSymbol);
                }
            }
        }

        designationDTOConverter = new SymbolDTOConverter(filteredDesignation);
        return filteredDesignation;
    }

    public static List<SymbolDTO> completeGender(String query, List<SymbolDTO> genderList, SymbolDTOConverter genderDTOConverter) {
        List<SymbolDTO> Gender = genderList;
        if (query == null || query.trim().equals("")) {
            genderDTOConverter = new SymbolDTOConverter(Gender);
            return Gender;
        }
        List<SymbolDTO> filteredGender = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO genderListSymbol;
        for (int i = 0; i < genderList.size(); i++) {
            genderListSymbol = genderList.get(i);
            nameAr = genderListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGender.contains(genderListSymbol)) {
                    filteredGender.add(genderListSymbol);
                }
            }

            code = genderListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGender.contains(genderListSymbol)) {
                    filteredGender.add(genderListSymbol);
                }
            }
        }

        genderDTOConverter = new SymbolDTOConverter(filteredGender);
        return filteredGender;
    }

    public static List<SymbolDTO> completeReligion(String query, List<SymbolDTO> religionList, SymbolDTOConverter religionDTOConverter) {
        List<SymbolDTO> Religion = religionList;
        if (query == null || query.trim().equals("")) {
            religionDTOConverter = new SymbolDTOConverter(Religion);
            return Religion;
        }
        List<SymbolDTO> filteredReligion = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO religionListSymbol;
        for (int i = 0; i < religionList.size(); i++) {
            religionListSymbol = religionList.get(i);
            nameAr = religionListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredReligion.contains(religionListSymbol)) {
                    filteredReligion.add(religionListSymbol);
                }
            }

            code = religionListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredReligion.contains(religionListSymbol)) {
                    filteredReligion.add(religionListSymbol);
                }
            }
        }

        religionDTOConverter = new SymbolDTOConverter(filteredReligion);
        return filteredReligion;
    }

    public static List<SymbolDTO> completeEducationLevel(String query, List<SymbolDTO> educationLevelList, SymbolDTOConverter educationLevelDTOConverter) {
        List<SymbolDTO> EducationLevel = educationLevelList;
        if (query == null || query.trim().equals("")) {
            educationLevelDTOConverter = new SymbolDTOConverter(EducationLevel);
            return EducationLevel;
        }
        List<SymbolDTO> filteredEducationLevel = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO educationLevelListSymbol;
        for (int i = 0; i < educationLevelList.size(); i++) {
            educationLevelListSymbol = educationLevelList.get(i);
            nameAr = educationLevelListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEducationLevel.contains(educationLevelListSymbol)) {
                    filteredEducationLevel.add(educationLevelListSymbol);
                }
            }

            code = educationLevelListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEducationLevel.contains(educationLevelListSymbol)) {
                    filteredEducationLevel.add(educationLevelListSymbol);
                }
            }
        }

        educationLevelDTOConverter = new SymbolDTOConverter(filteredEducationLevel);
        return filteredEducationLevel;
    }

    public static List<SymbolDTO> completeRelation(String query, List<SymbolDTO> relationList, SymbolDTOConverter relationDTOConverter) {
        List<SymbolDTO> Relation = relationList;
        if (query == null || query.trim().equals("")) {
            relationDTOConverter = new SymbolDTOConverter(Relation);
            return Relation;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < relationList.size(); i++) {
            relationListSymbol = relationList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        relationDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }

    public static List<SymbolDTO> completeSalaryUnitType(String query, List<SymbolDTO> salaryUnitTypeList, SymbolDTOConverter salaryUnitTypeConverter) {
        List<SymbolDTO> SalaryUnitType = salaryUnitTypeList;
        if (query == null || query.trim().equals("")) {
            salaryUnitTypeConverter = new SymbolDTOConverter(SalaryUnitType);
            return SalaryUnitType;
        }
        List<SymbolDTO> filteredSalaryUnitType = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO salaryUnitTypeSymbol;
        for (int i = 0; i < salaryUnitTypeList.size(); i++) {
            salaryUnitTypeSymbol = salaryUnitTypeList.get(i);
            nameAr = salaryUnitTypeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredSalaryUnitType.contains(salaryUnitTypeSymbol)) {
                    filteredSalaryUnitType.add(salaryUnitTypeSymbol);
                }
            }

            code = salaryUnitTypeSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredSalaryUnitType.contains(salaryUnitTypeSymbol)) {
                    filteredSalaryUnitType.add(salaryUnitTypeSymbol);
                }
            }
        }

        salaryUnitTypeConverter = new SymbolDTOConverter(filteredSalaryUnitType);
        return filteredSalaryUnitType;
    }

    public static List<HrSalaryEffectTypeDTO> completeHrSalaryEffectTypeAccuralDTO(String query, List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOAccuralList, hrSalaryEffectTypeConvertor salaryEffectTypeAccuralConvertor) {
        List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList = hrSalaryEffectTypeDTOAccuralList;
        if (query == null || query.trim().equals("")) {
            salaryEffectTypeAccuralConvertor = new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTOList);
            return hrSalaryEffectTypeDTOList;
        }
        List<HrSalaryEffectTypeDTO> filteredhrSalaryEffectType = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
        for (int i = 0; i < hrSalaryEffectTypeDTOAccuralList.size(); i++) {
            hrSalaryEffectTypeDTO = hrSalaryEffectTypeDTOAccuralList.get(i);
            nameAr = hrSalaryEffectTypeDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }

            code = hrSalaryEffectTypeDTO.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }
        }

        salaryEffectTypeAccuralConvertor = new hrSalaryEffectTypeConvertor(filteredhrSalaryEffectType);
        return filteredhrSalaryEffectType;
    }

    public static List<HrSalaryEffectTypeDTO> completeHrSalaryEffectTypeDeductionDTO(String query, List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTODeductionList, hrSalaryEffectTypeConvertor salaryEffectTypeDeductionConvertor) {
        List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList = hrSalaryEffectTypeDTODeductionList;
        if (query == null || query.trim().equals("")) {
            salaryEffectTypeDeductionConvertor = new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTOList);
            return hrSalaryEffectTypeDTOList;
        }
        List<HrSalaryEffectTypeDTO> filteredhrSalaryEffectType = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
        for (int i = 0; i < hrSalaryEffectTypeDTODeductionList.size(); i++) {
            hrSalaryEffectTypeDTO = hrSalaryEffectTypeDTODeductionList.get(i);
            nameAr = hrSalaryEffectTypeDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }

            code = hrSalaryEffectTypeDTO.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }
        }

        salaryEffectTypeDeductionConvertor = new hrSalaryEffectTypeConvertor(filteredhrSalaryEffectType);
        return filteredhrSalaryEffectType;
    }

    ///// CRM ////
    public static List<EmployeeDTO> completeEmployeeDTO(String query, List<EmployeeDTO> employeeDTOList, EmployeeDTOConverter employeeDTOConverter) {
        List<EmployeeDTO> Employee = employeeDTOList;
        if (query == null || query.trim().equals("")) {
            employeeDTOConverter = new EmployeeDTOConverter(Employee);
            return Employee;
        }
        List<EmployeeDTO> filteredEmployee = new ArrayList<>();

        String nameAr;
        Integer code;
        EmployeeDTO employeeSymbol;
        for (int i = 0; i < employeeDTOList.size(); i++) {
            employeeSymbol = employeeDTOList.get(i);
            nameAr = employeeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }

            code = employeeSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }
        }

        employeeDTOConverter = new EmployeeDTOConverter(filteredEmployee);
        return filteredEmployee;
    }
    
    public static List<SymbolDTO> completeLeadStatus(String query, List<SymbolDTO> leadStatusList, SymbolDTOConverter leadStatusDTOConverter) {
        List<SymbolDTO> leadStatus = leadStatusList;
        if (query == null || query.trim().equals("")) {
            leadStatusDTOConverter = new SymbolDTOConverter(leadStatus);
            return leadStatus;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < leadStatusList.size(); i++) {
            relationListSymbol = leadStatusList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        leadStatusDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completeEventStatus(String query, List<SymbolDTO> eventStatusList, SymbolDTOConverter eventStatusDTOConverter) {
        List<SymbolDTO> leadStatus = eventStatusList;
        if (query == null || query.trim().equals("")) {
            eventStatusDTOConverter = new SymbolDTOConverter(leadStatus);
            return leadStatus;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < eventStatusList.size(); i++) {
            relationListSymbol = eventStatusList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        eventStatusDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }

    public static List<SymbolDTO> completeSymbol(String query, List<SymbolDTO> symbolList, SymbolDTOConverter symbolConverter) {
        List<SymbolDTO> activityType = symbolList;
        if (query == null || query.trim().equals("")) {
            symbolConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < symbolList.size(); i++) {
            relationListSymbol = symbolList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        symbolConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completeActivityType(String query, List<SymbolDTO> activityTypeList, SymbolDTOConverter activityTypeDTOConverter) {
        List<SymbolDTO> activityType = activityTypeList;
        if (query == null || query.trim().equals("")) {
            activityTypeDTOConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < activityTypeList.size(); i++) {
            relationListSymbol = activityTypeList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        activityTypeDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completepriority(String query, List<SymbolDTO> priorityList, SymbolDTOConverter priorityDTOConverter) {
        List<SymbolDTO> activityType = priorityList;
        if (query == null || query.trim().equals("")) {
            priorityDTOConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < priorityList.size(); i++) {
            relationListSymbol = priorityList.get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        priorityDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }


}
