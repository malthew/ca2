package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDTO;
import dtos.ExceptionDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import exceptions.AlreadyInOrderException;
import exceptions.NotFoundException;
import facades.Facade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

@Path("phone")
public class PhoneResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Facade FACADE = Facade.getFacade(EMF);

    
    @GET
    @Path("/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findPhone(@PathParam("number") int number) {
        try {
            return GSON.toJson(FACADE.findPhone(number));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPhone(PhoneDTO phone) {
        try {
            return GSON.toJson(FACADE.createPhone(phone));
        } catch (AlreadyInOrderException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
}
