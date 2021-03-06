package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public PersonDTO editPersonHobby(String firstName, String lastName, String oldHobbyName, String newHobbyName, String newDescription) throws NotFoundException, AlreadyInOrderException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person personToEdit = q.getSingleResult();

            //checking if the hobby we are trying to add already exists in the DB
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE"
                    + " h.name = :hobby", Hobby.class)
                    .setParameter("hobby", newHobbyName);
            Hobby existingHobby = null;
            try {
                existingHobby = query.getSingleResult();
            } catch (NoResultException ex) {
                existingHobby = null;
            }

            List<Hobby> hobbies = personToEdit.getHobbys();

            //remembering which hobby to remove person from and remove from person to avoid ConcurrentModificationException
            Hobby hobbyToChange = null;
            for (Hobby hobby : hobbies) {
                if (hobby.getName().equals(oldHobbyName)) {
                    hobbyToChange = hobby;
                }
            }

            if (hobbyToChange == null) {
                throw new NotFoundException("Hobby you are trying to edit is not found with that person");
            }

            //if the existingHobby doesn't exist we create a new new Hobby to add to the person, otherwise we add the existing hobby
            if (existingHobby == null) {
                personToEdit.addHobby(new Hobby(newHobbyName, newDescription));
            } else {
                for (Person person : existingHobby.getPersons()) {
                    if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                        throw new AlreadyInOrderException("This person is already tied to the hobby you are trying to add");
                    }
                }
                personToEdit.addHobby(existingHobby);
            }
            personToEdit.removeHobby(hobbyToChange);
            hobbyToChange.removePerson(personToEdit);

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

    public PersonDTO createPerson(PersonDTO person) {
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
            return person;
        } finally {
            em.close();
        }
    }
    
    public PhoneDTO createPhone(PhoneDTO phone) throws AlreadyInOrderException {
        EntityManager em = emf.createEntityManager();
        Phone entphone = new Phone();
        entphone.setNumber(phone.getNumber());
        entphone.setDescription(phone.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(entphone);
            em.getTransaction().commit();
            return phone;
        } catch (Exception ex) {
            throw new AlreadyInOrderException("This phone is already created");
        } finally {
            em.close();
        }
    }
    
    public AddressDTO createAddress(AddressDTO address) throws AlreadyInOrderException {
        EntityManager em = emf.createEntityManager();
        Address entaddress = new Address();
        entaddress.setStreet(address.getStreet());
        entaddress.setAdditionalInfo(address.getAdditionalInfo());
        try {
            em.getTransaction().begin();
            em.persist(entaddress);
            em.getTransaction().commit();
            return address;
        } catch (Exception ex) {
            throw new AlreadyInOrderException("This address is already created");
        } finally {
            em.close();
        }
    }

    public PersonDTO findPerson(long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        //Person person;
        try {
            em.getTransaction().begin();
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.personid = :id", Person.class)
                    .setParameter("id", id);
            em.getTransaction().commit();
            return new PersonDTO(query.getSingleResult());
        } catch (Exception ex) {
            throw new NotFoundException("No person found with this ID");
        } finally {
            em.close();
        }
    }

    public AddressDTO findAddress(String street) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Address entaddress = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE"
                    + " a.street = :address", Address.class)
                    .setParameter("address", street);
            em.getTransaction().commit();
            entaddress = query.getSingleResult();
            return new AddressDTO(entaddress);
        } catch (Exception e) {
            throw new NotFoundException("No address found");
        } finally {
            em.close();
        }
    }

    public HobbyDTO findHobby(String hobby) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Hobby enthobby = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE"
                    + " h.name = :hobby", Hobby.class)
                    .setParameter("hobby", hobby);
            em.getTransaction().commit();
            enthobby = query.getSingleResult();
            return new HobbyDTO(enthobby);
        } catch (Exception e) {
            throw new NotFoundException("No hobby found");
        } finally {
            em.close();
        }
    }

    public PhoneDTO findPhone(int number) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Phone entphone = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE "
                    + " p.number = :number", Phone.class)
                    .setParameter("number", number);
            em.getTransaction().commit();
            entphone = query.getSingleResult();
            return new PhoneDTO(entphone);
        } catch (Exception e) {
            throw new NotFoundException("No phone found");
        } finally {
            em.close();
        }
    }

    public HobbyDTO addHobby(HobbyDTO hobby) throws AlreadyInUseException {
        EntityManager em = emf.createEntityManager();
        Hobby enthobby = new Hobby();
        enthobby.setName(hobby.getName());
        enthobby.setDescription(hobby.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(enthobby);
            em.getTransaction().commit();
            return hobby;
        } catch (Exception e) {
             throw new AlreadyInUseException("This hobby is already created");
        }
            finally {
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
            List<Person> persons = em.createQuery("SELECT p FROM Person p "
                    + "JOIN p.hobbys ph WHERE ph.name = :hobby", Person.class)
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

    public List<PersonDTO> getAllPersonsWithZip(int zipCode) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            List<Person> persons = em.createQuery("SELECT p FROM Person p "
                    + "JOIN p.address pa JOIN pa.cityInfo pac WHERE pac.zipCode = :zipCode", Person.class)
                    .setParameter("zipCode", zipCode)
                    .getResultList();

            List<PersonDTO> persondtos = new ArrayList();
            for (Person person : persons) {
                persondtos.add(new PersonDTO(person));
            }

            return persondtos;
        } catch (NoResultException ex) {
            throw new NotFoundException("Persons with given Zip Code could not be found");
        } finally {
            em.close();
        }
    }

    public String fillDB() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CityInfo c1 = new CityInfo(2100, "KBH Ø");
            CityInfo c2 = new CityInfo(2300, "KBH S");

            Person p1 = new Person(1, "email", "Gurli", "Mogensen");
            Person p2 = new Person(2, "mail", "Gunnar", "Hjorth");
            Person p4 = new Person(4, "gmail", "Georg", "Mogensen");

            Hobby h1 = new Hobby("Cykling", "Cykling på hold");
            Hobby h2 = new Hobby("Film", "Gyserfilm");
            Hobby h3 = new Hobby("Hockey", "Indendørs hockey");

            Address a1 = new Address("Testgade", "dejligt sted");
            Address a2 = new Address("Testvej", "fint sted");

            Phone phone1 = new Phone(1234, "hjemmetelefon");
            Phone phone2 = new Phone(5678, "mobil");
            Phone phone3 = new Phone(4321, "arbejdstelefon");

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
        if (phonedto.getNumber() == 0 || phonedto.getDescription() == null) {
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
            for (Phone p : person.getPhones()) {
                if (p.getNumber() == phonedto.getNumber()) {
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

    public PersonDTO createPersonWithInformations(PersonDTO person) throws AlreadyInUseException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Creating a Person entity
            Person entperson = new Person();
            entperson.setPersonid(person.getPersonid());
            entperson.setEmail(person.getEmail());
            entperson.setFirstName(person.getFirstName());
            entperson.setLastName(person.getLastName());

            //Checking if address exist
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE"
                    + " a.street = :address", Address.class)
                    .setParameter("address", person.getAddress().getStreet());
            Address entaddress = null;
            try {
                entaddress = query.getSingleResult();
            } catch (NoResultException ex) {
                entaddress = null;
            }

            //Setting address
            if (entaddress == null) {
                entaddress = new Address(person.getAddress().getStreet(), person.getAddress().getAdditionalInfo());
                //if the address is new we check in the cityInfo exists, if the address exists the cityInfo should also exist
                TypedQuery<CityInfo> query1 = em.createQuery("SELECT c FROM CityInfo c WHERE"
                        + " c.zipCode = :zipCode", CityInfo.class)
                        .setParameter("zipCode", person.getAddress().getCityInfo().getZipCode());
                CityInfo cityInfoent = null;
                try {
                    cityInfoent = query1.getSingleResult();
                } catch (NoResultException ex) {
                    cityInfoent = null;
                }
                //if cityInfo does not exist we create a new cityInfo object
                if (cityInfoent == null) {
                    cityInfoent = new CityInfo(person.getAddress().getCityInfo().getZipCode(), person.getAddress().getCityInfo().getCity());
                }
                //binding cityinfo to address, address to cityinfo, address to person and person to address
                cityInfoent.addAddress(entaddress);
                entaddress.setCityInfo(cityInfoent);
                entaddress.addPerson(entperson);
                entperson.setAddress(entaddress);
            } else {
                entperson.setAddress(entaddress);
                entaddress.addPerson(entperson);
            }

            //Checking if phone(s) exist    
            for (PhoneDTO phoneDTO : person.getPhones()) {
                TypedQuery<Phone> query2 = em.createQuery("SELECT p FROM Phone p WHERE "
                        + " p.number = :number", Phone.class)
                        .setParameter("number", phoneDTO.getNumber());
                Phone phoneToAdd = null;
                try {
                    phoneToAdd = query2.getSingleResult();
                } catch (NoResultException ex) {
                    phoneToAdd = null;
                }
                //if phone doesn't exist we create it and add it to the person
                if (phoneToAdd == null) {
                    phoneToAdd = new Phone(phoneDTO.getNumber(), phoneDTO.getDescription());
                    phoneToAdd.setPerson(entperson);
                    entperson.addPhone(phoneToAdd);
                } //if the phone is already in use we throw an exception
                else {
                    throw new AlreadyInUseException("One of the phone numbers are already in use.");
                }
            }
            //Checking if hobby(s) exist
            for (HobbyDTO hobby_ : person.getHobbies()) {
                TypedQuery<Hobby> query3 = em.createQuery("SELECT h FROM Hobby h WHERE"
                        + " h.name = :hobby", Hobby.class)
                        .setParameter("hobby", hobby_.getName());
                Hobby hobbyToAdd = null;
                try {
                    hobbyToAdd = query3.getSingleResult();
                } catch (NoResultException ex) {
                    hobbyToAdd = null;
                }
                //if the hobby exists already we bind the person to the hobby and the hobby to the person
                if (hobbyToAdd != null) {
                    hobbyToAdd.addPerson(entperson);
                    entperson.addHobby(hobbyToAdd);
                } //else we make a new hobby and bind the person and the hobby
                else {
                    hobbyToAdd = new Hobby(hobby_.getName(), hobby_.getDescription());
                    hobbyToAdd.addPerson(entperson);
                    entperson.addHobby(hobbyToAdd);
                }
            }
            em.persist(entperson);
            em.getTransaction().commit();
            PersonDTO pDTO = new PersonDTO(entperson);
            pDTO.addHobbiesFromEntity(entperson.getHobbys());
            pDTO.setAddressFromEntity(entperson.getAddress());
            pDTO.addPhonesFromEntity(entperson.getPhones());
            pDTO.setPersonid(entperson.getPersonid());
            return pDTO;
        } finally {
            em.close();
        }
    }


}
