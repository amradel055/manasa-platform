/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.glItems;

import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.SymbolService;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Symbol;

/**
 *
 * @author hq004
 */
@Named(value = "glItemsListMB")
@ViewScoped
public class GlItemsListMB extends BaseListBean {

    private SymbolDTO symbolDTOSelected;
    private List<SymbolDTO> symbolDTOList;

    @EJB
    SymbolService symbolService;

    @Override
    @PostConstruct
    public void init() {
        if (getUri().contains("glFixedAsset")) {
            setSymbolDTOList(symbolService.findFixedAssetByCompanyId(getUserData().getCompany().getId()));
        } else if (getUri().contains("glExpenses")) {
            setSymbolDTOList(symbolService.findExpensesByCompanyId(getUserData().getCompany().getId()));
        }
         else if (getUri().contains("itemSymbol")) {
            setSymbolDTOList(symbolService.findItemSymbolByCompanyId(getUserData().getCompany().getId()));
        }
    }

    @Override
    public void delete() {
        if (getSymbolDTOSelected() != null && getSymbolDTOSelected().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(getSymbolDTOSelected().getId());
            symbolService.deleteSymbol(symbol);
            getSymbolDTOList().remove(getSymbolDTOSelected());
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
            context.getSessionMap().put("ScreenMode", "AddglFixedAssetForm");
            if (getUri().contains("glFixedAsset")) {
                exit("../glFixedAsset/glFixedAssetForm.xhtml");
            } else if (getUri().contains("glExpenses")) {
                exit("../glExpenses/glExpensesForm.xhtml");
            }
             else if (getUri().contains("itemSymbol")) {
                exit("../itemSymbol/itemSymbolForm.xhtml");
            }

            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getSymbolDTOSelected() != null && getSymbolDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("glFixedAssetSelected", getSymbolDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditglFixedAssetForm");
                if (getUri().contains("glFixedAsset")) {
                    exit("../glFixedAsset/glFixedAssetForm.xhtml");
                } else if (getUri().contains("glExpenses")) {
                    exit("../glExpenses/glExpensesForm.xhtml");
                }else if (getUri().contains("itemSymbol")) {
                    exit("../itemSymbol/itemSymbolForm.xhtml");
                }
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
     * @return the symbolDTOList
     */
    public List<SymbolDTO> getSymbolDTOList() {
        return symbolDTOList;
    }

    /**
     * @param symbolDTOList the symbolDTOList to set
     */
    public void setSymbolDTOList(List<SymbolDTO> symbolDTOList) {
        this.symbolDTOList = symbolDTOList;
    }

    /**
     * @return the symbolDTOSelected
     */
    public SymbolDTO getSymbolDTOSelected() {
        return symbolDTOSelected;
    }

    /**
     * @param symbolDTOSelected the symbolDTOSelected to set
     */
    public void setSymbolDTOSelected(SymbolDTO symbolDTOSelected) {
        this.symbolDTOSelected = symbolDTOSelected;
    }

}
