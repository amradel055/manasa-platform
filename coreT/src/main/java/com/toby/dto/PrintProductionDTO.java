package com.toby.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintProductionDTO {

    private String Msg;
    private ProProductionStagesDTO fromStatus;
    private ProProductionStagesDTO toStatus;
    private InvInvoiceDTO fromInvoice;
    private Integer fromInvInvoice;
    private InvInvoiceDTO toInvoice;
    private Integer toInvInvoice;
    private Date toDate;
    private Date fromDate;
    private TobyUserDTO fromUser;
    private TobyUserDTO toUser;
    private InvInventoryDTO gallaryFrom;
    private List<InvInventoryDTO> gallaryListSelected;
    private InvInventoryDTO gallaryTo;
    private Integer invoiceStatus;

    public InvInvoiceDTO getFromInvoice() {
        return fromInvoice;
    }

    public void setFromInvoice(InvInvoiceDTO fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public InvInvoiceDTO getToInvoice() {
        return toInvoice;
    }

    public void setToInvoice(InvInvoiceDTO toInvoice) {
        this.toInvoice = toInvoice;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the fromUser
     */
    public TobyUserDTO getFromUser() {
        return fromUser;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(TobyUserDTO fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return the toUser
     */
    public TobyUserDTO getToUser() {
        return toUser;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUser(TobyUserDTO toUser) {
        this.toUser = toUser;
    }

    /**
     * @return the Msg
     */
    public String getMsg() {
        return Msg;
    }

    /**
     * @param Msg the Msg to set
     */
    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    /**
     * @return the fromInvInvoice
     */
    public Integer getFromInvInvoice() {
        return fromInvInvoice;
    }

    /**
     * @param fromInvInvoice the fromInvInvoice to set
     */
    public void setFromInvInvoice(Integer fromInvInvoice) {
        this.fromInvInvoice = fromInvInvoice;
    }

    /**
     * @return the toInvInvoice
     */
    public Integer getToInvInvoice() {
        return toInvInvoice;
    }

    /**
     * @param toInvInvoice the toInvInvoice to set
     */
    public void setToInvInvoice(Integer toInvInvoice) {
        this.toInvInvoice = toInvInvoice;
    }

    /**
     * @return the gallaryFrom
     */
    public InvInventoryDTO getGallaryFrom() {
        return gallaryFrom;
    }

    /**
     * @param gallaryFrom the gallaryFrom to set
     */
    public void setGallaryFrom(InvInventoryDTO gallaryFrom) {
        this.gallaryFrom = gallaryFrom;
    }

    /**
     * @return the gallaryTo
     */
    public InvInventoryDTO getGallaryTo() {
        return gallaryTo;
    }

    /**
     * @param gallaryTo the gallaryTo to set
     */
    public void setGallaryTo(InvInventoryDTO gallaryTo) {
        this.gallaryTo = gallaryTo;
    }

    /**
     * @return the invoiceStatus
     */
    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * @param invoiceStatus the invoiceStatus to set
     */
    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * @return the fromStatus
     */
    public ProProductionStagesDTO getFromStatus() {
        return fromStatus;
    }

    /**
     * @param fromStatus the fromStatus to set
     */
    public void setFromStatus(ProProductionStagesDTO fromStatus) {
        this.fromStatus = fromStatus;
    }

    /**
     * @return the toStatus
     */
    public ProProductionStagesDTO getToStatus() {
        return toStatus;
    }

    /**
     * @param toStatus the toStatus to set
     */
    public void setToStatus(ProProductionStagesDTO toStatus) {
        this.toStatus = toStatus;
    }

    /**
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {

        if (gallaryListSelected == null) {
            gallaryListSelected = new ArrayList<>();
        }
        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

}
