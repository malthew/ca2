/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Address;
import entities.Person;
import java.util.ArrayList;
import java.util.List;

public class AddressDTO {
    private String street;
    private String additionalInfo;
    private List<PersonDTO> persons;
    
    //Constructor for making AddressDTOs with data from the DB
    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
    }
    
    //Constructor for making addressDTOs with data from a POST
    public AddressDTO(String street, String additionalInfo) {
        this.street = street;
        this.additionalInfo = additionalInfo;
    }

    public AddressDTO() {
    }
    
    

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "AddressDTO{" + "street=" + street + ", additionalInfo=" + additionalInfo + ", persons=" + persons + '}';
    }
    
    
}
