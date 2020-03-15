package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int personid;
    private String email;
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "person" ,
            cascade = CascadeType.PERSIST)
    private List<Phone> phones;
    @ManyToOne
    private Address address;
    @ManyToMany(mappedBy = "persons")
    private List<Hobby> hobbys;
    
    

    public Person() {
    }

    public Person(int personid, String email, String firstName, String lastName) {
        this.personid = personid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public Address getAddress() {
        return address;
    }

    public List<Hobby> getHobbys() {
        return hobbys;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setHobbys(List<Hobby> hobbys) {
        this.hobbys = hobbys;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    

    
}
