/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "hos_patient_registration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HosPatientRegistration.findAll", query = "SELECT h FROM HosPatientRegistration h")
    , @NamedQuery(name = "HosPatientRegistration.findById", query = "SELECT h FROM HosPatientRegistration h WHERE h.id = :id")
    , @NamedQuery(name = "HosPatientRegistration.findByCode", query = "SELECT h FROM HosPatientRegistration h WHERE h.code = :code")
    , @NamedQuery(name = "HosPatientRegistration.findByName", query = "SELECT h FROM HosPatientRegistration h WHERE h.name = :name")
    , @NamedQuery(name = "HosPatientRegistration.findByType", query = "SELECT h FROM HosPatientRegistration h WHERE h.type = :type")
    , @NamedQuery(name = "HosPatientRegistration.findByPhone", query = "SELECT h FROM HosPatientRegistration h WHERE h.phone = :phone")
    , @NamedQuery(name = "HosPatientRegistration.findByFax", query = "SELECT h FROM HosPatientRegistration h WHERE h.fax = :fax")
    , @NamedQuery(name = "HosPatientRegistration.findByEmail", query = "SELECT h FROM HosPatientRegistration h WHERE h.email = :email")
    , @NamedQuery(name = "HosPatientRegistration.findByMobile", query = "SELECT h FROM HosPatientRegistration h WHERE h.mobile = :mobile")
    , @NamedQuery(name = "HosPatientRegistration.findByAddress", query = "SELECT h FROM HosPatientRegistration h WHERE h.address = :address")
    , @NamedQuery(name = "HosPatientRegistration.findByPostBox", query = "SELECT h FROM HosPatientRegistration h WHERE h.postBox = :postBox")
    , @NamedQuery(name = "HosPatientRegistration.findByDiscount", query = "SELECT h FROM HosPatientRegistration h WHERE h.discount = :discount")
    , @NamedQuery(name = "HosPatientRegistration.findByRemark", query = "SELECT h FROM HosPatientRegistration h WHERE h.remark = :remark")
    , @NamedQuery(name = "HosPatientRegistration.findByModificationDate", query = "SELECT h FROM HosPatientRegistration h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HosPatientRegistration.findByCreationDate", query = "SELECT h FROM HosPatientRegistration h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HosPatientRegistration.findByActive", query = "SELECT h FROM HosPatientRegistration h WHERE h.active = :active")
    , @NamedQuery(name = "HosPatientRegistration.findByZip", query = "SELECT h FROM HosPatientRegistration h WHERE h.zip = :zip")
    , @NamedQuery(name = "HosPatientRegistration.findByPhone2", query = "SELECT h FROM HosPatientRegistration h WHERE h.phone2 = :phone2")
    , @NamedQuery(name = "HosPatientRegistration.findByDiscountRatio", query = "SELECT h FROM HosPatientRegistration h WHERE h.discountRatio = :discountRatio")
    , @NamedQuery(name = "HosPatientRegistration.findByBearRatio", query = "SELECT h FROM HosPatientRegistration h WHERE h.bearRatio = :bearRatio")
    , @NamedQuery(name = "HosPatientRegistration.findByNationalId", query = "SELECT h FROM HosPatientRegistration h WHERE h.nationalId = :nationalId")
    , @NamedQuery(name = "HosPatientRegistration.findByPatientHistory", query = "SELECT h FROM HosPatientRegistration h WHERE h.patientHistory = :patientHistory")
    , @NamedQuery(name = "HosPatientRegistration.findByBirthday", query = "SELECT h FROM HosPatientRegistration h WHERE h.birthday = :birthday")
    , @NamedQuery(name = "HosPatientRegistration.findByAge", query = "SELECT h FROM HosPatientRegistration h WHERE h.age = :age")
    , @NamedQuery(name = "HosPatientRegistration.findByHeight", query = "SELECT h FROM HosPatientRegistration h WHERE h.height = :height")
    , @NamedQuery(name = "HosPatientRegistration.findByWeight", query = "SELECT h FROM HosPatientRegistration h WHERE h.weight = :weight")
    , @NamedQuery(name = "HosPatientRegistration.findByGender", query = "SELECT h FROM HosPatientRegistration h WHERE h.gender = :gender")})
public class HosPatientRegistration extends BaseEntity {

    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Integer type;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "phone")
    private String phone;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "fax")
    private String fax;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 45)
    @Column(name = "mobile")
    private String mobile;
    @Size(max = 450)
    @Column(name = "address")
    private String address;
    @Size(max = 45)
    @Column(name = "post_box")
    private String postBox;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private BigDecimal discount;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "active")
    private Integer active;
    @Size(max = 45)
    @Column(name = "zip")
    private String zip;
    @Size(max = 45)
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "discount_ratio")
    private BigDecimal discountRatio;
    @Column(name = "bear_ratio")
    private BigDecimal bearRatio;
    @Size(max = 45)
    @Column(name = "national_id")
    private String nationalId;
    @Size(max = 450)
    @Column(name = "patient_history")
    private String patientHistory;
    @Column(name = "birthday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    @Column(name = "age")
    private Integer age;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol countryId;

    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;

    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private HosPatientRegistration parent;
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol regionId;
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol nationalityId;
    @OneToMany(mappedBy = "hosPatientRegisterationId")
    private Collection<HosCheckUp> hosCheckUpCollection;

    public HosPatientRegistration() {
    }

    public HosPatientRegistration(Integer id) {
        this.id = id;
    }

    public HosPatientRegistration(Integer id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostBox() {
        return postBox;
    }

    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    public BigDecimal getBearRatio() {
        return bearRatio;
    }

    public void setBearRatio(BigDecimal bearRatio) {
        this.bearRatio = bearRatio;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPatientHistory() {
        return patientHistory;
    }

    public void setPatientHistory(String patientHistory) {
        this.patientHistory = patientHistory;
    }

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getCountryId() {
        return countryId;
    }

    public void setCountryId(Symbol countryId) {
        this.countryId = countryId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public HosPatientRegistration getParent() {
        return parent;
    }

    public void setParent(HosPatientRegistration parent) {
        this.parent = parent;
    }

    public Symbol getRegionId() {
        return regionId;
    }

    public void setRegionId(Symbol regionId) {
        this.regionId = regionId;
    }

    @XmlTransient
    public Collection<HosCheckUp> getHosCheckUpCollection() {
        return hosCheckUpCollection;
    }

    public void setHosCheckUpCollection(Collection<HosCheckUp> hosCheckUpCollection) {
        this.hosCheckUpCollection = hosCheckUpCollection;
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
        if (!(object instanceof HosPatientRegistration)) {
            return false;
        }
        HosPatientRegistration other = (HosPatientRegistration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code ; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the gender
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * @return the height
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the nationalityId
     */
    public Symbol getNationalityId() {
        return nationalityId;
    }

    /**
     * @param nationalityId the nationalityId to set
     */
    public void setNationalityId(Symbol nationalityId) {
        this.nationalityId = nationalityId;
    }

}
