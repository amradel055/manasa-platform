/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.roles;

import com.isag.businessservice.CompanyService;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagRole;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
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
import com.isag.businessservice.RoleService;
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
    private IsagRole isagRole;
    private UserData userData;
    private IsagCompany company;
    private List<IsagCompany> userCompany;
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
            isagRole = new IsagRole();
            load();
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "init");
        }
    }

    @Override
    public String save() {
        try {

            if (roleName != null && roleName.trim().length() > 0) {
                isagRole.setName(roleName);

                IsagCompany companySelected = new IsagCompany();
                companySelected.setId(companyId);
                isagRole.setCompanyId(companySelected);
                isagRole.setModifiedBy(userData.getUser());
                isagRole.setModificationDate(new Date());
                if (screenMode.equalsIgnoreCase("add")) {
                    try {
                        isagRole = rolesService.addRole(isagRole);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        e.printStackTrace();
                        return null;
                    }

                }
                if (screenMode.equalsIgnoreCase("edit")) {
                    isagRole.setId(roleId);
                    try {
                        isagRole = rolesService.updateRole(isagRole);
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
                        isagRole = rolesService.findRole(roleId);
                        companyId = isagRole.getCompanyId().getId();
                        setRoleName(isagRole.getName());
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

    public IsagRole getIsagRole() {
        return isagRole;
    }

    public void setIsagRole(IsagRole isagRole) {
        this.isagRole = isagRole;
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

    public List<IsagCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<IsagCompany> userCompany) {
        this.userCompany = userCompany;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public IsagCompany getCompany() {
        return company;
    }

    public void setCompany(IsagCompany company) {
        this.company = company;
    }

    public boolean isSetRendered() {
        return setRendered;
    }

    public void setSetRendered(boolean setRendered) {
        this.setRendered = setRendered;
    }
}
