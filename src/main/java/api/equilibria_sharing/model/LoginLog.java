package api.equilibria_sharing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * LoginLog Entity - for logging each employee login
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Entity
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private LocalDateTime loginTime;
    private String userAgent;
    private String ipAddress;

    public LoginLog() {}

    public LoginLog(String username, LocalDateTime loginTime, String userAgent, String ipAddress) {
        this.username = username;
        this.loginTime = loginTime;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

