package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.Accommodation;
import api.equilibria_sharing.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AccommodationRepository - important for CRUD Operations regarding this entity
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    Accommodation findByName(String name);

}

