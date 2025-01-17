package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Accommodation accomadation;

    @OneToOne
    private Person mainPerson;

    @OneToMany
    @JoinColumn(name = "person_id", nullable = false)
    private List<Person> persons;

    private LocalDateTime checkIn;

    private LocalDateTime expectedCheckOut;

    private LocalDateTime actualCheckOut;

    private boolean touristTax;
}
