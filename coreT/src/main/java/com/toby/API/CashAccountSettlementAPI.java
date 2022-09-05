/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.GlBankDTO;
import com.toby.dto.GlYearDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class CashAccountSettlementAPI extends BaseEntityDTOAPI {

    private Date dateFrom;
    private Date dateTo;
    private Integer dataType;
    private GlYearDTO glYearSelected;
    private List<GlBankDTO> glBankDTOListSelected;
    private String glBankIdSelected;

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
     * @return the dataType
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }



    /**
     * @return the glBankIdSelected
     */
    public String getGlBankIdSelected() {
        return glBankIdSelected;
    }

    /**
     * @param glBankIdSelected the glBankIdSelected to set
     */
    public void setGlBankIdSelected(String glBankIdSelected) {
        this.glBankIdSelected = glBankIdSelected;
    }

    /**
     * @return the glYearSelected
     */
    public GlYearDTO getGlYearSelected() {
        return glYearSelected;
    }

    /**
     * @param glYearSelected the glYearSelected to set
     */
    public void setGlYearSelected(GlYearDTO glYearSelected) {
        this.glYearSelected = glYearSelected;
    }

    /**
     * @return the glBankDTOListSelected
     */
    public List<GlBankDTO> getGlBankDTOListSelected() {
        return glBankDTOListSelected;
    }

    /**
     * @param glBankDTOListSelected the glBankDTOListSelected to set
     */
    public void setGlBankDTOListSelected(List<GlBankDTO> glBankDTOListSelected) {
        this.glBankDTOListSelected = glBankDTOListSelected;
    }

}
