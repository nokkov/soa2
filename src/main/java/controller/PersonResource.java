package controller;

import model.Person;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
    
    @PersistenceContext
    private EntityManager em;

    @GET
    public Response getPersons(@QueryParam("sort") String sort,
                               @QueryParam("filter") String filter,
                               @QueryParam("page") int page,
                               @QueryParam("size") int size) {
        List<Person> persons = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        return Response.ok(persons).build();
    }

    @POST
    @Transactional
    public Response addPerson(@Valid Person person) {
        person.setCreationDate(ZonedDateTime.now());
        em.persist(person);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getPerson(@PathParam("id") long id) {
        Person person = em.find(Person.class, id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(person).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response updatePerson(@PathParam("id") long id, @Valid Person person) {
        Person existingPerson = em.find(Person.class, id);
        if (existingPerson == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // FIXME: должны обновляться только запрошенные поля, иначе это не PATCH, а PUT
        existingPerson.setName(person.getName());
        existingPerson.setCoordinates(person.getCoordinates());
        existingPerson.setHeight(person.getHeight());
        existingPerson.setBirthday(person.getBirthday());
        existingPerson.setWeight(person.getWeight());
        existingPerson.setHairColor(person.getHairColor());
        existingPerson.setLocation(person.getLocation());

        em.merge(existingPerson);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePerson(@PathParam("id") long id) {
        Person person = em.find(Person.class, id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        em.remove(person);
        return Response.noContent().build();
    }

    @GET
    @Path("/count-by-location")
    public Response getCountByLocation(@QueryParam("location") String location) {
        Long count = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.location.name = :location", Long.class)
                .setParameter("location", location)
                .getSingleResult();
        return Response.ok(count).build();
    }
}
