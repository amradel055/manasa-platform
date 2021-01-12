package com.toby.dto;

import com.toby.entity.ProFactoryDeliveryDetail;
import java.math.BigDecimal;

public class ProProductionTransactionDetailDetailViewDTO extends BaseEntityDTO {
     
    private Integer invItemCode;
    private BigDecimal cost;
    private String invItemName;
    private BigDecimal quantity;
    private BigDecimal totalNumber;
    private BigDecimal numberExcute;
    private BigDecimal numberRemain;
    private Integer proFactoryDeliveryDetailId;
    


    /**
     * @return the invItemName
     */
    public String getInvItemName() {
        return invItemName;
    }

    /**
     * @param invItemName the invItemName to set
     */
    public void setInvItemName(String invItemName) {
        this.invItemName = invItemName;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the invItemCode
     */
    public Integer getInvItemCode() {
        return invItemCode;
    }

    /**
     * @param invItemCode the invItemCode to set
     */
    public void setInvItemCode(Integer invItemCode) {
        this.invItemCode = invItemCode;
    }

    /**
     * @return the totalNumber
     */
    public BigDecimal getTotalNumber() {
        return totalNumber;
    }

    /**
     * @param totalNumber the totalNumber to set
     */
    public void setTotalNumber(BigDecimal totalNumber) {
        this.totalNumber = totalNumber;
    }

    /**
     * @return the numberExcute
     */
    public BigDecimal getNumberExcute() {
        return numberExcute;
    }

    /**
     * @param numberExcute the numberExcute to set
     */
    public void setNumberExcute(BigDecimal numberExcute) {
        this.numberExcute = numberExcute;
    }

    /**
     * @return the numberRemain
     */
    public BigDecimal getNumberRemain() {
        return numberRemain;
    }

    /**
     * @param numberRemain the numberRemain to set
     */
    public void setNumberRemain(BigDecimal numberRemain) {
        this.numberRemain = numberRemain;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the proFactoryDeliveryDetailId
     */
    public Integer getProFactoryDeliveryDetailId() {
        return proFactoryDeliveryDetailId;
    }

    /**
     * @param proFactoryDeliveryDetailId the proFactoryDeliveryDetailId to set
     */
    public void setProFactoryDeliveryDetailId(Integer proFactoryDeliveryDetailId) {
        this.proFactoryDeliveryDetailId = proFactoryDeliveryDetailId;
    }
    
    

  
}
