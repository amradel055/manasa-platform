/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import java.util.List;

/**
 *
 * @author WIN7
 */
public class IsagCompanyEntity extends NewBaseEntity {

    private String name;
    private String code;
    private List<IsagUserEntity> isagUsersList;
    private String core_business;
    private String responsible;
    private String phone;
    private String address;
    private byte[] logo;
    private String image;
    private String fax;
    private Integer rowCountList;
    private Integer rowCountMasterDetails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<IsagUserEntity> getIsagUsersList() {
        return isagUsersList;
    }

    public void setIsagUsersList(List<IsagUserEntity> isagUsersList) {
        this.isagUsersList = isagUsersList;
    }

    public String getCore_business() {
        return core_business;
    }

    public void setCore_business(String core_business) {
        this.core_business = core_business;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getRowCountList() {
        return rowCountList;
    }

    public void setRowCountList(Integer rowCountList) {
        this.rowCountList = rowCountList;
    }

    public Integer getRowCountMasterDetails() {
        return rowCountMasterDetails;
    }

    public void setRowCountMasterDetails(Integer rowCountMasterDetails) {
        this.rowCountMasterDetails = rowCountMasterDetails;
    }
}
