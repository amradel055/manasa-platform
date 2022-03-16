package com.toby.itemOffer;

import com.toby.businessservice.ItemOfferDetailService;
import com.toby.businessservice.ItemOfferService;
import com.toby.dto.InvDetailDTO;
import com.toby.toby.InventoryBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "itemOfferFormMB")
@ViewScoped
public class ItemOfferFormMB extends InventoryBasicDataForm {

    @EJB
    ItemOfferService itemOfferService;
    @EJB
    ItemOfferDetailService itemOfferDetailService;

    @Override
    @PostConstruct
    public void init() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditItemOffer")) {
                Integer id = (Integer) getContext().getSessionMap().get("itemOfferSelected");
                setInvInvoiceDTO(itemOfferService.findItemOfferByItemId(id));
            }
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "init");
        }
    }

    @Override
    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "load");
        }
    }

    @Override
    public String save() {
        setInvInvoiceDTO(itemOfferService.addItemOffer(getInvInvoiceDTO(), getUserData().getUser()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteDetail(InvDetailDTO itemOfferDetailDTO) {
        try {
            if (itemOfferDetailDTO.getId() != null) {
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(itemOfferDetailDTO);
            }
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(itemOfferDetailDTO);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
