/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proGroup;

import com.toby.businessservice.ProGroupService;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "proGroupListBean")
@ViewScoped
public class ProGroupListBean extends BaseListBean implements Serializable {

    private List<ProGroupDTO> proGroupDTOList;
    private ProGroupDTO proGroupDTO;
    private Integer proGroupSelected;

    @EJB
    private ProGroupService proGroupService;
    private UserData userData;

    public Integer getProGroupSelected() {
        return proGroupSelected;
    }

    public void setProGroupSelected(Integer proGroupSelected) {
        this.proGroupSelected = proGroupSelected;
    }

    public ProGroupService getProGroupService() {
        return proGroupService;
    }

    public void setProGroupService(ProGroupService proGroupService) {
        this.proGroupService = proGroupService;
    }

    public ProGroupListBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getProGroupDTOList();
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (proGroupDTO != null) {
                try {
                    proGroupService.deleteProGroupDTO(proGroupDTO, getUserData().getUser());
                    proGroupDTOList= proGroupService.getproGroupDTOList(getUserData().getUser());
                    
                   
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحذف بنجاح", null));
                    
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "ProGroupListBean", "delete");
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
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../progroup/progroupForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "ProGroupListBean", "delete");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (proGroupSelected != null && proGroupSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proGroupSelected", proGroupSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../progroup/progroupForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "ProGroupListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the proGroupDTOList
     */
    public List<ProGroupDTO> getProGroupDTOList() {
        if (proGroupDTOList == null || proGroupDTOList.isEmpty()) {
            proGroupDTOList = proGroupService.getproGroupDTOList(getUserData().getUser());

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
