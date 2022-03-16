/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.glyear;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.GlYearService;
import com.isag.entity.Branch;
import com.isag.entity.GlYear;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "glyearFormBean")
@ViewScoped
public class GlyearFormBean extends BaseFormBean implements Serializable {

    private GlYear newGLYear;
    private String screenMode;
    private UserData user;
    private List<IsagCompany> companies;

    private GlYear glyearSelected;

    private Integer selectedCompanyId;

    private List<GlYear> glYears;
    private List<GlYear> glYearsDate;

    @EJB
    private GlYearService glYearService;
    @EJB
    private CompanyService companyService;
    @EJB
    private BranchService branchService;

    public GlyearFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public String save() {
        try {
            /* Integer newYear = newGLYear.getYear();
             Integer ExistedYear = 0 ;
             glYears = glYearService.getAllYear();

             for(GlYear glYear : glYears){                
             if( glYear.getYear() == newYear){
             ExistedYear++;
             }               
             }*/
            Integer newYear = newGLYear.getYear();
            Integer length = String.valueOf(newYear).length();

            glYears = glYearService.getSimilarYearsByBranchId(user.getSelectedBranch(), newGLYear);
            glYearsDate = glYearService.checkDateWithinDate(user.getSelectedBranch(), newGLYear);

            if (!glYears.isEmpty() || !glYearsDate.isEmpty() || length != 4) {
                if (!glYears.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("DUBLICATED_YEAR_YEAR_ERROR")));
                }
                if (!glYearsDate.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("DATE_OVELAPED_ERROR")));
                }
                if (length != 4) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("ERROR_WITH_YEAR")));
                }

                return "";
            } else {
                
                proceedSaveAfterValidation();
                exit("../Glyear/glyearlist.xhtml");
                return "";
            }
        } catch (Exception e) {
            saveError(e, "GlyearFormBean", "save");
            return null;
        }
    }

    public void proceedSaveAfterValidation() {
        try {
            if (selectedCompanyId != null && newGLYear != null) {
                IsagCompany companySelected = new IsagCompany();
                companySelected.setId(selectedCompanyId);
                newGLYear.setCompanyId(companySelected);
                Branch branch = new Branch();
                if (user.getSelectedBranch() != null) {
                    branch = branchService.findBranch(user.getSelectedBranch());
                    newGLYear.setBranchId(branch);
                }
                saveLogic();

            }
        } catch (Exception e) {
            saveError(e, "GlyearFormBean", "proceedSaveAfterValidation");
        }
    }

    public void saveLogic() {
        try {
            if (getScreenMode().equalsIgnoreCase("add")) {
                newGLYear.setCreatedBy(user.getUser());
                newGLYear.setCreationDate(new Date());
                try {
                    glYearService.updateYear(newGLYear);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("DUBLICATED")));
                    e.printStackTrace();
                }

            } else if (getScreenMode().equalsIgnoreCase("Edit")) {
                newGLYear.setModifiedBy(user.getUser());
                newGLYear.setModificationDate(new Date());
                try {
                    glYearService.updateYear(glyearSelected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("DUBLICATED")));
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "GlyearFormBean", "saveLogic");
        }

    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            user = (UserData) context.getSessionMap().get("userLogInData");
            screenMode = ((String) context.getSessionMap().get("ScreenMode"));
            if (user.getCompany() != null && user.getCompany().getId() != null) {
//            companies.add(user.getCompany());
                selectedCompanyId = user.getCompany().getId();
            } else {
                companies = companyService.getAllCompanies();
            }

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Add")) {
                newGLYear = new GlYear();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EDIT")) {
                try {
                    glyearSelected = (GlYear) context.getSessionMap().get("glyearSelected");
                    newGLYear = glyearSelected;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "GlyearFormBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        return "Glyear";
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /**
     * @return the user
     */
    public UserData getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserData user) {
        this.user = user;
    }

    /**
     * @return the glyearSelected
     */
    public GlYear getGlyearSelected() {
        return glyearSelected;
    }

    /**
     * @param glyearSelected the glyearSelected to set
     */
    public void setGlyearSelected(GlYear glyearSelected) {
        this.glyearSelected = glyearSelected;
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
     * @return the companies
     */
    public List<IsagCompany> getCompanies() {
        return companies;
    }

    /**
     * @param companies the companies to set
     */
    public void setCompanies(List<IsagCompany> companies) {
        this.companies = companies;
    }

    /**
     * @return the selectedCompanyId
     */
    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    /**
     * @param selectedCompanyId the selectedCompanyId to set
     */
    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public String exit() {
        exit("../Glyear/glyearlist.xhtml");
        return "";
    }

    public GlYear getNewGLYear() {
        return newGLYear;
    }

    public void setNewGLYear(GlYear newGLYear) {
        this.newGLYear = newGLYear;
    }

    /**
     * @return the glYears
     */
    public List<GlYear> getGlYears() {
        return glYears;
    }

    /**
     * @param glYears the glYears to set
     */
    public void setGlYears(List<GlYear> glYears) {
        this.glYears = glYears;
    }

    /**
     * @return the glYearsDate
     */
    public List<GlYear> getGlYearsDate() {
        return glYearsDate;
    }

    /**
     * @param glYearsDate the glYearsDate to set
     */
    public void setGlYearsDate(List<GlYear> glYearsDate) {
        this.glYearsDate = glYearsDate;
    }

}
