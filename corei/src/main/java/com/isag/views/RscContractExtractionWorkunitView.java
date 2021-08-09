/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mostafa
 */
@Entity
@Table(name = "rsc_contract_extraction_workunit_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscContractExtractionWorkunitView.findAll", query = "SELECT r FROM RscContractExtractionWorkunitView r"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findById", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.id = :id"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByContractQty", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.contractQty = :contractQty"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByCurrentQty", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.currentQty = :currentQty"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByWorkUnitId", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.workUnitId = :workUnitId"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByWorkUnitName", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.workUnitName = :workUnitName"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByWorkUnitCode", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.workUnitCode = :workUnitCode"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findByExtractDate", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.extractDate = :extractDate"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findBySiteId", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.siteId = :siteId"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findBySiteName", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.siteName = :siteName"),
    @NamedQuery(name = "RscContractExtractionWorkunitView.findBySiteCode", query = "SELECT r FROM RscContractExtractionWorkunitView r WHERE r.siteCode = :siteCode")
    })
public class RscContractExtractionWorkunitView implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "contract_qty")
    private BigDecimal contractQty;
    @Column(name = "current_qty")
    private BigDecimal currentQty;
    @Column(name = "work_unit_id")
    private Integer workUnitId;
    @Size(max = 45)
    @Column(name = "work_unit_name")
    private String workUnitName;
    @Size(max = 45)
    @Column(name = "work_unit_code")
    private String workUnitCode;
    @Column(name = "extract_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date extractDate;
    @Column(name = "site_id")
    private Integer siteId;
    @Size(max = 45)
    @Column(name = "site_name")
    private String siteName;
    @Size(max = 45)
    @Column(name = "site_code")
    private String siteCode;
    @Size(max = 45)
    @Column(name = "contract_remarks")
    private String contractRemarks;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "contract_id")
    private Integer contractId;
    @Column(name = "owner_id")
    private Integer ownerId;
    @Size(max = 45)
    @Column(name = "owner_name")
    private String ownerName;
    @Size(max = 45)
    @Column(name = "owner_code")
    private String ownerCode;
  

    public RscContractExtractionWorkunitView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public BigDecimal getContractQty() {
        return contractQty;
    }

    public void setContractQty(BigDecimal contractQty) {
        this.contractQty = contractQty;
    }

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    public Integer getWorkUnitId() {
        return workUnitId;
    }

    public void setWorkUnitId(Integer workUnitId) {
        this.workUnitId = workUnitId;
    }

    public String getWorkUnitName() {
        return workUnitName;
    }

    public void setWorkUnitName(String workUnitName) {
        this.workUnitName = workUnitName;
    }

    public String getWorkUnitCode() {
        return workUnitCode;
    }

    public void setWorkUnitCode(String workUnitCode) {
        this.workUnitCode = workUnitCode;
    }

    public Date getExtractDate() {
        return extractDate;
    }

    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the contractRemarks
     */
    public String getContractRemarks() {
        return contractRemarks;
    }

    /**
     * @param contractRemarks the contractRemarks to set
     */
    public void setContractRemarks(String contractRemarks) {
        this.contractRemarks = contractRemarks;
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the ownerId
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * @param ownerName the ownerName to set
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * @return the ownerCode
     */
    public String getOwnerCode() {
        return ownerCode;
    }

    /**
     * @param ownerCode the ownerCode to set
     */
    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
    
}
