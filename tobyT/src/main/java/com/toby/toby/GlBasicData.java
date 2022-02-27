/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.CurrencyServiceImpl;
import com.toby.businessservice.GlAccountServiceImpl;
import com.toby.businessservice.GlAdminUnitServiceImpl;
import com.toby.businessservice.GlProfitCenterServiceImpl;
import com.toby.converter.CurrencyConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlProfitCenterConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.Currency;
import com.toby.entity.CurrencyOperation;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.CostCenter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author m_els
 */
public class GlBasicData extends Basic {

    private List<GlAccount> accountList;
    private List<CostCenter> profitCenterList;
    private List<GlAdminUnit> adminUnitList;
    private List<Currency> currencyList;

    private GlaccountConverter accountConverter;
    private GlAdminUnitConverter adminUnitConverter;
    private GlProfitCenterConverter profitCenterConverter;
    private CurrencyConverter currencyConverter;

    @EJB
    private GlAccountServiceImpl glaccountFacadeREST;
    @EJB
    private GlProfitCenterServiceImpl glprofitcenterFacadeREST;
    @EJB
    private GlAdminUnitServiceImpl gladminunitFacadeREST;
    @EJB
    private CurrencyServiceImpl currencyFacadeREST;
    @EJB
    private CurrencyOperation currencyDetailFacadeREST;


    public List<GlAccount> completeAccountCode(Integer query) {
        List<GlAccount> glaccounts = getAccountList();
        if (query == null) {
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<GlAccount>();
        for (int i = 0; i < getAccountList().size(); i++) {
            GlAccount glaccount = glaccounts.get(i);
            Integer code = glaccount.getShotCode();
            if (code != null && code == query) {
                if (!filteredGlaccounts.contains(glaccount)) {
                    filteredGlaccounts.add(glaccount);
                }
            }
        }
        return filteredGlaccounts;
    }

    public List<GlAccount> completeAccountName(String query) {
        List<GlAccount> glaccounts = getAccountList();
        if (query == null || query.trim().equals("")) {
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<GlAccount>();
        for (int i = 0; i < getAccountList().size(); i++) {
            GlAccount glaccount = glaccounts.get(i);
            String nameAr = glaccount.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlaccounts.contains(glaccount)) {
                    filteredGlaccounts.add(glaccount);
                }
            }
        }
        return filteredGlaccounts;
    }

    public List<CostCenter> completeProfitCode(Integer query) {
        List<CostCenter> glprofitcenters = getProfitCenterList();
        if (query == null) {
            return glprofitcenters;
        }
        List<CostCenter> filteredGlprofitcenters = new ArrayList<CostCenter>();
        for (int i = 0; i < getProfitCenterList().size(); i++) {
            CostCenter glprofitcenter = glprofitcenters.get(i);
            Integer code = glprofitcenter.getShortCode();
            if (code != null && code == query) {
                if (!filteredGlprofitcenters.contains(glprofitcenter)) {
                    filteredGlprofitcenters.add(glprofitcenter);
                }
            }
        }
        return filteredGlprofitcenters;
    }

