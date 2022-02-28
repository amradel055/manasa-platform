/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.userrole;

import com.isag.converter.BranchConverter;
import com.isag.converter.IsagUserDTOConverter;
import com.isag.dto.IgmaUserDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUserRole;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.IgmaUserRoleService;
import com.isag.businessservice.IgmaUserService;

/**
 *
 * @author hq002
 */
@Named(value = "userRoleFieldFormBean")
@ViewScoped
public class UserRoleFieldFormBean extends BaseFormBean implements Serializable {

    private List<IgmaUserDTO> isagList;
    private List<Branch> branchList;

    private IgmaUserDTO isagUserSelected;
    private Branch branchDTOSelected;
    private IgmaUserRole isagUserRole;

    private IsagUserDTOConverter isagUserDTOConverter;
    private BranchConverter branchConverter;
    
    private Boolean costAvarageView;
    private Boolean sellPriceUpdate;

    @EJB
    IgmaUserRoleService isagUserRoleService;
    @EJB
    IgmaUserService isagUserService;

    public UserRoleFieldFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            getIsagList();
            getBranchList();
            selectBranch();
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "load");
        }
    }

    public void selectBranch() {
        if (getIsagUserSelected() != null && getIsagUserSelected().getId() != null && getBranchDTOSelected() != null && getBranchDTOSelected().getId() != null) {
            setIsagUserRole(isagUserRoleService.findUserRoleByUserIdAndBranch(getIsagUserSelected().getId(), getBranchDTOSelected().getId()));
            if(getIsagUserRole() != null && getIsagUserRole().getCostAvarageUpdate() != null && getIsagUserRole().getCostAvarageUpdate()){
                setCostAvarageView(Boolean.TRUE);
            }
            if(getIsagUserRole() != null && getIsagUserRole().getSellPriceUpdate()!= null && getIsagUserRole().getSellPriceUpdate()){
                setSellPriceUpdate(Boolean.TRUE);
            }
        }
    }

    @Override
    public String save() {
        getIsagUserRole().setCostAvarageUpdate(getCostAvarageView() );
        getIsagUserRole().setSellPriceUpdate(getSellPriceUpdate());
        isagUserRoleService.addUserRole(getIsagUserRole());
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the isagList
     */
    public List<IgmaUserDTO> getIsagList() {
        if (isagList == null || isagList.isEmpty()) {
            isagList = isagUserService.getUsersByCompanyId(getUserData().getCompany().getId());
            if (isagList != null && !isagList.isEmpty()) {
                setIsagUserSelected(isagList.get(0));
            }
            isagUserDTOConverter = new IsagUserDTOConverter(isagList);
        }
        return isagList;
    }

    /**
     * @param isagList the isagList to set
     */
    public void setIsagList(List<IgmaUserDTO> isagList) {
        this.isagList = isagList;
    }

    /**
     * @return the isagUserRole
     */
    public IgmaUserRole getIsagUserRole() {
        return isagUserRole;
    }

    /**
     * @param isagUserRole the isagUserRole to set
     */
    public void setIsagUserRole(IgmaUserRole isagUserRole) {
        this.isagUserRole = isagUserRole;
    }

    /**
     * @return the isagUserDTOConverter
     */
    public IsagUserDTOConverter getIsagUserDTOConverter() {
        return isagUserDTOConverter;
    }

    /**
     * @param isagUserDTOConverter the isagUserDTOConverter to set
     */
    public void setIsagUserDTOConverter(IsagUserDTOConverter isagUserDTOConverter) {
        this.isagUserDTOConverter = isagUserDTOConverter;
    }

    /**
     * @return the isagUserSelected
     */
    public IgmaUserDTO getIsagUserSelected() {
        return isagUserSelected;
    }

    /**
     * @param isagUserSelected the isagUserSelected to set
     */
    public void setIsagUserSelected(IgmaUserDTO isagUserSelected) {
        this.isagUserSelected = isagUserSelected;
    }

    /**
     * @return the branchList
     */
    public List<Branch> getBranchList() {
        if (getIsagUserSelected() != null && getIsagUserSelected().getId() != null) {
            branchList = isagUserRoleService.findBranchByUserId(getIsagUserSelected().getId());
            if (branchList != null && !branchList.isEmpty()) {
                setBranchDTOSelected(branchList.get(0));
            }
            setBranchConverter(new BranchConverter(branchList));
        }
        return branchList;
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    /**
     * @return the branchDTOSelected
     */
    public Branch getBranchDTOSelected() {
        return branchDTOSelected;
    }

    /**
     * @param branchDTOSelected the branchDTOSelected to set
     */
    public void setBranchDTOSelected(Branch branchDTOSelected) {
        this.branchDTOSelected = branchDTOSelected;
    }

    /**
     * @return the branchConverter
     */
    public BranchConverter getBranchConverter() {
        return branchConverter;
    }

    /**
     * @param branchConverter the branchConverter to set
     */
    public void setBranchConverter(BranchConverter branchConverter) {
        this.branchConverter = branchConverter;
    }

    /**
     * @return the costAvarageView
     */
    public Boolean getCostAvarageView() {
        if(costAvarageView == null){
            costAvarageView = Boolean.FALSE;
        }
        return costAvarageView;
    }

    /**
     * @param costAvarageView the costAvarageView to set
     */
    public void setCostAvarageView(Boolean costAvarageView) {
        this.costAvarageView = costAvarageView;
    }

    /**
     * @return the sellPriceUpdate
     */
    public Boolean getSellPriceUpdate() {
        if(sellPriceUpdate == null){
            sellPriceUpdate = Boolean.FALSE;
        }
        return sellPriceUpdate;
    }

    /**
     * @param sellPriceUpdate the sellPriceUpdate to set
     */
    public void setSellPriceUpdate(Boolean sellPriceUpdate) {
        this.sellPriceUpdate = sellPriceUpdate;
    }

}
