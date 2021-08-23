/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IgmaUser;
import com.isag.entity.Symbol;
import java.io.Serializable;
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
public class AutoComplete1 implements Serializable {

    @EJB
    private AutoCompleteService autoCompleteService;
    private Integer branchId;
    private Integer remCompanyId;
    private Integer employeeId;

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
            IgmaUser isagUser = (IgmaUser) externalContext.getSessionMap().get("userlogin");
            branchId = isagUser.getBranchId().getId();
        }
        return branchId;
    }

    /**
     * @return the remCompanyId
     */
    public Integer getRemCompanyId() {
         if (remCompanyId == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Integer remCompany = (Integer) externalContext.getSessionMap().get("remCompany");
            remCompanyId = remCompany;
        }
        return remCompanyId;
    }

    /**
     * @param remCompanyId the remCompanyId to set
     */
    public void setRemCompanyId(Integer remCompanyId) {
        this.remCompanyId = remCompanyId;
    }

}
