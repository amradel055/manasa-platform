/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.converter.CostCenterDTOConverter;
import com.toby.converter.CurrencyDTOConverter;
import com.toby.converter.GlAccountDTOConverter;
import com.toby.converter.GlAdminUnitDTOConverter;
import com.toby.converter.GlBankDTOConverter;
import com.toby.converter.InvDelegatorDTOConvertor;
import com.toby.converter.InvGroupDTOConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.converter.ItemDTOConverter;
import com.toby.converter.ItemsBarcodesDetailsViewConverter;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.converter.SymbolConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.converter.TobyUserProproductionDTOConverter;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.SymbolDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.dto.TobyUserProproductionDTO;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Symbol;
import com.toby.views.ItemsBarcodesDetailsView;
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
    
    public static List<TobyUserDTO> completeTobyUserDTO(String query, List<TobyUserDTO> tobyUserDTOList, TobyUserDTOConverter tobyUserDTOConverter) {
        List<TobyUserDTO> list = new ArrayList<>();
        try {
            List<TobyUserDTO> list1 = tobyUserDTOList;
            if (query == null || query.trim().equals("")) {
                tobyUserDTOConverter = new TobyUserDTOConverter(list1);
                return list1;
            }

            String nameAr;
            String code;
            TobyUserDTO tobyUserDTO;
            for (int i = 0; i < tobyUserDTOList.size(); i++) {
                tobyUserDTO = list1.get(i);
                nameAr = tobyUserDTO.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(tobyUserDTO)) {
                    list.add(tobyUserDTO);
                }

                code = tobyUserDTO.getUserCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase()) && !list.contains(tobyUserDTO)) {
                    list.add(tobyUserDTO);
                }
            }

            tobyUserDTOConverter = new TobyUserDTOConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

    public static List<TobyUserProproductionDTO> completeTobyUserProproductionDTO(String query, List<TobyUserProproductionDTO> tobyUserProproductionDTOList, TobyUserProproductionDTOConverter tobyUserProproductionDTOConverter) {
        List<TobyUserProproductionDTO> list = new ArrayList<>();
        try {
            List<TobyUserProproductionDTO> list1 = tobyUserProproductionDTOList;
            if (query == null || query.trim().equals("")) {
                tobyUserProproductionDTOConverter = new TobyUserProproductionDTOConverter(list1);
                return list1;
            }

            String nameAr;
            TobyUserProproductionDTO tobyUserDTO;
            for (int i = 0; i < tobyUserProproductionDTOList.size(); i++) {
                tobyUserDTO = list1.get(i);
                nameAr = tobyUserDTO.getProproductionName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(tobyUserDTO)) {
                    list.add(tobyUserDTO);
                }
            }

            tobyUserProproductionDTOConverter = new TobyUserProproductionDTOConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
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

    public static List<ProGroupDTO> completeProGroups(String query, List<ProGroupDTO> proGroupDTOList, ProGroupDTOConverter proGroupDTOConverter) {
        try {
            List<ProGroupDTO> groups = proGroupDTOList;
            if (query == null || query.trim().equals("")) {
                proGroupDTOConverter = new ProGroupDTOConverter(groups);
                return groups;
            }
            List<ProGroupDTO> invGroupsFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            ProGroupDTO group;
            for (int i = 0; i < proGroupDTOList.size(); i++) {
                group = groups.get(i);
                nameAr = group.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!invGroupsFiltered.contains(group)) {
                        invGroupsFiltered.add(group);
                    }
                }

                code = group.getId();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!invGroupsFiltered.contains(group)) {
                        invGroupsFiltered.add(group);
                    }
                }
            }

            proGroupDTOConverter = new ProGroupDTOConverter(invGroupsFiltered);
            return invGroupsFiltered;
        } catch (Exception e) {

            return null;
        }
    }

    
    public static List<InvGroupDTO> completeInvGroups(String query, List<InvGroupDTO> invGroupDTOList, InvGroupDTOConverter invGroupDTOConverter) {
        try {
            List<InvGroupDTO> groups = invGroupDTOList;
            if (query == null || query.trim().equals("")) {
                invGroupDTOConverter = new InvGroupDTOConverter(groups);
                return groups;
            }
            List<InvGroupDTO> invGroupsFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            InvGroupDTO group;
            for (int i = 0; i < invGroupDTOList.size(); i++) {
                group = groups.get(i);
                nameAr = group.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!invGroupsFiltered.contains(group)) {
                        invGroupsFiltered.add(group);
                    }
                }

                code = group.getId();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!invGroupsFiltered.contains(group)) {
                        invGroupsFiltered.add(group);
                    }
                }
            }

            invGroupDTOConverter = new InvGroupDTOConverter(invGroupsFiltered);
            return invGroupsFiltered;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<InvPurchaseInvoiceDTO1> completeInvpurchaseInvoice(String query, List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList, InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        try {
            List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOs = invPurchaseInvoiceDTOList;
            if (query == null || query.trim().equals("")) {
//                invPurchaseInvoiceDTOConverter = new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOs);
                return invPurchaseInvoiceDTOs;
            }
            List<InvPurchaseInvoiceDTO1> filteredCostCenters = new ArrayList<>();

            String invoiceid;
            String invoiceSerial;
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO;
            for (int i = 0; i < invPurchaseInvoiceDTOList.size(); i++) {
                invPurchaseInvoiceDTO = invPurchaseInvoiceDTOs.get(i);

                invoiceid = Integer.toString(invPurchaseInvoiceDTO.getId());
                if (invoiceid != null && invoiceid.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(invPurchaseInvoiceDTO)) {
                        filteredCostCenters.add(invPurchaseInvoiceDTO);
                    }
                }
                invoiceSerial = Integer.toString(invPurchaseInvoiceDTO.getSerial());
                if (invoiceSerial != null && invoiceSerial.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(invPurchaseInvoiceDTO)) {
                        filteredCostCenters.add(invPurchaseInvoiceDTO);
                    }
                }

            }

