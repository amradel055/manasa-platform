/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.roles;

import com.toby.businessservice.CompanyService;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyRole;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import com.toby.businessservice.RoleService;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hq002
 */
@Named(value = "RoleFormBean")
@ViewScoped
public class RoleFormBean extends BaseFormBean implements Serializable {

    private String roleName;
    private Integer companyId;
    private Integer roleId;
    private String screenMode;
    private TobyRole tobyRole;
    private UserData userData;
    private TobyCompany company;
    private List<TobyCompany> userCompany;
    private boolean setRendered = false;
    /**
     * Creates a new instance of NewJSFManagedBean
     */
    @EJB
    private RoleService rolesService;

    @EJB
    private CompanyService companyService;

    public RoleFormBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        try {
            tobyRole = new TobyRole();
            load();
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "init");
        }
    }

    @Override
    public String save() {
        try {

            if (roleName != null && roleName.trim().length() > 0) {
                tobyRole.setName(roleName);

                TobyCompany companySelected = new TobyCompany();
                companySelected.setId(companyId);
                tobyRole.setCompanyId(companySelected);
                tobyRole.setModifiedBy(userData.getUser());
                tobyRole.setModificationDate(new Date());
                if (screenMode.equalsIgnoreCase("add")) {
                    try {
                        tobyRole = rolesService.addRole(tobyRole);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        e.printStackTrace();
                        return null;
                    }

                }
                if (screenMode.equalsIgnoreCase("edit")) {
                    tobyRole.setId(roleId);
                    try {
                        tobyRole = rolesService.updateRole(tobyRole);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        e.printStackTrace();
                        return null;
                    }

                }
                exit("../role/roleList.xhtml");
                return "";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "save");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            screenMode = (String) context.getSessionMap().get("roleMode");

            if (screenMode != null && (screenMode.length()) > 0) {

                if (screenMode.equalsIgnoreCase("add")) {
                    setRendered = false;
                    userCompany = new ArrayList<>();
                    roleName = "";
                    if (userData.getCompany() != null) {
                        userCompany.add(userData.getCompany());
                        companyId = userData.getCompany().getId();
                    } else {
                        userCompany = companyService.getAllCompanies();
                    }
                } else if (screenMode.equalsIgnoreCase("edit")) {
                    userCompany = new ArrayList<>();
                    if (userData.getCompany() != null) {
                        userCompany.add(userData.getCompany());
                    } else {
                        userCompany = companyService.getAllCompanies();

                    }
                    try {
                        roleId = (Integer) (context.getSessionMap().get("selectedRole"));
                        tobyRole = rolesService.findRole(roleId);
                        companyId = tobyRole.getCompanyId().getId();
                        setRoleName(tobyRole.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "load");
        }
    }

    public void onCompanyListChange() {
        try {
            setRendered = false;
            RequestContext.getCurrentInstance().update("ScreenAddEditForm:com");
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "onCompanyListChange");
        }
    }

    public String cancel() {
        try {
            exit("../role/roleList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "role";

    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public TobyRole getTobyRole() {
        return tobyRole;
    }

    public void setTobyRole(TobyRole tobyRole) {
        this.tobyRole = tobyRole;
    }

    public RoleService getRolesService() {
        return rolesService;
    }

    public void setRolesService(RoleService rolesService) {
        this.rolesService = rolesService;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<TobyCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<TobyCompany> userCompany) {
        this.userCompany = userCompany;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public TobyCompany getCompany() {
        return company;
    }

    public void setCompany(TobyCompany company) {
        this.company = company;
    }

    public boolean isSetRendered() {
        return setRendered;
    }

    public void setSetRendered(boolean setRendered) {
        this.setRendered = setRendered;
    }
}
