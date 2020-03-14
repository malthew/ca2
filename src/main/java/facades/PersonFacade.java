package facades;

import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class PersonFacade {
    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    private PersonFacade(){}
    
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    private PersonDTO editPersonPhone(String firstName, String lastName, int oldNumber, int newNumber, String newDescription) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);
            
              Person personToEdit = q.getSingleResult();
              if (personToEdit == null) throw new PersonNotFoundException("Person with given name could not be found");
              
              List<Phone> phones = personToEdit.getPhones();
              
              for (Phone phone : phones) {
                if (phone.getNumber() == oldNumber){
                    phone.setNumber(newNumber);
                    phone.setDescription(newDescription);
                }
            }

            em.getTransaction().begin();
            em.merge(personToEdit);
            em.getTransaction().commit();

            return new PersonDTO(personToEdit);
        } finally {
            em.close();
        }
    }
    
    private PersonDTO editPersonHobby(String firstName, String lastName, String oldHobbyName, String newHobbyName, String newDescription) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);
            
              Person personToEdit = q.getSingleResult();
              if (personToEdit == null) throw new PersonNotFoundException("Person with given name could not be found");
              
              List<Hobby> hobbies = personToEdit.getHobbys();
              
              for (Hobby hobby : hobbies) {
                if (hobby.getName().equals(oldHobbyName)){
                    hobby.setName(newHobbyName);
                    hobby.setDescription(newDescription);
                }
            }

            em.getTransaction().begin();
            em.merge(personToEdit);
            em.getTransaction().commit();

            return new PersonDTO(personToEdit);
        } finally {
            em.close();
        }
    }
    
    private List<PhoneDTO> getPhonesFromPerson(String firstName, String lastName) throws PersonNotFoundException{
         EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE "
                    + "p.firstName = :firstName AND p.lastName = :lastName", Person.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);
            
              Person person = q.getSingleResult();
              if (person == null) throw new PersonNotFoundException("Person with given name could not be found");
              
              List<Phone> phones = person.getPhones();
              List<PhoneDTO> phonedtos = new ArrayList<>();
              for (Phone phone : phones) {
                phonedtos.add(new PhoneDTO(phone));
            }

            return phonedtos;
        } finally {
            em.close();
        }
    }
    
}
