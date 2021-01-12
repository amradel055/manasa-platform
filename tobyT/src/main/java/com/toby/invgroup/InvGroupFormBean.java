/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invgroup;

import com.toby.businessservice.InvGroupService;
import com.toby.converter.InvGroupDTOConverter;
import com.toby.dto.InvGroupDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "invGroupFormBean")
@ViewScoped
public class InvGroupFormBean extends BaseFormBean implements Serializable {

    private List<InvGroupDTO> invGroupDTOList;
    private InvGroupDTO invGroupDTO;
    private InvGroupDTOConverter invGroupDTOConverter;
    private boolean showMessage = Boolean.FALSE;

    @EJB
    private InvGroupService invGroupService;

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
                Integer invGroupSelected = ((Integer) getContext().getSessionMap().get("invGroupSelected"));
                setEditedInvGroupValues(invGroupSelected);
                getInvGroupDTOList();
            }

        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "init");
        }

    }

    public void setEditedInvGroupValues(Integer id) {
        try {
            invGroupDTO = invGroupService.findInvGroupDTO(id, getUserData().getUser());
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "setEditedInventoryDelegatedValues");
        }
    }

    @Override
    public String save() {

        try {
            if (getInvGroupDTO() != null && !getInvGroupDTO().getName().isEmpty() && getInvGroupDTO().getCode() != null) {
                setInvGroupDTO(invGroupService.saveInvGroupDTO(getInvGroupDTO(), getUserData().getUser()));
                if (getInvGroupDTO() != null && getInvGroupDTO().getId() != null && getInvGroupDTO().getMsg() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", null));
                    exit();
                } else {
                    errorMessage(getInvGroupDTO().getMsg());
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
            invGroupDTO = new InvGroupDTO();

        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "reset");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invgroup/invgroupList.xhtml");
        return "";
    }

    public List<InvGroupDTO> completeInvGroups(String query) {
        getInvGroupDTOList();
        return AutoComplete.completeInvGroups(query, getInvGroupDTOList(), getInvGroupDTOConverter());

    }

    public boolean validateCode() {
        if (invGroupDTO != null) {
            Boolean x = invGroupService.validateCode(getUserData().getUser(), invGroupDTO.getId(), invGroupDTO.getCode());
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
        if (getInvGroupDTO() != null) {
            Boolean x = invGroupService.validateName(getUserData().getUser(), getInvGroupDTO().getId(), getInvGroupDTO().getName());
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

    public InvGroupFormBean() {

    }

    public InvGroupDTOConverter getInvGroupDTOConverter() {
        return invGroupDTOConverter;
    }

    /**
     * @param invGroupDTOConverter the invGroupDTOConverter to set
     */
    public void setInvGroupDTOConverter(InvGroupDTOConverter invGroupDTOConverter) {
        this.invGroupDTOConverter = invGroupDTOConverter;
    }

    /**
     * @return the invGroupDTOList
     */
    public List<InvGroupDTO> getInvGroupDTOList() {
        if (invGroupDTOList == null || invGroupDTOList.isEmpty()) {
            invGroupDTOList = invGroupService.getallInvGroupByBranchIdDTO(getUserData().getUser());
            setInvGroupDTOConverter(new InvGroupDTOConverter(invGroupDTOList));
        }

        return invGroupDTOList;
    }

    /**
     * @param invGroupDTOList the invGroupDTOList to set
     */
    public void setInvGroupDTOList(List<InvGroupDTO> invGroupDTOList) {
        this.invGroupDTOList = invGroupDTOList;
    }

    /**
     * @return the invGroupDTO
     */
    public InvGroupDTO getInvGroupDTO() {
        if (invGroupDTO == null) {
            invGroupDTO = new InvGroupDTO();
        }
        return invGroupDTO;
    }

    /**
     * @param invGroupDTO the invGroupDTO to set
     */
    public void setInvGroupDTO(InvGroupDTO invGroupDTO) {
        this.invGroupDTO = invGroupDTO;
    }
}
