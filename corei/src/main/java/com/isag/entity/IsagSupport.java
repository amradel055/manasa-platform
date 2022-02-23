/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "isag_support")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagSupport.findAll", query = "SELECT i FROM IsagSupport i")
    , @NamedQuery(name = "IsagSupport.findById", query = "SELECT i FROM IsagSupport i WHERE i.id = :id")
    , @NamedQuery(name = "IsagSupport.findByDate", query = "SELECT i FROM IsagSupport i WHERE i.date = :date")
    , @NamedQuery(name = "IsagSupport.findByModule", query = "SELECT i FROM IsagSupport i WHERE i.module = :module")
    , @NamedQuery(name = "IsagSupport.findByScreen", query = "SELECT i FROM IsagSupport i WHERE i.screen = :screen")
    , @NamedQuery(name = "IsagSupport.findByTitle", query = "SELECT i FROM IsagSupport i WHERE i.title = :title")
    , @NamedQuery(name = "IsagSupport.findByDescription", query = "SELECT i FROM IsagSupport i WHERE i.description = :description")
    , @NamedQuery(name = "IsagSupport.findByImg", query = "SELECT i FROM IsagSupport i WHERE i.img = :img")
    , @NamedQuery(name = "IsagSupport.findByPriorety", query = "SELECT i FROM IsagSupport i WHERE i.priorety = :priorety")
    , @NamedQuery(name = "IsagSupport.findByTecincal", query = "SELECT i FROM IsagSupport i WHERE i.tecincal = :tecincal")
    , @NamedQuery(name = "IsagSupport.findByReply", query = "SELECT i FROM IsagSupport i WHERE i.reply = :reply")
    , @NamedQuery(name = "IsagSupport.findByRemark", query = "SELECT i FROM IsagSupport i WHERE i.remark = :remark")
    , @NamedQuery(name = "IsagSupport.findByModificationDate", query = "SELECT i FROM IsagSupport i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "IsagSupport.findByCreationDate", query = "SELECT i FROM IsagSupport i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "IsagSupport.findBySerial", query = "SELECT i FROM IsagSupport i WHERE i.serial = :serial")})
public class IsagSupport extends BaseEntity {

   
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "module")
    private Integer module;
    @JoinColumn(name = "screen", referencedColumnName = "id")
    @ManyToOne
    private IsagScreen screen;
    @Size(max = 100)
    @Column(name = "title")
    private String title;
    @Size(max = 600)
    @Column(name = "description")
    private String description;
    @Size(max = 200)
    @Column(name = "img")
    private String img;
    @Column(name = "status")
    private Integer status;
    @Column(name = "priorety")
    private Integer priorety;
    @Column(name = "tecincal")
    private Integer tecincal;
    @Size(max = 600)
    @Column(name = "reply")
    private String reply;
    @Size(max = 300)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "assign_to", referencedColumnName = "id")
    @ManyToOne
    private Employee assignTo;
    @Column(name = "ticket_solve_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ticketSolveDate;
    @Column(name = "transfer_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;
    

    public IsagSupport() {
    }

    public IsagSupport(Integer id) {
        this.id = id;
    }

     
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public IsagScreen getScreen() {
        return screen;
    }

    public void setScreen(IsagScreen screen) {
        this.screen = screen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriorety() {
        return priorety;
    }

    public void setPriorety(Integer priorety) {
        this.priorety = priorety;
    }

    public Integer getTecincal() {
        return tecincal;
    }

    public void setTecincal(Integer tecincal) {
        this.tecincal = tecincal;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
 
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof IsagSupport)) {
            return false;
        }
        IsagSupport other = (IsagSupport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial  + "";
    }

    /**
     * @return the assignTo
     */
    public Employee getAssignTo() {
        return assignTo;
    }

    /**
     * @param assignTo the assignTo to set
     */
    public void setAssignTo(Employee assignTo) {
        this.assignTo = assignTo;
    }

    /**
     * @return the ticketSolveDate
     */
    public Date getTicketSolveDate() {
        return ticketSolveDate;
    }

    /**
     * @param ticketSolveDate the ticketSolveDate to set
     */
    public void setTicketSolveDate(Date ticketSolveDate) {
        this.ticketSolveDate = ticketSolveDate;
    }

    /**
     * @return the transferDate
     */
    public Date getTransferDate() {
        return transferDate;
    }

    /**
     * @param transferDate the transferDate to set
     */
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    
}
