package entities;

import entities.Person;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-13T17:20:03")
@StaticMetamodel(Address.class)
public class Address_ { 

    public static volatile ListAttribute<Address, Person> persons;
    public static volatile SingularAttribute<Address, String> street;
    public static volatile SingularAttribute<Address, String> additionalInfo;
    public static volatile SingularAttribute<Address, Long> id;

}