package com.toby.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "inventory_transaction_production_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventoryTransactionProductionView.findAll", query = "SELECT i FROM InventoryTransactionProductionView i"),
    @NamedQuery(name = "InventoryTransactionProductionView.findByRowNum", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.rowNum = :rowNum"),
    @NamedQuery(name = "InventoryTransactionProductionView.findBySerial", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.serial = :serial"),
    @NamedQuery(name = "InventoryTransactionProductionView.findByName", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.name = :name"),
    @NamedQuery(name = "InventoryTransactionProductionView.findByBranchId", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.branchId = :branchId"),
    @NamedQuery(name = "InventoryTransactionProductionView.findByStatus", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.status = :status"),
    @NamedQuery(name = "InventoryTransactionProductionView.findByProProductionId", query = "SELECT i FROM InventoryTransactionProductionView i WHERE i.proProductionId = :proProductionId")})
public class InventoryTransactionProductionView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "serial")
    private Integer serial;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "pro_production_id")
    private Integer proProductionId;

    public InventoryTransactionProductionView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProProductionId() {
        return proProductionId;
    }

    public void setProProductionId(Integer proProductionId) {
        this.proProductionId = proProductionId;
    }
    
}
