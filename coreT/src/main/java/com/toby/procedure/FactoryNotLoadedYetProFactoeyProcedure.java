/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.procedure;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam
 */
@Entity
@Table(name = "factory_not_loaded_yet_Pro_Factoey_procedure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactoryNotLoadedYetProFactoeyProcedure.findAll", query = "SELECT t FROM FactoryNotLoadedYetProFactoeyProcedure t")})
public class FactoryNotLoadedYetProFactoeyProcedure implements Serializable {

    private static final long serialVersionUID = 1L;

    public FactoryNotLoadedYetProFactoeyProcedure() {
    }
    
    @Id   
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "detail_id")
    private Integer detailId;
    @Column(name = "item_barcode")
    @Max(40)
    private String itemBarcode;
    @Column(name = "number")
    private BigDecimal number;

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
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
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
     * @return the detailId
     */
    public Integer getDetailId() {
        return detailId;
    }

    /**
     * @param detailId the detailId to set
     */
    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

}
