/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.config;

import com.isag.rest.IgmaRest;
import com.isag.rest.clientAccountBalanceRest;
import com.isag.rest.InvPurchaseInvoiceRest;
import com.isag.rest.LoginRest;
import com.isag.rest.MonitorRest;
import com.isag.rest.clientAccountRest;
import com.isag.rest.contractorAccountBalanceRest;
import com.isag.rest.contractorAccountRest;
import com.isag.rest.supplierAccountBalanceRest;
import com.isag.rest.supplierAccountRest;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author AHMEDESSAM
 */
@ApplicationPath("/rest/*")
public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(LoginRest.class);
        resources.add(MonitorRest.class);
        resources.add(InvPurchaseInvoiceRest.class);
        resources.add(AuthenticationFilter.class);
        resources.add(clientAccountRest.class);
        resources.add(supplierAccountRest.class);
        resources.add(contractorAccountRest.class);
        resources.add(clientAccountBalanceRest.class);
        resources.add(supplierAccountBalanceRest.class);
        resources.add(contractorAccountBalanceRest.class);
        resources.add(IgmaRest.class);
        return resources;
    }

}
