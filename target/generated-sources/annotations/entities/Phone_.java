package entities;

import entities.Person;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-13T18:25:14")
@StaticMetamodel(Phone.class)
public class Phone_ { 

    public static volatile SingularAttribute<Phone, Integer> number;
    public static volatile SingularAttribute<Phone, Person> person;
    public static volatile SingularAttribute<Phone, String> description;
    public static volatile SingularAttribute<Phone, Long> id;

}