/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.InvInventoryDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class SummitionItemAPI extends BaseEntityDTOAPI {

    private Date dateFrom;
    private Date dateTo;
    private Integer gallaryId;
    private Integer groupId;
    private Integer invoiceType;
    private Integer organizationType;
    private List<InvInventoryDTO> gallaryList ;
    private int frompaymentType;
    private int topaymentType;
    

    private List<SymbolDTOAPI> symbolDTOAPIList;

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
     * @return the gallaryId
     */
    public Integer getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(Integer gallaryId) {
        this.gallaryId = gallaryId;
    }

//    /**
//     * @return the groupId
//     */
//    public Integer getGroupId() {
//        return groupId;
//    }
//
//    /**
//     * @param groupId the groupId to set
//     */
//    public void setGroupId(Integer groupId) {
//        this.groupId = groupId;
//    }

    /**
     * @return the symbolDTOAPIList
     */
    public List<SymbolDTOAPI> getSymbolDTOAPIList() {
        return symbolDTOAPIList;
    }

    /**
     * @param symbolDTOAPIList the symbolDTOAPIList to set
     */
    public void setSymbolDTOAPIList(List<SymbolDTOAPI> symbolDTOAPIList) {
        this.symbolDTOAPIList = symbolDTOAPIList;
    }

    /**
     * @return the organizationType
     */
    public Integer getOrganizationType() {
        return organizationType;
    }

    /**
     * @param organizationType the organizationType to set
     */
    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
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
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the frompaymentType
     */
    public int getFrompaymentType() {
        return frompaymentType;
    }

    /**
     * @param frompaymentType the frompaymentType to set
     */
    public void setFrompaymentType(int frompaymentType) {
        this.frompaymentType = frompaymentType;
    }

    /**
     * @return the topaymentType
     */
    public int getTopaymentType() {
        return topaymentType;
    }

    /**
     * @param topaymentType the topaymentType to set
     */
    public void setTopaymentType(int topaymentType) {
        this.topaymentType = topaymentType;
    }

 

}
