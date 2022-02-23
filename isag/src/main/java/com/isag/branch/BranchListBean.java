/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.branch;

import com.isag.businessservice.BranchService;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "branchListBean")
@ViewScoped
public class BranchListBean extends BaseListBean implements Serializable {

    private List<Branch> branches;
    private Branch branch;
    private Integer branchselected;

    @EJB
    private BranchService brancheService;
    private UserData userData;

    public BranchListBean() {
    }
    
    @PreDestroy
    public void preDestroy() {
        System.err.println("yarab");
    }

    /**
     * @return the branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getBranchselected() {
        return branchselected;
    }

    /**
     * @param branchselected the branchselected to set
     */
    public void setBranchselected(int branchselected) {
        this.branchselected = branchselected;
    }

    /**
     * @return the brancheService
     */
    public BranchService getBrancheService() {
        return brancheService;
    }

    /**
     * @param brancheService the brancheService to set
     */
    public void setBrancheService(BranchService brancheService) {
        this.brancheService = brancheService;
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "BranchListBean", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (branch != null && branch.getId() > 0) {
                try {
                    brancheService.deleteBranch(branch);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "delete");
        }
    }

    @Override
    public void filter() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../branch/branchForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (branchselected != null && branchselected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("brancheselected", branchselected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../branch/branchForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToEdit");
            return null;
        }

    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData != null) {
                IsagCompany companyId = userData.getCompany();
                if (companyId != null && companyId.getId() != null) {
                    branches = brancheService.getAllBranchByCompanyId(companyId.getId());
                } else {
                    branches = brancheService.getAllBranch();
                }
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
