/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invQuotation;

import com.isag.businessservice.InvQuotationService;
import com.isag.entity.InvQotation;
import com.isag.entiy.InvQuotationEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
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

/**
 *
 * @author hq002
 */
@Named(value = "invQuotationListMB")
@ViewScoped
public class invQuotationListMB extends BaseListBean {

    private UserData userData;
    private List<InvQotation> invQotations = new ArrayList<>();
    private InvQotation invQotation;
    private Integer invQuotationSelected;
    private InvQuotationEntity invQuotationEntity;
    private List<InvQuotationEntity> invQuotationEntities;
    private Integer index = 0;

    @EJB
    private InvQuotationService invQuotationService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invQotation = new InvQotation();
            invQuotationEntity = new InvQuotationEntity();
            invQuotationEntities = new ArrayList<>();
            load();
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            invQotations = invQuotationService.getALLInvQotationByBranchId(userData.getUserBranch().getId());
            for (InvQotation quotationMapper : invQotations) {
                invQuotationEntity = new InvQuotationEntity();
                invQuotationEntity.setSerial(quotationMapper.getSerial());
                invQuotationEntity.setId(quotationMapper.getId());
                invQuotationEntity.setDate(quotationMapper.getDate());
                invQuotationEntity.setEndDate(quotationMapper.getEndDate());

                invQuotationEntity.setRemark(quotationMapper.getRemark());
                invQuotationEntity.setOrganizationSite(quotationMapper.getCustomerId() != null ? quotationMapper.getCustomerId() : null);
                invQuotationEntity.setInvDelegator(quotationMapper.getDelegatorId() != null ? quotationMapper.getDelegatorId() : null);

                invQuotationEntities.add(invQuotationEntity);
                invQotation = quotationMapper;
            }
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (invQotation != null) {
                Integer InvPurchaseOrderIdToPass = invQuotationEntity.getId();
                invQotation.setId(invQuotationEntity.getId());
                try {
                    invQuotationService.deleteInvQotation(invQotation, InvPurchaseOrderIdToPass);
                    getInvQuotationEntities().remove(invQuotationEntity);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "delete");
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
            exit("../invQuotation/invQuotationForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invQuotationSelected != null && invQuotationSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invQuotationSelected", invQuotationSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invQuotation/invQuotationForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the invQotations
     */
    public List<InvQotation> getInvQotations() {
        return invQotations;
    }

    /**
     * @param invQotations the invQotations to set
     */
    public void setInvQotations(List<InvQotation> invQotations) {
        this.invQotations = invQotations;
    }

    /**
     * @return the invQotation
     */
    public InvQotation getInvQotation() {
        return invQotation;
    }

    /**
     * @param invQotation the invQotation to set
     */
    public void setInvQotation(InvQotation invQotation) {
        this.invQotation = invQotation;
    }

    /**
     * @return the invQuotationSelected
     */
    public Integer getInvQuotationSelected() {
        return invQuotationSelected;
    }

    /**
     * @param invQuotationSelected the invQuotationSelected to set
     */
    public void setInvQuotationSelected(Integer invQuotationSelected) {
        this.invQuotationSelected = invQuotationSelected;
    }

    /**
     * @return the invQuotationEntity
     */
    public InvQuotationEntity getInvQuotationEntity() {
        return invQuotationEntity;
    }

    /**
     * @param invQuotationEntity the invQuotationEntity to set
     */
    public void setInvQuotationEntity(InvQuotationEntity invQuotationEntity) {
        this.invQuotationEntity = invQuotationEntity;
    }

    /**
     * @return the invQuotationEntities
     */
    public List<InvQuotationEntity> getInvQuotationEntities() {
        return invQuotationEntities;
    }

    /**
     * @param invQuotationEntities the invQuotationEntities to set
     */
    public void setInvQuotationEntities(List<InvQuotationEntity> invQuotationEntities) {
        this.invQuotationEntities = invQuotationEntities;
    }

    /**
     * @return the invQuotationService
     */
    public InvQuotationService getInvQuotationService() {
        return invQuotationService;
    }

    /**
     * @param invQuotationService the invQuotationService to set
     */
    public void setInvQuotationService(InvQuotationService invQuotationService) {
        this.invQuotationService = invQuotationService;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

}
