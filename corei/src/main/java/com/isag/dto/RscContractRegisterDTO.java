/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscContractRegisterDTO extends BaseEntityDTO {

    private Integer status;
    private String statusText;
    private Date date;
    private Integer serial;
    private String documentNumber;
    private Date initialReceivedDate;
    private Date finallReceivedDate;
    private BigDecimal contractTotal;
    private BigDecimal initialValue;
    private BigDecimal initialPercentage;
    private InvOrganizationSiteDTO siteId;
    private String remarks;
    private RscQotationPriceTenderDTO rscQotationPriceTenderId;
    private RscTenderDTO rscTenderDTO;
    private BigDecimal totalquantity;
    private BigDecimal totalprice;
    private BigDecimal nettotal;
    private RscTenderDTO tenderId;

    private List<RscContractRegisterDetailWorkUnitDTO> contractRegisterDetailList;
    private List<RscContractRegisterDetailWorkUnitDTO> rscContractRegisterDetailWorkUnitDeletedList;
    private Boolean active;

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return the documentNumber
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the initialReceivedDate
     */
    public Date getInitialReceivedDate() {
        return initialReceivedDate;
    }

    /**
     * @param initialReceivedDate the initialReceivedDate to set
     */
    public void setInitialReceivedDate(Date initialReceivedDate) {
        this.initialReceivedDate = initialReceivedDate;
    }

    /**
     * @return the finallReceivedDate
     */
    public Date getFinallReceivedDate() {
        return finallReceivedDate;
    }

    /**
     * @param finallReceivedDate the finallReceivedDate to set
     */
    public void setFinallReceivedDate(Date finallReceivedDate) {
        this.finallReceivedDate = finallReceivedDate;
    }

    /**
     * @return the contractTotal
     */
    public BigDecimal getContractTotal() {
        return contractTotal;
    }

    /**
     * @param contractTotal the contractTotal to set
     */
    public void setContractTotal(BigDecimal contractTotal) {
        this.contractTotal = contractTotal;
    }

    /**
     * @return the initialValue
     */
    public BigDecimal getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * @return the initialPercentage
     */
    public BigDecimal getInitialPercentage() {
        return initialPercentage;
    }

    /**
     * @param initialPercentage the initialPercentage to set
     */
    public void setInitialPercentage(BigDecimal initialPercentage) {
        this.initialPercentage = initialPercentage;
    }

    /**
     * @return the siteId
     */
    public InvOrganizationSiteDTO getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(InvOrganizationSiteDTO siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the rscQotationPriceTenderId
     */
    public RscQotationPriceTenderDTO getRscQotationPriceTenderId() {

        return rscQotationPriceTenderId;
    }

    /**
     * @param rscQotationPriceTenderId the rscQotationPriceTenderId to set
     */
    public void setRscQotationPriceTenderId(RscQotationPriceTenderDTO rscQotationPriceTenderId) {
        this.rscQotationPriceTenderId = rscQotationPriceTenderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscPriceTenderDTO)) {
            return false;
        }
        RscPriceTenderDTO other = (RscPriceTenderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + " ";
    }

    /**
     * @return the rscTenderDTO
     */
    public RscTenderDTO getRscTenderDTO() {
        return rscTenderDTO;
    }

    /**
     * @param rscTenderDTO the rscTenderDTO to set
     */
    public void setRscTenderDTO(RscTenderDTO rscTenderDTO) {
        this.rscTenderDTO = rscTenderDTO;
    }

    /**
     * @return the totalquantity
     */
    public BigDecimal getTotalquantity() {
        return totalquantity;
    }

    /**
     * @param totalquantity the totalquantity to set
     */
    public void setTotalquantity(BigDecimal totalquantity) {
        this.totalquantity = totalquantity;
    }

    /**
     * @return the totalprice
     */
    public BigDecimal getTotalprice() {
        return totalprice;
    }

    /**
     * @param totalprice the totalprice to set
     */
    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * @return the nettotal
     */
    public BigDecimal getNettotal() {
        return nettotal;
    }

    /**
     * @param nettotal the nettotal to set
     */
    public void setNettotal(BigDecimal nettotal) {
        this.nettotal = nettotal;
    }

    /**
     * @return the contractRegisterDetailList
     */
    public List<RscContractRegisterDetailWorkUnitDTO> getContractRegisterDetailList() {
        if (contractRegisterDetailList == null || contractRegisterDetailList.isEmpty()) {

            contractRegisterDetailList = new ArrayList<>();

        }
        return contractRegisterDetailList;
    }

    /**
     * @param contractRegisterDetailList the contractRegisterDetailList to set
     */
    public void setContractRegisterDetailList(List<RscContractRegisterDetailWorkUnitDTO> contractRegisterDetailList) {
        this.contractRegisterDetailList = contractRegisterDetailList;
    }

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
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
     * @return the tenderId
     */
    public RscTenderDTO getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTenderDTO tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the rscContractRegisterDetailWorkUnitDeletedList
     */
    public List<RscContractRegisterDetailWorkUnitDTO> getRscContractRegisterDetailWorkUnitDeletedList() {
        if(rscContractRegisterDetailWorkUnitDeletedList == null){
            rscContractRegisterDetailWorkUnitDeletedList = new ArrayList<>();
        }
        return rscContractRegisterDetailWorkUnitDeletedList;
    }

    /**
     * @param rscContractRegisterDetailWorkUnitDeletedList the rscContractRegisterDetailWorkUnitDeletedList to set
     */
    public void setRscContractRegisterDetailWorkUnitDeletedList(List<RscContractRegisterDetailWorkUnitDTO> rscContractRegisterDetailWorkUnitDeletedList) {
        this.rscContractRegisterDetailWorkUnitDeletedList = rscContractRegisterDetailWorkUnitDeletedList;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

}
