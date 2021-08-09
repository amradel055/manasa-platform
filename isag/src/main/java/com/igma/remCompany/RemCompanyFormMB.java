/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remCompany;

import com.isag.businessservice.RemCompanyService;
import com.isag.dto.RemCompanyDTO;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "remCompanyFormMB")
@ViewScoped
public class RemCompanyFormMB extends BaseFormBean {

    private RemCompanyDTO companysDTO;

    @EJB
    RemCompanyService companysService;

    @Override
    public String save() {
        setCompanysDTO(companysService.save(getCompanysDTO(), getUserData().getUser()));
        return "";
    }

    
    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddCompanysDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditCompanysDTO")) {
            try {
                Integer busId = (Integer) getContext().getSessionMap().get("CompanysDTOlected");
                setCompanysDTO(companysService.findCompany(busId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reset() {
        setCompanysDTO(null);
    }
    
    public String exit() {
            exit("../remcompanys/companysList.xhtml");
        return "";
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
     * @return the companysDTO
     */
    public RemCompanyDTO getCompanysDTO() {
        if(companysDTO == null){
            companysDTO = new RemCompanyDTO();
        }
        return companysDTO;
    }

    /**
     * @param companysDTO the companysDTO to set
     */
    public void setCompanysDTO(RemCompanyDTO companysDTO) {
        this.companysDTO = companysDTO;
    }

}
