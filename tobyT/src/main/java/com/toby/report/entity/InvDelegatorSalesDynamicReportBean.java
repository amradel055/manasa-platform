/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class InvDelegatorSalesDynamicReportBean {

    private String organizationName;
    private BigDecimal field_1;
    private BigDecimal field_2;
    private BigDecimal field_3;
    private BigDecimal total;
    private Integer layerReference;

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the field_1
     */
    public BigDecimal getField_1() {
        return field_1;
    }

    /**
     * @param field_1 the field_1 to set
     */
    public void setField_1(BigDecimal field_1) {
        this.field_1 = field_1;
    }

    /**
     * @return the field_2
     */
    public BigDecimal getField_2() {
        return field_2;
    }

    /**
     * @param field_2 the field_2 to set
     */
    public void setField_2(BigDecimal field_2) {
        this.field_2 = field_2;
    }

    /**
     * @return the field_3
     */
    public BigDecimal getField_3() {
        return field_3;
    }

    /**
     * @param field_3 the field_3 to set
     */
    public void setField_3(BigDecimal field_3) {
        this.field_3 = field_3;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the layerReference
     */
    public Integer getLayerReference() {
        return layerReference;
    }

    /**
     * @param layerReference the layerReference to set
     */
    public void setLayerReference(Integer layerReference) {
        this.layerReference = layerReference;
    }

}
