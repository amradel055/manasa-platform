package com.igma.OfferCompany;

import com.toby.businessservice.OfferCompanyService;
import com.toby.dto.OfferCompanyDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author H
 */
@Named(value = "offerCompanyListBean")
@ViewScoped
public class OfferCompanyListBean extends BaseFormBean {

    private OfferCompanyDTO offerCompanyDTOSelected;

    private List<OfferCompanyDTO> offerCompanyDTOList;

    @EJB
    OfferCompanyService offerCompanyService;

    public OfferCompanyListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        offerCompanyDTOList = new ArrayList<>();
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("save")) {
            try {
                offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void load() {

    }

    public void delete() {
        offerCompanyService.deleteOfferCompany(getOfferCompanyDTOSelected());
        offerCompanyDTOList.remove(getOfferCompanyDTOSelected());
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../offerCompany/offerCompanyForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "offerCompanyListBean", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (offerCompanyDTOSelected != null && offerCompanyDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("offerCompanyDTOSelected", offerCompanyDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../offerCompany/offerCompanyForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "offerCompanyListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the offerCompanyDTOSelected
     */
    public OfferCompanyDTO getOfferCompanyDTOSelected() {
        return offerCompanyDTOSelected;
    }

    /**
     * @param offerCompanyDTOSelected the offerCompanyDTOSelected to set
     */
    public void setOfferCompanyDTOSelected(OfferCompanyDTO offerCompanyDTOSelected) {
        this.offerCompanyDTOSelected = offerCompanyDTOSelected;
    }

    /**
     * @return the offerCompanyDTOList
     */
    public List<OfferCompanyDTO> getOfferCompanyDTOList() {

        if (offerCompanyDTOList == null | offerCompanyDTOList.isEmpty()) {
            offerCompanyDTOList = offerCompanyService.getALLOfferCompanyByBranchId(getUserData().getUser());
        }

        return offerCompanyDTOList;
    }

    /**
     * @param offerCompanyDTOList the offerCompanyDTOList to set
     */
    public void setOfferCompanyDTOList(List<OfferCompanyDTO> offerCompanyDTOList) {
        this.offerCompanyDTOList = offerCompanyDTOList;
    }

}
