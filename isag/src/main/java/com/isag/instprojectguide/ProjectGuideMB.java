/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instprojectguide;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.InstProjectGuideService;
import com.isag.bussinessservice.global.Utility;
import com.isag.comparatorsVars.ProjectGuideComparator;
import com.isag.converter.GlaccountConverter;
import com.isag.entity.Branch;
import com.isag.entity.GlAccount;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.IsagCompany;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseTreeBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq003
 */
@Named(value = "projectGuideMB")
@ViewScoped
public class ProjectGuideMB extends BaseTreeBean {
    
    private UserData userData;
    private TreeNode projectGuideHierarchy;
    private InstProjectGuide fetchedProject;
    private InstProjectGuide projectSelected;
    private boolean visibalitiy;
    private TreeNode selectedProjectGuide;
    private InstProjectGuide selectedCS;
    private InstProjectGuide parentProjectGuide;
    private IsagCompany selectedCompany;
    private List<IsagCompany> allCompanies;
    private List<Branch> branchList = new ArrayList<>();
    private String action;
    private Branch selectedBranch;
    private boolean validateCode;
    private Integer selectedCompanyId;
    private Integer selectedBranchId;
    private Integer selectedParentId;
    private List<InstProjectGuide> allProjectGuide;
    private GlaccountConverter glaccountConverter;
    private List<GlAccount> glAccountList;    
    
    @EJB
    private BranchService branchService;
    @EJB
    private InstProjectGuideService projectGuideService;
    @EJB
    private CompanyService companyService;
    @EJB
    private GlAccountService glAccountService;
    
