package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Entity
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Accommodation accommodation;

    @Enumerated(EnumType.STRING)
    private Country apartmentCountry;

    @OneToOne
    private Person mainPerson;

    @OneToMany
    private List<Person> persons;


    private LocalDateTime checkIn;

    private LocalDateTime expectedCheckOut;

    private LocalDateTime actualCheckOut;

    private int totalGuests = 0;

    private int totalGuestsOver18 = 0;

    boolean touristTax;

    public Form(Accommodation accommodation, Country apartmentCountry, Person mainPerson, List<Person> persons, LocalDateTime checkIn, LocalDateTime expectedCheckOut, LocalDateTime actualCheckOut) {
        this.accommodation = accommodation;
        this.apartmentCountry = apartmentCountry;
        this.mainPerson = mainPerson;
        this.persons = persons;
        this.checkIn = checkIn;
        this.expectedCheckOut = expectedCheckOut;
        this.actualCheckOut = actualCheckOut;
        this.calculateTouristTax();
    }

    public Form() {
    }

    public void calculateTouristTax() {
        this.touristTax = apartmentCountry == Country.ITALY &&
                !mainPerson.getAddress().getCity().equalsIgnoreCase(accommodation.getAddress().getCity());
    }

    public void calculateGuests() {
        this.totalGuests = persons.size() + 1;

        this.totalGuestsOver18 = (int) persons.stream()
                .filter(person -> calculateAge(person) >= 18)
                .count();

        if (calculateAge(mainPerson) >= 18) {
            this.totalGuestsOver18++;
        }
    }

    public int calculateAge(Person person)  {
        return Period.between(person.getBirthDate(), LocalDate.now()).getYears();
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getApartmentCountry() {
        return apartmentCountry;
    }

    public void setApartmentCountry(Country apartmentCountry) {
        this.apartmentCountry = apartmentCountry;
    }

    public Person getMainPerson() {
        return mainPerson;
    }

    public void setMainPerson(Person mainPerson) {
        this.mainPerson = mainPerson;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
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

    public int getTotalGuests() {
        return totalGuests;
    }

    public void setTotalGuests(int totalGuests) {
        this.totalGuests = totalGuests;
    }

    public int getTotalGuestsOver18() {
        return totalGuestsOver18;
    }

    public void setTotalGuestsOver18(int totalGuestsOver18) {
        this.totalGuestsOver18 = totalGuestsOver18;
    }

    public boolean isTouristTax() {
        return touristTax;
    }

    public void setTouristTax(boolean touristTax) {
        this.touristTax = touristTax;
    }

    @Override
    public String toString() {
        return "Form{" +
                "id=" + id +
                ", accommodation=" + accommodation +
                ", apartmentCountry=" + apartmentCountry +
                ", mainPerson=" + mainPerson +
                ", persons=" + persons +
                ", checkIn=" + checkIn +
                ", expectedCheckOut=" + expectedCheckOut +
                ", actualCheckOut=" + actualCheckOut +
                ", totalGuests=" + totalGuests +
                ", totalGuestsOver18=" + totalGuestsOver18 +
                ", touristTax=" + touristTax +
                '}';
    }
}
