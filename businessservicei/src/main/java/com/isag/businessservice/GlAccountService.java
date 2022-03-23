/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.BalanceAccountMonthlySearchBean;
import com.isag.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.isag.businessservice.reports.searchBean.GlAccountReportSearchBean;
import com.isag.define.AccountGroupEnum;
import com.isag.entity.GlAccount;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author FreeComp
 */
@Remote
public interface GlAccountService {

    public List<GlAccount> getAllGlAccount();

    public void addGlAccount(GlAccount account);

    public GlAccount updateGlAccount(GlAccount account);

    public void deleteGlAccount(GlAccount account);

    public GlAccount findGlAccount(Integer glAccount);

    public List<GlAccount> getAllAccountByCompanyId(Integer companyId);

    public GlAccount searchAccount(GlAccount account);

    public boolean validateAccountNumber(Integer number);

    public boolean validateshortcode(Integer shortCode);

    public int findLevel(Integer shortcodeParent);

    public GlAccount findParentAccount(Integer shortcodeParent);

    public List<GlAccount> getAllGLAccounts();

    public List<GlAccount> getCompanyAccountRoots(Integer id);

    public List<GlAccount> getAllAccountRoots();

    public List<GlAccount> getAccountByNameAndNumber(Integer num, String name);

    public List<GlAccount> getAccountByName(String name, Integer branchId);

    public GlAccount getAccountNumber(Integer num, Integer branchId);

    public List<GlAccount> getGLAccountsForBranch(Integer selectedBranchId);

    public List<GlAccount> getGLAccountsForBranch(Integer selectedBranchId, AccountGroupEnum accountGroup);

    public List<GlAccount> getGLAccountsForBranchForMainAccounts(Integer selectedBranchId, AccountGroupEnum accountGroup);

    public List<GlAccount> getBranchGLAccountsActive(Integer id);
    
    public List<GlAccount> getBranchAllGLAccountsActive(Integer branchId);

    public List<GlAccount> getGLAccountsActiveByBranchAndCurrencyId(Integer branchId, Integer currencyId);

    public List<GlAccount> getCompanyGLAccounts(Integer companyId);

    public List<GlAccount> getCompanyGLAccounts(Integer companyId, AccountGroupEnum accountGroup);

    public List<GlAccount> getCompanyGLAccountsForMainAccounts(Integer selectedBranchId, AccountGroupEnum accountGroup);

    public List<GlAccount> getBranchAccountRoots(Integer id);

    public List<GlAccount> getAllGlAccounts(GlAccountReportSearchBean glAccountReportSearchBean);

    public List<GlAccount> getAllGlAccountsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer accNumber, Integer shotCode);

    public List<GlAccount> getAllGlAccountsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer accNumber, Integer shotCode, Integer glAccountId);

    public List<GlAccount> getBranchAccountRootsForExpensesAndIncome(Integer id);

    public List<GlAccount> getBranchAccountRootsForAssetsAndDeduction(Integer id);

    public List<GlAccount> getBranchAccountRoots(BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean);

    public List<Integer> getAccountList(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean);

    public List<GlAccount> getAllGlAccountsForLowerLevel(GlAccountReportSearchBean glAccountReportSearchBean);

    public List<GlAccount> getBranchGLAccountsActiveByLeveAndBranchId(Integer branchId);

    public Integer findLevelbyBranchId(Integer branchId);

    public void updateAllGlAccounts(List<GlAccount> glAccounts, Boolean check, Integer branchId);

    public List<GlAccount> getBranchGLAccountsActiveByBranch(Integer branchId);

    public GlAccount findGlAccountbyBranchIdAndAccountNumber(Integer branchId, Integer accountNumber);

    public List<GlAccount> getBranchGLAccountsOnlyActiveByBranch(Integer branchId);

    public GlAccount getGlAccountByBranchAndGlAccountId(Integer branchId, Integer glAccountId);

    public List<GlAccount> findAllMainGlAccountsByBranch(Integer branchId);
    
    public List<GlAccount> findAllGlAccountsByBranch(Integer branchId);
    
    public List<GlAccount> findAllGlAccountsByBranchAndLevel(Integer branchId,Integer level);

    public List<GlAccount> findAllGlAccountsExceptMainAndSubMainGlAccountsByBranch(Integer branchId);

    List<GlAccount> findAllGLAccountsWhichHaveGeneralBudgetId(Integer branchId, StringBuilder stringBuilder);

    public List<GlAccount> getBranchGLAccountsActiveWithoutGlBankAccounts(Integer branchId);

    public List<GlAccount> getBranchGLAccountsActiveWithException(Integer branchId);
}
