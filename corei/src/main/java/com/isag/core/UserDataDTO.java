/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.core;

import com.isag.dto.GlBankDTO;
import com.isag.dto.InvInventoryDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author hq004
 */

public class UserDataDTO implements Serializable {

    private Integer companyId;

    private Integer branchId;
    
    private List<InvInventoryDTO> InventoryDTOList;
    
    private InvInventoryDTO inventoryDTODefault;
    
    private List<GlBankDTO> glBankDTOList;
    
    private GlBankDTO glBankDTODefault;

  /**
     * @return the InventoryDTOList
     */
    public List<InvInventoryDTO> getInventoryDTOList() {
        return InventoryDTOList;
    }

    /**
     * @param InventoryDTOList the InventoryDTOList to set
     */
    public void setInventoryDTOList(List<InvInventoryDTO> InventoryDTOList) {
        this.InventoryDTOList = InventoryDTOList;
    }

    /**
     * @return the inventoryDTODefault
     */
    public InvInventoryDTO getInventoryDTODefault() {
        return inventoryDTODefault;
    }

    /**
     * @param inventoryDTODefault the inventoryDTODefault to set
     */
    public void setInventoryDTODefault(InvInventoryDTO inventoryDTODefault) {
        this.inventoryDTODefault = inventoryDTODefault;
    }

    /**
     * @return the glBankDTOList
     */
    public List<GlBankDTO> getGlBankDTOList() {
        return glBankDTOList;
    }

    /**
     * @param glBankDTOList the glBankDTOList to set
     */
    public void setGlBankDTOList(List<GlBankDTO> glBankDTOList) {
        this.glBankDTOList = glBankDTOList;
    }

    /**
     * @return the glBankDTODefault
     */
    public GlBankDTO getGlBankDTODefault() {
        return glBankDTODefault;
    }

    /**
     * @param glBankDTODefault the glBankDTODefault to set
     */
    public void setGlBankDTODefault(GlBankDTO glBankDTODefault) {
        this.glBankDTODefault = glBankDTODefault;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
