package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.toby.entity.GlYear;
import com.toby.views.ComplexRevisionBalance;
import java.util.List;
import javax.ejb.Remote;


@Remote
public interface ComplexRevisionReportViewService {

    public List<ComplexRevisionBalance> getComplexRevisionBalanceList();

    public List<ComplexRevisionBalance> getALLRevisionBalanceReport(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCredit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);

    public List<ComplexRevisionBalance> getFirstPeriodByBranchId(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnCostCenter(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder);

    public List<ComplexRevisionBalance> getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnAdminUnit(ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBean, GlYear glYear, StringBuilder stringBuilder);

    //public List<ComplexRevisionBalance> getALLRevisionBalanceReports(Integer);  
}

/*
    SELECT cr.glAccountId AS `glAccountNumber` , sum(ifnull(debit,0)) AS `debit`
    , sum(ifnull(credit,0))  AS `credit` , level AS `level`
    FROM isag.complex_revision_balance cr 
    where glBranchId = 81
    group by cr.glAccountId , cr.level;

 */
