package com.igma.report;

import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.Basic;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "invPurchaseInvoiceDeliveryPalace")
@ViewScoped
public class InvPurchaseInvoiceDeliveryPalace extends Basic {

    private Integer serial;
    private List<InvInventoryDTO> invInventoryDTOList;
    private InvInventoryDTOConverter invInventoryDTOConverter;
    private InvInvoiceDTO invInvoiceDTO;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    InvInventoryService inventoryService;

    @PostConstruct
    public void init() {
        load();
    }

    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceDeliveryPalace", "init");
        }

    }

    public void search() {
        setInvInvoiceDTO(invPurchaseInvoiceService_1.getInvoiceBySerial(getUserData().getUserBranch().getId(), serial));
        if (getInvInvoiceDTO().getDeliveryPlace() != null) {
           getInvInvoiceDTO().setDeliveryPlace(getInvInvoiceDTO().getDeliveryPlace());
        }
    }

    public String save() {
        invPurchaseInvoiceService_1.saveDeliveryPlace(getInvInvoiceDTO().getId(), getInvInvoiceDTO().getDeliveryPlace().getId());
        return "";

    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            setInvInventoryDTOList(inventoryService.findInventoryByBranch(getUserData().getUserBranch().getId(), getUserData().getUser()));
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }

        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            setInvInvoiceDTO(new InvInvoiceDTO());
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
