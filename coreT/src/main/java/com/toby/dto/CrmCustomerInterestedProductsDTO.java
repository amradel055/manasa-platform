/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.CrmCustomerInterestedProducts;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author User
 */
public class CrmCustomerInterestedProductsDTO extends BaseEntityDTO {

    private Integer interestedDegree;
    private Integer interestedDegreeBak;
    private Date askDate;
    private InvItemDTO itemId;
    private InvItemDTO itemIdBak;
    private InvOrganizationSiteDTO organizationSiteId;

    private Integer numOfCustomer;
    private BigDecimal percentageOfCustomerSatisfaction;
    private InstProjectGuideDTO projectId;
    private InstProjectGuideDTO projectIdBak;

    public CrmCustomerInterestedProductsDTO() {
    }

    public CrmCustomerInterestedProductsDTO(Integer id) {
        this.id = id;
    }

    public Integer getInterestedDegree() {
        return interestedDegree;
    }

    public void setInterestedDegree(Integer interestedDegree) {
        this.interestedDegree = interestedDegree;
    }

    public Date getAskDate() {
        return askDate;
    }

    public void setAskDate(Date askDate) {
        this.askDate = askDate;
    }

    public InvItemDTO getItemId() {
        return itemId;
    }

    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
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
        if (!(object instanceof CrmCustomerInterestedProducts)) {
            return false;
        }
        CrmCustomerInterestedProducts other = (CrmCustomerInterestedProducts) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdfsdfs.CrmCustomerInterestedProducts[ id=" + id + " ]";
    }

    /**
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the itemIdBak
     */
    public InvItemDTO getItemIdBak() {
        return itemIdBak;
    }

    /**
     * @param itemIdBak the itemIdBak to set
     */
    public void setItemIdBak(InvItemDTO itemIdBak) {
        this.itemIdBak = itemIdBak;
    }

    /**
     * @return the interestedDegreeBak
     */
    public Integer getInterestedDegreeBak() {
        return interestedDegreeBak;
    }

    /**
     * @param interestedDegreeBak the interestedDegreeBak to set
     */
    public void setInterestedDegreeBak(Integer interestedDegreeBak) {
        this.interestedDegreeBak = interestedDegreeBak;
    }

    /**
     * @return the numOfCustomer
     */
    public Integer getNumOfCustomer() {
        return numOfCustomer;
    }

    /**
     * @param numOfCustomer the numOfCustomer to set
     */
    public void setNumOfCustomer(Integer numOfCustomer) {
        this.numOfCustomer = numOfCustomer;
    }

    /**
     * @return the percentageOfCustomerSatisfaction
     */
    public BigDecimal getPercentageOfCustomerSatisfaction() {
        return percentageOfCustomerSatisfaction;
    }

    /**
     * @param percentageOfCustomerSatisfaction the
     * percentageOfCustomerSatisfaction to set
     */
    public void setPercentageOfCustomerSatisfaction(BigDecimal percentageOfCustomerSatisfaction) {
        this.percentageOfCustomerSatisfaction = percentageOfCustomerSatisfaction;
    }

    /**
     * @return the projectId
     */
    public InstProjectGuideDTO getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(InstProjectGuideDTO projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the projectIdBak
     */
    public InstProjectGuideDTO getProjectIdBak() {
        return projectIdBak;
    }

    /**
     * @param projectIdBak the projectIdBak to set
     */
    public void setProjectIdBak(InstProjectGuideDTO projectIdBak) {
        this.projectIdBak = projectIdBak;
    }

}
