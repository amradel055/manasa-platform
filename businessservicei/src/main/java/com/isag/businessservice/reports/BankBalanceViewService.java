/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.dto.BankBalanceReportBeanNew;
import com.isag.dto.BankBalanceReportSearchBean;
import com.isag.entity.GlYear;
import com.isag.views.BankBalanceView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface BankBalanceViewService {

    public List<BankBalanceView> getAllBankBalancesTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer bankIdFrom, Integer bankIdTo);

    public List<BankBalanceView> getAllBankBalancesTransactionsByBankId(Integer branchId, GlYear glYear, Date dateTo, StringBuilder builder);

    public List<BankBalanceReportBeanNew> searchNative(BankBalanceReportSearchBean bankBalanceReportSearchBean);

    public BigDecimal findBalanceForBankId(Integer bankId);
    

    public BigDecimal findCashBalanceByDate(Integer bankId);

    public BigDecimal findSettelmentBalanceByDate(Integer bankId);

    public BigDecimal findTransferBalanceByDate(Integer bankId);
    
    public BigDecimal findCashBalanceByMonth(Integer bankId);

    public BigDecimal findSettelmentBalanceByMonth(Integer bankId);

    public BigDecimal findTransferBalanceByMonth(Integer bankId);

}
