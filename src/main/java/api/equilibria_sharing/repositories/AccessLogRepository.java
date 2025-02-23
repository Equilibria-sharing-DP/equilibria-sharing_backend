package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {}
