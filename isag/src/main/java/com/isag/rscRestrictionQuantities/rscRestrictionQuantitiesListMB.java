package com.isag.rscRestrictionQuantities;

import com.isag.businessservice.RscRestrictionQuantitiesService;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Named(value = "rscRestrictionQuantitiesListMB")
@ViewScoped
public class rscRestrictionQuantitiesListMB extends BaseListBean {

    private List<RscRestrictionQuantitiesDTO> rscRestrictionQuantitiesDTOList = new ArrayList<>();
    private RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTOSelected;

    private RscRestrictionQuantitiesCommon rscRestrictionQuantitiesCommon;

    @EJB
    private RscRestrictionQuantitiesService rscRestrictionQuantitiesService;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (rscRestrictionQuantitiesDTOSelected != null) {
                try {
                   String error = rscRestrictionQuantitiesService.deleteRscRestrictionQuantities(rscRestrictionQuantitiesDTOSelected, getUserData().getUser());
                    if (error != null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                    } else {
                        rscRestrictionQuantitiesDTOList.remove(rscRestrictionQuantitiesDTOSelected);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "delete");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddrscRestrictionQuantitiesDTOSelected");

            exit("../rscRestrictionQuantities/newrscRestrictionQuantitiesform_1.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (rscRestrictionQuantitiesDTOSelected != null && rscRestrictionQuantitiesDTOSelected.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscRestrictionQuantitiesDTOSelected", rscRestrictionQuantitiesDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "EditrscRestrictionQuantitiesDTOSelected");
                exit("../rscRestrictionQuantities/newrscRestrictionQuantitiesform_1.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "goToEdit");
            return null;
        }
    }

    public void print() {
        try {
            if (rscRestrictionQuantitiesDTOSelected != null && rscRestrictionQuantitiesDTOSelected.getId() != null) {
                rscRestrictionQuantitiesDTOSelected = getRscRestrictionQuantitiesCommon().returnObject(rscRestrictionQuantitiesDTOSelected.getId());
                getRscRestrictionQuantitiesCommon().print(rscRestrictionQuantitiesDTOSelected);
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "print");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            rscRestrictionQuantitiesDTOList = rscRestrictionQuantitiesService.getRscRestrictionQuantitiesByBranch(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscRestrictionQuantitiesDTOList
     */
    public List<RscRestrictionQuantitiesDTO> getRscRestrictionQuantitiesDTOList() {
        return rscRestrictionQuantitiesDTOList;
    }

    /**
     * @param rscRestrictionQuantitiesDTOList the
     * rscRestrictionQuantitiesDTOList to set
     */
    public void setRscRestrictionQuantitiesDTOList(List<RscRestrictionQuantitiesDTO> rscRestrictionQuantitiesDTOList) {
        this.rscRestrictionQuantitiesDTOList = rscRestrictionQuantitiesDTOList;
    }

    /**
     * @return the rscRestrictionQuantitiesDTOSelected
     */
    public RscRestrictionQuantitiesDTO getRscRestrictionQuantitiesDTOSelected() {
        return rscRestrictionQuantitiesDTOSelected;
    }

    /**
     * @param rscRestrictionQuantitiesDTOSelected the
     * rscRestrictionQuantitiesDTOSelected to set
     */
    public void setRscRestrictionQuantitiesDTOSelected(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTOSelected) {
        this.rscRestrictionQuantitiesDTOSelected = rscRestrictionQuantitiesDTOSelected;
    }

    /**
     * @return the rscRestrictionQuantitiesCommon
     */
    public RscRestrictionQuantitiesCommon getRscRestrictionQuantitiesCommon() {
        if (rscRestrictionQuantitiesCommon == null) {
            rscRestrictionQuantitiesCommon = new RscRestrictionQuantitiesCommon(rscRestrictionQuantitiesService, getUserData());
        }
        return rscRestrictionQuantitiesCommon;
    }

    /**
     * @param rscRestrictionQuantitiesCommon the rscRestrictionQuantitiesCommon
     * to set
     */
    public void setRscRestrictionQuantitiesCommon(RscRestrictionQuantitiesCommon rscRestrictionQuantitiesCommon) {
        this.rscRestrictionQuantitiesCommon = rscRestrictionQuantitiesCommon;
    }
}
