/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hq004
 */
@Entity
@Table(name = "isag_company")
@NamedQueries({
        @NamedQuery(name = "IsagCompany.findAll", query = "SELECT i FROM IsagCompany i")})
public class IsagCompany extends BaseEntity {

   

    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Size(max = 200)
    @Column(name = "name_en")
    private String nameEn;
    
    @Size(max = 200)
    @Column(name = "address_en")
    private String addressEn;

    @Size(max = 3)
    @Column(name = "code")
    private String code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId", fetch = FetchType.LAZY)
    private List<IsagUser> isagUserList;

    @Size(max = 200)
    @Column(name = "core_business")
    private String core_business;

    @Size(max = 200)
    @Column(name = "responsible")
    private String responsible;

    @Size(max = 200)
    @Column(name = "phone")
    private String phone;

    @Size(max = 200)
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Size(max = 450)
    @Column(name = "image")
    private String image;

    @Size(max = 200)
    @Column(name = "fax")
    private String fax;

    @Column(name = "row_count_list")
    private Integer rowCountList;

    @Column(name = "row_count_master_details")
    private Integer rowCountMasterDetails;


    @OneToMany(mappedBy = "companyId")
    private Collection<IsagUserYear> isagUserYearCollection;


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

    public List<IsagUser> getIsagUserList() {
        return isagUserList;
    }

    public void setIsagUserList(List<IsagUser> isagUserList) {
        this.isagUserList = isagUserList;
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

    @XmlTransient
    public Collection<IsagUserYear> getIsagUserYearCollection() {
        return isagUserYearCollection;
    }

    public void setIsagUserYearCollection(Collection<IsagUserYear> isagUserYearCollection) {
        this.isagUserYearCollection = isagUserYearCollection;
    }

    
}
