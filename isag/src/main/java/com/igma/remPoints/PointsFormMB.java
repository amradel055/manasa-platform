/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remPoints;

import com.isag.dto.RemPointsDTO;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemPointsService;

/**
 *
 * @author hq004
 */
@Named(value = "pointsFormMB")
@ViewScoped
public class PointsFormMB extends BaseFormBean {

    private RemPointsDTO remPointsDTO;

    @EJB
    RemPointsService pointsService;

    @Override
    public String save() {
        setRemPointsDTO(pointsService.save(getRemPointsDTO(), getUserData().getUser()));
        return "";
    }

    
    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddPointsDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditpointsDTO")) {
            try {
                Integer pointId = (Integer) getContext().getSessionMap().get("pointsDTOlected");
                setRemPointsDTO(pointsService.findPoint(pointId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reset() {
        setRemPointsDTO(null);
    }
    
    public String exit() {
            exit("../rempoints/rempointdList.xhtml");
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
     * @return the remPointsDTO
     */
    public RemPointsDTO getRemPointsDTO() {
        if(remPointsDTO == null){
            remPointsDTO = new RemPointsDTO();
        }
        return remPointsDTO;
    }

    /**
     * @param remPointsDTO the remPointsDTO to set
     */
    public void setRemPointsDTO(RemPointsDTO remPointsDTO) {
        this.remPointsDTO = remPointsDTO;
    }


}
