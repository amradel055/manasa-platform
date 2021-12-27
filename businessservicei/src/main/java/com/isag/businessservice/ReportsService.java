/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.views.ProfitOfItemsSold;
import com.isag.views.SalesReportByValueAdedDetails;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface ReportsService {

//    public List<SalesReportByValueAded> getSalesReportByValueAded(SalesReportByValueAded salesReportByValueAded, Integer branchId);
//
//    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails, Integer branchId);
//
//    public List<CustomerBalances> getCustomerBalances(CustomerBalances customerBalances, Integer branchId);
//
//    public List<StatementOfBondsByBranche> getStatementOfBondsByBranch(StatementOfBondsByBranche statementOfBondsByBranche, Integer branchId);
//
//    public List<InvoicesIssuedWithinAperiod> getInvoiceWithinAperiod(InvoicesIssuedWithinAperiod issuedWithinAperiod, Integer branchId);
//
//    public List<CompanyInvoicesWithoutSewing> getCompanyInvoicesWithoutSewing(CompanyInvoicesWithoutSewing invoicesWithoutSewing, Integer branchId);
//
//    public List<ItemBalancesStatement> getItemBalancesStatement(ItemBalancesStatement itemBalancesStatement, Integer branchId);

    public List<ProfitOfItemsSold> getProfitOfItemsSold(ProfitOfItemsSold profitOfItemsSold, Integer branchId , Integer type);
    
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails, Integer branchId);
}
