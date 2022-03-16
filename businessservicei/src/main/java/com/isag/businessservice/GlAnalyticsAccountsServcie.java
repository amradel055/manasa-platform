package com.isag.businessservice;

import com.isag.entity.GlAnalyticsAccounts;
import java.util.List;
import javax.ejb.Remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hq002
 */
@Remote
public interface GlAnalyticsAccountsServcie {

    public GlAnalyticsAccounts addGlAnalyticsAccounts(GlAnalyticsAccounts newGlAn);

    public void saveGlAnalyticsAccounts(List<GlAnalyticsAccounts> newGlAn);

    public GlAnalyticsAccounts update(GlAnalyticsAccounts glAnalyticsAccounts);

    public void delete(GlAnalyticsAccounts glAnalyticsAccounts);

    public void delete(Integer branchId);

    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsByBranch(Integer branchId);

    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsByCompany();

    public GlAnalyticsAccounts getSpecificGlAnalyticAccount(Integer branchId, String account);

    public boolean checkGlAnalyticsExist(String account);

    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsForMonitoringByBranch(Integer branchId, Integer type);

    public List<GlAnalyticsAccounts> getALLNullMonitoringAccounts();

    public List<GlAnalyticsAccounts> getALLNullPurchaseAndSalesAccounts();

    public Long getCountsForMonitoringByBranch(Integer branchId, Integer type);
}
