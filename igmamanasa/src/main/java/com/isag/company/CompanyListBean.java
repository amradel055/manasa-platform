/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.company;

import com.isag.businessservice.CompanyService;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaPrivilege;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "companyListBean")
@ViewScoped
public class CompanyListBean extends BaseListBean implements Serializable {

    @EJB
    private CompanyService companyService;
    private List<IgmaCompany> companies;
    private IgmaCompany company;

    private IgmaPrivilege privilege;

    private Integer selectedCompany;
    private IgmaCompany selectedComp;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "CostCenterBean", "init");
        }
    }

    @Override
    public void delete() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            UserData userData = (UserData) context.getSessionMap().get("userLogInData");
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedCompany != null && selectedCompany > 0) {
                company = new IgmaCompany();
                company.setId(selectedCompany);
                setCompany(company);
                try {
                    companyService.deleteCompany(company);
                    load();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userDDs.get("UNIQE_KEY_ERROR")));
                }

            }
        } catch (Exception e) {
            saveError(e, "CompanyListBean", "delete");
        }
    }

    @Override
    public void filter() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        try {
            setCompanies(companyService.getAllCompanies());
        } catch (Exception e) {
            saveError(e, "CompanyListBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        return "Company";
    }

    /**
     * @return the companies
     */
    public List<IgmaCompany> getCompanies() {
        return companies;
    }

    /**
     * @param companies the companies to set
     */
    public void setCompanies(List<IgmaCompany> companies) {
        this.companies = companies;
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("companyScreenMode", "Add");
            exit("../company/companyForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CompanyListBean", "goToAdd");
            return null;
        }
    }

    public void add() {
        try {
            IgmaCompany newCompany = new IgmaCompany();
//        newCompany.setMarkEdit(Boolean.TRUE);
            companies.add(0, newCompany);
        } catch (Exception e) {
            saveError(e, "CompanyListBean", "add");
        }
    }

    @Override
    public String goToEdit() {
        try {

            if (selectedCompany != null && selectedCompany > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("selectedCompany", selectedCompany);
                context.getSessionMap().put("companyScreenMode", "Edit");
                exit("../company/companyForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "CompanyListBean", "goToEdit");
            return null;
        }
    }

    public void edit() {
//        if (selectedComp != null) {
////            selectedComp.setMarkEdit(Boolean.TRUE);
//        }
    }

    /**
     * @return the privilege
     */
    public IgmaPrivilege getPrivilege() {
        return privilege;
    }

    /**
     * @param privilege the privilege to set
     */
    public void setPrivilege(IgmaPrivilege privilege) {
        this.privilege = privilege;
    }

    /**
     * @return the selectedCompany
     */
    public Integer getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(Integer selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the company
     */
    public IgmaCompany getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(IgmaCompany company) {
        this.company = company;
    }

    /**
     * @return the selectedComp
     */
    public IgmaCompany getSelectedComp() {
        return selectedComp;
    }

    /**
     * @param selectedComp the selectedComp to set
     */
    public void setSelectedComp(IgmaCompany selectedComp) {
        this.selectedComp = selectedComp;
    }

    /**
     * @return the loggedInUser
     */
//    public IsagUser getLoggedInUser() {
//        return loggedInUser;
//    }
//
//    /**
//     * @param loggedInUser the loggedInUser to set
//     */
//    public void setLoggedInUser(IsagUser loggedInUser) {
//        this.loggedInUser = loggedInUser;
//    }
}
