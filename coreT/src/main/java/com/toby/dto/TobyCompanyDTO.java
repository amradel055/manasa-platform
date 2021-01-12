/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author AhmedEssam
 */
public class TobyCompanyDTO extends BaseEntityDTO{
    

     
    private String name; 
    private String nameEn; 
    private String addressEn; 
    private String code;  
    private String core_business; 
    private String responsible; 
    private String phone; 
    private String address; 
    private byte[] logo; 
    private String image; 
    private String fax; 
    private Integer rowCountList; 
    private Integer rowCountMasterDetails;

 


    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getCore_business() {
        return core_business;
    }

    public void setCore_business(String core_business) {
        this.core_business = core_business;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
 
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

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
     * @return the rowCountMasterDetails
     */
    public Integer getRowCountMasterDetails() {
        return rowCountMasterDetails;
    }

    /**
     * @param rowCountMasterDetails the rowCountMasterDetails to set
     */
    public void setRowCountMasterDetails(Integer rowCountMasterDetails) {
        this.rowCountMasterDetails = rowCountMasterDetails;
    }

    /**
     * @return the rowCountList
     */
    public Integer getRowCountList() {
        return rowCountList;
    }

    /**
     * @param rowCountList the rowCountList to set
     */
    public void setRowCountList(Integer rowCountList) {
        this.rowCountList = rowCountList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
     /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the addressEn
     */
    public String getAddressEn() {
        return addressEn;
    }

    /**
     * @param addressEn the addressEn to set
     */
    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
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
        if (!(object instanceof TobyCompanyDTO)) {
            return false;
        }
        TobyCompanyDTO other = (TobyCompanyDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }


}
