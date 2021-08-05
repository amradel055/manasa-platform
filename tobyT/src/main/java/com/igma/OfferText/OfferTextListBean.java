package com.igma.OfferText;

import com.toby.businessservice.OfferTextService;
import com.toby.dto.OfferTextDTO;
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
@Named(value = "offerTextListBean")
@ViewScoped
public class OfferTextListBean extends BaseFormBean {

    private OfferTextDTO offerTextDTOSelected;

    private List<OfferTextDTO> offerTextDTOList;

    @EJB
    OfferTextService offerTextService;

    public OfferTextListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        offerTextDTOList = offerTextService.getALLOfferTextByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
    }

    @Override
    public void load() {

    }

    public void delete() {
        offerTextService.deleteOfferText(getOfferTextDTOSelected());
        offerTextDTOList.remove(getOfferTextDTOSelected());
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../offerText/offerTextForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "offerTextListBean", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (offerTextDTOSelected != null && offerTextDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("offerTextDTOSelected", offerTextDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../offerText/offerTextForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "offerTextListBean", "goToEdit");
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
     * @return the offerTextDTOSelected
     */
    public OfferTextDTO getOfferTextDTOSelected() {
        return offerTextDTOSelected;
    }

    /**
     * @param offerTextDTOSelected the offerTextDTOSelected to set
     */
    public void setOfferTextDTOSelected(OfferTextDTO offerTextDTOSelected) {
        this.offerTextDTOSelected = offerTextDTOSelected;
    }

    /**
     * @return the offerTextDTOList
     */
    public List<OfferTextDTO> getOfferTextDTOList() {

        if (offerTextDTOList == null | offerTextDTOList.isEmpty()) {
            offerTextDTOList = offerTextService.getALLOfferTextByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
        }

        return offerTextDTOList;
    }

    /**
     * @param offerTextDTOList the offerTextDTOList to set
     */
    public void setOfferTextDTOList(List<OfferTextDTO> offerTextDTOList) {
        this.offerTextDTOList = offerTextDTOList;
    }

}
