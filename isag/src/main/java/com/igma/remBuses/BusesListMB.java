/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remBuses;

import com.isag.dto.RemBusesDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemBusesService;

/**
 *
 * @author hq004
 */
@Named(value = "busesListMB")
@ViewScoped
public class BusesListMB extends BaseListBean {

    private RemBusesDTO busesDTO;
    private List<RemBusesDTO> busesList;

    @EJB
    RemBusesService busesService;

    @Override
    @PostConstruct
    public void init() {
        setBusesList(busesService.findBuses(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        busesService.deleteBuses(getBusesDTO());
        busesList.remove(getBusesDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddbusesDTO");
            exit("../rembuses/busesForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (busesDTO != null && busesDTO.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("busesDTOlected", busesDTO.getId());
                context.getSessionMap().put("ScreenMode", "EditbusesDTO");
                exit("../rembuses/busesForm.xhtml");
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
     * @return the busesDTO
     */
    public RemBusesDTO getBusesDTO() {
        return busesDTO;
    }

    /**
     * @param busesDTO the busesDTO to set
     */
    public void setBusesDTO(RemBusesDTO busesDTO) {
        this.busesDTO = busesDTO;
    }

    /**
     * @return the busesList
     */
    public List<RemBusesDTO> getBusesList() {
        return busesList;
    }

    /**
     * @param busesList the busesList to set
     */
    public void setBusesList(List<RemBusesDTO> busesList) {
        this.busesList = busesList;
    }

}
