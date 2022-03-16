package com.toby.invstrip;

import com.toby.businessservice.InvStripService;
import com.toby.entity.InvStrip;
import com.toby.entiy.InvStripEntity;
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
@Named(value = "invStripListMB")
@ViewScoped
public class InvStripListMB extends BaseListBean {

    private UserData userData;
    private List<InvStrip> invStripList = new ArrayList<>();
    private InvStrip invStrip;
    private Integer invStripSelected;
    private InvStripEntity invStripEntity;
    private List<InvStripEntity> invStripEntityList = new ArrayList<>();

    @EJB
    private InvStripService invStripService;

    @Override
    @PostConstruct
    public void init() {
        invStrip = new InvStrip();
        invStripEntity = new InvStripEntity();
        invStripList = new ArrayList<>();
        this.load();

    }

    @Override
    public void delete() {
        Map<String, String> userDDs = userData.getUserDDs();
        if (invStrip != null) {
            Integer invStripIdToPass = invStripEntity.getId();
            invStrip.setId(invStripEntity.getId());
            try {
                invStripService.deleteInvStrip(invStrip, invStripIdToPass);
                getInvStripEntityList().remove(invStripEntity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../invstrip/invStripForm.xhtml");
        return "";
    }

    @Override
    public String goToEdit() {
        if (invStripSelected != null && invStripSelected > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invStripSelected", invStripSelected);
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../invstrip/invStripForm.xhtml");
            return "";
        } else {
            return "";
        }
    }

    @Override
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        invStripList = invStripService.getALLInvStripByBranchId(userData.getUserBranch().getId(), 0);
        for (InvStrip stripMapper : invStripList) {
            invStripEntity = new InvStripEntity();
            invStripEntity.setId(stripMapper.getId());
            invStripEntity.setSerial(stripMapper.getSerial());
            invStripEntity.setStripDate(stripMapper.getStripDate());
            invStripEntity.setRemark(stripMapper.getRemarks());
            invStripEntity.setStripDocument(stripMapper.getStripDocument());

            invStripEntityList.add(invStripEntity);
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

    public List<InvStrip> getInvStripList() {
        return invStripList;
    }

    public void setInvStripList(List<InvStrip> invStripList) {
        this.invStripList = invStripList;
    }

    public InvStrip getInvStrip() {
        return invStrip;
    }

    public void setInvStrip(InvStrip invStrip) {
        this.invStrip = invStrip;
    }

    public Integer getInvStripSelected() {
        return invStripSelected;
    }

    public void setInvStripSelected(Integer invStripSelected) {
        this.invStripSelected = invStripSelected;
    }

    public InvStripEntity getInvStripEntity() {
        return invStripEntity;
    }

    public void setInvStripEntity(InvStripEntity invStripEntity) {
        this.invStripEntity = invStripEntity;
    }

    public List<InvStripEntity> getInvStripEntityList() {
        return invStripEntityList;
    }

    public void setInvStripEntityList(List<InvStripEntity> invStripEntityList) {
        this.invStripEntityList = invStripEntityList;
    }
}
