/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invgroup;

import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.InvItemService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvGroupConverter;
import com.isag.entity.InvGroup;
import com.isag.entity.IsagCompany;
import com.isag.entiy.InvGroupEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "invGroupFormBean")
@ViewScoped
public class InvGroupFormBean extends BaseFormBean implements Serializable {

    private String invGroupName;
    private int parent;
    private List<InvGroupEntity> invGroupEntityList;
    private List<InvGroup> invGroupList;
    private List<IsagCompany> userCompany;
    private InvGroupEntity invGroupEntity;
    private Integer invGroupSelected;
    private InvGroup invGroup;
    private InvGroupConverter invGroupConverter;

    private boolean showMessage = Boolean.FALSE;

    @EJB
    private InvGroupService invGroupService;
    @EJB
    private InvItemService invItemService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            invGroupEntityList = new ArrayList<>();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Add")) {
                invGroupList = invGroupService.getallInvGroupByBranchId(getUserData().getUserBranch().getId());
                setInvGroupEntity(new InvGroupEntity());
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
                invGroupSelected = ((Integer) context.getSessionMap().get("invGroupSelected"));
                invGroup = invGroupService.findInvGroup(invGroupSelected);
                invGroupList = invGroupService.getInvGroupByBranchId(getUserData().getUserBranch().getId(), invGroup.getId());
                invGroupEntity = new InvGroupEntity();
                invGroupEntity = mapinvGroupToinvGroupEntity(invGroup, invGroupEntity);
            }

            invGroupConverter = new InvGroupConverter(invGroupList);

            for (InvGroup group : invGroupList) {
                InvGroupEntity groupEntity = new InvGroupEntity();
                mapinvGroupToinvGroupEntity(group, groupEntity);
                invGroupEntityList.add(groupEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "load");
        }
    }

    private InvGroupEntity mapinvGroupToinvGroupEntity(InvGroup invGroup, InvGroupEntity invGroupEntity) {
        try {
            invGroupEntity.setName(invGroup.getName());
            invGroupEntity.setInvGroupParentId(invGroup.getParent());
            invGroupEntity.setParentName(invGroup.getParent() == null ? null : invGroup.getParent().getName());
            invGroupEntity.setLevel(invGroup.getLevel());
            invGroupEntity.setCode(invGroup.getCode());
            if (invGroup.getId() != null) {
                invGroupEntity.setId(invGroup.getId());
                invGroupEntity.setCreatedBy(invGroup.getCreatedBy());
                invGroupEntity.setCreationDate(invGroup.getCreationDate());
            }

            return invGroupEntity;
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "mapinvGroupToinvGroupEntity");
            return null;
        }
    }

    @Override
    public String save() {
        try {

            invGroup = new InvGroup();
            invGroup.setName(invGroupEntity.getName());
            invGroup.setCode(invGroupEntity.getCode());
            InvGroup group = new InvGroup();
            if (invGroupEntity.getInvGroupParentId() != null) {
                group = invGroupService.findInvGroup(invGroupEntity.getInvGroupParentId().getId());

                //TO DO check if parent group has items or not
                invGroup.setParent(group);
                if (group.getLevel() != null) {
                    invGroup.setLevel(1 + group.getLevel());
                } else {
                    invGroup.setLevel(1);
                }
            }
            if (validateSave(group)) {
                if (invGroupEntity.getId() != null && invGroupEntity.getId() > 0) {
                    invGroup.setId(invGroupEntity.getId());
                    invGroup.setModifiedBy(getUserData().getUser());
                    invGroup.setModificationDate(new Date());
                    invGroup.setCreatedBy(invGroupEntity.getCreatedBy());
                    invGroup.setCreationDate(invGroupEntity.getCreationDate());
                } else {
                    invGroup.setCreatedBy(getUserData().getUser());
                    invGroup.setCreationDate(new Date());
                }

                invGroup.setBranchId(getUserData().getUserBranch());
                invGroup.setCompanyId(getUserData().getCompany());

                invGroup = invGroupService.updateInvGroup(invGroup);

                invGroupEntity = new InvGroupEntity();
                invGroupEntity = mapinvGroupToinvGroupEntity(invGroup, invGroupEntity);

                savedMeesage(getUserData().getUserDDs().get("SAVED"));
                return "";
            } else {
                showMessage = true;
                //  errorMessage("لا يمكن انشاء مجموعة فرعية لمجموعة بها اصناف");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يمكن انشاء مجموعة فرعية لمجموعة بها اصناف"));
            }
            return "";
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "save");
            return null;
        }
    }

    public boolean validateSave(InvGroup group) {

        if (invItemService.checkIfGroupHasItems(group)) {
            return true;
        } else {
            return false;
        }

    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
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

    public List<InvGroup> completeInvGroups(String query) {
        try{
        List<InvGroup> groups = invGroupList;
        if (query == null || query.trim().equals("")) {
            invGroupConverter = new InvGroupConverter(groups);
            return groups;
        }
        List<InvGroup> invGroupsFiltered = new ArrayList<>();

        String nameAr;
        Integer code;
        InvGroup group;
        for (int i = 0; i < invGroupList.size(); i++) {
            group = groups.get(i);
            nameAr = group.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!invGroupsFiltered.contains(group)) {
                    invGroupsFiltered.add(group);
                }
            }

            code = group.getId();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!invGroupsFiltered.contains(group)) {
                    invGroupsFiltered.add(group);
                }
            }
        }

        invGroupConverter = new InvGroupConverter(invGroupsFiltered);
        return invGroupsFiltered;
        } catch (Exception e) {
            saveError(e, "InvGroupFormBean", "completeInvGroups");
            return null;
        }
    }

    /**
     * @return the invGroupSelected
     */
    public Integer getInvGroupSelected() {
        return invGroupSelected;
    }

    /**
     * @param invGroupSelected the invGroupSelected to set
     */
    public void setInvGroupSelected(Integer invGroupSelected) {
        this.invGroupSelected = invGroupSelected;
    }

    /**
     * @return the invGroupList
     */
    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    /**
     * @param invGroupList the invGroupList to set
     */
    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    public String getInvGroupName() {
        return invGroupName;
    }

    public void setInvGroupName(String invGroupName) {
        this.invGroupName = invGroupName;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public List<InvGroupEntity> getInvGroupEntityList() {
        return invGroupEntityList;
    }

    public void setInvGroupEntityList(List<InvGroupEntity> invGroupEntityList) {
        this.invGroupEntityList = invGroupEntityList;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    /**
     * @return the invGroup
     */
    public InvGroup getInvGroup() {
        return invGroup;
    }

    /**
     * @param invGroup the invGroup to set
     */
    public void setInvGroup(InvGroup invGroup) {
        this.invGroup = invGroup;
    }

    public List<IsagCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<IsagCompany> userCompany) {
        this.userCompany = userCompany;
    }

    public InvGroupEntity getInvGroupEntity() {
        return invGroupEntity;
    }

    public void setInvGroupEntity(InvGroupEntity invGroupEntity) {
        this.invGroupEntity = invGroupEntity;
    }

    public InvGroupFormBean() {

    }

    public InvGroupConverter getInvGroupConverter() {
        return invGroupConverter;
    }

    public void setInvGroupConverter(InvGroupConverter invGroupConverter) {
        this.invGroupConverter = invGroupConverter;
    }
}
