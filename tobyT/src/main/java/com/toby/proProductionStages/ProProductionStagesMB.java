package com.toby.proProductionStages;

import com.toby.businessservice.ProProductionStagesService;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author H
 */
@Named(value = "proProductionStagesMB")
@ViewScoped
public class ProProductionStagesMB extends BaseFormBean {

    private ProProductionStagesDTO proProductionStagesDTO;

    @EJB
    private ProProductionStagesService proProductionStagesService;

    public ProProductionStagesMB() {
    }

    @Override
    @PostConstruct
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer proProductionStageId = (Integer) context.getSessionMap().get("proProductionStagesDTOSelected");
                if (proProductionStagesDTO == null) {
                    proProductionStagesDTO = proProductionStagesService.findProProductionStages(proProductionStageId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void load() {

    }

    @Override
    public String save() {
        if (getProProductionStagesDTO() != null) {
            proProductionStagesService.saveProProductionStage(getProProductionStagesDTO(), getUserData().getUser());
            if(proProductionStagesDTO.getName()!=null&&!"".equals(proProductionStagesDTO.getName())&&!"".equals(proProductionStagesDTO.getNameEn())&&proProductionStagesDTO.getNameEn()!=null&&proProductionStagesDTO.getPrice()!=null
//                    &&(proProductionStagesService.checkForLastStage(getUserData().getUser()) == null ||proProductionStagesDTO.getTypeStage()==0)
                    ){
           try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                context.getSessionMap().put("proProductionStageSelected", proProductionStagesDTO);
                exit("../proproductionstages/proProductionList.xhtml");
                return "";
            } catch (Exception e) {
                saveError(e, "proProductionStagesMB", "save");
                return null;
            }}
        }
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProProductionStagesDTO getProProductionStagesDTO() {
        if (proProductionStagesDTO == null) {
            proProductionStagesDTO = new ProProductionStagesDTO();
        }
        return proProductionStagesDTO;
    }

    public void setProProductionStagesDTO(ProProductionStagesDTO proProductionStagesDTO) {
        this.proProductionStagesDTO = proProductionStagesDTO;
    }

}
