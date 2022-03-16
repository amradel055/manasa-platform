package com.toby.report.entity;

import java.math.BigDecimal;

public class BalanceAccountMonthlyBean {

    private String accountName;
    private Integer accountNumber;
    private Integer id;
    private BigDecimal janValue = BigDecimal.ZERO;
    private BigDecimal febValue = BigDecimal.ZERO;
    private BigDecimal marValue = BigDecimal.ZERO;
    private BigDecimal aprValue = BigDecimal.ZERO;
    private BigDecimal mayValue = BigDecimal.ZERO;
    private BigDecimal juneValue = BigDecimal.ZERO;
    private BigDecimal julValue = BigDecimal.ZERO;
    private BigDecimal augValue = BigDecimal.ZERO;
    private BigDecimal sepValue = BigDecimal.ZERO;
    private BigDecimal octValue = BigDecimal.ZERO;
    private BigDecimal novValue = BigDecimal.ZERO;
    private BigDecimal decValue = BigDecimal.ZERO;
    private Integer jan;
    private Integer feb;
    private Integer mar;
    private Integer apl;
    private Integer may;
    private Integer jun;
    private Integer jly;
    private Integer aug;
    private Integer sep;
    private Integer oct;
    private Integer nov;
    private Integer dec;
    private BigDecimal totalValue = BigDecimal.ZERO;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getJanValue() {
        return janValue;
    }

    public void setJanValue(BigDecimal janValue) {
        this.janValue = janValue;
    }

    public BigDecimal getFebValue() {
        return febValue;
    }

    public void setFebValue(BigDecimal febValue) {
        this.febValue = febValue;
    }

    public BigDecimal getMarValue() {
        return marValue;
    }

    public void setMarValue(BigDecimal marValue) {
        this.marValue = marValue;
    }

    public BigDecimal getAprValue() {
        return aprValue;
    }

    public void setAprValue(BigDecimal aprValue) {
        this.aprValue = aprValue;
    }

    public BigDecimal getMayValue() {
        return mayValue;
    }

    public void setMayValue(BigDecimal mayValue) {
        this.mayValue = mayValue;
    }

    public BigDecimal getJuneValue() {
        return juneValue;
    }

    public void setJuneValue(BigDecimal juneValue) {
        this.juneValue = juneValue;
    }

    public BigDecimal getJulValue() {
        return julValue;
    }

    public void setJulValue(BigDecimal julValue) {
        this.julValue = julValue;
    }

    public BigDecimal getAugValue() {
        return augValue;
    }

    public void setAugValue(BigDecimal augValue) {
        this.augValue = augValue;
    }

    public BigDecimal getSepValue() {
        return sepValue;
    }

    public void setSepValue(BigDecimal sepValue) {
        this.sepValue = sepValue;
    }

    public BigDecimal getOctValue() {
        return octValue;
    }

    public void setOctValue(BigDecimal octValue) {
        this.octValue = octValue;
    }

    public BigDecimal getNovValue() {
        return novValue;
    }

    public void setNovValue(BigDecimal novValue) {
        this.novValue = novValue;
    }

    public BigDecimal getDecValue() {
        return decValue;
    }

    public void setDecValue(BigDecimal decValue) {
        this.decValue = decValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the accountNumber
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the jan
     */
    public Integer getJan() {
        return jan;
    }

    /**
     * @param jan the jan to set
     */
    public void setJan(Integer jan) {
        this.jan = jan;
    }

    /**
     * @return the feb
     */
    public Integer getFeb() {
        return feb;
    }

    /**
     * @param feb the feb to set
     */
    public void setFeb(Integer feb) {
        this.feb = feb;
    }

    /**
     * @return the mar
     */
    public Integer getMar() {
        return mar;
    }

    /**
     * @param mar the mar to set
     */
    public void setMar(Integer mar) {
        this.mar = mar;
    }

    /**
     * @return the apl
     */
    public Integer getApl() {
        return apl;
    }

    /**
     * @param apl the apl to set
     */
    public void setApl(Integer apl) {
        this.apl = apl;
    }

    /**
     * @return the may
     */
    public Integer getMay() {
        return may;
    }

    /**
     * @param may the may to set
     */
    public void setMay(Integer may) {
        this.may = may;
    }

    /**
     * @return the jun
     */
    public Integer getJun() {
        return jun;
    }

    /**
     * @param jun the jun to set
     */
    public void setJun(Integer jun) {
        this.jun = jun;
    }

    /**
     * @return the jly
     */
    public Integer getJly() {
        return jly;
    }

    /**
     * @param jly the jly to set
     */
    public void setJly(Integer jly) {
        this.jly = jly;
    }

    /**
     * @return the aug
     */
    public Integer getAug() {
        return aug;
    }

    /**
     * @param aug the aug to set
     */
    public void setAug(Integer aug) {
        this.aug = aug;
    }

    /**
     * @return the sep
     */
    public Integer getSep() {
        return sep;
    }

    /**
     * @param sep the sep to set
     */
    public void setSep(Integer sep) {
        this.sep = sep;
    }

    /**
     * @return the oct
     */
    public Integer getOct() {
        return oct;
    }

    /**
     * @param oct the oct to set
     */
    public void setOct(Integer oct) {
        this.oct = oct;
    }

    /**
     * @return the nov
     */
    public Integer getNov() {
        return nov;
    }

    /**
     * @param nov the nov to set
     */
    public void setNov(Integer nov) {
        this.nov = nov;
    }

    /**
     * @return the dec
     */
    public Integer getDec() {
        return dec;
    }

    /**
     * @param dec the dec to set
     */
    public void setDec(Integer dec) {
        this.dec = dec;
    }

    /**
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
