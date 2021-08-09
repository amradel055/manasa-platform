/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remCompany;


import com.isag.businessservice.RemCompanyService;
import com.isag.dto.RemCompanyDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "remCompanyListMB")
@ViewScoped
public class RemCompanyListMB extends BaseListBean {

    private RemCompanyDTO CompanysDTO;
    private List<RemCompanyDTO> CompanysList;

    @EJB
    RemCompanyService CompanysService;

    @Override
    @PostConstruct
    public void init() {
        setCompanyList(CompanysService.findCompanys(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        CompanysService.deleteCompany(getCompanyDTO());
        CompanysList.remove(getCompanyDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddCompanysDTO");
            exit("../remcompany/remcompanyForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (CompanysDTO != null && CompanysDTO.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("CompanysDTOlected", CompanysDTO.getId());
                context.getSessionMap().put("ScreenMode", "EditCompanysDTO");
                exit("../remcompany/remcompanyForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the CompanysDTO
     */
    public RemCompanyDTO getCompanyDTO() {
        return CompanysDTO;
    }

    /**
     * @param CompanysDTO the CompanysDTO to set
     */
    public void setCompanyDTO(RemCompanyDTO CompanysDTO) {
        this.CompanysDTO = CompanysDTO;
    }

    /**
     * @return the CompanysList
     */
    public List<RemCompanyDTO> getCompanyList() {
        return CompanysList;
    }

    /**
     * @param CompanysList the CompanysList to set
     */
    public void setCompanyList(List<RemCompanyDTO> CompanysList) {
        this.CompanysList = CompanysList;
    }

}
