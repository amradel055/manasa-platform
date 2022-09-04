/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;



public class CostCenterDTOAPI extends BaseEntityDTOAPI {

    private Integer code;
    private String name;
    private Integer currentaccountId;
    
    public CostCenterDTOAPI() {
    }

    public CostCenterDTOAPI(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof CostCenterDTOAPI)) {
            return false;
        }
        CostCenterDTOAPI other = (CostCenterDTOAPI) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the currentaccountId
     */
    public Integer getCurrentaccountId() {
        return currentaccountId;
    }

    /**
     * @param currentaccountId the currentaccountId to set
     */
    public void setCurrentaccountId(Integer currentaccountId) {
        this.currentaccountId = currentaccountId;
    }
}
