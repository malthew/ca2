package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDTO;
import dtos.ExceptionDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import exceptions.AlreadyInOrderException;
import exceptions.AlreadyInUseException;
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

@Path("address")
public class AddressResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Facade FACADE = Facade.getFacade(EMF);

    
    @GET
    @Path("/{street}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAddress(@PathParam("street") String street) {
        try {
            return GSON.toJson(FACADE.findAddress(street));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
        
    }
    
    @GET
    @Path("persons/{zipCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonsWithZip(@PathParam("zipCode") int zipCode) {
        try {
            return GSON.toJson(FACADE.getAllPersonsWithZip(zipCode));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
        
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAddress(AddressDTO address) {
        try {
            return GSON.toJson(FACADE.createAddress(address));
        } catch (AlreadyInOrderException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }

}
