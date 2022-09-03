/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.util.Date;
import java.util.List;

/**
 *
 * @author AhmedEssam
 */
public class GeneralJournalDTOAPI extends BaseEntityDTOAPI {

    private Integer id;
    private int generalNumber;
    private Date generalData;
    private String generalTypeName;
    private Integer generalTypeId;
    private String generalStatement;
    private Integer generalDecument;
    private String postFlagReview;
    private boolean post_flag = Boolean.FALSE;
    private Integer serial;
    private String glYearName;
    private Integer glYearId;
    private String macId;
    private boolean userModifyAbility;
    private Integer companyId;
    private Integer modifiedBy;
    private Date modificationDate;
    private String createdByName;
    private Integer documentId;
    private List<GeneralJournalDetailsDTOAPI> detailDTOList;

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
        if (!(object instanceof GeneralJournalDTOAPI)) {
            return false;
        }
        GeneralJournalDTOAPI other = (GeneralJournalDTOAPI) object;
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
     * @return the documentId
     */
    public Integer getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the glYearId
     */
    public Integer getGlYearId() {
        return glYearId;
    }

    /**
     * @param glYearId the glYearId to set
     */
    public void setGlYearId(Integer glYearId) {
        this.glYearId = glYearId;
    }

    /**
     * @return the generalTypeId
     */
    public Integer getGeneralTypeId() {
        return generalTypeId;
    }

    /**
     * @param generalTypeId the generalTypeId to set
     */
    public void setGeneralTypeId(Integer generalTypeId) {
        this.generalTypeId = generalTypeId;
    }

    /**
     * @return the createdByName
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * @param createdByName the createdByName to set
     */
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    /**
     * @return the glYearName
     */
    public String getGlYearName() {
        return glYearName;
    }

    /**
     * @param glYearName the glYearName to set
     */
    public void setGlYearName(String glYearName) {
        this.glYearName = glYearName;
    }

    /**
     * @return the generalTypeName
     */
    public String getGeneralTypeName() {
        return generalTypeName;
    }

    /**
     * @param generalTypeName the generalTypeName to set
     */
    public void setGeneralTypeName(String generalTypeName) {
        this.generalTypeName = generalTypeName;
    }

    /**
     * @return the detailDTOList
     */
    public List<GeneralJournalDetailsDTOAPI> getDetailDTOList() {
        return detailDTOList;
    }

    /**
     * @param detailDTOList the detailDTOList to set
     */
    public void setDetailDTOList(List<GeneralJournalDetailsDTOAPI> detailDTOList) {
        this.detailDTOList = detailDTOList;
    }

}
