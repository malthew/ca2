package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name = "Phone.deleteAllRows", query = "DELETE from Phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private int number;
    private String description;
    @ManyToOne
    private Person person;


    public Phone() {
    }

    public Phone(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }
    
        public void setPerson(Person person) {
        this.person = person;
    }
           
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Phone{" + "id=" + id + ", number=" + number + ", description=" + description + ", person=" + person + '}';
    }
    
    

}
