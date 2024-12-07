package controller;

import java.util.Arrays;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/eye-color")
public class EyeColorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEyeColors() {
        List<String> colors = Arrays.asList("blue", "green", "brown", "hazel");
        return Response.ok(colors).build();
    }
}