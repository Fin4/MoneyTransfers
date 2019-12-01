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

    private final MoneyTransferService transferService;

    @Inject
    public AccountResource(MoneyTransferService transferService) {
        this.transferService = transferService;
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public AccountDto getAccount(@PathParam("id") Long id) {
        return AccountDto.from(transferService.getById(id));
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public List<AccountDto> getAccounts() {
        return transferService.listAccounts().stream()
                             .map(AccountDto::from)
                             .collect(Collectors.toList());
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response postAccount(AccountPostRequetDto accRequest, @Context UriInfo uriInfo) {

        var createdAccount = AccountDto.from(transferService.create(accRequest));

        var location = uriInfo.getAbsolutePathBuilder()
                              .path(createdAccount.id.toString())
                              .build();

        return Response.created(location).entity(createdAccount).build();

    }

    @PATCH
    @Path("/{id}")
    @Consumes({ MediaType.TEXT_PLAIN})
    @Produces({ MediaType.APPLICATION_JSON})
    public AccountDto deposit(@PathParam("id") Long id, String amount) {
        return AccountDto.from(transferService.deposit(id, amount));
    }

    @POST
    @Path("/{id}/transfers")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response transferMoney(@PathParam("id") Long id, TransferRequestDto transferRequest, @Context UriInfo uriInfo) {

        var transfer = TransferDto.from(
                transferService.transferMoney(
                        id,
                        transferRequest.getReceiverId(),
                        transferRequest.getAmount(),
                        transferRequest.getNotes()));

        var location = uriInfo.getAbsolutePathBuilder()
                              .path(transfer.id.toString())
                              .build();

        return Response.created(location).entity(transfer).build();
    }

    @GET
    @Path("/{id}/transfers")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<TransferDto> listTransfers(@PathParam("id") Long id) {
        return transferService.listTransfers(id).stream()
            .map(TransferDto::from)
            .collect(Collectors.toList());
    }


    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response deleteAccount(@PathParam("id") Long id) {
        var deletedAcc = transferService.delete(id);

        return Response.ok().entity(deletedAcc).build();
    }
}