    public List<CostCenter> completeProfitName(String query) {
        List<CostCenter> glprofitcenters = getProfitCenterList();
        if (query == null || query.trim().equals("")) {
            return glprofitcenters;
        }
        List<CostCenter> filteredGlprofitcenters = new ArrayList<CostCenter>();
        for (int i = 0; i < getProfitCenterList().size(); i++) {
            CostCenter glprofitcenter = glprofitcenters.get(i);
            String nameAr = glprofitcenter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlprofitcenters.contains(glprofitcenter)) {
                    filteredGlprofitcenters.add(glprofitcenter);
                }
            }
        }
        return filteredGlprofitcenters;
    }

    public List<GlAdminUnit> completeAdminCode(Integer query) {
        List<GlAdminUnit> gladminunits = getAdminUnitList();
        if (query == null) {
            return gladminunits;
        }
        List<GlAdminUnit> filteredGladminunits = new ArrayList<GlAdminUnit>();
        for (int i = 0; i < getAdminUnitList().size(); i++) {
            GlAdminUnit gladminunit = gladminunits.get(i);
            Integer code = gladminunit.getShortCode();
            if (code != null && code == query) {
                if (!filteredGladminunits.contains(gladminunit)) {
                    filteredGladminunits.add(gladminunit);
                }
            }
        }
        return filteredGladminunits;
    }

    public List<GlAdminUnit> completeAdminName(String query) {
        List<GlAdminUnit> gladminunits = getAdminUnitList();
        if (query == null || query.trim().equals("")) {
            return gladminunits;
        }
        List<GlAdminUnit> filteredGladminunits = new ArrayList<GlAdminUnit>();
        for (int i = 0; i < getAdminUnitList().size(); i++) {
            GlAdminUnit gladminunit = gladminunits.get(i);
            String nameAr = gladminunit.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGladminunits.contains(gladminunit)) {
                    filteredGladminunits.add(gladminunit);
                }
            }
        }
        return filteredGladminunits;
    }

    public List<Currency> completeCurrencyName(String query) {
        List<Currency> currencys = getCurrencyList();
        if (query == null || query.trim().equals("")) {
            return currencys;
        }
        List<Currency> filteredCurrencys = new ArrayList<Currency>();
        for (int i = 0; i < getAdminUnitList().size(); i++) {
            Currency currency = currencys.get(i);
            String nameAr = currency.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCurrencys.contains(currency)) {
                    filteredCurrencys.add(currency);
                }
            }
        }
        return filteredCurrencys;
    }

    /**
     * @return the accountList
     */
    public List<GlAccount> getAccountList() {
        if (accountList == null) {
            accountList = glaccountFacadeREST.getAllGlAccount();
            accountConverter = new GlaccountConverter(accountList);
        }
        return accountList;
    }

    /**
     * @param accountList the accountList to set
     */
    public void setAccountList(List<GlAccount> accountList) {
        this.accountList = accountList;
    }

    /**
     * @return the profitCenterList
     */
    public List<CostCenter> getProfitCenterList() {
        if (profitCenterList == null) {
            profitCenterList = glprofitcenterFacadeREST.findAllprofitcenter();
            profitCenterConverter = new GlProfitCenterConverter(profitCenterList);
        }
        return profitCenterList;
    }

    /**
     * @param profitCenterList the profitCenterList to set
     */
    public void setProfitCenterList(List<CostCenter> profitCenterList) {
        this.profitCenterList = profitCenterList;
    }

    /**
     * @return the adminUnitList
     */
    public List<GlAdminUnit> getAdminUnitList() {
        if (adminUnitList == null) {
            adminUnitList = gladminunitFacadeREST.findAllAdminUnit();
            adminUnitConverter = new GlAdminUnitConverter(adminUnitList);
        }
        return adminUnitList;
    }

    /**
     * @param adminUnitList the adminUnitList to set
     */
    public void setAdminUnitList(List<GlAdminUnit> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }

    /**
     * @return the currencyList
     */
    public List<Currency> getCurrencyList() {

        if (currencyList == null) {
            currencyList = currencyFacadeREST.findAllCurrencyPerant();
            currencyConverter = new CurrencyConverter(currencyList);
        }
        return currencyList;
    }

    /**
     * @param currencyList the currencyList to set
     */
    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    /**
     * @return the accountConverter
     */
    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    /**
     * @param accountConverter the accountConverter to set
     */
    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * @return the adminUnitConverter
     */
    public GlAdminUnitConverter getAdminUnitConverter() {
        return adminUnitConverter;
    }

    /**
     * @param adminUnitConverter the adminUnitConverter to set
     */
    public void setAdminUnitConverter(GlAdminUnitConverter adminUnitConverter) {
        this.adminUnitConverter = adminUnitConverter;
    }

    /**
     * @return the profitCenterConverter
     */
    public GlProfitCenterConverter getProfitCenterConverter() {
        return profitCenterConverter;
    }

    /**
     * @param profitCenterConverter the profitCenterConverter to set
     */
    public void setProfitCenterConverter(GlProfitCenterConverter profitCenterConverter) {
        this.profitCenterConverter = profitCenterConverter;
    }

    /**
     * @return the currencyConverter
     */
    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }

    /**
     * @param currencyConverter the currencyConverter to set
     */
    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }
    
    

}