/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author amr
 */
@Entity
@Table(name = "toby_user_login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserLogin.findAll", query = "SELECT i FROM TobyUserLogin i"),
    @NamedQuery(name = "TobyUserLogin.findById", query = "SELECT i FROM TobyUserLogin i WHERE i.id = :id"),
    @NamedQuery(name = "TobyUserLogin.findByMacId", query = "SELECT i FROM TobyUserLogin i WHERE i.macId = :macId"),
    @NamedQuery(name = "TobyUserLogin.findByModificationDate", query = "SELECT i FROM TobyUserLogin i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "TobyUserLogin.findByCreationDate", query = "SELECT i FROM TobyUserLogin i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "TobyUserLogin.findByDateLogin", query = "SELECT i FROM TobyUserLogin i WHERE i.dateLogin = :dateLogin"),
    @NamedQuery(name = "TobyUserLogin.checkUserlogged",query = " SELECT  i.state FROM TobyUserLogin i WHERE i.userId.id =:userId AND i.branchId.id =:branchId   ORDER BY i.id DESC ")})
public class TobyUserLogin extends BaseEntity {

    @Size(max = 450)
    @Column(name = "mac_id")
    private String macId;
    @Column(name="ip_address")
    private String ipAddress;
    @Column(name="host_name")
    private String hostName;
    @Column(name="operating_system")
    private String operatingSystem;
    @Column(name="browser_Name")
    private String browserName;
    @Column(name = "date_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLogin;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;
    @Column(name = "state")
    private int state;

    public TobyUserLogin() {
    }

    public TobyUserLogin(Integer id) {
        this.id = id;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public Date getDateLogin() {
        return dateLogin;
    }

    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the operatingSystem
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return the browserName
     */
    public String getBrowserName() {
        return browserName;
    }

    /**
     * @param browserName the browserName to set
     */
    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
        this.userId = userId;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
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
        if (!(object instanceof TobyUserLogin)) {
            return false;
        }
        TobyUserLogin other = (TobyUserLogin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.TobyUserLogin[ id=" + id + " ]";
    }
}
