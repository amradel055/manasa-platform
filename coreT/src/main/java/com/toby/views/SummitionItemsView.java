package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;

public class SummitionItemsView implements Serializable {

    private BigDecimal quantity;
    private BigDecimal number;
    private BigDecimal allQuantity;
    private BigDecimal allCost;
    private BigDecimal allAvarageCost;
    private Integer code;
    private String name;
    private BigDecimal factoryNumber;
    private BigDecimal sendNumber;
    private BigDecimal deliveryNumber;
    private BigDecimal customerNumber;
    private BigDecimal Different;
    private Integer gallarycode;
    private String gallaryname;
    private Integer rowType;

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {

        if (quantity == null) {
            return BigDecimal.ZERO;
        }
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

        if (number == null) {
            return BigDecimal.ZERO;
        }
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

        if (allQuantity == null) {
            return BigDecimal.ZERO;
        }

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

        if (allCost == null) {
            return BigDecimal.ZERO;
        }
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

        if (allAvarageCost == null) {
            return BigDecimal.ZERO;
        }
        return allAvarageCost;
    }

    /**
     * @param allAvarageCost the allAvarageCost to set
     */
    public void setAllAvarageCost(BigDecimal allAvarageCost) {
        this.allAvarageCost = allAvarageCost;
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

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the factoryNumber
     */
    public BigDecimal getFactoryNumber() {
        if (factoryNumber == null) {

            return BigDecimal.ZERO;
        }
        return factoryNumber;
    }

    /**
     * @param factoryNumber the factoryNumber to set
     */
    public void setFactoryNumber(BigDecimal factoryNumber) {
        this.factoryNumber = factoryNumber;
    }

    /**
     * @return the sendNumber
     */
    public BigDecimal getSendNumber() {
        if (sendNumber == null) {

            return BigDecimal.ZERO;
        }
        return sendNumber;
    }

    /**
     * @param sendNumber the sendNumber to set
     */
    public void setSendNumber(BigDecimal sendNumber) {
        this.sendNumber = sendNumber;
    }

    /**
     * @return the deliveryNumber
     */
    public BigDecimal getDeliveryNumber() {
        if (deliveryNumber == null) {
            return BigDecimal.ZERO;
        }

        return deliveryNumber;
    }

    /**
     * @param deliveryNumber the deliveryNumber to set
     */
    public void setDeliveryNumber(BigDecimal deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    /**
     * @return the Different
     */
    public BigDecimal getDifferent() {
        if (Different == null) {
            return BigDecimal.ZERO;
        } else {
            Different = factoryNumber.subtract(sendNumber);
            return Different;
        }
    }

    /**
     * @param Different the Different to set
     */
    public void setDifferent(BigDecimal Different) {
        this.Different = Different;
    }

    /**
     * @return the customerNumber
     */
    public BigDecimal getCustomerNumber() {
        if (customerNumber == null) {
            return BigDecimal.ZERO;
        }
        return customerNumber;
    }

    /**
     * @param customerNumber the customerNumber to set
     */
    public void setCustomerNumber(BigDecimal customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * @return the gallarycode
     */
    public Integer getGallarycode() {
        return gallarycode;
    }

    /**
     * @param gallarycode the gallarycode to set
     */
    public void setGallarycode(Integer gallarycode) {
        this.gallarycode = gallarycode;
    }

    /**
     * @return the gallaryname
     */
    public String getGallaryname() {
        return gallaryname;
    }

    /**
     * @param gallaryname the gallaryname to set
     */
    public void setGallaryname(String gallaryname) {
        this.gallaryname = gallaryname;
    }

    /**
     * @return the rowType
     */
    public Integer getRowType() {
        if (rowType == null) {
            rowType = 0;
        }
        return rowType;
    }

    /**
     * @param rowType the rowType to set
     */
    public void setRowType(Integer rowType) {
        this.rowType = rowType;
    }

}
