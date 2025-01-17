package api.equilibria_sharing.model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.time.LocalDate;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean mainTraveler;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToOne
    @JoinColumn(name = "travel_document")
    private TravelDocument travelDocument;


    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    //temp: slug mit dem der main traveler (manuell) referenziert werden kann
    private String mainTravelerRef;

    public Person() {

    }

    public Person(boolean mainTraveler, String firstName, String lastName, Gender gender, LocalDate birthDate, TravelDocument travelDocument, Address address, String mainTravelerRef) {
        this.mainTraveler = mainTraveler;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        if (mainTraveler) {
            this.travelDocument = travelDocument;
        } else {
            this.mainTravelerRef = mainTravelerRef;
        }
        this.address = address;
    }

    public boolean isMainTraveler() {
        return mainTraveler;
    }


    public void setMainTraveler(boolean mainTraveler) {
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

    public String getMainTravelerRef() {
        return mainTravelerRef;
    }

    public void setMainTravelerRef(String mainTravelerRef) {
        this.mainTravelerRef = mainTravelerRef;
    }
}
