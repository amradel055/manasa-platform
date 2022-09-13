/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class ItemsDataAPI extends BaseEntityDTOAPI {

    private List<InvInventoryDTO> invInventoryDTOList;
    private List<ProGroupDTO> proGroupDTOList;
    private Integer invType;
    private Integer invoiceType;
    private Date dateFrom;
    private Date dateTo;
    private String itemCode;

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
     * @return the invType
     */
    public Integer getInvType() {
        return invType;
    }

    /**
     * @param invType the invType to set
     */
    public void setInvType(Integer invType) {
        this.invType = invType;
    }

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the proGroupDTOList
     */
    public List<ProGroupDTO> getProGroupDTOList() {
        return proGroupDTOList;
    }

    /**
     * @param proGroupDTOList the proGroupDTOList to set
     */
    public void setProGroupDTOList(List<ProGroupDTO> proGroupDTOList) {
        this.proGroupDTOList = proGroupDTOList;
    }

}
