package com.isag.businessservice.reports;

import java.util.List;
import javax.ejb.Remote;

import com.isag.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.isag.entity.GlYear;
import com.isag.views.ComplexRevisionBalance;

@Remote
public interface ComplexRevisionReportViewService {

    public List<ComplexRevisionBalance> getComplexRevisionBalanceList();

    public List<ComplexRevisionBalance> getALLRevisionBalanceReport(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCredit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);

    public List<ComplexRevisionBalance> getFirstPeriodByBranchId(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnCostCenter(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnAdminUnit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder);

    public List<ComplexRevisionBalance> getFirstPeriodByBranchIdDTO(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);
    
    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditDTO(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);
    //public List<ComplexRevisionBalance> getALLRevisionBalanceReports(Integer);  

    public List<ComplexRevisionBalance> getFirstPeriodByBranchIdSeperate(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear) ;
}

/*
    SELECT cr.glAccountId AS `glAccountNumber` , sum(ifnull(debit,0)) AS `debit`
    , sum(ifnull(credit,0))  AS `credit` , level AS `level`
    FROM isag.complex_revision_balance cr 
    where glBranchId = 81
    group by cr.glAccountId , cr.level;

 */
