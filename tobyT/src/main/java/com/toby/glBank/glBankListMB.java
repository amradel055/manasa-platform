/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.glBank;

import com.toby.businessservice.GlBankService;
import com.toby.entity.GlBank;
import com.toby.entiy.GlBankEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
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
@Named(value = "glBankListMB")
@ViewScoped
public class glBankListMB extends BaseListBean {

    private UserData userData;

    private GlBankEntity glBankEntity;
    private GlBankEntity glBankEntitySelected;
    private List<GlBankEntity> glBankEntityList;

    private GlBank glBank;
    private List<GlBank> glBankList;
    private GlBankEntity glBankSelected;

    private Integer index = 0;

    @EJB
    private GlBankService glBankService;

    @Override
    @PostConstruct
    public void init() {
        try {
        glBankEntityList = new ArrayList<>();
        glBankList = new ArrayList<>();
        glBank = new GlBank();
        load();
        } catch (Exception e) {
            saveError(e, "glBankListMB", "init");    
         }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        glBankList = glBankService.getAllGlBankByBranchId(userData.getUserBranch().getId());
        for (GlBank gbank : glBankList) {
            glBankEntity = new GlBankEntity();

            glBankEntity.setId(gbank.getId());
            glBankEntity.setBranchId(gbank.getBranchId() != null ? gbank.getBranchId() : null);
            glBankEntity.setAccountId(gbank.getAccountId() != null ? gbank.getAccountId() : null);
            glBankEntity.setCurrencyId(gbank.getCurrencyId() != null ? gbank.getCurrencyId() : null);
            glBankEntity.setCode(gbank.getCode() != null ? gbank.getCode() : null);
            glBankEntity.setName(gbank.getName() != null ? gbank.getName() : null);

            if (gbank.getType() != null) {

                if (gbank.getType() == 0) {
                    glBankEntity.setTypeName("خزنة");
                } else if (gbank.getType() == 1) {
                    glBankEntity.setTypeName("بنك");
                } else {
                    glBankEntity.setTypeName("خزينة شيكات");
                }

                glBankEntity.setType(gbank.getType());
            }

            glBankEntityList.add(glBankEntity);
        }
         } catch (Exception e) {
            saveError(e, "glBankListMB", "load");    
         }
    }

    @Override
    public void delete() {
        try {
        Map<String, String> userDDs = userData.getUserDDs();

        Integer glBankId = glBankEntitySelected.getId() != null ? glBankEntitySelected.getId() : null;
        glBank = new GlBank();
        glBank.setId(glBankId);

        try {
            glBankService.deleteGlBank(glBank);
            glBankEntityList.remove(glBankEntitySelected);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يوجد معاملات علي هذه الخزنة"));
        }
         } catch (Exception e) {
            saveError(e, "glBankListMB", "delete");    
         }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../glBank/glBankForm.xhtml");
        return "";
         } catch (Exception e) {
            saveError(e, "glBankListMB", "goToAdd");    
                 return null;
         }
    }

    @Override
    public String goToEdit() {
        try {
        if (glBankSelected != null && glBankSelected.getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("glBankSelected", glBankSelected.getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../glBank/glBankForm.xhtml");
            return "";
        } else {
            return "";
        }
        } catch (Exception e) {
            saveError(e, "glBankListMB", "goToEdit");    
                 return null;
         }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public GlBankEntity getGlBankEntity() {
        return glBankEntity;
    }

    public void setGlBankEntity(GlBankEntity glBankEntity) {
        this.glBankEntity = glBankEntity;
    }

    public GlBankEntity getGlBankEntitySelected() {
        return glBankEntitySelected;
    }

    public void setGlBankEntitySelected(GlBankEntity glBankEntitySelected) {
        this.glBankEntitySelected = glBankEntitySelected;
    }

    public List<GlBankEntity> getGlBankEntityList() {
        return glBankEntityList;
    }

    public void setGlBankEntityList(List<GlBankEntity> glBankEntityList) {
        this.glBankEntityList = glBankEntityList;
    }

    public GlBank getGlBank() {
        return glBank;
    }

    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    public GlBankEntity getGlBankSelected() {
        return glBankSelected;
    }

    public void setGlBankSelected(GlBankEntity glBankSelected) {
        this.glBankSelected = glBankSelected;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
