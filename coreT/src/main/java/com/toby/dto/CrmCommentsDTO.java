/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;


/**
 *
 * @author User
 */

public class CrmCommentsDTO extends BaseEntityDTO {
    private Integer interestedProductId;
    private Integer organizationSiteId;
    private String content;
    private Integer approved;
    private Integer parent;

    public CrmCommentsDTO() {
    }

    public CrmCommentsDTO(Integer id) {
        this.id = id;
    }

    public Integer getInterestedProductId() {
        return interestedProductId;
    }

    public void setInterestedProductId(Integer interestedProductId) {
        this.interestedProductId = interestedProductId;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
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
        if (!(object instanceof CrmCommentsDTO)) {
            return false;
        }
        CrmCommentsDTO other = (CrmCommentsDTO) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdfsdfs.CrmComments[ id=" + id + " ]";
    }
    
}
