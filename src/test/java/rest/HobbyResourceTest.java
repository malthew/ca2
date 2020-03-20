package rest;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import facades.Facade;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

public class HobbyResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Facade facade;
    private EntityManager em;
    
    private Person p1, p2;
    private Hobby h1, h2, h3;
    private Address a1, a2;
    private CityInfo c1, c2;
    private Phone phone1, phone2, phone3;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            c1 = new CityInfo(2100, "KBH Ø");
            c2 = new CityInfo(2300, "KBH S");

            p1 = new Person(1, "email", "Gurli", "Mogensen");
            p2 = new Person(2, "mail", "Gunnar", "Hjorth");

            h1 = new Hobby("Cykling", "Cykling på hold");
            h2 = new Hobby("Film", "Gyserfilm");
            h3 = new Hobby("Hockey", "Indendørs hockey");

            a1 = new Address("Testgade", "dejligt sted");
            a2 = new Address("Testvej", "fint sted");

            phone1 = new Phone(1234, "hjemmetelefon");
            phone2 = new Phone(5678, "mobil");
            phone3 = new Phone(4321, "arbejdstelefon");

            //adding address to CityInfo
            c1.addAddress(a1);
            c2.addAddress(a2);
            //adding cityInfo to address
            a1.setCityInfo(c1);
            a2.setCityInfo(c2);
            //Persisting CityInfo
            em.persist(c1);
            em.persist(c2);
            //adding address to persons
            p1.setAddress(a1);
            p2.setAddress(a2);
            //setting persons to phones
            phone1.setPerson(p1);
            phone2.setPerson(p1);
            phone3.setPerson(p2);
            //adding phones to persons
            p1.addPhone(phone1);
            p1.addPhone(phone2);
            p2.addPhone(phone3);
            //adding hobbies to persons 
            p1.addHobby(h1);
            p1.addHobby(h2);
            p2.addHobby(h3);
            //persisting persons
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testFindHobby() {
            given()
                .contentType("application/json")
                .get("/hobby/"+h1.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo("Cykling"))
                .body("description", equalTo("Cykling på hold"));
    }
    
    @Test
    public void testGetAllPersonsWithHobby() {
        given()
                .contentType("application/json")
                .get("/hobby/persons/" + h1.getName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", hasItems("Gurli"))
                .body("lastName", hasItems("Mogensen"));
    }
    
    @Test
    public void testAddHobby() {

        HobbyDTO hobbyToBeAdded = new HobbyDTO("Test", "new hobby");

        HobbyDTO result
                = with()
                        .body(hobbyToBeAdded) //include object in body
                        .contentType("application/json")
                        .when().request("POST", "/hobby/createhobby").then() //post REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(HobbyDTO.class); //extract result JSON as object

        //checking that nothing has changed that we don't want to change
        assertThat((result.getName()), equalTo("Test"));
        assertThat((result.getDescription()), equalTo("new hobby"));
    }
       
}
