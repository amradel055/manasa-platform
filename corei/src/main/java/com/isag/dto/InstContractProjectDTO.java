/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author amr
 */
public class InstContractProjectDTO extends BaseEntityDTO {

    private Integer instProjectGuide;
    private Integer instContract;
    private Integer instProjectGuideParent;
    
      
    public InstContractProjectDTO() {
    }

    public InstContractProjectDTO(Integer id) {
        this.id = id;
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
        if (!(object instanceof InstContractProjectDTO)) {
            return false;
        }
        InstContractProjectDTO other = (InstContractProjectDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * @return the instProjectGuide
     */
    public Integer getInstProjectGuide() {
        return instProjectGuide;
    }

    /**
     * @param instProjectGuide the instProjectGuide to set
     */
    public void setInstProjectGuide(Integer instProjectGuide) {
        this.instProjectGuide = instProjectGuide;
    }

    /**
     * @return the instContract
     */
    public Integer getInstContract() {
        return instContract;
    }

    /**
     * @param instContract the instContract to set
     */
    public void setInstContract(Integer instContract) {
        this.instContract = instContract;
    }

    /**
     * @return the instProjectGuideParent
     */
    public Integer getInstProjectGuideParent() {
        return instProjectGuideParent;
    }

    /**
     * @param instProjectGuideParent the instProjectGuideParent to set
     */
    public void setInstProjectGuideParent(Integer instProjectGuideParent) {
        this.instProjectGuideParent = instProjectGuideParent;
    }

}
