/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "hos_doctor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HosDoctor.findAll", query = "SELECT h FROM HosDoctor h")
    , @NamedQuery(name = "HosDoctor.findById", query = "SELECT h FROM HosDoctor h WHERE h.id = :id")
    , @NamedQuery(name = "HosDoctor.findByCode", query = "SELECT h FROM HosDoctor h WHERE h.code = :code")
    , @NamedQuery(name = "HosDoctor.findByName", query = "SELECT h FROM HosDoctor h WHERE h.name = :name")
    , @NamedQuery(name = "HosDoctor.findByLevel", query = "SELECT h FROM HosDoctor h WHERE h.level = :level")
    , @NamedQuery(name = "HosDoctor.findByActive", query = "SELECT h FROM HosDoctor h WHERE h.active = :active")
    , @NamedQuery(name = "HosDoctor.findByServicePrice", query = "SELECT h FROM HosDoctor h WHERE h.servicePrice = :servicePrice")
    , @NamedQuery(name = "HosDoctor.findByCreationDate", query = "SELECT h FROM HosDoctor h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HosDoctor.findByModificationDate", query = "SELECT h FROM HosDoctor h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HosDoctor.findByCommission", query = "SELECT h FROM HosDoctor h WHERE h.commission = :commission")})
public class HosDoctor extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 450)
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "active")
    private Boolean active;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "commission")
    private BigDecimal commission;
    @Column(name = "service_price")
    private BigDecimal servicePrice;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @OneToMany(mappedBy = "parent")
    private Collection<HosDoctor> hosDoctorCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private HosDoctor parent;
    @OneToMany(mappedBy = "hosDoctorId")
    private Collection<HosCheckUp> hosCheckUpCollection;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<HosDoctor> childNodes;

    public HosDoctor() {
    }

    public HosDoctor(Integer id) {
        this.id = id;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    @XmlTransient
    public Collection<HosDoctor> getHosDoctorCollection() {
        return hosDoctorCollection;
    }

    public void setHosDoctorCollection(Collection<HosDoctor> hosDoctorCollection) {
        this.hosDoctorCollection = hosDoctorCollection;
    }

    public HosDoctor getParent() {
        return parent;
    }

    public void setParent(HosDoctor parent) {
        this.parent = parent;
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
        if (!(object instanceof HosDoctor)) {
            return false;
        }
        HosDoctor other = (HosDoctor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the childNodes
     */
    public List<HosDoctor> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(List<HosDoctor> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return the servicePrice
     */
    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    /**
     * @param servicePrice the servicePrice to set
     */
    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

}
