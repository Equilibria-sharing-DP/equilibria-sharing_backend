package api.equilibria_sharing.controller;
import api.equilibria_sharing.exceptions.ConflictException;
import api.equilibria_sharing.exceptions.ResourceNotFoundException;
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

/**
 * Accommodation Controller - manage all Accommodation entries
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */
@RestController
@RequestMapping("/api/v1/accommodations")
@PreAuthorize("isAuthenticated()")
public class AccommodationController {
    private static final Logger log = LoggerFactory.getLogger(AccommodationController.class);


    private final AccommodationRepository accommodationRepository;
    private final AddressRepository addressRepository;


    public AccommodationController(AccommodationRepository accommodationRepository, AddressRepository addressRepository) {
        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * createAccommodation Method- Takes a POST Request from the employee frontend part and saves & processes the form data from employee,
     * this method is protected by authentication / authorization structure, only an employee can create a new accommodation!
     * @param accommodationRequest request by frontend
     * @return Status OK and new object in response body
     */
    @PostMapping
    public ResponseEntity<Accommodation> createAccommodation(@RequestBody AccommodationRequest accommodationRequest)  {
        if (accommodationRepository.findByName(accommodationRequest.getName()) == null) {
            throw new ConflictException("Accommodation with name " + accommodationRequest.getName() + " already exists");
        }
        log.info("Creating a new accommodation...");
        log.info("Initializing Accommodation Address...");
        // initialize address from request
        Address address = new Address();
        address.setCity(accommodationRequest.getCity());
        address.setCountry(accommodationRequest.getCountry());
        address.setPostalCode(accommodationRequest.getPostalCode());
        address.setStreet(accommodationRequest.getStreet());
        address.setHouseNumber(accommodationRequest.getHouseNumber());
        address.setAddressAdditional(accommodationRequest.getAddressAdditional());
        addressRepository.save(address);
        log.info("Address: " + address);

        // initializing accommodation entity from request
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(address);
        accommodation.setName(accommodationRequest.getName());
        accommodation.setType(accommodationRequest.getType());
        accommodation.setDescription(accommodationRequest.getDescription());
        accommodation.setMaxGuests(accommodationRequest.getMaxGuests());
        accommodation.setPricePerNight(accommodationRequest.getPricePerNight());
        accommodation.setPictureUrls(accommodationRequest.getPictureUrls());
        accommodationRepository.save(accommodation);

        log.info("Initialized following accommodation entity:" + accommodation);

        return ResponseEntity.status(HttpStatus.CREATED).body(accommodation);
    }

    /**
     * Get all Accommodations - Employee must be authenticated
     * @return Status OK and accommodations as JSON in resp body
     */
    @GetMapping
    public ResponseEntity<List<Accommodation>> getAllAccommodations() {
        log.info("Fetching all Accommodations");
        List<Accommodation> accommodations = accommodationRepository.findAll();
        return ResponseEntity.ok(accommodations);
    }

    /**
     * Get accommodation with specific ID - Employee must be authenticated
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Accommodation> getAccommodationById(@PathVariable("id") Long id) {
        log.info("Fetching accommodation with id: " + id);
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation with ID " + id + " not found"));
        return ResponseEntity.ok(accommodation);
    }

    /**
     * Delete ALL accommodations - Employee must be authenticated
     * @return ok
     */
    @DeleteMapping
    public ResponseEntity<Accommodation> deleteAllAccommodations() {
        log.warn("Deleting all accommodations!");
        accommodationRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Delete specific accommodation with ID - Employee must be authenticated
     * @param id id of accommodation to be deleted
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Accommodation> deleteAccommodationById(@PathVariable("id") Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation with ID " + id + " not found"));
        log.warn("Deleting accommodation with id: " + id);
        accommodationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update an existing Accommodation object with new data - Employee must be authenticated
     * @param id id of existing accommodation object
     * @param accommodationRequest updated accommodation data
     * @return OK with new object as JSON in resp. body
     */
    @PutMapping("/{id}")
    public ResponseEntity<Accommodation> updateAccommodationById(@PathVariable("id") Long id, @RequestBody AccommodationRequest accommodationRequest) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation with ID " + id + " not found"));
        log.info("Updating accommodation with id: " + id);
        // fetching existing accommodation
        Accommodation existingAccommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found"));

        // initialize address from request
        Address address = new Address();
        address.setCity(accommodationRequest.getCity());
        address.setCountry(accommodationRequest.getCountry());
        address.setPostalCode(accommodationRequest.getPostalCode());
        address.setStreet(accommodationRequest.getStreet());
        address.setHouseNumber(accommodationRequest.getHouseNumber());
        address.setAddressAdditional(accommodationRequest.getAddressAdditional());
        addressRepository.save(address);
        log.info("Address: " + address);

        // updating accommodation object
        existingAccommodation.setName(accommodationRequest.getName());
        existingAccommodation.setType(accommodationRequest.getType());
        existingAccommodation.setDescription(accommodationRequest.getDescription());
        existingAccommodation.setMaxGuests(accommodationRequest.getMaxGuests());
        existingAccommodation.setPictureUrls(accommodationRequest.getPictureUrls());
        existingAccommodation.setAddress(address);

        accommodationRepository.save(existingAccommodation);

        return ResponseEntity.ok(existingAccommodation);

    }






}
