/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.prodetailtype;

import com.toby.businessservice.ProDetailTypeService;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "proDetailTypeFormBean")
@ViewScoped
public class ProDetailTypeFormBean extends BaseFormBean implements Serializable {

    private ProDetailTypeDTO proDetailTypeDTO;
    private boolean showMessage = Boolean.FALSE;

    @EJB
    private ProDetailTypeService proDetailTypeService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Add")) {
                reset();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
                Integer proDetailTypSelected = ((Integer) getContext().getSessionMap().get("proDetailTypeSelected"));
                setEditedInvGroupValues(proDetailTypSelected);
            }

        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "init");
        }

    }

    public void setEditedInvGroupValues(Integer id) {
        try {
            proDetailTypeDTO = proDetailTypeService.findProDetailTypeDTO(id, getUserData().getUser());
        } catch (Exception e) {
            saveError(e, "ProDetailTypeFormBean", "setEditedInventoryDelegatedValues");
        }
    }

    @Override
    public String save() {

        try {
            if (getProDetailTypeDTO() != null && !getProDetailTypeDTO().getName().isEmpty() && getProDetailTypeDTO().getCode() != null) {
                setProDetailTypeDTO(proDetailTypeService.saveProDetailTypeDTO(getProDetailTypeDTO(), getUserData().getUser()));
                if (getProDetailTypeDTO() != null && getProDetailTypeDTO().getId() != null && getProDetailTypeDTO().getMsg() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", null));
                    exit();
                } else {
                    errorMessage(getProDetailTypeDTO().getMsg());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "الاسم او الكود فارغ", null));
            }
            showMessage = true;
            return "";
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "save");
            return null;
        }
    }

    public void reset() {
        try {
            setProDetailTypeDTO(null);
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "reset");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../proDetailType/proDetailTypeList.xhtml");
        return "";
    }

    public boolean validateCode() {
        if (getProDetailTypeDTO() != null && getProDetailTypeDTO().getCode() != null && getProDetailTypeDTO().getCode() > 0) {
            Boolean x = proDetailTypeService.validateCode(getUserData().getUser(), getProDetailTypeDTO().getId(), getProDetailTypeDTO().getCode());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود", null));
                showMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", null));
            showMessage = true;
            return false;
        }
    }

    public boolean validateName() {
        if (getProDetailTypeDTO() != null) {
            Boolean x = proDetailTypeService.validateName(getUserData().getUser(), getProDetailTypeDTO().getId(), getProDetailTypeDTO().getName());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الاسم موجود", null));
                showMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", null));
            showMessage = true;
            return false;
        }
    }

    /**
     * @return the invGroupList
     */
    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public ProDetailTypeFormBean() {

    }

    /**
     * @return the proDetailTypeDTO
     */
    public ProDetailTypeDTO getProDetailTypeDTO() {
        if(proDetailTypeDTO == null){
            proDetailTypeDTO = new ProDetailTypeDTO();
        }
        return proDetailTypeDTO;
    }

    /**
     * @param proDetailTypeDTO the proDetailTypeDTO to set
     */
    public void setProDetailTypeDTO(ProDetailTypeDTO proDetailTypeDTO) {
        this.proDetailTypeDTO = proDetailTypeDTO;
    }
}
