/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class InvDelegatorSalesByMainItemsGroupBean {

    private String organization_name;
    private BigDecimal posting;
    private BigDecimal cash;
    private BigDecimal credit_document;
    private BigDecimal total;


    /**
     * @return the posting
     */
    public BigDecimal getPosting() {
        return posting;
    }

    /**
     * @param posting the posting to set
     */
    public void setPosting(BigDecimal posting) {
        this.posting = posting;
    }

    /**
     * @return the cash
     */
    public BigDecimal getCash() {
        return cash;
    }

    /**
     * @param cash the cash to set
     */
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }


    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the credit_document
     */
    public BigDecimal getCredit_document() {
        return credit_document;
    }

    /**
     * @param credit_document the credit_document to set
     */
    public void setCredit_document(BigDecimal credit_document) {
        this.credit_document = credit_document;
    }

    /**
     * @return the organization_name
     */
    public String getOrganization_name() {
        return organization_name;
    }

    /**
     * @param organization_name the organization_name to set
     */
    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

}
