/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmItem;

import com.isag.businessservice.GmItemService;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.dto.GmItemDTO;
import com.isag.entity.GmItem;
import com.isag.entity.Symbol;

/**
 *
 * @author hq004
 */
@Named(value = "gmItemsListMB")
@ViewScoped
public class GmItemsListMB extends BaseListBean {

    private GmItemDTO gmItemDTOSeleceted;
    private List<GmItemDTO> gmItemDTOList;

    @EJB
    GmItemService gmItemService;
    @EJB
    AutoCompleteService autoCompleteService;

    @Override
    @PostConstruct
    public void init() {

        setGmItemDTOList(gmItemService.findGmItemByCompanyId(getUserData().getCompany().getId()));

    }

    @Override
    public void delete() {
        if (getGmItemDTOSeleceted() != null && getGmItemDTOSeleceted().getId() != null) {
            GmItem gmItem = new GmItem();
            gmItem.setId(getGmItemDTOSeleceted().getId());
            gmItemService.deleteGmItem(gmItem);
            getGmItemDTOList().remove(getGmItemDTOSeleceted());
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

            exit("../gmitemsymbol/gmitemsymbolForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getGmItemDTOSeleceted() != null && getGmItemDTOSeleceted().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("gmItemSelected", getGmItemDTOSeleceted().getId());
                context.getSessionMap().put("ScreenMode", "EditGmItem");

                exit("../gmitemsymbol/gmitemsymbolForm.xhtml");
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
     * @return the gmItemDTOSeleceted
     */
    public GmItemDTO getGmItemDTOSeleceted() {
        return gmItemDTOSeleceted;
    }

    /**
     * @param gmItemDTOSeleceted the gmItemDTOSeleceted to set
     */
    public void setGmItemDTOSeleceted(GmItemDTO gmItemDTOSeleceted) {
        this.gmItemDTOSeleceted = gmItemDTOSeleceted;
    }

    /**
     * @return the gmItemDTOList
     */
    public List<GmItemDTO> getGmItemDTOList() {
        return gmItemDTOList;
    }

    /**
     * @param gmItemDTOList the gmItemDTOList to set
     */
    public void setGmItemDTOList(List<GmItemDTO> gmItemDTOList) {
        this.gmItemDTOList = gmItemDTOList;
    }

}
