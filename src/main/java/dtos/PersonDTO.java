package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;

public class PersonDTO {
    private String email;
    private String firstName;
    private String lastName;
    private List<Phone> phones;
    private Address address;
    private List<Hobby> hobbies;

    //Constructor for making personDTOs with data from the DB
    public PersonDTO(Person person) {
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phones = person.getPhones();
        this.address = person.getAddress();
        this.hobbies = person.getHobbys();
    }
    
    //Constructor for making PersonDTOs with data from a POST
    public PersonDTO(String email, String firstName, String lastName, List<Phone> phones, Address address, List<Hobby> hobbies) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = phones;
        this.address = address;
        this.hobbies = hobbies;
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

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
    
}
