/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author amr
 */
public class RscGenaricLoad extends BaseEntityDTO {

    private String customerName;
    private Integer serial;
    private Integer tenderSerial;
    private Integer tenderId;
    private Integer priceTenderSerial;
    private Integer priceTenderId;
    private String tenderTypeName;
    private Integer tenderType;
    private Integer qotationPriceTenderId;
    private Integer qotationPriceTenderSerial;
    private String remark;

    public RscGenaricLoad() {
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the tenderSerial
     */
    public Integer getTenderSerial() {
        return tenderSerial;
    }

    /**
     * @param tenderSerial the tenderSerial to set
     */
    public void setTenderSerial(Integer tenderSerial) {
        this.tenderSerial = tenderSerial;
    }

    /**
     * @return the tenderTypeName
     */
    public String getTenderTypeName() {
        return tenderTypeName;
    }

    /**
     * @param tenderTypeName the tenderTypeName to set
     */
    public void setTenderTypeName(String tenderTypeName) {
        this.tenderTypeName = tenderTypeName;
    }

    /**
     * @return the tenderId
     */
    public Integer getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the priceTenderSerial
     */
    public Integer getPriceTenderSerial() {
        return priceTenderSerial;
    }

    /**
     * @param priceTenderSerial the priceTenderSerial to set
     */
    public void setPriceTenderSerial(Integer priceTenderSerial) {
        this.priceTenderSerial = priceTenderSerial;
    }

    /**
     * @return the priceTenderId
     */
    public Integer getPriceTenderId() {
        return priceTenderId;
    }

    /**
     * @param priceTenderId the priceTenderId to set
     */
    public void setPriceTenderId(Integer priceTenderId) {
        this.priceTenderId = priceTenderId;
    }

    /**
     * @return the tenderType
     */
    public Integer getTenderType() {
        return tenderType;
    }

    /**
     * @param tenderType the tenderType to set
     */
    public void setTenderType(Integer tenderType) {
        this.tenderType = tenderType;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the qotationPriceTenderId
     */
    public Integer getQotationPriceTenderId() {
        return qotationPriceTenderId;
    }

    /**
     * @param qotationPriceTenderId the qotationPriceTenderId to set
     */
    public void setQotationPriceTenderId(Integer qotationPriceTenderId) {
        this.qotationPriceTenderId = qotationPriceTenderId;
    }

    /**
     * @return the qotationPriceTenderSerial
     */
    public Integer getQotationPriceTenderSerial() {
        return qotationPriceTenderSerial;
    }

    /**
     * @param qotationPriceTenderSerial the qotationPriceTenderSerial to set
     */
    public void setQotationPriceTenderSerial(Integer qotationPriceTenderSerial) {
        this.qotationPriceTenderSerial = qotationPriceTenderSerial;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
