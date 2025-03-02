package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Accommodation entity - for the booking process
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Accommodation accommodation;

    @ManyToOne
    private Person mainTraveler;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Person> additionalGuests;

    private LocalDateTime checkIn;
    private LocalDateTime expectedCheckOut;
    private LocalDateTime actualCheckOut;
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

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getExpectedCheckOut() {
        return expectedCheckOut;
    }

    public void setExpectedCheckOut(LocalDateTime expectedCheckOut) {
        this.expectedCheckOut = expectedCheckOut;
    }

    public LocalDateTime getActualCheckOut() {
        return actualCheckOut;
    }

    public void setActualCheckOut(LocalDateTime actualCheckOut) {
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
     * if the city where the main traveler lives equals the city where the accommodation is -> no tourist tax,
     * else, yes
     */
    public void calculateTouristTax() {
        touristTax = Objects.equals(mainTraveler.getAddress().getCity(), accommodation.getAddress().getCity());
    }

    /**
     * Calculate all travelers that are over 18, important for tourist tax
     */
    public void calculatePeopleOver18() {
        int count = 0;

        // check age of main traveler
        if (mainTraveler != null && mainTraveler.getBirthDate() != null) {
            int age = calculateAge(mainTraveler.getBirthDate(), LocalDate.from(checkIn));
            if (age >= 18) {
                count++;
            }
        }

        // check age of additional guests
        if (additionalGuests != null) {
            for (Person guest : additionalGuests) {
                if (guest.getBirthDate() != null) {
                    int age = calculateAge(guest.getBirthDate(), LocalDate.from(checkIn));
                    if (age >= 18) {
                        count++;
                    }
                }
            }
        }

        // set the counter
        this.peopleOver18 = count;
    }

    /**
     * utility method to calculate the age in years
     *
     * @param birthDate     birth year of person
     * @param referenceDate the reference year
     * @return the age in years
     */
    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return referenceDate.getYear() - birthDate.getYear()
                - (referenceDate.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }

    

}

