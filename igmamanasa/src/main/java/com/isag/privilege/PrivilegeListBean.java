/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.privilege;

import com.isag.businessservice.PrivilegeService;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaPrivilege;
import com.isag.entity.IgmaRole;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import com.isag.businessservice.RoleService;
import com.isag.businessservice.ScreenService;
import com.isag.entity.IgmaScreen;
import com.isag.entity.IgmaUserRole;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.FacesMessage;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import javax.faces.event.ActionEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ToggleSelectEvent;
import com.isag.businessservice.IgmaUserRoleService;

/**
 *
 * @author hq003
 */
@Named(value = "privilegeListBean")
@ViewScoped
public class PrivilegeListBean extends BaseListBean implements Serializable {

    private IgmaPrivilege isagPrivilegeSelected = new IgmaPrivilege();
    private Set<IgmaPrivilege> isagPrivilegeSelectedList = new HashSet<>();

    private List<IgmaPrivilege> privileges = new ArrayList<>();

    private List<IgmaPrivilege> privilegesSelected = new ArrayList<>();

    private List<IgmaRole> roles = new ArrayList<>();
    private IgmaRole selectedRoleId;

    private boolean rowSelected;
    private UserData userData;

    private boolean operationMsg = Boolean.FALSE;
    private Integer moduleSelect;
    private List<IgmaPrivilege> moduleName;
    private List<IgmaPrivilege> ListPackup;
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
    private IgmaUserRoleService isagUserRoleService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void delete() {

    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        return "";
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            IgmaCompany companyId = userData.getCompany();
            if (companyId != null && companyId.getId() != null) {
                roles = roleService.getAllRolesListByCompanyId(companyId.getId());
            } else {
                roles = roleService.getAllRoles();
            }
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "load");
        }
    }

    public void selectRole(SelectEvent event) {
        try {
            try {
                IgmaRole selectedRole;
                List<IgmaScreen> unassignedScreens;
                selectedRole = (IgmaRole) event.getObject();
                setPrivileges(privilegeService.getRolePrivileges(selectedRole.getId()));
                setListPackup(getPrivileges());
                if (getUserData().getIsAdmin()) {
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRole.getId(), null);
                } else {
                    IgmaUserRole loginrole = isagUserRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRole.getId(), loginrole.getRoleId().getId());
                }
                for (int i = 0; i < unassignedScreens.size(); i++) {
                    IgmaScreen screen = unassignedScreens.get(i);
                    IgmaPrivilege priv = new IgmaPrivilege();
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
            saveError(e, "PrivilegeListBean", "selectRole");
        }
    }

    public void selectRolePrevelage(SelectEvent event) {
        try {
            try {
                isagPrivilegeSelected = (IgmaPrivilege) event.getObject();

                setRowSelected(true);
                int x = 5;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "selectRolePrevelage");
        }
    }

    public void unselectRole(SelectEvent event) {

    }

    @Override
    public String getScreenName() {
        return "Privilege";
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            IgmaPrivilege privilege = (IgmaPrivilege) event.getObject();
            List<IgmaScreen> unassignedScreens;
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
                    IgmaUserRole loginrole = isagUserRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());
                    unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRoleId.getId(), loginrole.getId());
                }
