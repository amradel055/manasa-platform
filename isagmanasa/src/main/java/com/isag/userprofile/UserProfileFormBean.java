/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.userprofile;

import com.isag.entity.IsagUser;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.isag.businessservice.IsagUserService;

public class UserProfileFormBean extends BaseFormBean {

    private String userName;
    private String oldPassword;
    private String userCode;
    private String userLang;
    private String email;
    private String imgPath;
    private String newUserPassword;
    private String newPasswordConfirm;

    private IsagUser isagUser;

    private Boolean showPassInput = Boolean.TRUE;

    @EJB
    IsagUserService isagUserService;

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
            isagUser = getUserData().getUser();
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
            isagUser = isagUserService.validateOldPassword(isagUser, oldPassword);

            if (isagUser.getMsg() != null && !"".equals(isagUser.getMsg())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), isagUser.getMsg()));
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
            String msg = isagUserService.validateNewPassword(oldPassword, newUserPassword);

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

            String msg = isagUserService.validateConfirmationPassword(newPasswordConfirm, newUserPassword);

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
                isagUser.setPassword(newUserPassword);

                isagUserService.updateUser(isagUser);

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
