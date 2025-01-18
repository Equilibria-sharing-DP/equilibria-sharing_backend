package api.equilibria_sharing.controller;
import api.equilibria_sharing.model.*;
import api.equilibria_sharing.model.requests.*;
import api.equilibria_sharing.repositories.*;
import api.equilibria_sharing.utilities.LoadAccommodations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        log.info("Main traveler after setting personal data: {}", mainTraveler);

        // create & save address of main traveler
        Address address = new Address();
        address.setStreet(bookingRequest.getMainTraveler().getStreet());
        address.setCity(bookingRequest.getMainTraveler().getCity());
        address.setHouseNumber(bookingRequest.getMainTraveler().getHouseNumber());
        address.setPostalCode(bookingRequest.getMainTraveler().getPostalCode());
        address.setAddressAdditional(bookingRequest.getMainTraveler().getAddressAdditional());
        addressRepository.save(address);

        mainTraveler.setAddress(address);

        log.info("Main traveler after setting address: {}", mainTraveler);

        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setPassportNr(bookingRequest.getMainTraveler().getPassportNr());
        travelDocument.setCountry(bookingRequest.getMainTraveler().getCountry());
        travelDocument.setIssueDate(bookingRequest.getMainTraveler().getIssueDate());
        travelDocument.setIssuingAuthority(bookingRequest.getMainTraveler().getIssuingAuthority());

        mainTraveler.setTravelDocument(travelDocument);
        personRepository.save(mainTraveler);

        log.info("Main traveler after setting travel document & saving: {}", mainTraveler);

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
}
