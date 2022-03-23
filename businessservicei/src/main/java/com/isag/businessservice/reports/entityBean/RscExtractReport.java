/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mostafa
 */
public class RscExtractReport {

    private String contractorName;
    private Integer serial;
    private Integer id;
    private BigDecimal extractPrice;
    private Date date;
    private String remarks;
    private List<RscRestrictionQuantitiesDetailDTO> restrictionQuantitiesDetailDTOList; 

    /**
     * @return the contractorName
     */
    public String getContractorName() {
        return contractorName;
    }

    /**
     * @param contractorName the contractorName to set
     */
    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    /**
     * @return the extractPrice
     */
    public BigDecimal getExtractPrice() {
        return extractPrice;
    }

    /**
     * @param extractPrice the extractPrice to set
     */
    public void setExtractPrice(BigDecimal extractPrice) {
        this.extractPrice = extractPrice;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * @return the restrictionQuantitiesDetailDTOList
     */
    public List<RscRestrictionQuantitiesDetailDTO> getRestrictionQuantitiesDetailDTOList() {
        return restrictionQuantitiesDetailDTOList;
    }

    /**
     * @param restrictionQuantitiesDetailDTOList the restrictionQuantitiesDetailDTOList to set
     */
    public void setRestrictionQuantitiesDetailDTOList(List<RscRestrictionQuantitiesDetailDTO> restrictionQuantitiesDetailDTOList) {
        this.restrictionQuantitiesDetailDTOList = restrictionQuantitiesDetailDTOList;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
