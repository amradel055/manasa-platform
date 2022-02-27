/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.adminunits;

import com.toby.businessservice.AdminUnitsService;
import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.entity.Branch;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseTreeBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq002
 */
@Named(value = "adminUnitBean")
@ViewScoped
public class AdminUnitBean extends BaseTreeBean {

    private UserData userData;
    private TreeNode adminUnitsHierarchy;

    private TreeNode selectedAdminUnits;
    private GlAdminUnit selectedAU = new GlAdminUnit();
    private boolean formView = false;
    private String unitName;
    private Integer code;
    private Integer shortCode;
    private Integer selectedParent;
    private boolean active;

    private boolean add = false;
    private Integer selectedBranch;
    private List<Branch> branchList = new ArrayList<>();
    private Branch branch;
    private TobyCompany CompanyId;
    private List<TobyCompany> companies;
    private Integer selectedCompanyId;
    private List<GlAdminUnit> allAdminUnits;
    @EJB
    AdminUnitsService adminUnitsService;
    @EJB
    private BranchService branchService;
    @EJB
    private CompanyService companyService;

    @Override
    public String save() {
        try {
            this.setShowMessage(Boolean.TRUE);
            Map<String, String> userDDs = userData.getUserDDs();

            //selectedAU=(GlAdminUnit) selectedAdminUnits.getData();
            List<GlAdminUnit> glAdminUnitList = new ArrayList<>();
            if (selectedAU.getId() == null) {
                glAdminUnitList = adminUnitsService.getAllAdminUnitsByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedAU.getCode());
            } else {
                glAdminUnitList = adminUnitsService.getAllAdminUnitsByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedAU.getCode(), selectedAU.getId());
            }
            if (glAdminUnitList == null || glAdminUnitList.isEmpty()) {
                if (selectedAU != null && CompanyId != null) {
                    selectedAU.setCreatedBy(userData.getUser());
                    selectedAU.setCreationDate(new Date());
                    if (selectedBranch == null) {
                        selectedBranch = userData.getUserBranch().getId();
                    }
                    selectedAU.setBranchId(branchService.findBranch(selectedBranch));
                    selectedAU.setCompanyId(CompanyId);

                    if (add || selectedAU.getId() == null) {
                        selectedAU.setActive(Boolean.TRUE);
                        selectedAU.setLevel(selectedAU.getParent() == null ? 1 : selectedAU.getParent().getLevel() + 1);
                        adminUnitsService.addAdminUnit(selectedAU);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                    } else {
                        selectedAU.setModificationDate(new Date());
                        adminUnitsService.addAdminUnit(selectedAU);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                    }
                    selectedAU = new GlAdminUnit();
                    selectedParent = null;
                    selectedBranch = null;
                    load();
                }
            }

