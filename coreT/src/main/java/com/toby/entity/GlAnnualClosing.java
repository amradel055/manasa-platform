/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "gl_annual_closing")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlAnnualClosing.findAll", query = "SELECT g FROM GlAnnualClosing g")
    , @NamedQuery(name = "GlAnnualClosing.findById", query = "SELECT g FROM GlAnnualClosing g WHERE g.id = :id")
    , @NamedQuery(name = "GlAnnualClosing.findByCreationDate", query = "SELECT g FROM GlAnnualClosing g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "GlAnnualClosing.findByModificationDate", query = "SELECT g FROM GlAnnualClosing g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "GlAnnualClosing.findByRatio", query = "SELECT g FROM GlAnnualClosing g WHERE g.ratio = :ratio")})
public class GlAnnualClosing extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ratio")
    private BigDecimal ratio;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "gl_account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount glAccountId;
    @JoinColumn(name = "gl_year_id", referencedColumnName = "id")
    @ManyToOne
    private GlYear glYearId;

    public GlAnnualClosing() {
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public GlAccount getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(GlAccount glAccountId) {
        this.glAccountId = glAccountId;
    }

    public GlYear getGlYearId() {
        return glYearId;
    }

    public void setGlYearId(GlYear glYearId) {
        this.glYearId = glYearId;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalDetails)) {
            return false;
        }
        GeneralJournalDetails other = (GeneralJournalDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
    }

}
