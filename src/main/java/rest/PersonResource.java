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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
    
    @PUT
    @Path("edit/phone/{oldNumber}/{newNumber}/{newDescription}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO editPersonPhone(PersonDTO person, @PathParam("oldNumber") int oldNumber,
            @PathParam("newNumber") int newNumber,@PathParam("newDescription") String newDescription) throws PersonNotFoundException {
        
        if (person.getFirstName() == null || person.getLastName() == null || oldNumber == 0 || newNumber == 0 || newDescription.isEmpty()) {
            throw new WebApplicationException("Not all required arguments included", 400);
        }
        try {
        return FACADE.editPersonPhone(person.getFirstName(), person.getLastName(), oldNumber, newNumber, newDescription);
        }catch(PersonNotFoundException ex){
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
    
    @PUT
    @Path("edit/hobby/{oldHobbyName}/{newHobbyName}/{newDescription}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO editPersonHobby(PersonDTO person, @PathParam("oldHobbyName") String oldHobbyName,
            @PathParam("newHobbyName") String newHobbyName,@PathParam("newDescription") String newDescription) throws PersonNotFoundException {     
        if (person.getFirstName() == null || person.getLastName() == null || oldHobbyName.isEmpty() || newHobbyName.isEmpty() || newDescription.isEmpty()) {
            throw new WebApplicationException("Not all required arguments included", 400);
        }
        try {
        return FACADE.editPersonHobby(person.getFirstName(), person.getLastName(), oldHobbyName, newHobbyName, newDescription);
        }catch(PersonNotFoundException ex){
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
    
    
}
