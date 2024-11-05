package com.spotler.resources;

import com.google.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/admin")
public class AdminResource {

    @Inject
    public AdminResource() {
    }

    @GET
    @Path("/status")
    public String health() {
        return "OK";
    }
}
