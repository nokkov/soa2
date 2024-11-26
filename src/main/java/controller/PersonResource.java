package controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.DatabaseManager;
import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;

import javax.validation.Valid;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    private DatabaseManager databaseManager;

    @GET
    public Response getPersons(@QueryParam("sort") String sort,
                               @QueryParam("filter") String filter,
                               @QueryParam("page") int page,
                               @QueryParam("size") int size) {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT * FROM persons";
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Person person = Person.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .coordinates(new Coordinates(rs.getDouble("coordinates_x"), rs.getLong("coordinates_y")))
                        .creationDate(rs.getObject("creation_date", ZonedDateTime.class))
                        .height(rs.getFloat("height"))
                        .birthday(rs.getDate("birthday"))
                        .weight(rs.getFloat("weight"))
                        .hairColor(Color.valueOf(rs.getString("hair_color")))
                        .location(new Location(rs.getDouble("location_x"), rs.getInt("location_y"), rs.getInt("location_z"), rs.getString("location_name")))
                        .build();
                persons.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(persons).build();
    }

    @POST
    public Response addPerson(@Valid Person person) {
        String query = "INSERT INTO persons (name, coordinates_x, coordinates_y, creation_date, height, birthday, weight, hair_color, location_x, location_y, location_z, location_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, person.getName());
            stmt.setDouble(2, person.getCoordinates().getX());
            stmt.setLong(3, person.getCoordinates().getY());
            stmt.setObject(4, ZonedDateTime.now());
            stmt.setFloat(5, person.getHeight());
            stmt.setDate(6, new java.sql.Date(person.getBirthday().getTime()));
            stmt.setObject(7, person.getWeight());
            stmt.setString(8, person.getHairColor().name());
            stmt.setDouble(9, person.getLocation().getX());
            stmt.setInt(10, person.getLocation().getY());
            stmt.setInt(11, person.getLocation().getZ());
            stmt.setString(12, person.getLocation().getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getPerson(@PathParam("id") long id) {
        String query = "SELECT * FROM persons WHERE id = ?";
        Person person = null;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    person = Person.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .coordinates(new Coordinates(rs.getDouble("coordinates_x"), rs.getLong("coordinates_y")))
                            .creationDate(rs.getObject("creation_date", ZonedDateTime.class))
                            .height(rs.getFloat("height"))
                            .birthday(rs.getDate("birthday"))
                            .weight(rs.getFloat("weight"))
                            .hairColor(Color.valueOf(rs.getString("hair_color")))
                            .location(new Location(rs.getDouble("location_x"), rs.getInt("location_y"), rs.getInt("location_z"), rs.getString("location_name")))
                            .build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(person).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") long id, @Valid Person person) {
        String query = "UPDATE persons SET name = ?, coordinates_x = ?, coordinates_y = ?, height = ?, birthday = ?, weight = ?, hair_color = ?, location_x = ?, location_y = ?, location_z = ?, location_name = ? WHERE id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            // FIXME: должны обновляться только запрошенные поля, иначе это не PATCH, а PUT
            stmt.setString(1, person.getName());
            stmt.setDouble(2, person.getCoordinates().getX());
            stmt.setLong(3, person.getCoordinates().getY());
            stmt.setFloat(4, person.getHeight());
            stmt.setDate(5, new java.sql.Date(person.getBirthday().getTime()));
            stmt.setObject(6, person.getWeight());
            stmt.setString(7, person.getHairColor().name());
            stmt.setDouble(8, person.getLocation().getX());
            stmt.setInt(9, person.getLocation().getY());
            stmt.setInt(10, person.getLocation().getZ());
            stmt.setString(11, person.getLocation().getName());
            stmt.setLong(12, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") long id) {
        String query = "DELETE FROM persons WHERE id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/count-by-location")
    public Response getCountByLocation(@QueryParam("location") String location) {
        String query = "SELECT COUNT(*) FROM persons WHERE location_name = ?";
        int count = 0;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, location);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(count).build();
    }
}
