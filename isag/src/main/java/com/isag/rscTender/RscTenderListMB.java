package com.isag.rscTender;

import com.isag.businessservice.RscTenderService;
import com.isag.dto.RscTenderDTO;
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
@Named(value = "rscTenderListMB")
@ViewScoped
public class RscTenderListMB extends BaseListBean {

    private List<RscTenderDTO> rscTenderDTOList = new ArrayList<>();
    private RscTenderDTO rscTenderDTOSelected;
    private RscTenderCommon rscTenderCommon;

    @EJB
    private RscTenderService rscTenderService;

    @Override
    @PostConstruct
    public void init() {
        try {
        this.load();
         } catch (Exception e) {
            saveError(e, "RscTenderListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (rscTenderDTOSelected != null) {
            try {
                rscTenderService.deleteRscTender(rscTenderDTOSelected, getUserData().getUser());
                rscTenderDTOList.remove(rscTenderDTOSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        }
         } catch (Exception e) {
            saveError(e, "RscTenderListMB", "delete");
        }
    }

    @Override
    public void filter() {

    }
    
    public void print(){
        try {
        
        rscTenderDTOSelected = getRscTenderCommon().returnObject(rscTenderDTOSelected.getId());
        if(rscTenderDTOSelected != null && rscTenderDTOSelected.getId()!=null 
                && rscTenderDTOSelected.getRscTenderDetailWorkUnitDTOList() !=null 
                && !rscTenderDTOSelected.getRscTenderDetailWorkUnitDTOList().isEmpty()){
        
        getRscTenderCommon().print(rscTenderDTOSelected);
        }
         } catch (Exception e) {
            saveError(e, "RscTenderListMB", "print");
        }
    
    }

    @Override
    public String goToAdd() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "AddrscTenderDTOSelected");
      
        exit("../rscTender/newrscTenderform.xhtml");
      
        return "";
         } catch (Exception e) {
            saveError(e, "RscTenderListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
        if (rscTenderDTOSelected != null && rscTenderDTOSelected.getId() != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("rscTenderDTOSelected", rscTenderDTOSelected.getId());
            context.getSessionMap().put("ScreenMode", "EditrscTenderDTOSelected");
             exit("../rscTender/newrscTenderform.xhtml");
            return "";
        } else {
            return "";
        }
        } catch (Exception e) {
            saveError(e, "RscTenderListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        rscTenderDTOList = rscTenderService.getBranchRscTenderActiveByBranch(getUserData().getUserBranch().getId(),getUserData().getUser());
        } catch (Exception e) {
            saveError(e, "RscTenderListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the rscTenderDTOSelected
     */
    public RscTenderDTO getRscTenderDTOSelected() {
        return rscTenderDTOSelected;
    }

    /**
     * @param rscTenderDTOSelected the rscTenderDTOSelected to set
     */
    public void setRscTenderDTOSelected(RscTenderDTO rscTenderDTOSelected) {
        this.rscTenderDTOSelected = rscTenderDTOSelected;
    }

    /**
     * @return the rscTenderDTOList
     */
    public List<RscTenderDTO> getRscTenderDTOList() {
        return rscTenderDTOList;
    }

    /**
     * @param rscTenderDTOList the rscTenderDTOList to set
     */
    public void setRscTenderDTOList(List<RscTenderDTO> rscTenderDTOList) {
        this.rscTenderDTOList = rscTenderDTOList;
    }

    /**
     * @return the rscTenderCommon
     */
    public RscTenderCommon getRscTenderCommon() {
        if(rscTenderCommon == null){
            rscTenderCommon = new RscTenderCommon(rscTenderService, getUserData());
        }
        return rscTenderCommon;
    }

    /**
     * @param rscTenderCommon the rscTenderCommon to set
     */
    public void setRscTenderCommon(RscTenderCommon rscTenderCommon) {
        this.rscTenderCommon = rscTenderCommon;
    }
}
