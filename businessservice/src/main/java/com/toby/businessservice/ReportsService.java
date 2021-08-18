/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.ClientsByInvInventory;
import com.toby.views.CompanyInvoicesWithoutSewing;
import com.toby.views.CustomerBalances;
import com.toby.views.CustomersHaveNoMovementFromTheDate;
import com.toby.views.InvoicesIssuedWithinAperiod;
import com.toby.views.InvoicesRegisteredMoreThanOnce;
import com.toby.views.ItemBalancesStatement;
import com.toby.views.ProfitOfItemsSold;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import com.toby.views.SalesStatementForThePeriod;
import com.toby.views.StatementOfBondsByBranche;
import java.math.BigDecimal;
import java.util.Date;
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

    public List<ItemBalancesStatement> getItemBalancesStatement(ItemBalancesStatement itemBalancesStatement, Integer branchId);

    public List<ProfitOfItemsSold> getProfitOfItemsSold(ProfitOfItemsSold profitOfItemsSold, Integer branchId, Integer type, Integer invoiceType);

    public List<SalesStatementForThePeriod> getSalesStatementForThePeriod(SalesStatementForThePeriod statementForThePeriod);

    public List<InvoicesRegisteredMoreThanOnce> getInvoicesRegisteredMoreThanOnce(InvoicesRegisteredMoreThanOnce registeredMoreThanOnce, Integer serial, Date date, Integer branchId);

    public void deleteIncrease(Integer proProductionStagesId, Integer proFactoryDeliveryDetailId, BigDecimal workNumber, BigDecimal originNumber);

    public List<CustomersHaveNoMovementFromTheDate> getCustomersHaveNoMovementFromTheDate(CustomersHaveNoMovementFromTheDate noMovementFromTheDate, Integer branchId);

    public List<ClientsByInvInventory> getClientsByInvInventory(ClientsByInvInventory invInventory, Integer branchId);
}
