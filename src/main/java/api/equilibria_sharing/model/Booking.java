package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Accommodation accommodation;

    @ManyToOne
    private Person mainTraveler; // Hauptreisender

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Person> additionalGuests; // Liste von Mitreisenden

    private LocalDate checkIn;
    private LocalDate expectedCheckOut;
    private LocalDate actualCheckOut;
    private boolean touristTax;

    private int peopleOver18 = 0;

    public Long getId() {
        return id;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Person getMainTraveler() {
        return mainTraveler;
    }

    public void setMainTraveler(Person mainTraveler) {
        this.mainTraveler = mainTraveler;
    }

    public List<Person> getAdditionalGuests() {
        return additionalGuests;
    }

    public void setAdditionalGuests(List<Person> additionalGuests) {
        this.additionalGuests = additionalGuests;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getExpectedCheckOut() {
        return expectedCheckOut;
    }

    public void setExpectedCheckOut(LocalDate expectedCheckOut) {
        this.expectedCheckOut = expectedCheckOut;
    }

    public LocalDate getActualCheckOut() {
        return actualCheckOut;
    }

    public void setActualCheckOut(LocalDate actualCheckOut) {
        this.actualCheckOut = actualCheckOut;
    }

    public boolean getTouristTax() {
        return touristTax;
    }

    public void setTouristTax(boolean touristTax) {
        this.touristTax = touristTax;
    }

    public boolean isTouristTax() {
        return touristTax;
    }

    public int getPeopleOver18() {
        return peopleOver18;
    }

    public void setPeopleOver18(int peopleOver18) {
        this.peopleOver18 = peopleOver18;
    }

    /**
     * if the city where the main traveler lives equals the city where the accomodation is -> no tourist tax,
     * else, yes
     */
    public void calculateTouristTax() {
        touristTax = Objects.equals(mainTraveler.getAddress().getCity(), accommodation.getAddress().getCity());
    }

    public void calculatePeopleOver18() {
        int count = 0;

        // Prüfe das Alter des Hauptreisenden
        if (mainTraveler != null && mainTraveler.getBirthDate() != null) {
            int age = calculateAge(mainTraveler.getBirthDate(), checkIn);
            if (age >= 18) {
                count++;
            }
        }

        // Prüfe das Alter der Mitreisenden
        if (additionalGuests != null) {
            for (Person guest : additionalGuests) {
                if (guest.getBirthDate() != null) {
                    int age = calculateAge(guest.getBirthDate(), checkIn);
                    if (age >= 18) {
                        count++;
                    }
                }
            }
        }

        // Setze den Zähler
        this.peopleOver18 = count;
    }

    /**
     * Hilfsmethode, um das Alter anhand des Geburtsdatums und eines Referenzdatums zu berechnen.
     *
     * @param birthDate     Das Geburtsdatum der Person
     * @param referenceDate Das Referenzdatum (z. B. Check-in-Datum)
     * @return Das Alter in Jahren
     */
    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return referenceDate.getYear() - birthDate.getYear()
                - (referenceDate.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }
}

