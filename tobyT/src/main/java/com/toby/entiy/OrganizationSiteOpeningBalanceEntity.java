package com.toby.entiy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrganizationSiteOpeningBalanceEntity implements Serializable {

    private Integer id;
    private BigDecimal openBalanceDebit;
    private BigDecimal openBalanceCredit;
    private String code;
    private String organizationSiteName;
    private String organizationSiteType;
    private Date organizationBalanceDate;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getOpenBalanceDebit() {
        return openBalanceDebit;
    }

    public void setOpenBalanceDebit(BigDecimal openBalanceDebit) {
        this.openBalanceDebit = openBalanceDebit;
    }

    public BigDecimal getOpenBalanceCredit() {
        return openBalanceCredit;
    }

    public void setOpenBalanceCredit(BigDecimal openBalanceCredit) {
        this.openBalanceCredit = openBalanceCredit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    public String getOrganizationSiteType() {
        return organizationSiteType;
    }

    public void setOrganizationSiteType(String organizationSiteType) {
        this.organizationSiteType = organizationSiteType;
    }

    public Date getOrganizationBalanceDate() {
        return organizationBalanceDate;
    }

    public void setOrganizationBalanceDate(Date organizationBalanceDate) {
        this.organizationBalanceDate = organizationBalanceDate;
    }

}
