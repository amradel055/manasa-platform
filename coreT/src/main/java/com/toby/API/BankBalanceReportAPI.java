/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class BankBalanceReportAPI extends BaseEntityDTOAPI {

    private Date dateFrom;
    private Date dateTo;
    private List<GlBankDTO> glBankDTOList;
    private List<InvInventoryDTO> invInventoryDTOList;
    private InvInventoryDTO gallarySellected;
    private Integer invType;


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
     * @return the glBankDTOList
     */
    public List<GlBankDTO> getGlBankDTOList() {
        return glBankDTOList;
    }

    /**
     * @param glBankDTOList the glBankDTOList to set
     */
    public void setGlBankDTOList(List<GlBankDTO> glBankDTOList) {
        this.glBankDTOList = glBankDTOList;
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
     * @return the gallarySellected
     */
    public InvInventoryDTO getGallarySellected() {
        return gallarySellected;
    }

    /**
     * @param gallarySellected the gallarySellected to set
     */
    public void setGallarySellected(InvInventoryDTO gallarySellected) {
        this.gallarySellected = gallarySellected;
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

}
