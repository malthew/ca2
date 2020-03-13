package entities;

import entities.Address;
import entities.Hobby;
import entities.Phone;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-13T18:25:14")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> firstName;
    public static volatile SingularAttribute<Person, String> lastName;
    public static volatile SingularAttribute<Person, Address> address;
    public static volatile ListAttribute<Person, Hobby> hobbys;
    public static volatile ListAttribute<Person, Phone> phones;
    public static volatile SingularAttribute<Person, Integer> personid;
    public static volatile SingularAttribute<Person, Long> id;
    public static volatile SingularAttribute<Person, String> email;

}