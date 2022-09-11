/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import java.math.BigDecimal;
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

    private Boolean notZeroValue;
    private Boolean zeroValue;
    private Boolean remain;
    private BigDecimal remainValue;

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

    /**
     * @return the notZeroValue
     */
    public Boolean getNotZeroValue() {
        if (notZeroValue == null) {
            notZeroValue = Boolean.FALSE;
        }
        return notZeroValue;
    }

    /**
     * @param notZeroValue the notZeroValue to set
     */
    public void setNotZeroValue(Boolean notZeroValue) {
        this.notZeroValue = notZeroValue;
    }

    /**
     * @return the zeroValue
     */
    public Boolean getZeroValue() {
        if (zeroValue == null) {
            zeroValue = Boolean.FALSE;
        }
        return zeroValue;
    }

    /**
     * @param zeroValue the zeroValue to set
     */
    public void setZeroValue(Boolean zeroValue) {
        this.zeroValue = zeroValue;
    }

    /**
     * @return the remain
     */
    public Boolean getRemain() {
        if (remain == null) {
            remain = Boolean.FALSE;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(Boolean remain) {
        this.remain = remain;
    }

    /**
     * @return the remainValue
     */
    public BigDecimal getRemainValue() {
        if (remainValue == null) {
            remainValue = BigDecimal.ZERO;
        }
        return remainValue;
    }

    /**
     * @param remainValue the remainValue to set
     */
    public void setRemainValue(BigDecimal remainValue) {
        this.remainValue = remainValue;
    }

}
