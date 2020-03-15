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
    }
    
    //Constructor for making PersonDTOs with data from a POST
    public HobbyDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public HobbyDTO() {
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

    @Override
    public String toString() {
        return "HobbyDTO{" + "name=" + name + ", description=" + '}';
    }
    
    
}
