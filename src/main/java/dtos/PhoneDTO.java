package dtos;

import entities.Phone;


public class PhoneDTO {
    private int number;
    private String description;
    private PersonDTO person;
    
    //Constructor for making personDTOs with data from the DB
    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
        this.description = phone.getDescription();
        this.person = new PersonDTO(phone.getPerson());
    }
    
    //Constructor for making PersonDTOs with data from a POST
    public PhoneDTO(int number, String description, PersonDTO person) {
        this.number = number;
        this.description = description;
        this.person = person;
    }
    

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }
    
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
