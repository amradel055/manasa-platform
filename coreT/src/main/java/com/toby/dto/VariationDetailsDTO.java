/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;


public class VariationDetailsDTO extends BaseEntityDTO {

   
    private Integer variationId;

 
    private Integer attributeDetailsId;





    /**
     * @return the itemId
     */
    /**
     * @return the variationId
     */
    public Integer getVariationId() {
        return variationId;
    }

    /**
     * @param variationId the variationId to set
     */
    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    /**
     * @return the attributeDetailsId
     */
    public Integer getAttributeDetailsId() {
        return attributeDetailsId;
    }

    /**
     * @param attributeDetailsId the attributeDetailsId to set
     */
    public void setAttributeDetailsId(Integer attributeDetailsId) {
        this.attributeDetailsId = attributeDetailsId;
    }

}
