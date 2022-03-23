/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author amr
 */
public class InvItemDTOApi extends BaseEntityDTO {

    private String name;
    private String code;
    private String remarks;
    private String image;
    private Integer groupId;
    private BigDecimal price;
    private List<AttributeDTO> atrributesList;
    private List<VariationDTO> variationsList;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * @return the atrributesList
     */
    public List<AttributeDTO> getAtrributesList() {
        return atrributesList;
    }

    /**
     * @param atrributesList the atrributesList to set
     */
    public void setAtrributesList(List<AttributeDTO> atrributesList) {
        this.atrributesList = atrributesList;
    }

    /**
     * @return the variationsList
     */
    public List<VariationDTO> getVariationsList() {
        return variationsList;
    }

    /**
     * @param variationsList the variationsList to set
     */
    public void setVariationsList(List<VariationDTO> variationsList) {
        this.variationsList = variationsList;
    }

    /**
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
