/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proSendCloth;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "proSendClothFormDTOMB")
@ViewScoped
public class ProSendClothFormDTOMB extends BaseFormBean {

    private Integer serialSelected;

    private InvInvoiceDTO invInvoiceDTO;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    @Override
    @PostConstruct
    public void init() {
        try {

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    public void search() {
        setInvInvoiceDTO(invPurchaseInvoiceService.findInvoiceBySerial(serialSelected));
    }

    @Override
    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() != null) {
            invPurchaseInvoiceService.updateInvoiceForsendCloth(getInvInvoiceDTO().getId(), (getInvInvoiceDTO().getSendCloth() ? 1 : 0));
        }
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {

        return "";
    }

    /**
     * @return the serialSelected
     */
    public Integer getSerialSelected() {
        return serialSelected;
    }

    /**
     * @param serialSelected the serialSelected to set
     */
    public void setSerialSelected(Integer serialSelected) {
        this.serialSelected = serialSelected;
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

}
