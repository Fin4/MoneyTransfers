package com.test.moneytransfers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Hello {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "hello";
    }
}
