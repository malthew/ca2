package dtos;

import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;

public class HobbyDTO {
    private String name;
    private String description;
    private List<PersonDTO> persons;

    //Constructor for making personDTOs with data from the DB
    public HobbyDTO(Hobby hobby) {
        this.name = hobby.getName();
        this.description = hobby.getDescription();
        this.persons = new ArrayList<>();
        for (Person person : hobby.getPersons()) {
            this.persons.add(new PersonDTO(person));
        }
    }
    
    //Constructor for making PersonDTOs with data from a POST
    public HobbyDTO(String name, String description, List<PersonDTO> persons) {
        this.name = name;
        this.description = description;
        this.persons = persons;
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

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}
