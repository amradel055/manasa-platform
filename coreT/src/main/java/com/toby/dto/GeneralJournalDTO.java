/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class GeneralJournalDTO extends BaseEntityDTO {

    private int generalNumber;
    private Date generalData;
    private SymbolDTO generalType;
    private String generalStatement;
    private Integer generalDecument;
    private String postFlagReview;
    private boolean post_flag = Boolean.FALSE;
    private Integer serial;
    private GlYearDTO glYear;
    private String macId;
    private boolean userModifyAbility;
    private Integer companyId;
    private Integer modifiedBy ; 
    private Date modificationDate;
    private TobyUserDTO created_by;
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

    public SymbolDTO getGeneralType() {
        return generalType;
    }

    public void setGeneralType(SymbolDTO generalType) {
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
    public GlYearDTO getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(GlYearDTO glYear) {
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
     * @return the generalDecument
     */
    public Integer getGeneralDecument() {
        return generalDecument;
    }

    /**
     * @param generalDecument the generalDecument to set
     */
    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalDTO)) {
            return false;
        }
        GeneralJournalDTO other = (GeneralJournalDTO) object;
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
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the modifiedBy
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(Integer modifiedBy) {
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
     * @return the created_by
     */
    public TobyUserDTO getCreated_by() {
        return created_by;
    }

    /**
     * @param created_by the created_by to set
     */
    public void setCreated_by(TobyUserDTO created_by) {
        this.created_by = created_by;
    }

}
