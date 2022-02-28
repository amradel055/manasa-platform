/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;


/**
 *
 * @author AhmedEssam
 */
public class invOrganizationSiteSearchBean {

    private InvOrganizationSiteDTO FrominvOrganizationSiteDTO;
    private InvOrganizationSiteDTO ToinvOrganizationSiteDTO;
    private SymbolDTO fromRegion;
    private SymbolDTO toRegion;

    /**
     * @return the FrominvOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getFrominvOrganizationSiteDTO() {
        return FrominvOrganizationSiteDTO;
    }

    /**
     * @param FrominvOrganizationSiteDTO the FrominvOrganizationSiteDTO to set
     */
    public void setFrominvOrganizationSiteDTO(InvOrganizationSiteDTO FrominvOrganizationSiteDTO) {
        this.FrominvOrganizationSiteDTO = FrominvOrganizationSiteDTO;
    }

    /**
     * @return the ToinvOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getToinvOrganizationSiteDTO() {
        return ToinvOrganizationSiteDTO;
    }

    /**
     * @param ToinvOrganizationSiteDTO the ToinvOrganizationSiteDTO to set
     */
    public void setToinvOrganizationSiteDTO(InvOrganizationSiteDTO ToinvOrganizationSiteDTO) {
        this.ToinvOrganizationSiteDTO = ToinvOrganizationSiteDTO;
    }

    /**
     * @return the fromRegion
     */
    public SymbolDTO getFromRegion() {
        return fromRegion;
    }

    /**
     * @param fromRegion the fromRegion to set
     */
    public void setFromRegion(SymbolDTO fromRegion) {
        this.fromRegion = fromRegion;
    }

    /**
     * @return the toRegion
     */
    public SymbolDTO getToRegion() {
        return toRegion;
    }

    /**
     * @param toRegion the toRegion to set
     */
    public void setToRegion(SymbolDTO toRegion) {
        this.toRegion = toRegion;
    }
}
