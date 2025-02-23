package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TravelDocumentRepository - important for CRUD Operations regarding this entity
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, String> {
}

