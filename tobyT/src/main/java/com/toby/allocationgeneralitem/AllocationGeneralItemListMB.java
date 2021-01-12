/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.allocationgeneralitem;

import com.toby.businessservice.GeneralBudgetGuideService;
import com.toby.businessservice.GlAccountService;
import com.toby.converter.GeneralBudgetGuideConvertor;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.GeneralBudgetGuide;
import com.toby.entity.GlAccount;
import com.toby.entiy.GlAccountEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "AllocationGeneralItemListMB")
@ViewScoped
public class AllocationGeneralItemListMB extends BaseListBean implements Serializable {

    private GlAccountEntity glAccountEntity;
    private GlAccountEntity glAccountEntitySelected;
    private GeneralBudgetGuideConvertor generalBudgetGuideConvertor;
    private GlaccountConverter glAccountConverter;
    private List<GlAccountEntity> glAccountEntityList;
    private Integer level;
    private Integer entityLevel;

    private Map<Integer, GlAccount> glAccounts;

    private Integer branchId;
    private UserData userData;

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;

    private List<GlAccountEntity> rowsDeleted;

    private List<GlAccount> glAccountList;
    private List<GeneralBudgetGuide> generalBudgetList;

    @EJB
    private GlAccountService glAccountService;

    @EJB
    private GeneralBudgetGuideService generalBudgetGuideService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            branchId = getUserData().getUserBranch().getId();
            load();
            fillLists();

            entityLevel = glAccountService.findLevelbyBranchId(branchId);

            if (entityLevel != null) {
                level = entityLevel;
            } else {
                level = entityLevel = 1;
            }
            loadGlAccounts();
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            glAccounts = new HashMap<>();
            glAccountEntity = new GlAccountEntity();
            glAccountEntityList = new ArrayList<>();
            glAccountList = new ArrayList<>();
            rowsDeleted = new ArrayList<>();
            generalBudgetList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "load");
        }
    }

    private void fillLists() {
        try {
            glAccountList = glAccountService.findAllGlAccountsExceptMainAndSubMainGlAccountsByBranch(branchId);
            generalBudgetList = generalBudgetGuideService.getAllGeneralBudgetGuideByBranchId(branchId);
            generalBudgetGuideConvertor = new GeneralBudgetGuideConvertor(generalBudgetList);
            glAccountConverter = new GlaccountConverter(glAccountList);
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "fillLists");
        }

    }

    public void loadGlAccounts() {
        try {
            System.out.println("level: " + level);
            System.out.println("glAccountListSize: " + glAccountList.size());
            GlAccountEntity glAccountLoadedEntity;

            glAccountEntityList = new ArrayList<>();
            for (GlAccount glAccount : glAccountList) {

                glAccountLoadedEntity = new GlAccountEntity();
                glAccountLoadedEntity.setId(glAccount.getId());
                glAccountLoadedEntity.setGlAccount(glAccount);
                glAccountLoadedEntity.setGeneralBudgetId(glAccount.getGeneralBudgetId() != null ? glAccount.getGeneralBudgetId().getId() : null);
                glAccountLoadedEntity.setGeneralBudgetDebitId(glAccount.getGeneralBudgetIdDebit() != null ? glAccount.getGeneralBudgetIdDebit().getId() : null);
                glAccountLoadedEntity.setGeneralBudgetCreditId(glAccount.getGeneralBudgetIdCredit() != null ? glAccount.getGeneralBudgetIdCredit().getId() : null);

                glAccountEntityList.add(glAccountLoadedEntity);

                glAccounts.put(glAccount.getId(), glAccount);
            }

            if (glAccountList == null || glAccountList.size() == 0) {
                glAccountEntityList = new ArrayList<>();
            }
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "loadGlAccounts");
        }
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);

            if (glAccountEntitySelected.getId() != null) {
                getRowsDeleted().add(glAccountEntitySelected);
            }

            glAccountEntityList.remove(glAccountEntitySelected);
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "delete");
        }
    }

    public void save() {
        try {

            try {

                GlAccount updatedEntity;
                glAccountList = new ArrayList<>();

                for (GlAccountEntity entity : glAccountEntityList) {
                    updatedEntity = new GlAccount();

                    updatedEntity = glAccounts.get(entity.getId());

                    if (entity.getGeneralBudgetDebitId() != null && entity.getGeneralBudgetDebitId() != 0) {
                        GeneralBudgetGuide budgetGuide = new GeneralBudgetGuide();
                        budgetGuide.setId(entity.getGeneralBudgetDebitId());
                        updatedEntity.setGeneralBudgetIdDebit(budgetGuide);
                    } else {
                        updatedEntity.setGeneralBudgetIdDebit(null);
                    }
                    if (entity.getGeneralBudgetCreditId() != null && entity.getGeneralBudgetCreditId() != 0) {
                        GeneralBudgetGuide budgetGuide = new GeneralBudgetGuide();
                        budgetGuide.setId(entity.getGeneralBudgetCreditId());
                        updatedEntity.setGeneralBudgetIdCredit(budgetGuide);
                    } else {
                        updatedEntity.setGeneralBudgetIdCredit(null);
                    }

                    glAccountList.add(updatedEntity);
                }

                if (level.equals(entityLevel)) {
                    glAccountService.updateAllGlAccounts(glAccountList, false, branchId);
                } else {
                    glAccountService.updateAllGlAccounts(glAccountList, true, branchId);
                }

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                showMessageGeneral = Boolean.TRUE;
            } catch (Exception e) {
                showMessageGeneral = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لم يتم الحفظ"));
            }
        } catch (Exception e) {
            saveError(e, "AllocationGeneralItemListMB", "save");
        }
    }

    public String exit() {
        return "";
    }

    public void onCellEdit(CellEditEvent event) {
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GlAccountEntity getGlAccountEntity() {
        return glAccountEntity;
    }

    public void setGlAccountEntity(GlAccountEntity glAccountEntity) {
        this.glAccountEntity = glAccountEntity;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<GlAccountEntity> getGlAccountEntityList() {
        return glAccountEntityList;
    }

    public void setGlAccountEntityList(List<GlAccountEntity> glAccountEntityList) {
        this.glAccountEntityList = glAccountEntityList;
    }

    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    public GlAccountEntity getGlAccountEntitySelected() {
        return glAccountEntitySelected;
    }

    public void setGlAccountEntitySelected(GlAccountEntity glAccountEntitySelected) {
        this.glAccountEntitySelected = glAccountEntitySelected;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public List<GlAccountEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<GlAccountEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<GeneralBudgetGuide> getGeneralBudgetList() {
        return generalBudgetList;
    }

    public void setGeneralBudgetList(List<GeneralBudgetGuide> generalBudgetList) {
        this.generalBudgetList = generalBudgetList;
    }

    public GeneralBudgetGuideConvertor getGeneralBudgetGuideConvertor() {
        return generalBudgetGuideConvertor;
    }

    public void setGeneralBudgetGuideConvertor(GeneralBudgetGuideConvertor generalBudgetGuideConvertor) {
        this.generalBudgetGuideConvertor = generalBudgetGuideConvertor;
    }

    public GlaccountConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlaccountConverter glAccountConverter) {
        this.glAccountConverter = glAccountConverter;
    }
}
