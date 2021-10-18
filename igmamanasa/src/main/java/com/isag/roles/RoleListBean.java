/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.roles;

import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaRole;
import com.isag.entity.IgmaUser;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.isag.businessservice.RoleService;
import java.util.Map;

/**
 *
 * @author hq002
 */
public class RoleListBean extends BaseListBean {

    @EJB
    private RoleService rolesService;

    private List<IgmaRole> roles;
    private IgmaRole role;
    private IgmaUser isagUser;

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
                role = new IgmaRole();
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
                IgmaCompany companyId = userData.getCompany();
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

    public List<IgmaRole> getAllRolesByCompanyId(Integer companyId) {
        setRoles(new ArrayList<IgmaRole>());
        return rolesService.getAllRolesListByCompanyId(companyId);
    }

    public List<IgmaRole> getAllRoles() {
        setRoles(new ArrayList<IgmaRole>());
        return rolesService.getAllRoles();
    }

    @Override
    public String getScreenName() {
        return "role";
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

    public IgmaUser getIsagUser() {
        return isagUser;
    }

    public void setIsagUser(IgmaUser isagUser) {
        this.isagUser = isagUser;
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

    public IgmaRole getRole() {
        return role;
    }

    public void setRole(IgmaRole role) {
        this.role = role;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
