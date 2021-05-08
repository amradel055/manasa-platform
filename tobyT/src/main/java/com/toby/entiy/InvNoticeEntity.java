/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.GlAccount;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class InvNoticeEntity {

    private Integer id;
    private Integer serial;
    private Integer branchId;
    private GlAccount accountId;
    private Integer type;
    private String typeName;
    private String remarks;
    private BigDecimal amount = BigDecimal.ZERO;
    private Date date;
    private Boolean markEdit;
    private SymbolDTO noticeNaming;
    private Integer postFlag;
    private InvOrganizationSiteDTO organizationSiteIdTo;
    private InvOrganizationSiteDTO organizationSiteId;
    private Integer invoiceId;
    private Integer invoiceSerial;

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
     * @return the accountId
     */
    public GlAccount getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * @return the organizationSiteIdTo
     */
    public InvOrganizationSiteDTO getOrganizationSiteIdTo() {
        return organizationSiteIdTo;
    }

    /**
     * @param organizationSiteIdTo the organizationSiteIdTo to set
     */
    public void setOrganizationSiteIdTo(InvOrganizationSiteDTO organizationSiteIdTo) {
        this.organizationSiteIdTo = organizationSiteIdTo;
    }

    /**
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

}
