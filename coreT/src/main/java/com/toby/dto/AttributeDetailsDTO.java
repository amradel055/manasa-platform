/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

public class AttributeDetailsDTO extends BaseEntityDTO  {
    
    private Integer headId;
    
    private Integer itemId;
    
    private Integer orderBy;
   
    private String value;

    /**
     * @return the headId
     */
    public Integer getHeadId() {
        return headId;
    }

    /**
     * @param headId the headId to set
     */
    public void setHeadId(Integer headId) {
        this.headId = headId;
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

    /**
     * @return the orderBy
     */
    public Integer getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }





}
