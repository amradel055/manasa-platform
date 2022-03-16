/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.roles;

import com.toby.entity.TobyCompany;
import com.toby.entity.TobyRole;
import com.toby.entity.TobyUser;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.toby.businessservice.RoleService;
import java.util.Map;

/**
 *
 * @author hq002
 */
public class RoleListBean extends BaseListBean {

    @EJB
    private RoleService rolesService;

    private List<TobyRole> roles;
    private TobyRole role;
    private TobyUser tobyUser;

    private Integer selectedRole;
    private UserData userData;

    public RoleListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "RoleListBean", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedRole != null) {
                role = new TobyRole();
                getRole().setId(selectedRole);
                setRole(getRole());

                try {
                    rolesService.deleteRole(role);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }

            }
        } catch (Exception e) {
            saveError(e, "RoleListBean", "delete");
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
            context.getSessionMap().put("roleMode", "add");
            exit("../role/roleForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "RoleListBean", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (selectedRole != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("selectedRole", selectedRole);
                context.getSessionMap().put("roleMode", "edit");
                exit("../role/roleForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "RoleListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData != null) {
                TobyCompany companyId = userData.getCompany();
                if (companyId != null && companyId.getId() != null) {
                    //  get all user to all roles to this company
                    roles = getAllRolesByCompanyId(companyId.getId());
                } else {
                    roles = getAllRoles();
//             get all roles to all companies
                }
            }
        } catch (Exception e) {
            saveError(e, "RoleListBean", "load");
        }
    }

    public List<TobyRole> getAllRolesByCompanyId(Integer companyId) {
        setRoles(new ArrayList<TobyRole>());
        return rolesService.getAllRolesListByCompanyId(companyId);
    }

    public List<TobyRole> getAllRoles() {
        setRoles(new ArrayList<TobyRole>());
        return rolesService.getAllRoles();
    }

    @Override
    public String getScreenName() {
        return "role";
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

    public TobyUser getTobyUser() {
        return tobyUser;
    }

    public void setTobyUser(TobyUser tobyUser) {
        this.tobyUser = tobyUser;
    }

    public RoleService getRolesService() {
        return rolesService;
    }

    public void setRolesService(RoleService rolesService) {
        this.rolesService = rolesService;
    }

    public Integer getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Integer selectedRole) {
        this.selectedRole = selectedRole;
    }

    public TobyRole getRole() {
        return role;
    }

    public void setRole(TobyRole role) {
        this.role = role;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
