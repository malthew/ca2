package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name = "Hobby.deleteAllRows", query = "DELETE from Hobby")
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String name;
    private String description;
    @ManyToMany (mappedBy = "hobbys", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Person> persons = new ArrayList<>();


    public Hobby() {
    }

    public Hobby(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    
    public void addPerson(Person person){
        this.persons.add(person);
    }
    
    public void removePerson(Person person){
        Person personToBeRemoved = null;
        for (Person person_ : this.persons){
            if (person_.getFirstName().equals(person.getFirstName()) && person_.getLastName().equals(person.getLastName())){
                personToBeRemoved = person_;
            }
        }
                this.persons.remove(personToBeRemoved);
    }
    
    public List<Person> getPersons() {
        return persons;
    }
    
    @Override
    public String toString() {
        return "Hobby{" + "id=" + id + ", name=" + name + ", description=" + description + ", persons=" + persons + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

    
}
