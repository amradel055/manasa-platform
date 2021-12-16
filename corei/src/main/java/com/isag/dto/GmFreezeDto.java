/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author Lenovo
 */
public class GmFreezeDto  extends BaseEntityDTO {
       private InvOrganizationSiteDTO customerId;
       private Date dateto;
       private Date datefrom;
       private Integer number;


    /**
     * @return the customerId
     */
    public InvOrganizationSiteDTO getCustomerId() {
        if(customerId == null){
            setCustomerId(new InvOrganizationSiteDTO());
        }
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSiteDTO customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the dateto
     */
    public Date getDateto() {
        return dateto;
    }

    /**
     * @param dateto the dateto to set
     */
    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }

    /**
     * @return the datefrom
     */
    public Date getDatefrom() {
        return datefrom;
    }

    /**
     * @param datefrom the datefrom to set
     */
    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }
}
