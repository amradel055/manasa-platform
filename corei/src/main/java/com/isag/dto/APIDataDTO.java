/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.List;

/**
 *
 * @author ahmed
 */
public class APIDataDTO {

    private String name;
    private Integer branchSelected;
    private List<APIBranchDTO> branchList;

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
     * @return the branchSelected
     */
    public Integer getBranchSelected() {
        return branchSelected;
    }

    /**
     * @param branchSelected the branchSelected to set
     */
    public void setBranchSelected(Integer branchSelected) {
        this.branchSelected = branchSelected;
    }

    /**
     * @return the branchList
     */
    public List<APIBranchDTO> getBranchList() {
        return branchList;
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<APIBranchDTO> branchList) {
        this.branchList = branchList;
    }

}
