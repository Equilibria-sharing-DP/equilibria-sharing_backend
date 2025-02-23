package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AccessLogsRepository - important for CRUD Operations regarding this entity
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {}
