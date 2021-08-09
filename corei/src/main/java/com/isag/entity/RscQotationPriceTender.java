/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "rsc_qotation_price_tender")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscQotationPriceTender.findAll", query = "SELECT r FROM RscQotationPriceTender r")
    , @NamedQuery(name = "RscQotationPriceTender.findById", query = "SELECT r FROM RscQotationPriceTender r WHERE r.id = :id")
    , @NamedQuery(name = "RscQotationPriceTender.findByDocNumber", query = "SELECT r FROM RscQotationPriceTender r WHERE r.docNumber = :docNumber")
    , @NamedQuery(name = "RscQotationPriceTender.findByDate", query = "SELECT r FROM RscQotationPriceTender r WHERE r.date = :date")
    , @NamedQuery(name = "RscQotationPriceTender.findByStatus", query = "SELECT r FROM RscQotationPriceTender r WHERE r.status = :status")
    , @NamedQuery(name = "RscQotationPriceTender.findByRemark", query = "SELECT r FROM RscQotationPriceTender r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscQotationPriceTender.findByCreationDate", query = "SELECT r FROM RscQotationPriceTender r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscQotationPriceTender.findByModificationDate", query = "SELECT r FROM RscQotationPriceTender r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscQotationPriceTender.findBySerial", query = "SELECT r FROM RscQotationPriceTender r WHERE r.serial = :serial")
    , @NamedQuery(name = "RscQotationPriceTender.findByQotationStatus", query = "SELECT r FROM RscQotationPriceTender r WHERE r.qotationStatus = :qotationStatus")
    , @NamedQuery(name = "RscQotationPriceTender.findByPostFlag", query = "SELECT r FROM RscQotationPriceTender r WHERE r.postFlag = :postFlag")})
public class RscQotationPriceTender extends BaseEntity {

    @Size(max = 45)
    @Column(name = "doc_number")
    private String docNumber;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerId;
    @Column(name = "qotation_status")
    private Integer qotationStatus;
    @Column(name = "post_flag")
    private Integer postFlag;
    @JoinColumn(name = "rsc_price_tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscPriceTender rscPriceTenderId;

    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;
    @JoinColumn(name = "sub_contractor", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite subContractor;

    public RscQotationPriceTender() {
    }

    public RscQotationPriceTender(Integer id) {
        this.id = id;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvOrganizationSite getCustomerId() {
        return customerId;
    }

    public void setCustomerId(InvOrganizationSite customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof RscQotationPriceTender)) {
            return false;
        }
        RscQotationPriceTender other = (RscQotationPriceTender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTender[ id=" + id + " ]";
    }

    /**
     * @return the qotationStatus
     */
    public Integer getQotationStatus() {
        return qotationStatus;
    }

    /**
     * @param qotationStatus the qotationStatus to set
     */
    public void setQotationStatus(Integer qotationStatus) {
        this.qotationStatus = qotationStatus;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public RscPriceTender getRscPriceTenderId() {
        return rscPriceTenderId;
    }

    public void setRscPriceTenderId(RscPriceTender rscPriceTenderId) {
        this.rscPriceTenderId = rscPriceTenderId;
    }

    /**
     * @return the tenderId
     */
    public RscTender getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the subContractor
     */
    public InvOrganizationSite getSubContractor() {
        return subContractor;
    }

    /**
     * @param subContractor the subContractor to set
     */
    public void setSubContractor(InvOrganizationSite subContractor) {
        this.subContractor = subContractor;
    }

}
