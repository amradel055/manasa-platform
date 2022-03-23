package com.igma.invCreateBarcode;

import com.isag.businessservice.InvGlobalService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.isag.InventoryBasicDataForm;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author khaled
 */
@Named(value = "invCreateBarcodeFormMB")
@ViewScoped
public class InvCreateBarcodeFormMB extends InventoryBasicDataForm {

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    InvGlobalService invGlobalService;

    @Override
    @PostConstruct
    public void init() {
        try {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVCREATEBARCODE);
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "init");

        }
    }

    public void loadInvPurchaseWithSerial() {
        if (getInvInvoiceDTO().getLoadedSerial() != null 
                && getInvInvoiceDTO().getInvInventoryId() != null 
                && getInvInvoiceDTO().getInvInventoryId().getId() != null) {
            setInvInvoiceDTO(invPurchaseInvoiceService_1.findInvPurchaseInvoiceBySerial(getInvInvoiceDTO(), getUserData().getUserBranch().getId()));
            List<InvDetailDTO> list = invGlobalService.AvailableQuantity(getInvInvoiceDTO().getInvPurchaseInvoiceDetailList(), getInvInvoiceDTO());
//            for( InvDetailDTO invDetailDTO : list){
//                invDetailDTO.setQuantity(BigDecimal.ONE);
//            }
            getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(list);
        }
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
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

}
