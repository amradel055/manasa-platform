/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.user;

import com.toby.businessservice.TobyUserService;
import com.toby.businessservice.TobyUserRoleService;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 * @author hq004
 */
public class UserListBean extends BaseListBean implements Serializable {

    @EJB
    private TobyUserService userService;
    @EJB
    private TobyUserRoleService tobyUserRoleService;

    private List<TobyUser> users;

    private TobyUser tobyUser;
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
                tobyUser = new TobyUser();
                getTobyUser().setId(selecteduser);
                setTobyUser(getTobyUser());
                try {
                    userService.deleteUser(getTobyUser());
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
            TobyCompany companyId = userData.getCompany();
            if (companyId != null && companyId.getId() != null) {
                //  get all user to all roles to this branch
                if (userData.getSelectedBranch() != null) {
                    users = tobyUserRoleService.getUsersForBranch(userData.getSelectedBranch());
                }

            } else {
                users = userService.getAllUsers();
            }
        } catch (Exception e) {
            saveError(e, "UserListBean", "load");
        }

    }

    public List<TobyUser> getAllUsersByCompanyId(Integer companyId) {
        try {
            setUsers(new ArrayList<TobyUser>());
            return userService.getAllUsersListByCompanyId(companyId);
        } catch (Exception e) {
            saveError(e, "UserListBean", "getAllUsersByCompanyId");
            return null;
        }

    }

    public List<TobyUser> getAllUsers() {
        try {
            setUsers(new ArrayList<TobyUser>());
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

    public TobyUserService getUserService() {
        return userService;
    }

    public void setUserService(TobyUserService userService) {
        this.userService = userService;
    }

    public List<TobyUser> getUsers() {
        return users;
    }

    public void setUsers(List<TobyUser> users) {
        this.users = users;
    }

    public TobyUser getTobyUser() {
        return tobyUser;
    }

    public void setTobyUser(TobyUser tobyUser) {
        this.tobyUser = tobyUser;
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
