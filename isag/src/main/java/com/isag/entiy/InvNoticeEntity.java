/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.dto.SymbolDTO;
import com.isag.entity.GlAccount;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InvOrganizationSite;
import com.isag.views.InstTransactionTemp;
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
    private InvOrganizationSite organizationSiteId;
    private BigDecimal amount = BigDecimal.ZERO;
    private Date date;
    private Boolean markEdit;
    private InstContract instContract;
    private InstContractDetail instContractDetail;
    private InstTransactionTemp instTransactionTemp;
    private SymbolDTO noticeNaming;

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
     * @return the organizationSiteId
     */
    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
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
     * @return the instContract
     */
    public InstContract getInstContract() {
        return instContract;
    }

    /**
     * @param instContract the instContract to set
     */
    public void setInstContract(InstContract instContract) {
        this.instContract = instContract;
    }

    /**
     * @return the instTransactionTemp
     */
    public InstTransactionTemp getInstTransactionTemp() {
        return instTransactionTemp;
    }

    /**
     * @param instTransactionTemp the instTransactionTemp to set
     */
    public void setInstTransactionTemp(InstTransactionTemp instTransactionTemp) {
        this.instTransactionTemp = instTransactionTemp;
    }

    /**
     * @return the instContractDetail
     */
    public InstContractDetail getInstContractDetail() {
        return instContractDetail;
    }

    /**
     * @param instContractDetail the instContractDetail to set
     */
    public void setInstContractDetail(InstContractDetail instContractDetail) {
        this.instContractDetail = instContractDetail;
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
}
