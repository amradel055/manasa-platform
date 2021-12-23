/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author hq004
 */
@MappedSuperclass
public class BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    protected Integer id;

    @Transient
    private Boolean markEdit;

    @Transient
    private String msg;
    
    @Transient
    private Boolean markDisable;

    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne()
    private TobyCompany companyId;

    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne()
    private TobyUser createdBy;

    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne()
    private TobyUser modifiedBy;

    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TobyCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the createdBy
     */
    public TobyUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the modifiedBy
     */
    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(TobyUser modifiedBy) {
        if (createdBy == null) {
            this.createdBy = modifiedBy;
        }
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        if (id == null) {
            this.creationDate = modificationDate;
        }
        this.modificationDate = modificationDate;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournal)) {
            return false;
        }
        GeneralJournal other = (GeneralJournal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the markDisable
     */
    public Boolean getMarkDisable() {
        return markDisable;
    }

    /**
     * @param markDisable the markDisable to set
     */
    public void setMarkDisable(Boolean markDisable) {
        this.markDisable = markDisable;
    }
}
