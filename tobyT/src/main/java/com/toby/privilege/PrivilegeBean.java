/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.privilege;

import com.toby.businessservice.PrivilegeService;
import com.toby.entity.TobyPrivilege;
import com.toby.entity.TobyRole;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import com.toby.businessservice.RoleService;
import com.toby.businessservice.ScreenService;
import com.toby.businessservice.TobyUserRoleService;
import com.toby.entity.TobyScreen;
import com.toby.entity.TobyUserRole;
import com.toby.toby.UserData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq003
 */
@Named(value = "privilegeBean")
@ViewScoped
public class PrivilegeBean implements Serializable {

    private List<TobyRole> roles;
    private TobyRole selectedRoleId;

    private List<TobyPrivilege> privileges;
    private TobyPrivilege tobyPrivilegeSelected;

    private boolean rowSelected;

    private boolean operationMsg;
    private Integer moduleSelect;
    private List<TobyPrivilege> moduleName;
    private List<TobyPrivilege> ListPackup;
    private Boolean viewSelect;
    private Boolean editSelect;
    private Boolean addSelect;
    private Boolean deleteSelect;

    @EJB
    private RoleService roleService;
    @EJB
    private PrivilegeService privilegeService;

    @EJB
    private ScreenService screenService;

    @EJB
    private TobyUserRoleService tobyUserRoleService;

    @PostConstruct
    public void init() {
        System.out.println("1");
    }

