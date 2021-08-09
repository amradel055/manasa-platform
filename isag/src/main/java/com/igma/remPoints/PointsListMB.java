/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remPoints;

import com.isag.dto.RemPointsDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemPointsService;

/**
 *
 * @author hq004
 */
@Named(value = "pointsListMB")
@ViewScoped
public class PointsListMB extends BaseListBean {

    private RemPointsDTO remPointsDTO;
    private List<RemPointsDTO> remPointsDTOList;

    @EJB
    RemPointsService pointsService;

    @Override
    @PostConstruct
    public void init() {
        setRemPointsDTOList(pointsService.findPoints(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        pointsService.deletePoint(getRemPointsDTO());
        remPointsDTOList.remove(getRemPointsDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddPointsDTO");
            exit("../rempoints/rempointForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRemPointsDTO() != null && getRemPointsDTO().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("pointsDTOlected", getRemPointsDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditpointsDTO");
                exit("../rempoints/rempointForm.xhtml");
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
     * @return the remPointsDTO
     */
    public RemPointsDTO getRemPointsDTO() {
        return remPointsDTO;
    }

    /**
     * @param remPointsDTO the remPointsDTO to set
     */
    public void setRemPointsDTO(RemPointsDTO remPointsDTO) {
        this.remPointsDTO = remPointsDTO;
    }

    /**
     * @return the remPointsDTOList
     */
    public List<RemPointsDTO> getRemPointsDTOList() {
        return remPointsDTOList;
    }

    /**
     * @param remPointsDTOList the remPointsDTOList to set
     */
    public void setRemPointsDTOList(List<RemPointsDTO> remPointsDTOList) {
        this.remPointsDTOList = remPointsDTOList;
    }
    
    
}
