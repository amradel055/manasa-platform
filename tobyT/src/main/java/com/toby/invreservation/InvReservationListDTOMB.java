package com.toby.invreservation;

import com.toby.businessservice.InvReservationService;
import com.toby.entity.InvReservation;
import com.toby.entiy.InvReservationEntity;
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
@Named(value = "invReservationListDTOMB")
@ViewScoped
public class InvReservationListDTOMB extends BaseListBean {

    private UserData userData;
    private List<InvReservation> invReservationList = new ArrayList<>();
    private InvReservation invReservation;
    private Integer invReservationSelected;
    private InvReservationEntity invReservationEntity;
    private List<InvReservationEntity> invReservationEntityList;

    @EJB
    private InvReservationService invReservationService;

    @Override
    @PostConstruct
    public void init() {
        invReservation = new InvReservation();
        invReservationEntity = new InvReservationEntity();
        invReservationList = new ArrayList<>();
        invReservationEntityList = new ArrayList<>();
        this.load();

    }

    @Override
    public void delete() {
        Map<String, String> userDDs = userData.getUserDDs();
        if (invReservation != null) {
            Integer invReservationIdToPass = invReservationEntity.getId();
            invReservation.setId(invReservationEntity.getId());
            try {
                invReservationService.deleteInvReservation(invReservation, invReservationIdToPass);
                getInvReservationEntityList().remove(invReservationEntity);
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
        context.getSessionMap().put("ScreenMode", "AddInvReservation");
        exit("../invreservation_1/invReservationForm.xhtml");
        return "";
    }

    @Override
    public String goToEdit() {
        if (invReservationSelected != null && invReservationSelected > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invReservationSelected", invReservationSelected);
            context.getSessionMap().put("ScreenMode", "EditInvReservation");
            exit("../invreservation_1/invReservationForm.xhtml");
            return "";
        } else {
            return "";
        }
    }

    @Override
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        invReservationList = invReservationService.getALLInvReservationByBranchId(userData.getUserBranch().getId());
        for (InvReservation transferMapper : invReservationList) {
            invReservationEntity = new InvReservationEntity();
            invReservationEntity.setId(transferMapper.getId());
            invReservationEntity.setReservationDate(transferMapper.getReservationDate());
            invReservationEntity.setEndDate(transferMapper.getEndDate());
            invReservationEntity.setSerial(transferMapper.getSerial());
            invReservationEntity.setDelegator(transferMapper.getDelegatorId());
            invReservationEntity.setRemark(transferMapper.getRemarks());
            invReservationEntity.setInventory(transferMapper.getInvId());
            invReservationEntity.setSite(transferMapper.getSiteId());
            invReservationEntity.setMainSite(transferMapper.getSiteIdMain());
            invReservationEntity.setAddress(transferMapper.getAddress());
            invReservationEntity.setCreatedBy(transferMapper.getCreatedBy());

            invReservationEntityList.add(invReservationEntity);
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

    public List<InvReservation> getInvReservationList() {
        return invReservationList;
    }

    public void setInvReservationList(List<InvReservation> invReservationList) {
        this.invReservationList = invReservationList;
    }

    public InvReservation getInvReservation() {
        return invReservation;
    }

    public void setInvReservation(InvReservation invReservation) {
        this.invReservation = invReservation;
    }

    public Integer getInvReservationSelected() {
        return invReservationSelected;
    }

    public void setInvReservationSelected(Integer invReservationSelected) {
        this.invReservationSelected = invReservationSelected;
    }

    public InvReservationEntity getInvReservationEntity() {
        return invReservationEntity;
    }

    public void setInvReservationEntity(InvReservationEntity invReservationEntity) {
        this.invReservationEntity = invReservationEntity;
    }

    public List<InvReservationEntity> getInvReservationEntityList() {
        return invReservationEntityList;
    }

    public void setInvReservationEntityList(List<InvReservationEntity> invReservationEntityList) {
        this.invReservationEntityList = invReservationEntityList;
    }
}
