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
import exceptions.AlreadyInUseException;
import exceptions.AlreadyInOrderException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

public class Facade implements FacadeInterface {

    private static Facade instance;
    private static EntityManagerFactory emf;

    private Facade() {
    }

    public static Facade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new Facade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PersonDTO editPersonPhone(String firstName, String lastName, int oldNumber, int newNumber, String newDescription) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person personToEdit = q.getSingleResult();

            List<Phone> phones = personToEdit.getPhones();
            //checking if the oldNumber provided is actually a number the person has
            for (Phone phone : phones) {
                if (phone.getNumber() == oldNumber) {
                    phone.setNumber(newNumber);
                    phone.setDescription(newDescription);
                }
            }

            em.getTransaction().begin();
            em.merge(personToEdit);
            em.getTransaction().commit();

            //making a list of phoneDTO's for the PersonDTO to have
            PersonDTO pDTO = new PersonDTO(personToEdit);
            List<PhoneDTO> phonedtos = new ArrayList<>();
            for (Phone phone : personToEdit.getPhones()) {
                phonedtos.add(new PhoneDTO(phone));
            }

            pDTO.setPhones(phonedtos);
            return pDTO;
        } catch (NoResultException ex) {
            throw new NotFoundException("Person with given name could not be found");
        } catch (NonUniqueResultException ex) {
            throw new NotFoundException("Multiple people with same name and last name found, cannot proceed with editing");
        } finally {
            em.close();
        }
    }

    public PersonDTO editPersonHobby(String firstName, String lastName, String oldHobbyName, String newHobbyName, String newDescription) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person personToEdit = q.getSingleResult();

            List<Hobby> hobbies = personToEdit.getHobbys();
            //remembering which hobby to remove person from and remove from person to avoid ConcurrentModificationException
            Hobby hobbyToChange = null;
            for (Hobby hobby : hobbies) {
                if (hobby.getName().equals(oldHobbyName)) {
                    hobbyToChange = hobby;
                }
            }
            //checking if we need to add a new hobby to person and remove a person from a hobby and doing it if needed
            if (hobbyToChange != null) {
                personToEdit.addHobby(new Hobby(newHobbyName, newDescription));
                personToEdit.removeHobby(hobbyToChange);
                hobbyToChange.removePerson(personToEdit);
            }

            em.getTransaction().begin();
            em.merge(personToEdit);
            em.getTransaction().commit();

            //making a list of hobbyDTO's for the PersonDTO to have
            PersonDTO pDTO = new PersonDTO(personToEdit);
            List<HobbyDTO> hobbydtos = new ArrayList<>();
            for (Hobby hobby : personToEdit.getHobbys()) {
                hobbydtos.add(new HobbyDTO(hobby));
            }

            pDTO.setHobbies(hobbydtos);
            return pDTO;
        } catch (NoResultException ex) {
            throw new NotFoundException("Person with given name could not be found");
        } catch (NonUniqueResultException ex) {
            throw new NotFoundException("Multiple people with same name and last name found, cannot proceed with editing");
        } finally {
            em.close();
        }
    }

    public List<PhoneDTO> getPhonesFromPerson(String firstName, String lastName) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person person = q.getSingleResult();

            List<Phone> phones = person.getPhones();
            //checking if the person has any phones
            if (phones.isEmpty()) {
                throw new NotFoundException("No phones found for person with that name");
            }
            //changing phones to phonedtos
            List<PhoneDTO> phonedtos = new ArrayList<>();
            for (Phone phone : phones) {
                phonedtos.add(new PhoneDTO(phone));
            }

            return phonedtos;
        } catch (NoResultException ex) {
            throw new NotFoundException("Person with given name could not be found");
        } catch (NonUniqueResultException ex) {
            throw new NotFoundException("Multiple people with same name and last name found");
        } finally {
            em.close();
        }
    }

    public void createPerson(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        Person entperson = new Person();
        entperson.setPersonid(person.getPersonid());
        entperson.setEmail(person.getEmail());
        entperson.setFirstName(person.getFirstName());
        entperson.setLastName(person.getLastName());
        try {
            em.getTransaction().begin();
            em.persist(entperson);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> findPerson(String firstName) {
        EntityManager em = emf.createEntityManager();
        //Person person;
        try {
            em.getTransaction().begin();
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName", Person.class)
                    .setParameter("firstName", firstName);
            em.getTransaction().commit();
            List<PersonDTO> list = new ArrayList();
            for (Person person : query.getResultList()) {
                list.add(new PersonDTO(person));
            }
            return list;
        } finally {
            em.close();
        }
    }

    // Not done 
    public void createPersonWithInformations(PersonDTO person, AddressDTO address, List<HobbyDTO> hobby, List<PhoneDTO> phone) throws AlreadyInUseException {
        EntityManager em = emf.createEntityManager();

        //Creating a Person entity
        Person entperson = new Person();
        entperson.setPersonid(person.getPersonid());
        entperson.setEmail(person.getEmail());
        entperson.setFirstName(person.getFirstName());
        entperson.setLastName(person.getLastName());

        em.getTransaction().begin();
        //em.persist(entperson);
        //Checking if address exist
        Address entaddress = findAddress(address);

        //Checking if phone(s) exist
        try {
            List<Phone> entphonelist = new ArrayList();
            for (PhoneDTO phoneDTO : phone) {
                Phone entphone = findPhone(phoneDTO);
                entphonelist.add(entphone);
            }
            if (entphonelist.size() != phone.size()) {
                throw new AlreadyInUseException("One of the phone numbers are already in use.");
            }

            //Checking if hobby(s) exist
            List<Hobby> enthobbylist = new ArrayList();
            for (HobbyDTO hobbys : hobby) {
                enthobbylist.add(findHobby(hobbys));
            }

            //Setting address
            if (entaddress == null) {
                entaddress = new Address(address.getStreet(), address.getAdditionalInfo());
                entperson.setAddress(entaddress);
            } else {
                entperson.setAddress(entaddress);
            }

            //Setting hobbies that already exists
            entperson.setHobbys(enthobbylist);
            //Setting remainding hobbies that needs to be created
            List<Hobby> newHobbyList = new ArrayList();
            for (Hobby hobby1 : enthobbylist) {
                if (hobby1 != null) {
                    for (HobbyDTO hobby2 : hobby) {
                        if (hobby1.getName() != hobby2.getName()) {
                            newHobbyList.add(hobby1);
                        }
                    }
                }

            }
            //entperson.setHobbys(newHobbyList);
            //Not sure if logic works ^

            //Setting phones
            entperson.setPhones(entphonelist);
            em.persist(entperson);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Address findAddress(AddressDTO address) {
        EntityManager em = emf.createEntityManager();
        Address entaddress = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE"
                    + " a.street = :address", Address.class)
                    .setParameter("address", address.getStreet());
            em.getTransaction().commit();
            entaddress = query.getSingleResult();
            return entaddress;
        } catch (Exception e) {
            return entaddress;
        } finally {
            em.close();
        }
    }

    public Hobby findHobby(HobbyDTO hobby) {
        EntityManager em = emf.createEntityManager();
        Hobby enthobby = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE"
                    + " h.name = :hobby", Hobby.class)
                    .setParameter("hobby", hobby.getName());
            em.getTransaction().commit();
            enthobby = query.getSingleResult();
            return enthobby;
        } catch (Exception e) {
            return enthobby;
        } finally {
            em.close();
        }
    }

    public Phone findPhone(PhoneDTO phone) {
        EntityManager em = emf.createEntityManager();
        Phone entphone = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE "
                    + " p.number = :number", Phone.class)
                    .setParameter("hobby", phone.getNumber());
            em.getTransaction().commit();
            entphone = query.getSingleResult();
            return entphone;
        } catch (Exception e) {
            return entphone;
        } finally {
            em.close();
        }
    }

    public void addHobby(HobbyDTO hobby) {
        EntityManager em = emf.createEntityManager();
        Hobby enthobby = new Hobby();
        enthobby.setName(hobby.getName());
        enthobby.setDescription(hobby.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(enthobby);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public long countFromHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            long count = (long) em.createQuery("SELECT COUNT(h) FROM Hobby h WHERE h.name = :hobby").setParameter("hobby", hobby).getSingleResult();
            return count;
        } finally {
            em.close();
        }
    }

    public void addCity(CityInfoDTO city) {
        EntityManager em = emf.createEntityManager();
        CityInfo entcity = new CityInfo();
        entcity.setZipCode(city.getZipCode());
        entcity.setCity(city.getCity());
        try {
            em.getTransaction().begin();
            em.persist(entcity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<CityInfoDTO> getAllZipCodes() {
        EntityManager em = emf.createEntityManager();
        try {
            List<CityInfoDTO> list = em.createQuery("SELECT c FROM CityInfo c").getResultList();
            return list;
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> getAllPersonsWithHobby(String hobbyName) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            List<Person> persons = em.createQuery("SELECT p FROM Person p JOIN p.hobbys ph WHERE ph.name = :hobby", Person.class)
                    .setParameter("hobby", hobbyName)
                    .getResultList();

            List<PersonDTO> persondtos = new ArrayList();
            for (Person person : persons) {
                persondtos.add(new PersonDTO(person));
            }

            return persondtos;
        } catch (NoResultException ex) {
            throw new NotFoundException("Persons with given Hobby name could not be found");
        } finally {
            em.close();
        }
    }

//    public List<PersonDTO> getAllPersonsWithZip(int zipCode) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            List<Person> persons = em.createQuery("SELECT p FROM Person p WHERE p.address")
//        }
//    }
    public String fillDB() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CityInfo c1 = new CityInfo(2100, "KBH Ø");
            CityInfo c2 = new CityInfo(2300, "KBH S");

            Person p1 = new Person(1, "email", "Gurli", "Mogensen");
            Person p2 = new Person(2, "mail", "Gunnar", "Hjorth");

            Hobby h1 = new Hobby("Cykling", "Cykling på hold");
            Hobby h2 = new Hobby("Film", "Gyserfilm");
            Hobby h3 = new Hobby("Film", "Dramafilm");

            Address a1 = new Address("Testgade", "dejligt sted");
            Address a2 = new Address("Testvej", "fint sted");

            Phone phone1 = new Phone(1234, "hjemmetelefon");
            Phone phone2 = new Phone(5678, "mobil");
            Phone phone3 = new Phone(4321, "arbejdstelefon");

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

            return "{\"status\":\"filled\"}";
        } finally {
            em.close();
        }
    }

    public List<HobbyDTO> getAllhobbies() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Hobby> hobbies = em.createQuery("SELECT h FROM Hobby h").getResultList();
            List<HobbyDTO> hobbydtos = new ArrayList<>();
            for (Hobby hobby : hobbies) {
                HobbyDTO hdto = new HobbyDTO(hobby);
                List<PersonDTO> persondtos = new ArrayList<>();
                for (Person person : hobby.getPersons()) {
                    persondtos.add(new PersonDTO(person));
                }
                hdto.setPersons(persondtos);
                hobbydtos.add(hdto);
            }
            return hobbydtos;
        } finally {
            em.close();
        }
    }

    public PersonDTO addPhone(PhoneDTO phonedto, String firstName, String lastName) throws NotFoundException, AlreadyInOrderException {
        EntityManager em = emf.createEntityManager();
        if (phonedto.getNumber() == 0 || phonedto.getDescription() == null){
            throw new NotFoundException("phone information missing in body");
        }
        Phone phone = new Phone(phonedto.getNumber(), phonedto.getDescription());
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);
            Person person = q.getSingleResult();
            
            
            //checking if person already has a phone with given number
            for(Phone p : person.getPhones()){
                if (p.getNumber() == phonedto.getNumber()){
                    throw new AlreadyInOrderException("Person already has a phone with that number");
                }
            }
            person.addPhone(phone);
            phone.setPerson(person);
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            
            //making list of PhoneDTOs for PersonDTO to have
            PersonDTO pDTO = new PersonDTO(person);
            List<PhoneDTO> phonedtos = new ArrayList<>();
            for (Phone ph : person.getPhones()) {
                phonedtos.add(new PhoneDTO(ph));
            }

            pDTO.setPhones(phonedtos);
            return pDTO;
        } catch (NoResultException ex) {
            throw new NotFoundException("Person with given name could not be found");
        } catch (NonUniqueResultException ex) {
            throw new NotFoundException("Multiple people with same name and last name found");
        } finally {
            em.close();
        }
    }

//    public static void main(String[] args) throws PersonNotFoundException {
//        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.NONE);
//        Facade facade = Facade.getFacade(emf);
//        PersonDTO pdto = facade.addPhone(new PhoneDTO(7777, "added phone"), "Gurli", "Mogensen");
//        //PersonDTO pdto = facade.editPersonPhone("Gurli", "Mogensen", 1234, 9999, "new phone");
//        System.out.println(pdto.getPhones());
//    }
//    public static void main(String[] args) throws AlreadyInUseException {
//        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
//        Facade pf = new Facade();
//        PersonDTO person = new PersonDTO(new Person(1, "email", "Gurli", "Mogensen"));
//        AddressDTO address = new AddressDTO(new Address("Testgade", "dejligt sted"));
//        List<HobbyDTO> hobby = new ArrayList();
//        hobby.add(new HobbyDTO("Cykling", "Cykling på hold"));
//        hobby.add(new HobbyDTO("Film", "Gyserfilm"));
//        List<PhoneDTO> phone = new ArrayList();
//        phone.add(new PhoneDTO(1234, "hjemmetelefon"));
//        phone.add(new PhoneDTO(5678, "mobil"));
//
//        pf.createPersonWithInformations(person, address, hobby, phone);
//    }
}
