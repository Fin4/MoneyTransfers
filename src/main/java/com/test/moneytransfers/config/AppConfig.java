package com.test.moneytransfers.config;

import com.test.moneytransfers.service.MoneyTransferService;
import com.test.moneytransfers.service.InMemoryMoneyTransferService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class AppConfig extends ResourceConfig {
    public AppConfig() {

        packages("com.test.moneytransfers");

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(InMemoryMoneyTransferService.class).to(MoneyTransferService.class).in(Singleton.class);
            }
        });
    }


}