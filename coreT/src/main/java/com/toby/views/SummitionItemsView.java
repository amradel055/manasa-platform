package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;



public class SummitionItemsView implements Serializable {
    
    
   private BigDecimal quantity ;
   private BigDecimal number ;
   private BigDecimal allQuantity ;
   private BigDecimal allCost ;
   private BigDecimal allAvarageCost ;
   private String code ;
   private String name ;

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
     * @return the number
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the allQuantity
     */
    public BigDecimal getAllQuantity() {
        return allQuantity;
    }

    /**
     * @param allQuantity the allQuantity to set
     */
    public void setAllQuantity(BigDecimal allQuantity) {
        this.allQuantity = allQuantity;
    }

    /**
     * @return the allCost
     */
    public BigDecimal getAllCost() {
        return allCost;
    }

    /**
     * @param allCost the allCost to set
     */
    public void setAllCost(BigDecimal allCost) {
        this.allCost = allCost;
    }

    /**
     * @return the allAvarageCost
     */
    public BigDecimal getAllAvarageCost() {
        return allAvarageCost;
    }

    /**
     * @param allAvarageCost the allAvarageCost to set
     */
    public void setAllAvarageCost(BigDecimal allAvarageCost) {
        this.allAvarageCost = allAvarageCost;
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

    }
