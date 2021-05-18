/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

public class GallerySettingsDTO extends BaseEntityDTO {

   
    private Integer deliveryDays;
    private InvInventoryDTO galleryId;

    public GallerySettingsDTO() {
    }

    public GallerySettingsDTO(Integer id) {
        this.id = id;
    }

    public Integer getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(Integer deliveryDays) {
        this.deliveryDays = deliveryDays;
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
        if (!(object instanceof GallerySettingsDTO)) {
            return false;
        }
        GallerySettingsDTO other = (GallerySettingsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.GallerySettings[ id=" + id + " ]";
    }

    /**
     * @return the galleryId
     */
    public InvInventoryDTO getGalleryId() {
        return galleryId;
    }

    /**
     * @param galleryId the galleryId to set
     */
    public void setGalleryId(InvInventoryDTO galleryId) {
        this.galleryId = galleryId;
    }

   
}
