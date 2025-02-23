package api.equilibria_sharing.services;

import api.equilibria_sharing.model.LoginLog;
import api.equilibria_sharing.repositories.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * JwtService - Service that logs employee logins with username LocalDateTime, userAgent and ipAddress
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Service
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public void logLogin(String username, String userAgent, String ipAddress) {
        LoginLog log = new LoginLog(username, LocalDateTime.now(), userAgent, ipAddress);
        loginLogRepository.save(log);
    }
}
