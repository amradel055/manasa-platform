package com.isag.businessservice.reports.searchBean;

import java.util.Date;

public class DeletedAndUpdatedSearchBean {

	private Integer voucherIdFrom;
	private Integer voucherIdTo;

	private Integer accountNumberFrom;
	private Integer accountNumberTo;

	private Date dateFrom;
	private Date dateTo;

	public Integer getVoucherIdFrom() {
		return voucherIdFrom;
	}

	public void setVoucherIdFrom(Integer voucherIdFrom) {
		this.voucherIdFrom = voucherIdFrom;
	}

	public Integer getVoucherIdTo() {
		return voucherIdTo;
	}

	public void setVoucherIdTo(Integer voucherIdTo) {
		this.voucherIdTo = voucherIdTo;
	}

	public Integer getAccountNumberFrom() {
		return accountNumberFrom;
	}

	public void setAccountNumberFrom(Integer accountNumberFrom) {
		this.accountNumberFrom = accountNumberFrom;
	}

	public Integer getAccountNumberTo() {
		return accountNumberTo;
	}

	public void setAccountNumberTo(Integer accountNumberTo) {
		this.accountNumberTo = accountNumberTo;
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
}
