/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.glaccount;

import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GlAccountService;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.define.AccountClassEnum;
import com.toby.define.AccountGroupEnum;
import com.toby.define.AdminUnitDependantEnum;
import com.toby.define.AssisstantAccountDependantEnum;
import com.toby.define.CostCenterDependantEnum;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseTreeBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq003
 */
public class GlAccountBean extends BaseTreeBean {

    private UserData userData;
    private TreeNode accountHierarchy;
    private TreeNode selectedAccount;
    private GlAccount selectedAcc;
    private GlAccount fetchedAccount;
    private GlAccount parentAccount;
    private List<GlAccount> glAccountLocalList;
    private TobyCompany selectedCompany;
    private List<TobyCompany> allCompanies;
    private List<Branch> branchList = new ArrayList<>();
    private String action;
    private Branch selectedBranch;
    private GlAccount glAccountSelected;
    private boolean visibalitiy;
    private boolean validateAccountNumber;
    private Integer selectedCompanyId;
    private Integer selectedBranchId;
    private Integer selectedParentId;
    private Integer selectedCurrencyId;
    private List<GlAccount> allAccounts;
    private List<Currency> currencyList;

    private List<GlAccount> glAccountList;
    private GlaccountConverter accountConverter;
    
    private List<CostCenter> costCenterList;
    private CostCenterConverter costCenterConverter;

    @EJB
    private BranchService branchService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private CompanyService companyService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private GeneraljournalService generalJournalService;
    @EJB
    private CostCenterService costCenterService;

