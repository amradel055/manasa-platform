/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;


public class OffersDetailsDTO extends BaseEntityDTO {

  
    private Integer parentOfferId;

  
    private Integer itemId;


    private Integer categoryId;


    private Integer status;


    
    
    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the parentOfferId
     */
    public Integer getParentOfferId() {
        return parentOfferId;
    }

    /**
     * @param parentOfferId the parentOfferId to set
     */
    public void setParentOfferId(Integer parentOfferId) {
        this.parentOfferId = parentOfferId;
    }

    /**
     * @return the itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

}
