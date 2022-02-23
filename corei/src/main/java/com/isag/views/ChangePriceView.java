/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elsakr6
 */
@Entity
@Table(name = "change_price_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChangePriceView.findAll", query = "SELECT c FROM ChangePriceView c"),
    @NamedQuery(name = "ChangePriceView.findById", query = "SELECT c FROM ChangePriceView c WHERE c.id = :id"),
    @NamedQuery(name = "ChangePriceView.findByBranchId", query = "SELECT c FROM ChangePriceView c WHERE c.branchId = :branchId"),
    @NamedQuery(name = "ChangePriceView.findByGroupName", query = "SELECT c FROM ChangePriceView c WHERE c.groupName = :groupName"),
    @NamedQuery(name = "ChangePriceView.findByGroupCode", query = "SELECT c FROM ChangePriceView c WHERE c.groupCode = :groupCode"),
    @NamedQuery(name = "ChangePriceView.findByGroupId", query = "SELECT c FROM ChangePriceView c WHERE c.groupId = :groupId"),
    @NamedQuery(name = "ChangePriceView.findByItemSum", query = "SELECT c FROM ChangePriceView c WHERE c.itemSum = :itemSum"),
    @NamedQuery(name = "ChangePriceView.findByCostAverage", query = "SELECT c FROM ChangePriceView c WHERE c.costAverage = :costAverage")})
public class ChangePriceView implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_code")
    private Integer groupCode;
    @Column(name = "group_id")
    private Integer groupId;
    @Basic(optional = false)
    @Column(name = "item_sum")
    private long itemSum;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost_average")
    private BigDecimal costAverage;

    public ChangePriceView() {
    }
    
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public long getItemSum() {
        return itemSum;
    }

    public void setItemSum(long itemSum) {
        this.itemSum = itemSum;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }
    
}
