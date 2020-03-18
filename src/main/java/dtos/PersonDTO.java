package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PersonDTO {
    private int personid;
    private String email;
    private String firstName;
    private String lastName;
    private List<PhoneDTO> phones = new ArrayList<>();
    private AddressDTO address;
    private List<HobbyDTO> hobbies = new ArrayList<>();

    //Constructor for making personDTOs with data from the DB
    public PersonDTO(Person person) {
        this.personid = person.getPersonid();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

    public PersonDTO() {
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

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }
    
    public void addPhonesFromEntity(List<Phone> phones){
        for (Phone phone : phones) {
            this.phones.add(new PhoneDTO(phone));
        }
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
    
    public void setAddressFromEntity(Address address){
        this.address = new AddressDTO(address);
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }
    
    public void addHobbiesFromEntity(List<Hobby> hobbies){
        for (Hobby hobby : hobbies) {
            this.hobbies.add(new HobbyDTO(hobby));
        }
    }

    @Override
    public String toString() {
        return "PersonDTO{" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }
    
    
 
    
    
}
