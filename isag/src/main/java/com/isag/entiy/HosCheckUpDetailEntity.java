/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.CostCenter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.InvOrganizationSite;
import java.math.BigDecimal;

/**
 *
 * @author WIN7
 */
public class HosCheckUpDetailEntity extends BaseEntity {

    private Integer index;
    private Integer serial;
    private String remarks;
    private BigDecimal value;
    private BigDecimal valueLocal;
    private Integer accountCreditId;
    private Integer accountDebitId;
    private Integer adminUnitId;
    private Integer costCenterId;
    private Integer hosCheckUpId;
    private GlBank bankFrom;
    private GlBank bankTo;
    private InvOrganizationSite invOrganizationSite;

    private CostCenter costCenter;
    private GlAdminUnit glAdminUnit;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Integer getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the bankFrom
     */
    public GlBank getBankFrom() {
        return bankFrom;
    }

    /**
     * @param bankFrom the bankFrom to set
     */
    public void setBankFrom(GlBank bankFrom) {
        this.bankFrom = bankFrom;
    }

    /**
     * @return the bankTo
     */
    public GlBank getBankTo() {
        return bankTo;
    }

    /**
     * @param bankTo the bankTo to set
     */
    public void setBankTo(GlBank bankTo) {
        this.bankTo = bankTo;
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSite getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSite invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    public Integer getAccountCreditId() {
        return accountCreditId;
    }

    public void setAccountCreditId(Integer accountCreditId) {
        this.accountCreditId = accountCreditId;
    }

    public Integer getAccountDebitId() {
        return accountDebitId;
    }

    public void setAccountDebitId(Integer accountDebitId) {
        this.accountDebitId = accountDebitId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the costCenter
     */
    public CostCenter getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the glAdminUnit
     */
    public GlAdminUnit getGlAdminUnit() {
        return glAdminUnit;
    }

    /**
     * @param glAdminUnit the glAdminUnit to set
     */
    public void setGlAdminUnit(GlAdminUnit glAdminUnit) {
        this.glAdminUnit = glAdminUnit;
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
     * @return the hosCheckUpId
     */
    public Integer getHosCheckUpId() {
        return hosCheckUpId;
    }

    /**
     * @param hosCheckUpId the hosCheckUpId to set
     */
    public void setHosCheckUpId(Integer hosCheckUpId) {
        this.hosCheckUpId = hosCheckUpId;
    }

}
