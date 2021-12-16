/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.isag.businessservice.GmFreezeService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.isag.BaseFormBean;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Lenovo
 */
@Named("gmfreezeMB")
@ViewScoped
public class gmFreezeMB extends BaseFormBean {

    private InvOrganizationSiteDTO siteDTO;
    private GmFreezeDto gmfreezeDto;
    private String query;
    private GmSubscriptionsDTO gmSubscriptionsDTOSelected ;
    @EJB
    GmFreezeService gmFreezeService;
    @EJB
    OrganizationSiteService organizationSiteService;
    private String customerName;

    public void reset() {
        try {
            setGmfreezeDto(null);

        } catch (Exception e) {
            saveError(e, "gmFreezeMB", "reset");
        }
    }

    @Override
    public String save() {

        gmFreezeService.save(getGmfreezeDto(), getUserData().getUser());

        return "";
    }

    public InvOrganizationSiteDTO searchCustomer() {
        if (getQuery() != null || !getQuery().isEmpty()) {
            siteDTO = organizationSiteService.getInvOrganizationSiteByQuery(query, getUserData().getUser());
            setCustomerName(siteDTO.getName());

            if (siteDTO != null) {
                gmfreezeDto.setCustomerId(siteDTO);
            }

        }
        return siteDTO;
    }
    
    public void getCustomer() {
        
            InvOrganizationSite entity = organizationSiteService.findOrganizationSiteById(getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId());
            siteDTO = organizationSiteService.mapToDTOSmall(entity);
            setCustomerName(siteDTO.getName());

            if (siteDTO != null) {
                gmfreezeDto.setCustomerId(siteDTO);
            }
        OpenDlg("dlg3");
    }

    /**
     * @return the gmfreezeDto
     */
    public GmFreezeDto getGmfreezeDto() {
        if (gmfreezeDto == null) {
            GmFreezeDto dto = new GmFreezeDto();
            gmfreezeDto = dto;
        }
        return gmfreezeDto;
    }

    /**
     * @param gmfreezeDto the gmfreezeDto to set
     */
    public void setGmfreezeDto(GmFreezeDto gmfreezeDto) {
        this.gmfreezeDto = gmfreezeDto;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
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
     * @return the siteDTO
     */
    public InvOrganizationSiteDTO getSiteDTO() {
        return siteDTO;
    }

    /**
     * @param siteDTO the siteDTO to set
     */
    public void setSiteDTO(InvOrganizationSiteDTO siteDTO) {
        this.siteDTO = siteDTO;
    }

    /**
     * @return the gmSubscriptionsDTOSelected
     */
    public GmSubscriptionsDTO getGmSubscriptionsDTOSelected() {
        return gmSubscriptionsDTOSelected;
    }

    /**
     * @param gmSubscriptionsDTOSelected the gmSubscriptionsDTOSelected to set
     */
    public void setGmSubscriptionsDTOSelected(GmSubscriptionsDTO gmSubscriptionsDTOSelected) {
        this.gmSubscriptionsDTOSelected = gmSubscriptionsDTOSelected;
    }

}
