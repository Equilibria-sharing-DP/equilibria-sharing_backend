package api.equilibria_sharing.model;

import jakarta.persistence.*;
// import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.time.LocalDate;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean mainTraveler;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    private TravelDocument travelDocument;

    @ManyToOne
    private Address address;

    @ManyToOne
    private Person mainTravelerRef;

    public Long getId() {
        return id;
    }

    public Boolean getMainTraveler() {
        return mainTraveler;
    }

    public void setMainTraveler(Boolean mainTraveler) {
        this.mainTraveler = mainTraveler;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public TravelDocument getTravelDocument() {
        return travelDocument;
    }

    public void setTravelDocument(TravelDocument travelDocument) {
        this.travelDocument = travelDocument;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Person getMainTravelerRef() {
        return mainTravelerRef;
    }

    public void setMainTravelerRef(Person mainTravelerRef) {
        this.mainTravelerRef = mainTravelerRef;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", mainTraveler=" + mainTraveler +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", travelDocument=" + travelDocument +
                ", address=" + address +
                ", mainTravelerRef=" + mainTravelerRef +
                '}';
    }
}
