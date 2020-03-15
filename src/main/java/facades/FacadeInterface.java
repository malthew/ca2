package facades;

import dtos.PhoneDTO;
import exceptions.PersonNotFoundException;
import java.util.List;


public interface FacadeInterface {
    
    public long countOfPeopleWithHobby(String hobby);
    
}
