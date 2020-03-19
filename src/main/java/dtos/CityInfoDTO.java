package dtos;

import entities.Address;
import entities.CityInfo;
import java.util.ArrayList;
import java.util.List;

public class CityInfoDTO {
    private int zipCode;
    private String city;
    //private List<AddressDTO> addresses = new ArrayList<>();
    
    //Constructor for making personDTOs with data from the DB
    public CityInfoDTO(CityInfo cityInfo) {
        this.zipCode = cityInfo.getZipCode();
        this.city = cityInfo.getCity();
    }
    
    //Constructor for making PersonDTOs with data from a POST
//    public CityInfoDTO(int zipCode, String city, List<AddressDTO> addresses) {
//        this.zipCode = zipCode;
//        this.city = city;
//        this.addresses = addresses;
//    }

    public CityInfoDTO(int zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }
    
    
    
    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    public List<AddressDTO> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<AddressDTO> addresses) {
//        this.addresses = addresses;
//    }
//    
//    public void addAddress(AddressDTO address){
//        this.addresses.add(address);
//    }
//
//    @Override
//    public String toString() {
//        return "CityInfoDTO{" + "zipCode=" + zipCode + ", city=" + city + ", addresses=" + addresses + '}';
//    }

    @Override
    public String toString() {
        return "CityInfoDTO{" + "zipCode=" + zipCode + ", city=" + city + '}';
    }
    
    
    
    
}
