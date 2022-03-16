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
@Table(name = "rsc_price_tender")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscPriceTender.findAll", query = "SELECT r FROM RscPriceTender r")
    , @NamedQuery(name = "RscPriceTender.findById", query = "SELECT r FROM RscPriceTender r WHERE r.id = :id")
    , @NamedQuery(name = "RscPriceTender.findByDocNumber", query = "SELECT r FROM RscPriceTender r WHERE r.docNumber = :docNumber")
    , @NamedQuery(name = "RscPriceTender.findByDate", query = "SELECT r FROM RscPriceTender r WHERE r.date = :date")
    , @NamedQuery(name = "RscPriceTender.findByStatus", query = "SELECT r FROM RscPriceTender r WHERE r.status = :status")
    , @NamedQuery(name = "RscPriceTender.findByRemark", query = "SELECT r FROM RscPriceTender r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscPriceTender.findByCreationDate", query = "SELECT r FROM RscPriceTender r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscPriceTender.findByModificationDate", query = "SELECT r FROM RscPriceTender r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscPriceTender.findBySerial", query = "SELECT r FROM RscPriceTender r WHERE r.serial = :serial")})
public class RscPriceTender extends BaseEntity {

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
   
    @JoinColumn(name = "general_consultant", referencedColumnName = "id")
    @ManyToOne
    private Symbol generalConsultant;
    @JoinColumn(name = "project_manager", referencedColumnName = "id")
    @ManyToOne
    private Symbol projectManager;
    @JoinColumn(name = "profissional_bussiness_consultant", referencedColumnName = "id")
    @ManyToOne
    private Symbol profissionalBussinessConsultant;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;

    public RscPriceTender() {
    }

    public RscPriceTender(Integer id) {
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

    public Symbol getGeneralConsultant() {
        return generalConsultant;
    }

    public void setGeneralConsultant(Symbol generalConsultant) {
        this.generalConsultant = generalConsultant;
    }

    public Symbol getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Symbol projectManager) {
        this.projectManager = projectManager;
    }

    public Symbol getProfissionalBussinessConsultant() {
        return profissionalBussinessConsultant;
    }

    public void setProfissionalBussinessConsultant(Symbol profissionalBussinessConsultant) {
        this.profissionalBussinessConsultant = profissionalBussinessConsultant;
    }

    public RscTender getTenderId() {
        return tenderId;
    }

    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
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
        if (!(object instanceof RscPriceTender)) {
            return false;
        }
        RscPriceTender other = (RscPriceTender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTender[ id=" + id + " ]";
    }
    
}
