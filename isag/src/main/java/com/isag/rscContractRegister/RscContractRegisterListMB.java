package com.isag.rscContractRegister;

import com.isag.businessservice.RscContractRegisterService;
import com.isag.dto.RscContractRegisterDTO;
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
@Named(value = "rscContractRegisterListMB")
@ViewScoped
public class RscContractRegisterListMB extends BaseListBean {

    private List<RscContractRegisterDTO> rscContractRegisterDTOList = new ArrayList<>();
    private RscContractRegisterDTO rscContractRegisterDTOSelected;
    private RscContractRegisterCommon rscContractRegisterCommon;

    @EJB
    private RscContractRegisterService rscContractRegisterService;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getRscContractRegisterDTOSelected() != null) {
                try {
                    rscContractRegisterService.deleteRscContractRegister(getRscContractRegisterDTOSelected(), getUserData().getUser());

                    if (rscContractRegisterDTOSelected.getMsg() != null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                    } else {
                        rscContractRegisterDTOList.remove(rscContractRegisterDTOSelected);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "delete");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddrscContractRegisterDTOSelected");

            exit("../rscContractRegistering/newrscContractregisterform.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRscContractRegisterDTOSelected() != null && getRscContractRegisterDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscContractRegisterDTOSelected", getRscContractRegisterDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditrscContractRegisterDTOSelected");
                exit("../rscContractRegistering/newrscContractregisterform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "goToEdit");
            return null;
        }
    }

    public void print() {
        try {
            rscContractRegisterDTOSelected = getRscContractRegisterCommon().returnObject(rscContractRegisterDTOSelected.getId());
            getRscContractRegisterCommon().print(rscContractRegisterDTOSelected);
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "print");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setRscContractRegisterDTOList(rscContractRegisterService.getBranchRscContractRegisterActiveByBranch(getUserData().getUserBranch().getId()));
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscContractRegisterDTOList
     */
    public List<RscContractRegisterDTO> getRscContractRegisterDTOList() {
        return rscContractRegisterDTOList;
    }

    /**
     * @param rscContractRegisterDTOList the rscContractRegisterDTOList to set
     */
    public void setRscContractRegisterDTOList(List<RscContractRegisterDTO> rscContractRegisterDTOList) {
        this.rscContractRegisterDTOList = rscContractRegisterDTOList;
    }

    /**
     * @return the rscContractRegisterDTOSelected
     */
    public RscContractRegisterDTO getRscContractRegisterDTOSelected() {
        return rscContractRegisterDTOSelected;
    }

    /**
     * @param rscContractRegisterDTOSelected the rscContractRegisterDTOSelected
     * to set
     */
    public void setRscContractRegisterDTOSelected(RscContractRegisterDTO rscContractRegisterDTOSelected) {
        this.rscContractRegisterDTOSelected = rscContractRegisterDTOSelected;
    }

    /**
     * @return the rscContractRegisterService
     */
    public RscContractRegisterService getRscContractRegisterService() {
        return rscContractRegisterService;
    }

    /**
     * @param rscContractRegisterService the rscContractRegisterService to set
     */
    public void setRscContractRegisterService(RscContractRegisterService rscContractRegisterService) {
        this.rscContractRegisterService = rscContractRegisterService;
    }

    /**
     * @return the rscContractRegisterCommon
     */
    public RscContractRegisterCommon getRscContractRegisterCommon() {
        if (rscContractRegisterCommon == null) {
            rscContractRegisterCommon = new RscContractRegisterCommon(rscContractRegisterService, getUserData());
        }
        return rscContractRegisterCommon;
    }

    /**
     * @param rscContractRegisterCommon the rscContractRegisterCommon to set
     */
    public void setRscContractRegisterCommon(RscContractRegisterCommon rscContractRegisterCommon) {
        this.rscContractRegisterCommon = rscContractRegisterCommon;
    }

}
