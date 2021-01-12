/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.InvGroup;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author H
 */
public class InvGroupDTO extends BaseEntityDTO {

    private String name;
    private Integer level;
    private Integer code;
    private Collection<InvItemDTO> invItemCollection;
    private Collection<InvGroupDTO> invGroupCollection;
    private InvGroupDTO parent;
    private Integer isDeleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @XmlTransient
    public Collection<InvItemDTO> getInvItemCollection() {
        return invItemCollection;
    }

    public void setInvItemCollection(Collection<InvItemDTO> invItemCollection) {
        this.invItemCollection = invItemCollection;
    }

    @XmlTransient
    public Collection<InvGroupDTO> getInvGroupCollection() {
        return invGroupCollection;
    }

    public void setInvGroupCollection(Collection<InvGroupDTO> invGroupCollection) {
        this.invGroupCollection = invGroupCollection;
    }

    public InvGroupDTO getParent() {
        return parent;
    }

    public void setParent(InvGroupDTO parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex()!= null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvGroup)) {
            return false;
        }
        InvGroupDTO other = (InvGroupDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
