package com.test.moneytransfers.api.rest;

import com.test.moneytransfers.dto.AccountDto;
import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.dto.TransferDto;
import com.test.moneytransfers.dto.TransferRequestDto;
import com.test.moneytransfers.service.MoneyTransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/accounts")
public class AccountResource {

    private MoneyTransferService accountService;

    @Inject
    public AccountResource(MoneyTransferService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public AccountDto getAccount(@PathParam("id") Long id) {
        return AccountDto.from(accountService.getById(id));
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public List<AccountDto> getAccounts() {
        return accountService.listAccounts().stream()
                             .map(AccountDto::from)
                             .collect(Collectors.toList());
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response postAccount(AccountPostRequetDto accRequest, @Context UriInfo uriInfo) {

        var createdAccount = AccountDto.from(accountService.create(accRequest));

        var location = uriInfo.getAbsolutePathBuilder()
                              .path(createdAccount.id.toString())
                              .build();

        return Response.created(location).entity(createdAccount).build();

    }

    @POST
    @Path("/transfers")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response transferMoney(TransferRequestDto transferRequest, @Context UriInfo uriInfo) {

        var transfer = TransferDto.from(accountService.transferMoney(transferRequest.getSenderId(),
                                                                transferRequest.getReceiverId(),
                                                                transferRequest.getAmount()));

        var location = uriInfo.getAbsolutePathBuilder()
                              .path(transfer.id.toString())
                              .build();

        return Response.created(location).entity(transfer).build();
    }

    @GET
    @Path("/transfers")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<TransferDto> listTransfers() {
        return accountService.listTransfers().stream()
            .map(TransferDto::from)
            .collect(Collectors.toList());
    }


    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response deleteAccount(@PathParam("id") Long id) {
        var deletedAcc = accountService.delete(id);

        return Response.ok().entity(deletedAcc).build();
    }
}
