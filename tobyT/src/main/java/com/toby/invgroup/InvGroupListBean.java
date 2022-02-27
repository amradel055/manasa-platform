/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invgroup;

import com.toby.businessservice.InvGroupService;
import com.toby.dto.InvGroupDTO;
import com.toby.entiy.InvGroupEntity;
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

@Named(value = "invGroupListBean")
@ViewScoped
public class InvGroupListBean extends BaseListBean implements Serializable {

    private List<InvGroupDTO> invGroupDTOList;
    private InvGroupDTO invGroupDTO;
    private Integer invGroupSelected;
    private List<InvGroupEntity> invGroupEntityList;
    private InvGroupEntity invGroupEntity;

    @EJB
    private InvGroupService invGroupService;
    private UserData userData;

    public Integer getInvGroupSelected() {
        return invGroupSelected;
    }

    public void setInvGroupSelected(Integer invGroupSelected) {
        this.invGroupSelected = invGroupSelected;
    }

    public InvGroupService getInvGroupService() {
        return invGroupService;
    }

    public void setInvGroupService(InvGroupService invGroupService) {
        this.invGroupService = invGroupService;
    }

    public List<InvGroupEntity> getInvGroupEntityList() {
        return invGroupEntityList;
    }

    public void setInvGroupEntityList(List<InvGroupEntity> invGroupEntityList) {
        this.invGroupEntityList = invGroupEntityList;
    }

    public InvGroupEntity getInvGroupEntity() {
        return invGroupEntity;
    }

    public void setInvGroupEntity(InvGroupEntity invGroupEntity) {
        this.invGroupEntity = invGroupEntity;
    }

    public InvGroupListBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getInvGroupDTOList();
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (invGroupDTO != null) {
                try {
                    invGroupService.deleteInvGroupDTO(invGroupDTO, getUserData().getUser());
                    invGroupDTOList= invGroupService.getinvGroupDTOList(getUserData().getUser());
                    
                   
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحذف بنجاح", null));
                    
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "InvGroupListBean", "delete");
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
            exit("../invgroup/invgroupForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvGroupListBean", "delete");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invGroupSelected != null && invGroupSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invGroupSelected", invGroupSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invgroup/invgroupForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvGroupListBean", "goToEdit");
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
     * @return the invGroupDTOList
     */
    public List<InvGroupDTO> getInvGroupDTOList() {
        if (invGroupDTOList == null || invGroupDTOList.isEmpty()) {
            invGroupDTOList = invGroupService.getinvGroupDTOList(getUserData().getUser());

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
