package facades;

import dtos.AddressDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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

    public PersonDTO editPersonPhone(String firstName, String lastName, int oldNumber, int newNumber, String newDescription) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person personToEdit = q.getSingleResult();

            List<Phone> phones = personToEdit.getPhones();

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
            throw new PersonNotFoundException("Person with given name could not be found");
        } finally {
            em.close();
        }
    }

    public PersonDTO editPersonHobby(String firstName, String lastName, String oldHobbyName, String newHobbyName, String newDescription) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person personToEdit = q.getSingleResult();

            List<Hobby> hobbies = personToEdit.getHobbys();

            for (Hobby hobby : hobbies) {
                if (hobby.getName().equals(oldHobbyName)) {
                    hobby.setName(newHobbyName);
                    hobby.setDescription(newDescription);
                }
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
            throw new PersonNotFoundException("Person with given name could not be found");
        } finally {
            em.close();
        }
    }

    public List<PhoneDTO> getPhonesFromPerson(String firstName, String lastName) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            Person person = q.getSingleResult();

            List<Phone> phones = person.getPhones();
            List<PhoneDTO> phonedtos = new ArrayList<>();
            for (Phone phone : phones) {
                phonedtos.add(new PhoneDTO(phone));
            }

            return phonedtos;
        } catch (NoResultException ex) {
            throw new PersonNotFoundException("Person with given name could not be found");
        } finally {
            em.close();
        }
    }

    public void createPerson(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Not done 
    public void createPersonWithInformations(PersonDTO person, AddressDTO address, List<HobbyDTO> hobby, List<PhoneDTO> phone) {
        //public void createPersonWithInformations(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            person.setAddress(address);
            person.setHobbies(hobby);
            person.setPhones(phone);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    protected void addHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public long countOfPeopleWithHobby(String hobby) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private long countFromHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            long count = (long) em.createQuery("SELECT COUNT(h) FROM Hobby h WHERE h.name = :hobby").setParameter("hobby", hobby).getSingleResult();
            return count;
        } finally {
            em.close();
        }
    }

}
