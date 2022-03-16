/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inst_contract_project")
public class InstContractProject extends BaseEntity {

    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide instProjectGuide;
    @JoinColumn(name = "inst_contract_id", referencedColumnName = "id")
    @ManyToOne
    private InstContract instContract;
    
      
    public InstContractProject() {
    }

    public InstContractProject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id)   {
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
        if (!(object instanceof InstContractProject)) {
            return false;
        }
        InstContractProject other = (InstContractProject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * @return the instProjectGuide
     */
    public InstProjectGuide getInstProjectGuide() {
        return instProjectGuide;
    }

    /**
     * @param instProjectGuide the instProjectGuide to set
     */
    public void setInstProjectGuide(InstProjectGuide instProjectGuide) {
        this.instProjectGuide = instProjectGuide;
    }

    /**
     * @return the instContract
     */
    public InstContract getInstContract() {
        return instContract;
    }

    /**
     * @param instContract the instContract to set
     */
    public void setInstContract(InstContract instContract) {
        this.instContract = instContract;
    }

}
