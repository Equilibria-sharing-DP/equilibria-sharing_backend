package api.equilibria_sharing.model.requests;

import java.time.LocalDate;

/**
 * BookingRequest class - Just a placeholder class for GuestRequests
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
public class GuestRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


}
