package com.test.moneytransfers.server;

import com.test.moneytransfers.config.AppConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class MoneyTransferServer {

    public static Server create(int port) {
        Server server = new Server(port);

        ResourceConfig config = new AppConfig();

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        servlet.setInitOrder(1);
        servlet.setInitParameter("jersey.config.server.provider.packages", "com.test.moneytransfers.api.rest");

        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.setContextPath("/");
        context.addServlet(servlet, "/*");

        return server;
    }

}
