/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "general_journal")
public class GeneralJournal extends BaseEntity {

    @Column(name = "general_number")
    private int generalNumber;

    @Column(name = "general_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalData;

    @JoinColumn(name = "symbol_id", referencedColumnName = "id")
    @ManyToOne()
    private Symbol generalType;

    @Column(name = "general_statement")
    private String generalStatement;

    @Column(name = "general_decument")
    private Integer generalDecument;

    @Column(name = "post_flag_review")
    private String postFlagReview;

    @Column(name = "post_flag")
    private boolean post_flag = Boolean.FALSE;

    @Column(name = "serial")
    private Integer serial;

    @JoinColumn(name = "gl_year", referencedColumnName = "id")
    @ManyToOne()
    private GlYear glYear;

    @Size(max = 450)
    @Column(name = "mac_id")
    private String macId;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;
    
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne()
    private TobyUser deletedBy;
    
    @Column(name = "delete_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    @Transient
    private boolean userModifyAbility;
    
    @Transient
    private GeneralJournalDetails generalJournalDetails ;

    public Integer getGeneralDecument() {
        return generalDecument;
    }

    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournal)) {
            return false;
        }
        GeneralJournal other = (GeneralJournal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
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
        if (generalData == null) {
            generalData = new Date();
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
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getGeneralType() {
        return generalType;
    }

    public void setGeneralType(Symbol generalType) {
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

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the glYear
     */
    public GlYear getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(GlYear glYear) {
        this.glYear = glYear;
    }

    /**
     * @return the postFlagReview
     */
    public String getPostFlagReview() {
        return postFlagReview;
    }

    /**
     * @param postFlagReview the postFlagReview to set
     */
    public void setPostFlagReview(String postFlagReview) {
        this.postFlagReview = postFlagReview;
    }

    /**
     * @return the userModifyAbility
     */
    public boolean isUserModifyAbility() {
        return userModifyAbility;
    }

    /**
     * @param userModifyAbility the userModifyAbility to set
     */
    public void setUserModifyAbility(boolean userModifyAbility) {
        this.userModifyAbility = userModifyAbility;
    }

    /**
     * @return the deletedBy
     */
    public TobyUser getDeletedBy() {
        return deletedBy;
    }

    /**
     * @param deletedBy the deletedBy to set
     */
    public void setDeletedBy(TobyUser deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * @return the deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * @param deleteDate the deleteDate to set
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * @return the generalJournalDetails
     */
    public GeneralJournalDetails getGeneralJournalDetails() {
        return generalJournalDetails;
    }

    /**
     * @param generalJournalDetails the generalJournalDetails to set
     */
    public void setGeneralJournalDetails(GeneralJournalDetails generalJournalDetails) {
        this.generalJournalDetails = generalJournalDetails;
    }

}
