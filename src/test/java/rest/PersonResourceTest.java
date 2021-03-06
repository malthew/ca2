package rest;

import dtos.AddressDTO;
import dtos.CityInfoDTO;
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
import java.util.ArrayList;
import java.util.List;
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

public class PersonResourceTest {

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
    public static void closeTestServer() {
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
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    @Test
    public void testGetPhonesFromPerson() {
        given()
                .contentType("application/json")
                .get("/person/phones/Gurli/Mogensen").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("description", hasItems("mobil", "hjemmetelefon"))
                .body("number", hasItems(1234, 5678));
    }

    @Test
    public void testGetPhonesFromPersonWrongName() {
        given()
                .contentType("application/json")
                .get("/person/phones/Gurli/Mogens").then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("code", equalTo(400))
                .body("message", equalTo("Bad Request"));
    }

    @Test
    public void testEditPersonPhone() {

        PersonDTO expResult = new PersonDTO(p1);

        PersonDTO result
                = with()
                        .body(expResult) //include object in body
                        .contentType("application/json")
                        .when().request("PUT", "/person/phone/1234/9999/new_phone").then() //put REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(PersonDTO.class); //extract result JSON as object

        //checking that nothing has changed that we don't want to change
        assertThat((result.getFirstName()), equalTo(expResult.getFirstName()));
        assertThat((result.getLastName()), equalTo(expResult.getLastName()));
        //checking that the person has the two phone numbers we want
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 9999));
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 5678));
        //checking that the person does not have the old number anymore
        assertFalse(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 1234));
    }

    @Test
    public void testEditPersonHobby() {

        PersonDTO expResult = new PersonDTO(p1);

        PersonDTO result
                = with()
                        .body(expResult) //include object in body
                        .contentType("application/json")
                        .when().request("PUT", "/person/hobby/Film/Løbe/new_hobby").then() //put REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(PersonDTO.class); //extract result JSON as object

        //checking that nothing has changed that we don't want to change
        assertThat((result.getFirstName()), equalTo(expResult.getFirstName()));
        assertThat((result.getLastName()), equalTo(expResult.getLastName()));
        //checking that the person has the two hobbies we want
        assertTrue(result.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Løbe")));
        assertTrue(result.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Cykling")));
        //checking that the person does not have the old hobby anymore
        assertFalse(result.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Film")));
    }

    @Test
    public void testAddPhone() {

        PhoneDTO phoneToBeAdded = new PhoneDTO(7777, "new phone");

        PersonDTO result
                = with()
                        .body(phoneToBeAdded) //include object in body
                        .contentType("application/json")
                        .when().request("POST", "/person/phone/Gurli/Mogensen").then() //post REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(PersonDTO.class); //extract result JSON as object

        //checking that nothing has changed that we don't want to change
        assertThat((result.getFirstName()), equalTo("Gurli"));
        assertThat((result.getLastName()), equalTo("Mogensen"));
        //checking that the person has the new phone with right number and description
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 7777));
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getDescription().equals("new phone")));
        //checking that the person still has both old phones
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 1234));
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 5678));
        //checking that the person has 3 phones now
        assertThat(result.getPhones().size(), equalTo(3));
    }

    @Test
    public void testCreatePerson() {
        Person person = new Person();
        person.setEmail("email@email.com");
        person.setLastName("Doe");
        person.setFirstName("Jane");
        person.setPersonid(35);
        PersonDTO expResult = new PersonDTO(person);
        PersonDTO result
                = with()
                        .body(expResult) //include object in body
                        .contentType("application/json")
                        .when().request("POST", "/person/createnormal").then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(PersonDTO.class); //extract result JSON as object

        //checking that we get the same personDTO back and we don't get an error.
        assertThat((result.getFirstName()), equalTo(expResult.getFirstName()));
        assertThat((result.getEmail()), equalTo(expResult.getEmail()));
        assertThat((result.getLastName()), equalTo(expResult.getLastName()));
        assertThat((result.getPersonid()), equalTo(expResult.getPersonid()));
    }

    @Test
    public void testFindPerson() {
        given()
                .contentType("application/json")
                .get("/person/" + p1.getPersonid()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Gurli"))
                .body("lastName", equalTo("Mogensen"));
    }
    
    @Test
    public void testCreatePersonWithInformation() {

        PersonDTO personToBeAdded = new PersonDTO(new Person(3, "email2", "Asger", "Jansen"));

        AddressDTO address = new AddressDTO(new Address("Testgade 4", "dejligt sted"));
        CityInfoDTO cityInfo = new CityInfoDTO(3000, "Ny by");
        address.setCityInfo(cityInfo);
        List<HobbyDTO> hobby = new ArrayList();
        hobby.add(new HobbyDTO("Cykling", "Cykling på hold"));
        hobby.add(new HobbyDTO("Svømning", "Crawl"));
        List<PhoneDTO> phone = new ArrayList();
        phone.add(new PhoneDTO(4444, "hjemmetelefon"));
        phone.add(new PhoneDTO(5555, "mobil"));
        personToBeAdded.setAddress(address);
        personToBeAdded.setHobbies(hobby);
        personToBeAdded.setPhones(phone);

        PersonDTO result
                = with()
                        .body(personToBeAdded) //include object in body
                        .contentType("application/json")
                        .when().request("POST", "/person/create").then().log().body() //post REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(PersonDTO.class); //extract result JSON as object

        //checking that the person has the right name
        assertThat((result.getFirstName()), equalTo("Asger"));
        assertThat((result.getLastName()), equalTo("Jansen"));
        //checking that the person has the right phone numbers
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 4444));
        assertTrue(result.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 5555));
        //checking that the person has the right hobbies
        assertTrue(result.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Cykling")));
        assertTrue(result.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Svømning")));
        //checking that the person has the right address
        assertThat(result.getAddress().getStreet(), equalTo("Testgade 4"));
    }

}
