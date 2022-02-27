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
public class HosClinicReportBean {

    private String clinicName;
    private Integer casesNumber = 0;
    private Integer netCachCases = 0;
    private Integer netCompanyCases = 0;
    private BigDecimal cashPaid = BigDecimal.ZERO;
    private BigDecimal cardPaid = BigDecimal.ZERO;
    private BigDecimal clincCompanyIncome = BigDecimal.ZERO;
    private BigDecimal net = BigDecimal.ZERO;
    private Integer docorsNumber = 0;

    /**
     * @return the clinicName
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     * @param clinicName the clinicName to set
     */
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    /**
     * @return the casesNumber
     */
    public Integer getCasesNumber() {
        return casesNumber;
    }

    /**
     * @param casesNumber the casesNumber to set
     */
    public void setCasesNumber(Integer casesNumber) {
        this.casesNumber = casesNumber;
    }

    /**
     * @return the netCachCases
     */
    public Integer getNetCachCases() {
        return netCachCases;
    }

    /**
     * @param netCachCases the netCachCases to set
     */
    public void setNetCachCases(Integer netCachCases) {
        this.netCachCases = netCachCases;
    }

    /**
     * @return the netCompanyCases
     */
    public Integer getNetCompanyCases() {
        return netCompanyCases;
    }

    /**
     * @param netCompanyCases the netCompanyCases to set
     */
    public void setNetCompanyCases(Integer netCompanyCases) {
        this.netCompanyCases = netCompanyCases;
    }

    /**
     * @return the cashPaid
     */
    public BigDecimal getCashPaid() {
        return cashPaid;
    }

    /**
     * @param cashPaid the cashPaid to set
     */
    public void setCashPaid(BigDecimal cashPaid) {
        this.cashPaid = cashPaid;
    }

    /**
     * @return the clincCompanyIncome
     */
    public BigDecimal getClincCompanyIncome() {
        return clincCompanyIncome;
    }

    /**
     * @param clincCompanyIncome the clincCompanyIncome to set
     */
    public void setClincCompanyIncome(BigDecimal clincCompanyIncome) {
        this.clincCompanyIncome = clincCompanyIncome;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the docorsNumber
     */
    public Integer getDocorsNumber() {
        return docorsNumber;
    }

    /**
     * @param docorsNumber the docorsNumber to set
     */
    public void setDocorsNumber(Integer docorsNumber) {
        this.docorsNumber = docorsNumber;
    }

    /**
     * @return the cardPaid
     */
    public BigDecimal getCardPaid() {
        return cardPaid;
    }

    /**
     * @param cardPaid the cardPaid to set
     */
    public void setCardPaid(BigDecimal cardPaid) {
        this.cardPaid = cardPaid;
    }
}
