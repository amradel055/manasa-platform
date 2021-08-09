/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InstProjectGuideDTO;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class ProjectAccountSearchBean {

    private Integer branchId;
    private InstProjectGuideDTO fromInstProjectGuideDTO;
    private InstProjectGuideDTO toInstProjectGuideDTO;
    private Date dateFrom;
    private Date dateTo;
    private Boolean detailed;
    private BigDecimal totle;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the fromInstProjectGuideDTO
     */
    public InstProjectGuideDTO getFromInstProjectGuideDTO() {
        return fromInstProjectGuideDTO;
    }

    /**
     * @param fromInstProjectGuideDTO the fromInstProjectGuideDTO to set
     */
    public void setFromInstProjectGuideDTO(InstProjectGuideDTO fromInstProjectGuideDTO) {
        this.fromInstProjectGuideDTO = fromInstProjectGuideDTO;
    }

    /**
     * @return the toInstProjectGuideDTO
     */
    public InstProjectGuideDTO getToInstProjectGuideDTO() {
        return toInstProjectGuideDTO;
    }

    /**
     * @param toInstProjectGuideDTO the toInstProjectGuideDTO to set
     */
    public void setToInstProjectGuideDTO(InstProjectGuideDTO toInstProjectGuideDTO) {
        this.toInstProjectGuideDTO = toInstProjectGuideDTO;
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
     * @return the detailed
     */
    public Boolean getDetailed() {
        if (detailed == null) {
            detailed = Boolean.FALSE;
        }
        return detailed;
    }

    /**
     * @param detailed the detailed to set
     */
    public void setDetailed(Boolean detailed) {
        this.detailed = detailed;
    }

    /**
     * @return the totle
     */
    public BigDecimal getTotle() {
        if (totle == null) {
            totle = BigDecimal.ZERO;
        }
        return totle;
    }

    /**
     * @param totle the totle to set
     */
    public void setTotle(BigDecimal totle) {
        this.totle = totle;
    }

}
