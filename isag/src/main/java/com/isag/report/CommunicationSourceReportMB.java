/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.SymbolService;
import com.isag.converter.SymbolConverter;
import com.isag.dto.SymbolDTO;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */
@Named(value = "communicationSourceReportMB")
@ViewScoped
public class CommunicationSourceReportMB implements Serializable {

    private List<SymbolDTO> symboldtoList;
    private SymbolConverter symbolConverter;
    private SymbolDTO communicationSelected;
    @EJB
    SymbolService symbolService;

    private UserData userData = new UserData();

    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));

    }

    public void reset() {
    }

    public void search() {
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
     * @return the symboldtoList
     */
    public List<SymbolDTO> getSymboldtoList() {

        if (symboldtoList == null || symboldtoList.isEmpty()) {
            symboldtoList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 55);
        }
        return symboldtoList;
    }

    /**
     * @param symboldtoList the symboldtoList to set
     */
    public void setSymboldtoList(List<SymbolDTO> symboldtoList) {
        this.symboldtoList = symboldtoList;
    }

    /**
     * @return the symbolConverter
     */
    public SymbolConverter getSymbolConverter() {
        return symbolConverter;
    }

    /**
     * @param symbolConverter the symbolConverter to set
     */
    public void setSymbolConverter(SymbolConverter symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    /**
     * @return the communicationSelected
     */
    public SymbolDTO getCommunicationSelected() {
        return communicationSelected;
    }

    /**
     * @param communicationSelected the communicationSelected to set
     */
    public void setCommunicationSelected(SymbolDTO communicationSelected) {
        this.communicationSelected = communicationSelected;
    }

 
   

}
