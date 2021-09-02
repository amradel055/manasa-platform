/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.OfferCompanyDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author m lapan
 */
public class ClientsByInvInventory {

    private String clientName;
    private String clientCode;
    private String clientMobile;
    private List<InvInventoryDTO> gallaryListSelected;
    private List<OfferCompanyDTO> offerCompanyListSelected;
    private InvInventoryDTO inventoryDTO;

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the clientCode
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * @param clientCode the clientCode to set
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    /**
     * @return the clientMobile
     */
    public String getClientMobile() {
        return clientMobile;
    }

    /**
     * @param clientMobile the clientMobile to set
     */
    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    @Override
    public String toString() {
        return "ClientsByInvInventory{" + "clientName=" + clientName + ", clientCode=" + clientCode + ", clientMobile=" + clientMobile + '}';
    }

    /**
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {
        if (gallaryListSelected == null) {
            gallaryListSelected = new ArrayList<>();
        }
        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

    /**
     * @return the inventoryDTO
     */
    public InvInventoryDTO getInventoryDTO() {
        return inventoryDTO;
    }

    /**
     * @param inventoryDTO the inventoryDTO to set
     */
    public void setInventoryDTO(InvInventoryDTO inventoryDTO) {
        this.inventoryDTO = inventoryDTO;
    }

    /**
     * @return the offerCompanyListSelected
     */
    public List<OfferCompanyDTO> getOfferCompanyListSelected() {
        return offerCompanyListSelected;
    }

    /**
     * @param offerCompanyListSelected the offerCompanyListSelected to set
     */
    public void setOfferCompanyListSelected(List<OfferCompanyDTO> offerCompanyListSelected) {
        this.offerCompanyListSelected = offerCompanyListSelected;
    }

}
