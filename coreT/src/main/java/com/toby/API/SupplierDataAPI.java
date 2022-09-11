/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.entity.InvOrganizationSite;

/**
 *
 * @author WIN7
 */
public class SupplierDataAPI extends BaseEntityDTOAPI {

    private Integer invType;
    private InvOrganizationSite fromorganizationName;
    private InvOrganizationSite toorganizationName;

    /**
     * @return the invType
     */
    public Integer getInvType() {
        return invType;
    }

    /**
     * @param invType the invType to set
     */
    public void setInvType(Integer invType) {
        this.invType = invType;
    }

    /**
     * @return the fromorganizationName
     */
    public InvOrganizationSite getFromorganizationName() {
        return fromorganizationName;
    }

    /**
     * @param fromorganizationName the fromorganizationName to set
     */
    public void setFromorganizationName(InvOrganizationSite fromorganizationName) {
        this.fromorganizationName = fromorganizationName;
    }

    /**
     * @return the toorganizationName
     */
    public InvOrganizationSite getToorganizationName() {
        return toorganizationName;
    }

    /**
     * @param toorganizationName the toorganizationName to set
     */
    public void setToorganizationName(InvOrganizationSite toorganizationName) {
        this.toorganizationName = toorganizationName;
    }

}
