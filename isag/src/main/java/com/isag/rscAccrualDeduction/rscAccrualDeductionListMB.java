package com.isag.rscAccrualDeduction;

import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.dto.RscAccrualDeductionDTO;
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
import javax.servlet.http.HttpServletRequest;

/**
 * @author khaled
 */
@Named(value = "rscAccrualDeductionListMB")
@ViewScoped
public class rscAccrualDeductionListMB extends BaseListBean {

    private List<RscAccrualDeductionDTO> rscAccrualDeductionDTOList = new ArrayList<>();
    private RscAccrualDeductionDTO RscAccrualDeductionDTOSelected;
    private RscAccrualDeductionCommon rscAccrualDeductionCommon;
    private String uri;
    @EJB
    private RscAccrualDeductionService rscAccrualDeductionService;

    @Override
    @PostConstruct
    public void init() {
        this.load();
    }

    @Override
    public void delete() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (RscAccrualDeductionDTOSelected != null) {
            try {
                rscAccrualDeductionService.deleteRscAccrual(RscAccrualDeductionDTOSelected, getUserData().getUser());
                rscAccrualDeductionDTOList.remove(RscAccrualDeductionDTOSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "AddrscAccrualDeductionDTOSelected");
        if (getUri().contains("rscAccrualDeductiontList")) {
            exit("../rscAccrualDeduction/rscAccrualDeductionform.xhtml");
        } else {
            exit("../hrAccrualDeduction/hrAccrualDeductionform.xhtml");
        }
        return "";
    }

    @Override
    public String goToEdit() {
        if (RscAccrualDeductionDTOSelected != null && RscAccrualDeductionDTOSelected.getId() != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("rscAccrualDeductionDTOSelected", RscAccrualDeductionDTOSelected.getId());
            context.getSessionMap().put("ScreenMode", "EditrscAccrualDeductionDTOSelected");
            if (getUri().contains("rscAccrualDeductiontList")) {
                exit("../rscAccrualDeduction/rscAccrualDeductionform.xhtml");
            } else {
                exit("../hrAccrualDeduction/hrAccrualDeductionform.xhtml");
            }
            return "";
        } else {
            return "";
        }
    }

    public void print() {
        if (RscAccrualDeductionDTOSelected != null && RscAccrualDeductionDTOSelected.getId() != null) {
            RscAccrualDeductionDTOSelected = getRscAccrualDeductionCommon().returnObject(RscAccrualDeductionDTOSelected.getId());
            getRscAccrualDeductionCommon().print(RscAccrualDeductionDTOSelected);
        }
    }

    @Override
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        if (getUri().contains("rscAccrualDeductiontList")) {
            //Rsc
            rscAccrualDeductionDTOList = rscAccrualDeductionService.getBranchRscAccrualActiveByBranch(getUserData().getUserBranch().getId(), 2, 3);
        } else {
            //HR
            rscAccrualDeductionDTOList = rscAccrualDeductionService.getBranchRscAccrualActiveByBranch(getUserData().getUserBranch().getId(), 0, 1);
        }

    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscAccrualDeductionDTOList
     */
    public List<RscAccrualDeductionDTO> getRscAccrualDeductionDTOList() {
        return rscAccrualDeductionDTOList;
    }

    /**
     * @param rscAccrualDeductionDTOList the rscAccrualDeductionDTOList to set
     */
    public void setRscAccrualDeductionDTOList(List<RscAccrualDeductionDTO> rscAccrualDeductionDTOList) {
        this.rscAccrualDeductionDTOList = rscAccrualDeductionDTOList;
    }

    /**
     * @return the rscAccrualDeductionCommon
     */
    public RscAccrualDeductionCommon getRscAccrualDeductionCommon() {
        if (rscAccrualDeductionCommon == null) {
            rscAccrualDeductionCommon = new RscAccrualDeductionCommon(rscAccrualDeductionService, getUserData());
        }
        return rscAccrualDeductionCommon;

    }

    /**
     * @param rscAccrualDeductionCommon the rscAccrualDeductionCommon to set
     */
    public void setRscAccrualDeductionCommon(RscAccrualDeductionCommon rscAccrualDeductionCommon) {
        this.rscAccrualDeductionCommon = rscAccrualDeductionCommon;
    }

    /**
     * @return the RscAccrualDeductionDTOSelected
     */
    public RscAccrualDeductionDTO getRscAccrualDeductionDTOSelected() {
        return RscAccrualDeductionDTOSelected;
    }

    /**
     * @param RscAccrualDeductionDTOSelected the RscAccrualDeductionDTOSelected
     * to set
     */
    public void setRscAccrualDeductionDTOSelected(RscAccrualDeductionDTO RscAccrualDeductionDTOSelected) {
        this.RscAccrualDeductionDTOSelected = RscAccrualDeductionDTOSelected;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
