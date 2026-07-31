/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "report_signature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportSignature.findAll", query = "SELECT r FROM ReportSignature r")
    , @NamedQuery(name = "ReportSignature.findById", query = "SELECT r FROM ReportSignature r WHERE r.id = :id")
    , @NamedQuery(name = "ReportSignature.findBySignature1", query = "SELECT r FROM ReportSignature r WHERE r.signature1 = :signature1")
    , @NamedQuery(name = "ReportSignature.findBySignature2", query = "SELECT r FROM ReportSignature r WHERE r.signature2 = :signature2")
    , @NamedQuery(name = "ReportSignature.findBySignature3", query = "SELECT r FROM ReportSignature r WHERE r.signature3 = :signature3")
    , @NamedQuery(name = "ReportSignature.findBySignature4", query = "SELECT r FROM ReportSignature r WHERE r.signature4 = :signature4")
    , @NamedQuery(name = "ReportSignature.findBySignature5", query = "SELECT r FROM ReportSignature r WHERE r.signature5 = :signature5")
    , @NamedQuery(name = "ReportSignature.findByScreen", query = "SELECT r FROM ReportSignature r WHERE r.screen = :screen")
    , @NamedQuery(name = "ReportSignature.findByModificationDate", query = "SELECT r FROM ReportSignature r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "ReportSignature.findByCreationDate", query = "SELECT r FROM ReportSignature r WHERE r.creationDate = :creationDate")})
public class ReportSignature extends BaseEntity {

    @Size(max = 450)
    @Column(name = "signature_1")
    private String signature1;
    @Size(max = 450)
    @Column(name = "signature_2")
    private String signature2;
    @Size(max = 450)
    @Column(name = "signature_3")
    private String signature3;
    @Size(max = 450)
    @Column(name = "signature_4")
    private String signature4;
    @Size(max = 450)
    @Column(name = "signature_5")
    private String signature5;
    @Column(name = "screen")
    private Integer screen;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
  
  

    public ReportSignature() {
    }

    public ReportSignature(Integer id) {
        this.id = id;
    }

   

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }

    public String getSignature3() {
        return signature3;
    }

    public void setSignature3(String signature3) {
        this.signature3 = signature3;
    }

    public String getSignature4() {
        return signature4;
    }

    public void setSignature4(String signature4) {
        this.signature4 = signature4;
    }

    public String getSignature5() {
        return signature5;
    }

    public void setSignature5(String signature5) {
        this.signature5 = signature5;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof ReportSignature)) {
            return false;
        }
        ReportSignature other = (ReportSignature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.db.ReportSignature[ id=" + id + " ]";
    }
    
}
