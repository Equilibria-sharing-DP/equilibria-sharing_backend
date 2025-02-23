package api.equilibria_sharing.controller;
import api.equilibria_sharing.model.*;
import api.equilibria_sharing.model.requests.*;
import api.equilibria_sharing.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);


    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final AccommodationRepository accommodationRepository;
    private final AddressRepository addressRepository;

    public BookingController(BookingRepository bookingRepository,
                             PersonRepository personRepository,
                             AccommodationRepository accommodationRepository, AddressRepository addressRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * createBooking Method - Takes a POST Method from the frontend and saves & processes the form data from customer
     * @param bookingRequest formdata from customer
     * @return exception or http created
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        log.info("Create booking request...");
        log.info("Fetching accommodation: {}", bookingRequest.getAccommodationId());
        // Fetch Accommodation
        Accommodation accommodation = accommodationRepository.findById(bookingRequest.getAccommodationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation not found"));
        log.info("Accommodation: {}", accommodation);

        log.info("Initializing main traveler...");
        // Create Main Traveler
        Person mainTraveler = new Person();
        mainTraveler.setMainTraveler(true);
        mainTraveler.setFirstName(bookingRequest.getMainTraveler().getFirstName());
        mainTraveler.setLastName(bookingRequest.getMainTraveler().getLastName());
        mainTraveler.setGender(bookingRequest.getMainTraveler().getGender());
        mainTraveler.setBirthDate(bookingRequest.getMainTraveler().getBirthDate());

        // create & save address of main traveler
        Address address = new Address();
        address.setStreet(bookingRequest.getMainTraveler().getStreet());
        address.setCity(bookingRequest.getMainTraveler().getCity());
        address.setCountry(bookingRequest.getMainTraveler().getCountry());
        address.setHouseNumber(bookingRequest.getMainTraveler().getHouseNumber());
        address.setPostalCode(bookingRequest.getMainTraveler().getPostalCode());
        address.setAddressAdditional(bookingRequest.getMainTraveler().getAddressAdditional());
        addressRepository.save(address);

        mainTraveler.setAddress(address);

        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setType(bookingRequest.getMainTraveler().getTravelDocumentType());
        travelDocument.setDocumentNr(bookingRequest.getMainTraveler().getDocumentNr());
        travelDocument.setCountry(bookingRequest.getMainTraveler().getCountry());
        travelDocument.setIssueDate(bookingRequest.getMainTraveler().getIssueDate());
        travelDocument.setExpiryDate(bookingRequest.getMainTraveler().getExpiryDate());
        travelDocument.setIssuingAuthority(bookingRequest.getMainTraveler().getIssuingAuthority());

        mainTraveler.setTravelDocument(travelDocument);
        personRepository.save(mainTraveler);

        // Create Booking
        Booking booking = new Booking();
        booking.setAccommodation(accommodation);
        booking.setMainTraveler(mainTraveler);
        booking.setCheckIn(bookingRequest.getCheckIn());
        booking.setExpectedCheckOut(bookingRequest.getExpectedCheckOut());

        // Add Additional Guests
        List<Person> additionalGuests = bookingRequest.getAdditionalGuests().stream().map(guest -> {
            Person person = new Person();
            person.setMainTraveler(false);
            person.setFirstName(guest.getFirstName());
            person.setLastName(guest.getLastName());
            person.setBirthDate(guest.getBirthDate());
            person.setAddress(address);
            person.setMainTravelerRef(mainTraveler);
            personRepository.save(person);
            return person;
        }).collect(Collectors.toList());
        booking.setAdditionalGuests(additionalGuests);

        // calculate data relevant for the tourist tax
        booking.calculateTouristTax();
        booking.calculatePeopleOver18();

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Saved booking: {}", savedBooking);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    /**
     * get a specific booking my id
     * @param id booking id
     * @return booking object or throw exception if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long id) {
        log.info("Fetching booking by id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return ResponseEntity.ok(booking);
    }

    /**
     * Fetch all bookings that are stored in the DB
     * @return all bookings
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Booking>> getAllBookings() {
        log.info("Fetching all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(bookings);
    }

    /**
     * delete ALL booking entities in the db
     * @return http OK
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> deleteAllBookings() {
        log.info("Deleting all bookings");
        bookingRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * delete specific booking from the db
     * @param id booking id
     * @return http OK
     */
    @DeleteMapping("/{id}")
    
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> deleteBooking(@PathVariable("id") Long id) {
        log.info("Deleting booking by id: {}", id);
        bookingRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    /**
     * Update a specific booking entry
     * @param id booking id of entry to edit
     * @param bookingRequest new data that should replace the old data
     * @return http ok with updated data or throw exception
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") Long id, @RequestBody BookingRequest bookingRequest) {

        // get existing booking
        log.info("Updating booking with id: {}", id);
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        // get existing accommodation
        log.info("Fetching accommodation: {}", bookingRequest.getAccommodationId());
        Accommodation accommodation = accommodationRepository.findById(bookingRequest.getAccommodationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation not found"));
        existingBooking.setAccommodation(accommodation);

        // update the main traveler
        log.info("Updating main traveler...");
        Person mainTraveler = existingBooking.getMainTraveler();
        mainTraveler.setFirstName(bookingRequest.getMainTraveler().getFirstName());
        mainTraveler.setLastName(bookingRequest.getMainTraveler().getLastName());
        mainTraveler.setGender(bookingRequest.getMainTraveler().getGender());
        mainTraveler.setBirthDate(bookingRequest.getMainTraveler().getBirthDate());

        // update address of main traveler
        Address address = mainTraveler.getAddress();
        address.setStreet(bookingRequest.getMainTraveler().getStreet());
        address.setCity(bookingRequest.getMainTraveler().getCity());
        address.setCountry(bookingRequest.getMainTraveler().getCountry());
        address.setHouseNumber(bookingRequest.getMainTraveler().getHouseNumber());
        address.setPostalCode(bookingRequest.getMainTraveler().getPostalCode());
        address.setAddressAdditional(bookingRequest.getMainTraveler().getAddressAdditional());
        addressRepository.save(address);


        // update the travel document of main traveler
        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setType(bookingRequest.getMainTraveler().getTravelDocumentType());
        travelDocument.setDocumentNr(bookingRequest.getMainTraveler().getDocumentNr());
        travelDocument.setCountry(bookingRequest.getMainTraveler().getCountry());
        travelDocument.setIssueDate(bookingRequest.getMainTraveler().getIssueDate());
        travelDocument.setExpiryDate(bookingRequest.getMainTraveler().getExpiryDate());
        travelDocument.setIssuingAuthority(bookingRequest.getMainTraveler().getIssuingAuthority());
        mainTraveler.setTravelDocument(travelDocument);
        personRepository.save(mainTraveler);

        // updating checkIn and expected checkout time
        existingBooking.setCheckIn(bookingRequest.getCheckIn());
        existingBooking.setExpectedCheckOut(bookingRequest.getExpectedCheckOut());

        // Clear existing additional guests and add new ones
        existingBooking.getAdditionalGuests().clear();
        List<Person> additionalGuests = bookingRequest.getAdditionalGuests().stream().map(guest -> {
            Person person = new Person();
            person.setMainTraveler(false);
            person.setFirstName(guest.getFirstName());
            person.setLastName(guest.getLastName());
            person.setBirthDate(guest.getBirthDate());
            person.setAddress(mainTraveler.getAddress());
            person.setMainTravelerRef(mainTraveler);
            personRepository.save(person);
            return person;
        }).collect(Collectors.toList());
        existingBooking.setAdditionalGuests(additionalGuests);

        // Recalculate taxes and age-related info
        existingBooking.calculateTouristTax();
        existingBooking.calculatePeopleOver18();

        Booking updatedBooking = bookingRepository.save(existingBooking);

        return ResponseEntity.ok(updatedBooking);
    }
}
