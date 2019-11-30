package com.test.moneytransfers.service;

import com.test.moneytransfers.dto.AccountDto;
import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.server.MoneyTransferServer;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.LongStream.rangeClosed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;


public class TransferServiceTest {

    private static final Server server = MoneyTransferServer.create(8081);
    private static final Client client = ClientBuilder.newClient();

    @BeforeAll
    public static void startServer() throws Exception {
        server.start();
    }

    @AfterAll
    public static void destroyServer() throws Exception {
        server.stop();
        server.destroy();
    }

    /*@Test
    public void createAccountSuccess() {

        var response = client.target("http://localhost:8081/accounts")
                .request()
                .post(Entity.json(new AccountPostRequetDto("EUR", "27.00")));

        var createdAcc = response.readEntity(AccountDto.class);

        assertEquals(201, response.getStatus());
        assertEquals("27.00", createdAcc.balance);
        assertEquals("EUR", createdAcc.currency);
    }*/

    @Test
    public void shootTheServer() throws Exception {
        final int num = 12;

        ExecutorService pool = Executors.newFixedThreadPool(num);

        List<Future<Response>> eur = IntStream.range(0, num)
                .mapToObj(i -> pool.submit(() ->
                        client.target("http://localhost:8081/accounts")
                                .request()
                                .post(Entity.json(new AccountPostRequetDto("EUR", "27.00")))))
                .collect(Collectors.toList());

        List<Long> ids = new ArrayList<>();
        for (Future<Response> responseFuture : eur) {
            AccountDto accountDto = responseFuture.get().readEntity(AccountDto.class);
            ids.add(accountDto.id);
        }

        assertThat(ids, hasSize(num));
        assertThat(ids, containsInAnyOrder(rangeClosed(1, num).boxed().toArray()));
    }


}
