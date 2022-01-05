/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.BalanceAccountMonthlySearchBean;
import com.toby.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.toby.businessservice.reports.searchBean.GlAccountReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.define.AccountGroupEnum;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlAccount;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author FreeComp
 */
@Stateless
public class GlAccountServiceImpl implements GlAccountService {

    @EJB
    private GenericDAO dao;

    @PersistenceContext(name = "TOBY_PU")
    private EntityManager em;

    @Override
    public List<GlAccount> getAllGlAccount() {
        List<GlAccount> accounts = dao.findListByQuery("SELECT g FROM GlAccount g order by g.accNumber ASC");
        if (accounts != null && !accounts.isEmpty()) {
            return accounts;
        }
        return null;
    }

    @Override
    public void addGlAccount(GlAccount account) {
        account.setCreationDate(new Date());
        dao.updateEntity(account);
    }

    @Override
    public GlAccount updateGlAccount(GlAccount account) {
        account.setModificationDate(new Date());
        return dao.updateEntity(account);
    }

    @Override
    public void deleteGlAccount(GlAccount account) {
        dao.deleteEntity(account);
    }

    @Override
    public GlAccount searchAccount(GlAccount account) {
        if (account != null) {
            StringBuilder filter = new StringBuilder();
            if (account.getId() != null) {
                if (filter.toString().isEmpty()) {
                    filter.append("where cast(acc_number as character) like '%").append(account.getAccNumber()).append("%'");
                } else {
                    filter.append("and cast(acc_number as character) like '%").append(account.getAccNumber()).append("%'");
                }
            }
            List<GlAccount> accounts = dao.executeNativeQuery("SELECT * FROM gl_account" + filter.toString() + "  order by acc_number ASC ");
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean validateAccountNumber(Integer number) {
        Map<String, Object> params = new HashMap();
        params.put("number", number);
        List<GlAccount> accounts = dao.findListByQuery("SELECT g FROM GlAccount g  WHERE g.accNumber = :number order by g.accNumber ASC", params);
        if (accounts.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateshortcode(Integer shortCode) {
        Map<String, Object> params = new HashMap();
        params.put("shortCode", shortCode);
        List<GlAccount> accounts = dao.findListByQuery("SELECT g FROM GlAccount g WHERE g.shotCode = :shortCode order by g.accNumber ASC", params);
        if (accounts.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public int findLevel(Integer shortcodeParent) {
        if (shortcodeParent != null) {
            GlAccount accountParent = findParentAccount(shortcodeParent);
            int level = accountParent.getLevelAcc() + 1;
            return level;
        } else {
            return 1;
        }
    }

    @Override
    public GlAccount findParentAccount(Integer shortcodeParent) {
        Map<String, Object> params = new HashMap();
        params.put("shortcodeParent", shortcodeParent);
        if (shortcodeParent != null) {
            List<GlAccount> accounts = dao.findListByQuery("SELECT g FROM GlAccount g  WHERE g.shotCode = :shortcodeParent order by g.accNumber ASC");
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public GlAccount findGlAccount(Integer glAccount) {
        if (glAccount != null) {
            return dao.findEntityById(GlAccount.class, glAccount);
        } else {
            return null;
        }
    }

    @Override
    public GlAccount findGlAccountbyBranchIdAndAccountNumber(Integer branchId, Integer accountNumber) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("accountNumber", accountNumber);
        String query;
        query = "SELECT g FROM GlAccount g  WHERE g.accNumber =:accountNumber AND g.branchId.id =:branchId order by g.accNumber ASC";
        GlAccount glAccount = dao.findEntityByQuery(query, params);
        return glAccount;
    }

    @Override
    public List<GlAccount> getAllAccountByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GlAccount.class, companyId);
    }

    @Override
    public List<GlAccount> getCompanyGLAccounts(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.companyId.id=:id AND acc.status=1 ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getAllGLAccounts() {
        return dao.findListByQuery("SELECT acc FROM GlAccount acc ORDER BY acc.accNumber ASC");
    }

    @Override
    public List<GlAccount> getCompanyAccountRoots(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL AND acc.companyId.id=:id ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getAllAccountRoots() {
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL ORDER BY acc.accNumber ASC");
    }

    @Override
    public List<GlAccount> getGLAccountsForBranch(Integer selectedBranchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:id ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getBranchAccountRoots(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL AND acc.branchId.id=:id ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getBranchAccountRootsForExpensesAndIncome(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);

        String sql = "SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL AND acc.branchId.id=:id AND (acc.accGroup = com.toby.define.AccountGroupEnum.EXPENSES OR acc.accGroup = com.toby.define.AccountGroupEnum.INCOME )  order by acc.accNumber ASC";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAccount> getBranchAccountRootsForAssetsAndDeduction(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);

        String sql = "SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL AND acc.branchId.id=:id AND (acc.accGroup = com.toby.define.AccountGroupEnum.ASSETS OR acc.accGroup = com.toby.define.AccountGroupEnum.DEDUCTION )   order by acc.accNumber ASC";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAccount> getAccountByNameAndNumber(Integer num, String name) {
        Map<String, Object> params = new HashMap();
        params.put("num", num);
        params.put("name", name);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.name=:name AND acc.accNumber=:num   order by acc.accNumber ASC", params);

    }

    @Override
    public List<GlAccount> getAccountByName(String name, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("name", name);
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.name=:name   order by acc.accNumber ASC", params);
    }

    @Override
    public GlAccount getAccountNumber(Integer num, Integer branchId) {
        Map<String, Object> params = new HashMap();
        List<GlAccount> accounts = null;
        params.put("num", num);
        params.put("branchId", branchId);
        accounts = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.accNumber=:num AND acc.branchId.id=:branchId   order by acc.accNumber ASC ", params);
        return (accounts != null && accounts.size() > 0) ? accounts.get(0) : null;
    }

    @Override
    public List<GlAccount> getBranchGLAccountsActive(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1  ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getBranchAllGLAccountsActive(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getBranchGLAccountsActiveWithException(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.id NOT IN (SELECT glv.accountId FROM GlAccountView glv WHERE glv.branchId=:branchId) AND acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getBranchGLAccountsActiveWithoutGlBankAccounts(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 and acc.id not in (SELECT gb.accountId.id FROM GlBank gb WHERE gb.branchId.id =:branchId) ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getGLAccountsActiveByBranchAndCurrencyId(Integer branchId, Integer currencyId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("currencyId", currencyId);

        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.currencyId.id =:currencyId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getBranchGLAccountsActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId ORDER BY acc.accNumber ASC ", params);
    }
//     @Override
//    public List<GlAccount> getGLAccountsByBranch(Integer branchId) {
//        Map<String, Object> params = new HashMap();
//        params.put("branchId", branchId);
//        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id= :branchId ", params);
//    }

    @Override
    public List<GlAccount> getBranchGLAccountsOnlyActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1 ORDER BY acc.accNumber ASC ", params);
    }

    @Override
    public List<GlAccount> getBranchGLAccountsActiveByLeveAndBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        String query;
        query = "SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status = 1 ORDER BY acc.accNumber ASC";
        return dao.findListByQuery(query, params);
    }

    @Override
    public Integer findLevelbyBranchId(Integer branchId) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        String query;
        query = "SELECT acc.levelAcc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status = 1 AND (acc.generalBudgetIdCredit.id != NULL OR acc.generalBudgetIdDebit.id != NULL) ORDER BY acc.accNumber ASC";

        Integer level = dao.findEntityByQuery(query, params);
        return level;
    }

    @Override
    public void updateAllGlAccounts(List<GlAccount> glAccounts, Boolean check, Integer branchId) {

        if (check) {

            String str;
            str = "UPDATE GlAccount acc SET acc.generalBudgetIdCredit = NULL, acc.generalBudgetIdDebit = NULL WHERE acc.branchId.id = " + branchId;

            Query query = em.createQuery(str);
            query.executeUpdate();

        }

        for (GlAccount account : glAccounts) {
            dao.updateEntity(account);
        }
    }

    @Override
    public List<GlAccount> getAllGlAccounts(GlAccountReportSearchBean glAccountReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", glAccountReportSearchBean.getBranchId());
        if (glAccountReportSearchBean.getAccountName() != null && !glAccountReportSearchBean.getAccountName().isEmpty()) {
            params.put("name", glAccountReportSearchBean.getAccountName());
        }

        StringBuilder queryBuilder = new StringBuilder();
        addAdminUnitName(queryBuilder, glAccountReportSearchBean);
        appendAccountNumberFrom(queryBuilder, glAccountReportSearchBean);
        appendAccountNumberTo(queryBuilder, glAccountReportSearchBean);
        appendAccountLevelFrom(queryBuilder, glAccountReportSearchBean);
        appendAccountLevelTo(queryBuilder, glAccountReportSearchBean);
        String query = "SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :branchId " + queryBuilder.toString() + "   order by acc.accNumber ASC";
        return dao.findListByQuery(query, params);
    }

    private void addAdminUnitName(StringBuilder queryBuilder, GlAccountReportSearchBean glAccountReportSearchBean) {
        if (glAccountReportSearchBean.getAccountName() != null && !glAccountReportSearchBean.getAccountName().isEmpty()) {
            queryBuilder.append(" and acc.name Like CONCAT('%',:name ,'%')");
        }
    }

    private void appendAccountNumberFrom(StringBuilder queryBuilder, GlAccountReportSearchBean glAccountReportSearchBean) {
        if (glAccountReportSearchBean.getAccountNumberFrom() != null && glAccountReportSearchBean.getAccountNumberFrom() > 0) {
            queryBuilder.append(" and acc.accNumber >= ").append(glAccountReportSearchBean.getAccountNumberFrom());
        }
    }

    private void appendAccountNumberTo(StringBuilder queryBuilder, GlAccountReportSearchBean glAccountReportSearchBean) {
        if (glAccountReportSearchBean.getAccountNumberTo() != null && glAccountReportSearchBean.getAccountNumberTo() > 0) {
            queryBuilder.append(" and acc.accNumber <= ").append(glAccountReportSearchBean.getAccountNumberTo());
        }
    }

    private void appendAccountLevelFrom(StringBuilder queryBuilder, GlAccountReportSearchBean glAccountReportSearchBean) {
        if (glAccountReportSearchBean.getLevelFrom() != null && glAccountReportSearchBean.getLevelFrom() > 0) {
            queryBuilder.append(" and acc.levelAcc >= ").append(glAccountReportSearchBean.getLevelFrom());
        }
    }

    private void appendAccountLevelTo(StringBuilder queryBuilder, GlAccountReportSearchBean glAccountReportSearchBean) {
        if (glAccountReportSearchBean.getLevelTo() != null && glAccountReportSearchBean.getLevelTo() > 0) {
            queryBuilder.append(" and acc.levelAcc <= ").append(glAccountReportSearchBean.getLevelTo());
        }
    }

    @Override
    public List<GlAccount> getGLAccountsForBranch(Integer selectedBranchId, AccountGroupEnum accountGroup) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("accountGroup", accountGroup);

        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :id AND acc.accGroup = :accountGroup ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getGLAccountsForBranchForMainAccounts(Integer selectedBranchId, AccountGroupEnum accountGroup) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("accountGroup", accountGroup);

        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :id AND acc.accGroup = :accountGroup AND acc.type = 0 ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getCompanyGLAccounts(Integer companyId, AccountGroupEnum accountGroup) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        params.put("accountGroup", accountGroup);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.companyId.id=:id AND acc.status=1 AND acc.accGroup = :accountGroup ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getCompanyGLAccountsForMainAccounts(Integer selectedBranchId, AccountGroupEnum accountGroup) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("accountGroup", accountGroup);

        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :id AND acc.accGroup = :accountGroup AND acc.type = 0 ORDER BY acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getAllGlAccountsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer accNumber, Integer shotCode) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("accNumber", accNumber);
        params.put("shotCode", shotCode);
        return dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :id AND (acc.accNumber = :accNumber OR acc.shotCode= :shotCode)   order by acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> getAllGlAccountsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer accNumber, Integer shotCode, Integer glAccountId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("accNumber", accNumber);
        params.put("shotCode", shotCode);
        params.put("glAccountId", glAccountId);
        String sql = "SELECT acc FROM GlAccount acc WHERE acc.id != :glAccountId and acc.branchId.id = :id AND (acc.accNumber = :accNumber OR acc.shotCode= :shotCode)   order by acc.accNumber ASC";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAccount> getBranchAccountRoots(BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        Map<String, Object> params = new HashMap();
        StringBuilder queryBuilder = new StringBuilder();
        appendAccountNumberFrom(queryBuilder, balanceAccountMonthlySearchBean);
        appendAccountNumberTo(queryBuilder, balanceAccountMonthlySearchBean);
        appendAccountCostCenterFrom(queryBuilder, balanceAccountMonthlySearchBean);
        appendAccountAdminUnitFrom(queryBuilder, balanceAccountMonthlySearchBean);
        params.put("id", balanceAccountMonthlySearchBean.getBranchId());
        String query = "SELECT acc FROM GlAccount acc WHERE acc.parentAcc IS NULL AND acc.branchId.id=:id " + queryBuilder.toString();
        queryBuilder.append(" ORDER BY acc.accNumber ASC");
        return dao.findListByQuery(query, params);
    }

    @Override
    public List<Integer> getAccountList(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean) {
        Map<String, Object> params = new HashMap<>();
        BalanceAccountMonthlySearchBean accountMonthlySearchBean = new BalanceAccountMonthlySearchBean();
        accountMonthlySearchBean.setAccountNumFrom(complexRevisionBalanceSearchBean.getAccountFrom());
        accountMonthlySearchBean.setAccountNumTo(complexRevisionBalanceSearchBean.getAccountTo());
        params.put("branchId", complexRevisionBalanceSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        appendAccountNumberFrom(queryBuilder, accountMonthlySearchBean);
        appendAccountNumberTo(queryBuilder, accountMonthlySearchBean);
        String query = "SELECT acc.accNumber FROM GlAccount acc WHERE acc.branchId.id = :branchId " + queryBuilder.toString() + "   order by acc.accNumber ASC";
        List<Integer> complexRevisionBalance = dao.findListByQuery(query, params);
        return complexRevisionBalance;
    }

    private void appendAccountNumberFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAccountNumFrom() != null && balanceAccountMonthlySearchBean.getAccountNumFrom() > 0) {
            queryBuilder.append(" and acc.accNumber >= ").append(balanceAccountMonthlySearchBean.getAccountNumFrom());
        }
    }

    private void appendAccountNumberTo(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getAccountNumTo() != null && balanceAccountMonthlySearchBean.getAccountNumTo() > 0) {
            queryBuilder.append(" and acc.accNumber <= ").append(balanceAccountMonthlySearchBean.getAccountNumTo());
        }
    }

    //no costCenter yet
    private void appendAccountCostCenterFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getCostCenterForm() != null && balanceAccountMonthlySearchBean.getCostCenterForm() > 0) {
            queryBuilder.append(" and acc.accNumber >= ").append(balanceAccountMonthlySearchBean.getCostCenterForm());
        }
    }

    //no adminUnit yet
    private void appendAccountAdminUnitFrom(StringBuilder queryBuilder, BalanceAccountMonthlySearchBean balanceAccountMonthlySearchBean) {
        if (balanceAccountMonthlySearchBean.getCostCenterTo() != null && balanceAccountMonthlySearchBean.getCostCenterTo() > 0) {
            queryBuilder.append(" and acc.accNumber >= ").append(balanceAccountMonthlySearchBean.getCostCenterTo());
        }
    }

    @Override
    public List<GlAccount> getAllGlAccountsForLowerLevel(GlAccountReportSearchBean glAccountReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", glAccountReportSearchBean.getBranchId());
        if (glAccountReportSearchBean.getAccountName() != null && !glAccountReportSearchBean.getAccountName().isEmpty()) {
            params.put("name", glAccountReportSearchBean.getAccountName());
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :branchId and acc.type = 1 ");
        addAdminUnitName(queryBuilder, glAccountReportSearchBean);
        appendAccountNumberFrom(queryBuilder, glAccountReportSearchBean);
        appendAccountNumberTo(queryBuilder, glAccountReportSearchBean);
        appendAccountLevelFrom(queryBuilder, glAccountReportSearchBean);
        appendAccountLevelTo(queryBuilder, glAccountReportSearchBean);
        return dao.findListByQuery(queryBuilder.toString() + "   order by acc.accNumber ASC ", params);
    }

    @Override
    public GlAccount getGlAccountByBranchAndGlAccountId(Integer branchId, Integer glAccountId) {
        Map<String, Object> params = new HashMap();
        params.put("glAccountId", glAccountId);
        params.put("branchId", branchId);

        return dao.findEntityByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :branchId AND acc.id = :glAccountId   order by acc.accNumber ASC", params);
    }

    @Override
    public List<GlAccount> findAllMainGlAccountsByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        String sql = "SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :branchId AND acc.type = 0 AND acc.status = 1 ORDER BY acc.accNumber ASC ";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAccount> findAllGlAccountsExceptMainAndSubMainGlAccountsByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        String sql = "SELECT acc FROM GlAccount acc WHERE acc.branchId.id = :branchId AND acc.parentAcc IS NOT NULL AND acc.type != 1 AND acc.status = 1 ORDER BY acc.accNumber ASC ";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAccount> findAllGLAccountsWhichHaveGeneralBudgetId(Integer branchId, StringBuilder stringBuilder) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        StringBuilder queryBuilder = new StringBuilder();;
        String query = "SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId ";
        queryBuilder.append(query);
        if (stringBuilder != null && stringBuilder.length() != 0) {
            queryBuilder.append(" and acc.generalBudgetIdCredit.id in (").append(stringBuilder).append(")");
            queryBuilder.append(" OR acc.generalBudgetIdDebit.id in (").append(stringBuilder).append(")");
        }
        return dao.findListByQuery(queryBuilder.toString(), params);
    }

    //mapToEntity
    public GlAccount mapToEntity(GlAccountDTO glAccountDTO, TobyUser tobyUser) {

        GlAccount glAccount = new GlAccount();
        glAccount.setId(glAccountDTO.getId());
        glAccount.setMarkDisable(glAccountDTO.getMarkDisable());
        glAccount.setMarkEdit(glAccountDTO.getMarkEdit());
        glAccount.setName(glAccountDTO.getName());
        if (glAccountDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(glAccountDTO.getCreatedBy());
            glAccount.setCreatedBy(user);
            glAccount.setCreationDate(glAccountDTO.getCreatedDate());
            glAccount.setModifiedBy(tobyUser);
            glAccount.setModificationDate(new Date());
            if (glAccountDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(glAccountDTO.getCompanyId());
                glAccount.setCompanyId(company);
            }

            if (glAccount.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(glAccountDTO.getBranchId());
                glAccount.setBranchId(branch);
            }
        } else {
            glAccount.setCreatedBy(tobyUser);
            glAccount.setCreationDate(new Date());
            glAccount.setCompanyId(tobyUser.getCompanyId());
            glAccount.setBranchId(tobyUser.getBranchId());
        }

        return glAccount;
    }

    public GlAccountDTO mapToDTO(GlAccount entity) {

        GlAccountDTO glAccountDTO = new GlAccountDTO();
        glAccountDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        glAccountDTO.setCreatedDate(entity.getCreationDate());
        glAccountDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        glAccountDTO.setId(entity.getId());
        glAccountDTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        glAccountDTO.setMarkDisable(entity.getMarkDisable());
        glAccountDTO.setMarkEdit(entity.getMarkEdit());
        glAccountDTO.setName(entity.getName());
        glAccountDTO.setMsg(entity.getMsg());
        glAccountDTO.setAccNumber(entity.getAccNumber());

        return glAccountDTO;
    }

    public List<GlAccountDTO> mapToDTOList(List<GlAccount> adminUnitList, TobyUser tobyUser) {
        List<GlAccountDTO> glAccountDTOList = new ArrayList<>();
        for (GlAccount glAccount : adminUnitList) {
            glAccountDTOList.add(mapToDTO(glAccount));
        }
        return glAccountDTOList;
    }

    @Override
    public List<GlAccountDTO> getBranchGLAccountDTOListActive(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<GlAccount> glAccountList = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1  ORDER BY acc.accNumber ASC ", params);
        return mapToDTOList(glAccountList, tobyUser);
    }

    @Override
    public List<GlAccountDTO> getBranchGLAccountsActiveDTO(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<GlAccount> details = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1  ORDER BY acc.accNumber ASC ", params);
        return mapToDTOList(details, tobyUser);
    }

    @Override
    public Map<Integer, GlAccount> getAllGlAccountMap() {
        List<GlAccount> accounts = dao.findListByQuery("SELECT g FROM GlAccount g where g.branchId.id = 182 order by g.accNumber ASC");
        Map<Integer, GlAccount> map = new HashMap<>();
        if (accounts != null && !accounts.isEmpty()) {
            for (GlAccount entity : accounts) {
                map.put(entity.getAccNumber(), entity);
            }
            return map;
        }
        return null;
    }
}
