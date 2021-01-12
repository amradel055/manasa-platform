/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "_history_general_journal")
public class HistoryGeneralJournal implements Serializable{

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
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    /**
     * @return the companyId
     */
    public TobyCompany getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the createdBy
     */
    public TobyUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the modifiedBy
     */
    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Transient
    private Boolean markEdit;

    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne()
    private TobyCompany companyId;

    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne()
    private TobyUser createdBy;

    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne()
    private TobyUser modifiedBy;

    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "general_number")
    private int generalNumber;
   
    @Column(name = "general_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalData;
    
    @Column(name = "symbol_id")
    private Integer generalType;

    @Column(name = "general_statement")
    private String generalStatement;

    @Column(name = "general_decument")
    private Integer generalDecument;
    
     @Column(name = "post_flag")
     private boolean post_flag = Boolean.FALSE;
     
    @Column(name = "symbol_id_new")
    private int symbolIdNew;
     
    @Column(name = "general_statement_new")
    private String generalStatementNew;
    
     @Column(name = "general_data_new")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalDataNew;
     
     
    @Column(name = "action")
    private int rowStatus;
    
    @Column(name = "modified_by_new")
    private Integer modifiedByNew; 
    
    @Column(name = "modification_date_new")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDateNew;
     
    @Column(name = "branch_id")
    private Integer branchId;

    public Integer getGeneralDecument() {
        return generalDecument;
    }

    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryGeneralJournal)) {
            return false;
        }
        HistoryGeneralJournal other = (HistoryGeneralJournal) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + getId() + " ]";
    }

    /**
     * @return the generalNumber
     */
    public int getGeneralNumber() {
        return generalNumber;
    }

    /**
     * @param generalNumber the generalNumber to set
     */
    public void setGeneralNumber(int generalNumber) {
        this.generalNumber = generalNumber;
    }

    /**
     * @return the generalData
     */
    public Date getGeneralData() {
        if(generalData == null)
        {
          generalData= new Date();
        }
        return generalData;
    }

    /**
     * @param generalData the generalData to set
     */
    public void setGeneralData(Date generalData) {
        this.generalData = generalData;
    }

    /**
     * @return the generalStatement
     */
    public String getGeneralStatement() {
        return generalStatement;
    }

    /**
     * @param generalStatement the generalStatement to set
     */
    public void setGeneralStatement(String generalStatement) {
        this.generalStatement = generalStatement;
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

    public Integer getGeneralType() {
        return generalType;
    }

    public void setGeneralType(Integer generalType) {
        this.generalType = generalType;
    }

    /**
     * @return the post_flag
     */
    public boolean isPost_flag() {
        return post_flag;
    }

    /**
     * @param post_flag the post_flag to set
     */
    public void setPost_flag(boolean post_flag) {
        this.post_flag = post_flag;
    }

    /**
     * @return the rowStatus
     */
    public int getRowStatus() {
        return rowStatus;
    }

    /**
     * @param rowStatus the rowStatus to set
     */
    public void setRowStatus(int rowStatus) {
        this.rowStatus = rowStatus;
    }


    /**
     * @return the modificationDateNew
     */
    public Date getModificationDateNew() {
        return modificationDateNew;
    }

    /**
     * @param modificationDateNew the modificationDateNew to set
     */
    public void setModificationDateNew(Date modificationDateNew) {
        this.modificationDateNew = modificationDateNew;
    }

    /**
     * @return the symbolIdNew
     */
    public int getSymbolIdNew() {
        return symbolIdNew;
    }

    /**
     * @param symbolIdNew the symbolIdNew to set
     */
    public void setSymbolIdNew(int symbolIdNew) {
        this.symbolIdNew = symbolIdNew;
    }

    /**
     * @return the generalStatementNew
     */
    public String getGeneralStatementNew() {
        return generalStatementNew;
    }

    /**
     * @param generalStatementNew the generalStatementNew to set
     */
    public void setGeneralStatementNew(String generalStatementNew) {
        this.generalStatementNew = generalStatementNew;
    }

    /**
     * @return the generalDataNew
     */
    public Date getGeneralDataNew() {
        return generalDataNew;
    }

    /**
     * @param generalDataNew the generalDataNew to set
     */
    public void setGeneralDataNew(Date generalDataNew) {
        this.generalDataNew = generalDataNew;
    }

    /**
     * @return the modifiedByNew
     */
    public Integer getModifiedByNew() {
        return modifiedByNew;
    }

    /**
     * @param modifiedByNew the modifiedByNew to set
     */
    public void setModifiedByNew(Integer modifiedByNew) {
        this.modifiedByNew = modifiedByNew;
    }

}
