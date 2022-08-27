package com.toby.config;

import com.toby.rest.AttributeDetailsRest;
import com.toby.rest.LoginRest;
import com.toby.rest.AttributeRest;
import com.toby.rest.CangePasswordRest;
import com.toby.rest.CostCenterRest;
import com.toby.rest.CrmEventRest;
import com.toby.rest.CustomerBalancesRest;
import com.toby.rest.EmployeeRest;
import com.toby.rest.GeneralJournalRest;
import com.toby.rest.GlAccountRest;
import com.toby.rest.GlYearRest;
import com.toby.rest.InventoryRest;
import com.toby.rest.ItemsRest;
import com.toby.rest.OffersDetailsRest;
import com.toby.rest.OffersRest;
import com.toby.rest.PayRest;
import com.toby.rest.SalesRest;
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
        resources.add(CustomerBalancesRest.class);
        resources.add(SupplierCustomerRest.class);
        resources.add(CrmEventRest.class);
        resources.add(EmployeeRest.class);
        resources.add(TobyUserRest.class);
        resources.add(InventoryRest.class);

        return resources;
    }

}
