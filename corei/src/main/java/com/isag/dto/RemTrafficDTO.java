/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IT Shop
 */
public class RemTrafficDTO extends BaseEntityDTO{

    private String name;
    private Integer code;
    private RemCompanyDTO remCompanyDTO;
    private List<RemTrafficDetailDTO> remTrafficDetailDTOList;
    private List<RemTrafficDetailDTO> remTrafficDetailDTODeletedList;
    private RemTrafficDetailDTO remTrafficDetailDTOSelected;

    public RemTrafficDTO() {
    }

    public RemTrafficDTO(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
        if (!(object instanceof RemTrafficDTO)) {
            return false;
        }
        RemTrafficDTO other = (RemTrafficDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name+ " " + code;
    }

    /**
     * @return the remCompanyDTO
     */
    public RemCompanyDTO getRemCompanyDTO() {
        return remCompanyDTO;
    }

    /**
     * @param remCompanyDTO the remCompanyDTO to set
     */
    public void setRemCompanyDTO(RemCompanyDTO remCompanyDTO) {
        this.remCompanyDTO = remCompanyDTO;
    }

    /**
     * @return the remTrafficDetailDTOList
     */
    public List<RemTrafficDetailDTO> getRemTrafficDetailDTOList() {
        if(remTrafficDetailDTOList == null){
            remTrafficDetailDTOList = new ArrayList<>();
        }
        return remTrafficDetailDTOList;
    }

    /**
     * @param remTrafficDetailDTOList the remTrafficDetailDTOList to set
     */
    public void setRemTrafficDetailDTOList(List<RemTrafficDetailDTO> remTrafficDetailDTOList) {
        this.remTrafficDetailDTOList = remTrafficDetailDTOList;
    }

    /**
     * @return the remTrafficDetailDTOSelected
     */
    public RemTrafficDetailDTO getRemTrafficDetailDTOSelected() {
        return remTrafficDetailDTOSelected;
    }

    /**
     * @param remTrafficDetailDTOSelected the remTrafficDetailDTOSelected to set
     */
    public void setRemTrafficDetailDTOSelected(RemTrafficDetailDTO remTrafficDetailDTOSelected) {
        this.remTrafficDetailDTOSelected = remTrafficDetailDTOSelected;
    }

    /**
     * @return the remTrafficDetailDTODeletedList
     */
    public List<RemTrafficDetailDTO> getRemTrafficDetailDTODeletedList() {
        return remTrafficDetailDTODeletedList;
    }

    /**
     * @param remTrafficDetailDTODeletedList the remTrafficDetailDTODeletedList to set
     */
    public void setRemTrafficDetailDTODeletedList(List<RemTrafficDetailDTO> remTrafficDetailDTODeletedList) {
        this.remTrafficDetailDTODeletedList = remTrafficDetailDTODeletedList;
    }
    
}
