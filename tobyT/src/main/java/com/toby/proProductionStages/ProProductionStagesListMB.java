package com.toby.proProductionStages;

import com.toby.businessservice.ProProductionStagesService;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "proProductionStagesListMB")
@ViewScoped
public class ProProductionStagesListMB extends BaseFormBean {

    private ProProductionStagesDTO proProductionStagesDTOSelected;

    private List<ProProductionStagesDTO> proProductionStagesDTOList;

    @EJB
    private ProProductionStagesService proProductionStagesService;

    public ProProductionStagesListMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("save")) {
            try {
                proProductionStagesDTOList = proProductionStagesService.getAllProProductionStagesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void load() {

    }

    public void delete() {
        try {
            proProductionStagesService.deleteProProductionStage(proProductionStagesDTOSelected.getId());
            proProductionStagesDTOList = proProductionStagesService.getAllProProductionStagesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "هذة المرحلة مستخدمة حذفها يؤدي لحدوث مشكلة"));
        }
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../proproductionstages/proProductionForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "proProductionStagesListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (proProductionStagesDTOSelected != null && proProductionStagesDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proProductionStagesDTOSelected", proProductionStagesDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../proProduction/proProductionForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "proProductionStagesListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProProductionStagesDTO getProProductionStagesDTOSelected() {
        return proProductionStagesDTOSelected;
    }

    public void setProProductionStagesDTOSelected(ProProductionStagesDTO proProductionStagesDTOSelected) {
        this.proProductionStagesDTOSelected = proProductionStagesDTOSelected;
    }

    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null | proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProProductionStagesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
