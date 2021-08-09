/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remBuses;

import com.isag.dto.RemBusesDTO;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemBusesService;

/**
 *
 * @author hq004
 */
@Named(value = "busesFormMB")
@ViewScoped
public class BusesFormMB extends BaseFormBean {

    private RemBusesDTO busesDTO;

    @EJB
    RemBusesService busesService;

    @Override
    public String save() {
        setBusesDTO(busesService.save(busesDTO, getUserData().getUser()));
        return "";
    }

    
    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddbusesDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditbusesDTO")) {
            try {
                Integer busId = (Integer) getContext().getSessionMap().get("busesDTOlected");
                setBusesDTO(busesService.findBus(busId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reset() {
        setBusesDTO(null);
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
     * @return the busesDTO
     */
    public RemBusesDTO getBusesDTO() {
        if(busesDTO == null){
            busesDTO = new RemBusesDTO();
        }
        return busesDTO;
    }

    /**
     * @param busesDTO the busesDTO to set
     */
    public void setBusesDTO(RemBusesDTO busesDTO) {
        this.busesDTO = busesDTO;
    }

}
