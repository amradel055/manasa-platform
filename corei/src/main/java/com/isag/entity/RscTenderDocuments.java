/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "rsc_tender_documents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscTenderDocuments.findAll", query = "SELECT r FROM RscTenderDocuments r")
    , @NamedQuery(name = "RscTenderDocuments.findById", query = "SELECT r FROM RscTenderDocuments r WHERE r.id = :id")
    , @NamedQuery(name = "RscTenderDocuments.findByCreationDate", query = "SELECT r FROM RscTenderDocuments r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscTenderDocuments.findByModificationDate", query = "SELECT r FROM RscTenderDocuments r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscTenderDocuments.findBySerial", query = "SELECT r FROM RscTenderDocuments r WHERE r.serial = :serial")})
public class RscTenderDocuments extends BaseEntity {

    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "documents_id", referencedColumnName = "id")
    @ManyToOne
    private RscDocuments documentsId;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;

    public RscTenderDocuments() {
    }

    public RscTenderDocuments(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public RscDocuments getDocumentsId() {
        return documentsId;
    }

    public void setDocumentsId(RscDocuments documentsId) {
        this.documentsId = documentsId;
    }

    public RscTender getTenderId() {
        return tenderId;
    }

    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
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
        if (!(object instanceof RscTenderDocuments)) {
            return false;
        }
        RscTenderDocuments other = (RscTenderDocuments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTenderDocuments[ id=" + id + " ]";
    }
    
}
