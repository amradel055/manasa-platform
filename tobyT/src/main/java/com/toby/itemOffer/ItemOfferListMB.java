package com.toby.itemOffer;

import com.toby.businessservice.ItemOfferService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.BaseListBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;

/**
 * @author khaled
 */
@Named(value = "itemOfferListMB")
@ViewScoped
public class ItemOfferListMB extends BaseListBean {

    private List<InvInvoiceDTO> itemOfferDTOs;
    private InvInvoiceDTO itemOfferDTOSelected;

    @EJB
    private ItemOfferService itemOfferService;

    @Override
    @PostConstruct
    public void init() {
        setItemOfferDTOs(itemOfferService.findItemOfferByBranch(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        if (getItemOfferDTOSelected() != null) {
            try {
                itemOfferService.deleteItemOffer(getItemOfferDTOSelected(), getUserData().getUser());
                getItemOfferDTOs().remove(getItemOfferDTOSelected());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_KEY_ERROR")));
            }
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "AddItemOffer");
        exit("../itemOffer/itemOfferForm.xhtml");
        return "";
    }

    @Override
    public String goToEdit() {
        if (getItemOfferDTOSelected() != null && getItemOfferDTOSelected().getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("itemOfferSelected", getItemOfferDTOSelected().getId());
            context.getSessionMap().put("ScreenMode", "EditItemOffer");
            exit("../itemOffer/itemOfferForm.xhtml");
        }
        return "";
    }

    @Override
    public void load() {

    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @return the itemOfferDTOs
     */
    public List<InvInvoiceDTO> getItemOfferDTOs() {
        return itemOfferDTOs;
    }

    /**
     * @param itemOfferDTOs the itemOfferDTOs to set
     */
    public void setItemOfferDTOs(List<InvInvoiceDTO> itemOfferDTOs) {
        this.itemOfferDTOs = itemOfferDTOs;
    }

    /**
     * @return the itemOfferDTOSelected
     */
    public InvInvoiceDTO getItemOfferDTOSelected() {
        return itemOfferDTOSelected;
    }

    /**
     * @param itemOfferDTOSelected the itemOfferDTOSelected to set
     */
    public void setItemOfferDTOSelected(InvInvoiceDTO itemOfferDTOSelected) {
        this.itemOfferDTOSelected = itemOfferDTOSelected;
    }

}
