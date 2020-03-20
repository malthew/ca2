package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDTO;
import dtos.ExceptionDTO;
import dtos.HobbyDTO;
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

@Path("hobby")
public class HobbyResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Facade FACADE = Facade.getFacade(EMF);

    
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findHobby(@PathParam("name") String name) {
        try {
            return GSON.toJson(FACADE.findHobby(name));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
        
    }
    
    @GET
    @Path("persons/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonsWithHobby(@PathParam("name") String hobbyName) {
        try {
            return GSON.toJson(FACADE.getAllPersonsWithHobby(hobbyName));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }

}
