package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PersonRepository - important for CRUD Operations regarding this entity
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