    public void selectRole(SelectEvent event) {
        try {
            try {
                TobyRole selectedRole;
                List<TobyScreen> unassignedScreens;
                selectedRole = (TobyRole) event.getObject();
                setPrivileges(privilegeService.getRolePrivileges(selectedRole.getId()));
                setListPackup(getPrivileges());

                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                UserData userData = (UserData) context.getSessionMap().get("userLogInData");
            
                TobyUserRole loginrole = tobyUserRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), 182);
                unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRole.getId(), loginrole.getRoleId().getId());
                for (int i = 0; i < unassignedScreens.size(); i++) {
                    TobyScreen screen = unassignedScreens.get(i);
                    TobyPrivilege priv = new TobyPrivilege();
                    priv.setAdd(Boolean.FALSE);
                    priv.setDelete(Boolean.FALSE);
                    priv.setEdit(Boolean.FALSE);
                    priv.setView(Boolean.FALSE);
                    priv.setRoleId(selectedRoleId);
                    priv.setScreenId(screen);
                    if (screen.getName().equalsIgnoreCase("company") && userData.getCompany() == null) {

                    } else {
                        privileges.add(priv);
                    }
                }
                checkAllSelecteActiveOrNot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "selectRole");
        }
    }
    
    private void checkAllSelecteActiveOrNot() {
        Boolean[] selectedResult = updateViewAllSelect();
        setViewSelect(selectedResult[0]);
        setEditSelect(selectedResult[1]);
        setAddSelect(selectedResult[2]);
        setDeleteSelect(selectedResult[3]);
    }
    
    private Boolean[] updateViewAllSelect() {
        Boolean notActiveView = Boolean.TRUE, notActiveEdit = Boolean.TRUE, notActiveAdd = Boolean.TRUE, notActiveDelete = Boolean.TRUE;
        for (TobyPrivilege tobyPrivilege : getPrivileges()) {
            if (!tobyPrivilege.getView() && notActiveView) {
                notActiveView = Boolean.FALSE;
            }
            if (!tobyPrivilege.getEdit() && notActiveEdit) {
                notActiveEdit = Boolean.FALSE;
            }
            if (!tobyPrivilege.getAdd() && notActiveAdd) {
                notActiveAdd = Boolean.FALSE;
            }
            if (!tobyPrivilege.getDelete() && notActiveDelete) {
                notActiveDelete = Boolean.FALSE;
            }
            if (!notActiveView && !notActiveEdit && !notActiveAdd && !notActiveDelete) {
                break;
            }
        }
        Boolean[] result = {notActiveView, notActiveEdit, notActiveAdd, notActiveDelete};
        return result;
    }

    /**
     * @return the roles
     */
    public List<TobyRole> getRoles() {
        if (roles == null) {
            roles = roleService.getAllRolesListByCompanyId(125);
        }
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<TobyRole> roles) {
        this.roles = roles;
    }

    /**
     * @return the tobyPrivilegeSelected
     */
    public TobyPrivilege getTobyPrivilegeSelected() {
        return tobyPrivilegeSelected;
    }

    /**
     * @param tobyPrivilegeSelected the tobyPrivilegeSelected to set
     */
    public void setTobyPrivilegeSelected(TobyPrivilege tobyPrivilegeSelected) {
        this.tobyPrivilegeSelected = tobyPrivilegeSelected;
    }

    /**
     * @return the selectedRoleId
     */
    public TobyRole getSelectedRoleId() {
        return selectedRoleId;
    }

    /**
     * @param selectedRoleId the selectedRoleId to set
     */
    public void setSelectedRoleId(TobyRole selectedRoleId) {
        this.selectedRoleId = selectedRoleId;
    }

    /**
     * @return the privileges
     */
    public List<TobyPrivilege> getPrivileges() {
        return privileges;
    }

    /**
     * @param privileges the privileges to set
     */
    public void setPrivileges(List<TobyPrivilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * @return the rowSelected
     */
    public boolean isRowSelected() {
        return rowSelected;
    }

    /**
     * @param rowSelected the rowSelected to set
     */
    public void setRowSelected(boolean rowSelected) {
        this.rowSelected = rowSelected;
    }

    /**
     * @return the operationMsg
     */
    public boolean isOperationMsg() {
        return operationMsg;
    }

    /**
     * @param operationMsg the operationMsg to set
     */
    public void setOperationMsg(boolean operationMsg) {
        this.operationMsg = operationMsg;
    }

    /**
     * @return the moduleSelect
     */
    public Integer getModuleSelect() {
        return moduleSelect;
    }

    /**
     * @param moduleSelect the moduleSelect to set
     */
    public void setModuleSelect(Integer moduleSelect) {
        this.moduleSelect = moduleSelect;
    }

    /**
     * @return the moduleName
     */
    public List<TobyPrivilege> getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(List<TobyPrivilege> moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @return the ListPackup
     */
    public List<TobyPrivilege> getListPackup() {
        return ListPackup;
    }

    /**
     * @param ListPackup the ListPackup to set
     */
    public void setListPackup(List<TobyPrivilege> ListPackup) {
        this.ListPackup = ListPackup;
    }

    /**
     * @return the viewSelect
     */
    public Boolean getViewSelect() {
        return viewSelect;
    }

    /**
     * @param viewSelect the viewSelect to set
     */
    public void setViewSelect(Boolean viewSelect) {
        this.viewSelect = viewSelect;
    }

    /**
     * @return the editSelect
     */
    public Boolean getEditSelect() {
        return editSelect;
    }

    /**
     * @param editSelect the editSelect to set
     */
    public void setEditSelect(Boolean editSelect) {
        this.editSelect = editSelect;
    }

    /**
     * @return the addSelect
     */
    public Boolean getAddSelect() {
        if (addSelect == null) {
            addSelect = Boolean.FALSE;
        }
        return addSelect;
    }

    /**
     * @param addSelect the addSelect to set
     */
    public void setAddSelect(Boolean addSelect) {
        this.addSelect = addSelect;
    }

    /**
     * @return the deleteSelect
     */
    public Boolean getDeleteSelect() {
        if (deleteSelect == null) {
            deleteSelect = Boolean.FALSE;
        }
        return deleteSelect;
    }

    /**
     * @param deleteSelect the deleteSelect to set
     */
    public void setDeleteSelect(Boolean deleteSelect) {
        this.deleteSelect = deleteSelect;
    }
}
