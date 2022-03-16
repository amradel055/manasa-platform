/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam
 */
@Entity
@Table(name = "inv_item_inst_project_guide_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvItemInstProjectGuideView.findAll", query = "SELECT i FROM InvItemInstProjectGuideView i")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findByRowNum", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.rowNum = :rowNum")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findById", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.id = :id")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findByName", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.name = :name")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findByCode", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.code = :code")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findByBranchId", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.branchId = :branchId")
    , @NamedQuery(name = "InvItemInstProjectGuideView.findByTableType", query = "SELECT i FROM InvItemInstProjectGuideView i WHERE i.tableType = :tableType")})
public class InvItemInstProjectGuideView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "table_type")
    private int tableType;
    @Column(name = "price")
    private BigDecimal price;

    public InvItemInstProjectGuideView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }
    @Override
    public String toString() {
        return " " + code +" " +name;
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
        if (!(object instanceof InvItemInstProjectGuideView)) {
            return false;
        }
        InvItemInstProjectGuideView other = (InvItemInstProjectGuideView) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
