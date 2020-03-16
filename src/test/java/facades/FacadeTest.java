package facades;

import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import entities.RenameMe;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;


public class FacadeTest {

    private static EntityManagerFactory emf;
    private static Facade facade;
    private EntityManager em;

    private Person p1, p2;
    private Hobby h1, h2, h3;
    private Address a1, a2;
    private CityInfo c1, c2;
    private Phone phone1, phone2, phone3;

    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = Facade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            c1 = new CityInfo(2100, "KBH Ø");
            c2 = new CityInfo(2300, "KBH S");

            p1 = new Person(1, "email", "Gurli", "Mogensen");
            p2 = new Person(2, "mail", "Gunnar", "Hjorth");

            h1 = new Hobby("Cykling", "Cykling på hold");
            h2 = new Hobby("Film", "Gyserfilm");
            h3 = new Hobby("Film", "Dramafilm");

            a1 = new Address("Testgade", "dejligt sted");
            a2 = new Address("Testvej", "fint sted");

            phone1 = new Phone(1234, "hjemmetelefon");
            phone2 = new Phone(5678, "mobil");
            phone3 = new Phone(4321, "arbejdstelefon");

            //adding address to CityInfo
            c1.addAddress(a1);
            c2.addAddress(a2);
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetPhonesFromPerson() throws PersonNotFoundException {
        //expecting p1 Gurli Mogensen to have 2 phones
        assertEquals(2, facade.getPhonesFromPerson("Gurli", "Mogensen").size());
    }
    @Test
    public void testGetPhonesFromPerson2() throws PersonNotFoundException {
        //expecting p1 Gurli Mogensen to have 2 phones
        assertEquals(1, facade.getPhonesFromPerson("Gunnar", "Hjorth").size());
    }

    @Test
    public void testGetPhonesFromPersonWrongName() throws PersonNotFoundException {
        try {
            List<PhoneDTO> phones = facade.getPhonesFromPerson("forkert", "Mogensen");
            fail("Expected a PersonNotFoundException to be thrown");
        } catch (PersonNotFoundException ex) {
            assertThat(ex.getMessage(), is("Person with given name could not be found"));
        }  
    }
    
    @Test
    public void testEditPersonPhone() throws PersonNotFoundException {
        String firstName = "Gurli";
        String lastName = "Mogensen";
        int oldNumber = 1234;
        int newNumber = 9999;
        String newDescription = "new number";
        PersonDTO p = facade.editPersonPhone(firstName, lastName, oldNumber, newNumber, newDescription);
        assertTrue(p.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 9999));
    }
    
    @Test
    public void testEditPersonHobby() throws PersonNotFoundException {
        String firstName = "Gurli";
        String lastName = "Mogensen";
        String oldHobbyName = "Film";
        String newHobbyName = "Løbe";
        String newDescription = "new hobby";
        PersonDTO p = facade.editPersonHobby(firstName, lastName, oldHobbyName, newHobbyName, newDescription);
        assertTrue(p.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Løbe")));
    }

}
