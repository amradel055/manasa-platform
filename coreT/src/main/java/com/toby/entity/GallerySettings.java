/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author m lapan
 */
@Entity
@Table(name = "gallery_settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GallerySettings.findAll", query = "SELECT g FROM GallerySettings g")
    , @NamedQuery(name = "GallerySettings.findById", query = "SELECT g FROM GallerySettings g WHERE g.id = :id")
    , @NamedQuery(name = "GallerySettings.findByDeliveryDays", query = "SELECT g FROM GallerySettings g WHERE g.deliveryDays = :deliveryDays")
    , @NamedQuery(name = "GallerySettings.findByModificationDate", query = "SELECT g FROM GallerySettings g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "GallerySettings.findByCreationDate", query = "SELECT g FROM GallerySettings g WHERE g.creationDate = :creationDate")})
public class GallerySettings extends BaseEntity {

    @Column(name = "delivery_days")
    private Integer deliveryDays;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory galleryId;

    public GallerySettings() {
    }

    public GallerySettings(Integer id) {
        this.id = id;
    }

    public Integer getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(Integer deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(InvInventory galleryId) {
        this.galleryId = galleryId;
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
        if (!(object instanceof GallerySettings)) {
            return false;
        }
        GallerySettings other = (GallerySettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.GallerySettings[ id=" + id + " ]";
    }

}
