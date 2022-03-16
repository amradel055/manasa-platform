/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
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
 * @author ahmed
 */
@Entity
@Table(name = "gl_account_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlAccountView.findAll", query = "SELECT g FROM GlAccountView g")
    , @NamedQuery(name = "GlAccountView.findByRowNum", query = "SELECT g FROM GlAccountView g WHERE g.rowNum = :rowNum")
    , @NamedQuery(name = "GlAccountView.findByAccountId", query = "SELECT g FROM GlAccountView g WHERE g.accountId = :accountId")
    , @NamedQuery(name = "GlAccountView.findByBranchId", query = "SELECT g FROM GlAccountView g WHERE g.branchId = :branchId")
    , @NamedQuery(name = "GlAccountView.findByScreenName", query = "SELECT g FROM GlAccountView g WHERE g.screenName = :screenName")})
public class GlAccountView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "ScreenName")
    private String screenName;

    public GlAccountView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
