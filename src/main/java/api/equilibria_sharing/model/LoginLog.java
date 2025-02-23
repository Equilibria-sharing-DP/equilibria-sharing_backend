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

    // Konstruktoren
    public LoginLog() {}

    public LoginLog(String username, LocalDateTime loginTime) {
        this.username = username;
        this.loginTime = loginTime;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
}
