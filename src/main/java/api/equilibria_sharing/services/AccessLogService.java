package api.equilibria_sharing.services;

import api.equilibria_sharing.model.AccessLog;
import api.equilibria_sharing.repositories.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * AccessLogsService - provides method that logs all accesses of employees, including the
 * username, endpoint, httpMethod, userAgent and the current LocalDateTime
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public void logAccess(String username, String endpoint, String httpMethod, String userAgent) {
        AccessLog log = new AccessLog(username, endpoint, httpMethod, userAgent, LocalDateTime.now());
        accessLogRepository.save(log);
    }
}
