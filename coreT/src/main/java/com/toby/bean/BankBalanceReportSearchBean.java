/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bean;

import com.toby.dto.GlBankDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author IT Shop
 */
public class BankBalanceReportSearchBean implements Serializable {

    private Date dateFrom;
    private Date dateTo;
    private List<GlBankDTO> glBankList;
    private Integer branchId;
    private String bankBuilder;
    private Set<Integer> serchedBanksSet;

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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

    /**
     * @return the serchedBanksSet
     */
    public Set<Integer> getSerchedBanksSet() {
        if(serchedBanksSet == null){
            serchedBanksSet = new HashSet<>();
        }
        return serchedBanksSet;
    }

    /**
     * @param serchedBanksSet the serchedBanksSet to set
     */
    public void setSerchedBanksSet(Set<Integer> serchedBanksSet) {
        this.serchedBanksSet = serchedBanksSet;
    }

    /**
     * @return the glBankList
     */
    public List<GlBankDTO> getGlBankList() {
        if(glBankList == null){
            glBankList = new ArrayList<>();
        }
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the bankBuilder
     */
    public String getBankBuilder() {
        return bankBuilder;
    }

    /**
     * @param bankBuilder the bankBuilder to set
     */
    public void setBankBuilder(String bankBuilder) {
        this.bankBuilder = bankBuilder;
    }

}
