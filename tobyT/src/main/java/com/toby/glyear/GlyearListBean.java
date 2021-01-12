/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.glyear;

import com.toby.businessservice.CompanyService;
import com.toby.businessservice.GlYearService;
import com.toby.entity.GlYear;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "glyearListBean")
@ViewScoped
public class GlyearListBean extends BaseListBean implements Serializable {

    @EJB
    private GlYearService glYearService;
    @EJB
    private CompanyService companyService;

    private List<GlYear> glYears;
    private GlYear glYearselected;
    private UserData userData;

    public GlyearListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try{
        load();
        } catch (Exception e) {
            saveError(e, "GlyearListBean", "init");
        }
    }

    @Override
    public void delete() {
        try {
        if (glYearselected != null) {
            try {
                glYearService.deleteYear(glYearselected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("DELETED")));
                load();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("UNIQE_KEY_ERROR")));

            }

        }
        } catch (Exception e) {
            saveError(e, "GlyearListBean", "delete");
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
        exit("../Glyear/glyearform.xhtml");
        return "";
        } catch (Exception e) {
            saveError(e, "GlyearListBean", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
        if (glYearselected != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("glyearSelected", glYearselected);
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../Glyear/glyearform.xhtml");
            return "-";
        } else {
            return "";
        }
         } catch (Exception e) {
            saveError(e, "GlyearListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = ((UserData) context.getSessionMap().get("userLogInData"));
        if (userData.getIsAdmin()) {
            glYears = glYearService.getAllYear();
        } else {
            if (userData.getSelectedBranch() != null) {
                glYears = glYearService.getALLGlyearByBranchId(userData.getSelectedBranch());
            }

        } } catch (Exception e) {
            saveError(e, "GlyearListBean", "load");
        }
        

    }

    @Override
    public String getScreenName() {
        return "Glyear";
    }

    /**
     * @return the glYear
     */
    public List<GlYear> getGlYears() {
        return glYears;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYears(List<GlYear> glYears) {
        this.glYears = glYears;
    }

    /**
     * @return the glYearselected
     */
    public GlYear getGlYearselected() {
        return glYearselected;
    }

    /**
     * @param glYearselected the glYearselected to set
     */
    public void setGlYearselected(GlYear glYearselected) {
        this.glYearselected = glYearselected;
    }

    /**
     * @return the glYearService
     */
    public GlYearService getGlYearService() {
        return glYearService;
    }

    /**
     * @param glYearService the glYearService to set
     */
    public void setGlYearService(GlYearService glYearService) {
        this.glYearService = glYearService;
    }

    /**
     * @return the companyService
     */
    public CompanyService getCompanyService() {
        return companyService;
    }

    /**
     * @param companyService the companyService to set
     */
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
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

    public String exit() {
        return "base/home";
    }

}
