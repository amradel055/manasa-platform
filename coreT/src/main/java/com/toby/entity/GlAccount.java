/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import com.toby.define.AccountClassEnum;
import com.toby.define.AccountGroupEnum;
import com.toby.define.AdminUnitDependantEnum;
import com.toby.define.AssisstantAccountDependantEnum;
import com.toby.define.CostCenterDependantEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "gl_account")
@XmlRootElement
public class GlAccount extends BaseEntity {

    @Column(name = "acc_number")
    private Integer accNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "level_acc")
    private Integer levelAcc;
    @Column(name = "type")
    private Integer type;
    @OneToMany(mappedBy = "parentAcc", fetch = FetchType.EAGER)
    private List<GlAccount> childAccounts;
    @JoinColumn(name = "parent_acc", referencedColumnName = "id")
    @ManyToOne
    private GlAccount parentAcc;
    @Column(name = "acc_group")
    @Enumerated(EnumType.STRING)
    private AccountGroupEnum accGroup;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "cost_center")
    @Enumerated(EnumType.STRING)
    private CostCenterDependantEnum costCenter;
    @Column(name = "shot_code")
    private Integer shotCode;
    @Column(name = "administrative_unit")
    @Enumerated(EnumType.STRING)
    private AdminUnitDependantEnum administrativeUnit;
    @Column(name = "assistant_acc")
    @Enumerated(EnumType.STRING)
    private AssisstantAccountDependantEnum assistantAcc;
    @Column(name = "acc_class")
    @Enumerated(EnumType.STRING)
    private AccountClassEnum accClass;
    @Column(name = "remarks")
    private String remarks;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
    @Transient
    private BigDecimal creditAmount;
    @Transient
    private BigDecimal debitAmount;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "general_budget_id_credit", referencedColumnName = "id")
    @ManyToOne
    private GeneralBudgetGuide generalBudgetIdCredit;
    @JoinColumn(name = "general_budget_id_debit", referencedColumnName = "id")
    @ManyToOne
    private GeneralBudgetGuide generalBudgetIdDebit;
    @JoinColumn(name = "general_budget_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralBudgetGuide generalBudgetId;
    @JoinColumn(name = "cost_center_id", referencedColumnName = "id")
    @ManyToOne
    private CostCenter costCenterId;

    /**
     * @return the accNumber
     */
    public Integer getAccNumber() {
        return accNumber;
    }

    /**
     * @param accNumber the accNumber to set
     */
    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the levelAcc
     */
    public Integer getLevelAcc() {
        return levelAcc;
    }

    /**
     * @param levelAcc the levelAcc to set
     */
    public void setLevelAcc(Integer levelAcc) {
        this.levelAcc = levelAcc;
    }

    /**
     * @return the childAccounts
     */
    public List<GlAccount> getChildAccounts() {
        if (childAccounts == null) {
            return (new ArrayList<>());
        }
        return childAccounts;
    }

    /**
     * @param childAccounts the childAccounts to set
     */
    public void setChildAccounts(List<GlAccount> childAccounts) {
        this.childAccounts = childAccounts;
    }

    /**
     * @return the parentAcc
     */
    public GlAccount getParentAcc() {
        return parentAcc;
    }

    /**
     * @param parentAcc the parentAcc to set
     */
    public void setParentAcc(GlAccount parentAcc) {
        this.parentAcc = parentAcc;
    }

    /**
     * @return the accGroup
     */
    public AccountGroupEnum getAccGroup() {
        return accGroup;
    }

    /**
     * @param accGroup the accGroup to set
     */
    public void setAccGroup(AccountGroupEnum accGroup) {
        this.accGroup = accGroup;
    }

    /**
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return the shotCode
     */
    public Integer getShotCode() {
        return shotCode;
    }

    /**
     * @param shotCode the shotCode to set
     */
    public void setShotCode(Integer shotCode) {
        this.shotCode = shotCode;
    }

    /**
     * @return the currencyId
     */
    public Currency getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the creditAmount
     */
    public BigDecimal getCreditAmount() {
        if (creditAmount == null) {
            return BigDecimal.ZERO;
        }
        return creditAmount;
    }

    /**
     * @param creditAmount the creditAmount to set
     */
    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    /**
     * @return the debitAmount
     */
    public BigDecimal getDebitAmount() {
        if (debitAmount == null) {
            return BigDecimal.ZERO;
        }
        return debitAmount;
    }

    /**
     * @param debitAmount the debitAmount to set
     */
    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
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

    /**
     * @return the costCenter
     */
    public CostCenterDependantEnum getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenterDependantEnum costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the administrativeUnit
     */
    public AdminUnitDependantEnum getAdministrativeUnit() {
        return administrativeUnit;
    }

    /**
     * @param administrativeUnit the administrativeUnit to set
     */
    public void setAdministrativeUnit(AdminUnitDependantEnum administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    /**
     * @return the assistantAcc
     */
    public AssisstantAccountDependantEnum getAssistantAcc() {
        return assistantAcc;
    }

    /**
     * @param assistantAcc the assistantAcc to set
     */
    public void setAssistantAcc(AssisstantAccountDependantEnum assistantAcc) {
        this.assistantAcc = assistantAcc;
    }

    /**
     * @return the accClass
     */
    public AccountClassEnum getAccClass() {
        return accClass;
    }

    /**
     * @param accClass the accClass to set
     */
    public void setAccClass(AccountClassEnum accClass) {
        this.accClass = accClass;
    }

    public GeneralBudgetGuide getGeneralBudgetIdCredit() {
        return generalBudgetIdCredit;
    }

    public void setGeneralBudgetIdCredit(GeneralBudgetGuide generalBudgetIdCredit) {
        this.generalBudgetIdCredit = generalBudgetIdCredit;
    }

    public GeneralBudgetGuide getGeneralBudgetIdDebit() {
        return generalBudgetIdDebit;
    }

    public void setGeneralBudgetIdDebit(GeneralBudgetGuide generalBudgetIdDebit) {
        this.generalBudgetIdDebit = generalBudgetIdDebit;
    }

    @Override
    public String toString() {
        return name + " " + accNumber; //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the generalBudgetId
     */
    public GeneralBudgetGuide getGeneralBudgetId() {
        return generalBudgetId;
    }

    /**
     * @param generalBudgetId the generalBudgetId to set
     */
    public void setGeneralBudgetId(GeneralBudgetGuide generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
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
        if (!(object instanceof GlAccount)) {
            return false;
        }
        GlAccount other = (GlAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
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
     * @return the costCenterId
     */
    public CostCenter getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(CostCenter costCenterId) {
        this.costCenterId = costCenterId;
    }
}
