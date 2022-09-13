/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public class APIDataDTO {

    private String name;
    private Integer id;
    private Integer branchSelected;
    private Integer companySelected;
    private Integer galleryId;
    private Integer accountIdAPI;
    private String mobile ;
    private String galleryName ;
    private Integer galleryType;
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

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the companySelected
     */
    public Integer getCompanySelected() {
        return companySelected;
    }

    /**
     * @param companySelected the companySelected to set
     */
    public void setCompanySelected(Integer companySelected) {
        this.companySelected = companySelected;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the galleryId
     */
    public Integer getGalleryId() {
        return galleryId;
    }

    /**
     * @param galleryId the galleryId to set
     */
    public void setGalleryId(Integer galleryId) {
        this.galleryId = galleryId;
    }

    /**
     * @return the accountIdAPI
     */
    public Integer getAccountIdAPI() {
        return accountIdAPI;
    }

    /**
     * @param accountIdAPI the accountIdAPI to set
     */
    public void setAccountIdAPI(Integer accountIdAPI) {
        this.accountIdAPI = accountIdAPI;
    }

    /**
     * @return the galleryName
     */
    public String getGalleryName() {
        return galleryName;
    }

    /**
     * @param galleryName the galleryName to set
     */
    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    /**
     * @return the galleryType
     */
    public Integer getGalleryType() {
        return galleryType;
    }

    /**
     * @param galleryType the galleryType to set
     */
    public void setGalleryType(Integer galleryType) {
        this.galleryType = galleryType;
    }

}
