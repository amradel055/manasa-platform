/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "gl_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlAccount.findAll", query = "SELECT g FROM GlAccount g")
    , @NamedQuery(name = "GlAccount.findById", query = "SELECT g FROM GlAccount g WHERE g.id = :id")
    , @NamedQuery(name = "GlAccount.findByAccNumber", query = "SELECT g FROM GlAccount g WHERE g.accNumber = :accNumber")
    , @NamedQuery(name = "GlAccount.findByName", query = "SELECT g FROM GlAccount g WHERE g.name = :name")
    , @NamedQuery(name = "GlAccount.findByLevelAcc", query = "SELECT g FROM GlAccount g WHERE g.levelAcc = :levelAcc")
    , @NamedQuery(name = "GlAccount.findByType", query = "SELECT g FROM GlAccount g WHERE g.type = :type")
    , @NamedQuery(name = "GlAccount.findByShotCode", query = "SELECT g FROM GlAccount g WHERE g.shotCode = :shotCode")
    , @NamedQuery(name = "GlAccount.findByAccGroup", query = "SELECT g FROM GlAccount g WHERE g.accGroup = :accGroup")
    , @NamedQuery(name = "GlAccount.findByStatus", query = "SELECT g FROM GlAccount g WHERE g.status = :status")
    , @NamedQuery(name = "GlAccount.findByCostCenter", query = "SELECT g FROM GlAccount g WHERE g.costCenter = :costCenter")
    , @NamedQuery(name = "GlAccount.findByAdministrativeUnit", query = "SELECT g FROM GlAccount g WHERE g.administrativeUnit = :administrativeUnit")
    , @NamedQuery(name = "GlAccount.findByAssistantAcc", query = "SELECT g FROM GlAccount g WHERE g.assistantAcc = :assistantAcc")
    , @NamedQuery(name = "GlAccount.findByAccClass", query = "SELECT g FROM GlAccount g WHERE g.accClass = :accClass")
    , @NamedQuery(name = "GlAccount.findByCreationDate", query = "SELECT g FROM GlAccount g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "GlAccount.findByModificationDate", query = "SELECT g FROM GlAccount g WHERE g.modificationDate = :modificationDate")})
public class GlAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acc_number")
    private int accNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "name")
    private String name;
    @Column(name = "level_acc")
    private Integer levelAcc;
    @Column(name = "type")
    private Integer type;
    @Column(name = "shot_code")
    private Integer shotCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "acc_group")
    private String accGroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cost_center")
    private String costCenter;
    @Size(max = 20)
    @Column(name = "administrative_unit")
    private String administrativeUnit;
    @Size(max = 20)
    @Column(name = "assistant_acc")
    private String assistantAcc;
    @Size(max = 20)
    @Column(name = "acc_class")
    private String accClass;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(mappedBy = "accountId")
    private Collection<Symbol> symbolCollection;
    @OneToMany(mappedBy = "accountGroup")
    private Collection<Generalbudgetguide> generalbudgetguideCollection;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IsagCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IsagUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser modifiedBy;
    @JoinColumn(name = "general_budget_id", referencedColumnName = "id")
    @ManyToOne
    private Generalbudgetguide generalBudgetId;
    @JoinColumn(name = "general_budget_id_credit", referencedColumnName = "id")
    @ManyToOne
    private Generalbudgetguide generalBudgetIdCredit;
    @JoinColumn(name = "general_budget_id_debit", referencedColumnName = "id")
    @ManyToOne
    private Generalbudgetguide generalBudgetIdDebit;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
    @OneToMany(mappedBy = "parentAcc")
    private Collection<GlAccount> glAccountCollection;
    @JoinColumn(name = "parent_acc", referencedColumnName = "id")
    @ManyToOne
    private GlAccount parentAcc;

    public GlAccount() {
    }

    public GlAccount(Integer id) {
        this.id = id;
    }

    public GlAccount(Integer id, int accNumber, String name, String accGroup, boolean status, String costCenter, Date creationDate) {
        this.id = id;
        this.accNumber = accNumber;
        this.name = name;
        this.accGroup = accGroup;
        this.status = status;
        this.costCenter = costCenter;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(int accNumber) {
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

    public String getAccGroup() {
        return accGroup;
    }

    public void setAccGroup(String accGroup) {
        this.accGroup = accGroup;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    public String getAssistantAcc() {
        return assistantAcc;
    }

    public void setAssistantAcc(String assistantAcc) {
        this.assistantAcc = assistantAcc;
    }

    public String getAccClass() {
        return accClass;
    }

    public void setAccClass(String accClass) {
        this.accClass = accClass;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @XmlTransient
    public Collection<Symbol> getSymbolCollection() {
        return symbolCollection;
    }

    public void setSymbolCollection(Collection<Symbol> symbolCollection) {
        this.symbolCollection = symbolCollection;
    }

    @XmlTransient
    public Collection<Generalbudgetguide> getGeneralbudgetguideCollection() {
        return generalbudgetguideCollection;
    }

    public void setGeneralbudgetguideCollection(Collection<Generalbudgetguide> generalbudgetguideCollection) {
        this.generalbudgetguideCollection = generalbudgetguideCollection;
    }

    public IsagCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompany companyId) {
        this.companyId = companyId;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public IsagUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IsagUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Generalbudgetguide getGeneralBudgetId() {
        return generalBudgetId;
    }

    public void setGeneralBudgetId(Generalbudgetguide generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
    }

    public Generalbudgetguide getGeneralBudgetIdCredit() {
        return generalBudgetIdCredit;
    }

    public void setGeneralBudgetIdCredit(Generalbudgetguide generalBudgetIdCredit) {
        this.generalBudgetIdCredit = generalBudgetIdCredit;
    }

    public Generalbudgetguide getGeneralBudgetIdDebit() {
        return generalBudgetIdDebit;
    }

    public void setGeneralBudgetIdDebit(Generalbudgetguide generalBudgetIdDebit) {
        this.generalBudgetIdDebit = generalBudgetIdDebit;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection() {
        return glAccountCollection;
    }

    public void setGlAccountCollection(Collection<GlAccount> glAccountCollection) {
        this.glAccountCollection = glAccountCollection;
    }

    public GlAccount getParentAcc() {
        return parentAcc;
    }

    public void setParentAcc(GlAccount parentAcc) {
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
        if (!(object instanceof GlAccount)) {
            return false;
        }
        GlAccount other = (GlAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.GlAccount[ id=" + id + " ]";
    }
    
}
