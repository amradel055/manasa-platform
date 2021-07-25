package com.toby.businessservice.reports.searchBean;

import com.toby.dto.GlBankDTO;
import com.toby.dto.GlYearDTO;
import com.toby.entity.Symbol;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SubAccountSummarySearchBean implements Serializable {

    private Date dateFrom;
    private Date dateTo;
    private Integer costCenterFrom;
    private Integer costCenterTo;
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    private Integer accountId;
    private Integer financailYear;
    private Integer branchId;
    private Integer currencyType;
    private String accClass;
    private GlBankDTO glBankFrom;
    private GlBankDTO  glBankTo;
    private Integer type;

    private Integer orderBy;
    private Integer transactionType; 
    private Integer serialFrom;
    private Integer serialTo;
    private Symbol itemSerialFrom;
    private Symbol itemSerialTo;
    private String TobyUserFrom;
    private String TobyUserTo;    
    private Integer bankId;
    private List<GlBankDTO>  glBankToListSelected;
    
    private GlYearDTO glYearSelected;
    private List<GlBankDTO> glBankDTOListSelected;
    
    public GlBankDTO getGlBankFrom() {
        return glBankFrom;
    }

    public void setGlBankFrom(GlBankDTO glBankFrom) {
        this.glBankFrom = glBankFrom;
    }

    public GlBankDTO getGlBankTo() {
        return glBankTo;
    }

    public void setGlBankTo(GlBankDTO glBankTo) {
        this.glBankTo = glBankTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getCostCenterFrom() {
        return costCenterFrom;
    }

    public void setCostCenterFrom(Integer costCenterFrom) {
        this.costCenterFrom = costCenterFrom;
    }

    public Integer getCostCenterTo() {
        return costCenterTo;
    }

    public void setCostCenterTo(Integer costCenterTo) {
        this.costCenterTo = costCenterTo;
    }

    public Integer getAdminUnitFrom() {
        return adminUnitFrom;
    }

    public void setAdminUnitFrom(Integer adminUnitFrom) {
        this.adminUnitFrom = adminUnitFrom;
    }

    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the financailYear
     */
    public Integer getFinancailYear() {
        return financailYear;
    }

    /**
     * @param financailYear the financailYear to set
     */
    public void setFinancailYear(Integer financailYear) {
        this.financailYear = financailYear;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the accClass
     */
    public String getAccClass() {
        return accClass;
    }

    /**
     * @param accClass the accClass to set
     */
    public void setAccClass(String accClass) {
        this.accClass = accClass;
    }

    /**
     * @return the transactionType
     */
    public Integer getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the serialFrom
     */
    public Integer getSerialFrom() {
        return serialFrom;
    }

    /**
     * @param serialFrom the serialFrom to set
     */
    public void setSerialFrom(Integer serialFrom) {
        this.serialFrom = serialFrom;
    }

    /**
     * @return the serialTo
     */
    public Integer getSerialTo() {
        return serialTo;
    }

    /**
     * @param serialTo the serialTo to set
     */
    public void setSerialTo(Integer serialTo) {
        this.serialTo = serialTo;
    }

    /**
     * @return the itemSerialFrom
     */
    public Symbol getItemSerialFrom() {
        return itemSerialFrom;
    }

    /**
     * @param itemSerialFrom the itemSerialFrom to set
     */
    public void setItemSerialFrom(Symbol itemSerialFrom) {
        this.itemSerialFrom = itemSerialFrom;
    }

    /**
     * @return the itemSerialTo
     */
    public Symbol getItemSerialTo() {
        return itemSerialTo;
    }

    /**
     * @param itemSerialTo the itemSerialTo to set
     */
    public void setItemSerialTo(Symbol itemSerialTo) {
        this.itemSerialTo = itemSerialTo;
    }

    /**
     * @return the TobyUserFrom
     */
    public String getTobyUserFrom() {
        return TobyUserFrom;
    }

    /**
     * @param TobyUserFrom the TobyUserFrom to set
     */
    public void setTobyUserFrom(String TobyUserFrom) {
        this.TobyUserFrom = TobyUserFrom;
    }

    /**
     * @return the TobyUserTo
     */
    public String getTobyUserTo() {
        return TobyUserTo;
    }

    /**
     * @param TobyUserTo the TobyUserTo to set
     */
    public void setTobyUserTo(String TobyUserTo) {
        this.TobyUserTo = TobyUserTo;
    }

    /**
     * @return the bankId
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the glBankToListSelected
     */
    public List<GlBankDTO> getGlBankToListSelected() {
        return glBankToListSelected;
    }

    /**
     * @param glBankToListSelected the glBankToListSelected to set
     */
    public void setGlBankToListSelected(List<GlBankDTO> glBankToListSelected) {
        this.glBankToListSelected = glBankToListSelected;
    }

    /**
     * @return the glYearSelected
     */
    public GlYearDTO getGlYearSelected() {
        return glYearSelected;
    }

    /**
     * @param glYearSelected the glYearSelected to set
     */
    public void setGlYearSelected(GlYearDTO glYearSelected) {
        this.glYearSelected = glYearSelected;
    }

    /**
     * @return the glBankDTOListSelected
     */
    public List<GlBankDTO> getGlBankDTOListSelected() {
        return glBankDTOListSelected;
    }

    /**
     * @param glBankDTOListSelected the glBankDTOListSelected to set
     */
    public void setGlBankDTOListSelected(List<GlBankDTO> glBankDTOListSelected) {
        this.glBankDTOListSelected = glBankDTOListSelected;
    }

}
