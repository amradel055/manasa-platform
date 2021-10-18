package com.igma.OfferText;

import com.toby.businessservice.OfferTextService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.OfferTextDTO;
import com.toby.toby.BaseFormBean;
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
@Named(value = "offerTextFormBean")
@ViewScoped
public class OfferTextFormBean extends BaseFormBean {

    private OfferTextDTO offerTextDTO;
    private List<InvInventoryDTO> invInventoryDTOList = new ArrayList<>();
    private InvInventoryDTOConverter invInventoryDTOConverter;

    @EJB
    OfferTextService offerTextService;
    @EJB
    private AutoCompleteService autoCompleteService;

    public OfferTextFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {

        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer offerTextId = (Integer) getContext().getSessionMap().get("offerTextDTOSelected");
                offerTextDTO = offerTextService.findOfferText(offerTextId, null);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void reset() {
        setOfferTextDTO(null);
    }

    public String exit() {
        try {
            exit("../offerText/offerTextList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "exit");
            return null;
        }
    }

    @Override
    public void load() {

    }

    public List<InvInventoryDTO> completeGallaryDTO(String query) {
        setInvInventoryDTOList(autoCompleteService.getGallaryByBranchId(getUserData().getUserBranch().getId(), query));
        setInvInventoryDTOConverter(new InvInventoryDTOConverter(getInvInventoryDTOList()));
        return getInvInventoryDTOList();
    }

    @Override
    public String save() {

        setOfferTextDTO(offerTextService.addOfferText(getOfferTextDTO(), getUserData().getUser()));
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "save");
            context.getSessionMap().put("offerTextDTOSelected", offerTextDTO);
            exit("../offerText/offerTextList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "OfferTextFormBean", "save");
            return null;
        }

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OfferTextDTO getOfferTextDTO() {

        if (offerTextDTO == null) {
            offerTextDTO = new OfferTextDTO();
        }
        return offerTextDTO;
    }

    /**
     * @param offerTextDTO the offerTextDTO to set
     */
    public void setOfferTextDTO(OfferTextDTO offerTextDTO) {
        this.offerTextDTO = offerTextDTO;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

}
