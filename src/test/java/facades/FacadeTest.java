package facades;

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
import exceptions.AlreadyInOrderException;
import exceptions.AlreadyInUseException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private Person p1, p2, p4;
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
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            c1 = new CityInfo(2100, "KBH Ø");
            c2 = new CityInfo(2300, "KBH S");

            p1 = new Person(1, "email", "Gurli", "Mogensen");
            p2 = new Person(2, "mail", "Gunnar", "Hjorth");
            p4 = new Person(4, "gmail", "Georg", "Mogensen");

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
            p4.setAddress(a1);
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
            em.persist(p4);
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
    public void testGetallHobbies() {
        //expecting there to be 3 hobbies
        assertEquals(3, facade.getAllhobbies().size());
    }
    
    @Test
    public void testGetPhonesFromPerson() throws NotFoundException {
        //expecting p1 Gurli Mogensen to have 2 phones
        assertEquals(2, facade.getPhonesFromPerson("Gurli", "Mogensen").size());
    }
    @Test
    public void testGetPhonesFromPerson2() throws NotFoundException {
        //expecting p1 Gurli Mogensen to have 2 phones
        assertEquals(1, facade.getPhonesFromPerson("Gunnar", "Hjorth").size());
    }

    @Test
    public void testGetPhonesFromPersonWrongName() throws NotFoundException {
        try {
            List<PhoneDTO> phones = facade.getPhonesFromPerson("forkert", "Mogensen");
            fail("Expected a PersonNotFoundException to be thrown");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("Person with given name could not be found"));
        }  
    }
    
    @Test
    public void testEditPersonPhone() throws NotFoundException {
        String firstName = "Gurli";
        String lastName = "Mogensen";
        int oldNumber = 1234;
        int newNumber = 9999;
        String newDescription = "new number";
        PersonDTO p = facade.editPersonPhone(firstName, lastName, oldNumber, newNumber, newDescription);
        assertTrue(p.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 9999));
    }
    
    @Test
    public void testEditPersonHobby() throws NotFoundException, AlreadyInOrderException {
        String firstName = "Gurli";
        String lastName = "Mogensen";
        String oldHobbyName = "Film";
        String newHobbyName = "Løbe";
        String newDescription = "new hobby";
        PersonDTO p = facade.editPersonHobby(firstName, lastName, oldHobbyName, newHobbyName, newDescription);
        //checking if the person now has a hobby with the name "Løbe"
        assertTrue(p.getHobbies().stream().anyMatch(hobbyDTO -> hobbyDTO.getName().equals("Løbe")));
        //checking of the total list of hobbies is now 4
        assertTrue(facade.getAllhobbies().size() == 4);
    }
    
    @Test
    public void testAddHobby()  {
        assertEquals(0, facade.countFromHobby("Fodbold"));
        facade.addHobby(new HobbyDTO("Fodbold", "Amatør fodbold"));
        assertEquals(1, facade.countFromHobby("Fodbold"));
    }
    
    @Test
    public void testCountFromHobby()  {
        assertEquals(1, facade.countFromHobby("Film"));
    }
    
    @Test
    public void testAddCityAndGet()  {
        CityInfo city = new CityInfo(2820, "Gentofte");
        facade.addCity(new CityInfoDTO(city));
        //As already 2 citys are created in @BeforeEach there should now be 3
        assertThat(facade.getAllZipCodes(), hasSize(3));
    }
    
    @Test
    public void testCreatePersonWithNoExtraVal() throws NotFoundException  {
        Person persons = new Person(30, "email@email.com", "John", "Doe");
        facade.createPerson(new PersonDTO(persons));
        PersonDTO dtoperson = facade.findPerson(30);
        assertTrue(dtoperson.getFirstName().equals("John"));
    }
    
    @Test
    public void testGetAllPersonsWithHobby() throws NotFoundException {
        assertThat(facade.getAllPersonsWithHobby("Film"), hasSize(1));
    }
    
    @Test
    public void testGetAllPersonsWithZip() throws NotFoundException {
        assertThat(facade.getAllPersonsWithZip(2100), hasSize(2));
        assertThat(facade.getAllPersonsWithZip(2300), hasSize(1));
    }
    
    @Test
    public void testAddPhone() throws NotFoundException, AlreadyInOrderException {
        PhoneDTO phonedto = new PhoneDTO(8888, "added phone");
        String firstName = "Gurli";
        String lastName = "Mogensen";
        PersonDTO pDTO = facade.addPhone(phonedto, firstName, lastName);
        //checking if person now has a phone with number 8888
        assertTrue(pDTO.getPhones().stream().anyMatch(phoneDTO -> phoneDTO.getNumber() == 8888));
        //checking if person now has 3 phones (started with 2)
        assertTrue(pDTO.getPhones().size() == 3);
    }
    
    @Test
    public void testAddPhoneNumberAlreadyExists() throws NotFoundException, AlreadyInOrderException {
        //already has phone with number 1234
        PhoneDTO phonedto = new PhoneDTO(1234, "added phone");
        String firstName = "Gurli";
        String lastName = "Mogensen";
        try {
            PersonDTO pDTO = facade.addPhone(phonedto, firstName, lastName);
            fail("Expected a AlreadyInOrderException to be thrown");
        } catch (AlreadyInOrderException ex) {
            assertThat(ex.getMessage(), is("Person already has a phone with that number"));
        }
    }
    
    @Test
    public void testFindPerson() throws NotFoundException {
        assertEquals(facade.findPerson(p1.getPersonid()).getFirstName(), new PersonDTO(p1).getFirstName());
    }
    
    @Test
    public void testFailFindPerson() throws NotFoundException {
        Person p4 = p1;
        p4.setPersonid(1626);
        try {
            PersonDTO pDTO = facade.findPerson(new PersonDTO(p4).getPersonid());
            fail("No person found with this ID");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("No person found with this ID"));
        }
    }
    
    @Test
    public void testFindAddress() throws NotFoundException {
        assertEquals("Testgade", facade.findAddress(new AddressDTO(a1)).getStreet());
    }
    
    @Test
    public void testFailFindAddress() throws NotFoundException {
        Address a4 = a1;
        a1.setStreet("thiswillfail");
        try {
            AddressDTO a = facade.findAddress(new AddressDTO(a4));
            fail("No address found");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("No address found"));
        }
    }
    
    @Test
    public void testFindHobby() throws NotFoundException {
        assertEquals("Cykling", facade.findHobby(new HobbyDTO(h1)).getName());
    }
    
    @Test
    public void testFailFindHobby() throws NotFoundException {
        Hobby h4 = h1;
        h4.setName("thiswillfail");
        try {
            HobbyDTO h = facade.findHobby(new HobbyDTO(h4));
            fail("No hobby found");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("No hobby found"));
        }
    }
    
    @Test
    public void testFindPhone() throws NotFoundException {
        assertEquals(1234, facade.findPhone(new PhoneDTO(phone1)).getNumber());
    }
    
    @Test
    public void testFailFindPhone() throws NotFoundException {
        Phone p4 = phone1;
        p4.setNumber(12616212);
        try {
            PhoneDTO h = facade.findPhone(new PhoneDTO(p4));
            fail("No phone found");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("No phone found"));
        }
    }
    
    @Test
    public void testCreatePersonWithInformation() throws AlreadyInUseException, NotFoundException{
        PersonDTO person = new PersonDTO(new Person(3, "email2", "Asger", "Jansen"));
        AddressDTO address = new AddressDTO(new Address("Testgade 4", "dejligt sted"));
        CityInfoDTO cityInfo = new CityInfoDTO(3000, "Ny by");
        //cityInfo.addAddress(address);
        address.setCityInfo(cityInfo);
        List<HobbyDTO> hobby = new ArrayList();
        hobby.add(new HobbyDTO("Cykling", "Cykling på hold"));
        hobby.add(new HobbyDTO("Svømning", "Crawl"));
        List<PhoneDTO> phone = new ArrayList();
        phone.add(new PhoneDTO(4444, "hjemmetelefon"));
        phone.add(new PhoneDTO(5555, "mobil"));
        person.setAddress(address);
        person.setHobbies(hobby);
        person.setPhones(phone);
        PersonDTO result = facade.createPersonWithInformations(person);
        //checking if someone with the name Asger is now in the db so findPerson can fetch him
        assertThat(result.getFirstName(), is (facade.findPerson(3).getFirstName()));
        //checking if the persons new hobby has been added to db and can now be found
        assertNotNull(facade.findHobby(new HobbyDTO("Svømning", "Crawl")));       
    }
    
    @Test
    public void testCreatePersonWithInformationWithPhoneAlreadyInUse() throws AlreadyInUseException, NotFoundException{
        PersonDTO person = new PersonDTO(new Person(3, "email2", "Asger", "Jansen"));
        AddressDTO address = new AddressDTO(new Address("Testgade 4", "dejligt sted"));
        CityInfoDTO cityInfo = new CityInfoDTO(3000, "Ny by");
        //cityInfo.addAddress(address);
        address.setCityInfo(cityInfo);
        List<HobbyDTO> hobby = new ArrayList();
        hobby.add(new HobbyDTO("Cykling", "Cykling på hold"));
        hobby.add(new HobbyDTO("Svømning", "Crawl"));
        List<PhoneDTO> phone = new ArrayList();
        //changed number to 1234 a number that is already in use
        phone.add(new PhoneDTO(1234, "hjemmetelefon"));
        phone.add(new PhoneDTO(5555, "mobil"));
        person.setAddress(address);
        person.setHobbies(hobby);
        person.setPhones(phone);
        try {
        PersonDTO result = facade.createPersonWithInformations(person);
        fail("expected AlreadyInUseException to be thrown");
        }catch(AlreadyInUseException ex) {
            assertThat(ex.getMessage(), is("One of the phone numbers are already in use."));
        }
    }

}
