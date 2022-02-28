package com.toby.proProductionTransaction;

import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionTransactionDetailMB")
@ViewScoped
public class ProProductionTransactionDetailMB extends BaseListBean {

    private InvInvoiceDTO invInvoiceDTO;
    private InvDetailDTO invDetailDTOSelected;
    private ProProductionTransactionDTO proProductionTransactionDTOSelected;

    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;

    public ProProductionTransactionDetailMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        try {
            Integer id = (Integer) context.getSessionMap().get("ProFactoryDeliveryIdSelected");
            getInvInvoiceDTO().setId(id);
            setInvInvoiceDTO(proFactoryDeliveryService.findProFactoryDeliveryById(getInvInvoiceDTO()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public String save() {
        getProProductionTransactionDTOSelected().setInvPurchaseInvoiceId(getInvInvoiceDTO().getInvPurchaseInvoiceId());
        getProProductionTransactionDTOSelected().setProFactoryDeliveryId(getInvInvoiceDTO().getId());
        getProProductionTransactionDTOSelected().setClothNumber(invDetailDTOSelected.getClothNumber());

        proProductionTransactionService.save(proProductionTransactionDTOSelected, 1, getUserData().getUser());
        init();
        return "";

    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
        }
        return invInvoiceDTO;
    }

    /**
     * @param invInvoiceDTO the invInvoiceDTO to set
     */
    public void setInvInvoiceDTO(InvInvoiceDTO invInvoiceDTO) {
        this.invInvoiceDTO = invInvoiceDTO;
    }

    /**
     * @return the invDetailDTOSelected
     */
    public InvDetailDTO getInvDetailDTOSelected() {
        return invDetailDTOSelected;
    }

    /**
     * @param invDetailDTOSelected the invDetailDTOSelected to set
     */
    public void setInvDetailDTOSelected(InvDetailDTO invDetailDTOSelected) {
        this.invDetailDTOSelected = invDetailDTOSelected;
    }

    public ProProductionTransactionDTO getProProductionTransactionDTOSelected() {
        if (proProductionTransactionDTOSelected == null) {
            proProductionTransactionDTOSelected = new ProProductionTransactionDTO();
        }
        return proProductionTransactionDTOSelected;
    }

    public void setProProductionTransactionDTOSelected(ProProductionTransactionDTO proProductionTransactionDTOSelected) {
        this.proProductionTransactionDTOSelected = proProductionTransactionDTOSelected;
    }

}
