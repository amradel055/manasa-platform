/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.generalbudgetguide;

import com.isag.businessservice.GeneralBudgetGuideService;
import com.isag.define.GroupItemEnum;
import com.isag.entity.GeneralBudgetGuide;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq002
 */
public class GeneralBudgetGuideListBean extends BaseListBean implements Serializable {

    private List<GeneralBudgetGuide> budgetGuideList;
    private GeneralBudgetGuide budgetGuideSelect;
    private Integer budgetGuideId;

    @EJB
    private GeneralBudgetGuideService generalBudgetGuideService;

    private UserData userData;

    public String save() {
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "GeneralBudgetGuideListBean", "init");
        }
    }

    @Override
    public String getScreenName() {
        return "";
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../generalbudget/generalBudgetForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "GeneralBudgetGuideListBean", "goToAdd");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            if (getUserData() != null) {
                IsagCompany companyId = getUserData().getCompany();
                if (companyId != null && companyId.getId() != null) {
                    budgetGuideList = generalBudgetGuideService.getAllGeneralBudgetGuideByCompanyId(companyId.getId());
                } else {
                    budgetGuideList = generalBudgetGuideService.getAllGeneralBudgetGuide();
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralBudgetGuideListBean", "load");

        }

    }

    @Override
    public String goToEdit() {
        try {
            if (budgetGuideId != null && budgetGuideId > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("budgetGuideId", budgetGuideId);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../generalbudget/generalBudgetForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "GeneralBudgetGuideListBean", "goToEdit");
            return null;
        }

    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (budgetGuideSelect != null && budgetGuideSelect.getId() > 0) {
                try {
                    generalBudgetGuideService.deleteGeneralBudgetGuide(budgetGuideSelect);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralBudgetGuideListBean", "delete");
        }
    }

    /**
     * @return the generalBudgetGuideService
     */
    public GeneralBudgetGuideService getGeneralBudgetGuideService() {
        return generalBudgetGuideService;
    }

    /**
     * @param generalBudgetGuideService the generalBudgetGuideService to set
     */
    public void setGeneralBudgetGuideService(GeneralBudgetGuideService generalBudgetGuideService) {
        this.generalBudgetGuideService = generalBudgetGuideService;
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
     * @return the budgetGuideList
     */
    public List<GeneralBudgetGuide> getBudgetGuideList() {
        return budgetGuideList;
    }

    /**
     * @param budgetGuideList the budgetGuideList to set
     */
    public void setBudgetGuideList(List<GeneralBudgetGuide> budgetGuideList) {
        this.budgetGuideList = budgetGuideList;
    }

    /**
     * @return the budgetGuideSelect
     */
    public GeneralBudgetGuide getBudgetGuideSelect() {
        return budgetGuideSelect;
    }

    /**
     * @param budgetGuideSelect the budgetGuideSelect to set
     */
    public void setBudgetGuideSelect(GeneralBudgetGuide budgetGuideSelect) {
        this.budgetGuideSelect = budgetGuideSelect;
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the budgetGuideId
     */
    public Integer getBudgetGuideId() {
        return budgetGuideId;
    }

    /**
     * @param budgetGuideId the budgetGuideId to set //
     */
    public void setBudgetGuideId(Integer budgetGuideId) {
        this.budgetGuideId = budgetGuideId;
    }

    public GroupItemEnum[] getAccountGroups() {
        return GroupItemEnum.values();
    }
}
