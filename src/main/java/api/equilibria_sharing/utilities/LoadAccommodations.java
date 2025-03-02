package api.equilibria_sharing.utilities;

import api.equilibria_sharing.model.Accommodation;
import api.equilibria_sharing.model.Address;
import api.equilibria_sharing.repositories.AddressRepository;
import api.equilibria_sharing.repositories.AccommodationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadAccommodations {
    private static final Logger log = LoggerFactory.getLogger(LoadAccommodations.class);

    @Bean
    CommandLineRunner commandLineRunner(AccommodationRepository accommodationRepository, AddressRepository addressRepository) {
        Address address = new Address("Vienna", "Austria", 1200, "Wexstrasse", 23, null);
        addressRepository.save(address);
        return args -> {
            log.info("Preloading " + accommodationRepository.save(new Accommodation("TGM", "Apartment", "Wonderful apartment ahh", address, 3000, 27)));
        };
    }
}
