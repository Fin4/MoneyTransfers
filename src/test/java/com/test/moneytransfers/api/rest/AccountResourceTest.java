package com.test.moneytransfers.api.rest;

import com.test.moneytransfers.dto.AccountDto;
import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.dto.TransferDto;
import com.test.moneytransfers.dto.TransferRequestDto;
import com.test.moneytransfers.server.MoneyTransferServer;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountResourceTest {

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
    public void getAccount() {
        var acc = createAccount("USD", "42.00")
                .readEntity(AccountDto.class);

        var response = getAccount(acc.id);
        var retrievedAcc = response.readEntity(AccountDto.class);

        assertEquals(200, response.getStatus());
        assertEquals("USD", retrievedAcc.currency);
        assertEquals("42.00", retrievedAcc.balance);
    }

    @Test
    public void getAccountNotFound() {

        var response = getAccount(Long.MAX_VALUE);

        assertEquals(404, response.getStatus());
        assertEquals("Account with given id does not exist", response.readEntity(String.class));
    }


    @Test
    public void createAccountSuccess() {

        var response = createAccount("EUR", "42.00");

        var createdAcc = response.readEntity(AccountDto.class);

        assertEquals(201, response.getStatus());
        assertEquals("42.00", createdAcc.balance);
        assertEquals("EUR", createdAcc.currency);
    }

    @Test
    public void deleteAccountSuccess() {

        var createdAcc = createAccount("EUR", "27.00")
                .readEntity(AccountDto.class);

        var response = client.target("http://localhost:8081/accounts/" + createdAcc.id)
                .request()
                .delete();

        var deletedAcc = response.readEntity(AccountDto.class);

        assertEquals(200, response.getStatus());
        assertEquals(createdAcc.balance, deletedAcc.balance);
        assertEquals(createdAcc.currency, deletedAcc.currency);
        assertEquals(createdAcc.id, deletedAcc.id);
    }

    @Test
    public void deleteAccountNotFound() {

        var response = client.target("http://localhost:8081/accounts/" + Long.MAX_VALUE)
                .request()
                .delete();

        assertEquals(404, response.getStatus());
        assertEquals("Account with given id does not exist", response.readEntity(String.class));
    }

    @Test
    public void transferSuccess() {
        var senderAcc = createAccount("EUR", "100.00")
                .readEntity(AccountDto.class);

        var receiverAcc = createAccount("EUR", "10.00")
                .readEntity(AccountDto.class);

        Response response = client.target("http://localhost:8081/accounts/transfers")
                .request()
                .post(Entity.json(new TransferRequestDto(senderAcc.id, receiverAcc.id, "55.33", "transfer notes")));

        var transfer = response.readEntity(TransferDto.class);
        var senderBalance = getBalance(senderAcc.id);
        var receiverBalance = getBalance(receiverAcc.id);

        assertEquals(201, response.getStatus());
        assertEquals("55.33", transfer.amount);
        assertEquals("transfer notes", transfer.notes);

        assertEquals("44.67", senderBalance);
        assertEquals("65.33", receiverBalance);

    }

    @Test
    public void transferFailure() {
        var senderAcc = createAccount("EUR", "100.00")
                .readEntity(AccountDto.class);

        var receiverAcc = createAccount("EUR", "10.00")
                .readEntity(AccountDto.class);

        Response response = client.target("http://localhost:8081/accounts/transfers")
                .request()
                .post(Entity.json(new TransferRequestDto(senderAcc.id, receiverAcc.id, "120", "transfer notes")));

        var message = response.readEntity(String.class);
        var senderBalance = getBalance(senderAcc.id);
        var receiverBalance = getBalance(receiverAcc.id);

        assertEquals(422, response.getStatus());
        assertEquals("Transfer not possible, insufficient funds", message);

        assertEquals("100.00", senderBalance);
        assertEquals("10.00", receiverBalance);

    }

    @Test
    public void depositSuccess() {
        //oops jersey client does not support PATCH method
    }


    private Response createAccount(String currency, String balance) {
        return client.target("http://localhost:8081/accounts")
                .request()
                .post(Entity.json(new AccountPostRequetDto(currency, balance)));
    }

    private Response getAccount(long id) {
        return client.target("http://localhost:8081/accounts/" + id)
                .request()
                .get();
    }

    private String getBalance(long accId) {
        return getAccount(accId)
                .readEntity(AccountDto.class)
                .balance;
    }
}
