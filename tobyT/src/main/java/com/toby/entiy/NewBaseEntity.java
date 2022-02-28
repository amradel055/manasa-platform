/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import java.util.Date;

/**
 *
 * @author WIN7
 */
public class NewBaseEntity {

    private Integer id;
    private Boolean markEdit;
    private TobyCompanyEntity companyId;
    private TobyUserEntity createdBy;
    private TobyUserEntity modifiedBy;
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

    public TobyCompanyEntity getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompanyEntity companyId) {
        this.companyId = companyId;
    }

    public TobyUserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TobyUserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public TobyUserEntity getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUserEntity modifiedBy) {
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
