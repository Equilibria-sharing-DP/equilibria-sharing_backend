package api.equilibria_sharing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String endpoint;
    private String httpMethod;
    private String userAgent;
    private LocalDateTime accessTime;

    public AccessLog() {}

    public AccessLog(String username, String endpoint, String httpMethod, String userAgent, LocalDateTime accessTime) {
        this.username = username;
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
        this.userAgent = userAgent;
        this.accessTime = accessTime;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public LocalDateTime getAccessTime() { return accessTime; }
    public void setAccessTime(LocalDateTime accessTime) { this.accessTime = accessTime; }
}
