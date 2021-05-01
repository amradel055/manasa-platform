/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.CompanyInvoicesWithoutSewing;
import com.toby.views.CustomerBalances;
import com.toby.views.InvoicesIssuedWithinAperiod;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import com.toby.views.StatementOfBondsByBranche;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface ReportsService {

    public List<SalesReportByValueAded> getSalesReportByValueAded(SalesReportByValueAded salesReportByValueAded, Integer branchId);

    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails, Integer branchId);

    public List<CustomerBalances> getCustomerBalances(CustomerBalances customerBalances, Integer branchId);

    public List<StatementOfBondsByBranche> getStatementOfBondsByBranch(StatementOfBondsByBranche statementOfBondsByBranche, Integer branchId);

    public List<InvoicesIssuedWithinAperiod> getInvoiceWithinAperiod(InvoicesIssuedWithinAperiod issuedWithinAperiod, Integer branchId);

    public List<CompanyInvoicesWithoutSewing> getCompanyInvoicesWithoutSewing(CompanyInvoicesWithoutSewing invoicesWithoutSewing, Integer branchId);
}