    @PostConstruct
    @Override
    public void init() {
        try {
        selectedAcc = new GlAccount();
        glAccountSelected = null;
        selectedAcc.setAccGroup(AccountGroupEnum.EXPENSES);
        load();

        allCompanies = companyService.getAllCompanies();
        if (getUserData().getIsAdmin()) {
            branchList = branchService.getAllBranchByCompanyId(allCompanies.get(0).getId());
            currencyList = currencyService.getAllCurrencyByCompanyId(allCompanies.get(0).getId());
            fillAccountsByCompany();
        } else {
            currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
        }

        selectedAcc.setLevelAcc(1);
        action = "";
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "init");
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        fetchedAccount = new GlAccount();

        setGlAccountList(accountService.getBranchGLAccountsActiveByBranch(getUserData().getUserBranch().getId()));
        glAccountLocalList = getGlAccountList();
        accountConverter = new GlaccountConverter(glAccountLocalList);

        if (getUserData().getIsAdmin()) {
            accountHierarchy = loadAllCostCenters();
            fillAccountsByCompany();
        } else {
            selectedBranchId = getUserData().getSelectedBranch();
            selectedCompany = getUserData().getCompany();
            accountHierarchy = loadAccountForBranch(getUserData().getSelectedBranch());
            fillAccountsByBranch();
        }
        setShowMessage(Boolean.FALSE);
        visibalitiy = false;
        validateAccountNumber = true;
        selectedAcc.setStatus(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "load");
        }
    }

    public void fillAccountsByBranch() {
        try {
        if (selectedAcc != null && selectedAcc.getAccGroup() != null) {
            allAccounts = accountService.getGLAccountsForBranchForMainAccounts(getUserData().getUserBranch().getId(), selectedAcc.getAccGroup());
        } else {
            allAccounts = new ArrayList<>();
        }
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "fillAccountsByBranch");
        }
    }

    public void fillAccountsByCompany() {
        try {
        if (selectedAcc != null && selectedAcc.getAccGroup() != null) {
            allAccounts = accountService.getCompanyGLAccountsForMainAccounts(getUserData().getCompany().getId(), selectedAcc.getAccGroup());
        } else {
            allAccounts = new ArrayList<>();
        }
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "fillAccountsByCompany");
        }
    }

    @Override
    public String save() {
        try {
        setShowMessage(Boolean.TRUE);
        try {
            selectedAcc.setCompanyId(selectedCompany);
            if (selectedCompany == null) {
                selectedAcc.setCompanyId(companyService.findCompany(selectedCompanyId));
            }
            selectedAcc.setBranchId(branchService.findBranch(getUserData().getSelectedBranch()));
            selectedAcc.setModifiedBy(getUserData().getUser());
            if (selectedParentId == null) {
                selectedAcc.setLevelAcc((1));
            } else {
                parentAccount = accountService.findGlAccount(selectedParentId);
                selectedAcc.setLevelAcc((parentAccount.getLevelAcc() == null) ? null : (parentAccount.getLevelAcc() + 1));
            }
            if (selectedCurrencyId != null) {
                selectedAcc.setCurrencyId(currencyService.findCurrencyById(selectedCurrencyId));
            }
            selectedAcc.setModificationDate(new Date());

            List<GlAccount> glAccounts;
            if (selectedAcc.getId() == null) {
                glAccounts = accountService.getAllGlAccountsByBranchIdAndAccNumOrShotCode(getUserData().getUserBranch().getId(), selectedAcc.getAccNumber(), selectedAcc.getShotCode());
            } else {
                glAccounts = accountService.getAllGlAccountsByBranchIdAndAccNumOrShotCode(getUserData().getUserBranch().getId(), selectedAcc.getAccNumber(), selectedAcc.getShotCode(), selectedAcc.getId());
            }
            if (glAccounts == null || glAccounts.isEmpty()) {
                accountService.addGlAccount(selectedAcc);
                init();
                selectedAcc = new GlAccount();
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("GLACCOUNT_SAVED")));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "رقم الحساب او رقم الحساب المختصر موجود من قبل!"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("GLACCOUNT_ERROR")));
            e.printStackTrace();
        }
        return "";
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "save");
            return null;
        }
    }

    public void edit() {
        try {
        action = "edit";
        selectedAcc.setBranchId(selectedBranch);
        setShowMessage(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "edit");
        }
        
    }

    @Override
    public void delete() {
        try {
        setShowMessage(Boolean.TRUE);
        try {
            selectedAcc = (GlAccount) selectedAccount.getData();
            if (selectedAcc.getChildAccounts() != null && !selectedAcc.getChildAccounts().isEmpty()) {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("GLACCOUNT_DELETE_HAS_CHILD")));
            } else {
                accountService.deleteGlAccount(selectedAcc);
                init();
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("GLACCOUNT_DELETED")));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("GLACCOUNT_DELETE_ERROR")));
            e.printStackTrace();
        }
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "delete");
        }

    }

    @Override
    public void add() {
        try {
        if (selectedAcc != null) {
            parentAccount = selectedAcc;
            GlAccount newelectedAcc = new GlAccount();
            newelectedAcc.setBranchId(selectedBranch);
            newelectedAcc.setParentAcc(parentAccount);
            newelectedAcc.setAccClass(parentAccount.getAccClass());
            newelectedAcc.setAccGroup(parentAccount.getAccGroup());
            newelectedAcc.setAdministrativeUnit(parentAccount.getAdministrativeUnit());
            newelectedAcc.setCostCenter(parentAccount.getCostCenter());
            newelectedAcc.setCurrencyId(parentAccount.getCurrencyId());
            newelectedAcc.setAssistantAcc(parentAccount.getAssistantAcc());
            newelectedAcc.setStatus(true);
            selectedParentId = parentAccount.getId();
            newelectedAcc.setCreatedBy(getUserData().getUser());
            if (parentAccount.getId() == null) {
                newelectedAcc.setLevelAcc((1));
            } else {
                parentAccount = accountService.findGlAccount(parentAccount.getId());
                newelectedAcc.setLevelAcc(parentAccount.getLevelAcc() + 1);
            }
            selectedAcc = newelectedAcc;
        } else {
            selectedAcc = new GlAccount();
            selectedAcc.setBranchId(selectedBranch);
            selectedAcc.setCreatedBy(getUserData().getUser());
            selectedAcc.setLevelAcc((1));
            selectedAcc.setStatus(true);
        }
        setShowMessage(Boolean.TRUE);
        action = "add";
        visibalitiy = false;
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "add");
        }
    }

    public void cancel() {
        try {
        selectedAcc = new GlAccount();
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "cancel");
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        try {
        selectedAcc = (GlAccount) event.getTreeNode().getData();
        fillaccount();
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "onNodeSelect");
        }
    }

    public void fillaccount() {
        try {
        if (selectedAcc != null) {
            selectedCompanyId = selectedAcc.getCompanyId().getId();
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
            selectedBranchId = selectedAcc.getBranchId().getId();
            currencyList = currencyService.getAllCurrencyByCompanyId(selectedCompanyId);
            selectedCurrencyId = selectedAcc.getCurrencyId().getId();
            selectedAcc.setCreditAmount(generalJournalService.getCreditAmountForGlAccount(selectedAcc.getId()));
            selectedAcc.setDebitAmount(generalJournalService.getDebitAmountForGlAccount(selectedAcc.getId()));
            if (selectedAcc.getParentAcc() == null) {
                selectedParentId = null;
            } else {
                selectedParentId = selectedAcc.getParentAcc().getId();
            }
            fillAccountsByBranch();
            visibalitiy = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), ("يجب اختيار حساب ")));
            setShowMessage(true);
        }
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "fillaccount");
        }
    }

    public void selectParent() {
        try {
        if (selectedParentId != null) {
            parentAccount = accountService.findGlAccount(selectedParentId);
            selectedAcc.setAccClass(parentAccount.getAccClass());
            selectedAcc.setAccGroup(parentAccount.getAccGroup());
            selectedAcc.setAdministrativeUnit(parentAccount.getAdministrativeUnit());
            selectedAcc.setCostCenter(parentAccount.getCostCenter());
            selectedAcc.setCurrencyId(parentAccount.getCurrencyId());
            selectedAcc.setAssistantAcc(parentAccount.getAssistantAcc());
            for (GlAccount account : allAccounts) {
                if (account.getId().equals(selectedParentId)) {
                    selectedAcc.setParentAcc(account);
                    if (account.getLevelAcc() != null) {
                        updateAccountLevels(selectedAcc, account.getLevelAcc());
                    }
                    break;
                }
            }
        } else {
            selectedAcc.setParentAcc(null);
            updateAccountLevels(selectedAcc, 0);
        }
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "selectParent");
        }
    }

    private void updateAccountLevels(GlAccount account, Integer level) {
        try {
        account.setLevelAcc(++level);
        if (account.getChildAccounts() != null) {
            for (GlAccount childNode : account.getChildAccounts()) {
                updateAccountLevels(childNode, level);
            }
        }
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "updateAccountLevels");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    public void selectCompany() {
        try {
        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
          } catch (Exception e) {
            saveError(e, "GlAccountBean", "selectCompany");
        }
    }

    private TreeNode loadAccountForBranch(Integer id) {
        try {
        TreeNode companyCostCenter = new DefaultTreeNode(new GlAccount(), null);
        List<GlAccount> companyAccs = accountService.getBranchAccountRoots(id);
        if (companyAccs != null && !companyAccs.isEmpty()) {
            companyCostCenter = new DefaultTreeNode(companyAccs.get(0), null);
            for (GlAccount acc : companyAccs) {
                getChildTreeNodesForAccount(acc, companyCostCenter);
            }
        }
        return companyCostCenter;
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "selectCompany");
            return null;
        }
        
    }

    private TreeNode loadAllCostCenters() {
        try {
        TreeNode root = new DefaultTreeNode(new GlAccount(), null);
        List<GlAccount> allAccs = accountService.getAllAccountRoots();
        for (GlAccount acc : allAccs) {
            getChildTreeNodesForAccount(acc, root);
        }
        return root;
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "loadAllCostCenters");
            return null;
        }
    }

    private void getChildTreeNodesForAccount(GlAccount current, TreeNode baseNode) {
        try {
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
        if (current.getChildAccounts() != null && !current.getChildAccounts().isEmpty()) {
            for (GlAccount childNode : current.getChildAccounts()) {
                getChildTreeNodesForAccount(childNode, parentNode);
            }
        }
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "getChildTreeNodesForAccount");
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
        List<GlAccount> glaccounts = glAccountLocalList;
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < glAccountLocalList.size(); i++) {
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

        accountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "completeGlAccount");
            return null;
        }
    }

    public void setValueToTheSelectedObject() {
        int x = 0;
    }

    public void searchForGlAccount() {
        try {
        selectedAcc = glAccountSelected;
        fillaccount();
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "searchForGlAccount");
        }
    }

    public void validateAccountNumberInputDublication() {
        try {
        for (GlAccount ga : glAccountList) {
            if (ga.getAccNumber().equals(selectedAcc.getAccNumber())) {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), ("هذا الحساب موجود ")));
                setValidateAccountNumber(false);
                fetchedAccount = new GlAccount();
                glAccountSelected = new GlAccount();
                fetchedAccount = accountService.findGlAccountbyBranchIdAndAccountNumber(getUserData().getUserBranch().getId(), selectedAcc.getAccNumber());
                glAccountSelected = fetchedAccount;
                searchForGlAccount();
                visibalitiy = true;
            }
        }
         } catch (Exception e) {
            saveError(e, "GlAccountBean", "validateAccountNumberInputDublication");
        }
    }

    public void reset() {
        try{
        selectedAcc = new GlAccount();
        selectedAcc.setAccGroup(AccountGroupEnum.EXPENSES);
        selectedAcc.setLevelAcc(1);
        } catch (Exception e) {
            saveError(e, "GlAccountBean", "reset");
        }
        
    }

    public void getSelectedAccountNumber() {
        selectedAcc.getAccNumber();

    }

    /**
     * @return the selectedAccount
     */
    public TreeNode getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(TreeNode selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    /**
     * @return the selectedAcc
     */
    public GlAccount getSelectedAcc() {
        return selectedAcc;
    }

    /**
     * @param selectedAcc the selectedAcc to set
     */
    public void setSelectedAcc(GlAccount selectedAcc) {
        this.selectedAcc = selectedAcc;
    }

    /**
     * @return the selectedCompany
     */
    public TobyCompany getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(TobyCompany selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the allCompanies
     */
    public List<TobyCompany> getAllCompanies() {
        return allCompanies;
    }

    /**
     * @param allCompanies the allCompanies to set
     */
    public void setAllCompanies(List<TobyCompany> allCompanies) {
        this.allCompanies = allCompanies;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    /**
     * @return the selectedCompanyId
     */
    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    /**
     * @param selectedCompanyId the selectedCompanyId to set
     */
    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    /**
     * @return the selectedBranchId
     */
    public Integer getSelectedBranchId() {
        return selectedBranchId;
    }

    /**
     * @param selectedBranchId the selectedBranchId to set
     */
    public void setSelectedBranchId(Integer selectedBranchId) {
        this.selectedBranchId = selectedBranchId;
    }

    /**
     * @return the parentAccount
     */
    public GlAccount getParentAccount() {
        return parentAccount;
    }

    /**
     * @param parentAccount the parentAccount to set
     */
    public void setParentAccount(GlAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    /**
     * @return the selectedParentId
     */
    public Integer getSelectedParentId() {
        return selectedParentId;
    }

    /**
     * @param selectedParentId the selectedParentId to set
     */
    public void setSelectedParentId(Integer selectedParentId) {
        this.selectedParentId = selectedParentId;
    }

    /**
     * @return the allAccounts
     */
    public List<GlAccount> getAllAccounts() {
        return allAccounts;
    }

    /**
     * @param allAccounts the allAccounts to set
     */
    public void setAllAccounts(List<GlAccount> allAccounts) {
        this.allAccounts = allAccounts;
    }

    public AccountGroupEnum[] getAccountGroups() {
        return AccountGroupEnum.values();
    }

    public CostCenterDependantEnum[] getCostCenterDependant() {
        return CostCenterDependantEnum.values();
    }

    public AdminUnitDependantEnum[] getAdministrativeUnitDependant() {
        return AdminUnitDependantEnum.values();
    }

    public AccountClassEnum[] getAccountClass() {
        return AccountClassEnum.values();
    }

    public AssisstantAccountDependantEnum[] getAssisstantAccount() {
        return AssisstantAccountDependantEnum.values();
    }

    /**
     * @return the currencyList
     */
    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    /**
     * @param currencyList the currencyList to set
     */
    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    /**
     * @return the selectedCurrencyId
     */
    public Integer getSelectedCurrencyId() {
        return selectedCurrencyId;
    }

    /**
     * @param selectedCurrencyId the selectedCurrencyId to set
     */
    public void setSelectedCurrencyId(Integer selectedCurrencyId) {
        this.selectedCurrencyId = selectedCurrencyId;
    }

    /**
     * @return the accountHierarchy
     */
    public TreeNode getAccountHierarchy() {
        return accountHierarchy;
    }

    /**
     * @param accountHierarchy the accountHierarchy to set
     */
    public void setAccountHierarchy(TreeNode accountHierarchy) {
        this.accountHierarchy = accountHierarchy;
    }

    /**
     * @return the fetchedAccount
     */
    public GlAccount getFetchedAccount() {
        return fetchedAccount;
    }

    /**
     * @param fetchedAccount the fetchedAccount to set
     */
    public void setFetchedAccount(GlAccount fetchedAccount) {
        this.fetchedAccount = fetchedAccount;
    }

    /**
     * @return the glAccountLocalList
     */
    public List<GlAccount> getGlAccountLocalList() {
        return glAccountLocalList;
    }

    /**
     * @param glAccountLocalList the glAccountLocalList to set
     */
    public void setGlAccountLocalList(List<GlAccount> glAccountLocalList) {
        this.glAccountLocalList = glAccountLocalList;
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
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the glAccountSelected
     */
    public GlAccount getGlAccountSelected() {
        return glAccountSelected;
    }

    /**
     * @param glAccountSelected the glAccountSelected to set
     */
    public void setGlAccountSelected(GlAccount glAccountSelected) {
        this.glAccountSelected = glAccountSelected;
    }

    /**
     * @return the visibalitiy
     */
    public boolean isVisibalitiy() {
        return visibalitiy;
    }

    /**
     * @param visibalitiy the visibalitiy to set
     */
    public void setVisibalitiy(boolean visibalitiy) {
        this.visibalitiy = visibalitiy;
    }

    /**
     * @return the validateAccountNumber
     */
    public boolean isValidateAccountNumber() {
        return validateAccountNumber;
    }

    /**
     * @param validateAccountNumber the validateAccountNumber to set
     */
    public void setValidateAccountNumber(boolean validateAccountNumber) {
        this.validateAccountNumber = validateAccountNumber;
    }

    /**
     * @return the costCenterList
     */
    public List<CostCenter> getCostCenterList() {
        if(costCenterList == null){
            costCenterList = costCenterService.getActiveSubCostCentersByBranch(getUserData().getUserBranch().getId());
            costCenterConverter = new CostCenterConverter(costCenterList);
        }
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    /**
     * @return the costCenterConverter
     */
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        if(userData == null){
           ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
        }
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
