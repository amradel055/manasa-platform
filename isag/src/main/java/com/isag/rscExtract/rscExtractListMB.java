package com.isag.rscExtract;

import com.isag.businessservice.RscExtractRegisterService;
import com.isag.dto.RscExtractRegisterDTO;
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
@Named(value = "rscExtractListMB")
@ViewScoped
public class rscExtractListMB extends BaseListBean {

    private List<RscExtractRegisterDTO> rscExtractRegisterDTOList = new ArrayList<>();
    private RscExtractRegisterDTO rscExtractRegisterDTOSelected;
    private RscExtractCommon rscExtractCommon;
    private String uri;
    @EJB
    private RscExtractRegisterService rscExtractRegisterService;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (rscExtractRegisterDTOSelected != null) {
                try {
                    rscExtractRegisterService.deleteRscExtractRegister(rscExtractRegisterDTOSelected, getUserData().getUser());
                    rscExtractRegisterDTOList.remove(rscExtractRegisterDTOSelected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "delete");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddrscExtractRegisterDTOSelected");
            if (uri.contains("rscExtractSummary")) {
                exit("../rscExtractSummary/rscExtractSummaryform.xhtml");
            } else if (uri.contains("rscExtractInv")) {
                exit("../rscExtractInv/rscExtractform.xhtml");
            } else {
                exit("../rscExtract/rscExtractform.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (rscExtractRegisterDTOSelected != null && rscExtractRegisterDTOSelected.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscExtractRegisterDTOSelected", rscExtractRegisterDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "EditrscExtractRegisterDTOSelected");
                if (uri.contains("rscExtractSummary")) {
                    exit("../rscExtractSummary/rscExtractSummaryform.xhtml");
                } else if (uri.contains("rscExtractInv")) {
                    exit("../rscExtractInv/rscExtractform.xhtml");
                } else {
                    exit("../rscExtract/rscExtractform.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "goToEdit");
            return null;
        }
    }

    public void print() {
        try {
            if (rscExtractRegisterDTOSelected != null && rscExtractRegisterDTOSelected.getId() != null) {
                rscExtractRegisterDTOSelected = getRscExtractCommon().returnObject(rscExtractRegisterDTOSelected.getId(), uri);
                getRscExtractCommon().print(rscExtractRegisterDTOSelected, uri);
            }
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "print");
        }
    }

    @Override
    public void load() {
        try {
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            rscExtractRegisterDTOList = rscExtractRegisterService.getBranchRscExtractRegisterActiveByBranch(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "rscExtractListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscTenderDTOSelected
     */
    public RscExtractRegisterDTO getRscExtractRegisterDTOSelected() {
        return rscExtractRegisterDTOSelected;
    }

    /**
     * @param rscTenderDTOSelected the rscTenderDTOSelected to set
     */
    public void setRscExtractRegisterDTOSelected(RscExtractRegisterDTO rscExtractRegisterDTOSelected) {
        this.rscExtractRegisterDTOSelected = rscExtractRegisterDTOSelected;
    }

    /**
     * @return the rscTenderDTOList
     */
    public List<RscExtractRegisterDTO> getRscExtractRegisterDTOList() {
        return rscExtractRegisterDTOList;
    }

    /**
     * @param rscTenderDTOList the rscTenderDTOList to set
     */
    public void setRscExtractRegisterDTOList(List<RscExtractRegisterDTO> rscExtractRegisterDTOList) {
        this.rscExtractRegisterDTOList = rscExtractRegisterDTOList;
    }

    /**
     * @return the rscExtractCommon
     */
    public RscExtractCommon getRscExtractCommon() {
        if (rscExtractCommon == null) {
            rscExtractCommon = new RscExtractCommon(rscExtractRegisterService, getUserData());
        }
        return rscExtractCommon;
    }

    /**
     * @param rscExtractCommon the rscExtractCommon to set
     */
    public void setRscExtractCommon(RscExtractCommon rscExtractCommon) {
        this.rscExtractCommon = rscExtractCommon;
    }

}
