/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author IT Shop
 */
public class ItemOfferDTO  extends BaseEntityDTO {

    private Date dateFrom;
    private Date dateTo;
    private List<ItemOfferDetailDTO> itemOfferDetailDTOList;
    private List<ItemOfferDetailDTO> itemOfferDetailDTODeletedList;
    private ItemOfferDetailDTO itemOfferDetailDTOSelected;

    public ItemOfferDTO() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOfferDTO)) {
            return false;
        }
        ItemOfferDTO other = (ItemOfferDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ItemOffer[ id=" + id + " ]";
    }

    /**
     * @return the itemOfferDetailDTOList
     */
    public List<ItemOfferDetailDTO> getItemOfferDetailDTOList() {
        if(itemOfferDetailDTOList == null){
            itemOfferDetailDTOList = new ArrayList<>();
        }
        return itemOfferDetailDTOList;
    }

    /**
     * @param itemOfferDetailDTOList the itemOfferDetailDTOList to set
     */
    public void setItemOfferDetailDTOList(List<ItemOfferDetailDTO> itemOfferDetailDTOList) {
        this.itemOfferDetailDTOList = itemOfferDetailDTOList;
    }

    /**
     * @return the itemOfferDetailDTOSelected
     */
    public ItemOfferDetailDTO getItemOfferDetailDTOSelected() {
        return itemOfferDetailDTOSelected;
    }

    /**
     * @param itemOfferDetailDTOSelected the itemOfferDetailDTOSelected to set
     */
    public void setItemOfferDetailDTOSelected(ItemOfferDetailDTO itemOfferDetailDTOSelected) {
        this.itemOfferDetailDTOSelected = itemOfferDetailDTOSelected;
    }

    /**
     * @return the itemOfferDetailDTODeletedList
     */
    public List<ItemOfferDetailDTO> getItemOfferDetailDTODeletedList() {
        return itemOfferDetailDTODeletedList;
    }

    /**
     * @param itemOfferDetailDTODeletedList the itemOfferDetailDTODeletedList to set
     */
    public void setItemOfferDetailDTODeletedList(List<ItemOfferDetailDTO> itemOfferDetailDTODeletedList) {
        this.itemOfferDetailDTODeletedList = itemOfferDetailDTODeletedList;
    }
    
}
