package com.toby.proProductionMovementDelivrey;

import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementService;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "proProductionMovementDelivreyListMB")
@ViewScoped
public class ProProductionMovementDelivreyListMB extends BaseFormBean {

    private List<ProProductMovementDTO> proProductMovementDTOs; 
    private List<ProProductMovementDetailDTO> proProductMovementDetailDTOs;
    private ProProductMovementDTO proProductMovementDTOSelected;
    
    @EJB
    private ProProductionMovementService proProductionMovementService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService ;
    
    public ProProductionMovementDelivreyListMB() {
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("save")) {
           try {
                proProductMovementDTOs = proProductionMovementService.getAllByType(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void delete() {
        proProductionMovementService.deleteBySelected(proProductMovementDTOSelected.getId());
        proProductMovementDTOs = proProductionMovementService.getAllByType(1);
    }

    public String goToAdd() {
        try{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../proProductionMovementDelivrey/proProductionMovementForm.xhtml");
        return "";
         } catch (Exception e) {
            saveError(e, "proProductMovementListMB", "goToAdd");    
                 return null;
        }
       }

    public String goToEdit() {
        
        try {
        if (proProductMovementDTOSelected!= null && proProductMovementDTOSelected.getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("proProductMovementDTOSelected", proProductMovementDTOSelected.getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../proProductionMovementDelivrey/proProductionMovementForm.xhtml");
            return "";
        } else {
            return "";
        }
        } catch (Exception e) {
            saveError(e, "proProductMovementListMB", "goToEdit");    
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
    
     public List<ProProductMovementDTO> getProProductMovementDTOs() {
        if(proProductMovementDTOs==null || proProductMovementDTOs.isEmpty()){
         proProductMovementDTOs = proProductionMovementService.getAllByType(1);
        }
        return proProductMovementDTOs;
    }

    
    public void setProProductMovementDTOs(List<ProProductMovementDTO> proProductMovementDTOs) {
        this.proProductMovementDTOs = proProductMovementDTOs;
    }

    public ProProductMovementDTO getProProductMovementDTOSelected() {
        return proProductMovementDTOSelected;
    }

    
    public void setProProductMovementDTOSelected(ProProductMovementDTO proProductMovementDTOSelected) {
        this.proProductMovementDTOSelected = proProductMovementDTOSelected;
    }

    
    public List<ProProductMovementDetailDTO> getProProductMovementDetailDTOs() {
        if (proProductMovementDetailDTOs == null || proProductMovementDetailDTOs.isEmpty()) {
            proProductMovementDetailDTOs = proProductionMovementDetailService.getAllByType(1);
        }
        return proProductMovementDetailDTOs;
    }


    public void setProProductMovementDetailDTOs(List<ProProductMovementDetailDTO> proProductMovementDetailDTOs) {
        this.proProductMovementDetailDTOs = proProductMovementDetailDTOs;
    }
    
}
