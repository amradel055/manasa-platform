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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "igma_error")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IgmaError.findAll", query = "SELECT i FROM IgmaError i")
    , @NamedQuery(name = "IgmaError.findById", query = "SELECT i FROM IgmaError i WHERE i.id = :id")
    , @NamedQuery(name = "IgmaError.findByError", query = "SELECT i FROM IgmaError i WHERE i.error = :error")
    , @NamedQuery(name = "IgmaError.findByMethod", query = "SELECT i FROM IgmaError i WHERE i.method = :method")
    , @NamedQuery(name = "IgmaError.findByCreationDate", query = "SELECT i FROM IgmaError i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "IgmaError.findByModificationDate", query = "SELECT i FROM IgmaError i WHERE i.modificationDate = :modificationDate")})
public class IgmaError  extends BaseEntity {

    @Size(max = 450)
    @Column(name = "error")
    private String error;
    @Size(max = 45)
    @Column(name = "class_name")
    private String className;
    @Size(max = 45)
    @Column(name = "method")
    private String method;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public IgmaError() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof IgmaError)) {
            return false;
        }
        IgmaError other = (IgmaError) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.IgmaError[ id=" + id + " ]";
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
}
