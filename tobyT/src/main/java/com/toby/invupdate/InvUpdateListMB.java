package com.toby.invupdate;

import com.toby.businessservice.InvUpdateService;
import com.toby.entity.InvUpdate;
import com.toby.entiy.InvUpdateEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Named(value = "invUpdateListMB")
@ViewScoped
public class InvUpdateListMB extends BaseListBean {

    private UserData userData;
    private List<InvUpdate> invUpdateList = new ArrayList<>();
    private InvUpdate invUpdate;
    private Integer invUpdateSelected;
    private InvUpdateEntity invUpdateEntity;
    private List<InvUpdateEntity> invUpdateEntityList = new ArrayList<>();

    @EJB
    private InvUpdateService invUpdateService;

    @Override
    @PostConstruct
    public void init() {
        try {

            invUpdate = new InvUpdate();
            invUpdateEntity = new InvUpdateEntity();
            invUpdateList = new ArrayList<>();
            this.load();
        } catch (Exception e) {
            saveError(e, "InvUpdateListMB", "init()");
        }
    }

    @Override
    public void delete() {
        try {

            Map<String, String> userDDs = userData.getUserDDs();
            if (invUpdate != null) {
                Integer invUpdateIdToPass = invUpdateEntity.getId();
                invUpdate.setId(invUpdateEntity.getId());
                try {
                    invUpdateService.deleteInvUpdate(invUpdate, invUpdateIdToPass);
                    getInvUpdateEntityList().remove(invUpdateEntity);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateListMB", "delete()");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invupdate/invUpdateForm.xhtml");
        } catch (Exception e) {
            saveError(e, "InvUpdateListMB", "goToAdd()");
        }
        return "";
    }

    @Override
    public String goToEdit() {
        try {
            if (invUpdateSelected != null && invUpdateSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invUpdateSelected", invUpdateSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invupdate/invUpdateForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateListMB", "goToEdit()");
            return "";
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            invUpdateList = invUpdateService.getALLInvUpdateByBranchId(userData.getUserBranch().getId(), 0);
            for (InvUpdate updateMapper : invUpdateList) {
                invUpdateEntity = new InvUpdateEntity();
                invUpdateEntity.setId(updateMapper.getId());
                invUpdateEntity.setSerial(updateMapper.getSerial());
                invUpdateEntity.setUpdateDate(updateMapper.getDate());
                invUpdateEntity.setRemark(updateMapper.getRemarks());
                invUpdateEntity.setUpdateDocument(updateMapper.getDocument());
                invUpdateEntity.setCreatedBy(updateMapper.getCreatedBy());
                invUpdateEntity.setCreationDate(updateMapper.getCreationDate());

                invUpdateEntityList.add(invUpdateEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateListMB", "load()");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<InvUpdate> getInvUpdateList() {
        return invUpdateList;
    }

    public void setInvUpdateList(List<InvUpdate> invUpdateList) {
        this.invUpdateList = invUpdateList;
    }

    public InvUpdate getInvUpdate() {
        return invUpdate;
    }

    public void setInvUpdate(InvUpdate invUpdate) {
        this.invUpdate = invUpdate;
    }

    public Integer getInvUpdateSelected() {
        return invUpdateSelected;
    }

    public void setInvUpdateSelected(Integer invUpdateSelected) {
        this.invUpdateSelected = invUpdateSelected;
    }

    public InvUpdateEntity getInvUpdateEntity() {
        return invUpdateEntity;
    }

    public void setInvUpdateEntity(InvUpdateEntity invUpdateEntity) {
        this.invUpdateEntity = invUpdateEntity;
    }

    public List<InvUpdateEntity> getInvUpdateEntityList() {
        return invUpdateEntityList;
    }

    public void setInvUpdateEntityList(List<InvUpdateEntity> invUpdateEntityList) {
        this.invUpdateEntityList = invUpdateEntityList;
    }
}
