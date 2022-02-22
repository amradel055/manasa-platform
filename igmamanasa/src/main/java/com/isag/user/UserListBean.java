/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.user;

import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;
import com.isag.businessservice.IgmaUserRoleService;
import com.isag.businessservice.IgmaUserService;

/**
 *
 * @author hq004
 */
public class UserListBean extends BaseListBean implements Serializable {

    @EJB
    private IgmaUserService userService;
    @EJB
    private IgmaUserRoleService isagUserRoleService;

    private List<IgmaUser> users;

    private IgmaUser isagUser;
    private Integer selecteduser;
    private UserData userData;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "UserListBean", "init");
        }

    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (selecteduser != null) {
                isagUser = new IgmaUser();
                getIsagUser().setId(selecteduser);
                setIsagUser(getIsagUser());
                try {
                    userService.deleteUser(getIsagUser());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }

            }
        } catch (Exception e) {
            saveError(e, "UserListBean", "delete");
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
            context.getSessionMap().put("userMode", "add");
            exit("../user/userForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "UserListBean", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (selecteduser != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("selecteduser", selecteduser);
                context.getSessionMap().put("userMode", "edit");
                exit("../user/userForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "UserListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            users = new ArrayList<>();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            IgmaCompany companyId = userData.getCompany();
            if (companyId != null && companyId.getId() != null) {
                //  get all user to all roles to this branch
                if (userData.getSelectedBranch() != null) {
                    users = isagUserRoleService.getUsersForCompany(userData.getCompany().getId());
                }

            } else {
                users = userService.getAllUsers();
            }
        } catch (Exception e) {
            saveError(e, "UserListBean", "load");
        }

    }

    public List<IgmaUser> getAllUsersByCompanyId(Integer companyId) {
        try {
            setUsers(new ArrayList<IgmaUser>());
            return userService.getAllUsersListByCompanyId(companyId);
        } catch (Exception e) {
            saveError(e, "UserListBean", "getAllUsersByCompanyId");
            return null;
        }

    }

    public List<IgmaUser> getAllUsers() {
        try {
            setUsers(new ArrayList<IgmaUser>());
            return userService.getAllUsers();
        } catch (Exception e) {
            saveError(e, "UserListBean", "getAllUsers");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "user";

    }

    public IgmaUserService getUserService() {
        return userService;
    }

    public void setUserService(IgmaUserService userService) {
        this.userService = userService;
    }

    public List<IgmaUser> getUsers() {
        return users;
    }

    public void setUsers(List<IgmaUser> users) {
        this.users = users;
    }

    public IgmaUser getIsagUser() {
        return isagUser;
    }

    public void setIsagUser(IgmaUser isagUser) {
        this.isagUser = isagUser;
    }

    public Integer getSelecteduser() {
        return selecteduser;
    }

    public void setSelecteduser(Integer selecteduser) {
        this.selecteduser = selecteduser;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
