/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam
 */
@Entity
@Table(name = "instcontractdetailwithbanktransaction_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstcontractdetailwithbanktransactionView.findAll", query = "SELECT i FROM InstcontractdetailwithbanktransactionView i WHERE i.instContractId = :instContractId")
   ,@NamedQuery(name = "InstcontractdetailwithbanktransactionView.findByBranchId", query = "SELECT i FROM InstcontractdetailwithbanktransactionView i WHERE i.branchId = :branchId")})
public class InstcontractdetailwithbanktransactionView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "inst_contract_id")
    private Integer instContractId;
    
    @Column(name = "branch_id")
    private Integer branchId;
  

    public InstcontractdetailwithbanktransactionView() {
    }

    public Integer getInstContractId() {
        return instContractId;
    }

    public void setInstContractId(Integer instContractId) {
        this.instContractId = instContractId;
    }


    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
    
}
