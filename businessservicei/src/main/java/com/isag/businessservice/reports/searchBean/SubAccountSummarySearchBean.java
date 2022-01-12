package com.isag.businessservice.reports.searchBean;

import com.isag.dto.GlBankDTO;
import com.isag.dto.GlYearDTO;
import com.isag.entity.GlBank;
import com.isag.entity.GlYear;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.io.Serializable;
import java.util.ArrayList;
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
    private GlBankDTO glBankTo;
    
    
    private Integer type;

    private Integer orderBy;
    private Integer transactionType;
    private Integer serialFrom;
    private Integer serialTo;
    private Symbol itemSerialFrom;
    private Symbol itemSerialTo;
    private String IsagUserFrom;
    private String IsagUserTo;
    private Integer bankId;
    private Boolean sessionClose;
    private Integer documentSerial;
    private Integer fromDocumentSerial;
    private Integer toDocumentSerial;
    private GlBankDTO bankDTO;

    private GlYearDTO glYearSelected;
    private List<GlBankDTO> glBankDTOListSelected;

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
     * @return the IsagUserFrom
     */
    public String getIsagUserFrom() {
        return IsagUserFrom;
    }

    /**
     * @param IsagUserFrom the IsagUserFrom to set
     */
    public void setIsagUserFrom(String IsagUserFrom) {
        this.IsagUserFrom = IsagUserFrom;
    }

    /**
     * @return the IsagUserTo
     */
    public String getIsagUserTo() {
        return IsagUserTo;
    }

    /**
     * @param IsagUserTo the IsagUserTo to set
     */
    public void setIsagUserTo(String IsagUserTo) {
        this.IsagUserTo = IsagUserTo;
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
     * @return the sessionClose
     */
    public Boolean getSessionClose() {
        return sessionClose;
    }

    /**
     * @param sessionClose the sessionClose to set
     */
    public void setSessionClose(Boolean sessionClose) {
        this.sessionClose = sessionClose;
    }

    /**
     * @return the documentSerial
     */
    public Integer getDocumentSerial() {
        return documentSerial;
    }

    /**
     * @param documentSerial the documentSerial to set
     */
    public void setDocumentSerial(Integer documentSerial) {
        this.documentSerial = documentSerial;
    }

    /**
     * @return the fromDocumentSerial
     */
    public Integer getFromDocumentSerial() {
        return fromDocumentSerial;
    }

    /**
     * @param fromDocumentSerial the fromDocumentSerial to set
     */
    public void setFromDocumentSerial(Integer fromDocumentSerial) {
        this.fromDocumentSerial = fromDocumentSerial;
    }

    /**
     * @return the toDocumentSerial
     */
    public Integer getToDocumentSerial() {
        return toDocumentSerial;
    }

    /**
     * @param toDocumentSerial the toDocumentSerial to set
     */
    public void setToDocumentSerial(Integer toDocumentSerial) {
        this.toDocumentSerial = toDocumentSerial;
    }

    /**
     * @return the bankDTO
     */
    public GlBankDTO getBankDTO() {
        return bankDTO;
    }

    /**
     * @param bankDTO the bankDTO to set
     */
    public void setBankDTO(GlBankDTO bankDTO) {
        this.bankDTO = bankDTO;
    }

    /**
     * @return the glBankFrom
     */
    public GlBankDTO getGlBankFrom() {
        return glBankFrom;
    }

    /**
     * @param glBankFrom the glBankFrom to set
     */
    public void setGlBankFrom(GlBankDTO glBankFrom) {
        this.glBankFrom = glBankFrom;
    }

    /**
     * @return the glBankTo
     */
    public GlBankDTO getGlBankTo() {
        return glBankTo;
    }

    /**
     * @param glBankTo the glBankTo to set
     */
    public void setGlBankTo(GlBankDTO glBankTo) {
        this.glBankTo = glBankTo;
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
        if (glBankDTOListSelected == null) {
            glBankDTOListSelected = new ArrayList<>();
        }
        return glBankDTOListSelected;
    }

    /**
     * @param glBankDTOListSelected the glBankDTOListSelected to set
     */
    public void setGlBankDTOListSelected(List<GlBankDTO> glBankDTOListSelected) {
        this.glBankDTOListSelected = glBankDTOListSelected;
    }


}
