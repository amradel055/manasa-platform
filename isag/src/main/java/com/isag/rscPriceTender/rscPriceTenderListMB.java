package com.isag.rscPriceTender;

import com.isag.businessservice.RscPriceTenderService;
import com.isag.dto.RscPriceTenderDTO;
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
@Named(value = "rscPriceTenderListMB")
@ViewScoped
public class rscPriceTenderListMB extends BaseListBean {

    private List<RscPriceTenderDTO> rscPriceTenderDTOList = new ArrayList<>();
    private RscPriceTenderDTO rscPriceTenderDTOSelected;
    private RscPriceTenderCommon rscPriceTenderCommon;
    @EJB
    private RscPriceTenderService rscPriceTenderService;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (rscPriceTenderDTOSelected != null) {
                try {
                    rscPriceTenderService.deleteRscPriceTender(rscPriceTenderDTOSelected, getUserData().getUser());
                    rscPriceTenderDTOList.remove(rscPriceTenderDTOSelected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "delete");
        }
    }

    public void print() {
        try {
            rscPriceTenderDTOSelected = getRscPriceTenderCommon().returnObject(rscPriceTenderDTOSelected.getId());
            if (rscPriceTenderDTOSelected != null && rscPriceTenderDTOSelected.getId() != null
                    && rscPriceTenderDTOSelected.getRscPriceTenderDetailWorkUnitDTOList() != null
                    && !rscPriceTenderDTOSelected.getRscPriceTenderDetailWorkUnitDTOList().isEmpty()) {
                rscPriceTenderDTOSelected = getRscPriceTenderCommon().returnObject(rscPriceTenderDTOSelected.getId());
                getRscPriceTenderCommon().print(rscPriceTenderDTOSelected);
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "print");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddrscPriceTenderDTOSelected");

            exit("../rscTenderPrice/rscTenderPriceForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (rscPriceTenderDTOSelected != null && rscPriceTenderDTOSelected.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscPriceTenderDTOSelected", rscPriceTenderDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "EditrscPriceTenderDTOSelected");
                exit("../rscTenderPrice/rscTenderPriceForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            rscPriceTenderDTOList = rscPriceTenderService.getBranchRscPriceTenderActiveByBranch(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "rscPriceTenderListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscTenderDTOSelected
     */
    public RscPriceTenderDTO getRscPriceTenderDTOSelected() {
        return rscPriceTenderDTOSelected;
    }

    /**
     * @param rscTenderDTOSelected the rscTenderDTOSelected to set
     */
    public void setRscPriceTenderDTOSelected(RscPriceTenderDTO rscPriceTenderDTOSelected) {
        this.rscPriceTenderDTOSelected = rscPriceTenderDTOSelected;
    }

    /**
     * @return the rscTenderDTOList
     */
    public List<RscPriceTenderDTO> getRscPriceTenderDTOList() {
        return rscPriceTenderDTOList;
    }

    /**
     * @param rscTenderDTOList the rscTenderDTOList to set
     */
    public void setRscPriceTenderDTOList(List<RscPriceTenderDTO> rscPriceTenderDTOList) {
        this.rscPriceTenderDTOList = rscPriceTenderDTOList;
    }

    /**
     * @return the rscPriceTenderCommon
     */
    public RscPriceTenderCommon getRscPriceTenderCommon() {
        if (rscPriceTenderCommon == null) {
            rscPriceTenderCommon = new RscPriceTenderCommon(rscPriceTenderService, getUserData());
        }
        return rscPriceTenderCommon;
    }

    /**
     * @param rscPriceTenderCommon the rscPriceTenderCommon to set
     */
    public void setRscPriceTenderCommon(RscPriceTenderCommon rscPriceTenderCommon) {
        this.rscPriceTenderCommon = rscPriceTenderCommon;
    }
}
