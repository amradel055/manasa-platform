/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmItem;

import com.isag.businessservice.GmItemService;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.dto.GmItemDTO;

/**
 *
 * @author hq004
 */
@Named(value = "gmItemsFormMB")
@ViewScoped
public class GmItemsFormMB extends BaseFormBean {

    private GmItemDTO gmItemDTO;

    @EJB
    GmItemService gmItemService;

    @Override
    public String save() {
        setGmItemDTO(gmItemService.updateGmItemDTO(getGmItemDTO(), getUserData().getUser()));
        savedMeesage(getUserData().getUserDDs().get("SAVED"));
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditGmItem")) {
            try {
                Integer EditGmItem = (Integer) getContext().getSessionMap().get("gmItemSelected");
                setGmItemDTO(gmItemService.getGmItemDTO(EditGmItem));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setGmItemDTO(null);
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
     * @return the gmItemDTO
     */
    public GmItemDTO getGmItemDTO() {
        if(gmItemDTO == null){
            setGmItemDTO(new GmItemDTO());
        }
        return gmItemDTO;
    }

    /**
     * @param gmItemDTO the gmItemDTO to set
     */
    public void setGmItemDTO(GmItemDTO gmItemDTO) {
        this.gmItemDTO = gmItemDTO;
    }

}
