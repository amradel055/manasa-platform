/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "gl_year")
@XmlRootElement
public class GlYear extends BaseEntity{

    @Column(name = "year")
    private Integer year;
    @Column(name = "date_from")
    @Temporal(TemporalType.DATE)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.DATE)
    private Date dateTo;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "openning")
    private Integer openning;
    @Column(name = "is_default")
    private Boolean isDefault;
     @Column(name = "company_type")
    private Integer companyType;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "year")
    private Collection<CloseAndSaveMonth> closeAndSaveMonthCollection;
    
    
    @OneToMany(mappedBy = "yearId")
    private Collection<TobyUserYear> tobyUserYearCollection;

    public GlYear() {
    }

    public GlYear(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOpenning() {
        return openning;
    }

    public void setOpenning(Integer openning) {
        this.openning = openning;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    @XmlTransient
    public Collection<CloseAndSaveMonth> getCloseAndSaveMonthCollection() {
        return closeAndSaveMonthCollection;
    }

    public void setCloseAndSaveMonthCollection(Collection<CloseAndSaveMonth> closeAndSaveMonthCollection) {
        this.closeAndSaveMonthCollection = closeAndSaveMonthCollection;
    }
    
    

    @XmlTransient
    public Collection<TobyUserYear> getTobyUserYearCollection() {
        return tobyUserYearCollection;
    }

    public void setTobyUserYearCollection(Collection<TobyUserYear> tobyUserYearCollection) {
        this.tobyUserYearCollection = tobyUserYearCollection;
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
        if (!(object instanceof GlYear)) {
            return false;
        }
        GlYear other = (GlYear) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.GlYear[ id=" + id + " ]";
    }

    /**
     * @return the companyType
     */
    public Integer getCompanyType() {
        return companyType;
    }

    /**
     * @param companyType the companyType to set
     */
    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

}
