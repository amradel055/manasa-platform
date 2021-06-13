/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.privilege;

import com.toby.businessservice.TobyUserRoleService;
import com.toby.businessservice.PrivilegeService;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyPrivilege;
import com.toby.entity.TobyRole;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import com.toby.businessservice.RoleService;
import com.toby.businessservice.ScreenService;
import com.toby.entity.TobyScreen;
import com.toby.entity.TobyUserRole;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.FacesMessage;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import javax.faces.event.ActionEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ToggleSelectEvent;

/**
 *
 * @author hq003
 */
@Named(value = "privilegeListBean")
@ViewScoped
public class PrivilegeListBean implements Serializable {

    private TobyPrivilege tobyPrivilegeSelected = new TobyPrivilege();
    private Set<TobyPrivilege> tobyPrivilegeSelectedList = new HashSet<>();

    private List<TobyPrivilege> privileges = new ArrayList<>();

    private List<TobyPrivilege> privilegesSelected = new ArrayList<>();

    private List<TobyRole> roles = new ArrayList<>();
    private TobyRole selectedRoleId;

    private boolean rowSelected;
    private UserData userData;

    private boolean operationMsg = Boolean.FALSE;
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
            load();
    }

    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            TobyCompany companyId = userData.getCompany();
            if (companyId != null && companyId.getId() != null) {
                roles = roleService.getAllRolesListByCompanyId(companyId.getId());
            } else {
                roles = roleService.getAllRoles();
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "load");
        }
    }

    public void selectRole(SelectEvent event) {
        try {
            try {
                TobyRole selectedRole;
                List<TobyScreen> unassignedScreens;
                selectedRole = (TobyRole) event.getObject();
                setPrivileges(privilegeService.getRolePrivileges(selectedRole.getId()));
                setListPackup(getPrivileges());
                if (getUserData().getIsAdmin()) {
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRole.getId(), null);
                } else {
                    TobyUserRole loginrole = tobyUserRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRole.getId(), loginrole.getRoleId().getId());
                }
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

    public void selectRolePrevelage(SelectEvent event) {
        try {
            try {
                tobyPrivilegeSelected = (TobyPrivilege) event.getObject();

                setRowSelected(true);
                int x = 5;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "selectRolePrevelage");
        }
    }

    public void unselectRole(SelectEvent event) {

    }

//    @Override
    public String getScreenName() {
        return "Privilege";
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            TobyPrivilege privilege = (TobyPrivilege) event.getObject();
            List<TobyScreen> unassignedScreens;
            privilege.setModifiedBy(userData.getUser());
            privilege.setModificationDate(new Date());
            if (privilege.getId() != null) {
                privilege.setCreatedBy(userData.getUser());
                privilegeService.save(privilege);
            } else {
                privilegeService.save(privilege);
                setPrivileges(privilegeService.getRolePrivileges(selectedRoleId.getId()));
                if (getUserData().getIsAdmin()) {
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRoleId.getId(), null);
                } else {
                    TobyUserRole loginrole = tobyUserRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRoleId.getId(), loginrole.getId());
                }
//                unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRoleId.getId());
                for (int i = 0; i < unassignedScreens.size(); i++) {
                    TobyScreen screen = unassignedScreens.get(i);
                    TobyPrivilege priv = new TobyPrivilege();
                    priv.setAdd(Boolean.FALSE);
                    priv.setDelete(Boolean.FALSE);
                    priv.setEdit(Boolean.FALSE);
                    priv.setView(Boolean.FALSE);
                    priv.setRoleId(selectedRoleId);
                    priv.setScreenId(screen);
                    privileges.add(priv);
                }
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "onRowEdit");
        }
    }

    public void onRowCancel(RowEditEvent event) {
        try {
            FacesMessage msg = new FacesMessage("Edit Cancelled", "rdu");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "onRowCancel");
        }
    }

    public void edittable(RowEditEvent event) {

        System.out.println(event);
    }

    public void onCheckBoxEdit(ToggleSelectEvent event) {
        try {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();

            event.getComponent();
            event.isSelected();

            String column_name = event.getComponent().getClientId();
            String[] parts = column_name.split("screenActionForm:privList:1:");
            String part1 = parts[0];
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "onCheckBoxEdit");
        }

    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();

            String column_name = event.getColumn().getClientId();
            String[] parts = column_name.split("screenActionForm:privList:1:");
            String part1 = parts[0];
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "onCellEdit");
        }

    }

    public void onCheckBoxChecked() {
        try {

            if (tobyPrivilegeSelected != null && tobyPrivilegeSelected.getId() != null) {

                tobyPrivilegeSelectedList.add(tobyPrivilegeSelected);
                privilegesSelected.add(tobyPrivilegeSelected);
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "onCheckBoxChecked");
        }
    }

    public void behindDelete() {
        if (tobyPrivilegeSelected != null && tobyPrivilegeSelected.getDelete() != null && tobyPrivilegeSelected.getDelete()) {
            tobyPrivilegeSelected.setAdd(true);
            tobyPrivilegeSelected.setEdit(true);
            tobyPrivilegeSelected.setView(true);
        } else {

        }
        replacedetail(tobyPrivilegeSelected);
    }

    public void behindEdit() {
        if (tobyPrivilegeSelected != null && tobyPrivilegeSelected.getEdit() != null && tobyPrivilegeSelected.getEdit()) {
            tobyPrivilegeSelected.setAdd(true);
            tobyPrivilegeSelected.setView(true);
        } else {
            tobyPrivilegeSelected.setDelete(false);

        }
        replacedetail(tobyPrivilegeSelected);
    }

    public void behindAdd() {
        if (tobyPrivilegeSelected != null && tobyPrivilegeSelected.getAdd() != null && tobyPrivilegeSelected.getAdd()) {
            tobyPrivilegeSelected.setView(true);
        } else {
            tobyPrivilegeSelected.setEdit(false);
            tobyPrivilegeSelected.setDelete(false);
        }
        replacedetail(tobyPrivilegeSelected);
    }

    public void unSelectALL() {
        if (tobyPrivilegeSelected != null && tobyPrivilegeSelected.getView() != null && !tobyPrivilegeSelected.getView()) {
            tobyPrivilegeSelected.setAdd(false);
            tobyPrivilegeSelected.setEdit(false);
            tobyPrivilegeSelected.setDelete(false);
        }
        replacedetail(tobyPrivilegeSelected);
    }

    public void updateAll() {
        try {
            operationMsg = Boolean.TRUE;
            if (selectedRoleId != null) {
                try {
                    privileges = ListPackup;
                    for (TobyPrivilege privilege : privileges) {
                        if (privilege.getId() == null) {
                            privilege.setCreatedBy(getUserData().getUser());
                            privilege.setCreationDate(new Date());
                            privilege.setCompanyId(getUserData().getCompany());
                        }
                    }
                    privilegeService.save(privileges);
                    privilegeService.revokePrivileges();
                    checkAllSelecteActiveOrNot();
                    setModuleSelect(null);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("PRIVILEGE_ERROR")));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("CHOOSE_GROUP_ERROR")));
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "updateAll");
        }

    }

    public void grantAll() {
        try {
            List<TobyScreen> screens = screenService.getAllScreens();
            if (privileges != null && selectedRoleId != null) {
                privilegeService.revokeAllPrivilege(selectedRoleId.getId());
                TobyPrivilege privilege;
                if (getUserData().getIsAdmin()) {
                    for (TobyScreen screen : screens) {
                        if (!"company".equals(screen.getName())) {
                            privilege = new TobyPrivilege();
                            fillPrivilage(privilege);
                            privilege.setScreenId(screen);
                            privilegeService.save(privilege);
                        }

                    }
                } else {
                    for (TobyPrivilege tobyPrivilege : privileges) {
                        fillPrivilage(tobyPrivilege);
                        privilegeService.save(tobyPrivilege);
                    }
                }
                setPrivileges(privilegeService.getRolePrivileges(selectedRoleId.getId()));
            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "grantAll");
        }
    }

    private void fillPrivilage(TobyPrivilege privilege) {
        privilege.setAdd(Boolean.TRUE);
        privilege.setView(Boolean.TRUE);
        privilege.setDelete(Boolean.TRUE);
        privilege.setEdit(Boolean.TRUE);
        privilege.setRoleId(selectedRoleId);
        privilege.setCreatedBy(userData.getUser());
        privilege.setCreationDate(new Date());
    }

    public void revokeAll() {
        try {
            if (privileges != null && selectedRoleId != null) {
                List<TobyScreen> screens = screenService.getAllScreens();
                privilegeService.revokeAllPrivilege(selectedRoleId.getId());
                // setPrivileges(privilegeService.getRolePrivileges(selectedRoleId.getId()));   
                privileges = new ArrayList<>();
                if (getUserData().getIsAdmin()) {
                    for (TobyScreen screen : screens) {
                        TobyPrivilege privilege = new TobyPrivilege();
                        privilege.setAdd(Boolean.FALSE);
                        privilege.setView(Boolean.FALSE);
                        privilege.setDelete(Boolean.FALSE);
                        privilege.setEdit(Boolean.FALSE);
                        privilege.setScreenId(screen);
                        privileges.add(privilege);
                    }
                } else {
                    for (TobyPrivilege privilege : privileges) {
                        privilege.setAdd(Boolean.FALSE);
                        privilege.setView(Boolean.FALSE);
                        privilege.setDelete(Boolean.FALSE);
                        privilege.setEdit(Boolean.FALSE);
                    }
                }

            }
        } catch (Exception e) {
//            saveError(e, "PrivilegeListBean", "revokeAll");
        }

    }

    public void deletePrivlege(ActionEvent e) {

    }

    private void replacedetail(TobyPrivilege tobyPrivilege) {
        if (tobyPrivilege != null) {
            for (TobyPrivilege privilege : getListPackup()) {
                if (privilege.getScreenId().getId().equals(tobyPrivilege.getScreenId().getId())) {
                    int itemIndex = getListPackup().indexOf(privilege);
                    if (itemIndex != -1) {
                        getListPackup().set(itemIndex, tobyPrivilege);
                    }
                }
            }
        }
    }

    public void findScreenByModuleType() {
        Integer filterText = getModuleSelect() == null ? null : getModuleSelect();
        if (filterText == null || filterText.equals("")) {
            setPrivileges(getListPackup());
            checkAllSelecteActiveOrNot();
            return;
        }
        setModuleName(new ArrayList<>());
        if (filterText == null || filterText > 0) {
            for (TobyPrivilege tobyPrivilege : getListPackup()) {
                if (tobyPrivilege != null && tobyPrivilege.getScreenId() != null && tobyPrivilege.getScreenId().getModuleType() != null && tobyPrivilege.getScreenId().getModuleType().equals(filterText)) {
                    getModuleName().add(tobyPrivilege);

                }
            }
        }
        setPrivileges(getModuleName());
        updeteViewSelect();

    }

    private void checkAllSelecteActiveOrNot() {
        Boolean[] selectedResult = updateViewAllSelect();
        setViewSelect(selectedResult[0]);
        setEditSelect(selectedResult[1]);
        setAddSelect(selectedResult[2]);
        setDeleteSelect(selectedResult[3]);
    }

    private void updeteViewSelect() {
        checkAllSelecteActiveOrNot();
        updateViewColumn();
        updateEditColumn();
        updateAddColumn();
        updateDeleteColumn();

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

    public void updateViewColumn() {
        for (TobyPrivilege tobyPrivilege : getPrivileges()) {
            tobyPrivilegeSelected = tobyPrivilege;
            tobyPrivilegeSelected.setView(getViewSelect());
            unSelectALL();
            tobyPrivilegeSelected = null;
        }
    }

    public void updateEditColumn() {

        for (TobyPrivilege tobyPrivilege : getPrivileges()) {
            tobyPrivilegeSelected = tobyPrivilege;
            tobyPrivilegeSelected.setEdit(getEditSelect());
            behindEdit();
            tobyPrivilegeSelected = null;
        }

    }

    public void updateAddColumn() {
        for (TobyPrivilege tobyPrivilege : getPrivileges()) {
            tobyPrivilegeSelected = tobyPrivilege;
            tobyPrivilegeSelected.setAdd(getAddSelect());
            behindAdd();
            tobyPrivilegeSelected = null;
        }
    }

    public void updateDeleteColumn() {
        for (TobyPrivilege tobyPrivilege : getPrivileges()) {
            tobyPrivilegeSelected = tobyPrivilege;
            tobyPrivilegeSelected.setDelete(getDeleteSelect());
            behindDelete();
            tobyPrivilegeSelected = null;
        }
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

    public TobyPrivilege getTobyPrivilegeSelected() {
        return tobyPrivilegeSelected;
    }

    public void setTobyPrivilegeSelected(TobyPrivilege tobyPrivilegeSelected) {
        this.tobyPrivilegeSelected = tobyPrivilegeSelected;
    }

    /**
     * @return the roles
     */
    public List<TobyRole> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<TobyRole> roles) {
        this.roles = roles;
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

    public Set<TobyPrivilege> getTobyPrivilegeSelectedList() {
        return tobyPrivilegeSelectedList;
    }

    public void setTobyPrivilegeSelectedList(Set<TobyPrivilege> tobyPrivilegeSelectedList) {
        this.tobyPrivilegeSelectedList = tobyPrivilegeSelectedList;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public boolean isRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(boolean rowSelected) {
        this.rowSelected = rowSelected;
    }

    public List<TobyPrivilege> getPrivilegesSelected() {
        return privilegesSelected;
    }

    public void setPrivilegesSelected(List<TobyPrivilege> privilegesSelected) {
        this.privilegesSelected = privilegesSelected;
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
     * @return the moduleName
     */
    public List<TobyPrivilege> getModuleName() {
        if (moduleName == null || moduleName.isEmpty()) {
            moduleName = new ArrayList<>();
        }
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
     * @return the addSelect
     */
    public Boolean getAddSelect() {
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
        return deleteSelect;
    }

    /**
     * @param deleteSelect the deleteSelect to set
     */
    public void setDeleteSelect(Boolean deleteSelect) {
        this.deleteSelect = deleteSelect;
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
}
