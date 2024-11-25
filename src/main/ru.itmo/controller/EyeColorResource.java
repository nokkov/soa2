package ru.itmo.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/eye-color")
public class EyeColorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEyeColors() {
        List<String> colors = Arrays.asList("blue", "green", "brown", "hazel");
        return Response.ok(colors).build();
    }
}