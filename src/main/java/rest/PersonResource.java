package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ExceptionDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import exceptions.PersonNotFoundException;
import facades.Facade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

@Path("person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Facade FACADE = Facade.getFacade(EMF);
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @GET
    @Path("phones/{firstname}/{lastname}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhoneDTO> getPhonesFromPerson(@PathParam("firstname") String firstname, @PathParam("lastname") String lastname) throws PersonNotFoundException {
        try {
        return FACADE.getPhonesFromPerson(firstname, lastname);
        }catch(PersonNotFoundException ex){
            //Jeg ved ikke hvordan vi håndtere exceptions
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
    
    @GET
    @Path("fill")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilling() {
        return FACADE.fillDB();
    }
}
