package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {}
