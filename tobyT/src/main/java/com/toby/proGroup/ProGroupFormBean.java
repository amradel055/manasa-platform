/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proGroup;

import com.toby.businessservice.ProGroupService;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.dto.ProGroupDTO;
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

@Named(value = "proGroupFormBean")
@ViewScoped
public class ProGroupFormBean extends BaseFormBean implements Serializable {

    private List<ProGroupDTO> proGroupDTOList;
    private ProGroupDTO proGroupDTO;
    private ProGroupDTOConverter proGroupDTOConverter;
    private boolean showMessage = Boolean.FALSE;

    @EJB
    private ProGroupService proGroupService;

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
                Integer proGroupSelected = ((Integer) getContext().getSessionMap().get("proGroupSelected"));
                setEditedProGroupValues(proGroupSelected);
                getProGroupDTOList();
            }

        } catch (Exception e) {
            saveError(e, "ProGroupFormBean", "init");
        }

    }

    public void setEditedProGroupValues(Integer id) {
        try {
            proGroupDTO = proGroupService.findProGroupDTO(id, getUserData().getUser());
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "setEditedInventoryDelegatedValues");
        }
    }

    @Override
    public String save() {

        try {
            if (getProGroupDTO() != null && !getProGroupDTO().getName().isEmpty() && getProGroupDTO().getCode() != null) {
                setProGroupDTO(proGroupService.saveProGroupDTO(getProGroupDTO(), getUserData().getUser()));
                if (getProGroupDTO() != null && getProGroupDTO().getId() != null && getProGroupDTO().getMsg() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", null));
                    exit();
                } else {
                    errorMessage(getProGroupDTO().getMsg());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "الاسم او الكود فارغ", null));
            }
            showMessage = true;
            return "";
        } catch (Exception e) {
            saveError(e, "ProGroupFormBean", "save");
            return null;
        }
    }

    public void reset() {
        try {
            proGroupDTO = new ProGroupDTO();

        } catch (Exception e) {
            saveError(e, "ProGroupFormBean", "reset");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../progroup/progroupList.xhtml");
        return "";
    }

    public List<ProGroupDTO> completeProGroups(String query) {
        getProGroupDTOList();
        return AutoComplete.completeProGroups(query, getProGroupDTOList(), getProGroupDTOConverter());

    }

    public boolean validateCode() {
        if (proGroupDTO != null) {
            Boolean x = proGroupService.validateCode(getUserData().getUser(), proGroupDTO.getId(), proGroupDTO.getCode());
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
        if (getProGroupDTO() != null) {
            Boolean x = proGroupService.validateName(getUserData().getUser(), getProGroupDTO().getId(), getProGroupDTO().getName());
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
     * @return the proGroupList
     */
    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public ProGroupFormBean() {

    }

    public ProGroupDTOConverter getProGroupDTOConverter() {
        return proGroupDTOConverter;
    }

    /**
     * @param proGroupDTOConverter the proGroupDTOConverter to set
     */
    public void setProGroupDTOConverter(ProGroupDTOConverter proGroupDTOConverter) {
        this.proGroupDTOConverter = proGroupDTOConverter;
    }

    /**
     * @return the proGroupDTOList
     */
    public List<ProGroupDTO> getProGroupDTOList() {
        if (proGroupDTOList == null || proGroupDTOList.isEmpty()) {
            proGroupDTOList = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUser());
            setProGroupDTOConverter(new ProGroupDTOConverter(proGroupDTOList));
        }

        return proGroupDTOList;
    }

    /**
     * @param proGroupDTOList the proGroupDTOList to set
     */
    public void setProGroupDTOList(List<ProGroupDTO> proGroupDTOList) {
        this.proGroupDTOList = proGroupDTOList;
    }

    /**
     * @return the proGroupDTO
     */
    public ProGroupDTO getProGroupDTO() {
        if (proGroupDTO == null) {
            proGroupDTO = new ProGroupDTO();
        }
        return proGroupDTO;
    }

    /**
     * @param proGroupDTO the proGroupDTO to set
     */
    public void setProGroupDTO(ProGroupDTO proGroupDTO) {
        this.proGroupDTO = proGroupDTO;
    }
}