//            invPurchaseInvoiceDTOConverter = new InvPurchaseInvoiceDTOConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<InvInventoryDTO> completeInventory(String query, List<InvInventoryDTO> invInventoryDTOList, InvInventoryDTOConverter invInventoryDTOConverter) {
        try {
            List<InvInventoryDTO> centersList = invInventoryDTOList;
            if (query == null || query.trim().equals("")) {

                invInventoryDTOConverter = new InvInventoryDTOConverter(centersList);
                return centersList;
            }
            List<InvInventoryDTO> filteredCostCenters = new ArrayList<>();

            String nameAr;
            String code;
            InvInventoryDTO costCenterFilter;
            for (int i = 0; i < invInventoryDTOList.size(); i++) {
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

            invInventoryDTOConverter = new InvInventoryDTOConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<InvItemDTO> completeInvItemData(String query, List<InvItemDTO> invItemDTOList, ItemDTOConverter itemDTOConverter) {
        try {
            List<InvItemDTO> centersList = invItemDTOList;
            if (query == null || query.trim().equals("")) {

                itemDTOConverter = new ItemDTOConverter(centersList);
                return centersList;
            }
            List<InvItemDTO> filteredCostCenters = new ArrayList<>();

            String nameAr;
            String code;
            InvItemDTO costCenterFilter;
            for (int i = 0; i < invItemDTOList.size(); i++) {
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

            itemDTOConverter = new ItemDTOConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }

    }

    public static List<InvInventoryDTO> autoCompleteInventoryDTO(String query, List<InvInventoryDTO> invInventoryDTOList, InvInventoryDTOConverter invInventoryDTOConverter) {
        List<InvInventoryDTO> filteredInvs = new ArrayList<>();
        try {

            List<InvInventoryDTO> invList = invInventoryDTOList;
            if (query == null || query.trim().equals("")) {

                invInventoryDTOConverter = new InvInventoryDTOConverter(invList);
                return invList;
            }

            String nameAr;
            String code;
            InvInventoryDTO invFilter;
            for (int i = 0; i < invInventoryDTOList.size(); i++) {
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

            invInventoryDTOConverter = new InvInventoryDTOConverter(filteredInvs);
            return filteredInvs;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<InvItemDTO> autoCompleteInvItemDTOData(String query, List<InvItemDTO> invItemDTOList, ItemDTOConverter itemDTOConverter) {
        List<InvItemDTO> filteredItems = new ArrayList<>();
        try {

            List<InvItemDTO> itemsBarcodesDetailsViews = invItemDTOList;
            if (query == null || query.trim().equals("")) {
                itemDTOConverter = new ItemDTOConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }

            String nameAr;
            String code;
            InvItemDTO barcodesDetailsView;
            for (int i = 0; i < invItemDTOList.size(); i++) {
                barcodesDetailsView = itemsBarcodesDetailsViews.get(i);
                nameAr = barcodesDetailsView.getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }

                code = barcodesDetailsView.getCode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }

            itemDTOConverter = new ItemDTOConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<InventoryDelegatorDTO> completeSalesPerson(String query, List<InventoryDelegatorDTO> SalesPersonsList, InvDelegatorDTOConvertor invDelegatorDTOConvertor) {
        try {
            List<InventoryDelegatorDTO> centersList = SalesPersonsList;
            if (query == null || query.trim().equals("")) {

                invDelegatorDTOConvertor = new InvDelegatorDTOConvertor(centersList);
                return centersList;
            }
            List<InventoryDelegatorDTO> filteredCostCenters = new ArrayList<>();

            String nameAr;
            String code;
            InventoryDelegatorDTO costCenterFilter;
            for (int i = 0; i < SalesPersonsList.size(); i++) {
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

            invDelegatorDTOConvertor = new InvDelegatorDTOConvertor(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<SymbolDTO> completeSupplierType(String query, List<SymbolDTO> SupplierTypeList, SymbolDTOConverter symbolDTOConverter) {
        try {
            List<SymbolDTO> symbols = SupplierTypeList;
            if (query == null || query.trim().equals("")) {

                symbolDTOConverter = new SymbolDTOConverter(symbols);
                return symbols;
            }
            List<SymbolDTO> filteredSymboles = new ArrayList<>();

            String nameAr;
            Integer serial;
            SymbolDTO symbolFilter;
            for (int i = 0; i < SupplierTypeList.size(); i++) {
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

            symbolDTOConverter = new SymbolDTOConverter(filteredSymboles);
            return filteredSymboles;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<InvOrganizationSiteDTO> completOrgSite(String query, List<InvOrganizationSiteDTO> invOrgSites, InvOrganizationSiteDTOConverter invOrgSiteConverter) {

        List<InvOrganizationSiteDTO> invOrgSiteList = invOrgSites;
        if (query == null || query.trim().equals("")) {

            invOrgSiteConverter = new InvOrganizationSiteDTOConverter(invOrgSites);
            return invOrgSiteList;
        }
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSiteDTO invOrgSiteFilter;

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

        invOrgSiteConverter = new InvOrganizationSiteDTOConverter(filteredInvs);
        return filteredInvs;
    }

    public static List<GlAccountDTO> completeGlAccount(String query, List<GlAccountDTO> glAccountDTOList, GlAccountDTOConverter accountDTOConverter) {
        try {
            List<GlAccountDTO> glaccounts = glAccountDTOList;
            if (query == null || query.trim().equals("")) {
                accountDTOConverter = new GlAccountDTOConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccountDTO> filteredGlaccounts = new ArrayList<>();
            String nameAr;
            Integer code;
            GlAccountDTO glaccount;
            for (int i = 0; i < glAccountDTOList.size(); i++) {
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
            return filteredGlaccounts;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<ProProductionStagesDTO> completeProProductionId(String query, List<ProProductionStagesDTO> proProductionStagesDTOList, ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        try {
            List<ProProductionStagesDTO> proProductionStagesDTOs = proProductionStagesDTOList;
            if (query == null || query.trim().equals("")) {
                proProductionStagesDTOConverter = new ProProductionStagesDTOConverter(proProductionStagesDTOs);
                return proProductionStagesDTOs;
            }
            List<ProProductionStagesDTO> filteredCostCenters = new ArrayList<>();

            String State;
            ProProductionStagesDTO proProductionStagesDTO;
            for (int i = 0; i < proProductionStagesDTOList.size(); i++) {
                proProductionStagesDTO = proProductionStagesDTOs.get(i);
                State = Integer.toString(proProductionStagesDTO.getId());
                if (State != null && State.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(proProductionStagesDTO)) {
                        filteredCostCenters.add(proProductionStagesDTO);
                    }
                }

            }

            proProductionStagesDTOConverter = new ProProductionStagesDTOConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }
    }

    public static List<ProProductionTransactionDTO> completeInvpurchaseInvoicefromproductionTrans(String query, List<ProProductionTransactionDTO> proProductionTransactionDTOList, ProProductionTransactionDTOConverter proProductionTransactionDTOConverter) {
        try {
            List<ProProductionTransactionDTO> proProductionTransactionDTOs = proProductionTransactionDTOList;
            if (query == null || query.trim().equals("")) {
                proProductionTransactionDTOConverter = new ProProductionTransactionDTOConverter(proProductionTransactionDTOs);
                return proProductionTransactionDTOs;
            }
            List<ProProductionTransactionDTO> filteredCostCenters = new ArrayList<>();

            String invoiceid;
            ProProductionTransactionDTO proProductionTransactionDTO;
            for (int i = 0; i < proProductionTransactionDTOList.size(); i++) {
                proProductionTransactionDTO = proProductionTransactionDTOs.get(i);
                invoiceid = Integer.toString(proProductionTransactionDTO.getInvPurchaseInvoiceId());
                if (invoiceid != null && invoiceid.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(proProductionTransactionDTO)) {
                        filteredCostCenters.add(proProductionTransactionDTO);
                    }
                }

            }

            proProductionTransactionDTOConverter = new ProProductionTransactionDTOConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {

            return null;
        }
    }
    
    public static List<TobyUserDTO> completusercode(String query, List<TobyUserDTO> tobyUserDTOList, TobyUserDTOConverter tobyUserDTOConverter) {

       List<TobyUserDTO> tobyUserDTOs = tobyUserDTOList;
        if (query == null || query.trim().equals("")) {

            tobyUserDTOConverter = new TobyUserDTOConverter(tobyUserDTOs);
            return tobyUserDTOList;
        }
         List<TobyUserDTO> filteredInvs = new ArrayList<>();

      
        String code;
        TobyUserDTO tobyUserDTO;

        for (int i = 0; i < tobyUserDTOs.size(); i++) {
            tobyUserDTO = tobyUserDTOList.get(i);
          
           

            code = tobyUserDTO.getUserCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(tobyUserDTO)) {
                    filteredInvs.add(tobyUserDTO);
                }
            }
        }

        tobyUserDTOConverter = new TobyUserDTOConverter(filteredInvs);
        return filteredInvs;
    }
    
    public static List<InvPurchaseInvoiceDTO1> completInvPurchaseInvoice(String query, List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOs, InvPurchaseInvoiceDTOConverter invoiceDTOConverter) {

        List<InvPurchaseInvoiceDTO1> invoiceDTOs = invPurchaseInvoiceDTOs;
        if (query == null || query.trim().equals("")) {

//            invoiceDTOConverter = new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOs);
            return invoiceDTOs;
        }
        List<InvPurchaseInvoiceDTO1> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvPurchaseInvoiceDTO1 invPurchaseFilter;

        for (int i = 0; i < invPurchaseInvoiceDTOs.size(); i++) {
            invPurchaseFilter = invoiceDTOs.get(i);
            nameAr = Integer.toString(invPurchaseFilter.getId());
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invPurchaseFilter)) {
                    filteredInvs.add(invPurchaseFilter);
                }
            }

            code = Integer.toString(invPurchaseFilter.getId());
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invPurchaseFilter)) {
                    filteredInvs.add(invPurchaseFilter);
                }
            }
        }

//        invoiceDTOConverter = new InvPurchaseInvoiceDTOConverter(filteredInvs);
        return filteredInvs;
    }
    
    public static List<CostCenterDTO> completeCostCenter(String query, List<CostCenterDTO> costCenterDTOList, CostCenterDTOConverter costCenterDTOConverter) {
        try {
            List<CostCenterDTO> glAdminUnits = costCenterDTOList;
            if (query == null || query.trim().equals("")) {
                costCenterDTOConverter = new CostCenterDTOConverter(costCenterDTOList);
                return glAdminUnits;
            }
            List<CostCenterDTO> filteredGlAdminUnitList = new ArrayList<>();

            String nameAr;
            Integer code;
            CostCenterDTO costCenterDTO;
            for (int i = 0; i < costCenterDTOList.size(); i++) {
                costCenterDTO = glAdminUnits.get(i);
                nameAr = costCenterDTO.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlAdminUnitList.contains(costCenterDTO)) {
                        filteredGlAdminUnitList.add(costCenterDTO);
                    }
                }

                code = costCenterDTO.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlAdminUnitList.contains(costCenterDTO)) {
                        filteredGlAdminUnitList.add(costCenterDTO);
                    }
                }
            }
            costCenterDTOConverter = new CostCenterDTOConverter(filteredGlAdminUnitList);
            return filteredGlAdminUnitList;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static List<GlAdminUnitDTO> completeAdminUnit(String query, List<GlAdminUnitDTO> glAdminUnitDTOList, GlAdminUnitDTOConverter glAdminUnitDTOConverter) {
        try {
            List<GlAdminUnitDTO> glAdminUnits = glAdminUnitDTOList;
            if (query == null || query.trim().equals("")) {
                glAdminUnitDTOConverter = new GlAdminUnitDTOConverter(glAdminUnitDTOList);
                return glAdminUnits;
            }
            List<GlAdminUnitDTO> filteredGlAdminUnitList = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAdminUnitDTO glAdminUnit;
            for (int i = 0; i < glAdminUnitDTOList.size(); i++) {
                glAdminUnit = glAdminUnits.get(i);
                nameAr = glAdminUnit.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }

                code = glAdminUnit.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }
            }
            glAdminUnitDTOConverter = new GlAdminUnitDTOConverter(filteredGlAdminUnitList);
            return filteredGlAdminUnitList;
        } catch (Exception e) {
            return null;
        }
    }

}