            return "";
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "save");
            return null;
        }
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
            setShowMessage(Boolean.FALSE);
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "init");
        }
    }

    @Override
    public void delete() {
        try {
            setShowMessage(Boolean.TRUE);
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedAU != null && selectedAU.getId() != null) {
                if (selectedAU.getAdminUnitChilds().isEmpty()) {
                    try {
                        adminUnitsService.deleteAdminUnit(selectedAU);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("UNIT_DELETED")));
                        load();
                        selectedAU = new GlAdminUnit();
                        selectedParent = null;
                        selectedBranch = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIT_DELETE_ERROR")));
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("UNIT_HAS_CHILD")));
                }
            }
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "delete");
        }
    }

    @Override
    public void add() {
        try{
        setShowMessage(Boolean.FALSE);
        add = true;
        allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByBranch(selectedBranch);
        GlAdminUnit parent;
        if (selectedAU != null && selectedAU.getName() != null) {

            selectedParent = selectedAU.getId();
            selectedBranch = selectedAU.getBranchId().getId();
            parent = selectedAU;
            selectedParent = parent.getId();
            selectedAU = new GlAdminUnit();
            selectedAU.setParent(parent);
            selectedAU.setCompanyId(parent.getCompanyId());
            selectedAU.setLevel(parent.getLevel() == null ? 1 : parent.getLevel() + 1);
            selectedAU.setActive(true);
            if (userData.getIsAdmin()) {
                CompanyId = parent.getCompanyId();
            }
        } else {
            selectedBranch = userData.getSelectedBranch();
            selectedAU = new GlAdminUnit();
            selectedAU.setParent(null);
            selectedAU.setLevel(1);
            selectedAU.setActive(true);
            if (userData.getIsAdmin()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("UNIT_DELETE_ERROR")));
            }
        }
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "add");
        }

    }

    public void onNodeSelected(NodeSelectEvent event) {
        try{
        setShowMessage(Boolean.TRUE);
//        setShowMessage(Boolean.FALSE);
        add = false;
        selectedAU = new GlAdminUnit();
        selectedAU = (GlAdminUnit) event.getTreeNode().getData();
        if (selectedAU.getBranchId() != null && selectedAU.getBranchId().getNameAr() != null) {
            selectedBranch = selectedAU.getBranchId().getId();
        }
        allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByBranch(selectedBranch);
        allAdminUnits.remove(selectedAU);
        selectedParent = null;
        if (selectedAU.getParent() != null && selectedAU.getParent().getName() != null) {

            selectedParent = selectedAU.getParent().getId();
        } else {
            allAdminUnits = new ArrayList<>();
        }
        selectedCompanyId = selectedAU.getCompanyId().getId();
        //allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByCompany(selectedCompanyId);
        // branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
        RequestContext.getCurrentInstance().update("form:messages");
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "onNodeSelected");
        }
    }

    public void selectParent() {
        try{
        if (selectedParent != null) {
            for (GlAdminUnit adminunit : allAdminUnits) {
                if (adminunit.getId().equals(selectedParent)) {
                    selectedAU.setParent(adminunit);
                    if (adminunit.getLevel() != null) {
                        updateAdminUnitsLevels(selectedAU, adminunit.getLevel());
                    }
                    break;
                }
            }
        } else {
            selectedAU.setParent(null);
            updateAdminUnitsLevels(selectedAU, 0);
        }
         } catch (Exception e) {
            saveError(e, "AdminUnitBean", "selectParent");
        }
    }

    private void updateAdminUnitsLevels(GlAdminUnit adminunit, Integer level) {
        try{
        adminunit.setLevel(++level);
        if (adminunit.getAdminUnitChilds() != null) {
            for (GlAdminUnit childNode : adminunit.getAdminUnitChilds()) {
                updateAdminUnitsLevels(childNode, level);
            }
        }
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "updateAdminUnitsLevels");
        }
    }

    @Override
    public void load() {
        try{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        if (userData.getIsAdmin()) {
            if (userData.getSelectedBranch() == null) {
                adminUnitsHierarchy = loadAllAdminUnitsRoots();
            } else {
                selectedBranch = userData.getSelectedBranch();
                adminUnitsHierarchy = loadAdminUnitForBranch(userData.getSelectedBranch());
                //branchList = branchService.getAllBranchByCompanyId(CompanyId.getId());
                allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByBranch(userData.getSelectedBranch());
            }
            // companies = companyService.getAllCompanies();
        } else {
            CompanyId = userData.getCompany();
            if (userData.getSelectedBranch() != null) {
//
//                adminUnitsHierarchy = loadAdminUnitForCompany(CompanyId.getId());
//                //branchList = branchService.getAllBranchByCompanyId(CompanyId.getId());
//                allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByCompany(CompanyId.getId());
//            } else {
                selectedBranch = userData.getSelectedBranch();
                adminUnitsHierarchy = loadAdminUnitForBranch(selectedBranch);
                //branchList = branchService.getAllBranchByCompanyId(CompanyId.getId());
                allAdminUnits = adminUnitsService.getAllAdminUnitsNodesByBranch(selectedBranch);
            }

        }
        selectedAU.setActive(true);
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "load");
        }
    }

    private TreeNode loadAdminUnitForBranch(Integer id) {
        try{
        TreeNode branchAdminUnit = new DefaultTreeNode(new GlAdminUnit(), null);
        List<GlAdminUnit> allAdminUnitsByBranch = adminUnitsService.getBranchAdminUnitsRoots(id);
        if (allAdminUnitsByBranch != null && !allAdminUnitsByBranch.isEmpty()) {
            branchAdminUnit = new DefaultTreeNode(allAdminUnitsByBranch.get(0), null);
            for (GlAdminUnit AUnit : allAdminUnitsByBranch) {
                getChildTreeNodesForAdminUnits(AUnit, branchAdminUnit);
            }
        }
        return branchAdminUnit;
        } catch (Exception e) {
            saveError(e, "AdminUnitBean", "loadAdminUnitForBranch");
            return null;
        }
    }

    private TreeNode loadAdminUnitForCompany(Integer id) {
        try{
        TreeNode companyAdminUnit = new DefaultTreeNode(new GlAdminUnit(), null);
        List<GlAdminUnit> allAdminUnitsByCompany = adminUnitsService.getCompanyAdminUnits(id);
        if (allAdminUnitsByCompany != null && !allAdminUnitsByCompany.isEmpty()) {
            companyAdminUnit = new DefaultTreeNode(allAdminUnitsByCompany.get(0), null);
            for (GlAdminUnit AUnit : allAdminUnitsByCompany) {
                getChildTreeNodesForAdminUnits(AUnit, companyAdminUnit);
            }
        }
        return companyAdminUnit;
         } catch (Exception e) {
            saveError(e, "AdminUnitBean", "loadAdminUnitForCompany");
            return null;
        }
    }

    private TreeNode loadAllAdminUnitsRoots() {
        try{
        TreeNode root = new DefaultTreeNode(new GlAdminUnit(), null);
        List<GlAdminUnit> allAdminUnit = adminUnitsService.getAllAdminUnits();
        for (GlAdminUnit AUnit : allAdminUnit) {
            getChildTreeNodesForAdminUnits(AUnit, root);
        }

        return root;
         } catch (Exception e) {
            saveError(e, "AdminUnitBean", "loadAllAdminUnitsRoots");
            return null;
        }
    }

    private void getChildTreeNodesForAdminUnits(GlAdminUnit current, TreeNode baseNode) {
        try {
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
        if (current.getAdminUnitChilds() != null && !current.getAdminUnitChilds().isEmpty()) {
            for (GlAdminUnit childNode : current.getAdminUnitChilds()) {
                getChildTreeNodesForAdminUnits(childNode, parentNode);
            }
        }
         } catch (Exception e) {
            saveError(e, "AdminUnitBean", "getChildTreeNodesForAdminUnits");
        }
    }

    @Override
    public String getScreenName() {
        return "adminunit";
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public TreeNode getAdminUnitsHierarchy() {
        return adminUnitsHierarchy;
    }

    public void setAdminUnitsHierarchy(TreeNode adminUnitsHierarchy) {
        this.adminUnitsHierarchy = adminUnitsHierarchy;
    }

    public GlAdminUnit getSelectedAU() {
        return selectedAU;
    }

    public void setSelectedAU(GlAdminUnit selectedAU) {
        this.selectedAU = selectedAU;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getShortCode() {
        return shortCode;
    }

    public void setShortCode(Integer shortCode) {
        this.shortCode = shortCode;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFormView() {
        return formView;
    }

    public void setFormView(boolean formView) {
        this.formView = formView;
    }

    public Integer getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Integer selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public List<TobyCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<TobyCompany> companies) {
        this.companies = companies;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public TobyCompany getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(TobyCompany CompanyId) {
        this.CompanyId = CompanyId;
    }

    public List<GlAdminUnit> getAllAdminUnits() {
        return allAdminUnits;
    }

    public void setAllAdminUnits(List<GlAdminUnit> allAdminUnits) {
        this.allAdminUnits = allAdminUnits;
    }

    public Integer getSelectedParent() {
        return selectedParent;
    }

    public void setSelectedParent(Integer selectedParent) {
        this.selectedParent = selectedParent;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public TreeNode getSelectedAdminUnits() {
        return selectedAdminUnits;
    }

    public void setSelectedAdminUnits(TreeNode selectedAdminUnits) {
        this.selectedAdminUnits = selectedAdminUnits;
    }

}
