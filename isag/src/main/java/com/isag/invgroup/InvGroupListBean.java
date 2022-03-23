/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invgroup;

import com.isag.businessservice.InvGroupService;
import com.isag.entity.InvGroup;
import com.isag.entiy.InvGroupEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
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

    private List<InvGroup> invGroups;
    private InvGroup invGroup;
    private Integer invGroupSelected;
    private List<InvGroupEntity> invGroupEntityList;
    private InvGroupEntity invGroupEntity;

    @EJB
    private InvGroupService invGroupService;
    private UserData userData;

    public List<InvGroup> getInvGroups() {
        return invGroups;
    }

    public void setInvGroups(List<InvGroup> invGroups) {
        this.invGroups = invGroups;
    }

    public InvGroup getInvGroup() {
        return invGroup;
    }

    public void setInvGroup(InvGroup invGroup) {
        this.invGroup = invGroup;
    }

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
        load();
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (invGroupSelected != null && invGroupSelected > 0) {
                try {
                    invGroup = new InvGroup();
                    invGroup.setId(invGroupSelected);
                    invGroupService.deleteInvGroup(invGroup);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
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
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            invGroups = invGroupService.getallInvGroupByBranchId(userData.getUserBranch().getId());
            invGroupEntityList = new ArrayList<>();
            for (InvGroup invGroup : invGroups) {
                invGroupEntity = new InvGroupEntity();
                invGroupEntity.setId(invGroup.getId());
                invGroupEntity.setName(invGroup.getName());
                invGroupEntity.setInvGroupParentId(invGroup.getParent());
                invGroupEntity.setParentName(invGroup.getParent() == null ? null : invGroup.getParent().getName());
                invGroupEntity.setLevel(invGroup.getLevel());
                invGroupEntity.setCode(invGroup.getCode());
                invGroupEntityList.add(invGroupEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvGroupListBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
