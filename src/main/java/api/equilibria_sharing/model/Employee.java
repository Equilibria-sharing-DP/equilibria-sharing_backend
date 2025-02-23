package api.equilibria_sharing.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Roles> roles = new ArrayList<>();

    private String usedRegistrationCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public void addRole(Roles role) {
        this.roles.add(role);
    }

    public String getUsedRegistrationCode() {
        return usedRegistrationCode;
    }

    public void setUsedRegistrationCode(String usedRegistrationCode) {
        this.usedRegistrationCode = usedRegistrationCode;
    }
}
