/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.userprofile;

import com.toby.businessservice.TobyUserService;
import com.toby.entity.TobyUser;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class UserProfileFormBean extends BaseFormBean {

    private String userName;
    private String oldPassword;
    private String userCode;
    private String userLang;
    private String email;
    private String imgPath;
    private String newUserPassword;
    private String newPasswordConfirm;

    private TobyUser tobyUser;

    private Boolean showPassInput = Boolean.TRUE;

    @EJB
    TobyUserService tobyUserService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();

            fillOutputs();
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            tobyUser = getUserData().getUser();
            setScreenMode((String) context.getSessionMap().get("userMode"));
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "load");
        }
    }

    private void fillOutputs() {
        try {
            userName = getUserData().getUser().getName();
            oldPassword = getUserData().getUser().getPassword();
            userCode = getUserData().getUser().getUserCode();
            userLang = getUserData().getUser().getLang().getName();
            email = "";
            imgPath = "";
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "fillOutputs");
        }
    }

    public void updatePassword() {
        try {
            showPassInput = Boolean.TRUE;
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "updatePassword");
        }
    }

    public boolean validateOldPassword() {
        try {
            tobyUser = tobyUserService.validateOldPassword(tobyUser, oldPassword);

            if (tobyUser.getMsg() != null && !"".equals(tobyUser.getMsg())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), tobyUser.getMsg()));
                return false;
            }

            return true;
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "validateOldPassword");
            return false;
        }
    }

    public boolean validateNewPassword() {
        try {
            String msg = tobyUserService.validateNewPassword(oldPassword, newUserPassword);

            if (!"".equals(msg)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), msg));
                return false;
            }

            return true;
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "validateNewPassword");
            return false;
        }
    }

    public boolean validateNewPasswordConfirm() {
        try {

            String msg = tobyUserService.validateConfirmationPassword(newPasswordConfirm, newUserPassword);

            if (!"".equals(msg)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), msg));
                return false;
            }

            return true;
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "validateNewPasswordConfirm");
            return false;
        }
    }

    @Override
    public String save() {
        try {
            if (showPassInput && validateOldPassword() && validateNewPassword() && validateNewPasswordConfirm()) {
                tobyUser.setPassword(newUserPassword);

                tobyUserService.updateUser(tobyUser);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
            }

            return "";
        } catch (Exception e) {
            saveError(e, "UserProfileFormBean", "save");
            return null;
        }
    }

    public String cancel() {
        return "";
    }

    @Override
    public String getScreenName() {
        return "user";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserLang() {
        return userLang;
    }

    public void setUserLang(String userLang) {
        this.userLang = userLang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public Boolean getShowPassInput() {
        return showPassInput;
    }

    public void setShowPassInput(Boolean showPassInput) {
        this.showPassInput = showPassInput;
    }
}
