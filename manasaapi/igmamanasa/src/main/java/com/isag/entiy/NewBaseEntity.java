/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import java.util.Date;

/**
 *
 * @author WIN7
 */
public class NewBaseEntity {

    private Integer id;
    private Boolean markEdit;
    private IsagCompanyEntity companyId;
    private IsagUserEntity createdBy;
    private IsagUserEntity modifiedBy;
    private Date modificationDate;
    private Date creationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    public IsagCompanyEntity getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompanyEntity companyId) {
        this.companyId = companyId;
    }

    public IsagUserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public IsagUserEntity getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IsagUserEntity modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
