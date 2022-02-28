
package com.toby.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "toby_user_proproduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserProproduction.findAll", query = "SELECT t FROM TobyUserProproduction t"),
    @NamedQuery(name = "TobyUserProproduction.findById", query = "SELECT t FROM TobyUserProproduction t WHERE t.id = :id"),
    @NamedQuery(name = "TobyUserProproduction.findByCreationDate", query = "SELECT t FROM TobyUserProproduction t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "TobyUserProproduction.findByModificationDate", query = "SELECT t FROM TobyUserProproduction t WHERE t.modificationDate = :modificationDate")})
public class TobyUserProproduction extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    
    @Column(name = "cost")
    private BigDecimal cost;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "proproduction_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductionStages proproductionId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;

    public TobyUserProproduction() {
    }

    public TobyUserProproduction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public ProProductionStages getProproductionId() {
        return proproductionId;
    }

    public void setProproductionId(ProProductionStages proproductionId) {
        this.proproductionId = proproductionId;
    }

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
        this.userId = userId;
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
        if (!(object instanceof TobyUserProproduction)) {
            return false;
        }
        TobyUserProproduction other = (TobyUserProproduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.omar.TobyUserProproduction[ id=" + id + " ]";
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    
}
