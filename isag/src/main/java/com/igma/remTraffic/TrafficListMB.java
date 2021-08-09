/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remTraffic;

import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemTrafficService;
import com.isag.dto.RemTrafficDTO;

/**
 *
 * @author hq004
 */
@Named(value = "trafficListMB")
@ViewScoped
public class TrafficListMB extends BaseListBean {

    private RemTrafficDTO remTrafficDTO;
    private List<RemTrafficDTO> remTrafficDTOList;

    @EJB
    RemTrafficService remTrafficService;
    

    @Override
    @PostConstruct
    public void init() {
        setRemTrafficDTOList(remTrafficService.findTraffics(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        setRemTrafficDTO(remTrafficService.findTraffic(getRemTrafficDTO().getId()));
        remTrafficService.deleteTraffic(getRemTrafficDTO());
        remTrafficDTOList.remove(getRemTrafficDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddtrafficDTO");
            exit("../remTraffic/trafficForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRemTrafficDTO() != null && getRemTrafficDTO().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("trafficDTOlected", getRemTrafficDTO().getId());
                context.getSessionMap().put("ScreenMode", "EdittrafficDTO");
                exit("../remTraffic/trafficForm.xhtml");
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
     * @return the remTrafficDTO
     */
    public RemTrafficDTO getRemTrafficDTO() {
        return remTrafficDTO;
    }

    /**
     * @param remTrafficDTO the remTrafficDTO to set
     */
    public void setRemTrafficDTO(RemTrafficDTO remTrafficDTO) {
        this.remTrafficDTO = remTrafficDTO;
    }

    /**
     * @return the remTrafficDTOList
     */
    public List<RemTrafficDTO> getRemTrafficDTOList() {
        return remTrafficDTOList;
    }

    /**
     * @param remTrafficDTOList the remTrafficDTOList to set
     */
    public void setRemTrafficDTOList(List<RemTrafficDTO> remTrafficDTOList) {
        this.remTrafficDTOList = remTrafficDTOList;
    }

}
