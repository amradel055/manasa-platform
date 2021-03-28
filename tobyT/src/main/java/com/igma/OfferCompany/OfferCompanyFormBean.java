package com.igma.OfferCompany;

import com.toby.businessservice.OfferCompanyService;
import com.toby.dto.OfferCompanyDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
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
@Named(value = "offerCompanyFormBean")
@ViewScoped
public class OfferCompanyFormBean extends BaseFormBean {

    private OfferCompanyDTO offerCompanyDTO;

    @EJB
    OfferCompanyService offerCompanyService;

    public OfferCompanyFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer offerCompanyId = (Integer) context.getSessionMap().get("offerCompanyDTOSelected");
                if (offerCompanyDTO == null) {
                    offerCompanyDTO = offerCompanyService.findOfferCompany(offerCompanyId, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void reset() {
        setOfferCompanyDTO(null);
    }

    public String exit() {
        try {
            exit("../offerCompany/offerCompanyList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "exit");
            return null;
        }
    }

    @Override
    public void load() {

    }

    @Override
    public String save() {
        if (getOfferCompanyDTO() != null) {
            setOfferCompanyDTO(offerCompanyService.addOfferCompany(getOfferCompanyDTO(), getUserData().getUser()));

            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                context.getSessionMap().put("offerCompanyDTOSelected", offerCompanyDTO);
                exit("../offerCompany/offerCompanyList.xhtml");
                return "";
            } catch (Exception e) {
                saveError(e, "OfferCompanyFormBean", "save");
                return null;
            }
        }

        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OfferCompanyDTO getOfferCompanyDTO() {

        if (offerCompanyDTO == null) {
            offerCompanyDTO = new OfferCompanyDTO();
        }
        return offerCompanyDTO;
    }

    /**
     * @param offerCompanyDTO the offerCompanyDTO to set
     */
    public void setOfferCompanyDTO(OfferCompanyDTO offerCompanyDTO) {
        this.offerCompanyDTO = offerCompanyDTO;
    }

}
