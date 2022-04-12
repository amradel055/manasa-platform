/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m lapan
 */
public class ProfitOfItemsSold {

    private String code;
    private String name;
    private BigDecimal costAverage;
    private BigDecimal totallSell;
    private BigDecimal totallDiscount;
    private BigDecimal totallNet;
    private BigDecimal totallNumber;
    private Date dateFrom;
    private Date dateTo;
    private List<InvInventoryDTO> gallarySelected;
    private List<ProGroupDTO> groupDTOSelected;
    private List<InvItemDTO> itemDTOSelected;
    private InvInventoryDTO gallaryFrom;
    private ProGroupDTO proGroupId;
    private String itemCode;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the costAverage
     */
    public BigDecimal getCostAverage() {
        if (costAverage == null) {
            costAverage = BigDecimal.ZERO;
        }
        return costAverage;
    }

    /**
     * @param costAverage the costAverage to set
     */
    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    /**
     * @return the totallSell
     */
    public BigDecimal getTotallSell() {
        if (totallSell == null) {
            totallSell = BigDecimal.ZERO;
        }
        return totallSell;
    }

    /**
     * @param totallSell the totallSell to set
     */
    public void setTotallSell(BigDecimal totallSell) {
        this.totallSell = totallSell;
    }

    /**
     * @return the totallDiscount
     */
    public BigDecimal getTotallDiscount() {
        if (totallDiscount == null) {
            totallDiscount = BigDecimal.ZERO;
        }
        return totallDiscount;
    }

    /**
     * @param totallDiscount the totallDiscount to set
     */
    public void setTotallDiscount(BigDecimal totallDiscount) {
        this.totallDiscount = totallDiscount;
    }

    /**
     * @return the totallNet
     */
    public BigDecimal getTotallNet() {
        if (totallNet == null) {
            totallNet = BigDecimal.ZERO;
        }
        return totallNet;
    }

    /**
     * @param totallNet the totallNet to set
     */
    public void setTotallNet(BigDecimal totallNet) {
        this.totallNet = totallNet;
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
     * @return the gallaryFrom
     */
    public InvInventoryDTO getGallaryFrom() {
        if (gallaryFrom == null) {
            gallaryFrom = new InvInventoryDTO();
        }
        return gallaryFrom;
    }

    /**
     * @param gallaryFrom the gallaryFrom to set
     */
    public void setGallaryFrom(InvInventoryDTO gallaryFrom) {
        this.gallaryFrom = gallaryFrom;
    }

    /**
     * @return the gallarySelected
     */
    public List<InvInventoryDTO> getGallarySelected() {
        if (gallarySelected == null) {
            gallarySelected = new ArrayList<>();
        }
        return gallarySelected;
    }

    /**
     * @param gallarySelected the gallarySelected to set
     */
    public void setGallarySelected(List<InvInventoryDTO> gallarySelected) {
        this.gallarySelected = gallarySelected;
    }

    /**
     * @return the totallNumber
     */
    public BigDecimal getTotallNumber() {
        if (totallNumber == null) {
            totallNumber = BigDecimal.ZERO;
        }
        return totallNumber;
    }

    /**
     * @param totallNumber the totallNumber to set
     */
    public void setTotallNumber(BigDecimal totallNumber) {
        this.totallNumber = totallNumber;
    }

    /**
     * @return the proGroupId
     */
    public ProGroupDTO getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(ProGroupDTO proGroupId) {
        this.proGroupId = proGroupId;
    }

     /**
     * @return the groupDTOSelected
     */
    public List<ProGroupDTO> getGroupDTOSelected() {
        if (groupDTOSelected == null) {
            groupDTOSelected = new ArrayList<>();
        }
        return groupDTOSelected;
    }

    /**
     * @param groupDTOSelected the groupDTOSelected to set
     */
    public void setGroupDTOSelected(List<ProGroupDTO> groupDTOSelected) {
        this.groupDTOSelected = groupDTOSelected;
    }

    /**
     * @return the itemDTOSelected
     */
    public List<InvItemDTO> getItemDTOSelected() {
        return itemDTOSelected;
    }

    /**
     * @param itemDTOSelected the itemDTOSelected to set
     */
    public void setItemDTOSelected(List<InvItemDTO> itemDTOSelected) {
        this.itemDTOSelected = itemDTOSelected;
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


}
