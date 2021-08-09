package com.isag.rscQotationPriceTender;

import com.isag.rscPriceTender.*;
import com.isag.businessservice.RscPriceTenderService;
import com.isag.businessservice.RscQotationPriceTenderService;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.entity.RscQotationPriceTender;
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
@Named(value = "rscQotationPriceTenderListMB")
@ViewScoped
public class rscQotationPriceTenderListMB extends BaseListBean {

    private List<RscQotationPriceTenderDTO> rscPriceTenderDTOList = new ArrayList<>();
    private RscQotationPriceTenderDTO rscPriceTenderDTOSelected;

    private RscQotationPriceTenderCommon qotationPriceTenderCommon;

    @EJB
    private RscQotationPriceTenderService rscPriceTenderService;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getRscPriceTenderDTOSelected() != null) {
                try {
                    rscPriceTenderService.deleteRscQotationPriceTender(getRscPriceTenderDTOSelected(), getUserData().getUser());
                    getRscPriceTenderDTOList().remove(getRscPriceTenderDTOSelected());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "delete");
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

            exit("../rscQotationPriceTender/rscQotationPriceTenderForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRscPriceTenderDTOSelected() != null && getRscPriceTenderDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscPriceTenderDTOSelected", getRscPriceTenderDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditrscPriceTenderDTOSelected");
                exit("../rscQotationPriceTender/rscQotationPriceTenderForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "goToEdit");
            return null;
        }

    }

    public void print() {
        try {

            rscPriceTenderDTOSelected = getQotationPriceTenderCommon().returnObject(rscPriceTenderDTOSelected.getId());
            if (rscPriceTenderDTOSelected != null && rscPriceTenderDTOSelected.getId() != null
                    && rscPriceTenderDTOSelected.getRscQotationPriceTenderDetailWorkUnitDTOList() != null
                    && !rscPriceTenderDTOSelected.getRscQotationPriceTenderDetailWorkUnitDTOList().isEmpty()) {

                getQotationPriceTenderCommon().print(rscPriceTenderDTOSelected);
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "print");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setRscPriceTenderDTOList(rscPriceTenderService.getBranchRscQotationPriceTenderActiveByBranch(getUserData().getUserBranch().getId()));
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscPriceTenderDTOList
     */
    public List<RscQotationPriceTenderDTO> getRscPriceTenderDTOList() {
        return rscPriceTenderDTOList;
    }

    /**
     * @param rscPriceTenderDTOList the rscPriceTenderDTOList to set
     */
    public void setRscPriceTenderDTOList(List<RscQotationPriceTenderDTO> rscPriceTenderDTOList) {
        this.rscPriceTenderDTOList = rscPriceTenderDTOList;
    }

    /**
     * @return the rscPriceTenderDTOSelected
     */
    public RscQotationPriceTenderDTO getRscPriceTenderDTOSelected() {
        return rscPriceTenderDTOSelected;
    }

    /**
     * @param rscPriceTenderDTOSelected the rscPriceTenderDTOSelected to set
     */
    public void setRscPriceTenderDTOSelected(RscQotationPriceTenderDTO rscPriceTenderDTOSelected) {
        this.rscPriceTenderDTOSelected = rscPriceTenderDTOSelected;
    }

    /**
     * @return the qotationPriceTenderCommon
     */
    public RscQotationPriceTenderCommon getQotationPriceTenderCommon() {
        if (qotationPriceTenderCommon == null) {
            qotationPriceTenderCommon = new RscQotationPriceTenderCommon(rscPriceTenderService, getUserData());
        }
        return qotationPriceTenderCommon;
    }

    /**
     * @param qotationPriceTenderCommon the qotationPriceTenderCommon to set
     */
    public void setQotationPriceTenderCommon(RscQotationPriceTenderCommon qotationPriceTenderCommon) {
        this.qotationPriceTenderCommon = qotationPriceTenderCommon;
    }

}
