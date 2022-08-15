/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AhmedEssam
 */
public class TobyUserDTO extends BaseEntityDTO {

    private String userCode;
    private String password;
    private String name;
    private SymbolDTO lang;
    private Integer master;
    private String residencyNumber;
    private Integer branchId;
    private List<TobyUserProproductionDTO> tobyUserProproductionDTOList;
    private List<ProProductionStagesDTO> productionStagesDTOList;

    private Boolean activeCrm;
    private String activeCrmName;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TobyUserDTO)) {
            return false;
        }
        TobyUserDTO other = (TobyUserDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + userCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lang
     */
    public SymbolDTO getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(SymbolDTO lang) {
        this.lang = lang;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    /**
     * @return the tobyUserProproductionDTOList
     */
    public List<TobyUserProproductionDTO> getTobyUserProproductionDTOList() {
        if (tobyUserProproductionDTOList == null) {
            tobyUserProproductionDTOList = new ArrayList<>();
        }
        return tobyUserProproductionDTOList;
    }

    /**
     * @param tobyUserProproductionDTOList the tobyUserProproductionDTOList to
     * set
     */
    public void setTobyUserProproductionDTOList(List<TobyUserProproductionDTO> tobyUserProproductionDTOList) {
        this.tobyUserProproductionDTOList = tobyUserProproductionDTOList;
    }

    /**
     * @return the productionStagesDTOList
     */
    public List<ProProductionStagesDTO> getProductionStagesDTOList() {
        if (productionStagesDTOList == null) {
            productionStagesDTOList = new ArrayList<>();
        }
        return productionStagesDTOList;
    }

    /**
     * @param productionStagesDTOList the productionStagesDTOList to set
     */
    public void setProductionStagesDTOList(List<ProProductionStagesDTO> productionStagesDTOList) {
        this.productionStagesDTOList = productionStagesDTOList;
    }

    /**
     * @return the residencyNumber
     */
    public String getResidencyNumber() {
        return residencyNumber;
    }

    /**
     * @param residencyNumber the residencyNumber to set
     */
    public void setResidencyNumber(String residencyNumber) {
        this.residencyNumber = residencyNumber;
    }

    /**
     * @return the activeCrm
     */
    public Boolean getActiveCrm() {
        return activeCrm;
    }

    /**
     * @param activeCrm the activeCrm to set
     */
    public void setActiveCrm(Boolean activeCrm) {
        this.activeCrm = activeCrm;
    }

    /**
     * @return the activeCrmName
     */
    public String getActiveCrmName() {
        return activeCrmName;
    }

    /**
     * @param activeCrmName the activeCrmName to set
     */
    public void setActiveCrmName(String activeCrmName) {
        this.activeCrmName = activeCrmName;
    }

}
