/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;


import com.isag.define.GroupItemEnum;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq002
 */
//public class GeneralBudgetGuide {
//generalbudgetguide
@Entity
@Table(name = "generalbudgetguide")
public class GeneralBudgetGuide extends BaseEntity {
   
    @Size(max = 100)
    @Column(name = "number")
    private String number;
 
    @Size(max = 100)
    @Column(name = "nameEn")
    private String nameEn;
    
     @Size(max = 100)
    @Column(name = "nameAr")
    private String nameAr;
    
    @Column(name = "acc_group")
    @Enumerated(EnumType.STRING)
    private GroupItemEnum accGroup;
     
    @JoinColumn(name = "accountGroup", referencedColumnName = "id")
    @ManyToOne()
    private GlAccount accountGroup;
    
    
    @OneToMany(mappedBy = "generalBudgetIdCredit")
    private Collection<GlAccount> glAccountCollection;
    @OneToMany(mappedBy = "generalBudgetIdDebit")
    private Collection<GlAccount> glAccountCollection1;
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the nameAr
     */
    public String getNameAr() {
        return nameAr;
    }

    /**
     * @param nameAr the nameAr to set
     */
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
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
     * @return the accGroup
     */
    public GroupItemEnum getAccGroup() {
        return accGroup;
    }

    /**
     * @param accGroup the accGroup to set
     */
    public void setAccGroup(GroupItemEnum accGroup) {
        this.accGroup = accGroup;
    }
    
    
    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection() {
        return glAccountCollection;
    }

    public void setGlAccountCollection(Collection<GlAccount> glAccountCollection) {
        this.glAccountCollection = glAccountCollection;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection1() {
        return glAccountCollection1;
    }

    public void setGlAccountCollection1(Collection<GlAccount> glAccountCollection1) {
        this.glAccountCollection1 = glAccountCollection1;
    }
    
    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the accountGroup
     */
    public GlAccount getAccountGroup() {
        return accountGroup;
    }

    /**
     * @param accountGroup the accountGroup to set
     */
    public void setAccountGroup(GlAccount accountGroup) {
        this.accountGroup = accountGroup;
    }
}
