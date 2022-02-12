/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.List;

public class AttributeDTO extends BaseEntityDTO  {
        
    private String name;

    private Integer parent;
    
    private List<AttributeDetailsDTO> terms;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return the terms
     */
    public List<AttributeDetailsDTO> getTerms() {
        return terms;
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(List<AttributeDetailsDTO> terms) {
        this.terms = terms;
    }

}
