/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.glItems;

import com.isag.businessservice.GlAccountServiceDTO;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.SymbolService;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.SymbolDTO;

/**
 *
 * @author hq004
 */
@Named(value = "glItemsFormMB")
@ViewScoped
public class GlItemsFormMB extends BaseFormBean {

    private SymbolDTO symbolDTO;

    @EJB
    SymbolService symbolService;
    @EJB
    GlAccountServiceDTO glAccountServiceDTO;

    @Override
    public String save() {
        GlAccountDTO glAccountDTO = null;
        if (getUri().contains("glFixedAsset")) {
            glAccountDTO = glAccountServiceDTO.getGlAccountForBank(getUserData().getUserBranch().getId(), 5);
        } else if (getUri().contains("glExpenses")) {
            glAccountDTO = glAccountServiceDTO.getGlAccountForBank(getUserData().getUserBranch().getId(), 6);            
        }
        getSymbolDTO().setAccountId(glAccountDTO == null ? null : glAccountDTO.getId());
        getSymbolDTO().setGeneralSymbolId(41);
        setSymbolDTO(symbolService.updateSymbolDTO(getSymbolDTO(), getUserData().getUser()));
        savedMeesage(getUserData().getUserDDs().get("SAVED"));
        return "";
    }
    

    public String saveItemSymbol() {
        getSymbolDTO().setGeneralSymbolId(58);
        setSymbolDTO(symbolService.updateSymbolDTO(getSymbolDTO(), getUserData().getUser()));
        savedMeesage(getUserData().getUserDDs().get("SAVED"));
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditglFixedAssetForm")) {
            try {
                Integer fixedAssetId = (Integer) getContext().getSessionMap().get("glFixedAssetSelected");
                setSymbolDTO(symbolService.getSymbolDTO(fixedAssetId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setSymbolDTO(null);
    }

    public String exit() {
        exit("../rembuses/busesList.xhtml");
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
     * @return the symbolDTO
     */
    public SymbolDTO getSymbolDTO() {
        if (symbolDTO == null) {
            symbolDTO = new SymbolDTO();
        }
        return symbolDTO;
    }

    /**
     * @param symbolDTO the symbolDTO to set
     */
    public void setSymbolDTO(SymbolDTO symbolDTO) {
        this.symbolDTO = symbolDTO;
    }

}
