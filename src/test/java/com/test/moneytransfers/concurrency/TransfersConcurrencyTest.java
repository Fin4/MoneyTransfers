package com.test.moneytransfers.concurrency;

import com.test.moneytransfers.dto.AccountDto;
import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.dto.TransferDto;
import com.test.moneytransfers.dto.TransferRequestDto;
import com.test.moneytransfers.server.MoneyTransferServer;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.LongStream.rangeClosed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;


public class TransfersConcurrencyTest {

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

    @Test
    public void testTransfers() {

        createAccount("EUR", "100.00");
        createAccount("EUR", "100.00");

        final int num = 10;

        ExecutorService pool = Executors.newFixedThreadPool(2 * num);

        var ids = Stream.concat(
                IntStream.range(0, num).mapToObj(i -> pool.submit(() -> transferMoney(1, 2, "1.00"))),
                IntStream.range(0, num).mapToObj(i -> pool.submit(() -> transferMoney(2, 1, "3.00"))))
                .map(f -> get(f).readEntity(TransferDto.class))
                .map(dto -> dto.id)
                .collect(Collectors.toList());

        String accBalance1 = client.target("http://localhost:8081/accounts/1")
                                  .request()
                                  .get().readEntity(AccountDto.class).balance;

        String accBalance2 = client.target("http://localhost:8081/accounts/2")
                                .request()
                                .get().readEntity(AccountDto.class).balance;

        Assertions.assertEquals("120.00", accBalance1);
        Assertions.assertEquals("80.00", accBalance2);

        assertThat(ids, hasSize(2 * num));
        assertThat(ids, containsInAnyOrder(rangeClosed(1, 2 * num).boxed().toArray()));
    }

    private void createAccount(String currency, String amount) {
        client.target("http://localhost:8081/accounts")
                .request()
                .post(Entity.json(new AccountPostRequetDto(currency, amount)));
    }

    private Response transferMoney(long senderId, long receiverId, String amount) {
        return client.target("http://localhost:8081/accounts/transfers")
                .request()
                .post(Entity.json(new TransferRequestDto(senderId, receiverId, amount, "bla bla bla")));
    }

    private <T> T get(Future<T> f) {
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