    @Override
    public String save() {
        try {
            setShowMessage(Boolean.TRUE);
            List<InstProjectGuide> projectGuideList;
            if (getSelectedCS().getId() == null) {
                projectGuideList = projectGuideService.getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), getSelectedCS().getCode());
            } else {
                projectGuideList = projectGuideService.getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), getSelectedCS().getCode(), getSelectedCS().getId());
            }
            
            if (projectGuideList == null || projectGuideList.isEmpty()) {
                getSelectedCS().setCompanyId(selectedCompany);
                if (selectedCompany == null) {
                    getSelectedCS().setCompanyId(companyService.findCompany(selectedCompanyId));
                }
                if (getSelectedCS().getCompanyId() != null) {
                    if (selectedBranchId == null) {
                        selectedBranchId = userData.getUserBranch().getId();
                    }
                    getSelectedCS().setBranchId(branchService.findBranch(selectedBranchId));
                    getSelectedCS().setModifiedBy(userData.getUser());
                    if (parentProjectGuide == null || selectedParentId == null) {
                        getSelectedCS().setLevel((1));
                        getSelectedCS().setActive(Boolean.TRUE);
                    } else {
                        getSelectedCS().setLevel((parentProjectGuide.getLevel() == null) ? null : (parentProjectGuide.getLevel() + 1));
                    }
                    getSelectedCS().setModificationDate(new Date());
                    
                    if (getSelectedCS().getLevel() == 3 && getSelectedCS().getPrice() == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "يجب ادخال سعر الوحدة"));
                        return "";                        
                    }
                    try {
                        projectGuideService.addInstProjectGuide(getSelectedCS(), getUserlogin());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("COST_CENTER_SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("ERROR")));
                        e.printStackTrace();
                    }
                    setSelectedCS(new InstProjectGuide());
                    selectedBranchId = null;
                    selectedParentId = null;
                    parentProjectGuide = null;
                    load();
                }
            } else {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "الكود مسجل يجب اختيار كود اخر"));
            }
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "save");
        }
        return "";
    }
    
    public void validateAccountNumberInputDublication() {
        try {
            for (InstProjectGuide ga : allProjectGuide) {
                if (ga.getCode().equals(selectedCS.getCode())) {
                    setShowMessage(Boolean.TRUE);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), ("الكود مسجل يجب اختيار كود اخر")));
                    setValidateCode(false);
                    fetchedProject = new InstProjectGuide();
                    projectSelected = new InstProjectGuide();
                    fetchedProject = projectGuideService.findInstProjectGuideByCode(selectedCS.getCode(), userData.getUserBranch().getId());
                    projectSelected = fetchedProject;
                    searchForGlAccount();
                    setVisibalitiy(true);
                }
            }
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "validateAccountNumberInputDublication");
        }
    }
    
    public void searchForGlAccount() {
        try {
            selectedCS = projectSelected;
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "searchForGlAccount");
        }
        
    }
    
    public void edit() {
        try {
            action = "edit";
            getSelectedCS().setBranchId(selectedBranch);
            setShowMessage(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "edit");
        }
    }
    
    @PostConstruct
    @Override
    public void init() {
        try {
            setShowMessage(Boolean.FALSE);
            setSelectedCS(new InstProjectGuide());
            projectSelected = null;
            load();
            action = "";
            getGlAccountList();
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "init");
        }
    }
    
    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            fetchedProject = new InstProjectGuide();
            if (userData.getIsAdmin()) {
                //  allCompanies = companyService.getAllCompanies();
                if (userData.getSelectedBranch() == null) {
                    projectGuideHierarchy = loadAllProjectGuide();
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
                    allProjectGuide = projectGuideService.getBranchWithoutParentInstProjectGuides(selectedBranchId);
                    allProjectGuide.sort(new ProjectGuideComparator());
                    projectGuideHierarchy = loadProjectGuideForBranch(selectedBranchId);
                    //   sortTreeCostCenterMap();

                }
            }
            getSelectedCS().setActive(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "load");
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
            if (getSelectedCS().getId() != null) {
                if (getSelectedCS().getChildNodes().isEmpty()) {
                    try {
                        projectGuideService.deleteInstProjectGuide(getSelectedCS());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("COST_CENTER_DELETED")));
                        setSelectedCS(new InstProjectGuide());
                        selectedBranchId = null;
                        selectedParentId = null;
                        parentProjectGuide = null;
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
            saveError(e, "ProjectGuideMB", "delete");
        }
    }
    
    @Override
    public void add() {
        try {
            setShowMessage(Boolean.FALSE);
            allProjectGuide = projectGuideService.getBranchWithoutParentInstProjectGuides(selectedBranchId);
            if (getSelectedCS() != null) {
                parentProjectGuide = getSelectedCS();
                InstProjectGuide newselectedCS = new InstProjectGuide();
                newselectedCS.setBranchId(selectedBranch);
                newselectedCS.setParent(parentProjectGuide);
                newselectedCS.setCreatedBy(userData.getUser());
                if (parentProjectGuide.getId() == null) {
                    newselectedCS.setLevel((1));
                } else {
                    newselectedCS.setLevel((parentProjectGuide.getLevel() == null) ? null : (parentProjectGuide.getLevel() + 1));
                }
                selectedParentId = parentProjectGuide.getId();
                setSelectedCS(newselectedCS);
                getSelectedCS().setActive(Boolean.TRUE);
            } else {
                setSelectedCS(new InstProjectGuide());
                getSelectedCS().setBranchId(selectedBranch);
                getSelectedCS().setCreatedBy(userData.getUser());
                getSelectedCS().setLevel((1));
                getSelectedCS().setActive(Boolean.TRUE);
            }
            
            action = "add";
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "add");
        }
    }
    
    public void cancel() {
        try {
            setSelectedCS(new InstProjectGuide());
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "cancel");
        }
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        try {
            setShowMessage(Boolean.FALSE);
            setSelectedCS((InstProjectGuide) selectedProjectGuide.getData());
            selectedCompanyId = getSelectedCS().getCompanyId().getId();
            if (getSelectedCS().getBranchId() != null && getSelectedCS().getBranchId().getNameAr() != null) {
                selectedBranchId = getSelectedCS().getBranchId().getId();
            }
            allProjectGuide = projectGuideService.getBranchWithoutParentInstProjectGuides(selectedBranchId);
            allProjectGuide.remove(getSelectedCS());
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
            if (getSelectedCS().getBranchId() != null) {
                selectedBranchId = getSelectedCS().getBranchId().getId();
            }
            if (getSelectedCS().getParent() == null) {
                allProjectGuide = new ArrayList<>();
                selectedParentId = null;
            } else {
                selectedParentId = getSelectedCS().getParent().getId();
            }
            parentProjectGuide = getSelectedCS().getParent();
            allProjectGuide.sort(new ProjectGuideComparator());
//        allCostCenters = costCenterService.getCompanyCostCenters(selectedCompanyId);
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "onNodeSelect");
        }
    }
    
    public void selectParent() {
        try {
            int temp = 0;
            if (selectedParentId != null) {
                for (InstProjectGuide projectGuide : allProjectGuide) {
                    if (projectGuide.getId().equals(selectedParentId)) {
                        setParentProjectGuide(projectGuide);
                        getSelectedCS().setParent(projectGuide);
                        if (Utility.checkContainString(getSelectedCS().getParent().getCode())) {
                            for (InstProjectGuide maxChild : getSelectedCS().getParent().getChildNodes()) {
                                if (temp <= Integer.valueOf(maxChild.getCode())) {
                                    temp = Integer.valueOf(maxChild.getCode());
                                }
                            }
                            getSelectedCS().setCode(String.valueOf(temp + 1));
                        }
                        
                        if (projectGuide.getLevel() != null) {
                            updateProjectGuideLevels(getSelectedCS(), projectGuide.getLevel());
                        }
                        break;
                    }
                }
            } else {
                getSelectedCS().setParent(null);
                updateProjectGuideLevels(getSelectedCS(), 0);
            }
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "selectParent");
        }
    }
    
    private void updateProjectGuideLevels(InstProjectGuide projectGuide, Integer level) {
        try {
            projectGuide.setLevel(++level);
            if (projectGuide.getChildNodes() != null) {
                for (InstProjectGuide childNode : projectGuide.getChildNodes()) {
                    updateProjectGuideLevels(childNode, level);
                }
            }
        } catch (Exception e) {
            saveError(e, "ProjectGuideMB", "updateProjectGuideLevels");
        }
    }
    
    @Override
    public String getScreenName() {
        return "costcenter";
    }
    
    public void selectCompany() {
        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
    }
    
    private TreeNode loadProjectGuideForBranch(Integer id) {
        TreeNode branchProjectGuide = new DefaultTreeNode(new InstProjectGuide(), null);
        List<InstProjectGuide> branchCost = projectGuideService.getBranchInstProjectGuideRoots(id);
        branchCost.sort(new ProjectGuideComparator());
        if (branchCost != null && !branchCost.isEmpty()) {
            branchProjectGuide = new DefaultTreeNode(branchCost.get(0), null);
            for (InstProjectGuide cost : branchCost) {
                getChildTreeNodesForProjectGuide(cost, branchProjectGuide);
            }
        }
        
        return branchProjectGuide;
    }
    
    private TreeNode loadProjectGuideForCompany(Integer id) {
        TreeNode companyProjectGuide = new DefaultTreeNode(new InstProjectGuide(), null);
        List<InstProjectGuide> companyCost = projectGuideService.getCompanyInstProjectGuideRoots(id);
        companyCost.sort(new ProjectGuideComparator());
        if (companyCost != null && !companyCost.isEmpty()) {
            companyProjectGuide = new DefaultTreeNode(companyCost.get(0), null);
            for (InstProjectGuide cost : companyCost) {
                getChildTreeNodesForProjectGuide(cost, companyProjectGuide);
            }
        }
        
        return companyProjectGuide;
    }
    
    private TreeNode loadAllProjectGuide() {
        TreeNode root = new DefaultTreeNode(new InstProjectGuide(), null);
        List<InstProjectGuide> allCosts = projectGuideService.getAllInstProjectGuideRoots();
        allCosts.sort(new ProjectGuideComparator());
        for (InstProjectGuide cost : allCosts) {
            getChildTreeNodesForProjectGuide(cost, root);
        }
        return root;
    }
    
    private void getChildTreeNodesForProjectGuide(InstProjectGuide current, TreeNode baseNode) {
        current.getChildNodes().sort(new ProjectGuideComparator());
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
        if (current.getChildNodes() != null && !current.getChildNodes().isEmpty()) {
            for (InstProjectGuide childNode : current.getChildNodes()) {
                getChildTreeNodesForProjectGuide(childNode, parentNode);
            }
        }
    }
    
    public List<GlAccount> completeGlAccount(String query) {
        return AutoComplete.completeGlAccount(query, getGlAccountList(), getGlaccountConverter());
    }

    /**
     * @return the selectedCompany
     */
    public IsagCompany getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(IsagCompany selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the allCompanies
     */
    public List<IsagCompany> getAllCompanies() {
        return allCompanies;
    }

    /**
     * @param allCompanies the allCompanies to set
     */
    public void setAllCompanies(List<IsagCompany> allCompanies) {
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
     * @return the projectGuideHierarchy
     */
    public TreeNode getProjectGuideHierarchy() {
        return projectGuideHierarchy;
    }

    /**
     * @param projectGuideHierarchy the projectGuideHierarchy to set
     */
    public void setProjectGuideHierarchy(TreeNode projectGuideHierarchy) {
        this.projectGuideHierarchy = projectGuideHierarchy;
    }

    /**
     * @return the selectedProjectGuide
     */
    public TreeNode getSelectedProjectGuide() {
        return selectedProjectGuide;
    }

    /**
     * @param selectedProjectGuide the selectedProjectGuide to set
     */
    public void setSelectedProjectGuide(TreeNode selectedProjectGuide) {
        this.selectedProjectGuide = selectedProjectGuide;
    }

    /**
     * @return the parentProjectGuide
     */
    public InstProjectGuide getParentProjectGuide() {
        return parentProjectGuide;
    }

    /**
     * @param parentProjectGuide the parentProjectGuide to set
     */
    public void setParentProjectGuide(InstProjectGuide parentProjectGuide) {
        this.parentProjectGuide = parentProjectGuide;
    }

    /**
     * @return the allProjectGuide
     */
    public List<InstProjectGuide> getAllProjectGuide() {
        return allProjectGuide;
    }

    /**
     * @param allProjectGuide the allProjectGuide to set
     */
    public void setAllProjectGuide(List<InstProjectGuide> allProjectGuide) {
        this.allProjectGuide = allProjectGuide;
    }

    /**
     * @return the projectGuideService
     */
    public InstProjectGuideService getProjectGuideService() {
        return projectGuideService;
    }

    /**
     * @param projectGuideService the projectGuideService to set
     */
    public void setProjectGuideService(InstProjectGuideService projectGuideService) {
        this.projectGuideService = projectGuideService;
    }

    /**
     * @return the selectedCS
     */
    public InstProjectGuide getSelectedCS() {
        return selectedCS;
    }

    /**
     * @param selectedCS the selectedCS to set
     */
    public void setSelectedCS(InstProjectGuide selectedCS) {
        this.selectedCS = selectedCS;
    }

    /**
     * @return the validateCode
     */
    public boolean isValidateCode() {
        return validateCode;
    }

    /**
     * @param validateCode the validateCode to set
     */
    public void setValidateCode(boolean validateCode) {
        this.validateCode = validateCode;
    }

    /**
     * @return the fetchedProject
     */
    public InstProjectGuide getFetchedProject() {
        return fetchedProject;
    }

    /**
     * @param fetchedProject the fetchedProject to set
     */
    public void setFetchedProject(InstProjectGuide fetchedProject) {
        this.fetchedProject = fetchedProject;
    }

    /**
     * @return the projectSelected
     */
    public InstProjectGuide getProjectSelected() {
        return projectSelected;
    }

    /**
     * @param projectSelected the projectSelected to set
     */
    public void setProjectSelected(InstProjectGuide projectSelected) {
        this.projectSelected = projectSelected;
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
     * @return the glaccountConverter
     */
    public GlaccountConverter getGlaccountConverter() {
        return glaccountConverter;
    }

    /**
     * @param glaccountConverter the glaccountConverter to set
     */
    public void setGlaccountConverter(GlaccountConverter glaccountConverter) {
        this.glaccountConverter = glaccountConverter;
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        if (glAccountList == null) {
            glAccountList = glAccountService.getBranchGLAccountsActiveWithException(getUserlogin().getBranchId().getId());
            setGlaccountConverter(new GlaccountConverter(glAccountList));
        }
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }
    
}
