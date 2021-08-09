/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remTraffic;

import com.isag.businessservice.RemTrafficDetailService;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemTrafficService;
import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemTrafficDetailDTO;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq004
 */
@Named(value = "trafficFormMB")
@ViewScoped
public class TrafficFormMB extends BaseFormBean {

    private RemTrafficDTO remTrafficDTO;

    @EJB
    RemTrafficService remTrafficService;
    @EJB
    RemTrafficDetailService remTrafficDetailService;

    @Override
    public String save() {
        if (getRemTrafficDTO().getRemTrafficDetailDTOList() != null && !getRemTrafficDTO().getRemTrafficDetailDTOList().isEmpty()) {
            setRemTrafficDTO(remTrafficService.save(getRemTrafficDTO(), getUserData().getUser()));
            msgSave(getRemTrafficDTO().getMsg());
        }else{
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        
        }
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddbusesDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EdittrafficDTO")) {
            try {
                Integer trafficId = (Integer) getContext().getSessionMap().get("trafficDTOlected");
                setRemTrafficDTO(remTrafficService.findTraffic(trafficId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteDetail(RemTrafficDetailDTO remTrafficDetailDTO) {
        if (remTrafficDetailDTO != null) {
            if (getRemTrafficDTO().getRemTrafficDetailDTOSelected().getId() != null) {
                getRemTrafficDTO().getRemTrafficDetailDTODeletedList().add(remTrafficDetailDTO);
            }
            getRemTrafficDTO().getRemTrafficDetailDTOList().remove(remTrafficDetailDTO);

            for (RemTrafficDetailDTO detail : getRemTrafficDTO().getRemTrafficDetailDTOList()) {
                if (detail.getPointId() == null && detail.getPointIdBakUp() != null) {
                    detail.setPointId(detail.getPointIdBakUp());
                }
            }
        }
    }

    public void addNewDetail() {
        try {
            setRemTrafficDTO(remTrafficDetailService.addDetail(getRemTrafficDTO(), getIndex()));
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "addNewDetail");
        }

    }

    public void reset() {
        setRemTrafficDTO(null);
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
     * @return the remTrafficDTO
     */
    public RemTrafficDTO getRemTrafficDTO() {
        if (remTrafficDTO == null) {
            remTrafficDTO = new RemTrafficDTO();
        }
        return remTrafficDTO;
    }

    /**
     * @param remTrafficDTO the remTrafficDTO to set
     */
    public void setRemTrafficDTO(RemTrafficDTO remTrafficDTO) {
        this.remTrafficDTO = remTrafficDTO;
    }

}
