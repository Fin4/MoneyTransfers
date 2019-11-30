package com.test.moneytransfers.api.rest;

import com.test.moneytransfers.dto.AccountDto;
import com.test.moneytransfers.dto.AccountPostRequetDto;
import com.test.moneytransfers.model.Account;
import com.test.moneytransfers.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/accounts")
public class AccountResource {

    private AccountService accountService;

    @Inject
    public AccountResource(AccountService accountService) {
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
        return accountService.getAll().stream()
                .map(AccountDto::from)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response postAccount(AccountPostRequetDto accRequest, @Context UriInfo uriInfo) {

        AccountDto createdAccount = AccountDto.from(accountService.create(accRequest));

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(createdAccount.id.toString());

        return Response.created(builder.build()).entity(createdAccount).build();

    }

    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response deleteAccount(@PathParam("id") Long id) {
        Account deletedAcc = accountService.delete(id);

        return Response.ok().entity(deletedAcc).build();
    }
}
