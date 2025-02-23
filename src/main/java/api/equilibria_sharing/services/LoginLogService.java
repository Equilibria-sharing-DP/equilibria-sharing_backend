package api.equilibria_sharing.services;

import api.equilibria_sharing.model.LoginLog;
import api.equilibria_sharing.repositories.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public void logLogin(String username, String userAgent) {
        LoginLog log = new LoginLog(username, LocalDateTime.now(), userAgent);
        loginLogRepository.save(log);
    }
}

