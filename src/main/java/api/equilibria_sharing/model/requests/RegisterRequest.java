package api.equilibria_sharing.model.requests;

public class RegisterRequest {
    private String username;
    private String password;
    private String uniqueCode;

    // Getter und Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUniqueCode() { return uniqueCode; }
    public void setUniqueCode(String uniqueCode) { this.uniqueCode = uniqueCode; }
}