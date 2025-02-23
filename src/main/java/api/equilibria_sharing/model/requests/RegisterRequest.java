package api.equilibria_sharing.model.requests;

/**
 * BookingRequest class - Just a placeholder class for Register Requests
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String uniqueCode;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUniqueCode() { return uniqueCode; }
    public void setUniqueCode(String uniqueCode) { this.uniqueCode = uniqueCode; }
}