/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author amr
 */
public class InvNoticeDTO extends BaseEntityDTO {

    private Date date;
    private BigDecimal value;
    private String remark;
    private Integer type;
    private String typeName;
    private BigDecimal rate;
    private GlAccountDTO contrastAccountId;
    private InvOrganizationSiteDTO organizationSiteId;
    private Integer serial;
    private Integer postFlag;
    private GeneralJournalDTO generalJournalId;
    private SymbolDTO noticeNaming;
    private Integer instContractSerial;

    public InvNoticeDTO() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public GlAccountDTO getContrastAccountId() {
        return contrastAccountId;
    }

    public void setContrastAccountId(GlAccountDTO contrastAccountId) {
        this.contrastAccountId = contrastAccountId;
    }

    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
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
        if (!(object instanceof InvNoticeDTO)) {
            return false;
        }
        InvNoticeDTO other = (InvNoticeDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvNotice[ id=" + id + " ]";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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

    /**
     * @return the generalJournalId
     */
    public GeneralJournalDTO getGeneralJournalId() {
        return generalJournalId;
    }

    /**
     * @param generalJournalId the generalJournalId to set
     */
    public void setGeneralJournalId(GeneralJournalDTO generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the noticeNaming
     */
    public SymbolDTO getNoticeNaming() {
        return noticeNaming;
    }

    /**
     * @param noticeNaming the noticeNaming to set
     */
    public void setNoticeNaming(SymbolDTO noticeNaming) {
        this.noticeNaming = noticeNaming;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the instContractSerial
     */
    public Integer getInstContractSerial() {
        return instContractSerial;
    }

    /**
     * @param instContractSerial the instContractSerial to set
     */
    public void setInstContractSerial(Integer instContractSerial) {
        this.instContractSerial = instContractSerial;
    }

}
