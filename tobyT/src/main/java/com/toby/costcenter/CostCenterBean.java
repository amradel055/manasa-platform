/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.costcenter;

import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.comparatorsVars.CostCenterCodeComparator;
import com.toby.converter.GlaccountConverter;
import com.toby.dto.CostCenterDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
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
public class CostCenterBean extends BaseTreeBean {

    private UserData userData;
    private TreeNode costCenterHierarchy;
    private TreeNode selectedCostCenter;
    private CostCenter selectedCS;
    private CostCenter parentCostCenter;
    private TobyCompany selectedCompany;
    private List<TobyCompany> allCompanies;
    private List<Branch> branchList = new ArrayList<>();
    private String action;
    private Branch selectedBranch;
    private Integer selectedCompanyId;
    private Integer selectedBranchId;
    private Integer selectedParentId;
    private List<CostCenter> allCostCenters;
    private List<GlAccount> currentacccountList;
    private GlaccountConverter currentaccountConverter;
    private CostCenterDTO costcenterdto;
    
    @EJB
    private BranchService branchService;
    @EJB
    private CostCenterService costCenterService;
    @EJB
    private CompanyService companyService;
    @EJB
    private GlAccountService glAccountService;
    @Override
    public String save() {
        try {
            setShowMessage(Boolean.TRUE);
            List<CostCenter> costCenterList;
           
            

            if (selectedCS.getId() == null) {
                costCenterList = costCenterService.getAllCostCenterByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedCS.getCode());
            } else {
                costCenterList = costCenterService.getAllCostCenterByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedCS.getCode(), selectedCS.getId());
            }
            if (costCenterList == null || costCenterList.isEmpty()) {
                selectedCS.setCompanyId(selectedCompany);
                if (selectedCompany == null) {
                    selectedCS.setCompanyId(companyService.findCompany(selectedCompanyId));
                }
                if (selectedCS.getCompanyId() != null) {
                    if (selectedBranchId == null) {
                        selectedBranchId = userData.getUserBranch().getId();
                    }
                    selectedCS.setBranchId(branchService.findBranch(selectedBranchId));
                    selectedCS.setModifiedBy(userData.getUser());
                    if (parentCostCenter == null || selectedParentId == null) {
                        selectedCS.setLevel((1));
                        selectedCS.setActive(Boolean.TRUE);
                    } else {
                        selectedCS.setLevel((parentCostCenter.getLevel() == null) ? null : (parentCostCenter.getLevel() + 1));
                    }
                    selectedCS.setModificationDate(new Date());
                    try {
                        costCenterService.addCostCenter(selectedCS);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("COST_CENTER_SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("COST_CENTER_ERROR")));
                        e.printStackTrace();
                    }
                    selectedCS = new CostCenter();
                    selectedBranchId = null;
                    selectedParentId = null;
                    load();
                }
            } else {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "الكود مسجل يجب اختيار كود اخر"));
            }
            return "";
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "save");
            return null;
        }
    }
    
      public void fillLists() {
        try {

            updateGlAccounts();

        } catch (Exception e) {
            saveError(e, "costCenterBean", "fillLists");
        }
    }
          public List<GlAccount> updateGlAccounts() {            
               
                if (getCurrentacccountList() != null) {      
                    currentacccountList = glAccountService.getGLAccountsByBranch(selectedBranchId);
                    
                }
                return currentacccountList;
          }
          
       public List<GlAccount> completecurrentaccount(String query) {
        try {
            List<GlAccount> glaccounts = getCurrentacccountList();
            if (query == null || query.trim().equals("")) {
                currentaccountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < getCurrentacccountList().size(); i++) {
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

            currentaccountConverter = new GlaccountConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "completeGlAccount");
            return null;
        }
    }

    public void edit() {
        try {
            action = "edit";
            selectedCS.setBranchId(selectedBranch);
            setShowMessage(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "edit");
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            setShowMessage(Boolean.FALSE);
            selectedCS = new CostCenter();
            load();
            action = "";
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "init");
        }
    }

    @Override
    public void load() {
        try {
                  

            
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData.getIsAdmin()) {
                //  allCompanies = companyService.getAllCompanies();
                if (userData.getSelectedBranch() == null) {
                    costCenterHierarchy = loadAllCostCenters();
                } else {
                    selectedBranchId = userData.getSelectedBranch();

                }
                //  sortTreeCostCenterMap();
            } else {
                selectedCompanyId = userData.getCompany().getId();
                if (userData.getSelectedBranch() != null) {
//                selectedCompany = userData.getCompany();
//                allCostCenters = costCenterService.getCompanyCostCenters(selectedCompany.getId());
//                costCenterHierarchy = loadCostCenterForCompany(selectedCompany.getId());
////                branchList = branchService.getAllBranchByCompanyId(selectedCompany.getId());
//            } else {
                    selectedBranchId = userData.getSelectedBranch();
                    allCostCenters = costCenterService.getBranchWithoutParentCostCenters(selectedBranchId);
                    allCostCenters.sort(new CostCenterCodeComparator());
                    costCenterHierarchy = loadCostCenterForBranch(selectedBranchId);
                    //   sortTreeCostCenterMap();

                }
            }
            selectedCS.setActive(Boolean.TRUE);
                fillLists();
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "load");
        }
    }

    /*  private void sortTreeCostCenterMap() {
     Collections.sort((List<CostCenter>) costCenterHierarchy, new Comparator() {
     @Override
     public int compare(Object t, Object t1) {
     CostCenter c1 = (CostCenter) t;
     CostCenter c2 = (CostCenter) t1;
     if (c1.getCode() > c2.getCode()) {
     return 1;
     } else {
     return -1;
     }
     }
     }
     );
     }*/
    @Override
    public void delete() {
        try {
            setShowMessage(Boolean.TRUE);
            if (selectedCS.getId() != null) {
                if (selectedCS.getChildNodes().isEmpty()) {
                    try {
                        costCenterService.deleteCostCenter(selectedCS);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("COST_CENTER_DELETED")));
                        selectedCS = new CostCenter();
                        selectedBranchId = null;
                        selectedParentId = null;
                        load();
                        action = "";

                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("COST_CENTER_DELETE_ERROR")));
                        e.printStackTrace();
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("COST_CENTER_DELETE_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "delete");
        }
    }

    @Override
    public void add() {
        try {
            setShowMessage(Boolean.FALSE);
            allCostCenters = costCenterService.getBranchWithoutParentCostCenters(selectedBranchId);
            if (selectedCS != null) {
                parentCostCenter = selectedCS;
                CostCenter newselectedCS = new CostCenter();
                newselectedCS.setBranchId(selectedBranch);
                newselectedCS.setParent(parentCostCenter);
                newselectedCS.setCreatedBy(userData.getUser());
                if (parentCostCenter.getId() == null) {
                    newselectedCS.setLevel((1));
                } else {
                    newselectedCS.setLevel((parentCostCenter.getLevel() == null) ? null : (parentCostCenter.getLevel() + 1));
                }
                selectedParentId = parentCostCenter.getId();
                selectedCS = newselectedCS;
                selectedCS.setActive(true);
            } else {
                selectedCS = new CostCenter();
                selectedCS.setBranchId(selectedBranch);
                selectedCS.setCreatedBy(userData.getUser());
                selectedCS.setLevel((1));
                selectedCS.setActive(true);
            }

            action = "add";
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "add");
        }
    }

    public void cancel() {
        try {
            selectedCS = new CostCenter();
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "cancel");
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        try {
            setShowMessage(Boolean.FALSE);
            selectedCS = (CostCenter) selectedCostCenter.getData();
            selectedCompanyId = selectedCS.getCompanyId().getId();
            if (selectedCS.getBranchId() != null && selectedCS.getBranchId().getNameAr() != null) {
                selectedBranchId = selectedCS.getBranchId().getId();
            }
            allCostCenters = costCenterService.getBranchWithoutParentCostCenters(selectedBranchId);
            allCostCenters.remove(selectedCS);
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
            if (selectedCS.getBranchId() != null) {
                selectedBranchId = selectedCS.getBranchId().getId();
            }
            if (selectedCS.getParent() == null) {
                allCostCenters = new ArrayList<>();
                selectedParentId = null;
            } else {
                selectedParentId = selectedCS.getParent().getId();
            }
            allCostCenters.sort(new CostCenterCodeComparator());
//        allCostCenters = costCenterService.getCompanyCostCenters(selectedCompanyId);
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "onNodeSelect");
        }
    }

    public void selectParent() {
        try {
            if (selectedParentId != null) {
                for (CostCenter costCenter : allCostCenters) {
                    if (costCenter.getId().equals(selectedParentId)) {
                        setParentCostCenter(costCenter);
                        selectedCS.setParent(costCenter);
                        if (costCenter.getLevel() != null) {
                            updateCostCenterLevels(selectedCS, costCenter.getLevel());
                        }
                        break;
                    }
                }
            } else {
                selectedCS.setParent(null);
                updateCostCenterLevels(selectedCS, 0);
            }
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "selectParent");
        }
    }

    private void updateCostCenterLevels(CostCenter costCenter, Integer level) {
        try {
            costCenter.setLevel(++level);
            if (costCenter.getChildNodes() != null) {
                for (CostCenter childNode : costCenter.getChildNodes()) {
                    updateCostCenterLevels(childNode, level);
                }
            }
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "updateCostCenterLevels");
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
            saveError(e, "CostCenterBean", "selectCompany");
        }
    }

    /**
     * @return the costCenterHierarchy
     */
    public TreeNode getCostCenterHierarchy() {
        return costCenterHierarchy;
    }

    /**
     * @param costCenterHierarchy the costCenterHierarchy to set
     */
    public void setCostCenterHierarchy(TreeNode costCenterHierarchy) {
        this.costCenterHierarchy = costCenterHierarchy;
    }

    private TreeNode loadCostCenterForBranch(Integer id) {
        try {
            TreeNode branchCostCenter = new DefaultTreeNode(new CostCenter(), null);
            List<CostCenter> branchCost = costCenterService.getBranchCostCenterRoots(id);
            branchCost.sort(new CostCenterCodeComparator());
            if (branchCost != null && !branchCost.isEmpty()) {
                branchCostCenter = new DefaultTreeNode(branchCost.get(0), null);
                for (CostCenter cost : branchCost) {
                    getChildTreeNodesForCostCenter(cost, branchCostCenter);
                }
            }

            return branchCostCenter;
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "loadCostCenterForBranch");
            return null;
        }

    }

    private TreeNode loadCostCenterForCompany(Integer id) {
        try {
            TreeNode companyCostCenter = new DefaultTreeNode(new CostCenter(), null);
            List<CostCenter> companyCost = costCenterService.getCompanyCostCenterRoots(id);
            companyCost.sort(new CostCenterCodeComparator());
            if (companyCost != null && !companyCost.isEmpty()) {
                companyCostCenter = new DefaultTreeNode(companyCost.get(0), null);
                for (CostCenter cost : companyCost) {
                    getChildTreeNodesForCostCenter(cost, companyCostCenter);
                }
            }

            return companyCostCenter;
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "loadCostCenterForCompany");
            return null;
        }
    }

    private TreeNode loadAllCostCenters() {
        try {
            TreeNode root = new DefaultTreeNode(new CostCenter(), null);
            List<CostCenter> allCosts = costCenterService.getAllCostCenterRoots();
            allCosts.sort(new CostCenterCodeComparator());
            for (CostCenter cost : allCosts) {
                getChildTreeNodesForCostCenter(cost, root);
            }
            return root;
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "loadAllCostCenters");
            return null;
        }
    }

    private void getChildTreeNodesForCostCenter(CostCenter current, TreeNode baseNode) {
        try {
            current.getChildNodes().sort(new CostCenterCodeComparator());
            TreeNode parentNode = new DefaultTreeNode(current, baseNode);
            if (current.getChildNodes() != null && !current.getChildNodes().isEmpty()) {
                for (CostCenter childNode : current.getChildNodes()) {
                    getChildTreeNodesForCostCenter(childNode, parentNode);
                }
            }
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "getChildTreeNodesForCostCenter");
        }
    }

    /**
     * @return the selectedCostCenter
     */
    public TreeNode getSelectedCostCenter() {
        return selectedCostCenter;
    }

    /**
     * @param selectedCostCenter the selectedCostCenter to set
     */
    public void setSelectedCostCenter(TreeNode selectedCostCenter) {
        this.selectedCostCenter = selectedCostCenter;
    }

    /**
     * @return the selectedCS
     */
    public CostCenter getSelectedCS() {
        return selectedCS;
    }

    /**
     * @param selectedCS the selectedCS to set
     */
    public void setSelectedCS(CostCenter selectedCS) {
        this.selectedCS = selectedCS;
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
     * @return the parentCostCenter
     */
    public CostCenter getParentCostCenter() {
        return parentCostCenter;
    }

    /**
     * @param parentCostCenter the parentCostCenter to set
     */
    public void setParentCostCenter(CostCenter parentCostCenter) {
        this.parentCostCenter = parentCostCenter;
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
     * @return the allCostCenters
     */
    public List<CostCenter> getAllCostCenters() {
        return allCostCenters;
    }

    /**
     * @param allCostCenters the allCostCenters to set
     */
    public void setAllCostCenters(List<CostCenter> allCostCenters) {
        this.allCostCenters = allCostCenters;
    }

    /**
     * @return the currentaccountConverter
     */
    public GlaccountConverter getCurrentaccountConverter() {
        return currentaccountConverter;
    }

    /**
     * @param currentaccountConverter the currentaccountConverter to set
     */
    public void setCurrentaccountConverter(GlaccountConverter currentaccountConverter) {
        this.currentaccountConverter = currentaccountConverter;
    }

    /**
     * @return the glAccountService
     */
    public GlAccountService getGlAccountService() {
        return glAccountService;
    }

    /**
     * @param glAccountService the glAccountService to set
     */
    public void setGlAccountService(GlAccountService glAccountService) {
        this.glAccountService = glAccountService;
    }

    /**
     * @return the currentacccountList
     */
    public List<GlAccount> getCurrentacccountList() {
       if(currentacccountList == null){
       currentacccountList = new ArrayList<>();
       }
        return currentacccountList;
    }

    /**
     * @param currentacccountList the currentacccountList to set
     */
    public void setCurrentacccountList(List<GlAccount> currentacccountList) {
        this.currentacccountList = currentacccountList;
    }

    /**
     * @return the costcenterdto
     */
    public CostCenterDTO getCostcenterdto() {
        return costcenterdto;
    }

    /**
     * @param costcenterdto the costcenterdto to set
     */
    public void setCostcenterdto(CostCenterDTO costcenterdto) {
        this.costcenterdto = costcenterdto;
    }

}
