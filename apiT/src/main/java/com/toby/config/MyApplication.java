package com.toby.config;

import com.toby.rest.AttributeDetailsRest;
import com.toby.rest.LoginRest;
import com.toby.rest.AttributeRest;
import com.toby.rest.ItemsRest;
import com.toby.rest.OffersDetailsRest;
import com.toby.rest.OffersRest;
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
        return resources;
    }

}
