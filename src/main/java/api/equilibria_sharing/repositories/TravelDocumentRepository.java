package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, String> {
}

