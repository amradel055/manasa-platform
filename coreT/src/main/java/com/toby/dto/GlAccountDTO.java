/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.define.AccountClassEnum;
import com.toby.define.AccountGroupEnum;
import com.toby.define.AdminUnitDependantEnum;
import com.toby.define.AssisstantAccountDependantEnum;
import com.toby.define.CostCenterDependantEnum;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */

public class GlAccountDTO extends BaseEntityDTO {

    private Integer accNumber;
    private String name;
    private Integer levelAcc;
    private Integer type;
    private Integer shotCode;
    private AccountGroupEnum accGroup;
    private boolean status;
    private CostCenterDependantEnum costCenter;
    private AdminUnitDependantEnum administrativeUnit;
    private AssisstantAccountDependantEnum assistantAcc;
    private AccountClassEnum accClass;
    private Integer generalBudgetId;
    private Integer generalBudgetIdCredit;
    private Integer generalBudgetIdDebit;
    private Integer currencyId;
    private GlAccountDTO parentAcc;
    private Integer parentAccId;

    public Integer getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevelAcc() {
        return levelAcc;
    }

    public void setLevelAcc(Integer levelAcc) {
        this.levelAcc = levelAcc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getShotCode() {
        return shotCode;
    }

    public void setShotCode(Integer shotCode) {
        this.shotCode = shotCode;
    }

    public AccountGroupEnum getAccGroup() {
        return accGroup;
    }

    public void setAccGroup(AccountGroupEnum accGroup) {
        this.accGroup = accGroup;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CostCenterDependantEnum getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(CostCenterDependantEnum costCenter) {
        this.costCenter = costCenter;
    }

    public AdminUnitDependantEnum getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(AdminUnitDependantEnum administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    public AssisstantAccountDependantEnum getAssistantAcc() {
        return assistantAcc;
    }

    public void setAssistantAcc(AssisstantAccountDependantEnum assistantAcc) {
        this.assistantAcc = assistantAcc;
    }

    public AccountClassEnum getAccClass() {
        return accClass;
    }

    public void setAccClass(AccountClassEnum accClass) {
        this.accClass = accClass;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public GlAccountDTO getParentAcc() {
        return parentAcc;
    }

    public void setParentAcc(GlAccountDTO parentAcc) {
        this.parentAcc = parentAcc;
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
        if (!(object instanceof GlAccountDTO)) {
            return false;
        }
        GlAccountDTO other = (GlAccountDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return accNumber + " - " + name;
    }

    /**
     * @return the generalBudgetId
     */
    public Integer getGeneralBudgetId() {
        return generalBudgetId;
    }

    /**
     * @param generalBudgetId the generalBudgetId to set
     */
    public void setGeneralBudgetId(Integer generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
    }

    /**
     * @return the generalBudgetIdCredit
     */
    public Integer getGeneralBudgetIdCredit() {
        return generalBudgetIdCredit;
    }

    /**
     * @param generalBudgetIdCredit the generalBudgetIdCredit to set
     */
    public void setGeneralBudgetIdCredit(Integer generalBudgetIdCredit) {
        this.generalBudgetIdCredit = generalBudgetIdCredit;
    }

    /**
     * @return the generalBudgetIdDebit
     */
    public Integer getGeneralBudgetIdDebit() {
        return generalBudgetIdDebit;
    }

    /**
     * @param generalBudgetIdDebit the generalBudgetIdDebit to set
     */
    public void setGeneralBudgetIdDebit(Integer generalBudgetIdDebit) {
        this.generalBudgetIdDebit = generalBudgetIdDebit;
    }

    /**
     * @return the parentAccId
     */
    public Integer getParentAccId() {
        return parentAccId;
    }

    /**
     * @param parentAccId the parentAccId to set
     */
    public void setParentAccId(Integer parentAccId) {
        this.parentAccId = parentAccId;
    }

}
