/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "offer_text")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OfferText.findAll", query = "SELECT o FROM OfferText o")
    , @NamedQuery(name = "OfferText.findById", query = "SELECT o FROM OfferText o WHERE o.id = :id")
    , @NamedQuery(name = "OfferText.findByText", query = "SELECT o FROM OfferText o WHERE o.text = :text")
    , @NamedQuery(name = "OfferText.findByDateFrom", query = "SELECT o FROM OfferText o WHERE o.dateFrom = :dateFrom")
    , @NamedQuery(name = "OfferText.findByDateTo", query = "SELECT o FROM OfferText o WHERE o.dateTo = :dateTo")
    , @NamedQuery(name = "OfferText.findByCreationDate", query = "SELECT o FROM OfferText o WHERE o.creationDate = :creationDate")
    , @NamedQuery(name = "OfferText.findByModificationDate", query = "SELECT o FROM OfferText o WHERE o.modificationDate = :modificationDate")})
public class OfferText extends BaseEntity {

    @Column(name = "text")
    private String text;
    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "galary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory galaryId;

    public OfferText() {
    }

    public OfferText(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getGalaryId() {
        return galaryId;
    }

    public void setGalaryId(InvInventory galaryId) {
        this.galaryId = galaryId;
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
        if (!(object instanceof OfferText)) {
            return false;
        }
        OfferText other = (OfferText) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.OfferText[ id=" + id + " ]";
    }

}