//                unassignedScreens = screenService.getAllScreenNotAssignedToRole(selectedRoleId.getId());
                for (int i = 0; i < unassignedScreens.size(); i++) {
                    IgmaScreen screen = unassignedScreens.get(i);
                    IgmaPrivilege priv = new IgmaPrivilege();
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
            saveError(e, "PrivilegeListBean", "onRowEdit");
        }
    }

    public void onRowCancel(RowEditEvent event) {
        try {
            FacesMessage msg = new FacesMessage("Edit Cancelled", "rdu");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "onRowCancel");
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
            saveError(e, "PrivilegeListBean", "onCheckBoxEdit");
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
            saveError(e, "PrivilegeListBean", "onCellEdit");
        }

    }

    public void onCheckBoxChecked() {
        try {

            if (isagPrivilegeSelected != null && isagPrivilegeSelected.getId() != null) {

                isagPrivilegeSelectedList.add(isagPrivilegeSelected);
                privilegesSelected.add(isagPrivilegeSelected);
            }
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "onCheckBoxChecked");
        }
    }

    public void behindDelete() {
        if (isagPrivilegeSelected != null && isagPrivilegeSelected.getDelete() != null && isagPrivilegeSelected.getDelete()) {
            isagPrivilegeSelected.setAdd(true);
            isagPrivilegeSelected.setEdit(true);
            isagPrivilegeSelected.setView(true);
        } else {

        }
        replacedetail(isagPrivilegeSelected);
    }

    public void behindEdit() {
        if (isagPrivilegeSelected != null && isagPrivilegeSelected.getEdit() != null && isagPrivilegeSelected.getEdit()) {
            isagPrivilegeSelected.setAdd(true);
            isagPrivilegeSelected.setView(true);
        } else {
            isagPrivilegeSelected.setDelete(false);

        }
        replacedetail(isagPrivilegeSelected);
    }

    public void behindAdd() {
        if (isagPrivilegeSelected != null && isagPrivilegeSelected.getAdd() != null && isagPrivilegeSelected.getAdd()) {
            isagPrivilegeSelected.setView(true);
        } else {
            isagPrivilegeSelected.setEdit(false);
            isagPrivilegeSelected.setDelete(false);
        }
        replacedetail(isagPrivilegeSelected);
    }

    public void unSelectALL() {
        if (isagPrivilegeSelected != null && isagPrivilegeSelected.getView() != null && !isagPrivilegeSelected.getView()) {
            isagPrivilegeSelected.setAdd(false);
            isagPrivilegeSelected.setEdit(false);
            isagPrivilegeSelected.setDelete(false);
        }
        replacedetail(isagPrivilegeSelected);
    }

    public void updateAll() {
        try {
            operationMsg = Boolean.TRUE;
            if (selectedRoleId != null) {
                try {
                    privileges = ListPackup;
                    for (IgmaPrivilege privilege : privileges) {
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
            saveError(e, "PrivilegeListBean", "updateAll");
        }

    }

    public void grantAll() {
        try {
            List<IgmaScreen> screens = screenService.getAllScreens();
            if (privileges != null && selectedRoleId != null) {
                privilegeService.revokeAllPrivilege(selectedRoleId.getId());
                IgmaPrivilege privilege;
                if (getUserData().getIsAdmin()) {
                    for (IgmaScreen screen : screens) {
                        if (!"company".equals(screen.getName())) {
                            privilege = new IgmaPrivilege();
                            fillPrivilage(privilege);
                            privilege.setScreenId(screen);
                            privilegeService.save(privilege);
                        }

                    }
                } else {
                    for (IgmaPrivilege isagPrivilege : privileges) {
                        fillPrivilage(isagPrivilege);
                        privilegeService.save(isagPrivilege);
                    }
                }
                setPrivileges(privilegeService.getRolePrivileges(selectedRoleId.getId()));
            }
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "grantAll");
        }
    }

    private void fillPrivilage(IgmaPrivilege privilege) {
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
                List<IgmaScreen> screens = screenService.getAllScreens();
                privilegeService.revokeAllPrivilege(selectedRoleId.getId());
                // setPrivileges(privilegeService.getRolePrivileges(selectedRoleId.getId()));   
                privileges = new ArrayList<>();
                if (getUserData().getIsAdmin()) {
                    for (IgmaScreen screen : screens) {
                        IgmaPrivilege privilege = new IgmaPrivilege();
                        privilege.setAdd(Boolean.FALSE);
                        privilege.setView(Boolean.FALSE);
                        privilege.setDelete(Boolean.FALSE);
                        privilege.setEdit(Boolean.FALSE);
                        privilege.setScreenId(screen);
                        privileges.add(privilege);
                    }
                } else {
                    for (IgmaPrivilege privilege : privileges) {
                        privilege.setAdd(Boolean.FALSE);
                        privilege.setView(Boolean.FALSE);
                        privilege.setDelete(Boolean.FALSE);
                        privilege.setEdit(Boolean.FALSE);
                    }
                }

            }
        } catch (Exception e) {
            saveError(e, "PrivilegeListBean", "revokeAll");
        }

    }

    public void deletePrivlege(ActionEvent e) {

    }

    private void replacedetail(IgmaPrivilege isagPrivilege) {
        if (isagPrivilege != null) {
            for (IgmaPrivilege privilege : getListPackup()) {
                if (privilege.getScreenId().getId().equals(isagPrivilege.getScreenId().getId())) {
                    int itemIndex = getListPackup().indexOf(privilege);
                    if (itemIndex != -1) {
                        getListPackup().set(itemIndex, isagPrivilege);
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
            for (IgmaPrivilege isagPrivilege : getListPackup()) {
                if (isagPrivilege != null && isagPrivilege.getScreenId() != null && isagPrivilege.getScreenId().getModuleType() != null && isagPrivilege.getScreenId().getModuleType().equals(filterText)) {
                    getModuleName().add(isagPrivilege);

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
        for (IgmaPrivilege isagPrivilege : getPrivileges()) {
            if (!isagPrivilege.getView() && notActiveView) {
                notActiveView = Boolean.FALSE;
            }
            if (!isagPrivilege.getEdit() && notActiveEdit) {
                notActiveEdit = Boolean.FALSE;
            }
            if (!isagPrivilege.getAdd() && notActiveAdd) {
                notActiveAdd = Boolean.FALSE;
            }
            if (!isagPrivilege.getDelete() && notActiveDelete) {
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
        for (IgmaPrivilege isagPrivilege : getPrivileges()) {
            isagPrivilegeSelected = isagPrivilege;
            isagPrivilegeSelected.setView(getViewSelect());
            unSelectALL();
            isagPrivilegeSelected = null;
        }
    }

    public void updateEditColumn() {

        for (IgmaPrivilege isagPrivilege : getPrivileges()) {
            isagPrivilegeSelected = isagPrivilege;
            isagPrivilegeSelected.setEdit(getEditSelect());
            behindEdit();
            isagPrivilegeSelected = null;
        }

    }

    public void updateAddColumn() {
        for (IgmaPrivilege isagPrivilege : getPrivileges()) {
            isagPrivilegeSelected = isagPrivilege;
            isagPrivilegeSelected.setAdd(getAddSelect());
            behindAdd();
            isagPrivilegeSelected = null;
        }
    }

    public void updateDeleteColumn() {
        for (IgmaPrivilege isagPrivilege : getPrivileges()) {
            isagPrivilegeSelected = isagPrivilege;
            isagPrivilegeSelected.setDelete(getDeleteSelect());
            behindDelete();
            isagPrivilegeSelected = null;
        }
    }

    /**
     * @return the privileges
     */
    public List<IgmaPrivilege> getPrivileges() {
        return privileges;
    }

    /**
     * @param privileges the privileges to set
     */
    public void setPrivileges(List<IgmaPrivilege> privileges) {
        this.privileges = privileges;
    }

    public IgmaPrivilege getIsagPrivilegeSelected() {
        return isagPrivilegeSelected;
    }

    public void setIsagPrivilegeSelected(IgmaPrivilege isagPrivilegeSelected) {
        this.isagPrivilegeSelected = isagPrivilegeSelected;
    }

    /**
     * @return the roles
     */
    public List<IgmaRole> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<IgmaRole> roles) {
        this.roles = roles;
    }

    /**
     * @return the selectedRoleId
     */
    public IgmaRole getSelectedRoleId() {
        return selectedRoleId;
    }

    /**
     * @param selectedRoleId the selectedRoleId to set
     */
    public void setSelectedRoleId(IgmaRole selectedRoleId) {
        this.selectedRoleId = selectedRoleId;
    }

    public Set<IgmaPrivilege> getIsagPrivilegeSelectedList() {
        return isagPrivilegeSelectedList;
    }

    public void setIsagPrivilegeSelectedList(Set<IgmaPrivilege> isagPrivilegeSelectedList) {
        this.isagPrivilegeSelectedList = isagPrivilegeSelectedList;
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

    public List<IgmaPrivilege> getPrivilegesSelected() {
        return privilegesSelected;
    }

    public void setPrivilegesSelected(List<IgmaPrivilege> privilegesSelected) {
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
    public List<IgmaPrivilege> getModuleName() {
        if (moduleName == null || moduleName.isEmpty()) {
            moduleName = new ArrayList<>();
        }
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(List<IgmaPrivilege> moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @return the ListPackup
     */
    public List<IgmaPrivilege> getListPackup() {
        return ListPackup;
    }

    /**
     * @param ListPackup the ListPackup to set
     */
    public void setListPackup(List<IgmaPrivilege> ListPackup) {
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
