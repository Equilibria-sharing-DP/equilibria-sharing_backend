package api.equilibria_sharing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private LocalDateTime loginTime;
    private String userAgent;

    public LoginLog() {}

    public LoginLog(String username, LocalDateTime loginTime, String userAgent) {
        this.username = username;
        this.loginTime = loginTime;
        this.userAgent = userAgent;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}

