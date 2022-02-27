/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "gl_admin_unit")
@XmlRootElement
public class GlAdminUnit extends BaseEntity {

    @Column(name = "code")
    private Integer code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "short_code")
    private Integer shortCode;
    @Column(name = "level")
    private Integer level;
    
    @Column(name = "active")
    private boolean active;
    
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<GlAdminUnit> AdminUnitChilds;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private GlAdminUnit parent;
    
    public GlAdminUnit() {
    }

    public GlAdminUnit(Integer id) {
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

    public Integer getShortCode() {
        return shortCode;
    }

    public void setShortCode(Integer shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public GlAdminUnit getParent() {
        return parent;
    }

    public void setParent(GlAdminUnit parent) {
        this.parent = parent;
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
        if (!(object instanceof GlAdminUnit)) {
            return false;
        }
        GlAdminUnit other = (GlAdminUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    public String getAdminUnit() {
        return code + " " + name;
    }
    
    @Override
    public String toString() {
       return code + " " + name;
    }

    /**
     * @return the AdminUnitChilds
     */
    public List<GlAdminUnit> getAdminUnitChilds() {
        return AdminUnitChilds;
    }

    /**
     * @param AdminUnitChilds the AdminUnitChilds to set
     */
    public void setAdminUnitChilds(List<GlAdminUnit> AdminUnitChilds) {
        this.AdminUnitChilds = AdminUnitChilds;
    }

    public void setAdminUnit(String adminUnit) {
        
    }
   /* public String convertActive(){
        boolean conevrt = getActive();
        String con = String.valueOf(conevrt);
        if(con == "true"){
            return "مفعل";
        }
        else{
            return "غير مفعل";
        }
    }*/
}
