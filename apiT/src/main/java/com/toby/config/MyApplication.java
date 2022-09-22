package com.toby.config;

import com.toby.rest.AttributeDetailsRest;
import com.toby.rest.LoginRest;
import com.toby.rest.AttributeRest;
import com.toby.rest.BankBalanceRest;
import com.toby.rest.CangePasswordRest;
import com.toby.rest.ClientNoMovementRest;
import com.toby.rest.CostCenterRest;
import com.toby.rest.CrmEventRest;
import com.toby.rest.CustomerBalanceRest;
import com.toby.rest.CustomerRest;
import com.toby.rest.DelegatorRest;
import com.toby.rest.EmployeeRest;
import com.toby.rest.GeneralJournalRest;
import com.toby.rest.GlAccountRest;
import com.toby.rest.GlBankTransactionRest;
import com.toby.rest.GlYearRest;
import com.toby.rest.GlbankRest;
import com.toby.rest.GroupDTORest;
import com.toby.rest.InventoryRest;
import com.toby.rest.ItemsRest;
import com.toby.rest.OffersDetailsRest;
import com.toby.rest.OffersRest;
import com.toby.rest.PayRest;
import com.toby.rest.PermisionOrderRest;
import com.toby.rest.ProfitOfItemsRest;
import com.toby.rest.ProproductionReportRest;
import com.toby.rest.PurchaseOfItemsBySupplierRest;
import com.toby.rest.ReportsRest;
import com.toby.rest.SalesRest;
import com.toby.rest.SalesReportRest;
import com.toby.rest.SalesReportValueAdedRest;
import com.toby.rest.SupplierCustomerRest;
import com.toby.rest.SymbolRest;
import com.toby.rest.TobyUserRest;
import com.toby.rest.VariationsDetailsRest;
import com.toby.rest.VariationsRest;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest/*")
public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(LoginRest.class);
        resources.add(AttributeRest.class);
        resources.add(CorsFilter.class);
        resources.add(AuthenticationFilter.class);
        resources.add(AttributeDetailsRest.class);
        resources.add(OffersRest.class);
        resources.add(OffersDetailsRest.class);
        resources.add(VariationsRest.class);
        resources.add(VariationsDetailsRest.class);
        resources.add(ItemsRest.class);
        resources.add(PayRest.class);
        resources.add(SalesRest.class);
        resources.add(CostCenterRest.class);
        resources.add(GlAccountRest.class);
        resources.add(GlYearRest.class);
        resources.add(SymbolRest.class);
        resources.add(GeneralJournalRest.class);
        resources.add(CangePasswordRest.class);
        resources.add(CustomerRest.class);
        resources.add(SupplierCustomerRest.class);
        resources.add(CrmEventRest.class);
        resources.add(EmployeeRest.class);
        resources.add(TobyUserRest.class);
        resources.add(InventoryRest.class);
        resources.add(DelegatorRest.class);
        resources.add(GlbankRest.class);
        resources.add(ProproductionReportRest.class);
        resources.add(SalesReportRest.class);
        resources.add(GlBankTransactionRest.class);
        resources.add(PurchaseOfItemsBySupplierRest.class);
        resources.add(BankBalanceRest.class);
        resources.add(SalesReportValueAdedRest.class);
        resources.add(CustomerBalanceRest.class);
        resources.add(ClientNoMovementRest.class);
        resources.add(ReportsRest.class);
        resources.add(GroupDTORest.class);
        resources.add(ProfitOfItemsRest.class);
        resources.add(PermisionOrderRest.class);

        return resources;
    }

}
