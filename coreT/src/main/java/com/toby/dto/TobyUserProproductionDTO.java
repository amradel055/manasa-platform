package com.toby.dto;

import java.math.BigDecimal;

public class TobyUserProproductionDTO extends BaseEntityDTO {

    private Integer proproductionId;
    private String proproductionName;
    private BigDecimal proproductionPrice;
    private Integer userId;

    public TobyUserProproductionDTO() {
    }

    public TobyUserProproductionDTO(Integer id) {
        this.id = id;
    }

    
    public Integer getProproductionId() {
        return proproductionId;
    }

    
    public void setProproductionId(Integer proproductionId) {
        this.proproductionId = proproductionId;
    }

    
    public Integer getUserId() {
        return userId;
    }

    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    @Override
    public String toString() {
        return proproductionName + id ;
    }

    /**
     * @return the proproductionName
     */
    public String getProproductionName() {
        return proproductionName;
    }

    /**
     * @param proproductionName the proproductionName to set
     */
    public void setProproductionName(String proproductionName) {
        this.proproductionName = proproductionName;
    }

    /**
     * @return the proproductionPrice
     */
    public BigDecimal getProproductionPrice() {
        return proproductionPrice;
    }

    /**
     * @param proproductionPrice the proproductionPrice to set
     */
    public void setProproductionPrice(BigDecimal proproductionPrice) {
        this.proproductionPrice = proproductionPrice;
    }


}


