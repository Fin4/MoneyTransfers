package com.test.moneytransfers.server;

import org.eclipse.jetty.server.Server;

public class StartUp {

    public static void main(String[] args) throws Exception {
        Server server = MoneyTransferServer.create(8080);
        try {
            server.start();
            server.join();
        } finally {
            server.stop();
            server.destroy();
        }
    }
}
