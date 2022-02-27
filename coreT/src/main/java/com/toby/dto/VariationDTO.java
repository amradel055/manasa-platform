/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class VariationDTO extends BaseEntityDTO{

 
    private Integer itemId;

 
    private String image;

   
    private String describtion;


    private BigDecimal price;

   
    private BigDecimal salePrice;

 
//    private Integer defaultVariation;

   private List <VariationDetailsDTO> variationDetails ;





    /**
     * @return the itemIdF
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

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the describtion
     */
    public String getDescribtion() {
        return describtion;
    }

    /**
     * @param describtion the describtion to set
     */
    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the salePrice
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

//    /**
//     * @return the defaultVariation
//     */
//    public Integer getDefaultVariation() {
//        return defaultVariation;
//    }
//
//    /**
//     * @param defaultVariation the defaultVariation to set
//     */
//    public void setDefaultVariation(Integer defaultVariation) {
//        this.defaultVariation = defaultVariation;
//    }

    /**
     * @return the variationDetails
     */
    public List <VariationDetailsDTO> getVariationDetails() {
        if(variationDetails == null){
        variationDetails = new ArrayList<>();
        }
        return variationDetails;
    }

    /**
     * @param variationDetails the variationDetails to set
     */
    public void setVariationDetails(List <VariationDetailsDTO> variationDetails) {
        this.variationDetails = variationDetails;
    }

}
